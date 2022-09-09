package tool;

import java.awt.event.MouseEvent;

public abstract class LayerTool extends BaseTool {

    @Override
    public void onMouseClicked(MouseEvent e) {
        if(canvas.getActiveLayer() == null) {
            return;
        }
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if(canvas.getActiveLayer() == null) {
            return;
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(canvas.getActiveLayer() == null) {
            return;
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(canvas.getActiveLayer() == null) {
            return;
        }
    }
}
