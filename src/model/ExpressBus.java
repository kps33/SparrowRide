package model;

public class ExpressBus implements Bus {
    @Override
    public double calculateFare(int distanceInKm) {
        return getCommonFare(distanceInKm) * 1.2; // 20% premium for express service
    }
} 