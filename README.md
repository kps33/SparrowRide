# 🛣️ SparrowRide – Java + PostgreSQL Console App

SparrowRide is a modular Java console application simulating a ride management system backed by PostgreSQL. Built with layered design principles separating business logic, data handling, and user interaction.

---
## 📁 Project Structure

.
├── src/
│ ├── model/ # Data models (e.g., User, Ride)
│ ├── service/ # Business logic and database operations
│ ├── ui/ # Console UI and interaction logic
│ ├── util/ # Utilities (e.g., DB connection, config)
│ └── Sparrow.java # Main entry point
├── lib/
│ └── postgresql-42.7.7.jar # PostgreSQL JDBC Driver
├── bin/ # Compiled .class files (auto-generated)


---

## 🚀 Features

- JDBC-based PostgreSQL database integration  
- Ride-sharing backend logic (user, ride, booking)  
- Console-based interaction  
- Clean modular Java design  
- Extensible service and model layers

---

## 🎮 How to Run

### ✅ Compile the Project

<sub>For Windows:</sub>
```bash
javac -cp "lib/postgresql-42.7.7.jar" -d bin src\model\*.java src\service\*.java src\ui\*.java src\util\*.java src\Sparrow.java

<sub>For Linux/macOS:</sub>

javac -cp "lib/postgresql-42.7.7.jar" -d bin src/model/*.java src/service/*.java src/ui/*.java src/util/*.java src/Sparrow.java

▶️ Run the App
<sub>For Windows:</sub>

java -cp "bin;lib/postgresql-42.7.7.jar" Sparrow

<sub>For Linux/macOS:</sub>

java -cp "bin:lib/postgresql-42.7.7.jar" Sparrow

🛠️ Requirements
Java 8 or above

PostgreSQL installed and running

PostgreSQL JDBC driver (lib/postgresql-42.7.7.jar)

Terminal or IDE (IntelliJ, Eclipse, etc.)

📚 Class Overview
Sparrow.java
Main class that starts the application

Connects UI and service layers

model/
Contains data classes like User.java, Ride.java, etc.

service/
Logic for booking, searching rides, and user operations

JDBC interactions with the database

ui/
Presents menus and captures user input

Guides flow of interaction

util/
Handles database connection setup and configuration

E.g., DBUtil.java, Config.java

🧩 Extend the Project
Add login/signup and user profiles

Admin dashboard for ride and user management

GUI version using JavaFX or Swing

REST API integration for frontend communication

📌 Notes
Make sure to:

Set up your PostgreSQL DB and configure credentials in the appropriate config file

Use the correct schema matching the JDBC operations in your code

Adjust path separators (\ vs /) based on your OS


