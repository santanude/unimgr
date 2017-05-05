/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.common;

import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.utils.SipHandler;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp_interface.rev170227.nrp.create.connectivity.service.end.point.attrs.NrpCgEthFrameFlowCpaAspec;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TpId;

/**
 * Class representing port (replacement for FcPort)
 *
 * @author marek.ryznar@amartus.com
 */
public class ServicePort {
    //netconf topology
    private TopologyId topoId;
    //represents device ie dev-68 in netconf topology
    private NodeId nodeId;
    //defines port
    private TpId tpId;
    //defines cTag VLAN ID
    private Long vlanId=null;

    public ServicePort(TopologyId topoId, NodeId nodeId, TpId tpId){
        this.topoId = topoId;
        this.nodeId = nodeId;
        this.tpId = tpId;
    }

    public TopologyId getTopology() {
        return topoId;
    }

    public void setTopology(TopologyId topoId) {
        this.topoId = topoId;
    }

    public NodeId getNode() {
        return nodeId;
    }

    public void setNode(NodeId nodeId) {
        this.nodeId = nodeId;
    }

    public TpId getTp() {
        return tpId;
    }

    public void setTp(TpId tpId) {
        this.tpId = tpId;
    }

    public Long getVlanId() {
        return vlanId;
    }

    public void setVlanId(Long vlanId) {
        this.vlanId = vlanId;
    }

    public static ServicePort toServicePort(EndPoint endPoint, String topologyName){
        UniversalId sip = endPoint.getEndpoint().getServiceInterfacePoint();
        TopologyId topologyId = new TopologyId(topologyName);
        NodeId nodeId = new NodeId(SipHandler.getDeviceName(sip));
        TpId tpId = new TpId(SipHandler.getPortName(sip));
        ServicePort servicePort = new ServicePort(topologyId,nodeId,tpId);
        if(hasVlan(endPoint)){
            servicePort.setVlanId(Long.valueOf(getVlan(endPoint)));
        }
        return servicePort;
    }

    public static boolean hasVlan(EndPoint endPoint){
        if( (endPoint.getAttrs() != null) && (endPoint.getAttrs().getNrpCgEthFrameFlowCpaAspec()!=null) ){
            NrpCgEthFrameFlowCpaAspec attr = endPoint.getAttrs().getNrpCgEthFrameFlowCpaAspec();
            if( (attr.getCeVlanIdList()!=null) && !(attr.getCeVlanIdList().getVlanIdList().isEmpty()) ){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static int getVlan(EndPoint endPoint){
        return endPoint.getAttrs().getNrpCgEthFrameFlowCpaAspec().getCeVlanIdList().getVlanIdList().get(0).getVlanId().getValue().intValue();
    }
}
