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

    public void paint(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }
}
