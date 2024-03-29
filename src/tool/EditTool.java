package tool;

import action.EditAction;
import action.PixelChange;
import frame.*;
import frame.Frame;

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
    public void onMousePressed(MouseEvent e) {
        if(isCanvasOrLayerNull()) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            activeLayer = canvas.getActiveLayer();
            if(activeLayer.isLocked()) {
                notifyLayerLocked();
                return;
            }

            BufferedImage i = activeLayer.getImage();

            originalImage = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
            originalImage.getGraphics().drawImage(i, 0, 0, null);

            initPressPoint = canvasPanel.getMousePos();
            lastDragPoint = initPressPoint;
            thisDragPoint = lastDragPoint;

            color = Controller.selectedColor();
            onLeftMousePressed();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = canvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(isCanvasOrLayerNull()) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            if(activeLayer.isLocked()) {
                notifyLayerLocked();
                return;
            }

            lastDragPoint = thisDragPoint;
            thisDragPoint = canvasPanel.getMousePos();

            if(!lastDragPoint.equals(thisDragPoint)) {
                onLeftMouseDragged();
            }
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = canvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(isCanvasOrLayerNull()) return;
        if(SwingUtilities.isLeftMouseButton(e)) {
            if(activeLayer.isLocked()) {
                notifyLayerLocked();
                return;
            }
            onLeftMouseReleased();

            initPressPoint = null;
            lastDragPoint = null;
            thisDragPoint = null;

            color = null;

            ArrayList<PixelChange> pixelChanges = activeLayer.getImageDifferences(originalImage);

            EditAction thisAction = new EditAction(canvas.getActiveLayerIndex(), pixelChanges);
            canvas.undoManager.addEdit(thisAction);
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            Color color = canvasPanel.getColorAtMousePos();
            Controller.setSelectedColor(color);
        }
    }

    @Override
    public void onMouseEntered(CanvasPanel panel) {
        canvasPanel = panel;
        if(isCanvasOrLayerNull()) {
            canvasPanel.setCursor(blockedCursor);
            Frame.notificationPanel.setCursor(blockedCursor);
        } else if(canvas.getActiveLayer().isLocked()) {
            canvasPanel.setCursor(blockedCursor);
            Frame.notificationPanel.setCursor(blockedCursor);
        } else {
            canvasPanel.setCursor(toolCursor);
            Frame.notificationPanel.setCursor(toolCursor);
        }
    }

    private boolean isCanvasOrLayerNull() {
        return canvas == null || canvas.getActiveLayer() == null;
    }

    private void notifyLayerLocked() {
        NotificationPanel.playNotification(NotificationMessage.TOOL_LAYER_LOCKED);
    }
}