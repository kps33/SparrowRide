package model;

public class User {
    private String name;
    private String mobileNumber;
    private String userId;
    private String password;
    private int noOfSeats;
    private String source;
    private String destination;
    private double busFare;
    private int distance;
    private int date;
    private int busNo;
    private String busType;

    // Constructor for new user sign up
    public User(String name, String mobileNumber, String userId, String password) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.userId = userId;
        this.password = password;
    }

    // Constructor for booking tickets
    public User(String source, String destination, int date) {
        this.source = source;
        this.destination = destination;
        this.date = date;
    }

    // Constructor for existing user login
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public int getNoOfSeats() { return noOfSeats; }
    public void setNoOfSeats(int noOfSeats) { this.noOfSeats = noOfSeats; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public double getBusFare() { return busFare; }
    public void setBusFare(double busFare) { this.busFare = busFare; }
    
    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }
    
    public int getDate() { return date; }
    public void setDate(int date) { this.date = date; }
    
    public int getBusNo() { return busNo; }
    public void setBusNo(int busNo) { this.busNo = busNo; }
    
    public String getBusType() { return busType; }
    public void setBusType(String busType) { this.busType = busType; }

    // Method to check password for login
    public boolean checkPassword(String pass, String userId) {
        return this.password.equalsIgnoreCase(pass) && this.userId.equalsIgnoreCase(userId);
    }
} 