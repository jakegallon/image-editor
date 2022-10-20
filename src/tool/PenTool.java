package tool;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class PenTool extends LayerTool {

    @Override
    public void onMouseClicked(MouseEvent e) {

    }

    @Override
    public void onMousePressed(MouseEvent e) {
        super.onMousePressed(e);
        if(SwingUtilities.isLeftMouseButton(e)) {

        }
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        super.onMouseDragged(e);
        if(SwingUtilities.isLeftMouseButton(e)) {
            actionLayer.drawLine(lastDragPoint, thisDragPoint, 1);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        super.onMouseReleased(e);
        if(SwingUtilities.isLeftMouseButton(e)) {

        }
    }
}
