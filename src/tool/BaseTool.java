package tool;

import frame.Canvas;
import frame.CanvasPanel;
import frame.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public abstract class BaseTool {

    public ToolCategory category;
    public String displayName;

    protected static CanvasPanel canvasPanel;
    public static Canvas canvas;

    protected Point initPressPoint;

    public Cursor toolCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    protected Cursor blockedCursor = getCursor(CustomCursor.BLOCKED);

    public abstract void attachProperties();

    protected abstract void onLeftMouseClicked();
    protected abstract void onLeftMousePressed();
    protected abstract void onLeftMouseDragged();
    protected abstract void onLeftMouseReleased();

    public void populateSettingsPanel() {
        attachProperties();
    }

    public void onMouseClicked(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            onLeftMouseClicked();
        } else if (SwingUtilities.isRightMouseButton(e)) {

        }
    }

    public void onMousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            initPressPoint = canvasPanel.getMousePos();

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

    public void onMouseEntered(CanvasPanel panel) {
        canvasPanel = panel;
        if(canvas == null) {
            canvasPanel.setCursor(blockedCursor);
        } else {
            canvasPanel.setCursor(toolCursor);
        }
    }

    protected void setSelectedColorToMousePosColor() {
        if(!canvasPanel.isMouseOverCanvas()) return;

        Color color = canvasPanel.getColorAtMousePos();
        Controller.setSelectedColor(color);
    }

    protected enum CustomCursor {
        BLOCKED
    }

    protected Cursor getCursor(CustomCursor customCursor) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        String name = customCursor.toString().toLowerCase();

        File target = new File("./src/res/" + name  + ".png");
        try {
            Image cursorIcon = ImageIO.read(target);
            return toolkit.createCustomCursor(cursorIcon, new Point(0, 0), name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}