#!/usr/bin/env bash
# Deploy maven artifact in current directory into Maven central repository
# using maven-release-plugin goals

read -p "Really deploy to maven central repository  (yes/no)? "

if [ "$REPLY" == "yes" ]; then
  mvn clean deploy -P ossrh | tee maven-central-deploy.log
else
  echo 'Exit without deploy'
fi
