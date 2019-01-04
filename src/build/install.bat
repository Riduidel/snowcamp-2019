REM Create the cluster
jx create cluster gke --cluster-name=snowcamp-2019 --default-admin-password=%DEFAULT_ADMIN_PASSWORD% --default-environment-prefix=snowcamp-2019 --project-id=snowcamp-2019-223915 --min-num-nodes=5 --max-num-nodes=10 --zone=europe-west1-d --machine-type=n1-standard-2 --gitops=false --batch-mode=true --install-dependencies=true --environment-git-owner=Zenika --git-api-token=%GIT_ACCESS_TOKEN% --git-username=Riduidel

REM install kafka
helm repo add confluent https://confluentinc.github.io/cp-helm-charts/
helm repo update
helm install confluent/cp-helm-charts --name kafka --namespace kafka

REM install some Kafka clients
kubectl apply -f ../k8s/kafka/kafka-client.yml
kubectl apply -f ../k8s/kafka/kafka-topics-ui.yml
kubectl apply -f ../k8s/kafka/kafka-connect-ui.yml

REM Install Elastic
helm repo add elastic https://helm.elastic.co
helm repo update
helm install --namespace elastic --name elasticsearch stable/elasticsearch
REM (voir https://github.com/elastic/helm-charts/blob/6.5.2-alpha1/elasticsearch/README.md)
helm install --namespace elastic  --name kibana elastic/kibana --set elasticSearchURL=http://elasticsearch-master.elastic.svc.cluster.local:9200
REM (voir https://github.com/elastic/helm-charts/tree/6.5.2-alpha1/kibana)

REM install weave scope
helm install stable/weave-scope --namespace weave-scope --name weave-scope
