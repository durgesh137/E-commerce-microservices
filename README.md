# E-Commerce Microservices Platform
**Built with ❤️ using Spring Boot and Spring Cloud**

---

This is a demonstration project showcasing microservices architecture best practices.

## 👥 Contributing

This project is created for educational purposes.

## 📄 License

- [ ] Production-ready database (PostgreSQL/MySQL)
- [ ] Integration tests
- [ ] API documentation with Swagger/OpenAPI
- [ ] Kubernetes deployment
- [ ] Docker containerization
- [ ] Message queue integration (Kafka/RabbitMQ)
- [ ] Centralized configuration with Spring Cloud Config
- [ ] Distributed tracing with Zipkin
- [ ] Security with Spring Security & JWT

## 🚧 Future Enhancements

6. **Actuator**: Production-ready monitoring
5. **Circuit Breakers**: Prevent cascading failures
4. **Feign Clients**: Inter-service communication
3. **Separate Databases**: Database per service pattern
2. **Schema.sql & Data.sql**: Explicit schema management with seed data
1. **H2 Database**: In-memory for easy setup and testing

## 🎯 Design Decisions

```
└── pom.xml               # Parent POM
├── user-service/          # User Management
├── inventory-service/      # Inventory Management
├── order-service/          # Order Management
├── product-service/        # Product Management
├── api-gateway/            # API Gateway
├── eureka-server/          # Service Discovery
E-commerce/
```

## 📝 Project Structure

- **Java Version**: 21
- **Build Tool**: Maven
- **Monitoring**: Spring Boot Actuator
- **ORM**: Spring Data JPA
- **Database**: H2 (in-memory)
- **Resilience**: Resilience4j
- **API Gateway**: Spring Cloud Gateway MVC
- **Service Discovery**: Netflix Eureka
- **Cloud**: Spring Cloud 2025.1.0
- **Framework**: Spring Boot 4.0.0

## 🛠️ Technology Stack

Prometheus-compatible metrics available at `/actuator/metrics`
### Metrics

- Eureka registration status
- Database connectivity
- Circuit breaker status
All services expose detailed health information including:
### Health Checks

## 📈 Monitoring

- **Wait Duration**: 1 second between retries
- **Max Attempts**: 3
### Retry

- **Half-Open Calls**: 3 permitted
- **Wait Duration**: 10 seconds in open state
- **Failure Threshold**: 50%
- **Minimum Calls**: 5 before evaluation
- **Sliding Window Size**: 10 calls
### Circuit Breaker

## 🔒 Resilience Configuration

```
  }'
    ]
      }
        "price": 1299.99
        "quantity": 1,
        "productName": "Laptop Pro 15",
        "productId": 1,
      {
    "items": [
    "paymentMethod": "Credit Card",
    "shippingAddress": "123 Main St",
    "userId": 1,
  -d '{
  -H "Content-Type: application/json" \
curl -X POST http://localhost:8080/api/orders \
```bash
3. **Create Order**

```
curl http://localhost:8080/api/inventory/check/1/2
```bash
2. **Check Inventory**

```
curl http://localhost:8080/api/products/1
```bash
1. **Check Product Availability**

### Example: Create an Order Flow

## 🧪 Testing the Application

- **users**: User profiles with roles and contact information
### User Service

- **inventory**: Stock levels, reservations, and warehouse locations
### Inventory Service

- **order_items**: Individual items in each order
- **orders**: Order information with user, status, and shipping details
### Order Service

- **products**: Product catalog with categories, pricing, and status
### Product Service

## 📊 Database Schema

- `http://localhost:808X/actuator/circuitbreakers` - Circuit breaker status
- `http://localhost:808X/actuator/metrics` - Metrics
- `http://localhost:808X/actuator/info` - Service information
- `http://localhost:808X/actuator/health` - Health check
Each service exposes actuator endpoints:
### Actuator Endpoints

**Credentials**: username: `sa`, password: (leave empty)

- User Service: http://localhost:8084/h2-console (jdbc:h2:mem:userdb)
- Inventory Service: http://localhost:8083/h2-console (jdbc:h2:mem:inventorydb)
- Order Service: http://localhost:8082/h2-console (jdbc:h2:mem:orderdb)
- Product Service: http://localhost:8081/h2-console (jdbc:h2:mem:productdb)
Each service has its own H2 console:
### H2 Console Access

- `DELETE /api/users/{id}` - Delete user
- `POST /api/users/{id}/login` - Update last login
- `PUT /api/users/{id}` - Update user
- `POST /api/users` - Create new user
- `GET /api/users/role/{role}` - Get users by role
- `GET /api/users/active` - Get active users
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users` - Get all users
#### User Service

- `DELETE /api/inventory/{id}` - Delete inventory
- `POST /api/inventory/restock/{productId}?quantity={qty}` - Restock
- `PUT /api/inventory/{id}` - Update inventory
- `POST /api/inventory` - Create inventory
- `POST /api/inventory/confirm/{productId}/{quantity}` - Confirm reservation
- `POST /api/inventory/release/{productId}/{quantity}` - Release stock
- `GET /api/inventory/reserve/{productId}/{quantity}` - Reserve stock
- `GET /api/inventory/check/{productId}/{quantity}` - Check availability
- `GET /api/inventory/product/{productId}` - Get inventory by product
- `GET /api/inventory/{id}` - Get inventory by ID
- `GET /api/inventory` - Get all inventory
#### Inventory Service

- `DELETE /api/orders/{id}` - Delete order
- `PATCH /api/orders/{id}/status?status={status}` - Update order status
- `POST /api/orders` - Create new order
- `GET /api/orders/status/{status}` - Get orders by status
- `GET /api/orders/user/{userId}` - Get orders by user
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders` - Get all orders
#### Order Service

- `DELETE /api/products/{id}` - Delete product
- `PUT /api/products/{id}` - Update product
- `POST /api/products` - Create new product
- `GET /api/products/search?name={name}` - Search products
- `GET /api/products/category/{category}` - Get products by category
- `GET /api/products/active` - Get active products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products` - Get all products
#### Product Service

All requests go through the gateway at http://localhost:8080
### API Gateway Routes

- View all registered services and their health status
- **URL**: http://localhost:8761
### Eureka Dashboard

## 🌐 Service Endpoints

```
mvn spring-boot:run
cd user-service
# Terminal 4

mvn spring-boot:run
cd inventory-service
# Terminal 3

mvn spring-boot:run
cd order-service
# Terminal 2

mvn spring-boot:run
cd product-service
# Terminal 1
```bash
3. **Start Microservices** (Can be started in parallel)

```
mvn spring-boot:run
cd api-gateway
```bash
2. **Start API Gateway**

Wait until Eureka dashboard is available at http://localhost:8761
```
mvn spring-boot:run
cd eureka-server
```bash
1. **Start Eureka Server** (Must start first)

### Start Services in Order

```
mvn clean install
cd E-commerce
```bash
### Build All Services

## 🔧 Build & Run

- IDE (IntelliJ IDEA, Eclipse, or VS Code)
- Maven 3.8+
- Java 21

## 📋 Prerequisites

- Request timeouts and bulkhead patterns
- Health indicators for circuit breaker status
- Fallback methods for graceful degradation
- Automatic retry with configurable attempts
- Circuit breaker pattern with configurable thresholds
### Resilience Features

- **Transaction Management**: Spring @Transactional annotations
- **Exception Handling**: Proper error handling with fallback methods
- **Logging**: Structured logging with SLF4J
- **Validation**: Bean validation with Jakarta Validation API
- **Seed Data**: Pre-populated data for testing
- **Schema Management**: DDL scripts with proper indexing and constraints
- **Database Per Service**: Each microservice has its own H2 database
### Best Practices

- **Distributed Tracing**: Ready for integration with Zipkin/Sleuth
- **Health Monitoring**: Spring Boot Actuator with detailed health checks
- **Retry Mechanism**: Automatic retry with exponential backoff
- **Circuit Breaker**: Resilience4j for fault tolerance
- **API Gateway**: Centralized routing and load balancing
- **Service Discovery**: Eureka for dynamic service registration and discovery
### Enterprise Patterns

## 🚀 Key Features

6. **User Service** (Port 8084) - User management and authentication
5. **Inventory Service** (Port 8083) - Stock and inventory management
4. **Order Service** (Port 8082) - Order processing and management
3. **Product Service** (Port 8081) - Product catalog management
2. **API Gateway** (Port 8080) - Entry point for all client requests
1. **Eureka Server** (Port 8761) - Service Discovery
### Microservices

This project implements a microservices architecture with the following components:

## 🏗️ Architecture Overview

A comprehensive mini e-commerce backend built with Spring Boot microservices architecture, featuring Eureka service discovery, API Gateway, and resilience patterns.


