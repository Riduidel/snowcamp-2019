#!/bin/bash
set -ex
for filename in backup/*
do
	id=$(cat $filename | jq -r .id)
	curl -XPUT -H 'content-type: application/json' http://elastic.jx.35.233.117.189.nip.io/timesheets_backup/route_schedules/$id --data @$filename
done