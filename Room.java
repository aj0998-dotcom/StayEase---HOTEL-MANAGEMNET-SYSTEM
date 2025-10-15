package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Room model class for the Hotel Management System
 * Represents a room entity and handles database operations
 */
public class Room {
    private int roomId;
    private String roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;
    private String description;
    
    // Constructors
    public Room() {}
    
    public Room(String roomNumber, String roomType, double pricePerNight, String description) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.isAvailable = true;
    }
    
    public Room(int roomId, String roomNumber, String roomType, double pricePerNight, boolean isAvailable, String description) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
        this.description = description;
    }
    
    // Getters and Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Add a new room to the database
     * @return true if successful, false otherwise
     */
    public boolean addRoom() {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, is_available, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setDouble(3, pricePerNight);
            stmt.setBoolean(4, isAvailable);
            stmt.setString(5, description);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.roomId = generatedKeys.getInt(1);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update room information in the database
     * @return true if successful, false otherwise
     */
    public boolean updateRoom() {
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, price_per_night = ?, is_available = ?, description = ? WHERE room_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setDouble(3, pricePerNight);
            stmt.setBoolean(4, isAvailable);
            stmt.setString(5, description);
            stmt.setInt(6, roomId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete room from the database
     * @param roomId ID of the room to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get room by ID
     * @param roomId ID of the room
     * @return Room object or null if not found
     */
    public static Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getBoolean("is_available"),
                    rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting room by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get room by room number
     * @param roomNumber Room number
     * @return Room object or null if not found
     */
    public static Room getRoomByNumber(String roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getBoolean("is_available"),
                    rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting room by number: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all rooms
     * @return List of all rooms
     */
    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getBoolean("is_available"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    /**
     * Get available rooms
     * @return List of available rooms
     */
    public static List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE is_available = TRUE ORDER BY room_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getBoolean("is_available"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    /**
     * Get rooms by type
     * @param roomType Type of room to search for
     * @return List of rooms of the specified type
     */
    public static List<Room> getRoomsByType(String roomType) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type = ? ORDER BY room_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getBoolean("is_available"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting rooms by type: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    /**
     * Get available rooms by type
     * @param roomType Type of room to search for
     * @return List of available rooms of the specified type
     */
    public static List<Room> getAvailableRoomsByType(String roomType) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type = ? AND is_available = TRUE ORDER BY room_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getBoolean("is_available"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available rooms by type: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    /**
     * Search rooms by room number or type
     * @param searchTerm Search term
     * @return List of matching rooms
     */
    public static List<Room> searchRooms(String searchTerm) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_number LIKE ? OR room_type LIKE ? OR description LIKE ? ORDER BY room_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getBoolean("is_available"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    /**
     * Update room availability
     * @param roomId ID of the room
     * @param available New availability status
     * @return true if successful, false otherwise
     */
    public static boolean updateRoomAvailability(int roomId, boolean available) {
        String sql = "UPDATE rooms SET is_available = ? WHERE room_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, available);
            stmt.setInt(2, roomId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating room availability: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("Room{ID: %d, Number: %s, Type: %s, Price: $%.2f, Available: %s}", 
                           roomId, roomNumber, roomType, pricePerNight, isAvailable ? "Yes" : "No");
    }
}
