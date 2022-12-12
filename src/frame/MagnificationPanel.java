package frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MagnificationPanel extends JLayeredPane {

    private static boolean isZoomedIn;
    private final SpringLayout springLayout = new SpringLayout();
    private final JSlider targetZoomSlider = new JSlider(0, 90, 1);
    private static final JToggleButton automaticModeButton = new JToggleButton();
    private final JToggleButton crosshairButton = new JToggleButton();
    private static final MagnifiedPanelRenderer magnifiedPanelRenderer = new MagnifiedPanelRenderer();

    private static Canvas canvas;
    private static double canvasScaleRatioX;
    private static double canvasScaleRatioY;

    private static Point mousePos;
    private static Point canvasOffset;
    private static double zoomFactor = 1.0;
    private static double targetZoom = 1.0;

    private static boolean isCrosshairEnabled = true;
    private static boolean isAutomaticMode = true;

    public static Dimension viewportDimension = new Dimension();

    private static double canvasWidthHeightRatio = 1.0;

    private static ImageIcon autoOnIcon;
    private static ImageIcon autoOffIcon;
    private static ImageIcon crosshairOnIcon;
    private static ImageIcon crosshairOffIcon;

    public MagnificationPanel() {
        init();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                if(canvas != null) {
                    magnifiedPanelRenderer.updateDrawVariables();
                    magnifiedPanelRenderer.updateZoomVariables();
                    magnifiedPanelRenderer.updateRatioVariables();
                }
            }
        });
    }

    public static void setMousePos(Point p) {
        mousePos = p;
        if(canvas != null) {
            magnifiedPanelRenderer.updateZoomedImageBoundsPosition();
        }
    }

    public static void setZoomFactor(double f) {
        zoomFactor = f;
        magnifiedPanelRenderer.updateZoomVariables();
        magnifiedPanelRenderer.repaint();
    }

    public static void setIsZoomedIn(boolean b) {
        isZoomedIn = b;
    }

    public static void setCanvasOffset(Point p) {
        canvasOffset = p;
    }

    public void setCanvas(Canvas c) {
        canvas = c;

        if(canvas == null) {
            magnifiedPanelRenderer.onCanvasNull();
            return;
        }

        canvasWidthHeightRatio = (double) c.getWidth() / c.getHeight();

        canvasScaleRatioX = (double) getWidth() / canvas.getWidth();
        canvasScaleRatioY = (double) getHeight() / canvas.getHeight();

        magnifiedPanelRenderer.onSetCanvas();
    }

    public static void setViewportDimension(Dimension d) {
        viewportDimension = d;
        magnifiedPanelRenderer.updateDrawVariables();
        magnifiedPanelRenderer.updateZoomVariables();
    }

    private void init() {
        setMinimumSize(new Dimension(0, 250));
        setLayout(springLayout);

        try {
            autoOnIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/auto_on.png"))));
            autoOffIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/auto_off.png"))));
            crosshairOnIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/crosshair_on.png"))));
            crosshairOffIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/crosshair_off.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        automaticModeButton.setPreferredSize(new Dimension(24, 24));
        crosshairButton.setPreferredSize(new Dimension(24, 24));

        add(automaticModeButton, 2, 0);
        automaticModeButton.setIcon(autoOnIcon);
        automaticModeButton.setBorderPainted(false);
        automaticModeButton.setBackground(new Color(0, 0, 0, 0));
        automaticModeButton.setSelected(true);
        add(targetZoomSlider, 2, 0);
        add(crosshairButton, 2, 0);
        crosshairButton.setSelected(true);
        crosshairButton.setIcon(crosshairOnIcon);
        crosshairButton.setBorderPainted(false);
        crosshairButton.setBackground(new Color(0, 0, 0, 0));
        add(magnifiedPanelRenderer, 1, 0);
        putConstraints();

        automaticModeButton.addChangeListener(e -> {
            isAutomaticMode = automaticModeButton.isSelected();
            if(automaticModeButton.isSelected()){
                automaticModeButton.setIcon(autoOnIcon);
            } else {
                automaticModeButton.setIcon(autoOffIcon);
            }
        });
        targetZoomSlider.addChangeListener(e -> setTargetZoom((targetZoomSlider.getValue()) / 10f + 1));
        crosshairButton.addChangeListener(e -> {
            isCrosshairEnabled = crosshairButton.isSelected();
            magnifiedPanelRenderer.repaint();
            if(crosshairButton.isSelected()){
                crosshairButton.setIcon(crosshairOnIcon);
            } else {
                crosshairButton.setIcon(crosshairOffIcon);
            }
        });

    }

    private void setTargetZoom(float f) {
        targetZoom = f;

        if(canvas != null) {
            magnifiedPanelRenderer.updateZoomedImageBoundsSize();
            magnifiedPanelRenderer.updateZoomedImageBoundsPosition();
        }
    }

    private void putConstraints() {
        springLayout.putConstraint(SpringLayout.WEST, automaticModeButton, 5, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, automaticModeButton, -5, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.WEST, targetZoomSlider, 0, SpringLayout.EAST, automaticModeButton);
        springLayout.putConstraint(SpringLayout.EAST, targetZoomSlider, 100, SpringLayout.WEST, targetZoomSlider);
        springLayout.putConstraint(SpringLayout.BASELINE, targetZoomSlider, -8, SpringLayout.SOUTH, automaticModeButton);

        springLayout.putConstraint(SpringLayout.EAST, crosshairButton, -5, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, crosshairButton, -5, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.WEST, magnifiedPanelRenderer, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, magnifiedPanelRenderer, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.NORTH, magnifiedPanelRenderer, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.SOUTH, magnifiedPanelRenderer, 0, SpringLayout.SOUTH, this);
    }

    private static class MagnifiedPanelRenderer extends JPanel{

        int xOffset;
        int yOffset;
        int scaledX;
        int scaledY;
        double baseScale;

        int drawOffsetX;
        int drawOffsetY;
        int drawWidth;
        int drawHeight;

        double pixelsInWidth;
        double pixelsInHeight;

        Rectangle zoomedImageBounds = new Rectangle(1, 1, 1, 1);
        double viewportDimensionRatioWidth;
        double viewportDimensionRatioHeight;

        private MagnifiedPanelRenderer() {
            setBackground(new Color(43, 43 ,43));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(canvas == null) {
                if(isCrosshairEnabled) drawCrosshair(g);
                return;
            }

            if(isAutomaticMode && isZoomedIn) {
                drawFullImage(g);
                drawFullViewArea(g);
            } else {
                drawZoomedImage(g);
                drawZoomedViewArea(g);
            }

            if(isCrosshairEnabled) drawCrosshair(g);
        }

        private void drawCrosshair(Graphics g) {
            g.setColor(new Color(60,63,65));

            g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
            g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
        }

        private void drawZoomedImage(Graphics g) {
            BufferedImage image = canvas.getImage();
            g.drawImage(image, zoomedImageBounds.x, zoomedImageBounds.y, zoomedImageBounds.width, zoomedImageBounds.height, null);
        }

        private void drawFullImage(Graphics g) {
            BufferedImage image = canvas.getImage();
            g.drawImage(image, xOffset, yOffset, scaledX, scaledY, null);
        }

        private void updateZoomedImageBoundsPosition(){
            Point convertedMousePos = convertPosToLocal(mousePos);

            zoomedImageBounds.x = convertedMousePos.x;
            zoomedImageBounds.y = convertedMousePos.y;
            repaint();
        }

        private Point convertPosToLocal(Point p) {
            double x = -p.x * baseScale * targetZoom;
            double y = -p.y * baseScale * targetZoom;

            x += scaledX / 2.0;
            y += scaledY / 2.0;

            x += xOffset;
            y += yOffset;

            return new Point((int) x, (int) y);
        }

        private void updateZoomedImageBoundsSize(){
            zoomedImageBounds.width = (int) (scaledX * targetZoom);
            zoomedImageBounds.height = (int) (scaledY * targetZoom);
            repaint();
        }

        private void drawFullViewArea(Graphics g) {
            g.setColor(Color.red);

            int localX = (int) ((canvasOffset.x + (xOffset / baseScale)) * baseScale);
            int localY = (int) ((canvasOffset.y + (yOffset / baseScale)) * baseScale);
            int scaledWidth = (int) ((pixelsInWidth * baseScale));
            int scaledHeight = (int) ((pixelsInHeight * baseScale));

            g.drawRect(localX, localY, scaledWidth, scaledHeight);
        }

        private void drawZoomedViewArea(Graphics g) {
            g.setColor(Color.red);

            Point correctedCanvasOffset = new Point(
                    (int) ((canvasOffset.x * baseScale) * targetZoom),
                    (int) ((canvasOffset.y * baseScale) * targetZoom)
            );

            int localX = zoomedImageBounds.x + correctedCanvasOffset.x;
            int localY = zoomedImageBounds.y + correctedCanvasOffset.y;
            int scaledWidth = (int) ((pixelsInWidth * baseScale) * targetZoom);
            int scaledHeight = (int) ((pixelsInHeight * baseScale) * targetZoom);

            g.drawRect(localX, localY, scaledWidth, scaledHeight);
        }

        private void updateDrawVariables() {
            if(getWidth() >= getHeight()){
                drawWidth = Math.min(getWidth(), getHeight());
                drawHeight = (int) (getHeight() / canvasWidthHeightRatio);
            } else {
                drawHeight = Math.min(getHeight(), getWidth());
                drawWidth = (int) (getWidth() / canvasWidthHeightRatio);
            }
            drawOffsetX = (getWidth() - drawWidth) / 2;
            drawOffsetY = (getHeight() - drawHeight) / 2;

            //todo move
            if(canvas == null) return;

            double imageWidthPercent = (double) getWidth() / canvas.getWidth();
            double imageHeightPercent = (double) getHeight() / canvas.getHeight();

            baseScale = Math.min(imageWidthPercent, imageHeightPercent);
            scaledX = (int) (canvas.getWidth() * baseScale);
            scaledY = (int) (canvas.getHeight() * baseScale);
            xOffset = (getWidth() - scaledX) / 2;
            yOffset = (getHeight() - scaledY) / 2;
        }

        private void updateRatioVariables() {
            viewportDimensionRatioWidth = (double) getWidth() / canvas.getWidth();
            viewportDimensionRatioHeight = (double) getHeight() / canvas.getHeight();
        }

        private void updateZoomVariables() {
            pixelsInWidth = viewportDimension.width / zoomFactor;
            pixelsInHeight = viewportDimension.height / zoomFactor;
        }

        private void onSetCanvas() {
            updateDrawVariables();
            updateZoomVariables();
            updateRatioVariables();

            updateZoomedImageBoundsSize();
            updateZoomedImageBoundsPosition();

            repaint();
        }

        private void onCanvasNull() {
            repaint();
        }
    }
}
