package tool;

import java.awt.*;

public class EraseTool extends EditTool {

    private final Color eraserColor = new Color(0f, 0f, 0f, 0.5f);

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        activeLayer.eraseLine(lastDragPoint, thisDragPoint, 1);
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
