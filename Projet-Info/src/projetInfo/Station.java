package projetInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;



public class Station extends Objet {

	static String[] NomImage = {"base.png"};
	//Circle limites;
	
	public Station(int ax, int ay, Rectangle aframe, String nom) {
		super(ax, ay, 0, 0, NomImage, aframe, nom, "Station", 1, 0);
		limites = new Area (new Ellipse2D.Double(drawX, drawY, images[0].getWidth(null), images[0].getHeight(null)));
//		((Circle) limites).setCenterX((float)(ax+images[0].getWidth(null)/2));
//		((Circle) limites).setCenterY((float)(ay+images[0].getHeight(null)/2));
//		((Circle) limites).setRadius(images[0].getWidth(null));
	}
	
	public void move(long t) {
		x += (2 * dx);
		y += (2 * dy);

		if (x < limitesframe.getX())
			x = limitesframe.getX();
		else if (x + l > limitesframe.getX() + limitesframe.getWidth())
			x = limitesframe.getX() + limitesframe.getWidth() - l;

		if (y < limitesframe.getY())
			y = limitesframe.getY();
		else if (y + h > limitesframe.getY() + limitesframe.getHeight())
			y = limitesframe.getY() + limitesframe.getHeight() - h;
		
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
		limites = new Area (new Ellipse2D.Double(drawX, drawY, images[0].getWidth(null), images[0].getHeight(null)));
	}
	
//	public void draw(long t, Graphics g){
//		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
//		g.setColor(Color.white);
//		g.drawOval(drawX, drawY, l, h);
//		g.setColor(Color.red);
//		g.drawOval((int)x, (int)y, 10, 10);
//	}
}
