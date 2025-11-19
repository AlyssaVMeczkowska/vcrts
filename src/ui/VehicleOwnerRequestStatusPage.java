package ui;

import data.RequestDataManager;
import data.VehicleDataManager;
import model.User;
import model.Request;
import model.Vehicle;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VehicleOwnerRequestStatusPage extends JFrame {
    private User currentUser;
    private RequestDataManager requestDataManager;
    private VehicleDataManager vehicleDataManager;
    
    private static final Color PAGE_BG = new Color(238, 238, 238);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color PRIMARY_COLOR = new Color(44, 116, 132);
    
    private DefaultTableModel tableModel;
    private JLabel pendingCountLabel;
    private JLabel acceptedCountLabel;

    public VehicleOwnerRequestStatusPage(User user) {
        this.currentUser = user;
        this.requestDataManager = new RequestDataManager();
        this.vehicleDataManager = new VehicleDataManager();

        setTitle("VCRTS - My Vehicle Submissions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initComponents();
        loadRequestData();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.WHITE);
        setContentPane(rootPanel);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 50, 15, 50)
        ));

        JLabel headerTitle = new JLabel("VCRTS");
        headerTitle.setFont(new Font("Georgia", Font.PLAIN, 28));
        headerPanel.add(headerTitle, BorderLayout.WEST);

        JPanel headerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerButtonsPanel.setBackground(Color.WHITE);

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VehicleSubmissionPage(currentUser).setVisible(true));
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setForeground(PRIMARY_COLOR);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
        });

        headerButtonsPanel.add(backButton);
        headerButtonsPanel.add(logoutButton);
        headerPanel.add(headerButtonsPanel, BorderLayout.EAST);

        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // Main content area with scroll
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(PAGE_BG);

        JPanel scrollContent = new JPanel(new GridBagLayout());
        scrollContent.setBackground(PAGE_BG);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(1200, 900));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20);
        scrollContent.add(mainPanel, gbc);

        scrollPane.setViewportView(scrollContent);

        // Title
        JLabel titleLabel = new JLabel("My Vehicle Submissions");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>" +
                "Track the status of your vehicle submissions.<br>" +
                "Requests are reviewed by the controller for approval." +
                "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(descLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Summary cards
        JPanel summaryContainer = new JPanel();
        summaryContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
        summaryContainer.setBackground(Color.WHITE);
        summaryContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        pendingCountLabel = new JLabel("0");
        acceptedCountLabel = new JLabel("0");

        JPanel pendingCard = createStatusCard("Pending Review", pendingCountLabel, 
            new Color(255, 149, 0), new Color(255, 245, 235));
        JPanel acceptedCard = createStatusCard("Accepted", acceptedCountLabel, 
            new Color(52, 199, 89), new Color(235, 250, 240));

        summaryContainer.add(pendingCard);
        summaryContainer.add(acceptedCard);

        mainPanel.add(summaryContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Refresh button
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setBackground(Color.WHITE);
        refreshPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        refreshPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setForeground(PRIMARY_COLOR);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadRequestData());

        refreshPanel.add(refreshButton);
        mainPanel.add(refreshPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Table
        String[] columnNames = {"Vehicle ID", "Make/Model", "Year", "License Plate", 
                                "Submitted", "Status"};
        int[] columnWidths = {120, 250, 100, 180, 250, 150};

        CustomTable requestTable = new CustomTable(columnNames, columnWidths);
        requestTable.setAlignmentX(Component.CENTER_ALIGNMENT);
        tableModel = requestTable.getModel();

        // Custom renderer for status column
        requestTable.getTable().getColumnModel().getColumn(5).setCellRenderer(
            new StatusCellRenderer()
        );

        mainPanel.add(requestTable);

        rootPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStatusCard(String title, JLabel valueLabel, Color accentColor, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 30)
        ));
        card.setPreferredSize(new Dimension(230, 130));
        card.setMinimumSize(new Dimension(230, 130));
        card.setMaximumSize(new Dimension(230, 130));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(70, 70, 70));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel accentBarWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        accentBarWrapper.setBackground(Color.WHITE);
        accentBarWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        int titleWidth = titleLabel.getPreferredSize().width;
        accentBar.setPreferredSize(new Dimension(titleWidth, 2));
        accentBarWrapper.add(accentBar);

        valueLabel.setFont(new Font("Arial", Font.PLAIN, 44));
        valueLabel.setForeground(new Color(50, 50, 50));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentPanel.add(accentBarWrapper);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        contentPanel.add(valueLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private void loadRequestData() {
        tableModel.setRowCount(0);

        int pendingCount = 0;
        int acceptedCount = 0;
        
        // Get max Vehicle ID across ALL users (globally unique)
        int globalMaxVehicleId = getNextVehicleId();
        int pendingVehicleIdCounter = globalMaxVehicleId + 1;
        
        // Temporary list to hold all rows before sorting
        List<Object[]> allRows = new ArrayList<>();

        // 1. Load PENDING requests from pending_requests.txt
        List<Request> allRequests = requestDataManager.getPendingRequests();
        
        for (Request request : allRequests) {
            // Only show this user's requests that are PENDING and VEHICLE_SUBMISSION type
            if (request.getUserId() == currentUser.getId() && 
                "VEHICLE_SUBMISSION".equals(request.getRequestType())) {
                
                // Parse the vehicle data from the request
                String[] dataLines = request.getData().split("\n");
                Integer vehicleId = null;
                String make = "";
                String model = "";
                String year = "";
                String licensePlate = "";
                
                for (String line : dataLines) {
                    if (line.startsWith("Vehicle ID:")) {
                        try {
                            vehicleId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                        } catch (NumberFormatException e) {
                            vehicleId = null;
                        }
                    } else if (line.startsWith("Make:")) {
                        make = line.substring(line.indexOf(":") + 1).trim();
                    } else if (line.startsWith("Model:")) {
                        model = line.substring(line.indexOf(":") + 1).trim();
                    } else if (line.startsWith("Year:")) {
                        year = line.substring(line.indexOf(":") + 1).trim();
                    } else if (line.startsWith("License Plate:")) {
                        licensePlate = line.substring(line.indexOf(":") + 1).trim();
                    }
                }
                
                // If Vehicle ID wasn't found, assign next sequential ID globally
                if (vehicleId == null) {
                    vehicleId = pendingVehicleIdCounter;
                    pendingVehicleIdCounter++;
                }
                
                // Format timestamp
                String timestamp = request.getTimestamp();
                try {
                    LocalDateTime dt = LocalDateTime.parse(timestamp);
                    timestamp = dt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
                } catch (Exception e) {
                    // Keep original if parsing fails
                }
                
                allRows.add(new Object[]{
                    vehicleId,
                    make + " " + model,
                    year,
                    licensePlate,
                    timestamp,
                    "PENDING"
                });
                
                pendingCount++;
            }
        }

        // 2. Load ACCEPTED vehicles from vcrts_data.txt
        List<Vehicle> allVehicles = vehicleDataManager.getAllVehicles();
        
        for (Vehicle vehicle : allVehicles) {
            // Only show this user's vehicles
            if (vehicle.getOwnerId() == currentUser.getId()) {
                // Format timestamp - handle both OffsetDateTime and LocalDateTime formats
                String timestamp = vehicle.getSubmissionTimestamp();
                try {
                    // Try parsing as OffsetDateTime first (has timezone offset like -05:00)
                    java.time.OffsetDateTime odt = java.time.OffsetDateTime.parse(timestamp);
                    timestamp = odt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
                } catch (Exception e1) {
                    try {
                        // If that fails, try LocalDateTime
                        LocalDateTime dt = LocalDateTime.parse(timestamp);
                        timestamp = dt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
                    } catch (Exception e2) {
                        // Keep original if both parsing attempts fail
                    }
                }
                
                allRows.add(new Object[]{
                    vehicle.getVehicleId(),
                    vehicle.getMake() + " " + vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getLicensePlate(),
                    timestamp,
                    "ACCEPTED"
                });
                
                acceptedCount++;
            }
        }
        
        // 3. Sort all rows by Vehicle ID (ascending order)
        allRows.sort((row1, row2) -> {
            Integer id1 = (Integer) row1[0];
            Integer id2 = (Integer) row2[0];
            return id1.compareTo(id2);
        });
        
        // 4. Add sorted rows to table
        for (Object[] row : allRows) {
            tableModel.addRow(row);
        }

        // Update summary cards
        pendingCountLabel.setText(String.valueOf(pendingCount));
        acceptedCountLabel.setText(String.valueOf(acceptedCount));
    }
    
    private int getNextVehicleId() {
        int maxVehicleId = 0;
        
        // Check ALL accepted vehicles (globally, not just this user)
        List<Vehicle> allVehicles = vehicleDataManager.getAllVehicles();
        for (Vehicle vehicle : allVehicles) {
            if (vehicle.getVehicleId() > maxVehicleId) {
                maxVehicleId = vehicle.getVehicleId();
            }
        }
        
        // Check ALL pending requests with Vehicle ID (globally, not just this user)
        List<Request> allRequests = requestDataManager.getPendingRequests();
        for (Request request : allRequests) {
            if ("VEHICLE_SUBMISSION".equals(request.getRequestType())) {
                String[] dataLines = request.getData().split("\n");
                for (String line : dataLines) {
                    if (line.startsWith("Vehicle ID:")) {
                        try {
                            int vehicleId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                            if (vehicleId > maxVehicleId) {
                                maxVehicleId = vehicleId;
                            }
                        } catch (NumberFormatException e) {
                            // Skip
                        }
                        break;
                    }
                }
            }
        }
        
        return maxVehicleId;
    }

    // Custom cell renderer for status column
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        public StatusCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                JLabel label = (JLabel) c;
                label.setOpaque(true);
                label.setFont(new Font("Arial", Font.BOLD, 13));
                
                if ("PENDING".equals(status)) {
                    label.setForeground(new Color(255, 149, 0));
                    label.setBackground(new Color(255, 245, 235));
                    label.setText("PENDING");
                } else if ("ACCEPTED".equals(status)) {
                    label.setForeground(new Color(52, 199, 89));
                    label.setBackground(new Color(235, 250, 240));
                    label.setText("ACCEPTED");
                }
                
                if (isSelected) {
                    label.setBackground(new Color(230, 230, 230));
                }
            }
            
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User(
                    2, "Test", "Owner", "owner@test.com", "testowner",
                    "123-456-7890", "hash", "Owner",
                    LocalDateTime.now().toString(), true
            );
            VehicleOwnerRequestStatusPage page = new VehicleOwnerRequestStatusPage(testUser);
            page.setVisible(true);
        });
    }
}