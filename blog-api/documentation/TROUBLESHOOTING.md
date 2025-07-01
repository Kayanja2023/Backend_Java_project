# Spring Boot Application Troubleshooting

## Issue 1: Port 8080 Already in Use

**Error:**
```
Web server failed to start. Port 8080 was already in use.
```

**Solution:**
1. Find process using port 8080:
   ```cmd
   netstat -ano | findstr :8080
   ```

2. Kill the process (replace XXXX with actual PID):
   ```cmd
   taskkill /PID XXXX /F
   ```

**Alternative:** Configure different port in `application.properties`:
```properties
server.port=8081
```

## Issue 2: H2 Database Not Found

**Error:**
```
Database "mem:testdb" not found, either pre-create it or allow remote database creation
```

**Root Cause:** Missing H2 database configuration in Spring Boot application.

**Solution:** Add H2 configuration to `src/main/resources/application.properties`:

```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

**Configuration Explained:**
- `jdbc:h2:mem:testdb` - Creates in-memory database named "testdb"
- `DB_CLOSE_DELAY=-1` - Keeps database alive during application lifecycle
- `DB_CLOSE_ON_EXIT=FALSE` - Prevents premature database closure
- `spring.h2.console.enabled=true` - Enables H2 web console at `/h2-console`

**Access H2 Console:** http://localhost:8081/h2-console

## Common Localhost URLs

### Main Application URLs
- **Application Root:** http://localhost:8081/
- **H2 Database Console:** http://localhost:8081/h2-console
- **Actuator Health Check:** http://localhost:8081/actuator/health
- **Actuator Info:** http://localhost:8081/actuator/info
- **All Actuator Endpoints:** http://localhost:8081/actuator

### API Endpoints
- **API Base:** http://localhost:8081/api/
- **Swagger UI (if enabled):** http://localhost:8081/swagger-ui.html
- **OpenAPI Docs:** http://localhost:8081/v3/api-docs

### Development Tools
- **Error Page:** http://localhost:8081/error

**Enable Actuator endpoints** by adding to `application.properties`:
```properties
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```