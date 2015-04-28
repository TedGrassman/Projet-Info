
package projetInfo;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;

public class Missile extends Astre {

	Polygon limites;

	public Missile(int ax, int ay, Rectangle aframe, String[] tab) {
		super(ax, ay, (float) 0, (float) 0, tab, aframe, "Missile.png", 1, 10);
		centreG = new CentreGravite(ax, ay); // A MODIFIER !!!!
		limites = new Polygon(5.0, 50, 10.0, 0.0, 0.0, 0.0);
	}

	@Override
	public void move(long t) {
		double xAstre=0.0;
		double yAstre=0.0;
		double teta=0.0;
		int masse =0;
		double vitesse=0;
		for (int i=0; i<liste.size(); i++){
			xAstre= liste.get(i).centreG.x;
			yAstre= liste.get(i).centreG.y;
			masse = liste.get(i).masse;
			if(this.centreG.x!= xAstre && this.centreG.y!= yAstre){
			// determiner angle a partir de deltax et deltay. Calculer force en norme. Projeter en dx dy.
			teta=Math.atan2(yAstre-this.y, xAstre-this.x);
			vitesse = (masse*this.masse)/((yAstre-this.y)*(yAstre-this.y)+(xAstre-this.x)*(xAstre-this.x));
			this.dx=(float) (dx+vitesse*Math.cos(teta));
			this.dy=(float) (dy+vitesse*Math.sin(teta));
			}
		}
		this.x=(int) (x+this.dx);
		this.y=(int) (y+this.dy);
		limites.setTranslateX(this.dx);
		limites.setTranslateY(this.dy);
		//limites.getTransforms().add(new Translate(this.dx, this.dy));
		limites.setRotate(teta-limites.getRotate());
		centreG.x = x;
		centreG.y = y;
	}
}
