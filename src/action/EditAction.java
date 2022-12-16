package action;

import frame.Controller;
import frame.Layer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;

public class EditAction extends AbstractUndoableEdit {

    final int layerIndex;
    final ArrayList<PixelChange> pixelChanges;

    public EditAction(int layerIndex, ArrayList<PixelChange> pixelChanges) {
        this.layerIndex = layerIndex;
        this.pixelChanges = pixelChanges;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        Layer layer = Controller.getActiveCanvas().layers.get(layerIndex);
        for(PixelChange pixelChange : pixelChanges) {
            layer.paint(pixelChange.x(), pixelChange.y(), pixelChange.oldColor());
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        Layer layer = Controller.getActiveCanvas().layers.get(layerIndex);
        for(PixelChange pixelChange : pixelChanges) {
            layer.paint(pixelChange.x(), pixelChange.y(), pixelChange.newColor());
        }
    }
}
