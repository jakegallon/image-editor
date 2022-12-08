package tool;

import frame.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SelectTool extends BaseTool {

    public SelectTool() {
        category = ToolCategory.SELECT;
        displayName = "Select";
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

            canvas.selectedArea.width = mousePos.x - initPressPoint.x;
            canvas.selectedArea.height = mousePos.y - initPressPoint.y;
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
