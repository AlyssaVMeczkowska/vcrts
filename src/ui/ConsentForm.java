/* Class is set up so that there will be a pop up each time an Owner tries to
 * submit a vehicle into the system
 */

package ui;
import java.awt.*;
import javax.swing.*;

public class ConsentForm extends JDialog {
    private boolean consentGiven = false;

    // Original constructor for consent agreement
    public ConsentForm(JFrame owner) {
        super(owner, "Terms & Conditions", true);
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //---------------------------------------------------------------------
        // TITLE
        JLabel titleLabel = new JLabel("Vehicle Owner Consent Agreement");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(44, 116, 132));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        //---------------------------------------------------------------------
        // Consent Form Message
        JTextArea consentText = new JTextArea();
        consentText.setEditable(false);
        consentText.setLineWrap(true);
        consentText.setWrapStyleWord(true);
        consentText.setFont(new Font("Arial", Font.BOLD, 13));
        consentText.setBackground(new Color(250, 250, 250));
        consentText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        //---------------------------------------------------------------------
        // Message if you want to add anything 
        consentText.setText(
            "VEHICLE OWNER CONSENT AGREEMENT\n\n" +  
            "By proceeding, you acknowledge and agree to the following\n\n" +
            "1. You confirm that you are the legal owner of the vehicle.\n" +
            "2. VCRTS can collect, process and store data about your vehicle.\n" +
            "3. Line about privacy. \n" +
            "4. Line about not being able to see the jobs the vehicle is submitted for.\n" +
            "5. Line about not being able to cancel after submission." +
            "By clicking 'I Agree', you confirm that you agree to these terms."
        );

        //---------------------------------------------------------------------
        // Scroll Panel
        JScrollPane scrollPane = new JScrollPane(consentText);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);

        //---------------------------------------------------------------------
        // For Terms & conditions
        JCheckBox agreeCheckBox = new JCheckBox("I have read and agree to the terms above");
        agreeCheckBox.setFont(new Font("Arial", Font.BOLD, 12));
        agreeCheckBox.setBackground(Color.WHITE);
        bottomPanel.add(agreeCheckBox, BorderLayout.NORTH);

        //-----------------------------------------------------------------------
        // BUTTONS 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        //-----------------------------------------------------------------------
        // Decline Button
        JButton declineButton = new JButton("Decline");
        declineButton.setFont(new Font("Arial", Font.BOLD, 14));
        declineButton.setForeground(new Color(100, 100, 100));
        declineButton.setBackground(Color.WHITE);
        declineButton.setPreferredSize(new Dimension(120, 38));
        declineButton.setFocusPainted(false);
        declineButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        declineButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        declineButton.addActionListener(e -> {
            consentGiven = false;
            dispose();
        });

        //--------------------------------------------------------
        // Agree Button
        GradientButton agreeButton = new GradientButton("I Agree");
        agreeButton.setFont(new Font("Arial", Font.BOLD, 16));
        agreeButton.setForeground(Color.WHITE);
        agreeButton.setPreferredSize(new Dimension(120, 38));
        agreeButton.setFocusPainted(false);
        agreeButton.setBorderPainted(false);
        agreeButton.setContentAreaFilled(false);
        agreeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        agreeButton.addActionListener(e -> {
            if (agreeCheckBox.isSelected()) {
                consentGiven = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please check the box to confirm you agree to the terms.",
                    "Agreement Required",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(declineButton);
        buttonPanel.add(agreeButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }



    public boolean isConsentGiven() {
        return consentGiven;
    }
}