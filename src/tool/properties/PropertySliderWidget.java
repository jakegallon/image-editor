package tool.properties;

import frame.ToolSettings;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PropertySliderWidget extends JPanel {

    public PropertySliderWidget(AtomicInteger value, String name, int minValue, int maxValue) {

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        JLabel propertyName = new JLabel(name);
        JSlider propertySlider = new JSlider(minValue, maxValue, value.get());
        propertySlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JSpinner propertySpinner = new JSpinner(new SpinnerNumberModel(value.get(), minValue, maxValue, 1));
        add(propertyName);
        add(propertySlider);
        add(propertySpinner);

        AtomicBoolean propertyLocked = new AtomicBoolean(false);
        propertySlider.addChangeListener(e -> {
            if(propertyLocked.get()) return;
            propertyLocked.set(true);
            value.set(propertySlider.getValue());
            propertySpinner.setValue(value.get());
            propertyLocked.set(false);

        });
        propertySpinner.addChangeListener(e -> {
            if(propertyLocked.get()) return;
            propertyLocked.set(true);
            value.set((int) propertySpinner.getValue());
            propertySlider.setValue(value.get());
            propertyLocked.set(false);
        });

        springLayout.putConstraint(SpringLayout.NORTH, propertyName, 2, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, propertyName, 2, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, propertySpinner, -4, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.NORTH, propertySlider, 0, SpringLayout.SOUTH, propertyName);
        springLayout.putConstraint(SpringLayout.WEST, propertySlider, 0, SpringLayout.WEST, propertyName);
        springLayout.putConstraint(SpringLayout.EAST, propertySlider, 0, SpringLayout.WEST, propertySpinner);
        springLayout.putConstraint(SpringLayout.SOUTH, propertySpinner, 0, SpringLayout.SOUTH, propertySlider);

        int width = ToolSettings.getCurrentWidth();
        int height = springLayout.getConstraint(SpringLayout.SOUTH, propertySpinner).getValue();

        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBounds(0, 0, width, height);
    }
}
