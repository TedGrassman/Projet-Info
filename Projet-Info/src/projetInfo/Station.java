package projetInfo;

import java.awt.Color;
import java.awt.Graphics;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;



public class Station extends Objet {

	static String[] NomImage = {"base.png"};
	//Circle limites;
	boolean tirFait;
	
	public Station(int ax, int ay, Rectangle aframe, String nom) {
		super(ax, ay, 0, 0, NomImage, aframe, nom, "Station", 1, 0);
		limites = new Circle();
//		((Circle) limites).setCenterX((float)(ax+images[0].getWidth(null)/2));
//		((Circle) limites).setCenterY((float)(ay+images[0].getHeight(null)/2));
//		((Circle) limites).setRadius(images[0].getWidth(null));
		((Circle) limites).setCenterX(ax);
		((Circle) limites).setCenterY(ay);
		((Circle) limites).setRadius(images[0].getWidth(null)/2);
		tirFait = false;
	}
	
	public void move(long t) {
		x += (2 * dx);
		y += (2 * dy);

		if (x -l/2 < limitesframe.getX())
			x = limitesframe.getX() + l/2;
		else if (x + l/2 > limitesframe.getX() + limitesframe.getWidth())
			x = limitesframe.getX() + limitesframe.getWidth() - l/2;

		if (y - h/2 < limitesframe.getY())
			y = limitesframe.getY() + h/2;
		else if (y + h/2 > limitesframe.getY() + limitesframe.getHeight())
			y = limitesframe.getY() + limitesframe.getHeight() - h/2;

		((Circle) limites).setCenterX(x);
		((Circle) limites).setCenterY(y);
		
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
	}
	
//	public void draw(long t, Graphics g){
//		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
//		g.setColor(Color.white);
//		g.drawOval(drawX, drawY, l, h);
//		g.setColor(Color.red);
//		g.drawOval((int)x, (int)y, 10, 10);
//	}
}
