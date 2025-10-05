import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class OwnerDashboard extends JFrame {
    private JTextField vehicleMakeField;
    private JTextField vehicleModelField;
    private JTextField vehicleYearField;
    private JComboBox<String> computingPowerCombo;
    private JTextField residencyStartField;
    private JTextField residencyEndField;
    private DefaultTableModel tableModel;
    private int entryCounter = 1;

    public OwnerDashboard() {
        setTitle("Owner Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        //------------------------------------------------------------------------------------
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

        // ------------------------------------------------------------------------------------
        //  MAIN CONTENT AREA 
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

        // ------------------------------------------------
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

        // ------------------------------------------------------------------------------------------------
        // Asks for Make, Model, Year etc Input
        JPanel firstRowPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        firstRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        firstRowPanel.setBackground(Color.WHITE);

        vehicleMakeField = new JTextField();
        vehicleModelField = new JTextField();
        vehicleYearField = new JTextField();

        firstRowPanel.add(makeColumn("Vehicle Make:", vehicleMakeField, defaultBorder, highlightListener));
        firstRowPanel.add(makeColumn("Vehicle Model:", vehicleModelField, defaultBorder, highlightListener));
        firstRowPanel.add(makeColumn("Vehicle Year:", vehicleYearField, defaultBorder, highlightListener));
        mainPanel.add(firstRowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		// -------------------------------------------------------------------------------------------------
		// Drop down box for Computing Power Level (Revise options)
        JLabel powerLabel = new JLabel("Computing Power Level:");
        powerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        powerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(powerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        String[] powerLevels = {
            "Low",
            "Medium",
            "High",
        };
        computingPowerCombo = new JComboBox<>(powerLevels);
        computingPowerCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        computingPowerCombo.setBackground(Color.WHITE);
        computingPowerCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(computingPowerCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ----------------------------------------------------------------------------------------------------
        // Residency Start Date and End Date
        JPanel dateRow = new JPanel(new GridLayout(1, 2, 20, 0));
        dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        dateRow.setBackground(Color.WHITE);
        residencyStartField = new JTextField();
        residencyEndField = new JTextField();
        
        // ----------------------------------------------------------------------------------------------------
        // Hint for Date (Optional)
        JPanel startColumn = new JPanel();
        startColumn.setLayout(new BoxLayout(startColumn, BoxLayout.Y_AXIS));
        startColumn.setBackground(Color.WHITE);

        JLabel startLabel = new JLabel("Residency Start Date:");
        startLabel.setFont(new Font("Arial", Font.BOLD, 14));
        startLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        startColumn.add(startLabel);
        startColumn.add(Box.createRigidArea(new Dimension(0, 8)));

        residencyStartField.setFont(new Font("Arial", Font.PLAIN, 14));
        residencyStartField.setBorder(defaultBorder);
        residencyStartField.addFocusListener(highlightListener);
        residencyStartField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        startColumn.add(residencyStartField);

        JLabel hint = new JLabel("Date format: yyyy-MM-dd");
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setForeground(new Color(100, 100, 100));
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        startColumn.add(Box.createRigidArea(new Dimension(0, 5)));
        startColumn.add(hint);

        dateRow.add(startColumn);
        dateRow.add(makeColumn("Residency End Date:", residencyEndField, defaultBorder, highlightListener));
        mainPanel.add(dateRow);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // -------------------------------------------------------------------------------------------------
        // Submit Button for the Form
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

        // --------------------------------------------------------------------------------------------------------------
        // Table Model
        String[] columnNames = {"ID", "Make", "Model", "Year", "Computing Power", "Start Date", "End Date", "Timestamp"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    //---------------------------------------------------------------------------------------------------------------------
    // Method for Vehicle Model, Make, & Year Layout 
    private JPanel makeColumn(String labelText, JTextField field, Border defaultBorder, FocusAdapter highlightListener) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(defaultBorder);
        field.addFocusListener(highlightListener);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        column.add(label);
        column.add(Box.createRigidArea(new Dimension(0, 8)));
        column.add(field);
        return column;
    }

    private void submitVehicle() {
        String make = vehicleMakeField.getText().trim();
        String model = vehicleModelField.getText().trim();
        String year = vehicleYearField.getText().trim();
        String power = (String) computingPowerCombo.getSelectedItem();
        String startDate = residencyStartField.getText().trim();
        String endDate = residencyEndField.getText().trim();

        // Field format enforcement
        if (make.isEmpty() || model.isEmpty() || year.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields");
            return;
        }

        int yearInt;
        try {
            yearInt = Integer.parseInt(year);
            if (yearInt < 1900 || yearInt > 2030) {
                JOptionPane.showMessageDialog(this, "Please enter a valid vehicle year (1900-2030)");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vehicle year must be a valid number");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            if (end.before(start)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date");
                return;
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Dates must be in yyyy-MM-dd format");
            return;
        }

        // Write client data to txt file
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String powerShort = power.split(" ")[0];
        Object[] row = {entryCounter, make, model, yearInt, powerShort, startDate, endDate, timestamp};
        tableModel.addRow(row);
        
        try (FileWriter writer = new FileWriter("owners_data.txt", true)) {
            writer.write(String.join(" | ",
                    String.valueOf(entryCounter++),
                    make,
                    model,
                    String.valueOf(yearInt),
                    power,
                    startDate,
                    endDate + "\n"));
            
            JOptionPane.showMessageDialog(this, "Vehicle availability submitted");
  
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error writing to file");
            
        }
        vehicleMakeField.setText("");
        vehicleModelField.setText("");
        vehicleYearField.setText("");
        computingPowerCombo.setSelectedIndex(0);
        residencyStartField.setText("");
        residencyEndField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OwnerDashboard dashboard = new OwnerDashboard();
            dashboard.setVisible(true);
        });
    }
}