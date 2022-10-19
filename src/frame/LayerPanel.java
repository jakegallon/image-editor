package frame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class LayerPanel extends JPanel {

    LayerPanelHandler layerPanelHandler;
    JPanel layerContainer;
    Controller controller;

    private final LinkedList<LayerWidget> layerWidgets = new LinkedList<>();

    private final JScrollPane scrollPane;

    LayerWidget activeLayerWidget;

    public LayerPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

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

    private static final Dimension SLIDER_THUMB_SIZE = (Dimension) UIManager.get("Slider.thumbSize");
    private static final int LAYER_WIDGET_HEIGHT = 42;

    private class LayerWidget extends JPanel implements MouseListener {

        Layer layer;

        private LayerWidget(Layer layer) {
            this.layer = layer;

            addMouseListener(this);

            setPreferredSize(new Dimension(layerContainer.getWidth(), LAYER_WIDGET_HEIGHT));
            setMaximumSize(new Dimension(layerContainer.getWidth(), LAYER_WIDGET_HEIGHT));
            setMinimumSize(new Dimension(layerContainer.getWidth(), LAYER_WIDGET_HEIGHT));

            SpringLayout springLayout = new SpringLayout();
            setLayout(springLayout);

            JLabel layerName = new JLabel(layer.getName());
            add(layerName);

            JPanel layerImage = new JPanel() {
              @Override
              protected void paintComponent(Graphics g) {
                  super.paintComponent(g);
                  g.drawImage(layer.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
                  if(layer == controller.getActiveCanvas().getActiveLayer()){
                      repaint();
                  }
              }
            };
            layerImage.setBorder(new LineBorder(Color.black));
            layerImage.setPreferredSize(new Dimension(LAYER_WIDGET_HEIGHT - 10, LAYER_WIDGET_HEIGHT - 10));
            add(layerImage);

            JSlider layerOpacity = new JSlider(0, 100, 100);
            add(layerOpacity);

            JButton deleteButton = new JButton("X");
            deleteButton.setBackground(Color.RED);
            deleteButton.setPreferredSize(new Dimension(16, 16));
            add(deleteButton);

            JButton lockButton = new JButton("L");
            lockButton.setBackground(Color.YELLOW);
            lockButton.setPreferredSize(new Dimension(16, 16));
            add(lockButton);

            JButton renameButton = new JButton("R");
            renameButton.setBackground(Color.GREEN);
            renameButton.setPreferredSize(new Dimension(16, 16));
            add(renameButton);

            springLayout.putConstraint(SpringLayout.NORTH, layerImage, 5, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.WEST, layerImage, 5, SpringLayout.WEST, this);
            springLayout.putConstraint(SpringLayout.NORTH, layerName, 0, SpringLayout.NORTH, layerImage);
            springLayout.putConstraint(SpringLayout.WEST, layerName, 10, SpringLayout.EAST, layerImage);
            springLayout.putConstraint(SpringLayout.WEST, layerOpacity, -SLIDER_THUMB_SIZE.width, SpringLayout.WEST, layerName);
            springLayout.putConstraint(SpringLayout.SOUTH, layerOpacity, 0, SpringLayout.SOUTH, layerImage);
            springLayout.putConstraint(SpringLayout.EAST, layerOpacity, 100, SpringLayout.WEST, layerOpacity);
            springLayout.putConstraint(SpringLayout.NORTH, deleteButton, 4, SpringLayout.NORTH, this);
            springLayout.putConstraint(SpringLayout.EAST, deleteButton, -4, SpringLayout.EAST, this);
            springLayout.putConstraint(SpringLayout.SOUTH, lockButton, -4, SpringLayout.SOUTH, this);
            springLayout.putConstraint(SpringLayout.EAST, lockButton, 0, SpringLayout.EAST, deleteButton);
            springLayout.putConstraint(SpringLayout.NORTH, renameButton, 0, SpringLayout.NORTH, deleteButton);
            springLayout.putConstraint(SpringLayout.EAST, renameButton, -4, SpringLayout.WEST, deleteButton);

            if(layer == controller.getActiveCanvas().getActiveLayer()){
                setNewActiveLayerWidget(this);
            }
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

        @Override
        public void mouseClicked(MouseEvent e) {
            controller.getActiveCanvas().setActiveLayer(layer);
            setNewActiveLayerWidget(this);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}