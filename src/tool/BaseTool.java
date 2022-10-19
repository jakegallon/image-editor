package tool;

import frame.Canvas;
import frame.CanvasPanel;
import frame.Controller;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class BaseTool {

    protected CanvasPanel activeCanvasPanel;

    public abstract void onMouseClicked(MouseEvent e);
    public abstract void onMousePressed(MouseEvent e);
    public abstract void onMouseDragged(MouseEvent e);
    public abstract void onMouseReleased(MouseEvent e);

    public void onMouseEntered(MouseEvent e) {
        activeCanvasPanel = (CanvasPanel) e.getComponent();
        activeCanvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void onMouseExited() {
        activeCanvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected Canvas getCanvas() {
        return Controller.getActiveCanvas();
    }
}