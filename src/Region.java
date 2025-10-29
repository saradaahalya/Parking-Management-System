package src;

public class Region {
    private String name;
    private ParkingLot parkingLot;

    public Region(String name, ParkingLot parkingLot) {
        this.name = name;
        this.parkingLot = parkingLot;
    }

    public String getName() {
        return name;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    @Override
    public String toString() {
        return name;
    }
}
