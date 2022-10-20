package tool;

public class PenTool extends BaseTool {

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        actionLayer.drawLine(lastDragPoint, thisDragPoint, 1);
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
