package ui;

import ClientServer.RequestSender;
import data.RequestDataManager;
import data.UserDataManager;
import data.VehicleDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import model.Request;
import model.User;
import model.Vehicle;
import model.VehicleStatus;
import validation.VehicleValidator;

public class VehicleSubmissionPage extends JFrame {
    private final VehicleValidator validator = new VehicleValidator();
    private final VehicleDataManager dataManager = new VehicleDataManager();
    private final UserDataManager userDataManager = new UserDataManager();
    private final RequestDataManager requestDataManager = new RequestDataManager();
    private User currentUser;
    private Border defaultBorder, focusBorder, errorBorder;
    private JPanel formsContainer;
    private GradientButton submitButton;
    private List<VehicleFormPanel> vehicleForms;
    private int vehicleCounter = 1;
    private JPanel mainPanel;
    public VehicleSubmissionPage(User user) {
        this.currentUser = user;
        this.vehicleForms = new ArrayList<>();
        
        setTitle("Vehicle Availability");
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
        
        JPanel headerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerButtonsPanel.setBackground(Color.WHITE);
        JButton viewSubmissionsButton = new JButton("My Submissions");
        viewSubmissionsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewSubmissionsButton.setFocusPainted(false);
        viewSubmissionsButton.setBorderPainted(false);
        viewSubmissionsButton.setContentAreaFilled(false);
        viewSubmissionsButton.setForeground(new Color(44, 116, 132));
        viewSubmissionsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewSubmissionsButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new VehicleOwnerRequestStatusPage(currentUser).setVisible(true));
        });
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
        headerButtonsPanel.add(viewSubmissionsButton);
        headerButtonsPanel.add(logoutButton);
        headerPanel.add(headerButtonsPanel, BorderLayout.EAST);

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
        JLabel titleLabel = new JLabel("Submit Vehicle Availability");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        JLabel subtitleLabel = new JLabel("Register your vehicle");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
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
        formsContainer = new JPanel();
        formsContainer.setLayout(new BoxLayout(formsContainer, BoxLayout.Y_AXIS));
        formsContainer.setBackground(Color.WHITE);
        formsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(formsContainer);

        JLabel vehicle1Label = new JLabel("Vehicle 1");
        vehicle1Label.setFont(new Font("Georgia", Font.BOLD, 28));
        vehicle1Label.setForeground(new Color(0, 124, 137));
        
        JPanel vehicle1LabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        vehicle1LabelPanel.setBackground(Color.WHITE);
        vehicle1LabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vehicle1LabelPanel.add(vehicle1Label);
        vehicle1LabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, vehicle1Label.getPreferredSize().height));
        formsContainer.add(vehicle1LabelPanel);
        formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        addVehicleForm();

        submitButton = new GradientButton("Submit All Vehicles");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setForeground(Color.WHITE);
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> submitAllVehicles());
        mainPanel.add(submitButton);

        this.getRootPane().setDefaultButton(submitButton);
    }

    private void addVehicleForm() {
        if (vehicleCounter > 1) {
            if (!vehicleForms.isEmpty()) {
                VehicleFormPanel lastForm = vehicleForms.get(vehicleForms.size() - 1);
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
            JLabel vehicleLabel = new JLabel("Vehicle " + vehicleCounter);
            vehicleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
            vehicleLabel.setForeground(new Color(0, 124, 137));
            JPanel vehicleLabelPanel = new JPanel(new BorderLayout());
            vehicleLabelPanel.setBackground(Color.WHITE);
            vehicleLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            vehicleLabelPanel.add(vehicleLabel, BorderLayout.WEST);
            
            JLabel removeButton = new JLabel("−");
            removeButton.setFont(new Font("Arial", Font.PLAIN, 24));
            removeButton.setForeground(new Color(0, 124, 137));
            removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            removeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                    if (!vehicleForms.isEmpty()) {
                        vehicleForms.remove(vehicleForms.size() - 1);
        
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
            vehicleLabelPanel.add(removeButton, BorderLayout.EAST);
            vehicleLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, vehicleLabel.getPreferredSize().height));
            formsContainer.add(vehicleLabelPanel);
            formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        VehicleFormPanel vehicleForm = new VehicleFormPanel();
        vehicleForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        vehicleForms.add(vehicleForm);
        formsContainer.add(vehicleForm);
        
        vehicleCounter++;
        
        int topBottomUIsHeight = 225;
        int firstFormHeight = 380;
        int subsequentFormHeight = 480;

        int newHeight;
        if (vehicleForms.size() == 1) {
            newHeight = topBottomUIsHeight + firstFormHeight;
        } else {
            newHeight = topBottomUIsHeight + firstFormHeight + ((vehicleForms.size() - 1) * subsequentFormHeight);
        }

        mainPanel.setPreferredSize(new Dimension(900, newHeight));
        mainPanel.setMaximumSize(new Dimension(900, newHeight));
        mainPanel.revalidate();
        SwingUtilities.invokeLater(() -> {
            vehicleForm.scrollRectToVisible(vehicleForm.getBounds());
        });
    }

    private void rebuildFormsContainer() {
        formsContainer.removeAll();
        JLabel vehicle1Label = new JLabel("Vehicle 1");
        vehicle1Label.setFont(new Font("Georgia", Font.BOLD, 28));
        vehicle1Label.setForeground(new Color(0, 124, 137));
        JPanel vehicle1LabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        vehicle1LabelPanel.setBackground(Color.WHITE);
        vehicle1LabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vehicle1LabelPanel.add(vehicle1Label);
        vehicle1LabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, vehicle1Label.getPreferredSize().height));
        formsContainer.add(vehicle1LabelPanel);
        formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        for (int i = 0; i < vehicleForms.size(); i++) {
            if (i > 0) {
                formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
                JSeparator sep = new JSeparator();
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                sep.setForeground(new Color(220, 220, 220));
                formsContainer.add(sep);
                formsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
                JLabel vLabel = new JLabel("Vehicle " + (i + 1));
                vLabel.setFont(new Font("Georgia", Font.BOLD, 28));
                vLabel.setForeground(new Color(0, 124, 137));
                JPanel vLabelPanel = new JPanel(new BorderLayout());
                vLabelPanel.setBackground(Color.WHITE);
                vLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                vLabelPanel.add(vLabel, BorderLayout.WEST);
                
                JLabel remBtn = new JLabel("−");
                remBtn.setFont(new Font("Arial", Font.PLAIN, 24));
                remBtn.setForeground(new Color(0, 124, 137));
                remBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                final int formIndex = i;
                remBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        vehicleForms.remove(formIndex);
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
                vLabelPanel.add(remBtn, BorderLayout.EAST);
                vLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, vLabel.getPreferredSize().height));
                formsContainer.add(vLabelPanel);
                formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
            
            VehicleFormPanel currentForm = vehicleForms.get(i);
            formsContainer.add(currentForm);
            
            if (i == vehicleForms.size() - 1) {
                JPanel addVehicleButtonPanel = new JPanel();
                addVehicleButtonPanel.setLayout(new BoxLayout(addVehicleButtonPanel, BoxLayout.X_AXIS));
                addVehicleButtonPanel.setBackground(Color.WHITE);
                addVehicleButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                addVehicleButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                JButton addVehicleButton = new JButton("+ Add Vehicle");
                addVehicleButton.setFont(new Font("Arial", Font.BOLD, 14));
                addVehicleButton.setForeground(new Color(0, 124, 137));
                addVehicleButton.setBackground(Color.WHITE);
                addVehicleButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
                addVehicleButton.setFocusPainted(false);
                addVehicleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                addVehicleButton.addActionListener(evt -> addVehicleForm());
                
                addVehicleButtonPanel.add(Box.createHorizontalGlue());
                addVehicleButtonPanel.add(addVehicleButton);
                
                formsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                formsContainer.add(addVehicleButtonPanel);
            }
        }
        
        vehicleCounter = vehicleForms.size() + 1;
        formsContainer.revalidate();
        formsContainer.repaint();
    }

    private void updateMainPanelHeight() {
        int topBottomUIsHeight = 225;
        int firstFormHeight = 380;
        int subsequentFormHeight = 480;

        int newHeight;
        if (vehicleForms.size() == 1) {
            newHeight = topBottomUIsHeight + firstFormHeight;
        } else {
            newHeight = topBottomUIsHeight + firstFormHeight + ((vehicleForms.size() - 1) * subsequentFormHeight);
        }

        mainPanel.setPreferredSize(new Dimension(900, newHeight));
        mainPanel.setMaximumSize(new Dimension(900, newHeight));
        mainPanel.revalidate();
    }

    private boolean validateAllForms() {
        boolean allValid = true;
        for (VehicleFormPanel form : vehicleForms) {
            if (!form.validateForm()) {
                allValid = false;
            }
        }
        return allValid;
    }

    private void submitAllVehicles()
    {
        if (!currentUser.hasAgreedToTerms())
        {
            ConsentForm consentForm = new ConsentForm(this);
            consentForm.setVisible(true);

            if (consentForm.isConsentGiven())
            {
                userDataManager.updateUserConsent(currentUser);
                currentUser.setHasAgreedToTerms(true);
            }
            else
            {
                CustomDialog dialog = new CustomDialog(this, "Submission Cancelled",
                        "You must agree to the terms and conditions to register a vehicle.",
                        CustomDialog.DialogType.WARNING);
                dialog.setVisible(true);
                return;
            }
        }

        if (!validateAllForms())
        {
            return;
        }

        int nextVehicleId = getNextVehicleId();
        int successCount = 0;
        StringBuilder vehicleIds = new StringBuilder();

        for (VehicleFormPanel form : vehicleForms)
        {
            Vehicle vehicle = new Vehicle(
                    0,
                    currentUser.getId(),
                    form.getVehicleMake(),
                    form.getVehicleModel(),
                    Integer.parseInt(form.getVehicleYear()),
                    form.getVinNumber(),
                    form.getLicensePlate(),
                    form.getComputingPower(),
                    LocalDate.parse(form.getResidencyStart()),
                    LocalDate.parse(form.getResidencyEnd()),
                    VehicleStatus.AVAILABLE
            );

            String payload = buildPayload(vehicle);


            boolean accepted = RequestSender.sendVehicleSubmission(payload);

            if (accepted) {
                successCount++;
                if (vehicleIds.length() > 0) {
                    vehicleIds.append(", ");
                }
                vehicleIds.append("#").append(nextVehicleId);
                nextVehicleId++;
            }
        }

        if (successCount == vehicleForms.size()) {
            String message = String.format(
                    "%d vehicle(s) submitted for controller review!\n\nVehicle ID(s): %s\n\n" +
                            "You will be notified once the controller reviews your submission(s).",
                    successCount, vehicleIds.toString()
            );
            CustomDialog dialog = new CustomDialog(this, "Requests Submitted",
                    message, CustomDialog.DialogType.SUCCESS);
            dialog.setVisible(true);
            clearAllForms();
        } else if (successCount > 0) {
            String message = String.format(
                    "%d of %d vehicle(s) submitted for review.\n\nVehicle ID(s): %s\n\nSome vehicles failed to submit.",
                    successCount, vehicleForms.size(), vehicleIds.toString()
            );
            CustomDialog dialog = new CustomDialog(this, "Partial Success",
                    message, CustomDialog.DialogType.WARNING);
            dialog.setVisible(true);
        } else {
            CustomDialog dialog = new CustomDialog(this, "Submission Failed",
                    "Failed to submit vehicles. Please try again.", CustomDialog.DialogType.WARNING);
            dialog.setVisible(true);
        }
    }
    
    private int getNextVehicleId() {
        int maxVehicleId = 0;
        List<Vehicle> allVehicles = dataManager.getAllVehicles();
        for (Vehicle vehicle : allVehicles) {
            if (vehicle.getVehicleId() > maxVehicleId) {
                maxVehicleId = vehicle.getVehicleId();
            }
        }

        List<Request> allRequests = requestDataManager.getPendingRequests();
        for (Request request : allRequests) {
            if ("VEHICLE_SUBMISSION".equals(request.getRequestType())) {
                String[] dataLines = request.getData().split("\n");
                for (String line : dataLines) {
                    if (line.startsWith("Vehicle ID:")) {
                        try {
                            int vehicleId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                            if (vehicleId > maxVehicleId) {
                                maxVehicleId = vehicleId;
                            }
                        } catch (NumberFormatException e) {
                        }
                
                        break;
                    }
                }
            }
        }
        
        return maxVehicleId + 1;
    }

    private String buildPayload(Vehicle v)
    {
        return "type: vehicle_availability\n"
                + "user_id: " + v.getOwnerId() + "\n"
                + "vin: " + v.getVin() + "\n"
                + "license_plate: " + v.getLicensePlate() + "\n"
        
                + "vehicle_make: " + v.getMake() + "\n"
                + "vehicle_model: " + v.getModel() + "\n"
                + "vehicle_year: " + v.getYear() + "\n"
                + "computing_power: " + v.getComputingPower() + "\n"
                + "start_date: " + 
v.getArrivalDate() + "\n"
                + "end_date: " + v.getDepartureDate() + "\n"
                + "---";
    }


    private void clearAllForms() {
        formsContainer.removeAll();
        vehicleForms.clear();
        vehicleCounter = 1;
        JLabel vehicle1Label = new JLabel("Vehicle 1");
        vehicle1Label.setFont(new Font("Georgia", Font.BOLD, 28));
        vehicle1Label.setForeground(new Color(0, 124, 137));
        JPanel vehicle1LabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        vehicle1LabelPanel.setBackground(Color.WHITE);
        vehicle1LabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vehicle1LabelPanel.add(vehicle1Label);
        vehicle1LabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, vehicle1Label.getPreferredSize().height));
        formsContainer.add(vehicle1LabelPanel);
        formsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        
        addVehicleForm();
        formsContainer.revalidate();
        formsContainer.repaint();
    }

    private void showCalendar(JTextField targetField, boolean restrictToFuture, Date minSelectableDate, JLabel errorLabel) {
        CalendarDialog calendarDialog = new CalendarDialog(this, restrictToFuture, minSelectableDate);
        calendarDialog.setLocationRelativeTo(targetField);
        calendarDialog.setVisible(true);
        if (calendarDialog.getSelectedDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetField.setText(dateFormat.format(calendarDialog.getSelectedDate()));
            targetField.setBorder(defaultBorder);
            errorLabel.setText(" ");
        }
    }

    private class VehicleFormPanel extends JPanel {
        private PlaceholderTextField vehicleMakeField;
        private PlaceholderTextField vehicleModelField;
        private PlaceholderTextField vehicleYearField;
        private PlaceholderTextField licensePlateField;
        private PlaceholderTextField vinNumberField;
        private JComboBox<String> computingPowerCombo;
        private PlaceholderTextField residencyStartField;
        private PlaceholderTextField residencyEndField;
        private JLabel vehicleMakeErrorLabel, vehicleModelErrorLabel, vehicleYearErrorLabel;
        private JLabel licensePlateErrorLabel, vinNumberErrorLabel;
        private JLabel residencyStartErrorLabel, residencyEndErrorLabel;
        private JPanel addButtonPanel;
        public VehicleFormPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 375));
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
                        ((JComponent) 
                        e.getComponent()).setBorder(defaultBorder);
                    }
                }
            };
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
            add(firstRowPanel);
            add(Box.createRigidArea(new Dimension(0, 5)));
            
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
            add(powerPanel);
            add(Box.createRigidArea(new Dimension(0, 5)));
            
            JPanel secondRowPanel = new JPanel(new GridLayout(1, 2, 20, 0));
            secondRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            secondRowPanel.setBackground(Color.WHITE);
            licensePlateField = new PlaceholderTextField("Enter license plate");
            vinNumberField = new PlaceholderTextField("Enter VIN number");
            licensePlateErrorLabel = new JLabel(" ");
            vinNumberErrorLabel = new JLabel(" ");
            secondRowPanel.add(createColumn("<html>License Plate: <font color='red'>*</font></html>", licensePlateField, licensePlateErrorLabel, highlightListener));
            secondRowPanel.add(createColumn("<html>VIN Number: <font color='red'>*</font></html>", vinNumberField, vinNumberErrorLabel, highlightListener));
            add(secondRowPanel);
            add(Box.createRigidArea(new Dimension(0, 5)));
            
            JPanel dateRow = new JPanel(new GridLayout(1, 2, 20, 0));
            dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            dateRow.setBackground(Color.WHITE);
            residencyStartField = new PlaceholderTextField("Select a date");
            residencyStartField.setEditable(false);
            residencyStartField.setBackground(Color.WHITE);
            residencyStartField.setCursor(new Cursor(Cursor.HAND_CURSOR));
            residencyStartField.setFocusable(false);
            residencyStartField.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showCalendar(residencyStartField, true, null, residencyStartErrorLabel);
                }
            });
            residencyEndField = new PlaceholderTextField("Select a date");
            residencyEndField.setEditable(false);
            residencyEndField.setBackground(Color.WHITE);
            residencyEndField.setCursor(new Cursor(Cursor.HAND_CURSOR));
            residencyEndField.setFocusable(false);
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
                    showCalendar(residencyEndField, 
false, minEndDate, residencyEndErrorLabel);
                }
            });
            residencyStartErrorLabel = new JLabel(" ");
            residencyEndErrorLabel = new JLabel(" ");
            dateRow.add(createColumn("<html>Residency Start Date: <font color='red'>*</font></html>", residencyStartField, residencyStartErrorLabel, highlightListener));
            dateRow.add(createColumn("<html>Residency End Date: <font color='red'>*</font></html>", residencyEndField, residencyEndErrorLabel, highlightListener));
            add(dateRow);
            add(Box.createRigidArea(new Dimension(0, 8)));
            
            addButtonPanel = new JPanel();
            addButtonPanel.setLayout(new BoxLayout(addButtonPanel, BoxLayout.X_AXIS));
            addButtonPanel.setBackground(Color.WHITE);
            addButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            addButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JButton localAddButton = new JButton("+ Add Vehicle");
            localAddButton.setFont(new Font("Arial", Font.BOLD, 14));
            localAddButton.setForeground(new Color(0, 124, 137));
            localAddButton.setBackground(Color.WHITE);
            localAddButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            localAddButton.setFocusPainted(false);
            localAddButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            localAddButton.addActionListener(evt -> {
                addButtonPanel.setVisible(false);
                addVehicleForm();
            });
            addButtonPanel.add(Box.createHorizontalGlue());
            addButtonPanel.add(localAddButton);
            add(addButtonPanel);
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

        public boolean validateForm() {
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

        public String getVehicleMake() { 
            return vehicleMakeField.getText().trim();
        }
        public String getVehicleModel() { 
            return vehicleModelField.getText().trim();
        }
        public String getVehicleYear() { 
            return vehicleYearField.getText().trim();
        }
        public String getLicensePlate() { 
            return licensePlateField.getText().trim();
        }
        public String getVinNumber() { 
            return vinNumberField.getText().trim();
        }
        public String getComputingPower() { 
            return (String) computingPowerCombo.getSelectedItem();
        }
        public String getResidencyStart() { 
            return residencyStartField.getText().trim();
        }
        public String getResidencyEnd() { 
            return residencyEndField.getText().trim();
        }
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
}