# E-Commerce

## Overview

E-Commerce is a scalable and efficient online shopping platform built using modern technologies. It provides seamless payment processing, user notifications, an quick search API, and a secure authentication system, ensuring a smooth and fast user experience.

## Tech Stack

### **Frontend:**

- React.js
- CSS

### **Backend:**

- Spring Boot
- Firestore
- Redis (Caching & Streams for notifications)
- JUnit (Unit Testing)
- PostgreSQL (Database)
- Spring Data JPA (JPA Repository)

## Features

- **Payment Gateway Integration**: Secure payment processing for seamless transactions.
- **Scalable Notification Service**: Built using Redis Streams to handle high loads efficiently.
- **Automated Receipt Generation**: Generates and sends receipts upon successful transactions.
- **Search API**: Uses Firestore for fast and intelligent product searches.
- **Order Management**: Enables users to place, track, and manage orders.
- **Review Service**: Allows customers to provide feedback and rate products.
- **Wishlist & Cart Service**: Save products for later and manage cart items effortlessly.
- **Custom OTP-Based Authentication**: Ensures secure login and hassle free user verification.
- **Performance Optimization**:
  - **Browser-Side Caching**: Reduces redundant API calls for a faster frontend experience.
  - **Server-Side Caching**: Optimized response times using Redis.
- **Extensive Unit Testing**: 35 unit tests written using JUnit to ensure code reliability.
- **User Feedback and Queries**: Users Feedback and queries feature.

![image](https://github.com/user-attachments/assets/4e34f899-e14f-4681-8971-1fda478822bb)

##
### **Backend Setup:**

1. Clone the repository:
2. Configure Firestore, Redis, PostgreSQL and Java Mail Sender in `application.properties`.
3. Build and run the Spring Boot application:
   ```sh
   cd ecommerce
   mvn clean install
   mvn spring-boot:run
   ```

### **Frontend Setup:**

1. Navigate to the frontend directory:
   ```sh
   cd client
   npm install
   npm start
   ```

## Running Tests

To run unit tests using JUnit:

```sh
mvn test
```

