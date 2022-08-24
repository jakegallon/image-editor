package colorpanel;

import frame.Controller;

import javax.swing.*;
import java.awt.*;

public class ColorTabbedPane extends JTabbedPane {

    private final PalettePanel palettePanel;
    private final ColorPanel colorPanel;
    private final Controller controller;

    public ColorTabbedPane(Controller controller) {
        this.controller = controller;

        palettePanel = new PalettePanel(this);
        colorPanel = new ColorPanel(this);

        addTab("Color", colorPanel);
        addTab("Palette", palettePanel);
    }

    public Color getPrimaryColor() {
        return colorPanel.getPrimaryColor();
    }

    public Color getSecondaryColor() {
        return colorPanel.getSecondaryColor();
    }

    public Color getSelectedColor() {
        return colorPanel.getSelectedColor();
    }

    protected void setSelectedColor(Color color) {
        colorPanel.setSelectedColor(color);
    }

    protected void onSelectedColorChange(Color color, boolean isPrimarySelected) {
        controller.setIsPrimarySelected(isPrimarySelected);

        if(isPrimarySelected) controller.setPrimaryColor(color);
        else controller.setSecondaryColor(color);

        palettePanel.deselectSelectedHolder();
    }
}
