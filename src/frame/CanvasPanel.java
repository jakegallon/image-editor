package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CanvasPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private static final float ZOOM_MULTIPLIER = 1.1892071f; //1.189207115002721
    private float zoomFactor = 1.0f;

    private int x = 0, y = 0;
    private Point initialGraphicsOffset;
    private Point middleMousePressPoint;

    private boolean scrollLocked = false;

    private final Canvas canvas = new Canvas();

    public CanvasPanel() {
        setBackground(new Color(43, 43 ,43));
        setLayout(null);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        add(canvas);
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
        float newZoomFactor = zoomFactor * ZOOM_MULTIPLIER;
        if(newZoomFactor <= 4096.0f){
            zoomFactor = newZoomFactor;
        }
    }

    protected void zoomOut() {
        float newZoomFactor = zoomFactor / ZOOM_MULTIPLIER;
        if(newZoomFactor >= 0.01f){
            zoomFactor = newZoomFactor;
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
        // start tracking mouse position relative to canvas
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // stop tracking mouse position relative to canvas
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
    }
}
