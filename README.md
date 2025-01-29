# Stock Microservice

The **Stock** microservice is responsible for inventory management in the database. Its main function is to register and manage items, categories, and brands, providing an efficient and scalable API for stock management.  

This service follows a hexagonal architecture, ensuring a clear separation between business logic and infrastructure layers. Additionally, it integrates with other microservices, such as the transactions service, to ensure consistency in inventory management.  

## Installation

To run the **Stock** microservice, follow these steps:

### 1. Clone the repository  
```sh
git clone https://github.com/julian98789/stock_microservice.git
cd stock-microservice
```

### 2. Configure database connection
Add the database connection in the `application-dev.properties` file with the following fields:

```
spring.datasource.url=jdbc:mysql://<YOUR_CONNECTION_URL>
spring.datasource.username=<YOUR_USERNAME>
spring.datasource.password=<YOUR_PASSWORD>
```

### 3. Add JWT secret key
Add the JWT secret key in the following field of the `application-dev.properties` file:

```
JWT_SECRET_KEY=<YOUR_JWT_SECRET_KEY>
```

### 4. Run the microservice  
Run the microservice locally with the following command:

```sh
gradle bootRun
```

## Configure Docker 

### 1. Create the `.env` file 
Create a `.env` file in the root of the project and add the following fields:

```
JAVA_APP_USER_PORT=<YOUR_SERVICE_PORT>
MYSQL_ROOT_PASSWORD=<YOUR_MYSQL_PASSWORD>
MYSQL_PORT=<YOUR_MYSQL_PORT>
MYSQL_DATABASE=<YOUR_DATABASE_NAME>
MYSQL_ROOT_USERNAME=<YOUR_MYSQL_USERNAME>
JWT_SECRET_KEY=<YOUR_JWT_SECRET_KEY>
```

### 2. Activate the `prod` profile
Activate the `prod` profile in the following field of the `application.properties` file: 
```
spring.profiles.active=prod
```

### 3. Build and run with Docker Compose  
Run the following commands to start the containers:

```sh
docker-compose up --build -d
```

To stop the containers:

```sh
docker-compose down
```

# Technologies Used

This project uses the following technologies for its development:

## Spring Boot

- **Description**: A framework for building Java applications quickly and easily.
- **Documentation**: [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## Docker

- **Description**: A platform that allows packaging applications and their dependencies into containers.
- **Documentation**: [Docker Documentation](https://docs.docker.com/)

## MySQL

- **Description**: A relational database management system that uses SQL to access and manage data.
- **Documentation**: [MySQL Documentation](https://dev.mysql.com/doc/)

# Project Microservices

This project consists of several microservices that work together. Below are the links to each of them:

- **Stock Microservice**: [stock_microservice](https://github.com/julian98789/stock_microservice.git)
- **User Microservice**: [user_microservice](https://github.com/julian98789/user_microservice.git)
- **Shopping Cart Microservice**: [shopping_cart_microservice](https://github.com/julian98789/shopping_cart_microservice.git)
- **Reports Microservice**: [reports_microservice](https://github.com/julian98789/reports_microservice.git)
- **Transaction Microservice**: [transaction_microservice](https://github.com/julian98789/transaction_microservice.git)
