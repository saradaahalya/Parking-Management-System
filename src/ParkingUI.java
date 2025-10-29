package src;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class ParkingUI extends JFrame {
    private List<Region> regions;
    private Region currentRegion;
    private JPanel slotPanel;
    private JTextArea consoleArea;
    private HashMap<Integer, JPanel> slotBoxes;
    private JLabel revenueLabel;
    private JComboBox<String> rateTypeCombo;
    private JTextField rateField;
    private JComboBox<String> regionCombo;
    
    public ParkingUI(java.util.List<Region> regions) {
        this.regions = regions;
        this.currentRegion = (regions == null || regions.isEmpty()) ? null : regions.get(0);
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
        
        // Region selector
        JPanel regionPanel = new JPanel();
        regionCombo = new JComboBox<>();
        for (Region r : regions) regionCombo.addItem(r.getName());
        regionCombo.addActionListener(e -> {
            int idx = regionCombo.getSelectedIndex();
            if (idx >= 0 && idx < regions.size()) {
                currentRegion = regions.get(idx);
                updateSlotDisplay();
            }
        });
        regionPanel.add(new JLabel("Region:"));
        regionPanel.add(regionCombo);
        headerPanel.add(regionPanel);
        
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
            case "Park Car":
                parkCar();
                break;
            case "Remove Car":
                removeCar();
                break;
            case "Search Car":
                searchCar();
                break;
            case "Show Statistics":
                showStatistics();
                break;
            case "Refresh":
                updateSlotDisplay();
                break;
            default:
                // no-op
                break;
        }
    }

    private void parkCar() {
        JTextField plateField = new JTextField();
        JTextField ownerField = new JTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"REGULAR", "VIP"});
        JTextField colorField = new JTextField();
        JComboBox<String> dialogRegionCombo = new JComboBox<>();
        for (Region r : regions) dialogRegionCombo.addItem(r.getName());

        Object[] fields = {
            "Number Plate:", plateField,
            "Owner Name:", ownerField,
            "Type:", typeCombo,
            "Region:", dialogRegionCombo,
            "Color:", colorField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, 
            "Enter Car Details", JOptionPane.OK_CANCEL_OPTION);
            
        if (result == JOptionPane.OK_OPTION) {
            String plate = plateField.getText().trim();
            if (plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Number plate cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean isVip = typeCombo.getSelectedItem().equals("VIP");
            int sel = dialogRegionCombo.getSelectedIndex();
            Region target = (sel >= 0 && sel < regions.size()) ? regions.get(sel) : currentRegion;

            // Ensure uniqueness across all regions
            for (Region r : regions) {
                if (r.getParkingLot().findCar(plate)) {
                    JOptionPane.showMessageDialog(this, "A car with this number plate is already parked in " + r.getName() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (target == null) {
                JOptionPane.showMessageDialog(this, "No region selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int suggested = target.getParkingLot().peekAppropriateSlot(isVip);
            if (suggested == -1) {
                JOptionPane.showMessageDialog(this, "No appropriate slots available in " + target.getName(), "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    String.format("Please go to Slot %d in %s. Click OK to confirm parking.", suggested, target.getName()),
                    "Confirm Slot",
                    JOptionPane.OK_CANCEL_OPTION);

            if (confirm == JOptionPane.OK_OPTION) {
                // actually park at the suggested slot
                target.getParkingLot().parkCarAt(plate, isVip, suggested);
                consoleArea.append("Parked " + plate + " in " + target.getName() + " at slot " + suggested + "\n");
            } else {
                consoleArea.append("Parking cancelled for " + plate + "\n");
            }

            updateSlotDisplay();
        }
    }

    private void removeCar() {
        JTextField plateField = new JTextField();
        JComboBox<String> dialogRegionCombo = new JComboBox<>();
        dialogRegionCombo.addItem("All Regions");
        for (Region r : regions) dialogRegionCombo.addItem(r.getName());

        Object[] fields = {
            "Number Plate:", plateField,
            "Region:", dialogRegionCombo
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Remove Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String plate = plateField.getText().trim();
            if (plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Number plate cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int sel = dialogRegionCombo.getSelectedIndex(); // 0 => All Regions, else region index+1
            boolean removed = false;
            double charge = 0.0;
            if (sel == 0) {
                // search all
                for (Region r : regions) {
                    if (r.getParkingLot().findCar(plate)) {
                        charge = r.getParkingLot().removeCar(plate);
                        removed = true;
                        consoleArea.append("Removed from " + r.getName() + "\n");
                        break;
                    }
                }
            } else {
                int ridx = sel - 1;
                if (ridx >= 0 && ridx < regions.size()) {
                    Region target = regions.get(ridx);
                    if (target.getParkingLot().findCar(plate)) {
                        charge = target.getParkingLot().removeCar(plate);
                        removed = true;
                        consoleArea.append("Removed from " + target.getName() + "\n");
                    } else {
                        JOptionPane.showMessageDialog(this, "Car not found in selected region.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            consoleArea.append("Car " + plate + (removed ? " removed." : " not found.") + "\n");
            if (removed) {
                consoleArea.append(String.format("Parking charge: $%.2f\n", charge));
            }
            updateSlotDisplay();
        }
    }

    private void searchCar() {
        String plate = JOptionPane.showInputDialog(this, "Enter Car Number to Search:");
        if (plate != null && !plate.trim().isEmpty()) {
            boolean found = false;
            // Search current region first
            if (currentRegion != null && currentRegion.getParkingLot().findCar(plate)) {
                found = true;
            } else {
                for (Region r : regions) {
                    if (r.getParkingLot().findCar(plate)) { found = true; break; }
                }
            }
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
            if (currentRegion != null) currentRegion.getParkingLot().setRate(rateType, rate);
            JOptionPane.showMessageDialog(this, "Rate updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rate value.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStatistics() {
        int totalCars = currentRegion == null ? 0 : currentRegion.getParkingLot().getTotalCars();
        double totalRevenue = currentRegion == null ? 0.0 : currentRegion.getParkingLot().getTotalRevenue();
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
        // Ensure background/foreground are applied on all LookAndFeels (macOS may ignore otherwise)
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
    }

    private void updateSlotDisplay() {
        slotPanel.removeAll();
        slotBoxes.clear();

        int totalSlots = currentRegion == null ? 0 : currentRegion.getParkingLot().getTotalSlots();
        for (int i = 1; i <= totalSlots; i++) {
            JPanel slotBox = new JPanel();
            // Reduce size from 80x80 to 60x60
            slotBox.setPreferredSize(new Dimension(60, 60));
            slotBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));  // Also made border thinner
            slotBox.setLayout(new BorderLayout());

            JLabel label = new JLabel("Slot " + i, JLabel.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));  // Reduced font size slightly
            slotBox.add(label, BorderLayout.CENTER);

            ParkingLot p = currentRegion == null ? null : currentRegion.getParkingLot();
            boolean occupied = p != null && p.isSlotOccupied(i);
            if (occupied) {
                slotBox.setBackground(new Color(220, 70, 70)); // red
            } else {
                slotBox.setBackground(new Color(90, 200, 90)); // green
            }

            slotBoxes.put(i, slotBox);
            slotPanel.add(slotBox);
        }

        slotPanel.revalidate();
        slotPanel.repaint();

        // Update revenue label for the currently selected region
        double rev = currentRegion == null ? 0.0 : currentRegion.getParkingLot().getTotalRevenue();
        DecimalFormat df = new DecimalFormat("#.##");
        revenueLabel.setText("Total Revenue: $" + df.format(rev));
    }
}
