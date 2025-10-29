package src;

import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingLot {
    private List<Slot> slots;
    private PriorityQueue<Integer> availableSlots;
    private Map<String, Slot> parkedCars;
    private Map<String, Double> parkingRates;
    private Map<String, LocalDateTime> entryTimes;
    private int vipSlots;
    private double hourlyRate;
    private double totalRevenue;

    public ParkingLot(int totalSlots) {
        this.slots = new ArrayList<>(totalSlots);
        this.availableSlots = new PriorityQueue<>();
        this.parkedCars = new HashMap<>();
        this.parkingRates = new HashMap<>();
        this.entryTimes = new HashMap<>();
        this.vipSlots = totalSlots / 5; // 20% VIP slots
        this.hourlyRate = 10.0; // $10 per hour
        this.totalRevenue = 0;

        // Initialize slots with VIP designation
        for (int i = 1; i <= totalSlots; i++) {
            boolean isVip = i <= vipSlots;
            slots.add(new Slot(i, isVip));
            availableSlots.add(i);
        }

        // Initialize special rates
        parkingRates.put("VIP", 20.0);
        parkingRates.put("REGULAR", 10.0);
        parkingRates.put("WEEKEND", 15.0);
    }

    // Allocate nearest available slot
    public void parkCar(String numberPlate, boolean isVip) {
        if (availableSlots.isEmpty()) {
            System.out.println("❌ Parking Full! No available slots.");
            return;
        }

        if (parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car already parked!");
            return;
        }

        int slotId = findAppropriateSlot(isVip);
        if (slotId == -1) {
            System.out.println("❌ No appropriate slots available!");
            return;
        }

        Slot slot = getSlotById(slotId);
        Car car = new Car(numberPlate);
        slot.parkCar(car);
        parkedCars.put(numberPlate, slot);
        entryTimes.put(numberPlate, LocalDateTime.now());

        System.out.println("✅ Car " + numberPlate + " parked at " + 
                         (slot.isVip() ? "VIP " : "") + "Slot " + slot.getId());
    }

    // Remove car and free slot
    public double removeCar(String numberPlate) {
        if (!parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car not found in parking lot!");
            return 0.0;
        }

        Slot slot = parkedCars.get(numberPlate);
        LocalDateTime entryTime = entryTimes.get(numberPlate);
        LocalDateTime exitTime = LocalDateTime.now();
        
        double charge = calculateParkingCharge(numberPlate, entryTime, exitTime, slot.isVip());
        totalRevenue += charge;

        slot.removeCar();
        availableSlots.add(slot.getId());
        parkedCars.remove(numberPlate);
        entryTimes.remove(numberPlate);

        System.out.printf("🅿️ Car %s removed from Slot %d\n", numberPlate, slot.getId());
        System.out.printf("💰 Parking charge: $%.2f\n", charge);
        return charge;
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

    private int findAppropriateSlot(boolean isVip) {
        if (isVip) {
            // Try to find VIP slot first
            for (Integer slotId : availableSlots) {
                if (getSlotById(slotId).isVip()) {
                    availableSlots.remove(slotId);
                    return slotId;
                }
            }
        }
        return availableSlots.poll();
    }

    // Peek an appropriate slot without removing it from available pool
    public int peekAppropriateSlot(boolean isVip) {
        if (availableSlots.isEmpty()) return -1;
        if (isVip) {
            for (Integer slotId : availableSlots) {
                Slot s = getSlotById(slotId);
                if (s != null && s.isVip()) {
                    return slotId;
                }
            }
            return -1;
        } else {
            Integer next = availableSlots.peek();
            return next == null ? -1 : next;
        }
    }

    // Park car at a specific slot id (used when user confirms a suggested slot)
    public void parkCarAt(String numberPlate, boolean isVip, int slotId) {
        if (availableSlots.isEmpty()) {
            System.out.println("❌ Parking Full! No available slots.");
            return;
        }

        if (parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car already parked!");
            return;
        }

        Slot slot = getSlotById(slotId);
        if (slot == null) {
            System.out.println("❌ Invalid slot selected.");
            return;
        }

        if (slot.isOccupied()) {
            System.out.println("❌ Selected slot is already occupied.");
            return;
        }

        // Remove chosen slot from available pool
        availableSlots.remove(slotId);

        Car car = new Car(numberPlate);
        slot.parkCar(car);
        parkedCars.put(numberPlate, slot);
        entryTimes.put(numberPlate, java.time.LocalDateTime.now());

        System.out.println("✅ Car " + numberPlate + " parked at " + 
                         (slot.isVip() ? "VIP " : "") + "Slot " + slot.getId());
    }

    private double calculateParkingCharge(String numberPlate, LocalDateTime entry, 
                                        LocalDateTime exit, boolean isVip) {
        long hours = Duration.between(entry, exit).toHours() + 1;
        double rate = isVip ? parkingRates.get("VIP") : parkingRates.get("REGULAR");
        
        // Weekend surcharge
        if (exit.getDayOfWeek().getValue() >= 6) {
            rate = parkingRates.get("WEEKEND");
        }

        return hours * rate;
    }

    public void displayStatistics() {
        System.out.println("\n=== Parking Statistics ===");
        System.out.println("Total Slots: " + slots.size());
        System.out.println("VIP Slots: " + vipSlots);
        System.out.println("Available Slots: " + availableSlots.size());
        System.out.println("Occupied Slots: " + parkedCars.size());
        System.out.printf("Total Revenue: $%.2f\n", totalRevenue);
        System.out.println("========================\n");
    }

    public Map<String, Double> getParkingRates() {
        return Collections.unmodifiableMap(parkingRates);
    }

    public void updateParkingRate(String type, double newRate) {
        if (parkingRates.containsKey(type)) {
            parkingRates.put(type, newRate);
            System.out.printf("Updated %s rate to $%.2f\n", type, newRate);
        }
    }

    public void setRate(String rateType, double rate) {
        if (parkingRates.containsKey(rateType)) {
            parkingRates.put(rateType, rate);
            System.out.printf("✅ %s rate updated to $%.2f\n", rateType, rate);
        } else {
            System.out.println("❌ Invalid rate type: " + rateType);
        }
    }

    // Add these new methods
    public double getTotalRevenue() {
        return totalRevenue;
    }

    public String displaySlots() {
        StringBuilder sb = new StringBuilder();
        for (Slot slot : slots) {
            sb.append(slot.toString()).append("\n");
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

    public int getTotalCars() {
        return parkedCars.size();
    }
}
