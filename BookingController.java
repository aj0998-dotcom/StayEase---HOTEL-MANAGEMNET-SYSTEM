package controller;

import model.Booking;
import model.Customer;
import model.Room;
import view.AdminDashboard;
import view.BookingView;
import view.BillView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Booking Controller for the Hotel Management System
 * Handles booking management operations
 */
public class BookingController {
    private AdminDashboard adminDashboard;
    private BookingView bookingView;
    private BillView billView;
    private RoomController roomController;
    private CustomerController customerController;
    private JTable bookingsTable;
    private DefaultTableModel bookingsTableModel;
    
    public BookingController(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        this.bookingsTable = adminDashboard.getBookingsTable();
        this.bookingsTableModel = adminDashboard.getBookingsTableModel();
        
        // Initialize controllers
        this.roomController = new RoomController(adminDashboard);
        this.customerController = new CustomerController(adminDashboard);
        
        setupEventHandlers();
        loadBookings();
    }
    
    private void setupEventHandlers() {
        // Booking management buttons
        adminDashboard.addBookingsAddButtonListener(e -> showAddBookingDialog());
        adminDashboard.addBookingsEditButtonListener(e -> showEditBookingDialog());
        adminDashboard.addBookingsDeleteButtonListener(e -> deleteBooking());
        adminDashboard.addBookingsRefreshButtonListener(e -> loadBookings());
        adminDashboard.addBookingsConfirmButtonListener(e -> confirmBooking());
        adminDashboard.addBookingsCheckInButtonListener(e -> checkInBooking());
        adminDashboard.addBookingsCheckOutButtonListener(e -> checkOutBooking());
        adminDashboard.addBookingsCancelButtonListener(e -> cancelBooking());
    }
    
    private void showAddBookingDialog() {
        bookingView = new BookingView(adminDashboard, "New Booking");
        bookingView.setEditMode(false);
        populateBookingViewData();
        setupBookingViewEventHandlers();
        bookingView.display();
    }
    
    private void showEditBookingDialog() {
        int selectedRow = adminDashboard.getSelectedBookingsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a booking to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get booking data from selected row
        String bookingIdStr = (String) bookingsTableModel.getValueAt(selectedRow, 0);
        int bookingId = Integer.parseInt(bookingIdStr);
        
        Booking booking = Booking.getBookingById(bookingId);
        if (booking != null) {
            bookingView = new BookingView(adminDashboard, "Edit Booking");
            bookingView.setEditMode(true);
            populateBookingViewData();
            populateBookingView(booking);
            setupBookingViewEventHandlers();
            bookingView.display();
        }
    }
    
    private void populateBookingViewData() {
        // Populate customer combo box
        String[] customerData = customerController.createCustomerComboBoxData();
        bookingView.setCustomerComboBoxData(customerData);
        
        // Populate room combo box with available rooms
        List<Room> availableRooms = roomController.getAvailableRooms();
        String[] roomData = new String[availableRooms.size()];
        for (int i = 0; i < availableRooms.size(); i++) {
            Room room = availableRooms.get(i);
            roomData[i] = room.getRoomId() + " - " + room.getRoomNumber() + " (" + room.getRoomType() + ")";
        }
        bookingView.setRoomComboBoxData(roomData);
    }
    
    private void populateBookingView(Booking booking) {
        bookingView.setBookingId(String.valueOf(booking.getBookingId()));
        
        // Set customer
        String customerData = booking.getCustomerId() + " - " + booking.getCustomerName();
        bookingView.setSelectedCustomer(customerData);
        
        // Set room
        String roomData = booking.getRoomId() + " - " + booking.getRoomNumber() + " (" + booking.getRoomType() + ")";
        bookingView.setSelectedRoom(roomData);
        
        bookingView.setCheckInDate(booking.getCheckInDate());
        bookingView.setCheckOutDate(booking.getCheckOutDate());
        bookingView.setTotalAmount(String.format("%.2f", booking.getTotalAmount()));
        bookingView.setSelectedStatus(booking.getBookingStatus().name());
        
        // Set additional info
        bookingView.setCustomerName(booking.getCustomerName());
        bookingView.setRoomDetails(booking.getRoomType() + " - $" + booking.getPricePerNight() + "/night");
        bookingView.setNights(booking.getNumberOfNights() + " nights");
        bookingView.setPricePerNight("$" + String.format("%.2f", booking.getPricePerNight()));
    }
    
    private void setupBookingViewEventHandlers() {
        bookingView.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveBooking();
            }
        });
        
        bookingView.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookingView.dispose();
            }
        });
        
        bookingView.addCalculateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBookingAmount();
            }
        });
        
        bookingView.addRoomComboBoxListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoomDetails();
            }
        });
        
        bookingView.addCheckInDateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBookingAmount();
            }
        });
        
        bookingView.addCheckOutDateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBookingAmount();
            }
        });
    }
    
    private void updateRoomDetails() {
        int roomId = bookingView.getSelectedRoomId();
        if (roomId != -1) {
            Room room = roomController.getRoomById(roomId);
            if (room != null) {
                bookingView.setRoomDetails(room.getRoomType() + " - $" + room.getPricePerNight() + "/night");
                bookingView.setPricePerNight("$" + String.format("%.2f", room.getPricePerNight()));
                calculateBookingAmount();
            }
        }
    }
    
    private void calculateBookingAmount() {
        if (bookingView.getCheckInDate() != null && bookingView.getCheckOutDate() != null) {
            long nights = bookingView.calculateNights();
            bookingView.setNights(nights + " nights");
            
            int roomId = bookingView.getSelectedRoomId();
            if (roomId != -1) {
                Room room = roomController.getRoomById(roomId);
                if (room != null) {
                    double totalAmount = room.getPricePerNight() * nights;
                    bookingView.setTotalAmount(String.format("%.2f", totalAmount));
                }
            }
        }
    }
    
    private void handleSaveBooking() {
        if (!bookingView.validateInput()) {
            return;
        }
        
        bookingView.setLoadingState(true);
        
        try {
            Booking booking;
            boolean success = false;
            String message = "";
            
            if (bookingView.getTitle().equals("Edit Booking")) {
                // Update existing booking
                booking = Booking.getBookingById(Integer.parseInt(bookingView.getBookingId()));
                if (booking != null) {
                    booking.setCustomerId(bookingView.getSelectedCustomerId());
                    booking.setRoomId(bookingView.getSelectedRoomId());
                    booking.setCheckInDate(bookingView.getCheckInDate());
                    booking.setCheckOutDate(bookingView.getCheckOutDate());
                    booking.setTotalAmount(Double.parseDouble(bookingView.getTotalAmount()));
                    booking.setBookingStatus(Booking.BookingStatus.valueOf(bookingView.getSelectedStatus()));
                    
                    success = booking.updateBooking();
                    message = success ? "Booking updated successfully!" : "Failed to update booking!";
                }
            } else {
                // Create new booking
                int customerId = bookingView.getSelectedCustomerId();
                int roomId = bookingView.getSelectedRoomId();
                LocalDate checkIn = bookingView.getCheckInDate();
                LocalDate checkOut = bookingView.getCheckOutDate();
                double totalAmount = Double.parseDouble(bookingView.getTotalAmount());
                
                // Check room availability
                if (!Booking.isRoomAvailable(roomId, checkIn, checkOut)) {
                    bookingView.showErrorMessage("Room is not available for the selected dates!");
                    bookingView.setLoadingState(false);
                    return;
                }
                
                booking = new Booking(customerId, roomId, checkIn, checkOut, totalAmount);
                success = booking.addBooking();
                message = success ? "Booking created successfully!" : "Failed to create booking!";
            }
            
            if (success) {
                bookingView.showSuccessMessage(message);
                bookingView.dispose();
                loadBookings();
            } else {
                bookingView.showErrorMessage(message);
            }
            
        } catch (Exception e) {
            bookingView.showErrorMessage("Error: " + e.getMessage());
        } finally {
            bookingView.setLoadingState(false);
        }
    }
    
    private void confirmBooking() {
        int selectedRow = adminDashboard.getSelectedBookingsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a booking to confirm.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bookingIdStr = (String) bookingsTableModel.getValueAt(selectedRow, 0);
        int bookingId = Integer.parseInt(bookingIdStr);
        
        if (Booking.updateBookingStatus(bookingId, Booking.BookingStatus.CONFIRMED)) {
            JOptionPane.showMessageDialog(adminDashboard, "Booking confirmed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBookings();
        } else {
            JOptionPane.showMessageDialog(adminDashboard, "Failed to confirm booking!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void checkInBooking() {
        int selectedRow = adminDashboard.getSelectedBookingsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a booking to check in.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bookingIdStr = (String) bookingsTableModel.getValueAt(selectedRow, 0);
        int bookingId = Integer.parseInt(bookingIdStr);
        
        if (Booking.updateBookingStatus(bookingId, Booking.BookingStatus.CHECKED_IN)) {
            JOptionPane.showMessageDialog(adminDashboard, "Guest checked in successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBookings();
        } else {
            JOptionPane.showMessageDialog(adminDashboard, "Failed to check in guest!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void checkOutBooking() {
        int selectedRow = adminDashboard.getSelectedBookingsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a booking to check out.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bookingIdStr = (String) bookingsTableModel.getValueAt(selectedRow, 0);
        int bookingId = Integer.parseInt(bookingIdStr);
        
        if (Booking.updateBookingStatus(bookingId, Booking.BookingStatus.CHECKED_OUT)) {
            JOptionPane.showMessageDialog(adminDashboard, "Guest checked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBookings();
        } else {
            JOptionPane.showMessageDialog(adminDashboard, "Failed to check out guest!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelBooking() {
        int selectedRow = adminDashboard.getSelectedBookingsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a booking to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bookingIdStr = (String) bookingsTableModel.getValueAt(selectedRow, 0);
        int bookingId = Integer.parseInt(bookingIdStr);
        
        int result = JOptionPane.showConfirmDialog(adminDashboard, 
            "Are you sure you want to cancel this booking?", 
            "Confirm Cancellation", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (Booking.updateBookingStatus(bookingId, Booking.BookingStatus.CANCELLED)) {
                JOptionPane.showMessageDialog(adminDashboard, "Booking cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookings();
            } else {
                JOptionPane.showMessageDialog(adminDashboard, "Failed to cancel booking!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteBooking() {
        int selectedRow = adminDashboard.getSelectedBookingsRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a booking to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bookingIdStr = (String) bookingsTableModel.getValueAt(selectedRow, 0);
        int bookingId = Integer.parseInt(bookingIdStr);
        
        int result = JOptionPane.showConfirmDialog(adminDashboard, 
            "Are you sure you want to delete this booking?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (Booking.deleteBooking(bookingId)) {
                JOptionPane.showMessageDialog(adminDashboard, "Booking deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookings();
            } else {
                JOptionPane.showMessageDialog(adminDashboard, "Failed to delete booking!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadBookings() {
        // Clear existing data
        bookingsTableModel.setRowCount(0);
        
        // Load all bookings
        List<Booking> bookings = Booking.getAllBookings();
        for (Booking booking : bookings) {
            Object[] rowData = {
                booking.getBookingId(),
                booking.getCustomerName(),
                booking.getRoomNumber(),
                booking.getCheckInDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                booking.getCheckOutDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.format("$%.2f", booking.getTotalAmount()),
                booking.getBookingStatus().name(),
                booking.getCreatedAt().toString().split(" ")[0] // Date only
            };
            bookingsTableModel.addRow(rowData);
        }
        
        System.out.println("Loaded " + bookings.size() + " bookings");
    }
    
    // Method to show bill for selected booking
    public void showBill(int bookingId) {
        Booking booking = Booking.getBookingById(bookingId);
        if (booking != null) {
            billView = new BillView(adminDashboard, "Booking Invoice");
            populateBillView(booking);
            setupBillViewEventHandlers();
            billView.display();
        }
    }
    
    private void populateBillView(Booking booking) {
        billView.setBookingId(String.valueOf(booking.getBookingId()));
        billView.setCustomerName(booking.getCustomerName());
        billView.setCustomerEmail(customerController.getCustomerEmailById(booking.getCustomerId()));
        billView.setCustomerPhone(customerController.getCustomerPhoneById(booking.getCustomerId()));
        billView.setRoomNumber(booking.getRoomNumber());
        billView.setRoomType(booking.getRoomType());
        billView.setCheckInDate(booking.getCheckInDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        billView.setCheckOutDate(booking.getCheckOutDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        billView.setNights(String.valueOf(booking.getNumberOfNights()));
        billView.setPricePerNight("$" + String.format("%.2f", booking.getPricePerNight()));
        
        double subtotal = booking.getTotalAmount();
        double tax = subtotal * 0.10; // 10% tax
        double total = subtotal + tax;
        
        billView.setSubtotal("$" + String.format("%.2f", subtotal));
        billView.setTax("$" + String.format("%.2f", tax));
        billView.setTotalAmount("$" + String.format("%.2f", total));
        billView.setStatus(booking.getBookingStatus().name());
        billView.setIssueDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
    
    private void setupBillViewEventHandlers() {
        billView.addPrintButtonListener(e -> printBill());
        billView.addEmailButtonListener(e -> emailBill());
        billView.addCloseButtonListener(e -> billView.dispose());
        billView.addRefreshButtonListener(e -> {
            // Refresh bill data if needed
        });
    }
    
    private void printBill() {
        // Implementation for printing bill
        JOptionPane.showMessageDialog(billView, "Print functionality would be implemented here.", "Print Bill", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void emailBill() {
        // Implementation for emailing bill
        JOptionPane.showMessageDialog(billView, "Email functionality would be implemented here.", "Email Bill", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Method to get bookings by status
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return Booking.getBookingsByStatus(status);
    }
    
    // Method to get bookings by customer
    public List<Booking> getBookingsByCustomer(int customerId) {
        return Booking.getBookingsByCustomerId(customerId);
    }
    
    // Method to get booking count
    public int getBookingCount() {
        return Booking.getAllBookings().size();
    }
    
    // Method to refresh bookings data
    public void refreshBookings() {
        loadBookings();
    }
}
