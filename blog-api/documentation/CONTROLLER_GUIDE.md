# Understanding Controllers in Spring Boot: A Beginner's Guide

## What are Controllers?

**Controllers** are the **front door** of your Spring Boot application. They handle all incoming HTTP requests from clients (web browsers, mobile apps, other services) and send back appropriate responses.

Think of controllers as **receptionists** at a hotel who:
- Greet visitors (receive HTTP requests)
- Understand what visitors need (parse request data)
- Direct visitors to the right department (call appropriate services)
- Provide responses back to visitors (return HTTP responses)

In simple terms: **Controllers handle the "how to communicate with the outside world" part** of your application.

## The Problem Controllers Solve

Without controllers, there would be no way for external clients to interact with your application:

```java
// ❌ Without Controllers - No way to access your app!
public class UserService {
    public UserDto createUser(UserDto userDto) {
        // Great business logic, but how do clients call this?
        // No HTTP endpoints, no REST API, no communication!
    }
}
```

**Problems without controllers:**
- **No external access** - Services exist but can't be called from outside
- **No standardized communication** - Each client would need custom integration
- **No HTTP protocol support** - Can't use standard web technologies
- **No REST API** - Can't follow REST conventions

## The Controller Solution

Controllers expose your services through standard HTTP endpoints:

```java
// ✅ With Controllers - Clean HTTP API!
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
```

Now clients can call: `POST /api/v1/users` with JSON data!

## The Complete Architecture Flow

```
HTTP Request → Controller → Service → Repository → Database
     ↓            ↓          ↓          ↓           ↓
   JSON         HTTP       Business    Data        SQL
  Payload      Handler     Logic      Access      Query
     ↑            ↑          ↑          ↑           ↑
HTTP Response ← Controller ← Service ← Repository ← Database
```

### Layer Responsibilities:

- **Controller**: Handle HTTP requests/responses, validation, routing
- **Service**: Business logic, coordination, data transformation
- **Repository**: Database operations, data persistence
- **Entity**: Data structure, database mapping

## Our Controller Classes Explained

### 1. UserController - Managing User HTTP Endpoints

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
```

**Key Annotations Explained:**

#### `@RestController`
```java
@RestController  // Combines @Controller + @ResponseBody
public class UserController {
    // All methods automatically return JSON/XML instead of HTML views
}
```
**What it does**: Tells Spring this class handles REST API requests and responses should be JSON/XML, not HTML pages.

#### `@RequestMapping("/api/v1/users")`
```java
@RequestMapping("/api/v1/users")  // Base URL for all methods in this controller
public class UserController {
    
    @GetMapping  // This becomes: GET /api/v1/users
    public ResponseEntity<List<UserDto>> getAllUsers() { ... }
    
    @PostMapping  // This becomes: POST /api/v1/users
    public ResponseEntity<UserDto> createUser() { ... }
}
```
**What it does**: Sets the base URL path for all endpoints in this controller.

#### `@RequiredArgsConstructor`
```java
@RequiredArgsConstructor  // Lombok generates constructor for final fields
public class UserController {
    private final UserService userService;  // Automatically injected by Spring
}
```
**What it does**: Automatically creates constructor for dependency injection.

### HTTP Method Mappings Explained

#### `@GetMapping` - Retrieve Data
```java
@GetMapping  // GET /api/v1/users
public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> users = userService.getAllUsers();
    return ResponseEntity.ok(users);  // HTTP 200 OK
}

@GetMapping("/{id}")  // GET /api/v1/users/123
public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    UserDto user = userService.getUserById(id);
    return ResponseEntity.ok(user);  // HTTP 200 OK
}
```

**When to use GET:**
- Retrieving data
- No side effects (doesn't change anything)
- Safe to call multiple times
- Can be cached by browsers

#### `@PostMapping` - Create New Data
```java
@PostMapping  // POST /api/v1/users
public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // HTTP 201 Created
}
```

**When to use POST:**
- Creating new resources
- Has side effects (changes data)
- Not safe to call multiple times
- Request body contains data

#### `@PutMapping` - Update Existing Data
```java
@PutMapping("/{id}")  // PUT /api/v1/users/123
public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
    UserDto updatedUser = userService.updateUser(id, userDto);
    return ResponseEntity.ok(updatedUser);  // HTTP 200 OK
}
```

**When to use PUT:**
- Updating existing resources
- Complete replacement of resource
- Idempotent (same result if called multiple times)

#### `@DeleteMapping` - Remove Data
```java
@DeleteMapping("/{id}")  // DELETE /api/v1/users/123
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();  // HTTP 204 No Content
}
```

**When to use DELETE:**
- Removing resources
- Idempotent (safe to call multiple times)
- Usually returns no content

### 2. PostController - Managing Blog Post Endpoints

```java
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPostsByTitle(@RequestParam String title) {
        List<PostDto> posts = postService.searchPostsByTitle(title);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostDto>> getPostsByAuthor(@PathVariable Long authorId) {
        List<PostDto> posts = postService.getPostsByAuthor(authorId);
        return ResponseEntity.ok(posts);
    }
}
```

**Advanced URL Patterns:**

#### Query Parameters with `@RequestParam`
```java
@GetMapping("/search")  // GET /api/v1/posts/search?title=spring&author=john
public ResponseEntity<List<PostDto>> searchPosts(
    @RequestParam String title,           // Required parameter
    @RequestParam(required = false) String author  // Optional parameter
) {
    // title = "spring", author = "john" (or null if not provided)
}
```

**Usage**: `GET /api/v1/posts/search?title=spring boot`

#### Path Variables with `@PathVariable`
```java
@GetMapping("/author/{authorId}")  // GET /api/v1/posts/author/123
public ResponseEntity<List<PostDto>> getPostsByAuthor(@PathVariable Long authorId) {
    // authorId = 123
}
```

**Usage**: `GET /api/v1/posts/author/123`

### 3. CommentController - Managing Comment Endpoints

```java
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody String content) {
        CommentDto updatedComment = commentService.updateComment(id, content);
        return ResponseEntity.ok(updatedComment);
    }
}
```

**Nested Resource Patterns:**
- `/api/v1/comments/post/{postId}` - Get comments for a specific post
- `/api/v1/comments/author/{authorId}` - Get comments by a specific author

This follows REST conventions for related resources.

## HTTP Status Codes Explained

Controllers should return appropriate HTTP status codes:

```java
// ✅ Proper Status Codes
@PostMapping
public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // 201 Created
}

@GetMapping("/{id}")
public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    UserDto user = userService.getUserById(id);
    return ResponseEntity.ok(user);  // 200 OK
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();  // 204 No Content
}
```

### Common Status Codes:

| Code | Meaning | When to Use |
|------|---------|-------------|
| 200 | OK | Successful GET, PUT requests |
| 201 | Created | Successful POST (creation) |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input data |
| 404 | Not Found | Resource doesn't exist |
| 500 | Internal Server Error | Unexpected server error |

## Request/Response Handling

### Request Body with `@RequestBody`
```java
@PostMapping
public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
    // Spring automatically converts JSON to UserDto object
    // @Valid triggers validation annotations in UserDto
    UserDto createdUser = userService.createUser(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
}
```

**Client sends:**
```json
POST /api/v1/users
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com"
}
```

**Spring automatically converts JSON to UserDto object!**

### Response Body (Automatic)
```java
@GetMapping("/{id}")
public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    UserDto user = userService.getUserById(id);
    return ResponseEntity.ok(user);  // Spring converts UserDto to JSON
}
```

**Client receives:**
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com"
}
```

## Validation with `@Valid`

```java
@PostMapping
public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
    // @Valid triggers validation based on annotations in UserDto:
    // @NotBlank, @Email, etc.
    // If validation fails, Spring returns HTTP 400 Bad Request automatically
}
```

**UserDto with validation:**
```java
public class UserDto {
    @NotBlank(message = "Username is required")
    private String username;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
```

If client sends invalid data, Spring automatically returns:
```json
HTTP/1.1 400 Bad Request
{
  "errors": [
    "Username is required",
    "Invalid email format"
  ]
}
```

## Complete API Endpoints Summary

### User Endpoints
| Method | URL | Description | Request Body | Response |
|--------|-----|-------------|--------------|----------|
| GET | `/api/v1/users` | Get all users | None | List of UserDto |
| GET | `/api/v1/users/{id}` | Get user by ID | None | UserDto |
| POST | `/api/v1/users` | Create user | UserDto | UserDto (201) |
| PUT | `/api/v1/users/{id}` | Update user | UserDto | UserDto |
| DELETE | `/api/v1/users/{id}` | Delete user | None | None (204) |

### Post Endpoints
| Method | URL | Description | Request Body | Response |
|--------|-----|-------------|--------------|----------|
| GET | `/api/v1/posts` | Get all posts | None | List of PostDto |
| GET | `/api/v1/posts/{id}` | Get post by ID | None | PostDto |
| GET | `/api/v1/posts/author/{authorId}` | Get posts by author | None | List of PostDto |
| GET | `/api/v1/posts/search?title=keyword` | Search posts | None | List of PostDto |
| POST | `/api/v1/posts` | Create post | CreatePostDto | PostDto (201) |
| PUT | `/api/v1/posts/{id}` | Update post | CreatePostDto | PostDto |
| DELETE | `/api/v1/posts/{id}` | Delete post | None | None (204) |

### Comment Endpoints
| Method | URL | Description | Request Body | Response |
|--------|-----|-------------|--------------|----------|
| GET | `/api/v1/comments/post/{postId}` | Get comments for post | None | List of CommentDto |
| GET | `/api/v1/comments/author/{authorId}` | Get comments by author | None | List of CommentDto |
| GET | `/api/v1/comments/{id}` | Get comment by ID | None | CommentDto |
| POST | `/api/v1/comments` | Create comment | CreateCommentDto | CommentDto (201) |
| PUT | `/api/v1/comments/{id}` | Update comment | String (content) | CommentDto |
| DELETE | `/api/v1/comments/{id}` | Delete comment | None | None (204) |

## Testing Your Controllers

### Using curl:
```bash
# Create a user
curl -X POST http://localhost:8081/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","email":"john@example.com"}'

# Get all users
curl http://localhost:8081/api/v1/users

# Search posts
curl "http://localhost:8081/api/v1/posts/search?title=spring"
```

### Using Postman:
1. Set method (GET, POST, PUT, DELETE)
2. Enter URL: `http://localhost:8081/api/v1/users`
3. For POST/PUT: Add JSON body in "Body" tab
4. Send request and view response

## Benefits of Controllers

### ✅ Clean Separation of Concerns
```java
// Controller: Handle HTTP communication
@PostMapping
public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);  // Delegate to service
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
}

// Service: Handle business logic
public UserDto createUser(UserDto userDto) {
    // Validation, conversion, business rules here
}
```

### ✅ Standardized REST API
All endpoints follow REST conventions:
- Consistent URL patterns
- Proper HTTP methods
- Standard status codes
- JSON request/response format

### ✅ Easy Testing
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void shouldCreateUser() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"john\",\"email\":\"john@example.com\"}"))
                .andExpect(status().isCreated());
    }
}
```

## Common Beginner Mistakes

### ❌ Putting Business Logic in Controllers
```java
// DON'T DO THIS
@PostMapping
public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    // Business logic in controller - BAD!
    if (userRepository.existsByEmail(userDto.getEmail())) {
        return ResponseEntity.badRequest().build();
    }
    // More business logic...
}
```

### ✅ Keep Controllers Thin
```java
// DO THIS
@PostMapping
public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);  // Delegate to service
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
}
```

### ❌ Wrong HTTP Methods
```java
// DON'T DO THIS
@GetMapping("/delete/{id}")  // Using GET for deletion - BAD!
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.ok().build();
}
```

### ✅ Use Correct HTTP Methods
```java
// DO THIS
@DeleteMapping("/{id}")  // Use DELETE for deletion - GOOD!
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();  // 204 No Content
}
```

### ❌ Ignoring Status Codes
```java
// DON'T DO THIS
@PostMapping
public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);
    return ResponseEntity.ok(createdUser);  // Should be 201 Created, not 200 OK
}
```

### ✅ Use Proper Status Codes
```java
// DO THIS
@PostMapping
public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // 201 Created
}
```

## Next Steps

Now that you understand controllers, you should learn about:

1. **Exception Handling** - Handle errors gracefully with @ControllerAdvice
2. **Security** - Add authentication and authorization
3. **API Documentation** - Use Swagger/OpenAPI for documentation
4. **Testing** - Write comprehensive controller tests

## Key Takeaways

- **Controllers are the front door** - They handle all HTTP communication
- **Keep controllers thin** - Delegate business logic to services
- **Follow REST conventions** - Use proper HTTP methods and status codes
- **Use appropriate annotations** - @RestController, @RequestMapping, @Valid
- **Handle requests and responses properly** - @RequestBody, @PathVariable, @RequestParam
- **Return meaningful status codes** - 200, 201, 204, 400, 404, 500
- **Controllers complete the architecture** - They make your services accessible to the world

Controllers are the final piece that makes your Spring Boot application a complete, functional REST API. They transform your internal business logic into a standardized, accessible web service that any client can use.