#!/bin/bash

# E-Commerce Microservices Startup Script
# This script starts all microservices in the correct order

echo "================================================"
echo "   E-Commerce Microservices Startup Script"
echo "================================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        return 0
    else
        return 1
    fi
}

# Function to wait for service to be ready
wait_for_service() {
    local port=$1
    local service_name=$2
    local max_attempts=30
    local attempt=0

    echo -e "${YELLOW}Waiting for $service_name to start on port $port...${NC}"

    while [ $attempt -lt $max_attempts ]; do
        if check_port $port; then
            echo -e "${GREEN}✓ $service_name is ready!${NC}"
            sleep 5  # Additional wait for service to fully initialize
            return 0
        fi
        attempt=$((attempt + 1))
        sleep 2
    done

    echo -e "${RED}✗ $service_name failed to start within timeout${NC}"
    return 1
}

# Build all services
echo -e "${YELLOW}Building all services...${NC}"
mvn clean install -DskipTests
if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Build failed!${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Build successful!${NC}"
echo ""

# Start Eureka Server
echo -e "${YELLOW}Starting Eureka Server...${NC}"
cd eureka-server
mvn spring-boot:run > ../logs/eureka-server.log 2>&1 &
EUREKA_PID=$!
cd ..

if ! wait_for_service 8761 "Eureka Server"; then
    echo -e "${RED}Failed to start Eureka Server${NC}"
    exit 1
fi
echo ""

# Start API Gateway
echo -e "${YELLOW}Starting API Gateway...${NC}"
cd api-gateway
mvn spring-boot:run > ../logs/api-gateway.log 2>&1 &
API_GATEWAY_PID=$!
cd ..

if ! wait_for_service 8080 "API Gateway"; then
    echo -e "${RED}Failed to start API Gateway${NC}"
    exit 1
fi
echo ""

# Start Product Service
echo -e "${YELLOW}Starting Product Service...${NC}"
cd product-service
mvn spring-boot:run > ../logs/product-service.log 2>&1 &
PRODUCT_PID=$!
cd ..

if ! wait_for_service 8081 "Product Service"; then
    echo -e "${RED}Failed to start Product Service${NC}"
    exit 1
fi
echo ""

# Start Inventory Service
echo -e "${YELLOW}Starting Inventory Service...${NC}"
cd inventory-service
mvn spring-boot:run > ../logs/inventory-service.log 2>&1 &
INVENTORY_PID=$!
cd ..

if ! wait_for_service 8083 "Inventory Service"; then
    echo -e "${RED}Failed to start Inventory Service${NC}"
    exit 1
fi
echo ""

# Start Order Service
echo -e "${YELLOW}Starting Order Service...${NC}"
cd order-service
mvn spring-boot:run > ../logs/order-service.log 2>&1 &
ORDER_PID=$!
cd ..

if ! wait_for_service 8082 "Order Service"; then
    echo -e "${RED}Failed to start Order Service${NC}"
    exit 1
fi
echo ""

# Start User Service
echo -e "${YELLOW}Starting User Service...${NC}"
cd user-service
mvn spring-boot:run > ../logs/user-service.log 2>&1 &
USER_PID=$!
cd ..

if ! wait_for_service 8084 "User Service"; then
    echo -e "${RED}Failed to start User Service${NC}"
    exit 1
fi
echo ""

echo "================================================"
echo -e "${GREEN}   All services started successfully!${NC}"
echo "================================================"
echo ""
echo "Service URLs:"
echo "  • Eureka Dashboard: http://localhost:8761"
echo "  • API Gateway:      http://localhost:8080"
echo "  • Product Service:  http://localhost:8081"
echo "  • Order Service:    http://localhost:8082"
echo "  • Inventory Service: http://localhost:8083"
echo "  • User Service:     http://localhost:8084"
echo ""
echo "PIDs:"
echo "  • Eureka Server:     $EUREKA_PID"
echo "  • API Gateway:       $API_GATEWAY_PID"
echo "  • Product Service:   $PRODUCT_PID"
echo "  • Order Service:     $ORDER_PID"
echo "  • Inventory Service: $INVENTORY_PID"
echo "  • User Service:      $USER_PID"
echo ""
echo "To stop all services, run: ./stop-services.sh"
echo ""
echo "Logs are available in the logs/ directory"
echo "================================================"

# Save PIDs to file for stop script
echo $EUREKA_PID > .pids
echo $API_GATEWAY_PID >> .pids
echo $PRODUCT_PID >> .pids
echo $ORDER_PID >> .pids
echo $INVENTORY_PID >> .pids
echo $USER_PID >> .pids

# Wait for user interrupt
echo ""
echo -e "${YELLOW}Press Ctrl+C to stop all services${NC}"
wait

