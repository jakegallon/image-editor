package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {

    JSplitPane leftMiddle, leftMiddleRight;
    JPanel leftPanel, rightPanel, topPanel;
    CanvasPanel canvasPanel;
    InfoPanel infoPanel;

    int leftPanelWidth = 300, rightPanelWidth = 300;

    public Window() {
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

        // Main panels
        leftPanel=new JPanel();
        leftPanel.setBackground(Color.RED);
        leftPanel.setMinimumSize(new Dimension(250, 0));
        rightPanel=new JPanel();
        rightPanel.setBackground(Color.BLUE);
        rightPanel.setMinimumSize(new Dimension(250, 0));
        JPanel middlePanel=new JPanel();
        middlePanel.setMinimumSize(new Dimension(250, 0));
        middlePanel.setLayout(new BorderLayout());

        // Middle sub panels
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
