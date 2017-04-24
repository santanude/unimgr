package org.opendaylight.unimgr.mef.nrp.cisco.xr.common;

import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TpId;

/**
 * Class representing port in cisco xr devices.
 *
 * @author marek.ryznar@amartus.com
 */
public class XrPort {
    //netconf topology
    private TopologyId topoId;
    //represents device ie dev-68 in netconf topology
    private NodeId nodeId;
    //defines port
    private TpId tpId;

    public XrPort(TopologyId topoId, NodeId nodeId, TpId tpId){
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


}
