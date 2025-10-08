package ui;
import java.awt.*;
import javax.swing.*;

public class ConsentForm extends JDialog {
    private boolean consentGiven = false;
    public ConsentForm(JFrame owner) {
        super(owner, "Terms & Conditions", true);
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setResizable(false);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //----------------------------------------------------------------------
        // TITLE
        JLabel titleLabel = new JLabel("Vehicle Owner Consent Agreement");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        titleLabel.setForeground(new Color(44, 116, 132));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        //----------------------------------------------------------------------
        // Consent Message
        JTextArea consentText = new JTextArea();
        consentText.setEditable(false);
        consentText.setLineWrap(true);
        consentText.setWrapStyleWord(true);
        consentText.setFocusable(false);
        consentText.setFont(new Font("Arial", Font.BOLD, 13));
        consentText.setBackground(new Color(250, 250, 250));
        consentText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        consentText.setText(
            "Owner Consent Agreement\n\n" +     
            "By proceeding, you acknowledge and agree to the following:\n\n" +
            "1. You confirm that you are the legal owner of the every vehicle you register.\n" +
            "2. VCRTS may collect, process and store data about your vehicle.\n" +     
            "3. Your vehicle information will be handled in accordance with applicable data protection laws. \n" +
            "4. Once your vehicle is registered into the platform, you will not be able to access the jobs submitted to your vehicle. \n" +
            "5. Vehicle submissions are final. Once a vehicle has been submitted into the VCRTS platform, you cannot cancel your submission during the specified residency period.\n" +
            "By clicking 'I Agree', you confirm that you agree to these terms. \n"
        );
        JScrollPane scrollPane = new JScrollPane(consentText);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);
        //---------------------------------------------------------------------------------
        // CheckBox for Terms and Conditions
        JCheckBox agreeCheckBox = new JCheckBox("I have read and agree to the terms above");
        agreeCheckBox.setFont(new Font("Arial", Font.BOLD, 12));
        agreeCheckBox.setBackground(Color.WHITE);
        bottomPanel.add(agreeCheckBox, BorderLayout.NORTH);

        //------------------------------------------------------------------------
        // BUTTONS 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        //------------------------------------------------------------------------
        // DECLINE Button: White Gradient
        GradientButton declineButton = new GradientButton("Decline", true);
        declineButton.setFont(new Font("Arial", Font.BOLD, 14));
        declineButton.setForeground(new Color(100, 100, 100));
        declineButton.setPreferredSize(new Dimension(120, 38));
        declineButton.setFocusPainted(false);
        declineButton.setBorderPainted(false);
        declineButton.setContentAreaFilled(false);
        declineButton.addActionListener(e -> {
            consentGiven = false;
            dispose();
        });
        //-------------------------------------------------------------------------
        // AGREE Button: Teal Gradient
        GradientButton agreeButton = new GradientButton("I Agree");
        agreeButton.setFont(new Font("Arial", Font.BOLD, 16));
        agreeButton.setForeground(Color.WHITE);
        agreeButton.setPreferredSize(new Dimension(120, 38));
        agreeButton.setFocusPainted(false);
        agreeButton.setBorderPainted(false);
        agreeButton.setContentAreaFilled(false);
        agreeButton.addActionListener(e -> {
            if (agreeCheckBox.isSelected()) {
                consentGiven = true;
                dispose();
            } else {
                CustomDialog dialog = new CustomDialog(this, "Agreement Required", "Please check the box to confirm you agree to the terms.", CustomDialog.DialogType.WARNING);
                dialog.setVisible(true);
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

