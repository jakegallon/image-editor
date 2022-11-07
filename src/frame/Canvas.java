package frame;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends JPanel {

    private String fileName;

    public ArrayList<Layer> layers = new ArrayList<>();
    private Layer activeLayer;

    public UndoManager undoManager = new UndoManager();
    public GridInformation gridInformation = new GridInformation(32, 32, GridStyle.NONE);

    public Canvas() {
        setBounds(0, 0, 256, 256);
    }

    private void drawGrid(Graphics g) {
        switch (gridInformation.gridStyle()){
            case NONE -> {
            }
            case SOLID -> {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(0.1f));
                g2d.setColor(Color.black);

                Rectangle bounds = getBounds();
                for(int x = 0; x < bounds.width; x += gridInformation.gridX()) {
                    g2d.drawLine(x, 0, x, bounds.height);
                }
                for(int y = 0; y < bounds.height; y += gridInformation.gridY()) {
                    g2d.drawLine(0, y, bounds.width, y);
                }
            }
        }
    }

    public void setGridX(int gridX) {
        int curY = gridInformation.gridY();
        GridStyle curStyle = gridInformation.gridStyle();
        gridInformation = new GridInformation(gridX, curY, curStyle);
    }

    public void setGridY(int gridY) {
        int curX = gridInformation.gridX();
        GridStyle curStyle = gridInformation.gridStyle();
        gridInformation = new GridInformation(curX, gridY, curStyle);
    }

    public void setGridStyle(GridStyle gridStyle) {
        int curX = gridInformation.gridX();
        int curY = gridInformation.gridY();
        gridInformation = new GridInformation(curX, curY, gridStyle);
    }

    public void getGridCellAtPoint(Point p) {
        // todo
    }

    private BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateImage();
        g.drawImage(image, 0, 0, getBounds().width, getBounds().height, null);
        drawGrid(g);
    }

    private void updateImage() {
        image = new BufferedImage(getBounds().width, getBounds().height, BufferedImage.TYPE_INT_ARGB);

        for (int i = layers.size() - 1; i >= 0; i--) {
            if(layers.get(i).isVisible()) {
                Graphics gImage = image.getGraphics();
                gImage.drawImage(layers.get(i).getImage(), 0, 0, getBounds().width, getBounds().height, null);
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public Color getColorAtPoint(Point p) {
        int c = image.getRGB(p.x, p.y);
        return new Color(c);
    }

    public int getActiveLayerIndex() {
        if(activeLayer == null) return 0;
        return layers.indexOf(activeLayer);
    }

    public void addLayer(Layer layer) {
        int targetIndex = getActiveLayerIndex();
        layers.add(targetIndex, layer);
        activeLayer = layer;
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

    public void deleteLayer() {
        deleteLayer(getActiveLayerIndex());
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

    public void mergeActiveLayerDown() {
        int activeIndex = getActiveLayerIndex();
        Layer targetLayer = layers.get(activeIndex + 1);
        targetLayer.addImage(activeLayer.getImage());
        deleteLayer(activeIndex);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

