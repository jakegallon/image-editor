package frame;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DebugTool extends Tool {
    @Override
    public void onMouseClicked(MouseEvent e) {
        Point mousePos = activeCanvasPanel.getMousePos();
        Color selectedColor = activeCanvasPanel.controller.getSelectedColor();
        canvas.setPixel(mousePos, selectedColor);
    }

    @Override
    public void onMousePressed(MouseEvent e) {

    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        Point mousePos = activeCanvasPanel.getMousePos();
        Color selectedColor = activeCanvasPanel.controller.getSelectedColor();
        canvas.setPixel(mousePos, selectedColor);
    }

    @Override
    public void onMouseReleased(MouseEvent e) {

    }
}
