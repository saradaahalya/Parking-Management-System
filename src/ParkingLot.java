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
    private Map<String, String> carRateType; // stores VIP/REGULAR/WEEKEND type per car
    private int vipSlots;
    private double hourlyRate;
    private double totalRevenue;

    public ParkingLot(int totalSlots) {
        this.slots = new ArrayList<>(totalSlots);
        this.availableSlots = new PriorityQueue<>();
        this.parkedCars = new HashMap<>();
        this.parkingRates = new HashMap<>();
        this.entryTimes = new HashMap<>();
        this.carRateType = new HashMap<>();
        this.vipSlots = Math.max(1, totalSlots / 5); // ~20% VIP slots (at least 1)
        this.hourlyRate = 10.0; // fallback hourly rate
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

    /**
     * Park a car using an explicit rateType.
     * rateType must be one of: "VIP", "REGULAR", "WEEKEND" (case-insensitive accepted)
     */
    public void parkCar(String numberPlate, String rateType) {
        if (numberPlate == null || numberPlate.isEmpty()) {
            System.out.println("❌ Invalid plate.");
            return;
        }

        rateType = normalizeRateType(rateType);

        if (availableSlots.isEmpty()) {
            System.out.println("❌ Parking Full! No available slots.");
            return;
        }

        if (parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car already parked!");
            return;
        }

        int slotId = findAppropriateSlot(rateType);
        if (slotId == -1) {
            System.out.println("❌ No appropriate slots available!");
            return;
        }

        Slot slot = getSlotById(slotId);
        Car car = new Car(numberPlate);
        slot.parkCar(car);
        parkedCars.put(numberPlate, slot);
        entryTimes.put(numberPlate, LocalDateTime.now());
        carRateType.put(numberPlate, rateType);

        System.out.println("✅ Car " + numberPlate + " parked at " +
                         (slot.isVip() ? "VIP " : "") + "Slot " + slot.getId() +
                         " [" + rateType + "]");
    }

    /**
     * Remove car and compute charge based on stored rateType for that car.
     */
    public double removeCar(String numberPlate) {
        if (!parkedCars.containsKey(numberPlate)) {
            System.out.println("⚠️ Car not found in parking lot!");
            return 0.0;
        }

        Slot slot = parkedCars.get(numberPlate);
        LocalDateTime entryTime = entryTimes.get(numberPlate);
        LocalDateTime exitTime = LocalDateTime.now();

        String rateType = carRateType.getOrDefault(numberPlate, slot.isVip() ? "VIP" : "REGULAR");
        double charge = calculateParkingCharge(entryTime, exitTime, rateType);
        totalRevenue += charge;

        slot.removeCar();
        availableSlots.add(slot.getId());
        parkedCars.remove(numberPlate);
        entryTimes.remove(numberPlate);
        carRateType.remove(numberPlate);

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
            // don't spam logs for UI search; keep consistent with previous behavior
            System.out.println("❌ Car " + numberPlate + " not found!");
            return false;
        }
    }

    private Slot getSlotById(int id) {
        if (id <= 0 || id > slots.size()) return null;
        return slots.get(id - 1);
    }

    /**
     * Choose a slot based on rateType:
     * - "VIP" => prefer VIP slots (search availableSlots for a VIP slot)
     * - others ("REGULAR","WEEKEND") => any available slot (nearest)
     */
    private int findAppropriateSlot(String rateType) {
        if ("VIP".equals(rateType)) {
            for (Integer slotId : new ArrayList<>(availableSlots)) {
                Slot s = getSlotById(slotId);
                if (s != null && s.isVip()) {
                    availableSlots.remove(slotId);
                    return slotId;
                }
            }
            // no VIP available -> fallback to any slot
        }
        Integer next = availableSlots.poll();
        return next == null ? -1 : next;
    }

    // Peek an appropriate slot without removing it from available pool
    public int peekAppropriateSlot(String rateType) {
        if (availableSlots.isEmpty()) return -1;
        if ("VIP".equals(rateType)) {
            for (Integer slotId : availableSlots) {
                Slot s = getSlotById(slotId);
                if (s != null && s.isVip()) return slotId;
            }
            return -1;
        } else {
            Integer next = availableSlots.peek();
            return next == null ? -1 : next;
        }
    }

    /**
     * Park at a specific slot id, storing the provided rateType for future billing.
     */
    public void parkCarAt(String numberPlate, String rateType, int slotId) {
        if (numberPlate == null || numberPlate.isEmpty()) {
            System.out.println("❌ Invalid plate.");
            return;
        }

        rateType = normalizeRateType(rateType);

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
        entryTimes.put(numberPlate, LocalDateTime.now());
        carRateType.put(numberPlate, rateType);

        System.out.println("✅ Car " + numberPlate + " parked at " +
                         (slot.isVip() ? "VIP " : "") + "Slot " + slot.getId() +
                         " [" + rateType + "]");
    }

    /**
     * Calculate parking charge using stored rateType.
     * Behavior:
     *  - If rateType == "WEEKEND" -> use WEEKEND rate
     *  - Else if exit day is weekend -> use WEEKEND rate (surcharge)
     *  - Else use rate corresponding to rateType (VIP/REGULAR)
     */
    private double calculateParkingCharge(LocalDateTime entry, LocalDateTime exit, String rateType) {
        long hours = Duration.between(entry, exit).toHours() + 1;
        rateType = normalizeRateType(rateType);

        double rate;
        if ("WEEKEND".equals(rateType)) {
            rate = parkingRates.getOrDefault("WEEKEND", hourlyRate);
        } else {
            // If exit happens on weekend, apply weekend rate as surcharge
            if (exit.getDayOfWeek().getValue() >= 6) {
                rate = parkingRates.getOrDefault("WEEKEND", hourlyRate);
            } else {
                rate = parkingRates.getOrDefault(rateType, hourlyRate);
            }
        }

        return hours * rate;
    }

    // Normalize input (case-insensitive) and default to REGULAR if unknown
    private String normalizeRateType(String rt) {
        if (rt == null) return "REGULAR";
        rt = rt.trim().toUpperCase();
        if (rt.equals("VIP")) return "VIP";
        if (rt.equals("WEEKEND")) return "WEEKEND";
        return "REGULAR";
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
        type = type == null ? "" : type.trim().toUpperCase();
        if (parkingRates.containsKey(type)) {
            parkingRates.put(type, newRate);
            System.out.printf("Updated %s rate to $%.2f\n", type, newRate);
        } else {
            System.out.println("❌ Invalid rate type: " + type);
        }
    }

    public void setRate(String rateType, double rate) {
        updateParkingRate(rateType, rate);
    }

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
