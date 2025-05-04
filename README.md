# ChaTop Backend

![ChaTop Logo](./src/main/resources/img/chatop-banner.png)

You have just finished your previous project and you change your team to start a new project, very important for your business. Châtop wants to develop an online portal to allow potential tenants to contact the owners of the various properties they wish to rent.

## Tools required

Before installing the project, you must check that you have all the required tools :  

- Java Development

- Apache Maven

- MySQL

## Configuration

1. Create a new database in your Mysql console or Mysql workbench

Create a new database for your application and add all the tables to your database:

```sql
DROP DATABASE IF EXISTS `chatop`;

CREATE DATABASE `chatop`;

USE `chatop`;

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(255)        NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE rentals
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id    BIGINT                              NOT NULL,
    name        VARCHAR(255)                        NOT NULL,
    surface     INT CHECK (surface >= 1),
    price       DECIMAL(10, 2) CHECK (price > 0.0),
    picture     LONGBLOB,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE messages
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT                              NOT NULL,
    rental_id  BIGINT                              NOT NULL,
    message    TEXT                                NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_rental FOREIGN KEY (rental_id) REFERENCES rentals (id)
);
```

## Installation Procedure

**Cloning the project:**

1. `git clone https://github.com/killiadmin/chatop-backend.git`

**Set up the `application.properties` file:**

Once you have cloned the repository, you'll need to add the `application.properties` file on the `src/main/resources/` folder containing these properties:

```properties
# ==========================
# Application information
# ==========================
spring.application.name=your-application-name

# ==========================
# Server configuration
# ==========================
server.port=8080

# ==========================
# Database configuration
# ==========================
spring.datasource.url=jdbc:mysql://<DB_HOST>:<DB_PORT>/<DB_NAME>
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.sql.init.platform=mysql

# ==========================
# Configuration to JPA (Java Persistence API)
# ==========================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# ==========================
# Logs Configuration
# ==========================
logging.level.root=INFO
logging.level.com.yourcompany=DEBUG

# ==========================
# Console H2 (for development only)
# ==========================
spring.h2.console.enabled=true

# ==========================
# App configuration
# ==========================
app.secret-key=<YOUR_RANDOM_SECRET_KEY>
app.jwt.expiration=3600

# ==========================
# Spring MVC configuration
# ==========================
spring.mvc.pathmatch.matching-strategy=ant-path-matcher

# ==========================
# Swagger
# ==========================
springdoc.swagger-ui.enabled=true
```

2. Run the application `mvn spring-boot:run` in the project directory.

3. Access the Swagger URL to explore. `http://localhost:3001/swagger-ui/index.html`

## Dependencies

### Core Spring Boot Framework
- **Spring Boot Starter (Core)**: `spring-boot-starter`
- **Spring Boot Starter Web**: `spring-boot-starter-web`
- **Spring Security**: `spring-boot-starter-security`
- **Spring Boot Starter Data JPA (Database management)**: `spring-boot-starter-data-jpa`
- **OAuth 2.0 Resource Server**: `spring-boot-starter-oauth2-resource-server`

### Database
- **MySQL JDBC Connector**: `mysql-connector-java` (Version: 8.0.33)
- **Hibernate (ORM)**: `hibernate-core` (Version: 6.4.1.Final)
- **Jakarta Persistence API**: (Version: 3.1.0) `jakarta.persistence-api`
- **Jakarta Validation API**: (Version: 3.0.2) `jakarta.validation-api`
- **Hibernate Validator**: (Version: 8.0.0.Final) `hibernate-validator`

### Utility Libraries
- **Lombok (to reduce boilerplate code)**: (Only provided during development) `lombok`
- **Servlet API (Web application support)**: (Version: 4.0.1, provided only) `javax.servlet-api`

### Security and JWT (JSON Web Token) Management
- **JWT API**: (Version: 0.11.5) `jjwt-api`
- **JWT Implementation**: (Version: 0.11.5, runtime only) `jjwt-impl`
- **JWT Jackson Integration**: (Version: 0.11.5, runtime only) `jjwt-jackson`

### API Documentation
- **SpringDoc OpenAPI - Swagger UI**: (Version: 2.2.0) `springdoc-openapi-starter-webmvc-ui`
- **SpringDoc OpenAPI - Common Starter**: (Version: 2.2.0) `springdoc-openapi-starter-common`

### Testing
The following dependencies are required for testing (they are only used in a testing environment):
- **Spring Boot Starter Test**: `spring-boot-starter-test`
- **Spring Security Test**: `spring-security-test`

## Used Technologies
- **Java**: Version 17 (Defined in Maven properties)
- **Spring Boot**: Version 3.4.4 (Defined in the parent `spring-boot-starter-parent`)
