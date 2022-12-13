package frame;

import action.LayerCreationAction;
import action.LayerDeletionAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class LayerPanelHandler extends JPanel {

    private static Canvas activeCanvas;

    private static JButton addButton;
    private static JButton delButton;

    public LayerPanelHandler() {
        setPreferredSize(new Dimension(0, 32));
        setBorder(new MatteBorder(1, 0, 0, 0, new Color(43, 43, 43)));

        BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout(layout);

        ImageIcon addIcon;
        ImageIcon subIcon;
        ImageIcon helpIcon;

        try {
            addIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/add.png"))));
            subIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/sub.png"))));
            helpIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/help.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addButton = new JButton();
        addButton.setIcon(addIcon);
        addButton.setBorderPainted(false);
        addButton.setBackground(new Color(0, 0, 0, 0));
        addButton.setPreferredSize(new Dimension(22, 22));
        addButton.addActionListener(e -> {
            int index = activeCanvas.getActiveLayerIndex();
            index = index == -1 ? 0 : index;

            Controller.addNewLayerToActiveCanvas(index);

            LayerCreationAction thisAction = new LayerCreationAction(index);
            activeCanvas.undoManager.addEdit(thisAction);
        });
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setEnabled(false);

        delButton = new JButton();
        delButton.setIcon(subIcon);
        delButton.setBorderPainted(false);
        delButton.setBackground(new Color(0, 0, 0, 0));
        delButton.setPreferredSize(new Dimension(22, 22));
        delButton.addActionListener(e -> {
            int index = activeCanvas.getActiveLayerIndex();
            Layer layer = activeCanvas.getActiveLayer();

            Controller.deleteLayerFromActiveCanvas(index);

            LayerDeletionAction thisAction = new LayerDeletionAction(layer, index);
            activeCanvas.undoManager.addEdit(thisAction);
        });
        delButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        delButton.setEnabled(false);

        JButton helpButton = new JButton();
        helpButton.setIcon(helpIcon);
        helpButton.setBorderPainted(false);
        helpButton.setBackground(new Color(0, 0, 0, 0));
        helpButton.setPreferredSize(new Dimension(22, 22));
        helpButton.addActionListener(e -> {
            HelpDialog helpDialog = new HelpDialog(helpPanel());
            helpDialog.setVisible(true);
            Point p = getLocationOnScreen();
            helpDialog.setLocation(new Point(p.x - helpDialog.getWidth() + getWidth(), p.y - helpDialog.getHeight()));
        });
        helpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add(addButton);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(delButton);
        add(Box.createHorizontalGlue());
        add(helpButton);
    }

    private JPanel helpPanel() {
        JPanel helpPanel = new JPanel();

        SpringLayout springLayout = new SpringLayout();
        helpPanel.setLayout(springLayout);
        helpPanel.setBorder(new LineBorder(Frame.borderColor));

        JLabel addLabel = new JLabel("<html>- Adds a layer to the current canvas.</html>");
        JLabel delLabel = new JLabel("<html>- Removes the currently active layer from the current canvas.</html>");
        JLabel visibilityLabel = new JLabel("<html>- Toggles the corresponding layer's visibility. If a layer is invisible, it can't be seen.</html>");
        JLabel renameLabel = new JLabel("<html>- Changes the name of the corresponding layer. Double clicking the Layer name also changes the name of the corresponding layer.</html>");
        JLabel mergeDownLabel = new JLabel("<html>- Merges the corresponding layer into the layer beneath.</html>");
        JLabel delLayerLabel = new JLabel("<html>- Removes the corresponding layer from the current canvas.</html>");
        JLabel lockLabel = new JLabel("<html>- Toggles the corresponding layer's lock. If a layer is locked, it can't be edited.</html>");
        JLabel opacityLabel = new JLabel("<html>- Adjusts the corresponding layer's opacity.</html>");

        helpPanel.add(addLabel);
        helpPanel.add(delLabel);
        helpPanel.add(visibilityLabel);
        helpPanel.add(renameLabel);
        helpPanel.add(mergeDownLabel);
        helpPanel.add(delLayerLabel);
        helpPanel.add(lockLabel);
        helpPanel.add(opacityLabel);

        springLayout.putConstraint(SpringLayout.WEST, addLabel, 30, SpringLayout.WEST, helpPanel);
        springLayout.putConstraint(SpringLayout.WEST, delLabel, 0, SpringLayout.WEST, addLabel);
        springLayout.putConstraint(SpringLayout.WEST, visibilityLabel, 0, SpringLayout.WEST, addLabel);
        springLayout.putConstraint(SpringLayout.WEST, renameLabel, 0, SpringLayout.WEST, addLabel);
        springLayout.putConstraint(SpringLayout.WEST, mergeDownLabel, 0, SpringLayout.WEST, addLabel);
        springLayout.putConstraint(SpringLayout.WEST, delLayerLabel, 0, SpringLayout.WEST, addLabel);
        springLayout.putConstraint(SpringLayout.WEST, lockLabel, 0, SpringLayout.WEST, addLabel);
        springLayout.putConstraint(SpringLayout.WEST, opacityLabel, 0, SpringLayout.WEST, addLabel);

        int verticalGap = 4;

        springLayout.putConstraint(SpringLayout.NORTH, addLabel, verticalGap, SpringLayout.NORTH, helpPanel);
        springLayout.putConstraint(SpringLayout.NORTH, delLabel, verticalGap, SpringLayout.SOUTH, addLabel);
        springLayout.putConstraint(SpringLayout.NORTH, visibilityLabel, verticalGap * 2, SpringLayout.SOUTH, delLabel);
        springLayout.putConstraint(SpringLayout.NORTH, renameLabel, verticalGap, SpringLayout.SOUTH, visibilityLabel);
        springLayout.putConstraint(SpringLayout.NORTH, mergeDownLabel, verticalGap, SpringLayout.SOUTH, renameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, delLayerLabel, verticalGap, SpringLayout.SOUTH, mergeDownLabel);
        springLayout.putConstraint(SpringLayout.NORTH, lockLabel, verticalGap, SpringLayout.SOUTH, delLayerLabel);
        springLayout.putConstraint(SpringLayout.NORTH, opacityLabel, verticalGap, SpringLayout.SOUTH, lockLabel);

        springLayout.putConstraint(SpringLayout.EAST, addLabel, -4, SpringLayout.EAST, helpPanel);
        springLayout.putConstraint(SpringLayout.EAST, delLabel, 0, SpringLayout.EAST, addLabel);
        springLayout.putConstraint(SpringLayout.EAST, visibilityLabel, 0, SpringLayout.EAST, addLabel);
        springLayout.putConstraint(SpringLayout.EAST, renameLabel, 0, SpringLayout.EAST, addLabel);
        springLayout.putConstraint(SpringLayout.EAST, mergeDownLabel, 0, SpringLayout.EAST, addLabel);
        springLayout.putConstraint(SpringLayout.EAST, delLayerLabel, 0, SpringLayout.EAST, addLabel);
        springLayout.putConstraint(SpringLayout.EAST, lockLabel, 0, SpringLayout.EAST, addLabel);
        springLayout.putConstraint(SpringLayout.EAST, opacityLabel, 0, SpringLayout.EAST, addLabel);

        JSeparator separator = new JSeparator();
        helpPanel.add(separator);

        springLayout.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, helpPanel);
        springLayout.putConstraint(SpringLayout.NORTH, separator, verticalGap, SpringLayout.SOUTH, delLabel);
        springLayout.putConstraint(SpringLayout.EAST, separator, 0, SpringLayout.EAST, helpPanel);


        ImageIcon addIcon;
        ImageIcon delIcon;
        ImageIcon visibilityIcon;
        ImageIcon renameIcon;
        ImageIcon mergeDownIcon;
        ImageIcon delLayerIcon;
        ImageIcon lockIcon;
        ImageIcon sliderIcon;

        try {
            addIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/add.png"))));
            delIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/sub.png"))));
            visibilityIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/visible.png"))));
            renameIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/rename.png"))));
            mergeDownIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/merge.png"))));
            delLayerIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/delete.png"))));
            lockIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/locked.png"))));
            sliderIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/res/slider.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JLabel addImageLabel = new JLabel(addIcon);
        JLabel delImageLabel = new JLabel(delIcon);
        JLabel visibilityImageLabel = new JLabel(visibilityIcon);
        JLabel renameImageLabel = new JLabel(renameIcon);
        JLabel mergeDownImageLabel = new JLabel(mergeDownIcon);
        JLabel delLayerImageLabel = new JLabel(delLayerIcon);
        JLabel lockImageLabel = new JLabel(lockIcon);
        JLabel opacityImageLabel = new JLabel(sliderIcon);

        helpPanel.add(addImageLabel);
        helpPanel.add(delImageLabel);
        helpPanel.add(visibilityImageLabel);
        helpPanel.add(renameImageLabel);
        helpPanel.add(mergeDownImageLabel);
        helpPanel.add(delLayerImageLabel);
        helpPanel.add(lockImageLabel);
        helpPanel.add(opacityImageLabel);

        springLayout.putConstraint(SpringLayout.NORTH, addImageLabel, 0, SpringLayout.NORTH, addLabel);
        springLayout.putConstraint(SpringLayout.NORTH, delImageLabel, 0, SpringLayout.NORTH, delLabel);
        springLayout.putConstraint(SpringLayout.NORTH, visibilityImageLabel, 0, SpringLayout.NORTH, visibilityLabel);
        springLayout.putConstraint(SpringLayout.NORTH, renameImageLabel, 0, SpringLayout.NORTH, renameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, mergeDownImageLabel, 0, SpringLayout.NORTH, mergeDownLabel);
        springLayout.putConstraint(SpringLayout.NORTH, delLayerImageLabel, 0, SpringLayout.NORTH, delLayerLabel);
        springLayout.putConstraint(SpringLayout.NORTH, lockImageLabel, 0, SpringLayout.NORTH, lockLabel);
        springLayout.putConstraint(SpringLayout.NORTH, opacityImageLabel, 0, SpringLayout.NORTH, opacityLabel);

        int horizontalGap = -4;

        springLayout.putConstraint(SpringLayout.EAST, addImageLabel, horizontalGap, SpringLayout.WEST, addLabel);
        springLayout.putConstraint(SpringLayout.EAST, delImageLabel, horizontalGap, SpringLayout.WEST, delLabel);
        springLayout.putConstraint(SpringLayout.EAST, visibilityImageLabel, horizontalGap, SpringLayout.WEST, visibilityLabel);
        springLayout.putConstraint(SpringLayout.EAST, renameImageLabel, horizontalGap, SpringLayout.WEST, renameLabel);
        springLayout.putConstraint(SpringLayout.EAST, mergeDownImageLabel, horizontalGap, SpringLayout.WEST, mergeDownLabel);
        springLayout.putConstraint(SpringLayout.EAST, delLayerImageLabel, horizontalGap, SpringLayout.WEST, delLayerLabel);
        springLayout.putConstraint(SpringLayout.EAST, lockImageLabel, horizontalGap, SpringLayout.WEST, lockLabel);
        springLayout.putConstraint(SpringLayout.EAST, opacityImageLabel, horizontalGap, SpringLayout.WEST, opacityLabel);

        helpPanel.setSize(new Dimension(400, 222));

        return helpPanel;
    }

    public static void setActiveCanvas(Canvas canvas) {
        activeCanvas = canvas;

        boolean doesCanvasExist = canvas != null;

        addButton.setEnabled(doesCanvasExist);
        delButton.setEnabled(doesCanvasExist);
    }
}
