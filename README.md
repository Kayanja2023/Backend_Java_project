# Blog REST API – Spring Boot Project

A simple blogging platform backend built with **Java**, **Spring Boot**, **Hibernate (JPA)**, and **JUnit**. It supports basic CRUD operations for **Users**, **Posts**, and **Comments**, demonstrating best practices in RESTful API design, layered architecture, and testing.

---

## Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA (Hibernate)**
- **MySQL** or **H2 (for testing)**
- **JUnit 5 + Mockito**
- **Spring Web (Spring MVC)**
- **Lombok**
- **Swagger (OpenAPI)**
- **Postman** (for API testing)

---

## Features

- Create, update, and delete **users**
- Allow users to write **posts**
- Add **comments** to blog posts
- Basic validation and error handling
- Layered architecture (`Controller → Service → Repository`)
- Unit and integration testing with JUnit/Mockito
- API documentation with Swagger UI

---

## Project Structure

```
src/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
├── service/
└── test/
```

---

## API Endpoints

| Method | Endpoint                          | Description                  |
|--------|-----------------------------------|------------------------------|
| POST   | `/api/v1/users`                   | Create a user                |
| GET    | `/api/v1/users`                   | Get all users                |
| GET    | `/api/v1/users/{id}`              | Get user by ID               |
| PUT    | `/api/v1/users/{id}`              | Update a user                |
| DELETE | `/api/v1/users/{id}`              | Delete a user                |
| POST   | `/api/v1/posts`                   | Create post                  |
| GET    | `/api/v1/posts`                   | Get all posts                |
| GET    | `/api/v1/posts/{id}`              | Get post by ID               |
| POST   | `/api/v1/posts/{id}/comments`     | Add comment to post          |
| GET    | `/api/v1/posts/{id}/comments`     | Get all comments for post    |
| DELETE | `/api/v1/comments/{id}`           | Delete comment               |

---

## Running Locally

### 1. Clone the project

```bash
git clone https://gitlab.com/your-username/blog-api-springboot.git
cd blog-api-springboot
```

### 2. Configure DB (optional)

Set your database in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blogdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

Or use the in-memory H2 database (default configuration):

```properties
server.port=8081
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

### 4. Access the Application

- **Application:** http://localhost:8081/
- **H2 Database Console:** http://localhost:8081/h2-console
- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **Health Check:** http://localhost:8081/actuator/health

---

## Sample Payloads

### Create User

```http
POST /api/v1/users
Content-Type: application/json

{
  "name": "Andile Lwanga",
  "email": "andile@example.com"
}
```

### Create Post

```http
POST /api/v1/posts
Content-Type: application/json

{
  "title": "My First Blog",
  "content": "Excited to write this!",
  "userId": 1
}
```

---

##  Running Tests

```bash
./mvnw test
```

Includes:
- Unit tests for service layer (JUnit + Mockito)
- Integration tests for REST endpoints (MockMvc)

---

## Troubleshooting

For common issues and solutions, see [TROUBLESHOOTING.md](TROUBLESHOOTING.md).

### Quick Fixes
- **Port 8080 in use:** Application runs on port 8081
- **Database issues:** H2 console available at http://localhost:8081/h2-console

---

## Learning Objectives

- RESTful API design with Spring Boot
- Object-relational mapping using Hibernate
- Dependency injection & layered architecture
- Error handling and validation
- Testing with JUnit/Mockito
- API documentation with Swagger

---

