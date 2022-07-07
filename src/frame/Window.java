package frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window extends JFrame {

    JSplitPane leftMiddle, leftMiddleRight;
    JPanel leftPanel, rightPanel, centerPanel, topPanel, bottomPanel;

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
        centerPanel =new JPanel();
        centerPanel.setBackground(new Color(43, 43 ,43));

        BufferedImage img;
        try {
            img = ImageIO.read(new File("res/spritesheet.png").getAbsoluteFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JLabel picture = new JLabel(new ImageIcon(img));

        centerPanel.add(picture);

        JScrollPane drawingComponent = new JScrollPane(centerPanel);
        middlePanel.add(new CanvasPanel(), BorderLayout.CENTER);
        topPanel=new JPanel();
        topPanel.setBackground(Color.MAGENTA);
        topPanel.setPreferredSize(new Dimension(pane.getWidth(), 30));
        middlePanel.add(topPanel, BorderLayout.PAGE_START);
        bottomPanel=new JPanel();
        bottomPanel.setBackground(Color.PINK);
        bottomPanel.setPreferredSize(new Dimension(pane.getWidth(), 30));
        middlePanel.add(bottomPanel, BorderLayout.PAGE_END);

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
