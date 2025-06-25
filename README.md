# 🛣️ SparrowRide – Java + PostgreSQL Console App

SparrowRide is a modular Java console application simulating a ride management system backed by PostgreSQL. Built with layered design principles separating business logic, data handling, and user interaction.

---

## 📁 Project Structure

```
.
├── src/
│   ├── model/        # Data models (e.g., User, Ride)
│   ├── service/      # Business logic and database operations
│   ├── ui/           # Console UI and interaction logic
│   ├── util/         # Utilities (e.g., DB connection, config)
│   └── Sparrow.java  # Main entry point
├── lib/
│   └── postgresql-42.7.7.jar  # PostgreSQL JDBC Driver
├── bin/             # Compiled .class files (auto-generated)
```

---

## 🚀 Features

- User Login/Signup and basic Authentication
- JDBC-based PostgreSQL database integration  
- Ride-sharing backend logic (user, ride, booking)  
- Console-based interaction  
- Clean modular Java design  
- Extensible service and model layers

---

## 🎮 How to Run

### ✅ Compile the Project

**Windows:**
```bash
javac -cp "lib/postgresql-42.7.7.jar" -d bin src\model\*.java src\service\*.java src\ui\*.java src\util\*.java src\Sparrow.java
```

**Linux/macOS:**
```bash
javac -cp "lib/postgresql-42.7.7.jar" -d bin src/model/*.java src/service/*.java src/ui/*.java src/util/*.java src/Sparrow.java
```

---

### ▶️ Run the App

**Windows:**
```bash
java -cp "bin;lib/postgresql-42.7.7.jar" Sparrow
```

**Linux/macOS:**
```bash
java -cp "bin:lib/postgresql-42.7.7.jar" Sparrow
```

---

## 🛠️ Requirements

- Java 8 or above  
- PostgreSQL JDBC driver (`lib/postgresql-42.7.7.jar`)  
- Terminal or IDE (IntelliJ, Eclipse, VS Code etc.)

---

## 📚 Class Overview

### `Sparrow.java`
- Main class that starts the application
- Connects UI and service layers

### `model/`
- Contains data classes like `User.java`, `Ride.java`, etc.

### `service/`
- Logic for booking, searching rides, and user operations
- JDBC interactions with the database

### `ui/`
- Presents menus and captures user input
- Guides flow of interaction

### `util/`
- Handles database connection setup and configuration
- E.g., `DBUtil.java`, `Config.java`

---

## 📌 Notes

- Ensure your PostgreSQL DB is set up and matches the schema your Java classes expect  
- Edit database credentials in the config class (usually in `util/`)  
- Use correct path separators based on your OS (`\` for Windows, `/` for Unix)

---

Happy coding! 💡
