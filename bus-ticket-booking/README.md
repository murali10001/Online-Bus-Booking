# 🚌 Bus Ticket Booking Application — Spring Boot + MySQL

A RESTful Bus Ticket Booking System built with Spring Boot 3, JPA/Hibernate, and MySQL.

---

## 🛠️ Tech Stack

| Layer       | Technology              |
|-------------|------------------------|
| Framework   | Spring Boot 3.2         |
| Language    | Java 17                 |
| ORM         | Spring Data JPA (Hibernate) |
| Database    | MySQL 8.x               |
| Build Tool  | Maven                   |
| Test Client | Postman                 |

---

## ⚙️ Setup Instructions

### 1. MySQL Setup
```sql
CREATE DATABASE bus_ticket_db;
```
> Or leave it — `createDatabaseIfNotExist=true` in application.properties will auto-create it.

### 2. Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bus_ticket_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root   ← Change to your MySQL password
```

### 3. Run the Application
```bash
mvn spring-boot:run
```
Or build and run:
```bash
mvn clean package
java -jar target/bus-ticket-booking-1.0.0.jar
```

The app starts at: **http://localhost:8080**

---

## 📦 Database Tables (Auto-created)

- **buses** — Bus information (name, number, route, seats, price, timings)
- **passengers** — Passenger details (name, email, phone, address)
- **bookings** — Ticket bookings linking buses and passengers

---

## 🔁 API Endpoints — Postman Guide

> Base URL: `http://localhost:8080`
> Content-Type: `application/json`

---

### 🚍 BUS APIs

#### 1. Add a Bus
```
POST /api/buses
```
**Body:**
```json
{
  "busName": "Royal Travels",
  "busNumber": "TN01AB1234",
  "source": "Chennai",
  "destination": "Bangalore",
  "totalSeats": 40,
  "ticketPrice": 450.00,
  "departureTime": "08:00 AM",
  "arrivalTime": "02:00 PM"
}
```

---

#### 2. Get All Buses
```
GET /api/buses
```

---

#### 3. Get Active Buses Only
```
GET /api/buses/active
```

---

#### 4. Get Bus by ID
```
GET /api/buses/1
```

---

#### 5. Get Bus by Bus Number
```
GET /api/buses/number/TN01AB1234
```

---

#### 6. Search Buses by Route
```
GET /api/buses/search?source=Chennai&destination=Bangalore
```

---

#### 7. Search Available Buses (with seats filter)
```
GET /api/buses/available?source=Chennai&destination=Bangalore&seats=2
```

---

#### 8. Check Seat Availability
```
GET /api/buses/1/availability
```
**Response:**
```json
{
  "busId": 1,
  "busName": "Royal Travels",
  "totalSeats": 40,
  "availableSeats": 38,
  "bookedSeats": 2,
  "ticketPrice": 450.0
}
```

---

#### 9. Update Bus
```
PUT /api/buses/1
```
**Body:** (same as Add Bus)

---

#### 10. Deactivate Bus
```
DELETE /api/buses/1
```

---

### 👤 PASSENGER APIs

#### 1. Register Passenger
```
POST /api/passengers
```
**Body:**
```json
{
  "passengerName": "Ravi Kumar",
  "email": "ravi@example.com",
  "phoneNumber": "9876543210",
  "address": "123, Anna Nagar, Chennai",
  "age": "28",
  "gender": "Male"
}
```

---

#### 2. Get All Passengers
```
GET /api/passengers
```

---

#### 3. Get Passenger by ID
```
GET /api/passengers/1
```

---

#### 4. Get Passenger by Email
```
GET /api/passengers/email/ravi@example.com
```

---

#### 5. Update Passenger
```
PUT /api/passengers/1
```
**Body:** (same as Register Passenger)

---

#### 6. Delete Passenger
```
DELETE /api/passengers/1
```

---

### 🎫 BOOKING APIs

#### 1. Book a Ticket
```
POST /api/bookings
```
**Body:**
```json
{
  "busId": 1,
  "passengerId": 1,
  "numberOfSeats": 2,
  "journeyDate": "2025-12-25"
}
```
**Response includes PNR number, total fare, booking status.**

---

#### 2. Get All Bookings
```
GET /api/bookings
```

---

#### 3. Get Booking by ID
```
GET /api/bookings/1
```

---

#### 4. Get Booking by PNR Number
```
GET /api/bookings/pnr/PNR1734567890123
```

---

#### 5. Get All Bookings of a Passenger
```
GET /api/bookings/passenger/1
```

---

#### 6. Get Confirmed Bookings of a Passenger
```
GET /api/bookings/passenger/1/confirmed
```

---

#### 7. Get All Bookings of a Bus
```
GET /api/bookings/bus/1
```

---

#### 8. Cancel Ticket by Booking ID
```
PUT /api/bookings/1/cancel
```
> Seats are automatically restored to the bus.

---

#### 9. Cancel Ticket by PNR Number
```
PUT /api/bookings/pnr/PNR1734567890123/cancel
```

---

## ⚠️ Error Responses

All errors follow this format:
```json
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Not enough seats available. Available: 1, Requested: 3"
}
```

Validation errors:
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Validation Failed",
  "validationErrors": {
    "email": "Invalid email format",
    "phoneNumber": "Phone number must be 10 digits"
  }
}
```

---

## 🧪 Postman Quick Test Flow

1. `POST /api/buses` → Add a bus → note `busId`
2. `POST /api/passengers` → Register a passenger → note `passengerId`
3. `GET /api/buses/1/availability` → Check seats before booking
4. `POST /api/bookings` → Book ticket using `busId` + `passengerId` → note `pnrNumber`
5. `GET /api/bookings/pnr/{pnr}` → Verify booking
6. `PUT /api/bookings/pnr/{pnr}/cancel` → Cancel if needed
7. `GET /api/buses/1/availability` → Verify seats restored

---

## 📁 Project Structure

```
src/main/java/com/busticket/
├── BusTicketBookingApplication.java   ← Main class
├── JacksonConfig.java                 ← JSON config
├── model/
│   ├── Bus.java
│   ├── Passenger.java
│   └── Booking.java
├── repository/
│   ├── BusRepository.java
│   ├── PassengerRepository.java
│   └── BookingRepository.java
├── service/
│   ├── BusService.java
│   ├── PassengerService.java
│   └── BookingService.java
├── controller/
│   ├── BusController.java
│   ├── PassengerController.java
│   └── BookingController.java
└── exception/
    ├── ResourceNotFoundException.java
    ├── BusinessException.java
    └── GlobalExceptionHandler.java
```
