package ui;

import data.VehicleDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.Year;
import javax.swing.*;
import javax.swing.border.Border;
import model.Vehicle;
import validation.VehicleValidator;

public class OwnerDashboard extends JFrame {
    private PlaceholderTextField vehicleMakeField;
    private PlaceholderTextField vehicleModelField;
    private PlaceholderTextField vehicleYearField;
    private JComboBox<String> computingPowerCombo;
    private PlaceholderTextField residencyStartField;
    private PlaceholderTextField residencyEndField;

    private final VehicleValidator validator = new VehicleValidator();
    private final VehicleDataManager dataManager = new VehicleDataManager();

    private JLabel vehicleMakeErrorLabel, vehicleModelErrorLabel, vehicleYearErrorLabel, residencyStartErrorLabel, residencyEndErrorLabel;
    private Border defaultBorder, focusBorder, errorBorder;

    public OwnerDashboard() {
        setTitle("Owner Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // Header Panel
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

        // MAIN CONTENT AREA
        JPanel contentArea = new JPanel(new GridBagLayout());
        contentArea.setBackground(new Color(238, 238, 238));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        contentArea.add(mainPanel, gbc);
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(contentArea, BorderLayout.CENTER);

        // FORM COMPONENTS
        JLabel titleLabel = new JLabel("Submit Vehicle Availability");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel subtitleLabel = new JLabel("Register your vehicle");
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

        // UI Component Initialization
        JPanel firstRowPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        firstRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        firstRowPanel.setBackground(Color.WHITE);
        vehicleMakeField = new PlaceholderTextField("Enter make");
        vehicleModelField = new PlaceholderTextField("Enter model");
        vehicleYearField = new PlaceholderTextField("Enter year");
        vehicleMakeErrorLabel = new JLabel(" ");
        vehicleModelErrorLabel = new JLabel(" ");
        vehicleYearErrorLabel = new JLabel(" ");
        firstRowPanel.add(createColumn("Vehicle Make:", vehicleMakeField, vehicleMakeErrorLabel, highlightListener));
        firstRowPanel.add(createColumn("Vehicle Model:", vehicleModelField, vehicleModelErrorLabel, highlightListener));
        firstRowPanel.add(createColumn("Vehicle Year:", vehicleYearField, vehicleYearErrorLabel, highlightListener));
        mainPanel.add(firstRowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel powerLabel = new JLabel("Computing Power Level:");
        powerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        powerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(powerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        String[] powerLevels = { "Low", "Medium", "High" };
        computingPowerCombo = new JComboBox<>(powerLevels);
        computingPowerCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        computingPowerCombo.setBackground(Color.WHITE);
        computingPowerCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(computingPowerCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel dateRow = new JPanel(new GridLayout(1, 2, 20, 0));
        dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        dateRow.setBackground(Color.WHITE);
        residencyStartField = new PlaceholderTextField("yyyy-MM-dd");
        residencyEndField = new PlaceholderTextField("yyyy-MM-dd");
        residencyStartErrorLabel = new JLabel(" ");
        residencyEndErrorLabel = new JLabel(" ");
        dateRow.add(createColumn("Residency Start Date:", residencyStartField, residencyStartErrorLabel, highlightListener));
        dateRow.add(createColumn("Residency End Date:", residencyEndField, residencyEndErrorLabel, highlightListener));
        mainPanel.add(dateRow);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton submitButton = new JButton("Submit Vehicle");
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
        submitButton.addActionListener(e -> submitVehicle());
        mainPanel.add(submitButton);
    }

    private JPanel createColumn(String labelText, JTextField field, JLabel errorLabel, FocusAdapter highlightListener) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(defaultBorder);
        field.addFocusListener(highlightListener);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        errorPanel.add(errorLabel);
        column.add(label);
        column.add(Box.createRigidArea(new Dimension(0, 8)));
        column.add(field);
        column.add(errorPanel);
        return column;
    }

    private boolean validateForm() {
        boolean isValid = true;
        String make = vehicleMakeField.getText();
        String model = vehicleModelField.getText();
        String year = vehicleYearField.getText();
        String startDate = residencyStartField.getText();
        String endDate = residencyEndField.getText();

        // Reset UI state
        vehicleMakeField.setBorder(defaultBorder);
        vehicleModelField.setBorder(defaultBorder);
        vehicleYearField.setBorder(defaultBorder);
        residencyStartField.setBorder(defaultBorder);
        residencyEndField.setBorder(defaultBorder);
        vehicleMakeErrorLabel.setText(" ");
        vehicleModelErrorLabel.setText(" ");
        vehicleYearErrorLabel.setText(" ");
        residencyStartErrorLabel.setText(" ");
        residencyEndErrorLabel.setText(" ");

        if (!validator.isFieldValid(make)) {
            vehicleMakeErrorLabel.setText("Vehicle make is required.");
            vehicleMakeField.setBorder(errorBorder);
            isValid = false;
        }

        if (!validator.isFieldValid(model)) {
            vehicleModelErrorLabel.setText("Vehicle model is required.");
            vehicleModelField.setBorder(errorBorder);
            isValid = false;
        }
        
        if (!validator.isYearValid(year)) {
            int maxYear = Year.now().getValue() + 5;
            vehicleYearErrorLabel.setText("Please enter a valid year (1900-" + maxYear + ").");
            vehicleYearField.setBorder(errorBorder);
            isValid = false;
        }
        
        boolean isStartDateValid = true;
        if (!validator.isDateFormatValid(startDate)) {
            residencyStartErrorLabel.setText("Invalid date format (yyyy-mm-dd).");
            residencyStartField.setBorder(errorBorder);
            isValid = false;
            isStartDateValid = false;
        }

        boolean isEndDateValid = true;
        if (!validator.isDateFormatValid(endDate)) {
            residencyEndErrorLabel.setText("Invalid date format (yyyy-mm-dd).");
            residencyEndField.setBorder(errorBorder);
            isValid = false;
            isEndDateValid = false;
        }

        if (isStartDateValid && isEndDateValid && !validator.isDateRangeValid(startDate, endDate)) {
            residencyEndErrorLabel.setText("End date cannot be before the start date.");
            residencyEndField.setBorder(errorBorder);
            isValid = false;
        }

        return isValid;
    }

    private void submitVehicle() {
        if (!validateForm()) {
            return;
        }

        Vehicle vehicle = new Vehicle(
                vehicleMakeField.getText().trim(),
                vehicleModelField.getText().trim(),
                Integer.parseInt(vehicleYearField.getText().trim()),
                (String) computingPowerCombo.getSelectedItem(),
                residencyStartField.getText().trim(),
                residencyEndField.getText().trim()
        );

        if (dataManager.addVehicle(vehicle)) {
            JOptionPane.showMessageDialog(this, "Vehicle availability submitted successfully!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error writing to file. Please check console.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        vehicleMakeField.setText("");
        vehicleModelField.setText("");
        vehicleYearField.setText("");
        computingPowerCombo.setSelectedIndex(0);
        residencyStartField.setText("");
        residencyEndField.setText("");

        vehicleMakeField.setBorder(defaultBorder);
        vehicleModelField.setBorder(defaultBorder);
        vehicleYearField.setBorder(defaultBorder);
        residencyStartField.setBorder(defaultBorder);
        residencyEndField.setBorder(defaultBorder);

        vehicleMakeErrorLabel.setText(" ");
        vehicleModelErrorLabel.setText(" ");
        vehicleYearErrorLabel.setText(" ");
        residencyStartErrorLabel.setText(" ");
        residencyEndErrorLabel.setText(" ");
        
        repaint();
    }

    private static class PlaceholderTextField extends JTextField {
        private String placeholder;
        public PlaceholderTextField(String placeholder) { this.placeholder = placeholder; }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (placeholder == null || placeholder.isEmpty() || !getText().isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(150, 150, 150));
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, getInsets().left + 5, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OwnerDashboard dashboard = new OwnerDashboard();
            dashboard.setVisible(true);
        });
    }
}
