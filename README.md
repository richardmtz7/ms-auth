# Auth Service API

This project contains an authentication controller for handling user registration and login processes. The controller interacts with the service layer to create new users and authenticate existing ones.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
  - [Register a New User](#1-register-a-new-user)
  - [User Login](#2-user-login)
- [Classes Overview](#classes-overview)
- [How to Run](#how-to-run)
- [Dependencies](#dependencies)

## Technologies Used
- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Hibernate Validator (for input validation)
- JWT (for authentication)

## Project Structure

- **Controller**: Manages HTTP requests related to user authentication.
- **Service**: Handles business logic for user creation and authentication.
- **DTO**: Data Transfer Objects to encapsulate request and response data.

## API Endpoints

### 1. Register a New User

**URL:** `/api/auth/sign-up`  
**Method:** `POST`  
**Request Body:**  

{
  "username": "string",
  "password": "string",
  "email": "string"
}

### Response:

- HTTP Status: 201 Created
- Body: "User successfully registered"

### Description:
This endpoint allows users to create an account by providing valid credentials. It validates the input data using annotations such as @Valid and delegates the creation process to the service layer.

### 2. User Login
**URL:** `/api/auth/login`
**Method:** `POST`
**Request Body:**

{
  "username": "string",
  "password": "string"
}

### Response:

- HTTP Status: 200 OK
- Body:
{
  "token": "string",
  "expiresAt": "string"
}

### Description:
This endpoint allows users to log in by providing valid credentials. On successful login, it returns a JWT token, which can be used to authenticate subsequent API requests.

### Classes Overview
## AuthController
The main controller responsible for handling authentication-related requests.

## UserDetailServiceImpl
This service class implements the logic for user creation and login. It interacts with the database and handles JWT token generation for successful logins.

## DTOs
-AuthCreateUserRequest: Represents the data for creating a new user.
-AuthLoginRequest: Represents the login request data.
-AuthResponse: Contains the JWT token and other relevant login details.

## How to Run
1. Clone the repository.
2. Ensure you have Java 17 and Maven installed.
3. Run mvn spring-boot:run to start the application.
4. The API will be accessible at http://localhost:8080/api/auth.

## Dependencies
-Spring Boot Starter Web
-Spring Boot Starter Security
-Hibernate Validator
-JWT
