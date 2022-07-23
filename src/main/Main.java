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

        Window window = new Frame();
    }
}
