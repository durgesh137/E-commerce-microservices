#!/bin/bash

# E-Commerce Microservices Stop Script

echo "================================================"
echo "   Stopping E-Commerce Microservices"
echo "================================================"
echo ""

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Check if PIDs file exists
if [ ! -f .pids ]; then
    echo -e "${YELLOW}No PID file found. Attempting to kill by port...${NC}"

    # Kill processes by port
    for port in 8761 8080 8081 8082 8083 8084; do
        PID=$(lsof -ti :$port)
        if [ ! -z "$PID" ]; then
            echo -e "${YELLOW}Killing process on port $port (PID: $PID)${NC}"
            kill -9 $PID 2>/dev/null
            if [ $? -eq 0 ]; then
                echo -e "${GREEN}✓ Stopped service on port $port${NC}"
            fi
        fi
    done
else
    # Read PIDs from file and kill
    while read pid; do
        if ps -p $pid > /dev/null 2>&1; then
            echo -e "${YELLOW}Killing process $pid${NC}"
            kill -9 $pid 2>/dev/null
            if [ $? -eq 0 ]; then
                echo -e "${GREEN}✓ Stopped process $pid${NC}"
            fi
        fi
    done < .pids

    # Remove PID file
    rm .pids
fi

echo ""
echo -e "${GREEN}All services stopped!${NC}"
echo "================================================"

