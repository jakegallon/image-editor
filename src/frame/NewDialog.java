package frame;

import javax.swing.*;
import java.awt.*;

public class NewDialog extends JDialog {

    SpringLayout springLayout = new SpringLayout();

    JTextField fileNameField = new JTextField();
    JSpinner width = new JSpinner(new SpinnerNumberModel(320, 1, 16384, 1));
    JSpinner height = new JSpinner(new SpinnerNumberModel(320, 1, 16384, 1));
    JCheckBox hasPaper = new JCheckBox();

    JButton confirm = new JButton("Confirm");

    private static int untitledCount = 0;

    public NewDialog(Frame owner) {
        super(owner);
        init();

        addFileNameComponents();
        addDimensionComponents();
        addPaperComponents();
        addConfirmButton();

        confirm.addActionListener(e -> {
            Canvas canvas = new Canvas();

            String fileName = fileNameField.getText();

            if(fileName.equals("")) {
                if(untitledCount == 0) {
                    fileName = "untitled";
                } else {
                    fileName = "untitled (" + untitledCount + ")";
                }
                untitledCount ++;
            }

            int canvasWidth = (int) width.getValue();
            int canvasHeight = (int) height.getValue();

            canvas.setFileName(fileName);
            canvas.setBounds(0, 0, canvasWidth, canvasHeight);

            if(hasPaper.isSelected()) {
                Layer paper = new Layer(canvas);
                paper.setName("paper");
                paper.toggleLayerLock();
                paper.fillAll(Color.white);
                canvas.addLayer(paper);
            }

            Layer newLayer = new Layer(canvas);
            newLayer.setName("Layer " + canvas.nextNameNumber());
            canvas.addLayer(newLayer);

            Controller.addNewCanvas(canvas);
            dispose();
        });
    }

    private void addFileNameComponents() {
        JLabel fileNameLabel = new JLabel("File name:");
        add(fileNameLabel);
        springLayout.putConstraint(SpringLayout.WEST, fileNameLabel, 5, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, fileNameLabel, 5, SpringLayout.NORTH, this);

        add(fileNameField);
        springLayout.putConstraint(SpringLayout.WEST, fileNameField, 5, SpringLayout.EAST, fileNameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, fileNameField, 0, SpringLayout.NORTH, fileNameLabel);
        springLayout.putConstraint(SpringLayout.EAST, fileNameField, 200, SpringLayout.WEST, fileNameField);
    }

    private void addDimensionComponents() {
        add(width);
        springLayout.putConstraint(SpringLayout.WEST, width, 0, SpringLayout.WEST, fileNameField);
        springLayout.putConstraint(SpringLayout.EAST, width, 100, SpringLayout.WEST, width);
        springLayout.putConstraint(SpringLayout.NORTH, width, 10, SpringLayout.SOUTH, fileNameField);

        add(height);
        springLayout.putConstraint(SpringLayout.WEST, height, 0, SpringLayout.WEST, width);
        springLayout.putConstraint(SpringLayout.EAST, height, 100, SpringLayout.WEST, width);
        springLayout.putConstraint(SpringLayout.NORTH, height, 10, SpringLayout.SOUTH, width);

        JLabel widthLabel = new JLabel("Width:");
        add(widthLabel);
        springLayout.putConstraint(SpringLayout.EAST, widthLabel, -5, SpringLayout.WEST, width);
        springLayout.putConstraint(SpringLayout.NORTH, widthLabel, 0, SpringLayout.NORTH, width);

        JLabel heightLabel = new JLabel("Height:");
        add(heightLabel);
        springLayout.putConstraint(SpringLayout.EAST, heightLabel, -5, SpringLayout.WEST, height);
        springLayout.putConstraint(SpringLayout.NORTH, heightLabel, 0, SpringLayout.NORTH, height);
    }

    private void addPaperComponents() {
        add(hasPaper);
        springLayout.putConstraint(SpringLayout.WEST, hasPaper, -3, SpringLayout.WEST, height);
        springLayout.putConstraint(SpringLayout.NORTH, hasPaper, 10, SpringLayout.SOUTH, height);

        JLabel hasPaperLabel = new JLabel("Has paper:");
        add(hasPaperLabel);
        springLayout.putConstraint(SpringLayout.EAST, hasPaperLabel, -2, SpringLayout.WEST, hasPaper);
        springLayout.putConstraint(SpringLayout.NORTH, hasPaperLabel, 0, SpringLayout.NORTH, hasPaper);
    }

    private void addConfirmButton() {
        add(confirm);
        springLayout.putConstraint(SpringLayout.NORTH, confirm, 10, SpringLayout.SOUTH, hasPaper);
        springLayout.putConstraint(SpringLayout.WEST, confirm, 0, SpringLayout.WEST, fileNameField);
    }

    private void init() {
        setSize(new Dimension(350, 200));
        setLocationRelativeTo(this);
        setResizable(false);

        setTitle("New");

        setLayout(springLayout);
    }
}
