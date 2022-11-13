package tool;

import javax.swing.*;
import java.awt.*;

public class FillTool extends EditTool {

    public FillTool() {
        name = "fill";
    }

    @Override
    public void populateSettingsPanel(JPanel panel) {
        panel.setBackground(Color.blue);
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
