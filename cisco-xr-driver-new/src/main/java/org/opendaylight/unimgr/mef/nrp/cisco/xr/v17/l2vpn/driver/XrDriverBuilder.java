/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.l2vpn.driver;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.MountPointService;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriver;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriverBuilder;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.common.util.SipHandler;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.l2vpn.activator.AbstractL2vpnActivator;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.l2vpn.activator.AbstractL2vpnBridgeDomainActivator;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.l2vpn.activator.L2vpnBridgeDomainActivator;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.l2vpn.activator.L2vpnLocalConnectActivator;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.l2vpn.activator.L2vpnP2pConnectActivator;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.v17.l2vpn.helper.PseudowireHelper;
import org.opendaylight.unimgr.mef.nrp.common.ResourceActivatorException;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp._interface.rev180321.NrpConnectivityServiceAttrs;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author marek.ryznar@amartus.com
 */
public class XrDriverBuilder implements ActivationDriverBuilder {

    public static final String XR_NODE = "xr-node-new";

    private static final Logger LOG = LoggerFactory.getLogger(XrDriverBuilder.class);
    private DataBroker dataBroker;
    private MountPointService mountPointService;
    private L2vpnLocalConnectActivator localActivator;
    private L2vpnP2pConnectActivator p2pActivator;
    private L2vpnBridgeDomainActivator bdActivator;

    public XrDriverBuilder(DataBroker dataBroker, MountPointService mountPointService) {
        this.dataBroker = dataBroker;
        this.mountPointService = mountPointService;
    }

    protected ActivationDriver getDriver() {
        final ActivationDriver driver = new ActivationDriver() {
            List<Map.Entry<EndPoint,EndPoint>> bridgeActivatedPairs = null;
            List<EndPoint> endPoints;
            String serviceId;
            boolean isExclusive;
            String serviceType;
       
            @Override
            public void commit() {
                //ignore for the moment
            }

            @Override
            public void rollback() {
                //ignore for the moment
            }

            @Override
            public void initialize(List<EndPoint> endPoints, String serviceId, NrpConnectivityServiceAttrs context, boolean isExclusive, String serviceType) {
                this.endPoints = endPoints;
                this.serviceId = serviceId;
                this.isExclusive = isExclusive;
                this.serviceType = serviceType;

                if (serviceType != null && serviceType.equals(ServiceType.MULTIPOINTCONNECTIVITY.getName())) {
                    bdActivator = new L2vpnBridgeDomainActivator(dataBroker, mountPointService);
                } else {
                    localActivator = new L2vpnLocalConnectActivator(dataBroker, mountPointService);
                    p2pActivator = new L2vpnP2pConnectActivator(dataBroker, mountPointService);
                }
            }

            @Override
            public void activate() throws TransactionCommitFailedException {
                PseudowireHelper.generatePseudowireId();
                if (serviceType != null && serviceType.equals(ServiceType.MULTIPOINTCONNECTIVITY.getName())) {
                    handleBdEndpoints(activateBd);
                } else {
                    handleEndpoints(activate);
                }
            }

            @Override
            public void deactivate() throws TransactionCommitFailedException {

                if (serviceType != null && serviceType.equals(ServiceType.MULTIPOINTCONNECTIVITY.getName())) {
                    handleBdEndpoints(deactivateBd);
                } else {
                    handleEndpoints(deactivate);
                }
            }

            @Override
            public int priority() {
                return 0;
            }

            private void handleEndpoints(BiConsumer<List<EndPoint>,AbstractL2vpnActivator> action) {
                endPoints.forEach(endPoint -> connectWithAllNeighbors(action,endPoint,endPoints));
            }

            private void connectWithAllNeighbors(BiConsumer<List<EndPoint>,AbstractL2vpnActivator> action, EndPoint endPoint, List<EndPoint> neighbors) {
                neighbors.stream()
                        .filter(neighbor -> !neighbor.equals(endPoint))
                        .forEach(neighbor -> activateNeighbors(action,endPoint,neighbor));
            }

            private void activateNeighbors(BiConsumer<List<EndPoint>,AbstractL2vpnActivator> action, EndPoint portA, EndPoint portZ) {
                List<EndPoint> endPointsToActivate = Arrays.asList(portA,portZ);

                if (SipHandler.isTheSameDevice(portA.getEndpoint().getServiceInterfacePoint(),portZ.getEndpoint().getServiceInterfacePoint())) {
                    if (bridgeActivatedPairs==null) {
                        bridgeActivatedPairs = new ArrayList<>();
                    }
                    if (isPairActivated(portA,portZ)) {
                        return;
                    }
                    action.accept(endPointsToActivate, localActivator);
                    bridgeActivatedPairs.add(new AbstractMap.SimpleEntry<>(portA, portZ));
                } else {
                    action.accept(endPointsToActivate, p2pActivator);
                }
            }

            private boolean isPairActivated(EndPoint a, EndPoint z) {
                return bridgeActivatedPairs.stream()
                        .anyMatch( entry -> {
                            if ( (entry.getKey().equals(a) && entry.getValue().equals(z))
                                    || (entry.getKey().equals(z) && entry.getValue().equals(a))) {
                                return true;
                            }
                            return false;
                        });
            }

            BiConsumer<List<EndPoint>,AbstractL2vpnActivator> activate = (neighbors, activator) -> {
                try {
                    activator.activate(neighbors, serviceId, isExclusive, serviceType);
                } catch (TransactionCommitFailedException e) {
                    LOG.error("Activation error occured: {}",e.getMessage());
                }
            };

            BiConsumer<List<EndPoint>,AbstractL2vpnActivator> deactivate = (neighbors, activator) -> {
                        try {
                            activator.deactivate(neighbors, serviceId, isExclusive, serviceType);
                        } catch (TransactionCommitFailedException e) {
                            LOG.error("Deactivation error occured: {}", e.getMessage());
                        }
                    };

            // L2vpn Bridge Domain configuration implementation
            private void handleBdEndpoints(BiConsumer<List<EndPoint>,AbstractL2vpnBridgeDomainActivator> action) {
                endPoints.forEach(endPoint -> connectWithAllBdNeighbors(action,endPoint,endPoints));
            }
            
            private void connectWithAllBdNeighbors(BiConsumer<List<EndPoint>,AbstractL2vpnBridgeDomainActivator> action, EndPoint endPoint, List<EndPoint> neighbors) {
                neighbors.stream()
                        .filter(neighbor -> !neighbor.equals(endPoint))
                        .forEach(neighbor -> activateBdNeighbors(action,endPoint,neighbor));
            }
            
            private void activateBdNeighbors(BiConsumer<List<EndPoint>,AbstractL2vpnBridgeDomainActivator> action, EndPoint portA, EndPoint portZ) {
                List<EndPoint> endPointsToActivate = Arrays.asList(portA, portZ);

                action.accept(endPointsToActivate, bdActivator);
            }

            BiConsumer<List<EndPoint>,AbstractL2vpnBridgeDomainActivator> activateBd = (neighbors, activator) -> {
                        try {
                            activator.activate(neighbors, serviceId, isExclusive, serviceType);
                        } catch (TransactionCommitFailedException | ResourceActivatorException e) {
                            LOG.error("Activation error occured: {}", e.getMessage());
                        }
                    };

            BiConsumer<List<EndPoint>,AbstractL2vpnBridgeDomainActivator> deactivateBd = (neighbors, activator) -> {
                        try {
                            activator.deactivate(neighbors, serviceId, isExclusive, serviceType);
                        } catch (TransactionCommitFailedException | ResourceActivatorException e) {
                            LOG.error("Deactivation error occured: {}", e.getMessage());
                        }
                    };

        };

        return driver;
    }

    @Override
    public Optional<ActivationDriver> driverFor(BuilderContext context) {
        return Optional.of(getDriver());
    }

    @Override
    public String getActivationDriverId() {
        return XR_NODE;
    }
}
