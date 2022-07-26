package frame;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JTabbedPane {

    public ColorPanel() {
        addColorPanel();
        addPalettePanel();
    }

    private void addColorPanel() {
        JPanel colorPanel = new JPanel(new GridBagLayout());
        JPanel colorSelector = new ColorSelector();
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        colorPanel.add(colorSelector, c);

        JPanel colorValues = new JPanel();
        colorValues.setLayout(new BoxLayout(colorValues, BoxLayout.Y_AXIS));

        SpringLayout rgbLayout = new SpringLayout();
        JPanel rgbPanel = new JPanel(rgbLayout);

        // rgb
        Font uiFont = new Font("Segoe UI", Font.PLAIN, 15);
        JLabel redLabel   = new JLabel("R: ");
        redLabel.setFont(uiFont);
        rgbPanel.add(redLabel);
        JSpinner redSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        rgbPanel.add(redSpinner);
        JLabel greenLabel = new JLabel("G: ");
        greenLabel.setFont(uiFont);
        rgbPanel.add(greenLabel);
        JSpinner greenSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        rgbPanel.add(greenSpinner);
        JLabel blueLabel  = new JLabel("B: ");
        blueLabel.setFont(uiFont);
        rgbPanel.add(blueLabel);
        JSpinner blueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        rgbPanel.add(blueSpinner);

        rgbLayout.putConstraint(SpringLayout.EAST, redSpinner, 0, SpringLayout.EAST, rgbPanel);
        rgbLayout.putConstraint(SpringLayout.NORTH, redSpinner, 6, SpringLayout.NORTH, rgbPanel);
        rgbLayout.putConstraint(SpringLayout.EAST, greenSpinner, 0, SpringLayout.EAST, redSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, greenSpinner, 0, SpringLayout.SOUTH, redSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, blueSpinner, 0, SpringLayout.EAST, redSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, blueSpinner, 0, SpringLayout.SOUTH, greenSpinner);

        rgbLayout.putConstraint(SpringLayout.EAST, redLabel, 0, SpringLayout.WEST, redSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, redLabel, 0, SpringLayout.NORTH, redSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, greenLabel, 0, SpringLayout.WEST, greenSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, greenLabel, 0, SpringLayout.NORTH, greenSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, blueLabel, 0, SpringLayout.WEST, blueSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, blueLabel, 0, SpringLayout.NORTH, blueSpinner);

        // separator
        JSeparator separator = new JSeparator(BoxLayout.X_AXIS);
        rgbPanel.add(separator);
        rgbLayout.putConstraint(SpringLayout.NORTH, separator, 6, SpringLayout.SOUTH, blueLabel);
        rgbLayout.putConstraint(SpringLayout.EAST, separator, 0, SpringLayout.EAST, rgbPanel);
        rgbLayout.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, rgbPanel);

        // hsv
        JLabel hueLabel   = new JLabel("H: ");
        hueLabel.setFont(uiFont);
        rgbPanel.add(hueLabel);
        JSpinner hueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 360, 1));
        rgbPanel.add(hueSpinner);
        JLabel satLabel = new JLabel("S: ");
        satLabel.setFont(uiFont);
        rgbPanel.add(satLabel);
        JSpinner satSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        rgbPanel.add(satSpinner);
        JLabel valLabel  = new JLabel("V: ");
        valLabel.setFont(uiFont);
        rgbPanel.add(valLabel);
        JSpinner valSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        rgbPanel.add(valSpinner);

        rgbLayout.putConstraint(SpringLayout.EAST, hueSpinner, 0, SpringLayout.EAST, separator);
        rgbLayout.putConstraint(SpringLayout.NORTH, hueSpinner, 6, SpringLayout.SOUTH, separator);
        rgbLayout.putConstraint(SpringLayout.EAST, satSpinner, 0, SpringLayout.EAST, hueSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, satSpinner, 0, SpringLayout.SOUTH, hueSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, valSpinner, 0, SpringLayout.EAST, hueSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, valSpinner, 0, SpringLayout.SOUTH, satSpinner);

        rgbLayout.putConstraint(SpringLayout.EAST, hueLabel, 0, SpringLayout.WEST, hueSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, hueLabel, 0, SpringLayout.NORTH, hueSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, satLabel, 0, SpringLayout.WEST, satSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, satLabel, 0, SpringLayout.NORTH, satSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, valLabel, 0, SpringLayout.WEST, valSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, valLabel, 0, SpringLayout.NORTH, valSpinner);

        // separator
        JSeparator separator2 = new JSeparator(BoxLayout.X_AXIS);
        rgbPanel.add(separator2);
        rgbLayout.putConstraint(SpringLayout.NORTH, separator2, 6, SpringLayout.SOUTH, valLabel);
        rgbLayout.putConstraint(SpringLayout.EAST, separator2, 0, SpringLayout.EAST, rgbPanel);
        rgbLayout.putConstraint(SpringLayout.WEST, separator2, 0, SpringLayout.WEST, rgbPanel);

        // hex
        JTextField hexTextField = new JTextField("#000000");
        hexTextField.setFont(uiFont);
        hexTextField.setPreferredSize(new Dimension(83, 24));
        rgbPanel.add(hexTextField);
        rgbLayout.putConstraint(SpringLayout.NORTH, hexTextField, 6, SpringLayout.SOUTH, separator2);
        rgbLayout.putConstraint(SpringLayout.EAST, hexTextField, 0, SpringLayout.EAST, rgbPanel);


        colorValues.add(rgbPanel);
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        colorValues.setBackground(Color.yellow);
        colorPanel.add(colorValues, c);

        addTab("Color", colorPanel);
    }

    private void addPalettePanel() {
        JPanel palettePanel = new JPanel();

        addTab("Palette", palettePanel);
    }
}