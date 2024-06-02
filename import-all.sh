#!/usr/bin/env bash

set -e

DATABASE=kbo

mysql -uroot ${DATABASE} < clean.sql

for sql_script in ?-*.sql
do
  echo "$sql_script"
  # Parallelization cuts the loading time in half:
  mysql  --local-infile=1 -uroot ${DATABASE} < "$sql_script" &
done

wait # until everything is loaded before creating the foreign key constraints:
echo "fk.sql"
mysql -uroot ${DATABASE} < fk.sql
