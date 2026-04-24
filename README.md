# Job Application Tracker API

A REST API for tracking job applications built with Spring Boot.

## Tech Stack
- Java 17
- Spring Boot 3.4.1
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Lombok

## Security
- JWT Authentication
- Role based access control (USER / ADMIN)
- Each user sees only their own applications
- Admin can view all applications
- BCrypt password encoding

## API Endpoints

### Auth (Public)
POST /auth/register       → Register new user
POST /auth/login          → Login, returns JWT token
POST /auth/admin-register → Register admin user

### Applications (Requires JWT token)
GET    /applications              → Get your applications
POST   /applications              → Add new application
PUT    /applications/{id}         → Update your application
DELETE /applications/{id}         → Delete your application
GET    /applications/status/{s}   → Filter by status
GET    /applications/jobTitle/{k} → Search by job title

## Setup
1. Clone the repository
2. Create MySQL database: CREATE DATABASE applicationdb;
3. Update application.properties with your MySQL password
4. Run the project

## How To Use
1. Register: POST /auth/register
2. Login: POST /auth/login → copy the token
3. Add "Authorization: Bearer {token}" header to all requests
