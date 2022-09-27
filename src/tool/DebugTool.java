package tool;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class DebugTool extends LayerTool {

    public void onMouseClicked(MouseEvent e) {
        if(canvas.getActiveLayer() == null) return;
        super.onMouseClicked(e);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            if(canvas.getActiveLayer() == null) return;
            super.onMousePressed(e);

            onMouseClicked(e);
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            if(canvas.getActiveLayer() == null) return;
            super.onMouseDragged(e);

            actionLayer.drawLine(lastDragPoint, thisDragPoint, 1);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            if(canvas.getActiveLayer() == null) return;
            super.onMouseReleased(e);
        }
    }
}
