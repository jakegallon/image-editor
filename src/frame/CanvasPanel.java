package frame;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;

public class CanvasPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private final Canvas canvas = new Canvas();
    private Point canvasOffset = new Point(0, 0);

    private volatile Point mousePos;
    private Point initialMousePos;

    private static final float ZOOM_MULTIPLIER = 1.1f;
    private float zoomFactor = 1.0f;
    private float minZoom = 0.01f;
    private float maxZoom = 100f;

    protected final Controller controller;

    private final SwingPropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this);
    private static final String MOUSE_POS_EVENT = "mouse moved";
    private static final String ZOOM_EVENT = "canvas zoomed";

    Tool activeTool = new DebugTool();

    public CanvasPanel(Controller controller, InfoPanel infoPanel) {
        this.controller = controller;
        setBackground(new Color(43, 43 ,43));
        setLayout(null);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        add(canvas);

        propertyChangeSupport.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals(MOUSE_POS_EVENT)){
                infoPanel.setMouseLocation((Point) evt.getNewValue());
            }
            if(evt.getPropertyName().equals(ZOOM_EVENT)){
                calculateZoomOffset(evt);
                infoPanel.setZoomFactor(zoomFactor);
            }
        });
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

    private final float[] snapValues = {0.1f, 0.25f, 0.5f, 0.75f, 1.5f, 2f, 2.5f, 5f, 10f, 50f};
    private float prevUnsnap, nextUnsnap;
    private boolean snapped = false;

    protected void zoomIn() {
        if(zoomFactor == maxZoom) return;

        if(snapped) {
            setZoomFactor(nextUnsnap);
            snapped = false;
            return;
        }

        float oldZoomFactor = zoomFactor;
        float newZoomFactor = zoomFactor * ZOOM_MULTIPLIER;

        for (float snapFactor : snapValues) {
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
        float oldZoomFactor = zoomFactor;
        float newZoomFactor = zoomFactor / ZOOM_MULTIPLIER;

        if(zoomFactor == minZoom) return;

        if(snapped) {
            setZoomFactor(prevUnsnap);
            snapped = false;
            return;
        }

        for (int i = snapValues.length - 1; i >= 0; i--) {
            float snapFactor = snapValues[i];

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
        float zoomRatio = (float) evt.getNewValue() / (float) evt.getOldValue();
        int oldOffsetToMouseX = mousePos.x - canvasOffset.x;
        int oldOffsetToMouseY = mousePos.y - canvasOffset.y;
        int newOffsetToMouseX = (int) (oldOffsetToMouseX / zoomRatio);
        int newOffsetToMouseY = (int) (oldOffsetToMouseY / zoomRatio);
        canvasOffset = new Point(mousePos.x - newOffsetToMouseX, mousePos.y - newOffsetToMouseY);
    }

    private void setZoomFactor(Float zoomFactor) {
        float oldValue = this.zoomFactor;
        this.zoomFactor = zoomFactor;
        propertyChangeSupport.firePropertyChange(ZOOM_EVENT, oldValue, zoomFactor);
    }

    private Point initialCanvasOffset;

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            initialMousePos = mousePos;
            initialCanvasOffset = canvasOffset;
            scrollLocked = true;
        } else {
            activeTool.onMousePressed(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setMousePos(mouseEventPosToAbsolutePos(e.getPoint()));
        if(SwingUtilities.isMiddleMouseButton(e)){
            Point trueMousePos = mouseEventPosToAbsolutePos(e.getPoint());
            Point d = getPointTranslation(initialMousePos, trueMousePos);
            int newX = initialCanvasOffset.x + d.x;
            int newY = initialCanvasOffset.y + d.y;
            canvasOffset = new Point(newX, newY);
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
        if(SwingUtilities.isMiddleMouseButton(e)){
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
        activeTool.onMouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        activeTool.onMouseExited();
    }
}
