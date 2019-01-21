#!/bin/sh
helm repo add confluent https://confluentinc.github.io/cp-helm-charts/
helm repo update
helm install confluent/cp-helm-charts --name kafka --namespace kafka
