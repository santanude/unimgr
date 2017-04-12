/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.common;

import com.google.common.base.Optional;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.unimgr.mef.nrp.api.TapiConstants;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.Context;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePointKey;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.Context1;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.NodeEdgePoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointKey;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Node;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.NodeBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.NodeKey;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.Topology;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.TopologyKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author bartosz.michalik@amartus.com
 */
public class NrpDao  {
    private static final Logger log = LoggerFactory.getLogger(NrpDao.class);
    private final ReadWriteTransaction tx;

    public NrpDao(ReadWriteTransaction tx) {
        this.tx = tx;
    }

    private Function<NodeEdgePoint, OwnedNodeEdgePoint> toNep = nep -> new OwnedNodeEdgePointBuilder(nep).build();

    public void createSystemNode(String nodeId, List<OwnedNodeEdgePoint> neps) {

        UniversalId uuid = new UniversalId(nodeId);
        Node node = new NodeBuilder()
                .setKey(new NodeKey(uuid))
                .setUuid(uuid)
                .setOwnedNodeEdgePoint(neps)
                .build();
        tx.put(LogicalDatastoreType.OPERATIONAL, node(nodeId), node);
    }

    /**
     * Update nep or add if it does not exist
     * @param nodeId
     * @param nep
     */
    public void updateNep(String nodeId, OwnedNodeEdgePoint nep) {
        InstanceIdentifier<OwnedNodeEdgePoint> nodeIdent = node(nodeId).child(OwnedNodeEdgePoint.class, new OwnedNodeEdgePointKey(nep.getUuid()));
        tx.merge(LogicalDatastoreType.OPERATIONAL, nodeIdent, nep);
    }

    public void removeNep(String nodeId, String nepId, boolean removeSips) {
        InstanceIdentifier<OwnedNodeEdgePoint> nepIdent = node(nodeId).child(OwnedNodeEdgePoint.class, new OwnedNodeEdgePointKey(new UniversalId(nepId)));
        try {
            Optional<OwnedNodeEdgePoint> opt = tx.read(LogicalDatastoreType.OPERATIONAL, nepIdent).checkedGet();
            if(opt.isPresent()) {
                tx.delete(LogicalDatastoreType.OPERATIONAL,nepIdent);
                if(removeSips){
                    List<UniversalId> sips = opt.get().getMappedServiceInterfacePoint();
                    removeSips(sips == null ? null : sips.stream());
                }
            }
        } catch (ReadFailedException e) {
            log.error("Cannot read {} with id {}",OwnedNodeEdgePoint.class, nodeId);
        }
    }

    public void addSip(ServiceInterfacePoint sip) {
        tx.put(LogicalDatastoreType.OPERATIONAL,
        ctx().child(ServiceInterfacePoint.class, new ServiceInterfacePointKey(sip.getUuid())),
                sip);
    }

    public boolean hasSip(String nepID) {
        UniversalId universalId = new UniversalId("sip:" + nepID);
        try {
            return tx.read(LogicalDatastoreType.OPERATIONAL,
                    ctx().child(ServiceInterfacePoint.class, new ServiceInterfacePointKey(universalId))).checkedGet().isPresent();
        } catch (ReadFailedException e) {
            log.error("Cannot read sip with id {}", universalId.getValue());
        }
        return false;
    }

    public boolean hasNep(String nodeId, String nepId) throws ReadFailedException {
        KeyedInstanceIdentifier<OwnedNodeEdgePoint, OwnedNodeEdgePointKey> nepIdent = node(nodeId)
                .child(OwnedNodeEdgePoint.class, new OwnedNodeEdgePointKey(new UniversalId(nepId)));
        return tx.read(LogicalDatastoreType.OPERATIONAL, nepIdent).checkedGet().isPresent();
    }

    protected InstanceIdentifier<Context> ctx() {
        return InstanceIdentifier.create(Context.class);
    }

    protected InstanceIdentifier<Topology> topo(String topoId) {
        return ctx()
                .augmentation(Context1.class)
                .child(Topology.class, new TopologyKey(new UniversalId(topoId)));
    }

    protected InstanceIdentifier<Node> node(String nodeId) {
        return topo(TapiConstants.PRESTO_SYSTEM_TOPO).child(Node.class, new NodeKey(new UniversalId(nodeId)));
    }

    protected InstanceIdentifier<Node> abstractNode() {
        return topo(TapiConstants.PRESTO_EXT_TOPO).child(Node.class, new NodeKey(new UniversalId(TapiConstants.PRESTO_ABSTRACT_NODE)));
    }

    public void removeSips(Stream<UniversalId>  uuids) {
        if(uuids == null) return ;
        uuids.forEach(sip -> {
            log.debug("removing ServiceInterfacePoint with id {}", sip);
            tx.delete(LogicalDatastoreType.OPERATIONAL, ctx().child(ServiceInterfacePoint.class, new ServiceInterfacePointKey(sip)));
        });
    }

    public void removeNode(String nodeId, boolean removeSips) {
        if(removeSips) {
            try {
                Optional<Node> opt = tx.read(LogicalDatastoreType.OPERATIONAL, node(nodeId)).checkedGet();
                if(opt.isPresent()) {
                    removeSips(opt.get().getOwnedNodeEdgePoint().stream().flatMap(nep -> nep.getMappedServiceInterfacePoint() == null ?
                            Stream.empty() : nep.getMappedServiceInterfacePoint().stream()
                    ));
                }
            } catch (ReadFailedException e) {
                log.error("Cannot read node with id {}", nodeId);
            }
        }

        tx.delete(LogicalDatastoreType.OPERATIONAL, node(nodeId));
    }

    public void updateAbstractNep(OwnedNodeEdgePoint nep){
        InstanceIdentifier<OwnedNodeEdgePoint> nodeIdent = abstractNode().child(OwnedNodeEdgePoint.class, new OwnedNodeEdgePointKey(nep.getUuid()));
        tx.merge(LogicalDatastoreType.OPERATIONAL, nodeIdent, nep);
    }

    public void deleteAbstractNep(OwnedNodeEdgePoint nep){
        InstanceIdentifier<OwnedNodeEdgePoint> nodeIdent = abstractNode().child(OwnedNodeEdgePoint.class, new OwnedNodeEdgePointKey(nep.getUuid()));
        tx.delete(LogicalDatastoreType.OPERATIONAL, nodeIdent);
    }
}
