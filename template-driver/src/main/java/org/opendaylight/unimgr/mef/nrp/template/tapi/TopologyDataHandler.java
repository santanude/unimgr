/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.template.tapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.TopologyManager;
import org.opendaylight.unimgr.mef.nrp.common.NrpDao;
import org.opendaylight.unimgr.mef.nrp.common.TapiUtils;
import org.opendaylight.unimgr.mef.nrp.template.TemplateConstants;
import org.opendaylight.yang.gen.v1.urn.mef.yang.mef.common.types.rev171221.NaturalNumber;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp._interface.rev171221.ServiceInterfacePoint1;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp._interface.rev171221.ServiceInterfacePoint1Builder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp._interface.rev171221.nrp.sip.attrs.NrpCarrierEthEnniNResourceBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp._interface.rev171221.nrp.sip.attrs.NrpCarrierEthInniNResourceBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp._interface.rev171221.nrp.sip.attrs.NrpCarrierEthUniNResourceBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.common.rev171113.*;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.common.rev171113.context.attrs.ServiceInterfacePoint;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.common.rev171113.context.attrs.ServiceInterfacePointBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.link.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.node.OwnedNodeEdgePoint;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.node.OwnedNodeEdgePointBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.topology.Link;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.topology.LinkBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.topology.LinkKey;
import org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.topology.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.opendaylight.unimgr.mef.nrp.api.TapiConstants.PRESTO_SYSTEM_TOPO;

/**
 * @author bartosz.michalik@amartus.com
 */
public class TopologyDataHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TopologyDataHandler.class);
    private DataBroker dataBroker;
    private TopologyManager topologyManager;
    private enum SIPType {uni, enni, inni}

    public TopologyDataHandler(DataBroker dataBroker, TopologyManager topologyManager) {
        this.dataBroker = dataBroker;
        this.topologyManager = topologyManager;
    }

    public void init() {
        Objects.requireNonNull(dataBroker);
        LOG.info("Starting topology handler");
        // this is a static and simplistic topology push to the TAPI system topology

        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();

        try {

            // we have prepared an dao abstraction to make it easier to use some of the common interactions with
            // MD-SAL but you can use tx.put tx.merge etc. by yourself if you prefere to
            NrpDao nrpDao = new NrpDao(tx);

            Node node1 = nrpDao.createNode(topologyManager.getSystemTopologyId(), "node-id-1",TemplateConstants.DRIVER_ID, ETH.class, null);
            Node node2 = nrpDao.createNode(topologyManager.getSystemTopologyId(), "node-id-2",TemplateConstants.DRIVER_ID, ETH.class, null);

            //we are creating a list of NodeEdgePoints for the nodes no sips are added to the system
            List<OwnedNodeEdgePoint> node1Endpoints = createSomeEndpoints(node1.getUuid().getValue(), 1, 2, 5, 7);
            List<OwnedNodeEdgePoint> node2Endpoints = createSomeEndpoints(node2.getUuid().getValue(), 1, 2, 5, 7);
            nrpDao.updateNep(node1.getUuid().getValue(), node1Endpoints.get(0));
            nrpDao.updateNep(node2.getUuid().getValue(), node2Endpoints.get(0));
            createLink(tx,node1,node1Endpoints.get(0),node2,node2Endpoints.get(0));
            //add sip for one of these endpoints

            //create sid and add it to model
            ServiceInterfacePoint someSip1 = createSomeSip("some-sip-1", SIPType.uni);
            ServiceInterfacePoint someSip2 = createSomeSip("some-sip-2", SIPType.inni);
            ServiceInterfacePoint someSip3 = createSomeSip("some-sip-3", SIPType.enni);
            nrpDao.addSip(someSip1);
            nrpDao.addSip(someSip2);
            nrpDao.addSip(someSip3);

            //update an existing nep with mapping to sip
            OwnedNodeEdgePoint updatedNep1 = new OwnedNodeEdgePointBuilder(node1Endpoints.get(1))
                    .setMappedServiceInterfacePoint(Collections.singletonList(someSip1.getUuid()))
                    .build();

            OwnedNodeEdgePoint updatedNep2 = new OwnedNodeEdgePointBuilder(node1Endpoints.get(2))
                    .setMappedServiceInterfacePoint(Collections.singletonList(someSip2.getUuid()))
                    .build();

            OwnedNodeEdgePoint updatedNep3 = new OwnedNodeEdgePointBuilder(node2Endpoints.get(3))
                    .setMappedServiceInterfacePoint(Collections.singletonList(someSip3.getUuid()))
                    .build();

            nrpDao.updateNep(node1.getUuid().getValue(), updatedNep1);
            nrpDao.updateNep(node1.getUuid().getValue(), updatedNep2);
            nrpDao.updateNep(node2.getUuid().getValue(), updatedNep3);


            tx.submit().checkedGet();
        } catch (TransactionCommitFailedException e) {
            LOG.error("Adding nodes to system topology has failed", e);
        }

    }

    private ServiceInterfacePoint createSomeSip(String idx, SIPType type) {

        ServiceInterfacePoint1Builder sipBuilder = new ServiceInterfacePoint1Builder();


        switch(type) {
            case enni:
                sipBuilder.setNrpCarrierEthEnniNResource(new NrpCarrierEthEnniNResourceBuilder()
                    .setMaxFrameSize(new NaturalNumber(new Long(1024)))
                    .build()

                );
                break;
            case uni:
                sipBuilder.setNrpCarrierEthUniNResource(new NrpCarrierEthUniNResourceBuilder()
                        .setMaxFrameSize(new NaturalNumber(new Long(1024)))
                        .build()

                );
                break;
            case inni:
            default:
                sipBuilder.setNrpCarrierEthInniNResource(new NrpCarrierEthInniNResourceBuilder()
                        .setMaxFrameSize(new NaturalNumber(new Long(1024)))
                        .build()

                );
                break;
        }

        return new ServiceInterfacePointBuilder()
                .setUuid(new Uuid("sip" + ":" + TemplateConstants.DRIVER_ID + ":" + idx))
                .setLayerProtocol(Collections.singletonList(TapiUtils.toSipPN(ETH.class)))
                .addAugmentation(ServiceInterfacePoint1.class, sipBuilder.build())
                .build();
    }

    private List<OwnedNodeEdgePoint> createSomeEndpoints(String nodeId, int... indexes) {

        return Arrays.stream(indexes).mapToObj(idx -> new OwnedNodeEdgePointBuilder()
                .setUuid(new Uuid(nodeId + ":nep" + idx))
                .setLayerProtocol(Collections.singletonList(TapiUtils.toNepPN(ETH.class)))
                .setLinkPortDirection(PortDirection.BIDIRECTIONAL)
                .setLinkPortRole(PortRole.SYMMETRIC)
                .setState(new org.opendaylight.yang.gen.v1.urn.onf.params.xml.ns.yang.tapi.topology.rev171113.node.edge.point.StateBuilder()
                        .setAdministrativeState(AdministrativeState.UNLOCKED)
                        .setLifecycleState(LifecycleState.INSTALLED)
                        .setOperationalState(OperationalState.DISABLED)
                        .build()
                )
                .build()).collect(Collectors.toList());
    }

    private void createLink(ReadWriteTransaction tx, Node n1, OwnedNodeEdgePoint onep1, Node n2, OwnedNodeEdgePoint onep2){
        Uuid uuid = new Uuid(onep1.getUuid().getValue()+onep2.getUuid().getValue());
        Link link = new LinkBuilder()
                .setUuid(uuid)
                .setKey(new LinkKey(uuid))
                .setDirection(ForwardingDirection.BIDIRECTIONAL)
                .setLayerProtocolName(Collections.singletonList(ETH.class))
                .setNode(Stream.of(n1.getUuid(),n2.getUuid()).collect(Collectors.toList()))
                .setNodeEdgePoint(Stream.of(onep1.getUuid(),onep2.getUuid()).collect(Collectors.toList()))
                .setState(new StateBuilder().setOperationalState(OperationalState.ENABLED).build())
                .build();

        tx.put(LogicalDatastoreType.OPERATIONAL, NrpDao.topo(PRESTO_SYSTEM_TOPO).child(Link.class, new LinkKey(uuid)), link);
    }

    public void close() {
        LOG.info("Closing topology handler");
    }

}
