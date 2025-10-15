package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

/**
 * Bill view for displaying and printing booking invoices
 * Provides user interface for billing management
 */
public class BillView extends JDialog {
    private JLabel hotelNameLabel;
    private JLabel hotelAddressLabel;
    private JLabel hotelContactLabel;
    
    private JLabel bookingIdLabel;
    private JLabel customerNameLabel;
    private JLabel customerEmailLabel;
    private JLabel customerPhoneLabel;
    
    private JLabel roomNumberLabel;
    private JLabel roomTypeLabel;
    private JLabel checkInDateLabel;
    private JLabel checkOutDateLabel;
    private JLabel nightsLabel;
    private JLabel pricePerNightLabel;
    private JLabel subtotalLabel;
    private JLabel taxLabel;
    private JLabel totalAmountLabel;
    
    private JLabel statusLabel;
    private JLabel issueDateLabel;
    
    private JButton printButton;
    private JButton emailButton;
    private JButton closeButton;
    private JButton refreshButton;
    
    private JPanel billPanel;
    private JScrollPane scrollPane;
    
    public BillView(JFrame parent, String title) {
        super(parent, title, true);
        initializeComponents();
        setupLayout();
        setupDialog();
    }
    
    private void initializeComponents() {
        // Hotel information labels
        hotelNameLabel = new JLabel("Grand Hotel Paradise");
        hotelNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        hotelNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        hotelAddressLabel = new JLabel("123 Paradise Street, Beach City, BC 12345");
        hotelAddressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        hotelAddressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        hotelContactLabel = new JLabel("Phone: (555) 123-4567 | Email: info@grandhotel.com");
        hotelContactLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        hotelContactLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Booking information labels
        bookingIdLabel = new JLabel("");
        customerNameLabel = new JLabel("");
        customerEmailLabel = new JLabel("");
        customerPhoneLabel = new JLabel("");
        
        // Room information labels
        roomNumberLabel = new JLabel("");
        roomTypeLabel = new JLabel("");
        checkInDateLabel = new JLabel("");
        checkOutDateLabel = new JLabel("");
        nightsLabel = new JLabel("");
        pricePerNightLabel = new JLabel("");
        subtotalLabel = new JLabel("");
        taxLabel = new JLabel("");
        totalAmountLabel = new JLabel("");
        
        // Status and date labels
        statusLabel = new JLabel("");
        issueDateLabel = new JLabel("");
        
        // Initialize buttons
        printButton = new JButton("Print Bill");
        emailButton = new JButton("Email Bill");
        closeButton = new JButton("Close");
        refreshButton = new JButton("Refresh");
        
        printButton.setPreferredSize(new Dimension(120, 30));
        emailButton.setPreferredSize(new Dimension(120, 30));
        closeButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.setPreferredSize(new Dimension(100, 30));
        
        // Set fonts for labels
        Font labelFont = new Font("Arial", Font.PLAIN, 12);
        Font valueFont = new Font("Arial", Font.BOLD, 12);
        
        bookingIdLabel.setFont(valueFont);
        customerNameLabel.setFont(valueFont);
        customerEmailLabel.setFont(valueFont);
        customerPhoneLabel.setFont(valueFont);
        roomNumberLabel.setFont(valueFont);
        roomTypeLabel.setFont(valueFont);
        checkInDateLabel.setFont(valueFont);
        checkOutDateLabel.setFont(valueFont);
        nightsLabel.setFont(valueFont);
        pricePerNightLabel.setFont(valueFont);
        subtotalLabel.setFont(valueFont);
        taxLabel.setFont(valueFont);
        totalAmountLabel.setFont(valueFont);
        statusLabel.setFont(valueFont);
        issueDateLabel.setFont(valueFont);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create bill panel
        billPanel = new JPanel(new GridBagLayout());
        billPanel.setBackground(Color.WHITE);
        billPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Hotel header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        billPanel.add(hotelNameLabel, gbc);
        gbc.gridy = 1;
        billPanel.add(hotelAddressLabel, gbc);
        gbc.gridy = 2;
        billPanel.add(hotelContactLabel, gbc);
        
        // Separator line
        gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        billPanel.add(new JSeparator(), gbc);
        
        // Bill title
        gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JLabel billTitle = new JLabel("BOOKING INVOICE");
        billTitle.setFont(new Font("Arial", Font.BOLD, 16));
        billTitle.setForeground(new Color(52, 73, 94));
        billPanel.add(billTitle, gbc);
        
        // Booking Information Section
        gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.NONE;
        JLabel bookingSectionTitle = new JLabel("Booking Information");
        bookingSectionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        bookingSectionTitle.setForeground(new Color(52, 73, 94));
        billPanel.add(bookingSectionTitle, gbc);
        
        // Booking details
        gbc.gridy = 6; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        billPanel.add(new JLabel("Booking ID:"), gbc);
        gbc.gridx = 1;
        billPanel.add(bookingIdLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        billPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        billPanel.add(statusLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        billPanel.add(new JLabel("Issue Date:"), gbc);
        gbc.gridx = 1;
        billPanel.add(issueDateLabel, gbc);
        
        // Customer Information Section
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        JLabel customerSectionTitle = new JLabel("Customer Information");
        customerSectionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        customerSectionTitle.setForeground(new Color(52, 73, 94));
        billPanel.add(customerSectionTitle, gbc);
        
        // Customer details
        gbc.gridy = 10; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        billPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        billPanel.add(customerNameLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 11;
        billPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        billPanel.add(customerEmailLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 12;
        billPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        billPanel.add(customerPhoneLabel, gbc);
        
        // Room Information Section
        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        JLabel roomSectionTitle = new JLabel("Room Information");
        roomSectionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        roomSectionTitle.setForeground(new Color(52, 73, 94));
        billPanel.add(roomSectionTitle, gbc);
        
        // Room details
        gbc.gridy = 14; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        billPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        billPanel.add(roomNumberLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 15;
        billPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        billPanel.add(roomTypeLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 16;
        billPanel.add(new JLabel("Check-in Date:"), gbc);
        gbc.gridx = 1;
        billPanel.add(checkInDateLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 17;
        billPanel.add(new JLabel("Check-out Date:"), gbc);
        gbc.gridx = 1;
        billPanel.add(checkOutDateLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 18;
        billPanel.add(new JLabel("Number of Nights:"), gbc);
        gbc.gridx = 1;
        billPanel.add(nightsLabel, gbc);
        
        // Billing Information Section
        gbc.gridx = 0; gbc.gridy = 19; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        JLabel billingSectionTitle = new JLabel("Billing Information");
        billingSectionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        billingSectionTitle.setForeground(new Color(52, 73, 94));
        billPanel.add(billingSectionTitle, gbc);
        
        // Billing details
        gbc.gridy = 20; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        billPanel.add(new JLabel("Price per Night:"), gbc);
        gbc.gridx = 1;
        billPanel.add(pricePerNightLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 21;
        billPanel.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 1;
        billPanel.add(subtotalLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 22;
        billPanel.add(new JLabel("Tax (10%):"), gbc);
        gbc.gridx = 1;
        billPanel.add(taxLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 23;
        billPanel.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        JLabel totalLabel = new JLabel("$0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(new Color(46, 204, 113));
        billPanel.add(totalAmountLabel, gbc);
        
        // Footer
        gbc.gridx = 0; gbc.gridy = 25; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JLabel footerLabel = new JLabel("Thank you for choosing Grand Hotel Paradise!");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(128, 128, 128));
        billPanel.add(footerLabel, gbc);
        
        // Create scroll pane
        scrollPane = new JScrollPane(billPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(printButton);
        buttonPanel.add(emailButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setResizable(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    // Setters for bill information
    public void setBookingId(String bookingId) { bookingIdLabel.setText(bookingId); }
    public void setCustomerName(String customerName) { customerNameLabel.setText(customerName); }
    public void setCustomerEmail(String customerEmail) { customerEmailLabel.setText(customerEmail); }
    public void setCustomerPhone(String customerPhone) { customerPhoneLabel.setText(customerPhone); }
    public void setRoomNumber(String roomNumber) { roomNumberLabel.setText(roomNumber); }
    public void setRoomType(String roomType) { roomTypeLabel.setText(roomType); }
    public void setCheckInDate(String checkInDate) { checkInDateLabel.setText(checkInDate); }
    public void setCheckOutDate(String checkOutDate) { checkOutDateLabel.setText(checkOutDate); }
    public void setNights(String nights) { nightsLabel.setText(nights); }
    public void setPricePerNight(String pricePerNight) { pricePerNightLabel.setText(pricePerNight); }
    public void setSubtotal(String subtotal) { subtotalLabel.setText(subtotal); }
    public void setTax(String tax) { taxLabel.setText(tax); }
    public void setTotalAmount(String totalAmount) { totalAmountLabel.setText(totalAmount); }
    public void setStatus(String status) { statusLabel.setText(status); }
    public void setIssueDate(String issueDate) { issueDateLabel.setText(issueDate); }
    
    // Action listeners
    public void addPrintButtonListener(ActionListener listener) { printButton.addActionListener(listener); }
    public void addEmailButtonListener(ActionListener listener) { emailButton.addActionListener(listener); }
    public void addCloseButtonListener(ActionListener listener) { closeButton.addActionListener(listener); }
    public void addRefreshButtonListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    
    // Utility methods
    public void clearBill() {
        setBookingId("");
        setCustomerName("");
        setCustomerEmail("");
        setCustomerPhone("");
        setRoomNumber("");
        setRoomType("");
        setCheckInDate("");
        setCheckOutDate("");
        setNights("");
        setPricePerNight("");
        setSubtotal("");
        setTax("");
        setTotalAmount("");
        setStatus("");
        setIssueDate("");
    }
    
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    public void setLoadingState(boolean loading) {
        printButton.setEnabled(!loading);
        emailButton.setEnabled(!loading);
        if (loading) {
            printButton.setText("Printing...");
            emailButton.setText("Sending...");
        } else {
            printButton.setText("Print Bill");
            emailButton.setText("Email Bill");
        }
    }
    
    public JPanel getBillPanel() {
        return billPanel;
    }
    
    public void display() {
        setVisible(true);
    }
    
    public void dispose() {
        super.dispose();
    }
}
