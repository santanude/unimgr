/*
 * Copyright (c) 2018 Xoriant Corporation and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs.util;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.common.ResourceNotAvailableException;
import org.opendaylight.unimgr.mef.nrp.ovs.exception.VlanPoolExhaustedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Sets;

/**
 * @author Arif.Hussain@Xoriant.Com
 *
 */
public class EtreeUtils {
    
    private final static Set<Integer> usedVlans = new HashSet<>();
    private final static Map<String,Integer> usedService = new Hashtable<String, Integer>();
    private final static Set<Integer> possibleRootVlans = IntStream.range(2048, 4094).boxed().collect(Collectors.toSet());
    private final static String VLAN_POOL_EXHAUSTED_ERROR_MESSAGE = "All VLAN IDs are in use. VLAN pool exhausted.";

    private final static Logger LOG = LoggerFactory.getLogger(VlanUtils.class);


    /**
     * Method return vlan ID for e-tree service. If service is unique generate new one otherwise return same vlan-ID belong to service  .
     * @param serviceName
     */
    public Integer getVlanID(String serviceName) throws ResourceNotAvailableException, TransactionCommitFailedException {

        Optional<Integer> o = usedService.entrySet().stream()
                .filter(e -> e.getKey().equals(serviceName)).map(Map.Entry::getValue).findFirst();

        return o.isPresent() ? o.get().intValue() : generateVid(serviceName);
    }



    private Integer generateVid(String serviceName) throws VlanPoolExhaustedException, TransactionCommitFailedException {

        Set<Integer> difference = Sets.difference(possibleRootVlans, usedVlans);

        if (difference.isEmpty()) {
            LOG.warn(VLAN_POOL_EXHAUSTED_ERROR_MESSAGE);
            throw new VlanPoolExhaustedException(VLAN_POOL_EXHAUSTED_ERROR_MESSAGE);
        }
        return updateNodeNewServiceVLAN(serviceName, difference.iterator().next());
    }


    private Integer updateNodeNewServiceVLAN(String serviceName, Integer vlanId) {
        usedService.put(serviceName, vlanId);
        usedVlans.add(vlanId);
        return vlanId;
    }

    public void releaseTreeServiceVlan(String serviceName) {
        usedVlans.remove(usedService.entrySet().stream().filter(e -> e.getKey().equals(serviceName)).map(Map.Entry::getValue).findFirst().get());
        usedService.entrySet()
                .removeIf(serviceVlanMap -> serviceVlanMap.getKey().equals(serviceName));
    }
}
