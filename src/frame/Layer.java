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
    private boolean isVisible = true;
    private float opacity = 1.0f;

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

    public void setOpacity(int opacityInt) {
        if(opacityInt == 255) {
            opacity = 1f;
            return;
        } else if (opacityInt == 0) {
            opacity = 0f;
            return;
        }

        this.opacity = normalizeRGBAToFloat(opacityInt);
    }

    private float normalizeRGBAToFloat(int i) {
        return Math.abs( - (i / 255f) );
    }

    public BufferedImage getImage() {
        return image;
    }

    public float getOpacity() {
        return opacity;
    }

    public ArrayList<PixelChange> getImageDifferences(BufferedImage originalImage) {
        ArrayList<PixelChange> pixelChanges = new ArrayList<>();
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int newColor = image.getRGB(x, y);
                int oldColor = originalImage.getRGB(x, y);
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

    public void drawLine(Point p1, Point p2, int width, Color color) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public void eraseLine(Point p1, Point p2, int width) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));

        g.setColor(new Color(0, 0, 0, 0.5f));
        g.setStroke(new BasicStroke(width));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }

    public void fillAll(Color color) {
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
