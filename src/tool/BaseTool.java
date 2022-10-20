package tool;

import action.EditAction;
import action.PixelChange;
import frame.Canvas;
import frame.CanvasPanel;
import frame.Controller;
import frame.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class BaseTool {

    protected CanvasPanel activeCanvasPanel;
    public static Canvas canvas;

    private Layer originalLayer;
    protected Layer actionLayer;

    protected Point initDragPoint;
    protected Point lastDragPoint;
    protected Point thisDragPoint;

    protected Cursor toolCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    protected abstract void onLeftMouseClicked();
    protected abstract void onLeftMousePressed();
    protected abstract void onLeftMouseDragged();
    protected abstract void onLeftMouseReleased();

    public void onMouseClicked(MouseEvent e) {
        if(canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            onLeftMouseClicked();
        } else if (SwingUtilities.isRightMouseButton(e)) {

        }
    }

    public void onMousePressed(MouseEvent e) {
        if(canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            originalLayer = canvas.getActiveLayer();
            actionLayer = new Layer(canvas);
            canvas.addLayer(actionLayer);

            initDragPoint = activeCanvasPanel.getMousePos();
            lastDragPoint = initDragPoint;
            thisDragPoint = lastDragPoint;

            onLeftMousePressed();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = activeCanvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

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

    public void onMouseReleased(MouseEvent e) {
        if(canvas.getActiveLayer() == null) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            initDragPoint = null;
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

    public void onMouseEntered(CanvasPanel canvasPanel) {
        activeCanvasPanel = canvasPanel;
        activeCanvasPanel.setCursor(toolCursor);
    }
}