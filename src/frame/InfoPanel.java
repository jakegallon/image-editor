package frame;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class InfoPanel extends JPanel {

    JLabel xPos, yPos, zoomFactor;

    public InfoPanel() {
        setPreferredSize(new Dimension(0, 30));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        Font uiFont = new Font("Segoe UI", Font.PLAIN, 15);

        Box leftBox = new Box(BoxLayout.X_AXIS);
        leftBox.setPreferredSize(new Dimension(130, getPreferredSize().height));
        zoomFactor = new JLabel("zoom: 100.00%");
        zoomFactor.setFont(uiFont);
        zoomFactor.setPreferredSize(new Dimension(65, getPreferredSize().height));
        leftBox.add(zoomFactor);

        Box rightBox = new Box(BoxLayout.X_AXIS);
        rightBox.setPreferredSize(new Dimension(130, getPreferredSize().height));
        xPos = new JLabel("x: 0");
        xPos.setFont(uiFont);
        xPos.setPreferredSize(new Dimension(65, getPreferredSize().height));
        yPos = new JLabel("y: 0");
        yPos.setFont(uiFont);
        yPos.setPreferredSize(new Dimension(65, getPreferredSize().height));
        rightBox.add(xPos);
        rightBox.add(yPos);

        add(leftBox);
        add(Box.createHorizontalGlue());
        add(rightBox);
    }

    public void setMouseLocation(Point p) {
        xPos.setText("x: " + p.x);
        yPos.setText("y: " + p.y);
    }

    DecimalFormat df = new DecimalFormat("0.00");

    public void setZoomFactor(float zoomFactor) {
        float zoomPercent = zoomFactor * 100f;
        this.zoomFactor.setText("zoom: " + df.format(zoomPercent) + "%");
    }

}
