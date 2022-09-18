package frame;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.util.ArrayList;

public class Canvas extends JPanel {

    public ArrayList<Layer> layers = new ArrayList<>();
    private Layer activeLayer;

    public UndoManager undoManager = new UndoManager();

    public Canvas() {
        setBounds(0, 0, 256, 256);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = layers.size() - 1; i >= 0; i--) {
            g.drawImage(layers.get(i).getImage(), 0, 0, getBounds().width, getBounds().height, null);
        }
    }

    public int getActiveLayerIndex() {
        if(activeLayer == null) return 0;
        return layers.indexOf(activeLayer);
    }

    public void addLayer(Layer layer, int index) {
        layers.add(index, layer);
        activeLayer = layer;
    }

    public int nextNameNumber() {
        ArrayList<Integer> takenNums = new ArrayList<>();
        for(Layer layer : layers) {
            String name = layer.getName();
            if(name.matches("(Layer)\\s\\d+")){
                int num = Integer.parseInt(name.substring(6));
                takenNums.add(num);
            }
        }
        for (int i = 1; i <= layers.size() + 1; i++) {
            if (!takenNums.contains(i)) {
                return i;
            }
        }
        return 1;
    }

    public void deleteLayer(int index) {
        if(layers.size() == 0) return;

        layers.remove(index);

        if(layers.size() == 0) {
            activeLayer = null;
            return;
        }

        if(index >= layers.size()) index = layers.size() - 1;
        activeLayer = layers.get(index);
    }

    public void setActiveLayer(Layer layer) {
        activeLayer = layer;
    }

    public Layer getActiveLayer() {
        return activeLayer;
    }
}

