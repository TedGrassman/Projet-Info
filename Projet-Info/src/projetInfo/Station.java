package projetInfo;

import java.awt.Rectangle;


public class Station extends Objet {

	static String[] NomImage = {"base2.png"};
	
	public Station(Rectangle aframe, String nom) {
		super((aframe.width / 2) - 13, aframe.height - 200, 0, 0, 10, NomImage,
				aframe, nom, 1, 0);
	}
	
	public void move(long t) {
		x += (int) (vitesse * dx);
		y += (int) (vitesse * dy);

		if (x < limitesframe.x)
			x = limitesframe.x;
		else if (x + l > limitesframe.x + limitesframe.width)
			x = limitesframe.x + limitesframe.width - l;

		if (y < limitesframe.y)
			y = limitesframe.y;
		else if (y + h > limitesframe.y + limitesframe.height)
			y = limitesframe.y + limitesframe.height - h;

		limites.setLocation(x, y);
	}
}
