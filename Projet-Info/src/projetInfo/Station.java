package projetInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;



@SuppressWarnings("unused")
public class Station extends Astre {


	static String[] NomImage = {"base.png"};
	//Circle limites;
	boolean tirFait;

	
	public Station(int ax, int ay, Rectangle aframe, String nom) {
		super(ax, ay, 0, 0, NomImage, aframe, nom, "Station", 1, 0);

//		limites = new Circle();
//		((Circle) limites).setCenterX(ax);
//		((Circle) limites).setCenterY(ay);
//		((Circle) limites).setRadius(images[0].getWidth(null)/2);
		tirFait = false;
		limites = new Area (new Ellipse2D.Double(drawX, drawY, images[0].getWidth(null), images[0].getHeight(null))); //Hitbox elliptique
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
		
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
		limites = new Area (new Ellipse2D.Double(drawX, drawY, images[0].getWidth(null), images[0].getHeight(null)));	//Actualisation de la hitbox
	}
	
	public void draw(long t, Graphics g){
		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
		
		/*
		Graphics2D g2d =(Graphics2D)g;							------------
		AffineTransform at2 = new AffineTransform();			DEBBUGING : dessin de la hitbox
		GeneralPath path1 = new GeneralPath();					------------
		path1.append(limites.getPathIterator(at2), true);
		g2d.setColor(Color.WHITE);
        g2d.fill(path1);
        g2d.draw(path1.getBounds());
		*/
	}
}
