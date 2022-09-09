package tool;

import action.EditAction;
import action.PixelChange;
import frame.Layer;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DebugTool extends LayerTool {

    public void onMouseClicked(MouseEvent e) {
        super.onMouseClicked(e);

        Point mousePos = activeCanvasPanel.getMousePos();
        Layer targetLayer = canvas.getActiveLayer();
        Color selectedColor = activeCanvasPanel.controller.getSelectedColor();

        EditAction thisAction = new EditAction(targetLayer, new PixelChange[]{new PixelChange(mousePos.x, mousePos.y, new Color(targetLayer.getImage().getRGB(mousePos.x, mousePos.y), true), selectedColor)});
        canvas.actions.add(thisAction);

        targetLayer.paint(mousePos.x, mousePos.y, selectedColor);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        super.onMousePressed(e);

    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        super.onMouseDragged(e);

    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        super.onMouseReleased(e);

    }
}
