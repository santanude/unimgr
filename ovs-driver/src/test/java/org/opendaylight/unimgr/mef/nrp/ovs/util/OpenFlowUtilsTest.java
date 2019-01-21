/*
 * Copyright (c) 2018 Xoriant Corporation and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.Table;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Link;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author OmS.awasthi@Xoriant.Com*
 */

@SuppressWarnings("deprecation")
@RunWith(PowerMockRunner.class)
@PrepareForTest({OpenFlowUtils.class, EtreeUtils.class})
public class OpenFlowUtilsTest {

    private List<Flow> flows;
    private String servicePort;
    private int internalVlanId;
    private String serviceName;
    private Optional<Integer> externalVlanId;
    private List<Link> interswitchLinks;
    @Mock
    private Flow flow;
    private long queueNumber;
    private String role;
    private long rootCount;
    private boolean isExclusive = true;
    private int vlanID;
    @Mock
    private EtreeUtils eTreeUtils;
    @Mock
    private Link link;
    public static final String CREATEVLANPASSINGGLOW = "createVlanPassingFlows";
    public static final String CREATEVLANINGRESSFLOW = "createVlanIngressFlow";
    public static final String GETVLANFLOWS = "getVlanFlows";
    public static final String CREATEPASSINGFLOWS = "createPassingFlows";
    public static final String CREATEINGRESSFLOW = "createIngressFlow";
    public static final String GETFLOWS = "getFlows";
    public static final String GETTABLE = "getTable";
    public static final String GETEVPTREE = "getEvpTree";
    public static final String GENERATEPREPOPVLAN = "generatePrePopVlan";
    public static final String DEFAULTFLOWS = "defaultFlows";
    public static final String UNTAGGEDFLOWS = "unTaggedFlows";
    public static final String GETEPTREE = "getEpTree";

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        flows = new ArrayList<>();
        interswitchLinks = new ArrayList<Link>();
        servicePort = "3";
        internalVlanId = 301;
        externalVlanId = null;
        serviceName = null;
        queueNumber = 0;
        interswitchLinks.add(link);
        role = "ROOT";
        rootCount = 2;
        vlanID = 301;
        PowerMockito.mockStatic(OpenFlowUtils.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void getVlanFlowsTest() throws Exception {

        MemberModifier.suppress(MemberMatcher.method(OpenFlowUtils.class, CREATEVLANPASSINGGLOW));
        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, CREATEVLANPASSINGGLOW, servicePort,
                internalVlanId, externalVlanId, serviceName, interswitchLinks);
        PowerMockito.doReturn(flow).when(OpenFlowUtils.class, CREATEVLANINGRESSFLOW, servicePort,
                internalVlanId, externalVlanId, serviceName, interswitchLinks, queueNumber);
        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, GETVLANFLOWS, servicePort,
                internalVlanId, externalVlanId, interswitchLinks, serviceName, queueNumber);
        List<Flow> actualFlows = OpenFlowUtils.getVlanFlows(servicePort, internalVlanId,
                externalVlanId, interswitchLinks, serviceName, queueNumber);
        assertThat(actualFlows, is(flows));
    }

    @Test
    public void getFlowsTest() throws Exception {

        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, CREATEPASSINGFLOWS, servicePort,
                serviceName, interswitchLinks);
        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, CREATEINGRESSFLOW, servicePort,
                serviceName, interswitchLinks);
        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, GETFLOWS, servicePort,
                interswitchLinks, serviceName, queueNumber);
        List<Flow> actualFlows =
                OpenFlowUtils.getFlows(servicePort, interswitchLinks, serviceName, queueNumber);
        assertThat(actualFlows, is(flows));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getTableTest() throws Exception {

        Node node = PowerMockito.mock(Node.class);
        String valueId = "1";
        NodeId nodeId = new NodeId(valueId);
        when(node.getId()).thenReturn(nodeId);

        FlowCapableNode flowCapableNode = PowerMockito.mock(FlowCapableNode.class);
        when(node.getAugmentation(FlowCapableNode.class)).thenReturn(flowCapableNode);
        List<Table> listTable = new ArrayList<Table>();
        Table table = PowerMockito.mock(Table.class);
        Optional<Table> optTable = PowerMockito.mock(Optional.class);
        when(flowCapableNode.getTable()).thenReturn(listTable);
        Stream<Table> streem = PowerMockito.mock(Stream.class);
        when(streem.findFirst()).thenReturn(optTable);
        when(optTable.isPresent()).thenReturn(true);
        when(optTable.get()).thenReturn(table);
        PowerMockito.doReturn(table).when(OpenFlowUtils.class, GETTABLE, node);

        Table actualTable = OpenFlowUtils.getTable(node);
        assertThat(actualTable, is(table));

    }

    @Test
    public void getEvpTreeTest() throws Exception {

        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, CREATEVLANPASSINGGLOW, servicePort,
                internalVlanId, externalVlanId, serviceName, interswitchLinks);
        when(eTreeUtils.getVlanID(serviceName)).thenReturn(vlanID);

        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, CREATEVLANPASSINGGLOW, servicePort,
                vlanID, externalVlanId, serviceName, interswitchLinks);
        PowerMockito.doReturn(flow).when(OpenFlowUtils.class, CREATEVLANINGRESSFLOW, servicePort,
                vlanID, externalVlanId, serviceName, interswitchLinks, queueNumber);

        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, GETEVPTREE, role, servicePort,
                internalVlanId, externalVlanId, interswitchLinks, serviceName, queueNumber,
                rootCount, eTreeUtils);

        List<Flow> actualFlows = OpenFlowUtils.getEvpTree(role, servicePort, internalVlanId,
                externalVlanId, interswitchLinks, serviceName, queueNumber, rootCount, eTreeUtils);
        assertThat(actualFlows, is(flows));

    }

    @Test
    public void getEpTreeTest() throws Exception {

        Set<Integer> set = new TreeSet<Integer>();
        PowerMockito.mockStatic(EtreeUtils.class);
        PowerMockito.when(EtreeUtils.cpeVlanRange()).thenReturn(set);
        PowerMockito.doNothing().when(EtreeUtils.class, GENERATEPREPOPVLAN);
        PowerMockito.when(EtreeUtils.speVlanRange(vlanID)).thenReturn(set);
        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, DEFAULTFLOWS, role, flows,
                servicePort, internalVlanId, externalVlanId, interswitchLinks, serviceName,
                queueNumber, rootCount, eTreeUtils, isExclusive);
        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, UNTAGGEDFLOWS, role, flows,
                servicePort, internalVlanId, interswitchLinks, serviceName, queueNumber, rootCount,
                eTreeUtils, isExclusive);
        PowerMockito.doReturn(flows).when(OpenFlowUtils.class, GETEPTREE, role, servicePort,
                internalVlanId, externalVlanId, interswitchLinks, serviceName, queueNumber,
                rootCount, eTreeUtils, isExclusive);
        List<Flow> actualFlows = OpenFlowUtils.getEpTree(role, servicePort, internalVlanId,
                externalVlanId, interswitchLinks, serviceName, queueNumber, rootCount, eTreeUtils,
                isExclusive);
        assertThat(actualFlows, is(flows));
    }

}
