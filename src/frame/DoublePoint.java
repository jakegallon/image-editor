package frame;

import java.awt.*;

public class DoublePoint {

    public double x, y;

    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point toIntPoint() {
        return new Point((int) x, (int) y);
    }

    public static DoublePoint toDoublePoint(Point p) {
        return new DoublePoint(p.x, p.y);
    }
}
