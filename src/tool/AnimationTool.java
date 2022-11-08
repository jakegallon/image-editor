package tool;

import frame.AnimationPanel;

public class AnimationTool extends BaseTool {

    AnimationPanel.SpritePanel spritePanel;

    public AnimationTool(AnimationPanel.SpritePanel spritePanel) {
        this.spritePanel = spritePanel;
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
