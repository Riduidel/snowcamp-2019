#!/bin/sh
# Create cluster
jx create cluster gke --cluster-name=snowcamp-2019 --default-admin-password=$DEFAULT_ADMIN_PASSWORD --default-environment-prefix=snowcamp-2019 --project-id=snowcamp-2019-223915 --min-num-nodes=5 --max-num-nodes=10 --zone=europe-west1-d --machine-type=n1-standard-2 --gitops=false --batch-mode=true --install-dependencies=true --environment-git-owner=Zenika --git-api-token=$GIT_ACCESS_TOKEN --git-username=Riduidel

# install kafka
helm repo add confluent https://confluentinc.github.io/cp-helm-charts/
helm repo update
helm install confluent/cp-helm-charts --name kafka --namespace kafka

# install some Kafka clients
kubectl create configmap kafka-hq-config --namespace kafka --from-file=application.conf=src\k8s\kafka\kafka-hq-config.hocon
kubectl apply -f src/k8s/kafka/kafka-connect-ui-deployment.yml
kubectl apply -f src/k8s/kafka/kafka-connect-ui-service.yml
kubectl apply -f src/k8s/kafka/kafka-connect-ui-ingress.yml
kubectl apply -f src/k8s/kafka/kafka-hq-deployment.yml
kubectl apply -f src/k8s/kafka/kafka-hq-service.yml
kubectl apply -f src/k8s/kafka/kafka-hq-ingress.yml

# Install Elastic
helm repo add elastic https://helm.elastic.co
helm repo update
helm install stable/elasticsearch --namespace elastic --name elasticsearch --set imageTag=6.5.4
# (voir https://github.com/elastic/helm-charts/blob/6.5.2-alpha1/elasticsearch/README.md)
helm install elastic/kibana --namespace elastic --name kibana --set imageTag=6.5.4,elasticsearchURL=http://elasticsearch-client.elastic.svc.cluster.local:9200,image=docker.elastic.co/kibana/kibana-oss
# (voir https://github.com/elastic/helm-charts/tree/6.5.2-alpha1/kibana)

kubectl apply -f src/k8s/elastic/kibana-ingress.yml

# Install weave scope
helm install stable/weave-scope --namespace weave-scope --name weave-scope
kubectl apply -f src/k8s/weave/weave-scope-ingress.yml

# Install configmaps for application configuration
kubectl create configmap kafka-config -n jx-production --from-literal=kafka.topic=timesheets_production --from-literal=kafka.url=kafka-cp-kafka.kafka.svc.cluster.local:9092
kubectl create configmap kafka-config -n jx-staging --from-literal=kafka.topic=timesheets_staging --from-literal=kafka.url=kafka-cp-kafka.kafka.svc.cluster.local:9092

kubectl create namespace configuration
kubectl create -f src\k8s\navitia.yaml  -n configuration
kubectl get secret navitia-token --namespace=configuration --export -o yaml | kubectl apply --namespace=jx-staging -f -
kubectl get secret navitia-token --namespace=configuration --export -o yaml | kubectl apply --namespace=jx-production -f -
