package frame;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ColorSelector extends JPanel {

    private final ColorSquare colorSquare = new ColorSquare();
    private final HueSlider hue = new HueSlider();

    private final SwingPropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this);
    private static final String SELECTED_COLOR_CHANGED = "color changed";
    private int selectedColor;

    public ColorSelector(ColorPanel colorPanel) {
        setLayout(new BorderLayout());

        add(hue, BorderLayout.PAGE_START);
        add(colorSquare, BorderLayout.CENTER);

        propertyChangeSupport.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals(SELECTED_COLOR_CHANGED)){
                colorPanel.setSelectedColor((Integer) evt.getNewValue());
            }
        });
    }

    public void setColor(int color) {
        int blue  =  color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red   = (color & 0xff0000) >> 16;

        float[] hsv = new float[3];
        Color.RGBtoHSB(red, green, blue, hsv);

        colorSquare.redrawColorCanvas(Color.getHSBColor(hsv[0], 1f, 1f));
    }

    class ColorSquare extends JPanel implements MouseListener {

        private BufferedImage bilinearColorSquare;

        private ColorSquare(){
            addMouseListener(this);
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

        private void setSelectedColor(int newColor) {
            float oldValue = selectedColor;
            selectedColor = newColor;
            propertyChangeSupport.firePropertyChange(SELECTED_COLOR_CHANGED, oldValue, selectedColor);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            setSelectedColor(bilinearColorSquare.getRGB(e.getX(), e.getY()));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

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