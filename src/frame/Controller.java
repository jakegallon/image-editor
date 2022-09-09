package frame;

import action.BaseAction;

import java.awt.*;

public class Controller {

    private boolean isPrimarySelected = true;
    private Color primaryColor;
    private Color secondaryColor;

    public void setIsPrimarySelected(boolean isPrimarySelected) {
        this.isPrimarySelected = isPrimarySelected;
    }

    public void setPrimaryColor(Color c) {
        this.primaryColor = c;
    }

    public void setSecondaryColor(Color c) {
        this.secondaryColor = c;
    }

    public Color getSelectedColor() {
        if(isPrimarySelected) return primaryColor;
        else return secondaryColor;
    }

    public Color getNotSelectedColor() {
        if(!isPrimarySelected) return primaryColor;
        else return secondaryColor;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    private Canvas activeCanvas;

    public void setActiveCanvas(Canvas canvas) {
        activeCanvas = canvas;
    }

    public Canvas getActiveCanvas() {
        return activeCanvas;
    }

    public void undoAction() {
        int lastIndex = activeCanvas.actions.size() - 1;
        BaseAction lastAction = activeCanvas.actions.get(lastIndex);
        lastAction.undo();
    }

    public void redoAction() {
        int lastIndex = activeCanvas.actions.size() - 1;
        BaseAction lastAction = activeCanvas.actions.get(lastIndex);
        lastAction.redo();
    }
}
