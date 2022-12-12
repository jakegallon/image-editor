package tool;

import frame.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MoveSelectionTool extends EditTool {

    private Layer proxyLayer;

    public MoveSelectionTool() {
        category = ToolCategory.MOVE;
        displayName = "Move Selection";
        toolCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    }

    @Override
    public void attachProperties() {

    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {
        proxyLayer = new Layer(canvas);

        Rectangle selectedArea = canvas.getSelectedArea();

        if(selectedArea.width < 0) {
            selectedArea.width = Math.abs(selectedArea.width);
            selectedArea.x -= selectedArea.width;
        }
        if(selectedArea.height < 0) {
            selectedArea.height = Math.abs(selectedArea.height);
            selectedArea.y -= selectedArea.height;
        }

        BufferedImage selectionCopy = new BufferedImage(selectedArea.width, selectedArea.height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage selection = activeLayer.getImage().getSubimage(
                selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
        selectionCopy.getGraphics().drawImage(selection, 0, 0, null);

        activeLayer.clearRect(selectedArea);

        canvas.addLayer(proxyLayer, canvas.getActiveLayerIndex());
        proxyLayer.getImage().getGraphics().drawImage(
                selectionCopy, selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height, null);
}

    @Override
    protected void onLeftMouseDragged() {
        int dx = thisDragPoint.x - lastDragPoint.x;
        int dy = thisDragPoint.y - lastDragPoint.y;

        proxyLayer.moveLayer(dx, dy);
        canvas.shiftSelectedArea(dx, dy);
    }

    @Override
    protected void onLeftMouseReleased() {
        activeLayer.mergeLayerIntoThis(proxyLayer);
        canvas.deleteLayer();

        proxyLayer = null;
    }
}