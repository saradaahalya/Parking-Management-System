package src;

public class Slot implements Comparable<Slot> {
    private int id;
    private boolean occupied;
    private boolean isVip;
    private Car car;
    private String status; // AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE

    public Slot(int id, boolean isVip) {
        this.id = id;
        this.isVip = isVip;
        this.occupied = false;
        this.car = null;
        this.status = "AVAILABLE";
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public boolean isVip() {
        return isVip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void parkCar(Car car) {
        this.car = car;
        this.occupied = true;
    }

    public void removeCar() {
        this.car = null;
        this.occupied = false;
    }

    public Car getCar() {
        return car;
    }

    @Override
    public String toString() {
        String prefix = isVip ? "VIP " : "";
        return occupied ? 
            prefix + "Slot " + id + " → " + car.getNumberPlate() + " (" + status + ")" :
            prefix + "Slot " + id + " → [" + status + "]";
    }

    @Override
    public int compareTo(Slot other) {
        return Integer.compare(this.id, other.id);
    }
}
