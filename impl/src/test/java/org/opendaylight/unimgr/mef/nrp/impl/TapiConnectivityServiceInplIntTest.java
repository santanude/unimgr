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
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriver;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriverRepoService;
import org.opendaylight.unimgr.mef.nrp.api.RequestValidator;
import org.opendaylight.unimgr.mef.nrp.impl.decomposer.BasicDecomposer;
import org.opendaylight.unimgr.utils.ActivationDriverMocks;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.PortRole;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.Context1;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.CreateConnectivityServiceInput;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.CreateConnectivityServiceInputBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.CreateConnectivityServiceOutput;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.context.Connection;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.input.EndPoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.input.EndPointBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author bartosz.michalik@amartus.com
 */
public class TapiConnectivityServiceInplIntTest extends AbstractTestWithTopo {

    private ActivationDriver ad1;
    private ActivationDriver ad2;

    private String uuid1 = "uuid1";
    private String uuid2 = "uuid2";
    private TapiConnectivityServiceImpl connectivityService;

    @Before
    public void setUp() throws Exception {
        BasicDecomposer decomposer = new BasicDecomposer(dataBroker);

        ad1 = mock(ActivationDriver.class);
        ad2 = mock(ActivationDriver.class);
        ActivationDriverRepoService repo = ActivationDriverMocks.builder()
                .add(new UniversalId(uuid1), ad1)
                .add(new UniversalId(uuid2), ad2)
                .build();

        RequestValidator validator = mock(RequestValidator.class);
        when(validator.checkValid(any())).thenReturn(new RequestValidator.ValidationResult());

        connectivityService = new TapiConnectivityServiceImpl();
        connectivityService.setDriverRepo(repo);
        connectivityService.setDecomposer(decomposer);
        connectivityService.setValidator(validator);
        connectivityService.setBroker(getDataBroker());
        connectivityService.init();
    }

    @Test
    public void testSingleDriver() throws Exception {
        //having
        CreateConnectivityServiceInput input = new CreateConnectivityServiceInputBuilder()
                .setEndPoint(eps(uuid1 + ":1", uuid1 + ":2"))
                .build();

        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();
        n(tx, uuid1, uuid1 + ":1", uuid1 + ":2", uuid1 + ":3");



        tx.submit().checkedGet();


        //when
        RpcResult<CreateConnectivityServiceOutput> result = this.connectivityService.createConnectivityService(input).get();
        //then
        assertTrue(result.isSuccessful());
        verify(ad1).activate();
        verify(ad1).commit();
        verifyZeroInteractions(ad2);
        verifyZeroInteractions(ad2);

        ReadOnlyTransaction tx2 = dataBroker.newReadOnlyTransaction();
        Context1 connCtx = tx2.read(LogicalDatastoreType.OPERATIONAL, TapiConnectivityServiceImpl.connectivityCtx).checkedGet().get();
        assertEquals(2, connCtx.getConnection().size());
        connCtx.getConnection().forEach(this::verifyConnection);

        assertEquals(1, connCtx.getConnectivityService().size());
        assertFalse(connCtx.getConnectivityService().get(0).getEndPoint().isEmpty());

    }

    private void verifyConnection(Connection connection) {
        assertFalse(connection.getConnectionEndPoint().isEmpty());
    }

    private List<EndPoint> eps(String ... uuids) {
        return Arrays.stream(uuids).map(uuid -> new EndPointBuilder()
                .setLocalId("e:" + uuid)
                .setRole(PortRole.Symmetric)
                .setServiceInterfacePoint(new UniversalId("sip:" + uuid))
                .build()).collect(Collectors.toList());
    }

}
