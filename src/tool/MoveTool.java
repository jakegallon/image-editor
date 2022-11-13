package tool;

import javax.swing.*;
import java.awt.*;

public class MoveTool extends BaseTool {

    public MoveTool() {
        name = "move";
        toolCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    }

    @Override
    public void populateSettingsPanel(JPanel panel) {
        panel.setBackground(Color.yellow);
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
