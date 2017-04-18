/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.unimgr.mef.nrp.api.*;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.common.NrpDao;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp_interface.rev170227.EndPoint2;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.ConnectivityService;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connection.ConnectionEndPoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connection.ConnectionEndPointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connection.RouteBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connection.end.point.LayerProtocolBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.context.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.context.Connection;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.service.*;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.create.connectivity.service.output.ServiceBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    private RequestValidator validator;
    private DataBroker broker;

    private ExecutorService executor = new ThreadPoolExecutor(4, 16,
            30, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>());

    final static  InstanceIdentifier<Context1> connectivityCtx = NrpDao.ctx().augmentation(Context1.class);

    public void init() {
        Objects.requireNonNull(driverRepo);
        Objects.requireNonNull(decomposer);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(broker);
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
        private List<Subrequrest> decomposedRequest;
        private List<EndPoint> endpoints;

        CreateConnectivity(CreateConnectivityServiceInput input) {
            this.input = input;
        }

        @Override
        public RpcResult<CreateConnectivityServiceOutput> call() throws Exception {
            log.debug("running CreateConnectivityService task");

            try {
                RequestValidator.ValidationResult validationResult = validateInput();
                if(!validationResult.isValid()) {
                    RpcResultBuilder<CreateConnectivityServiceOutput> res = RpcResultBuilder.failed();
                    validationResult.getProblems().forEach(p -> res.withError(RpcError.ErrorType.APPLICATION, p));
                    return res.build();

                }

                endpoints = input.getEndPoint().stream().map(ep -> {
                    EndPoint2 nrpAttributes = ep.getAugmentation(EndPoint2.class);
                    return new EndPoint(ep, nrpAttributes);
                }).collect(Collectors.toList());

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
            decomposedRequest = decomposer.decompose(endpoints, null);

            ActivationTransaction tx = new ActivationTransaction();

            decomposedRequest.stream().map(s -> {
                Optional<ActivationDriver> driver = driverRepo.getDriver(s.getNodeUuid());
                if(!driver.isPresent()) {
                    throw new IllegalStateException(MessageFormat.format("driver {} cannot be created", s.getNodeUuid()));
                }
                driver.get().initialize(s.getEndpoints(), null);
                log.debug("driver {} added to activation transaction", driver.get());
                return driver.get();
            }).forEach(tx::addDriver);

            return tx;
        }

        private RequestValidator.ValidationResult validateInput() {
            return validator.checkValid(input);

        }

        private ConnectivityService createConnectivityModel() {
            assert decomposedRequest != null : "this method can be only run after request was successfuly decomposed";
            //sort of unique ;)
            String uniqueStamp =  Long.toString(System.currentTimeMillis(), 16);
            log.debug("Preparing connectivity related model for {}", uniqueStamp);

            List<Connection> systemConnections = decomposedRequest.stream().map(s -> new ConnectionBuilder()
                    .setUuid(new UniversalId("conn:" + s.getNodeUuid().getValue() + ":" + uniqueStamp))
//                        .setState()
                    .setDirection(ForwardingDirection.Bidirectional)
                    .setLayerProtocolName(LayerProtocolName.Eth)
                    .setNode(s.getNodeUuid())
                    .setConnectionEndPoint(toConnectionPoints(s.getEndpoints(), uniqueStamp))
                    .build()).collect(Collectors.toList());

            Connection globalConnection = new ConnectionBuilder()
                    .setUuid(new UniversalId("conn:" + TapiConstants.PRESTO_ABSTRACT_NODE + ":" + uniqueStamp))
//                        .setState()
                    .setDirection(ForwardingDirection.Bidirectional)
                    .setLayerProtocolName(LayerProtocolName.Eth)
                    .setNode(new UniversalId(TapiConstants.PRESTO_ABSTRACT_NODE))
                    .setConnectionEndPoint(toConnectionPoints(endpoints, uniqueStamp))
                    .setRoute(Collections.singletonList(new RouteBuilder()
                            .setLocalId("route")
                            .setLowerConnection(systemConnections.stream().map(GlobalClass::getUuid).collect(Collectors.toList()))
                            .build())
                    ).build();


            ConnConstraint connConstraint = input.getConnConstraint() == null ? null : new ConnConstraintBuilder(input.getConnConstraint()).build();

            org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.context.ConnectivityService cs = new org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.context.ConnectivityServiceBuilder()
                    .setUuid(new UniversalId("cs:" + uniqueStamp))
//                    .setState()
                    .setConnConstraint(connConstraint)
                    .setConnection(Collections.singletonList(globalConnection.getUuid()))
                    .setEndPoint(toConnectionServiceEndpoints(endpoints, uniqueStamp))
                    .build();

            final WriteTransaction tx = broker.newWriteOnlyTransaction();
            systemConnections.forEach(c -> {
                tx.put(LogicalDatastoreType.OPERATIONAL, connectivityCtx.child(Connection.class, new ConnectionKey(c.getUuid())), c);
            });
            tx.put(LogicalDatastoreType.OPERATIONAL,
                    connectivityCtx.child(org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.context.ConnectivityService.class,
                            new ConnectivityServiceKey(cs.getUuid())), cs);

            tx.put(LogicalDatastoreType.OPERATIONAL, connectivityCtx.child(Connection.class, new ConnectionKey(globalConnection.getUuid())), globalConnection);

            log.debug("Storing connectivity related model for {} to operational data store", uniqueStamp);
            Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
                @Override
                public void onSuccess(@Nullable Void result) {
                    log.info("Success with serializing Connections and Connectivity Service for {}", uniqueStamp);
                }

                @Override
                public void onFailure(Throwable t) {
                    log.error("Error with serializing Connections and Connectivity Service for " + uniqueStamp, t);
                }
            });


            return new ServiceBuilder(cs).build();
        }

        private List<org.opendaylight.yang.gen.v1.urn.mef.yang.tapiconnectivity.rev170227.connectivity.service.EndPoint> toConnectionServiceEndpoints(List<EndPoint> endpoints, String uniqueStamp) {
            return endpoints.stream().map(ep -> new EndPointBuilder()
                            .setLocalId("sep:" +   ep.getSystemNepUuid()  + ":" + uniqueStamp)
                    .setServiceInterfacePoint(ep.getEndpoint().getServiceInterfacePoint())
                    .setDirection(PortDirection.Bidirectional)
                    .setLayerProtocolName(LayerProtocolName.Eth)
                    .setRole(PortRole.Symmetric)
                    .build()
            ).collect(Collectors.toList());
        }

        private List<ConnectionEndPoint> toConnectionPoints(List<EndPoint> endpoints, String uniqueStamp) {
            return endpoints.stream().map(ep -> new ConnectionEndPointBuilder()
                    .setUuid(new UniversalId("cep:" +   ep.getSystemNepUuid()  + ":" + uniqueStamp))
//                    .setState()
                    .setConnectionPortDirection(PortDirection.Bidirectional)
                    .setConnectionPortRole(PortRole.Symmetric)
                    .setServerNodeEdgePoint(ep.getSystemNepUuid())
                    .setLayerProtocol(Collections.singletonList(new LayerProtocolBuilder().setLayerProtocolName(LayerProtocolName.Eth).build()))
                    .setTerminationDirection(TerminationDirection.Bidirectional)
                    .build()
            ).collect(Collectors.toList());


        }
    }

    public void setValidator(RequestValidator validator) {
        this.validator = validator;
    }

    public void setDriverRepo(ActivationDriverRepoService driverRepo) {
        this.driverRepo = driverRepo;
    }

    public void setDecomposer(RequestDecomposer decomposer) {
        this.decomposer = decomposer;
    }

    public void setBroker(DataBroker broker) {
        this.broker = broker;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
