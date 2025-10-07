package ui;

import data.VehicleDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import model.User;
import model.Vehicle;
import validation.VehicleValidator;

public class OwnerDashboard extends JFrame {
    private PlaceholderTextField vehicleMakeField;
    private PlaceholderTextField vehicleModelField;
    private PlaceholderTextField vehicleYearField;
    private PlaceholderTextField licensePlateField;
    private PlaceholderTextField vinNumberField;
    private JComboBox<String> computingPowerCombo;
    private JTextField residencyStartField;
    private JTextField residencyEndField;

    private final VehicleValidator validator = new VehicleValidator();
    private final VehicleDataManager dataManager = new VehicleDataManager();
    private User currentUser;

    private JLabel vehicleMakeErrorLabel, vehicleModelErrorLabel, vehicleYearErrorLabel, licensePlateErrorLabel, vinNumberErrorLabel, residencyStartErrorLabel, residencyEndErrorLabel;
    private Border defaultBorder, focusBorder, errorBorder;

    public OwnerDashboard(User user) {
        this.currentUser = user;
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
        contentArea.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(900, 650));
        mainPanel.setMaximumSize(new Dimension(900, 650));
        mainPanel.setMinimumSize(new Dimension(900, 650));
        GridBagConstraints gbc = new GridBagConstraints();
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
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
        );
        focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 124, 137), 2),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
        );
        errorBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
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
        firstRowPanel.add(createColumn("<html>Vehicle Make: <font color='red'>*</font></html>", vehicleMakeField, vehicleMakeErrorLabel, highlightListener));
        firstRowPanel.add(createColumn("<html>Vehicle Model: <font color='red'>*</font></html>", vehicleModelField, vehicleModelErrorLabel, highlightListener));
        firstRowPanel.add(createColumn("<html>Vehicle Year: <font color='red'>*</font></html>", vehicleYearField, vehicleYearErrorLabel, highlightListener));
        mainPanel.add(firstRowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel powerPanel = new JPanel(new BorderLayout());
        powerPanel.setBackground(Color.WHITE);
        powerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JPanel powerInnerPanel = new JPanel();
        powerInnerPanel.setLayout(new BoxLayout(powerInnerPanel, BoxLayout.Y_AXIS));
        powerInnerPanel.setBackground(Color.WHITE);
        
        JPanel powerLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        powerLabelPanel.setBackground(Color.WHITE);

        JLabel powerLabel = new JLabel("<html>Computing Power Level: <font color='red'>*</font></html>");
        powerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        powerLabelPanel.add(powerLabel);
        powerInnerPanel.add(powerLabelPanel);
        powerInnerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        String[] powerLevels = { "Low", "Medium", "High" };
        computingPowerCombo = new JComboBox<>(powerLevels);
        computingPowerCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        computingPowerCombo.setBackground(Color.WHITE);
        powerInnerPanel.add(computingPowerCombo);
        
        powerPanel.add(powerInnerPanel, BorderLayout.CENTER);
        mainPanel.add(powerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Second Row - License Plate and VIN Number
        JPanel secondRowPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        secondRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        secondRowPanel.setBackground(Color.WHITE);
        licensePlateField = new PlaceholderTextField("Enter license plate");
        vinNumberField = new PlaceholderTextField("Enter VIN number");
        licensePlateErrorLabel = new JLabel(" ");
        vinNumberErrorLabel = new JLabel(" ");
        secondRowPanel.add(createColumn("<html>License Plate: <font color='red'>*</font></html>", licensePlateField, licensePlateErrorLabel, highlightListener));
        secondRowPanel.add(createColumn("<html>VIN Number: <font color='red'>*</font></html>", vinNumberField, vinNumberErrorLabel, highlightListener));
        mainPanel.add(secondRowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel dateRow = new JPanel(new GridLayout(1, 2, 20, 0));
        dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        dateRow.setBackground(Color.WHITE);
        
        residencyStartField = new JTextField();
        residencyStartField.setEditable(false);
        residencyStartField.setBackground(Color.WHITE);
        residencyStartField.setCursor(new Cursor(Cursor.HAND_CURSOR));
        residencyStartField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showCalendar(residencyStartField, true, null);
            }
        });

        residencyEndField = new JTextField();
        residencyEndField.setEditable(false);
        residencyEndField.setBackground(Color.WHITE);
        residencyEndField.setCursor(new Cursor(Cursor.HAND_CURSOR));
        residencyEndField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Date minEndDate = null;
                if (!residencyStartField.getText().isEmpty()) {
                    try {
                        minEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(residencyStartField.getText());
                    } catch (ParseException ex) {
                        System.err.println("Error parsing start date: " + ex.getMessage());
                    }
                } else {
                    minEndDate = new Date();
                }
                showCalendar(residencyEndField, false, minEndDate);
            }
        });
        
        residencyStartErrorLabel = new JLabel(" ");
        residencyEndErrorLabel = new JLabel(" ");
        dateRow.add(createColumn("<html>Residency Start Date: <font color='red'>*</font></html>", residencyStartField, residencyStartErrorLabel, highlightListener));
        dateRow.add(createColumn("<html>Residency End Date: <font color='red'>*</font></html>", residencyEndField, residencyEndErrorLabel, highlightListener));
        mainPanel.add(dateRow);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        GradientButton submitButton = new GradientButton("Submit Vehicle");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setForeground(Color.WHITE);
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        submitButton.setPreferredSize(new Dimension(800, 38));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> submitVehicle());
        mainPanel.add(submitButton);
    }

    private JPanel createColumn(String labelText, JTextField field, JLabel errorLabel, FocusAdapter highlightListener) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(Color.WHITE);
        
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        labelPanel.add(label);
        
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(defaultBorder);
        field.addFocusListener(highlightListener);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        errorPanel.add(errorLabel);
        
        column.add(labelPanel);
        column.add(Box.createRigidArea(new Dimension(0, 8)));
        column.add(field);
        column.add(errorPanel);
        return column;
    }
    
    private void showCalendar(JTextField targetField, boolean restrictToFuture, Date minSelectableDate) {
        CalendarDialog calendarDialog = new CalendarDialog(this, restrictToFuture, minSelectableDate);
        calendarDialog.setLocationRelativeTo(targetField);
        calendarDialog.setVisible(true);
        if (calendarDialog.getSelectedDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetField.setText(dateFormat.format(calendarDialog.getSelectedDate()));
            targetField.setBorder(defaultBorder);
            if (targetField == residencyStartField) {
                residencyStartErrorLabel.setText(" ");
            } else if (targetField == residencyEndField) {
                residencyEndErrorLabel.setText(" ");
            }
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        String make = vehicleMakeField.getText();
        String model = vehicleModelField.getText();
        String year = vehicleYearField.getText();
        String licensePlate = licensePlateField.getText();
        String vin = vinNumberField.getText();
        String startDate = residencyStartField.getText();
        String endDate = residencyEndField.getText();

        vehicleMakeField.setBorder(defaultBorder);
        vehicleModelField.setBorder(defaultBorder);
        vehicleYearField.setBorder(defaultBorder);
        licensePlateField.setBorder(defaultBorder);
        vinNumberField.setBorder(defaultBorder);
        residencyStartField.setBorder(defaultBorder);
        residencyEndField.setBorder(defaultBorder);
        
        vehicleMakeErrorLabel.setText(" ");
        vehicleModelErrorLabel.setText(" ");
        vehicleYearErrorLabel.setText(" ");
        licensePlateErrorLabel.setText(" ");
        vinNumberErrorLabel.setText(" ");
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

        if (!validator.isFieldValid(licensePlate)) {
            licensePlateErrorLabel.setText("License plate is required.");
            licensePlateField.setBorder(errorBorder);
            isValid = false;
        } else if (dataManager.isLicensePlateTaken(licensePlate)) {
            licensePlateErrorLabel.setText("This license plate is already registered.");
            licensePlateField.setBorder(errorBorder);
            isValid = false;
        }


        if (!validator.isFieldValid(vin)) {
            vinNumberErrorLabel.setText("VIN number is required.");
            vinNumberField.setBorder(errorBorder);
            isValid = false;
        } else if (dataManager.isVinTaken(vin)) {
            vinNumberErrorLabel.setText("This VIN is already registered.");
            vinNumberField.setBorder(errorBorder);
            isValid = false;
        }
        
        if (startDate.isEmpty()) {
            residencyStartErrorLabel.setText("Start date is required.");
            residencyStartField.setBorder(errorBorder);
            isValid = false;
        } else if (!validator.isDateInFuture(startDate)) {
             residencyStartErrorLabel.setText("Start date cannot be in the past.");
             residencyStartField.setBorder(errorBorder);
             isValid = false;
        }

        if (endDate.isEmpty()) {
            residencyEndErrorLabel.setText("End date is required.");
            residencyEndField.setBorder(errorBorder);
            isValid = false;
        } else if (!startDate.isEmpty() && !validator.isDateRangeValid(startDate, endDate)) {
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
                currentUser.getUserId(),
                vehicleMakeField.getText().trim(),
                vehicleModelField.getText().trim(),
                Integer.parseInt(vehicleYearField.getText().trim()),
                vinNumberField.getText().trim(),
                licensePlateField.getText().trim(),
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
        licensePlateField.setText("");
        vinNumberField.setText("");
        computingPowerCombo.setSelectedIndex(0);
        residencyStartField.setText("");
        residencyEndField.setText("");

        vehicleMakeField.setBorder(defaultBorder);
        vehicleModelField.setBorder(defaultBorder);
        vehicleYearField.setBorder(defaultBorder);
        licensePlateField.setBorder(defaultBorder);
        vinNumberField.setBorder(defaultBorder);
        residencyStartField.setBorder(defaultBorder);
        residencyEndField.setBorder(defaultBorder);

        vehicleMakeErrorLabel.setText(" ");
        vehicleModelErrorLabel.setText(" ");
        vehicleYearErrorLabel.setText(" ");
        licensePlateErrorLabel.setText(" ");
        vinNumberErrorLabel.setText(" ");
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
        User testUser = new User(998, "Owner", "Test", "owner@test.com", "ownertest", "555-5555", "hash", "Owner", "timestamp");
        SwingUtilities.invokeLater(() -> {
            OwnerDashboard dashboard = new OwnerDashboard(testUser);
            dashboard.setVisible(true);
        });
    }
}
