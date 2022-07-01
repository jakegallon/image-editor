package frame;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    JPanel leftPanel, rightPanel, centerPanel, topPanel, bottomPanel;

    public Window() {
        initializeWindow();
        createLayout();
    }

    private void initializeWindow() {
        setTitle("Image Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
    }

    private void createLayout(){
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        leftPanel=new JPanel();
        leftPanel.setBackground(Color.RED);
        leftPanel.setPreferredSize(new Dimension(300, pane.getHeight()));
        pane.add(leftPanel, BorderLayout.LINE_START);

        rightPanel=new JPanel();
        rightPanel.setBackground(Color.BLUE);
        rightPanel.setPreferredSize(new Dimension(300, pane.getHeight()));
        pane.add(rightPanel, BorderLayout.LINE_END);

        JPanel centerContainer=new JPanel();
        pane.add(centerContainer, BorderLayout.CENTER);
        centerContainer.setLayout(new BorderLayout());

        centerPanel=new JPanel();
        centerPanel.setBackground(Color.YELLOW);
        centerContainer.add(centerPanel, BorderLayout.CENTER);

        topPanel=new JPanel();
        topPanel.setBackground(Color.MAGENTA);
        topPanel.setPreferredSize(new Dimension(pane.getWidth(), 50));
        centerContainer.add(topPanel, BorderLayout.PAGE_START);

        bottomPanel=new JPanel();
        bottomPanel.setBackground(Color.PINK);
        bottomPanel.setPreferredSize(new Dimension(pane.getWidth(), 50));
        centerContainer.add(bottomPanel, BorderLayout.PAGE_END);
    }

}
