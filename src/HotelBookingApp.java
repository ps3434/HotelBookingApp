import java.io.*;
import java.util.*;
import java.util.concurrent.*;

// Booking Class
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    String guestName;
    String roomType;

    public Booking(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return guestName + " -> " + roomType;
    }
}

// Inventory Class
class HotelInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> rooms = new HashMap<>();

    public HotelInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
    }

    public boolean allocateRoom(String roomType) {
        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            rooms.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("Inventory: " + rooms);
    }
}

// System State (Wrapper for persistence)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    HotelInventory inventory;
    List<Booking> bookings;

    public SystemState(HotelInventory inventory, List<Booking> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // Save State
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\n✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("⚠ Error saving data: " + e.getMessage());
        }
    }

    // Load State
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("⚠ No saved data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("✅ System state restored successfully.");
            return state;

        } catch (Exception e) {
            System.out.println("⚠ Corrupted data. Starting fresh.");
            return null;
        }
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        HotelInventory inventory;
        List<Booking> bookings;

        // Step 1: Load previous state
        SystemState state = PersistenceService.load();

        if (state != null) {
            inventory = state.inventory;
            bookings = state.bookings;
        } else {
            inventory = new HotelInventory();
            bookings = new ArrayList<>();
        }
    }

        // Step 2: Simulate bookings
        processBooking("Alice", "Single", inventory, bookings);
        processBooking("Bob", "Single", inventory, bookings);
        processBooking("Charlie", "Single", inventory, bookings);
        processBooking("David", "Double", inventory, bookings);

        // Step 3: Display current state
        System.out.println("\n--- Current Bookings ---");
        for (Booking b : bookings) {
            System.out.println(b);
        }

        inventory.display();

        // Step 4: Save state before shutdown
        PersistenceService.save(new SystemState(inventory, bookings));
    }

    // Booking logic
    public static void processBooking(String name, String roomType,
                                      HotelInventory inventory,
                                      List<Booking> bookings) {

        if (inventory.allocateRoom(roomType)) {
            bookings.add(new Booking(name, roomType));
            System.out.println("Booking SUCCESS for " + name);
        } else {
            System.out.println("Booking FAILED for " + name +
                    " (No " + roomType + " rooms available)");
        }
    }
}