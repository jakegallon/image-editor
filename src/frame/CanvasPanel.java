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

    private int x = 0, y = 0;
    private Point initialGraphicsOffset;
    private Point middleMousePressPoint;

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(x, y);
        g2d.scale(zoomFactor, zoomFactor);
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(scrollLocked) return;
        boolean scrollingUp = (e.getWheelRotation() == -1);
        if(scrollingUp){
            zoomIn();
            return;
        }
        zoomOut();
    }

    protected void zoomIn() {
        if(zoomFactor == 0.01f){
            setZoomFactor(closeMinZoom);
            return;
        }
        if(zoomFactor == 100.0f) {
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
        if(zoomFactor == 100.0f){
            setZoomFactor(closeMaxZoom);
            return;
        }
        if(zoomFactor == 0.01f) {
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

    private Point getAbsolutePoint(Point point){
        float localX = point.x + x;
        float localY = point.y + y;
        return new Point((int)localX, (int)localY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            initialGraphicsOffset = new Point(x, y);
            middleMousePressPoint = getAbsolutePoint(e.getPoint());
            scrollLocked = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            scrollLocked = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // mouse cursor change
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            x = initialGraphicsOffset.x * 2 + e.getPoint().x - middleMousePressPoint.x;
            y = initialGraphicsOffset.y * 2 + e.getPoint().y - middleMousePressPoint.y;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMousePos(e.getPoint());
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
