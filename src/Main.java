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
                // Initialize 5 placeholder regions with 20 slots each (can be customized later)
                java.util.List<Region> regions = new java.util.ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    regions.add(new Region("Region " + i, new ParkingLot(20)));
                }
                new ParkingUI(regions);
            });
        } else {
            runCliMode(sc);
        }
    }

    private static void runCliMode(Scanner sc) {
        System.out.print("Enter number of parking slots per region (5 regions will be created): ");
        int perRegionSlots = sc.nextInt();

        java.util.List<Region> regions = new java.util.ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            regions.add(new Region("Region " + i, new ParkingLot(perRegionSlots)));
        }

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
                case 1: {
                    System.out.print("Enter car number plate: ");
                    String num = sc.next();
                    System.out.print("Is VIP? (y/n): ");
                    boolean isVip = sc.next().toLowerCase().startsWith("y");
                    // Select region
                    System.out.println("Select region:");
                    for (int i = 0; i < regions.size(); i++) {
                        System.out.printf("%d. %s\n", i + 1, regions.get(i).getName());
                    }
                    int r = sc.nextInt();
                    if (r < 1 || r > regions.size()) r = 1;
                    // Ensure the car isn't already parked in any region
                    boolean exists = false;
                    for (Region reg : regions) {
                        if (reg.getParkingLot().findCar(num)) { exists = true; break; }
                    }
                    if (exists) {
                        System.out.println("⚠️ A car with this number plate is already parked in a region.");
                    } else {
                        regions.get(r - 1).getParkingLot().parkCar(num, isVip);
                    }
                    break;
                }
                case 2: {
                    System.out.print("Enter car number plate to remove: ");
                    String num = sc.next();
                    // Try removing from all regions
                    double charge = 0.0;
                    boolean found = false;
                    for (Region reg : regions) {
                        if (reg.getParkingLot().findCar(num)) {
                            charge = reg.getParkingLot().removeCar(num);
                            found = true;
                            break;
                        }
                    }
                    if (!found) System.out.println("❌ Car not found in any region.");
                    System.out.printf("Parking charge: $%.2f\n", charge);
                    break;
                }
                case 3: {
                    // Display status per region
                    for (Region reg : regions) {
                        System.out.println("\n== " + reg.getName() + " ==");
                        reg.getParkingLot().displayStatus();
                    }
                    break;
                }
                case 4: {
                    System.out.print("Enter car number plate to search: ");
                    String q = sc.next();
                    boolean found = false;
                    for (Region reg : regions) {
                        if (reg.getParkingLot().findCar(q)) {
                            System.out.println("Found in " + reg.getName());
                            found = true;
                            break;
                        }
                    }
                    if (!found) System.out.println("❌ Car not found in any region.");
                    break;
                }
                case 5: {
                    for (Region reg : regions) {
                        System.out.println("\n== Statistics for " + reg.getName() + " ==");
                        reg.getParkingLot().displayStatistics();
                    }
                    break;
                }
                case 6: {
                    // choose region to update rates for
                    System.out.println("Select region to update rates:");
                    for (int i = 0; i < regions.size(); i++) {
                        System.out.printf("%d. %s\n", i + 1, regions.get(i).getName());
                    }
                    int r = sc.nextInt(); if (r < 1 || r > regions.size()) r = 1;
                    updateParkingRates(sc, regions.get(r - 1).getParkingLot());
                    break;
                }
                case 7: {
                    System.out.println("👋 Exiting system. Goodbye!");
                    running = false;
                    break;
                }
                default: {
                    System.out.println("⚠️ Invalid choice! Try again.");
                    break;
                }
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