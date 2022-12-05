package frame;

import action.LayerCreationAction;

import javax.swing.*;
import java.awt.*;

public class LayerPanelHandler extends JPanel {

    private static Canvas activeCanvas;

    public LayerPanelHandler() {
        setPreferredSize(new Dimension(0, 32));

        JButton addButton = new JButton("add");
        addButton.addActionListener(e -> {
            //todo lock buttons when no active canvas
            int index = activeCanvas.getActiveLayerIndex();
            index = index == -1 ? 0 : index;

            Controller.addNewLayerToActiveCanvas(index);

            LayerCreationAction thisAction = new LayerCreationAction(index);
            activeCanvas.undoManager.addEdit(thisAction);
        });

        JButton delButton = new JButton("del");
        delButton.addActionListener(e -> {
            int index = activeCanvas.getActiveLayerIndex();

            Controller.deleteLayerFromActiveCanvas(index);
        });

        // lock/unlock, opacity, merge below

        add(addButton);
        add(delButton);
    }

    public static void setActiveCanvas(Canvas canvas) {
        activeCanvas = canvas;
    }
}
