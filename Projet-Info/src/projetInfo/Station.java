package projetInfo;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;



public class Station extends Objet {

	static String[] NomImage = {"base.png"};
	Circle limites;
	
	public Station(int ax, int ay, Rectangle aframe, String nom) {
		super(ax, ay, (float)0, (float)0, NomImage,aframe, nom, 1, 0);
		limites = new Circle();
		limites.setCenterX((float)(ax+images[0].getWidth(null)/2));
		limites.setCenterY((float)(ay+images[0].getHeight(null)/2));
		limites.setRadius(images[0].getWidth(null));
	}
	
	public void move(long t) {
		x += (int) (vitesse * dx);
		y += (int) (vitesse * dy);

		if (x < limitesframe.getX())
			x = (int)limitesframe.getX();
		else if (x + l > limitesframe.getX() + limitesframe.getWidth())
			x = (int)(limitesframe.getX() + limitesframe.getWidth() - l);

		if (y < limitesframe.getY())
			y = (int)limitesframe.getY();
		else if (y + h > limitesframe.getY() + limitesframe.getHeight())
			y = (int)(limitesframe.getY() + limitesframe.getHeight() - h);

		limites.setCenterX((double) x);
		limites.setCenterY((double) y);
	}
}
