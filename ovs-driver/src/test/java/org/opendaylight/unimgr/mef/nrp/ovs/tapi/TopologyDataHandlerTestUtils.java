/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs.tapi;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.unimgr.mef.nrp.impl.NrpInitializer;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Uri;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.Context;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.Context1;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNodeConnectorBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.port.rev130925.PortNumberUni;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ovsdb.rev150105.*;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.*;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.link.attributes.DestinationBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.link.attributes.SourceBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.*;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.node.TerminationPoint;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.node.TerminationPointBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.node.TerminationPointKey;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author marek.ryznar@amartus.com
 */
public class TopologyDataHandlerTestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TopologyDataHandlerTestUtils.class);
    private static final TopologyId ovsdbTopologyId = new TopologyId(new Uri("ovsdb:1"));
    private static final TopologyId flowTopologyId = new TopologyId(new Uri("flow:1"));
    private static final InstanceIdentifier<Topology> OVSDB_TOPO_IID = getTopologyIid(ovsdbTopologyId);
    private static final InstanceIdentifier<Topology> FLOW_TOPO_IID = getTopologyIid(flowTopologyId);
    private static String bridgeName = "br1";
    private static String bridgeId = "ovsdb:bridge/"+bridgeName;
    private static String ofBridgeName = "openflow:1";
    private static String tp1Name = "br1-eth1";
    private static String tp2Name = "br1-eth2";
    private static String tp3Name = "br1-eth3";
    private static Long tp1OFport = 1L;
    private static Long tp2OFport = 2L;
    private static Long tp3OFport = 3L;

    private static final String prestoNrpTopoId = "mef:presto-nrp-topology-system";
    private static final String ovsNodeId = "ovs-node";

    private final DataBroker dataBroker;

    protected TopologyDataHandlerTestUtils(DataBroker dataBroker){
        this.dataBroker = dataBroker;
    }
    /**
     * Creates ovsdb bridge "ovsdb:bridge/br1" with 3 ports:
     *  1. tp1 - nep
     *  2. tp2 - nep
     *  3. tp3 - not nep, becouse it is connected to other switch defined in {@link #createFlowTopology()}
     */
    protected void createTestBridge(){
        Node node = prepareTestNode(bridgeId);
        InstanceIdentifier instanceIdentifier = nodeInstanceIdentifier(node.getNodeId());

        write(node,instanceIdentifier);
    }

    protected void deleteTestBridge(){
        InstanceIdentifier instanceIdentifier = nodeInstanceIdentifier(new NodeId(bridgeId));
        delete(instanceIdentifier);
    }

    protected void deletePort(String port){
        InstanceIdentifier<TerminationPoint> tpIid = portInstanceIdentifier(bridgeId,port);
        delete(tpIid);
    }

    protected void addPort(String bridgeName, String portName, Long ofNumber){
        String bridgeId = "ovsdb:bridge/"+bridgeName;
        //openflow init
        NodeConnector nodeConnector = createNodeConnector(ofBridgeName,ofNumber,portName);
        InstanceIdentifier<NodeConnector> nodeConnectorInstanceIdentifier =
                nodeConnectorInstanceIdentifier(ofBridgeName,ofBridgeName+":"+ofNumber.toString());
        write(nodeConnector,nodeConnectorInstanceIdentifier);
        //ovsdb init
        TerminationPoint terminationPoint = buildTerminationPoint(portName,ofNumber);
        InstanceIdentifier<TerminationPoint> tpIid = portInstanceIdentifier(bridgeId,portName);
        write(terminationPoint,tpIid);

    }

    private InstanceIdentifier<NodeConnector> nodeConnectorInstanceIdentifier(String ofBridgeName, String nodeConnectorId){
        return InstanceIdentifier.builder(Nodes.class)
                .child(org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node.class,
                        new org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey(
                                new org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId(ofBridgeName)))
                .child(NodeConnector.class, new NodeConnectorKey(new NodeConnectorId(nodeConnectorId)))
                .build();
    }

    private static InstanceIdentifier nodeInstanceIdentifier(NodeId nodeId){
        return InstanceIdentifier
                .builder(NetworkTopology.class)
                .child(Topology.class,
                        new TopologyKey(ovsdbTopologyId))
                .child(org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node.class,
                        new NodeKey(nodeId))
                .build();
    }

    private InstanceIdentifier portInstanceIdentifier(String nodeName, String portName){
        return nodeInstanceIdentifier(new NodeId(nodeName))
                .child(TerminationPoint.class,
                new TerminationPointKey(new TpId(portName)));
    }

    protected Node prepareTestNode(String nodeId){
        List<TerminationPoint> tps = new LinkedList<>();

        tps.add(buildTerminationPoint(tp1Name,tp1OFport));
        tps.add(buildTerminationPoint(tp2Name,tp2OFport));
        tps.add(buildTerminationPoint(tp3Name,tp3OFport));

        NodeBuilder nodeBuilder = new NodeBuilder();
        nodeBuilder.setNodeId(new NodeId(nodeId));
        nodeBuilder.setTerminationPoint(tps);
        nodeBuilder.addAugmentation(OvsdbBridgeAugmentation.class,createOvsdbBridgeAugmentation(bridgeName));
        return nodeBuilder.build();
    }

    private OvsdbTerminationPointAugmentation createOvsdbTerminationPointAugmentation(Long ofPort){
        OvsdbTerminationPointAugmentationBuilder ovsdbTerminationPointAugmentationBuilder = new OvsdbTerminationPointAugmentationBuilder();
        ovsdbTerminationPointAugmentationBuilder.setOfport(ofPort);
        return ovsdbTerminationPointAugmentationBuilder.build();
    }

    private OvsdbBridgeAugmentation createOvsdbBridgeAugmentation(String ovsdbBridgeName){
        OvsdbBridgeAugmentationBuilder ovsdbBridgeAugmentationBuilder = new OvsdbBridgeAugmentationBuilder();
        ovsdbBridgeAugmentationBuilder.setBridgeName(new OvsdbBridgeName(ovsdbBridgeName));
        return ovsdbBridgeAugmentationBuilder.build();
    }

    private TerminationPoint buildTerminationPoint(String tpId, Long ofName){
        TerminationPointBuilder terminationPointBuilder = new TerminationPointBuilder();
        terminationPointBuilder.setTpId(new TpId(tpId));
        terminationPointBuilder.setKey(new TerminationPointKey(new TpId(tpId)));
        terminationPointBuilder.addAugmentation(OvsdbTerminationPointAugmentation.class,createOvsdbTerminationPointAugmentation(ofName));
        TerminationPoint terminationPoint = terminationPointBuilder.build();
        return terminationPoint;
    }

    /**
     * Creates OpenFlow Nodes with one Node ("openflow:1" openflow equivalent of ovsdb's "br1"), which consist of 3 NodeConnectors:
     *  1. id:"openflow:1:1", name: "br1-eth1", portNumber: "1"
     *  2. id:"openflow:1:2", name: "br1-eth2", portNumber: "2"
     *  3. id:"openflow:1:3", name: "br1-eth3", portNumber: "3"
     */
    protected void createOpenFlowNodes(){
        NodesBuilder nodesBuilder = new NodesBuilder();
        List<org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node> nodeList = new ArrayList<>();
        nodeList.add(createOpenFlowNode(ofBridgeName));
        nodesBuilder.setNode(nodeList);
        Nodes nodes = nodesBuilder.build();
        InstanceIdentifier<Nodes> nodesIId = InstanceIdentifier.builder(Nodes.class).build();

        ReadWriteTransaction transaction = dataBroker.newReadWriteTransaction();
        transaction.put(LogicalDatastoreType.OPERATIONAL,nodesIId,nodes);
        transaction.submit();
    }

    private org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node createOpenFlowNode(String oFName){
        org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeBuilder nodeBuilder =
                new org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeBuilder();
        org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId nodeId =
                new org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId(oFName);
        nodeBuilder.setId(nodeId);
        nodeBuilder.setKey(new org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey(nodeId));
        List<NodeConnector> nodeConnectorList = new ArrayList<>();
        nodeConnectorList.add(createNodeConnector(oFName,tp1OFport,tp1Name));
        nodeConnectorList.add(createNodeConnector(oFName,tp2OFport,tp2Name));
        nodeConnectorList.add(createNodeConnector(oFName,tp3OFport,tp3Name));
        nodeBuilder.setNodeConnector(nodeConnectorList);
        return nodeBuilder.build();
    }

    private NodeConnector createNodeConnector(String ofBridgeName, Long portNumber, String ovsdbPortName){
        NodeConnectorBuilder nodeConnectorBuilder = new NodeConnectorBuilder();
        String ofPortName = ofBridgeName + ":" + portNumber.toString();
        NodeConnectorId nodeConnectorId = new NodeConnectorId(ofPortName);
        nodeConnectorBuilder.setId(nodeConnectorId);
        nodeConnectorBuilder.setKey(new NodeConnectorKey(nodeConnectorId));
        nodeConnectorBuilder.addAugmentation(FlowCapableNodeConnector.class,createFlowCapableNodeConnector(ovsdbPortName,portNumber));
        return nodeConnectorBuilder.build();
    }

    private FlowCapableNodeConnector createFlowCapableNodeConnector(String ovsdbName, Long portNumber){
        FlowCapableNodeConnectorBuilder flowCapableNodeConnectorBuilder = new FlowCapableNodeConnectorBuilder();
        flowCapableNodeConnectorBuilder.setName(ovsdbName);
        flowCapableNodeConnectorBuilder.setPortNumber(new PortNumberUni(portNumber));
        return flowCapableNodeConnectorBuilder.build();
    }

    /**
     * Creates flow topology with link nodes (Links between ovs).
     * Links between openflow:1:3 and openflow:2:1 was defined here (bridge openflow:2 was defined only here for test purpose).
     */
    protected void createFlowTopology(){
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setTopologyId(flowTopologyId);
        topologyBuilder.setLink(getLinkList());
        Topology topology = topologyBuilder.build();
        write(topology,FLOW_TOPO_IID);
    }

    private List<Link> getLinkList(){
        List<Link> linkList = new ArrayList<>();

        //For testing purposes only - can't be find anywhere else in DataStore
        String of2NodeName = "openflow:2";
        Long of2PortNumber = 1L;

        //openflow:1:3 -> <- openflow:2:1
        linkList.add(createLink(ofBridgeName,tp3OFport,of2NodeName,of2PortNumber));
        linkList.add(createLink(of2NodeName,of2PortNumber,ofBridgeName,tp3OFport));

        return linkList;
    }

    private Link createLink(String sourceNode, Long sourcePort, String destNode, Long destPort){
        LinkBuilder linkBuilder = new LinkBuilder();
        String sourcePortName = sourceNode + ":" + sourcePort.toString();
        String destPortName = destNode + ":" + destPort;

        linkBuilder.setLinkId(new LinkId(sourcePortName));

        SourceBuilder sourceBuilder = new SourceBuilder();
        sourceBuilder.setSourceTp(new TpId(sourcePortName));
        sourceBuilder.setSourceNode(new NodeId(sourceNode));
        linkBuilder.setSource(sourceBuilder.build());

        DestinationBuilder destinationBuilder = new DestinationBuilder();
        destinationBuilder.setDestTp(new TpId(destPortName));
        destinationBuilder.setDestNode(new NodeId(destNode));
        linkBuilder.setDestination(destinationBuilder.build());

        return linkBuilder.build();
    }

    /**
     * Ovsdb topology initializator.
     */
    protected void createOvsdbTopology(){
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setTopologyId(ovsdbTopologyId);
        Topology topology = topologyBuilder.build();
        write(topology,OVSDB_TOPO_IID);
    }

    private static InstanceIdentifier<Topology> getTopologyIid(TopologyId topologyId){
        return InstanceIdentifier
                .create(NetworkTopology.class)
                .child(Topology.class, new TopologyKey(topologyId));
    }

    private <T extends DataObject> void write(T object, InstanceIdentifier<T> instanceIdentifier){
        ReadWriteTransaction transaction = dataBroker.newReadWriteTransaction();
        transaction.put(LogicalDatastoreType.OPERATIONAL,instanceIdentifier,object,true);

        Futures.addCallback(transaction.submit(), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void result) {
                LOG.debug("Object: {} created.",object.toString());
            }

            @Override
            public void onFailure(Throwable t) {
                LOG.debug("Object: {} wasn't created due to a error: {}",object.toString(), t.getMessage());
            }
        });
    }

    private void delete(InstanceIdentifier instanceIdentifier){
        ReadWriteTransaction transaction = dataBroker.newReadWriteTransaction();
        transaction.delete(LogicalDatastoreType.OPERATIONAL, instanceIdentifier);

        Futures.addCallback(transaction.submit(), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void result) {
                LOG.debug("Object: {} deleted.",instanceIdentifier.toString());
            }

            @Override
            public void onFailure(Throwable t) {
                LOG.debug("Object: {} wasn't deleted due to a error: {}",instanceIdentifier.toString(), t.getMessage());
            }
        });
    }

    protected void createPrestoSystemTopology(){
        NrpInitializer nrpInitializer = new NrpInitializer(dataBroker);
        try {
            nrpInitializer.init();
        } catch (Exception e) {
            fail("Could not initialize NRP topology.");
        }
    }

    protected org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Node readOvsNode(){
        ReadWriteTransaction transaction = dataBroker.newReadWriteTransaction();
        try {
            Optional<org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Node> optNode
                    = (Optional<org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Node>) transaction.read(LogicalDatastoreType.OPERATIONAL,getNodeIid()).checkedGet();
            if(optNode.isPresent()){
                return optNode.get();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return null;
    }

    protected List<org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePoint> readSips(){
        ReadWriteTransaction readWriteTransaction = dataBroker.newReadWriteTransaction();
        try {
            Optional<Context> opt = readWriteTransaction.read(LogicalDatastoreType.OPERATIONAL, InstanceIdentifier.create(Context.class)).checkedGet();
            if(opt.isPresent()){
                return opt.get().getServiceInterfacePoint();
            } else {
                fail("There are no sips.");
            }
        } catch (ReadFailedException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private static InstanceIdentifier getNodeIid(){
        return getTopoIid()
                .child(org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Node.class,
                        new org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.NodeKey(new UniversalId(ovsNodeId)));

    }

    private static InstanceIdentifier getTopoIid(){
        return InstanceIdentifier.create(Context.class)
                .augmentation(Context1.class)
                .child(org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.Topology.class,
                        new org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.context.TopologyKey(new UniversalId(prestoNrpTopoId)));
    }
}