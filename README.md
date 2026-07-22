# Wushu Training Manager

A web application built with Spring Boot for managing Wushu training activities.

The application provides authentication and role-based authorization, allowing administrators and coaches to manage students, coaches, training groups, exercises, training sessions, and attendance records.

This project was developed to practice backend development using modern Java and Spring technologies.

---

## ✨ Features

- Authentication and authorization with Spring Security
- Role-based access control (ADMIN / COACH)
- Coach management
- Student management
- Training group management
- Training session management
- Exercise management
- Attendance management
- Pagination support
- Input validation
- Database versioning with Liquibase
- Unit testing with JUnit 5 and Mockito

## 🗺️ Roadmap (In Progress)
The application is currently being actively updated to meet modern enterprise standards. The current focus includes:
- [ ] **Database Migration:** Transitioning the primary database from MySQL to **PostgreSQL** using Liquibase.
- [ ] **REST API Development:** Designing and implementing RESTful endpoints alongside the existing MVC controllers for frontend decoupling.
- [ ] **API Documentation:** Integrating Swagger/OpenAPI for the upcoming REST endpoints.
- [ ] **Testing:** Finalizing the test coverage using the H2 in-memory database for integration tests.
---

## 🛠 Technology Stack

### Backend

- Java 17
- Spring Boot 3
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

### Database

- MySQL (Current)
- **PostgreSQL (Migration in progress)**
- Liquibase
- H2 (for tests)

### Testing

- Unit tests for the service layer
- JUnit 5
- Mockito

### Logging

- SLF4J
- Logback

### Build & Tools

- Maven
- Apache Tomcat
- Lombok
- Checkstyle
- JaCoCo


---

## 🏗 Architecture

The application follows a layered architecture with a clear separation of responsibilities.

```
Client
   │
   ▼
Spring MVC Controllers
   │
   ▼
Service Layer
   │
   ▼
Repository Layer (Spring Data JPA)
   │
   ▼
MySQL Database
```

### Main Components

- **Controllers** – process HTTP requests and return views.
- **Services** – contain business logic.
- **Repositories** – provide database access through Spring Data JPA.
- **DTOs & Mappers** – separate the presentation layer from persistence models.
- **Spring Security** – authentication and role-based authorization.
- **Liquibase** – database schema versioning and migrations.

---

## 📂 Project Structure

```
src
├── main
│   ├── java
│   │   └── com.wushu
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── filter
│   │       ├── mapper
│   │       ├── repository
│   │       ├── security
│   │       └── service
│   └── resources
│       ├── db
│       ├── static
│       └── templates
└── test
```

---

## 🔐 User Roles

The application supports two user roles:

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access to application functionality |
| **COACH** | Limited access according to the assigned role |

After successful authentication, users are redirected to the **Coaches** page (`/coaches`).

---

## 🚀 Getting Started

### Requirements

- Java 17 or higher
- Maven
- MySQL Server

### Clone the repository

```bash
git clone https://github.com/ganjorik/wushu-training-manager.git
```

### Configure the database

Create a MySQL database:

```sql
CREATE DATABASE wushu_training_db;
```

Open `application.properties` and replace:

```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

with your own MySQL credentials.

### Run the application

```bash
mvn spring-boot:run
```

The application will be available at:

```
http://localhost:8080/login
```

On the first startup, Liquibase will automatically create the database schema.
