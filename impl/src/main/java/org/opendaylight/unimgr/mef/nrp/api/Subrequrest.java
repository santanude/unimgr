/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.api;

import org.opendaylight.yang.gen.v1.urn.mef.yang.tapicommon.rev170227.UniversalId;

import java.util.List;

/**
 * @author bartosz.michalik@amartus.com
 */
public class Subrequrest {
    final UniversalId uuid;
    final List<EndPoint> endpoints;

    public Subrequrest(UniversalId uuid, List<EndPoint> endpoints) {
        this.uuid = uuid;
        this.endpoints = endpoints;
    }

    public UniversalId getUuid() {
        return uuid;
    }

    public List<EndPoint> getEndpoints() {
        return endpoints;
    }
}
