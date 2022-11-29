package tool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class EraseTool extends EditTool {

    public EraseTool() {
        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/erase.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        activeLayer.eraseLine(lastDragPoint, thisDragPoint, 1);
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
