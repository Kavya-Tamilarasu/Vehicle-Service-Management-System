# Car Service — User Management System

A robust User Management System built with **Spring Boot 3**, **Java 21**, and **MySQL**. This application provides a comprehensive solution for managing users, roles, and authentication in a car service ecosystem.

## 🚀 Features

- **User Registration & Login:** Secure authentication with BCrypt password encoding.
- **Role-Based Management:** Support for different user roles (USER, ADMIN).
- **Admin Dashboard:** Centralized interface for managing all registered users.
- **Profile Management:** Users can view and update their personal information.
- **RESTful API:** Clean API endpoints for integration with other services.
- **Modern UI:** Responsive frontend built with HTML5, CSS3, and JavaScript.

## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot 3.5.13
- **Security:** Spring Security 6 (BCrypt Encoding)
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA / Hibernate
- **Frontend:** HTML, CSS (Segoe UI), JavaScript (Fetch API)
- **Build Tool:** Maven

## 📋 Prerequisites

Before you begin, ensure you have the following installed on your Windows machine:

1.  **Java Development Kit (JDK) 21** or higher.
2.  **MySQL Server 8.0** or higher.
3.  **Maven** (optional, as the project includes the Maven Wrapper `mvnw.cmd`).

## ⚡ Quick Start

For a detailed walkthrough, please refer to the [Getting Started Guide](GETTING_STARTED.md).

1.  **Database Setup:**
    Create a database named `car_service_db` in your MySQL instance.
    ```sql
    CREATE DATABASE car_service_db;
    ```

2.  **Configuration:**
    Open `src/main/resources/application.properties` and update your MySQL credentials:
    ```properties
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

3.  **Run the Application:**
    Open your terminal (Command Prompt or PowerShell) in the project root and run:
    ```bash
    mvnw.cmd spring-boot:run
    ```

4.  **Access the App:**
    Open your browser and navigate to `http://localhost:8080`.

## 📂 Project Structure

- `src/main/java`: Backend source code (Controllers, Services, Models, Repositories).
- `src/main/resources/static`: Frontend assets (HTML, CSS, Images).
- `src/main/resources/application.properties`: Primary configuration file.

## 📄 License

This project is for educational purposes.
