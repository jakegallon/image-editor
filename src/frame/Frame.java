package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Frame extends JFrame {

    JSplitPane leftMiddle, leftMiddleRight;
    JPanel leftPanel, rightPanel, topPanel;

    CanvasPanel canvasPanel;
    InfoPanel infoPanel;

    int leftPanelWidth = 350, rightPanelWidth = 350;

    public Frame() {
        setVisible(true);
        initializeWindow();
        createLayout();
    }

    private void initializeWindow() {
        setTitle("Image Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setMinimumSize(new Dimension(960, 540));
    }

    private void createLayout(){
        Container pane = getContentPane();

        // Temporary panel creations
        // leftPanel - toolBar
        JPanel toolBar = new JPanel();
        toolBar.setBackground(Color.gray);
        toolBar.setPreferredSize(new Dimension(40, 0));
        // leftPanel - brushSettings
        JPanel brushSettings = new JPanel();
        brushSettings.setBackground(Color.cyan);
        brushSettings.setMinimumSize(new Dimension(0, 250));
        // leftPanel - colorSettings
        JPanel colorSettings = new JPanel();
        colorSettings.setBackground(Color.green);
        colorSettings.setMinimumSize(new Dimension(0, 250));
        // rightPanel - canvasOverview
        JPanel canvasOverview = new JPanel();
        canvasOverview.setBackground(Color.RED);
        canvasOverview.setMinimumSize(new Dimension(0, 250));
        // rightPanel - animationPanel
        JPanel animationPanel = new JPanel();
        animationPanel.setBackground(Color.ORANGE);
        animationPanel.setMinimumSize(new Dimension(0, 250));
        // rightPanel - layerPanel
        JPanel layerPanel = new JPanel();
        layerPanel.setBackground(Color.YELLOW);
        layerPanel.setMinimumSize(new Dimension(0, 250));

        // left panel
        leftPanel=new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.RED);
        leftPanel.setMinimumSize(new Dimension(250, 0));

        leftPanel.add(toolBar, BorderLayout.LINE_START);
        JSplitPane brushColor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, brushSettings, colorSettings);
        brushColor.setResizeWeight(1.0);
        leftPanel.add(brushColor);

        // Right panel
        rightPanel=new JPanel(new BorderLayout());
        JSplitPane canvasAnimation = new JSplitPane(JSplitPane.VERTICAL_SPLIT, canvasOverview, animationPanel);
        JSplitPane canvasAnimationLayer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, canvasAnimation, layerPanel);
        canvasAnimationLayer.setResizeWeight(1.0);

        rightPanel.add(canvasAnimationLayer);
        rightPanel.setMinimumSize(new Dimension(250, 0));

        // Main panel
        JPanel middlePanel=new JPanel();
        middlePanel.setMinimumSize(new Dimension(250, 0));
        middlePanel.setLayout(new BorderLayout());

        topPanel=new JPanel();
        topPanel.setBackground(Color.MAGENTA);
        topPanel.setPreferredSize(new Dimension(pane.getWidth(), 30));
        middlePanel.add(topPanel, BorderLayout.PAGE_START);
        infoPanel = new InfoPanel();
        middlePanel.add(infoPanel, BorderLayout.PAGE_END);
        canvasPanel =new CanvasPanel(infoPanel);
        middlePanel.add(canvasPanel, BorderLayout.CENTER);

        // Triple SplitPane
        leftMiddle = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);
        leftMiddle.setDividerLocation(leftPanelWidth);
        leftMiddleRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftMiddle, rightPanel);
        leftMiddleRight.setDividerLocation(pane.getWidth() - rightPanelWidth);
        leftMiddleRight.setResizeWeight(1.0);

        pane.add(leftMiddleRight);

        leftMiddleRight.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                leftMiddleRight.setDividerLocation(leftMiddleRight.getWidth() - rightPanelWidth);
                leftMiddle.setDividerLocation(leftPanelWidth);
            }
        });

        leftMiddle.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
            leftPanelWidth = (int)evt.getNewValue();
        });

        leftMiddleRight.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
            rightPanelWidth = pane.getWidth() - (int)evt.getNewValue();
        });
    }
}
