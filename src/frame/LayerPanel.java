package frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    public LayerPanel() {
        setMinimumSize(new Dimension(0, 250));
        setPreferredSize(new Dimension(0, 400));
        setLayout(new BorderLayout());

        try {
            gradient = ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/gradient.png")));
            gradient_alt = ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/gradient_alt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        layerPanelHandler = new LayerPanelHandler(this);
        add(layerPanelHandler, BorderLayout.PAGE_START);

        layerContainer = new JPanel();
        layerContainer.setLayout(new BoxLayout(layerContainer, BoxLayout.PAGE_AXIS));

        scrollPane = new JScrollPane(layerContainer);
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
        int oldIndex = getIndexOfLayerWidget(layerWidget);
        Collections.swap(layerWidgets, oldIndex, index);
        redrawLayerContainerFromLayerWidgets();
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

    private static final Dimension SLIDER_THUMB_SIZE = (Dimension) UIManager.get("Slider.thumbSize");
    private static final int LAYER_WIDGET_HEIGHT = 42;

    private class LayerWidget extends JPanel implements MouseListener, MouseMotionListener {

        Layer layer;
        private SpringLayout springLayout;

        private JLabel layerName;
        private JPanel layerImage;
        private JSlider layerOpacity;
        private JButton deleteButton;
        private JButton lockButton;
        private JButton renameButton;
        private JToggleButton visibleButton;

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
            layerName = new JLabel(layer.getName());
            add(layerName);

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
            add(layerOpacity);

            layerOpacity.addChangeListener(e -> layer.setOpacity(layerOpacity.getValue()));

            deleteButton = new JButton("X");
            deleteButton.setBackground(Color.RED);
            deleteButton.setPreferredSize(new Dimension(16, 16));
            add(deleteButton);

            deleteButton.addActionListener(deleteListener());

            lockButton = new JButton("L");
            lockButton.setBackground(Color.YELLOW);
            lockButton.setPreferredSize(new Dimension(16, 16));
            add(lockButton);

            lockButton.addActionListener(lockListener());

            renameButton = new JButton("R");
            renameButton.setBackground(Color.GREEN);
            renameButton.setPreferredSize(new Dimension(16, 16));
            add(renameButton);

            renameButton.addActionListener(renameListener());

            visibleButton = new JToggleButton("V");
            visibleButton.setBackground(Color.GRAY);
            visibleButton.setPreferredSize(new Dimension(16, 16));
            add(visibleButton);
            visibleButton.setSelected(true);

            visibleButton.addChangeListener(visibleListener());
        }

        private ActionListener deleteListener() {
            return e -> {
                ArrayList<Layer> l = Controller.getActiveCanvas().layers;
                deleteLayer(l.indexOf(layer));
                l.remove(layer);
            };
        }

        //todo
        private ActionListener lockListener() {
            return null;
        }

        //todo
        private ActionListener renameListener() {
            return null;
        }

        private ChangeListener visibleListener() {
            return e -> layer.setVisible(visibleButton.isSelected());
        }

        private void alignBase() {
            springLayout.putConstraint(SpringLayout.NORTH, layerImage, 5, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.WEST, layerImage, 5, SpringLayout.WEST, this);
            springLayout.putConstraint(SpringLayout.NORTH, visibleButton, 0, SpringLayout.NORTH, layerImage);
            springLayout.putConstraint(SpringLayout.WEST, visibleButton, 4, SpringLayout.EAST, layerImage);
            springLayout.putConstraint(SpringLayout.NORTH, layerName, 0, SpringLayout.NORTH, visibleButton);
            springLayout.putConstraint(SpringLayout.WEST, layerName, 4, SpringLayout.EAST, visibleButton);
            springLayout.putConstraint(SpringLayout.WEST, layerOpacity, -SLIDER_THUMB_SIZE.width, SpringLayout.WEST, visibleButton);
            springLayout.putConstraint(SpringLayout.SOUTH, layerOpacity, 0, SpringLayout.SOUTH, layerImage);
            springLayout.putConstraint(SpringLayout.EAST, layerOpacity, 100, SpringLayout.WEST, layerOpacity);
            springLayout.putConstraint(SpringLayout.NORTH, deleteButton, 4, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.EAST, deleteButton, -4, SpringLayout.EAST, this);
            springLayout.putConstraint(SpringLayout.SOUTH, lockButton, -4, SpringLayout.SOUTH, this);
            springLayout.putConstraint(SpringLayout.EAST, lockButton, 0, SpringLayout.EAST, deleteButton);
            springLayout.putConstraint(SpringLayout.NORTH, renameButton, 0, SpringLayout.NORTH, deleteButton);
            springLayout.putConstraint(SpringLayout.EAST, renameButton, -4, SpringLayout.WEST, deleteButton);
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

            unfloatWidget();
            isMousePressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            isMouseOverWidget = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!getVisibleRect().contains(e.getPoint())) {
                isMouseOverWidget = false;
                if (isMousePressed) floatWidget();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int relativeIndexTarget = Math.floorDiv(e.getPoint().y, LAYER_WIDGET_HEIGHT);
            if(relativeIndexTarget != currentIndexOffset) {
                currentIndexOffset = relativeIndexTarget;
                if(isValidLayerWidgetsIndex(index + currentIndexOffset)){
                    trueIndex = index + currentIndexOffset;
                    swapLayerWidgetToIndex(this, trueIndex);
                    index = getIndexOfLayerWidget(this);
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