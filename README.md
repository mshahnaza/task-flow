# Task Manager API

## Overview
Task Manager API is a Spring Boot-based project for managing tasks. It allows users to create, update, delete, and retrieve tasks using RESTful endpoints. The project uses an H2 in-memory database by default, making it easy to set up and test.

## Features
- User authentication via **OAuth2**
- JWT token generation and authorization
- Create, update, delete, and retrieve tasks
- Filter and sort tasks by status and category
- Create and delete categories
- Add, delete, and retrieve comments per task
- Error handling via `@ControllerAdvice`
- Swagger for API documentation

## Technologies Used
- Java 23
- Spring Boot 3
- Spring Security (JWT + OAuth2)
- Spring MVC (REST API)
- Spring Data JPA
- H2 Database (in-memory)
- PostgreSQL (production)
- Lombok
- Swagger / OpenAPI
- Maven

## Installation & Setup

### Prerequisites
- Java 23
- Maven

### Running the Project

1. Clone the repository
2. Configure your OAuth2 and mail credentials in `application-oauth.properties`:
    ```properties
    spring.mail.username=${MAIL_USERNAME}
    spring.mail.password=${MAIL_PASSWORD}

    spring.security.oauth2.client.registration.google.provider=google
    spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
    spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
    spring.security.oauth2.client.registration.google.scope=email,profile

    spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
    spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
    spring.security.oauth2.client.registration.github.scope=read:user,user:email
    ```
3. Build the project
4. Run the application
5. API is available at `http://localhost:8080`

## API Endpoints
Only per-user access control (made using @PreAuthorize)

| Method | Endpoint                  | Description                             |
|--------|---------------------------|-----------------------------------------|
| GET    | /task/all                 | Get all tasks                           |
| POST   | /task/add                 | Create new task                         |
| GET    | /task/{id}                | Get task by ID                          |
| PATCH  | /task/update/{id}         | Update task by ID                       |
| DELETE | /task/delete/{id}         | Delete task by ID                       |
| GET    | /category/all             | Get all categories                      |
| POST   | /category/add             | Create a category                       |
| DELETE | /category/delete/{id}     | Delete category by ID                   |
| GET    | /comment/task/{id}        | Get comments for task                   |
| POST   | /comment/add/{id}         | Add comment to task                     |
| DELETE | /comment/delete/{id}      | Delete comment by ID                    |
| GET    | /comment/all              | Get all comments                        |
| POST   | /auth/login               | Authenticate user (JWT)                 |
| POST   | /auth/register            | Register user                           |
| GET    | /auth/verify-email        | Verify email with link                  |
| POST   | /auth/resend-verification | Resend email verification link          |
| DELETE | /auth/user/delete         | Delete current user                     |
| GET    | /admin/users/all          | Get all users (only for admins)         |
| GET    | /admin/user/{username}    | Get user by username (only for admins)  |
| POST   | /admin/register           | Add admin (only for admins)             |
| DELETE | /admin/delete/user/{id}   | Delete any user by id (only for admins) |





## Authentication & Security

- This project uses Spring Security to secure all endpoints.
- Users can login using Google, Github OAuth2.
- After login, a JWT token is generated and returned.
- All secured endpoints require a Bearer token in the Authorization header.

### JWT Example Usage

After successful login:

```json
{
  "acces_token": "eyJhbGciOiJIUzI1NiJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Add this token to requests like this:

```
Authorization: Bearer <token>
```

Also you can:
1. Open browser and open Developer Tools
2. Open Network -> Headers
3. Login and find Cookies
4. Copy and pate in Headers in Postman like this:
```
Key               Values 
Cookies          JSESSIONID=5A63F85...
```

## Database Configuration
By default, the project uses an **H2 in-memory database**. The configuration is in `application-dev.properties`:
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2
spring.datasource.url=jdbc:h2:mem:task-flow
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```
The H2 database console is available at: [http://localhost:8080/h2](http://localhost:8080/h2)

There's also `application-prod.properties` for configuration of Postgres database
```
spring.datasource.url=jdbc:postgresql://localhost:5432/task-flow
spring.datasource.username=postgres
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```
In `application.properties` active by default profile is dev
```
spring.application.name=task-manager

spring.profiles.active=dev
```

## Error Handling
All exceptions are handled globally using `@ControllerAdvice` and `@ExceptionHandler`. Errors are returned in a standardized format with meaningful messages.

## API Documentation (Swagger)
API documentation is available at:

```
http://localhost:8080/swagger-ui/index.html
```
