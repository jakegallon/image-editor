package tool;

import action.EditAction;
import action.PixelChange;
import frame.Controller;
import frame.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class EditTool extends BaseTool {

    private BufferedImage originalImage;
    protected Layer activeLayer;
    protected Point lastDragPoint;
    protected Point thisDragPoint;

    protected Color color;

    @Override
    public void onMouseClicked(MouseEvent e) {
        if(isCanvasOrLayerNull()) return;
        super.onMouseClicked(e);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if(isCanvasOrLayerNull()) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            activeLayer = canvas.getActiveLayer();
            BufferedImage i = activeLayer.getImage();

            originalImage = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
            originalImage.getGraphics().drawImage(i, 0, 0, null);

            initPressPoint = activeCanvasPanel.getMousePos();
            lastDragPoint = initPressPoint;
            thisDragPoint = lastDragPoint;

            color = Controller.selectedColor();
            onLeftMousePressed();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = activeCanvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(isCanvasOrLayerNull()) return;
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
        if(isCanvasOrLayerNull()) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            onLeftMouseReleased();

            initPressPoint = null;
            lastDragPoint = null;
            thisDragPoint = null;

            color = null;

            ArrayList<PixelChange> pixelChanges = activeLayer.getImageDifferences(originalImage);

            EditAction thisAction = new EditAction(activeLayer, pixelChanges);
            canvas.undoManager.addEdit(thisAction);
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = activeCanvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    private boolean isCanvasOrLayerNull() {
        return canvas == null || canvas.getActiveLayer() == null;
    }
}
