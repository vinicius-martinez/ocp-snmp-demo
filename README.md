# INTRODUCTION

This is a simple demo showcasing Openshift NodePort feature to enable consuming SNMP traps within OCP cluster.

## Environment

- Openshift 3.10
- [Container Development Kit 3.6](https://developers.redhat.com/products/cdk/overview/)
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

- Log on Openshift/Minishift:
```
oc login -u developer -p developer [minishift:ip]:[minishift:port]
```
- Build *Trap-Receiver* on Openshift/Minishift:
```
oc new-build redhat-openjdk18-openshift:1.2~https://github.com/vinicius-martinez/ocp-snmp-demo.git -e ARTIFACT_DIR="trap-receiver/target" -e JAVA_APP_DIR="trap-receiver"
```
- Deploy *Trap-Receiver* on Openshift/Minishift:
```
oc new-app --image-stream=ocp-snmp-demo
```
- Remove the default *Kubernetes Service*:
```
oc delete svc ocp-snmp-demo
```
- Create a new *Kubernetes Service* with *NodePort* support:
```
oc expose dc ocp-snmp-demo --type=LoadBalancer --name=ocp-snmp-demo-ingress --protocol="UDP" --port=1062
```
- Take note of the *NodePort* by running:
```
oc export svc ocp-snmp-demo-ingress
...
ports:
  - nodePort: 31942
    port: 1062
    protocol: UDP
    targetPort: 1062
  selector:
```
- Update your *Trap-Sender* [Sender Main Class](https://github.com/vinicius-martinez/ocp-snmp-demo/blob/master/trap-sender/src/main/java/com/redhat/copel/snmp/sender/Main.java) client with the Node IP and *NodePort* values. Example:
```
TrapSender sender = new TrapSender("192.168.64.12", 31942);
```
- Finally if your *Trap-Sender Pod* is displaying the following output:
```
oc get pods
NAME                    READY     STATUS      RESTARTS   AGE
ocp-snmp-demo-1-build   0/1       Completed   0          12m
ocp-snmp-demo-1-v7p88   1/1       Running     0          10m

oc logs -f ocp-snmp-demo-1-v7p88
Starting the Java application using /opt/run-java/run-java.sh ...
exec java -javaagent:/opt/jolokia/jolokia.jar=config=/opt/jolokia/etc/jolokia.properties -XX:+UseParallelGC -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:MaxMetaspaceSize=100m -XX:+ExitOnOutOfMemoryError -cp . -jar /deployments/trap-receiver.1.0.0-SNAPSHOT.jar
Listening on 0.0.0.0/1062
I> No access restrictor found, access to any MBean is allowed
Jolokia: Agent started with URL https://172.17.0.8:8778/jolokia/
Received PDU...
Trap Type = -89
Variables = [1.3.6.1.2.1.1.3.0 = Fri Oct 26 18:34:02 BRST 2018, 1.3.6.1.6.3.1.1.4.1.0 = 1.3.6.1.2.1.1.8, 1.3.6.1.6.3.18.1.3.0 = 192.168.64.12, 1.3.6.1.2.1.1.8 = Major]
Received PDU...
Trap Type = -89
Variables = [1.3.6.1.2.1.1.3.0 = Fri Oct 26 18:34:07 BRST 2018, 1.3.6.1.6.3.1.1.4.1.0 = 1.3.6.1.2.1.1.8, 1.3.6.1.6.3.18.1.3.0 = 192.168.64.12, 1.3.6.1.2.1.1.8 = Major]
```

## Further Reading

[CDK/Minishift Download](https://developers.redhat.com/products/cdk/download/)

[CDK/Minishift Documentation](https://developers.redhat.com/products/cdk/docs-and-apis/)

[CDK/Minishift NodePort](https://access.redhat.com/documentation/en-us/red_hat_container_development_kit/3.6/html-single/getting_started_guide/#nodeport-services)

[Openshift NodePort](https://docs.openshift.com/container-platform/3.10/dev_guide/expose_service/expose_internal_ip_nodeport.html)
