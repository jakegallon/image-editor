package tool;

import javax.swing.*;

public class FillTool extends EditTool {

    public FillTool() {
        category = ToolCategory.FILL;
        displayName = "Fill";
    }

    @Override
    public void attachProperties(JPanel panel) {

    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {
        activeLayer.initiateFill(initPressPoint, color);
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
