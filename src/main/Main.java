package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        createWindow();
    }

    private static void createWindow(){
        JFrame frame = new JFrame("Image Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);

        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        JPanel leftPanel=new JPanel();
        leftPanel.setBackground(Color.RED);
        leftPanel.setPreferredSize(new Dimension(300, pane.getHeight()));
        pane.add(leftPanel, BorderLayout.LINE_START);

        JPanel rightPanel=new JPanel();
        rightPanel.setBackground(Color.BLUE);
        rightPanel.setPreferredSize(new Dimension(300, pane.getHeight()));
        pane.add(rightPanel, BorderLayout.LINE_END);

        JPanel centerContainer=new JPanel();
        pane.add(centerContainer, BorderLayout.CENTER);
        centerContainer.setLayout(new BorderLayout());

        JPanel centerPanel=new JPanel();
        centerPanel.setBackground(Color.YELLOW);
        centerContainer.add(centerPanel, BorderLayout.CENTER);

        JPanel topPanel=new JPanel();
        topPanel.setBackground(Color.MAGENTA);
        topPanel.setPreferredSize(new Dimension(pane.getWidth(), 50));
        centerContainer.add(topPanel, BorderLayout.PAGE_START);

        JPanel bottomPanel=new JPanel();
        bottomPanel.setBackground(Color.PINK);
        bottomPanel.setPreferredSize(new Dimension(pane.getWidth(), 50));
        centerContainer.add(bottomPanel, BorderLayout.PAGE_END);

        frame.setVisible(true);
    }
}
