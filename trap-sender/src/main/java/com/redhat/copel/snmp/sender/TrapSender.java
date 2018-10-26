package com.redhat.copel.snmp.sender;

import java.util.Date;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TrapSender {
	
	private String ipAdress;
	private int portNumber;
	
	public static final String community = "public";

	// Sending Trap for sysLocation of RFC1213
	public static final String Oid = ".1.3.6.1.2.1.1.8";
	
	public TrapSender(String ip, int port) {
		this.ipAdress = ip;
		this.portNumber = port;
	}

	public void sendTrap() {
		try {
			// Create Transport Mapping
			TransportMapping transport = new DefaultUdpTransportMapping();
			transport.listen();

			// Create Target
			CommunityTarget cTarget = new CommunityTarget();
			cTarget.setCommunity(new OctetString(community));
			cTarget.setVersion(SnmpConstants.version2c);
			cTarget.setAddress(new UdpAddress(this.ipAdress + "/" + this.portNumber));
			cTarget.setRetries(2);
			cTarget.setTimeout(5000);

			// Create PDU for V2
			PDU pdu = new PDU();

			// need to specify the system up time
			pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
					new OctetString(new Date().toString())));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(
					Oid)));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
					new IpAddress(this.ipAdress)));

			pdu.add(new VariableBinding(new OID(Oid), new OctetString(
					"Major")));
			pdu.setType(PDU.NOTIFICATION);

			// Send the PDU
			Snmp snmp = new Snmp(transport);
			System.out.println("Sending V2 Trap... Check Wheather NMS is Listening or not? ");
			System.out.println(this.ipAdress + "/" + this.portNumber);
			snmp.send(pdu, cTarget);
			snmp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
}
