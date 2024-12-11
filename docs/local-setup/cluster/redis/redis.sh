#!/bin/bash
set -e

# Check if required arguments are provided
if [ $# -ne 2 ]; then
    echo "Usage: $0 <ANNOUNCE_IP> <ANNOUNCE_PORT>"
    exit 1
fi

ANNOUNCE_IP=$1
ANNOUNCE_PORT=$2
ANNOUNCE_BUS_PORT=$((ANNOUNCE_PORT + 100))

CONF_FILE="/tmp/redis.conf"

# Ensure the file is clean before writing
> "$CONF_FILE"

echo "Configuring Redis with:"
echo "  IP: $ANNOUNCE_IP"
echo "  Port: $ANNOUNCE_PORT"
echo "  Bus Port: $ANNOUNCE_BUS_PORT"

# Write configuration to file
cat > "$CONF_FILE" << EOF
port 6379
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
loglevel debug
requirepass karya
masterauth karya
protected-mode no
maxclients 10000
cluster-announce-ip $ANNOUNCE_IP
cluster-announce-port $ANNOUNCE_PORT
cluster-announce-bus-port $ANNOUNCE_BUS_PORT
EOF

# Ensure script is executable
chmod +x "$CONF_FILE"

# Start Redis server
exec redis-server "$CONF_FILE"