package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class ParkingUI extends JFrame {
    private ParkingLot parkingLot;
    private JPanel slotPanel;
    private JTextArea consoleArea;
    private HashMap<Integer, JPanel> slotBoxes;

    public ParkingUI(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.slotBoxes = new HashMap<>();

        setTitle("🚗 Smart Parking Manager - VIT");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 10));

        // Header
        JLabel title = new JLabel("Smart Parking Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Slot visualization panel
        slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(2, 5, 15, 15)); // 10 slots (2 rows x 5 columns)
        slotPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        updateSlotDisplay();
        add(slotPanel, BorderLayout.CENTER);

        // Console Area
        consoleArea = new JTextArea(5, 20);
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(consoleArea);
        add(scrollPane, BorderLayout.SOUTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton parkButton = new JButton("Park Car");
        JButton removeButton = new JButton("Remove Car");
        JButton searchButton = new JButton("Search Car");
        JButton refreshButton = new JButton("Refresh Slots");

        styleButton(parkButton);
        styleButton(removeButton);
        styleButton(searchButton);
        styleButton(refreshButton);

        buttonPanel.add(parkButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Button actions
        parkButton.addActionListener(e -> parkCar());
        removeButton.addActionListener(e -> removeCar());
        searchButton.addActionListener(e -> searchCar());
        refreshButton.addActionListener(e -> updateSlotDisplay());

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(55, 120, 190));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void parkCar() {
        String plate = JOptionPane.showInputDialog(this, "Enter Car Number:");
        if (plate != null && !plate.trim().isEmpty()) {
            parkingLot.parkCar(plate);
            consoleArea.append("Car " + plate + " parked.\n");
            updateSlotDisplay();
        }
    }

    private void removeCar() {
        String plate = JOptionPane.showInputDialog(this, "Enter Car Number to Remove:");
        if (plate != null && !plate.trim().isEmpty()) {
            parkingLot.removeCar(plate);
            consoleArea.append("Car " + plate + " removed.\n");
            updateSlotDisplay();
        }
    }

    private void searchCar() {
        String plate = JOptionPane.showInputDialog(this, "Enter Car Number to Search:");
        if (plate != null && !plate.trim().isEmpty()) {
            boolean found = parkingLot.findCar(plate);
            consoleArea.append(found ? "✅ Car " + plate + " found.\n"
                                     : "❌ Car " + plate + " not found.\n");
        }
    }

    private void updateSlotDisplay() {
        slotPanel.removeAll();
        slotBoxes.clear();

        int totalSlots = parkingLot.getTotalSlots();
        for (int i = 1; i <= totalSlots; i++) {
            JPanel slotBox = new JPanel();
            slotBox.setPreferredSize(new Dimension(80, 80));
            slotBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            slotBox.setLayout(new BorderLayout());

            JLabel label = new JLabel("Slot " + i, JLabel.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            slotBox.add(label, BorderLayout.CENTER);

            if (parkingLot.isSlotOccupied(i)) {
                slotBox.setBackground(new Color(220, 70, 70)); // red
            } else {
                slotBox.setBackground(new Color(90, 200, 90)); // green
            }

            slotBoxes.put(i, slotBox);
            slotPanel.add(slotBox);
        }

        slotPanel.revalidate();
        slotPanel.repaint();
    }
}
