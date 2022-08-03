package colorpanel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ColorPanelSelector extends JPanel{
    private boolean locked = false;

    private final ColorSquare colorSquare = new ColorSquare();
    private final HueSlider hueSlider = new HueSlider();
    boolean isHueLocked = false;

    private final ColorPanel colorPanel;

    public ColorPanelSelector(ColorPanel colorPanel) {
        this.colorPanel = colorPanel;
        setLayout(new BorderLayout());

        add(hueSlider, BorderLayout.PAGE_START);
        add(colorSquare, BorderLayout.CENTER);
    }

    public void updateHSV() {
        updateHue();
        updateSat();
        updateVal();
    }

    public void updateHue() {
        int hue = colorPanel.getHue();
        locked = true;
        hueSlider.setValue(hue);
        locked = false;
    }

    public void updateSat() {
        float sat = colorPanel.getSat() / 100f;
        colorSquare.selectedX = (int) (sat * colorSquare.bilinearColorSquare.getWidth());
    }

    public void updateVal() {
        float val = colorPanel.getVal() / 100f;
        colorSquare.selectedY = (int) ((1- val) * colorSquare.bilinearColorSquare.getHeight());
    }

    class ColorSquare extends JPanel implements MouseListener, MouseMotionListener {

        private BufferedImage bilinearColorSquare = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        int selectedX = 0;
        int selectedY = 0;
        final int selectedCursorSize = 10;

        private ColorSquare(){
            addMouseListener(this);
            addMouseMotionListener(this);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    bilinearColorSquare = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                    redrawColorCanvas(hueSlider.getCurrentColor());
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

        @Override
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                int x = Math.min(e.getX(), bilinearColorSquare.getWidth() - 1);
                x = Math.max(x, 0);
                int y = Math.min(e.getY(), bilinearColorSquare.getHeight() - 1);
                y = Math.max(y, 0);

                colorPanel.setSat((float) x/bilinearColorSquare.getWidth());
                colorPanel.setVal(1 - (float) y/bilinearColorSquare.getHeight());

                colorPanel.notifySV();
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

                colorPanel.setSat((float) x/bilinearColorSquare.getWidth());
                colorPanel.setVal(1 - (float) y/bilinearColorSquare.getHeight());

                colorPanel.notifySV();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    class HueSlider extends JSlider{

        private HueSlider() {
            setMinimum(0);
            setMaximum(360);
            setValue(0);

            addChangeListener(e -> {
                if(!locked) {
                    colorPanel.setHue(getValue());
                    colorPanel.notifyH();
                }
                colorSquare.redrawColorCanvas(getCurrentColor());
            });
        }

        private Color getCurrentColor() {
            return Color.getHSBColor(getValue()/360f, 1f, 1f);
        }
    }
}
