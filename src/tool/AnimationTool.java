package tool;

import frame.AnimationPanel;
import frame.NotificationMessage;
import frame.NotificationPanel;
import frame.ToolSettings;

import javax.swing.*;

public class AnimationTool extends BaseTool {

    final AnimationPanel.SpritePanel spritePanel;

    public AnimationTool(AnimationPanel.SpritePanel spritePanel) {
        this.spritePanel = spritePanel;
    }

    @Override
    public void attachProperties() {
        JPanel animationGuide = new JPanel();

        SpringLayout springLayout = new SpringLayout();
        animationGuide.setLayout(springLayout);

        JLabel title = new JLabel("Animation Tool");
        title.setFont(title.getFont().deriveFont(16f));
        animationGuide.add(title);

        springLayout.putConstraint(SpringLayout.NORTH, title, 2, SpringLayout.NORTH, animationGuide);
        springLayout.putConstraint(SpringLayout.WEST, title, 2, SpringLayout.WEST, animationGuide);

        JLabel description = new JLabel("<html>This tool is used alongside the grid to create the illusion of animation where each grid cell is a frame in the animation.</html>");
        animationGuide.add(description);

        springLayout.putConstraint(SpringLayout.NORTH, description, 5, SpringLayout.SOUTH, title);
        springLayout.putConstraint(SpringLayout.WEST, description, 0, SpringLayout.WEST, title);
        springLayout.putConstraint(SpringLayout.EAST, description, -2, SpringLayout.EAST, animationGuide);

        JLabel usage = new JLabel("<html>To use this tool, click inside the grid's cells in the desired order to make an animation appear in the animation tab on the right.</html>");
        animationGuide.add(usage);

        springLayout.putConstraint(SpringLayout.NORTH, usage, 5, SpringLayout.SOUTH, description);
        springLayout.putConstraint(SpringLayout.WEST, usage, 0, SpringLayout.WEST, title);
        springLayout.putConstraint(SpringLayout.EAST, usage, 0, SpringLayout.EAST, description);

        ToolSettings.addComponentToToolSettings(animationGuide);
    }

    @Override
    protected void onLeftMousePressed() {
        if(canvas.getBounds().contains(initPressPoint)){
            spritePanel.addFrame(canvas.getGridCellAtPoint(initPressPoint));
        } else {
            NotificationPanel.playNotification(NotificationMessage.TOOL_PRESS_OOB);
        }
    }

    @Override
    protected void onLeftMouseDragged() {

    }

    @Override
    protected void onLeftMouseReleased() {

    }
}
