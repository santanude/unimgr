/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.common;

import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;

import java.util.List;

/**
 * Device facing SPI for activating or deactivating a fragment of an NRP
 * Connectivity Service on a single device.
 */
public interface ResourceActivator {

    /**
     * Activate connectivity betwee the provided endpoints.
     * @param endPoints list of endpoint to connect
     * @param serviceName generated service id
     * @throws ResourceActivatorException activation problem
     * @throws TransactionCommitFailedException transaction commit failed
     */
    void activate(List<EndPoint> endPoints, String serviceName, boolean isExclusive, String serviceType) throws  ResourceActivatorException, TransactionCommitFailedException;

    /**
     * Deactivate connectivity between the provided endpoints.
     * @param endPoints list of endpoint between which connection have to be deactivated
     * @param serviceName generated service id
     * @throws ResourceActivatorException activation problem
     * @throws TransactionCommitFailedException transaction commit failed
     */
    void deactivate(List<EndPoint> endPoints, String serviceName, String serviceType, boolean isExclusive) throws TransactionCommitFailedException, ResourceActivatorException;
}
