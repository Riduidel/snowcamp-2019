#!/usr/bin/fish
kubectl port-forward --namespace elastic (kubectl get pod --namespace elastic --selector="app=elasticsearch,release=elasticsearch,component=client" --output jsonpath='{.items[0].metadata.name}') 18180:9200
