package frame;

import action.LayerDeletionAction;
import action.LayerMergeAction;
import action.LayerOrderAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class LayerPanel extends JPanel {

    LayerPanelHandler layerPanelHandler;
    JPanel layerContainer;

    private final LinkedList<LayerWidget> layerWidgets = new LinkedList<>();

    private final JScrollPane scrollPane;

    LayerWidget activeLayerWidget;

    private static BufferedImage gradient;
    private static BufferedImage gradient_alt;

    private static ImageIcon lockedIcon;
    private static ImageIcon unlockedIcon;
    private static ImageIcon mergeIcon;
    private static ImageIcon renameIcon;
    private static ImageIcon deleteIcon;
    private static ImageIcon visibleIcon;
    private static ImageIcon notVisibleIcon;

    public LayerPanel() {
        setMinimumSize(new Dimension(0, 250));
        setPreferredSize(new Dimension(0, 400));
        setLayout(new BorderLayout());

        try {
            gradient = ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/gradient.png")));
            gradient_alt = ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/gradient_alt.png")));
            lockedIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/locked.png"))));
            unlockedIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/unlocked.png"))));
            mergeIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/merge.png"))));
            renameIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/rename.png"))));
            deleteIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/delete.png"))));
            visibleIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/visible.png"))));
            notVisibleIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/notvisible.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        layerPanelHandler = new LayerPanelHandler();
        add(layerPanelHandler, BorderLayout.PAGE_START);

        layerContainer = new JPanel();
        layerContainer.setLayout(new BoxLayout(layerContainer, BoxLayout.PAGE_AXIS));

        scrollPane = new JScrollPane(layerContainer);
        scrollPane.setBorder(new MatteBorder(1, 0, 0, 0, new Color(97, 99, 101)));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                callWidgetResize();
            }
        });

        scrollPane.getVerticalScrollBar().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                callWidgetResize();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentShown(e);
                callWidgetResize();
            }
        });
    }

    public void onCanvasSwitch() {
        layerContainer.removeAll();
        layerWidgets.clear();

        Canvas activeCanvas = Controller.getActiveCanvas();
        if(activeCanvas == null) {
            layerContainer.revalidate();
            layerContainer.repaint();
            return;
        }

        ArrayList<Layer> layers = activeCanvas.layers;
        if(!layers.isEmpty()) {
            for(int i = layers.size() - 1; i >= 0; i--) {
                LayerWidget layerWidget = new LayerWidget(layers.get(i));
                layerWidgets.add(0, layerWidget);

                layerContainer.add(layerWidget, 0);
            }
        }
        layerContainer.revalidate();
        layerContainer.repaint();
    }

    private void callWidgetResize() {
        for(Component c : layerContainer.getComponents()) {
            if(c instanceof LayerWidget) {
                ((LayerWidget) c).resize(getWidth());
            }
        }
    }

    public void addLayer(Layer layer, int index) {
        LayerWidget layerWidget = new LayerWidget(layer);
        layerWidgets.add(index, layerWidget);

        layerContainer.add(layerWidget, index);
        layerContainer.revalidate();
        layerContainer.repaint();
    }

    public void deleteLayer(int index) {
        if(layerWidgets.size() == 0) return;
        layerWidgets.remove(index);

        layerContainer.remove(index);
        layerContainer.revalidate();
        layerContainer.repaint();

        if(layerWidgets.size() <= 0) return;
        if(index >= layerWidgets.size()) index = layerWidgets.size() - 1;

        LayerWidget layerWidget = layerWidgets.get(index);
        layerWidget.setActive();
        activeLayerWidget = layerWidget;
    }

    private void setNewActiveLayerWidget(LayerWidget layerWidget) {
        if(activeLayerWidget != null) activeLayerWidget.unsetActive();
        layerWidget.setActive();
        activeLayerWidget = layerWidget;
    }

    private int getIndexOfLayerWidget(LayerWidget layerWidget) {
        return layerWidgets.indexOf(layerWidget);
    }

    private boolean isValidLayerWidgetsIndex(int index) {
        return index >= 0 && index < layerWidgets.size();
    }

    private void swapLayerWidgetToIndex(LayerWidget layerWidget, int index) {
        layerWidgets.remove(layerWidget);
        layerWidgets.add(index, layerWidget);

        redrawLayerContainerFromLayerWidgets();
        reorderCanvasByLayerWidgets();
    }

    public void moveLayerIndexToIndex(int takenIndex, int placedIndex) {
        LayerWidget storedWidget = layerWidgets.get(takenIndex);
        layerWidgets.remove(takenIndex);
        layerWidgets.add(placedIndex, storedWidget);

        redrawLayerContainerFromLayerWidgets();
        reorderCanvasByLayerWidgets();
    }

    private void redrawLayerContainerFromLayerWidgets() {
        layerContainer.removeAll();

        for(int i = layerWidgets.size() - 1; i >= 0; i--) {
            LayerWidget layerWidget = layerWidgets.get(i);
            layerContainer.add(layerWidget, 0);
        }

        layerContainer.revalidate();
        layerContainer.repaint();
    }

    private void reorderCanvasByLayerWidgets() {
        ArrayList<Layer> layers = new ArrayList<>();

        for(LayerWidget layerWidget : layerWidgets) {
            layers.add(layerWidget.layer);
        }

        Canvas activeCanvas = Controller.getActiveCanvas();
        activeCanvas.layers = layers;
    }

    private static final Dimension SLIDER_THUMB_SIZE = (Dimension) UIManager.get("Slider.thumbSize");
    private static final int LAYER_WIDGET_HEIGHT = 42;

    private class LayerWidget extends JPanel implements MouseListener, MouseMotionListener {

        private final Layer layer;
        private SpringLayout springLayout;

        private JLabel layerName;
        private JPanel layerImage;
        private JSlider layerOpacity;
        private JButton deleteButton;
        private JToggleButton lockButton;
        private JButton renameButton;
        private JButton mergeDownButton;
        private JToggleButton visibleButton;
        private JTextField renameTextField;

        private LayerWidget(Layer layer) {
            this.layer = layer;

            init();
        }

        private void init() {
            addMouseListener(this);
            addMouseMotionListener(this);

            setPreferredSize(new Dimension(layerContainer.getWidth(), LAYER_WIDGET_HEIGHT));
            setMaximumSize(new Dimension(layerContainer.getWidth(), LAYER_WIDGET_HEIGHT));
            setMinimumSize(new Dimension(layerContainer.getWidth(), LAYER_WIDGET_HEIGHT));

            springLayout = new SpringLayout();
            setLayout(springLayout);

            addWidgetComponents();
            alignBase();

            if(Controller.isActiveLayer(layer)){
                setNewActiveLayerWidget(this);
            }
        }

        private void addWidgetComponents() {
            renameTextField = new JTextField(layer.getName());
            renameTextField.setVisible(false);
            add(renameTextField);


            layerName = new JLabel(layer.getName());
            add(layerName);

            layerName.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    if(e.getClickCount() > 1) {
                        renameTextField.setVisible(true);
                        renameTextField.grabFocus();

                        renameTextField.addKeyListener(keyListener);
                        Toolkit.getDefaultToolkit().addAWTEventListener(awtEventListener, AWTEvent.MOUSE_EVENT_MASK);
                    }
                }
            });

            layerName.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

            layerImage = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(layer.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
                    if(Controller.isActiveLayer(layer)){
                        repaint();
                    }
                }
            };
            layerImage.setBorder(new LineBorder(Color.black));
            layerImage.setPreferredSize(new Dimension(LAYER_WIDGET_HEIGHT - 10, LAYER_WIDGET_HEIGHT - 10));
            add(layerImage);

            layerOpacity = new JSlider(0, 255, 255);
            layerOpacity.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(layerOpacity);

            layerOpacity.addChangeListener(e -> layer.setOpacity(layerOpacity.getValue()));

            deleteButton = new JButton();
            deleteButton.setIcon(deleteIcon);
            deleteButton.setBorderPainted(false);
            deleteButton.setBackground(new Color(0, 0, 0, 0));
            deleteButton.setPreferredSize(new Dimension(16, 16));
            deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(deleteButton);

            deleteButton.addActionListener(deleteListener());

            lockButton = new JToggleButton();
            if(layer.isLocked()) {
                lockButton.setIcon(lockedIcon);
                lockButton.setSelected(true);
            } else {
                lockButton.setIcon(unlockedIcon);
                lockButton.setSelected(false);
            }
            lockButton.setBorderPainted(false);
            lockButton.setBackground(new Color(0, 0, 0, 0));
            lockButton.setPreferredSize(new Dimension(16, 16));
            lockButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(lockButton);

            lockButton.addActionListener(lockListener());

            renameButton = new JButton();
            renameButton.setIcon(renameIcon);
            renameButton.setBorderPainted(false);
            renameButton.setBackground(new Color(0, 0, 0, 0));
            renameButton.setPreferredSize(new Dimension(16, 16));
            renameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(renameButton);

            renameButton.addActionListener(renameListener());

            visibleButton = new JToggleButton();
            if(layer.isVisible()) {
                visibleButton.setIcon(visibleIcon);
                visibleButton.setSelected(true);
            } else {
                visibleButton.setIcon(notVisibleIcon);
                visibleButton.setSelected(false);
            }
            visibleButton.setBorderPainted(false);
            visibleButton.setBackground(new Color(0, 0, 0, 0));
            visibleButton.setPreferredSize(new Dimension(16, 16));
            visibleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(visibleButton);

            visibleButton.addActionListener(visibleListener());

            mergeDownButton = new JButton();
            mergeDownButton.setIcon(mergeIcon);
            mergeDownButton.setBorderPainted(false);
            mergeDownButton.setBackground(new Color(0, 0, 0, 0));
            mergeDownButton.setPreferredSize(new Dimension(16, 16));
            mergeDownButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(mergeDownButton);

            mergeDownButton.addActionListener(mergeDownListener());
        }

        private ActionListener deleteListener() {
            return e -> {
                ArrayList<Layer> l = Controller.getActiveCanvas().layers;
                deleteLayer(l.indexOf(layer));
                l.remove(layer);

                LayerDeletionAction thisAction = new LayerDeletionAction(layer, index);
                Controller.getActiveCanvas().undoManager.addEdit(thisAction);
            };
        }

        private ActionListener lockListener() {
            return e -> {
                layer.toggleLayerLock();

                if(layer.isLocked()) {
                    lockButton.setIcon(lockedIcon);
                    lockButton.setSelected(true);
                } else {
                    lockButton.setIcon(unlockedIcon);
                    lockButton.setSelected(false);
                }
            };
        }

        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    onRenameEnd();
                } else if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
                    layer.setName(renameTextField.getText());
                    layerName.setText(layer.getName());
                    onRenameEnd();
                }
            }
        };

        AWTEventListener awtEventListener = awtEvent -> {
            MouseEvent mouseEvent = (MouseEvent) awtEvent;
            if(mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
                if(mouseEvent.getComponent() != renameTextField) {
                    onRenameEnd();
                }
            }
        };

        private ActionListener renameListener() {
            return e -> {
                renameTextField.setVisible(true);
                renameTextField.grabFocus();

                renameTextField.addKeyListener(keyListener);
                Toolkit.getDefaultToolkit().addAWTEventListener(awtEventListener, AWTEvent.MOUSE_EVENT_MASK);
            };
        }

        private void onRenameEnd() {
            renameTextField.removeKeyListener(keyListener);
            Toolkit.getDefaultToolkit().removeAWTEventListener(awtEventListener);
            renameTextField.setVisible(false);
            renameTextField.removeActionListener(renameListener());
        }

        private ActionListener mergeDownListener() {
            return e -> {
                ArrayList<Layer> l = Controller.getActiveCanvas().layers;
                int targetIndex = l.indexOf(layer) + 1;
                if(targetIndex >= l.size()) return;

                Layer targetLayer = Controller.getActiveCanvas().layers.get(targetIndex);

                LayerMergeAction thisAction = new LayerMergeAction(layer, targetLayer, targetIndex);
                Controller.getActiveCanvas().undoManager.addEdit(thisAction);

                Controller.mergeLayerIntoLayer(layer, targetLayer, targetIndex);
            };
        }

        private ActionListener visibleListener() {
            return e -> {
                layer.setVisible(visibleButton.isSelected());

                if(layer.isVisible()) {
                    visibleButton.setIcon(visibleIcon);
                    visibleButton.setSelected(true);
                } else {
                    visibleButton.setIcon(notVisibleIcon);
                    visibleButton.setSelected(false);
                }
            };
        }

        private void alignBase() {
            springLayout.putConstraint(SpringLayout.NORTH, layerImage, 5, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.WEST, layerImage, 5, SpringLayout.WEST, this);
            springLayout.putConstraint(SpringLayout.NORTH, visibleButton, 0, SpringLayout.NORTH, layerImage);
            springLayout.putConstraint(SpringLayout.WEST, visibleButton, 4, SpringLayout.EAST, layerImage);
            springLayout.putConstraint(SpringLayout.NORTH, layerName, 0, SpringLayout.NORTH, visibleButton);
            springLayout.putConstraint(SpringLayout.WEST, layerName, 9, SpringLayout.EAST, visibleButton);
            springLayout.putConstraint(SpringLayout.WEST, layerOpacity, -SLIDER_THUMB_SIZE.width, SpringLayout.WEST, visibleButton);
            springLayout.putConstraint(SpringLayout.SOUTH, layerOpacity, 0, SpringLayout.SOUTH, layerImage);
            springLayout.putConstraint(SpringLayout.EAST, layerOpacity, 100, SpringLayout.WEST, layerOpacity);
            springLayout.putConstraint(SpringLayout.NORTH, deleteButton, 4, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.EAST, deleteButton, -4, SpringLayout.EAST, this);
            springLayout.putConstraint(SpringLayout.SOUTH, lockButton, -4, SpringLayout.SOUTH, this);
            springLayout.putConstraint(SpringLayout.EAST, lockButton, 0, SpringLayout.EAST, deleteButton);
            springLayout.putConstraint(SpringLayout.NORTH, mergeDownButton, 0, SpringLayout.NORTH, deleteButton);
            springLayout.putConstraint(SpringLayout.EAST, mergeDownButton, -4, SpringLayout.WEST, deleteButton);
            springLayout.putConstraint(SpringLayout.NORTH, renameButton, 0, SpringLayout.NORTH, mergeDownButton);
            springLayout.putConstraint(SpringLayout.EAST, renameButton, -4, SpringLayout.WEST, mergeDownButton);
            springLayout.putConstraint(SpringLayout.BASELINE, renameTextField, 0, SpringLayout.BASELINE, layerName);
            springLayout.putConstraint(SpringLayout.WEST, renameTextField, -7, SpringLayout.WEST, layerName);
            springLayout.putConstraint(SpringLayout.EAST, renameTextField, -5, SpringLayout.WEST, renameButton);
        }

        JPanel floatGradient;

        private void floatWidget() {
            alignFloated();

            floatGradient = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if(activeLayerWidget == this.getParent()) {
                        g.drawImage(gradient, 0, 0, floatGradient.getWidth(), floatGradient.getHeight(), null);
                    } else {
                        g.drawImage(gradient_alt, 0, 0, floatGradient.getWidth(), floatGradient.getHeight(), null);
                    }
                }
            };
            add(floatGradient);
            springLayout.putConstraint(SpringLayout.WEST, floatGradient, 0, SpringLayout.WEST, this);
            springLayout.putConstraint(SpringLayout.NORTH, floatGradient, 0, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.EAST, floatGradient, -5, SpringLayout.WEST, layerImage);
            springLayout.putConstraint(SpringLayout.SOUTH, floatGradient, 0, SpringLayout.SOUTH, this);

            revalidate();
        }

        private void unfloatWidget() {
            alignNormal();

            if(floatGradient != null) {
                remove(floatGradient);
            }

            revalidate();
            repaint();
        }

        private void alignNormal() {
            springLayout.putConstraint(SpringLayout.WEST, layerImage, 5, SpringLayout.WEST, this);
        }

        private void alignFloated() {
            springLayout.putConstraint(SpringLayout.WEST, layerImage, 25, SpringLayout.WEST, this);
        }

        public void setActive() {
            setBackground(new Color(64, 75, 93));
        }

        public void unsetActive() {
            setBackground(null);
        }

        public void resize(int width) {
            if(scrollPane.getVerticalScrollBar().isVisible()) {
                width -= scrollPane.getVerticalScrollBar().getWidth();
            }
            setBounds(getX(), getY(), width, LAYER_WIDGET_HEIGHT);
            setMaximumSize(new Dimension(width, LAYER_WIDGET_HEIGHT));
            setMinimumSize(new Dimension(width, LAYER_WIDGET_HEIGHT));
            setPreferredSize(new Dimension(width, LAYER_WIDGET_HEIGHT));
        }

        private boolean isMouseOverWidget = false;

        private boolean isMousePressed = false;

        private int initialIndex;
        private int index;
        private int currentIndexOffset;
        private int trueIndex;

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        private void resetIndexingVariables() {
            currentIndexOffset = 0;
            index = getIndexOfLayerWidget(this);
            trueIndex = index;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            isMousePressed = true;
            currentIndexOffset = 0;
            index = getIndexOfLayerWidget(this);
            initialIndex = index;
            trueIndex = index;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(isMouseOverWidget && index == initialIndex) {
                setLayerAsActive();
            }

            if(index != initialIndex) {
                LayerOrderAction thisAction = new LayerOrderAction(initialIndex, index);
                Controller.getActiveCanvas().undoManager.addEdit(thisAction);
            }

            unfloatWidget();
            isMousePressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            isMouseOverWidget = true;
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!getVisibleRect().contains(e.getPoint())) {
                isMouseOverWidget = false;
                if (isMousePressed) floatWidget();
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int relativeIndexTarget = Math.floorDiv(e.getPoint().y, LAYER_WIDGET_HEIGHT);
            if(relativeIndexTarget != currentIndexOffset) {
                currentIndexOffset = relativeIndexTarget;
                if(isValidLayerWidgetsIndex(index + currentIndexOffset)){
                    trueIndex = index + currentIndexOffset;
                    swapLayerWidgetToIndex(this, trueIndex);
                    resetIndexingVariables();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        private void setLayerAsActive() {
            Controller.getActiveCanvas().setActiveLayer(layer);
            setNewActiveLayerWidget(this);
        }
    }
}