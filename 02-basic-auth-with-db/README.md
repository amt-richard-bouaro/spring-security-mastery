# Spring Security - HTTP Basic Authentication Module with Postgres DB 🔐

This module demonstrates the simplest form of authentication in Spring Security using HTTP Basic Authentication with postgres database users and their authorities.

## 🚀 Running the Application

Setup environment variables:
Check the example-env file and create a `.env` file in the root directory of the project. You can also set these variables in your IDE or terminal.

Spin the postgres database container using Docker:
```bash
  docker-compose up -d
```

Start the application:
```bash
  mvn spring-boot:run
```

Access protected endpoints:

```bash
  curl -u user:user http://localhost:10010/api/resouce
```
Try admin-only endpoint:
```bash
  curl -u admin:admin http://localhost:10010/api/resource
```