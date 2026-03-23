import java.util.*;

// Represents an individual add-on service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages add-on services linked to reservations
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0.0;

        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Selected Add-On Services:");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main class to simulate the use case
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        // Simulated reservation ID (already created in previous use case)
        String reservationId = "RES123";

        System.out.println("=== Add-On Service Selection ===");
        System.out.println("Reservation ID: " + reservationId);

        // Sample services
        AddOnService wifi = new AddOnService("Premium WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 300);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800);

        while (true) {
            System.out.println("\nSelect Add-On Service:");
            System.out.println("1. Premium WiFi (₹200)");
            System.out.println("2. Breakfast (₹300)");
            System.out.println("3. Airport Pickup (₹800)");
            System.out.println("4. Finish Selection");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, wifi);
                    System.out.println("Added Premium WiFi");
                    break;

                case 2:
                    manager.addService(reservationId, breakfast);
                    System.out.println("Added Breakfast");
                    break;

                case 3:
                    manager.addService(reservationId, airportPickup);
                    System.out.println("Added Airport Pickup");
                    break;

                case 4:
                    System.out.println("\nFinal Add-On Summary:");
                    manager.displayServices(reservationId);
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}