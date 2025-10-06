package ui;

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

        // --- Job Type Row ---
        JPanel jobTypeRow = new JPanel(new BorderLayout(10, 0));
        jobTypeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        jobTypeRow.setBackground(Color.WHITE);

        JLabel jobTypeLabel = new JLabel("Job Type:");
        jobTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jobTypeLabel.setPreferredSize(new Dimension(120, 20));
        jobTypeRow.add(jobTypeLabel, BorderLayout.WEST);

        String[] jobTypes = {
            "Data Storage & Transfer",
            "Computational Task",
            "Simulation",
            "Networking & Communication",
            "Real-Time Processing",
            "Batch Processing"
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

        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        durationLabel.setPreferredSize(new Dimension(120, 20));
        durationRow.add(durationLabel, BorderLayout.WEST);

        durationField = new JTextField();
        durationField.setFont(new Font("Arial", Font.PLAIN, 14));
        durationField.setBorder(defaultBorder);
        durationField.setPreferredSize(new Dimension(250, 20));
        durationField.addFocusListener(highlightListener);
        durationRow.add(durationField, BorderLayout.CENTER);

        mainPanel.add(durationRow);
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

        mainPanel.add(deadlineRow);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Job Description Row ---
        JPanel descriptionRow = new JPanel(new BorderLayout(10, 0));
        descriptionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        descriptionRow.setBackground(Color.WHITE);

        JLabel descriptionLabel = new JLabel("Job Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabel.setPreferredSize(new Dimension(120, 20));
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP); // top-left align
        descriptionRow.add(descriptionLabel, BorderLayout.WEST);

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.addFocusListener(highlightListener);

        // Only scroll pane gets border (prevents double border)
        descriptionArea.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(400, 80));
        scrollPane.setBorder(defaultBorder);

        descriptionRow.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(descriptionRow);
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
        durationField.setText("");
        deadlineField.setText("");
        descriptionArea.setText("");
    }
}
