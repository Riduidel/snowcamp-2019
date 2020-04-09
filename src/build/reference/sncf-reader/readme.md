# sncf-reader application

This application allows us to inject SNCF timesheets into our Kafka engine, for later processing.

## Configuration
This application requires the following environment variables to be set

* `SNCF_READER_TOKEN` access token for Navitia API
* `SNCF_READER_READ_AT_STARTUP` When set to true, immediatly start reading SNCF timesheet
* `SNCF_READER_KAFKA_BOOTSTRAP_SERVER` url of Kafka server to connect to
* `SNCF_READER_TOPIC_SCHEDULE` Topic where to post schedule. Defaults to `sncfReaderSchedule`
