package frame;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.awt.event.*;

public class CanvasPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private static final String MOUSE_POS_EVENT = "mouse moved";
    private static final String ZOOM_EVENT = "canvas zoomed";

    private static final float ZOOM_MULTIPLIER = 1.1f; //1.189207115002721
    private float zoomFactor = 1.0f;
    private float closeMinZoom;
    private float closeMaxZoom;

    private Point canvasOffset;
    private Point initialCanvasOffset;

    private boolean scrollLocked = false;

    private final Canvas canvas = new Canvas();

    private volatile Point mousePos;
    private final SwingPropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this);

    public CanvasPanel(InfoPanel infoPanel) {
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
                infoPanel.setZoomFactor(zoomFactor);
            }
        });
        canvasOffset = new Point(0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(-canvasOffset.x*zoomFactor, -canvasOffset.y*zoomFactor);
        g2d.scale(zoomFactor, zoomFactor);
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(scrollLocked) return;
        Point scrollPoint = mousePos;
        float oldZoomFactor = zoomFactor;
        int relX = scrollPoint.x - canvasOffset.x;
        int relY = scrollPoint.y - canvasOffset.y;
        boolean scrollingUp = (e.getWheelRotation() == -1);
        if(scrollingUp){
            zoomIn();
            int newX = (int) (relX / (zoomFactor / oldZoomFactor));
            int newY = (int) (relY / (zoomFactor / oldZoomFactor));
            canvasOffset = new Point(scrollPoint.x - newX, scrollPoint.y - newY);
            return;
        }
        zoomOut();
        int newX = (int) (relX / (zoomFactor / oldZoomFactor));
        int newY = (int) (relY / (zoomFactor / oldZoomFactor));
        canvasOffset = new Point(scrollPoint.x - newX, scrollPoint.y - newY);
    }

    protected void zoomIn() {
        if(zoomFactor == 100.0f) {
            return;
        }
        if(zoomFactor == 0.01f){
            setZoomFactor(closeMinZoom);
            return;
        }
        float newZoomFactor = zoomFactor * ZOOM_MULTIPLIER;
        if(newZoomFactor <= 100.0f){
            setZoomFactor(newZoomFactor);
        } else {
            closeMaxZoom = zoomFactor;
            setZoomFactor(100.0f);
        }
    }

    protected void zoomOut() {
        if(zoomFactor == 0.01f) {
            return;
        }
        if(zoomFactor == 100.0f){
            setZoomFactor(closeMaxZoom);
            return;
        }
        float newZoomFactor = zoomFactor / ZOOM_MULTIPLIER;
        if(newZoomFactor >= 0.01f){
            setZoomFactor(newZoomFactor);
        } else {
            closeMinZoom = zoomFactor;
            setZoomFactor(0.01f);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            initialCanvasOffset = canvasOffset;
            scrollLocked = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            Point trueMousePos = mouseEventPosToAbsolutePos(e.getPoint());
            Point d = getPointTranslation(mousePos, trueMousePos);
            int newX = initialCanvasOffset.x + d.x;
            int newY = initialCanvasOffset.y + d.y;
            canvasOffset = new Point(newX, newY);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            scrollLocked = false;
        }
    }

    private Point getPointTranslation(Point p1, Point p2){
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        return new Point(dx, dy);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // mouse cursor change
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMousePos(mouseEventPosToAbsolutePos(e.getPoint()));
    }

    private Point mouseEventPosToAbsolutePos(Point p) {
        int mX = (int) (p.x / zoomFactor) + canvasOffset.x;
        int mY = (int) (p.y / zoomFactor) + canvasOffset.y;
        return new Point(mX, mY);
    }

    private void setMousePos(Point p) {
        Point oldValue = mousePos;
        mousePos = p;
        propertyChangeSupport.firePropertyChange(MOUSE_POS_EVENT, oldValue, p);
    }

    private void setZoomFactor(Float zoomFactor) {
        float oldValue = this.zoomFactor;
        this.zoomFactor = zoomFactor;
        propertyChangeSupport.firePropertyChange(ZOOM_EVENT, oldValue, zoomFactor);
    }
}
