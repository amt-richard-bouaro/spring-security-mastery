# Spring Security Mastery Project Guidelines

This document provides guidelines for developing and testing the Spring Security Mastery project.

## Build/Configuration Instructions

### Project Structure
The project is organized into multiple modules, each demonstrating different aspects of Spring Security:
- 01-basic-auth: Basic authentication
- 02-basic-auth-with-db: Basic authentication with database integration
- 03-custom-authentication: Custom authentication implementation
- 04-multi-authentication-filter: Multiple authentication filters
- 05-authorization: Role-based authorization

### Build Requirements
- Java 21
- Maven 3.6+

### Building the Project
Each module can be built independently using Maven:

```bash
cd <module-directory>
./mvnw clean install
```

For example:
```bash
cd 05-authorization
./mvnw clean install
```

### Running the Applications
Each module can be run as a standalone Spring Boot application:

```bash
cd <module-directory>
./mvnw spring-boot:run
```

For example:
```bash
cd 05-authorization
./mvnw spring-boot:run
```

The application will start on the port specified in the `application.yml` file (e.g., port 10010 for the 05-authorization module).

## Testing Information

### Test Configuration
The project uses JUnit 5 for testing with the following Spring Boot testing dependencies:
- spring-boot-starter-test: Core testing framework
- spring-security-test: Security-specific testing utilities

### Running Tests
Tests can be run using Maven:

```bash
cd <module-directory>
./mvnw test
```

### Writing Tests for Controllers
When writing tests for controllers with security, use the `@WebMvcTest` annotation for slice testing:

```java
@WebMvcTest(YourController.class)
public class YourControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    // Tests...
}
```

### Testing Secured Endpoints
Use the `@WithMockUser` annotation to simulate authenticated users:

```java
@Test
@WithMockUser(roles = "USER")
public void testSecuredEndpoint() throws Exception {
    mockMvc.perform(get("/secured-endpoint"))
            .andExpect(status().isOk());
}
```

### Testing Unauthenticated Access
Test that secured endpoints return 401 Unauthorized when accessed without authentication:

```java
@Test
public void testSecuredEndpointWithoutAuthentication() throws Exception {
    mockMvc.perform(get("/secured-endpoint"))
            .andExpect(status().isUnauthorized());
}
```

## Additional Development Information

### Code Style
- The project follows standard Spring Boot application structure
- Controllers are in the `controller` package
- Security configuration is in the `config` or `security` package
- Use annotations for configuration where possible

### Security Configuration
Security is configured using the new functional style introduced in Spring Security 6:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .httpBasic(httpBasic -> httpBasic.init(http))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/secured-path").hasRole("REQUIRED_ROLE")
                    .requestMatchers("/public-path").permitAll()
                    .anyRequest().authenticated()
            )
            .build();
}
```

### User Management
The project demonstrates different approaches to user management:
- In-memory user details service
- Database-backed user details service
- Custom authentication providers

### Debugging Tips
- Enable debug logging for Spring Security by adding the following to `application.yml`:
  ```yaml
  logging:
    level:
      org.springframework.security: DEBUG
  ```
- Use the Spring Security Test framework for testing security configurations
- For API testing, you can use tools like Postman or curl with the appropriate authentication headers