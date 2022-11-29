package colorpanel;

import frame.Controller;

import javax.swing.*;
import java.awt.*;

public class ColorTabbedPane extends JTabbedPane {

    private final PalettePanel palettePanel;
    private final ColorPanel colorPanel;

    public ColorTabbedPane() {
        setMinimumSize(new Dimension(250, 273));
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

    public void setSelectedColor(Color color) {
        colorPanel.setSelectedColor(color);
    }

    protected void onSelectedColorChange(Color color, boolean isPrimarySelected) {
        Controller.setIsPrimarySelected(isPrimarySelected);

        if(isPrimarySelected) Controller.primaryColor = color;
        else Controller.secondaryColor = color;

        palettePanel.deselectSelectedHolder();
    }
}
