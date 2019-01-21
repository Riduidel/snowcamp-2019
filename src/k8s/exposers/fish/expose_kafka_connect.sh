#!/bin/sh
kubectl -n kafka port-forward $(kubectl -n kafka get endpoints kafka-cp-kafka-connect -o jsonpath='{.subsets[0].addresses[0].targetRef.name}') 18083:8083