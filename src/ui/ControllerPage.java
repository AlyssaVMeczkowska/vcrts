package ui;

import java.awt.*;
import javax.swing.*;
import model.User;

public class ControllerPage extends JFrame {

    private User currentUser;

    public ControllerPage(User user) {
        this.currentUser = user;

        setTitle("VCRTS Controller Dashboard");
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
        
        JLabel headerTitle = new JLabel("VCRTS Controller");
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
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(238, 238, 238));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + "!");
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.5; 
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(40, 0, 0, 0); 
        contentPanel.add(welcomeLabel, gbc);

        GradientButton calcButton = new GradientButton("Calculate Job Completion Times");
        calcButton.setFont(new Font("Arial", Font.BOLD, 16));
        calcButton.setForeground(Color.WHITE);
        calcButton.setPreferredSize(new Dimension(300, 38)); 
        calcButton.setMaximumSize(new Dimension(300, 38));   
        calcButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        calcButton.addActionListener(e -> {
            System.out.println("Calculating job completion times...");
        });

        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(20, 0, 0, 0);
        contentPanel.add(calcButton, gbc);
        
        rootPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testController = new User(
                 1, 
                 "Admin", 
                 "User", 
                 "controller@vcrts.com", 
                 "admin", 
                 "(123) 456-7890", 
                 "dummyhash", 
                 "Controller", 
                 "2025-01-01T12:00:00", 
                 true
             );
            
             ControllerPage controllerPage = new ControllerPage(testController);
             controllerPage.setVisible(true);
         });
     }
}