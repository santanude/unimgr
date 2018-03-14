/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.common;

import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.LayerProtocolName;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.TerminationDirection;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.TerminationState;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.transfer.cost.pac.CostCharacteristic;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.transfer.cost.pac.CostCharacteristicBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.transfer.timing.pac.LatencyCharacteristic;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.transfer.timing.pac.LatencyCharacteristicBuilder;

import java.util.Collections;
import java.util.List;

/**
 * @author bartosz.michalik@amartus.com
 */
public class TapiUtils {
    public static org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.service._interface.point.LayerProtocol toSipPN(Class<? extends LayerProtocolName> name) {
        org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.service._interface.point.LayerProtocolBuilder builder
                = new org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.service._interface.point.LayerProtocolBuilder();

        return builder.setLayerProtocolName(name).setLocalId(name.getSimpleName()).build();
    }


    public static org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.node.edge.point.LayerProtocol toNepPN(Class<? extends LayerProtocolName> name) {
        org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.node.edge.point.LayerProtocolBuilder builder
                = new org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.topology.rev180307.node.edge.point.LayerProtocolBuilder();

        return builder
                .setLayerProtocolName(name)
                .setLocalId(name.toString())
                .setTerminationDirection(TerminationDirection.BIDIRECTIONAL)
                .setTerminationState(TerminationState.TERMINATEDBIDIRECTIONAL)
                .build();
    }

    public static List<LatencyCharacteristic> emptyTransferCost() {
        return Collections.singletonList(new LatencyCharacteristicBuilder()
                .setTrafficPropertyName("empty")
                .setTrafficPropertyQueingLatency("n/a").build()
        );
    }

    public static List<CostCharacteristic> emptyCostCharacteristic() {
        CostCharacteristic cost = new CostCharacteristicBuilder()
                .setCostName("empty")
                .setCostAlgorithm("n/a")
                .setCostValue("0")
                .build();
        return Collections.singletonList(cost);
    }
}
