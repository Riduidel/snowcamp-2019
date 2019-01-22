#!/bin/bash
set -ex
for filename in backup/*
do
	id=$(cat $filename | jq -r .id)
	curl -XPUT -H 'content-type: application/json' localhost:18180/timesheets_backup/route_schedules/$id --data @$filename
done