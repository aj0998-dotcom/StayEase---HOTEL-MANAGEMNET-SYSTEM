package controller;

import model.Customer;
import view.AdminDashboard;
import view.CustomerRegistrationView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Customer Controller for the Hotel Management System
 * Handles customer management operations
 */
public class CustomerController {
    private AdminDashboard adminDashboard;
    private CustomerRegistrationView customerView;
    private JTable customersTable;
    private DefaultTableModel customersTableModel;
    
    public CustomerController(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        this.customersTable = adminDashboard.getCustomersTable();
        this.customersTableModel = adminDashboard.getCustomersTableModel();
        
        setupEventHandlers();
        loadCustomers();
    }
    
    private void setupEventHandlers() {
        // Customer management buttons
        adminDashboard.addCustomersAddButtonListener(e -> showAddCustomerDialog());
        adminDashboard.addCustomersEditButtonListener(e -> showEditCustomerDialog());
        adminDashboard.addCustomersDeleteButtonListener(e -> deleteCustomer());
        adminDashboard.addCustomersRefreshButtonListener(e -> loadCustomers());
        adminDashboard.addCustomersSearchButtonListener(e -> searchCustomers());
    }
    
    private void showAddCustomerDialog() {
        customerView = new CustomerRegistrationView(adminDashboard, "Add New Customer");
        customerView.setEditMode(false);
        setupCustomerViewEventHandlers();
        customerView.display();
    }
    
    private void showEditCustomerDialog() {
        int selectedRow = adminDashboard.getSelectedCustomersRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a customer to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get customer data from selected row
        String customerIdStr = (String) customersTableModel.getValueAt(selectedRow, 0);
        int customerId = Integer.parseInt(customerIdStr);
        
        Customer customer = Customer.getCustomerById(customerId);
        if (customer != null) {
            customerView = new CustomerRegistrationView(adminDashboard, "Edit Customer");
            customerView.setEditMode(true);
            populateCustomerView(customer);
            setupCustomerViewEventHandlers();
            customerView.display();
        }
    }
    
    private void populateCustomerView(Customer customer) {
        customerView.populateFields(
            String.valueOf(customer.getCustomerId()),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress()
        );
    }
    
    private void setupCustomerViewEventHandlers() {
        customerView.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveCustomer();
            }
        });
        
        customerView.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerView.dispose();
            }
        });
        
        customerView.addClearButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerView.clearFields();
            }
        });
    }
    
    private void handleSaveCustomer() {
        if (!customerView.validateInput()) {
            return;
        }
        
        customerView.setLoadingState(true);
        
        try {
            Customer customer;
            boolean success = false;
            String message = "";
            
            if (customerView.getTitle().equals("Edit Customer")) {
                // Update existing customer
                customer = new Customer();
                customer.setCustomerId(Integer.parseInt(customerView.getCustomerId()));
                customer.setFirstName(customerView.getFirstName());
                customer.setLastName(customerView.getLastName());
                customer.setEmail(customerView.getEmail());
                customer.setPhone(customerView.getPhone());
                customer.setAddress(customerView.getAddress());
                
                success = customer.updateCustomer();
                message = success ? "Customer updated successfully!" : "Failed to update customer!";
            } else {
                // Create new customer
                customer = new Customer(
                    customerView.getFirstName(),
                    customerView.getLastName(),
                    customerView.getEmail(),
                    customerView.getPhone(),
                    customerView.getAddress()
                );
                
                success = customer.addCustomer();
                message = success ? "Customer added successfully!" : "Failed to add customer!";
            }
            
            if (success) {
                customerView.showSuccessMessage(message);
                customerView.dispose();
                loadCustomers();
            } else {
                customerView.showErrorMessage(message);
            }
            
        } catch (Exception e) {
            customerView.showErrorMessage("Error: " + e.getMessage());
        } finally {
            customerView.setLoadingState(false);
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = adminDashboard.getSelectedCustomersRow();
        if (selectedRow == -1) {
            adminDashboard.showMessage("Please select a customer to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String customerIdStr = (String) customersTableModel.getValueAt(selectedRow, 0);
        String customerName = (String) customersTableModel.getValueAt(selectedRow, 1) + " " + 
                             (String) customersTableModel.getValueAt(selectedRow, 2);
        
        int result = JOptionPane.showConfirmDialog(adminDashboard, 
            "Are you sure you want to delete customer " + customerName + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            int customerId = Integer.parseInt(customerIdStr);
            
            if (Customer.deleteCustomer(customerId)) {
                JOptionPane.showMessageDialog(adminDashboard, "Customer deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCustomers();
            } else {
                JOptionPane.showMessageDialog(adminDashboard, "Failed to delete customer!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadCustomers() {
        // Clear existing data
        customersTableModel.setRowCount(0);
        
        // Load all customers
        List<Customer> customers = Customer.getAllCustomers();
        for (Customer customer : customers) {
            Object[] rowData = {
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCreatedAt().toString().split(" ")[0] // Date only
            };
            customersTableModel.addRow(rowData);
        }
        
        System.out.println("Loaded " + customers.size() + " customers");
    }
    
    private void searchCustomers() {
        String searchTerm = adminDashboard.getCustomersSearchText();
        
        if (searchTerm.isEmpty()) {
            loadCustomers();
            return;
        }
        
        // Clear existing data
        customersTableModel.setRowCount(0);
        
        // Search customers
        List<Customer> customers = Customer.searchCustomers(searchTerm);
        for (Customer customer : customers) {
            Object[] rowData = {
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCreatedAt().toString().split(" ")[0] // Date only
            };
            customersTableModel.addRow(rowData);
        }
        
        System.out.println("Found " + customers.size() + " customers matching: " + searchTerm);
    }
    
    // Method to get all customers (for combo boxes)
    public List<Customer> getAllCustomers() {
        return Customer.getAllCustomers();
    }
    
    // Method to get customer by ID
    public Customer getCustomerById(int customerId) {
        return Customer.getCustomerById(customerId);
    }
    
    // Method to get customer by email
    public Customer getCustomerByEmail(String email) {
        return Customer.getCustomerByEmail(email);
    }
    
    // Method to create customer combo box data
    public String[] createCustomerComboBoxData() {
        List<Customer> customers = getAllCustomers();
        String[] customerData = new String[customers.size()];
        
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            customerData[i] = customer.getCustomerId() + " - " + customer.getFirstName() + " " + customer.getLastName();
        }
        
        return customerData;
    }
    
    // Method to get customer name by ID
    public String getCustomerNameById(int customerId) {
        Customer customer = getCustomerById(customerId);
        return customer != null ? customer.getFullName() : "Unknown Customer";
    }
    
    // Method to get customer email by ID
    public String getCustomerEmailById(int customerId) {
        Customer customer = getCustomerById(customerId);
        return customer != null ? customer.getEmail() : "";
    }
    
    // Method to get customer phone by ID
    public String getCustomerPhoneById(int customerId) {
        Customer customer = getCustomerById(customerId);
        return customer != null ? customer.getPhone() : "";
    }
    
    // Method to check if customer exists
    public boolean customerExists(int customerId) {
        return getCustomerById(customerId) != null;
    }
    
    // Method to get customer count
    public int getCustomerCount() {
        return getAllCustomers().size();
    }
    
    // Method to refresh customers data
    public void refreshCustomers() {
        loadCustomers();
    }
}
