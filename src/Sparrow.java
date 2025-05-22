import ui.UserInterface;
import service.BookingService;

public class Sparrow {
    public static void main(String[] args) {
        // // Reset seats at startup
        // BookingService bookingService = new BookingService();
        // bookingService.resetSeats();
        
        // Start the application
        UserInterface ui = new UserInterface();
        ui.start();
    }
} 