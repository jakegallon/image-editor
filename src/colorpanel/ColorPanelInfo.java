package colorpanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ColorPanelInfo extends JPanel {

    private final ColorPanel colorPanel;

    private final JSpinner redSpinner;
    private final JSpinner grnSpinner;
    private final JSpinner bluSpinner;
    private final JSpinner hueSpinner;
    private final JSpinner satSpinner;
    private final JSpinner valSpinner;
    private final JTextField hexTextField;

    private boolean locked = false;

    private static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    public ColorPanelInfo(ColorPanel colorPanel) {
        this.colorPanel = colorPanel;

        SpringLayout rgbLayout = new SpringLayout();
        setLayout(rgbLayout);

        setPreferredSize(new Dimension(90, 0));

        JLabel redLabel   = new JLabel("R: ");
        redLabel.setFont(UI_FONT);
        add(redLabel);
        redSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        redSpinner.addChangeListener(e -> {
            if(!locked) {
                colorPanel.setRed((Integer) redSpinner.getValue());
            }
        });
        add(redSpinner);

        JLabel greenLabel = new JLabel("G: ");
        greenLabel.setFont(UI_FONT);
        add(greenLabel);
        grnSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        grnSpinner.addChangeListener(e -> {
            if(!locked) {
                colorPanel.setGrn((Integer) grnSpinner.getValue());
            }
        });
        add(grnSpinner);

        JLabel blueLabel  = new JLabel("B: ");
        blueLabel.setFont(UI_FONT);
        add(blueLabel);
        bluSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        bluSpinner.addChangeListener(e -> {
            if(!locked) {
                colorPanel.setBlu((Integer) bluSpinner.getValue());
            }
        });
        add(bluSpinner);

        JSeparator RGBSeparator = new JSeparator(BoxLayout.X_AXIS);
        add(RGBSeparator);

        JLabel hueLabel   = new JLabel("H: ");
        hueLabel.setFont(UI_FONT);
        add(hueLabel);
        hueSpinner = new JSpinner(new SpinnerNumberModel(0, -1, 360, 1));
        hueSpinner.addChangeListener(e -> {
            if((Integer) hueSpinner.getValue() == -1) {
                hueSpinner.setValue(359);
                return;
            }
            if((Integer) hueSpinner.getValue() == 360) {
                hueSpinner.setValue(0);
                return;
            }
            if(!locked) {
                colorPanel.setHue((Integer) hueSpinner.getValue());
            }
        });
        add(hueSpinner);

        Float min = 0f;
        Float max = 100f;
        Float step = 0.1f;

        JLabel satLabel = new JLabel("S: ");
        satLabel.setFont(UI_FONT);
        add(satLabel);
        satSpinner = new JSpinner(new SpinnerNumberModel(min, min, max, step));
        satSpinner.addChangeListener(e -> {
            if(!locked) {
                Float sat = (Float) satSpinner.getValue();
                colorPanel.setSat(sat/100f);
            }
        });
        add(satSpinner);

        JLabel valLabel  = new JLabel("V: ");
        valLabel.setFont(UI_FONT);
        add(valLabel);
        valSpinner = new JSpinner(new SpinnerNumberModel(max, min, max, step));
        valSpinner.addChangeListener(e -> {
            if(!locked) {
                Float val = (Float) valSpinner.getValue();
                colorPanel.setVal(val/100f);
            }
        });
        add(valSpinner);

        JSeparator HSVSeparator = new JSeparator(BoxLayout.X_AXIS);
        add(HSVSeparator);

        hexTextField = new JTextField("#000000");
        hexTextField.setFont(UI_FONT);
        hexTextField.setPreferredSize(new Dimension(83, 24));
        hexTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!locked) {
                    SwingUtilities.invokeLater(update());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!locked) {
                    SwingUtilities.invokeLater(update());
                }
            }

            private Runnable update() {
                return () -> {
                    String hex = hexTextField.getText();
                    if(hex.matches("((0x)|#)[A-Fa-f\\d]{6}")){
                        colorPanel.setHex(hex);
                    } else if (hex.matches("[A-Fa-f\\d]{6}")){
                        colorPanel.setHex("#"+hex);
                    }
                };
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        add(hexTextField);

        rgbLayout.putConstraint(SpringLayout.EAST, redSpinner, 0, SpringLayout.EAST, this);
        rgbLayout.putConstraint(SpringLayout.NORTH, redSpinner, 6, SpringLayout.NORTH, this);
        rgbLayout.putConstraint(SpringLayout.EAST, grnSpinner, 0, SpringLayout.EAST, redSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, grnSpinner, 0, SpringLayout.SOUTH, redSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, bluSpinner, 0, SpringLayout.EAST, redSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, bluSpinner, 0, SpringLayout.SOUTH, grnSpinner);

        rgbLayout.putConstraint(SpringLayout.EAST, redLabel, 0, SpringLayout.WEST, redSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, redLabel, 0, SpringLayout.NORTH, redSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, greenLabel, 0, SpringLayout.WEST, grnSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, greenLabel, 0, SpringLayout.NORTH, grnSpinner);
        rgbLayout.putConstraint(SpringLayout.EAST, blueLabel, 0, SpringLayout.WEST, bluSpinner);
        rgbLayout.putConstraint(SpringLayout.NORTH, blueLabel, 0, SpringLayout.NORTH, bluSpinner);

        rgbLayout.putConstraint(SpringLayout.NORTH, RGBSeparator, 6, SpringLayout.SOUTH, blueLabel);
        rgbLayout.putConstraint(SpringLayout.EAST, RGBSeparator, 0, SpringLayout.EAST, this);
        rgbLayout.putConstraint(SpringLayout.WEST, RGBSeparator, 0, SpringLayout.WEST, this);

        rgbLayout.putConstraint(SpringLayout.EAST, hueSpinner, 0, SpringLayout.EAST, RGBSeparator);
        rgbLayout.putConstraint(SpringLayout.NORTH, hueSpinner, 6, SpringLayout.SOUTH, RGBSeparator);
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

        rgbLayout.putConstraint(SpringLayout.NORTH, HSVSeparator, 6, SpringLayout.SOUTH, valLabel);
        rgbLayout.putConstraint(SpringLayout.EAST, HSVSeparator, 0, SpringLayout.EAST, this);
        rgbLayout.putConstraint(SpringLayout.WEST, HSVSeparator, 0, SpringLayout.WEST, this);

        rgbLayout.putConstraint(SpringLayout.NORTH, hexTextField, 6, SpringLayout.SOUTH, HSVSeparator);
        rgbLayout.putConstraint(SpringLayout.EAST, hexTextField, 0, SpringLayout.EAST, this);
    }

    protected void updateNotRGB() {
        locked = true;
        hueSpinner.setValue(colorPanel.getHue());
        satSpinner.setValue(colorPanel.getSat());
        valSpinner.setValue(colorPanel.getVal());
        hexTextField.setText(colorPanel.getHex());
        locked = false;
    }

    protected void updateNotHSV() {
        locked = true;
        redSpinner.setValue(colorPanel.getRed());
        grnSpinner.setValue(colorPanel.getGrn());
        bluSpinner.setValue(colorPanel.getBlu());
        hexTextField.setText(colorPanel.getHex());
        locked = false;
    }

    protected void updateNotHex() {
        locked = true;
        hueSpinner.setValue(colorPanel.getHue());
        satSpinner.setValue(colorPanel.getSat());
        valSpinner.setValue(colorPanel.getVal());
        redSpinner.setValue(colorPanel.getRed());
        grnSpinner.setValue(colorPanel.getGrn());
        bluSpinner.setValue(colorPanel.getBlu());
        locked = false;
    }

    public void setH() {
        locked = true;
        hueSpinner.setValue(colorPanel.getHue());
        locked = false;
    }

    public void setSV() {
        locked = true;
        satSpinner.setValue(colorPanel.getSat());
        valSpinner.setValue(colorPanel.getVal());
        locked = false;
    }
}
