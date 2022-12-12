package tool;

import frame.Controller;
import frame.NotificationMessage;
import frame.NotificationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SelectTool extends BaseTool {

    public SelectTool() {
        category = ToolCategory.SELECT;
        displayName = "Select";
        toolCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public void attachProperties() {

    }

    @Override
    protected void onLeftMouseClicked() {
    }

    @Override
    protected void onLeftMousePressed() {
        if(canvas == null || canvas.getActiveLayer() == null) return;

        Point mousePos = canvasPanel.getMousePos();
        if(mousePos.x < 0 || mousePos.x > canvas.getWidth()) {
            initPressPoint = null;
            NotificationPanel.playNotification(NotificationMessage.TOOL_PRESS_OOB);
            return;
        }
        if(mousePos.y < 0 || mousePos.y > canvas.getHeight()) {
            initPressPoint = null;
            NotificationPanel.playNotification(NotificationMessage.TOOL_PRESS_OOB);
            return;
        }
        canvas.initializeSelectedArea(initPressPoint.x, initPressPoint.y);
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(canvas == null || canvas.getActiveLayer() == null) return;
        if(initPressPoint == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            Point mousePos = canvasPanel.getMousePos();

            int width;
            int height;

            if(mousePos.x < 0 || mousePos.x >= canvas.getWidth()) {
                if(mousePos.x < 0) {
                    width = -initPressPoint.x;
                    canvas.setSelectedAreaWidth(width >= 0 ? width + 1 : width - 1);
                } else {
                    width = canvas.getWidth() - initPressPoint.x;
                    canvas.setSelectedAreaWidth(width);
                }
            } else {
                width = mousePos.x - initPressPoint.x;
                canvas.setSelectedAreaWidth(width >= 0 ? width + 1 : width - 1);
            }
            if(mousePos.y < 0 || mousePos.y >= canvas.getHeight()) {
                if(mousePos.y < 0) {
                    height = -initPressPoint.y;
                    canvas.setSelectedAreaHeight(height >= 0 ? height + 1 : height - 1);
                } else {
                    height = canvas.getHeight() - initPressPoint.y;
                    canvas.setSelectedAreaHeight(height);
                }
            } else {
                height = mousePos.y - initPressPoint.y;
                canvas.setSelectedAreaHeight(height >= 0 ? height + 1 : height - 1);
            }

            canvas.setSelectedAreaOffset(
                    width >= 0 ? initPressPoint.x : initPressPoint.x + 1,
                    height >= 0 ? initPressPoint.y : initPressPoint.y + 1
            );
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = canvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    @Override
    protected void onLeftMouseDragged() {
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(initPressPoint == null) return;
        if(initPressPoint == canvasPanel.getMousePos()) {
            canvas.nullifySelectedArea();
            return;
        }
        canvas.checkSelectedArea();
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
