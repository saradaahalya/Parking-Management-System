package src;

import java.util.*;

public class ParkingLot {
    private List<Slot> slots;
    private PriorityQueue<Integer> availableSlots;
    private Map<String, Slot> parkedCars;

    public ParkingLot(int totalSlots) {
        this.slots = new ArrayList<>(totalSlots);
        this.availableSlots = new PriorityQueue<>();
        this.parkedCars = new HashMap<>();

        for (int i = 1; i <= totalSlots; i++) {
            slots.add(new Slot(i));
            availableSlots.add(i);
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

        int slotId = availableSlots.poll();
        Slot slot = getSlotById(slotId);
        Car car = new Car(numberPlate);
        slot.parkCar(car);
        parkedCars.put(numberPlate, slot);

        System.out.println("✅ Car " + numberPlate + " parked at Slot " + slot.getId());
    }

    // Remove car and free slot
    public void removeCar(String numberPlate) {
        if (!parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car not found in parking lot!");
            return;
        }

        Slot slot = parkedCars.get(numberPlate);
        slot.removeCar();
        availableSlots.add(slot.getId());
        parkedCars.remove(numberPlate);

        System.out.println("🅿️ Car " + numberPlate + " removed from Slot " + slot.getId());
    }

    // Display parking status
    public void displayStatus() {
        System.out.println("\n--- Parking Lot Status ---");
        for (Slot slot : slots) {
            System.out.println(slot);
        }
        System.out.println("---------------------------\n");
    }

    // Search car by number plate (returns boolean for UI)
    public boolean findCar(String numberPlate) {
        if (parkedCars.containsKey(numberPlate)) {
            Slot slot = parkedCars.get(numberPlate);
            System.out.println("🔍 Car " + numberPlate + " found at Slot " + slot.getId());
            return true;
        } else {
            System.out.println("❌ Car " + numberPlate + " not found!");
            return false;
        }
    }

    private Slot getSlotById(int id) {
        if (id <= 0 || id > slots.size()) return null;
        return slots.get(id - 1);
    }

    public String displaySlots() {
        StringBuilder sb = new StringBuilder();
        for (Slot slot : slots) {
            sb.append(slot).append("\n");
        }
        return sb.toString();
    }

    public int getTotalSlots() {
        return slots.size();
    }

    public boolean isSlotOccupied(int slotNumber) {
        Slot slot = getSlotById(slotNumber);
        return slot != null && slot.isOccupied();
    }
}
