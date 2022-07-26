package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ColorSelector extends JPanel {

    private final ColorSquare colorSquare = new ColorSquare();
    private final HueSlider hue = new HueSlider();

    public ColorSelector() {
        setLayout(new BorderLayout());

        add(hue, BorderLayout.PAGE_START);
        add(colorSquare, BorderLayout.CENTER);
    }

    class ColorSquare extends JPanel {

        private BufferedImage bilinearColorSquare;

        private ColorSquare(){
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    bilinearColorSquare = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                    redrawColorCanvas(hue.currentHue);
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bilinearColorSquare, 0, 0, getWidth(), getHeight(), null);
            repaint();
        }

        private void redrawColorCanvas(Color topRight) {
            Color topLeft = Color.white;
            Color bottom = Color.black;

            Graphics2D g = bilinearColorSquare.createGraphics();

            int colorGridWidth = getWidth();
            int colorGridHeight = getHeight();

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

    class HueSlider extends JSlider{

        private Color currentHue;

        private HueSlider() {
            setMinimum(0);
            setMaximum(360);
            setValue(0);
            currentHue = Color.getHSBColor(getValue()/360f, 1f, 1f);

            addChangeListener(e -> {
                currentHue = Color.getHSBColor(getValue()/360f, 1f, 1f);
                colorSquare.redrawColorCanvas(currentHue);
            });
        }
    }
}
