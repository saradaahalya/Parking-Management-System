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
                java.util.List<Region> regions = new java.util.ArrayList<>();
                regions.add(new Region("PRP Parking", new ParkingLot(50)));
                regions.add(new Region("SJT Parking", new ParkingLot(25)));
                regions.add(new Region("TT Parking", new ParkingLot(20)));
                regions.add(new Region("Lake side Parking", new ParkingLot(25)));
                regions.add(new Region("Woodys Parking", new ParkingLot(50)));
                regions.add(new Region("Main gate Parking", new ParkingLot(40)));
                new ParkingUI(regions);
            });
        } else {
            runCliMode(sc);
        }
    }

    private static void runCliMode(Scanner sc) {
        java.util.List<Region> regions = new java.util.ArrayList<>();
        regions.add(new Region("PRP Parking", new ParkingLot(50)));
        regions.add(new Region("SJT Parking", new ParkingLot(25)));
        regions.add(new Region("TT Parking", new ParkingLot(20)));
        regions.add(new Region("Lake side Parking", new ParkingLot(25)));
        regions.add(new Region("Woodys Parking", new ParkingLot(50)));
        regions.add(new Region("Main gate Parking", new ParkingLot(40)));

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
                    System.out.print("Enter parking type (REGULAR / VIP / WEEKEND): ");
                    String type = sc.next().toUpperCase();

                    System.out.println("Select region:");
                    for (int i = 0; i < regions.size(); i++) {
                        System.out.printf("%d. %s\n", i + 1, regions.get(i).getName());
                    }
                    int r = sc.nextInt();
                    if (r < 1 || r > regions.size()) r = 1;

                    Region selectedRegion = regions.get(r - 1);

                    // Check if car already exists in any region
                    boolean exists = false;
for (Region reg : regions) {
    if (reg.getParkingLot().isCarParked(num)) { exists = true; break; }
}


                    if (exists) {
                        System.out.println("⚠️  A car with this number plate is already parked in a region.");
                    } else {
                        selectedRegion.getParkingLot().parkCar(num, type);
                        System.out.println("✅ Car parked successfully in " + selectedRegion.getName());
                    }
                    break;
                }

                case 2: {
                    System.out.print("Enter car number plate to remove: ");
                    String num = sc.next();

                    double charge = 0.0;
                    boolean found = false;

                    for (Region reg : regions) {
                        if (reg.getParkingLot().findCar(num)) {
                            charge = reg.getParkingLot().removeCar(num);
                            found = true;
                            System.out.println("✅ Car removed from " + reg.getName());
                            break;
                        }
                    }

                    if (!found) System.out.println("❌ Car not found in any region.");
                    else System.out.printf("Parking charge: $%.2f\n", charge);
                    break;
                }

                case 3: {
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
                            System.out.println("✅ Found in " + reg.getName());
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
                    System.out.println("Select region to update rates:");
                    for (int i = 0; i < regions.size(); i++) {
                        System.out.printf("%d. %s\n", i + 1, regions.get(i).getName());
                    }
                    int r = sc.nextInt();
                    if (r < 1 || r > regions.size()) r = 1;
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
