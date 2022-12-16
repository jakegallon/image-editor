package colorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class ColorPanelSelector extends JPanel implements MouseListener, MouseMotionListener {
    int squareSize = 0;
    final int ovalThickness = 20;

    BufferedImage hueImage = new BufferedImage(1 ,1 ,BufferedImage.TYPE_INT_ARGB);
    private Area hueArea;
    double selectedAngle;

    final ColorPanel colorPanel;
    final ColorSquare colorSquare = new ColorSquare();
    private int hueMarkerX;
    private int hueMarkerY;

    public ColorPanelSelector(ColorPanel colorPanel) {
        this.colorPanel = colorPanel;
        setLayout(null);
        add(colorSquare);
        addMouseListener(this);
        addMouseMotionListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                squareSize = Math.min(getWidth(), getHeight());
                redrawHueImage();
                repositionHueMarker();
                alignColorPanel();
            }
        });
    }

    public void redrawHueImage() {
        int width = getWidth();
        int height = getHeight();
        Point squareCorner = new Point((width - squareSize)/2, (height - squareSize)/2);
        hueImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        hueArea = new Area(new Ellipse2D.Double(squareCorner.x, squareCorner.y, squareSize, squareSize));
        Area innerArea = new Area(new Ellipse2D.Double(squareCorner.x + ovalThickness, squareCorner.y + ovalThickness, squareSize - ovalThickness * 2, squareSize - ovalThickness * 2));
        hueArea.subtract(innerArea);
        for (int x = 0; x < hueImage.getWidth(); x++) {
            for (int y = 0; y < hueImage.getHeight(); y++) {
                if(hueArea.contains(new Point(x, y))){
                    double angle = -Math.toDegrees(Math.atan2(x - width/2f, y - height/2f)) + 180;
                    Color hue = Color.getHSBColor((float) (angle / 360f), 1, 1);
                    hueImage.setRGB(x, y, hue.getRGB());
                }
            }
        }
    }

    private void alignColorPanel() {
        int r = (int) (squareSize/2f - (ovalThickness));
        int colorSquareSize = (int) ((r * Math.sqrt(2)));
        int csx = (int) (getWidth()/2f - (r * Math.sin(Math.toRadians(45))));
        int csy = (int) (getHeight()/2f - (r * Math.cos(Math.toRadians(45))));
        colorSquare.setBounds(csx, csy, colorSquareSize, colorSquareSize);
    }

    public void updateHSV() {
        updateHue();
        updateSat();
        updateVal();
    }

    public void updateHue() {
        selectedAngle = colorPanel.getHue();
        colorSquare.redrawColorCanvas((float) selectedAngle);
        repositionHueMarker();
    }

    public void updateSat() {
        float sat = colorPanel.getSat() / 100f;
        colorSquare.selectedX = (int) (sat * colorSquare.bilinearColorSquare.getWidth());
    }

    public void updateVal() {
        float val = colorPanel.getVal() / 100f;
        colorSquare.selectedY = (int) ((1- val) * colorSquare.bilinearColorSquare.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(hueImage, 0, 0, getWidth(), getHeight(), null);
        drawHueMarker(g);
        repaint();
    }

    private void repositionHueMarker() {
        int r = (int) (squareSize/2f - (ovalThickness / 2f));
        hueMarkerX = (int) (r * Math.sin(Math.toRadians(selectedAngle))) + getWidth()/2;
        hueMarkerY = -(int) (r * Math.cos(Math.toRadians(selectedAngle))) + getHeight()/2;
    }

    private void drawHueMarker(Graphics g) {
        g.setColor(Color.gray);
        g.drawOval(hueMarkerX - ovalThickness/2, hueMarkerY - ovalThickness/2, ovalThickness, ovalThickness);
        g.setColor(Color.white);
        g.drawOval((hueMarkerX - ovalThickness/2) + 1, (hueMarkerY - ovalThickness/2) + 1, ovalThickness - 2, ovalThickness - 2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(hueArea.contains(e.getPoint())){
            selectedAngle = -Math.toDegrees(Math.atan2(e.getX() - getWidth()/2f, e.getY() - getHeight()/2f)) + 180;
            colorPanel.setHue((int) selectedAngle);
            colorPanel.notifyH();
        }
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

    @Override
    public void mouseDragged(MouseEvent e) {
        if(hueArea.contains(e.getPoint())){
            selectedAngle = -Math.toDegrees(Math.atan2(e.getX() - getWidth()/2f, e.getY() - getHeight()/2f)) + 180;
            colorPanel.setHue((int) selectedAngle);
            colorPanel.notifyH();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
                    redrawColorCanvas((float) selectedAngle);
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

        private void redrawColorCanvas(float hue) {
            Color topLeft = Color.white;
            Color bottom = Color.black;
            Color topRight = Color.getHSBColor(hue/360f, 1f, 1f);

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
                int x = Math.min(e.getX(), bilinearColorSquare.getWidth());
                x = Math.max(x, 0);
                int y = Math.min(e.getY(), bilinearColorSquare.getHeight());
                y = Math.max(y, 0);

                colorPanel.setSat((float) x/bilinearColorSquare.getWidth());
                colorPanel.setVal(1 - (float) y/bilinearColorSquare.getHeight());

                selectedX = x;
                selectedY = y;

                colorPanel.notifySV();
            }
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

        @Override
        public void mouseDragged(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                int x = Math.min(e.getX(), bilinearColorSquare.getWidth());
                x = Math.max(x, 0);
                int y = Math.min(e.getY(), bilinearColorSquare.getHeight());
                y = Math.max(y, 0);

                colorPanel.setSat((float) x/bilinearColorSquare.getWidth());
                colorPanel.setVal(1 - (float) y/bilinearColorSquare.getHeight());

                selectedX = x;
                selectedY = y;

                colorPanel.notifySV();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}
