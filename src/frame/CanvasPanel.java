package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CanvasPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private static final float ZOOM_MULTIPLIER = 1.1892071f; //1.189207115002721
    private float zoomFactor = 1.0f;

    private Point middleMousePressPoint;
    private Point initialCanvasPosition;
    private boolean scrollLocked = false;

    JPanel test;

    public CanvasPanel() {
        setBackground(new Color(43, 43 ,43));
        setLayout(null);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // Temporary object
        test = new JPanel();
        test.setBounds(0, 0, 200, 200);
        Point canvasPos = new Point(0, 0);
        test.setLocation(canvasPos);
        test.setBackground(Color.white);
        add(test);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
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

    private Point getLocalizedPoint(Point point){
        float localX = point.x / zoomFactor;
        float localY = point.y / zoomFactor;
        return new Point((int)localX, (int)localY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isMiddleMouseButton(e)){
            middleMousePressPoint = getLocalizedPoint(e.getPoint());
            initialCanvasPosition = test.getLocation();
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
        Point mouseLocation = getLocalizedPoint(e.getPoint());
        if(SwingUtilities.isMiddleMouseButton(e)){
            int xOffset = mouseLocation.x - middleMousePressPoint.x;
            int yOffset = mouseLocation.y - middleMousePressPoint.y;
            test.setLocation(initialCanvasPosition.x + xOffset, initialCanvasPosition.y + yOffset);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
