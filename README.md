# Hotel Management System

A comprehensive Java-based hotel management system built with Swing GUI and MySQL database.

## Features

### 🏨 Room Management
- Add, edit, and delete rooms
- Manage room types and pricing
- Track room availability
- Search and filter rooms

### 👥 Customer Management
- Customer registration and profiles
- Customer information management
- Search customers by name or email
- Customer booking history

### 📅 Booking Management
- Create new bookings
- Edit existing bookings
- Confirm, check-in, and check-out guests
- Cancel bookings
- Booking status tracking

### 💰 Billing System
- Generate booking invoices
- Print bills
- Email bills (placeholder)
- Tax calculation

### 🔐 Admin Authentication
- Secure login system
- Admin user management
- Session management

## System Requirements

### Software Requirements
- **Java**: JDK 8 or higher
- **Database**: MySQL 5.7 or higher
- **IDE**: Any Java IDE (Eclipse, IntelliJ IDEA, NetBeans)

### Hardware Requirements
- **RAM**: Minimum 4GB
- **Storage**: 100MB free space
- **Display**: 1024x768 resolution or higher

## Installation & Setup

### 1. Database Setup

1. **Install MySQL** on your system
2. **Create a database user** with appropriate privileges:
   ```sql
   CREATE USER 'hotel_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON hotel_db.* TO 'hotel_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Import the database schema**:
   ```bash
   mysql -u hotel_user -p < database/hotel_db.sql
   ```

### 2. Database Configuration

Update the database connection settings in `src/model/DBConnection.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_db";
private static final String DB_USER = "hotel_user";
private static final String DB_PASSWORD = "your_password";
```

### 3. Dependencies

Add the MySQL JDBC driver to your project:

#### Maven (pom.xml)
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

#### Gradle (build.gradle)
```gradle
implementation 'mysql:mysql-connector-java:8.0.33'
```

#### Manual Installation
Download `mysql-connector-java-8.0.33.jar` and add it to your project's classpath.

### 4. Compilation & Execution

#### Using Command Line
```bash
# Compile all Java files
javac -cp ".:mysql-connector-java-8.0.33.jar" src/*.java src/*/*.java

# Run the application
java -cp ".:src:mysql-connector-java-8.0.33.jar" Main
```

#### Using IDE
1. Import the project into your IDE
2. Add the MySQL JDBC driver to the project's classpath
3. Run the `Main.java` file

## Default Login Credentials

- **Username**: `admin`
- **Password**: `admin123`

## Project Structure

```
HotelBookingSystem/
├── src/
│   ├── model/              # Data models and database connection
│   │   ├── Customer.java
│   │   ├── Room.java
│   │   ├── Booking.java
│   │   └── DBConnection.java
│   ├── view/               # GUI components
│   │   ├── LoginView.java
│   │   ├── AdminDashboard.java
│   │   ├── BookingView.java
│   │   ├── CustomerRegistrationView.java
│   │   └── BillView.java
│   ├── controller/         # Business logic controllers
│   │   ├── LoginController.java
│   │   ├── RoomController.java
│   │   ├── CustomerController.java
│   │   └── BookingController.java
│   └── Main.java          # Application entry point
├── database/
│   └── hotel_db.sql       # Database schema and sample data
└── README.md              # This file
```

## Usage Guide

### 1. Login
- Launch the application
- Enter admin credentials
- Click "Login" to access the dashboard

### 2. Dashboard
The main dashboard provides:
- **Statistics**: Overview of rooms, bookings, and customers
- **Quick Actions**: Access to all management features

### 3. Room Management
- **Add Room**: Click "Add Room" to create new rooms
- **Edit Room**: Select a room and click "Edit Room"
- **Delete Room**: Select a room and click "Delete Room"
- **Search**: Use the search field to find specific rooms

### 4. Customer Management
- **Add Customer**: Click "Add Customer" to register new customers
- **Edit Customer**: Select a customer and click "Edit Customer"
- **Delete Customer**: Select a customer and click "Delete Customer"
- **Search**: Use the search field to find customers

### 5. Booking Management
- **New Booking**: Click "New Booking" to create reservations
- **Edit Booking**: Select a booking and click "Edit Booking"
- **Booking Actions**: Use status buttons to manage bookings:
  - **Confirm**: Confirm a pending booking
  - **Check In**: Mark guest as checked in
  - **Check Out**: Mark guest as checked out
  - **Cancel**: Cancel a booking

### 6. Billing
- Generate invoices for bookings
- Print or email bills to customers
- Automatic tax calculation (10%)

## Database Schema

### Tables
- **admin_users**: System administrators
- **rooms**: Hotel room information
- **customers**: Customer profiles
- **bookings**: Reservation details

### Views
- **available_rooms**: Currently available rooms
- **booking_details**: Complete booking information
- **room_occupancy**: Current room occupancy status

### Stored Procedures
- **CheckRoomAvailability**: Verify room availability for dates
- **CalculateBookingTotal**: Calculate booking costs
- **UpdateRoomAvailability**: Update room status

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials
   - Ensure MySQL JDBC driver is in classpath

2. **Login Failed**
   - Use default credentials: admin/admin123
   - Check database connection
   - Verify admin_users table has data

3. **Room Availability Issues**
   - Check booking status in database
   - Verify triggers are working correctly
   - Manually update room availability if needed

### Error Logs
Check console output for detailed error messages. The application logs important operations and errors.

## Development

### Adding New Features
1. Follow the MVC pattern:
   - **Model**: Data access and business logic
   - **View**: GUI components
   - **Controller**: Event handling and coordination

2. Update database schema if needed
3. Add appropriate validation and error handling

### Code Style
- Use meaningful variable and method names
- Add comments for complex logic
- Follow Java naming conventions
- Handle exceptions appropriately

## License

This project is created for educational purposes. Feel free to use and modify as needed.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review error logs
3. Verify database setup
4. Check system requirements

## Future Enhancements

Potential improvements:
- Email integration for notifications
- Payment processing integration
- Reporting and analytics
- Multi-language support
- Mobile application
- Web-based interface
- Advanced search and filtering
- Backup and restore functionality
