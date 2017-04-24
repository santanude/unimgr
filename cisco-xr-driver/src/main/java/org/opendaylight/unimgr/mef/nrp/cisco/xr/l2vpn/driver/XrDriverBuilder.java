package org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.driver;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.MountPointService;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriver;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriverBuilder;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.common.DriverConstants;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.activator.AbstractL2vpnActivator;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.activator.L2vpnBridgeActivator;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.activator.L2vpnXconnectActivator;
import org.opendaylight.unimgr.utils.SipHandler;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp_interface.rev170227.NrpCreateConnectivityServiceAttrs;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.forwarding.constructs.ForwardingConstruct;
import org.opendaylight.yang.gen.v1.urn.onf.core.network.module.rev160630.g_forwardingconstruct.FcPort;

import java.util.List;
import java.util.Optional;

/**
 * @author marek.ryznar@amartus.com
 */
public class XrDriverBuilder implements ActivationDriverBuilder {

    private DataBroker dataBroker;
    private MountPointService mountPointService;
    private AbstractL2vpnActivator activator;

    public XrDriverBuilder(DataBroker dataBroker, MountPointService mountPointService){
        this.dataBroker = dataBroker;
        this.mountPointService = mountPointService;
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
                if(isBridge(endPoints)){
                    activator = new L2vpnBridgeActivator(dataBroker,mountPointService);
                } else {
                    activator = new L2vpnXconnectActivator(dataBroker,mountPointService);
                }
            }

            @Override
            public void activate() throws TransactionCommitFailedException {
                activator.activate(endPoints,serviceId);

            }

            @Override
            public void deactivate() throws TransactionCommitFailedException {
                activator.deactivate(endPoints,serviceId);
            }

            @Override
            public int priority() {
                return 0;
            }

            private boolean isBridge(List<EndPoint> endPoints){
                return SipHandler.isTheSameDevice(endPoints.get(0).getEndpoint().getServiceInterfacePoint(),endPoints.get(1).getEndpoint().getServiceInterfacePoint());
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
