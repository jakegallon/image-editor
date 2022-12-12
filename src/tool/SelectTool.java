package tool;

import frame.Controller;

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
        canvas.initializeSelectedArea(initPressPoint.x, initPressPoint.y);
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(canvas == null || canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            Point mousePos = canvasPanel.getMousePos();

            int width = mousePos.x - initPressPoint.x;
            int height = mousePos.y - initPressPoint.y;

            canvas.setSelectedAreaOffset(
                    width >= 0 ? initPressPoint.x : initPressPoint.x + 1,
                    height >= 0 ? initPressPoint.y : initPressPoint.y + 1
            );
            canvas.setSelectedAreaSize(
                    width >= 0 ? width + 1 : width - 1,
                    height >= 0 ? height + 1 : height - 1
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
        if(initPressPoint == canvasPanel.getMousePos()) {
            canvas.nullifySelectedArea();
        }
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
