package tool;

public class EyeTool extends BaseTool {

    public EyeTool() {
        category = ToolCategory.EYE;
        displayName = "Colour Picker";
        toolCursor = getCursor(CustomCursor.EYE_TOOL, CursorOffset.TOP_RIGHT);
    }

    @Override
    public void attachProperties() {

    }

    @Override
    protected void onLeftMousePressed() {
        setSelectedColorToMousePosColor();
    }

    @Override
    protected void onLeftMouseDragged() {
        setSelectedColorToMousePosColor();
    }

    @Override
    protected void onLeftMouseReleased() {
        setSelectedColorToMousePosColor();
    }
}
