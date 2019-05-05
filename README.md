# currency-rate web-service

Short instruction.


### Basic and image build
To build the application:

```
mvn clean package
```

To try it out standalone:
    
```
java -jar target/currency-rate-service-1.0.0.jar &
curl http://localhost:8080/api/rate/USD/2015-09-24
```

To build an image and push it to Docker Hub then:
```
mvn dockerfile:build dockerfile:push -Ddockerfile.username=<username> -Ddockerfile.password=<password>
```

### Deployment in Minikube
To deploy web-service in Minikube:
```
kubectl create -f k8s-files/currency-deployment.yaml
```

To be able to send requests to "pretty" address, like this: `curl http://currency.com/api/rate/USD/2015-09-24`, execute:
```
kubectl create -f k8s-files/currency-service-nodeport.yaml
kubectl create -f k8s-files/currency-ingress.yaml
kubectl get ingress | grep currency | awk '{print $3 " " $2}' | sudo tee -a /etc/hosts
```
### Update in Minikube

To initiate an update (v1.0.0 -> v2.0.0):
```
sed 's/1.0.0/2.0.0/' -i k8s-files/currency-deployment.yaml
kubectl apply -f k8s-files/currency-deployment.yaml
````
