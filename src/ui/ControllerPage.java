package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Controller;
import model.Job;
import model.User;

public class ControllerPage extends JFrame {

    private User currentUser;
    private Controller controller; 

    private CustomTable jobTable;
    

    private static final Color PAGE_BG = new Color(238, 238, 238);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);

    public ControllerPage(User user) {
        this.currentUser = user;
        this.controller = new Controller(1, null, null, null, null, null);

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


        JPanel contentArea = new JPanel(new GridBagLayout()); 
        contentArea.setBackground(PAGE_BG);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentArea.add(mainPanel, gbc);
        

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + "!");
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(welcomeLabel);

        GradientButton calcButton = new GradientButton("Calculate Job Completion Times");
        calcButton.setFont(new Font("Arial", Font.BOLD, 16));
        calcButton.setForeground(Color.WHITE);
        calcButton.setPreferredSize(new Dimension(300, 38)); 
        calcButton.setMaximumSize(new Dimension(300, 38));
        calcButton.setMinimumSize(new Dimension(300, 38));
        calcButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calcButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        calcButton.addActionListener(e -> {
            displayJobCompletionTimes();
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(calcButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
  
        String[] columnNames = {"Job ID", "Client ID", "Job Type", "Duration", "Arrival Time", "Completion Time"};
        
        int[] columnWidths = {100, 100, 250, 100, 400, 150}; 
        
        jobTable = new CustomTable(columnNames, columnWidths);
        jobTable.setVisible(false); 
        jobTable.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        mainPanel.add(jobTable);
        
        rootPanel.add(contentArea, BorderLayout.CENTER);
    }
    
    private void displayJobCompletionTimes() {
        DefaultTableModel tableModel = jobTable.getModel();
        tableModel.setRowCount(0); 

        List<Job> jobs = controller.calculateAllCompletionTimes();

        for (Job job : jobs) {
            Object[] row = {
                job.getJobId(),
                job.getAccountId(),
                job.getJobType(),
                job.getDuration(),
                job.getSubmissionTimestamp(),
                job.getCompletionTime()
            };
            tableModel.addRow(row);
        }

        jobTable.setVisible(true);
        
        this.revalidate();
        this.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testController = new User(
                    1, "Admin", "User", "controller@vcrts.com", "admin", 
                    "(123) 456-7890", "dummyhash", "Controller", 
                    "2025-01-01T12:00:00", true
                );
            
            ControllerPage controllerPage = new ControllerPage(testController);
            controllerPage.setVisible(true);
        });
    }
}