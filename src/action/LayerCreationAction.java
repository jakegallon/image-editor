package action;

import frame.Controller;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class LayerCreationAction extends AbstractUndoableEdit {

    int index;

    public LayerCreationAction(int index) {
        this.index = index;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        Controller.deleteLayerFromActiveCanvas(index);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        Controller.addNewLayerToActiveCanvas(index);
    }
}