package tool;

import javax.swing.*;
import java.awt.*;

public class PenTool extends EditTool {

    public PenTool() {
        name = "pen";
    }

    @Override
    public void populateSettingsPanel(JPanel panel) {
        panel.setBackground(Color.cyan);
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        activeLayer.drawLine(lastDragPoint, thisDragPoint, 1, color);
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
