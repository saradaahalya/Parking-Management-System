// src/ParkingUI.java
package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ParkingUI extends JFrame {
    private ParkingLot parkingLot;
    private JTextArea displayArea;

    public ParkingUI(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;

        setTitle("Smart Parking Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Heading
        JLabel title = new JLabel("Smart Parking Manager - VIT", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton parkButton = new JButton("Park Car");
        JButton removeButton = new JButton("Remove Car");
        JButton displayButton = new JButton("Display Slots");

        buttonPanel.add(parkButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(displayButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        parkButton.addActionListener(e -> parkCar());
        removeButton.addActionListener(e -> removeCar());
        displayButton.addActionListener(e -> displayStatus());

        setVisible(true);
    }

    private void parkCar() {
        String plate = JOptionPane.showInputDialog(this, "Enter Car Number:");
        if (plate != null && !plate.isEmpty()) {
            parkingLot.parkCar(plate);
            JOptionPane.showMessageDialog(this,
        "Car parked (or parking full)!",
        "Parking Status", JOptionPane.INFORMATION_MESSAGE);
            displayStatus();
        }
    }

    private void removeCar() {
        String plate = JOptionPane.showInputDialog(this, "Enter Car Number to Remove:");
        if (plate != null && !plate.isEmpty()) {
            parkingLot.removeCar(plate);
            JOptionPane.showMessageDialog(this,
        "Car removed (or not found)!",
        "Remove Status", JOptionPane.INFORMATION_MESSAGE);
            displayStatus();
        }
    }

    private void displayStatus() {
        displayArea.setText(parkingLot.displaySlots());
    }
}
