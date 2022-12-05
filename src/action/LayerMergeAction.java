package action;

import frame.Controller;
import frame.Layer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.image.BufferedImage;

public class LayerMergeAction extends AbstractUndoableEdit {

    Layer topLayer;
    Layer bottomLayer;
    int bottomIndex;

    BufferedImage bottomLayerOriginalImage;

    public LayerMergeAction(Layer topLayer, Layer bottomLayer, int bottomIndex) {
        this.topLayer = topLayer;
        this.bottomLayer = bottomLayer;
        this.bottomIndex = bottomIndex;

        bottomLayerOriginalImage = copyImage(bottomLayer.getImage());
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        bottomLayer.setImage(copyImage(bottomLayerOriginalImage));
        Controller.undoMergeLayer(topLayer, bottomLayer, bottomIndex);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        Controller.mergeLayerIntoLayer(topLayer, bottomLayer, bottomIndex);
    }

    private BufferedImage copyImage(BufferedImage sourceImage) {
        BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        image.getGraphics().drawImage(sourceImage, 0, 0, null);
        return image;
    }
}