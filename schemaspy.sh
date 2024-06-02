#!/usr/bin/env sh

exec java \
  -jar ~/tools/schemaspy.jar \
  -configFile schemaspy.properties \
  -dp ~/.m2/repository/com/mysql/mysql-connector-j/8.0.32/mysql-connector-j-8.0.32.jar
