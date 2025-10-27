package src;

import java.time.LocalDateTime;

public class Car {
    private String numberPlate;
    private String ownerName;
    private String vehicleType;
    private boolean isVip;
    private LocalDateTime entryTime;
    private String color;

    public Car(String numberPlate) {
        this(numberPlate, "", "REGULAR", false, "Unknown");
    }

    public Car(String numberPlate, String ownerName, String vehicleType, 
               boolean isVip, String color) {
        this.numberPlate = numberPlate;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.isVip = isVip;
        this.color = color;
        this.entryTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getNumberPlate() { return numberPlate; }
    public String getOwnerName() { return ownerName; }
    public String getVehicleType() { return vehicleType; }
    public boolean isVip() { return isVip; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public String getColor() { return color; }

    @Override
    public String toString() {
        return String.format("Car[%s, %s, %s%s]", 
            numberPlate, vehicleType, 
            isVip ? "VIP, " : "", color);
    }
}
