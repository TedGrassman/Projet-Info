package gameframework;

public class Point {	//Point g�om�trique (2D)

    public double x, y; //Coordonn�es du point

    public Point(double ax, double ay) {
        x = ax;
        y = ay;
    }

    public double distance(Point otherPoint) { //Renvoie la distance entre le point et un autre point fourni en param�tre
        double dx = x - otherPoint.x;
        double dy = y - otherPoint.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String toString() {					//Permet l'affichage des coordonn�es dans un println(Point)
        return "[x=" + x + ",y=" + y + "]";
    }

}
