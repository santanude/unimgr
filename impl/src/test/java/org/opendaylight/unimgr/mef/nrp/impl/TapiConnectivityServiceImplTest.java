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
import org.opendaylight.unimgr.mef.nrp.api.*;
import org.opendaylight.unimgr.utils.ActivationDriverMocks;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.PortRole;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.ConnectivityServiceEndPoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.CreateConnectivityServiceInput;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.CreateConnectivityServiceInputBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.CreateConnectivityServiceOutput;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.input.EndPoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.input.EndPointBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bartosz.michalik@amartus.com
 */
public class TapiConnectivityServiceImplTest {


    private ActivationDriver ad1;
    private ActivationDriver ad2;
    private ActivationDriver ad3;


    private UniversalId uuid1 = new UniversalId("uuid1");
    private UniversalId uuid2 = new UniversalId("uuid2");
    private UniversalId uuid3 = new UniversalId("uuid3");
    private TapiConnectivityServiceImpl connectivityService;
    private RequestDecomposer decomposer;

    @Before
    public void setUp() {
        ad1 = mock(ActivationDriver.class);
        ad2 = mock(ActivationDriver.class);
        ad3 = mock(ActivationDriver.class);
        ActivationDriverRepoService repo = ActivationDriverMocks.builder()
                .add(uuid1, ad1)
                .add(uuid2, ad2)
                .add(uuid3, ad3)
                .build();

        decomposer = mock(RequestDecomposer.class);

        connectivityService = new TapiConnectivityServiceImpl();
        connectivityService.setDriverRepo(repo);
        connectivityService.setDecomposer(decomposer);
    }


    @Test
    public void emptyInputTest() throws Exception {
        //having
        CreateConnectivityServiceInput empty = new CreateConnectivityServiceInputBuilder()
                .build();
        //when
        RpcResult<CreateConnectivityServiceOutput> result = this.connectivityService.createConnectivityService(empty).get();
        //then
        assertFalse(result.isSuccessful());
    }

    @Test
    public void sucessfullTwoDrivers() throws ExecutionException, InterruptedException {
        //having
        CreateConnectivityServiceInput input = input(5);

        when(decomposer.decompose(any(), any(Constraints.class)))
        .thenAnswer(a -> {
            List<org.opendaylight.unimgr.mef.nrp.api.EndPoint> eps = a.getArgumentAt(0, List.class);

            Subrequrest s1 = new Subrequrest(uuid1, Arrays.asList(eps.get(0), eps.get(1), eps.get(2)));
            Subrequrest s3 = new Subrequrest(uuid3, Arrays.asList(eps.get(3), eps.get(4)));

            return Arrays.asList(s1, s3);
        });

        //when
        RpcResult<CreateConnectivityServiceOutput> result = this.connectivityService.createConnectivityService(input).get();
        //then
        assertTrue(result.isSuccessful());

    }

    private CreateConnectivityServiceInput input(int count) {

        List<EndPoint> eps = IntStream.range(0, count).mapToObj(x -> ep("ep" + x)).collect(Collectors.toList());

        return new CreateConnectivityServiceInputBuilder()
                .setEndPoint(eps)
                .build();
    }

    private org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.input.EndPoint ep(String id) {
        return new EndPointBuilder()
                .setLocalId(id)
                .setRole(PortRole.Symmetric)
//                .setServiceInterfacePoint()
        .build();
    }

}