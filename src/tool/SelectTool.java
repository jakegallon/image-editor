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
        canvas.selectedArea = new Rectangle(0, 0, 0, 0);
        canvas.selectedArea.x = initPressPoint.x;
        canvas.selectedArea.y = initPressPoint.y;
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(canvas == null || canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            Point mousePos = canvasPanel.getMousePos();

            int width = mousePos.x - initPressPoint.x;
            int height = mousePos.y - initPressPoint.y;

            canvas.selectedArea.x = width >= 0 ? initPressPoint.x : initPressPoint.x + 1;
            canvas.selectedArea.y = height >= 0 ? initPressPoint.y : initPressPoint.y + 1;
            canvas.selectedArea.width = width >= 0 ? width + 1 : width - 1;
            canvas.selectedArea.height = height >= 0 ? height + 1 : height - 1;
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
    protected void onLeftMouseReleased() {

    }
}
