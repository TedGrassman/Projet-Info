package gameframework;

public class Point {	//Point géométrique (2D)

    public double x, y; //Coordonnées du point

    public Point(double ax, double ay) {
        x = ax;
        y = ay;
    }

    public double distance(Point otherPoint) { //Renvoie la distance entre le point et un autre point fourni en paramètre
        double dx = x - otherPoint.x;
        double dy = y - otherPoint.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String toString() {					//Permet l'affichage des coordonnées dans un println(Point)
        return "[x=" + x + ",y=" + y + "]";
    }

}
