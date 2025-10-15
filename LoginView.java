package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Login view for the Hotel Management System
 * Provides user interface for admin authentication
 */
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private JLabel statusLabel;
    
    public LoginView() {
        initializeComponents();
        setupLayout();
        setupFrame();
    }
    
    private void initializeComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");
        statusLabel = new JLabel("Enter your credentials to access the system");
        
        // Set component properties
        loginButton.setPreferredSize(new Dimension(100, 30));
        exitButton.setPreferredSize(new Dimension(100, 30));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.BLUE);
        
        // Set fonts
        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);
        
        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        loginButton.setFont(labelFont);
        exitButton.setFont(labelFont);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("Hotel Management System - Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Username field
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);
        
        // Password field
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        // Status label
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(statusLabel, gbc);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupFrame() {
        setTitle("Hotel Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Set icon (if available)
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }
    
    // Getters
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    // Setters
    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }
    
    public void setStatusColor(Color color) {
        statusLabel.setForeground(color);
    }
    
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
    
    // Action listeners
    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
    
    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
    
    public void addEnterKeyListener(ActionListener listener) {
        usernameField.addActionListener(listener);
        passwordField.addActionListener(listener);
    }
    
    // Utility methods
    public void showErrorMessage(String message) {
        setStatusMessage(message);
        setStatusColor(Color.RED);
        clearFields();
        usernameField.requestFocus();
    }
    
    public void showSuccessMessage(String message) {
        setStatusMessage(message);
        setStatusColor(Color.GREEN);
    }
    
    public void showInfoMessage(String message) {
        setStatusMessage(message);
        setStatusColor(Color.BLUE);
    }
    
    public void setLoadingState(boolean loading) {
        loginButton.setEnabled(!loading);
        if (loading) {
            loginButton.setText("Logging in...");
            setStatusMessage("Authenticating...");
        } else {
            loginButton.setText("Login");
            setStatusMessage("Enter your credentials to access the system");
        }
    }
    
    // Validation methods
    public boolean validateInput() {
        if (getUsername().isEmpty()) {
            showErrorMessage("Username cannot be empty!");
            usernameField.requestFocus();
            return false;
        }
        
        if (getPassword().isEmpty()) {
            showErrorMessage("Password cannot be empty!");
            passwordField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public void dispose() {
        super.dispose();
    }
}
