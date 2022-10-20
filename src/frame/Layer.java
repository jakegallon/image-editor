package frame;

import action.PixelChange;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Layer {

    private final BufferedImage image;
    private String name = "";
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

    public ArrayList<PixelChange> getImageDifferences(BufferedImage targetImage) {
        ArrayList<PixelChange> pixelChanges = new ArrayList<>();
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int newColor = targetImage.getRGB(x, y);
                if(newColor == 0) continue;
                int oldColor = image.getRGB(x, y);
                if(newColor == oldColor) continue;

                PixelChange pixelChange = new PixelChange(x, y, oldColor, newColor);
                pixelChanges.add(pixelChange);
            }
        }
        return pixelChanges;
    }

    public void addImage(BufferedImage image) {
        g.drawImage(image, 0, 0, null);
    }

    public void paint(int x, int y, int color) {
        image.setRGB(x, y, color);
    }

    public void drawLine(Point p1, Point p2, int width) {
        //todo set properly
        g.setColor(Controller.selectedColor());
        g.setStroke(new BasicStroke(width));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public void fillAll(Color color) {
        Color cacheColor = g.getColor();
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(cacheColor);
    }
}
