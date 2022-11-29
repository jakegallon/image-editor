package tool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class EyeTool extends BaseTool {

    public EyeTool() {
        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/eye.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        toolCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public void attachProperties(JPanel panel) {

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
