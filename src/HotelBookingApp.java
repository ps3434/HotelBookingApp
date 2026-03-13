import java.util.*;
class Reservation {
    private String guestName;
    private String roomType;
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
    public String getGuestName() {
        return guestName;
    }
    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory {
    private Map<String, Integer> availability;
    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }
    public int getAvailableRooms(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }
    public void decrementInventory(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }
}
class RoomAllocationService {
    /* Stores all allocated room IDs */
    private Set<String> allocatedRooms;
    private Map<String, Set<String>> assignmentsByType;
    public RoomAllocationService() {
        allocatedRooms = new HashSet<>();
        assignmentsByType = new HashMap<>();
    }
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        if (inventory.getAvailableRooms(roomType) <= 0) {
            System.out.println("No rooms available for " + roomType);
            return;
        }
        String roomId = generateRoomId(roomType);
        allocatedRooms.add(roomId);
        assignmentsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        inventory.decrementInventory(roomType);

        System.out.println(
                "Booking confirmed for Guest: "
                        + reservation.getGuestName()
                        + ", Room ID: "
                        + roomId
        );
    }
    private String generateRoomId(String roomType) {

        int nextNumber = assignmentsByType
                .getOrDefault(roomType, new HashSet<>())
                .size() + 1;

        return roomType + "-" + nextNumber;
    }
}
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing\n");

        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Anil", "Single"));
        bookingQueue.add(new Reservation("Sara", "Single"));
        bookingQueue.add(new Reservation("Venkatesh", "Suite"));

        while (!bookingQueue.isEmpty()) {

            Reservation reservation = bookingQueue.poll();

            allocationService.allocateRoom(reservation, inventory);
        }
    }
}