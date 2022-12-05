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

    public static void addNewCanvas(Canvas canvas) {
        Frame.topPanel.createTabForCanvas(canvas);
        setActiveCanvas(canvas);
    }

    public static void addNewLayerToActiveCanvas(int index) {
        Layer newLayer = new Layer(activeCanvas);
        newLayer.setName("Layer " + activeCanvas.nextNameNumber());
        activeCanvas.addLayer(newLayer, index);
        Frame.layerPanel.addLayer(newLayer, index);
    }

    public static void restoreLayerToActiveCanvas(Layer layer, int index) {
        activeCanvas.addLayer(layer, index);
        Frame.layerPanel.addLayer(layer, index);
    }

    public static void mergeLayerIntoLayer(Layer topLayer, Layer bottomLayer, int bottomIndex) {
        bottomLayer.mergeLayerIntoThis(topLayer);

        deleteLayerFromActiveCanvas(bottomIndex - 1);
    }

    public static void undoMergeLayer(Layer topLayer, Layer bottomLayer, int bottomIndex) {
        int topIndex = bottomIndex - 1;
        restoreLayerToActiveCanvas(bottomLayer, bottomIndex);
        restoreLayerToActiveCanvas(topLayer, topIndex);
        deleteLayerFromActiveCanvas(bottomIndex);
    }

    public static void deleteLayerFromActiveCanvas(int index) {
        activeCanvas.deleteLayer(index);
        Frame.layerPanel.deleteLayer(index);
    }

    public static void setActiveCanvas(Canvas canvas) {
        activeCanvas = canvas;
        Frame.canvasPanel.setCanvas(canvas);
        Frame.layerPanel.onCanvasSwitch();
        Frame.magnificationPanel.setCanvas(canvas);
        Frame.topPanel.setActiveWidgetByCanvas(canvas);
        LayerPanelHandler.setActiveCanvas(canvas);
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
        ToolSettings.onNewTool(tool);
    }

    public static BaseTool getActiveTool() {
        return activeTool;
    }
}
