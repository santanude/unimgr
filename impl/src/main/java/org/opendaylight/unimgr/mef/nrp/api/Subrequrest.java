/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.api;

import java.util.List;

import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.Uuid;

/**
 * @author bartosz.michalik@amartus.com
 */
public class Subrequrest {
    private final Uuid nodeUuid;
    private Uuid nepUuid;
    private final List<EndPoint> endpoints;

    public Subrequrest(Uuid nodeUuid, List<EndPoint> endpoints) {
        this.nodeUuid = nodeUuid;
        this.endpoints = endpoints;
    }

    public Uuid getNodeUuid() {
        return nodeUuid;
    }

    public List<EndPoint> getEndpoints() {
        return endpoints;
    }
}
