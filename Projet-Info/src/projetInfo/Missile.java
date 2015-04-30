
package projetInfo;

import java.awt.Color;
import java.awt.Graphics;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
//import javafx.scene.shape.Polygon;
//import javafx.scene.transform.Translate;

public class Missile extends Astre {

	Circle limites;
	static final int MASSE_MISSILE=10;
	static String[] NomImage = {"missile.png"};

	public Missile(int ax, int ay, Rectangle aframe, String[] tab) {
		super(ax, ay, 0, 0, tab, aframe, "Missile", 1, MASSE_MISSILE);
		//centreG = new CentreGravite(ax, ay); // A MODIFIER !!!!
		//limites = new Polygon(5.0, 50, 10.0, 0.0, 0.0, 0.0);
		limites = new Circle(ax, ay, images[0].getWidth(null)/2);
	}
	
	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe) {
		super(ax, ay, adx, ady, NomImage, aframe, "Missile", 1, MASSE_MISSILE);
		//centreG = new CentreGravite(ax, ay); // A MODIFIER !!!!
		//limites = new Polygon(5.0, 50, 10.0, 0.0, 0.0, 0.0);
		limites = new Circle(ax, ay, images[0].getWidth(null)/2);
	}

	@Override
	public void move(long t) {
		double xAstre=0.0;
		double yAstre=0.0;
		double teta=0.0;
		int masse = 0;
		double vitesse=0;
		for (int i=0; i<liste.size(); i++){
			xAstre = liste.get(i).centreG.x;
			yAstre = liste.get(i).centreG.y;
			masse = liste.get(i).masse;
			if(this.centreG.x != xAstre && this.centreG.y != yAstre && liste.get(i).nom_objet != "Missile"){
				// determiner angle a partir de deltax et deltay. Calculer force en norme. Projeter en dx dy.
				teta=Math.atan2(yAstre-this.y, xAstre-this.x);
				vitesse = (masse*this.masse)/((yAstre-this.y)*(yAstre-this.y)+(xAstre-this.x)*(xAstre-this.x));
				this.dx = dx+vitesse*Math.cos(teta);
				this.dy = dy+vitesse*Math.sin(teta);
			}
		}
		this.x = x+this.dx;
		this.y = y+this.dy;
		limites.setTranslateX(this.dx);
		limites.setTranslateY(this.dy);
		//limites.getTransforms().add(new Translate(this.dx, this.dy));
		limites.setRotate(teta-limites.getRotate());
		centreG.x = x;
		centreG.y = y;
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
	}
	
	public void draw(long t, Graphics g){
		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
		g.setColor(Color.white);
		g.drawOval(drawX, drawY, l, h);
		g.setColor(Color.green);
		g.drawOval((int)x-1, (int)y-1, 2, 2);
	}
}
