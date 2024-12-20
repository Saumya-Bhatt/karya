#!/usr/bin/env bash

MEMORY_XMS="${MEMORY_XMS:-512m}"
MEMORY_XMX="${MEMORY_XMX:-1536m}"

function _log {
  echo "$(date +"%F %T") ${SELF_NAME} $1"
}

_log "starting the scheduler"

java -Xms"$MEMORY_XMS" -Xmx"$MEMORY_XMX" \
  -jar app.jar
