# INTRODUCTION

This is a simple demo showcasing Openshift NodePort feature to enable consuming SNMP traps within OCP cluster.

## Environment

- CDK v3.6.0-1
- Java 1.8

## Deploy @ Localhost

- Clone the application:
```
git clone https://github.com/vinicius-martinez/ocp-snmp-demo.git
```
- Build all modules:
```
cd ocp-snmp-demo
mvn clean install
```
- Start SNMP Trap Receiver:
```
java -jar trap-receiver/target/trap-receiver.1.0.0-SNAPSHOT.jar
```
- The following output is expected:
```
Listening on 127.0.0.1/1062
```
*by default we're running the server-side on 0.0.0.0:1062. If you need/want/desire to change these values, you need to upgrade* [Receiver Main Class](https://github.com/vinicius-martinez/ocp-snmp-demo/blob/master/trap-receiver/src/main/java/com/redhat/copel/snmp/receiver/Main.java)

- Start SNMP Trap Sender:
```
java -jar trap-sender/target/trap-sender.1.0.0-SNAPSHOT.jar
```
*by default we're running the server-side on 0.0.0.0:1062. If you need/want/desire to change these values, you need to upgrade* [Sender Main Class](https://github.com/vinicius-martinez/ocp-snmp-demo/blob/master/trap-sender/src/main/java/com/redhat/copel/snmp/sender/Main.java)
- The following output is expected:
```
Sending V2 Trap... Check Wheather NMS is Listening or not?
127.0.0.1/1062
```
- Switch Back to *trap-receiver* terminal and check for the incoming traps with the following output:
```
Received PDU...
Trap Type = -89
Variables = [1.3.6.1.2.1.1.3.0 = Fri Oct 26 17:49:16 BRST 2018, 1.3.6.1.6.3.1.1.4.1.0 = 1.3.6.1.2.1.1.8, 1.3.6.1.6.3.18.1.3.0 = 127.0.0.1, 1.3.6.1.2.1.1.8 = Major]
```
## Deploy on OCP
