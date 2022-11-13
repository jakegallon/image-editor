package tool;

public class PenTool extends EditTool {

    public PenTool() {
        name = "pen";
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        activeLayer.drawLine(lastDragPoint, thisDragPoint, 1, color);
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
