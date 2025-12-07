# Architecture Documentation

## System Architecture

```
                                    ┌─────────────────┐
                                    │  Eureka Server  │
                                    │   Port: 8761    │
                                    └────────┬────────┘
                                             │
                    ┌────────────────────────┼────────────────────────┐
                    │                        │                        │
                    ▼                        ▼                        ▼
         ┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
         │  Product Service │    │   Order Service  │    │ Inventory Service│
         │   Port: 8081     │    │   Port: 8082     │    │   Port: 8083     │
         └──────────────────┘    └──────────────────┘    └──────────────────┘
                    │                        │                        │
                    └────────────────────────┼────────────────────────┘
                                             │
                                             ▼
                                    ┌─────────────────┐
                                    │   API Gateway   │
                                    │   Port: 8080    │
                                    └────────┬────────┘
                                             │
                                             ▼
                                      [Client Requests]
```

## Service Communication Flow

### Order Creation Flow
```
Client → API Gateway → Order Service → Inventory Service (Check Stock)
                                    ↓
                              Reserve Stock
                                    ↓
                              Create Order
                                    ↓
                              Return Response
```

## Database Schema

### Product Service (productdb)
```sql
products
├── id (PK)
├── name
├── description
├── price
├── category
├── image_url
├── active
├── created_at
└── updated_at
```

### Order Service (orderdb)
```sql
orders                          order_items
├── id (PK)                    ├── id (PK)
├── user_id                    ├── order_id (FK)
├── total_amount               ├── product_id
├── status                     ├── product_name
├── shipping_address           ├── quantity
├── payment_method             ├── price
├── created_at                 └── subtotal
└── updated_at
```

### Inventory Service (inventorydb)
```sql
inventory
├── id (PK)
├── product_id (UNIQUE)
├── quantity
├── reserved_quantity
├── warehouse_location
├── reorder_level
├── last_restocked
├── created_at
└── updated_at
```

### User Service (userdb)
```sql
users
├── id (PK)
├── username (UNIQUE)
├── email (UNIQUE)
├── first_name
├── last_name
├── phone_number
├── address
├── city
├── state
├── postal_code
├── country
├── role
├── active
├── created_at
├── updated_at
└── last_login
```

## Resilience Patterns

### Circuit Breaker Configuration
- **Pattern**: Fail-fast with fallback
- **Window Size**: 10 calls
- **Failure Threshold**: 50%
- **Wait Duration**: 10 seconds
- **Half-Open State**: 3 test calls

### Retry Pattern
- **Max Attempts**: 3
- **Wait Duration**: 1 second
- **Exponential Backoff**: Not configured (can be added)

## Best Practices Implemented

### 1. Database Per Service
Each microservice has its own database, ensuring loose coupling and independent scalability.

### 2. API Gateway Pattern
Single entry point for all client requests with:
- Load balancing
- Routing
- Security (ready for implementation)
- Rate limiting (ready for implementation)

### 3. Service Discovery
Dynamic service registration and discovery using Eureka:
- Automatic health checks
- Load balancing
- Failover

### 4. Fault Tolerance
- Circuit breaker for preventing cascading failures
- Retry mechanism for transient failures
- Fallback methods for graceful degradation

### 5. Observability
- Health endpoints
- Metrics endpoints
- Circuit breaker status
- Custom application metrics

### 6. Data Management
- Schema versioning with SQL scripts
- Seed data for testing
- Proper indexing
- Constraints and validations

### 7. Code Quality
- Lombok for reducing boilerplate
- Bean validation
- Proper exception handling
- Structured logging
- Transaction management

## Scaling Considerations

### Horizontal Scaling
All services can be scaled horizontally:
```bash
# Start multiple instances of Product Service
java -jar product-service.jar --server.port=8091
java -jar product-service.jar --server.port=8092
```

Eureka will automatically load balance between instances.

### Database Scaling
Currently using H2 in-memory. For production:
- Migrate to PostgreSQL/MySQL
- Implement connection pooling
- Consider read replicas
- Implement caching (Redis)

## Security Considerations

### Future Enhancements
- [ ] Spring Security integration
- [ ] JWT-based authentication
- [ ] OAuth2 authorization
- [ ] API rate limiting
- [ ] HTTPS/TLS
- [ ] Input sanitization
- [ ] CORS configuration

## Monitoring Strategy

### Metrics to Monitor
1. **Service Health**: UP/DOWN status
2. **Circuit Breaker**: Open/Closed state
3. **Response Times**: p50, p95, p99
4. **Error Rates**: 4xx, 5xx
5. **Request Volume**: Requests per second
6. **JVM Metrics**: Memory, GC, Threads

### Recommended Tools
- **Prometheus**: Metrics collection
- **Grafana**: Metrics visualization
- **ELK Stack**: Log aggregation
- **Zipkin**: Distributed tracing

## Deployment Architecture

### Local Development
```
Docker Compose (Future)
├── Eureka Server
├── API Gateway
├── Product Service
├── Order Service
├── Inventory Service
└── User Service
```

### Production (Kubernetes)
```
Kubernetes Cluster
├── Namespace: e-commerce
├── Deployments
│   ├── eureka-server (replicas: 2)
│   ├── api-gateway (replicas: 3)
│   ├── product-service (replicas: 3)
│   ├── order-service (replicas: 3)
│   ├── inventory-service (replicas: 2)
│   └── user-service (replicas: 2)
├── Services (ClusterIP)
├── Ingress (External access)
└── ConfigMaps & Secrets
```

