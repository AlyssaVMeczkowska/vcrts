package ui;

import java.awt.*;
import javax.swing.*;

public class CustomInputDialog extends JDialog {

    private String input = null;

    public String getInput() {
        return input;
    }

    public CustomInputDialog(Window owner, String message) {
        super(owner, "", ModalityType.APPLICATION_MODAL);
        setUndecorated(true);

        // Outer rounded border panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        GridBagConstraints gbc = new GridBagConstraints();

        // -----------------------------------------------------
        // MESSAGE
        // -----------------------------------------------------
        JLabel msg = new JLabel(
            "<html><div style='text-align:center; width:300px;'>" 
            + message + 
            "</div></html>"
        );
        msg.setFont(new Font("Arial", Font.BOLD, 18));
        msg.setForeground(new Color(60, 60, 60));

        // -----------------------------------------------------
        // INPUT FIELD
        // -----------------------------------------------------
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 40));
        textField.setFont(new Font("Arial", Font.PLAIN, 15));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // -----------------------------------------------------
        // CANCEL BUTTON — Oval, matches ConfirmDialog
        // -----------------------------------------------------
        JButton cancelBtn = new JButton("Cancel") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(245, 245, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        cancelBtn.setPreferredSize(new Dimension(120, 42));
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setOpaque(false);
        cancelBtn.setForeground(new Color(70, 70, 70));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 15));
        cancelBtn.addActionListener(e -> dispose());

        // -----------------------------------------------------
        // OK BUTTON — Gradient, white text
        // -----------------------------------------------------
        GradientButton okBtn = new GradientButton("OK");
        okBtn.setPreferredSize(new Dimension(120, 42));
        okBtn.setFont(new Font("Arial", Font.BOLD, 15));
        okBtn.setForeground(Color.WHITE);  // White text
        okBtn.addActionListener(e -> {
            input = textField.getText().trim();
            dispose();
        });

        // -----------------------------------------------------
        // BUTTON PANEL
        // -----------------------------------------------------
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);

        // -----------------------------------------------------
        // GRIDBAG LAYOUT
        // -----------------------------------------------------
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        content.add(msg, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        content.add(textField, gbc);

        gbc.gridy = 2;
        content.add(btnPanel, gbc);

        mainPanel.add(content, BorderLayout.CENTER);
        setContentPane(mainPanel);

        pack();
        setLocationRelativeTo(owner);
    }
}
