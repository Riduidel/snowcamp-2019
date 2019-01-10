set RESIZER_CREDENTIAL=C:\Users\nicolas-delsaux\snowcamp-2019-gke-resizer.json
set RESIZER_PROJECT=snowcamp-2019-223915
set RESIZER_ZONE=europe-west1-d
set RESIZER_CLUSTER=snowcamp-2019
set RESIZER_TARGETPOOL=default-pool:0
set RESIZER_RESETPOOL=false


docker run -ti -v %RESIZER_CREDENTIAL%:/config/cluster.json -e RESIZER_PROJECT=snowcamp-2019-223915 -e RESIZER_ZONE=europe-west1-d -e RESIZER_CLUSTER=snowcamp-2019 -e RESIZER_TARGETPOOL=default-pool:0 -e RESIZER_RESETPOOL=false snahelou/gke-resizer