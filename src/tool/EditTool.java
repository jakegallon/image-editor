package tool;

import action.EditAction;
import action.PixelChange;
import frame.Controller;
import frame.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class EditTool extends BaseTool {

    private Layer originalLayer;
    protected Layer actionLayer;
    protected Point lastDragPoint;
    protected Point thisDragPoint;

    @Override
    public void onMousePressed(MouseEvent e) {
        if(canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            originalLayer = canvas.getActiveLayer();
            actionLayer = new Layer(canvas);
            canvas.addLayer(actionLayer);

            initPressPoint = activeCanvasPanel.getMousePos();
            lastDragPoint = initPressPoint;
            thisDragPoint = lastDragPoint;

            onLeftMousePressed();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = activeCanvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            lastDragPoint = thisDragPoint;
            thisDragPoint = activeCanvasPanel.getMousePos();

            onLeftMouseDragged();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = activeCanvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            initPressPoint = null;
            lastDragPoint = null;
            thisDragPoint = null;

            ArrayList<PixelChange> pixelChanges = originalLayer.getImageDifferences(actionLayer.getImage());

            canvas.mergeActiveLayerDown();

            EditAction thisAction = new EditAction(originalLayer, pixelChanges);
            canvas.undoManager.addEdit(thisAction);

            onLeftMouseReleased();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = activeCanvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }
}
