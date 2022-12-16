package main;

import com.formdev.flatlaf.FlatDarkLaf;
import frame.Frame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        UIManager.put("TabbedPane.tabHeight", 15);
        UIManager.put("TabbedPane.tabInsets", new Insets(2, 10, 2, 10));
        UIManager.put("Slider.focusedColor", new Color(0, 0, 0, 0));
        UIManager.put("Slider.thumbSize", new Dimension(8, 8));

        Window window = new Frame();

        window.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/icon.png")));
    }
}
