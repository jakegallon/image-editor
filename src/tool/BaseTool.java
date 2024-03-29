package tool;

import frame.Canvas;
import frame.CanvasPanel;
import frame.Controller;
import frame.Frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public abstract class BaseTool {

    public ToolCategory category;
    public String displayName;

    protected static CanvasPanel canvasPanel;
    public static Canvas canvas;

    protected Point initPressPoint;

    public Cursor toolCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    protected final Cursor blockedCursor = getCursor(CustomCursor.BLOCKED, CursorOffset.CENTERED);

    public abstract void attachProperties();

    protected abstract void onLeftMousePressed();
    protected abstract void onLeftMouseDragged();
    protected abstract void onLeftMouseReleased();

    public void populateSettingsPanel() {
        attachProperties();
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
            Frame.notificationPanel.setCursor(blockedCursor);
        } else {
            canvasPanel.setCursor(toolCursor);
            Frame.notificationPanel.setCursor(toolCursor);
        }
    }

    protected void setSelectedColorToMousePosColor() {
        if(!canvasPanel.isMouseOverCanvas()) return;

        Color color = canvasPanel.getColorAtMousePos();
        Controller.setSelectedColor(color);
    }

    protected enum CustomCursor {
        GENERIC_TOOL,
        BLOCKED,
        EYE_TOOL,
        FILL_TOOL
    }

    protected enum CursorOffset {
        CENTERED,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    protected Cursor getCursor(CustomCursor customCursor, CursorOffset cursorOffset) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        String name = customCursor.toString().toLowerCase();

        try {
            Image cursorIcon;
            switch (customCursor) {
                case GENERIC_TOOL -> cursorIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/generic_tool.png")));
                case BLOCKED -> cursorIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/blocked.png")));
                case EYE_TOOL -> cursorIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/eye.png")));
                case FILL_TOOL -> cursorIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/fill.png")));
                default -> throw new IllegalStateException("Unexpected value: " + customCursor);
            }
            Dimension d = toolkit.getBestCursorSize(cursorIcon.getWidth(null), cursorIcon.getHeight(null));
            switch (cursorOffset) {
                case CENTERED -> {
                    return toolkit.createCustomCursor(cursorIcon, new Point(d.width/2, d.height/2), name);
                }
                case TOP_LEFT -> {
                    return toolkit.createCustomCursor(cursorIcon, new Point(d.width - 1, d.height - 1), name);
                }
                case TOP_RIGHT -> {
                    return toolkit.createCustomCursor(cursorIcon, new Point(0, d.height - 1), name);
                }
                case BOTTOM_LEFT -> {
                    return toolkit.createCustomCursor(cursorIcon, new Point(d.width - 1, 0), name);
                }
                case BOTTOM_RIGHT -> {
                    return toolkit.createCustomCursor(cursorIcon, new Point(0, 0), name);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}