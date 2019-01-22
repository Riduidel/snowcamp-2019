apiVersion: v1
data:
  kafka.topic: timesheets_preview
  kafka.url: kafka-cp-kafka.kafka.svc.cluster.local:9092
kind: ConfigMap
metadata:
  name: kafka-config
