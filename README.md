# Job Application Tracker API

A secured, production-quality REST API for tracking job applications — built with Spring Boot 3, Spring Security, JWT authentication, and deployed to AWS with a complete CI/CD pipeline.

**Live API:** `http://job-tracker-env.eba-h6n4ciex.ca-central-1.elasticbeanstalk.com`

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Security + JWT Authentication**
- **Spring Data JPA / Hibernate**
- **MySQL (local) / AWS RDS (production)**
- **JUnit 5 + Mockito + MockMvc**
- **Docker + Docker Compose**
- **GitHub Actions CI/CD**
- **AWS ECR + Elastic Beanstalk**
- **Lombok + Maven**

---

## Features

### Security
- JWT authentication — stateless, no sessions
- BCrypt password encoding — passwords never stored as plain text
- Role-based access control — `ROLE_USER` and `ROLE_ADMIN`
- Data isolation — each user accesses only their own applications
- Ownership verification on all update and delete operations

### Testing
- JUnit 5 unit tests with Mockito for service layer
- MockMvc integration tests for HTTP layer and security
- Tests found and fixed four real security bugs including a missing ownership check that allowed unauthorized data deletion
- H2 in-memory database for fast, isolated test runs

### DevOps
- Multi-stage Docker build — image size reduced from ~500MB to ~150MB
- Docker Compose — one command starts Spring Boot + MySQL together
- GitHub Actions CI/CD — every push triggers automatic test run, Docker build, ECR push, and Elastic Beanstalk deployment
- Live on AWS — EC2 via Elastic Beanstalk, MySQL on RDS

---

## API Endpoints

### Auth (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login — returns JWT token |

### Applications (JWT required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/applications` | Get your applications (paginated) |
| GET | `/applications/with-interviews` | Get applications with all interviews |
| POST | `/applications` | Add new application |
| PUT | `/applications/{id}` | Update your application |
| DELETE | `/applications/{id}` | Delete your application |
| GET | `/applications/status/{status}` | Filter by status |
| GET | `/applications/jobTitle/{keyword}` | Search by job title |

### Interviews (JWT required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/applications/{appId}/interviews` | Get all interviews |
| POST | `/applications/{appId}/interviews` | Add interview round |
| PUT | `/applications/{appId}/interviews/{id}` | Update interview |
| DELETE | `/applications/{appId}/interviews/{id}` | Delete interview |

---

## How To Use

### Step 1 — Register
```json
POST /auth/register
{
    "name": "John Smith",
    "email": "john@gmail.com",
    "password": "pass123"
}
```

### Step 2 — Login
```json
POST /auth/login
{
    "email": "john@gmail.com",
    "password": "pass123"
}
```
Response:
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

### Step 3 — Add Authorization Header
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Step 4 — Add An Application
```json
POST /applications
Authorization: Bearer your_token

{
    "companyName": "Boeing",
    "jobTitle": "Java Developer Intern",
    "status": "Applied",
    "appliedDate": "2026-09-01",
    "notes": "Applied through LinkedIn",
    "salaryExpectation": 25000
}
```

---

## Running Locally With Docker

The easiest way to run the app locally — no Java or MySQL installation needed:

```bash
git clone https://github.com/Advaitkataria/applicationTracker.git
cd applicationTracker
docker-compose up
```

App starts at `http://localhost:8080`

To stop:
```bash
docker-compose down
```

To reset everything including database:
```bash
docker-compose down -v
```

---

## Running Locally Without Docker

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/Advaitkataria/applicationTracker.git
cd applicationTracker
```

**2. Create MySQL database**
```sql
CREATE DATABASE applicationTracker;
```

**3. Configure application.properties**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/applicationTracker
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.flyway.enabled=false
jwt.secret=YOUR_SECRET_KEY
```

**4. Run**
```bash
mvn spring-boot:run
```

---

## Running Tests

```bash
mvn test
```

Tests use H2 in-memory database — no MySQL required. Test suite covers:
- Service layer unit tests with Mockito
- HTTP layer integration tests with MockMvc
- Security tests — 401 without token, 403 for unauthorized access
- Ownership verification — users cannot access other users data

---

## CI/CD Pipeline

Every push to `main` branch automatically:

```
Push to GitHub
        ↓
GitHub Actions triggers
        ↓
Run full test suite (JUnit 5 + MockMvc)
        ↓
Tests pass → Build Docker image
        ↓
Push image to AWS ECR
        ↓
Deploy to AWS Elastic Beanstalk
        ↓
Live URL updated automatically
```

If any test fails — deployment stops immediately. No broken code ever reaches production.

---

## Project Structure

```
src/
├── main/java/org/example/applicationtracker/
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── ApplicationController.java
│   │   ├── AuthController.java
│   │   └── InterviewController.java
│   ├── filter/
│   │   └── JwtAuthFilter.java
│   ├── model/
│   │   ├── Application.java
│   │   ├── Interview.java
│   │   └── User.java
│   ├── repository/
│   │   ├── ApplicationRepository.java
│   │   ├── InterviewRepository.java
│   │   └── UserRepository.java
│   ├── service/
│   │   ├── ApplicationService.java
│   │   ├── AuthService.java
│   │   ├── InterviewService.java
│   │   └── JwtService.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ApplicationNotFoundException.java
│       ├── EmailAlreadyExistsException.java
│       └── UnauthorizedAccessException.java
│
└── test/java/org/example/applicationtracker/
    ├── service/
    │   └── ApplicationServiceTest.java
    └── controller/
        └── ApplicationControllerTest.java
```

---

## Architecture

```
HTTP Request
        ↓
JwtAuthFilter — validates token, sets user in SecurityContext
        ↓
SecurityFilterChain — checks authentication and authorization
        ↓
Controller — receives request, delegates to service
        ↓
Service — business logic, ownership verification
        ↓
Repository — database operations
        ↓
HTTP Response
```

---

## Key Technical Decisions

- **Stateless JWT** — no server-side sessions, scales horizontally
- **Constructor injection** — testable, immutable dependencies
- **SecurityContextHolder** — get current user anywhere without passing it around
- **JOIN FETCH** — eliminates N+1 queries when loading applications with interviews
- **@Transactional(readOnly=true)** — performance optimization for read operations
- **orElseThrow** — always throw meaningful exceptions, never return null
- **@ControllerAdvice** — centralized error handling, clean controllers
- **H2 for tests** — fast, isolated, no external database required
- **Multi-stage Docker build** — production image contains only runtime, not build tools
- **GitHub Actions** — tests gate every deployment, broken code never reaches production
