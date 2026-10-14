# Book Management REST API

A RESTful backend application built with Spring Boot for managing personal book collections. Integrates with the Google Books API for book discovery and metadata, with secure user authentication and persistent storage using PostgreSQL.

## Features

### 1. User Accounts & Authentication

- **User Registration:** Users can create an account with a unique username.
- **Authentication:** Secures API endpoints using Spring Security and HTTP Basic authentication.
- **User-Specific Shelves:** Each authenticated user can manage their own personal book shelf.

### 2. Book Discovery

- **Book Search:** Search for books by title or ISBN.
- **Google Books Integration:** Retrieves book information and metadata from the Google Books API.
- **Book Importing:** Books selected from search results can be added to the authenticated user's shelf.

### 3. Shelf Management

- **Reading Status:** Track books as `PLAN_TO_READ`, `READING`, `COMPLETED`, or `DROPPED`.
- **Book Ratings:** Rate books from 1–5 or leave them unrated.
- **Shelf Updates:** Update ratings and reading statuses for existing shelf entries.
- **Book Removal:** Remove books from a personal shelf.
- **Duplicate Prevention:** Prevents the same Google Books entry from being added to a user's shelf multiple times.

### 4. API Validation & Error Handling

- **Request Validation:** Validates incoming API requests before processing.
- **Centralized Exception Handling:** Provides structured HTTP error responses for invalid requests and missing resources.

## Tech Stack

### Backend

- **Java 21**
- **Spring Boot**
- **Spring MVC**
- **Spring Data JPA**
- **Spring Security**

### Database

- **PostgreSQL**
- **Flyway** – Database schema migrations

### External API

- **Google Books API** – Book search and metadata retrieval

### Testing

- **JUnit 5**
- **Mockito**

### Build & Development

- **Gradle**
- **Docker & Docker Compose**