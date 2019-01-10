SET SCRIPT_PATH=%~dp0
SET CONFERENCE_HOME=%SCRIPT_PATH%..\..\
SET TIMESHEET_HOME=%CONFERENCE_HOME%..\snowcamp-2019-sncf-timesheet-reader\
mkdir %TIMESHEET_HOME%
pushd %TIMESHEET_HOME%

echo now in directory %cd%
git clone git@github.com:Riduidel/snowcamp-2019-sncf-timesheet-reader.git %TIMESHEET_HOME%
cd %TIMESHEET_HOME%
git -C %TIMESHEET_HOME% checkout -b PR-1-working-version
robocopy %SCRIPT_PATH%reference\sncf-reader %TIMESHEET_HOME% /mir /xf /xx
git -C %TIMESHEET_HOME% add --all
git -C %TIMESHEET_HOME% commit -m "A working version !"
git -C %TIMESHEET_HOME% push -u origin PR-1-working-version
popd