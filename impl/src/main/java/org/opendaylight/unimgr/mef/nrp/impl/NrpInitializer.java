/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.impl;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.common.NrpDao;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointKey;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.NodeBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Node;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.NodeKey;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.TopologyBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.TopologyKey;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.opendaylight.unimgr.mef.nrp.api.TapiConstants.*;

/**
 * @author bartosz.michalik@amartus.com
 */
public class NrpInitializer {
    private static final Logger log = LoggerFactory.getLogger(NrpInitializer.class);
    private final DataBroker dataBroker;


    public NrpInitializer(DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    public void init() throws Exception {
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();
        InstanceIdentifier<Context> ctxId = InstanceIdentifier.create(Context.class);
        CheckedFuture<? extends Optional<? extends DataObject>, ReadFailedException> result = tx.read(LogicalDatastoreType.OPERATIONAL, ctxId);

        Optional<? extends DataObject> context = result.checkedGet();

        if(! context.isPresent()) {
            log.info("initialize Presto NRP context");
            Context ctx = new ContextBuilder()
                    .setUuid(new UniversalId(PRESTO_CTX))
                    .addAugmentation(org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.Context1.class, context())
                    .build();
            tx.put(LogicalDatastoreType.OPERATIONAL, ctxId, ctx);
            try {
                tx.submit().checkedGet();
                log.debug("Presto context model created");
            } catch (TransactionCommitFailedException e) {
                log.error("Failed to create presto context model");
                throw new IllegalStateException("cannot create presto context", e);
            }
        }
    }

    private void testInsertNode(ReadWriteTransaction tx) {
        NrpDao nrpDao = new NrpDao(tx);
        nrpDao.createSystemNode("xrNode", IntStream.range(1,5).mapToObj(i -> {
            UniversalId uuid = new UniversalId("nep" + i);
            return new OwnedNodeEdgePointBuilder()
                    .setKey(new OwnedNodeEdgePointKey(uuid))
                    .setUuid(uuid)
                    .setTerminationDirection(TerminationDirection.Bidirectional)
                    .build();
        }).collect(Collectors.toList()));

    }

    private org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.Context1 context() {
        return new org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.Context1Builder()
                .setTopology(Arrays.asList(systemTopo(), extTopo()))
                .build();
    }

    private org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.Topology extTopo() {
        UniversalId topoId = new UniversalId(PRESTO_EXT_TOPO);
        log.debug("Adding {}", PRESTO_EXT_TOPO);
        return new TopologyBuilder()
                .setLayerProtocolName(Collections.singletonList(LayerProtocolName.Eth))
                .setUuid(topoId)
                .setKey(new TopologyKey(topoId))
                .setNode(Collections.singletonList(node("mef:presto-nrp-abstract-node")))
                .build();
    }

    private Node node(String uuid) {
        UniversalId uid = new UniversalId(uuid);
        return new NodeBuilder()
                .setLayerProtocolName(Collections.singletonList(LayerProtocolName.Eth))
                .setEncapTopology(new UniversalId(PRESTO_SYSTEM_TOPO))
                .setKey(new NodeKey(uid))
//                .setState()
                .setUuid(uid)
                .build();
    }

    private org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.Topology systemTopo() {
        UniversalId topoId = new UniversalId(PRESTO_SYSTEM_TOPO);
        log.debug("Adding {}", PRESTO_SYSTEM_TOPO);
        return new TopologyBuilder()
                .setLayerProtocolName(Collections.singletonList(LayerProtocolName.Eth))
                .setUuid(topoId)
                .setKey(new TopologyKey(topoId))
                .build();
    }


    public void close() {

    }
}
