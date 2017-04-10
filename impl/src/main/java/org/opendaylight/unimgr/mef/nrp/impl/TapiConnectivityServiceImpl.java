/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.impl;

import org.opendaylight.unimgr.mef.nrp.api.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.ConnectivityService;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.context.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.output.ServiceBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author bartosz.michalik@amartus.com
 */
public class TapiConnectivityServiceImpl implements TapiConnectivityService, AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(TapiConnectivityServiceImpl.class);
    private ActivationDriverRepoService driverRepo;
    private RequestDecomposer decomposer;

    private ExecutorService executor = new ThreadPoolExecutor(4, 16,
            30, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>());

    public void init() {
        log.info("TapiConnectivityService initialized");
    }

    @Override
    public Future<RpcResult<CreateConnectivityServiceOutput>> createConnectivityService(CreateConnectivityServiceInput input) {
        return executor.submit(new CreateConnectivity(input));
    }


    @Override
    public Future<RpcResult<UpdateConnectivityServiceOutput>> updateConnectivityService(UpdateConnectivityServiceInput input) {
        return null;
    }

    @Override
    public Future<RpcResult<GetConnectionDetailsOutput>> getConnectionDetails(GetConnectionDetailsInput input) {
        return null;
    }

    @Override
    public Future<RpcResult<GetConnectivityServiceDetailsOutput>> getConnectivityServiceDetails(GetConnectivityServiceDetailsInput input) {
        return null;
    }

    @Override
    public Future<RpcResult<DeleteConnectivityServiceOutput>> deleteConnectivityService(DeleteConnectivityServiceInput input) {
        return null;
    }

    @Override
    public Future<RpcResult<GetConnectivityServiceListOutput>> getConnectivityServiceList() {
        return null;
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
    }

    class CreateConnectivity implements Callable<RpcResult<CreateConnectivityServiceOutput>> {

        private final CreateConnectivityServiceInput input;

        CreateConnectivity(CreateConnectivityServiceInput input) {
            this.input = input;
        }

        @Override
        public RpcResult<CreateConnectivityServiceOutput> call() throws Exception {
            log.debug("running CreateConnectivityService task");

            try {
                validateInput();
                ActivationTransaction tx = prepareTransaction();
                if(tx != null) {
                    ActivationTransaction.Result txResult = tx.activate();
                    if(txResult.isSuccessful()) {
                        log.info("ConnectivityService construct activated successfully, request = {} ", input);

                        ConnectivityService service = createConnectivityModel();
                        CreateConnectivityServiceOutput result = new CreateConnectivityServiceOutputBuilder()
                                .setService(new ServiceBuilder(service).build()).build();
                        return RpcResultBuilder.success(result).build();
                    } else {
                        log.warn("CreateConnectivityService failed, reason = {}, request = {}", txResult.getMessage(), input);
                    }
                }
                throw new IllegalStateException("no transaction created for create connectivity request");



            } catch(Exception e) {
                log.warn("Exception in create connectivity service", e);
                return RpcResultBuilder
                        .<CreateConnectivityServiceOutput>failed()
                        .build();
            }
        }

        private ActivationTransaction prepareTransaction() {
            log.debug("decompose request");
            List<Subrequrest> decomposed = decomposer.decompose(
                    input.getEndPoint().stream().map(ep -> new EndPoint(ep, null)).collect(Collectors.toList()),
                    null);

            ActivationTransaction tx = new ActivationTransaction();

            decomposed.stream().map(s -> {
                Optional<ActivationDriver> driver = driverRepo.getDriver(s.getUuid());
                if(!driver.isPresent()) {
                    throw new IllegalStateException(MessageFormat.format("driver {} cannot be created", s.getUuid()));
                }
                driver.get().initialize(s.getEndpoints(), null);
                log.debug("driver {} added to activation transaction", driver.get());
                return driver.get();
            }).forEach(tx::addDriver);

            return tx;
        }

        private void validateInput() {

        }

        private ConnectivityService createConnectivityModel() {
            //FIXME provide implementation
            return new ConnectivityServiceBuilder()
                    .build();
//            return null;
        }
    }


    public void setDriverRepo(ActivationDriverRepoService driverRepo) {
        this.driverRepo = driverRepo;
    }

    public void setDecomposer(RequestDecomposer decomposer) {
        this.decomposer = decomposer;
    }
}
