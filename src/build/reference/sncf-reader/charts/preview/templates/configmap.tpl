apiVersion: v1
data:
  kafka.topic: sncfReaderPreview
  kafka.url: kafka-cp-kafka.kafka.svc.cluster.local:9092
kind: ConfigMap
metadata:
  name: kafka-config
