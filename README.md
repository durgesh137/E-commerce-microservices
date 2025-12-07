# E-Commerce Microservices Platform

**Spring Boot 4.0 • Spring Cloud 2025.1 • Microservices Architecture**

A production-ready demonstration of microservices patterns with Eureka Discovery, API Gateway, Circuit Breakers, and comprehensive monitoring.

---

## 🚀 Quick Start

```bash
# Clone and build
git clone https://github.com/durgesh137/E-commerce-microservices.git
cd E-commerce-microservices
mvn clean install -DskipTests

# Start all services
./start-services.sh
```

**Access Points:**
- 🌐 API Gateway: http://localhost:8080
- 📡 Eureka Dashboard: http://localhost:8761

**Test the API:**
```bash
curl http://localhost:8080/api/products
```

---

## 🏗️ Architecture

### Services
| Service | Port | Purpose |
|---------|------|---------|
| **Eureka Server** | 8761 | Service Discovery |
| **API Gateway** | 8080 | Single Entry Point |
| **Product Service** | 8084 | Product Catalog |
| **User Service** | 8081 | User Management |
| **Order Service** | 8082 | Order Processing |
| **Inventory Service** | 8083 | Stock Tracking |

### Patterns Implemented
- ✅ Service Discovery (Eureka)
- ✅ API Gateway (Spring Cloud Gateway MVC)
- ✅ Circuit Breaker (Resilience4j)
- ✅ Database per Service (H2)
- ✅ Health Monitoring (Actuator)

📖 **Details:** [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)

---

## 🛠️ Technology Stack

- **Framework:** Spring Boot 4.0.0
- **Cloud:** Spring Cloud 2025.1.0
- **Discovery:** Netflix Eureka
- **Resilience:** Resilience4j
- **Database:** H2 (In-Memory)
- **Build:** Maven 3.8+
- **Java:** 21

---

## 📋 API Examples

```bash
# Get products
curl http://localhost:8080/api/products

# Check inventory
curl http://localhost:8080/api/inventory/check/1/5

# Create order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "shippingAddress": "123 Main St",
    "paymentMethod": "Credit Card",
    "items": [{
      "productId": 1,
      "productName": "Laptop Pro 15",
      "quantity": 1,
      "price": 1299.99
    }]
  }'
```

📋 **Full API Reference:** [docs/API.md](docs/API.md)

---

## 🔧 Development

### Start Services
```bash
./start-services.sh    # Start all
./stop-services.sh     # Stop all
```

### Manual Build
```bash
mvn clean install              # Full build
mvn clean install -DskipTests  # Skip tests
mvn spring-boot:run            # Run individual service
```

### Monitoring
- **Eureka Dashboard:** http://localhost:8761
- **Health Checks:** http://localhost:808X/actuator/health
- **H2 Consoles:** http://localhost:808X/h2-console
  - Username: `sa`, Password: (empty)

🛠️ **Build Guide:** [docs/API.md](docs/API.md#maven-build-commands)

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| **[API.md](docs/API.md)** | API endpoints, testing guide, Maven commands |
| **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** | System design, patterns, database schemas |
| **[TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md)** | Common issues and solutions |

---

## 🐛 Common Issues

```bash
# Port conflict
./stop-services.sh

# Build error
mvn clean install -DskipTests

# Service not registering
# Wait 30s, check http://localhost:8761
```

🔍 **More Help:** [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md)

---

## 📝 Project Structure

```
E-commerce/
├── eureka-server/       # Service Discovery
├── api-gateway/         # API Gateway  
├── product-service/     # Products (8084)
├── user-service/        # Users (8081)
├── order-service/       # Orders (8082)
├── inventory-service/   # Inventory (8083)
├── docs/                # Documentation
└── pom.xml              # Parent POM
```

---

## 🔗 Links

- **GitHub:** https://github.com/durgesh137/E-commerce-microservices
- **Eureka:** http://localhost:8761
- **API Gateway:** http://localhost:8080

---

**Build Time:** ~3-5 min (first) • ~30-60s (subsequent) • **Startup:** ~2-3 min all services

🎉 **Happy Coding!**
