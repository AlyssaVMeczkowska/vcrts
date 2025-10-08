package ui;

import java.awt.*;
import javax.swing.*; 

public class CustomDialog extends JDialog {

    public enum DialogType {
        SUCCESS,
        WARNING
    }

    public CustomDialog(JFrame owner, String title, String message) {
        this(owner, title, message, DialogType.SUCCESS);
    }

    public CustomDialog(Window owner, String title, String message, DialogType type) {
        super(owner, title, Dialog.ModalityType.APPLICATION_MODAL); 
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font iconFont = null;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            iconFont = new Font("Segoe UI Symbol", Font.BOLD, 36);
        } else if (os.contains("mac")) {
            iconFont = new Font("Apple Symbols", Font.BOLD, 36);
        } else {
            iconFont = new Font("Dialog", Font.BOLD, 36);
        }

        JLabel iconLabel = new JLabel();
        iconLabel.setFont(iconFont);

        switch (type) {
            case SUCCESS:
                iconLabel.setText("\u2713");
                iconLabel.setForeground(new Color(30, 110, 130)); 
                break;
            case WARNING:
                iconLabel.setText("\u26A0"); 
                iconLabel.setForeground(new Color(237, 162, 0));
                break;
        }

        JLabel messageLabel = new JLabel("<html><body style='width: 250px; text-align: center;'>" + message + "</body></html>");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(new Color(80, 80, 80));

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0); 
        contentPanel.add(iconLabel, gbc);
        
        gbc.gridy = 1;
        contentPanel.add(messageLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        contentPanel.add(okButton, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        pack(); 
        setLocationRelativeTo(owner);
    }
}