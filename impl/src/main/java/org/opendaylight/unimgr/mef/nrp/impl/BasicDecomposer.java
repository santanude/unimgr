/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.impl;

import org.opendaylight.unimgr.mef.nrp.api.Constraints;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.api.RequestDecomposer;
import org.opendaylight.unimgr.mef.nrp.api.Subrequrest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Basic implementation of a decomposer
 * @author bartosz.michalik@amartus.com
 */
public class BasicDecomposer implements RequestDecomposer {
    private static final Logger log = LoggerFactory.getLogger(BasicDecomposer.class);

    public BasicDecomposer() {
        log.trace("basic decomposer initialized");
    }

    @Override
    public List<Subrequrest> decompose(List<EndPoint> endpoints, Constraints constraint) {
        return null;
    }


}
