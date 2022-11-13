package tool;

import javax.swing.*;
import java.awt.*;

public class EraseTool extends EditTool {

    public EraseTool() {
        name = "erase";
    }

    @Override
    public void populateSettingsPanel(JPanel panel) {
        panel.setBackground(Color.gray);
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
