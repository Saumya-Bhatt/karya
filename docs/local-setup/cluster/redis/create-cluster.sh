#!/bin/bash

COMPOSE_FILE_PATH="$(pwd)/docs/local-setup/cluster/redis.docker-compose.yml"

echo "COMPOSE_FILE_PATH: $COMPOSE_FILE_PATH"

if [ -z "$LOCAL_IP" ]; then
  echo "Error: LOCAL_IP is not set. Could not determine local IP address."
  exit 1
fi

echo "Using LOCAL_IP: $LOCAL_IP"

# Run the redis-cli command inside the redis-node-1 container
docker-compose -f $COMPOSE_FILE_PATH exec -T redis-node-1 redis-cli -a 'karya' \
  --cluster create $LOCAL_IP:7001 $LOCAL_IP:7002 $LOCAL_IP:7003 \
  --cluster-replicas 0 \
  --cluster-yes
