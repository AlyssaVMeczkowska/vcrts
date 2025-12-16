package ui;

import data.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.*;
import model.*;

public class ControllerRequestPage extends JFrame {

    private User currentUser;
    private RequestDataManager requestDataManager;
    private JobDataManager jobDataManager;
    private VehicleDataManager vehicleDataManager;
    private Timer liveUpdateTimer; 

    private JTable requestsTable;
    private DefaultTableModel tableModel;

    private JEditorPane detailsArea;
    private JButton acceptButton;
    private JButton rejectButton;
    
    private JCheckBox selectAllCheckbox;
    
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
        setSize(1200,800);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        initComponents();
        loadPendingRequests();
        liveUpdateTimer = new Timer(500, e -> loadPendingRequests());
        liveUpdateTimer.start();
    }

    @Override
    public void dispose() {
        if (liveUpdateTimer != null && liveUpdateTimer.isRunning()) {
            liveUpdateTimer.stop();
        }
        super.dispose();
    }

    private void initComponents() {

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.WHITE);
        setContentPane(rootPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JLabel titleLabel = new JLabel("Request Management - Controller Dashboard");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 15));
        titleWrapper.setBackground(Color.WHITE);
        titleWrapper.add(titleLabel);
        headerPanel.add(titleWrapper, BorderLayout.WEST);

        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new ControllerPage(currentUser).setVisible(true));
        });
        
        JPanel backWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 15));
        backWrapper.setBackground(Color.WHITE);
        backWrapper.add(backButton);
        headerPanel.add(backWrapper, BorderLayout.EAST);
        
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        rootPanel.add(mainPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel desc = new JLabel("<html>Review and manage pending job and vehicle submissions. "
                + "Select a request to view details, then <b>Accept</b> or <b>Reject</b>.</html>");
        desc.setForeground(new Color(100, 100, 100));
        desc.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        desc.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(desc, BorderLayout.NORTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(1);
        splitPane.setDividerLocation(600);
        splitPane.setBorder(null);
        splitPane.setBackground(Color.WHITE);
        splitPane.setOpaque(false);
        
        BasicSplitPaneUI ui = new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void setBorder(Border b) {}
                };
            }
        };
        splitPane.setUI(ui);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                "Pending Requests",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(70, 70, 70)
        ));

        selectAllCheckbox = new JCheckBox("Select All");
        selectAllCheckbox.setFont(new Font("Arial", Font.PLAIN, 13));
        selectAllCheckbox.setBackground(Color.WHITE);

        selectAllCheckbox.addActionListener(e -> {
            boolean check = selectAllCheckbox.isSelected();
            if (check) {
                requestsTable.selectAll();
            } else {
                requestsTable.clearSelection();
            }
        });

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectPanel.setBackground(Color.WHITE);
        selectPanel.add(selectAllCheckbox);
        tablePanel.add(selectPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Type", "User", "Timestamp"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        requestsTable = new JTable(tableModel);
        requestsTable.setShowVerticalLines(false);
        requestsTable.setShowHorizontalLines(false);
        requestsTable.setIntercellSpacing(new Dimension(0, 0));
        requestsTable.setGridColor(new Color(0, 0, 0, 0));

        requestsTable.setRowHeight(35);
        requestsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        requestsTable.setSelectionBackground(new Color(44, 116, 132));
        requestsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < cols.length; i++)
            requestsTable.getColumnModel().getColumn(i).setCellRenderer(center);
        
        requestsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                displayRequestDetails();
        });

        JScrollPane sp = new JScrollPane(requestsTable);
        sp.setBorder(null);               
        sp.setViewportBorder(null);
        tablePanel.add(sp, BorderLayout.CENTER);

        splitPane.setLeftComponent(tablePanel);


        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        splitPane.setRightComponent(rightPanel);

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                "Request Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        detailsArea = new JEditorPane("text/html", "");
        detailsArea.setEditable(false);
        detailsArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        detailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsArea.setText("Select a request to view details");
        
        detailsPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        rightPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 12, 0));

        acceptButton = new GradientButton("Accept");
        acceptButton.setFont(new Font("Arial", Font.BOLD, 16));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setPreferredSize(new Dimension(0, 45));
        acceptButton.setEnabled(false);
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.addActionListener(e -> handleAccept());

        rejectButton = new GradientButton("Reject", true);
        rejectButton.setFont(new Font("Arial", Font.BOLD, 16));
        rejectButton.setForeground(new Color(180, 0, 0));
        rejectButton.setPreferredSize(new Dimension(0, 45));
        rejectButton.setEnabled(false);
        rejectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rejectButton.addActionListener(e -> handleReject());

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadPendingRequests() {
        int[] selectedRows = requestsTable.getSelectedRows();
        List<Integer> selectedIds = new ArrayList<>();
        for (int row : selectedRows) {
            try {
                selectedIds.add((int) tableModel.getValueAt(row, 0));
            } catch (Exception e) {}
        }

        tableModel.setRowCount(0);
        List<Request> list = requestDataManager.getPendingRequests();

        for (Request r : list) {
            String type = r.getRequestType().equals("JOB_SUBMISSION") ? "Job" : "Vehicle";
            String ts = r.getTimestamp();

            try {
                LocalDateTime dt = LocalDateTime.parse(ts);
                ts = dt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
            } catch (Exception ignored) {}

            tableModel.addRow(new Object[]{
                    r.getRequestId(),
                    type,
                    r.getUserName(),
                    ts
            });
        }

        if (list.isEmpty()) {
            detailsArea.setText("No pending requests.");
            acceptButton.setEnabled(false);
            rejectButton.setEnabled(false);
        } else {
            if (!selectedIds.isEmpty()) {
                requestsTable.getSelectionModel().setValueIsAdjusting(true);
                for (int i = 0; i < requestsTable.getRowCount(); i++) {
                    int id = (int) requestsTable.getValueAt(i, 0);
                    if (selectedIds.contains(id)) {
                        requestsTable.addRowSelectionInterval(i, i);
                    }
                }
                requestsTable.getSelectionModel().setValueIsAdjusting(false);
            }
        }
    }

    private void displayRequestDetails() {
        int[] selected = requestsTable.getSelectedRows();
        if (selected.length == 0) {
            detailsArea.setText("Select a request to view details");
            acceptButton.setEnabled(false);
            rejectButton.setEnabled(false);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family:Arial;font-size:12px;'>");

        for (int row : selected) {
            int id = (int) tableModel.getValueAt(row, 0);
            Request r = requestDataManager.getRequestById(id);
            if (r == null) continue;

            sb.append("<div style='margin-bottom:15px;'>");
            sb.append("<div style='font-size:12px;font-weight:bold;color:#2C7484;'>")
                    .append("Request Details (ID: ").append(id).append(")</div>");
            sb.append("<div style='border:1px solid #ccc;padding:8px;font-size:10px;border-radius:6px;background:#FAFAFA;'>");
            sb.append("<b>Type:</b> ").append(r.getRequestType()).append("<br>");
            sb.append("<b>User:</b> ").append(r.getUserName()).append("<br>");
            sb.append("<b>Timestamp:</b> ").append(r.getTimestamp()).append("<br>");
            sb.append("</div>");
            sb.append("<div style='font-size:12px;font-weight:bold;margin-top:8px;color:#2C7484;'>")
                    .append("Submission Data:</div>");
            sb.append("<div style='border:1px solid #ccc;padding:8px;font-size:10px;border-radius:6px;background:#FAFAFA;'>");

            for (String line : r.getData().split("\n")) {
                if (!line.trim().isEmpty()) {
                    sb.append(line.replace(":", ": <b>")).append("</b><br>");
                }
            }

            sb.append("</div>");
            sb.append("<hr>");
            sb.append("</div>");
        }

        sb.append("</body></html>");

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);

        acceptButton.setEnabled(true);
        rejectButton.setEnabled(true);
    }


    private void handleAccept() {
        int[] rows = requestsTable.getSelectedRows();
        if (rows.length == 0) return;

        boolean bulk = rows.length > 1;
        CustomConfirmDialog confirm = new CustomConfirmDialog(
                this,
                bulk ? "Accept ALL selected submissions?" : "Accept this submission?",
                false
        );
        confirm.setVisible(true);
        if (!confirm.getResult()) return;
        
        boolean atLeastOneSuccess = false;

        for (int row : rows) {
            int requestId = (int) tableModel.getValueAt(row, 0);
            Request r = requestDataManager.getRequestById(requestId);
            if (r == null) continue;

            boolean ok = false;
            if (r.getRequestType().equals("JOB_SUBMISSION")) {
                ok = processJobSubmission(r);
            } else if (r.getRequestType().equals("VEHICLE_SUBMISSION")) {
                ok = processVehicleSubmission(r);
            }

            if (ok) {
                requestDataManager.updateRequestStatus(requestId, RequestStatus.ACCEPTED, null);
                atLeastOneSuccess = true;
            }
        }

        if (atLeastOneSuccess) {
            showMessage("Success",
                    bulk ? "Selected requests have been accepted!"
                         : "Request accepted successfully!",
                    CustomDialog.DialogType.SUCCESS);
            
            resetSelections();
            loadPendingRequests();
        } else {
            showMessage("Error", "Failed to process requests.", CustomDialog.DialogType.WARNING);
        }
    }

    private void handleReject() {
        int[] rows = requestsTable.getSelectedRows();
        if (rows.length == 0) return;

        boolean bulk = rows.length > 1;
        CustomInputDialog dialog = new CustomInputDialog(
                this,
                bulk ? "Enter rejection reason for ALL selected requests (optional): "
                     : "Enter rejection reason (optional): "
        );
        dialog.setVisible(true);
        String reason = dialog.getInput();

        if (reason == null) return;

        for (int row : rows) {
            int requestId = (int) tableModel.getValueAt(row, 0);
            requestDataManager.updateRequestStatus(requestId, RequestStatus.REJECTED, reason);
        }

        showMessage(
                "Success",
                bulk ? "All selected requests have been rejected."
                     : "Request rejected.",
                CustomDialog.DialogType.SUCCESS
        );
        resetSelections();
        loadPendingRequests();
        detailsArea.setText("Request(s) rejected.");
    }
    
    private void resetSelections() {
        selectAllCheckbox.setSelected(false);
        requestsTable.clearSelection();
        acceptButton.setEnabled(false);
        rejectButton.setEnabled(false);
    }

    private boolean processJobSubmission(Request request) {
        try {
            String[] lines = request.getData().split("\n");
            Map<String, String> jobData = new HashMap<>();
            
            for (String line : lines) {
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    jobData.put(parts[0].trim(), parts[1].trim());
                }
            }

            Job job = new Job(
                    Integer.parseInt(jobData.getOrDefault("user_id", "0")),
                    jobData.getOrDefault("job_type", "Unknown"),
                    Integer.parseInt(jobData.getOrDefault("duration", "1")),
                    jobData.getOrDefault("deadline", "2025-01-01"),
                    jobData.getOrDefault("description", "")
            );
            
            // Link the Job to the Request ID
            job.setRequestId(request.getRequestId());
            
            return jobDataManager.addJob(job);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean processVehicleSubmission(Request request) {
        try {
            String data = request.getData();
            if (data.startsWith("SOCKET_")) {
                data = data.split("\n", 2)[1];
            }
            
            String[] lines = data.split("\n");
            Map<String, String> vehicleData = new HashMap<>();
            
            for (String line : lines) {
                if (line.startsWith("type:") || line.equals("---")) {
                    continue;
                }
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    vehicleData.put(parts[0].trim(), parts[1].trim());
                }
            }

            int ownerId = Integer.parseInt(vehicleData.getOrDefault("user_id", "0"));
            Vehicle vehicle = new Vehicle(
                    0, 
                    ownerId,
                    vehicleData.get("vehicle_make"),
                    vehicleData.get("vehicle_model"),
                    Integer.parseInt(vehicleData.get("vehicle_year")),
                    vehicleData.get("vin"),
                    vehicleData.get("license_plate"),
                    vehicleData.get("computing_power"),
                    java.time.LocalDate.parse(vehicleData.get("start_date")),
                    java.time.LocalDate.parse(vehicleData.get("end_date")),
                    VehicleStatus.AVAILABLE
            );
            
            vehicle.setRequestId(request.getRequestId());
            
            return vehicleDataManager.addVehicle(vehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showMessage(String title, String message, CustomDialog.DialogType type) {
        new CustomDialog(this, title, message, type).setVisible(true);
    }
}