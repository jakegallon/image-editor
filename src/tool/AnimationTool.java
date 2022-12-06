package tool;

import frame.AnimationPanel;

import javax.swing.*;

public class AnimationTool extends BaseTool {

    AnimationPanel.SpritePanel spritePanel;

    public AnimationTool(AnimationPanel.SpritePanel spritePanel) {
        this.spritePanel = spritePanel;
    }

    @Override
    public void attachProperties(JPanel panel) {
        JLabel title = new JLabel("Animation Tool");
        title.setFont(title.getFont().deriveFont(16f));
        panel.add(title);

        JLabel description = new JLabel("<html>This tool is used alongside the grid to create the<br>illusion of animation where each grid cell is a<br>frame in the animation.</html>");
        panel.add(description);

        JLabel usage = new JLabel("<html><br>To use this tool, click inside the grid's cells in the<br>desired order to make an animation appear in the<br>animation tab on the right.</html>");
        panel.add(usage);
    }

    @Override
    protected void onLeftMouseClicked() {

    }

    @Override
    protected void onLeftMousePressed() {
        spritePanel.addFrame(canvas.getGridCellAtPoint(initPressPoint));
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
