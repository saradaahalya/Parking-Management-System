package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        ParkingLot lot = new ParkingLot(10); // Example: 10 slots
        new ParkingUI(lot);

        System.out.println("=== 🚗 Smart Parking Management System ===");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter total number of parking slots: ");
        int totalSlots = sc.nextInt();

        ParkingLot parkingLot = new ParkingLot(totalSlots);
        boolean running = true;

        while (running) {
            System.out.println("\n----- MENU -----");
            System.out.println("1. Park a Car");
            System.out.println("2. Remove a Car");
            System.out.println("3. Display Parking Status");
            System.out.println("4. Search Car by Number");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter car number plate: ");
                    String num = sc.next();
                    parkingLot.parkCar(num);
                }
                case 2 -> {
                    System.out.print("Enter car number plate to remove: ");
                    String num = sc.next();
                    parkingLot.removeCar(num);
                }
                case 3 -> parkingLot.displayStatus();
                case 4 -> {
                    System.out.print("Enter car number plate to search: ");
                    String num = sc.next();
                    parkingLot.findCar(num);
                }
                case 5 -> {
                    System.out.println("👋 Exiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("⚠️ Invalid choice! Try again.");
            }
        }

        sc.close();
    }
}
