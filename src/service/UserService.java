package service;

import model.User;
import util.DatabaseUtil;
import java.sql.*;

public class UserService {
    private static final String USER_DB_URL = "jdbc:mysql://localhost:3306/UserData";
    
    public boolean registerUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return false;
        }
        
        if (user.getMobileNumber() == null || !user.getMobileNumber().matches("\\d{10}")) {
            System.out.println("Invalid mobile number. Please enter a 10-digit number.");
            return false;
        }
        
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            System.out.println("User ID cannot be empty.");
            return false;
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }

        String sql = "INSERT INTO dataTable (name, mobileNumber, userId, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(USER_DB_URL, "root", "ZeEl@51271895@");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getMobileNumber());
            pstmt.setString(3, user.getUserId());
            pstmt.setString(4, user.getPassword());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public User loginUser(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("User ID cannot be empty.");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return null;
        }

        String sql = "SELECT * FROM dataTable WHERE userId = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(USER_DB_URL, "root", "ZeEl@51271895@");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("name"),
                    rs.getString("mobileNumber"),
                    rs.getString("userId"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean isUserIdExists(String userId) {
        String sql = "SELECT userId FROM dataTable WHERE userId = ?";
        
        try (Connection conn = DriverManager.getConnection(USER_DB_URL, "root", "ZeEl@51271895@");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String getMobileNumber(String userId) {
        String sql = "SELECT mobileNumber FROM dataTable WHERE userId = ?";
        
        try (Connection conn = DriverManager.getConnection(USER_DB_URL, "root", "ZeEl@51271895@");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("mobileNumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 