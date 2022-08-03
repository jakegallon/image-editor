package colorpanel;

import javax.swing.*;
import java.awt.*;

public class ColorTabbedPane extends JTabbedPane {

    public Color currentColor;

    private ColorPanel colorPanel = new ColorPanel();

    public ColorTabbedPane() {
        currentColor = new Color(0, 0, 0);
        addTab("Color", colorPanel);
    }

}
