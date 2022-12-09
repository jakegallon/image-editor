package tool;

import frame.ToolSettings;
import tool.properties.PropertyBooleanWidget;
import tool.properties.PropertySliderWidget;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class EraseTool extends EditTool {

    AtomicInteger width = new AtomicInteger(1);
    AtomicInteger opacity = new AtomicInteger(255);

    AtomicBoolean antiAliasing = new AtomicBoolean(false);
    public EraseTool() {
        category = ToolCategory.ERASE;
        displayName = "Eraser";
        toolCursor = getCursor(CustomCursor.GENERIC_TOOL, CursorOffset.CENTERED);
    }

    @Override
    public void attachProperties() {
        ToolSettings.addComponentToToolSettings(new PropertySliderWidget(width, "Brush Size", 1, 100));
        ToolSettings.addComponentToToolSettings(new PropertySliderWidget(opacity, "Opacity", 0, 255));

        ToolSettings.addComponentToToolSettings(new PropertyBooleanWidget(antiAliasing, "Anti-aliasing"));
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        activeLayer.eraseLine(lastDragPoint, thisDragPoint, width.get(), opacity.get(), antiAliasing.get());
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
