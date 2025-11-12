package ui;

import data.*;
import model.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ControllerRequestPage extends JFrame {
    private User currentUser;
    private RequestDataManager requestDataManager;
    private JobDataManager jobDataManager;
    private VehicleDataManager vehicleDataManager;
    
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;
    private JButton acceptButton;
    private JButton rejectButton;
    private JButton refreshButton;
    
    private static final Color PAGE_BG = new Color(238, 238, 238);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color PRIMARY_COLOR = new Color(44, 116, 132);

    public ControllerRequestPage(User user) {
        this.currentUser = user;
        this.requestDataManager = new RequestDataManager();
        this.jobDataManager = new JobDataManager();
        this.vehicleDataManager = new VehicleDataManager();

        setTitle("VCRTS - Request Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);


        initComponents();
        loadPendingRequests();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.WHITE);
        setContentPane(rootPanel);

        // Header
        // Unified Header (matches ControllerPage aesthetic)
JPanel headerPanel = new JPanel(new BorderLayout());
headerPanel.setBackground(Color.WHITE);
headerPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
        BorderFactory.createEmptyBorder(15, 50, 15, 50)
));

JLabel headerTitle = new JLabel("VCRTS Controller");
headerTitle.setFont(new Font("Georgia", Font.PLAIN, 28));
headerPanel.add(headerTitle, BorderLayout.WEST);

JButton backButton = new JButton("← Back to Dashboard");
backButton.setFont(new Font("Arial", Font.BOLD, 14));
backButton.setFocusPainted(false);
backButton.setBorderPainted(false);
backButton.setContentAreaFilled(false);
backButton.setForeground(PRIMARY_COLOR);
backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
backButton.addActionListener(e -> {
    dispose();
    SwingUtilities.invokeLater(() -> new ControllerPage(currentUser).setVisible(true));
});
headerPanel.add(backButton, BorderLayout.EAST);

rootPanel.add(headerPanel, BorderLayout.NORTH);


        // Main content area
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Top panel with description and refresh button (aligned in one row)
JPanel topPanel = new JPanel(new BorderLayout());
topPanel.setBackground(Color.WHITE);
topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

JLabel descLabel = new JLabel("<html>Review and manage pending job and vehicle submissions. " +
        "Select a request to view details, then <b>Accept</b> or <b>Reject</b>.</html>");
descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
descLabel.setForeground(new Color(100, 100, 100));

refreshButton = new JButton("Refresh");
refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
refreshButton.setFocusPainted(false);
refreshButton.setBorderPainted(false);
refreshButton.setContentAreaFilled(false);
refreshButton.setForeground(PRIMARY_COLOR);
refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
refreshButton.addActionListener(e -> loadPendingRequests());

// horizontal layout
JPanel rowPanel = new JPanel(new BorderLayout());
rowPanel.setBackground(Color.WHITE);
rowPanel.add(descLabel, BorderLayout.WEST);
rowPanel.add(refreshButton, BorderLayout.EAST);

topPanel.add(rowPanel, BorderLayout.CENTER);
mainPanel.add(topPanel, BorderLayout.NORTH);

        // Split pane for table and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setBorder(null);

        // Left: Requests Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                "Pending Requests",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(80, 80, 80)
        ));

        String[] columnNames = {
    "ID", 
    "User", 
    "Timestamp", 
    "Job Deadline", 
};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        requestsTable = new JTable(tableModel);
        requestsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        requestsTable.setRowHeight(35);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displayRequestDetails();
            }
        });

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < requestsTable.getColumnCount(); i++) {
            requestsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        tableScrollPane.setBorder(null);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(tablePanel);

        // Right: Details and Actions
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                "Request Details",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(80, 80, 80)
        ));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setMargin(new Insets(10, 10, 10, 10));
        detailsArea.setText("Select a request to view details");

        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBorder(null);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);

        rightPanel.add(detailsPanel, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        acceptButton = new GradientButton("✓ Accept");
        acceptButton.setFont(new Font("Arial", Font.BOLD, 16));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setPreferredSize(new Dimension(0, 45));
        acceptButton.setEnabled(false);
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.addActionListener(e -> handleAccept());

        rejectButton = new GradientButton("✗ Reject", true);
        rejectButton.setFont(new Font("Arial", Font.BOLD, 16));
        rejectButton.setForeground(new Color(180, 0, 0));
        rejectButton.setPreferredSize(new Dimension(0, 45));
        rejectButton.setEnabled(false);
        rejectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rejectButton.addActionListener(e -> handleReject());

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        splitPane.setRightComponent(rightPanel);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        rootPanel.add(mainPanel, BorderLayout.CENTER);
    }

    private void loadPendingRequests() {
        tableModel.setRowCount(0);
        List<Request> pendingRequests = requestDataManager.getPendingRequests();

        for (Request request : pendingRequests) {
            String type = request.getRequestType().equals("JOB_SUBMISSION") ? "Job" : "Vehicle";
            String timestamp = request.getTimestamp();
            
            // Format timestamp to be more readable
            try {
                LocalDateTime dt = LocalDateTime.parse(timestamp);
                timestamp = dt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
            } catch (Exception e) {
                // Keep original if parsing fails
            }

            tableModel.addRow(new Object[]{
                    request.getRequestId(),
                    type,
                    request.getUserName(),
                    timestamp
            });
        }

        if (pendingRequests.isEmpty()) {
            detailsArea.setText("No pending requests.\n\nAll submissions have been processed.");
            acceptButton.setEnabled(false);
            rejectButton.setEnabled(false);
        }
    }

    private void displayRequestDetails() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            detailsArea.setText("Select a request to view details");
            acceptButton.setEnabled(false);
            rejectButton.setEnabled(false);
            return;
        }

        int requestId = (int) tableModel.getValueAt(selectedRow, 0);
        Request request = requestDataManager.getRequestById(requestId);

        if (request != null) {
            StringBuilder details = new StringBuilder();
            details.append("═══════════════════════════════════════\n");
            details.append("  REQUEST DETAILS\n");
            details.append("═══════════════════════════════════════\n\n");
            details.append(String.format("Request ID:    %d\n", request.getRequestId()));
            details.append(String.format("Type:          %s\n", request.getRequestType()));
            details.append(String.format("User:          %s (ID: %d)\n", request.getUserName(), request.getUserId()));
            details.append(String.format("Timestamp:     %s\n", request.getTimestamp()));
            details.append("\n───────────────────────────────────────\n");
            details.append("  SUBMISSION DATA\n");
            details.append("───────────────────────────────────────\n\n");
            details.append(request.getData());
            details.append("\n\n═══════════════════════════════════════\n");

            detailsArea.setText(details.toString());
            detailsArea.setCaretPosition(0);

            acceptButton.setEnabled(true);
            rejectButton.setEnabled(true);
        }
    }

    private void handleAccept() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) return;

        int requestId = (int) tableModel.getValueAt(selectedRow, 0);
        Request request = requestDataManager.getRequestById(requestId);

        if (request == null) {
            showMessage("Error", "Could not find request.", CustomDialog.DialogType.WARNING);
            return;
        }

        // Confirm action
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Accept this " + (request.getRequestType().equals("JOB_SUBMISSION") ? "job" : "vehicle") + " submission?",
                "Confirm Acceptance",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Process the request based on type
        boolean success = false;
        if (request.getRequestType().equals("JOB_SUBMISSION")) {
            success = processJobSubmission(request);
        } else if (request.getRequestType().equals("VEHICLE_SUBMISSION")) {
            success = processVehicleSubmission(request);
        }

        if (success) {
            // Update request status
            requestDataManager.updateRequestStatus(requestId, RequestStatus.ACCEPTED, null);
            
            showMessage("Success", "Request accepted and data saved successfully!", CustomDialog.DialogType.SUCCESS);
            
            // Refresh the table
            loadPendingRequests();
        } else {
            showMessage("Error", "Failed to save data. Please try again.", CustomDialog.DialogType.WARNING);
        }
    }

    private void handleReject() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) return;

        int requestId = (int) tableModel.getValueAt(selectedRow, 0);
        Request request = requestDataManager.getRequestById(requestId);

        if (request == null) {
            showMessage("Error", "Could not find request.", CustomDialog.DialogType.WARNING);
            return;
        }

        // Get rejection reason
        String reason = JOptionPane.showInputDialog(
                this,
                "Enter rejection reason (optional):",
                "Reject Request",
                JOptionPane.QUESTION_MESSAGE
        );

        if (reason == null) {
            return; // User cancelled
        }

        // Update request status
        boolean updated = requestDataManager.updateRequestStatus(requestId, RequestStatus.REJECTED, reason);

        if (updated) {
            showMessage("Success", "Request rejected.", CustomDialog.DialogType.SUCCESS);
            loadPendingRequests();
        } else {
            showMessage("Error", "Failed to update request status.", CustomDialog.DialogType.WARNING);
        }
    }

    private boolean processJobSubmission(Request request) {
        try {
            // Parse job data from request
            String[] lines = request.getData().split("\n");
            Map<String, String> jobData = new HashMap<>();
            
            for (String line : lines) {
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    jobData.put(parts[0].trim(), parts[1].trim());
                }
            }

            Job job = new Job(
                    Integer.parseInt(jobData.get("Client ID")),
                    jobData.get("Job Type"),
                    Integer.parseInt(jobData.get("Duration")),
                    jobData.get("Deadline"),
                    jobData.getOrDefault("Description", "")
            );

            return jobDataManager.addJob(job);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean processVehicleSubmission(Request request) {
        try {
            // Parse vehicle data from request
            String[] lines = request.getData().split("\n");
            Map<String, String> vehicleData = new HashMap<>();
            
            for (String line : lines) {
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    vehicleData.put(parts[0].trim(), parts[1].trim());
                }
            }

            Vehicle vehicle = new Vehicle(
                    Integer.parseInt(vehicleData.get("Owner ID")),
                    Integer.parseInt(vehicleData.get("Owner ID")),
                    vehicleData.get("Make"),
                    vehicleData.get("Model"),
                    Integer.parseInt(vehicleData.get("Year")),
                    vehicleData.get("VIN"),
                    vehicleData.get("License Plate"),
                    vehicleData.get("Computing Power"),
                    java.time.LocalDate.parse(vehicleData.get("Arrival Date")),
                    java.time.LocalDate.parse(vehicleData.get("Departure Date")),
                    VehicleStatus.AVAILABLE
            );

            return vehicleDataManager.addVehicle(vehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showMessage(String title, String message, CustomDialog.DialogType type) {
        CustomDialog dialog = new CustomDialog(this, title, message, type);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testController = new User(
                    1, "Admin", "Controller", "controller@vcrts.com", "controller",
                    "123-456-7890", "hash", "Controller",
                    LocalDateTime.now().toString(), true
            );
            ControllerRequestPage page = new ControllerRequestPage(testController);
            page.setVisible(true);
        });
    }
}