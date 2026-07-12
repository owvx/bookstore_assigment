# Bookstore REST API

A Spring Boot application built with Java 25 and Maven, designed to manage a bookstore inventory (Books and Authors) using Spring Data JPA with an in-memory H2 database.

---

## 🚀 Prerequisites

*   **Java 25** (installed and configured in your environment)
*   **Maven** 

---

## 🛠️ Getting Started & Running the Application

### 1. Start the Server
To run the Spring Boot application using Maven, execute the following command in the project root:

```bash
mvn spring-boot:run
```

Once started, the application will run locally on **port 8080**.

### 2. Access H2 Database Console
The in-memory database GUI can be accessed at:
👉 **[http://localhost:8080/h2-console](http://localhost:8080/h2-console)**

Use the following configuration details to connect:
*   **JDBC URL**: `jdbc:h2:mem:bookstore`
*   **Username**: `sa`
*   **Password**: *(leave blank)*

---

## 🧪 Running the Test Suite

To run the unit and integration tests (which cover validations, exception handling, and database relationship mapping), execute:

```bash
mvn test
```

---

## 📡 API Endpoints

### 1. Authors API (`/api/authors`)
*   `GET /api/authors` — Retrieve all authors.
*   `GET /api/authors/{id}` — Retrieve an author by ID (returns `404` if not found).
*   `POST /api/authors` — Create a new author.
*   `PUT /api/authors/{id}` — Update an existing author by ID.
*   `DELETE /api/authors/{id}` — Delete an author by ID.

#### Example Payload (POST / PUT):
```json
{
  "firstName": "Jane",
  "lastName": "Austen",
  "biography": "English novelist."
}
```

### 2. Books API (`/api/books`)
*   `GET /api/books` — Retrieve all books.
*   `GET /api/books/{id}` — Retrieve a book by ID.
*   `GET /api/books/isbn/{isbn}` — Retrieve a book by its unique ISBN.
*   `POST /api/books` — Create a new book (can be linked to an author by `authorId`).
*   `PUT /api/books/{id}` — Update a book by ID.
*   `DELETE /api/books/{id}` — Delete a book by ID.

#### Example Payload (POST / PUT):
```json
{
  "title": "Pride and Prejudice",
  "author": "Jane Austen",
  "isbn": "9780141439518",
  "price": 12.99,
  "authorId": 1
}
```
