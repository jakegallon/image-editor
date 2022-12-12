package frame;

import tool.AnimationTool;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Canvas extends JPanel implements Serializable {

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

    private Rectangle selectedArea;

    private float currentDashPhase = 0f;
    private final Timer timer = new Timer(500, e -> currentDashPhase = currentDashPhase == 0f ? 0.5f : 0f);

    private void drawSelectedArea(Graphics g) {
        if(!timer.isRunning()) timer.start();

        Graphics2D g2d = (Graphics2D) g;
        drawDashingRectangle(g2d);
    }

    public Rectangle getSelectedArea() {
        return selectedArea;
    }

    public void initializeSelectedArea(int x, int y) {
        selectedArea = new Rectangle(x, y, 0, 0);
    }

    public void setSelectedAreaOffset(int x, int y) {
        selectedArea.x = x;
        selectedArea.y = y;
    }

    public void shiftSelectedArea(int dx, int dy) {
        int targetX = selectedArea.x + dx;
        int targetY = selectedArea.y + dy;

        if(targetX >= 0 && targetX + selectedArea.width <= getWidth()) selectedArea.x = targetX;
        if(targetY >= 0 && targetY + selectedArea.height <= getHeight()) selectedArea.y = targetY;
    }

    public void setSelectedAreaWidth(int width) {
        selectedArea.width = width;
    }

    public void setSelectedAreaHeight(int height) {
        selectedArea.height = height;
    }

    public void nullifySelectedArea() {
        selectedArea = null;
    }

    public void checkSelectedArea() {
        if(selectedArea.x + selectedArea.width < selectedArea.x) {
            selectedArea.x = selectedArea.x + selectedArea.width;
            selectedArea.width = Math.abs(selectedArea.width);
        }
        if(selectedArea.y + selectedArea.height < selectedArea.y) {
            selectedArea.y = selectedArea.y + selectedArea.height;
            selectedArea.height = Math.abs(selectedArea.height);
        }
    }

    private void drawDashingRectangle(Graphics2D g2d) {
        int x = selectedArea.x;
        int y = selectedArea.y;
        int x2 = selectedArea.x + selectedArea.width;
        int y2 = selectedArea.y + selectedArea.height;

        g2d.setStroke(new BasicStroke(0.1f));
        g2d.setColor(Color.white);

        g2d.drawLine(x, y, x, y2);
        g2d.drawLine(x, y, x2, y);
        g2d.drawLine(x, y2, x2, y2);
        g2d.drawLine(x2, y, x2, y2);

        Stroke dashingStroke = new BasicStroke(0.1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{0.5f}, currentDashPhase);
        g2d.setStroke(dashingStroke);
        g2d.setColor(Color.black);

        g2d.drawLine(x, y, x, y2);
        g2d.drawLine(x, y, x2, y);
        g2d.drawLine(x, y2, x2, y2);
        g2d.drawLine(x2, y, x2, y2);
    }

    public void setGridX(int gridX) {
        int curY = gridInformation.gridY();
        GridStyle curStyle = gridInformation.gridStyle();
        setGridInformation(new GridInformation(gridX, curY, curStyle));
    }

    public void setGridY(int gridY) {
        int curX = gridInformation.gridX();
        GridStyle curStyle = gridInformation.gridStyle();
        setGridInformation(new GridInformation(curX, gridY, curStyle));
    }

    public void setGridStyle(GridStyle gridStyle) {
        int curX = gridInformation.gridX();
        int curY = gridInformation.gridY();
        setGridInformation(new GridInformation(curX, curY, gridStyle));
    }

    private void setGridInformation(GridInformation g) {
        gridInformation = g;
        AnimationPanel.setGridInformation(g);
    }

    public GridInformation getGridInformation() {
        return gridInformation;
    }

    public Point getGridCellAtPoint(Point p) {
        return new Point(p.x / gridInformation.gridX(), p.y / gridInformation.gridY());
    }

    private transient BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateImage();
        g.drawImage(image, 0, 0, getBounds().width, getBounds().height, null);
        drawGhost(g);
        drawGrid(g);

        if(selectedArea != null) drawSelectedArea(g);

        if(Controller.getActiveTool() instanceof AnimationTool) {
            g.setColor(Color.blue);
            ((Graphics2D) g).setStroke(new BasicStroke(0.2f));
            g.drawRect(highlightRect.x, highlightRect.y, highlightRect.width, highlightRect.height);
        }
    }

    public Rectangle highlightRect = new Rectangle();

    public boolean isDrawingGhost = false;
    public boolean isWrappingGhost = false;
    public int ghostOpacity = 20;

    private void drawGhost(Graphics g) {
        if(!isDrawingGhost) return;

        Image ghost = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        ghost.getGraphics().drawImage(
                image.getSubimage(0, 0, image.getWidth() - gridInformation.gridX(), image.getHeight()),
                gridInformation.gridX(), 0, null);

        if(isWrappingGhost) ghost.getGraphics().drawImage(
                image.getSubimage(image.getWidth() - gridInformation.gridX(),
                        0, gridInformation.gridX(), image.getHeight() - gridInformation.gridY()),
                0, gridInformation.gridY(), null);

        Composite savedComposite = ((Graphics2D) g).getComposite();
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ghostOpacity/255f));
        g.drawImage(ghost, 0, 0, null);
        ((Graphics2D) g).setComposite(savedComposite);
    }

    private void updateImage() {
        image = new BufferedImage(getBounds().width, getBounds().height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();

        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);

            if (layer.isVisible() && layer.getOpacity() > 0f) {
                ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.getOpacity()));
                g.drawImage(layer.getImage(), 0, 0, getBounds().width, getBounds().height, null);
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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    }
}