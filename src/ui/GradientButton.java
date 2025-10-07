package ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class GradientButton extends JButton {
    private boolean isHovered = false;
    private boolean isWhite = false;


    public GradientButton(String text) {
        this(text, false);
    }


    public GradientButton(String text, boolean white) {
        super(text);
        this.isWhite = white;
        setOpaque(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered = true;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color1, color2;
        
        if (isWhite) {

            if (isHovered) {
                color1 = new Color(255, 255, 255);
                color2 = new Color(240, 240, 240);
            } else {
                color1 = new Color(250, 250, 250);
                color2 = new Color(220, 220, 220);
            }
        } else {
 
            if (isHovered) {
                color1 = new Color(60, 200, 220);
                color2 = new Color(20, 140, 160);
            } else {
                color1 = new Color(50, 170, 190);
                color2 = new Color(30, 110, 130);
            }
        }

        GradientPaint gradient = new GradientPaint(
            0, 0, color1,
            getWidth(), 0, color2
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

        g2.dispose();
        super.paintComponent(g);
    }
}