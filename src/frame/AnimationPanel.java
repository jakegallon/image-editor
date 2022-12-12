package frame;

import tool.AnimationTool;
import tool.ToolCategory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AnimationPanel extends JPanel {

    private final SpringLayout springLayout = new SpringLayout();

    private final JButton normalLayoutButton = new JButton();
    private final JButton focusedLayoutButton = new JButton();
    private final JButton crossLayoutButton = new JButton();
    private final JButton landscapeLayoutButton = new JButton();
    private final JButton helpButton = new JButton();

    private final SpritePanel spritePanel1 = new SpritePanel();
    private final SpritePanel spritePanel2 = new SpritePanel();
    private final SpritePanel spritePanel3 = new SpritePanel();
    private final SpritePanel spritePanel4 = new SpritePanel();
    private static SpritePanel activeSpritePanel;

    private int fps = 10;
    private final JSpinner fpsSpinner = new JSpinner(new SpinnerNumberModel(fps, 1, 999, 1));

    private final Timer timer = new Timer(100, null);

    private static final Color HIGHLIGHT_COLOR = new Color(70, 106, 146);

    private static GridInformation gridInformation = new GridInformation(0, 0, GridStyle.NONE);
    private static Canvas canvas;

    private static final SwingPropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(AnimationPanel.class);
    private static final String GRID_EVENT = "grid changed";

    private enum LayoutState {
        NORMAL,
        FOCUSED,
        CROSS,
        LANDSCAPE
    }

    private LayoutState layoutState = LayoutState.NORMAL;

    public AnimationPanel() {
        init();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                switch (layoutState) {
                    case NORMAL    -> constraintAsNormal();
                    case FOCUSED   -> constraintAsFocused();
                    case CROSS     -> constraintAsCross();
                    case LANDSCAPE -> constraintAsLandscape();
                }
            }
        });

        propertyChangeSupport.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals(GRID_EVENT)) {

                GridInformation old = (GridInformation) evt.getOldValue();

                Boolean isXChanged = old.gridX() != gridInformation.gridX();
                Boolean isYChanged = old.gridY() != gridInformation.gridY();

                if(isXChanged || isYChanged) {
                    spritePanel1.wipe();
                    spritePanel2.wipe();
                    spritePanel3.wipe();
                    spritePanel4.wipe();

                    spritePanel1.calculateNewDrawBounds();
                    spritePanel2.calculateNewDrawBounds();
                    spritePanel3.calculateNewDrawBounds();
                    spritePanel4.calculateNewDrawBounds();
                }
            }
        });

        timer.start();
    }

    public static void setCanvas(Canvas c) {
        canvas = c;
        if(c == null) {
            if(Controller.getActiveTool() instanceof AnimationTool) {
                ToolSelectPanel.highlightButtonByCategory(ToolCategory.MOVE);
                ToolSettings.onNewCategory(ToolCategory.MOVE);
                setActiveSpritePanel(null);
            }
            return;
        }
        setGridInformation(c.getGridInformation());
    }

    public static void setGridInformation(GridInformation g) {
        GridInformation old = gridInformation;
        gridInformation = g;
        propertyChangeSupport.firePropertyChange(GRID_EVENT, old, g);
    }

    private static ImageIcon layout1Icon;
    private static ImageIcon layout2Icon;
    private static ImageIcon layout3Icon;
    private static ImageIcon layout4Icon;

    private void init() {
        setLayout(springLayout);

        ImageIcon layout1IconSelected;
        ImageIcon layout2IconSelected;
        ImageIcon layout3IconSelected;
        ImageIcon layout4IconSelected;
        ImageIcon helpIcon;

        try {
            layout1Icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_1.png"))));
            layout2Icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_2.png"))));
            layout3Icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_3.png"))));
            layout4Icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_4.png"))));
            layout1IconSelected = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_1_selected.png"))));
            layout2IconSelected = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_2_selected.png"))));
            layout3IconSelected = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_3_selected.png"))));
            layout4IconSelected = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/anim_layout_4_selected.png"))));
            helpIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/help.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        add(normalLayoutButton);
        normalLayoutButton.setIcon(layout1IconSelected);
        normalLayoutButton.setBorderPainted(false);
        normalLayoutButton.setBackground(new Color(0, 0, 0, 0));
        normalLayoutButton.setPreferredSize(new Dimension(21, 21));
        normalLayoutButton.addActionListener(e -> {
            if(layoutState != LayoutState.NORMAL) {
                deselectCurrentButton();
                constraintAsNormal();
                normalLayoutButton.setIcon(layout1IconSelected);
            }
        });
        normalLayoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(focusedLayoutButton);
        focusedLayoutButton.setIcon(layout2Icon);
        focusedLayoutButton.setBorderPainted(false);
        focusedLayoutButton.setBackground(new Color(0, 0, 0, 0));
        focusedLayoutButton.setPreferredSize(new Dimension(21, 21));
        focusedLayoutButton.addActionListener(e -> {
            if(layoutState != LayoutState.FOCUSED) {
                deselectCurrentButton();
                constraintAsFocused();
                focusedLayoutButton.setIcon(layout2IconSelected);
            }
        });
        focusedLayoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(crossLayoutButton);
        crossLayoutButton.setIcon(layout3Icon);
        crossLayoutButton.setBorderPainted(false);
        crossLayoutButton.setBackground(new Color(0, 0, 0, 0));
        crossLayoutButton.setPreferredSize(new Dimension(21, 21));
        crossLayoutButton.addActionListener(e -> {
            if(layoutState != LayoutState.CROSS) {
                deselectCurrentButton();
                constraintAsCross();
                crossLayoutButton.setIcon(layout3IconSelected);
            }
        });
        crossLayoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(landscapeLayoutButton);
        landscapeLayoutButton.setIcon(layout4Icon);
        landscapeLayoutButton.setBorderPainted(false);
        landscapeLayoutButton.setBackground(new Color(0, 0, 0, 0));
        landscapeLayoutButton.setPreferredSize(new Dimension(21, 21));
        landscapeLayoutButton.addActionListener(e -> {
            if(layoutState != LayoutState.LANDSCAPE) {
                deselectCurrentButton();
                constraintAsLandscape();
                landscapeLayoutButton.setIcon(layout4IconSelected);
            }
        });
        landscapeLayoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel fpsLabel = new JLabel("fps");
        add(fpsLabel);
        add(fpsSpinner);
        fpsSpinner.addChangeListener(e -> {
            fps = (int) fpsSpinner.getValue();
            timer.setDelay((int) Math.round(1000.0 / fps));
        });

        add(helpButton);
        helpButton.setIcon(helpIcon);
        helpButton.setBorderPainted(false);
        helpButton.setBackground(new Color(0, 0, 0, 0));
        helpButton.setPreferredSize(new Dimension(21, 21));
        helpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        springLayout.putConstraint(SpringLayout.NORTH, normalLayoutButton, 6, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.SOUTH, normalLayoutButton, 20, SpringLayout.NORTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.NORTH, focusedLayoutButton, 0, SpringLayout.NORTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.SOUTH, focusedLayoutButton, 20, SpringLayout.NORTH, focusedLayoutButton);
        springLayout.putConstraint(SpringLayout.NORTH, crossLayoutButton, 0, SpringLayout.NORTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.SOUTH, crossLayoutButton, 20, SpringLayout.NORTH, crossLayoutButton);
        springLayout.putConstraint(SpringLayout.NORTH, landscapeLayoutButton, 0, SpringLayout.NORTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.SOUTH, landscapeLayoutButton, 20, SpringLayout.NORTH, landscapeLayoutButton);
        springLayout.putConstraint(SpringLayout.WEST, normalLayoutButton, 4, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, normalLayoutButton, 20, SpringLayout.WEST, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.WEST, focusedLayoutButton, 4, SpringLayout.EAST, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.EAST, focusedLayoutButton, 20, SpringLayout.WEST, focusedLayoutButton);
        springLayout.putConstraint(SpringLayout.WEST, crossLayoutButton, 4, SpringLayout.EAST, focusedLayoutButton);
        springLayout.putConstraint(SpringLayout.EAST, crossLayoutButton, 20, SpringLayout.WEST, crossLayoutButton);
        springLayout.putConstraint(SpringLayout.WEST, landscapeLayoutButton, 4, SpringLayout.EAST, crossLayoutButton);
        springLayout.putConstraint(SpringLayout.EAST, landscapeLayoutButton, 20, SpringLayout.WEST, landscapeLayoutButton);

        springLayout.putConstraint(SpringLayout.WEST, fpsLabel, 16, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.NORTH, fpsSpinner, 0, SpringLayout.NORTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.WEST, fpsSpinner, 4, SpringLayout.EAST, fpsLabel);
        springLayout.putConstraint(SpringLayout.EAST, fpsSpinner, 54, SpringLayout.WEST, fpsSpinner);
        springLayout.putConstraint(SpringLayout.BASELINE, fpsLabel, 0, SpringLayout.BASELINE, fpsSpinner);

        springLayout.putConstraint(SpringLayout.NORTH, helpButton, 0, SpringLayout.NORTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.SOUTH, helpButton, 20, SpringLayout.NORTH, helpButton);
        springLayout.putConstraint(SpringLayout.EAST, helpButton, -4, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, helpButton, -20, SpringLayout.EAST, helpButton);

        add(spritePanel1);
        add(spritePanel2);
        add(spritePanel3);
        add(spritePanel4);

        constraintAsNormal();
    }

    private void deselectCurrentButton() {
        switch(layoutState) {
            case NORMAL    -> normalLayoutButton.setIcon(layout1Icon);
            case FOCUSED   -> focusedLayoutButton.setIcon(layout2Icon);
            case CROSS     -> crossLayoutButton.setIcon(layout3Icon);
            case LANDSCAPE -> landscapeLayoutButton.setIcon(layout4Icon);
        }
    }

    private void constraintAsNormal() {
        layoutState = LayoutState.NORMAL;

        int halfHeight = (getHeight() - 32) / 2;

        springLayout.putConstraint(SpringLayout.WEST, spritePanel1, 4, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel1, 6, SpringLayout.SOUTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel1, -2, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel1, halfHeight, SpringLayout.NORTH, spritePanel1);

        springLayout.putConstraint(SpringLayout.EAST, spritePanel2, -4, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel2, 0, SpringLayout.NORTH, spritePanel1);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel2, 2, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel2, 0, SpringLayout.SOUTH, spritePanel1);

        springLayout.putConstraint(SpringLayout.WEST, spritePanel3, 4, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel3, 4, SpringLayout.SOUTH, spritePanel1);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel3, 0, SpringLayout.EAST, spritePanel1);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel3, 0, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.EAST, spritePanel4, 0, SpringLayout.EAST, spritePanel2);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel4, 0, SpringLayout.NORTH, spritePanel3);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel4, 0, SpringLayout.WEST, spritePanel2);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel4, 0, SpringLayout.SOUTH, spritePanel3);

        revalidate();
    }

    private void constraintAsFocused() {
        layoutState = LayoutState.FOCUSED;

        springLayout.putConstraint(SpringLayout.WEST, spritePanel1, 4, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel1, 6, SpringLayout.SOUTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel1, -4, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel1, -100, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.WEST, spritePanel2, 4, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel2, 4, SpringLayout.SOUTH, spritePanel1);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel2, (getWidth() / 3) - 7, SpringLayout.WEST, spritePanel2);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel2, -8, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.EAST, spritePanel4, -4, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel4, 0, SpringLayout.NORTH, spritePanel2);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel4, -(getWidth() / 3) + 7, SpringLayout.EAST, spritePanel4);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel4, 0, SpringLayout.SOUTH, spritePanel2);

        springLayout.putConstraint(SpringLayout.WEST, spritePanel3, 4, SpringLayout.EAST, spritePanel2);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel3, 0, SpringLayout.NORTH, spritePanel2);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel3, -4, SpringLayout.WEST, spritePanel4);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel3, 0, SpringLayout.SOUTH, spritePanel2);

        revalidate();
    }

    private void constraintAsCross() {
        layoutState = LayoutState.CROSS;

        int thirdWidth = getWidth() / 3;
        int thirdHeight = (getHeight() - 32) / 3;

        springLayout.putConstraint(SpringLayout.WEST, spritePanel1, thirdWidth, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel1, 30, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel1, thirdWidth, SpringLayout.WEST, spritePanel1);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel1, thirdHeight, SpringLayout.NORTH, spritePanel1);

        springLayout.putConstraint(SpringLayout.WEST, spritePanel2, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel2, 0, SpringLayout.SOUTH, spritePanel1);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel2, 0, SpringLayout.WEST, spritePanel1);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel2, thirdHeight, SpringLayout.NORTH, spritePanel2);

        springLayout.putConstraint(SpringLayout.EAST, spritePanel3, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel3, 0, SpringLayout.SOUTH, spritePanel1);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel3, 0, SpringLayout.EAST, spritePanel1);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel3, thirdHeight, SpringLayout.NORTH, spritePanel3);

        springLayout.putConstraint(SpringLayout.WEST, spritePanel4, 0, SpringLayout.WEST, spritePanel1);
        springLayout.putConstraint(SpringLayout.NORTH, spritePanel4, 0, SpringLayout.SOUTH, spritePanel2);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel4, 0, SpringLayout.EAST, spritePanel1);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel4, 0, SpringLayout.SOUTH, this);

        revalidate();
    }

    private void constraintAsLandscape() {
        layoutState = LayoutState.LANDSCAPE;

        int quarterHeight = (getHeight() - 32) / 4;

        springLayout.putConstraint(SpringLayout.NORTH, spritePanel1, 4, SpringLayout.SOUTH, normalLayoutButton);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel1, quarterHeight, SpringLayout.NORTH, spritePanel1);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel1, 4, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel1, -4, SpringLayout.EAST, this);

        springLayout.putConstraint(SpringLayout.NORTH, spritePanel2, 4, SpringLayout.SOUTH, spritePanel1);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel2, quarterHeight, SpringLayout.NORTH, spritePanel2);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel2, 0, SpringLayout.WEST, spritePanel1);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel2, 0, SpringLayout.EAST, spritePanel1);

        springLayout.putConstraint(SpringLayout.NORTH, spritePanel3, 4, SpringLayout.SOUTH, spritePanel2);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel3, quarterHeight, SpringLayout.NORTH, spritePanel3);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel3, 0, SpringLayout.WEST, spritePanel1);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel3, 0, SpringLayout.EAST, spritePanel1);

        springLayout.putConstraint(SpringLayout.NORTH, spritePanel4, 4, SpringLayout.SOUTH, spritePanel3);
        springLayout.putConstraint(SpringLayout.SOUTH, spritePanel4, 0, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.WEST, spritePanel4, 0, SpringLayout.WEST, spritePanel1);
        springLayout.putConstraint(SpringLayout.EAST, spritePanel4, 0, SpringLayout.EAST, spritePanel1);

        revalidate();
    }

    private static void setActiveSpritePanel(SpritePanel spritePanel) {
        if(activeSpritePanel != null) {
            activeSpritePanel.setBorder(null);
        }
        activeSpritePanel = spritePanel;
    }

    public class SpritePanel extends JPanel implements MouseListener {

        ArrayList<Frame> frames = new ArrayList<>();
        int framesIndex = 0;
        boolean isUpdating = false;

        Rectangle drawBounds = new Rectangle();

        public SpritePanel() {
            addMouseListener(this);
            setBackground(new Color(43, 43 ,43));

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);

                    if(canvas != null) calculateNewDrawBounds();
                }
            });
        }

        public void calculateNewDrawBounds() {
            double gridXRatio = (double) getWidth() / gridInformation.gridX();
            if(gridInformation.gridY() * gridXRatio < getHeight()) {
                drawBounds.width = (int) (gridInformation.gridX() * gridXRatio);
                drawBounds.height = (int) (gridInformation.gridY() * gridXRatio);
            } else {
                double gridYRatio = (double) getHeight() / gridInformation.gridY();
                drawBounds.width = (int) (gridInformation.gridX() * gridYRatio);
                drawBounds.height = (int) (gridInformation.gridY() * gridYRatio);
            }
            drawBounds.x = (getWidth() - drawBounds.width) / 2;
            drawBounds.y = (getHeight() - drawBounds.height) / 2;
        }

        public void addFrame(Point point) {
            Rectangle target = new Rectangle();

            target.x = point.x * gridInformation.gridX();
            target.y = point.y * gridInformation.gridY();
            target.width = gridInformation.gridX();
            target.height = gridInformation.gridY();

            Frame currentFrame = new Frame(target);
            frames.add(currentFrame);

            if(!isUpdating) setIsUpdating(true);
        }

        private void setIsUpdating(Boolean b) {
            if (b) addToTimer();
            else removeFromTimer();
            isUpdating = b;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(frames.isEmpty()) return;

            Frame currentFrame = frames.get(framesIndex);
            g.drawImage(currentFrame.getImage(), drawBounds.x, drawBounds.y, drawBounds.width, drawBounds.height, null);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(Controller.getActiveCanvas() == null) {
                NotificationPanel.playNotification(NotificationMessage.TOOL_REQUIRES_CANVAS);
                return;
            }
            setActiveSpritePanel(this);

            onNewAnimation();

            AnimationTool animationTool = new AnimationTool(this);
            Controller.setActiveTool(animationTool);
        }

        public void wipe() {
            onNewAnimation();
        }

        private void onNewAnimation() {
            if(isUpdating) setIsUpdating(false);
            frames.clear();
            framesIndex = 0;
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if(Controller.getActiveCanvas() == null) return;
            setBorder(new LineBorder(HIGHLIGHT_COLOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if(activeSpritePanel != this) {
                setBorder(null);
            }
        }

        ActionListener timerTask = e -> {
            if(framesIndex >= frames.size() - 1) framesIndex = 0;
            else framesIndex++;
            repaint();
        };

        public void addToTimer() {
            timer.addActionListener(timerTask);
        }

        public void removeFromTimer() {
            timer.removeActionListener(timerTask);
        }

        private class Frame {
            Rectangle target;

            public Frame(Rectangle target) {
                this.target = target;
            }

            public BufferedImage getImage() {
                return canvas.getImage().getSubimage(target.x, target.y, target.width, target.height);
            }
        }
    }
}
