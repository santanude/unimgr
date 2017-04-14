/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.impl;

import org.junit.Before;
import org.junit.Test;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.binding.test.AbstractConcurrentDataBrokerTest;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.api.Subrequrest;
import org.opendaylight.unimgr.mef.nrp.common.NrpDao;
import org.opendaylight.unimgr.mef.nrp.impl.decomposer.BasicDecomposer;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.ForwardingDirection;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.LayerProtocolName;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.OperationalState;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.ConnectivityServiceEndPoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.input.EndPointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.link.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.Link;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.LinkBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.topology.LinkKey;
import org.opendaylight.yangtools.yang.common.OperationFailedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.opendaylight.unimgr.mef.nrp.api.TapiConstants.PRESTO_SYSTEM_TOPO;

/**
 * @author bartosz.michalik@amartus.com
 */
public class BasicDecomposerTest extends AbstractConcurrentDataBrokerTest {
    private BasicDecomposer decomposer;
    private DataBroker dataBroker;

    @Before
    public void setUp() throws Exception {
        dataBroker = getDataBroker();
        decomposer = new BasicDecomposer(dataBroker);
        new NrpInitializer(dataBroker).init();
    }

    @Test
    public void singleNodeTest() throws OperationFailedException {
        //having
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();
        n(tx, "n1", "n1:1", "n1:2", "n1:3");
        n(tx, "n2", "n2:1", "n2:2", "n2:3");
        tx.submit().checkedGet();
        //when
        List<Subrequrest> decomposed = decomposer.decompose(Arrays.asList(ep("n1:1"), ep("n1:2")), null);

        assertEquals(1, decomposed.size());
    }

    @Test
    public void noPathTest() throws OperationFailedException {
        //having
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();
        n(tx, "n1", "n1:1", "n1:2", "n1:3");
        n(tx, "n2", "n2:1", "n2:2", "n2:3");
        tx.submit().checkedGet();
        //when
        List<Subrequrest> decomposed = decomposer.decompose(Arrays.asList(ep("n1:1"), ep("n2:2")), null);
        assertNull(decomposed);
    }

    @Test
    public void twoNodesTest() throws OperationFailedException {
        //having
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();
        n(tx, "n1", "n1:1", "n1:2", "n1:3");
        n(tx, "n2", "n2:1", "n2:2", "n2:3");
        n(tx, "n3", "n3:1", "n3:2", "n3:3");
        l(tx, "n1", "n1:1", "n2", "n2:1", OperationalState.Enabled);
        l(tx, "n2", "n2:3", "n3", "n3:3", OperationalState.Enabled);
        tx.submit().checkedGet();
        //when
        List<Subrequrest> decomposed = decomposer.decompose(Arrays.asList(ep("n1:2"), ep("n2:2")), null);
        assertNotNull(decomposed);
        assertEquals(2, decomposed.size());
    }

    @Test
    public void threeNodesTest() throws OperationFailedException {
        //having
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();
        n(tx, "n1", "n1:1", "n1:2", "n1:3");
        n(tx, "n2", "n2:1", "n2:2", "n2:3");
        n(tx, "n3", "n3:1", "n3:2", "n3:3");
        l(tx, "n1", "n1:1", "n2", "n2:1", OperationalState.Enabled);
        l(tx, "n2", "n2:3", "n3", "n3:3", OperationalState.Enabled);
        tx.submit().checkedGet();
        //when
        List<Subrequrest> decomposed = decomposer.decompose(Arrays.asList(ep("n1:2"), ep("n3:2")), null);
        assertNotNull(decomposed);
        assertEquals(3, decomposed.size());
    }

    @Test
    public void threeNodesDisabledLinkTest() throws OperationFailedException {
        //having
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();
        n(tx, "n1", "n1:1", "n1:2", "n1:3");
        n(tx, "n2", "n2:1", "n2:2", "n2:3");
        n(tx, "n3", "n3:1", "n3:2", "n3:3");
        l(tx, "n1", "n1:1", "n2", "n2:1", OperationalState.Disabled);
        l(tx, "n2", "n2:3", "n3", "n3:3", OperationalState.Enabled);
        tx.submit().checkedGet();
        //when
        List<Subrequrest> decomposed = decomposer.decompose(Arrays.asList(ep("n1:2"), ep("n3:2")), null);
        assertNull(decomposed);
    }

    private EndPoint ep(String nepId) {
        ConnectivityServiceEndPoint ep = new EndPointBuilder()
                .setLocalId("ep_" + nepId)
                .setServiceInterfacePoint(new UniversalId("sip_" + nepId))
                .build();

        return new EndPoint(ep, null);
    }
    private void l(ReadWriteTransaction tx, String nA, String nepA, String nB, String nepB, OperationalState state) {
        UniversalId uuid = new UniversalId(nepA + "-" + nepB);
        Link link = new LinkBuilder()
                .setUuid(uuid)
                .setKey(new LinkKey(uuid))
                .setDirection(ForwardingDirection.Bidirectional)
                .setLayerProtocolName(Collections.singletonList(LayerProtocolName.Eth))
                .setNode(toIds(nA, nB).collect(Collectors.toList()))
                .setNodeEdgePoint(toIds(nepA, nepB).collect(Collectors.toList()))
                .setState(new StateBuilder().setOperationalState(state).build())
                .build();

        tx.put(LogicalDatastoreType.OPERATIONAL, NrpDao.topo(PRESTO_SYSTEM_TOPO).child(Link.class, new LinkKey(uuid)), link);
    }

    private Stream<UniversalId> toIds(String ... uuids) {
        return toIds(Arrays.stream(uuids));
    }

    private Stream<UniversalId> toIds(Stream<String> uuids) {
        return uuids.map(UniversalId::new);
    }

    private void n(ReadWriteTransaction tx, String node, String ... endpoints) {
        NrpDao nrpDao = new NrpDao(tx);
        Arrays.stream(endpoints).map(e -> new ServiceInterfacePointBuilder()
                .setUuid(new UniversalId("sip_" + e))
                .build())
        .forEach(nrpDao::addSip);


        nrpDao.createSystemNode(node, Arrays.stream(endpoints)
                .map(e->
                        new OwnedNodeEdgePointBuilder()
                        .setUuid(new UniversalId(e))
                        .setMappedServiceInterfacePoint(Collections.singletonList(new UniversalId("sip_"+e)))
                        .build()
            ).collect(Collectors.toList()));
    }
}
