# Task Manager API

## Overview
Task Manager API is a Spring Boot-based project for managing tasks. It allows users to create, update, delete, and retrieve tasks using RESTful endpoints. The project uses an H2 in-memory database, making it easy to set up and test.

## Features
- Create a new task
- Retrieve a task by ID
- Update an existing task
- Delete a task by ID
- Retrieve all tasks with optional filters (status, category, sorting order)
- Create a category
- Delete a category by ID
- Retrieve all categorires
- Create a comment
- Retrieve comments by task ID
- Retrieve all comments
- Delete comment by ID

## Technologies Used
- Java 23
- Spring Boot 3
- Spring MVC (REST API)
- Spring Data JPA
- H2 Database (in-memory)
- Lombok
- OpenAPI (Swagger) for documentation
- Maven (build tool)

## Installation & Setup

### Prerequisites
- Java 23
- Maven

### Running the Project
1. Clone the repository
2. Build the project:
3. Run the application:
4. The API will be available at `http://localhost:8080`.

## API Endpoints

GET 	/task/all	Get a list of tasks
POST 	/task/add	Create a task
GET 	/task/{id}	Get a task by ID
PATCH 	/task/update/{id}	Update tasks data
DELETE	/task/delete/{id}	Delete a task
GET	  /category/all	Get all categories
POST	/category/add	Create a category
DELETE	/category/delete/{id}	Delete a category
GET	  /comment/task/{id}	Get tasks comments
POST	/comment/add/{id}	Add a comment
DELETE	/comment/delete/{id}	Delete a comment
GET 	/comment/all	Get all comments


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
The H2 database console is available at: [http://localhost:8080/h2-console](http://localhost:8080/h2)

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
- All errors are handled globally using `@ControllerAdvice` and `@ExceptionHandler`.

## API Documentation (Swagger)
Swagger UI is available at:
```
http://localhost:8080/swagger-ui/index.html
```


