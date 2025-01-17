# Freelancer Project Management System

---

## Overview
This project is a Freelancer Project Management
System that allows clients to create and manage
projects while freelancers can browse and apply
for available tasks. Admins oversee the platform
by verifying users and managing assignments.

## Features
- Authentication & Authorization
- User login & registration (Client, Freelancer, Admin)
- Role-based access control with Spring Security

### Project Management
- Clients can create, edit, and manage projects
- Freelancers can browse and apply for projects
- Admins approve project listings and assignments

### Freelancer Management
- Freelancers submit applications for projects
- Clients review applications and assign freelancers
- Admins verify freelancer profiles

### Admin Capabilities
- Review & approve projects
- Manage disputes between clients and freelancers
- Remove invalid projects and requests

---

### Tech Stack
- <b>Backend:</b> Spring Boot (Java), Hibernate (JPA), MySQL/PostgreSQL
- <b>Frontend:</b> Thymeleaf (integrated template engine)
- <b>Security:</b> Spring Security (JWT-based authentication)
- <b>Build Tools:</b> Maven / Gradle

---

### 1. Installation & Setup
- Clone the Repository
```sh
git clone [GitHub Repository Link]
cd /to/project/folder
```

### 2. Configure the Database
Modify the application.properties file with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dsproject2024
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build & Run the Application
```sh
mvn clean install
mvn spring-boot:run
```

---

## API Endpoints
<b><u>Authentication</u></b>

- <b>POST /auth/register – User registration</b>

- <b>POST /auth/login – User login</b>


<b><u>Projects</u></b>

- <b>POST /projects – Create a new project (Client)</b>

- <b>GET /projects/{id} – View project details</b>

- <b>PUT /projects/{id} – Update project (Client)</b>

<b><u>Requests & Assignments</u></b>

- <b>POST /requests – Freelancer applies for a project</b>

- <b>PUT /requests/{id}/approve – Client approves/rejects application</b>

- <b>POST /assignments – Admin assigns freelancer to a project</b>


### Full API documentation is available in the project report.
