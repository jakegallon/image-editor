package tool;

import javax.swing.*;
import java.awt.*;

public class MoveCameraTool extends BaseTool {

    public MoveCameraTool() {
        category = ToolCategory.MOVE;
        displayName = "Move Camera";
        toolCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    }

    @Override
    public void attachProperties(JPanel panel) {

    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
