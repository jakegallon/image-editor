package action;

import frame.Layer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;

public class EditAction extends AbstractUndoableEdit {

    Layer layer;
    ArrayList<PixelChange> pixelChanges;

    public EditAction(Layer layer, ArrayList<PixelChange> pixelChanges) {
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
