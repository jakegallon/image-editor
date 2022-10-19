package frame;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class InfoPanel extends JPanel {

    static JLabel xPos, yPos, zoomFactor;

    public InfoPanel() {
        setPreferredSize(new Dimension(0, 30));

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        Font uiFont = new Font("Segoe UI", Font.PLAIN, 15);

        zoomFactor = new JLabel("zoom: 100.00%");
        zoomFactor.setFont(uiFont);
        add(zoomFactor);

        xPos = new JLabel("x: 0");
        xPos.setFont(uiFont);
        add(xPos);

        yPos = new JLabel("y: 0");
        yPos.setFont(uiFont);
        add(yPos);

        springLayout.putConstraint(SpringLayout.WEST, zoomFactor, 15, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, zoomFactor, 3, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, yPos, -15, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.NORTH, yPos, 0, SpringLayout.NORTH, zoomFactor);
        springLayout.putConstraint(SpringLayout.EAST, xPos, -15, SpringLayout.WEST, yPos);
        springLayout.putConstraint(SpringLayout.NORTH, xPos, 0, SpringLayout.NORTH, zoomFactor);
    }

    public static void setMouseLocation(Point p) {
        xPos.setText("x: " + p.x);
        yPos.setText("y: " + p.y);
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void setZoomFactor(float f) {
        float zoomPercent = f * 100f;
        zoomFactor.setText("zoom: " + df.format(zoomPercent) + "%");
    }
}
