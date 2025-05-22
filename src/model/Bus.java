package model;

public interface Bus {
    double calculateFare(int distanceInKm);
    
    default double getCommonFare(int distanceInKm) {
        if (distanceInKm < 50) {
            return 2.5 * distanceInKm;
        } else {
            return 1.25 * 50 + 1.0 * (distanceInKm - 50);
        }
    }
} 