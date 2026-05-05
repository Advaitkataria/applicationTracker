# Job Application Tracker API

A secured, production-quality REST API for tracking job applications — built with Spring Boot 3, Spring Security, and JWT authentication.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Spring Data JPA / Hibernate**
- **MySQL**
- **Lombok**
- **Maven**

---

## Features

### Security
- JWT authentication — stateless, no sessions
- BCrypt password encoding — passwords never stored as plain text
- Role-based access control — `ROLE_USER` and `ROLE_ADMIN`
- Data isolation — each user accesses only their own applications
- Admin can view all applications across all users
- Ownership verification on update and delete

### API
- Full CRUD for job applications
- Link interview rounds to each application
- Filter by status, job title, company and date
- Pagination — handle large datasets efficiently
- Global exception handling with proper HTTP status codes
- Input validation with meaningful error messages

---

## API Endpoints

### Auth (Public — no token required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login — returns JWT token |
| POST | `/auth/admin-register` | Register admin user |

### Applications (JWT token required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/applications?page=0&size=10` | Get your applications (paginated) |
| GET | `/applications/with-interviews` | Get applications with all interview rounds |
| POST | `/applications` | Add new application |
| PUT | `/applications/{id}` | Update your application |
| DELETE | `/applications/{id}` | Delete your application |
| GET | `/applications/status/{status}` | Filter by status |
| GET | `/applications/jobTitle/{keyword}` | Search by job title |

### Interviews (JWT token required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/applications/{appId}/interviews` | Get all interviews for an application |
| POST | `/applications/{appId}/interviews` | Add interview round |
| PUT | `/applications/{appId}/interviews/{id}` | Update interview |
| DELETE | `/applications/{appId}/interviews/{id}` | Delete interview |

---

## How To Use

### Step 1 — Register
```bash
POST /auth/register
{
    "name": "John",
    "email": "john@gmail.com",
    "password": "pass123"
}
```

### Step 2 — Login
```bash
POST /auth/login
{
    "email": "john@gmail.com",
    "password": "pass123"
}
```

Response:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Step 3 — Use the token

Add to every request header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Step 4 — Add an application
```bash
POST /applications
Authorization: Bearer your_token

{
    "companyName": "Google",
    "jobTitle": "Java Developer Intern",
    "status": "Applied",
    "appliedDate": "2026-04-23",
    "notes": "Applied through LinkedIn",
    "salaryExpectation": 80000
}
```

### Step 5 — Log an interview round
```bash
POST /applications/1/interviews
Authorization: Bearer your_token

{
    "interviewDate": "2026-04-30",
    "type": "Phone",
    "outcome": "Waiting",
    "notes": "Initial HR screening"
}
```

---

## Validation Rules

### Application
- `companyName` — required
- `jobTitle` — required
- `status` — must be `Applied`, `Interviewing`, `Offer`, or `Rejected`
- `appliedDate` — required
- `notes` — required
- `salaryExpectation` — required, must be positive

### Interview
- `interviewDate` — required
- `type` — required (Phone / Technical / HR / Final)
- `outcome` — required (Passed / Failed / Waiting)

---

## Error Responses

All errors return a consistent format:

```json
{
    "status": 403,
    "message": "You can only delete your own applications",
    "timeStamp": "2026-04-23T12:00:00"
}
```

| Status | Meaning |
|--------|---------|
| 400 | Validation failed or duplicate email |
| 401 | Not logged in or wrong credentials |
| 403 | Trying to access someone else's data |
| 404 | Application or interview not found |
| 500 | Internal server error |

---

## Setup & Installation

### Prerequisites

- Java 17+
- MySQL 8+
- Maven

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/job-application-tracker.git
cd job-application-tracker
```

**2. Create MySQL database**
```sql
CREATE DATABASE applicationdb;
```

**3. Configure application.properties**
```properties
spring.application.name=applicationTracker
spring.datasource.url=jdbc:mysql://localhost:3306/applicationdb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

**4. Run the project**
```bash
mvn spring-boot:run
```

---

## Project Structure

```
src/main/java/org/example/applicationtracker/
├── controller/
│   ├── ApplicationController.java
│   ├── AuthController.java
│   └── InterviewController.java
├── model/
│   ├── Application.java
│   ├── Interview.java
│   └── User.java
├── repository/
│   ├── ApplicationRepository.java
│   ├── InterviewRepository.java
│   └── UserRepository.java
├── service/
│   ├── ApplicationService.java
│   ├── AuthService.java
│   ├── InterviewService.java
│   └── JwtService.java
├── filter/
│   └── JwtAuthFilter.java
├── config/
│   └── SecurityConfig.java
└── exception/
    ├── ApplicationNotFoundException.java
    ├── EmailAlreadyExistsException.java
    ├── UnauthorizedAccessException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
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
Controller — receives request, calls service
     ↓
Service — business logic, ownership verification
     ↓
Repository — database operations (MySQL)
     ↓
HTTP Response
```

---

## Key Technical Decisions

- **Stateless JWT** — no server-side sessions, scales horizontally
- **Constructor injection** — testable, immutable dependencies
- **`SecurityContextHolder`** — get current user anywhere without passing it around
- **`@ManyToOne` with `FetchType.LAZY`** — avoid N+1 query problem
- **`JOIN FETCH`** — single query when loading applications with interviews
- **`@Transactional(readOnly = true)`** — performance optimization for read operations
- **`orElseThrow`** — always throw meaningful exceptions, never return null
- **`@ControllerAdvice`** — centralized error handling, clean controllers
