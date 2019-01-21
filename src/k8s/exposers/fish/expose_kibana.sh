#!/usr/bin/fish
kubectl port-forward --namespace elastic (kubectl get pod --namespace elastic --selector="app=kibana,release=kibana" --output jsonpath='{.items[0].metadata.name}') 18181:5601