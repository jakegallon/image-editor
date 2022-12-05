package action;


import frame.Frame;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class LayerOrderAction extends AbstractUndoableEdit {

    int oldLayerIndex;
    int newLayerIndex;

    public LayerOrderAction(int oldLayerIndex, int newLayerIndex) {
        this.oldLayerIndex = oldLayerIndex;
        this.newLayerIndex = newLayerIndex;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        Frame.layerPanel.moveLayerIndexToIndex(newLayerIndex, oldLayerIndex);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        Frame.layerPanel.moveLayerIndexToIndex(oldLayerIndex, newLayerIndex);
    }
}
