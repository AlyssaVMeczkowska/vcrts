import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CreateAccountPage extends JFrame {
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField; // Added field
    private JCheckBox showPasswordCheckBox;
    private JCheckBox ownerCheckBox;
    private JCheckBox clientCheckBox;
    private JButton signUpButton;

    public CreateAccountPage() {
        setTitle("Create Account");
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
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(new Color(44, 116, 132));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(loginButton, BorderLayout.EAST);

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
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipadx = 350;
        gbc.ipady = 0;
        contentArea.add(mainPanel, gbc);

        // --- ADD PANELS TO ROOT ---
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(contentArea, BorderLayout.CENTER);

        // --- COMPONENTS ---
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitleLabel = new JLabel("Create a free account or login");
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

        JPanel emailLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        emailLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        emailLabelPanel.setBackground(Color.WHITE);
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabelPanel.add(emailLabel);
        mainPanel.add(emailLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(defaultBorder);
        emailField.addFocusListener(highlightListener);
        mainPanel.add(emailField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel usernameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        usernameLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        usernameLabelPanel.setBackground(Color.WHITE);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabelPanel.add(usernameLabel);
        mainPanel.add(usernameLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(defaultBorder);
        usernameField.addFocusListener(highlightListener);
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

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

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(defaultBorder);
        passwordField.addFocusListener(highlightListener);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- NEW CONFIRM PASSWORD FIELD ---
        JPanel confirmPasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        confirmPasswordLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        confirmPasswordLabelPanel.setBackground(Color.WHITE);
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        confirmPasswordLabelPanel.add(confirmPasswordLabel);
        mainPanel.add(confirmPasswordLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBorder(defaultBorder);
        confirmPasswordField.addFocusListener(highlightListener);
        mainPanel.add(confirmPasswordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));


        JPanel accountTypeLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        accountTypeLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        accountTypeLabelPanel.setBackground(Color.WHITE);
        JLabel accountTypeLabel = new JLabel("Account Type");
        accountTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accountTypeLabelPanel.add(accountTypeLabel);
        JLabel infoIcon = new JLabel(" ⓘ ");
        infoIcon.setFont(new Font("Arial", Font.BOLD, 12));
        infoIcon.setForeground(new Color(0, 124, 137));
        infoIcon.setToolTipText("<html><b>Select \"Owner\" if you are interested in:<br><b>Renting your vehicle for its unused computational power<br><br><b>Select \"Client\" if you are interested in:<br><b>Using a vehicle's computational power to run a job</html>");
        UIManager.put("ToolTip.background", Color.WHITE);
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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

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
        mainPanel.add(signUpButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        ToolTipManager.sharedInstance().registerComponent(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CreateAccountPage frame = new CreateAccountPage();
            frame.setVisible(true);
        });
    }
}