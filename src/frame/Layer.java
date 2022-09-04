package frame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Layer {

    private final BufferedImage image;
    private String name;
    private boolean isLocked = false;
    private final Graphics2D g;

    public Layer(Canvas canvas) {
        image = new BufferedImage(canvas.getBounds().width, canvas.getBounds().height, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
    }

    public void toggleLayerLock() {
        isLocked = !isLocked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void paint(Point p, Color color) {
        paint(p.x, p.y, color);
    }

    public void paint(int x, int y, Color color) {
        paint(x, y, 1, color);
    }

    public void paint(Point p, int size, Color color) {
        paint(p.x, p.y, size, color);
    }

    public void paint(int x, int y, int size, Color color) {
        g.setColor(color);
        int offset = size/2;
        g.fillRect(x - offset, y - offset, size, size);
    }
}
