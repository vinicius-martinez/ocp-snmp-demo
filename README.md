# INTRODUCTION

This is a simple demo showcasing Openshift NodePort feature to enable consuming SNMP traps within OCP cluster.

## Environment

- CDK v3.6.0-1
- Java "1.8.0_162"

## Deploy @ Localhost

- clone the application:
```
git clone https://github.com/vinicius-martinez/ocp-snmp-demo.git
```
- build all modules
```
cd ocp-snmp-demo
mvn clean install
```
- start SNMP Trap receiver
```
java -jar trap-receiver/target/trap-receiver.1.0.0-SNAPSHOT.jar
```
- The following output is expected:
```
Listening on 127.0.0.1/1062
```
## Deploy on OCP
