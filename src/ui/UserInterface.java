package ui;

import model.User;
import service.UserService;
import service.BookingService;
import java.util.Scanner;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.time.LocalTime;

public class UserInterface {
    private final UserService userService;
    private final BookingService bookingService;
    private final Scanner scanner;
    private User currentUser;
    
    // ANSI Color Constants
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";

    // UI Helper Methods
    private void printLoadingMessage(String message) {
        System.out.println(ANSI_YELLOW + ANSI_BOLD + "[INFO] " + message + ANSI_RESET);
    }

    private void printSuccessMessage(String message) {
        System.out.println(ANSI_GREEN + ANSI_BOLD + "[SUCCESS] " + message + ANSI_RESET);
    }

    private void printErrorMessage(String message) {
        System.out.println(ANSI_RED + ANSI_BOLD + "[ERROR] " + message + ANSI_RESET);
    }

    private void printInfoMessage(String message) {
        System.out.println(ANSI_BLUE + ANSI_BOLD + "[INFO] " + message + ANSI_RESET);
    }

     private void printBusLogo() {
        System.out.println(ANSI_CYAN + ANSI_BOLD + 
            "+--------------------------------------------------------------+\n" +
            "|                                                              |\n" +
            "|   ███████╗██████╗  █████╗ ██████╗ ██████╗  ██████╗ ██╗    |\n" +
            "|   ██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔═══██╗██║    |\n" +
            "|   ███████╗██████╔╝███████║██████╔╝██║  ██║██║   ██║██║    |\n" +
            "|   ╚════██║██╔═══╝ ██╔══██║██╔══██╗██║  ██║██║   ██║██║    |\n" +
            "|   ███████║██║     ██║  ██║██║  ██║██████╔╝╚██████╔╝██║    |\n" +
            "|   ╚══════╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚═╝    |\n" +
            "|                                                              |\n" +
            "|                     Bus Booking System                        |\n" +
            "+--------------------------------------------------------------+" + ANSI_RESET);
    }

    private void printBusTypeInfo(String busType, double fare, int seats, String departure, String arrival) {
        String color;
        switch (busType) {
            case "Express":
                color = ANSI_YELLOW;
                break;
            case "Luxury":
                color = ANSI_PURPLE;
                break;
            case "AC_Sleeper":
                color = ANSI_CYAN;
                break;
            case "NonAC_Sleeper":
                color = ANSI_BLUE;
                break;
            default:
                color = ANSI_RESET;
        }

        System.out.println(color + ANSI_BOLD + "\n+--------------------------------------------------------------+");
        System.out.println("| " + busType + " Bus Information" + " ".repeat(40 - busType.length()) + "|");
        System.out.println("|--------------------------------------------------------------|");
        System.out.println("| Fare: ₹" + String.format("%.2f", fare) + " ".repeat(45 - String.format("%.2f", fare).length()) + "|");
        System.out.println("| Available Seats: " + seats + " ".repeat(35 - String.valueOf(seats).length()) + "|");
        System.out.println("| Departure: " + departure + " ".repeat(40 - departure.length()) + "|");
        System.out.println("| Arrival: " + arrival + " ".repeat(42 - arrival.length()) + "|");
        System.out.println("+--------------------------------------------------------------+" + ANSI_RESET);
    }

    private void printBookingDetails(String ticketNumber, double totalFare) {
        System.out.println(ANSI_GREEN + ANSI_BOLD + "\n+--------------------------------------------------------------+");
        System.out.println("|                     Booking Confirmed!                       |");
        System.out.println("|--------------------------------------------------------------|");
        System.out.println("| Ticket Number: " + ticketNumber + " ".repeat(35 - ticketNumber.length()) + "|");
        System.out.println("| Total Fare: ₹" + String.format("%.2f", totalFare) + " ".repeat(38 - String.format("%.2f", totalFare).length()) + "|");
        System.out.println("+--------------------------------------------------------------+" + ANSI_RESET);
    }

    public UserInterface() {
        this.userService = new UserService();
        this.bookingService = new BookingService();
        this.scanner = new Scanner(System.in);
    }
    
    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            // Handle exceptions if clearing fails (e.g., in unsupported environments)
            System.out.println("\n" + "=".repeat(50) + "\n"); // Print separator if clear fails
        }
    }
    
    public void start() {
        while (true) {
            clearScreen();
            if (currentUser == null) {
            showMainMenu();
            int choice = getValidIntInput();
            
            switch (choice) {
                case 1:
                    handleSignUp();
                    break;
                case 2:
                    handleLogin();
                    break;
                    case 3:
                        System.out.println("Thank you for using Sparrow Travellers!");
                        scanner.close();
                        return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                handleBooking(currentUser);
            }
        }
    }
    
    private void showMainMenu() {
        clearScreen();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "\n+--------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "|        Welcome to Sparrow Travellers!    |" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "+--------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "| 1." + ANSI_RESET + " Sign Up" + " ".repeat(24) + "|");
        System.out.println(ANSI_YELLOW + "| 2." + ANSI_RESET + " Login" + " ".repeat(26) + "|");
        System.out.println(ANSI_RED + "| 3." + ANSI_RESET + " Exit" + " ".repeat(27) + "|");
        System.out.println(ANSI_CYAN + ANSI_BOLD + "+--------------------------------------+" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "\nEnter your choice: " + ANSI_RESET);
    }
    
    private void handleSignUp() {
        clearScreen();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "\n+--------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "|              Sign Up               |" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "+--------------------------------------+" + ANSI_RESET);
        
        System.out.print(ANSI_YELLOW + "\nEnter name: " + ANSI_RESET);
        String name = scanner.nextLine();
        
        System.out.print(ANSI_YELLOW + "Enter mobile number: " + ANSI_RESET);
        String mobileNumber = getValidMobileNumber();
        
        System.out.print(ANSI_YELLOW + "Enter UserId: " + ANSI_RESET);
        String userId = getValidUserId();
        
        System.out.print(ANSI_YELLOW + "Enter password: " + ANSI_RESET);
        String password = scanner.nextLine();
        
        User newUser = new User(name, mobileNumber, userId, password);
        if (userService.registerUser(newUser)) {
            printSuccessMessage("Registration successful!");
        } else {
            printErrorMessage("Registration failed. User ID might already exist.");
        }
        System.out.println(ANSI_BLUE + "\nPress Enter to return to Main Menu..." + ANSI_RESET);
        scanner.nextLine(); // Wait for user to press Enter
    }
    
    private void handleLogin() {
        clearScreen();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "\n+--------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "|               Login                |" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "+--------------------------------------+" + ANSI_RESET);
        
        System.out.print(ANSI_YELLOW + "\nEnter UserId: " + ANSI_RESET);
        String userId = scanner.nextLine();
        
        System.out.print(ANSI_YELLOW + "Enter password: " + ANSI_RESET);
        String password = scanner.nextLine();
        
        User user = userService.loginUser(userId, password);
        if (user != null) {
            printSuccessMessage("Login successful!");
            currentUser = user;
        } else {
            printErrorMessage("Invalid credentials. Please try again.");
        }
        System.out.println(ANSI_BLUE + "\nPress Enter to continue..." + ANSI_RESET);
        scanner.nextLine(); // Wait for user to press Enter
    }
    
    private void handleBooking(User user) {
        while (true) {
            clearScreen();
            try {
                System.out.println(ANSI_CYAN + ANSI_BOLD + "\n+----------------------------------+" + ANSI_RESET);
                System.out.println(ANSI_CYAN + ANSI_BOLD + "|           Booking Menu           |" + ANSI_RESET);
                System.out.println(ANSI_CYAN + ANSI_BOLD + "+----------------------------------+" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + "| 1." + ANSI_RESET + " Book Tickets" + " ".repeat(16) + "|");
                System.out.println(ANSI_YELLOW + "| 2." + ANSI_RESET + " Cancel Tickets" + " ".repeat(14) + "|");
                System.out.println(ANSI_YELLOW + "| 3." + ANSI_RESET + " View Booking Details" + " ".repeat(6) + "|");
                System.out.println(ANSI_RED + "| 4." + ANSI_RESET + " Logout" + " ".repeat(21) + "|");
                System.out.println(ANSI_CYAN + ANSI_BOLD + "+----------------------------------+" + ANSI_RESET);
                System.out.print(ANSI_GREEN + "\nEnter your choice: " + ANSI_RESET);
                
                int choice = getValidIntInput();
                
                switch (choice) {
                    case 1:
                        bookTickets(user);
                        break;
                    case 2:
                        cancelTickets();
                        break;
                    case 3:
                        viewBookingDetails(user);
                        break;
                    case 4:
                        clearScreen();
                        printInfoMessage("Logging out...");
                        currentUser = null;
                        return;
                    default:
                        printErrorMessage("Invalid choice. Please try again.");
                        System.out.println(ANSI_BLUE + "Press Enter to continue..." + ANSI_RESET);
                        scanner.nextLine(); // Wait for user to press Enter
                }
            } catch (Exception e) {
                printErrorMessage("An error occurred: " + e.getMessage());
                System.out.println(ANSI_BLUE + "Press Enter to continue..." + ANSI_RESET);
                scanner.nextLine(); // Wait for user to press Enter
            }
        }
    }
    
    public void bookTickets(User user) {
        clearScreen();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "\n+--------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "|            Book Tickets            |" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "+--------------------------------------+" + ANSI_RESET);
        
        System.out.print(ANSI_YELLOW + "\nEnter source city: " + ANSI_RESET);
        String source = scanner.nextLine();
        user.setSource(source);
        
        System.out.print(ANSI_YELLOW + "Enter destination city: " + ANSI_RESET);
        String destination = scanner.nextLine();
        user.setDestination(destination);
        
        System.out.print(ANSI_YELLOW + "Enter date for ride (dd/mm/yyyy): " + ANSI_RESET);
        String dateStr;
        int date = 0;
        while (true) {
            dateStr = scanner.nextLine();
            try {
                if (dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    String[] parts = dateStr.split("/");
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    
                    // Get current date
                    java.time.LocalDate currentDate = java.time.LocalDate.now();
                    java.time.LocalDate bookingDate = java.time.LocalDate.of(year, month, day);
                    
                    if (day > 0 && day <= 31 && month > 0 && month <= 12 && 
                        !bookingDate.isBefore(currentDate)) {
                        date = day; // Store only the day number
                        break;
                    } else if (bookingDate.isBefore(currentDate)) {
                        printErrorMessage("Please enter today's or a future date.");
                        System.out.print(ANSI_YELLOW + "Enter valid date (dd/mm/yyyy): " + ANSI_RESET);
                    } else {
                        printErrorMessage("Enter valid date (dd/mm/yyyy).");
                        System.out.print(ANSI_YELLOW + "Enter valid date (dd/mm/yyyy): " + ANSI_RESET);
                    }
                } else {
                    printErrorMessage("Invalid date format. Please use dd/mm/yyyy.");
                    System.out.print(ANSI_YELLOW + "Enter valid date (dd/mm/yyyy): " + ANSI_RESET);
                }
            } catch (Exception e) {
                printErrorMessage("Invalid input. Please enter a valid date in dd/mm/yyyy format.");
                System.out.print(ANSI_YELLOW + "Enter valid date (dd/mm/yyyy): " + ANSI_RESET);
            }
        }
        user.setDate(date);
        
        List<Integer> availableBuses = bookingService.getAvailableBuses(source, destination, date);
        
        if (availableBuses.isEmpty()) {
            printInfoMessage("No buses available for this route on the selected date.");
            System.out.println(ANSI_BLUE + "\nPress Enter to return to Booking Menu..." + ANSI_RESET);
            scanner.nextLine(); // Wait for user to press Enter
            return;
        }
        
        printInfoMessage("Available bus numbers: " + availableBuses);
        
        int busNo;
        while (true) {
            System.out.print(ANSI_YELLOW + "Enter bus number to book: " + ANSI_RESET);
            // Consume newline left-over from previous nextInt()
            if (scanner.hasNextInt()) {
                busNo = scanner.nextInt();
                scanner.nextLine();
            } else {
                printErrorMessage("Invalid input. Please enter a number for the bus.");
                scanner.nextLine(); // Consume invalid input
                continue;
            }
            
            if (availableBuses.contains(busNo)) {
                break;
            } else {
                printErrorMessage("Invalid bus number. Please select from available buses.");
                printInfoMessage("Available bus numbers: " + availableBuses);
            }
        }
        
        System.out.println(ANSI_YELLOW + "\nSelect bus type:" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "  E - Express Bus" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "  L - Luxury Bus" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "  A - AC Sleeper Bus" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "  N - Non-AC Sleeper Bus" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "Enter your choice (E/L/A/N): " + ANSI_RESET);
        String busType = scanner.nextLine().toUpperCase();
        
        if (!busType.matches("[ELAN]")) {
            printErrorMessage("Invalid bus type. Please select from available options.");
            System.out.println(ANSI_BLUE + "Press Enter to return to Booking Menu..." + ANSI_RESET);
            scanner.nextLine(); // Wait for user to press Enter
            return;
        }
        
        System.out.print(ANSI_YELLOW + "Enter number of seats to book: " + ANSI_RESET);
        int noOfSeats;
         if (scanner.hasNextInt()) {
            noOfSeats = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } else {
            printErrorMessage("Invalid input. Please enter a number for the number of seats.");
            scanner.nextLine(); // Consume invalid input
            System.out.println(ANSI_BLUE + "\nPress Enter to return to Booking Menu..." + ANSI_RESET);
            scanner.nextLine(); // Wait for user to press Enter
            return;
        }
        
        if (bookingService.bookSeats(user, busNo, busType, noOfSeats, date)) {
            printSuccessMessage("Booking successful! Your ticket details will be shown in View Booking Details.");
        } else {
            printErrorMessage("Booking failed. Please try again.");
        }
        System.out.println(ANSI_BLUE + "\nPress Enter to return to Booking Menu..." + ANSI_RESET);
        scanner.nextLine(); // Wait for user to press Enter
    }
    
    private void cancelTickets() {
        clearScreen();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "\n+--------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "|           Cancel Tickets           |" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "+--------------------------------------+" + ANSI_RESET);
        if (currentUser == null) {
            printErrorMessage("Please login first to cancel tickets.");
            System.out.println(ANSI_BLUE + "\nPress Enter to return to Main Menu..." + ANSI_RESET);
            scanner.nextLine(); // Wait for user to press Enter
            return;
        }

        // First show all booked tickets
        viewBookingDetails(currentUser);
        
        System.out.println(ANSI_YELLOW + "\nOptions:" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "  C - Cancel a booking" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "  B - Go back to menu" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "Enter your choice: " + ANSI_RESET);
        String choice = scanner.nextLine();
        
        while (!choice.equalsIgnoreCase("C") && !choice.equalsIgnoreCase("B")) {
            printErrorMessage("Invalid input. Please enter 'C' or 'B'.");
             System.out.println(ANSI_YELLOW + "\nOptions:" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "  C - Cancel a booking" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "  B - Go back to menu" + ANSI_RESET);
             System.out.print(ANSI_GREEN + "Enter your choice: " + ANSI_RESET);
            choice = scanner.nextLine();
        }
        
        if (choice.equalsIgnoreCase("B")) {
            return;
        }
        
        // Ask for ticket number to cancel
        System.out.print(ANSI_YELLOW + "\nEnter the Ticket Number you wish to cancel (e.g., SPR13830001): " + ANSI_RESET);
        String ticketNumber = scanner.nextLine().trim().toUpperCase();
        
        while (true) {
            if (bookingService.validateTicketNumber(ticketNumber)) {
                break;
            } else {
                printErrorMessage("Invalid ticket number format or ticket not found.");
                System.out.print(ANSI_YELLOW + "Enter the Ticket Number you wish to cancel (e.g., SPR13830001): " + ANSI_RESET);
                ticketNumber = scanner.nextLine().trim().toUpperCase();
            }
        }
        
        // Get confirmation
        System.out.println(ANSI_YELLOW + "\nAre you sure you want to cancel your booking for ticket number " + ticketNumber + "?" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Please type 'Yes' to confirm or 'No' to cancel." + ANSI_RESET);
        String confirmation;
        while (true) {
            System.out.print(ANSI_GREEN + "Enter your choice: " + ANSI_RESET);
            confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("Yes") || confirmation.equalsIgnoreCase("No")) {
                break;
            } else {
                printErrorMessage("Please enter 'Yes' or 'No' only.");
                System.out.println(ANSI_YELLOW + "Are you sure you want to cancel your booking?" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + "Press Yes or No" + ANSI_RESET);
            }
        }
        
        if (!confirmation.equalsIgnoreCase("Yes")) {
            printInfoMessage("Cancellation cancelled.");
            System.out.println(ANSI_BLUE + "\nPress Enter to return to Booking Menu..." + ANSI_RESET);
            scanner.nextLine(); // Wait for user to press Enter
            return;
        }
        
        // Get booking details and cancel
        int bookingId = Integer.parseInt(ticketNumber.substring(3)) - 13830000;
        
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://ep-lucky-credit-a8tv1gm8-pooler.eastus2.azure.neon.tech:5432/userdata?user=seats_owner&password=npg_Nso9FKZyR7ST&sslmode=require&channel_binding=require");
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM BookingData WHERE idx = ? AND UserId = ?")) {
            
            pstmt.setInt(1, bookingId);
            pstmt.setString(2, currentUser.getUserId());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int busNo = rs.getInt("busNO");
                    String busType = rs.getString("bustype");
                    int noOfSeats = rs.getInt("NoOfSeats");
                    int date = rs.getInt("date");
                    
                    if (bookingService.cancelSeats(busNo, busType, noOfSeats, date, currentUser.getUserId()) &&
                        bookingService.deleteBooking(bookingId, currentUser.getUserId())) {
                        printSuccessMessage("Your booking is cancelled successfully.");
                    } else {
                         printErrorMessage("Failed to cancel booking. Please try again.");
                    }
                } else {
                    printErrorMessage("No booking found with this ticket number.");
                }
            }
        } catch (SQLException e) {
            printErrorMessage("Error cancelling booking: " + e.getMessage());
        }
         System.out.println(ANSI_BLUE + "\nPress Enter to return to Booking Menu..." + ANSI_RESET);
        scanner.nextLine(); // Wait for user to press Enter
    }
    
    private void viewBookingDetails(User user) {
        clearScreen();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "\n+--------------------------------------+" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "|           Booking Details          |" + ANSI_RESET);
        System.out.println(ANSI_CYAN + ANSI_BOLD + "+--------------------------------------+" + ANSI_RESET);
        String sql = "SELECT * FROM BookingData WHERE UserId = ?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://ep-lucky-credit-a8tv1gm8-pooler.eastus2.azure.neon.tech:5432/userdata?user=seats_owner&password=npg_Nso9FKZyR7ST&sslmode=require&channel_binding=require");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    printInfoMessage("No booking details found for your account.");
                } else {
                    System.out.println(ANSI_YELLOW + ANSI_BOLD + "\nYour Bookings:" + ANSI_RESET);
                    do {
                        int bookingId = rs.getInt("idx");
                        String source = rs.getString("Source");
                        String destination = rs.getString("Destination");
                        String busType = rs.getString("bustype");
                        int noOfSeats = rs.getInt("NoOfSeats");
                        int date = rs.getInt("date");
                        int busNo = rs.getInt("busNO");
                        
                        System.out.println(ANSI_GREEN + "\n+--------------------------------------+" + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "| Ticket Number: SPR" + (13830000 + bookingId) + " ".repeat(18 - String.valueOf(13830000 + bookingId).length()) + "|" + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "+--------------------------------------+" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "| Bus: " + busNo + " (" + source + " to " + destination + ")" + " ".repeat(29 - (String.valueOf(busNo).length() + source.length() + destination.length() + 6)) + "|" + ANSI_RESET);
                         System.out.println(ANSI_BLUE + "| Bus Type: " + busType + " ".repeat(27 - busType.length()) + "|" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "| Seats: " + noOfSeats + " ".repeat(30 - String.valueOf(noOfSeats).length()) + "|" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "| Date: " + date + " ".repeat(31 - String.valueOf(date).length()) + "|" + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "+--------------------------------------+" + ANSI_RESET);
                    } while (rs.next());
                }
            }
        } catch (SQLException e) {
             printErrorMessage("Error viewing booking details: " + e.getMessage());
        }
         System.out.println(ANSI_BLUE + "\nPress Enter to return to Booking Menu..." + ANSI_RESET);
        scanner.nextLine(); // Wait for user to press Enter
    }
    
    private String getValidMobileNumber() {
        while (true) {
            System.out.print(ANSI_YELLOW + "Enter valid 10-digit mobile number: " + ANSI_RESET);
            String mobile = scanner.nextLine();
            if (mobile.matches("\\d{10}")) {
                return mobile;
            }
        }
    }
    
    private String getValidUserId() {
        while (true) {
             System.out.print(ANSI_YELLOW + "Enter UserId: " + ANSI_RESET);
            String userId = scanner.nextLine();
            if (!userService.isUserIdExists(userId)) {
                return userId;
            }
            printErrorMessage("UserId already exists. Please enter another UserId.");
        }
    }
    
    private int getValidIntInput() {
        while (true) {
             System.out.print(ANSI_GREEN + "Enter your choice: " + ANSI_RESET);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printErrorMessage("Please enter a valid number.");
            }
        }
    }

    // Redundant method - cancelTickets handles the cancellation flow
    // public void cancelBooking(User user) {\n    //    clearScreen();\n    //    System.out.println(ANSI_CYAN + ANSI_BOLD + \"\\n--- Cancel Booking ---\" + ANSI_RESET);\n    //    System.out.println(ANSI_RED + \"This method is under development or redundant.\" + ANSI_RESET);\n    //    System.out.println(ANSI_BLUE + \"Press Enter to return to Booking Menu...\" + ANSI_RESET);\n    //    scanner.nextLine(); // Wait for user to press Enter\n    // }\n

} 