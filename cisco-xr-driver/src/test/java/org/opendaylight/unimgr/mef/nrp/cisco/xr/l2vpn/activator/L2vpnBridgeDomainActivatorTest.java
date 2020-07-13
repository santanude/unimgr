/*
 * Copyright (c) 2019 Xoriant Corporation and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.activator;

/**
 * @author Om.SAwasthi@Xoriant.Com
 *
 */
public class L2vpnBridgeDomainActivatorTest {

	/*
	 * @Mock private DataBroker dataBroker;
	 * 
	 * @Mock private MountPointService mountService; private static final String
	 * uuid1 = "sip:ciscoD1:GigabitEthernet0/0/0/1"; private static final String
	 * uuid2 = "sip:ciscoD2:GigabitEthernet0/0/0/1"; private static final String
	 * NETCONF_TOPOLODY_NAME = "topology-netconf"; private EndPoint ep1; private
	 * EndPoint ep2; private ServicePort port; private ServicePort neighbor; private
	 * L2vpnBridgeDomainActivator l2vpnBridgeDomainActivator;
	 * 
	 * @Before public void setUp() throws Exception { ConnectivityServiceEndPoint
	 * cep = new
	 * org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.
	 * connectivity.service.EndPointBuilder() .setServiceInterfacePoint(
	 * TapiUtils.toSipRef(new Uuid(uuid1), ServiceInterfacePoint.class))
	 * .setDirection(PortDirection.BIDIRECTIONAL).build(); ep1 = new EndPoint(cep,
	 * null); ConnectivityServiceEndPoint cep1 = new
	 * org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.
	 * connectivity.service.EndPointBuilder() .setServiceInterfacePoint(
	 * TapiUtils.toSipRef(new Uuid(uuid2), ServiceInterfacePoint.class))
	 * .setDirection(PortDirection.BIDIRECTIONAL).build(); ep2 = new EndPoint(cep1,
	 * null); port = ServicePort.toServicePort(ep1, NETCONF_TOPOLODY_NAME); neighbor
	 * = ServicePort.toServicePort(ep2, NETCONF_TOPOLODY_NAME);
	 * l2vpnBridgeDomainActivator = new L2vpnBridgeDomainActivator(dataBroker,
	 * mountService); }
	 * 
	 * @Test public void activateBridgeDomainTest() {
	 * 
	 * String outerName = "cs:16b9e18aa84:-364cc8e5"; String innerName =
	 * "cs:16b9e18aa84:-364cc8e5"; BdPseudowires bdPseudowires =
	 * PowerMockito.mock(BdPseudowires.class); boolean isExclusive = true;
	 * 
	 * BridgeDomainGroups dominGroups =
	 * l2vpnBridgeDomainActivator.activateBridgeDomain(outerName, innerName, port,
	 * neighbor, bdPseudowires, isExclusive); List<BridgeDomainGroup>
	 * domainGroupList = dominGroups.getBridgeDomainGroup(); BridgeDomainGroup
	 * bridgeDomainGroup = domainGroupList.get(0); BridgeDomains bridgeDomains =
	 * bridgeDomainGroup.getBridgeDomains(); List<BridgeDomain> bdlist =
	 * bridgeDomains.getBridgeDomain();
	 * 
	 * BridgeDomain bd = (BridgeDomain) bdlist.get(0); BdAttachmentCircuits
	 * bdAttachmentCircuits = bd.getBdAttachmentCircuits();
	 * List<BdAttachmentCircuit> bdAttachmentCircuitList =
	 * bdAttachmentCircuits.getBdAttachmentCircuit(); BdAttachmentCircuit
	 * bdAttachmentCircuit = bdAttachmentCircuitList.get(0);
	 * assertEquals("GigabitEthernet0/0/0/1",
	 * bdAttachmentCircuit.getName().getValue()); }
	 * 
	 * @Test public void activateInterfaceTest() {
	 * 
	 * InterfaceConfigurations interfaceConfigruaton =
	 * l2vpnBridgeDomainActivator.activateInterface(port, neighbor, 2000, false);
	 * interfaceConfigruaton.implementedInterface(); List<InterfaceConfiguration>
	 * list = interfaceConfigruaton.getInterfaceConfiguration();
	 * InterfaceConfiguration interfaceConfiguration = list.get(0);
	 * assertEquals("act", interfaceConfiguration.getActive().getValue()); }
	 * 
	 * @Test public void createSubInterfaceTest() { port.setVlanId(301l);
	 * InterfaceConfigurations interfaceConfiguration =
	 * l2vpnBridgeDomainActivator.createSubInterface(port, neighbor, 2000);
	 * Assert.assertNotNull(interfaceConfiguration); }
	 */

}
