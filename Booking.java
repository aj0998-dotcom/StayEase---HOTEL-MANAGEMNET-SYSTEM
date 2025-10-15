package model;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Booking model class for the Hotel Management System
 * Represents a booking entity and handles database operations
 */
public class Booking {
    public enum BookingStatus {
        PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    }
    
    private int bookingId;
    private int customerId;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private BookingStatus bookingStatus;
    private Timestamp createdAt;
    
    // Additional fields for display purposes
    private String customerName;
    private String roomNumber;
    private String roomType;
    private double pricePerNight;
    
    // Constructors
    public Booking() {}
    
    public Booking(int customerId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, double totalAmount) {
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = BookingStatus.PENDING;
    }
    
    public Booking(int bookingId, int customerId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, 
                   double totalAmount, BookingStatus bookingStatus, Timestamp createdAt) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    
    /**
     * Calculate number of nights
     * @return Number of nights
     */
    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    /**
     * Calculate total amount based on room price and number of nights
     * @param pricePerNight Price per night
     * @return Calculated total amount
     */
    public double calculateTotalAmount(double pricePerNight) {
        long nights = getNumberOfNights();
        return nights * pricePerNight;
    }
    
    /**
     * Add a new booking to the database
     * @return true if successful, false otherwise
     */
    public boolean addBooking() {
        String sql = "INSERT INTO bookings (customer_id, room_id, check_in_date, check_out_date, total_amount, booking_status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, roomId);
            stmt.setDate(3, Date.valueOf(checkInDate));
            stmt.setDate(4, Date.valueOf(checkOutDate));
            stmt.setDouble(5, totalAmount);
            stmt.setString(6, bookingStatus.name());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.bookingId = generatedKeys.getInt(1);
                    }
                }
                
                // Update room availability
                Room.updateRoomAvailability(roomId, false);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update booking information in the database
     * @return true if successful, false otherwise
     */
    public boolean updateBooking() {
        String sql = "UPDATE bookings SET customer_id = ?, room_id = ?, check_in_date = ?, check_out_date = ?, total_amount = ?, booking_status = ? WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, roomId);
            stmt.setDate(3, Date.valueOf(checkInDate));
            stmt.setDate(4, Date.valueOf(checkOutDate));
            stmt.setDouble(5, totalAmount);
            stmt.setString(6, bookingStatus.name());
            stmt.setInt(7, bookingId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update booking status
     * @param bookingId ID of the booking
     * @param status New booking status
     * @return true if successful, false otherwise
     */
    public static boolean updateBookingStatus(int bookingId, BookingStatus status) {
        String sql = "UPDATE bookings SET booking_status = ? WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            stmt.setInt(2, bookingId);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0 && (status == BookingStatus.CANCELLED || status == BookingStatus.CHECKED_OUT)) {
                // Get room_id and make room available again
                String getRoomIdSql = "SELECT room_id FROM bookings WHERE booking_id = ?";
                try (PreparedStatement getRoomStmt = conn.prepareStatement(getRoomIdSql)) {
                    getRoomStmt.setInt(1, bookingId);
                    ResultSet rs = getRoomStmt.executeQuery();
                    if (rs.next()) {
                        int roomId = rs.getInt("room_id");
                        Room.updateRoomAvailability(roomId, true);
                    }
                }
            }
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete booking from the database
     * @param bookingId ID of the booking to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get booking by ID
     * @param bookingId ID of the booking
     * @return Booking object or null if not found
     */
    public static Booking getBookingById(int bookingId) {
        String sql = """
            SELECT b.*, CONCAT(c.first_name, ' ', c.last_name) as customer_name, 
                   r.room_number, r.room_type, r.price_per_night
            FROM bookings b
            JOIN customers c ON b.customer_id = c.customer_id
            JOIN rooms r ON b.room_id = r.room_id
            WHERE b.booking_id = ?
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("booking_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getDouble("total_amount"),
                    BookingStatus.valueOf(rs.getString("booking_status")),
                    rs.getTimestamp("created_at")
                );
                booking.setCustomerName(rs.getString("customer_name"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setRoomType(rs.getString("room_type"));
                booking.setPricePerNight(rs.getDouble("price_per_night"));
                return booking;
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all bookings
     * @return List of all bookings
     */
    public static List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, CONCAT(c.first_name, ' ', c.last_name) as customer_name, 
                   r.room_number, r.room_type, r.price_per_night
            FROM bookings b
            JOIN customers c ON b.customer_id = c.customer_id
            JOIN rooms r ON b.room_id = r.room_id
            ORDER BY b.created_at DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("booking_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getDouble("total_amount"),
                    BookingStatus.valueOf(rs.getString("booking_status")),
                    rs.getTimestamp("created_at")
                );
                booking.setCustomerName(rs.getString("customer_name"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setRoomType(rs.getString("room_type"));
                booking.setPricePerNight(rs.getDouble("price_per_night"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get bookings by customer ID
     * @param customerId ID of the customer
     * @return List of customer's bookings
     */
    public static List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, CONCAT(c.first_name, ' ', c.last_name) as customer_name, 
                   r.room_number, r.room_type, r.price_per_night
            FROM bookings b
            JOIN customers c ON b.customer_id = c.customer_id
            JOIN rooms r ON b.room_id = r.room_id
            WHERE b.customer_id = ?
            ORDER BY b.created_at DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("booking_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getDouble("total_amount"),
                    BookingStatus.valueOf(rs.getString("booking_status")),
                    rs.getTimestamp("created_at")
                );
                booking.setCustomerName(rs.getString("customer_name"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setRoomType(rs.getString("room_type"));
                booking.setPricePerNight(rs.getDouble("price_per_night"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by customer ID: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get bookings by status
     * @param status Booking status
     * @return List of bookings with the specified status
     */
    public static List<Booking> getBookingsByStatus(BookingStatus status) {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, CONCAT(c.first_name, ' ', c.last_name) as customer_name, 
                   r.room_number, r.room_type, r.price_per_night
            FROM bookings b
            JOIN customers c ON b.customer_id = c.customer_id
            JOIN rooms r ON b.room_id = r.room_id
            WHERE b.booking_status = ?
            ORDER BY b.created_at DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("booking_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getDouble("total_amount"),
                    BookingStatus.valueOf(rs.getString("booking_status")),
                    rs.getTimestamp("created_at")
                );
                booking.setCustomerName(rs.getString("customer_name"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setRoomType(rs.getString("room_type"));
                booking.setPricePerNight(rs.getDouble("price_per_night"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by status: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Check if a room is available for the given date range
     * @param roomId ID of the room
     * @param checkInDate Check-in date
     * @param checkOutDate Check-out date
     * @return true if available, false otherwise
     */
    public static boolean isRoomAvailable(int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        String sql = """
            SELECT COUNT(*) as count FROM bookings 
            WHERE room_id = ? AND booking_status IN ('CONFIRMED', 'CHECKED_IN') 
            AND ((check_in_date <= ? AND check_out_date > ?) OR 
                 (check_in_date < ? AND check_out_date >= ?) OR 
                 (check_in_date >= ? AND check_out_date <= ?))
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkOutDate));
            stmt.setDate(3, checkInDate);
            stmt.setDate(4, checkOutDate);
            stmt.setDate(5, checkInDate);
            stmt.setDate(6, checkInDate);
            stmt.setDate(7, checkOutDate);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking room availability: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("Booking{ID: %d, Customer: %s, Room: %s, Check-in: %s, Check-out: %s, Status: %s, Amount: $%.2f}", 
                           bookingId, customerName != null ? customerName : "Unknown", 
                           roomNumber != null ? roomNumber : "Unknown", 
                           checkInDate, checkOutDate, bookingStatus, totalAmount);
    }
}
