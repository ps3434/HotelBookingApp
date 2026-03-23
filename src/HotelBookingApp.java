import java.util.*;
import java.util.concurrent.*;

// Booking Request Class
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Hotel Inventory Class (Shared Resource)
class HotelInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public HotelInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
    }

    // Critical Section
    public synchronized boolean allocateRoom(String roomType, String guestName) {
        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " allocating " + roomType + " room to " + guestName);

            rooms.put(roomType, available - 1);

            try {
                Thread.sleep(100); // simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Booking SUCCESS for " + guestName);
            return true;
        } else {
            System.out.println("Booking FAILED for " + guestName +
                    " (No " + roomType + " rooms available)");
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + rooms);
    }
}

// Booking Processor (Multi-threaded Worker)
class BookingProcessor implements Runnable {
    private Queue<BookingRequest> bookingQueue;
    private HotelInventory inventory;

    public BookingProcessor(Queue<BookingRequest> bookingQueue, HotelInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Synchronize queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            if (request != null) {
                inventory.allocateRoom(request.roomType, request.guestName);
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) {

        // Shared Queue
        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        // Add Requests
        bookingQueue.add(new BookingRequest("Alice", "Single"));
        bookingQueue.add(new BookingRequest("Bob", "Single"));
        bookingQueue.add(new BookingRequest("Charlie", "Single"));
        bookingQueue.add(new BookingRequest("David", "Double"));
        bookingQueue.add(new BookingRequest("Eve", "Double"));
        bookingQueue.add(new BookingRequest("Frank", "Double"));

        // Shared Inventory
        HotelInventory inventory = new HotelInventory();

        // Create Threads
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-3");

        // Start Threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final Inventory
        inventory.displayInventory();
    }
}