package frame;

import tool.BaseTool;
import tool.MoveTool;
import tool.PenTool;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;

public class CanvasPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private Canvas canvas;
    private Point canvasOffset = new Point(0, 0);

    private volatile Point mousePos;
    private Point initialMousePos;

    private static final double ZOOM_MULTIPLIER = 1.1;
    private static double zoomFactor = 1.0;
    public static double fullscreenZoomFactor = 1.0;

    private final SwingPropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this);
    private static final String MOUSE_POS_EVENT = "mouse moved";
    private static final String ZOOM_EVENT = "canvas zoomed";
    private static final String OFFSET_EVENT = "canvas offset altered";

    public BaseTool activeTool = new PenTool();

    public CanvasPanel() {
        init();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                if(canvas != null) {
                    updateFullscreenZoomFactor();
                    MagnificationPanel.setViewportDimension(getSize());
                }
            }
        });
    }

    public void setCanvas(Canvas canvas) {
        removeCurrentCanvas();
        add(canvas);
        this.canvas = canvas;
        zoomToFitCanvas();
        MagnificationPanel.setViewportDimension(getSize());
        GridPanel.onCanvasAdded();
    }

    private void removeCurrentCanvas() {
        if(canvas == null) return;
        remove(canvas);
    }

    private void zoomToFitCanvas() {
        updateFullscreenZoomFactor();

        setZoomFactor(fullscreenZoomFactor / ZOOM_MULTIPLIER);

        setCanvasOffset(getOffsetToCenterCanvas());
    }

    private void updateFullscreenZoomFactor() {
        double targetZoomFactor = calculateTargetZoomFactor();
        fullscreenZoomFactor = ((float) getAbsoluteZoomFactor(targetZoomFactor));
    }

    private double calculateTargetZoomFactor() {
        float canvasWidthPercent = (float) canvas.getWidth() / getWidth();
        float canvasHeightPercent = (float) canvas.getHeight() / getHeight();
        float canvasPercent = Math.max(canvasWidthPercent, canvasHeightPercent);

        return 1 / canvasPercent;
    }

    private double getAbsoluteZoomFactor(double targetZoomFactor) {
        double zoomIterations = Math.log(targetZoomFactor) / Math.log(ZOOM_MULTIPLIER);

        if(targetZoomFactor > 1.0)
            zoomIterations = Math.floor(zoomIterations);
        else
            zoomIterations = Math.ceil(zoomIterations);

        return Math.pow(ZOOM_MULTIPLIER, zoomIterations);
    }

    private Point getOffsetToCenterCanvas() {
        Dimension canvasDimension = canvas.getSize();
        return  getOffsetToCenterCanvas(canvasDimension);
    }

    private Point getOffsetToCenterCanvas(Dimension canvasDimension) {
        int currentWidth = (int) (getWidth() / zoomFactor);
        int currentHeight = (int) (getHeight() / zoomFactor);

        int xOffset = (currentWidth - canvasDimension.width) / 2;
        int yOffset = (currentHeight - canvasDimension.height) / 2;

        return new Point(-xOffset, -yOffset);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(-canvasOffset.x*zoomFactor, -canvasOffset.y*zoomFactor);
        g2d.scale(zoomFactor, zoomFactor);
        repaint();
    }

    private boolean scrollLocked = false;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(scrollLocked) return;

        boolean scrollingUp = e.getWheelRotation() < 0;
        if(scrollingUp){
            zoomIn();
        } else {
            zoomOut();
        }
    }

    private final double[] snapValues = {0.1f, 0.25f, 0.5f, 0.75f, 1.5f, 2f, 2.5f, 5f, 10f, 50f};
    private double prevUnsnap, nextUnsnap;
    private boolean snapped = false;

    protected void zoomIn() {
        double maxZoom = 100f;
        if(zoomFactor == maxZoom) return;

        if(snapped) {
            setZoomFactor(nextUnsnap);
            snapped = false;
            return;
        }

        double oldZoomFactor = zoomFactor;
        double newZoomFactor = zoomFactor * ZOOM_MULTIPLIER;

        for (double snapFactor : snapValues) {
            if(oldZoomFactor > snapFactor) continue;
            if(newZoomFactor < snapFactor) break;

            snapped = true;
            prevUnsnap = zoomFactor;
            nextUnsnap = newZoomFactor;
            setZoomFactor(snapFactor);
            return;
        }

        if(newZoomFactor <= maxZoom) {
            setZoomFactor(newZoomFactor);
        } else {
            snapped = true;
            prevUnsnap = zoomFactor;
            nextUnsnap = 1.0f;
            setZoomFactor(maxZoom);
        }
    }

    protected void zoomOut() {
        double oldZoomFactor = zoomFactor;
        double newZoomFactor = zoomFactor / ZOOM_MULTIPLIER;

        double minZoom = 0.01f;
        if(zoomFactor == minZoom) return;

        if(snapped) {
            setZoomFactor(prevUnsnap);
            snapped = false;
            return;
        }

        for (int i = snapValues.length - 1; i >= 0; i--) {
            double snapFactor = snapValues[i];

            if(oldZoomFactor < snapFactor) continue;
            if(newZoomFactor > snapFactor) break;

            snapped = true;
            prevUnsnap = newZoomFactor;
            nextUnsnap = zoomFactor;
            setZoomFactor(snapFactor);
            return;
        }

        if(newZoomFactor >= minZoom){
            setZoomFactor(newZoomFactor);
        } else {
            snapped = true;
            prevUnsnap = 1.0f;
            nextUnsnap = zoomFactor;
            setZoomFactor(minZoom);
        }
    }

    private void calculateZoomOffset(PropertyChangeEvent evt) {
        double zoomRatio = (double) evt.getNewValue() / (double) evt.getOldValue();
        int oldOffsetToMouseX = mousePos.x - canvasOffset.x;
        int oldOffsetToMouseY = mousePos.y - canvasOffset.y;
        int newOffsetToMouseX = (int) (oldOffsetToMouseX / zoomRatio);
        int newOffsetToMouseY = (int) (oldOffsetToMouseY / zoomRatio);
        setCanvasOffset(new Point(mousePos.x - newOffsetToMouseX, mousePos.y - newOffsetToMouseY));
    }

    private void setZoomFactor(Double d) {
        double oldValue = zoomFactor;
        zoomFactor = d;
        propertyChangeSupport.firePropertyChange(ZOOM_EVENT, oldValue, zoomFactor);
    }

    private void setCanvasOffset(Point p) {
        Point oldValue = canvasOffset;
        canvasOffset = p;
        propertyChangeSupport.firePropertyChange(OFFSET_EVENT, oldValue, canvasOffset);
    }

    private Point initialCanvasOffset;

    @Override
    public void mousePressed(MouseEvent e) {
        if(shouldScroll(e)){
            initialMousePos = mousePos;
            initialCanvasOffset = canvasOffset;
            scrollLocked = true;
        } else {
            activeTool.onMousePressed(e);
        }
    }

    private boolean shouldScroll(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)) return true;
        return SwingUtilities.isLeftMouseButton(e) && activeTool instanceof MoveTool;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setMousePos(mouseEventPosToAbsolutePos(e.getPoint()));
        if(shouldScroll(e)){
            Point trueMousePos = mouseEventPosToAbsolutePos(e.getPoint());
            Point d = getPointTranslation(initialMousePos, trueMousePos);
            int newX = initialCanvasOffset.x + d.x;
            int newY = initialCanvasOffset.y + d.y;
            setCanvasOffset(new Point(newX, newY));
        } else {
            activeTool.onMouseDragged(e);
        }
    }

    private Point mouseEventPosToAbsolutePos(Point p) {
        int mX = (int) (p.x / zoomFactor) + canvasOffset.x;
        int mY = (int) (p.y / zoomFactor) + canvasOffset.y;
        return new Point(mX, mY);
    }

    private Point getPointTranslation(Point p1, Point p2){
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        return new Point(dx, dy);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(shouldScroll(e)){
            scrollLocked = false;
        } else {
            activeTool.onMouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMousePos(mouseEventPosToAbsolutePos(e.getPoint()));
    }

    private void setMousePos(Point p) {
        Point oldValue = mousePos;
        mousePos = p;
        propertyChangeSupport.firePropertyChange(MOUSE_POS_EVENT, oldValue, p);
    }

    public Point getMousePos() {
        return mousePos;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        activeTool.onMouseClicked(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(activeTool == null) return; //todo remove
        activeTool.onMouseEntered(this);
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void init() {
        setBackground(new Color(43, 43 ,43));
        setLayout(null);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        propertyChangeSupport.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals(MOUSE_POS_EVENT)){
                InfoPanel.setMouseLocation(mousePos);
                MagnificationPanel.setMousePos(mousePos);
            }
            if(evt.getPropertyName().equals(ZOOM_EVENT)){
                calculateZoomOffset(evt);
                InfoPanel.setZoomFactor(zoomFactor);
                MagnificationPanel.setZoomFactor(zoomFactor);
                MagnificationPanel.setIsZoomedIn(zoomFactor > fullscreenZoomFactor);
            }
            if(evt.getPropertyName().equals(OFFSET_EVENT)){
                MagnificationPanel.setCanvasOffset(canvasOffset);
            }
        });
    }

    public Color getColorAtMousePos() {
        return canvas.getColorAtPoint(mousePos);
    }

    public boolean isMouseOverCanvas() {
        if(canvas == null) return false;
        return canvas.contains(mousePos);
    }
}
