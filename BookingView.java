package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Booking view for creating and editing hotel bookings
 * Provides user interface for booking management
 */
public class BookingView extends JDialog {
    private JTextField bookingIdField;
    private JComboBox<String> customerComboBox;
    private JComboBox<String> roomComboBox;
    private JFormattedTextField checkInDateField;
    private JFormattedTextField checkOutDateField;
    private JTextField totalAmountField;
    private JComboBox<String> statusComboBox;
    
    private JButton saveButton;
    private JButton cancelButton;
    private JButton calculateButton;
    
    private JLabel customerNameLabel;
    private JLabel roomDetailsLabel;
    private JLabel nightsLabel;
    private JLabel pricePerNightLabel;
    
    private boolean isEditMode = false;
    
    public BookingView(JFrame parent, String title) {
        super(parent, title, true);
        initializeComponents();
        setupLayout();
        setupDialog();
    }
    
    private void initializeComponents() {
        // Initialize fields
        bookingIdField = new JTextField(10);
        bookingIdField.setEditable(false);
        bookingIdField.setBackground(Color.LIGHT_GRAY);
        
        customerComboBox = new JComboBox<>();
        customerComboBox.setPreferredSize(new Dimension(200, 25));
        
        roomComboBox = new JComboBox<>();
        roomComboBox.setPreferredSize(new Dimension(200, 25));
        
        // Date fields with formatters
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        checkInDateField = new JFormattedTextField(dateFormatter);
        checkOutDateField = new JFormattedTextField(dateFormatter);
        checkInDateField.setColumns(10);
        checkOutDateField.setColumns(10);
        
        // Set default dates
        checkInDateField.setValue(LocalDate.now());
        checkOutDateField.setValue(LocalDate.now().plusDays(1));
        
        totalAmountField = new JTextField(10);
        totalAmountField.setEditable(false);
        totalAmountField.setBackground(Color.LIGHT_GRAY);
        
        statusComboBox = new JComboBox<>(new String[]{"PENDING", "CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED"});
        statusComboBox.setPreferredSize(new Dimension(150, 25));
        
        // Initialize buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        calculateButton = new JButton("Calculate Amount");
        
        saveButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setPreferredSize(new Dimension(100, 30));
        calculateButton.setPreferredSize(new Dimension(150, 30));
        
        // Initialize labels
        customerNameLabel = new JLabel("");
        roomDetailsLabel = new JLabel("");
        nightsLabel = new JLabel("");
        pricePerNightLabel = new JLabel("");
        
        customerNameLabel.setForeground(Color.BLUE);
        roomDetailsLabel.setForeground(Color.BLUE);
        nightsLabel.setForeground(Color.GREEN);
        pricePerNightLabel.setForeground(Color.GREEN);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 73, 94));
        JLabel titleLabel = new JLabel("Booking Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Booking ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Booking ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(bookingIdField, gbc);
        
        // Customer selection
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Customer:"), gbc);
        gbc.gridx = 1;
        formPanel.add(customerComboBox, gbc);
        gbc.gridx = 2;
        formPanel.add(customerNameLabel, gbc);
        
        // Room selection
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roomComboBox, gbc);
        gbc.gridx = 2;
        formPanel.add(roomDetailsLabel, gbc);
        
        // Check-in date
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Check-in Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkInDateField, gbc);
        
        // Check-out date
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Check-out Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkOutDateField, gbc);
        
        // Calculate button
        gbc.gridx = 2;
        formPanel.add(calculateButton, gbc);
        
        // Price per night and nights
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Price/Night:"), gbc);
        gbc.gridx = 1;
        formPanel.add(pricePerNightLabel, gbc);
        gbc.gridx = 2;
        formPanel.add(nightsLabel, gbc);
        
        // Total amount
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        formPanel.add(totalAmountField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusComboBox, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    // Getters
    public String getBookingId() { return bookingIdField.getText().trim(); }
    public String getSelectedCustomer() { return (String) customerComboBox.getSelectedItem(); }
    public String getSelectedRoom() { return (String) roomComboBox.getSelectedItem(); }
    public LocalDate getCheckInDate() { return (LocalDate) checkInDateField.getValue(); }
    public LocalDate getCheckOutDate() { return (LocalDate) checkOutDateField.getValue(); }
    public String getTotalAmount() { return totalAmountField.getText().trim(); }
    public String getSelectedStatus() { return (String) statusComboBox.getSelectedItem(); }
    
    // Setters
    public void setBookingId(String id) { bookingIdField.setText(id); }
    public void setSelectedCustomer(String customer) { customerComboBox.setSelectedItem(customer); }
    public void setSelectedRoom(String room) { roomComboBox.setSelectedItem(room); }
    public void setCheckInDate(LocalDate date) { checkInDateField.setValue(date); }
    public void setCheckOutDate(LocalDate date) { checkOutDateField.setValue(date); }
    public void setTotalAmount(String amount) { totalAmountField.setText(amount); }
    public void setSelectedStatus(String status) { statusComboBox.setSelectedItem(status); }
    
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
        if (editMode) {
            setTitle("Edit Booking");
            saveButton.setText("Update");
        } else {
            setTitle("New Booking");
            saveButton.setText("Save");
            clearFields();
        }
    }
    
    public void setCustomerName(String name) { customerNameLabel.setText(name); }
    public void setRoomDetails(String details) { roomDetailsLabel.setText(details); }
    public void setNights(String nights) { nightsLabel.setText(nights); }
    public void setPricePerNight(String price) { pricePerNightLabel.setText(price); }
    
    // ComboBox management
    public void setCustomerComboBoxData(String[] customers) {
        customerComboBox.removeAllItems();
        for (String customer : customers) {
            customerComboBox.addItem(customer);
        }
    }
    
    public void setRoomComboBoxData(String[] rooms) {
        roomComboBox.removeAllItems();
        for (String room : rooms) {
            roomComboBox.addItem(room);
        }
    }
    
    // Action listeners
    public void addSaveButtonListener(ActionListener listener) { saveButton.addActionListener(listener); }
    public void addCancelButtonListener(ActionListener listener) { cancelButton.addActionListener(listener); }
    public void addCalculateButtonListener(ActionListener listener) { calculateButton.addActionListener(listener); }
    public void addCustomerComboBoxListener(ActionListener listener) { customerComboBox.addActionListener(listener); }
    public void addRoomComboBoxListener(ActionListener listener) { roomComboBox.addActionListener(listener); }
    public void addCheckInDateListener(ActionListener listener) { checkInDateField.addActionListener(listener); }
    public void addCheckOutDateListener(ActionListener listener) { checkOutDateField.addActionListener(listener); }
    
    // Validation methods
    public boolean validateInput() {
        if (getSelectedCustomer() == null || getSelectedCustomer().trim().isEmpty()) {
            showErrorMessage("Please select a customer!");
            customerComboBox.requestFocus();
            return false;
        }
        
        if (getSelectedRoom() == null || getSelectedRoom().trim().isEmpty()) {
            showErrorMessage("Please select a room!");
            roomComboBox.requestFocus();
            return false;
        }
        
        if (getCheckInDate() == null) {
            showErrorMessage("Please enter a valid check-in date!");
            checkInDateField.requestFocus();
            return false;
        }
        
        if (getCheckOutDate() == null) {
            showErrorMessage("Please enter a valid check-out date!");
            checkOutDateField.requestFocus();
            return false;
        }
        
        if (getCheckOutDate().isBefore(getCheckInDate()) || getCheckOutDate().isEqual(getCheckInDate())) {
            showErrorMessage("Check-out date must be after check-in date!");
            checkOutDateField.requestFocus();
            return false;
        }
        
        if (getCheckInDate().isBefore(LocalDate.now())) {
            showErrorMessage("Check-in date cannot be in the past!");
            checkInDateField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // Utility methods
    public void clearFields() {
        setBookingId("");
        customerComboBox.setSelectedIndex(-1);
        roomComboBox.setSelectedIndex(-1);
        setCheckInDate(LocalDate.now());
        setCheckOutDate(LocalDate.now().plusDays(1));
        setTotalAmount("");
        setSelectedStatus("PENDING");
        setCustomerName("");
        setRoomDetails("");
        setNights("");
        setPricePerNight("");
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void setLoadingState(boolean loading) {
        saveButton.setEnabled(!loading);
        if (loading) {
            saveButton.setText(isEditMode ? "Updating..." : "Saving...");
        } else {
            saveButton.setText(isEditMode ? "Update" : "Save");
        }
    }
    
    // Helper methods for extracting IDs
    public int getSelectedCustomerId() {
        String selected = getSelectedCustomer();
        if (selected != null && selected.contains("-")) {
            try {
                return Integer.parseInt(selected.split("-")[0].trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
    
    public int getSelectedRoomId() {
        String selected = getSelectedRoom();
        if (selected != null && selected.contains("-")) {
            try {
                return Integer.parseInt(selected.split("-")[0].trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
    
    public long calculateNights() {
        if (getCheckInDate() != null && getCheckOutDate() != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(getCheckInDate(), getCheckOutDate());
        }
        return 0;
    }
    
    public void display() {
        setVisible(true);
    }
    
    public void dispose() {
        super.dispose();
    }
}
