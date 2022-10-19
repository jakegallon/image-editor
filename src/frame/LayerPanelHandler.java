package frame;

import javax.swing.*;
import java.awt.*;

public class LayerPanelHandler extends JPanel {

    public Canvas activeCanvas;

    public LayerPanelHandler(LayerPanel layerPanel) {
        setPreferredSize(new Dimension(0, 32));

        JButton addButton = new JButton("add");
        addButton.addActionListener(e -> {
            //todo remove
            setActiveCanvas(layerPanel.controller.getActiveCanvas());

            //todo lock buttons when no active canvas
            int index = activeCanvas.getActiveLayerIndex();

            Layer newLayer = newLayer();

            activeCanvas.addLayer(newLayer, index);
            layerPanel.addLayer(newLayer, index);
        });

        JButton delButton = new JButton("del");
        delButton.addActionListener(e -> {
            //todo remove
            setActiveCanvas(layerPanel.controller.getActiveCanvas());

            int index = activeCanvas.getActiveLayerIndex();
            activeCanvas.deleteLayer(index);
            layerPanel.deleteLayer(index);

        });

        // lock/unlock, opacity, merge below

        add(addButton);
        add(delButton);
    }

    public void setActiveCanvas(Canvas activeCanvas) {
        this.activeCanvas = activeCanvas;
    }

    private Layer newLayer() {
        Layer newLayer = new Layer(activeCanvas);
        newLayer.setName("Layer " + activeCanvas.nextNameNumber());
        return newLayer;
    }
}
