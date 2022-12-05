package action;

import frame.Controller;
import frame.Layer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class LayerDeletionAction extends AbstractUndoableEdit {

    Layer layer;
    int index;

    public LayerDeletionAction(Layer layer, int index) {
        this.layer = layer;
        this.index = index;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        Controller.restoreLayerToActiveCanvas(layer, index);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        Controller.deleteLayerFromActiveCanvas(index);
    }
}
