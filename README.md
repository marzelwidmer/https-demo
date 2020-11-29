# Generating a Keystore
Now we'll create a set of cryptographic keys and store it in a keystore.
We can use the following command to generate our PKCS12 keystore format:

`keytool -genkeypair -alias keecalm -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keepcalm.p12 -validity 3650`

We can store as many numbers of key-pair in the same keystore each identified by a unique alias.

For generating our keystore in a JKS format, we can use the following command:

`keytool -genkeypair -alias keepcalm -keyalg RSA -keysize 2048 -keystore keepcalm.jks -validity 3650`

It is recommended to use the PKCS12 format which is an industry standard format. 
So in case we already have a JKS keystore, we can convert it to PKCS12 format using the following command:

`keytool -importkeystore -srckeystore keepcalm.jks -destkeystore keepcalm.p12 -deststoretype pkcs12`

We'll have to provide the source keystore password and set a new keystore password. The alias and keystore password will be needed later.

```
keytool -genkeypair -alias keecalm -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keepcalm.p12 -validity 3650
keytool -genkeypair -alias keepcalm -keyalg RSA -keysize 2048 -keystore keepcalm.jks -validity 3650
keytool -importkeystore -srckeystore keepcalm.jks -destkeystore keepcalm.p12 -deststoretype pkcs12
```


# Spring Initializr Create Project

```
http https://start.spring.io/starter.tgz \
    dependencies==actuator,web \
    description=="Demo project kboot" \
    applicationName==HttpsDemoApplicationServer \
    name==https-demo-server \
    groupId==ch.keepcalm \
    artifactId==https-demo-server \
    packageName==ch.keepcalm.demo \
    javaVersion==11 \
    language==kotlin \
    type==gradle-project \
    baseDir==https-demo-server | tar -xzvf -
```

```
cd kboot-demo-https
```

```
http https://raw.githubusercontent.com/marzelwidmer/marzelwidmer.github.io/master/img/banner.txt \
    > src/main/resources/banner.txt
```

```
echo "spring:
  application:
    name: https-demo-server" | > src/main/resources/application.yaml
```

```
rm src/main/resources/application.properties
```


```
mkdir src/main/resources/keystore
cp ../keepcalm.p12 src/main/resources/keystore
```

# Minikube Test Cluster
Start Test Cluster with `6144 Memory` and `4 CPUs`.

```
minikube start --memory 6144 --cpus 4 -p test
```

## Addons for Test Cluster
```
minikube addons enable dashboard -p test
minikube addons enable metrics-server -p test
minikube addons enable ingress -p test
minikube addons enable registry -p test
```
## List Addons from Test Cluster
```
minikube addons list -p test
```

## Minikube List Profiles
```
minikube profile list
```


# Get K8s Resources for appGroup https-demo
```
k get po --field-selector status.phase=Running  -l 'appGroup in (https-demo)' -n default -w
```


# Skaffold Deployment
## Deploy Applicaton 
```
skaffold run 
```


# Port-Forwarding 
## https-demo-client
```
kubectl port-forward service/https-demo-client 8080:80
```
## https-demo-server
```
k port-forward service/https-demo-server 8081:80
```


