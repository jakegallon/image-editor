package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {

    private final BufferedImage image;
    private final Graphics2D g;

    public Canvas() {
        setBounds(0, 0, 256, 256);
        setBackground(Color.white);
        image = new BufferedImage(getBounds().width, getBounds().height, BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getBounds().width, getBounds().height, null);
    }

    public void setPixel(Point p, Color color) {
        g.setColor(color);
        g.fillRect(p.x, p.y, 1, 1);
    }
}

