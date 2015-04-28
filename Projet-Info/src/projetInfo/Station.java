package projetInfo;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;



public class Station extends Objet {

	static String[] NomImage = {"base.png"};
	Circle limites;
	
	public Station(int ax, int ay, Rectangle aframe, String nom) {
		super(ax, ay, 0, 0, NomImage,aframe, nom, 1, 0);
		limites = new Circle();
		((Circle) limites).setCenterX((float)(ax+images[0].getWidth(null)/2));
		((Circle) limites).setCenterY((float)(ay+images[0].getHeight(null)/2));
		((Circle) limites).setRadius(images[0].getWidth(null));
	}
	
	public void move(long t) {
		x += (int) (2 * dx);
		y += (int) (2 * dy);

		if (x < limitesframe.getX())
			x = (int)limitesframe.getX();
		else if (x + l > limitesframe.getX() + limitesframe.getWidth())
			x = (int)(limitesframe.getX() + limitesframe.getWidth() - l);

		if (y < limitesframe.getY())
			y = (int)limitesframe.getY();
		else if (y + h > limitesframe.getY() + limitesframe.getHeight())
			y = (int)(limitesframe.getY() + limitesframe.getHeight() - h);

		((Circle) limites).setCenterX((double) x);
		((Circle) limites).setCenterY((double) y);
	}
}
