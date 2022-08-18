package colorpanel;

import javax.swing.*;
import java.awt.*;

public class ColorTabbedPane extends JTabbedPane {

    private final PalettePanel palettePanel;
    private final ColorPanel colorPanel;

    public ColorTabbedPane() {
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

    protected void notifyColorChange() {
        palettePanel.deselectSelectedHolder();
    }
}
