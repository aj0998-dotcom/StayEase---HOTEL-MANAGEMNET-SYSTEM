package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Customer Registration view for adding and editing customer information
 * Provides user interface for customer management
 */
public class CustomerRegistrationView extends JDialog {
    private JTextField customerIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    
    private JButton saveButton;
    private JButton cancelButton;
    private JButton clearButton;
    
    private boolean isEditMode = false;
    
    public CustomerRegistrationView(JFrame parent, String title) {
        super(parent, title, true);
        initializeComponents();
        setupLayout();
        setupDialog();
    }
    
    private void initializeComponents() {
        // Initialize fields
        customerIdField = new JTextField(10);
        customerIdField.setEditable(false);
        customerIdField.setBackground(Color.LIGHT_GRAY);
        
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        
        addressArea = new JTextArea(4, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Initialize buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        clearButton = new JButton("Clear");
        
        saveButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setPreferredSize(new Dimension(100, 30));
        clearButton.setPreferredSize(new Dimension(100, 30));
        
        // Set fonts
        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);
        
        firstNameField.setFont(fieldFont);
        lastNameField.setFont(fieldFont);
        emailField.setFont(fieldFont);
        phoneField.setFont(fieldFont);
        addressArea.setFont(fieldFont);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 73, 94));
        JLabel titleLabel = new JLabel("Customer Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(customerIdField, gbc);
        
        // First Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("First Name:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Last Name:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Phone:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JScrollPane(addressArea), gbc);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        JLabel infoLabel = new JLabel("* Required fields");
        infoLabel.setForeground(Color.RED);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoPanel.add(infoLabel);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(infoPanel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(450, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    // Getters
    public String getCustomerId() { return customerIdField.getText().trim(); }
    public String getFirstName() { return firstNameField.getText().trim(); }
    public String getLastName() { return lastNameField.getText().trim(); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPhone() { return phoneField.getText().trim(); }
    public String getAddress() { return addressArea.getText().trim(); }
    
    // Setters
    public void setCustomerId(String id) { customerIdField.setText(id); }
    public void setFirstName(String firstName) { firstNameField.setText(firstName); }
    public void setLastName(String lastName) { lastNameField.setText(lastName); }
    public void setEmail(String email) { emailField.setText(email); }
    public void setPhone(String phone) { phoneField.setText(phone); }
    public void setAddress(String address) { addressArea.setText(address); }
    
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
        if (editMode) {
            setTitle("Edit Customer");
            saveButton.setText("Update");
        } else {
            setTitle("New Customer");
            saveButton.setText("Save");
            clearFields();
        }
    }
    
    // Action listeners
    public void addSaveButtonListener(ActionListener listener) { saveButton.addActionListener(listener); }
    public void addCancelButtonListener(ActionListener listener) { cancelButton.addActionListener(listener); }
    public void addClearButtonListener(ActionListener listener) { clearButton.addActionListener(listener); }
    
    // Validation methods
    public boolean validateInput() {
        if (getFirstName().isEmpty()) {
            showErrorMessage("First name is required!");
            firstNameField.requestFocus();
            return false;
        }
        
        if (getLastName().isEmpty()) {
            showErrorMessage("Last name is required!");
            lastNameField.requestFocus();
            return false;
        }
        
        if (getEmail().isEmpty()) {
            showErrorMessage("Email is required!");
            emailField.requestFocus();
            return false;
        }
        
        if (!isValidEmail(getEmail())) {
            showErrorMessage("Please enter a valid email address!");
            emailField.requestFocus();
            return false;
        }
        
        if (getPhone().isEmpty()) {
            showErrorMessage("Phone number is required!");
            phoneField.requestFocus();
            return false;
        }
        
        if (!isValidPhone(getPhone())) {
            showErrorMessage("Please enter a valid phone number!");
            phoneField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    private boolean isValidPhone(String phone) {
        // Accept various phone number formats
        String phoneRegex = "^[\\+]?[0-9\\s\\-\\(\\)]{10,}$";
        return phone.matches(phoneRegex);
    }
    
    // Utility methods
    public void clearFields() {
        if (!isEditMode) {
            setCustomerId("");
        }
        setFirstName("");
        setLastName("");
        setEmail("");
        setPhone("");
        setAddress("");
        firstNameField.requestFocus();
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void setLoadingState(boolean loading) {
        saveButton.setEnabled(!loading);
        if (loading) {
            saveButton.setText(isEditMode ? "Updating..." : "Saving...");
        } else {
            saveButton.setText(isEditMode ? "Update" : "Save");
        }
    }
    
    public void setFieldsEditable(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
        addressArea.setEditable(editable);
    }
    
    public void populateFields(String id, String firstName, String lastName, String email, String phone, String address) {
        setCustomerId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setAddress(address);
    }
    
    public void display() {
        setVisible(true);
    }
    
    public void dispose() {
        super.dispose();
    }
}
