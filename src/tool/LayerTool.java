package tool;

import action.EditAction;
import action.PixelChange;
import frame.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class LayerTool extends BaseTool {

    private Layer originalLayer;
    protected Layer actionLayer;
    protected Point lastDragPoint;
    protected Point thisDragPoint;
    protected Point initDragPoint;

    @Override
    public void onMouseClicked(MouseEvent e) {

    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            originalLayer = canvas.getActiveLayer();
            actionLayer = new Layer(canvas);
            canvas.addLayer(actionLayer);

            initDragPoint = activeCanvasPanel.getMousePos();
            lastDragPoint = initDragPoint;
            thisDragPoint = lastDragPoint;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            //todo
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            lastDragPoint = thisDragPoint;
            thisDragPoint = activeCanvasPanel.getMousePos();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            //todo
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            initDragPoint = null;
            lastDragPoint = null;
            thisDragPoint = null;

            ArrayList<PixelChange> pixelChanges = originalLayer.getImageDifferences(actionLayer.getImage());

            canvas.mergeActiveLayerDown();

            EditAction thisAction = new EditAction(originalLayer, pixelChanges);
            canvas.undoManager.addEdit(thisAction);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            //todo
        }
    }
}