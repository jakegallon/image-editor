package tool;

import java.awt.*;

public class EyeTool extends BaseTool {

    public EyeTool() {
        name = "eye";
        toolCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {
        setSelectedColorToMousePosColor();
    }

    @Override
    protected void onLeftMouseDragged() {
        setSelectedColorToMousePosColor();
    }

    @Override
    protected void onLeftMouseReleased() {
        setSelectedColorToMousePosColor();
    }
}
