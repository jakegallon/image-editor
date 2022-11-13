package frame;

import tool.BaseTool;

import java.awt.*;

public class Controller {

    static private boolean isPrimarySelected = true;
    static public Color primaryColor;
    static public Color secondaryColor;

    public static void setIsPrimarySelected(boolean b) {
        isPrimarySelected = b;
    }

    public static Color selectedColor() {
        if(isPrimarySelected) return primaryColor;
        else return secondaryColor;
    }

    public static void setSelectedColor(Color color) {
        Frame.colorSettings.setSelectedColor(color);
    }

    public static Color notSelectedColor() {
        if(!isPrimarySelected) return primaryColor;
        else return secondaryColor;
    }

    private static Canvas activeCanvas;

    public static void setActiveCanvas(Canvas canvas) {
        activeCanvas = canvas;
        Frame.canvasPanel.setCanvas(canvas);
        Frame.layerPanel.onCanvasSwitch();
        Frame.magnificationPanel.setCanvas(canvas);
        BaseTool.canvas = canvas;
        AnimationPanel.setCanvas(canvas);
    }

    public static Canvas getActiveCanvas() {
        return activeCanvas;
    }

    public static void undo() {
        try {
            activeCanvas.undoManager.undo();
        } catch (Exception ignored) {

        }
    }

    public static void redo() {
        try {
            activeCanvas.undoManager.redo();
        } catch (Exception ignored) {

        }
    }

    public static boolean isActiveLayer(Layer layer) {
        return layer == activeCanvas.getActiveLayer();
    }

    private static BaseTool activeTool;

    public static void setActiveTool(BaseTool tool) {
        activeTool = tool;
        //todo change to get/set
        Frame.canvasPanel.activeTool = activeTool;
        tool.populateSettingsPanel(Frame.toolSettings);
    }

    public static BaseTool getActiveTool() {
        return activeTool;
    }
}
