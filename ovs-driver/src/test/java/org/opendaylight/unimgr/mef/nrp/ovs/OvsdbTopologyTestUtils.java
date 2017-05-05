/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Uri;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ovsdb.rev150105.*;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TpId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.node.TerminationPoint;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.node.TerminationPointBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.node.TerminationPointKey;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author marek.ryznar@amartus.com
 */
public class OvsdbTopologyTestUtils {
    private static final TopologyId ovsdbTopologyId = new TopologyId(new Uri("ovsdb:1"));
    private static final InstanceIdentifier ovsdbTopology =
            InstanceIdentifier
                .create(NetworkTopology.class)
                .child(Topology.class, new TopologyKey(ovsdbTopologyId));

    /**
     * Ovsdb topology initializator.
     */
    public static void createOvsdbTopology(DataBroker dataBroker){
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setTopologyId(ovsdbTopologyId);
        Topology topology = topologyBuilder.build();
        DataStoreTestUtils.write(topology,ovsdbTopology, dataBroker);
    }

    public static void writeBridge(Node node, DataBroker dataBroker){
        InstanceIdentifier bridgeIid = getNodeInstanceIdentifier(node.getNodeId());
        DataStoreTestUtils.write(node,bridgeIid,dataBroker);
    }

    public static Node createBridge(String nodeId, List<TerminationPoint> tps){
        NodeBuilder nodeBuilder = new NodeBuilder();
        nodeBuilder.setNodeId(new NodeId(nodeId));
        nodeBuilder.setTerminationPoint(tps);
        nodeBuilder.addAugmentation(OvsdbBridgeAugmentation.class,createOvsdbBridgeAugmentation(nodeId));
        return nodeBuilder.build();
    }

    public static InstanceIdentifier getNodeInstanceIdentifier(NodeId nodeId){
        return InstanceIdentifier
                .builder(NetworkTopology.class)
                .child(Topology.class,
                        new TopologyKey(ovsdbTopologyId))
                .child(Node.class,
                        new NodeKey(nodeId))
                .build();
    }

    public static InstanceIdentifier getPortInstanceIdentifier(String nodeName, String portName){
        return getNodeInstanceIdentifier(new NodeId(nodeName))
                .child(TerminationPoint.class,
                        new TerminationPointKey(new TpId(portName)));
    }

    private static OvsdbBridgeAugmentation createOvsdbBridgeAugmentation(String ovsdbBridgeName){
        OvsdbBridgeAugmentationBuilder ovsdbBridgeAugmentationBuilder = new OvsdbBridgeAugmentationBuilder();
        ovsdbBridgeAugmentationBuilder.setBridgeName(new OvsdbBridgeName(ovsdbBridgeName));
        return ovsdbBridgeAugmentationBuilder.build();
    }

    public static TerminationPoint createTerminationPoint(String tpId, Long ofName) {
        TerminationPointBuilder terminationPointBuilder = new TerminationPointBuilder();
        terminationPointBuilder.setTpId(new TpId(tpId));
        terminationPointBuilder.setKey(new TerminationPointKey(new TpId(tpId)));
        terminationPointBuilder.addAugmentation(OvsdbTerminationPointAugmentation.class, createOvsdbTerminationPointAugmentation(ofName));
        TerminationPoint terminationPoint = terminationPointBuilder.build();
        return terminationPoint;
    }

    private static OvsdbTerminationPointAugmentation createOvsdbTerminationPointAugmentation(Long ofPort) {
        OvsdbTerminationPointAugmentationBuilder ovsdbTerminationPointAugmentationBuilder = new OvsdbTerminationPointAugmentationBuilder();
        ovsdbTerminationPointAugmentationBuilder.setOfport(ofPort);
        return ovsdbTerminationPointAugmentationBuilder.build();
    }

    public static Topology readOvsdbTopology(DataBroker dataBroker){
        return DataStoreTestUtils.read(ovsdbTopology,dataBroker);
    }
}