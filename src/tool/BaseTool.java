package tool;

import frame.Canvas;
import frame.CanvasPanel;
import frame.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class BaseTool {

    public ToolCategory category;
    public String displayName;

    protected CanvasPanel activeCanvasPanel;
    public static Canvas canvas;

    protected Point initPressPoint;

    protected Cursor toolCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    public abstract void attachProperties(JPanel panel);

    protected abstract void onLeftMouseClicked();
    protected abstract void onLeftMousePressed();
    protected abstract void onLeftMouseDragged();
    protected abstract void onLeftMouseReleased();

    public void populateSettingsPanel(JPanel panel) {
        attachProperties(panel);
    }

    public void onMouseClicked(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            onLeftMouseClicked();
        } else if (SwingUtilities.isRightMouseButton(e)) {

        }
    }

    public void onMousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            initPressPoint = activeCanvasPanel.getMousePos();

            onLeftMousePressed();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            setSelectedColorToMousePosColor();
        }
    }

    public void onMouseDragged(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            onLeftMouseDragged();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            setSelectedColorToMousePosColor();
        }
    }

    public void onMouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            initPressPoint = null;

            onLeftMouseReleased();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            setSelectedColorToMousePosColor();
        }
    }

    public void onMouseEntered(CanvasPanel canvasPanel) {
        activeCanvasPanel = canvasPanel;
        activeCanvasPanel.setCursor(toolCursor);
    }

    protected void setSelectedColorToMousePosColor() {
        if(!activeCanvasPanel.isMouseOverCanvas()) return;

        Color color = activeCanvasPanel.getColorAtMousePos();
        Controller.setSelectedColor(color);
    }
}