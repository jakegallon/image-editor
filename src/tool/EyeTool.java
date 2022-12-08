package tool;

import javax.swing.*;
import java.awt.*;

public class EyeTool extends BaseTool {

    public EyeTool() {
        category = ToolCategory.EYE;
        displayName = "Colour Picker";
        toolCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public void attachProperties() {

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
