package com.redhat.copel.snmp.receiver;

import org.snmp4j.smi.UdpAddress;
import com.redhat.copel.snmp.receiver.TrapReceiver;

public class Main {

	public static void main(String[] args) {
		TrapReceiver trapReceiver = new TrapReceiver();
		try {
			trapReceiver.listen(new UdpAddress("127.0.0.1/162"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
