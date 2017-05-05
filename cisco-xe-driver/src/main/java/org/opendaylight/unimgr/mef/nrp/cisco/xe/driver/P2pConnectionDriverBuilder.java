/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.cisco.xe.driver;


import java.util.List;
import java.util.Optional;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriver;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriverBuilder;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.cisco.xe.activator.P2pConnectionActivator;
import org.opendaylight.unimgr.mef.nrp.common.ResourceActivatorException;
import org.opendaylight.unimgr.utils.DriverConstants;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp_interface.rev170227.NrpCreateConnectivityServiceAttrs;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.forwarding.constructs.ForwardingConstruct;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.g_forwardingconstruct.FcPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P2pConnectionDriverBuilder implements ActivationDriverBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(P2pConnectionDriverBuilder.class);

    private static final String GROUP_NAME = "local";
    private static final String XE_TOPOLOGY_ID = "topology-cisco-xe";
    private P2pConnectionActivator activator;

    public P2pConnectionDriverBuilder(DataBroker dataBroker) {
        activator = new P2pConnectionActivator(dataBroker);
    }

    protected ActivationDriver getDriver() {
        final ActivationDriver driver = new ActivationDriver() {
            List<EndPoint> endPoints;
            String serviceId;

            @Override
            public void commit() {
                //ignore for the moment
            }

            @Override
            public void rollback() {
                //ignore for the moment
            }

            @Override
            public void initialize(List<EndPoint> endPoints, String serviceId, NrpCreateConnectivityServiceAttrs context) {
                this.endPoints = endPoints;
                this.serviceId = serviceId;
            }


            @Override
            public void activate() throws TransactionCommitFailedException, ResourceActivatorException {
                activator.activate(endPoints,serviceId);
            }

            @Override
            public void deactivate() throws TransactionCommitFailedException, ResourceActivatorException {
                activator.deactivate(endPoints,serviceId);
            }

            @Override
            public int priority() {
                return 0;
            }
        };

        return driver;
    }

    @Override
    public Optional<ActivationDriver> driverFor(BuilderContext context) {
        return  Optional.of(getDriver());
    }

    @Override
    public UniversalId getNodeUuid() {
        return new UniversalId(DriverConstants.XE_NODE);
    }
}
