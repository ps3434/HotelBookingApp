import java.util.*;

// Booking class to store reservation details
class Booking {
    String bookingId;
    String guestName;
    String roomType;
    String roomId;
    boolean isCancelled;

    public Booking(String bookingId, String guestName, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }
}

// Main system class
public class UseCase10BookingCancellation {

    // Inventory: Room Type -> Available Count
    static Map<String, Integer> inventory = new HashMap<>();

    // Booking storage: Booking ID -> Booking Object
    static Map<String, Booking> bookings = new HashMap<>();

    // Available rooms: Room Type -> Queue of Room IDs
    static Map<String, Queue<String>> availableRooms = new HashMap<>();

    // Rollback stack (LIFO)
    static Stack<String> rollbackStack = new Stack<>();

    public static void main(String[] args) {

        // Initialize inventory
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        // Initialize room IDs
        availableRooms.put("Deluxe", new LinkedList<>(Arrays.asList("D1", "D2")));
        availableRooms.put("Suite", new LinkedList<>(Arrays.asList("S1")));

        // Simulate booking
        createBooking("B101", "Alice", "Deluxe");
        createBooking("B102", "Bob", "Suite");

        // Show state before cancellation
        System.out.println("\n--- Before Cancellation ---");
        displayState();

        // Perform cancellation
        cancelBooking("B101");

        // Show state after cancellation
        System.out.println("\n--- After Cancellation ---");
        displayState();

        // Attempt invalid cancellation
        cancelBooking("B999"); // non-existent
        cancelBooking("B101"); // already cancelled
    }

    // Create booking
    public static void createBooking(String bookingId, String guestName, String roomType) {

        if (!inventory.containsKey(roomType) || inventory.get(roomType) == 0) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }

        String roomId = availableRooms.get(roomType).poll();
        inventory.put(roomType, inventory.get(roomType) - 1);

        Booking booking = new Booking(bookingId, guestName, roomType, roomId);
        bookings.put(bookingId, booking);

        System.out.println("Booking Confirmed: " + bookingId + " | Room: " + roomId);
    }

    // Cancel booking with rollback logic
    public static void cancelBooking(String bookingId) {

        System.out.println("\nProcessing cancellation for: " + bookingId);

        // Validation
        if (!bookings.containsKey(bookingId)) {
            System.out.println("Cancellation Failed: Booking does not exist.");
            return;
        }

        Booking booking = bookings.get(bookingId);

        if (booking.isCancelled) {
            System.out.println("Cancellation Failed: Booking already cancelled.");
            return;
        }

        // Step 1: Push room ID to rollback stack
        rollbackStack.push(booking.roomId);

        // Step 2: Restore inventory
        inventory.put(booking.roomType, inventory.get(booking.roomType) + 1);

        // Step 3: Add room back to availability queue
        availableRooms.get(booking.roomType).offer(booking.roomId);

        // Step 4: Mark booking as cancelled
        booking.isCancelled = true;

        // Step 5: Log success
        System.out.println("Cancellation Successful for Booking: " + bookingId);
        System.out.println("Rolled back Room ID: " + booking.roomId);
    }

    // Display system state
    public static void displayState() {

        System.out.println("\nInventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }

        System.out.println("\nAvailable Rooms:");
        for (String type : availableRooms.keySet()) {
            System.out.println(type + " -> " + availableRooms.get(type));
        }

        System.out.println("\nBookings:");
        for (Booking b : bookings.values()) {
            System.out.println(b.bookingId + " | " + b.guestName + " | " + b.roomType +
                    " | Room: " + b.roomId + " | Cancelled: " + b.isCancelled);
        }

        System.out.println("\nRollback Stack:");
        System.out.println(rollbackStack);
    }
}