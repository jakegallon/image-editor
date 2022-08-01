package frame;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class ColorPanel extends JTabbedPane {

    JSpinner redSpinner, greenSpinner, blueSpinner, hueSpinner, satSpinner, valSpinner;
    JTextField hexTextField;
    Boolean isAutomaticChange = false;
    Boolean isColorChangeLocked = false;

    private final SwingPropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this);
    private static final String COLOR_UPDATED = "color updated";

    public ColorPanel() {
        addColorPanel();
        addPalettePanel();
    }

    private void addColorPanel() {
        JPanel colorPanel = new JPanel(new GridBagLayout());
        ColorSelector colorSelector = new ColorSelector(this);
        GridBagConstraints c = new GridBagConstraints();

        propertyChangeSupport.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals(COLOR_UPDATED)){
                colorSelector.setColor((Integer) evt.getNewValue());
            }
        });

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
        redSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        redSpinner.addChangeListener(e -> {
            if(!isColorChangeLocked){
                isColorChangeLocked = true;
                updateColorFromRGB();
            }
        });
        rgbPanel.add(redSpinner);
        JLabel greenLabel = new JLabel("G: ");
        greenLabel.setFont(uiFont);
        rgbPanel.add(greenLabel);
        greenSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        greenSpinner.addChangeListener(e -> {
            if (!isColorChangeLocked) {
                isColorChangeLocked = true;
                updateColorFromRGB();
            }
        });
        rgbPanel.add(greenSpinner);
        JLabel blueLabel  = new JLabel("B: ");
        blueLabel.setFont(uiFont);
        rgbPanel.add(blueLabel);
        blueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        blueSpinner.addChangeListener(e -> {
            if (!isColorChangeLocked) {
                isColorChangeLocked = true;
                updateColorFromRGB();
            }
        });
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
        hueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 360, 1));
        hueSpinner.addChangeListener(e -> {
            colorSelector.updateHue((Integer) hueSpinner.getValue());
            if (!isColorChangeLocked) {
                isColorChangeLocked = true;
                updateColorFromHSV();
            }
        });
        rgbPanel.add(hueSpinner);
        JLabel satLabel = new JLabel("S: ");
        satLabel.setFont(uiFont);
        rgbPanel.add(satLabel);
        satSpinner = new JSpinner(new SpinnerNumberModel(0f, 0f, 100f, 0.1f));
        satSpinner.addChangeListener(e -> {
            if (!isColorChangeLocked) {
                isColorChangeLocked = true;
                updateColorFromHSV();
            }
        });
        rgbPanel.add(satSpinner);
        JLabel valLabel  = new JLabel("V: ");
        valLabel.setFont(uiFont);
        rgbPanel.add(valLabel);
        valSpinner = new JSpinner(new SpinnerNumberModel(0f, 0f, 100f, 0.1f));
        valSpinner.addChangeListener(e -> {
            if (!isColorChangeLocked) {
                isColorChangeLocked = true;
                updateColorFromHSV();
            }
        });
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
        hexTextField = new JTextField("#000000");
        hexTextField.setFont(uiFont);
        hexTextField.setPreferredSize(new Dimension(83, 24));
        rgbPanel.add(hexTextField);
        rgbLayout.putConstraint(SpringLayout.NORTH, hexTextField, 6, SpringLayout.SOUTH, separator2);
        rgbLayout.putConstraint(SpringLayout.EAST, hexTextField, 0, SpringLayout.EAST, rgbPanel);

        hexTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isColorChangeLocked) {
                    isColorChangeLocked = true;
                    updateColorFromHex();
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isColorChangeLocked) {
                    isColorChangeLocked = true;
                    updateColorFromHex();
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!isColorChangeLocked) {
                    isColorChangeLocked = true;
                    updateColorFromHex();
                }
            }
        });

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

    protected void setSelectedColor(int color) {
        isAutomaticChange = true;

        int blue  =  color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red   = (color & 0xff0000) >> 16;

        redSpinner.setValue(red);
        greenSpinner.setValue(green);
        blueSpinner.setValue(blue);

        float[] hsv = new float[3];
        Color.RGBtoHSB(red, green, blue, hsv);

        int hue = Math.round(hsv[0]*360);
        int sat = (int) (hsv[1]*1000);
        int val = (int) (hsv[2]*1000);

        hueSpinner.setValue(hue);
        satSpinner.setValue(sat / 10f);
        valSpinner.setValue(val / 10f);

        hexTextField.setText("#" + Integer.toHexString(color - 0xFF000000));

        isAutomaticChange = false;
    }

    private void addPalettePanel() {
        JPanel palettePanel = new JPanel();

        addTab("Palette", palettePanel);
    }

    private void updateColorFromRGB() {
        int red = (int) redSpinner.getValue();
        int green = (int) greenSpinner.getValue();
        int blue = (int) blueSpinner.getValue();
        float[] hsv = new float[3];
        Color.RGBtoHSB(red, green, blue, hsv);

        int newColor = Color.getHSBColor(hsv[0], hsv[1], hsv[2]).getRGB();
        setUpdatedColor(newColor);

        int hue = Math.round(hsv[0]*360);
        int sat = (int) (hsv[1]*1000);
        int val = (int) (hsv[2]*1000);
        hueSpinner.setValue(hue);
        satSpinner.setValue(sat / 10f);
        valSpinner.setValue(val / 10f);

        hexTextField.setText("#" + Integer.toHexString(newColor - 0xFF000000));

        isColorChangeLocked = false;
    }

    private void updateColorFromHSV() {
        float hue = (int) hueSpinner.getValue() / 360f;
        float sat = Float.parseFloat(satSpinner.getValue().toString()) / 100f;
        float val = Float.parseFloat(valSpinner.getValue().toString()) / 100f;
        int newColor = Color.getHSBColor(hue, sat, val).getRGB();
        setUpdatedColor(newColor);

        int blue  =  newColor & 0xff;
        int green = (newColor & 0xff00) >> 8;
        int red   = (newColor & 0xff0000) >> 16;
        redSpinner.setValue(red);
        greenSpinner.setValue(green);
        blueSpinner.setValue(blue);

        hexTextField.setText("#" + Integer.toHexString(newColor - 0xFF000000));

        isColorChangeLocked = false;
    }

    private void updateColorFromHex() {
        String hex = hexTextField.getText();
        if(hex.startsWith("#")){
            hex = hex.substring(1);
        }
        if(hex.length() == 6){
            hex = "0x" + hex;
            int hexInt = Integer.decode(hex);
            hexInt += 0xFF000000;
            setUpdatedColor(hexInt);

            int blue  =  hexInt & 0xff;
            int green = (hexInt & 0xff00) >> 8;
            int red   = (hexInt & 0xff0000) >> 16;
            redSpinner.setValue(red);
            greenSpinner.setValue(green);
            blueSpinner.setValue(blue);

            float[] hsv = new float[3];
            Color.RGBtoHSB(red, green, blue, hsv);
            int hue = Math.round(hsv[0]*360);
            int sat = (int) (hsv[1]*1000);
            int val = (int) (hsv[2]*1000);
            hueSpinner.setValue(hue);
            satSpinner.setValue(sat / 10f);
            valSpinner.setValue(val / 10f);
        }
        isColorChangeLocked = false;
    }

    private void setUpdatedColor(int newColor) {
        propertyChangeSupport.firePropertyChange(COLOR_UPDATED, 0, newColor);
    }
}