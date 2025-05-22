# Sparrow Bus Booking System

A Java-based bus booking system that allows users to book and manage bus tickets.

## Features

- User registration and login
- Multiple bus types (Express, Luxury, AC Sleeper, Non-AC Sleeper)
- Seat booking and cancellation
- Fare calculation based on distance and bus type
- MySQL database integration

## Prerequisites

- Java 11 or higher
- Maven
- MySQL Server

## Database Setup

1. Create two databases:
   ```sql
   CREATE DATABASE UserData;
   CREATE DATABASE seats;
   ```

2. Create required tables in UserData:
   ```sql
   USE UserData;
   CREATE TABLE dataTable (
       name VARCHAR(100),
       mobileNumber VARCHAR(10),
       userId VARCHAR(50) PRIMARY KEY,
       password VARCHAR(50)
   );
   ```

3. Create required tables in seats:
   ```sql
   USE seats;
   CREATE TABLE listOfSeats1 (
       Express INT,
       Luxury INT,
       NonAC_Sleeper INT,
       AC_Sleeper INT
   );
   -- Repeat for listOfSeats2 to listOfSeats20
   ```

## Building and Running

1. Clone the repository
2. Navigate to project directory
3. Build the project:
   ```bash
   mvn clean package
   ```
4. Run the application:
   ```bash
   java -jar target/sparrow-bus-booking-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

## Project Structure

```
com.sparrow
├── model
│   ├── User.java
│   ├── Bus.java
│   ├── ExpressBus.java
│   ├── LuxuryBus.java
│   ├── ACSleeperBus.java
│   └── NonACSleeperBus.java
├── service
│   ├── UserService.java
│   └── BookingService.java
├── util
│   └── DatabaseUtil.java
├── ui
│   └── UserInterface.java
└── Sparrow.java
```

## Configuration

Update database credentials in `DatabaseUtil.java` if needed.

## License

This project is licensed under the MIT License.
