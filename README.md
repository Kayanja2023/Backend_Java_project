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

Or use the in-memory H2 database for testing.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

### 4. Access Swagger UI (API Docs)

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

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

## Learning Objectives

- RESTful API design with Spring Boot
- Object-relational mapping using Hibernate
- Dependency injection & layered architecture
- Error handling and validation
- Testing with JUnit/Mockito
- API documentation with Swagger

---

