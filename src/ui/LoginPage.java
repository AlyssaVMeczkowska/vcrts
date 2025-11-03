package ui;

import data.UserDataManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import model.User;

public class LoginPage extends JFrame {
    private PlaceholderTextField usernameField;
    private PlaceholderPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JLabel loginErrorLabel;

    private UserDataManager userDataManager = new UserDataManager();
    public LoginPage() {
        setTitle("Login");
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
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 14));
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setForeground(new Color(44, 116, 132));
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new CreateAccountPage().setVisible(true));
        });
        headerPanel.add(createAccountButton, BorderLayout.EAST);
        
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
        
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Georgia", Font.PLAIN, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel subtitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        subtitlePanel.setBackground(Color.WHITE);
        JLabel subtitlePart1 = new JLabel("Enter your credentials or ");
        subtitlePart1.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitlePart1.setForeground(new Color(100, 100, 100));
        JLabel createAccountLink = new JLabel("create an account");
        createAccountLink.setFont(new Font("Arial", Font.PLAIN, 16));
        createAccountLink.setForeground(new Color(44, 116, 132));
        createAccountLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Font font = createAccountLink.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        createAccountLink.setFont(font.deriveFont(attributes));
        createAccountLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new CreateAccountPage().setVisible(true));
            }
        });
        subtitlePanel.add(subtitlePart1);
        subtitlePanel.add(createAccountLink);
        subtitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        Border defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        Border focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 124, 137), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        Border errorBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1, true),
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

        JPanel usernameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        usernameLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        usernameLabelPanel.setBackground(Color.WHITE);
        JLabel usernameLabel = new JLabel("Username or Email");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabelPanel.add(usernameLabel);
        mainPanel.add(usernameLabelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        usernameField = new PlaceholderTextField("Enter your username or email");
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
            if (showPasswordCheckBox.isSelected()) passwordField.setEchoChar((char) 0);
            else passwordField.setEchoChar('•');
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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        loginErrorLabel = new JLabel(" ");
        loginErrorLabel.setForeground(Color.RED);
        loginErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        loginErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginErrorLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        GradientButton loginButton = new GradientButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.addActionListener(e -> attemptLogin());

        mainPanel.add(loginButton);
        
        this.getRootPane().setDefaultButton(loginButton);
    }

    private void attemptLogin() {
        String usernameOrEmail = usernameField.getText();
        String password = new String(passwordField.getPassword());

        usernameField.setBorder(usernameField.getBorder()); 
        passwordField.setBorder(passwordField.getBorder());
        loginErrorLabel.setText(" ");

        User loggedInUser = userDataManager.verifyUser(usernameOrEmail, password);

        if (loggedInUser != null) {
            String accountType = loggedInUser.getAccountType();
            dispose();

            if ("Owner".equals(accountType)) {
                SwingUtilities.invokeLater(() -> new VehicleSubmissionPage(loggedInUser).setVisible(true));
            } else if ("Client".equals(accountType)) {
                SwingUtilities.invokeLater(() -> new JobSubmissionPage(loggedInUser).setVisible(true));
            } else if ("Controller".equals(accountType)) {
                SwingUtilities.invokeLater(() -> new ControllerPage(loggedInUser).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(null, "Error: Unknown account type '" + accountType + "'.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            loginErrorLabel.setText("Invalid username/email or password.");
            Border errorBorder = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 1, true),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            );
            usernameField.setBorder(errorBorder);
            passwordField.setBorder(errorBorder);
        }
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
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         LoginPage frame = new LoginPage();
    //         frame.setVisible(true);
    //     });
    // }
}