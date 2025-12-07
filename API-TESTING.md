# E-Commerce Microservices API Testing Guide
- Eureka Dashboard shows all registered services at http://localhost:8761
- H2 Console is available on each service at /h2-console
- Direct service access is also possible on their respective ports
- All requests go through API Gateway (port 8080)
## Notes

```
curl http://localhost:8081/actuator/prometheus
```bash
### Prometheus Format

```
curl http://localhost:8081/actuator/metrics/jvm.memory.used
# Specific metric

curl http://localhost:8081/actuator/metrics
# Product Service metrics
```bash
### View Metrics

## Metrics and Monitoring

```
curl http://localhost:8081/actuator/circuitbreakers
# Check circuit breaker status

# Should return empty array from fallback method
curl http://localhost:8080/api/products
# While product service is down
```bash

5. Circuit breaker should automatically close after half-open state testing
4. Restart the service
3. Circuit breaker should open and fallback methods should be called
2. Make requests through API Gateway
1. Stop a service (e.g., Product Service)
### Simulate Service Failure

## Testing Circuit Breaker

```
done
  echo "Request $i completed"
  curl -s http://localhost:8080/api/products > /dev/null
for i in {1..100}; do
# Simple load test - fetch products 100 times
#!/bin/bash
```bash

### 8. Load Testing Script

```
curl http://localhost:8080/api/inventory/product/1
# Step 5: Check inventory after order

echo "Created Order: $ORDER_RESPONSE"
  }')
    ]
      }
        "price": 1299.99
        "quantity": 1,
        "productName": "Laptop Pro 15",
        "productId": 1,
      {
    "items": [
    "paymentMethod": "Credit Card",
    "shippingAddress": "123 Test St, Test City, TS 12345",
    "userId": 1,
  -d '{
  -H "Content-Type: application/json" \
ORDER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/orders \
# Step 4: Create order

curl http://localhost:8080/api/inventory/check/1/1
# Step 3: Check inventory

curl http://localhost:8080/api/products/1
# Step 2: Check product availability

echo "Created User: $USER_RESPONSE"
  }')
    "active": true
    "role": "CUSTOMER",
    "phoneNumber": "5551112222",
    "lastName": "Test",
    "firstName": "Order",
    "email": "order.test@example.com",
    "username": "order.test",
  -d '{
  -H "Content-Type: application/json" \
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users \
# Step 1: Create a user
```bash

### 7. Complete Order Flow Test

```
curl http://localhost:8084/actuator/circuitbreakers
# User Service

curl http://localhost:8083/actuator/circuitbreakers
# Inventory Service

curl http://localhost:8082/actuator/circuitbreakers
# Order Service

curl http://localhost:8081/actuator/circuitbreakers
# Product Service
```bash
#### Check Circuit Breaker Status

```
curl http://localhost:8084/actuator/health
# User Service

curl http://localhost:8083/actuator/health
# Inventory Service

curl http://localhost:8082/actuator/health
# Order Service

curl http://localhost:8081/actuator/health
# Product Service

curl http://localhost:8080/actuator/health
# API Gateway

curl http://localhost:8761/actuator/health
# Eureka
```bash
#### Check All Service Health

### 6. Health Checks

```
curl -X PATCH "http://localhost:8080/api/orders/1/status?status=SHIPPED"
```bash
#### Update Order Status

```
  }'
    ]
      }
        "price": 149.99
        "quantity": 1,
        "productName": "Mechanical Keyboard",
        "productId": 3,
      {
      },
        "price": 29.99
        "quantity": 2,
        "productName": "Wireless Mouse",
        "productId": 2,
      {
    "items": [
    "paymentMethod": "Credit Card",
    "shippingAddress": "123 Main St, New York, NY 10001",
    "userId": 1,
  -d '{
  -H "Content-Type: application/json" \
curl -X POST http://localhost:8080/api/orders \
```bash
#### Create Order

```
curl http://localhost:8080/api/orders/status/PENDING
```bash
#### Get Orders by Status

```
curl http://localhost:8080/api/orders/user/1
```bash
#### Get Orders by User

```
curl http://localhost:8080/api/orders/1
```bash
#### Get Order by ID

```
curl http://localhost:8080/api/orders
```bash
#### Get All Orders

### 5. Order Service Tests

```
curl -X POST "http://localhost:8080/api/inventory/restock/1?quantity=50"
```bash
#### Restock Inventory

```
curl -X POST http://localhost:8080/api/inventory/release/1/2
```bash
#### Release Stock

```
curl http://localhost:8080/api/inventory/reserve/1/2
```bash
#### Reserve Stock

```
curl http://localhost:8080/api/inventory/check/1/5
```bash
#### Check Availability

```
curl http://localhost:8080/api/inventory/product/1
```bash
#### Get Inventory by Product ID

```
curl http://localhost:8080/api/inventory
```bash
#### Get All Inventory

### 4. Inventory Service Tests

```
  }'
    "active": true
    "role": "CUSTOMER",
    "country": "USA",
    "postalCode": "12345",
    "state": "TS",
    "city": "Test City",
    "address": "123 Test St",
    "phoneNumber": "5559998888",
    "lastName": "User",
    "firstName": "Test",
    "email": "test.user@example.com",
    "username": "test.user",
  -d '{
  -H "Content-Type: application/json" \
curl -X POST http://localhost:8080/api/users \
```bash
#### Create User

```
curl http://localhost:8080/api/users/active
```bash
#### Get Active Users

```
curl http://localhost:8080/api/users/username/john.doe
```bash
#### Get User by Username

```
curl http://localhost:8080/api/users/1
```bash
#### Get User by ID

```
curl http://localhost:8080/api/users
```bash
#### Get All Users

### 3. User Service Tests

```
  }'
    "active": true
    "imageUrl": "https://example.com/laptop-new.jpg",
    "category": "Electronics",
    "price": 1199.99,
    "description": "Updated description",
    "name": "Laptop Pro 15 Updated",
  -d '{
  -H "Content-Type: application/json" \
curl -X PUT http://localhost:8080/api/products/1 \
```bash
#### Update Product

```
  }'
    "active": true
    "imageUrl": "https://example.com/watch.jpg",
    "category": "Electronics",
    "price": 199.99,
    "description": "Fitness tracking smart watch",
    "name": "Smart Watch",
  -d '{
  -H "Content-Type: application/json" \
curl -X POST http://localhost:8080/api/products \
```bash
#### Create Product

```
curl http://localhost:8080/api/products/search?name=laptop
```bash
#### Search Products

```
curl http://localhost:8080/api/products/category/Electronics
```bash
#### Get Products by Category

```
curl http://localhost:8080/api/products/active
```bash
#### Get Active Products

```
curl http://localhost:8080/api/products/1
```bash
#### Get Product by ID

```
curl http://localhost:8080/api/products
```bash
#### Get All Products

### 2. Product Service Tests

```
curl http://localhost:8761/eureka/apps
```bash
### 1. Check Eureka Service Registration

## Quick Test Commands


