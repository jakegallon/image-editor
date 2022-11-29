package frame;

import javax.swing.*;
import java.awt.*;

public class SpriteTabbedPane extends JTabbedPane {

    public SpriteTabbedPane() {
        setMinimumSize(new Dimension(0, 250));
        AnimationPanel animationPanel = new AnimationPanel();
        GridPanel gridPanel = new GridPanel();

        addTab("Animation", animationPanel);
        addTab("Grid", gridPanel);
    }

}
