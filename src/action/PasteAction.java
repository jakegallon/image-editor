package action;

import frame.Controller;
import frame.Layer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class PasteAction extends AbstractUndoableEdit {

    Layer pastedLayer;
    int pastedLayerIndex;

    public PasteAction(Layer pastedLayer, int pastedLayerIndex) {
        this.pastedLayer = pastedLayer;
        this.pastedLayerIndex = pastedLayerIndex;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        Controller.deleteLayerFromActiveCanvas(pastedLayerIndex);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        Controller.addLayerToActiveCanvas(pastedLayer, pastedLayerIndex);
    }
}