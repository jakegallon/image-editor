package tool;

import action.EditAction;
import action.PixelChange;
import frame.Layer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class LayerTool extends BaseTool {

    private Layer originalLayer;
    protected Layer actionLayer;
    protected Point lastDragPoint;
    protected Point thisDragPoint;

    @Override
    public void onMouseClicked(MouseEvent e) {

    }

    @Override
    public void onMousePressed(MouseEvent e) {
        originalLayer = canvas.getActiveLayer();
        actionLayer = new Layer(canvas);
        canvas.addLayer(actionLayer);

        lastDragPoint = activeCanvasPanel.getMousePos();
        thisDragPoint = lastDragPoint;
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        lastDragPoint = thisDragPoint;
        thisDragPoint = activeCanvasPanel.getMousePos();
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        lastDragPoint = null;
        thisDragPoint = null;

        ArrayList<PixelChange> pixelChanges = originalLayer.getImageDifferences(actionLayer.getImage());

        canvas.mergeActiveLayerDown();

        EditAction thisAction = new EditAction(originalLayer, pixelChanges);
        canvas.undoManager.addEdit(thisAction);
    }
}
