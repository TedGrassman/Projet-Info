package projetInfo;

public class Point {

    public double x, y;

    public Point(double ax, double ay) {
        x = ax;
        y = ay;
    }

    public double distance(Point otherPoint) {
        double dx = x - otherPoint.x;
        double dy = y - otherPoint.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String toString() {
        return "[x=" + x + ",y=" + y + "]";
    }

}
