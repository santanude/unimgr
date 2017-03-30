/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.*;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.unimgr.mef.nrp.common.NrpDao;
import org.opendaylight.unimgr.mef.nrp.common.ResourceNotAvailableException;
import org.opendaylight.unimgr.mef.nrp.ovs.transaction.TopologyTransaction;
import org.opendaylight.unimgr.utils.CapabilitiesService;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Uri;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.LifecycleState;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.TerminationDirection;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePointKey;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.service._interface.point.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ovsdb.rev150105.InterfaceTypeInternal;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ovsdb.rev150105.OvsdbBridgeAugmentation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ovsdb.rev150105.OvsdbTerminationPointAugmentation;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Link;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.node.TerminationPoint;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.opendaylight.unimgr.utils.CapabilitiesService.Capability.Mode.AND;
import static org.opendaylight.unimgr.utils.CapabilitiesService.NodeContext.NodeCapability.*;

/**
 * @author bartosz.michalik@amartus.com
 */
public class TopologyDataHandler implements DataTreeChangeListener<Node> {
    private static final Logger LOG = LoggerFactory.getLogger(TopologyDataHandler.class);
    private static final String OVS_NODE = "ovs-node";
    private static final String DELIMETER = ":";
    private static final InstanceIdentifier<Topology> OVSDB_TOPO_IID = InstanceIdentifier
            .create(NetworkTopology.class)
            .child(Topology.class, new TopologyKey(new TopologyId(new Uri("ovsdb:1"))));
    private CapabilitiesService capabilitiesService;
    private ListenerRegistration<TopologyDataHandler> registration;
    private TopologyTransaction topologyTransaction;

    private final DataBroker dataBroker;

    public TopologyDataHandler(DataBroker dataBroker) {
        Objects.requireNonNull(dataBroker);
        this.dataBroker = dataBroker;
        topologyTransaction = new TopologyTransaction(dataBroker);
    }

    public void init() {
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();

        NrpDao dao = new NrpDao(tx);
        dao.createSystemNode(OVS_NODE, null);

        Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void result) {
                LOG.info("Node {} created", OVS_NODE);
            }

            @Override
            public void onFailure(Throwable t) {
                LOG.error("No node created due to the error", t);
            }
        });

        capabilitiesService = new CapabilitiesService(dataBroker);

        registerOvsdbTreeListener();
    }

    private void registerOvsdbTreeListener(){
        InstanceIdentifier<Node> nodeId = OVSDB_TOPO_IID.child(Node.class);
        registration = dataBroker.registerDataTreeChangeListener(new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL, nodeId), this);
    }

    public void close() {
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();

        NrpDao dao = new NrpDao(tx);
        dao.removeNode(OVS_NODE, true);

        Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void result) {
                LOG.info("Node {} created", OVS_NODE);
            }

            @Override
            public void onFailure(Throwable t) {
                LOG.error("No node created due to the error", t);
            }
        });

        if(registration != null) {
            LOG.info("closing netconf tree listener");
            registration.close();
        }
    }

    private void addEndpoint(NrpDao dao, String nepName) {
        ServiceInterfacePoint sip = createSip(nepName);

        dao.addSip(sip);
        dao.updateNep(OVS_NODE, nep(nepName, sip.getUuid()));
    }

    private OwnedNodeEdgePoint nep(String nepName, UniversalId sipUuid) {
        UniversalId uuid = new UniversalId(OVS_NODE + DELIMETER + nepName);
        return new OwnedNodeEdgePointBuilder()
                .setUuid(uuid)
                .setKey(new OwnedNodeEdgePointKey(uuid))
                .setMappedServiceInterfacePoint(Collections.singletonList(sipUuid))
                .build();
    }

    private ServiceInterfacePoint createSip(String nep) {
        UniversalId uuid = new UniversalId( OVS_NODE + DELIMETER + "sip" + DELIMETER + nep);
        return new ServiceInterfacePointBuilder()
                .setUuid(uuid)
                .setKey(new ServiceInterfacePointKey(uuid))
                .setState(new StateBuilder().setLifecycleState(LifecycleState.Installed).build())
                .setDirection(TerminationDirection.Bidirectional)
                .build();
    }

    Function<DataObjectModification<Node>, Node> addedNode = mod -> mod.getModificationType() == DataObjectModification.ModificationType.WRITE ?
            mod.getDataAfter() : null;

    @Override
    public void onDataTreeChanged(@Nonnull Collection<DataTreeModification<Node>> collection) {
        List<Node> addedNodes = collection.stream().map(DataTreeModification::getRootNode)
                .map(addedNode::apply)
                .filter(n -> {
                    if (n == null) return false;
                    return capabilitiesService.node(n).isSupporting(AND, OVSDB);
                }).collect(Collectors.toList());

        try {
            onAddedNodes(addedNodes);
        } catch(Exception e) {
            //TODO improve error handling
            LOG.error("error while processing new Cisco nodes", e);
        }
    }

    private void onAddedNodes(@Nonnull Collection<Node> added) throws ReadFailedException {
        if(added.isEmpty()) return;

        final ReadWriteTransaction topoTx = dataBroker.newReadWriteTransaction();
        NrpDao dao = new NrpDao(topoTx);
        toTp(added).forEach(nep -> addEndpoint(dao,nep.getKey().getUuid().getValue()));

        Futures.addCallback(topoTx.submit(), new FutureCallback<Void>() {

            @Override
            public void onSuccess(@Nullable Void result) {
                LOG.info("TAPI node upadate successful");
            }

            @Override
            public void onFailure(Throwable t) {
                LOG.warn("TAPI node upadate failed due to an error", t);
            }
        });
    }

    private List<OwnedNodeEdgePoint> toTp(Collection<Node> nodes) {
        OwnedNodeEdgePointBuilder tpBuilder = new OwnedNodeEdgePointBuilder();

        return nodes.stream()
                .flatMap(node -> {
                    List<OwnedNodeEdgePoint> tps;
                    tps = node.getTerminationPoint().stream()
                        .filter(this::isNep)
                        .map(tp -> {
                            String bridgeName = node.getAugmentation(OvsdbBridgeAugmentation.class).getBridgeName().getValue();
                            UniversalId tpId = new UniversalId(bridgeName + DELIMETER + tp.getTpId().getValue());
                            return tpBuilder
                                .setUuid(tpId)
                                .setKey(new OwnedNodeEdgePointKey(tpId))
                                .build();
                        })
                        .collect(Collectors.toList());
                    return tps.stream();
                })
                .collect(Collectors.toList());
    }

    private boolean isNep(TerminationPoint terminationPoint){
        OvsdbTerminationPointAugmentation ovsdbTerminationPoint = terminationPoint.getAugmentation(OvsdbTerminationPointAugmentation.class);
        if( ovsdbTerminationPoint==null || (ovsdbTerminationPoint.getInterfaceType()!=null && ovsdbTerminationPoint.getInterfaceType().equals(InterfaceTypeInternal.class))) {
            return false;
        }

        if( ovsdbTerminationPoint.getOfport() == null )
            return false;

        String ofPortNumber = ovsdbTerminationPoint.getOfport().toString();
        try {
            org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node node = topologyTransaction.readNode(terminationPoint.getTpId().getValue());
            String ofPortName = node.getId().getValue()+":"+ofPortNumber;
            List<Link> links = topologyTransaction.readLinks(node);
            return !links.stream()
                    .anyMatch(link -> link.getSource().getSourceTp().getValue().equals(ofPortName));
        } catch (ResourceNotAvailableException e) {
            LOG.warn(e.getMessage());
        }
        return false;
    }
}
