package action;

import frame.Layer;

public class EditAction extends BaseAction {

    Layer layer;
    PixelChange[] pixelChanges;

    public EditAction(Layer layer, PixelChange[] pixelChanges) {
        this.layer = layer;
        this.pixelChanges = pixelChanges;
    }

    @Override
    public void undo() {
        for(PixelChange pixelChange : pixelChanges) {
            layer.paint(pixelChange.x(), pixelChange.y(), pixelChange.oldColor());
        }
    }

    @Override
    public void redo() {
        for(PixelChange pixelChange : pixelChanges) {
            layer.paint(pixelChange.x(), pixelChange.y(), pixelChange.newColor());
        }
    }
}
