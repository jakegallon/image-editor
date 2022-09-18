package action;

import frame.Layer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class EditAction extends AbstractUndoableEdit {

    Layer layer;
    PixelChange[] pixelChanges;

    public EditAction(Layer layer, PixelChange[] pixelChanges) {
        this.layer = layer;
        this.pixelChanges = pixelChanges;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        for(PixelChange pixelChange : pixelChanges) {
            layer.paint(pixelChange.x(), pixelChange.y(), pixelChange.oldColor());
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        for(PixelChange pixelChange : pixelChanges) {
            layer.paint(pixelChange.x(), pixelChange.y(), pixelChange.newColor());
        }
    }
}
