package ui;

import data.JobDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.Border;
import model.Job;
import validation.JobValidator;

public class ClientDashboard extends JFrame {
    private JComboBox<String> jobTypeCombo;
    private JTextField durationField;
    private JTextField deadlineField;
    private JTextArea descriptionArea;

    private final JobDataManager dataManager = new JobDataManager();
    private final JobValidator validator = new JobValidator();

    private JLabel durationErrorLabel, deadlineErrorLabel, descriptionErrorLabel;
    private Border defaultBorder, focusBorder, errorBorder;

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

        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
        });
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

        defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 124, 137), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        errorBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );

        FocusAdapter highlightListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!((JComponent) e.getComponent()).getBorder().equals(errorBorder)) {
                    ((JComponent) e.getComponent()).setBorder(focusBorder);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (!((JComponent) e.getComponent()).getBorder().equals(errorBorder)) {
                    ((JComponent) e.getComponent()).setBorder(defaultBorder);
                }
            }
        };

        // --- Job Type Row ---
        JPanel jobTypeRow = new JPanel(new BorderLayout(10, 0));
        jobTypeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        jobTypeRow.setBackground(Color.WHITE);
        JLabel jobTypeLabel = new JLabel("Job Type:");
        jobTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jobTypeLabel.setPreferredSize(new Dimension(120, 20));
        jobTypeRow.add(jobTypeLabel, BorderLayout.WEST);
        String[] jobTypes = {
            "Data Storage & Transfer", "Computational Task", "Simulation",
            "Networking & Communication", "Real-Time Processing", "Batch Processing"
        };
        jobTypeCombo = new JComboBox<>(jobTypes);
        jobTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        jobTypeCombo.setBackground(Color.WHITE);
        jobTypeCombo.setPreferredSize(new Dimension(250, 20));
        jobTypeRow.add(jobTypeCombo, BorderLayout.CENTER);
        mainPanel.add(jobTypeRow);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Duration Row ---
        JPanel durationRow = new JPanel(new BorderLayout(10, 0));
        durationRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        durationRow.setBackground(Color.WHITE);
        JLabel durationLabel = new JLabel("Duration (Hours):");
        durationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        durationLabel.setPreferredSize(new Dimension(120, 20));
        durationRow.add(durationLabel, BorderLayout.WEST);
        durationField = new JTextField();
        durationField.setFont(new Font("Arial", Font.PLAIN, 14));
        durationField.setBorder(defaultBorder);
        durationField.setPreferredSize(new Dimension(250, 20));
        durationField.addFocusListener(highlightListener);
        durationRow.add(durationField, BorderLayout.CENTER);
        
        durationErrorLabel = new JLabel(" ");
        JPanel durationContainer = new JPanel();
        durationContainer.setLayout(new BoxLayout(durationContainer, BoxLayout.Y_AXIS));
        durationContainer.setBackground(Color.WHITE);
        durationContainer.add(durationRow);
        durationContainer.add(createErrorPanel(durationErrorLabel, 130));
        
        mainPanel.add(durationContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Deadline Row ---
        JPanel deadlineRow = new JPanel(new BorderLayout(10, 0));
        deadlineRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        deadlineRow.setBackground(Color.WHITE);
        JLabel deadlineLabel = new JLabel("Deadline:");
        deadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
        deadlineLabel.setPreferredSize(new Dimension(120, 20));
        deadlineRow.add(deadlineLabel, BorderLayout.WEST);
        deadlineField = new JTextField();
        deadlineField.setFont(new Font("Arial", Font.PLAIN, 14));
        deadlineField.setBorder(defaultBorder);
        deadlineField.setPreferredSize(new Dimension(250, 20));
        deadlineField.addFocusListener(highlightListener);
        deadlineRow.add(deadlineField, BorderLayout.CENTER);
        
        deadlineErrorLabel = new JLabel(" ");
        JPanel deadlineContainer = new JPanel();
        deadlineContainer.setLayout(new BoxLayout(deadlineContainer, BoxLayout.Y_AXIS));
        deadlineContainer.setBackground(Color.WHITE);
        deadlineContainer.add(deadlineRow);
        deadlineContainer.add(createErrorPanel(deadlineErrorLabel, 130));

        mainPanel.add(deadlineContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Job Description Row ---
        JPanel descriptionRow = new JPanel(new BorderLayout(10, 0));
        descriptionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        descriptionRow.setBackground(Color.WHITE);
        JLabel descriptionLabel = new JLabel("Job Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabel.setPreferredSize(new Dimension(120, 20));
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptionRow.add(descriptionLabel, BorderLayout.WEST);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        FocusAdapter scrollPaneHighlightListener = new FocusAdapter() {
            private void updateBorder(FocusEvent e, boolean hasFocus) {
                JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, (Component) e.getSource());
                if (scrollPane != null && !scrollPane.getBorder().equals(errorBorder)) {
                    scrollPane.setBorder(hasFocus ? focusBorder : defaultBorder);
                }
            }
            @Override public void focusGained(FocusEvent e) { updateBorder(e, true); }
            @Override public void focusLost(FocusEvent e) { updateBorder(e, false); }
        };
        descriptionArea.addFocusListener(scrollPaneHighlightListener);
        descriptionArea.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(400, 80));
        scrollPane.setBorder(defaultBorder);
        descriptionRow.add(scrollPane, BorderLayout.CENTER);
        
        descriptionErrorLabel = new JLabel(" ");
        JPanel descriptionContainer = new JPanel();
        descriptionContainer.setLayout(new BoxLayout(descriptionContainer, BoxLayout.Y_AXIS));
        descriptionContainer.setBackground(Color.WHITE);
        descriptionContainer.add(descriptionRow);
        descriptionContainer.add(createErrorPanel(descriptionErrorLabel, 130));

        mainPanel.add(descriptionContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // --- Submit Button ---
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
            public void mouseEntered(java.awt.event.MouseEvent evt) { submitButton.setBackground(hoverColor); }
            public void mouseExited(java.awt.event.MouseEvent evt) { submitButton.setBackground(defaultColor); }
        });
        submitButton.addActionListener(e -> submitJob());
        mainPanel.add(submitButton);
    }

    private JPanel createErrorPanel(JLabel errorLabel, int leftIndent) {
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.setBorder(BorderFactory.createEmptyBorder(0, leftIndent, 0, 0));
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        errorPanel.add(errorLabel);
        return errorPanel;
    }

    private boolean validateForm() {
        boolean isValid = true;
        
        durationField.setBorder(defaultBorder);
        deadlineField.setBorder(defaultBorder);
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, descriptionArea);
        if (scrollPane != null) {
            scrollPane.setBorder(defaultBorder);
        }
        durationErrorLabel.setText(" ");
        deadlineErrorLabel.setText(" ");
        descriptionErrorLabel.setText(" ");

        if (!validator.isDurationValid(durationField.getText())) {
            durationField.setBorder(errorBorder);
            durationErrorLabel.setText("Duration must be a positive number.");
            isValid = false;
        }
        if (!validator.isDeadlineValid(deadlineField.getText())) {
            deadlineField.setBorder(errorBorder);
            deadlineErrorLabel.setText("Deadline must be a valid date (yyyy-MM-dd).");
            isValid = false;
        }
        if (!validator.isFieldValid(descriptionArea.getText())) {
            if (scrollPane != null) {
                scrollPane.setBorder(errorBorder);
            }
            descriptionErrorLabel.setText("Description cannot be empty.");
            isValid = false;
        }
        return isValid;
    }

    private void submitJob() {
        if (!validateForm()) {
            return;
        }

        String jobType = (String) jobTypeCombo.getSelectedItem();
        String duration = durationField.getText();
        String deadline = deadlineField.getText();
        String description = descriptionArea.getText();
        
        Job job = new Job(
            jobType,
            Integer.parseInt(duration.trim()),
            deadline.trim(),
            description.trim()
        );
        
        if (dataManager.addJob(job)) {
            JOptionPane.showMessageDialog(this, "Job submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error saving job to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        jobTypeCombo.setSelectedIndex(0);
        durationField.setText("");
        deadlineField.setText("");
        descriptionArea.setText("");

        durationField.setBorder(defaultBorder);
        deadlineField.setBorder(defaultBorder);
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, descriptionArea);
        if (scrollPane != null) {
            scrollPane.setBorder(defaultBorder);
        }
        durationErrorLabel.setText(" ");
        deadlineErrorLabel.setText(" ");
        descriptionErrorLabel.setText(" ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientDashboard dashboard = new ClientDashboard();
            dashboard.setVisible(true);
        });
    }
}