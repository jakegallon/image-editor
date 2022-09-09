package tool;


import frame.Layer;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DebugTool extends LayerTool {

    public void onMouseClicked(MouseEvent e) {
        super.onMouseClicked(e);
        Point mousePos = activeCanvasPanel.getMousePos();
        Color selectedColor = activeCanvasPanel.controller.getSelectedColor();

        Layer targetLayer = canvas.getActiveLayer();
        targetLayer.paint(mousePos, 10, selectedColor);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        super.onMousePressed(e);

        onMouseDragged(e);
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        super.onMouseDragged(e);

        Point mousePos = activeCanvasPanel.getMousePos();
        Color selectedColor = activeCanvasPanel.controller.getSelectedColor();

        Layer targetLayer = canvas.getActiveLayer();
        targetLayer.paint(mousePos, 10, selectedColor);
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        super.onMouseReleased(e);
    }
}
