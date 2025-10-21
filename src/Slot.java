package src;

public class Slot implements Comparable<Slot> {
    private int id;
    private boolean occupied;
    private Car car;

    public Slot(int id) {
        this.id = id;
        this.occupied = false;
        this.car = null;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
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
        return occupied ? "Slot " + id + " → " + car.getNumberPlate() : "Slot " + id + " → [EMPTY]";
    }

    @Override
    public int compareTo(Slot other) {
        return Integer.compare(this.id, other.id);
    }
}
