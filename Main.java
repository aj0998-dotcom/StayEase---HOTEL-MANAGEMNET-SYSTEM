import controller.LoginController;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main application class for the Hotel Management System
 * Entry point of the application
 */
public class Main {
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Set system properties for better UI
        System.setProperty("swing.aatext", "true");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        
        // Initialize and start the application
        try {
            System.out.println("Starting Hotel Management System...");
            LoginController loginController = new LoginController();
            System.out.println("Application initialized successfully!");
        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
            
            // Show error dialog
            javax.swing.JOptionPane.showMessageDialog(
                null, 
                "Failed to start the Hotel Management System:\n" + e.getMessage(), 
                "Startup Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
