package src;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 🚗 Advanced Parking Management System ===");
        System.out.println("1. Launch GUI Mode");
        System.out.println("2. Launch CLI Mode");
        System.out.print("Select mode: ");

        Scanner sc = new Scanner(System.in);
        int mode = sc.nextInt();

        if (mode == 1) {
            SwingUtilities.invokeLater(() -> {
                ParkingLot lot = new ParkingLot(20); // Default 20 slots
                new ParkingUI(lot);
            });
        } else {
            runCliMode(sc);
        }
    }

    private static void runCliMode(Scanner sc) {
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
            System.out.println("5. Show Statistics");
            System.out.println("6. Update Parking Rates");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter car number plate: ");
                    String num = sc.next();
                    System.out.print("Is VIP? (y/n): ");
                    boolean isVip = sc.next().toLowerCase().startsWith("y");
                    parkingLot.parkCar(num, isVip);
                }
                case 2 -> {
                    System.out.print("Enter car number plate to remove: ");
                    String num = sc.next();
                    double charge = parkingLot.removeCar(num);
                    System.out.printf("Parking charge: $%.2f\n", charge);
                }
                case 3 -> parkingLot.displayStatus();
                case 4 -> {
                    System.out.print("Enter car number plate to search: ");
                    parkingLot.findCar(sc.next());
                }
                case 5 -> parkingLot.displayStatistics();
                case 6 -> updateParkingRates(sc, parkingLot);
                case 7 -> {
                    System.out.println("👋 Exiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("⚠️ Invalid choice! Try again.");
            }
        }
        sc.close();
    }

    private static void updateParkingRates(Scanner sc, ParkingLot parkingLot) {
        System.out.println("Available rate types: REGULAR, VIP, WEEKEND");
        System.out.print("Enter rate type to update: ");
        String type = sc.next().toUpperCase();
        System.out.print("Enter new rate: ");
        double rate = sc.nextDouble();
        parkingLot.updateParkingRate(type, rate);
    }
}
