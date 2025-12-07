# API Documentation & Build Guide

Complete reference for API endpoints, testing, and Maven build commands.

**📖 Also See:**
- [README.md](../README.md) - Project overview and quick start
- [ARCHITECTURE.md](ARCHITECTURE.md) - Architecture and design patterns
- [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - Common issues and solutions

---

## 📋 Table of Contents

1. [Maven Build Commands](#maven-build-commands)
2. [API Testing Guide](#api-testing-guide)
3. [Service Endpoints](#service-endpoints)
4. [Monitoring & Health Checks](#monitoring--health-checks)

---

## Maven Build Commands

### 🔨 Essential Build Commands

#### Basic Build
```bash
# Build all services (from project root)
mvn clean install

# Build without tests (faster)
mvn clean install -DskipTests

# Build offline (after first build)
mvn clean install -o
```

#### Individual Service Build
```bash
# Build specific service
cd product-service
mvn clean package

# Build from root for specific module
mvn clean package -pl product-service

# Build multiple modules
mvn clean package -pl eureka-server,api-gateway
```

#### Maven Wrapper (No Maven Install Required)
```bash
./mvnw clean install
./mvnw clean install -DskipTests
```

### ▶️ Running Services

#### Using Shell Script (Recommended)
```bash
# Start all services
./start-services.sh

# Stop all services
./stop-services.sh

# View logs
tail -f logs/product-service.log
```

#### Using Maven
```bash
# Run from service directory
cd product-service
mvn spring-boot:run

# Run from root
mvn spring-boot:run -pl product-service
```

#### Using JAR Files
```bash
# After mvn clean install
java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
java -jar product-service/target/product-service-0.0.1-SNAPSHOT.jar
```

### 🔍 Verification Commands

#### Check Build Success
```bash
# List all generated JARs
find . -name "*.jar" -path "*/target/*" | grep -v "original"

# Expected: 6 JAR files
# - eureka-server-0.0.1-SNAPSHOT.jar
# - api-gateway-0.0.1-SNAPSHOT.jar
# - product-service-0.0.1-SNAPSHOT.jar
# - order-service-0.0.1-SNAPSHOT.jar
# - inventory-service-0.0.1-SNAPSHOT.jar
# - user-service-0.0.1-SNAPSHOT.jar
```

#### Check Running Services
```bash
# List Java processes
jps -l | grep "com.example"

# Check specific port
lsof -i :8080  # API Gateway
lsof -i :8761  # Eureka Server

# Health checks
curl http://localhost:8761/actuator/health
curl http://localhost:8080/actuator/health
```

### 🧹 Cleanup Commands

```bash
# Remove all target directories
mvn clean

# Deep clean (including IDE files)
mvn clean
rm -rf .idea/ **/*.iml

# Stop all services
./stop-services.sh
# or
pkill -f "spring-boot"
```

### 🚀 Advanced Maven Commands

#### Dependency Management
```bash
# Show dependency tree
mvn dependency:tree

# Show dependencies for specific module
mvn dependency:tree -pl product-service

# Update dependencies
mvn versions:display-dependency-updates
```

#### Multi-Module Tips
```bash
# Build only changed modules
mvn install -pl product-service -am

# Build module and dependents
mvn install -pl product-service -amd

# Resume from specific module
mvn install -rf :product-service
```

**⏱️ Build Time Expectations:**
- First build: ~3-5 minutes (downloading dependencies)
- Subsequent builds: ~30-60 seconds
- Clean build: ~1-2 minutes
- With tests: +30-60 seconds per module

---

## API Testing Guide

### 🌐 Base URL

All requests go through API Gateway: `http://localhost:8080`

### Quick Test Commands

#### 1. Check Eureka Service Registration
```bash
curl http://localhost:8761/eureka/apps
```

#### 2. Product Service Tests

##### Get All Products
```bash
curl http://localhost:8080/api/products
```

##### Get Product by ID
```bash
curl http://localhost:8080/api/products/1
```

##### Get Active Products
```bash
curl http://localhost:8080/api/products/active
```

##### Get Products by Category
```bash
curl http://localhost:8080/api/products/category/Electronics
```

##### Search Products
```bash
curl http://localhost:8080/api/products/search?name=laptop
```

##### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smart Watch",
    "description": "Fitness tracking smart watch",
    "price": 199.99,
    "category": "Electronics",
    "imageUrl": "https://example.com/watch.jpg",
    "active": true
  }'
```

##### Update Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Pro 15 Updated",
    "description": "Updated description",
    "price": 1199.99,
    "category": "Electronics",
    "imageUrl": "https://example.com/laptop-new.jpg",
    "active": true
  }'
```

#### 3. User Service Tests

##### Get All Users
```bash
curl http://localhost:8080/api/users
```

##### Get User by ID
```bash
curl http://localhost:8080/api/users/1
```

##### Get User by Username
```bash
curl http://localhost:8080/api/users/username/john.doe
```

##### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test.user",
    "email": "test.user@example.com",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "5559998888",
    "address": "123 Test St",
    "city": "Test City",
    "state": "TS",
    "postalCode": "12345",
    "country": "USA",
    "role": "CUSTOMER",
    "active": true
  }'
```

#### 4. Inventory Service Tests

##### Get All Inventory
```bash
curl http://localhost:8080/api/inventory
```

##### Get Inventory by Product ID
```bash
curl http://localhost:8080/api/inventory/product/1
```

##### Check Availability
```bash
curl http://localhost:8080/api/inventory/check/1/5
```

##### Reserve Stock
```bash
curl http://localhost:8080/api/inventory/reserve/1/2
```

##### Release Stock
```bash
curl -X POST http://localhost:8080/api/inventory/release/1/2
```

##### Restock Inventory
```bash
curl -X POST "http://localhost:8080/api/inventory/restock/1?quantity=50"
```

#### 5. Order Service Tests

##### Get All Orders
```bash
curl http://localhost:8080/api/orders
```

##### Get Order by ID
```bash
curl http://localhost:8080/api/orders/1
```

##### Get Orders by User
```bash
curl http://localhost:8080/api/orders/user/1
```

##### Get Orders by Status
```bash
curl http://localhost:8080/api/orders/status/PENDING
```

##### Create Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "shippingAddress": "123 Main St, New York, NY 10001",
    "paymentMethod": "Credit Card",
    "items": [
      {
        "productId": 2,
        "productName": "Wireless Mouse",
        "quantity": 2,
        "price": 29.99
      },
      {
        "productId": 3,
        "productName": "Mechanical Keyboard",
        "quantity": 1,
        "price": 149.99
      }
    ]
  }'
```

##### Update Order Status
```bash
curl -X PATCH "http://localhost:8080/api/orders/1/status?status=SHIPPED"
```

### 🔄 Complete Order Flow Test

```bash
# Step 1: Create a user
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "order.test",
    "email": "order.test@example.com",
    "firstName": "Order",
    "lastName": "Test",
    "phoneNumber": "5551112222",
    "role": "CUSTOMER",
    "active": true
  }')
echo "Created User: $USER_RESPONSE"

# Step 2: Check product availability
curl http://localhost:8080/api/products/1

# Step 3: Check inventory
curl http://localhost:8080/api/inventory/check/1/1

# Step 4: Create order
ORDER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "shippingAddress": "123 Test St, Test City, TS 12345",
    "paymentMethod": "Credit Card",
    "items": [
      {
        "productId": 1,
        "productName": "Laptop Pro 15",
        "quantity": 1,
        "price": 1299.99
      }
    ]
  }')
echo "Created Order: $ORDER_RESPONSE"

# Step 5: Check inventory after order
curl http://localhost:8080/api/inventory/product/1
```

### 📊 Load Testing Script

```bash
#!/bin/bash
# Simple load test - fetch products 100 times
for i in {1..100}; do
  curl -s http://localhost:8080/api/products > /dev/null
  echo "Request $i completed"
done
```

---

## Service Endpoints

### Product Service API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/active` | Get active products |
| GET | `/api/products/category/{category}` | Get by category |
| GET | `/api/products/search?name={name}` | Search products |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

### Order Service API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/user/{userId}` | Get orders by user |
| GET | `/api/orders/status/{status}` | Get orders by status |
| POST | `/api/orders` | Create order |
| PATCH | `/api/orders/{id}/status?status={status}` | Update status |
| DELETE | `/api/orders/{id}` | Delete order |

### Inventory Service API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory` | Get all inventory |
| GET | `/api/inventory/{id}` | Get by ID |
| GET | `/api/inventory/product/{productId}` | Get by product |
| GET | `/api/inventory/check/{productId}/{quantity}` | Check availability |
| GET | `/api/inventory/reserve/{productId}/{quantity}` | Reserve stock |
| POST | `/api/inventory` | Create inventory |
| POST | `/api/inventory/release/{productId}/{quantity}` | Release stock |
| POST | `/api/inventory/confirm/{productId}/{quantity}` | Confirm reservation |
| POST | `/api/inventory/restock/{productId}?quantity={qty}` | Restock |
| PUT | `/api/inventory/{id}` | Update inventory |
| DELETE | `/api/inventory/{id}` | Delete inventory |

### User Service API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get by username |
| GET | `/api/users/email/{email}` | Get by email |
| GET | `/api/users/active` | Get active users |
| GET | `/api/users/role/{role}` | Get users by role |
| POST | `/api/users` | Create user |
| POST | `/api/users/{id}/login` | Update last login |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

---

## Monitoring & Health Checks

### Health Endpoints

#### Check All Service Health
```bash
# Eureka
curl http://localhost:8761/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health

# Product Service
curl http://localhost:8081/actuator/health

# Order Service
curl http://localhost:8082/actuator/health

# Inventory Service
curl http://localhost:8083/actuator/health

# User Service
curl http://localhost:8084/actuator/health
```

#### Check Circuit Breaker Status
```bash
# Product Service
curl http://localhost:8081/actuator/circuitbreakers

# Order Service
curl http://localhost:8082/actuator/circuitbreakers

# Inventory Service
curl http://localhost:8083/actuator/circuitbreakers

# User Service
curl http://localhost:8084/actuator/circuitbreakers
```

### Metrics

#### View Metrics
```bash
# Product Service metrics
curl http://localhost:8081/actuator/metrics

# Specific metric
curl http://localhost:8081/actuator/metrics/jvm.memory.used
```

#### Prometheus Format
```bash
curl http://localhost:8081/actuator/prometheus
```

### Testing Circuit Breaker

#### Simulate Service Failure
1. Stop a service (e.g., Product Service)
2. Make requests through API Gateway
3. Circuit breaker should open and fallback methods should be called
4. Restart the service
5. Circuit breaker should automatically close after half-open state testing

```bash
# While product service is down
curl http://localhost:8080/api/products
# Should return empty array from fallback method

# Check circuit breaker status
curl http://localhost:8082/actuator/circuitbreakers
```

### Database Access

#### H2 Console
- **Product Service**: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:productdb`
- **Order Service**: http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:orderdb`
- **Inventory Service**: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:inventorydb`
- **User Service**: http://localhost:8084/h2-console
  - JDBC URL: `jdbc:h2:mem:userdb`

**Credentials**: Username: `sa`, Password: (empty)

### Eureka Dashboard

Access at http://localhost:8761 to view:
- All registered services
- Service instance status
- Health checks
- Metadata

---

## Notes

- All requests go through API Gateway (port 8080)
- Direct service access is also possible on their respective ports
- H2 Console is available on each service at `/h2-console`
- Eureka Dashboard shows all registered services at http://localhost:8761

---

**🔗 Related Documentation:**
- [Back to README](../README.md)
- [Architecture Details](ARCHITECTURE.md)
- [Troubleshooting Guide](TROUBLESHOOTING.md)
