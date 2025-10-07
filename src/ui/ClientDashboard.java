package ui;

import data.JobDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        contentArea.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // --- FORM PANEL ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(900, 650));
        mainPanel.setMaximumSize(new Dimension(900, 650));
        mainPanel.setMinimumSize(new Dimension(900, 650));

        // --- LAYOUT LOGIC ---
        GridBagConstraints gbc = new GridBagConstraints();
        contentArea.add(mainPanel, gbc);

        // --- ADD PANELS TO ROOT ---
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(contentArea, BorderLayout.CENTER);

        // --- FORM COMPONENTS ---
        JLabel titleLabel = new JLabel("Submit A Job");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalGlue());
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel subtitleLabel = new JLabel("Enter job details to submit a new job request");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setLayout(new BoxLayout(subtitlePanel, BoxLayout.X_AXIS));
        subtitlePanel.setBackground(Color.WHITE);
        subtitlePanel.add(Box.createHorizontalGlue());
        subtitlePanel.add(subtitleLabel);
        subtitlePanel.add(Box.createHorizontalGlue());
        mainPanel.add(subtitlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 124, 137), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        errorBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1, true),
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

        // --- Job Type ---
        JPanel jobTypeContainer = new JPanel();
        jobTypeContainer.setLayout(new BoxLayout(jobTypeContainer, BoxLayout.Y_AXIS));
        jobTypeContainer.setBackground(Color.WHITE);
        jobTypeContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JPanel jobTypeLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jobTypeLabelPanel.setBackground(Color.WHITE);
        jobTypeLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel jobTypeLabel = new JLabel("<html>Job Type: <font color='red'>*</font></html>");
        jobTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jobTypeLabelPanel.add(jobTypeLabel);
        jobTypeContainer.add(jobTypeLabelPanel);
        jobTypeContainer.add(Box.createRigidArea(new Dimension(0, 8)));

        String[] jobTypes = {
            "Data Storage & Transfer", "Computational Task", "Simulation",
            "Networking & Communication", "Real-Time Processing", "Batch Processing"
        };
        jobTypeCombo = new JComboBox<>(jobTypes);
        jobTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        jobTypeCombo.setBackground(Color.WHITE);
        jobTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        jobTypeContainer.add(jobTypeCombo);
        
        mainPanel.add(jobTypeContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Duration ---
        JPanel durationContainer = new JPanel();
        durationContainer.setLayout(new BoxLayout(durationContainer, BoxLayout.Y_AXIS));
        durationContainer.setBackground(Color.WHITE);
        durationContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        JPanel durationLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        durationLabelPanel.setBackground(Color.WHITE);
        durationLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel durationLabel = new JLabel("<html>Duration (Hours): <font color='red'>*</font></html>");
        durationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        durationLabelPanel.add(durationLabel);
        durationContainer.add(durationLabelPanel);
        durationContainer.add(Box.createRigidArea(new Dimension(0, 8)));

        durationField = new JTextField();
        durationField.setFont(new Font("Arial", Font.PLAIN, 14));
        durationField.setBorder(defaultBorder);
        durationField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        durationField.addFocusListener(highlightListener);
        durationContainer.add(durationField);
        
        durationErrorLabel = new JLabel(" ");
        durationErrorLabel.setForeground(Color.RED);
        durationErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel durationErrorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        durationErrorPanel.setBackground(Color.WHITE);
        durationErrorPanel.add(durationErrorLabel);
        durationContainer.add(durationErrorPanel);
        
        mainPanel.add(durationContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Deadline ---
        JPanel deadlineContainer = new JPanel();
        deadlineContainer.setLayout(new BoxLayout(deadlineContainer, BoxLayout.Y_AXIS));
        deadlineContainer.setBackground(Color.WHITE);
        deadlineContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        JPanel deadlineLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        deadlineLabelPanel.setBackground(Color.WHITE);
        deadlineLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel deadlineLabel = new JLabel("<html>Deadline: <font color='red'>*</font></html>");
        deadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
        deadlineLabelPanel.add(deadlineLabel);
        deadlineContainer.add(deadlineLabelPanel);
        deadlineContainer.add(Box.createRigidArea(new Dimension(0, 8)));

        deadlineField = new JTextField();
        deadlineField.setFont(new Font("Arial", Font.PLAIN, 14));
        deadlineField.setBorder(defaultBorder);
        deadlineField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        deadlineField.setEditable(false);
        deadlineField.setBackground(Color.WHITE);
        deadlineField.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        deadlineField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showCalendar(deadlineField, true, null);
            }
        });
        deadlineContainer.add(deadlineField);
        
        deadlineErrorLabel = new JLabel(" ");
        deadlineErrorLabel.setForeground(Color.RED);
        deadlineErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel deadlineErrorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        deadlineErrorPanel.setBackground(Color.WHITE);
        deadlineErrorPanel.add(deadlineErrorLabel);
        deadlineContainer.add(deadlineErrorPanel);

        mainPanel.add(deadlineContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Job Description ---
        JPanel descriptionContainer = new JPanel();
        descriptionContainer.setLayout(new BoxLayout(descriptionContainer, BoxLayout.Y_AXIS));
        descriptionContainer.setBackground(Color.WHITE);
        descriptionContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        JPanel descriptionLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        descriptionLabelPanel.setBackground(Color.WHITE);
        descriptionLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel descriptionLabel = new JLabel("Job Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabelPanel.add(descriptionLabel);
        descriptionContainer.add(descriptionLabelPanel);
        descriptionContainer.add(Box.createRigidArea(new Dimension(0, 8)));

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
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollPane.setBorder(defaultBorder);
        descriptionContainer.add(scrollPane);
        
        descriptionErrorLabel = new JLabel(" ");
        descriptionErrorLabel.setForeground(Color.RED);
        descriptionErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel descriptionErrorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        descriptionErrorPanel.setBackground(Color.WHITE);
        descriptionErrorPanel.add(descriptionErrorLabel);
        descriptionContainer.add(descriptionErrorPanel);

        mainPanel.add(descriptionContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // --- Submit Button ---
        GradientButton submitButton = new GradientButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setForeground(Color.WHITE);
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        submitButton.setPreferredSize(new Dimension(800, 38));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> submitJob());
        mainPanel.add(submitButton);
    }

    private void showCalendar(JTextField targetField, boolean restrictToFuture, Date minSelectableDate) {
        CalendarDialog calendarDialog = new CalendarDialog(this, restrictToFuture, minSelectableDate);
        calendarDialog.setLocationRelativeTo(targetField);
        calendarDialog.setVisible(true);
        if (calendarDialog.getSelectedDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetField.setText(dateFormat.format(calendarDialog.getSelectedDate()));
            targetField.setBorder(defaultBorder);
            deadlineErrorLabel.setText(" ");
        }
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
        
        if (deadlineField.getText().isEmpty()) {
            deadlineField.setBorder(errorBorder);
            deadlineErrorLabel.setText("Deadline date is required.");
            isValid = false;
        } else if (!validator.isDeadlineInFuture(deadlineField.getText())) {
            deadlineField.setBorder(errorBorder);
            deadlineErrorLabel.setText("Deadline cannot be in the past.");
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

    private static class GradientButton extends JButton {
        private boolean isHovered = false;
        
        public GradientButton(String text) {
            super(text);
            setOpaque(false);
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    isHovered = true;
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    isHovered = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color color1, color2;
            if (isHovered) {
                color1 = new Color(60, 200, 220);
                color2 = new Color(20, 140, 160);
            } else {
                color1 = new Color(50, 170, 190);
                color2 = new Color(30, 110, 130);
            }
            
            GradientPaint gradient = new GradientPaint(
                0, 0, color1,
                getWidth(), 0, color2
            );
            
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientDashboard dashboard = new ClientDashboard();
            dashboard.setVisible(true);
        });
    }
}