/*
 * Copyright (c) 2016 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.utils;

import org.opendaylight.yang.gen.v1.urn.mef.yang.tapi.common.rev170712.Uuid;

/**
 * @author marek.ryznar@amartus.com
 */
public class SipHandler {

    public static String getDeviceName(Uuid sip) {
        String[] sipTab = sip.getValue().split(":");
        return sipTab[sipTab.length - 2];
    }

    public static String getPortName(Uuid sip) {
        String[] sipTab = sip.getValue().split(":");
        return sipTab[sipTab.length - 1];
    }

    public static boolean isTheSameDevice(Uuid sip1, Uuid sip2) {
        return getDeviceName(sip1).equals(getDeviceName(sip2));
    }
}
