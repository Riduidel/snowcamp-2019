#!/bin/bash
set -ex
for filename in backup/*
do
	id=$(cat $filename | jq -r .id)
	curl -XPUT -H 'content-type: application/json' http://elastic.jx.34.76.21.136.nip.io/timesheets_backup/route_schedules/$id --data @$filename
done