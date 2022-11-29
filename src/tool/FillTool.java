package tool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class FillTool extends EditTool {

    public FillTool() {
        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/fill.png"))));
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
        activeLayer.fillAll(color);
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
