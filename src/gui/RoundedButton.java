package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RoundedButton extends JButton {
    public RoundedButton(String label) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background oval
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

        // Text and other default stuff
        super.paintComponent(g);
        g2.dispose();
    }

    // OPTIONAL: override setBorder kalau kamu mau matiin efek border
    @Override
    public void setBorder(Border border) {
        // Do nothing, disable default border
    }
}
