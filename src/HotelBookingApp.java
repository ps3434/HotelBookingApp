import java.util.*;

// Represents a confirmed reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.totalCost = totalCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights +
                ", Total Cost: ₹" + totalCost;
    }
}

// Maintains booking history
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations (read-only usage expected)
    public List<Reservation> getAllReservations() {
        return reservations;
    }
}

// Generates reports from booking history
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No booking history available.");
            return;
        }

        System.out.println("\n=== Booking History ===");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0.0;

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();
        }

        System.out.println("\n=== Booking Summary Report ===");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings (from previous use cases)
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 2, 4000);
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", 3, 9000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Standard", 1, 1500);

        // Add to booking history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin views booking history
        reportService.showAllBookings(history.getAllReservations());

        // Admin generates summary report
        reportService.generateSummary(history.getAllReservations());
    }
}