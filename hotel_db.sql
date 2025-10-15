-- Hotel Management System Database Schema
-- Created for MySQL Database

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS admin_users;

-- Create admin_users table
CREATE TABLE admin_users (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create rooms table
CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create customers table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create bookings table
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    booking_status ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE CASCADE,
    INDEX idx_booking_dates (check_in_date, check_out_date),
    INDEX idx_booking_status (booking_status),
    INDEX idx_booking_customer (customer_id),
    INDEX idx_booking_room (room_id)
);

-- Insert sample admin user
INSERT INTO admin_users (username, password, full_name, email) VALUES
('admin', 'admin123', 'System Administrator', 'admin@hotel.com'),
('manager', 'manager123', 'Hotel Manager', 'manager@hotel.com');

-- Insert sample rooms
INSERT INTO rooms (room_number, room_type, price_per_night, is_available, description) VALUES
('101', 'Standard Single', 80.00, TRUE, 'Comfortable single room with basic amenities, perfect for solo travelers'),
('102', 'Standard Double', 120.00, TRUE, 'Spacious double room for two guests with modern facilities'),
('103', 'Deluxe Suite', 200.00, TRUE, 'Luxury suite with premium amenities and city view'),
('104', 'Standard Single', 80.00, TRUE, 'Comfortable single room with basic amenities, perfect for solo travelers'),
('105', 'Standard Double', 120.00, TRUE, 'Spacious double room for two guests with modern facilities'),
('106', 'Deluxe Suite', 200.00, TRUE, 'Luxury suite with premium amenities and city view'),
('201', 'Executive Suite', 300.00, TRUE, 'Premium executive suite with business facilities'),
('202', 'Standard Single', 80.00, TRUE, 'Comfortable single room with basic amenities, perfect for solo travelers'),
('203', 'Standard Double', 120.00, TRUE, 'Spacious double room for two guests with modern facilities'),
('204', 'Deluxe Suite', 200.00, TRUE, 'Luxury suite with premium amenities and city view'),
('205', 'Presidential Suite', 500.00, TRUE, 'Ultimate luxury suite with panoramic views and butler service'),
('301', 'Standard Single', 80.00, TRUE, 'Comfortable single room with basic amenities, perfect for solo travelers'),
('302', 'Standard Double', 120.00, TRUE, 'Spacious double room for two guests with modern facilities'),
('303', 'Deluxe Suite', 200.00, TRUE, 'Luxury suite with premium amenities and city view'),
('304', 'Executive Suite', 300.00, TRUE, 'Premium executive suite with business facilities');

-- Insert sample customers
INSERT INTO customers (first_name, last_name, email, phone, address) VALUES
('John', 'Doe', 'john.doe@email.com', '+1-555-0101', '123 Main Street, New York, NY 10001'),
('Jane', 'Smith', 'jane.smith@email.com', '+1-555-0102', '456 Oak Avenue, Los Angeles, CA 90210'),
('Michael', 'Johnson', 'michael.johnson@email.com', '+1-555-0103', '789 Pine Road, Chicago, IL 60601'),
('Sarah', 'Williams', 'sarah.williams@email.com', '+1-555-0104', '321 Elm Street, Houston, TX 77001'),
('David', 'Brown', 'david.brown@email.com', '+1-555-0105', '654 Maple Drive, Phoenix, AZ 85001'),
('Emily', 'Davis', 'emily.davis@email.com', '+1-555-0106', '987 Cedar Lane, Philadelphia, PA 19101'),
('Robert', 'Miller', 'robert.miller@email.com', '+1-555-0107', '147 Birch Boulevard, San Antonio, TX 78201'),
('Lisa', 'Wilson', 'lisa.wilson@email.com', '+1-555-0108', '258 Spruce Court, San Diego, CA 92101'),
('James', 'Moore', 'james.moore@email.com', '+1-555-0109', '369 Willow Way, Dallas, TX 75201'),
('Jennifer', 'Taylor', 'jennifer.taylor@email.com', '+1-555-0110', '741 Ash Avenue, San Jose, CA 95101');

-- Insert sample bookings
INSERT INTO bookings (customer_id, room_id, check_in_date, check_out_date, total_amount, booking_status) VALUES
(1, 1, '2024-01-15', '2024-01-18', 240.00, 'CONFIRMED'),
(2, 2, '2024-01-20', '2024-01-25', 600.00, 'CHECKED_IN'),
(3, 3, '2024-01-22', '2024-01-24', 400.00, 'PENDING'),
(4, 4, '2024-01-25', '2024-01-30', 400.00, 'CONFIRMED'),
(5, 5, '2024-01-28', '2024-02-02', 600.00, 'CHECKED_OUT'),
(6, 6, '2024-02-01', '2024-02-03', 400.00, 'CANCELLED'),
(7, 7, '2024-02-05', '2024-02-10', 1500.00, 'CONFIRMED'),
(8, 8, '2024-02-08', '2024-02-12', 320.00, 'PENDING'),
(9, 9, '2024-02-10', '2024-02-15', 600.00, 'CONFIRMED'),
(10, 10, '2024-02-12', '2024-02-14', 400.00, 'CHECKED_IN');

-- Create views for common queries

-- View for available rooms
CREATE VIEW available_rooms AS
SELECT 
    room_id,
    room_number,
    room_type,
    price_per_night,
    description
FROM rooms 
WHERE is_available = TRUE
ORDER BY room_number;

-- View for booking details with customer and room information
CREATE VIEW booking_details AS
SELECT 
    b.booking_id,
    CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
    c.email AS customer_email,
    c.phone AS customer_phone,
    r.room_number,
    r.room_type,
    b.check_in_date,
    b.check_out_date,
    DATEDIFF(b.check_out_date, b.check_in_date) AS nights,
    r.price_per_night,
    b.total_amount,
    b.booking_status,
    b.created_at
FROM bookings b
JOIN customers c ON b.customer_id = c.customer_id
JOIN rooms r ON b.room_id = r.room_id
ORDER BY b.created_at DESC;

-- View for room occupancy
CREATE VIEW room_occupancy AS
SELECT 
    r.room_id,
    r.room_number,
    r.room_type,
    r.price_per_night,
    r.is_available,
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM bookings b 
            WHERE b.room_id = r.room_id 
            AND b.booking_status IN ('CONFIRMED', 'CHECKED_IN')
            AND CURDATE() BETWEEN b.check_in_date AND b.check_out_date
        ) THEN 'OCCUPIED'
        ELSE 'AVAILABLE'
    END AS current_status
FROM rooms r
ORDER BY r.room_number;

-- Create stored procedures

-- Procedure to check room availability for a date range
DELIMITER //
CREATE PROCEDURE CheckRoomAvailability(
    IN p_room_id INT,
    IN p_check_in_date DATE,
    IN p_check_out_date DATE,
    OUT p_is_available BOOLEAN
)
BEGIN
    DECLARE booking_count INT DEFAULT 0;
    
    SELECT COUNT(*) INTO booking_count
    FROM bookings 
    WHERE room_id = p_room_id 
    AND booking_status IN ('CONFIRMED', 'CHECKED_IN')
    AND (
        (check_in_date <= p_check_out_date AND check_out_date > p_check_in_date) OR
        (check_in_date < p_check_out_date AND check_out_date >= p_check_in_date) OR
        (check_in_date >= p_check_in_date AND check_out_date <= p_check_out_date)
    );
    
    SET p_is_available = (booking_count = 0);
END //
DELIMITER ;

-- Procedure to calculate booking total
DELIMITER //
CREATE PROCEDURE CalculateBookingTotal(
    IN p_room_id INT,
    IN p_check_in_date DATE,
    IN p_check_out_date DATE,
    OUT p_total_amount DECIMAL(10,2)
)
BEGIN
    DECLARE nights INT;
    DECLARE price_per_night DECIMAL(10,2);
    
    SET nights = DATEDIFF(p_check_out_date, p_check_in_date);
    
    SELECT price_per_night INTO price_per_night
    FROM rooms 
    WHERE room_id = p_room_id;
    
    SET p_total_amount = nights * price_per_night;
END //
DELIMITER ;

-- Procedure to update room availability
DELIMITER //
CREATE PROCEDURE UpdateRoomAvailability(
    IN p_room_id INT,
    IN p_is_available BOOLEAN
)
BEGIN
    UPDATE rooms 
    SET is_available = p_is_available 
    WHERE room_id = p_room_id;
END //
DELIMITER ;

-- Create triggers

-- Trigger to update room availability when booking status changes
DELIMITER //
CREATE TRIGGER update_room_availability_on_booking_status
AFTER UPDATE ON bookings
FOR EACH ROW
BEGIN
    -- If booking is cancelled or checked out, make room available
    IF NEW.booking_status IN ('CANCELLED', 'CHECKED_OUT') THEN
        UPDATE rooms SET is_available = TRUE WHERE room_id = NEW.room_id;
    -- If booking is confirmed or checked in, make room unavailable
    ELSEIF NEW.booking_status IN ('CONFIRMED', 'CHECKED_IN') THEN
        UPDATE rooms SET is_available = FALSE WHERE room_id = NEW.room_id;
    END IF;
END //
DELIMITER ;

-- Trigger to update room availability when new booking is created
DELIMITER //
CREATE TRIGGER update_room_availability_on_new_booking
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
    -- If booking is confirmed or checked in, make room unavailable
    IF NEW.booking_status IN ('CONFIRMED', 'CHECKED_IN') THEN
        UPDATE rooms SET is_available = FALSE WHERE room_id = NEW.room_id;
    END IF;
END //
DELIMITER ;

-- Create indexes for better performance
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_phone ON customers(phone);
CREATE INDEX idx_rooms_room_number ON rooms(room_number);
CREATE INDEX idx_rooms_room_type ON rooms(room_type);
CREATE INDEX idx_rooms_availability ON rooms(is_available);
CREATE INDEX idx_bookings_dates ON bookings(check_in_date, check_out_date);
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_room ON bookings(room_id);
CREATE INDEX idx_bookings_status ON bookings(booking_status);
CREATE INDEX idx_admin_username ON admin_users(username);

-- Insert additional sample data for testing

-- More rooms
INSERT INTO rooms (room_number, room_type, price_per_night, is_available, description) VALUES
('401', 'Standard Single', 80.00, TRUE, 'Comfortable single room with basic amenities'),
('402', 'Standard Double', 120.00, TRUE, 'Spacious double room for two guests'),
('403', 'Deluxe Suite', 200.00, TRUE, 'Luxury suite with premium amenities'),
('404', 'Executive Suite', 300.00, TRUE, 'Premium executive suite with business facilities'),
('405', 'Presidential Suite', 500.00, TRUE, 'Ultimate luxury suite with panoramic views');

-- More customers
INSERT INTO customers (first_name, last_name, email, phone, address) VALUES
('Christopher', 'Anderson', 'christopher.anderson@email.com', '+1-555-0111', '852 Oak Street, Austin, TX 78701'),
('Amanda', 'Thomas', 'amanda.thomas@email.com', '+1-555-0112', '963 Pine Avenue, Jacksonville, FL 32201'),
('Matthew', 'Jackson', 'matthew.jackson@email.com', '+1-555-0113', '741 Elm Drive, Fort Worth, TX 76101'),
('Jessica', 'White', 'jessica.white@email.com', '+1-555-0114', '852 Maple Lane, Columbus, OH 43201'),
('Andrew', 'Harris', 'andrew.harris@email.com', '+1-555-0115', '963 Cedar Road, Charlotte, NC 28201');

-- Display database information
SELECT 'Hotel Management System Database Created Successfully!' AS Status;
SELECT COUNT(*) AS 'Total Rooms' FROM rooms;
SELECT COUNT(*) AS 'Total Customers' FROM customers;
SELECT COUNT(*) AS 'Total Bookings' FROM bookings;
SELECT COUNT(*) AS 'Total Admin Users' FROM admin_users;

-- Show sample data
SELECT 'Sample Rooms:' AS Info;
SELECT room_number, room_type, CONCAT('$', price_per_night) AS price, 
       CASE WHEN is_available THEN 'Available' ELSE 'Occupied' END AS status
FROM rooms 
ORDER BY room_number 
LIMIT 10;

SELECT 'Sample Customers:' AS Info;
SELECT CONCAT(first_name, ' ', last_name) AS customer_name, email, phone
FROM customers 
ORDER BY customer_name 
LIMIT 10;

SELECT 'Sample Bookings:' AS Info;
SELECT bd.booking_id, bd.customer_name, bd.room_number, 
       bd.check_in_date, bd.check_out_date, 
       CONCAT('$', bd.total_amount) AS total, bd.booking_status
FROM booking_details bd
ORDER BY bd.created_at DESC
LIMIT 10;
