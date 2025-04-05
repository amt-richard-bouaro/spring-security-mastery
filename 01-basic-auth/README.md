# Spring Security - HTTP Basic Authentication Module 🔐

This module demonstrates the simplest form of authentication in Spring Security using HTTP Basic Authentication with in-memory users.

## 📌 Features

- Default Spring Security configuration
- HTTP Basic Authentication
- In-memory user storage
- Role-based access control (USER/ADMIN)
- Demonstration of password encoding (with security caveat)

## 🛠️ Configuration

The security configuration is set up in [SecurityConfig.java](src/main/java/com/rbouaro/basicauth/security/SecurityConfig.java)

## 🔐 Default Credentials

| Username | Password | Role |
|----------|----------|------|
| user     | password | USER |
| admin    | password | ADMIN |

## 🚀 Running the Application

Start the application:
```bash
  mvn spring-boot:run
```

Access protected endpoints:

```bash
  curl -u user:password http://localhost:10010/user/resouce
```
Try admin-only endpoint:
```bash
  curl -u admin:password http://localhost:10010/admin/resource
```