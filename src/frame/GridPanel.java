package frame;

import javax.swing.*;

public class GridPanel extends JPanel {

    private static SpringLayout springLayout;

    private static JComboBox<GridStyle> gridStyleBox;
    private static JSpinner gridXSpinner;
    private static JSpinner gridYSpinner;

    private static JCheckBox cellGhostCheckbox;
    private static JCheckBox cellGhostWrapCheckbox;
    private static JSlider cellGhostOpacitySlider;
    private static JLabel cellGhostWrapLabel;
    private static JLabel cellGhostOpacityLabel;

    public GridPanel() {
        springLayout = new SpringLayout();
        setLayout(springLayout);

        addGridStyleWidget();
        addGridSizeWidget();
        addSeparator();
        addCellGhostWidget();

        setAllEnabled(false);
    }

    private void addGridStyleWidget() {
        JLabel gridStyleLabel = new JLabel("Grid Style:");

        gridStyleBox = new JComboBox<>(GridStyle.values());
        gridStyleBox.addActionListener(
                e -> Controller.getActiveCanvas().setGridStyle((GridStyle) gridStyleBox.getSelectedItem())
        );
        add(gridStyleBox);
        add(gridStyleLabel);

        springLayout.putConstraint(SpringLayout.NORTH, gridStyleLabel, 10, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, gridStyleLabel, 7, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.BASELINE, gridStyleBox, 0, SpringLayout.BASELINE, gridStyleLabel);
        springLayout.putConstraint(SpringLayout.WEST, gridStyleBox, 5, SpringLayout.EAST, gridStyleLabel);
    }

    private void addGridSizeWidget() {
        JLabel gridXLabel = new JLabel("Cell X:");

        gridXSpinner = new JSpinner(new SpinnerNumberModel(32, 0, 5001, 1));
        gridXSpinner.addChangeListener(
                e -> {
                    int value = (int) gridXSpinner.getValue();
                    if (value == 0) {
                        value = 5000;
                        gridXSpinner.setValue(value);
                    } else if (value == 5001) {
                        value = 1;
                        gridXSpinner.setValue(value);
                    }
                    Controller.getActiveCanvas().setGridX(value);
                }
        );
        add(gridXSpinner);
        add(gridXLabel);

        springLayout.putConstraint(SpringLayout.NORTH, gridXLabel, 10, SpringLayout.SOUTH, gridStyleBox);
        springLayout.putConstraint(SpringLayout.WEST, gridXLabel, 7, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.BASELINE, gridXSpinner, 0, SpringLayout.BASELINE, gridXLabel);
        springLayout.putConstraint(SpringLayout.WEST, gridXSpinner, 5, SpringLayout.EAST, gridXLabel);
        springLayout.putConstraint(SpringLayout.EAST, gridXSpinner, 75, SpringLayout.WEST, gridXSpinner);

        JLabel gridYLabel = new JLabel("Cell Y:");

        gridYSpinner = new JSpinner(new SpinnerNumberModel(32, 0, 5001, 1));
        gridYSpinner.addChangeListener(
                e -> {
                    int value = (int) gridYSpinner.getValue();
                    if (value == 0) {
                        value = 5000;
                        gridYSpinner.setValue(value);
                    } else if (value == 5001) {
                        value = 1;
                        gridYSpinner.setValue(value);
                    }
                    Controller.getActiveCanvas().setGridY(value);
                }
        );
        add(gridYSpinner);
        add(gridYLabel);

        springLayout.putConstraint(SpringLayout.NORTH, gridYLabel, 0, SpringLayout.NORTH, gridXLabel);
        springLayout.putConstraint(SpringLayout.WEST, gridYLabel, 7, SpringLayout.EAST, gridXSpinner);
        springLayout.putConstraint(SpringLayout.BASELINE, gridYSpinner, 0, SpringLayout.BASELINE, gridXLabel);
        springLayout.putConstraint(SpringLayout.WEST, gridYSpinner, 5, SpringLayout.EAST, gridYLabel);
        springLayout.putConstraint(SpringLayout.EAST, gridYSpinner, 75, SpringLayout.WEST, gridYSpinner);
        springLayout.putConstraint(SpringLayout.EAST, gridStyleBox, 0, SpringLayout.EAST, gridYSpinner);
    }

    private void addSeparator() {
        JSeparator separator = new JSeparator();
        add(separator);

        springLayout.putConstraint(SpringLayout.NORTH, separator, 7, SpringLayout.SOUTH, gridXSpinner);
        springLayout.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, separator, 0, SpringLayout.EAST, this);
    }

    private void addCellGhostWidget() {
        JLabel cellGhostLabel = new JLabel("Show last cell:");
        cellGhostCheckbox = new JCheckBox();

        add(cellGhostLabel);
        add(cellGhostCheckbox);

        springLayout.putConstraint(SpringLayout.NORTH, cellGhostLabel, 14, SpringLayout.SOUTH, gridXSpinner);
        springLayout.putConstraint(SpringLayout.WEST, cellGhostLabel, 7, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, cellGhostCheckbox, 0, SpringLayout.NORTH, cellGhostLabel);
        springLayout.putConstraint(SpringLayout.WEST, cellGhostCheckbox, 3, SpringLayout.EAST, cellGhostLabel);

        cellGhostCheckbox.addActionListener(e -> {
            Controller.getActiveCanvas().isDrawingGhost = cellGhostCheckbox.isSelected();
            if(cellGhostCheckbox.isSelected()) {
                onCellGhostEnable();
            } else {
                onCellGhostDisable();
            }
        });

        cellGhostWrapLabel = new JLabel("Wrap:");
        cellGhostWrapCheckbox = new JCheckBox();

        add(cellGhostWrapLabel);
        add(cellGhostWrapCheckbox);

        springLayout.putConstraint(SpringLayout.NORTH, cellGhostWrapLabel, 2, SpringLayout.SOUTH, cellGhostLabel);
        springLayout.putConstraint(SpringLayout.EAST, cellGhostWrapLabel, 0, SpringLayout.EAST, cellGhostLabel);
        springLayout.putConstraint(SpringLayout.NORTH, cellGhostWrapCheckbox, 0, SpringLayout.NORTH, cellGhostWrapLabel);
        springLayout.putConstraint(SpringLayout.WEST, cellGhostWrapCheckbox, 0, SpringLayout.WEST, cellGhostCheckbox);

        cellGhostWrapCheckbox.addActionListener(e ->
                Controller.getActiveCanvas().isWrappingGhost = cellGhostWrapCheckbox.isSelected());

        cellGhostOpacityLabel = new JLabel("Opacity:");
        cellGhostOpacitySlider = new JSlider(0, 255, 20);


        add(cellGhostOpacityLabel);
        add(cellGhostOpacitySlider);

        springLayout.putConstraint(SpringLayout.NORTH, cellGhostOpacityLabel, 2, SpringLayout.SOUTH, cellGhostWrapLabel);
        springLayout.putConstraint(SpringLayout.EAST, cellGhostOpacityLabel, 0, SpringLayout.EAST, cellGhostLabel);
        springLayout.putConstraint(SpringLayout.NORTH, cellGhostOpacitySlider, 2, SpringLayout.NORTH, cellGhostOpacityLabel);
        springLayout.putConstraint(SpringLayout.WEST, cellGhostOpacitySlider, -4, SpringLayout.WEST, cellGhostCheckbox);
        springLayout.putConstraint(SpringLayout.EAST, cellGhostOpacitySlider, 0, SpringLayout.EAST, gridYSpinner);

        cellGhostOpacitySlider.addChangeListener(e -> Controller.getActiveCanvas().ghostOpacity = cellGhostOpacitySlider.getValue());

        onCellGhostDisable();
    }

    private static void onCellGhostEnable() {
        cellGhostWrapLabel.setEnabled(true);
        cellGhostWrapCheckbox.setEnabled(true);
        cellGhostOpacityLabel.setEnabled(true);
        cellGhostOpacitySlider.setEnabled(true);
    }

    private static void onCellGhostDisable() {
        cellGhostWrapLabel.setEnabled(false);
        cellGhostWrapCheckbox.setEnabled(false);
        cellGhostOpacityLabel.setEnabled(false);
        cellGhostOpacitySlider.setEnabled(false);
    }

    private static void setAllEnabled(Boolean b) {
        gridStyleBox.setEnabled(b);
        gridXSpinner.setEnabled(b);
        gridYSpinner.setEnabled(b);

        cellGhostCheckbox.setEnabled(b);
        onCellGhostDisable();
    }

    public static void onCanvasAdded() {
        setAllEnabled(true);

        Canvas activeCanvas = Controller.getActiveCanvas();

        GridInformation gridInformation = activeCanvas.getGridInformation();
        gridStyleBox.getModel().setSelectedItem(gridInformation.gridStyle());
        gridXSpinner.setValue(gridInformation.gridX());
        gridYSpinner.setValue(gridInformation.gridY());

        cellGhostCheckbox.setSelected(activeCanvas.isDrawingGhost);
        cellGhostWrapCheckbox.setSelected(activeCanvas.isWrappingGhost);
        cellGhostOpacitySlider.setValue(activeCanvas.ghostOpacity);

        cellGhostWrapLabel.setEnabled(cellGhostCheckbox.isSelected());
        cellGhostWrapCheckbox.setEnabled(cellGhostCheckbox.isSelected());
        cellGhostOpacityLabel.setEnabled(cellGhostCheckbox.isSelected());
        cellGhostOpacitySlider.setEnabled(cellGhostCheckbox.isSelected());
    }
}
