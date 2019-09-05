/*
 * Copyright (c) 2019 Xoriant Corporation and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.activator;

import static org.opendaylight.unimgr.mef.nrp.cisco.xr.common.helper.BandwidthProfileComposition.BwpApplicability.UNI;
import static org.opendaylight.unimgr.mef.nrp.cisco.xr.common.helper.BandwidthProfileComposition.BwpDirection.EGRESS;
import static org.opendaylight.unimgr.mef.nrp.cisco.xr.common.helper.BandwidthProfileComposition.BwpDirection.INGRESS;
import java.util.List;
import java.util.Optional;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.MountPointService;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.common.ServicePort;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.common.helper.BandwidthProfileHelper;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.common.util.LoopbackUtils;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.helper.BridgeDomainAttachmentCircuitHelper;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.helper.BridgeDomainHelper;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.helper.BridgeDomainPseudowireHelper;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.helper.L2vpnHelper;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.ifmgr.cfg.rev150730.InterfaceConfigurations;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.ifmgr.cfg.rev150730._interface.configurations.InterfaceConfiguration;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.infra.policymgr.cfg.rev161215.PolicyManager;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.l2vpn.cfg.rev151109.L2vpn;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.l2vpn.cfg.rev151109.l2vpn.database.BridgeDomainGroups;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.l2vpn.cfg.rev151109.l2vpn.database.bridge.domain.groups.BridgeDomainGroup;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.l2vpn.cfg.rev151109.l2vpn.database.bridge.domain.groups.bridge.domain.group.bridge.domains.BridgeDomain;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.l2vpn.cfg.rev151109.l2vpn.database.bridge.domain.groups.bridge.domain.group.bridge.domains.bridge.domain.BdAttachmentCircuits;
import org.opendaylight.yang.gen.v1.http.cisco.com.ns.yang.cisco.ios.xr.l2vpn.cfg.rev151109.l2vpn.database.bridge.domain.groups.bridge.domain.group.bridge.domains.bridge.domain.BdPseudowires;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.Uuid;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arif.hussain@xoriant.com
 */
public class L2vpnBridgeDomainActivator extends AbstractL2vpnBridgeDomainActivator {

    private static final Logger LOG = LoggerFactory.getLogger(L2vpnBridgeDomainActivator.class);

    public L2vpnBridgeDomainActivator(DataBroker dataBroker, MountPointService mountService) {
        super(dataBroker, mountService);
    }

    @Override
    public L2vpn activateL2Vpn(BridgeDomainGroups bridgeDomainGroups) {
        return L2vpnHelper.build(bridgeDomainGroups);
    }

    @Override
    public BridgeDomainGroups activateBridgeDomain(String outerName, String innerName,
            ServicePort port, ServicePort neighbor, BdPseudowires bdPseudowires,
            boolean isExclusive) {

        BdAttachmentCircuits bdattachmentCircuits =
                new BridgeDomainAttachmentCircuitHelper().addPort(port, isExclusive).build();
        BridgeDomainGroup bridgeDomainGroup = new BridgeDomainHelper()
                .appendBridgeDomain(innerName, bdattachmentCircuits, bdPseudowires)
                .build(outerName);

        return BridgeDomainHelper.createBridgeDomainGroups(bridgeDomainGroup);
    }

    @Override
    public BdPseudowires activateBdPseudowire(ServicePort neighbor) {

        return new BridgeDomainPseudowireHelper()
                .addBdPseudowire(LoopbackUtils.getIpv4Address(neighbor, dataBroker)).build();
    }

    @Override
    protected String getInnerName(String serviceId) {
        return replaceForbidenCharacters(serviceId);
    }

    @Override
    protected String getOuterName(String serviceId) {
        return replaceForbidenCharacters(serviceId);
    }

    /**
     * ASR 9000 can't accept colon in bridgeDomain group name, so it have to be replaced with
     * underscore. If any other restriction will be found, this is a good place to change serviceId
     * name.
     *
     * @param serviceId old service id
     * @return new service id
     */
    private String replaceForbidenCharacters(String serviceId) {
        return serviceId.replace(":", "_");
    }

    @Override
    protected InterfaceConfigurations activateInterface(ServicePort port, ServicePort neighbor,
            long mtu, boolean isExclusive) {

        return new InterfaceActivator().activateInterface(port, neighbor, mtu, isExclusive);
    }

    @Override
    public InterfaceConfigurations createSubInterface(ServicePort port, ServicePort neighbor,
            long mtu) {

        return new InterfaceActivator().createSubInterface(port, neighbor, mtu);
    }

    @Override
    protected void doActivate(String node, InterfaceConfigurations interfaceConfigurations,
            L2vpn l2vpn, MountPointService mountService2, Optional<PolicyManager> qosConfig) throws TransactionCommitFailedException {

        new TransactionActivator().activate(node, interfaceConfigurations, l2vpn, mountService2, qosConfig);
    }

    @Override
    protected void createSubInterface(String nodeName,
            InterfaceConfigurations subInterfaceConfigurations, MountPointService mountService2)
            throws TransactionCommitFailedException {

        new TransactionActivator().createSubInterface(nodeName, subInterfaceConfigurations, mountService2);
    }

    @Override
    protected Optional<PolicyManager> activateQos(String name, ServicePort port) {
        return new BandwidthProfileHelper(port)
                .addPolicyMap(name, INGRESS, UNI)
                .addPolicyMap(name, EGRESS, UNI)
                .build();
    }

    @Override
    protected InstanceIdentifier<InterfaceConfiguration> deactivateInterface(ServicePort port,
            boolean isExclusive) {
        return new InterfaceActivator().deactivateInterface(port, isExclusive);
    }

    @Override
    protected void doDeactivate(ServicePort port, InstanceIdentifier<BridgeDomain> bridgeDomainId,
            InstanceIdentifier<InterfaceConfiguration> interfaceConfigurationId,
            boolean isExclusive, EndPoint endPoint, List<String> dvls2, List<Uuid> inls2)
            throws TransactionCommitFailedException {

        new TransactionActivator().doDeactivate(port, bridgeDomainId, interfaceConfigurationId, isExclusive, endPoint, mountService, dvls2, inls2);
    }

}
