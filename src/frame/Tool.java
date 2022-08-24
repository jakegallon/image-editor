package frame;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class Tool {

    protected CanvasPanel activeCanvasPanel;
    protected Canvas canvas;

    public abstract void onMouseClicked(MouseEvent e);

    public abstract void onMousePressed(MouseEvent e);
    public abstract void onMouseDragged(MouseEvent e);
    public abstract void onMouseReleased(MouseEvent e);

    public void onMouseEntered(MouseEvent e) {
        activeCanvasPanel = (CanvasPanel) e.getComponent();
        canvas = activeCanvasPanel.getCanvas();
        activeCanvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void onMouseExited() {
        activeCanvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}