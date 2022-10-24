package frame;

import javax.swing.*;

public class SpriteTabbedPane extends JTabbedPane {

    private final AnimationPanel animationPanel;
    private final GridPanel gridPanel;

    public SpriteTabbedPane() {
        animationPanel = new AnimationPanel();
        gridPanel = new GridPanel();

        addTab("Animation", animationPanel);
        addTab("Grid", gridPanel);
    }

}
