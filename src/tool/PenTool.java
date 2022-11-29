package tool;

import javax.swing.*;
import java.awt.*;

public class PenTool extends EditTool {

    public PenTool() {
        name = "pen";
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
