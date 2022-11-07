package frame;

import javax.swing.*;

public class GridPanel extends JPanel {

    private static JComboBox<GridStyle> gridStyleBox;
    private static JSpinner gridXSpinner;
    private static JSpinner gridYSpinner;

    public GridPanel() {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);


        JLabel gridStyleLabel = new JLabel("Grid Style:");

        gridStyleBox = new JComboBox<>(GridStyle.values());
        gridStyleBox.addActionListener(
                e -> Controller.getActiveCanvas().setGridStyle((GridStyle) gridStyleBox.getSelectedItem())
        );
        add(gridStyleBox);
        add(gridStyleLabel);

        springLayout.putConstraint(SpringLayout.NORTH, gridStyleLabel, 7, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, gridStyleLabel, 7, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.BASELINE, gridStyleBox, 0, SpringLayout.BASELINE, gridStyleLabel);
        springLayout.putConstraint(SpringLayout.WEST, gridStyleBox, 5, SpringLayout.EAST, gridStyleLabel);

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

        springLayout.putConstraint(SpringLayout.NORTH, gridXLabel, 10, SpringLayout.SOUTH, gridStyleLabel);
        springLayout.putConstraint(SpringLayout.WEST, gridXLabel, 0, SpringLayout.WEST, gridStyleLabel);
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

        setAllEnabled(false);
    }

    private static void setAllEnabled(Boolean b) {
        gridStyleBox.setEnabled(b);
        gridXSpinner.setEnabled(b);
        gridYSpinner.setEnabled(b);
    }

    public static void onCanvasAdded() {
        setAllEnabled(true);

        GridInformation gridInformation = Controller.getActiveCanvas().getGridInformation();
        gridStyleBox.getModel().setSelectedItem(gridInformation.gridStyle());
        gridXSpinner.setValue(gridInformation.gridX());
        gridYSpinner.setValue(gridInformation.gridY());
    }
}
