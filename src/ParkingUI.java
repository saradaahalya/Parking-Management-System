package src;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class ParkingUI extends JFrame {
    private ParkingLot parkingLot;
    private JPanel slotPanel;
    private JTextArea consoleArea;
    private HashMap<Integer, JPanel> slotBoxes;
    private JLabel revenueLabel;
    private JComboBox<String> rateTypeCombo;
    private JTextField rateField;
    
    public ParkingUI(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.slotBoxes = new HashMap<>();

        setTitle("🚗 Smart Parking Manager - Advanced");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(500);

        // Top panel with controls
        JPanel topPanel = createTopPanel();
        splitPane.setTopComponent(topPanel);

        // Bottom panel with console and statistics
        JPanel bottomPanel = createBottomPanel();
        splitPane.setBottomComponent(bottomPanel);

        add(splitPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Header with stats
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        revenueLabel = new JLabel("Total Revenue: $0.00");
        revenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(revenueLabel);
        
        // Rate management
        JPanel ratePanel = new JPanel();
        rateTypeCombo = new JComboBox<>(new String[]{"REGULAR", "VIP", "WEEKEND"});
        rateField = new JTextField(8);
        JButton updateRateBtn = new JButton("Update Rate");
        updateRateBtn.addActionListener(e -> updateRate());
        
        ratePanel.add(new JLabel("Rate Type:"));
        ratePanel.add(rateTypeCombo);
        ratePanel.add(new JLabel("New Rate:"));
        ratePanel.add(rateField);
        ratePanel.add(updateRateBtn);
        
        headerPanel.add(ratePanel);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Slot visualization
        slotPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        slotPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        updateSlotDisplay();
        
        JScrollPane slotScroll = new JScrollPane(slotPanel);
        panel.add(slotScroll, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        consoleArea = new JTextArea(10, 50);
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(consoleArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        String[] buttonLabels = {"Park Car", "Remove Car", "Search Car", "Show Statistics", "Refresh"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            styleButton(button);
            button.addActionListener(e -> handleButtonClick(label));
            panel.add(button);
        }

        return panel;
    }

    private void handleButtonClick(String action) {
        switch (action) {
            case "Park Car" -> parkCar();
            case "Remove Car" -> removeCar();
            case "Search Car" -> searchCar();
            case "Show Statistics" -> showStatistics();
            case "Refresh" -> updateSlotDisplay();
        }
    }

    private void parkCar() {
        JTextField plateField = new JTextField();
        JTextField ownerField = new JTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"REGULAR", "VIP"});
        JTextField colorField = new JTextField();

        Object[] fields = {
            "Number Plate:", plateField,
            "Owner Name:", ownerField,
            "Type:", typeCombo,
            "Color:", colorField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, 
            "Enter Car Details", JOptionPane.OK_CANCEL_OPTION);
            
        if (result == JOptionPane.OK_OPTION) {
            boolean isVip = typeCombo.getSelectedItem().equals("VIP");
            parkingLot.parkCar(plateField.getText(), isVip);
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

    private void updateRate() {
        String rateType = (String) rateTypeCombo.getSelectedItem();
        String rateValue = rateField.getText().trim();
        
        if (rateValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rate value cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double rate = Double.parseDouble(rateValue);
            parkingLot.setRate(rateType, rate);
            JOptionPane.showMessageDialog(this, "Rate updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rate value.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStatistics() {
        int totalCars = parkingLot.getTotalCars();
        double totalRevenue = parkingLot.getTotalRevenue();
        DecimalFormat df = new DecimalFormat("#.##");
        
        String stats = "Total Cars: " + totalCars + "\n" +
                       "Total Revenue: $" + df.format(totalRevenue);
        
        JOptionPane.showMessageDialog(this, stats, "Parking Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(55, 120, 190));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
