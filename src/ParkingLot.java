package src;

import java.util.*;

public class ParkingLot {
    private PriorityQueue<Slot> availableSlots;
    private Map<String, Slot> parkedCars;

    public ParkingLot(int totalSlots) {
        availableSlots = new PriorityQueue<>();
        parkedCars = new HashMap<>();

        for (int i = 1; i <= totalSlots; i++) {
            availableSlots.add(new Slot(i));
        }
    }

    // Allocate nearest available slot
    public void parkCar(String numberPlate) {
        if (availableSlots.isEmpty()) {
            System.out.println("❌ Parking Full! No available slots.");
            return;
        }

        if (parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car already parked!");
            return;
        }

        Slot nearestSlot = availableSlots.poll();
        Car car = new Car(numberPlate);
        nearestSlot.parkCar(car);
        parkedCars.put(numberPlate, nearestSlot);

        System.out.println("✅ Car " + numberPlate + " parked at Slot " + nearestSlot.getId());
    }

    // Remove car and free slot
    public void removeCar(String numberPlate) {
        if (!parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car not found in parking lot!");
            return;
        }

        Slot slot = parkedCars.get(numberPlate);
        slot.removeCar();
        availableSlots.add(slot);
        parkedCars.remove(numberPlate);

        System.out.println("🅿️ Car " + numberPlate + " removed from Slot " + slot.getId());
    }

    // Display parking status
    public void displayStatus() {
        System.out.println("\n--- Parking Lot Status ---");
        int totalSlots = availableSlots.size() + parkedCars.size();
        for (int i = 1; i <= totalSlots; i++) {
            Slot slot = getSlotById(i);
            System.out.println(slot);
        }
        System.out.println("---------------------------\n");
    }

    // Search car by number plate
    public void findCar(String numberPlate) {
        if (parkedCars.containsKey(numberPlate)) {
            Slot slot = parkedCars.get(numberPlate);
            System.out.println("🔍 Car " + numberPlate + " found at Slot " + slot.getId());
        } else {
            System.out.println("❌ Car " + numberPlate + " not found!");
        }
    }

    private Slot getSlotById(int id) {
        // If occupied, find from parkedCars map
        for (Slot slot : parkedCars.values()) {
            if (slot.getId() == id) return slot;
        }
        // Otherwise, find in available slots
        for (Slot slot : availableSlots) {
            if (slot.getId() == id) return slot;
        }
        return null;
    }

    public String displaySlots() {
    StringBuilder sb = new StringBuilder();
    int totalSlots = availableSlots.size() + parkedCars.size();
    for (int i = 1; i <= totalSlots; i++) {
        Slot slot = getSlotById(i);
        sb.append(slot).append("\n");
    }
    return sb.toString();
    
    }

}
