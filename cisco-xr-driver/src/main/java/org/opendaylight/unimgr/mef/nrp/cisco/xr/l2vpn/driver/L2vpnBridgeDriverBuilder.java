/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.driver;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.MountPointService;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriver;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriverBuilder;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.common.DriverConstants;
import org.opendaylight.unimgr.utils.CapabilitiesService;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.activator.L2vpnBridgeActivator;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp_interface.rev170227.NrpCreateConnectivityServiceAttrs;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.forwarding.constructs.ForwardingConstruct;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.g_forwardingconstruct.FcPort;

import java.util.List;
import java.util.Optional;

import static org.opendaylight.unimgr.utils.CapabilitiesService.Capability.Mode.AND;
import static org.opendaylight.unimgr.utils.CapabilitiesService.NodeContext.NodeCapability.*;

/**
 * Provides drivers for binding two ports on the same node.
 * @author bartosz.michalik@amartus.com
 */
public class L2vpnBridgeDriverBuilder implements ActivationDriverBuilder {

    private final DataBroker dataBroker;

    private L2vpnBridgeActivator activator;

    private static final String GROUP_NAME = "local";

    public L2vpnBridgeDriverBuilder(DataBroker dataBroker, MountPointService mountPointService) {
        this.dataBroker = dataBroker;
        activator = new L2vpnBridgeActivator(dataBroker, mountPointService);
    }

    protected ActivationDriver getDriver() {
        final ActivationDriver driver = new ActivationDriver() {
            public FcPort aEnd;
            public FcPort zEnd;

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
                //FIXME implement new API
                this.zEnd = null;
                this.aEnd = null;
            }

            @Override
            public void activate() throws TransactionCommitFailedException {
                long mtu = 1500;

                String aEndNodeName = aEnd.getNode().getValue();
                activator.activate(aEndNodeName, GROUP_NAME, GROUP_NAME, aEnd, zEnd, mtu);
            }

            @Override
            public void deactivate() throws TransactionCommitFailedException {
                long mtu = 1500;

                String aEndNodeName = aEnd.getNode().getValue();
                activator.deactivate(aEndNodeName, GROUP_NAME, GROUP_NAME, aEnd, zEnd, mtu);
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
        return Optional.of(getDriver());
    }

    @Override
    public UniversalId getNodeUuid() {
        return new UniversalId(DriverConstants.XR_NODE);
    }
}
