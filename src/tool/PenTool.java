package tool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class PenTool extends EditTool {

    public PenTool() {
        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/pen.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void attachProperties(JPanel panel) {
        ToolProperty.WIDTH.attachToPanelAndTool(panel, this);
        ToolProperty.OPACITY.attachToPanelAndTool(panel, this);
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        if(opacity == 255) {
            activeLayer.drawLine(lastDragPoint, thisDragPoint, width, color);
        } else {
            Color alphaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
            activeLayer.drawLine(lastDragPoint, thisDragPoint, width, alphaColor);
        }
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
