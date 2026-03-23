import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents a Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// Validator class (Fail-Fast Design)
class InvalidBookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite"));

    // Validate all inputs before booking
    public static void validate(String guestName, String roomType, int nights, Map<String, Integer> inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than zero.");
        }

        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Room type not available in inventory.");
        }

        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

// Booking Manager with validation and safe state handling
class BookingManager {

    private Map<String, Integer> inventory;

    public BookingManager() {
        inventory = new HashMap<>();

        // Initial inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 0); // intentionally zero for testing
    }

    public Reservation createBooking(String reservationId, String guestName, String roomType, int nights)
            throws InvalidBookingException {

        // Fail-fast validation
        InvalidBookingValidator.validate(guestName, roomType, nights, inventory);

        // Safe state update (only after validation passes)
        inventory.put(roomType, inventory.get(roomType) - 1);

        return new Reservation(reservationId, guestName, roomType, nights);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms: " + inventory.get(type));
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        BookingManager manager = new BookingManager();
        Scanner scanner = new Scanner(System.in);

        manager.displayInventory();

        try {
            System.out.println("\nEnter Guest Name:");
            String name = scanner.nextLine();

            System.out.println("Enter Room Type (Standard/Deluxe/Suite):");
            String roomType = scanner.nextLine();

            System.out.println("Enter Number of Nights:");
            int nights = scanner.nextInt();

            Reservation reservation = manager.createBooking(
                    "RES" + new Random().nextInt(1000),
                    name,
                    roomType,
                    nights
            );

            System.out.println("\nBooking Successful!");
            System.out.println(reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("\nBooking Failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch unexpected errors
            System.out.println("\nUnexpected Error: " + e.getMessage());
        }

        // System continues running safely
        manager.displayInventory();

        scanner.close();
    }
}