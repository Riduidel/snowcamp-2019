#!/bin/sh
kubectl -n weave-scope port-forward $(kubectl -n weave-scope get endpoints weave-scope-weave-scope -o jsonpath='{.subsets[0].addresses[0].targetRef.name}') 18280:4040
