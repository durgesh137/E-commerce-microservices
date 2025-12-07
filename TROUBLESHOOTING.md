# Troubleshooting Guide

## Common Issues and Solutions

### 1. Port Already in Use

**Problem**: `Port 8080 is already in use`

**Solution**:
```bash
# Find and kill the process using the port
lsof -ti :8080 | xargs kill -9

# Or use the stop script
./stop-services.sh
```

### 2. Eureka Server Not Starting

**Problem**: Services cannot register with Eureka

**Solution**:
- Ensure Eureka Server starts first
- Wait for Eureka dashboard to be accessible at http://localhost:8761
- Check logs: `cat logs/eureka-server.log`
- Verify port 8761 is not in use

### 3. Service Registration Issues

**Problem**: Service appears as DOWN in Eureka dashboard

**Solution**:
- Check if the service is actually running: `curl http://localhost:808X/actuator/health`
- Verify `eureka.client.service-url.defaultZone` is set correctly
- Check network connectivity
- Restart the service

### 4. H2 Console Access Issues

**Problem**: Cannot access H2 console

**Solution**:
- Ensure H2 console is enabled in application.properties
- Use correct JDBC URL (e.g., `jdbc:h2:mem:productdb`)
- Username: `sa`, Password: (empty)
- Access directly on service port, not through gateway

### 5. Order Creation Fails

**Problem**: Order creation returns 400 Bad Request

**Possible Causes**:
1. Inventory service is down
2. Insufficient stock
3. Invalid product ID

**Solution**:
```bash
# Check inventory service health
curl http://localhost:8083/actuator/health

# Check product availability
curl http://localhost:8080/api/inventory/check/1/1

# Check circuit breaker status
curl http://localhost:8082/actuator/circuitbreakers
```

### 6. Circuit Breaker Always Open

**Problem**: Circuit breaker stays open even after service recovery

**Solution**:
- Wait for the configured wait duration (10 seconds)
- Check if the service is actually healthy
- Verify circuit breaker configuration
- Review logs for errors

### 7. Maven Build Failures

**Problem**: `mvn clean install` fails

**Solution**:
```bash
# Clean all target directories
find . -name "target" -type d -exec rm -rf {} +

# Rebuild
mvn clean install -DskipTests

# If still failing, try cleaning Maven cache
rm -rf ~/.m2/repository/com/example
```

### 8. OutOfMemoryError

**Problem**: Service crashes with OutOfMemoryError

**Solution**:
```bash
# Increase JVM memory when starting service
export MAVEN_OPTS="-Xmx1024m"
mvn spring-boot:run
```

### 9. Slow Service Startup

**Problem**: Services take too long to start

**Solution**:
- This is normal for Spring Boot applications
- Wait for "Started Application in X seconds" message
- Use the startup script which waits for each service
- Consider using Spring Boot DevTools for faster restarts in development

### 10. API Gateway Returns 503

**Problem**: Gateway returns 503 Service Unavailable

**Possible Causes**:
1. Backend service is not registered with Eureka
2. Service is DOWN
3. All instances are unhealthy

**Solution**:
```bash
# Check Eureka dashboard
open http://localhost:8761

# Check service health
curl http://localhost:8081/actuator/health

# Check gateway routes
curl http://localhost:8080/actuator/health
```

## Debugging Commands

### Check All Services Status
```bash
echo "Eureka Server:" && curl -s http://localhost:8761/actuator/health | grep status
echo "API Gateway:" && curl -s http://localhost:8080/actuator/health | grep status
echo "Product Service:" && curl -s http://localhost:8081/actuator/health | grep status
echo "Order Service:" && curl -s http://localhost:8082/actuator/health | grep status
echo "Inventory Service:" && curl -s http://localhost:8083/actuator/health | grep status
echo "User Service:" && curl -s http://localhost:8084/actuator/health | grep status
```

### View Service Logs
```bash
# View logs in real-time
tail -f logs/product-service.log

# Search for errors
grep -i error logs/*.log
```

### Check Process Status
```bash
# Check if services are running
lsof -i :8761,8080,8081,8082,8083,8084
```

### Test Service Connectivity
```bash
# Test from API Gateway to backend service
curl http://localhost:8080/api/products
curl http://localhost:8080/api/users
curl http://localhost:8080/api/inventory
curl http://localhost:8080/api/orders
```

## Performance Issues

### High CPU Usage
- Check for infinite loops in logs
- Review circuit breaker configurations
- Consider reducing retry attempts
- Check for database query issues

### High Memory Usage
- Increase JVM heap size
- Check for memory leaks
- Review database connection pooling
- Consider using production database instead of H2

### Slow Response Times
- Check database query performance
- Review circuit breaker timeout settings
- Consider adding caching
- Check network latency between services

## Database Issues

### Schema Not Created
- Verify `spring.sql.init.mode=always`
- Check schema.sql syntax
- Review logs for SQL errors
- Ensure H2 dependency is included

### Data Not Seeded
- Verify `spring.sql.init.data-locations` is set
- Check data.sql syntax
- Ensure schema is created first
- Review initialization order

## Network Issues

### Services Cannot Communicate
- Check if all services are registered with Eureka
- Verify network connectivity
- Check firewall settings
- Ensure correct service names in Feign clients

### DNS Resolution Failures
- Use IP addresses instead of hostnames
- Check /etc/hosts file
- Verify DNS settings

## IDE Issues

### IntelliJ IDEA
- Reimport Maven project: Right-click pom.xml → Maven → Reload Project
- Invalidate caches: File → Invalidate Caches / Restart
- Enable annotation processing: Settings → Build → Compiler → Annotation Processors

### Eclipse
- Update Maven project: Right-click project → Maven → Update Project
- Clean and rebuild: Project → Clean
- Ensure Lombok is installed

## Getting Help

### Log Collection
```bash
# Collect all logs
tar -czf logs.tar.gz logs/

# Get system information
uname -a
java -version
mvn -version
```

### Health Check Report
```bash
# Generate health report
./health-check.sh > health-report.txt
```

### Useful Resources
- Spring Cloud Documentation: https://spring.io/projects/spring-cloud
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Resilience4j Documentation: https://resilience4j.readme.io
- Netflix Eureka Documentation: https://github.com/Netflix/eureka

## Quick Reset

If everything is broken, perform a complete reset:

```bash
# Stop all services
./stop-services.sh

# Clean everything
mvn clean
rm -rf logs/*
rm -rf target/
find . -name "target" -type d -exec rm -rf {} +

# Rebuild
mvn clean install -DskipTests

# Start fresh
./start-services.sh
```

