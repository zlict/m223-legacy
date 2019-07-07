#!/bin/sh

TIMEOUT=15
QUIET=0

HOST=$DB_HOST
PORT=$DB_PORT

echo Waiting for $HOST:$PORT...
echo and runnign "$@"

for i in `seq $TIMEOUT` ; do
  nc -z "$HOST" "$PORT" > /dev/null 2>&1

  result=$?
  if [ $result -eq 0 ] ; then
    if [ $# -gt 0 ] ; then
      exec "$@"
    fi
    exit 0
  fi
  sleep 1
done
echo "Operation timed out" >&2
exit 1
