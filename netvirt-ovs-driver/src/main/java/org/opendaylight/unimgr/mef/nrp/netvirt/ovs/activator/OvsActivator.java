/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.netvirt.ovs.activator;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.common.ResourceActivator;
import org.opendaylight.unimgr.mef.nrp.common.ResourceNotAvailableException;
import org.opendaylight.unimgr.mef.nrp.netvirt.ovs.util.NetvirtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//import org.opendaylight.yang.gen.v1.http.metroethernetforum.org.ns.yang.mef.interfaces.rev150526.mef.interfaces.unis.uni.VlanToPort;
//import org.opendaylight.yang.gen.v1.http.metroethernetforum.org.ns.yang.mef.interfaces.rev150526.mef.interfaces.unis.uni.VlanToPortBuilder;
//import org.opendaylight.yang.gen.v1.http.metroethernetforum.org.ns.yang.mef.types.rev150526.VlanIdOrNoneType;

/**
 * @author marek.ryznar@amartus.com
 */
public class OvsActivator implements ResourceActivator {

    private final DataBroker dataBroker;
    private String serviceName;
    private boolean isETree;
    private static final Logger LOG = LoggerFactory.getLogger(OvsActivator.class);

    public OvsActivator(DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    /**
     * Set state for the driver for a (de)activation transaction.
     * @param endPoints list of endpoint to interconnect
     */
    @Override
    public void activate(List<EndPoint> endPoints, String serviceName) throws ResourceNotAvailableException, TransactionCommitFailedException {
        this.serviceName = serviceName;
        //for now we hardcode that it is not ETree service
        //we can get it from nrp-cg-eth-conn-serv-spec { "connection-type": }
        isETree = false;
        createElanInstance(endPoints);
        //maybe commit transaction here
        for (EndPoint endPoint:endPoints)
            activateEndpoint(endPoint);
    }

    private void activateEndpoint(EndPoint endPoint) throws ResourceNotAvailableException, TransactionCommitFailedException {
        Long vlan = getVlan(endPoint);
        String interfaceName = getInterfaceNameForVlan(OvsActivatorHelper.getPortName(endPoint.getEndpoint().getServiceInterfacePoint().getValue()),vlan);

        NetvirtUtils.createElanInterface(dataBroker, serviceName, interfaceName, isETree);

        //here we can add QoS and ACL (get code from EVCListener)
    }

    @Override
    public void deactivate(List<EndPoint> endPoints, String serviceName) throws TransactionCommitFailedException, ResourceNotAvailableException {
        for (EndPoint endPoint:endPoints)
            deactivateEndpoint(endPoint);
    }

    private void deactivateEndpoint(EndPoint endPoint) throws ResourceNotAvailableException, TransactionCommitFailedException {

    }

    private void createElanInstance(List<EndPoint> endPoints){
        Long segmentationId = 1000L;
        Long macTimeout = 300000L;

        NetvirtUtils.createElanInstance(dataBroker, serviceName, isETree, segmentationId, macTimeout);
    }

    private Long getVlan(EndPoint endPoint){

        return endPoint.getAttrs().getNrpCgEthFrameFlowCpaAspec().getCeVlanIdList().getVlanIdList().get(0).getVlanId().getValue().longValue();
    }

    public static String getInterfaceNameForVlan(String interfaceName, Long vlan) {
        final StringBuilder s = new StringBuilder();
        s.append(interfaceName);
        if (vlan != null) {
            s.append(".").append(vlan);
        }
        s.append("-trunk");

        return java.util.UUID.nameUUIDFromBytes(s.toString().getBytes()).toString();
    }

}
