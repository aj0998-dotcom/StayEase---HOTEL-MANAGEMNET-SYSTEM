package controller;

import model.Room;
import view.AdminDashboard;
import view.BookingView;
import view.CustomerRegistrationView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Room Controller for the Hotel Management System
 * Handles room management operations
 */
public class RoomController {
    private AdminDashboard adminDashboard;
    private JDialog roomDialog;
    private JTable roomsTable;
    private DefaultTableModel roomsTableModel;
    
    public RoomController(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        this.roomsTable = adminDashboard.getRoomsTable();
        this.roomsTableModel = adminDashboard.getRoomsTableModel();
        
        setupEventHandlers();
        loadRooms();
    }
    
    private void setupEventHandlers() {
        // Room management buttons
        adminDashboard.addRoomsAddButtonListener(e -> showAddRoomDialog());
        adminDashboard.addRoomsEditButtonListener(e -> showEditRoomDialog());
        adminDashboard.addRoomsDeleteButtonListener(e -> deleteRoom());
        adminDashboard.addRoomsRefreshButtonListener(e -> loadRooms());
        adminDashboard.addRoomsSearchButtonListener(e -> searchRooms());
    }
    
    private void showAddRoomDialog() {
        roomDialog = createRoomDialog("Add New Room", null);
        roomDialog.setVisible(true);
    }
    
    private void showEditRoomDialog() {
        int selectedRow = adminDashboard.getSelectedRoomsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a room to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get room data from selected row
        String roomIdStr = (String) roomsTableModel.getValueAt(selectedRow, 0);
        int roomId = Integer.parseInt(roomIdStr);
        
        Room room = Room.getRoomById(roomId);
        if (room != null) {
            roomDialog = createRoomDialog("Edit Room", room);
            roomDialog.setVisible(true);
        }
    }
    
    private JDialog createRoomDialog(String title, Room room) {
        JDialog dialog = new JDialog(adminDashboard, title, true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(adminDashboard);
        dialog.setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Room Number
        JTextField roomNumberField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Number:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(roomNumberField, gbc);
        
        // Room Type
        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{
            "Standard Single", "Standard Double", "Deluxe Suite", "Executive Suite", "Presidential Suite"
        });
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Room Type:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(roomTypeCombo, gbc);
        
        // Price per Night
        JTextField priceField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Price per Night:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);
        
        // Available checkbox
        JCheckBox availableCheckBox = new JCheckBox("Available");
        availableCheckBox.setSelected(true);
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(availableCheckBox, gbc);
        
        // Description
        JTextArea descriptionArea = new JTextArea(4, 15);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(descriptionScroll, gbc);
        
        // Populate fields if editing
        boolean isEditMode = (room != null);
        if (isEditMode) {
            roomNumberField.setText(room.getRoomNumber());
            roomTypeCombo.setSelectedItem(room.getRoomType());
            priceField.setText(String.valueOf(room.getPricePerNight()));
            availableCheckBox.setSelected(room.isAvailable());
            descriptionArea.setText(room.getDescription());
        }
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton(isEditMode ? "Update" : "Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            if (validateAndSaveRoom(roomNumberField, roomTypeCombo, priceField, availableCheckBox, descriptionArea, room)) {
                dialog.dispose();
                loadRooms();
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        return dialog;
    }
    
    private boolean validateAndSaveRoom(JTextField roomNumberField, JComboBox<String> roomTypeCombo, 
                                      JTextField priceField, JCheckBox availableCheckBox, 
                                      JTextArea descriptionArea, Room existingRoom) {
        // Validation
        if (roomNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(adminDashboard, "Room number is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            roomNumberField.requestFocus();
            return false;
        }
        
        if (roomTypeCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(adminDashboard, "Please select a room type!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price <= 0) {
                JOptionPane.showMessageDialog(adminDashboard, "Price must be greater than 0!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                priceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(adminDashboard, "Please enter a valid price!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return false;
        }
        
        // Create or update room
        Room room;
        if (existingRoom != null) {
            // Update existing room
            room = existingRoom;
            room.setRoomNumber(roomNumberField.getText().trim());
            room.setRoomType((String) roomTypeCombo.getSelectedItem());
            room.setPricePerNight(Double.parseDouble(priceField.getText().trim()));
            room.setAvailable(availableCheckBox.isSelected());
            room.setDescription(descriptionArea.getText().trim());
            
            if (room.updateRoom()) {
                JOptionPane.showMessageDialog(adminDashboard, "Room updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(adminDashboard, "Failed to update room!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            // Create new room
            room = new Room(
                roomNumberField.getText().trim(),
                (String) roomTypeCombo.getSelectedItem(),
                Double.parseDouble(priceField.getText().trim()),
                descriptionArea.getText().trim()
            );
            room.setAvailable(availableCheckBox.isSelected());
            
            if (room.addRoom()) {
                JOptionPane.showMessageDialog(adminDashboard, "Room added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(adminDashboard, "Failed to add room!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
    
    private void deleteRoom() {
        int selectedRow = adminDashboard.getSelectedRoomsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a room to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String roomNumber = (String) roomsTableModel.getValueAt(selectedRow, 1);
        String roomType = (String) roomsTableModel.getValueAt(selectedRow, 2);
        
        int result = JOptionPane.showConfirmDialog(adminDashboard, 
            "Are you sure you want to delete room " + roomNumber + " (" + roomType + ")?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            String roomIdStr = (String) roomsTableModel.getValueAt(selectedRow, 0);
            int roomId = Integer.parseInt(roomIdStr);
            
            if (Room.deleteRoom(roomId)) {
                JOptionPane.showMessageDialog(adminDashboard, "Room deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(adminDashboard, "Failed to delete room!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadRooms() {
        // Clear existing data
        roomsTableModel.setRowCount(0);
        
        // Load all rooms
        List<Room> rooms = Room.getAllRooms();
        for (Room room : rooms) {
            Object[] rowData = {
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                String.format("$%.2f", room.getPricePerNight()),
                room.isAvailable() ? "Yes" : "No",
                room.getDescription()
            };
            roomsTableModel.addRow(rowData);
        }
        
        System.out.println("Loaded " + rooms.size() + " rooms");
    }
    
    private void searchRooms() {
        String searchTerm = adminDashboard.getRoomsSearchText();
        
        if (searchTerm.isEmpty()) {
            loadRooms();
            return;
        }
        
        // Clear existing data
        roomsTableModel.setRowCount(0);
        
        // Search rooms
        List<Room> rooms = Room.searchRooms(searchTerm);
        for (Room room : rooms) {
            Object[] rowData = {
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                String.format("$%.2f", room.getPricePerNight()),
                room.isAvailable() ? "Yes" : "No",
                room.getDescription()
            };
            roomsTableModel.addRow(rowData);
        }
        
        System.out.println("Found " + rooms.size() + " rooms matching: " + searchTerm);
    }
    
    // Method to get available rooms for booking
    public List<Room> getAvailableRooms() {
        return Room.getAvailableRooms();
    }
    
    // Method to get available rooms by type
    public List<Room> getAvailableRoomsByType(String roomType) {
        return Room.getAvailableRoomsByType(roomType);
    }
    
    // Method to get room by ID
    public Room getRoomById(int roomId) {
        return Room.getRoomById(roomId);
    }
    
    // Method to update room availability
    public boolean updateRoomAvailability(int roomId, boolean available) {
        return Room.updateRoomAvailability(roomId, available);
    }
}
