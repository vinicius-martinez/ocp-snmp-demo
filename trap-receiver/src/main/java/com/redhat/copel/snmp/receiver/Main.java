package com.redhat.copel.snmp.receiver;

import org.snmp4j.smi.UdpAddress;
import com.redhat.copel.snmp.receiver.TrapReceiver;

public class Main {

	public static void main(String[] args) {
		TrapReceiver trapReceiver = new TrapReceiver();
		String receiverIp = System.getenv("TRAP_RECEIVER_SERVICE_HOST");
		try {
			trapReceiver.listen(new UdpAddress(receiverIp + "/162"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
