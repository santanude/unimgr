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

import java.util.List;

/**
 * Basic implementation of a decomposer
 * @author bartosz.michalik@amartus.com
 */
public class BasicDecomposer implements RequestDecomposer {
    @Override
    public List<Subrequrest> decompose(List<EndPoint> endpoints, Constraints constraint) {
        return null;
    }
}
