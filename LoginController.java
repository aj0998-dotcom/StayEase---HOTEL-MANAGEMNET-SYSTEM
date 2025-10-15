package controller;

import model.DBConnection;
import view.LoginView;
import view.AdminDashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Login Controller for the Hotel Management System
 * Handles authentication logic and navigation
 */
public class LoginController {
    private LoginView loginView;
    private AdminDashboard adminDashboard;
    private boolean isAuthenticated = false;
    
    public LoginController() {
        initializeDatabase();
        initializeViews();
        setupEventHandlers();
        showLoginView();
    }
    
    private void initializeDatabase() {
        try {
            DBConnection.initializeDatabase();
            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error initializing database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeViews() {
        loginView = new LoginView();
        adminDashboard = new AdminDashboard();
    }
    
    private void setupEventHandlers() {
        // Login button event
        loginView.addLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Exit button event
        loginView.addExitButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleExit();
            }
        });
        
        // Enter key events
        loginView.addEnterKeyListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Admin dashboard logout event
        adminDashboard.addLogoutMenuItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
        
        // Admin dashboard exit event
        adminDashboard.addExitMenuItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleExit();
            }
        });
        
        // Admin dashboard about event
        adminDashboard.addAboutMenuItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });
    }
    
    private void handleLogin() {
        if (!loginView.validateInput()) {
            return;
        }
        
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        
        loginView.setLoadingState(true);
        
        // Perform authentication in a separate thread to prevent UI freezing
        SwingUtilities.invokeLater(() -> {
            try {
                if (authenticateUser(username, password)) {
                    isAuthenticated = true;
                    loginView.showSuccessMessage("Login successful! Welcome " + username);
                    
                    // Initialize other controllers
                    initializeOtherControllers();
                    
                    // Hide login view and show dashboard
                    SwingUtilities.invokeLater(() -> {
                        loginView.setVisible(false);
                        adminDashboard.setVisible(true);
                        adminDashboard.refreshStatsButton.doClick(); // Refresh statistics on login
                    });
                } else {
                    loginView.showErrorMessage("Invalid username or password!");
                }
            } catch (Exception e) {
                System.err.println("Login error: " + e.getMessage());
                loginView.showErrorMessage("Login failed: " + e.getMessage());
            } finally {
                loginView.setLoadingState(false);
            }
        });
    }
    
    private boolean authenticateUser(String username, String password) {
        String sql = "SELECT admin_id, username, full_name FROM admin_users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("User authenticated: " + rs.getString("full_name"));
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            throw new RuntimeException("Database error during authentication", e);
        }
        
        return false;
    }
    
    private void initializeOtherControllers() {
        // Initialize other controllers that need the authenticated session
        try {
            RoomController roomController = new RoomController(adminDashboard);
            CustomerController customerController = new CustomerController(adminDashboard);
            BookingController bookingController = new BookingController(adminDashboard);
            
            System.out.println("All controllers initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing controllers: " + e.getMessage());
            JOptionPane.showMessageDialog(adminDashboard, 
                "Error initializing system components: " + e.getMessage(), 
                "Initialization Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(adminDashboard, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            isAuthenticated = false;
            adminDashboard.setVisible(false);
            loginView.clearFields();
            loginView.setVisible(true);
            loginView.showInfoMessage("You have been logged out successfully!");
        }
    }
    
    private void handleExit() {
        int result = JOptionPane.showConfirmDialog(
            isAuthenticated ? adminDashboard : loginView, 
            "Are you sure you want to exit the application?", 
            "Confirm Exit", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                DBConnection.closeConnection();
                System.out.println("Database connection closed.");
            } catch (Exception e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
            
            System.exit(0);
        }
    }
    
    private void showAboutDialog() {
        String aboutMessage = """
            Hotel Management System v1.0
            
            Developed for managing hotel operations including:
            • Room management
            • Customer registration
            • Booking management
            • Billing and invoicing
            
            © 2024 Hotel Management System
            """;
        
        JOptionPane.showMessageDialog(adminDashboard, 
            aboutMessage, 
            "About Hotel Management System", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showLoginView() {
        loginView.setVisible(true);
    }
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    public LoginView getLoginView() {
        return loginView;
    }
    
    public AdminDashboard getAdminDashboard() {
        return adminDashboard;
    }
    
    // Method to add a new admin user (for setup purposes)
    public boolean addAdminUser(String username, String password, String fullName, String email) {
        String sql = "INSERT INTO admin_users (username, password, full_name, email) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding admin user: " + e.getMessage());
            return false;
        }
    }
    
    // Method to change admin password
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // First verify old password
        if (!authenticateUser(username, oldPassword)) {
            return false;
        }
        
        String sql = "UPDATE admin_users SET password = ? WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }
    
    // Method to get admin user information
    public String[] getAdminInfo(String username) {
        String sql = "SELECT username, full_name, email, created_at FROM admin_users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new String[]{
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at").toString()
                };
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting admin info: " + e.getMessage());
        }
        
        return null;
    }
}
