package tool;

import frame.Canvas;
import frame.CanvasPanel;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class BaseTool {

    protected CanvasPanel activeCanvasPanel;
    public static Canvas canvas;

    protected Cursor toolCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    public abstract void onMouseClicked(MouseEvent e);
    public abstract void onMousePressed(MouseEvent e);
    public abstract void onMouseDragged(MouseEvent e);
    public abstract void onMouseReleased(MouseEvent e);

    public void onMouseEntered(CanvasPanel canvasPanel) {
        activeCanvasPanel = canvasPanel;
        activeCanvasPanel.setCursor(toolCursor);
    }
}