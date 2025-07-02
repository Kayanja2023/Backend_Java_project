# Exception Handling Code Guide

## Code Structure Overview

The exception handling system consists of 3 main components:

1. **ApiException Class** - Custom exception extending RuntimeException
2. **GlobalExceptionHandler** - Catches and handles all exceptions
3. **Service Layer Integration** - Throws exceptions with messages

---

## 1. ApiException Class

### ApiException.java
```java
package com.andile.blogapi.exception;

public class ApiException extends RuntimeException {
    
    public ApiException(String message) {
        super(message);
    }
}
```

**Explanation:**
- Extends `RuntimeException` (unchecked exception)
- Simple constructor that takes a message
- Used for all API-related business logic errors
- Cleaner than using RuntimeException directly

---

## 2. GlobalExceptionHandler

### GlobalExceptionHandler.java
```java
package com.andile.blogapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleApiException(ApiException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
}
```

**Key Annotations:**
- `@ControllerAdvice` - Makes this class handle exceptions globally across all controllers
- `@ExceptionHandler(ExceptionType.class)` - Specifies which exception this method handles

**Method Breakdown:**

### ApiException Handler
```java
@ExceptionHandler(ApiException.class)
public ResponseEntity<String> handleApiException(ApiException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
}
```
**Explanation:**
- Catches all `ApiException` instances
- Returns 400 Bad Request status
- Uses the exception's message as response body
- Handles business logic errors (user not found, email exists, etc.)

### RuntimeException Handler
```java
@ExceptionHandler(RuntimeException.class)
public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
}
```
**Explanation:**
- Catches other `RuntimeException` instances not handled by ApiException
- Fallback for runtime errors
- Returns 400 Bad Request status

### Generic Exception Handler
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<String> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
}
```
**Explanation:**
- Catches any unhandled exceptions
- Returns 500 Internal Server Error status
- Returns generic message to avoid exposing sensitive details
- Acts as fallback for unexpected errors

---

## 3. Service Layer Integration

### UserService.java (Exception Usage)
```java
// Get user by ID
public UserDto getUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ApiException("User not found"));
    return convertToDto(user);
}

// Create user with duplicate email check
public UserDto createUser(UserDto userDto) {
    if (userRepository.existsByEmail(userDto.getEmail())) {
        throw new ApiException("Email already exists");
    }
    if (userRepository.existsByUsername(userDto.getUsername())) {
        throw new ApiException("Username already exists");
    }
    // ... rest of method
}
```

### PostService.java (Exception Usage)
```java
// Get post by ID
public PostDto getPostById(Long id) {
    Post post = postRepository.findById(id)
            .orElseThrow(() -> new ApiException("Post not found"));
    return convertToDto(post);
}

// Create post with author validation
public PostDto createPost(PostDto postDto) {
    User author = userRepository.findById(postDto.getAuthorId())
            .orElseThrow(() -> new ApiException("Author not found"));
    // ... rest of method
}
```

**Explanation:**
- `orElseThrow(() -> new ApiException("message"))` - Throws exception if Optional is empty
- `throw new ApiException("message")` - Explicitly throws exception for business rule violations
- Simple descriptive messages that will be returned to client
- Using ApiException makes code more readable than generic RuntimeException

---

## Exception Flow

1. **Service Method Called** (e.g., `getUserById(999)`)
2. **Database Query Fails** (user 999 doesn't exist)
3. **ApiException Thrown** (`new ApiException("User not found")`)
4. **GlobalExceptionHandler Catches** (via `@ExceptionHandler(ApiException.class)`)
5. **400 Response Sent** (with "User not found" message)

## Exception Hierarchy Flow

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

## Exception Priority

1. **ApiException** - Handled first (most specific)
2. **RuntimeException** - Fallback for other runtime errors
3. **Exception** - Catches any unexpected errors (500 status)

## Benefits of This Approach

- **Simplicity** - Only one exception handler class
- **Minimal Code** - ~20 lines total for exception handling
- **Clear Messages** - Direct error messages to clients
- **Meets Requirements** - Satisfies "rudimentary exception handling"
- **Easy Maintenance** - Simple custom exception + handler
- **Spring Integration** - Uses Spring's `@ControllerAdvice` properly
- **Code Clarity** - ApiException is more descriptive than RuntimeException
- **Flexibility** - Can easily add more exception types later