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

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
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
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.*;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.tapi.context.ServiceInterfacePoint;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.tapi.context.ServiceInterfacePointBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.node.OwnedNodeEdgePoint;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.node.OwnedNodeEdgePointBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.node.edge.point.MappedServiceInterfacePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            //we are creating a list of NodeEdgePoints for the node no sips are added to the system
            List<OwnedNodeEdgePoint> someEndpoints = createSomeEndpoints(1, 2, 5, 7);
            nrpDao.createNode(topologyManager.getSystemTopologyId(), TemplateConstants.DRIVER_ID, LayerProtocolName.ETH, null);
            //add sip for one of these endpoints

            //create sid and add it to model
            ServiceInterfacePoint someSip1 = createSomeSip("some-sip-1", SIPType.uni);
            ServiceInterfacePoint someSip2 = createSomeSip("some-sip-2", SIPType.inni);
            ServiceInterfacePoint someSip3 = createSomeSip("some-sip-3", SIPType.enni);
            nrpDao.addSip(someSip1);
            nrpDao.addSip(someSip2);
            nrpDao.addSip(someSip3);

            //update an existing nep with mapping to sip

            MappedServiceInterfacePoint sipRef1 =
                    TapiUtils.toSipRef(new Uuid(someSip1.getUuid()), MappedServiceInterfacePoint.class);
            MappedServiceInterfacePoint sipRef2 =
                    TapiUtils.toSipRef(new Uuid(someSip2.getUuid()), MappedServiceInterfacePoint.class);
            MappedServiceInterfacePoint sipRef3 =
                    TapiUtils.toSipRef(new Uuid(someSip3.getUuid()), MappedServiceInterfacePoint.class);

            OwnedNodeEdgePoint updatedNep1 = new OwnedNodeEdgePointBuilder(someEndpoints.get(1))
                    .setMappedServiceInterfacePoint(Collections.singletonList(sipRef1))
                    .build();

            OwnedNodeEdgePoint updatedNep2 = new OwnedNodeEdgePointBuilder(someEndpoints.get(2))
                    .setMappedServiceInterfacePoint(Collections.singletonList(sipRef2))
                    .build();

            OwnedNodeEdgePoint updatedNep3 = new OwnedNodeEdgePointBuilder(someEndpoints.get(3))
                    .setMappedServiceInterfacePoint(Collections.singletonList(sipRef3))
                    .build();

            nrpDao.updateNep(TemplateConstants.DRIVER_ID, updatedNep1);
            nrpDao.updateNep(TemplateConstants.DRIVER_ID, updatedNep2);
            nrpDao.updateNep(TemplateConstants.DRIVER_ID, updatedNep3);


            tx.submit().checkedGet();
        } catch (TransactionCommitFailedException e) {
            LOG.error("Adding node to system topology has failed", e);
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
                .setLayerProtocolName(Collections.singletonList(LayerProtocolName.ETH))
                .addAugmentation(ServiceInterfacePoint1.class, sipBuilder.build())
                .build();
    }

    private List<OwnedNodeEdgePoint> createSomeEndpoints(int... indexes) {

        return Arrays.stream(indexes).mapToObj(idx -> new OwnedNodeEdgePointBuilder()
                .setUuid(new Uuid(TemplateConstants.DRIVER_ID + ":nep" + idx))
                .setLayerProtocolName(LayerProtocolName.ETH)
                .setLinkPortDirection(PortDirection.BIDIRECTIONAL)
                .setLinkPortRole(PortRole.SYMMETRIC)
                .setAdministrativeState(AdministrativeState.UNLOCKED)
                .setLifecycleState(LifecycleState.INSTALLED)
                .setOperationalState(OperationalState.DISABLED)
                .build()).collect(Collectors.toList());
    }

    public void close() {
        LOG.info("Closing topology handler");
    }

}
