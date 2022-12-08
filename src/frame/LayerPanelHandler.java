package frame;

import action.LayerCreationAction;
import action.LayerDeletionAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class LayerPanelHandler extends JPanel {

    private static Canvas activeCanvas;

    public LayerPanelHandler() {
        setPreferredSize(new Dimension(0, 32));

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

        JButton addButton = new JButton();
        addButton.setIcon(addIcon);
        addButton.setBorderPainted(false);
        addButton.setBackground(new Color(0, 0, 0, 0));
        addButton.setPreferredSize(new Dimension(22, 22));
        addButton.addActionListener(e -> {
            //todo lock buttons when no active canvas
            int index = activeCanvas.getActiveLayerIndex();
            index = index == -1 ? 0 : index;

            Controller.addNewLayerToActiveCanvas(index);

            LayerCreationAction thisAction = new LayerCreationAction(index);
            activeCanvas.undoManager.addEdit(thisAction);
        });

        JButton delButton = new JButton();
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

        JButton helpButton = new JButton();
        helpButton.setIcon(helpIcon);
        helpButton.setBorderPainted(false);
        helpButton.setBackground(new Color(0, 0, 0, 0));
        helpButton.setPreferredSize(new Dimension(22, 22));
        helpButton.addActionListener(e -> {
            //todo
        });

        add(addButton);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(delButton);
        add(Box.createHorizontalGlue());
        add(helpButton);
    }

    public static void setActiveCanvas(Canvas canvas) {
        activeCanvas = canvas;
    }
}
