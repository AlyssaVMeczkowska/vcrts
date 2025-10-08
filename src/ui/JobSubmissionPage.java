package ui;

import data.JobDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import model.Job;
import model.User;
import validation.JobValidator;

public class JobSubmissionPage extends JFrame {
    private final JobDataManager dataManager = new JobDataManager();
    private final JobValidator validator = new JobValidator();
    private Border defaultBorder, focusBorder, errorBorder;
    
    private User currentUser;
    private JPanel formsContainer;
    private GradientButton submitButton;
    private List<JobFormPanel> jobForms;
    private int jobCounter = 1;
    private JPanel mainPanel;

    public JobSubmissionPage(User user) { 
        this.currentUser = user;
        this.jobForms = new ArrayList<>();
        setTitle("Client Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

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

        JPanel contentArea = new JPanel(new GridBagLayout());
        contentArea.setBackground(new Color(238, 238, 238));

        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 15, 60));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setMinimumSize(new Dimension(900, 650));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 50, 15, 50);
        contentArea.add(mainPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Submit A Job");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel subtitleLabel = new JLabel("Enter job details to submit a new job request");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
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
        formsContainer = new JPanel();
        formsContainer.setLayout(new BoxLayout(formsContainer, BoxLayout.Y_AXIS));
        formsContainer.setBackground(Color.WHITE);
        formsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(formsContainer);
        // Add "Job 1" label for the first form
        JLabel job1Label = new JLabel("Job 1");
        job1Label.setFont(new Font("Georgia", Font.BOLD, 28));
        job1Label.setForeground(new Color(0, 124, 137));
        
        JPanel job1LabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        job1LabelPanel.setBackground(Color.WHITE);
        job1LabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        job1LabelPanel.add(job1Label);
        job1LabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, job1Label.getPreferredSize().height));
        formsContainer.add(job1LabelPanel);
        formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        addJobForm();

        submitButton = new GradientButton("Submit All Jobs");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setForeground(Color.WHITE);
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> submitAllJobs());
        mainPanel.add(submitButton);
    }

    private void rebuildFormsContainer() {
        formsContainer.removeAll();
        // Re-add Job 1 label
        JLabel job1Label = new JLabel("Job 1");
        job1Label.setFont(new Font("Georgia", Font.BOLD, 28));
        job1Label.setForeground(new Color(0, 124, 137));
        
        JPanel job1LabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        job1LabelPanel.setBackground(Color.WHITE);
        job1LabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        job1LabelPanel.add(job1Label);
        job1LabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, job1Label.getPreferredSize().height));
        formsContainer.add(job1LabelPanel);
        formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Re-add all remaining forms with updated job numbers
        for (int i = 0; i < jobForms.size(); i++) {
            if (i > 0) {
                formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
                JSeparator sep = new JSeparator();
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                sep.setForeground(new Color(220, 220, 220));
                formsContainer.add(sep);
                formsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
                JLabel jLabel = new JLabel("Job " + (i + 1));
                jLabel.setFont(new Font("Georgia", Font.BOLD, 28));
                jLabel.setForeground(new Color(0, 124, 137));
                JPanel jLabelPanel = new JPanel(new BorderLayout());
                jLabelPanel.setBackground(Color.WHITE);
                jLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                jLabelPanel.add(jLabel, BorderLayout.WEST);
                // Add remove button for jobs 2 and above
                JLabel remBtn = new JLabel("−");
                remBtn.setFont(new Font("Arial", Font.PLAIN, 24));
                remBtn.setForeground(new Color(0, 124, 137));
                remBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                final int formIndex = i;
                remBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jobForms.remove(formIndex);
                        rebuildFormsContainer();
                        updateMainPanelHeight();
                    }
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        remBtn.setForeground(new Color(220, 53, 69));
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        remBtn.setForeground(new Color(0, 124, 137));
                    }
                });
                jLabelPanel.add(remBtn, BorderLayout.EAST);
                jLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, jLabel.getPreferredSize().height));
                formsContainer.add(jLabelPanel);
                formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
            
            JobFormPanel currentForm = jobForms.get(i);
            // Re-add the form itself
            formsContainer.add(currentForm);
            // Add the "+ Add Job" button only after the last form
            if (i == jobForms.size() - 1) {
                JPanel addJobButtonPanel = new JPanel();
                addJobButtonPanel.setLayout(new BoxLayout(addJobButtonPanel, BoxLayout.X_AXIS));
                addJobButtonPanel.setBackground(Color.WHITE);
                addJobButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                addJobButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                JButton addJobButton = new JButton("+ Add Job");
                addJobButton.setFont(new Font("Arial", Font.BOLD, 14));
                addJobButton.setForeground(new Color(0, 124, 137));
                addJobButton.setBackground(Color.WHITE);
                addJobButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
                addJobButton.setFocusPainted(false);
                addJobButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                addJobButton.addActionListener(evt -> addJobForm());
                
                addJobButtonPanel.add(Box.createHorizontalGlue());
                addJobButtonPanel.add(addJobButton);
                
                formsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                formsContainer.add(addJobButtonPanel);
            }
        }
        
        jobCounter = jobForms.size() + 1;
        formsContainer.revalidate();
        formsContainer.repaint();
    }

    private void updateMainPanelHeight() {
        int topBottomUIsHeight = 225;
        int firstFormHeight = 380;
        int subsequentFormHeight = 480;

        int newHeight;
        if (jobForms.size() == 1) {
            newHeight = topBottomUIsHeight + firstFormHeight;
        } else {
            newHeight = topBottomUIsHeight + firstFormHeight + ((jobForms.size() - 1) * subsequentFormHeight);
        }

        mainPanel.setPreferredSize(new Dimension(900, newHeight));
        mainPanel.setMaximumSize(new Dimension(900, newHeight));
        mainPanel.revalidate();
    }

    private void addJobForm() {
        if (jobCounter > 1) {
            // Hide the add button on the previous form
            if (!jobForms.isEmpty()) {
                JobFormPanel lastForm = jobForms.get(jobForms.size() - 1);
                if (lastForm.addButtonPanel != null) {
                    lastForm.addButtonPanel.setVisible(false);
                }
            }
            
            formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            separator.setForeground(new Color(220, 220, 220));
            formsContainer.add(separator);
            formsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
            JLabel jobLabel = new JLabel("Job " + jobCounter);
            jobLabel.setFont(new Font("Georgia", Font.BOLD, 28));
            jobLabel.setForeground(new Color(0, 124, 137));
            JPanel jobLabelPanel = new JPanel(new BorderLayout());
            jobLabelPanel.setBackground(Color.WHITE);
            jobLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            jobLabelPanel.add(jobLabel, BorderLayout.WEST);
            // Add remove button for Job 2 and above
            JLabel removeButton = new JLabel("−");
            removeButton.setFont(new Font("Arial", Font.PLAIN, 24));
            removeButton.setForeground(new Color(0, 124, 137));
            removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            final int formIndexToRemove = jobForms.size();
            removeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (jobForms.size() > formIndexToRemove) {
                        jobForms.remove(formIndexToRemove);
                    }
                    rebuildFormsContainer();
                    updateMainPanelHeight();
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    removeButton.setForeground(new Color(220, 53, 69));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    removeButton.setForeground(new Color(0, 124, 137));
                }
            });
            jobLabelPanel.add(removeButton, BorderLayout.EAST);
            jobLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, jobLabel.getPreferredSize().height));
            formsContainer.add(jobLabelPanel);

            formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        JobFormPanel jobForm = new JobFormPanel();
        jobForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        jobForms.add(jobForm);
        formsContainer.add(jobForm);
        
        jobCounter++;
        
        // Fixed height calculation
        int topBottomUIsHeight = 225;
        int firstFormHeight = 380;
        int subsequentFormHeight = 480;

        int newHeight;
        if (jobForms.size() == 1) {
            newHeight = topBottomUIsHeight + firstFormHeight;
        } else {
            newHeight = topBottomUIsHeight + firstFormHeight + ((jobForms.size() - 1) * subsequentFormHeight);
        }

        mainPanel.setPreferredSize(new Dimension(900, newHeight));
        mainPanel.setMaximumSize(new Dimension(900, newHeight));
        mainPanel.revalidate();
        SwingUtilities.invokeLater(() -> {
            jobForm.scrollRectToVisible(jobForm.getBounds());
        });
    }

    private boolean validateAllForms() {
        boolean allValid = true;
        for (JobFormPanel form : jobForms) {
            if (!form.validateForm()) {
                allValid = false;
            }
        }
        return allValid;
    }

    private void submitAllJobs() {
        if (!validateAllForms()) {
            return;
        }

        int successCount = 0;
        for (JobFormPanel form : jobForms) {
            String jobType = form.getJobType();
            String duration = form.getDuration();
            String deadline = form.getDeadline();
            String description = form.getDescription();
            Job job = new Job(
                currentUser.getId(), 
                jobType,
                Integer.parseInt(duration.trim()),
                deadline.trim(),
                description.trim()
            );
            if (dataManager.addJob(job)) {
                successCount++;
            }
        }

        if (successCount == jobForms.size()) {
            CustomDialog dialog = new CustomDialog(this, "Success", 
                successCount + " job(s) submitted successfully!");
            dialog.setVisible(true);
            clearAllForms();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Some jobs could not be saved. " + successCount + " of " + jobForms.size() + " succeeded.", 
                "Partial Success", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearAllForms() {
        formsContainer.removeAll();
        jobForms.clear();
        jobCounter = 1;
        addJobForm();
        formsContainer.revalidate();
        formsContainer.repaint();
    }

    private void showCalendar(JTextField targetField, JLabel errorLabel) {
        CalendarDialog calendarDialog = new CalendarDialog(this, true, null);
        calendarDialog.setLocationRelativeTo(targetField);
        calendarDialog.setVisible(true);
        if (calendarDialog.getSelectedDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetField.setText(dateFormat.format(calendarDialog.getSelectedDate()));
            targetField.setBorder(defaultBorder);
            errorLabel.setText(" ");
        }
    }

    private class JobFormPanel extends JPanel {
        private JComboBox<String> jobTypeCombo;
        private PlaceholderTextField durationField;
        private PlaceholderTextField deadlineField;
        private PlaceholderTextArea descriptionArea;
        private JLabel durationErrorLabel, deadlineErrorLabel, descriptionErrorLabel;
        private JScrollPane descScrollPane;
        private JPanel addButtonPanel;

        public JobFormPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 370));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            
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

            JLabel jobTypeLabel = new JLabel("<html>Job Type: <font color='red'>*</font></html>");
            jobTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JPanel jobTypeLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            jobTypeLabelPanel.setBackground(Color.WHITE);
            jobTypeLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            jobTypeLabelPanel.add(jobTypeLabel);
            jobTypeLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, jobTypeLabel.getPreferredSize().height));
            add(jobTypeLabelPanel);

            add(Box.createRigidArea(new Dimension(0, 3)));
            String[] jobTypes = {
                "Data Storage & Transfer", "Computational Task", "Simulation",
                "Networking & Communication", "Real-Time Processing", "Batch Processing"
            };
            jobTypeCombo = new JComboBox<>(jobTypes);
            jobTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
            jobTypeCombo.setBackground(Color.WHITE);
            jobTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, jobTypeCombo.getPreferredSize().height));
            jobTypeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(jobTypeCombo);
            add(Box.createRigidArea(new Dimension(0, 6)));

            JLabel durationLabel = new JLabel("<html>Duration (Hours): <font color='red'>*</font></html>");
            durationLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JPanel durationLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            durationLabelPanel.setBackground(Color.WHITE);
            durationLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            durationLabelPanel.add(durationLabel);
            durationLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, durationLabel.getPreferredSize().height));
            add(durationLabelPanel);

            add(Box.createRigidArea(new Dimension(0, 3)));
            
            durationField = new PlaceholderTextField("Enter number of hours");
            durationField.setFont(new Font("Arial", Font.PLAIN, 14));
            durationField.setBorder(defaultBorder);
            durationField.setMaximumSize(new Dimension(Integer.MAX_VALUE, durationField.getPreferredSize().height));
            durationField.setAlignmentX(Component.CENTER_ALIGNMENT);
            durationField.addFocusListener(highlightListener);
            add(durationField);
            
            durationErrorLabel = new JLabel(" ");
            durationErrorLabel.setForeground(Color.RED);
            durationErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            durationErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(durationErrorLabel);
            add(Box.createRigidArea(new Dimension(0, 6)));

            JLabel deadlineLabel = new JLabel("<html>Deadline: <font color='red'>*</font></html>");
            deadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JPanel deadlineLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            deadlineLabelPanel.setBackground(Color.WHITE);
            deadlineLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            deadlineLabelPanel.add(deadlineLabel);
            deadlineLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, deadlineLabel.getPreferredSize().height));
            add(deadlineLabelPanel);

            add(Box.createRigidArea(new Dimension(0, 3)));
            deadlineField = new PlaceholderTextField("Select a date");
            deadlineField.setFont(new Font("Arial", Font.PLAIN, 14));
            deadlineField.setBorder(defaultBorder);
            deadlineField.setMaximumSize(new Dimension(Integer.MAX_VALUE, deadlineField.getPreferredSize().height));
            deadlineField.setAlignmentX(Component.CENTER_ALIGNMENT);
            deadlineField.setEditable(false);
            deadlineField.setBackground(Color.WHITE);
            deadlineField.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deadlineField.setFocusable(false);
            deadlineField.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showCalendar(deadlineField, deadlineErrorLabel);
                }
            });
            add(deadlineField);
            
            deadlineErrorLabel = new JLabel(" ");
            deadlineErrorLabel.setForeground(Color.RED);
            deadlineErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            deadlineErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(deadlineErrorLabel);
            add(Box.createRigidArea(new Dimension(0, 6)));

            JLabel descriptionLabel = new JLabel("Job Description:");
            descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JPanel descriptionLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            descriptionLabelPanel.setBackground(Color.WHITE);
            descriptionLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            descriptionLabelPanel.add(descriptionLabel);
            descriptionLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, descriptionLabel.getPreferredSize().height));
            add(descriptionLabelPanel);

            add(Box.createRigidArea(new Dimension(0, 3)));
            
            descriptionArea = new PlaceholderTextArea("Enter a description", 5, 20);
            descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);

            FocusAdapter scrollPaneHighlightListener = new FocusAdapter() {
                private void updateBorder(FocusEvent e, boolean hasFocus) {
                    if (descScrollPane != null && !descScrollPane.getBorder().equals(errorBorder)) {
                        descScrollPane.setBorder(hasFocus ? focusBorder : defaultBorder);
                    }
                }
                @Override public void focusGained(FocusEvent e) { updateBorder(e, true); }
                @Override public void focusLost(FocusEvent e) { updateBorder(e, false); }
            };
            descriptionArea.addFocusListener(scrollPaneHighlightListener);
            descriptionArea.setBorder(null);
            
            descScrollPane = new JScrollPane(descriptionArea);
            descScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            descScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            descScrollPane.setBorder(defaultBorder);
            descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(descScrollPane);
            
            descriptionErrorLabel = new JLabel(" ");
            descriptionErrorLabel.setForeground(Color.RED);
            descriptionErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            descriptionErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(descriptionErrorLabel);
            add(Box.createRigidArea(new Dimension(0, 8)));  // Reduced from 10
            
            addButtonPanel = new JPanel();
            addButtonPanel.setLayout(new BoxLayout(addButtonPanel, BoxLayout.X_AXIS));
            addButtonPanel.setBackground(Color.WHITE);
            addButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            addButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JButton localAddButton = new JButton("+ Add Job");
            localAddButton.setFont(new Font("Arial", Font.BOLD, 14));
            localAddButton.setForeground(new Color(0, 124, 137));
            localAddButton.setBackground(Color.WHITE);
            localAddButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); 
            localAddButton.setFocusPainted(false);
            localAddButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Store reference to this button's panel
            final JPanel buttonPanel = addButtonPanel;
            localAddButton.addActionListener(evt -> {
                buttonPanel.setVisible(false);
                addJobForm();
            });
            addButtonPanel.add(Box.createHorizontalGlue());
            addButtonPanel.add(localAddButton);
            add(addButtonPanel);
        }

        public boolean validateForm() {
            boolean isValid = true;
            durationField.setBorder(defaultBorder);
            deadlineField.setBorder(defaultBorder);
            descScrollPane.setBorder(defaultBorder);
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

        public void clearForm() {
            jobTypeCombo.setSelectedIndex(0);
            durationField.setText("");
            deadlineField.setText("");
            descriptionArea.setText("");
            durationField.setBorder(defaultBorder);
            deadlineField.setBorder(defaultBorder);
            descScrollPane.setBorder(defaultBorder);
            durationErrorLabel.setText(" ");
            deadlineErrorLabel.setText(" ");
            descriptionErrorLabel.setText(" ");
        }

        public String getJobType() { return (String) jobTypeCombo.getSelectedItem(); }
        public String getDuration() { return durationField.getText(); }
        public String getDeadline() { return deadlineField.getText(); }
        public String getDescription() { return descriptionArea.getText(); }
    }

    private static class PlaceholderTextField extends JTextField {
        private String placeholder;
        public PlaceholderTextField(String placeholder) { this.placeholder = placeholder; }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (placeholder == null || placeholder.isEmpty() || !getText().isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(150, 150, 150));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(placeholder, getInsets().left + 5, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
        }
    }
    
    private static class PlaceholderTextArea extends JTextArea {
        private String placeholder;
        public PlaceholderTextArea(String placeholder, int rows, int columns) {
            super(rows, columns);
            this.placeholder = placeholder;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (placeholder == null || placeholder.isEmpty() || !getText().isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(150, 150, 150));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(placeholder, getInsets().left + 5, getInsets().top + fm.getAscent());
        }
    }

    public static void main(String[] args) {
        User testUser = new User(999, "Test", "User", "test@example.com", "testuser", "1234567890", "hash", "Client", "timestamp", true);
        SwingUtilities.invokeLater(() -> {
            JobSubmissionPage dashboard = new JobSubmissionPage(testUser);
            dashboard.setVisible(true);
        });
    }
}