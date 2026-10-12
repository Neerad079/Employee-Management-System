# Employee Management System

A robust full-stack Spring Boot enterprise web application designed to manage employee records. The system provides both a modern **RESTful API** (`/api/employee`) for headless integrations and mobile/frontend clients, as well as a server-side rendered **Thymeleaf Web UI** (`/ui/employees`) for browser-based operations.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Project Architecture & Directory Structure](#project-architecture--directory-structure)
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Application Setup](#application-setup)
- [How to Run the Application](#how-to-run-the-application)
- [API Details](#api-details)
  - [Endpoints Summary](#endpoints-summary)
  - [Web UI Routes](#web-ui-routes)
- [Sample Requests and Responses](#sample-requests-and-responses)
  - [1. Create a New Employee (POST)](#1-create-a-new-employee-post)
  - [2. Get All Employees (GET)](#2-get-all-employees-get)
  - [3. Get Employee by ID (GET)](#3-get-employee-by-id-get)
  - [4. Search Employees (GET)](#4-search-employees-get)
  - [5. Update an Employee (PUT)](#5-update-an-employee-put)
  - [6. Delete an Employee (DELETE)](#6-delete-an-employee-delete)
  - [7. Error Response Examples](#7-error-response-examples)
- [Exception Handling Approach](#exception-handling-approach)
  - [REST API Exception Flow](#rest-api-exception-flow)
  - [Thymeleaf UI Error Binding](#thymeleaf-ui-error-binding)
- [Testing Instructions](#testing-instructions)
  - [Automated Unit / Integration Tests](#automated-unit--integration-tests)
  - [Manual Testing with cURL / Postman](#manual-testing-with-curl--postman)
  - [Web UI Testing](#web-ui-testing)

---

## Project Overview

The Employee Management System streamlines HR and administrative tasks by offering complete employee lifecycle management (Create, Read, Update, Delete, and Search). 

It enforces strict business rules, such as:
- Unique employee codes and email addresses.
- Strict phone number format validation (Indian 10-digit mobile standards starting with digits 6–9).
- Mandatory valid email formatting.
- Temporal constraints ensuring joining dates cannot be set in the future.
- Strict employment status validation (`Active` or `Inactive`).

---

## Key Features

- **Dual-Interface Design**: Access functionality either through high-speed RESTful JSON endpoints or intuitive HTML forms via Thymeleaf.
- **Dynamic Search**: Case-insensitive substring search matching across employee first names, last names, email addresses, and departments.
- **Defensive Data Validation**: Utilizes Jakarta Bean Validation annotations alongside custom business logic validation at both service and database layers.
- **Centralized Exception Handling**: Standardized HTTP status codes (`400 Bad Request`, `404 Not Found`, `409 Conflict`) with clear, informative error messages and structured JSON responses (`ApiErrorResponse`).
- **Smooth Form UX**: Seamless validation error highlighting on forms with flash message persistence across redirects.

---

## Technology Stack

| Layer | Technology | Details |
| :--- | :--- | :--- |
| **Language** | Java 21 | Modern LTS Java runtime |
| **Framework** | Spring Boot 4.1.1 | Auto-configured, production-ready framework |
| **Web MVC** | Spring Web | REST controllers and MVC routing |
| **Template Engine** | Thymeleaf | Server-side template rendering (`spring-boot-starter-thymeleaf`) |
| **Data & ORM** | Spring Data JPA / Hibernate | Object-Relational Mapping with custom repository queries |
| **Database** | MySQL 8.x | Relational database (using `mysql-connector-j`) |
| **Validation** | Jakarta Bean Validation | Hibernate Validator (`spring-boot-starter-validation`) |
| **Boilerplate Helper**| Project Lombok | `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor` |
| **Build Tool** | Apache Maven | Build lifecycle management via Maven Wrapper (`mvnw` / `mvnw.cmd`) |



---

## Project Architecture & Directory Structure

```text
managment/
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/employee/managment/
│   │   │   ├── ManagmentApplication.java          # Spring Boot main entry point
│   │   │   ├── controller/
│   │   │   │   ├── employee.java                  # REST API controller (/api/employee)
│   │   │   │   └── EmployeePageController.java    # Thymeleaf UI controller (/ui/employees)
│   │   │   ├── exceptions/
│   │   │   │   ├── DuplicateFieldsException.java  # Custom exception for unique constraint conflicts
│   │   │   │   ├── EmployeeStatusException.java   # Custom exception for invalid employee status
│   │   │   │   ├── employeeNotFoundException.java # Custom exception for missing entity records
│   │   │   │   └── GlobalExceptionHandler.java    # Centralized @RestControllerAdvice error handler
│   │   │   ├── model/
│   │   │   │   ├── ApiErrorResponse.java          # Record DTO for JSON error response structure
│   │   │   │   └── employeeDB.java                # JPA Entity & validation definitions
│   │   │   ├── repository/
│   │   │   │   └── employeeRepo.java              # Spring Data JPA repository with JPQL search
│   │   │   └── services/
│   │   │       └── employeeService.java           # Core business logic and validation orchestration
│   │   └── resources/
│   │       ├── application.yaml                   # Server, DB, JPA, and Thymeleaf settings
│   │       ├── static/                            # Static assets (CSS, JS, images)
│   │       └── templates/
│   │           ├── hello.html                     # Demo / welcome page
│   │           └── employees/
│   │               ├── form.html                  # Add & Edit employee HTML form
│   │               ├── list.html                  # Employee directory list table
│   │               └── view.html                  # Individual employee details card
│   └── test/
│       └── java/employee/managment/
│           └── ManagmentApplicationTests.java    # Application context load verification test
├── pom.xml                                        # Maven project dependencies & plugins
├── mvnw                                           # Linux/macOS Maven wrapper script
└── mvnw.cmd                                       # Windows Maven wrapper batch script
```

---

## Prerequisites

Before setting up and running the application, ensure you have the following installed:

1. **Java Development Kit (JDK) 21** or higher:
   ```bash
   java -version
   ```
2. **MySQL Server 8.0+**: Ensure the MySQL service is running on `localhost:3306`.
3. **Maven 3.8+** *(Optional)*: A bundled wrapper (`./mvnw` or `mvnw.cmd`) is included in the project.
4. **Git**: For source version control.
5. **API Client**: cURL, Postman, or Thunder Client for testing endpoints.

---

## Database Setup

1. **Start MySQL Server** and open your MySQL client (MySQL CLI, MySQL Workbench, DBeaver, or phpMyAdmin).

2. **Create the Database**:
   The application connection string contains `createDatabaseIfNotExist=true`, which will create `employee_db` automatically if your MySQL user has sufficient privileges. However, you can create it manually:

   ```sql
   CREATE DATABASE IF NOT EXISTS employee_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Configure User Credentials** *(Optional)*:
   If you wish to create a dedicated user instead of `root`:
   ```sql
   CREATE USER 'employee_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON employee_db.* TO 'employee_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. **Verify Database Configuration in `application.yaml`**:
   Open [`src/main/resources/application.yaml`](src/main/resources/application.yaml) and adjust credentials to match your local setup:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/employee_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
       username: root
       password: password  # <--- Change this to your local MySQL password
       driver-class-name: com.mysql.cj.jdbc.Driver

     jpa:
       hibernate:
         ddl-auto: update  # Automatically creates or alters tables based on employeeDB entity
       show-sql: true
       properties:
         hibernate:
           format_sql: true
       open-in-view: false
   ```

### Entity Schema Definition (`employeedb` table)

| Field Name | Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- | :--- |
| `employeeId` | `employee_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique internal record ID |
| `employeeCode` | `employee_code` | INT | NOT NULL, UNIQUE | Organization employee identification code |
| `firstName` | `first_name` | VARCHAR(255) | NOT NULL | Employee first name |
| `lastName` | `last_name` | VARCHAR(255) | NULLABLE | Employee surname |
| `email` | `email` | VARCHAR(255) | NOT NULL, UNIQUE | Unique email address |
| `phoneNo` | `phone_no` | VARCHAR(255) | NOT NULL | 10-digit mobile number (`^[6-9][0-9]{9}$`) |
| `department` | `department` | VARCHAR(255) | NULLABLE | Department name (e.g. Engineering, HR) |
| `designation` | `designation` | VARCHAR(255) | NULLABLE | Job title (e.g. Senior Software Engineer) |
| `salary` | `salary` | BIGINT | NULLABLE | Annual or monthly compensation amount |
| `joiningDate` | `joining_date` | DATE | NOT NULL | Date joined (cannot be a future date) |
| `status` | `status_` | VARCHAR(255) | Must be `Active` or `Inactive` | Employment status |

---

## Application Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Neerad079/Employee-Management-System.git
   cd Employee-Management-System/managment
   ```

2. **Verify Configuration**:
   Ensure `src/main/resources/application.yaml` matches your MySQL database username and password.

3. **Build the Application**:
   - **Linux / macOS**:
     ```bash
     chmod +x mvnw
     ./mvnw clean compile
     ```
   - **Windows (PowerShell or CMD)**:
     ```cmd
     .\mvnw.cmd clean compile
     ```

---

## How to Run the Application

### Option 1: Running with Maven Wrapper (Development Mode)

- **Linux / macOS**:
  ```bash
  ./mvnw spring-boot:run
  ```
- **Windows (PowerShell / Command Prompt)**:
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

### Option 2: Running as an Executable JAR

1. Build the production package:
   ```bash
   # Linux / macOS
   ./mvnw clean package -DskipTests

   # Windows
   .\mvnw.cmd clean package -DskipTests
   ```
2. Launch the resulting JAR from the `target/` directory:
   ```bash
   java -jar target/managment-0.0.1-SNAPSHOT.jar
   ```

### Option 3: Running from an IDE

- Open the `managment` project directory in **IntelliJ IDEA**, **Eclipse**, or **VS Code**.
- Ensure Lombok annotation processing is enabled in your IDE settings.
- Run `employee.managment.ManagmentApplication.java`.

Once started, the application will be accessible at:
- **Default Server Port**: `8080`
- **Application Base URL**: `http://localhost:8080`
- **Thymeleaf UI Portal**: `http://localhost:8080/ui/employees`
- **REST API Base URL**: `http://localhost:8080/api/employee`

---

## API Details

### Endpoints Summary

| HTTP Method | Endpoint | Description | Request Body | Response (Success) |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/employee` | Retrieve a list of all employees | None | `200 OK` (JSON Array) |
| `GET` | `/api/employee/{id}` | Retrieve single employee by their ID | None | `200 OK` (JSON Object) |
| `GET` | `/api/employee/search?keyword={keyword}` | Search employees by first name, last name, email, or department | Query Param: `keyword` | `200 OK` (JSON Array) |
| `POST` | `/api/employee` | Register / create a new employee record | Employee JSON Object | `200 OK` (Created JSON Object) |
| `PUT` | `/api/employee/{id}` | Update existing employee record by ID | Updated Employee JSON Object | `200 OK` (Updated JSON Object) |
| `DELETE` | `/api/employee/{id}` | Remove employee record by ID | None | `200 OK` (Plain text message) |

### Web UI Routes

| HTTP Method | Route | Description |
| :--- | :--- | :--- |
| `GET` | `/ui/hello` | Welcome page demonstrating Thymeleaf model rendering |
| `GET` | `/ui/employees` | Dashboard listing all registered employees with action links |
| `GET` | `/ui/employees/new` | Displays employee registration form |
| `POST` | `/ui/employees` | Submits new employee registration form |
| `GET` | `/ui/employees/{id}` | Displays detailed employee profile card |
| `GET` | `/ui/employees/{id}/edit` | Displays populated employee edit form |
| `POST` | `/ui/employees/{id}/edit` | Submits edited employee form |
| `POST` | `/ui/employees/{id}/delete` | Form action to delete an employee |

---

## Sample Requests and Responses

### 1. Create a New Employee (POST)

**Endpoint:** `POST /api/employee`  
**Content-Type:** `application/json`

**Sample Request (cURL):**
```bash
curl -X POST http://localhost:8080/api/employee \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": 1001,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNo": "9876543210",
    "department": "Engineering",
    "designation": "Backend Engineer",
    "salary": 950000,
    "joiningDate": "2024-01-15",
    "status": "Active"
  }'
```

**Success Response (`200 OK`):**
```json
{
  "employeeId": 1,
  "employeeCode": 1001,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNo": "9876543210",
  "department": "Engineering",
  "designation": "Backend Engineer",
  "salary": 950000,
  "joiningDate": "2024-01-15",
  "status": "Active"
}
```

---

### 2. Get All Employees (GET)

**Endpoint:** `GET /api/employee`

**Sample Request (cURL):**
```bash
curl -X GET http://localhost:8080/api/employee
```

**Success Response (`200 OK`):**
```json
[
  {
    "employeeId": 1,
    "employeeCode": 1001,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNo": "9876543210",
    "department": "Engineering",
    "designation": "Backend Engineer",
    "salary": 950000,
    "joiningDate": "2024-01-15",
    "status": "Active"
  }
]
```

---

### 3. Get Employee by ID (GET)

**Endpoint:** `GET /api/employee/1`

**Sample Request (cURL):**
```bash
curl -X GET http://localhost:8080/api/employee/1
```

**Success Response (`200 OK`):**
```json
{
  "employeeId": 1,
  "employeeCode": 1001,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNo": "9876543210",
  "department": "Engineering",
  "designation": "Backend Engineer",
  "salary": 950000,
  "joiningDate": "2024-01-15",
  "status": "Active"
}
```

---

### 4. Search Employees (GET)

**Endpoint:** `GET /api/employee/search?keyword={keyword}`

Searches across `firstName`, `lastName`, `email`, and `department`.

**Sample Request (cURL):**
```bash
curl -X GET "http://localhost:8080/api/employee/search?keyword=Engineering"
```

**Success Response (`200 OK`):**
```json
[
  {
    "employeeId": 1,
    "employeeCode": 1001,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNo": "9876543210",
    "department": "Engineering",
    "designation": "Backend Engineer",
    "salary": 950000,
    "joiningDate": "2024-01-15",
    "status": "Active"
  }
]
```

---

### 5. Update an Employee (PUT)

**Endpoint:** `PUT /api/employee/1`  
**Content-Type:** `application/json`

**Sample Request (cURL):**
```bash
curl -X PUT http://localhost:8080/api/employee/1 \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": 1001,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNo": "9876543210",
    "department": "Engineering",
    "designation": "Lead Backend Engineer",
    "salary": 1200000,
    "joiningDate": "2024-01-15",
    "status": "Active"
  }'
```

**Success Response (`200 OK`):**
```json
{
  "employeeId": 1,
  "employeeCode": 1001,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNo": "9876543210",
  "department": "Engineering",
  "designation": "Lead Backend Engineer",
  "salary": 1200000,
  "joiningDate": "2024-01-15",
  "status": "Active"
}
```

---

### 6. Delete an Employee (DELETE)

**Endpoint:** `DELETE /api/employee/1`

**Sample Request (cURL):**
```bash
curl -X DELETE http://localhost:8080/api/employee/1
```

**Success Response (`200 OK`):**
```text
Employee with id 1 deleted successfully
```

---

### 7. Error Response Examples

#### Case A: Bean Validation Failure (Invalid Phone Number & Blank Email)
**Request Body:**
```json
{
  "employeeCode": 1002,
  "firstName": "Jane",
  "email": "",
  "phoneNo": "12345",
  "joiningDate": "2024-01-01",
  "status": "Active"
}
```
**Response Status:** `400 Bad Request`  
**Response Body:**
```json
{
  "status": 400,
  "message": "Email cannot be empty; Phone number must be a valid 10 digit phone number"
}
```

#### Case B: Invalid Employee Status
**Request Body:**
```json
{
  "employeeCode": 1003,
  "firstName": "Alex",
  "email": "alex@example.com",
  "phoneNo": "9812345678",
  "joiningDate": "2024-01-01",
  "status": "Probation"
}
```
**Response Status:** `400 Bad Request`  
**Response Body:**
```text
Employee Status Must be either Active or Inactive
```

#### Case C: Duplicate Employee Code or Email
**Request Body with existing email:**
```json
{
  "employeeCode": 9999,
  "firstName": "Jane",
  "email": "john.doe@example.com",
  "phoneNo": "9876543211",
  "joiningDate": "2024-01-01",
  "status": "Active"
}
```
**Response Status:** `409 Conflict`  
**Response Body:**
```text
Email already exists
```

#### Case D: Employee Record Not Found
**Request:** `GET /api/employee/999`  
**Response Status:** `404 Not Found`  
**Response Body:**
```text
Employee with id 999 not found
```

---

## Exception Handling Approach

The application implements a multi-layered exception handling strategy to deliver clear error reporting to both API consumers and UI users:

### REST API Exception Flow

The [`GlobalExceptionHandler`](src/main/java/employee/managment/exceptions/GlobalExceptionHandler.java) class is annotated with `@RestControllerAdvice`, intercepting thrown exceptions across all `@RestController` endpoints:

1. **`MethodArgumentNotValidException` (`400 Bad Request`)**:
   - Triggers when bean validation annotations (`@NotBlank`, `@Email`, `@Pattern`, `@PastOrPresent`, `@NotNull`) fail.
   - Extracts all field validation error messages, eliminates duplicates, and concatenates them with semicolons into the structured [`ApiErrorResponse`](src/main/java/employee/managment/model/ApiErrorResponse.java) record:
     ```json
     { "status": 400, "message": "Email cannot be empty; Phone number must be a valid 10 digit phone number" }
     ```

2. **`DuplicateFieldsException` (`409 Conflict`)**:
   - Thrown by `employeeService` when `existsByEmployeeCode` or `existsByEmail` checks detect duplicate records.
   - Responds with `HttpStatus.CONFLICT (409)` and the descriptive conflict message (e.g. `"Employee code already exists"` or `"Email already exists"`).

3. **`EmployeeStatusException` (`400 Bad Request`)**:
   - Thrown when the employee status string is neither `"Active"` nor `"Inactive"`.
   - Responds with `HttpStatus.BAD_REQUEST (400)`.

4. **`employeeNotFoundException` (`404 Not Found`)**:
   - Thrown when querying or operating on an employee ID that does not exist in the database.
   - Responds with `HttpStatus.NOT_FOUND (404)`.

### Thymeleaf UI Error Binding

In [`EmployeePageController`](src/main/java/employee/managment/controller/EmployeePageController.java):
- Form binding uses Spring's `BindingResult` alongside `@Valid`.
- When validation fails, execution returns to the form view (`employees/form`) with the model preserved, highlighting each invalid input in red with an inline error message.
- If a `DuplicateFieldsException` is thrown during save or update, the controller catches it and uses `result.rejectValue(ex.getField(), "duplicate", ex.getMessage())` to bind the error directly to the corresponding input box (`email` or `employeeCode`).
- On successful form operations, flash attributes (`redirectAttributes.addFlashAttribute("successMessage", ...)`) carry feedback to the refreshed listing page.

---

## Testing Instructions

### Automated Unit / Integration Tests

The project includes Spring Boot test support (`spring-boot-starter-data-jpa-test` and `spring-boot-starter-thymeleaf-test`).

To run the automated test suite:

- **Linux / macOS**:
  ```bash
  ./mvnw test
  ```
- **Windows (PowerShell / Command Prompt)**:
  ```powershell
  .\mvnw.cmd test
  ```

To run a specific test class:
```powershell
.\mvnw.cmd test -Dtest=ManagmentApplicationTests
```

### Manual Testing with cURL / Postman

You can import the following steps into Postman or run them in your terminal to verify API functionality:

```bash
# 1. Create Employee
curl -X POST http://localhost:8080/api/employee \
  -H "Content-Type: application/json" \
  -d '{"employeeCode": 101, "firstName": "Alice", "lastName": "Smith", "email": "alice@test.com", "phoneNo": "9123456780", "department": "Finance", "designation": "Analyst", "salary": 800000, "joiningDate": "2023-05-10", "status": "Active"}'

# 2. Get All Employees
curl -X GET http://localhost:8080/api/employee

# 3. Search for Employee by keyword
curl -X GET "http://localhost:8080/api/employee/search?keyword=Finance"

# 4. Attempt Duplicate Registration (Should return 409 Conflict)
curl -X POST http://localhost:8080/api/employee \
  -H "Content-Type: application/json" \
  -d '{"employeeCode": 101, "firstName": "Alice", "lastName": "Smith", "email": "alice@test.com", "phoneNo": "9123456780", "joiningDate": "2023-05-10", "status": "Active"}'

# 5. Update Employee
curl -X PUT http://localhost:8080/api/employee/1 \
  -H "Content-Type: application/json" \
  -d '{"employeeCode": 101, "firstName": "Alice", "lastName": "Smith", "email": "alice@test.com", "phoneNo": "9123456780", "department": "Finance", "designation": "Senior Analyst", "salary": 950000, "joiningDate": "2023-05-10", "status": "Active"}'

# 6. Delete Employee
curl -X DELETE http://localhost:8080/api/employee/1
```

### Web UI Testing

1. Start the application:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
2. Open your browser and navigate to:
   - **`http://localhost:8080/ui/employees`** – Check that the employee list loads.
   - Click **Add Employee** (or go to `http://localhost:8080/ui/employees/new`).
   - Try submitting invalid data (e.g., blank email or 5-digit phone number) to test inline form validation.
   - Submit a valid employee to verify redirection and the flash success notification.
   - Click **View** and **Edit** on a table row to test record details and editing.
   - Test the **Delete** action to confirm record removal.
