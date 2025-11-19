package ui;

import java.awt.*;
import javax.swing.*;

public class CustomConfirmDialog extends JDialog {

    private boolean result = false;

    public boolean getResult() {
        return result;
    }

    public CustomConfirmDialog(Window owner, String message, boolean isDanger) {
        super(owner, "", ModalityType.APPLICATION_MODAL);
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel msg = new JLabel(
            "<html><div style='text-align:center; width:300px;'>" 
            + message + 
            "</div></html>"
        );
        msg.setFont(new Font("Arial", Font.BOLD, 18));
        msg.setForeground(new Color(60, 60, 60));

        GradientButton yesBtn = new GradientButton("Yes");
        yesBtn.setPreferredSize(new Dimension(120, 42));
        yesBtn.setFont(new Font("Arial", Font.BOLD, 15));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.addActionListener(e -> { result = true; dispose(); });

        JButton noBtn = new JButton("No") {
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

        noBtn.setPreferredSize(new Dimension(120, 42));
        noBtn.setBorderPainted(false);
        noBtn.setFocusPainted(false);
        noBtn.setContentAreaFilled(false);
        noBtn.setOpaque(false);
        noBtn.setForeground(new Color(70, 70, 70));
        noBtn.setFont(new Font("Arial", Font.BOLD, 15));
        noBtn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(noBtn);
        btnPanel.add(yesBtn);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        content.add(msg, gbc);

        gbc.gridy = 1;
        content.add(btnPanel, gbc);

        mainPanel.add(content, BorderLayout.CENTER);
        setContentPane(mainPanel);

        pack();
        setLocationRelativeTo(owner);
    }
}
