package colorpanel;

import frame.Controller;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ColorSplitPane extends JPanel {

    private final PalettePanel palettePanel;
    private final ColorPanel colorPanel;

    public ColorSplitPane() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new MatteBorder(1, 1, 0, 1, new Color(43, 43 ,43)));

        palettePanel = new PalettePanel(this);
        colorPanel = new ColorPanel(this);

        palettePanel.setMinimumSize(new Dimension(250, 100));
        colorPanel.setMinimumSize(new Dimension(250, 273));

        JSplitPane colorPalette = new JSplitPane(JSplitPane.VERTICAL_SPLIT, colorPanel, palettePanel);
        colorPalette.setResizeWeight(0.5);

        add(colorPalette);
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
