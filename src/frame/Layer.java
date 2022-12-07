package frame;

import action.PixelChange;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Layer implements Serializable {

    private transient Graphics2D g;
    private transient BufferedImage image;
    private String name = "";
    private boolean isLocked = false;
    private boolean isVisible = true;
    private float opacity = 1.0f;

    public Layer(Canvas canvas) {
        image = new BufferedImage(canvas.getBounds().width, canvas.getBounds().height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();
    }

    public void toggleLayerLock() {
        isLocked = !isLocked;
    }

    public boolean isLocked() {
        return isLocked;
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

    public void setImage(BufferedImage image) {
        this.image = image;
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

    public void moveLayer(int dx, int dy) {
        BufferedImage duplicateImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        duplicateImage.getGraphics().drawImage(image, 0, 0, null);

        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        g.drawImage(duplicateImage, dx, dy, null);
    }

    public void drawLine(Point p1, Point p2, int width, Color color) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));

        Point[] points = getPointsOnLine(p1, p2);
        for(Point p : points){
            //noinspection SuspiciousNameCombination
            g.fillOval(p.x - width/2, p.y  - width/2, width, width);
        }
    }

    public void eraseLine(Point p1, Point p2, int width) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));

        g.setColor(new Color(0, 0, 0, 0.5f));
        g.setStroke(new BasicStroke(width));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }

    private Point[] getPointsOnLine(Point p1, Point p2) {
        int num = (int) p1.distance(p2);
        Point[] points = new Point[num];

        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;

        for(int i = 0; i  < num; i++) {
            double d = (double) i / num;
            Point p = new Point(p1.x + (int)(dx * d), p1.y + (int)(dy * d));
            points[i] = p;
        }

        return points;
    }

    public void fillAll(Color color) {
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public void initiateFill(Point pixel, Color color) {
        int targetColor = image.getRGB(pixel.x, pixel.y);

        ArrayList<Point> pixelsToCheck = new ArrayList<>();
        pixelsToCheck.add(new Point(pixel.x, pixel.y));

        while(!pixelsToCheck.isEmpty()) {
            Point thisPixel = pixelsToCheck.get(0);
            if(image.getRGB(thisPixel.x, thisPixel.y) == targetColor) {
                image.setRGB(thisPixel.x, thisPixel.y, color.getRGB());
                pixelsToCheck.addAll(getSurroundingPixels(thisPixel));
            }
            pixelsToCheck.remove(0);
        }
    }

    public ArrayList<Point> getSurroundingPixels(Point pixel) {
        ArrayList<Point> points = new ArrayList<>();

        Point north = new Point(pixel.x, pixel.y - 1);
        if(north.y >= 0) points.add(north);
        Point south = new Point(pixel.x, pixel.y + 1);
        if(south.y < image.getHeight()) points.add(south);
        Point east = new Point(pixel.x + 1, pixel.y);
        if(east.x < image.getWidth()) points.add(east);
        Point west = new Point(pixel.x - 1, pixel.y);
        if(west.x >= 0) points.add(west);

        return points;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void mergeLayerIntoThis(Layer layer) {
        BufferedImage mergingLayerImage = layer.image;
        image.getGraphics().drawImage(mergingLayerImage, 0, 0, null);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
        g = (Graphics2D) image.getGraphics();
    }
}
