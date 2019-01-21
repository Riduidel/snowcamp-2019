#!/bin/sh
helm repo add elastic https://helm.elastic.co
helm repo update
helm install stable/elasticsearch --namespace elastic --name elasticsearch --set imageTag=6.5.4
REM (voir https://github.com/elastic/helm-charts/blob/6.5.2-alpha1/elasticsearch/README.md)
helm install elastic/kibana --namespace elastic --name kibana --set imageTag=6.5.4,elasticsearchURL=http://elasticsearch-client.elastic.svc.cluster.local:9200,image=docker.elastic.co/kibana/kibana-oss