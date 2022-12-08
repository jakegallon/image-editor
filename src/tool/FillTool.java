package tool;

import frame.ToolSettings;
import tool.properties.PropertyBooleanWidget;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FillTool extends EditTool {

    AtomicBoolean spillDiagonally = new AtomicBoolean(false);

    public FillTool() {
        category = ToolCategory.FILL;
        displayName = "Fill";
    }

    @Override
    public void attachProperties() {
        ToolSettings.addComponentToToolSettings(new PropertyBooleanWidget(spillDiagonally, "Spill over diagonals"));
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {
        activeLayer.initiateFill(initPressPoint, color, spillDiagonally.get());
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
