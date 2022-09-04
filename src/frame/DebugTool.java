package frame;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DebugTool extends Tool {
    @Override
    public void onMouseClicked(MouseEvent e) {
        if(notActiveLayerExist()) return;

        Point mousePos = activeCanvasPanel.getMousePos();
        Color selectedColor = activeCanvasPanel.controller.getSelectedColor();

        Layer targetLayer = canvas.getActiveLayer();
        targetLayer.paint(mousePos, 10, selectedColor);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        onMouseDragged(e);
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(notActiveLayerExist()) return;

        Point mousePos = activeCanvasPanel.getMousePos();
        Color selectedColor = activeCanvasPanel.controller.getSelectedColor();

        Layer targetLayer = canvas.getActiveLayer();
        targetLayer.paint(mousePos, 10, selectedColor);
    }

    @Override
    public void onMouseReleased(MouseEvent e) {

    }
}
