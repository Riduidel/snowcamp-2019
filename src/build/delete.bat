REM immediatly delete cluster and all contained data
gcloud container clusters delete snowcamp-2019 --quiet --zone=europe-west1-d

REM delete the reader app
curl -X DELETE -H "Authorization: token %GIT_ACCESS_TOKEN%" https://api.github.com/repos/Riduidel/snowcamp-2019-sncf-timesheet-reader
curl -X DELETE -H "Authorization: token %GIT_ACCESS_TOKEN%" https://api.github.com/repos/Zenika/snowcamp-2019-sncf-timesheet-reader

REM delete the environments