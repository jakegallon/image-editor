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

    private final DoublePoint canvasOffset = new DoublePoint(0, 0);

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
        if(canvas == null) return;

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

    private DoublePoint getOffsetToCenterCanvas() {
        Dimension canvasDimension = canvas.getSize();
        return  getOffsetToCenterCanvas(canvasDimension);
    }

    private DoublePoint getOffsetToCenterCanvas(Dimension canvasDimension) {
        double currentWidth = getWidth() / zoomFactor;
        double currentHeight = getHeight() / zoomFactor;

        double xOffset = (currentWidth - canvasDimension.width) / 2.0;
        double yOffset = (currentHeight - canvasDimension.height) / 2.0;

        return new DoublePoint(-xOffset, -yOffset);
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
        double oldOffsetToMouseX = mousePos.x - canvasOffset.x;
        double oldOffsetToMouseY = mousePos.y - canvasOffset.y;
        double newOffsetToMouseX = oldOffsetToMouseX / zoomRatio;
        double newOffsetToMouseY = oldOffsetToMouseY / zoomRatio;
        setCanvasOffset(mousePos.x - newOffsetToMouseX, mousePos.y - newOffsetToMouseY);
    }

    private void setZoomFactor(Double d) {
        double oldValue = zoomFactor;
        zoomFactor = d;
        propertyChangeSupport.firePropertyChange(ZOOM_EVENT, oldValue, zoomFactor);
    }

    private void setCanvasOffset(DoublePoint p) {
        setCanvasOffset(p.x, p.y);
    }

    private void setCanvasOffset(double x, double y) {
        DoublePoint oldVal = new DoublePoint(canvasOffset.x, canvasOffset.y);
        canvasOffset.x = x;
        canvasOffset.y = y;
        propertyChangeSupport.firePropertyChange(OFFSET_EVENT, oldVal, canvasOffset);
    }

    private DoublePoint initialCanvasOffset;

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
        setMousePos(mouseEventPosToAbsolutePos(e.getPoint()).toIntPoint());
        if(shouldScroll(e)){
            DoublePoint trueMousePos = mouseEventPosToAbsolutePos(e.getPoint());
            DoublePoint d = getPointTranslation(DoublePoint.toDoublePoint(initialMousePos), trueMousePos);
            double newX = initialCanvasOffset.x + d.x;
            double newY = initialCanvasOffset.y + d.y;
            setCanvasOffset(newX, newY);
        } else {
            activeTool.onMouseDragged(e);
        }
    }

    private DoublePoint mouseEventPosToAbsolutePos(Point p) {
        double mX = (p.x / zoomFactor) + canvasOffset.x;
        double mY = (p.y / zoomFactor) + canvasOffset.y;
        return new DoublePoint(mX, mY);
    }

    private DoublePoint getPointTranslation(DoublePoint p1, DoublePoint p2){
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return new DoublePoint(dx, dy);
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
        setMousePos(mouseEventPosToAbsolutePos(e.getPoint()).toIntPoint());
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
                MagnificationPanel.setCanvasOffset(((DoublePoint) evt.getNewValue()).toIntPoint());
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
