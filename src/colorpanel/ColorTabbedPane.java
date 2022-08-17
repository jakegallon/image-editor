package colorpanel;

import javax.swing.*;
import java.awt.*;

public class ColorTabbedPane extends JTabbedPane {

    private final ColorPanel colorPanel;
    private final PalettePanel palettePanel;

    public ColorTabbedPane() {
        colorPanel = new ColorPanel();
        palettePanel = new PalettePanel(this);

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
}
