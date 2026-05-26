# EcommerceProjectMySQL

A production-grade **E-Commerce REST API** built with **Spring Boot**, **MySQL**, and **Gradle**. This project demonstrates enterprise-level backend architecture with complete CRUD operations, error handling, and database optimization.

## 🎯 Project Overview

- **Layered Architecture** (Controller → Service → Repository)
- **MySQL Database** integration with JPA/Hibernate
- **RESTful API** with proper HTTP semantics
- **Error Handling** with global exception management
- **Input Validation** with declarative annotations
- **Database Optimization** (N+1 problem solved)
- **Pagination & Filtering** support

## 📋 Features

### Core Functionality

- ✅ **Category Management** - Create, Read, Update, Delete categories
- ✅ **Product Management** - CRUD operations with relationships
- ✅ **Inventory Tracking** - Stock levels and updates
- ✅ **Advanced Search** - Find products by name, category, price range
- ✅ **Pagination** - Efficient data retrieval for large datasets
- ✅ **Error Handling** - Centralized exception management with meaningful responses

### Architecture Patterns

- ✅ **Layered Architecture** - Clear separation of concerns
- ✅ **Service Layer** - Business logic encapsulation
- ✅ **Repository Pattern** - Database abstraction
- ✅ **DTO Pattern** - Data transfer objects for API responses
- ✅ **Transaction Management** - ACID compliance with @Transactional
- ✅ **N+1 Optimization** - JOIN FETCH queries for performance

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│         REST API (HTTP Requests)        │
└────────────────┬────────────────────────┘
                 │
        ┌────────▼────────┐
        │    Controller    │  @RestController, @RequestMapping
        │  (HTTP Handling) │  Validates input, delegates to service
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │    Service      │  @Service, @Transactional
        │  (Business      │  Business logic, optimization,
        │   Logic)        │  transaction management
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │  Repository     │  @Repository, extends JpaRepository
        │  (DB Access)    │  Custom queries, pagination
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │   MySQL DB      │  Entity tables, relationships
        └─────────────────┘
```

## 📂 Project Structure

```
EcommerceProjectMySQL/
├── src/
│   ├── main/
│   │   ├── java/org/example/ecommerceprojectmysql/
│   │   │   ├── EcommerceProjectMySqlApplication.java    # Entry point
│   │   │   ├── model/
│   │   │   │   ├── Category.java                        # Entity
│   │   │   │   └── Product.java                         # Entity
│   │   │   ├── dto/
│   │   │   │   ├── CategoryDTO.java                     # Data Transfer Object
│   │   │   │   └── ProductDTO.java                      # Data Transfer Object
│   │   │   ├── controller/
│   │   │   │   ├── CategoryController.java              # HTTP endpoints
│   │   │   │   └── ProductController.java               # HTTP endpoints
│   │   │   ├── service/
│   │   │   │   ├── CategoryService.java                 # Business logic
│   │   │   │   └── ProductService.java                  # Business logic
│   │   │   ├── repository/
│   │   │   │   ├── CategoryRepository.java              # DB queries
│   │   │   │   └── ProductRepository.java               # DB queries
│   │   │   ├── exception/
│   │   │   │   ├── ResourceNotFoundException.java       # Custom exceptions
│   │   │   │   └── InvalidOperationException.java       # Custom exceptions
│   │   │   └── handler/
│   │   │       └── GlobalExceptionHandler.java          # Central error handling
│   │   └── resources/
│   │       ├── application.properties                   # Spring Boot config
│   │       ├── application-dev.properties               # Dev config
│   │       └── application-prod.properties              # Prod config
│   └── test/
│       └── java/...                                     # Integration tests
├── build.gradle                                          # Gradle dependencies
├── gradlew                                               # Gradle wrapper
├── README.md                                             # This file
└── HELP.md                                               # Quick help
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 5.7 or higher
- Gradle 8.x (or use gradlew)
- Git

### 1. Clone the Repository

```bash
cd /home/siddarth/projects/EcommerceProjectMySQL
```

### 2. Database Setup

Create MySQL database and tables:

```bash
mysql -u root -p
```

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- Create Category table
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Product table
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    category_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    INDEX idx_category (category_id),
    INDEX idx_name (name)
);
```

### 3. Configuration

Edit `src/main/resources/application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.springframework=INFO
logging.level.org.springframework.web.servlet.mvc.method.annotation=DEBUG
```

### 4. Build the Project

```bash
# Using gradlew
./gradlew build

# Or using gradle (if installed)
gradle build
```

### 5. Run the Application

```bash
# Using gradlew
./gradlew bootRun

# Or run the JAR
java -jar build/libs/EcommerceProjectMySQL-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 📡 API Endpoints

### Category Endpoints

| Method | Endpoint               | Description                        |
| ------ | ---------------------- | ---------------------------------- |
| GET    | `/api/categories`      | Get all categories with pagination |
| GET    | `/api/categories/{id}` | Get category by ID                 |
| POST   | `/api/categories`      | Create new category                |
| PUT    | `/api/categories/{id}` | Update category                    |
| DELETE | `/api/categories/{id}` | Delete category                    |

### Product Endpoints

| Method | Endpoint               | Description                      |
| ------ | ---------------------- | -------------------------------- |
| GET    | `/api/products`        | Get all products with pagination |
| GET    | `/api/products/{id}`   | Get product by ID                |
| POST   | `/api/products`        | Create new product               |
| PUT    | `/api/products/{id}`   | Update product                   |
| DELETE | `/api/products/{id}`   | Delete product                   |
| GET    | `/api/products/search` | Search products by name          |
| GET    | `/api/products/filter` | Filter by price range            |

### Example Requests

```bash
# Create a category
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and gadgets"
  }'

# Create a product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stockQuantity": 50,
    "categoryId": 1
  }'

# Get all products (paginated)
curl "http://localhost:8080/api/products?page=0&size=10"

# Search products
curl "http://localhost:8080/api/products/search?name=Laptop"
```

## 🧪 Testing

### Run Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests CategoryServiceTest

# Run with coverage
./gradlew test jacocoTestReport
```

## 📊 Database Schema

### Category Table

```sql
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### Product Table

```sql
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    category_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);
```

## 🚨 Error Handling

The application provides centralized error handling:

```
HTTP Request
    ↓
Controller (input validation)
    ↓
Service (business logic)
    ↓
Repository (database)
    ↓
Exception (if any)
    ↓
GlobalExceptionHandler (catches & converts to HTTP response)
    ↓
HTTP Response (with proper status code & error message)
```