import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class ClientDashboard extends JFrame {
    private JComboBox<String> jobTypeCombo;
    private JTextField durationField;
    private JTextField deadlineField;
    private JTextField otherJobTypeField;
    private JTextArea descriptionArea;
    private DefaultTableModel tableModel;
    private int jobCounter = 1;

    public ClientDashboard() {
        setTitle("Client Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 50, 15, 50)
        ));
        JLabel headerTitle = new JLabel("VCRTS");
        headerTitle.setFont(new Font("Georgia", Font.PLAIN, 28));
        headerPanel.add(headerTitle, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setForeground(new Color(44, 116, 132));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // --- MAIN CONTENT AREA ---
        JPanel contentArea = new JPanel(new GridBagLayout());
        contentArea.setBackground(new Color(238, 238, 238));

        // --- FORM PANEL ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);

        // --- LAYOUT LOGIC ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        contentArea.add(mainPanel, gbc);

        // --- ADD PANELS TO ROOT ---
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(contentArea, BorderLayout.CENTER);

        // --- FORM COMPONENTS ---
        JLabel titleLabel = new JLabel("Submit A Job");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitleLabel = new JLabel("Enter job details to submit a new job request");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        Border defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );

        Border focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 124, 137), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );

        FocusAdapter highlightListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                ((JComponent) e.getComponent()).setBorder(focusBorder);
            }
            @Override
            public void focusLost(FocusEvent e) {
                ((JComponent) e.getComponent()).setBorder(defaultBorder);
            }
        };

        // --- FIRST ROW: Job Type and Duration ---
        JPanel firstRowPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        firstRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        firstRowPanel.setBackground(Color.WHITE);

        // Job Type Column
        JPanel jobTypeColumn = new JPanel();
        jobTypeColumn.setLayout(new BoxLayout(jobTypeColumn, BoxLayout.Y_AXIS));
        jobTypeColumn.setBackground(Color.WHITE);
        
        JLabel jobTypeLabel = new JLabel("Job Type:");
        jobTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jobTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jobTypeColumn.add(jobTypeLabel);
        jobTypeColumn.add(Box.createRigidArea(new Dimension(0, 8)));
        
        String[] jobTypes = {
            "Data Storage & Transfer",
            "Computational Task",
            "Simulation",
            "Networking & Communication",
            "Real-Time Processing",
            "Batch Processing",
            "Other"
        };
        jobTypeCombo = new JComboBox<>(jobTypes);
        jobTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        jobTypeCombo.setBackground(Color.WHITE);
        jobTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        jobTypeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        jobTypeColumn.add(jobTypeCombo);
        
        firstRowPanel.add(jobTypeColumn);

        // Duration Column
        JPanel durationColumn = new JPanel();
        durationColumn.setLayout(new BoxLayout(durationColumn, BoxLayout.Y_AXIS));
        durationColumn.setBackground(Color.WHITE);
        
        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        durationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        durationColumn.add(durationLabel);
        durationColumn.add(Box.createRigidArea(new Dimension(0, 8)));
        
        durationField = new JTextField();
        durationField.setFont(new Font("Arial", Font.PLAIN, 14));
        durationField.setBorder(defaultBorder);
        durationField.setPreferredSize(new Dimension(250, 35));
        durationField.setMaximumSize(new Dimension(250, 35));
        durationField.addFocusListener(highlightListener);
        durationField.setAlignmentX(Component.LEFT_ALIGNMENT);
        durationColumn.add(durationField);
        
        firstRowPanel.add(durationColumn);
        
        mainPanel.add(firstRowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SECOND ROW: If Other and Deadline ---
        JPanel secondRowPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        secondRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        secondRowPanel.setBackground(Color.WHITE);

        // Other Job Type Column
        JPanel otherColumn = new JPanel();
        otherColumn.setLayout(new BoxLayout(otherColumn, BoxLayout.Y_AXIS));
        otherColumn.setBackground(Color.WHITE);
        
        JLabel otherLabel = new JLabel("If \"Other\", please specify:");
        otherLabel.setFont(new Font("Arial", Font.BOLD, 14));
        otherLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        otherColumn.add(otherLabel);
        otherColumn.add(Box.createRigidArea(new Dimension(0, 8)));
        
        otherJobTypeField = new JTextField();
        otherJobTypeField.setFont(new Font("Arial", Font.PLAIN, 14));
        otherJobTypeField.setBorder(defaultBorder);
        otherJobTypeField.setPreferredSize(new Dimension(250, 35));
        otherJobTypeField.setMaximumSize(new Dimension(250, 35));
        otherJobTypeField.addFocusListener(highlightListener);
        otherJobTypeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        otherColumn.add(otherJobTypeField);
        
        secondRowPanel.add(otherColumn);

        // Deadline Column
        JPanel deadlineColumn = new JPanel();
        deadlineColumn.setLayout(new BoxLayout(deadlineColumn, BoxLayout.Y_AXIS));
        deadlineColumn.setBackground(Color.WHITE);
        
        JLabel deadlineLabel = new JLabel("Deadline:");
        deadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
        deadlineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        deadlineColumn.add(deadlineLabel);
        deadlineColumn.add(Box.createRigidArea(new Dimension(0, 8)));
        
        deadlineField = new JTextField();
        deadlineField.setFont(new Font("Arial", Font.PLAIN, 14));
        deadlineField.setBorder(defaultBorder);
        deadlineField.setPreferredSize(new Dimension(250, 35));
        deadlineField.setMaximumSize(new Dimension(250, 35));
        deadlineField.addFocusListener(highlightListener);
        deadlineField.setAlignmentX(Component.LEFT_ALIGNMENT);
        deadlineColumn.add(deadlineField);
        
        secondRowPanel.add(deadlineColumn);
        
        mainPanel.add(secondRowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Job Description ---
        JPanel descriptionLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        descriptionLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        descriptionLabelPanel.setBackground(Color.WHITE);
        JLabel descriptionLabel = new JLabel("Job Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabelPanel.add(descriptionLabel);
        mainPanel.add(descriptionLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(defaultBorder);
        descriptionArea.addFocusListener(highlightListener);
        descriptionArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(descriptionArea);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(44, 116, 132));
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setOpaque(true);
        Color defaultColor = new Color(44, 116, 132);
        Color hoverColor = new Color(37, 94, 106);
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(defaultColor);
            }
        });
        submitButton.addActionListener(e -> submitJob());
        mainPanel.add(submitButton);
    }

    private void submitJob() {
        String jobType = (String) jobTypeCombo.getSelectedItem();
        
        // If "Other" is selected, use the custom input
        if ("Other".equals(jobType)) {
            String customJobType = otherJobTypeField.getText().trim();
            if (customJobType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please specify the job type!");
                return;
            }
            jobType = customJobType;
        }
        
        String duration = durationField.getText().trim();
        String deadline = deadlineField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Field format enforcement
        if (duration.isEmpty() || deadline.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required!");
            return;
        }

        int durationInt;
        try {
            durationInt = Integer.parseInt(duration);
            if (durationInt <= 0) {
                JOptionPane.showMessageDialog(this, "Duration must be an hour or more!");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duration must be a valid number!");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(deadline);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Deadline must be in yyyy-mm-dd format!");
            return;
        }

        // Write client data to txt file
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Object[] row = {jobCounter++, jobType, durationInt, deadline, description, timestamp};

        try (FileWriter writer = new FileWriter("clients_data.txt", true)) {
            writer.write(String.join(" | ", 
                row[0].toString(), 
                jobType, 
                String.valueOf(durationInt), 
                deadline, 
                description.replace("\n", " "),
                timestamp) + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error writing to file!");
        }

        jobTypeCombo.setSelectedIndex(0);
        otherJobTypeField.setText("");
        durationField.setText("");
        deadlineField.setText("");
        descriptionArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientDashboard dashboard = new ClientDashboard();
            dashboard.setVisible(true);
        });
    }
}