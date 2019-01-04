mkdir reader-updated && cd reader-updated

git clone git@github.com:Riduidel/snowcamp-2019-sncf-timesheet-reader.git
cd snowcamp-2019-sncf-timesheet-reader
git remote add reference git@github.com:Riduidel/snowcamp-2019-sncf-timesheet-reader-reference.git
git fetch --all
git checkout -b PR-1-working-version
git cherry-pick 159980e 
git cherry-pick 69537e7
git cherry-pick 6bf426f