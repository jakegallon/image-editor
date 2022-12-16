package tool;

import frame.ToolSettings;
import tool.properties.PropertyBooleanWidget;
import tool.properties.PropertySliderWidget;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FillTool extends EditTool {

    final AtomicBoolean spillDiagonally = new AtomicBoolean(false);
    final AtomicInteger opacity = new AtomicInteger(255);

    public FillTool() {
        category = ToolCategory.FILL;
        displayName = "Fill";
        toolCursor = getCursor(CustomCursor.FILL_TOOL, CursorOffset.TOP_LEFT);
    }

    @Override
    public void attachProperties() {
        ToolSettings.addComponentToToolSettings(new PropertySliderWidget(opacity, "Opacity", 0, 255));
        ToolSettings.addComponentToToolSettings(new PropertyBooleanWidget(spillDiagonally, "Spill over diagonals"));
    }

    @Override
    protected void onLeftMousePressed() {
        if(opacity.get() == 255) {
            activeLayer.initiateFill(initPressPoint, color, spillDiagonally.get());
        } else {
            Color alphaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity.get());
            activeLayer.initiateFill(initPressPoint, alphaColor, spillDiagonally.get());
        }
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
