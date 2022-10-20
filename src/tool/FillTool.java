package tool;

import frame.Controller;

public class FillTool extends EditTool {
    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {
        activeLayer.fillAll(Controller.selectedColor());
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
