package tool;

public class FillTool extends EditTool {

    public FillTool() {
        name = "fill";
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {
        activeLayer.fillAll(color);
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
