package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection class for the Hotel Management System
 * Handles database connectivity and initialization
 */
public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private static Connection connection = null;
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found!");
                e.printStackTrace();
                throw new SQLException("Database driver not found", e);
            }
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Initialize database tables if they don't exist
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create rooms table
            String createRoomsTable = """
                CREATE TABLE IF NOT EXISTS rooms (
                    room_id INT PRIMARY KEY AUTO_INCREMENT,
                    room_number VARCHAR(10) UNIQUE NOT NULL,
                    room_type VARCHAR(50) NOT NULL,
                    price_per_night DECIMAL(10,2) NOT NULL,
                    is_available BOOLEAN DEFAULT TRUE,
                    description TEXT
                )
                """;
            
            // Create customers table
            String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INT PRIMARY KEY AUTO_INCREMENT,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    address TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            
            // Create bookings table
            String createBookingsTable = """
                CREATE TABLE IF NOT EXISTS bookings (
                    booking_id INT PRIMARY KEY AUTO_INCREMENT,
                    customer_id INT NOT NULL,
                    room_id INT NOT NULL,
                    check_in_date DATE NOT NULL,
                    check_out_date DATE NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    booking_status ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED') DEFAULT 'PENDING',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
                    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE CASCADE
                )
                """;
            
            // Create admin_users table
            String createAdminUsersTable = """
                CREATE TABLE IF NOT EXISTS admin_users (
                    admin_id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            
            stmt.execute(createRoomsTable);
            stmt.execute(createCustomersTable);
            stmt.execute(createBookingsTable);
            stmt.execute(createAdminUsersTable);
            
            // Insert sample data
            insertSampleData(stmt);
            
            System.out.println("Database tables created successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Insert sample data into tables
     */
    private static void insertSampleData(Statement stmt) throws SQLException {
        // Insert sample rooms
        String insertRooms = """
            INSERT IGNORE INTO rooms (room_number, room_type, price_per_night, description) VALUES
            ('101', 'Standard Single', 80.00, 'Comfortable single room with basic amenities'),
            ('102', 'Standard Double', 120.00, 'Spacious double room for two guests'),
            ('103', 'Deluxe Suite', 200.00, 'Luxury suite with premium amenities'),
            ('104', 'Standard Single', 80.00, 'Comfortable single room with basic amenities'),
            ('105', 'Standard Double', 120.00, 'Spacious double room for two guests'),
            ('201', 'Deluxe Suite', 200.00, 'Luxury suite with premium amenities'),
            ('202', 'Standard Single', 80.00, 'Comfortable single room with basic amenities'),
            ('203', 'Standard Double', 120.00, 'Spacious double room for two guests')
            """;
        
        // Insert sample admin user
        String insertAdmin = """
            INSERT IGNORE INTO admin_users (username, password, full_name, email) VALUES
            ('admin', 'admin123', 'System Administrator', 'admin@hotel.com')
            """;
        
        stmt.execute(insertRooms);
        stmt.execute(insertAdmin);
        
        System.out.println("Sample data inserted successfully!");
    }
}
