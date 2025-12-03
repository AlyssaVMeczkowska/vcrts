package ui;

import data.RequestDataManager;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Controller;
import model.Job;
import model.User;
import model.Vehicle;

public class ControllerPage extends JFrame {

    private User currentUser;
    private Controller controller; 
    private JPanel tablesContainer;
    private JPanel mainPanel; 
    
    private JLabel vehicleCountLabel;
    private JLabel jobCountLabel;
    private JLabel pendingRequestsLabel;
    private RequestDataManager requestDataManager;
    private Timer liveUpdateTimer;

    private static final Color PAGE_BG = new Color(238, 238, 238);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color VEHICLE_HEADER_BG = new Color(44, 116, 132);
    private static final Color VEHICLE_HEADER_TEXT = Color.WHITE;

    public ControllerPage(User user) {
        this.currentUser = user;
        this.controller = new Controller(1, null, null, null, null, null);
        this.requestDataManager = new RequestDataManager();
        System.out.println(">>> NEW ControllerPage CREATED <<<");
        
        setTitle("VCRTS Controller Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 50, 15, 50)
        ));
        JLabel headerTitle = new JLabel("VCRTS Controller");
        headerTitle.setFont(new Font("Georgia", Font.PLAIN, 28));
        headerPanel.add(headerTitle, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setForeground(new Color(44, 116, 132));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(PAGE_BG);
        
        JPanel scrollContent = new JPanel(new GridBagLayout());
        scrollContent.setBackground(PAGE_BG);
        mainPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(1100, Math.max(800, d.height));
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.fill = GridBagConstraints.NONE; 

        gbc.insets = new Insets(30, 0, 30, 0);
        
        scrollContent.add(mainPanel, gbc);
        scrollPane.setViewportView(scrollContent);
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + "!");
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(welcomeLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel descriptionLabel = new JLabel("<html><div style='text-align: center;'>" +
            "Manage job assignments, review pending requests, and monitor system status.<br>" +
            "Use the buttons below to access different management functions." +
            "</div></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setForeground(new Color(100, 100, 100));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainPanel.add(descriptionLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        int totalVehicles = controller.viewVehicles() != null ? controller.viewVehicles().size() : 0;
        int totalJobs = controller.viewJobs() != null ? controller.viewJobs().size() : 0;
        int pendingRequests = requestDataManager.getPendingRequests().size();
        
        vehicleCountLabel = new JLabel(String.valueOf(totalVehicles));
        jobCountLabel = new JLabel(String.valueOf(totalJobs));
        pendingRequestsLabel = new JLabel(String.valueOf(pendingRequests));
        
        JPanel summaryContainer = new JPanel();
        summaryContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
        summaryContainer.setBackground(Color.WHITE);
        summaryContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        JPanel vehicleCard = createStyledSummaryCard(
            "Available Vehicles",
            vehicleCountLabel, 
            "Ready for job assignment",
            new Color(44, 116, 132),
            new Color(230, 245, 248)
        );
        JPanel jobsCard = createStyledSummaryCard(
            "Total Jobs",
            jobCountLabel, 
            "Pending completion",
            new Color(52, 199, 89),
            new Color(235, 250, 240)
        );
        JPanel requestsCard = createStyledSummaryCard(
            "Pending Requests",
            pendingRequestsLabel,
            "Awaiting review",
            new Color(255, 149, 0),
            new Color(255, 245, 235)
        );
        summaryContainer.add(vehicleCard);
        summaryContainer.add(jobsCard);
        summaryContainer.add(requestsCard);
        
        mainPanel.add(summaryContainer);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionButtonsPanel.setBackground(Color.WHITE);
        actionButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GradientButton manageRequestsButton = new GradientButton("Manage Requests");
        manageRequestsButton.setFont(new Font("Arial", Font.BOLD, 16));
        manageRequestsButton.setForeground(Color.WHITE);
        manageRequestsButton.setPreferredSize(new Dimension(250, 38));
        manageRequestsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        manageRequestsButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new ControllerRequestPage(currentUser).setVisible(true));
        });
        GradientButton calcButton = new GradientButton("Calculate Job Completion Times");
        calcButton.setFont(new Font("Arial", Font.BOLD, 16));
        calcButton.setForeground(Color.WHITE);
        calcButton.setPreferredSize(new Dimension(300, 38));
        calcButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calcButton.addActionListener(e -> displayJobCompletionTimes());
        
        actionButtonsPanel.add(manageRequestsButton);
        actionButtonsPanel.add(calcButton);
        
        mainPanel.add(actionButtonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        tablesContainer = new JPanel();
        tablesContainer.setLayout(new BoxLayout(tablesContainer, BoxLayout.Y_AXIS));
        tablesContainer.setBackground(Color.WHITE);
        tablesContainer.setVisible(false);
        
        mainPanel.add(tablesContainer);
        
        rootPanel.add(scrollPane, BorderLayout.CENTER);
        
        liveUpdateTimer = new Timer(500, e -> updateDashboardData());
        liveUpdateTimer.start();
        
        this.validate();
        this.repaint();
    }
    
    private void updateDashboardData() {
        int currentPending = requestDataManager.getPendingRequests().size();
        pendingRequestsLabel.setText(String.valueOf(currentPending));
        if (tablesContainer.isVisible()) {
            displayJobCompletionTimes(); 
        } else {
            controller.refreshAndProcessData();
            int totalVehicles = controller.viewVehicles().size();
            int totalJobs = controller.viewJobs().size();
            
            vehicleCountLabel.setText(String.valueOf(totalVehicles));
            jobCountLabel.setText(String.valueOf(totalJobs));
        }
    }

    @Override
    public void dispose() {
        if (liveUpdateTimer != null && liveUpdateTimer.isRunning()) {
            liveUpdateTimer.stop();
        }
        super.dispose();
    }
    
    private void displayJobCompletionTimes() {
        controller.refreshAndProcessData();
        int newVehicleCount = controller.viewVehicles().size();
        int newJobCount = controller.viewJobs().size();
        
        vehicleCountLabel.setText(String.valueOf(newVehicleCount));
        jobCountLabel.setText(String.valueOf(newJobCount));
        tablesContainer.removeAll();
        Map<Integer, Queue<Job>> vehicleQueues = controller.getVehicleJobQueues();
        
        if (vehicleQueues == null || vehicleQueues.isEmpty()) {
            JLabel noDataLabel = new JLabel("No vehicles or jobs available.");
            noDataLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            tablesContainer.add(noDataLabel);
        } else {
            List<Integer> vehicleIDs = controller.getAllVehicleIDs();
            for (Integer vehicleId : vehicleIDs) {
                Vehicle vehicle = controller.getVehicleById(vehicleId);
                List<Job> vehicleJobs = controller.getJobsForVehicle(vehicleId);
                
                if (vehicle != null) { 
                    JPanel vehiclePanel = createVehiclePanel(vehicle, vehicleJobs);
                    tablesContainer.add(vehiclePanel);
                    tablesContainer.add(Box.createRigidArea(new Dimension(0, 25)));
                }
            }
        }
        
        tablesContainer.setVisible(true);
        mainPanel.revalidate();
        this.revalidate();
        this.repaint();
    }
    
    private JPanel createStyledSummaryCard(String title, JLabel valueLabel, String subtitle, Color accentColor, Color bgColor) {
        JPanel outerCard = new JPanel(new BorderLayout());
        outerCard.setBackground(Color.WHITE);
        outerCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 30)
        ));
        outerCard.setPreferredSize(new Dimension(230, 130));
        outerCard.setMinimumSize(new Dimension(230, 130));
        outerCard.setMaximumSize(new Dimension(230, 130));
        
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
        
        outerCard.add(contentPanel, BorderLayout.CENTER);
        
        return outerCard;
    }
    
    private JPanel createVehiclePanel(Vehicle vehicle, List<Job> jobs) {
        JPanel vehiclePanel = new JPanel();
        vehiclePanel.setLayout(new BoxLayout(vehiclePanel, BoxLayout.Y_AXIS));
        vehiclePanel.setBackground(Color.WHITE);
        vehiclePanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(VEHICLE_HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel leftHeaderPanel = new JPanel();
        leftHeaderPanel.setLayout(new BoxLayout(leftHeaderPanel, BoxLayout.Y_AXIS));
        leftHeaderPanel.setBackground(VEHICLE_HEADER_BG);
        
        String vehicleInfo = String.format("Vehicle #%d - %s %s %d", 
            vehicle.getVehicleId(), 
            vehicle.getMake(), 
            vehicle.getModel(), 
            vehicle.getYear()
        );
        JLabel vehicleLabel = new JLabel(vehicleInfo);
        vehicleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        vehicleLabel.setForeground(VEHICLE_HEADER_TEXT);
        JLabel vehicleDetailsLabel = new JLabel(String.format("VIN: %s | License: %s | Power: %s", 
            vehicle.getVin(), 
            vehicle.getLicensePlate(),
            vehicle.getComputingPower()
        ));
        vehicleDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        vehicleDetailsLabel.setForeground(VEHICLE_HEADER_TEXT);
        
        leftHeaderPanel.add(vehicleLabel);
        leftHeaderPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        leftHeaderPanel.add(vehicleDetailsLabel);
        
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        
        JLabel jobCountLabel = new JLabel(jobs.size() + " job(s)");
        jobCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jobCountLabel.setForeground(VEHICLE_HEADER_TEXT);
        headerPanel.add(jobCountLabel, BorderLayout.EAST);
        
        vehiclePanel.add(headerPanel);
        vehiclePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        String[] columnNames = {"Job ID", "Client ID", "Job Type", "Duration (hrs)", "Completion Time (hrs)", "Deadline"};
        int[] columnWidths = {
            90,  
            90,  
            250, 
            120, 
            180, 
            200
        };
        CustomTable jobTable = new CustomTable(columnNames, columnWidths);
        jobTable.setAlignmentX(Component.CENTER_ALIGNMENT); 
        DefaultTableModel tableModel = jobTable.getModel();
        for (Job job : jobs) {
            Object[] row = {
                job.getJobId(),
                job.getAccountId(),
                job.getJobType(),
                job.getDuration(),
                 job.getCompletionTime(),
                job.getDeadline()
            };
            tableModel.addRow(row);
        }
        

        jobTable.adjustTableHeight();
        
        vehiclePanel.add(jobTable);
        return vehiclePanel;
    }
}