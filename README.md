# Bookstore API

---

## Overview

The **Bookstore API** is a RESTful backend service built with **Java (JAX-RS)** that manages:
- Books
- Authors
- Customers
- Shopping Carts
- Orders

It simulates a real-world bookstore e-commerce backend, fully tested with **Postman** and featuring **automated test scripts** and **clean JSON error handling**.

---

## Features

- CRUD operations for Books, Authors, Customers
- Add books to cart, update quantities, remove items
- Place orders from cart and view order history
- In-memory data storage (no external database)
- Proper REST conventions with JSON responses
- Global exception handling for errors like Not Found, Invalid Input, and Out of Stock
- Fully documented and tested

---

## Technologies Used

- Java 11+
- JAX-RS (Jersey)
- Grizzly HTTP Server
- Postman (for testing)

---

## Project Structure

```
src/
├── model/         # Book, Author, Customer, CartItem, Order
├── resource/      # REST API endpoints
├── exception/     # Custom exceptions and mappers
├── storage/       # InMemoryDatabase class
├── utils/         # EntityUtils helper class
└── Main.java      # Server startup
```

---

## Getting Started

### Requirements
- Java 11 or newer
- Maven installed
- Postman installed

### Running the API
1. Open the project in your IDE (IntelliJ, NetBeans, or Eclipse).
2. Build and Run `Main.java`.
3. The server will start at:  
   `http://localhost:8080/api`

### Testing the API
1. Open Postman.
2. Import the file:  
   `Bookstore API.postman_collection.json`
3. Use the organized folders (Books, Authors, Customers, Carts, Orders) to send requests and view responses.
4. Tests will automatically validate status codes and fields.

---

## Example Endpoints

- **Books**
    - `POST /api/books`
    - `GET /api/books`
    - `GET /api/books/{id}`
- **Authors**
    - `POST /api/authors`
    - `GET /api/authors`
- **Customers**
    - `POST /api/customers`
    - `GET /api/customers`
- **Carts**
    - `POST /api/customers/{id}/cart/items`
    - `GET /api/customers/{id}/cart`
- **Orders**
    - `POST /api/customers/{id}/orders`
    - `GET /api/customers/{id}/orders`
