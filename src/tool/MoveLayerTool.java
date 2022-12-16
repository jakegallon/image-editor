package tool;

import java.awt.*;

public class MoveLayerTool extends EditTool {

    public MoveLayerTool() {
        category = ToolCategory.MOVE;
        displayName = "Move Layer";
        toolCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    }

    @Override
    public void attachProperties() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        int dx = thisDragPoint.x - lastDragPoint.x;
        int dy = thisDragPoint.y - lastDragPoint.y;

        activeLayer.moveLayer(dx, dy);
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
