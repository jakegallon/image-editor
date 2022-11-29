package tool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class MoveTool extends BaseTool {

    public MoveTool() {
        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/move.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
