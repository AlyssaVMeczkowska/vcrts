package ui;

import java.awt.*;
import javax.swing.*;

public class CustomDialog extends JDialog {

    public CustomDialog(JFrame owner, String title, String message) {
        super(owner, title, true);
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel contentPanel = new JPanel(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Icon Font Logic
        Font iconFont = null;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            iconFont = new Font("Segoe UI Symbol", Font.BOLD, 36);
        } else if (os.contains("mac")) {
            iconFont = new Font("Apple Symbols", Font.BOLD, 36);
        } else {
            iconFont = new Font("Dialog", Font.BOLD, 36); 
        }

        JLabel iconLabel = new JLabel("\u2713"); // Checkmark symbol
        iconLabel.setFont(iconFont);
        iconLabel.setForeground(new Color(30, 110, 130));
        
        // Message Label with HTML for wrapping
        JLabel messageLabel = new JLabel("<html><body style='width: 250px; text-align: center;'>" + message + "</body></html>");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(new Color(80, 80, 80));

        // OK Button
        GradientButton okButton = new GradientButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.setContentAreaFilled(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.setMaximumSize(new Dimension(100, 35));
        okButton.addActionListener(e -> dispose());
        
        // Add components using GridBagConstraints for proper centering
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0); 
        contentPanel.add(iconLabel, gbc);
        
        gbc.gridy = 1;
        contentPanel.add(messageLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 0, 0);
        contentPanel.add(okButton, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        pack(); 
        setLocationRelativeTo(owner);
    }
}