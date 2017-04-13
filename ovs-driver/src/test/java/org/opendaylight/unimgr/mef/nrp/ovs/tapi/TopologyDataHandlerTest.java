/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs.tapi;

import org.junit.Before;
import org.junit.Test;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.test.AbstractDataBrokerTest;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Node;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

/**
 * @author marek.ryznar@amartus.com
 */
public class TopologyDataHandlerTest extends AbstractDataBrokerTest{

    private TopologyDataHandler topologyDataHandler;
    private DataBroker dataBroker;
    private TopologyDataHandlerTestUtils helper;
    private static final String ovs_nep_prefix = "ovs-node:";
    private static final String sip_prefix = "sip:";
    private static final String bridgeName = "br1";
    private static final String expectedNep1 = "br1:br1-eth1";
    private static final String expectedNep2 = "br1:br1-eth2";

    @Before
    public void setUp(){
        //given
        dataBroker = getDataBroker();
        helper = new TopologyDataHandlerTestUtils(dataBroker);

        helper.createOvsdbTopology();
        helper.createOpenFlowNodes();
        helper.createFlowTopology();
        helper.createPrestoSystemTopology();

        topologyDataHandler = new TopologyDataHandler(dataBroker);
        topologyDataHandler.init();
    }

    @Test
    public void testBridgeAddition(){
        //when
        helper.createTestBridge();

        //then
        Node ovsNode = helper.readOvsNode();
        assertNotNull(ovsNode);
        checkNeps(ovsNode,expectedNep1,expectedNep2);
        checkSips(helper.readSips(),expectedNep1,expectedNep2);
    }

    @Test
    public void testPortAddition(){
        //given
        String newPortName = "br1-eth4";
        Long ofPortNumber = 4L;
        helper.createTestBridge();
        Node ovsNode = helper.readOvsNode();
        assertEquals(2,ovsNode.getOwnedNodeEdgePoint().size());

        //when
        helper.addPort(bridgeName,newPortName,ofPortNumber);

        //then
        ovsNode = helper.readOvsNode();
        assertEquals(3,ovsNode.getOwnedNodeEdgePoint().size());
        checkNeps(ovsNode,"br1:"+newPortName,expectedNep1,expectedNep2);
        checkSips(helper.readSips(),"br1:"+newPortName,expectedNep1,expectedNep2);
    }

    @Test
    public void testBridgeRemoval(){
        //given
        helper.createTestBridge();
        Node ovsNode = helper.readOvsNode();
        assertEquals(2,ovsNode.getOwnedNodeEdgePoint().size());

        //when
        helper.deleteTestBridge();

        //then
        ovsNode = helper.readOvsNode();
        assertEquals(0,ovsNode.getOwnedNodeEdgePoint().size());
    }

    @Test
    public void testPortRemoval(){
        //given
        String portNameToRemove = "br1-eth2";
        String fullPortNameToRemove = "br1:"+portNameToRemove;
        helper.createTestBridge();
        Node ovsNode = helper.readOvsNode();
        assertEquals(2,ovsNode.getOwnedNodeEdgePoint().size());

        //when
        helper.deletePort(portNameToRemove);

        //then
        ovsNode = helper.readOvsNode();
        assertEquals(1,ovsNode.getOwnedNodeEdgePoint().size());
        assertFalse(checkNep.apply(ovsNode,fullPortNameToRemove));
        assertFalse(checkSip.apply(helper.readSips(), fullPortNameToRemove));
    }

    private BiFunction<Node, String, Boolean> checkNep = (node,nepName) ->
            node.getOwnedNodeEdgePoint().stream()
                    .anyMatch(ownedNep ->
                        ownedNep.getMappedServiceInterfacePoint().contains(new UniversalId(sip_prefix + ovs_nep_prefix + nepName))
                                && ownedNep.getUuid().getValue().equals(ovs_nep_prefix + nepName)
                    );

    private void checkNeps(Node node,String ... neps){
        Arrays.stream(neps)
                .forEach(nep -> assertTrue(checkNep.apply(node,nep)));
    }

    private BiFunction<List<org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePoint>, String, Boolean> checkSip =
            (sips, nep) -> sips.stream()
                .anyMatch(sip -> sip.getUuid().getValue().equals(sip_prefix + ovs_nep_prefix + nep));

    private void checkSips(List<org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePoint> sips, String ... neps){
        Arrays.stream(neps)
                .forEach(nep -> assertTrue(checkSip.apply(sips,nep)));
    }

}
