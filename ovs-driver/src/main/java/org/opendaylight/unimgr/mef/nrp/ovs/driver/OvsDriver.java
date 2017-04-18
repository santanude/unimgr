/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs.driver;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriver;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriverBuilder;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.common.ResourceNotAvailableException;
import org.opendaylight.unimgr.mef.nrp.ovs.activator.OvsActivator;
import org.opendaylight.unimgr.utils.CapabilitiesService;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp_interface.rev170227.NrpCreateConnectivityServiceAttrs;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.forwarding.constructs.ForwardingConstruct;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.g_forwardingconstruct.FcPort;

import java.util.List;
import java.util.Optional;

import static org.opendaylight.unimgr.utils.CapabilitiesService.Capability.Mode.AND;
import static org.opendaylight.unimgr.utils.CapabilitiesService.NodeContext.NodeCapability.OVSDB;

/**
 * @author marek.ryznar@amartus.com
 */
public class OvsDriver implements ActivationDriverBuilder {

    private OvsActivator activator;

    public OvsDriver(DataBroker dataBroker){
        activator = new OvsActivator(dataBroker);
    }


    private ActivationDriver getDriver() {
        return new ActivationDriver() {
            List<EndPoint> endPoints;

            @Override
            public void commit() {

            }

            @Override
            public void rollback() {

            }

            @Override
            public void initialize(List<EndPoint> endPoints, NrpCreateConnectivityServiceAttrs context) {
                this.endPoints = endPoints;
            }

            @Override
            public void activate() throws TransactionCommitFailedException, ResourceNotAvailableException {
                activator.activate(endPoints);
            }

            @Override
            public void deactivate() throws TransactionCommitFailedException, ResourceNotAvailableException {
                activator.deactivate(endPoints);
            }

            @Override
            public int priority() {
                return 0;
            }
        };
    }

    @Override
    public Optional<ActivationDriver> driverFor(BuilderContext context) {
        return Optional.ofNullable(getDriver());
    }

    @Override
    public UniversalId getNodeUuid() {
        return new UniversalId("ovs-node");
    }
}
