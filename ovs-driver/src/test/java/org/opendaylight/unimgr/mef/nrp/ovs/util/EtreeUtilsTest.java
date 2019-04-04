/*
 * Copyright (c) 2018 Xoriant Corporation and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author OmS.awasthi@Xoriant.Com*
 */

@SuppressWarnings("deprecation")
@RunWith(PowerMockRunner.class)
@PrepareForTest(EtreeUtils.class)
public class EtreeUtilsTest {

    private static final Map<String, Integer> usedService =
            PowerMockito.spy(new Hashtable<String, Integer>());
    private static final Map<Integer, String> prePopMap =
            PowerMockito.spy(new Hashtable<Integer, String>());
    public static final String CPETYPE = "CPE";
    public static final String GENERATEVID = "generateVid";
    public static final String GENERATEVLNID = "getVlanID";
    public static final String RELEASETREESERVICEVLAN = "releaseTreeServiceVlan";
    public static final String CPEVLANRANGE = "cpeVlanRange";
    public static final String SPEVLANRANGE = "speVlanRange";
    public static final String GENERATEPREPROPVLAN = "generatePrePopVlan";
    public static final String AVAILABLEVLAN = "isAvailableVlan";
    public static final String RELEASEEPTREE = "releaseEpTreeServiceVlan";
    public static final String GETSERVICETYPE = "getServiceType";
    private String serviceName;
    @Mock
    private EtreeUtils mock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

    }

    @Test
    @SuppressWarnings("unchecked")
    public void getVlanIDTest() throws Exception {

        Map<String, Integer> usedService1 = PowerMockito.spy(new Hashtable<String, Integer>());
        Set<Entry<String, Integer>> set = usedService.entrySet();
        Integer in = new Integer(0);
        when(usedService1.entrySet()).thenReturn(set);
        Optional<Integer> optInt = PowerMockito.mock(Optional.class);

        @SuppressWarnings("rawtypes")
        Stream streem = PowerMockito.mock(Stream.class);
        when(streem.findFirst()).thenReturn(optInt);
        when(optInt.isPresent()).thenReturn(false);
        when(optInt.get()).thenReturn(in);
        PowerMockito.doReturn(in).when(mock, GENERATEVID, serviceName);
        PowerMockito.doReturn(in).when(mock, GENERATEVLNID, serviceName);
        Integer actualInt = mock.getVlanID(serviceName);
        Assert.assertEquals(in, actualInt);
    }

    @Test
    @SuppressWarnings("unchecked")

    public void releaseTreeServiceVlanTest() throws Exception {
        @SuppressWarnings("unused")
        Map<String, Integer> usedService1 = PowerMockito.spy(new Hashtable<String, Integer>());
        String str = "";
        Set<Entry<String, Integer>> set = usedService.entrySet();
        @SuppressWarnings("rawtypes")
        Stream streem = PowerMockito.mock(Stream.class);
        when(usedService1.entrySet()).thenReturn(set);
        Optional<String> optStr = PowerMockito.mock(Optional.class);
        when(streem.findFirst()).thenReturn(optStr);
        when(optStr.get()).thenReturn(str);
        PowerMockito.doNothing().when(mock, RELEASETREESERVICEVLAN, serviceName);
        mock.releaseTreeServiceVlan(serviceName);
    }

    @Test
    public void cpeVlanRangeTest() throws Exception {
        PowerMockito.mockStatic(EtreeUtils.class, Mockito.CALLS_REAL_METHODS);

        Set<Integer> cpeRange = PowerMockito.spy(new TreeSet<Integer>());
        PowerMockito.doReturn(cpeRange).when(EtreeUtils.class, CPEVLANRANGE);
        Set<Integer> actualCpeRange = EtreeUtils.cpeVlanRange();
        assertThat(actualCpeRange, is(cpeRange));

    }

    @Test
    public void speVlanRangeTest() throws Exception {
        PowerMockito.mockStatic(EtreeUtils.class, Mockito.CALLS_REAL_METHODS);
        Set<Integer> speRange = PowerMockito.spy(new TreeSet<Integer>());
        int vlan = 301;
        PowerMockito.doReturn(speRange).when(EtreeUtils.class, SPEVLANRANGE, vlan);
        Set<Integer> actualCpeRange = EtreeUtils.speVlanRange(vlan);
        assertThat(actualCpeRange, is(speRange));
    }

    @Test
    public void generatePrePopVlanTest() throws Exception {
        PowerMockito.mockStatic(EtreeUtils.class, Mockito.CALLS_REAL_METHODS);
        PowerMockito.doNothing().when(EtreeUtils.class, GENERATEPREPROPVLAN);
        EtreeUtils.generatePrePopVlan();
    }

    @Test
    public void isAvailableVlanTest() throws Exception {
        PowerMockito.mockStatic(EtreeUtils.class, Mockito.CALLS_REAL_METHODS);
        Set<Entry<Integer, String>> set = prePopMap.entrySet();
        int vlan = 301;
        @SuppressWarnings("rawtypes")
        Stream streem = PowerMockito.mock(Stream.class);
        when(prePopMap.entrySet()).thenReturn(set);
        @SuppressWarnings("unchecked")
        Optional<String> firstKey = PowerMockito.mock(Optional.class);
        when(streem.findFirst()).thenReturn(firstKey);
        when(firstKey.isPresent()).thenReturn(true);
        String str = "101-102-103";
        when(firstKey.get()).thenReturn(str);
        int[] num = new int[] {101, 102};
        PowerMockito.doReturn(num).when(EtreeUtils.class, AVAILABLEVLAN, vlan);
        int[] actualNum = EtreeUtils.isAvailableVlan(vlan);
        assertThat(actualNum, is(num));

    }

    @Test
    public void releaseEpTreeServiceVlan() throws Exception {
        @SuppressWarnings("unused")
        Map<String, Integer> usedEpTreeService = PowerMockito.spy(new Hashtable<String, Integer>());
        String str = "";
        Set<Entry<String, Integer>> set = usedService.entrySet();
        @SuppressWarnings("rawtypes")
        Stream streem = PowerMockito.mock(Stream.class);
        when(usedEpTreeService.entrySet()).thenReturn(set);
        @SuppressWarnings("unchecked")
        Optional<String> optStr = PowerMockito.mock(Optional.class);
        when(streem.findFirst()).thenReturn(optStr);
        when(optStr.get()).thenReturn(str);
        PowerMockito.doNothing().when(mock, RELEASEEPTREE, serviceName);
        mock.releaseEpTreeServiceVlan(serviceName);
    }

    @Test
    public void getServiceTypeTest() throws Exception {
        String nodeId = "301";
        DataBroker dataBroker = PowerMockito.mock(DataBroker.class);
        ReadOnlyTransaction readOnly = PowerMockito.mock(ReadOnlyTransaction.class);
        when(dataBroker.newReadOnlyTransaction()).thenReturn(readOnly);
        PowerMockito.doReturn(true).when(mock, GETSERVICETYPE, dataBroker, nodeId);
        Boolean actualValue = mock.getServiceType(dataBroker, nodeId);
        Assert.assertEquals(actualValue, Boolean.TRUE);
    }
}
