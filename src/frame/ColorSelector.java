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
        if(colorSquare.isHueLocked) return;

        int blue  =  color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red   = (color & 0xff0000) >> 16;

        float[] hsv = new float[3];
        Color.RGBtoHSB(red, green, blue, hsv);

        colorSquare.redrawColorCanvas(Color.getHSBColor(hsv[0], 1f, 1f));
        colorSquare.updateSelectedPosition(hsv[1], hsv[2]);
    }

    public void updateHue(int hue) {
        this.hue.setValue(hue);
    }

    class ColorSquare extends JPanel implements MouseListener, MouseMotionListener {

        private BufferedImage bilinearColorSquare = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        int selectedX = 0, selectedY = 0;
        final int selectedCursorSize = 10;
        boolean isHueLocked = false;

        private ColorSquare(){
            addMouseListener(this);
            addMouseMotionListener(this);
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
            drawColorMarker(g);
            repaint();
        }

        private void drawColorMarker(Graphics g){
            g.setColor(Color.gray);
            g.drawOval(selectedX - selectedCursorSize/2, selectedY - selectedCursorSize/2, selectedCursorSize, selectedCursorSize);
            g.setColor(Color.white);
            g.drawOval((selectedX - selectedCursorSize/2) + 1, (selectedY - selectedCursorSize/2) + 1, selectedCursorSize - 2, selectedCursorSize - 2);
        }

        private void updateSelectedColor(){
            setSelectedColor(bilinearColorSquare.getRGB(selectedX, selectedY));
        }

        private void updateSelectedPosition(float sat, float val){
            selectedX = (int) (sat * bilinearColorSquare.getWidth());
            selectedY = (int) ((1-val) * bilinearColorSquare.getHeight());
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
            if(SwingUtilities.isLeftMouseButton(e)){
                isHueLocked = true;
                selectedX = e.getX();
                selectedY = e.getY();
                updateSelectedColor();
                isHueLocked = false;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                isHueLocked = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                isHueLocked = false;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                int x = Math.min(e.getX(), bilinearColorSquare.getWidth() - 1);
                x = Math.max(x, 0);
                int y = Math.min(e.getY(), bilinearColorSquare.getHeight() - 1);
                y = Math.max(y, 0);
                selectedX = x;
                selectedY = y;
                updateSelectedColor();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

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
                colorSquare.updateSelectedColor();
            });
        }
    }
}
