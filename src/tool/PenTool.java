package tool;

import frame.ToolSettings;
import tool.properties.PropertyBooleanWidget;
import tool.properties.PropertySliderWidget;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PenTool extends EditTool {

    final AtomicInteger width = new AtomicInteger(1);
    final AtomicInteger opacity = new AtomicInteger(255);

    final AtomicBoolean antiAliasing = new AtomicBoolean(false);

    public PenTool() {
        category = ToolCategory.PEN;
        displayName = "Pen";
        toolCursor = getCursor(CustomCursor.GENERIC_TOOL, CursorOffset.CENTERED);
    }

    @Override
    public void attachProperties() {
        ToolSettings.addComponentToToolSettings(new PropertySliderWidget(width, "Brush Size", 1, 100));
        ToolSettings.addComponentToToolSettings(new PropertySliderWidget(opacity, "Opacity", 0, 255));

        ToolSettings.addComponentToToolSettings(new PropertyBooleanWidget(antiAliasing, "Anti-aliasing"));
    }

    @Override
    protected void onLeftMousePressed() {

    }

    @Override
    protected void onLeftMouseDragged() {
        if(opacity.get() == 255) {
            activeLayer.drawLine(lastDragPoint, thisDragPoint, width.get(), color, antiAliasing.get());
        } else {
            Color alphaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity.get());
            activeLayer.drawLine(lastDragPoint, thisDragPoint, width.get(), alphaColor, antiAliasing.get());
        }
    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
