# Blog API - Spring Boot Learning Project

A basic blogging application built with Spring Boot to demonstrate fundamental concepts including RESTful web services, Hibernate ORM, and object-oriented programming principles.

## Table of Contents

- [Project Overview](#project-overview)
- [Learning Objectives](#learning-objectives)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Data Model](#data-model)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Exception Handling](#exception-handling)
- [Project Structure](#project-structure)

## Project Overview

This is a **4-week learning project** that implements a simple blogging system with basic CRUD operations. The focus is on demonstrating core Spring Boot concepts, Hibernate ORM usage, and fundamental web development practices rather than building a production-ready application.

## Learning Objectives

This project demonstrates understanding of:

- **Spring Boot Fundamentals**: Dependency injection, auto-configuration, and basic annotations
- **Hibernate ORM**: Entity relationships, basic annotations, and data persistence
- **RESTful Web Services**: Basic CRUD endpoints following REST principles
- **Object-Oriented Programming**: Encapsulation, abstraction, and separation of concerns
- **Testing Fundamentals**: Unit tests with JUnit and Mockito, basic integration testing
- **Exception Handling**: Simple error management using Spring's exception handling

## Technology Stack

- **Java 17** - Core programming language
- **Spring Boot 3.2.5** - Main application framework
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM for database operations
- **H2 Database** - In-memory database for simplicity
- **Maven** - Build tool and dependency management
- **JUnit 5** - Testing framework
- **Mockito** - Mocking for unit tests
- **Bean Validation** - Basic input validation

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Quick Start

1. **Clone and build**
   ```bash
   git clone <repository-url>
   cd blog-api
   mvn clean compile
   ```

2. **Run tests**
   ```bash
   mvn test
   ```

3. **Start application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access application**
   - API Base: http://localhost:8081/api/v1
   - H2 Console: http://localhost:8081/h2-console
   - Health Check: http://localhost:8081/actuator/health

### H2 Database Access
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (leave empty)

## Data Model

The application implements a simple blog data model with three main entities:

### Entities and Relationships

```
User (1) -----> (*) Post     # One user can have many posts
User (1) -----> (*) Comment  # One user can have many comments  
Post (1) -----> (*) Comment  # One post can have many comments
```

### Entity Details

**User Entity**
- `id` (Primary Key)
- `username` (Unique, Required)
- `email` (Unique, Required, Validated)
- `password` (Required, Min 6 characters)

**Post Entity**
- `id` (Primary Key)
- `title` (Required)
- `content` (Required)
- `createdAt` (Auto-generated)
- `authorId` (Foreign Key to User)

**Comment Entity**
- `id` (Primary Key)
- `content` (Required)
- `createdAt` (Auto-generated)
- `authorId` (Foreign Key to User)
- `postId` (Foreign Key to Post)

### Hibernate Annotations Used
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
- `@OneToMany`, `@ManyToOne`, `@JoinColumn`
- `@NotBlank`, `@Email`, `@Size` for validation

## API Endpoints

Basic CRUD operations for all entities following RESTful principles:

### Users
- `GET /api/v1/users` - List all users
- `GET /api/v1/users/{id}` - Get specific user
- `POST /api/v1/users` - Create new user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Posts
- `GET /api/v1/posts` - List all posts
- `GET /api/v1/posts/{id}` - Get specific post
- `POST /api/v1/posts` - Create new post
- `PUT /api/v1/posts/{id}` - Update post
- `DELETE /api/v1/posts/{id}` - Delete post

### Comments
- `GET /api/v1/comments/post/{postId}` - Get comments for a post
- `GET /api/v1/comments/{id}` - Get specific comment
- `POST /api/v1/comments` - Create new comment
- `PUT /api/v1/comments/{id}` - Update comment
- `DELETE /api/v1/comments/{id}` - Delete comment


## Testing

Basic testing implementation demonstrating testing fundamentals:

### Test Types
- **Unit Tests**: Service layer testing with Mockito
- **Integration Tests**: Controller endpoint testing
- **Exception Tests**: Error handling validation

### Test Statistics
- **Total Tests**: 47
- **Pass Rate**: 100%
- **Coverage**: Available via JaCoCo

### Running Tests
```bash
# Run all tests
mvn test

# Generate coverage report
mvn clean test jacoco:report

# View coverage report
start target/site/jacoco/index.html
```

## Exception Handling

Basic exception handling using Spring's `@ControllerAdvice`:

### Exception Types Handled
- **ApiException**: Custom business logic errors (400 Bad Request)
- **RuntimeException**: General runtime errors (400 Bad Request)
- **MethodArgumentNotValidException**: Validation errors (400 Bad Request)
- **Generic Exception**: Unexpected errors (500 Internal Server Error)

### Example Error Response
```
HTTP/1.1 400 Bad Request
Content-Type: text/plain

User not found
```

## Project Structure

```
blog-api/
├── src/main/java/com/andile/blogapi/
│   ├── controllers/     # REST endpoints (@RestController)
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # JPA entities with Hibernate annotations
│   ├── exception/      # Global exception handling
│   ├── repositories/   # Data access layer (Spring Data JPA)
│   ├── service/        # Business logic layer
│   └── BlogApiApplication.java
├── src/main/resources/
│   └── application.properties
├── src/test/java/      # Unit and integration tests
├── documentation/      # Additional learning guides
└── pom.xml            # Maven dependencies
```
