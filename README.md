# Job Application Tracker API

A production-grade REST API for tracking job applications, built with **Java 17** and **Spring Boot 3**. Features JWT authentication, role-based access control, a full test suite, and a microservices architecture with service discovery, API gateway, inter-service communication, and event-driven messaging. Containerized with Docker and deployed to AWS through an automated CI/CD pipeline.

---

## Tech Stack

**Backend:** Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate, Spring MVC

**Microservices:** Eureka Service Discovery, Spring Cloud Gateway, OpenFeign, Apache Kafka

**Security:** JWT Authentication, BCrypt, Role-Based Access Control (RBAC), SecurityFilterChain

**Database:** MySQL, JPA Relationships (@OneToMany, @ManyToOne), JPQL, Pagination, Optimistic Locking

**Testing:** JUnit 5, Mockito, MockMvc, @SpringBootTest, @WebMvcTest

**DevOps:** Docker, Docker Compose, GitHub Actions CI/CD, AWS (EC2, RDS, ECR, Elastic Beanstalk)

---

## Architecture

```
Client (Postman / Frontend)
        │
        ▼
API Gateway (port 8080)
        │ routes via Eureka
        ▼
┌──────────────────────────────────┐
│  Application Tracker (port 8081) │
│  ├── Auth    (/auth/*)           │
│  ├── Applications (/applications)│
│  └── Interviews   (/interviews)  │
└──────────────────────────────────┘
        │                │
        ▼                ▼
Eureka Server       Apache Kafka
(port 8761)         (port 9092)
Service Registry    Event Messaging
```

- **Eureka Server** — services register on startup and discover each other automatically, no hardcoded addresses
- **Spring Cloud Gateway** — single entry point for all requests, routes to the correct service via Eureka
- **OpenFeign** — services call each other through typed interfaces, Feign handles HTTP and Eureka lookup behind the scenes
- **Apache Kafka** — application status changes publish to a topic, consumed asynchronously by a notification service with no tight coupling

---

## Features

- **JWT Authentication** — stateless token-based auth with Spring Security
- **Role-Based Access Control** — USER and ADMIN roles with endpoint-level restrictions
- **Per-User Data Isolation** — each user accesses only their own data via SecurityContextHolder
- **Nested Resources** — interviews belong to applications through JPA @OneToMany relationships
- **Filtering & Pagination** — query applications by status, company, date with paginated results
- **Event-Driven Messaging** — status changes publish to Kafka, consumed by notification service independently
- **Service Discovery** — all services register with Eureka, find each other automatically
- **API Gateway** — single entry point on port 8080, routes to correct service
- **Full Test Suite** — unit and integration tests that caught 4 real security bugs before production

---

## API Endpoints

### Auth
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/auth/register` | Register new user | Public |
| POST | `/auth/login` | Login and receive JWT | Public |
| POST | `/auth/admin-register` | Register admin | Public |
| GET | `/auth/validate` | Validate JWT token | Public |

### Applications
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/applications` | Get user's applications (paginated) | Authenticated |
| GET | `/applications/{id}` | Get single application | Owner only |
| POST | `/applications` | Create application | Authenticated |
| PUT | `/applications/{id}` | Update application | Owner only |
| DELETE | `/applications/{id}` | Delete application | Owner only |

### Interviews
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/applications/{id}/interviews` | Get interviews for application | Owner only |
| POST | `/applications/{id}/interviews` | Add interview | Owner only |
| PUT | `/interviews/{id}` | Update interview | Owner only |
| DELETE | `/interviews/{id}` | Delete interview | Owner only |

---

## Testing — What The Tests Found

A full test suite (JUnit 5, Mockito, MockMvc) covering unit and integration tests. The suite caught **4 real security bugs** before deployment:

1. Missing ownership check — one user could access another user's applications
2. Admin endpoints accessible without admin role
3. JWT filter allowing expired tokens through
4. Unauthorized users able to delete other users' interviews

Tests run automatically on every push through the CI/CD pipeline. No code deploys without passing.

---

## CI/CD Pipeline

```
Developer pushes to main
        │
        ▼
GitHub Actions triggers
        │
        ▼
Run full test suite (JUnit 5, Mockito, MockMvc)
        │ tests pass
        ▼
Build Docker image (multi-stage build)
        │
        ▼
Push image to AWS ECR
        │
        ▼
Deploy to AWS Elastic Beanstalk (EC2 + RDS)
        │
        ▼
Live in production
```

---

## Run Locally

### Prerequisites
- Java 17
- Maven
- MySQL
- Docker (for Kafka and microservices)

### 1. Clone the repository
```bash
git clone https://github.com/Advaitkataria/applicationTracker.git
cd applicationTracker
```

### 2. Configure MySQL
Create a database:
```sql
CREATE DATABASE applicationtracker;
```

Update `src/main/resources/application.properties` with your MySQL credentials.

### 3. Run with Docker Compose (recommended)
```bash
docker-compose up -d
```
This starts the application, MySQL, Kafka, and Zookeeper together.

### 4. Run without Docker
```bash
mvn spring-boot:run
```

### 5. Run the microservices stack
Start in this order:
```bash
# Terminal 1 — Eureka Server
cd eureka-server && mvn spring-boot:run

# Terminal 2 — Application Tracker
cd applicationTracker && mvn spring-boot:run

# Terminal 3 — API Gateway
cd api-gateway && mvn spring-boot:run
```

### 6. Test in Postman
```
POST http://localhost:8081/auth/register
Body: {"name": "Test User", "email": "test@gmail.com", "password": "password123"}

POST http://localhost:8081/auth/login
Body: {"email": "test@gmail.com", "password": "password123"}
→ Copy the token from response

GET http://localhost:8081/applications
Header: Authorization: Bearer <token>
```

Through Gateway:
```
POST http://localhost:8080/applicationtracker/auth/register
→ Same endpoints, routed through Gateway via Eureka
```

---

## Project Structure

```
applicationTracker/
├── src/main/java/org/example/applicationtracker/
│   ├── config/          SecurityConfig, JWT configuration
│   ├── controller/      REST controllers
│   ├── dto/             Data transfer objects
│   ├── exception/       Custom exceptions + global handler
│   ├── filter/          JWT authentication filter
│   ├── kafka/           Kafka producer and consumer
│   ├── model/           JPA entities
│   ├── repository/      Spring Data JPA repositories
│   └── service/         Business logic
├── src/test/            Unit and integration tests
├── Dockerfile           Multi-stage Docker build
├── docker-compose.yml   Full stack (app + MySQL + Kafka)
├── .github/workflows/   CI/CD pipeline
└── pom.xml
```

---

## Related Repositories

- [eureka-server](https://github.com/Advaitkataria/eureka-server) — Service discovery registry
- [api-gateway](https://github.com/Advaitkataria/api-gateway) — API Gateway with route configuration

---

## License

This project is for educational and portfolio purposes.
