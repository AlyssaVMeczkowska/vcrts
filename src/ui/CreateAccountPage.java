package ui;

import data.UserDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import validation.UserValidator;

public class CreateAccountPage extends JFrame {
    private PlaceholderTextField firstNameField, lastNameField, emailField, usernameField, phoneNumberField;
    private PlaceholderPasswordField passwordField, confirmPasswordField;
    private JCheckBox showPasswordCheckBox, ownerCheckBox, clientCheckBox;
    private JLabel firstNameErrorLabel, lastNameErrorLabel, passwordErrorLabel, emailErrorLabel, usernameErrorLabel, phoneNumberErrorLabel, accountTypeErrorLabel, passwordRequirementsLabel;
    private Border defaultBorder, focusBorder, errorBorder;
    private UserValidator validator = new UserValidator();
    private UserDataManager userDataManager = new UserDataManager();
    private static final Color TEAL = new Color(44, 116, 132);
    private static final Color TEAL_HOVER = new Color(37, 94, 106);
    private static final Color LINK = new Color(0, 124, 137);
    private static final Color GRAY = new Color(100, 100, 100);

    public CreateAccountPage() {
        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setResizable(true);
        setLocationRelativeTo(null);
        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);
        rootPanel.add(createHeader(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel contentArea = new JPanel(new GridBagLayout());
        contentArea.setBackground(new Color(238, 238, 238));
        scrollPane.setViewportView(contentArea);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 350;
        contentArea.add(mainPanel, gbc);

        initBorders();
        buildForm(mainPanel);
        ToolTipManager.sharedInstance().registerComponent(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 50, 15, 50)));
        header.add(new JLabel("VCRTS") {{ setFont(new Font("Georgia", Font.PLAIN, 28)); }}, BorderLayout.WEST);
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setForeground(TEAL);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> navToLogin());
        header.add(loginBtn, BorderLayout.EAST);
        return header;
    }

    private void initBorders() {
        defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
        focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINK, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
        errorBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void buildForm(JPanel main) {
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Georgia", Font.PLAIN, 42));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel subtitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        subtitle.setBackground(Color.WHITE);
        subtitle.add(new JLabel("Create a free account or ") {{
            setFont(new Font("Arial", Font.PLAIN, 16));
            setForeground(GRAY);
        }});
        JLabel loginLink = new JLabel("login");
        loginLink.setFont(new Font("Arial", Font.PLAIN, 16));
        loginLink.setForeground(LINK);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Font f = loginLink.getFont();
        Map<TextAttribute, Object> attr = new HashMap<>(f.getAttributes());
        attr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        loginLink.setFont(f.deriveFont(attr));
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navToLogin(); }
        });
        subtitle.add(loginLink);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(subtitle);
        main.add(Box.createRigidArea(new Dimension(0, 40)));

        FocusAdapter focus = new FocusAdapter() {
            public void focusGained(FocusEvent e) { ((JComponent) e.getComponent()).setBorder(focusBorder); }
            public void focusLost(FocusEvent e) { ((JComponent) e.getComponent()).setBorder(defaultBorder); }
        };

        addNameFieldsInRow(main, focus);
        addField(main, "Username", usernameField = new PlaceholderTextField("Enter your username"),
                 usernameErrorLabel = new JLabel(" "), focus, true);
        addField(main, "Email", emailField = new PlaceholderTextField("Enter your email"),
                 emailErrorLabel = new JLabel(" "), focus, true);
        addField(main, "Phone Number", phoneNumberField = new PlaceholderTextField("(123) 456-7890"),
                 phoneNumberErrorLabel = new JLabel(" "), focus, false);
        addPasswordFields(main, focus);
        addAccountType(main);
        addSignUpBtn(main);
    }

    private void addNameFieldsInRow(JPanel main, FocusAdapter focus) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel leftPanel = createFormFieldPanel("First Name",
                firstNameField = new PlaceholderTextField("Enter your first name"),
                firstNameErrorLabel = new JLabel(" "), focus, true);
        
        JPanel rightPanel = createFormFieldPanel("Last Name",
                lastNameField = new PlaceholderTextField("Enter your last name"),
                lastNameErrorLabel = new JLabel(" "), focus, true);

        rowPanel.add(leftPanel);
        rowPanel.add(rightPanel);
        main.add(rowPanel);
        main.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JPanel createFormFieldPanel(String labelText, JTextField field, JLabel errorLabel, FocusAdapter focus, boolean isRequired) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JPanel lbl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lbl.setBackground(Color.WHITE);
        String labelHtml = isRequired ? "<html>" + labelText + " <font color='red'>*</font></html>" : labelText;
        lbl.add(new JLabel(labelHtml) {{ setFont(new Font("Arial", Font.BOLD, 14)); }});
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(defaultBorder);
        field.addFocusListener(focus);
        panel.add(field);

        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel err = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        err.setBackground(Color.WHITE);
        err.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        err.add(errorLabel);
        panel.add(err);

        return panel;
    }

    private void addField(JPanel main, String label, JTextField field, JLabel error, FocusAdapter focus, boolean isRequired) {
        JPanel fieldPanel = createFormFieldPanel(label, field, error, focus, isRequired);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        main.add(fieldPanel);
        main.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private void addPasswordFields(JPanel main, FocusAdapter focus) {
        JPanel header = new JPanel(new BorderLayout());
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        header.setBackground(Color.WHITE);
        header.add(new JLabel("<html>Password <font color='red'>*</font></html>") {{ setFont(new Font("Arial", Font.BOLD, 14)); }}, BorderLayout.WEST);
        showPasswordCheckBox = new JCheckBox("Show");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        showPasswordCheckBox.setForeground(LINK);
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.addActionListener(e -> {
            char c = showPasswordCheckBox.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(c);
            confirmPasswordField.setEchoChar(c);
        });
        header.add(showPasswordCheckBox, BorderLayout.EAST);
        main.add(header);
        main.add(Box.createRigidArea(new Dimension(0, 8)));
        
        passwordField = new PlaceholderPasswordField("Enter your password");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(defaultBorder);
        passwordField.addFocusListener(focus);
        main.add(passwordField);
        
        passwordRequirementsLabel = new JLabel("<html><body style='width: 100%'>Must contain at least:<br>"
                + "  &#8226; 8 characters<br>  &#8226; 1 uppercase & 1 lowercase letter<br>"
                + "  &#8226; 1 number & 1 special character (!@#...)</body></html>");
        passwordRequirementsLabel.setForeground(GRAY);
        passwordRequirementsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel req = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        req.setBackground(Color.WHITE);
        req.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        req.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        req.add(passwordRequirementsLabel);
        main.add(req);
        main.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel lbl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        lbl.setBackground(Color.WHITE);
        lbl.add(new JLabel("<html>Confirm Password <font color='red'>*</font></html>") {{ setFont(new Font("Arial", Font.BOLD, 14)); }});
        main.add(lbl);
        main.add(Box.createRigidArea(new Dimension(0, 8)));
        
        confirmPasswordField = new PlaceholderPasswordField("Re-enter your password");
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBorder(defaultBorder);
        confirmPasswordField.addFocusListener(focus);
        main.add(confirmPasswordField);
        
        passwordErrorLabel = new JLabel(" ");
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel err = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        err.setBackground(Color.WHITE);
        err.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        err.add(passwordErrorLabel);
        main.add(err);
        main.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private void addAccountType(JPanel main) {
        JPanel lbl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        lbl.setBackground(Color.WHITE);
        lbl.add(new JLabel("<html>Account Type <font color='red'>*</font></html>") {{ setFont(new Font("Arial", Font.BOLD, 14)); }});
        lbl.add(Box.createRigidArea(new Dimension(4, 0)));
        Font infoFont = null;
        try {
            File bundled = new File("assets/NotoSansSymbols-Regular.ttf");
            if (bundled.exists()) {
                infoFont = Font.createFont(Font.TRUETYPE_FONT, bundled).deriveFont(Font.PLAIN, 15f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(infoFont);
            }
        } catch (Exception e) {
            System.err.println("Warning: failed to load bundled info font: " + e.getMessage());
        }

        if (infoFont == null) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                infoFont = new Font("Segoe UI Symbol", Font.PLAIN, 15);
            } else if (os.contains("mac")) {
                infoFont = new Font("Apple Symbols", Font.PLAIN, 15);
            } else {
                infoFont = new Font("Arial Unicode MS", Font.PLAIN, 15);
            }
        }

        JLabel infoIcon = new JLabel("\u24D8");
        infoIcon.setFont(infoFont);
        infoIcon.setForeground(Color.BLACK);
        infoIcon.setVerticalAlignment(SwingConstants.BOTTOM);
        infoIcon.setBorder(BorderFactory.createEmptyBorder(-4, 0, 0, 0));
        infoIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        infoIcon.setToolTipText("<html><span style='font-size:10px;'><b>Select \"Owner\" if you are interested in:<br><b>Renting your vehicle for its unused computational power<br><br><b>Select \"Client\" if you are interested in:<br><b>Using a vehicle's computational power to run a job</span></html>");
        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("ToolTip.font", new Font("Arial", Font.PLAIN, 11));
        ToolTipManager.sharedInstance().setInitialDelay(200);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        lbl.add(infoIcon);
        main.add(lbl);
        main.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel types = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        types.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        types.setBackground(Color.WHITE);
        ownerCheckBox = new JCheckBox("Owner");
        ownerCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        ownerCheckBox.setBackground(Color.WHITE);
        ownerCheckBox.addActionListener(e -> { if (ownerCheckBox.isSelected()) clientCheckBox.setSelected(false); });
        clientCheckBox = new JCheckBox("Client");
        clientCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        clientCheckBox.setBackground(Color.WHITE);
        clientCheckBox.addActionListener(e -> { if (clientCheckBox.isSelected()) ownerCheckBox.setSelected(false); });
        types.add(ownerCheckBox);
        types.add(Box.createRigidArea(new Dimension(25, 0)));
        types.add(clientCheckBox);
        main.add(types);
        
        accountTypeErrorLabel = new JLabel(" ");
        accountTypeErrorLabel.setForeground(Color.RED);
        accountTypeErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel err = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        err.setBackground(Color.WHITE);
        err.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        err.add(accountTypeErrorLabel);
        main.add(err);
        main.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    private void addSignUpBtn(JPanel main) {
        GradientButton signUpButton = new GradientButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.addActionListener(e -> {
            if (validateForm()) {
                String type = ownerCheckBox.isSelected() ? "Owner" : "Client";
                userDataManager.addUser(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    usernameField.getText(),
                    phoneNumberField.getText(),
                    new String(passwordField.getPassword()),
                    type
                );
  
                JOptionPane.showMessageDialog(this, "Account Created Successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                navToLogin();
            }
        });
        main.add(signUpButton);
        main.add(Box.createRigidArea(new Dimension(0, 40)));
    }

    private void navToLogin() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }

    private boolean validateForm() {
        boolean valid = true;

        if (!validator.isNameValid(firstNameField.getText())) {
            valid = false;
            firstNameField.setBorder(errorBorder);
            firstNameErrorLabel.setText("First name is required.");
        } else {
            firstNameField.setBorder(defaultBorder);
            firstNameErrorLabel.setText(" ");
        }

        if (!validator.isNameValid(lastNameField.getText())) {
            valid = false;
            lastNameField.setBorder(errorBorder);
            lastNameErrorLabel.setText("Last name is required.");
        } else {
            lastNameField.setBorder(defaultBorder);
            lastNameErrorLabel.setText(" ");
        }

        if (!validator.isEmailValid(emailField.getText())) {
            valid = false;
            emailField.setBorder(errorBorder);
            emailErrorLabel.setText("Please enter a valid email address.");
        } else if (userDataManager.isEmailTaken(emailField.getText())) {
            valid = false;
            emailField.setBorder(errorBorder);
            emailErrorLabel.setText("This email is already taken.");
        } else {
            emailField.setBorder(defaultBorder);
            emailErrorLabel.setText(" ");
        }

        if (!validator.isUsernameValid(usernameField.getText())) {
            valid = false;
            usernameField.setBorder(errorBorder);
            usernameErrorLabel.setText("Username must be longer than 6 characters.");
        } else if (userDataManager.isUsernameTaken(usernameField.getText())) {
            valid = false;
            usernameField.setBorder(errorBorder);
            usernameErrorLabel.setText("This username is already taken.");
        } else {
            usernameField.setBorder(defaultBorder);
            usernameErrorLabel.setText(" ");
        }
        
        String phone = phoneNumberField.getText();
        if (!phone.trim().isEmpty() && !validator.isPhoneNumberValid(phone)) {
            valid = false;
            phoneNumberField.setBorder(errorBorder);
            phoneNumberErrorLabel.setText("Invalid format. Please use a format like (123) 456-7890.");
        } else {
            phoneNumberField.setBorder(defaultBorder);
            phoneNumberErrorLabel.setText(" ");
        }

        String pwd = new String(passwordField.getPassword());
        boolean len = validator.hasValidLength(pwd);
        boolean cas = validator.hasCaseVariety(pwd);
        boolean num = validator.hasNumberAndSpecialChar(pwd);
        boolean strong = len && cas && num;
        passwordRequirementsLabel.setText(String.format(
            "<html><body style='width: 100%%'>Must contain at least:<br>" +
            "<span style='color:%s'>&#8226; 8 characters</span><br>" +
            "<span style='color:%s'>&#8226; 1 uppercase & 1 lowercase letter</span><br>" +
            "<span style='color:%s'>&#8226; 1 number & 1 special character (!@#...)</span></body></html>",
            len ? "#006400" : "#C00000", cas ? "#006400" : "#C00000", num ? "#006400" : "#C00000"));
        
        if (!strong) {
            valid = false;
            passwordField.setBorder(errorBorder);
        } else {
            passwordField.setBorder(defaultBorder);
        }

        if (pwd.isEmpty() || !Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
            valid = false;
            confirmPasswordField.setBorder(errorBorder);
            passwordErrorLabel.setText("Please make sure your passwords match.");
        } else {
            confirmPasswordField.setBorder(defaultBorder);
            passwordErrorLabel.setText(" ");
        }
        
        if (!ownerCheckBox.isSelected() && !clientCheckBox.isSelected()) {
            valid = false;
            accountTypeErrorLabel.setText("Please select an account type.");
        } else {
            accountTypeErrorLabel.setText(" ");
        }

        return valid;
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
    
    private static class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;
        public PlaceholderPasswordField(String placeholder) { this.placeholder = placeholder; }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (placeholder == null || placeholder.isEmpty() || getPassword().length > 0) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(150, 150, 150));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(placeholder, getInsets().left + 5, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
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
        SwingUtilities.invokeLater(() -> {
            CreateAccountPage createAccountPage = new CreateAccountPage();
            createAccountPage.setVisible(true);
        });
    }
}