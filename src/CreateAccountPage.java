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

public class CreateAccountPage extends JFrame {
    private PlaceholderTextField emailField;
    private PlaceholderTextField usernameField;
    private PlaceholderPasswordField passwordField;
    private PlaceholderPasswordField confirmPasswordField;
    
    private JCheckBox showPasswordCheckBox;
    private JCheckBox ownerCheckBox;
    private JCheckBox clientCheckBox;
    private JButton signUpButton;

    private JLabel passwordErrorLabel;
    private JLabel emailErrorLabel;
    private JLabel usernameErrorLabel;
    private JLabel accountTypeErrorLabel;
    private JLabel passwordRequirementsLabel;
    private Border defaultBorder;
    private Border focusBorder;
    private Border errorBorder;

    private UserValidator validator = new UserValidator();
    private UserDataManager userDataManager = new UserDataManager();

    public CreateAccountPage() {
        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        //UI component and layout 
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 50, 15, 50)
        ));
        JLabel headerTitle = new JLabel("VCRTS");
        headerTitle.setFont(new Font("Georgia", Font.PLAIN, 28));
        headerPanel.add(headerTitle, BorderLayout.WEST);
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(new Color(44, 116, 132));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
        });
        headerPanel.add(loginButton, BorderLayout.EAST);
        JPanel contentArea = new JPanel(new GridBagLayout());
        contentArea.setBackground(new Color(238, 238, 238));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipadx = 350;
        gbc.ipady = 0;
        contentArea.add(mainPanel, gbc);
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(contentArea, BorderLayout.CENTER);
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel subtitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        subtitlePanel.setBackground(Color.WHITE);
        JLabel subtitlePart1 = new JLabel("Create a free account or ");
        subtitlePart1.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitlePart1.setForeground(new Color(100, 100, 100));
        JLabel loginLink = new JLabel("login");
        loginLink.setFont(new Font("Arial", Font.PLAIN, 16));
        loginLink.setForeground(new Color(44, 116, 132));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Font font = loginLink.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        loginLink.setFont(font.deriveFont(attributes));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
            }
        });
        subtitlePanel.add(subtitlePart1);
        subtitlePanel.add(loginLink);
        subtitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitlePanel);
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
                ((JComponent) e.getComponent()).setBorder(focusBorder);
            }
            @Override
            public void focusLost(FocusEvent e) {
                ((JComponent) e.getComponent()).setBorder(defaultBorder);
            }
        };
        JPanel emailLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        emailLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        emailLabelPanel.setBackground(Color.WHITE);
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabelPanel.add(emailLabel);
        mainPanel.add(emailLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        emailField = new PlaceholderTextField("Enter your email");
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(defaultBorder);
        emailField.addFocusListener(highlightListener);
        mainPanel.add(emailField);
        emailErrorLabel = new JLabel(" ");
        emailErrorLabel.setForeground(Color.RED);
        emailErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel emailErrorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        emailErrorPanel.setBackground(Color.WHITE);
        emailErrorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        emailErrorPanel.add(emailErrorLabel);
        mainPanel.add(emailErrorPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JPanel usernameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        usernameLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        usernameLabelPanel.setBackground(Color.WHITE);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabelPanel.add(usernameLabel);
        mainPanel.add(usernameLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        usernameField = new PlaceholderTextField("Enter your username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(defaultBorder);
        usernameField.addFocusListener(highlightListener);
        mainPanel.add(usernameField);
        usernameErrorLabel = new JLabel(" ");
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel usernameErrorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        usernameErrorPanel.setBackground(Color.WHITE);
        usernameErrorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        usernameErrorPanel.add(usernameErrorLabel);
        mainPanel.add(usernameErrorPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JPanel passwordHeaderPanel = new JPanel(new BorderLayout());
        passwordHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        passwordHeaderPanel.setBackground(Color.WHITE);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordHeaderPanel.add(passwordLabel, BorderLayout.WEST);
        showPasswordCheckBox = new JCheckBox("Show");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        showPasswordCheckBox.setForeground(new Color(0, 124, 137));
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
                confirmPasswordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
                confirmPasswordField.setEchoChar('•');
            }
        });
        passwordHeaderPanel.add(showPasswordCheckBox, BorderLayout.EAST);
        mainPanel.add(passwordHeaderPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        passwordField = new PlaceholderPasswordField("Enter your password");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(defaultBorder);
        passwordField.addFocusListener(highlightListener);
        mainPanel.add(passwordField);
        String requirementsText = "<html><body style='width: 100%'>"
                + "Must contain at least:<br>"
                + "  &#8226; 8 characters<br>"
                + "  &#8226; 1 uppercase & 1 lowercase letter<br>"
                + "  &#8226; 1 number & 1 special character (!@#...)"
                + "</body></html>";
        passwordRequirementsLabel = new JLabel(requirementsText);
        passwordRequirementsLabel.setForeground(new Color(100, 100, 100));
        passwordRequirementsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel passwordRequirementsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        passwordRequirementsPanel.setBackground(Color.WHITE);
        passwordRequirementsPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        passwordRequirementsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        passwordRequirementsPanel.add(passwordRequirementsLabel);
        mainPanel.add(passwordRequirementsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- CONFIRM PASSWORD FIELD ---
        JPanel confirmPasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        confirmPasswordLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        confirmPasswordLabelPanel.setBackground(Color.WHITE);
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        confirmPasswordLabelPanel.add(confirmPasswordLabel);
        mainPanel.add(confirmPasswordLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        confirmPasswordField = new PlaceholderPasswordField("Re-enter your password");
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBorder(defaultBorder);
        confirmPasswordField.addFocusListener(highlightListener);
        mainPanel.add(confirmPasswordField);
        passwordErrorLabel = new JLabel(" ");
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        errorPanel.add(passwordErrorLabel);
        mainPanel.add(errorPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel accountTypeLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        accountTypeLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        accountTypeLabelPanel.setBackground(Color.WHITE);
        JLabel accountTypeLabel = new JLabel("Account Type");
        accountTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accountTypeLabelPanel.add(accountTypeLabel);
        accountTypeLabelPanel.add(Box.createRigidArea(new Dimension(4, 0)));

        // Prefer a bundled font that contains the circled 'i' to guarantee the glyph across platforms.
        Font infoFont = null;
        try {
            File bundled = new File("assets/NotoSansSymbols-Regular.ttf");
            System.err.println("[info icon] checking bundled font path: " + bundled.getAbsolutePath());
            if (bundled.exists()) {
                System.err.println("[info icon] bundled font FOUND, size=" + bundled.length());
                infoFont = Font.createFont(Font.TRUETYPE_FONT, bundled).deriveFont(Font.PLAIN, 15f);
                boolean registered = GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(infoFont);
                System.err.println("[info icon] registered font? " + registered + ", canDisplay(\u24D8)=" + infoFont.canDisplay('\u24D8'));
            } else {
                System.err.println("[info icon] bundled font not found at path");
            }
        } catch (Exception e) {
            // If registration fails, we'll fall back to system fonts below
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
            // If chosen system font can't display the glyph, fall back to Dialog (plain 'i' fallback handled elsewhere)
            if (!infoFont.canDisplay('\u24D8')) {
                infoFont = infoFont.deriveFont(Font.PLAIN, 15f);
            }
        }

        JLabel infoIcon = new JLabel("\u24D8"); // Unicode for circled 'i'
        infoIcon.setFont(infoFont);
        infoIcon.setForeground(Color.BLACK);
        infoIcon.setVerticalAlignment(SwingConstants.BOTTOM);
        infoIcon.setBorder(BorderFactory.createEmptyBorder(-4, 0, 0, 0)); // more negative for higher alignment
        infoIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        infoIcon.setToolTipText("<html><span style='font-size:10px;'><b>Select \"Owner\" if you are interested in:<br><b>Renting your vehicle for its unused computational power<br><br><b>Select \"Client\" if you are interested in:<br><b>Using a vehicle's computational power to run a job</span></html>");
        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("ToolTip.font", new Font("Arial", Font.PLAIN, 11));
        ToolTipManager.sharedInstance().setInitialDelay(200);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        accountTypeLabelPanel.add(infoIcon);
        mainPanel.add(accountTypeLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel accountTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        accountTypePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        accountTypePanel.setBackground(Color.WHITE);
        ownerCheckBox = new JCheckBox("Owner");
        ownerCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        ownerCheckBox.setBackground(Color.WHITE);
        ownerCheckBox.addActionListener(e -> {
            if (ownerCheckBox.isSelected()) clientCheckBox.setSelected(false);
        });
        clientCheckBox = new JCheckBox("Client");
        clientCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        clientCheckBox.setBackground(Color.WHITE);
        clientCheckBox.addActionListener(e -> {
            if (clientCheckBox.isSelected()) ownerCheckBox.setSelected(false);
        });
        accountTypePanel.add(ownerCheckBox);
        accountTypePanel.add(Box.createRigidArea(new Dimension(25, 0)));
        accountTypePanel.add(clientCheckBox);
        mainPanel.add(accountTypePanel);
        accountTypeErrorLabel = new JLabel(" ");
        accountTypeErrorLabel.setForeground(Color.RED);
        accountTypeErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel accountTypeErrorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        accountTypeErrorPanel.setBackground(Color.WHITE);
        accountTypeErrorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        accountTypeErrorPanel.add(accountTypeErrorLabel);
        mainPanel.add(accountTypeErrorPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBackground(new Color(44, 116, 132));
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorderPainted(false);
        signUpButton.setOpaque(true);
        Color defaultColor = new Color(44, 116, 132);
        Color hoverColor = new Color(37, 94, 106);
        signUpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signUpButton.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signUpButton.setBackground(defaultColor);
            }
        });
        
        signUpButton.addActionListener(e -> {
            if (validateForm()) {
                String accountType = ownerCheckBox.isSelected() ? "Owner" : "Client";
                userDataManager.addUser(emailField.getText(), usernameField.getText(), new String(passwordField.getPassword()), accountType);
                JOptionPane.showMessageDialog(this, "Account Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
            }
        });
        
        mainPanel.add(signUpButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        ToolTipManager.sharedInstance().registerComponent(mainPanel);
    }
    
    private boolean validateForm() {
        boolean isAllValid = true;
        
        if (!validator.isEmailValid(emailField.getText())) {
            isAllValid = false;
            emailField.setBorder(errorBorder);
            emailErrorLabel.setText("Please enter a valid email address.");
        } else if (userDataManager.isEmailTaken(emailField.getText())) {
            isAllValid = false;
            emailField.setBorder(errorBorder);
            emailErrorLabel.setText("This email is already taken.");
        } else {
            emailField.setBorder(defaultBorder);
            emailErrorLabel.setText(" ");
        }

        if (!validator.isUsernameValid(usernameField.getText())) {
            isAllValid = false;
            usernameField.setBorder(errorBorder);
            usernameErrorLabel.setText("Username must be longer than 6 characters.");
        } else if (userDataManager.isUsernameTaken(usernameField.getText())) {
            isAllValid = false;
            usernameField.setBorder(errorBorder);
            usernameErrorLabel.setText("This username is already taken.");
        } else {
            usernameField.setBorder(defaultBorder);
            usernameErrorLabel.setText(" ");
        }
        
        String password = new String(passwordField.getPassword());
        String green = "#006400";
        String red = "#C00000";
        boolean hasLength = validator.hasValidLength(password);
        boolean hasCase = validator.hasCaseVariety(password);
        boolean hasNumAndSpecial = validator.hasNumberAndSpecialChar(password);
        boolean isPasswordStrong = hasLength && hasCase && hasNumAndSpecial;
        String feedbackText = "<html><body style='width: 100%'>"
                + "Must contain at least:<br>"
                + String.format("<span style='color:%s'>&#8226; 8 characters</span><br>", hasLength ? green : red)
                + String.format("<span style='color:%s'>&#8226; 1 uppercase & 1 lowercase letter</span><br>", hasCase ? green : red)
                + String.format("<span style='color:%s'>&#8226; 1 number & 1 special character (!@#...)</span>", hasNumAndSpecial ? green : red)
                + "</body></html>";
        passwordRequirementsLabel.setText(feedbackText);
        if (!isPasswordStrong) {
            isAllValid = false;
            passwordField.setBorder(errorBorder);
        } else {
            passwordField.setBorder(defaultBorder);
        }

        if (password.isEmpty() || !Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
             isAllValid = false;
             confirmPasswordField.setBorder(errorBorder);
             passwordErrorLabel.setText("Please make sure your passwords match.");
        } else {
             confirmPasswordField.setBorder(defaultBorder);
             passwordErrorLabel.setText(" ");
        }
        
        if (!ownerCheckBox.isSelected() && !clientCheckBox.isSelected()) {
            isAllValid = false;
            accountTypeErrorLabel.setText("Please select an account type.");
        } else {
            accountTypeErrorLabel.setText(" ");
        }

        return isAllValid;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CreateAccountPage frame = new CreateAccountPage();
            frame.setVisible(true);
        });
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
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, getInsets().left + 5, y);
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
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, getInsets().left + 5, y);
        }
    }
}
