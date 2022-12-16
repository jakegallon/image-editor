package tool.properties;

import frame.ToolSettings;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PropertyBooleanWidget extends JPanel {

    public final JCheckBox propertyCheckBox = new JCheckBox();

    public PropertyBooleanWidget(AtomicBoolean value, String name) {

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        JLabel propertyName = new JLabel(name);
        propertyCheckBox.setSelected(value.get());
        propertyCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add(propertyName);
        add(propertyCheckBox);

        propertyCheckBox.addChangeListener(e -> value.set(propertyCheckBox.isSelected()));

        springLayout.putConstraint(SpringLayout.NORTH, propertyName, 2, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, propertyName, 2, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, propertyCheckBox, -4, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, propertyCheckBox, 30, SpringLayout.NORTH, this);

        int width = ToolSettings.getCurrentWidth();
        int height = springLayout.getConstraint(SpringLayout.SOUTH, propertyCheckBox).getValue();

        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBounds(0, 0, width, height);
    }
}