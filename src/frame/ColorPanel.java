package frame;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JTabbedPane {

    public ColorPanel() {
        addColorPanel();
        addPalettePanel();
    }

    private void addColorPanel() {
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));

        JSlider hue = new JSlider(0, 360, 0);
        JPanel colorGrid = new JPanel();
        colorGrid.setBackground(Color.black);

        hue.addChangeListener(e -> {
            Color newColor = Color.getHSBColor(hue.getValue()/360f, 1f, 1f);
            redrawColorGrid(colorGrid, newColor);
        });

        colorPanel.add(hue);
        colorPanel.add(colorGrid);

        addTab("Color", colorPanel);
    }

    private void addPalettePanel() {
        JPanel palettePanel = new JPanel();

        addTab("Palette", palettePanel);
    }

    private void redrawColorGrid(JPanel colorGrid, Color topRight) {
        Color topLeft = Color.white;
        Color bottom = Color.black;

        int colorGridWidth = colorGrid.getWidth();
        int colorGridHeight = colorGrid.getHeight();

        Graphics2D g = (Graphics2D) colorGrid.getGraphics();

        for(int i = 0; i < colorGridHeight; i++) {
            float heightFraction = (float)i / colorGridHeight;
            Color left = lerpColor(topLeft, bottom, heightFraction);
            Color right = lerpColor(topRight, bottom, heightFraction);
            for(int j = 0; j < colorGridWidth; j++) {
                float widthFraction = (float)j / colorGridWidth;
                Color c = lerpColor(left, right, widthFraction);
                g.setColor(c);
                g.fillRect(j, i, 1, 1);
            }
        }
    }

    private Color lerpColor(Color c1, Color c2, float f) {
        float rf = 1 - f;
        int red = (int) (c1.getRed() * rf + c2.getRed() * f);
        int green = (int) (c1.getGreen() * rf + c2.getGreen() * f);
        int blue = (int) (c1.getBlue() * rf + c2.getBlue() * f);
        return new Color(red, green, blue);
    }
}