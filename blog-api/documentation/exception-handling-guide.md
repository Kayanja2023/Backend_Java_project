# Exception Handling Guide

## Overview
This blog API implements basic exception handling to provide simple error responses to clients using Spring's `@ControllerAdvice`.

## Architecture

### Custom Exception Class
`ApiException` - Simple custom exception extending RuntimeException

### Global Exception Handler
`GlobalExceptionHandler` class uses `@ControllerAdvice` to catch exceptions across the application.

### Error Response Format
Errors return simple text messages:
- **ApiException** → 400 Bad Request with error message
- **RuntimeException** → 400 Bad Request with error message
- **Generic Exception** → 500 Internal Server Error with generic message

## HTTP Status Codes

| Exception Type | Status Code | Response |
|----------------|-------------|----------|
| ApiException | 400 | Error message from exception |
| RuntimeException | 400 | Error message from exception |
| Generic Exception | 500 | "An error occurred" |

## Examples

### User Not Found
**Request:** `GET /api/v1/users/999`
**Response:** 400 Bad Request
```
User not found
```

### Email Already Exists
**Request:** `POST /api/v1/users` with existing email
**Response:** 400 Bad Request
```
Email already exists
```

### Unexpected Error
**Request:** Any request causing unexpected error
**Response:** 500 Internal Server Error
```
An error occurred
```

## Implementation
Services throw `ApiException` with descriptive messages:
```java
throw new ApiException("User not found");
throw new ApiException("Email already exists");
```

### ApiException Class
```java
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
```

## Exception Flow

```
Client Request
     ↓
Controller
     ↓
Service Layer
     ↓
Repository/Database
     ↓
Error Occurs (e.g., user not found)
     ↓
ApiException thrown
     ↓
GlobalExceptionHandler catches
     ↓
400 Bad Request response
     ↓
Client receives error message
```

## Benefits
- Simple implementation
- Clear error messages
- Centralized error handling
- Meets basic project requirements
- Custom exception more descriptive than RuntimeException