package src;

public class Car {
    private String numberPlate;
    private long entryTime;

    public Car(String numberPlate) {
        this.numberPlate = numberPlate;
        this.entryTime = System.currentTimeMillis();
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public long getEntryTime() {
        return entryTime;
    }

    @Override
    public String toString() {
        return "Car[" + numberPlate + "]";
    }
}
