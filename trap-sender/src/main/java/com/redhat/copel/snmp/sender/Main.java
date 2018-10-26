package com.redhat.copel.snmp.sender;

public class Main {

	public static void main(String[] args) {
		TrapSender sender = new TrapSender("192.168.64.12", 31258);
		while (true) {
			try {
				sender.sendTrap();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
