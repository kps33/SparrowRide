package service;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;

public class BookingService {
    private static final String ROUTE_DB_URL = "jdbc:mysql://localhost:3306/route";
    private static final String SEATS_DB_URL = "jdbc:mysql://localhost:3306/seats";
    private static final String USER_DB_URL = "jdbc:mysql://localhost:3306/UserData";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "ZeEl@51271895@";
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";

    private void printBusLogo() {
        System.out.println(ANSI_CYAN + ANSI_BOLD + 
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║                                                              ║\n" +
            "║   ███████╗██████╗  █████╗ ██████╗ ██████╗  ██████╗ ██╗    ║\n" +
            "║   ██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔═══██╗██║    ║\n" +
            "║   ███████╗██████╔╝███████║██████╔╝██║  ██║██║   ██║██║    ║\n" +
            "║   ╚════██║██╔═══╝ ██╔══██║██╔══██╗██║  ██║██║   ██║██║    ║\n" +
            "║   ███████║██║     ██║  ██║██║  ██║██████╔╝╚██████╔╝██║    ║\n" +
            "║   ╚══════╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚═╝    ║\n" +
            "║                                                              ║\n" +
            "║                     Bus Booking System                        ║\n" +
            "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

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

    public List<Integer> getAvailableBuses(String source, String destination, int selectedDate) {
        printBusLogo();
        printLoadingMessage("Searching for buses...");
        List<Integer> availableBuses = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(ROUTE_DB_URL, DB_USER, DB_PASS)) {
            for (int i = 1; i <= 20; i++) {
                String firstQuery = "SELECT * FROM route" + i + " ORDER BY distance ASC LIMIT 1";
                String lastQuery = "SELECT * FROM route" + i + " ORDER BY distance DESC LIMIT 1";
                String routeQuery = "SELECT * FROM route" + i + " WHERE name = ? OR name = ?";
                
                String firstCity = null;
                int firstDist = 0;
                String lastCity = null;
                int lastDist = 0;
                
                // Get first city details
                try (Statement stmt = conn.createStatement();
                     ResultSet firstRs = stmt.executeQuery(firstQuery)) {
                    if (firstRs.next()) {
                        firstCity = firstRs.getString("name");
                        firstDist = firstRs.getInt("distance");
                    }
                }
                
                // Get last city details
                try (Statement stt = conn.createStatement();
                     ResultSet lastRs = stt.executeQuery(lastQuery)) {
                    if (lastRs.next()) {
                        lastCity = lastRs.getString("name");
                        lastDist = lastRs.getInt("distance");
                    }
                }
                
                if (firstCity != null && lastCity != null) {
                    try (PreparedStatement pstmt = conn.prepareStatement(routeQuery)) {
                        pstmt.setString(1, source);
                        pstmt.setString(2, destination);
                        
                        try (ResultSet routeRs = pstmt.executeQuery()) {
                            int sourceDist = -1;
                            int destDist = -1;
                            String sourceName = "";
                            
                            while (routeRs.next()) {
                                String name = routeRs.getString("name");
                                int distance = routeRs.getInt("distance");
                                
                                if (name.equalsIgnoreCase(source)) {
                                    sourceDist = distance;
                                    sourceName = name;
                                }
                                if (name.equalsIgnoreCase(destination)) {
                                    destDist = distance;
                                }
                            }
                            
                            if (sourceDist != -1 && destDist != -1) {
                                int totalDistance = Math.abs(destDist - sourceDist);
                                int disBefore;
                                
                                if (sourceName.equalsIgnoreCase(firstCity)) {
                                    disBefore = Math.abs(firstDist - sourceDist);
                                    printInfoMessage("Bus " + i + " (" + firstCity + " to " + lastCity + ")");
                                } else {
                                    disBefore = Math.abs(lastDist - destDist);
                                    printInfoMessage("Bus " + i + " (" + lastCity + " to " + firstCity + ")");
                                }
                                
                                printInfoMessage("Route: " + source + " to " + destination);
                                printInfoMessage("Distance: " + totalDistance + " km");
                                
                                availableBuses.add(i);
                                showBusTypes(i, totalDistance, disBefore, selectedDate);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            printErrorMessage("Error getting available buses: " + e.getMessage());
        }
        
        return availableBuses;
    }
    
    private void showBusTypes(int busNo, int distance, int disBefore, int selectedDate) {
        try (Connection conn = DriverManager.getConnection(SEATS_DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM listOfSeats" + busNo + " WHERE date = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selectedDate);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int expressSeats = rs.getInt("Express");
                        int luxurySeats = rs.getInt("Luxury");
                        int acSleeperSeats = rs.getInt("AC_Sleeper");
                        int nonAcSleeperSeats = rs.getInt("NonAC_Sleeper");
                        
                        if (expressSeats > 0 || luxurySeats > 0 || acSleeperSeats > 0 || nonAcSleeperSeats > 0) {
                            printInfoMessage("Available Bus Types for Bus " + busNo + " on date " + selectedDate + ":");
                            
                            if (expressSeats > 0) {
                                printBusTypeInfo("Express", calculateFare("E", distance), expressSeats,
                                    getDepartureTime(disBefore, busNo, 0), getArrivalTime(disBefore + distance, busNo, 0));
                            }
                            
                            if (luxurySeats > 0) {
                                printBusTypeInfo("Luxury", calculateFare("L", distance), luxurySeats,
                                    getDepartureTime(disBefore, busNo, 1), getArrivalTime(disBefore + distance, busNo, 1));
                            }
                            
                            if (acSleeperSeats > 0) {
                                printBusTypeInfo("AC_Sleeper", calculateFare("A", distance), acSleeperSeats,
                                    getDepartureTime(disBefore, busNo, 2), getArrivalTime(disBefore + distance, busNo, 2));
                            }
                            
                            if (nonAcSleeperSeats > 0) {
                                printBusTypeInfo("NonAC_Sleeper", calculateFare("N", distance), nonAcSleeperSeats,
                                    getDepartureTime(disBefore, busNo, 3), getArrivalTime(disBefore + distance, busNo, 3));
                            }
                        } else {
                            printErrorMessage("No seats available for Bus " + busNo + " on date " + selectedDate);
                        }
                    } else {
                        printErrorMessage("No seat data found for Bus " + busNo + " on date " + selectedDate);
                    }
                }
            }
        } catch (SQLException e) {
            printErrorMessage("Error showing bus types: " + e.getMessage());
        }
    }
    
    private String getDepartureTime(int distance, int busNo, int busType) {
        try {
            String csvFile = "G:\\tempppppp\\study\\techworld\\ProgrammingProject\\oops\\UpdatedSparrow\\updated sparrow\\src\\k.csv";
            String line = "";
            String cvsSplitBy = ",";
            List<String[]> records = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);
                    records.add(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "08:00"; // Default time if file not found
            }

            String[] st = records.get(busNo - 1);
            String time = st[busType];
            LocalTime departureTime = LocalTime.of(
                Integer.parseInt(time.substring(0, 2)),
                Integer.parseInt(time.substring(3))
            );
            
            return departureTime.toString();
        } catch (Exception e) {
            System.err.println("Error getting departure time: " + e.getMessage());
            e.printStackTrace();
            return "08:00"; // Default time if any error
        }
    }
    
    private String getArrivalTime(int distance, int busNo, int busType) {
        try {
            String csvFile = "G:\\tempppppp\\study\\techworld\\ProgrammingProject\\oops\\UpdatedSparrow\\updated sparrow\\src\\k.csv";
            String line = "";
            String cvsSplitBy = ",";
            List<String[]> records = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);
                    records.add(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "10:00"; // Default time if file not found
            }

            String[] st = records.get(busNo - 1);
            String time = st[busType];
            LocalTime departureTime = LocalTime.of(
                Integer.parseInt(time.substring(0, 2)),
                Integer.parseInt(time.substring(3))
            );
            
            double speed = 1.5; // Speed in km per minute
            double travelTimeInMinutes = distance / speed;
            LocalTime arrivalTime = departureTime.plusMinutes((long) travelTimeInMinutes);
            
            return arrivalTime.toString();
        } catch (Exception e) {
            System.err.println("Error getting arrival time: " + e.getMessage());
            e.printStackTrace();
            return "10:00"; // Default time if any error
        }
    }
    
    public int getDistance(String source, String destination) {
        for (int i = 1; i <= 20; i++) {
            String sql = "SELECT * FROM route" + i + " WHERE name = ? OR name = ?";
            try (Connection conn = DriverManager.getConnection(ROUTE_DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, source);
                pstmt.setString(2, destination);
                
                ResultSet rs = pstmt.executeQuery();
                int sourceDist = -1;
                int destDist = -1;
                
                while (rs.next()) {
                    String name = rs.getString("name");
                    int distance = rs.getInt("distance");
                    
                    if (name.equalsIgnoreCase(source)) {
                        sourceDist = distance;
                    }
                    if (name.equalsIgnoreCase(destination)) {
                        destDist = distance;
                    }
                }
                
                if (sourceDist != -1 && destDist != -1) {
                    return Math.abs(destDist - sourceDist);
                }
            } catch (SQLException e) {
                System.err.println("Error getting distance from route" + i + ": " + e.getMessage());
            }
        }
        return 0;
    }
    
    public boolean bookSeats(User user, int busNo, String busType, int noOfSeats, int date) {
        if (busNo < 1 || busNo > 20) {
            printErrorMessage("Invalid bus number. Please enter a number between 1 and 20.");
            return false;
        }
        
        if (noOfSeats <= 0) {
            printErrorMessage("Invalid number of seats. Please enter a positive number.");
            return false;
        }

        int distance = getDistance(user.getSource(), user.getDestination());
        if (distance <= 0) {
            printErrorMessage("Invalid route. Could not calculate distance between " + user.getSource() + " and " + user.getDestination());
            return false;
        }
        user.setDistance(distance);

        String type = getBusTypeString(busType);
        int availableSeats = getAvailableSeats(busNo, busType, date);
        
        if (noOfSeats > availableSeats) {
            printErrorMessage("Not enough seats available. Only " + availableSeats + " seats left.");
            return false;
        }

        String sql = "UPDATE listOfSeats" + busNo + " SET " + type + " = " + type + " - ? WHERE date = ?";
        
        try (Connection conn = DriverManager.getConnection(SEATS_DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noOfSeats);
            pstmt.setInt(2, date);
            
            int updatedRows = pstmt.executeUpdate();
            
            if (updatedRows > 0) {
                double busFare = calculateFare(busType, distance);
                user.setBusFare(busFare);
                insertBookingDetails(user, busNo, busType, noOfSeats, date);
                return true;
            }
        } catch (SQLException e) {
            printErrorMessage("Error booking seats: " + e.getMessage());
        }
        return false;
    }
    
    private void insertBookingDetails(User user, int busNo, String busType, int noOfSeats, int date) {
        String sql = "INSERT INTO BookingData (idx, UserId, Source, Destination, bustype, NoOfSeats, date, busNO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(USER_DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int nextIdx = getNextIdx(conn);
            
            pstmt.setInt(1, nextIdx);
            pstmt.setString(2, user.getUserId());
            pstmt.setString(3, user.getSource());
            pstmt.setString(4, user.getDestination());
            pstmt.setString(5, busType);
            pstmt.setInt(6, noOfSeats);
            pstmt.setInt(7, date);
            pstmt.setInt(8, busNo);
            
            pstmt.executeUpdate();
            
            double totalFare = user.getBusFare() * noOfSeats;
            String ticketNumber = "SPR" + (13830000 + nextIdx);
            printBookingDetails(ticketNumber, totalFare);
        } catch (SQLException e) {
            printErrorMessage("Error inserting booking details: " + e.getMessage());
        }
    }
    
    private int getNextIdx(Connection conn) throws SQLException {
        String sql = "SELECT MAX(idx) as max_idx FROM BookingData";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("max_idx") + 1;
            }
        }
        return 1;
    }
    
    public boolean cancelSeats(int busNo, String busType, int noOfSeats, int date, String userId) {
        if (busNo < 1 || busNo > 20) {
            System.out.println("Invalid bus number. Please enter a number between 1 and 20.");
            return false;
        }
        
        if (noOfSeats <= 0) {
            System.out.println("Invalid number of seats. Please enter a positive number.");
            return false;
        }

        // First verify if the ticket belongs to the user
        if (!verifyTicketOwnership(busNo, busType, noOfSeats, date, userId)) {
            System.out.println("This ticket does not belong to you.");
            return false;
        }

        String type = getBusTypeString(busType);
        String sql = "UPDATE listOfSeats" + busNo + " SET " + type + " = " + type + " + ? WHERE date = ?";
        
        try (Connection conn = DriverManager.getConnection(SEATS_DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noOfSeats);
            pstmt.setInt(2, date);
            
            int updatedRows = pstmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Seats cancelled successfully.");
                return true;
            } else {
                System.out.println("Failed to cancel seats.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error cancelling seats: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean verifyTicketOwnership(int busNo, String busType, int noOfSeats, int date, String userId) {
        String sql = "SELECT * FROM BookingData WHERE busNO = ? AND bustype = ? AND NoOfSeats = ? AND date = ? AND UserId = ?";
        
        try (Connection conn = DriverManager.getConnection(USER_DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, busNo);
            pstmt.setString(2, busType);
            pstmt.setInt(3, noOfSeats);
            pstmt.setInt(4, date);
            pstmt.setString(5, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if ticket exists and belongs to user
            }
        } catch (SQLException e) {
            System.err.println("Error verifying ticket ownership: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteBooking(int bookingId, String userId) {
        String sql = "DELETE FROM BookingData WHERE idx = ? AND UserId = ?";
        
        try (Connection conn = DriverManager.getConnection(USER_DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            pstmt.setString(2, userId);
            
            int deletedRows = pstmt.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("Booking cancelled successfully.");
                return true;
            } else {
                System.out.println("Failed to cancel booking. Please verify the booking ID and try again.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public int getAvailableSeats(int busNo, String busType, int date) {
        if (busNo < 1 || busNo > 20) {
            System.out.println("Invalid bus number. Please enter a number between 1 and 20.");
            return 0;
        }

        String type = getBusTypeString(busType);
        String sql = "SELECT * FROM listOfSeats" + busNo + " WHERE date = ?";
        
        try (Connection conn = DriverManager.getConnection(SEATS_DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int seats = rs.getInt(type);
                    System.out.println("Found " + seats + " available seats for bus " + busNo + " type " + type + " on date " + date);
                    return seats;
                } else {
                    System.out.println("No seat data found for bus " + busNo + " on date " + date);
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting available seats: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    private int getDefaultSeats(String busType) {
        switch (busType) {
            case "Express":
                return 52;
            case "Luxury":
                return 41;
            case "AC_Sleeper":
            case "NonAC_Sleeper":
                return 30;
            default:
                return 0;
        }
    }
    
    private String getBusTypeString(String busType) {
        switch (busType.toUpperCase()) {
            case "E":
                return "Express";
            case "L":
                return "Luxury";
            case "N":
                return "NonAC_Sleeper";
            case "A":
                return "AC_Sleeper";
            default:
                return "Express";
        }
    }
    
    private double calculateFare(String busType, int distance) {
        if (distance <= 0) {
            return 0.0;
        }

        double baseFare;
        if (distance < 50) {
            baseFare = 2.5 * distance;
        } else {
            baseFare = 1.25 * 50 + 1.0 * (distance - 50);
        }

        switch (busType.toUpperCase()) {
            case "E":
                return baseFare; // Express bus uses base fare
            case "L":
                return baseFare + (distance * 0.25); // Luxury bus adds 25% surcharge
            case "N":
                return baseFare + distance; // Non-AC Sleeper adds 1 rupee per km
            case "A":
                return baseFare + (distance * 1.5); // AC Sleeper adds 150% surcharge
            default:
                return 0.0;
        }
    }
    
    public void resetSeats() {
        for (int busNo = 1; busNo <= 20; busNo++) {
            String sql = "UPDATE listOfSeats" + busNo + " SET Express=52, Luxury=41, NonAC_Sleeper=30, AC_Sleeper=30 WHERE date = ?";
            try (Connection conn = DriverManager.getConnection(SEATS_DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                for (int date = 1; date <= 31; date++) {
                    pstmt.setInt(1, date);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Error resetting seats for bus " + busNo + ": " + e.getMessage());
            }
        }
    }

    public boolean validateTicketNumber(String ticketNumber) {
        if (ticketNumber == null) return false;
        
        // Check if ticket number matches SPR format (case insensitive)
        if (!ticketNumber.matches("(?i)^SPR\\d+$")) {
            return false;
        }
        
        try {
            // Extract numeric part and check if it's greater than 13830000
            int ticketId = Integer.parseInt(ticketNumber.substring(3));
            return ticketId > 13830000;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 