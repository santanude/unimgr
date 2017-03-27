/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.unimgr.mef.nrp.common.NrpDao;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.LifecycleState;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.TerminationDirection;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.context.attrs.ServiceInterfacePointKey;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.service._interface.point.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePoint;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointBuilder;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapitopology.rev170227.node.OwnedNodeEdgePointKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * @author bartosz.michalik@amartus.com
 */
public class TopologyDataHandler {
    private static final Logger log = LoggerFactory.getLogger(TopologyDataHandler.class);
    private static final String OVS_NODE = "ovs-node";

    private final DataBroker dataBroker;

    public TopologyDataHandler(DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    public void init() {
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();

        NrpDao dao = new NrpDao(tx);
        dao.createSystemNode(OVS_NODE, null);

        IntStream.range(0, 10).forEach(i -> addEndpoint(dao, i));


        Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void result) {
                log.info("Node {} created", OVS_NODE);
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("No node created due to the error", t);
            }
        });

    }

    public void close() {
        ReadWriteTransaction tx = dataBroker.newReadWriteTransaction();

        NrpDao dao = new NrpDao(tx);
        dao.removeNode(OVS_NODE, true);



        Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void result) {
                log.info("Node {} created", OVS_NODE);
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("No node created due to the error", t);
            }
        });
    }

    private void addEndpoint(NrpDao dao, int i) {
        ServiceInterfacePoint sip = createSip(i);

        dao.addSip(sip);
        dao.updateNep(OVS_NODE, nep(i, sip.getUuid()));
    }

    private OwnedNodeEdgePoint nep(int idx, UniversalId sipUuid) {
        UniversalId uuid = new UniversalId(OVS_NODE + "_" + "nep" + idx);
        return new OwnedNodeEdgePointBuilder()
                .setUuid(uuid)
                .setKey(new OwnedNodeEdgePointKey(uuid))
                .setMappedServiceInterfacePoint(Collections.singletonList(sipUuid))
                .build();
    }

    private ServiceInterfacePoint createSip(int i) {
        UniversalId uuid = new UniversalId( OVS_NODE + "_" +"sip" + i);
        return new ServiceInterfacePointBuilder()
                .setUuid(uuid)
                .setKey(new ServiceInterfacePointKey(uuid))
                .setState(new StateBuilder().setLifecycleState(LifecycleState.Installed).build())
                .setDirection(TerminationDirection.Bidirectional)
                .build();
    }
}
