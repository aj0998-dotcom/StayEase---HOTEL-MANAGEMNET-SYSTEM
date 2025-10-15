package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Admin Dashboard for the Hotel Management System
 * Main interface for hotel administrators
 */
public class AdminDashboard extends JFrame {
    private JTabbedPane tabbedPane;
    
    // Dashboard components
    private JLabel totalRoomsLabel;
    private JLabel availableRoomsLabel;
    private JLabel totalBookingsLabel;
    private JLabel totalCustomersLabel;
    private JButton refreshStatsButton;
    
    // Rooms management components
    private JTable roomsTable;
    private DefaultTableModel roomsTableModel;
    private JButton addRoomButton;
    private JButton editRoomButton;
    private JButton deleteRoomButton;
    private JButton refreshRoomsButton;
    private JTextField searchRoomsField;
    private JButton searchRoomsButton;
    
    // Customers management components
    private JTable customersTable;
    private DefaultTableModel customersTableModel;
    private JButton addCustomerButton;
    private JButton editCustomerButton;
    private JButton deleteCustomerButton;
    private JButton refreshCustomersButton;
    private JTextField searchCustomersField;
    private JButton searchCustomersButton;
    
    // Bookings management components
    private JTable bookingsTable;
    private DefaultTableModel bookingsTableModel;
    private JButton addBookingButton;
    private JButton editBookingButton;
    private JButton deleteBookingButton;
    private JButton refreshBookingsButton;
    private JButton confirmBookingButton;
    private JButton checkInButton;
    private JButton checkOutButton;
    private JButton cancelBookingButton;
    
    // Menu components
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenu helpMenu;
    private JMenuItem logoutMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem aboutMenuItem;
    
    public AdminDashboard() {
        initializeComponents();
        setupLayout();
        setupFrame();
    }
    
    private void initializeComponents() {
        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Initialize dashboard components
        totalRoomsLabel = new JLabel("0");
        availableRoomsLabel = new JLabel("0");
        totalBookingsLabel = new JLabel("0");
        totalCustomersLabel = new JLabel("0");
        refreshStatsButton = new JButton("Refresh Statistics");
        
        // Initialize rooms components
        initializeRoomsComponents();
        
        // Initialize customers components
        initializeCustomersComponents();
        
        // Initialize bookings components
        initializeBookingsComponents();
        
        // Initialize menu
        initializeMenu();
    }
    
    private void initializeRoomsComponents() {
        String[] roomsColumns = {"Room ID", "Room Number", "Type", "Price/Night", "Available", "Description"};
        roomsTableModel = new DefaultTableModel(roomsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomsTable = new JTable(roomsTableModel);
        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomsTable.setRowHeight(25);
        
        addRoomButton = new JButton("Add Room");
        editRoomButton = new JButton("Edit Room");
        deleteRoomButton = new JButton("Delete Room");
        refreshRoomsButton = new JButton("Refresh");
        searchRoomsField = new JTextField(15);
        searchRoomsButton = new JButton("Search");
        
        // Set button properties
        addRoomButton.setPreferredSize(new Dimension(100, 30));
        editRoomButton.setPreferredSize(new Dimension(100, 30));
        deleteRoomButton.setPreferredSize(new Dimension(100, 30));
        refreshRoomsButton.setPreferredSize(new Dimension(100, 30));
        searchRoomsButton.setPreferredSize(new Dimension(80, 30));
    }
    
    private void initializeCustomersComponents() {
        String[] customersColumns = {"Customer ID", "First Name", "Last Name", "Email", "Phone", "Address", "Created"};
        customersTableModel = new DefaultTableModel(customersColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customersTable = new JTable(customersTableModel);
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customersTable.setRowHeight(25);
        
        addCustomerButton = new JButton("Add Customer");
        editCustomerButton = new JButton("Edit Customer");
        deleteCustomerButton = new JButton("Delete Customer");
        refreshCustomersButton = new JButton("Refresh");
        searchCustomersField = new JTextField(15);
        searchCustomersButton = new JButton("Search");
        
        // Set button properties
        addCustomerButton.setPreferredSize(new Dimension(120, 30));
        editCustomerButton.setPreferredSize(new Dimension(120, 30));
        deleteCustomerButton.setPreferredSize(new Dimension(120, 30));
        refreshCustomersButton.setPreferredSize(new Dimension(100, 30));
        searchCustomersButton.setPreferredSize(new Dimension(80, 30));
    }
    
    private void initializeBookingsComponents() {
        String[] bookingsColumns = {"Booking ID", "Customer", "Room", "Check-in", "Check-out", "Amount", "Status", "Created"};
        bookingsTableModel = new DefaultTableModel(bookingsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingsTable = new JTable(bookingsTableModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.setRowHeight(25);
        
        addBookingButton = new JButton("New Booking");
        editBookingButton = new JButton("Edit Booking");
        deleteBookingButton = new JButton("Delete Booking");
        refreshBookingsButton = new JButton("Refresh");
        confirmBookingButton = new JButton("Confirm");
        checkInButton = new JButton("Check In");
        checkOutButton = new JButton("Check Out");
        cancelBookingButton = new JButton("Cancel");
        
        // Set button properties
        addBookingButton.setPreferredSize(new Dimension(120, 30));
        editBookingButton.setPreferredSize(new Dimension(120, 30));
        deleteBookingButton.setPreferredSize(new Dimension(120, 30));
        refreshBookingsButton.setPreferredSize(new Dimension(100, 30));
        confirmBookingButton.setPreferredSize(new Dimension(100, 30));
        checkInButton.setPreferredSize(new Dimension(100, 30));
        checkOutButton.setPreferredSize(new Dimension(100, 30));
        cancelBookingButton.setPreferredSize(new Dimension(100, 30));
    }
    
    private void initializeMenu() {
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");
        
        logoutMenuItem = new JMenuItem("Logout");
        exitMenuItem = new JMenuItem("Exit");
        aboutMenuItem = new JMenuItem("About");
        
        fileMenu.add(logoutMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        helpMenu.add(aboutMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create tabs
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Rooms", createRoomsPanel());
        tabbedPane.addTab("Customers", createCustomersPanel());
        tabbedPane.addTab("Bookings", createBookingsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        setJMenuBar(menuBar);
    }
    
    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);
        
        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(52, 73, 94));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create stat cards
        statsPanel.add(createStatCard("Total Rooms", totalRoomsLabel, new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Available Rooms", availableRoomsLabel, new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Bookings", totalBookingsLabel, new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Total Customers", totalCustomersLabel, new Color(230, 126, 34)));
        
        // Refresh button panel
        JPanel refreshPanel = new JPanel(new FlowLayout());
        refreshPanel.setBackground(Color.WHITE);
        refreshPanel.add(refreshStatsButton);
        
        dashboardPanel.add(statsPanel, BorderLayout.CENTER);
        dashboardPanel.add(refreshPanel, BorderLayout.SOUTH);
        
        return dashboardPanel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRoomsPanel() {
        JPanel roomsPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchRoomsField);
        searchPanel.add(searchRoomsButton);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(refreshRoomsButton);
        
        // Table panel
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addRoomButton);
        buttonPanel.add(editRoomButton);
        buttonPanel.add(deleteRoomButton);
        
        roomsPanel.add(searchPanel, BorderLayout.NORTH);
        roomsPanel.add(scrollPane, BorderLayout.CENTER);
        roomsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return roomsPanel;
    }
    
    private JPanel createCustomersPanel() {
        JPanel customersPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchCustomersField);
        searchPanel.add(searchCustomersButton);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(refreshCustomersButton);
        
        // Table panel
        JScrollPane scrollPane = new JScrollPane(customersTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(editCustomerButton);
        buttonPanel.add(deleteCustomerButton);
        
        customersPanel.add(searchPanel, BorderLayout.NORTH);
        customersPanel.add(scrollPane, BorderLayout.CENTER);
        customersPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return customersPanel;
    }
    
    private JPanel createBookingsPanel() {
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        
        // Table panel
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addBookingButton);
        buttonPanel.add(editBookingButton);
        buttonPanel.add(deleteBookingButton);
        buttonPanel.add(refreshBookingsButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(confirmBookingButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(cancelBookingButton);
        
        bookingsPanel.add(scrollPane, BorderLayout.CENTER);
        bookingsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return bookingsPanel;
    }
    
    private void setupFrame() {
        setTitle("Hotel Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    // Getters for table models
    public DefaultTableModel getRoomsTableModel() { return roomsTableModel; }
    public DefaultTableModel getCustomersTableModel() { return customersTableModel; }
    public DefaultTableModel getBookingsTableModel() { return bookingsTableModel; }
    
    // Getters for tables
    public JTable getRoomsTable() { return roomsTable; }
    public JTable getCustomersTable() { return customersTable; }
    public JTable getBookingsTable() { return bookingsTable; }
    
    // Getters for search fields
    public String getRoomsSearchText() { return searchRoomsField.getText().trim(); }
    public String getCustomersSearchText() { return searchCustomersField.getText().trim(); }
    
    // Statistics getters
    public JLabel getTotalRoomsLabel() { return totalRoomsLabel; }
    public JLabel getAvailableRoomsLabel() { return availableRoomsLabel; }
    public JLabel getTotalBookingsLabel() { return totalBookingsLabel; }
    public JLabel getTotalCustomersLabel() { return totalCustomersLabel; }
    
    // Action listeners for rooms
    public void addRoomsAddButtonListener(ActionListener listener) { addRoomButton.addActionListener(listener); }
    public void addRoomsEditButtonListener(ActionListener listener) { editRoomButton.addActionListener(listener); }
    public void addRoomsDeleteButtonListener(ActionListener listener) { deleteRoomButton.addActionListener(listener); }
    public void addRoomsRefreshButtonListener(ActionListener listener) { refreshRoomsButton.addActionListener(listener); }
    public void addRoomsSearchButtonListener(ActionListener listener) { searchRoomsButton.addActionListener(listener); }
    
    // Action listeners for customers
    public void addCustomersAddButtonListener(ActionListener listener) { addCustomerButton.addActionListener(listener); }
    public void addCustomersEditButtonListener(ActionListener listener) { editCustomerButton.addActionListener(listener); }
    public void addCustomersDeleteButtonListener(ActionListener listener) { deleteCustomerButton.addActionListener(listener); }
    public void addCustomersRefreshButtonListener(ActionListener listener) { refreshCustomersButton.addActionListener(listener); }
    public void addCustomersSearchButtonListener(ActionListener listener) { searchCustomersButton.addActionListener(listener); }
    
    // Action listeners for bookings
    public void addBookingsAddButtonListener(ActionListener listener) { addBookingButton.addActionListener(listener); }
    public void addBookingsEditButtonListener(ActionListener listener) { editBookingButton.addActionListener(listener); }
    public void addBookingsDeleteButtonListener(ActionListener listener) { deleteBookingButton.addActionListener(listener); }
    public void addBookingsRefreshButtonListener(ActionListener listener) { refreshBookingsButton.addActionListener(listener); }
    public void addBookingsConfirmButtonListener(ActionListener listener) { confirmBookingButton.addActionListener(listener); }
    public void addBookingsCheckInButtonListener(ActionListener listener) { checkInButton.addActionListener(listener); }
    public void addBookingsCheckOutButtonListener(ActionListener listener) { checkOutButton.addActionListener(listener); }
    public void addBookingsCancelButtonListener(ActionListener listener) { cancelBookingButton.addActionListener(listener); }
    
    // Action listeners for menu
    public void addLogoutMenuItemListener(ActionListener listener) { logoutMenuItem.addActionListener(listener); }
    public void addExitMenuItemListener(ActionListener listener) { exitMenuItem.addActionListener(listener); }
    public void addAboutMenuItemListener(ActionListener listener) { aboutMenuItem.addActionListener(listener); }
    
    // Action listeners for dashboard
    public void addRefreshStatsButtonListener(ActionListener listener) { refreshStatsButton.addActionListener(listener); }
    
    // Utility methods
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    public boolean confirmAction(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirm Action", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    public int getSelectedRoomsRow() { return roomsTable.getSelectedRow(); }
    public int getSelectedCustomersRow() { return customersTable.getSelectedRow(); }
    public int getSelectedBookingsRow() { return bookingsTable.getSelectedRow(); }
    
    public void clearRoomsSelection() { roomsTable.clearSelection(); }
    public void clearCustomersSelection() { customersTable.clearSelection(); }
    public void clearBookingsSelection() { bookingsTable.clearSelection(); }
    
    public void setRoomsSearchText(String text) { searchRoomsField.setText(text); }
    public void setCustomersSearchText(String text) { searchCustomersField.setText(text); }
}
