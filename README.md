# 🎁 Charity Box Manager

A backend application for managing collection boxes used during fundraising events for charity organizations.

## ✅ Project Overview

This project was implemented as part of the LAT 2025 task.

The system allows you to:
- Create and manage fundraising events (each with a single-currency account)
- Register physical collection boxes (each with a unique identifier)
- Assign boxes to events — only empty boxes can be assigned
- Automatically empty and remove boxes if unregistered (without transferring money)
- Add money in multiple currencies to boxes
- Transfer collected money from boxes to events (with automatic exchange rate conversion using live API)
- Generate financial reports with total collected amounts per event

✅ All functional and non-functional requirements from LAT 2025 task have been met.

⚠️ This project assumes that each collection box is registered with a unique, user-provided identifier, 
while also having a system-generated internal boxId used internally.

---

## 🔧 Technologies

- Java 17
- Spring Boot
- Maven
- H2 in-memory database
- REST API (JSON)
- Lombok

---

## 🚀 How to Run

### Requirements:
- Java 17+
- Maven

### 1. Clone the repo

```bash
git clone https://github.com/Wojciech-Fortuna/charity-box-manager.git
cd charity-box-manager
```

### 2. Run the application

### On Linux/macOS:
```bash
./mvnw spring-boot:run
```

### On Windows:
```bash
mvnw.cmd spring-boot:run
```

### 3. API runs on:
`http://localhost:8080`

### 4. H2 Console:
`http://localhost:8080/h2-console`  
(JDBC URL: `jdbc:h2:mem:testdb`)

---

## 🌐 REST API Endpoints

### 1. Create a new fundraising event
`POST /api/events`
```json
{
  "name": "Help Ukraine",
  "currency": "PLN"
}
```
Example response:
```json
{
"eventId": 1,
"name": "Help Ukraine",
"currency": "PLN",
"accountAmount": 0,
"boxes": null
}
```

### 2. Register a new collection box
`POST /api/boxes`
```json
{
  "identifier": "BOX123"
}
```
Example response:
```json
{
"boxId": 1,
"identifier": "BOX123",
"event": null,
"moneyList": null,
"empty": true,
"assigned": false
}
```

### 3. List all collection boxes
`GET /api/boxes`  
Example response:
```json
[
  {
    "id": 1,
    "assigned": true,
    "empty": false
  }
]
```

### 4. Unregister a collection box
`DELETE /api/boxes/{boxId}`

### 5. Assign a box to an event
`POST /api/boxes/{boxId}/assign/{eventId}`

⚠️ The collection box must be empty before assignment.

### 6. Add money to a box
`POST /api/money/add`
```json
{
  "boxId": 1,
  "currency": "USD",
  "amount": 10.00
}
```
⚠️ You can only add money to a box that is assigned to a fundraising event.

### 7. Empty a collection box (transfer money to event)
`POST /api/boxes/{boxId}/empty`

⚠️ Fails if the box is not assigned.

### 8. Financial report
`GET /api/events/report`  
Example response:
```json
[
  {
    "name": "Help Ukraine",
    "amount": 2048.00,
    "currency": "PLN"
  }
]
```

---

## 🛡️ Validation & Error Handling

The application includes automatic validation of request payloads and global error handling.

### Validation

User inputs are validated using Jakarta Bean Validation:

- `@NotNull`, `@NotBlank` for required fields
- `@DecimalMin("0.01")` for monetary amounts
- Invalid requests result in `400 Bad Request`

Example error response:
```json
{
  "error": "amount: must be greater than or equal to 0.01"
}
```

### Error Handling

A `@RestControllerAdvice` handles common error cases globally:

- `IllegalArgumentException` — e.g. duplicate identifier, invalid logic
- `IllegalStateException` — e.g. assigning a non-empty box
- `HttpMessageNotReadableException` — malformed JSON or unknown enum value
- `MethodArgumentNotValidException` — DTO input validation failures
- `RuntimeException` — e.g. box or event not found

Example error response:
```json
{
  "error": "Box with this identifier already exists."
}
```

Responses include clear error messages and appropriate HTTP status codes (400, 404, etc.).

---

## 💱 Currency Support

- Supported currencies: **EUR**, **PLN**, **USD**
- Live rates fetched from: [https://latest.currency-api.pages.dev/v1/currencies/{code}.json](https://latest.currency-api.pages.dev/v1/currencies/usd.json)

---

## 🗂️ Database Schema

A visual representation of the database schema is available in:

docs/database-schema.png

---

## 🧪 Testing

### Unit Tests

The project includes unit tests for core service classes:

- `CollectionBoxServiceTest`
- `FundraisingEventServiceTest`
- `MoneyServiceTest`

Additionally, a basic `contextLoads()` test ensures that the Spring Boot application context starts successfully.

Tests can be run with:

#### On Linux/macOS:
```bash
./mvnw test
```

#### On Windows:
```bash
mvnw.cmd test
```

### Sample API Calls

You can test application using tools like `Postman` or `curl`.
