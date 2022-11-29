package tool;

import frame.ToolSettings;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public enum ToolProperty {
    WIDTH,
    OPACITY;

    public void attachToPanelAndTool(JPanel panel, BaseTool tool) {
        switch (this) {
            case WIDTH -> attachWidth(panel, tool);
            case OPACITY -> attachOpacity(panel, tool);
        }
    }

    private void attachWidth(JPanel panel, BaseTool tool) {
        JPanel widthComponent = new JPanel();
        SpringLayout springLayout = new SpringLayout();
        widthComponent.setLayout(springLayout);

        JLabel widthLabel = new JLabel("Brush Size");
        JSlider widthSlider = new JSlider(1, 100, 1);
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        widthComponent.add(widthLabel);
        widthComponent.add(widthSlider);
        widthComponent.add(widthSpinner);

        AtomicBoolean widthLocked = new AtomicBoolean(false);
        widthSlider.addChangeListener(e -> {
            if(widthLocked.get()) return;
            widthLocked.set(true);
            tool.width = widthSlider.getValue();
            widthSpinner.setValue(tool.width);
            widthLocked.set(false);

        });
        widthSpinner.addChangeListener(e -> {
            if(widthLocked.get()) return;
            widthLocked.set(true);
            tool.width = (int) widthSpinner.getValue();
            widthSlider.setValue(tool.width);
            widthLocked.set(false);
        });

        springLayout.putConstraint(SpringLayout.NORTH, widthLabel, 2, SpringLayout.NORTH, widthComponent);
        springLayout.putConstraint(SpringLayout.WEST, widthLabel, 2, SpringLayout.WEST, widthComponent);
        springLayout.putConstraint(SpringLayout.EAST, widthSpinner, -4, SpringLayout.EAST, widthComponent);
        springLayout.putConstraint(SpringLayout.NORTH, widthSlider, 0, SpringLayout.SOUTH, widthLabel);
        springLayout.putConstraint(SpringLayout.WEST, widthSlider, 0, SpringLayout.WEST, widthLabel);
        springLayout.putConstraint(SpringLayout.EAST, widthSlider, 0, SpringLayout.WEST, widthSpinner);
        springLayout.putConstraint(SpringLayout.SOUTH, widthSpinner, 0, SpringLayout.SOUTH, widthSlider);

        int height = springLayout.getConstraint(SpringLayout.SOUTH, widthSpinner).getValue();

        widthComponent.setMinimumSize(new Dimension(panel.getWidth(), height));
        widthComponent.setPreferredSize(new Dimension(panel.getWidth(), height));
        widthComponent.setMaximumSize(new Dimension(panel.getWidth(), height));
        widthComponent.setBounds(0, 0, panel.getWidth(), height);

        ToolSettings.addComponentToToolSettings(widthComponent);
    }

    private void attachOpacity(JPanel panel, BaseTool tool) {
        JPanel opacityComponent = new JPanel();
        SpringLayout springLayout = new SpringLayout();
        opacityComponent.setLayout(springLayout);

        JLabel opacityLabel = new JLabel("Opacity");
        JSlider opacitySlider = new JSlider(0, 255, 255);
        JSpinner opacitySpinner = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));
        opacityComponent.add(opacityLabel);
        opacityComponent.add(opacitySlider);
        opacityComponent.add(opacitySpinner);

        AtomicBoolean opacityLocked = new AtomicBoolean(false);
        opacitySlider.addChangeListener(e -> {
            if(opacityLocked.get()) return;
            opacityLocked.set(true);
            tool.opacity = opacitySlider.getValue();
            opacitySpinner.setValue(tool.opacity);
            opacityLocked.set(false);

        });
        opacitySpinner.addChangeListener(e -> {
            if(opacityLocked.get()) return;
            opacityLocked.set(true);
            tool.opacity = (int) opacitySpinner.getValue();
            opacitySlider.setValue(tool.opacity);
            opacityLocked.set(false);
        });

        springLayout.putConstraint(SpringLayout.NORTH, opacityLabel, 2, SpringLayout.NORTH, opacityComponent);
        springLayout.putConstraint(SpringLayout.WEST, opacityLabel, 2, SpringLayout.WEST, opacityComponent);
        springLayout.putConstraint(SpringLayout.EAST, opacitySpinner, -4, SpringLayout.EAST, opacityComponent);
        springLayout.putConstraint(SpringLayout.NORTH, opacitySlider, 0, SpringLayout.SOUTH, opacityLabel);
        springLayout.putConstraint(SpringLayout.WEST, opacitySlider, 0, SpringLayout.WEST, opacityLabel);
        springLayout.putConstraint(SpringLayout.EAST, opacitySlider, 0, SpringLayout.WEST, opacitySpinner);
        springLayout.putConstraint(SpringLayout.SOUTH, opacitySpinner, 0, SpringLayout.SOUTH, opacitySlider);

        int height = springLayout.getConstraint(SpringLayout.SOUTH, opacitySpinner).getValue();

        opacityComponent.setMinimumSize(new Dimension(panel.getWidth(), height));
        opacityComponent.setPreferredSize(new Dimension(panel.getWidth(), height));
        opacityComponent.setMaximumSize(new Dimension(panel.getWidth(), height));
        opacityComponent.setBounds(0, 0, panel.getWidth(), height);

        ToolSettings.addComponentToToolSettings(opacityComponent);
    }
}