
package projetInfo;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
//import javafx.scene.shape.Polygon;
//import javafx.scene.transform.Translate;

import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.Rectangle;


@SuppressWarnings("unused")
public class Missile extends Astre {

//	Polygon limites;								//hitbox triangulaire
	static final int MASSE_MISSILE=10;				//masse des missiles (par défaut)
	double angle; 									//orientation du missile par rapport à la verticale
	static String[] NomImage = {"missile2.png"};	//nom des PNG du missile
	Color couleur;									//couleur de la trajectoire
	Trajectoire traj;								//trajectoire du missile



	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String[] tab) {
		super(ax, ay, adx,ady, tab, aframe, "Missile", "Missile", 1, MASSE_MISSILE);
		centreG = new CentreGravite(ax, ay);
		angle = 0.0 ;
		//limites = new Polygon(10.0, 0, 20.0, 50.0, 0.0, 50.0);
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
		int[]xpoints = {-10,0,10};									//Creation des tableaux de coordonnées du triangle de hitbox
		int[]ypoints = {25,-25,25};
		limites = new Area (new Polygon(xpoints, ypoints, 3));		//Creation de la hitbox (triangle)
	}
	
	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String nom, Color acouleur) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "Missile", 1, MASSE_MISSILE);
		centreG = new CentreGravite(ax, ay);
		angle = 0.0 ;
		//limites = new Polygon(10.0, 0, 20.0, 50.0, 0.0, 50.0);
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
		centreG = new CentreGravite(ax, ay);						//Creation du centre de gravité au centre du missile
		int[]xpoints = {-10,0,10};									//Creation des tableaux de coordonnées du triangle de hitbox
		int[]ypoints = {25,-25,25};
		limites = new Area (new Polygon(xpoints, ypoints, 3));		//Creation de la hitbox (triangle)
		angle = 0.0 ;												//Orientation initiale du missile : verticale
		couleur = acouleur;
		traj = new Trajectoire (this, 90, 5, couleur);				//Creation de la trajectoire

	}

	
	public void move(long t) {										//déplacement du missile à chaque cycle
		double xAstre=0.0;
		double yAstre=0.0;
		double teta=0.0;
		int masse = 0;
		double vitesse=0;
		for (int i=0; i<liste.size(); i++){							//calcul du déplacement lié à la gravité
			xAstre = liste.get(i).centreG.x;
			yAstre = liste.get(i).centreG.y;
			masse = liste.get(i).masse;

			if(this.centreG.x != xAstre && this.centreG.y != yAstre && !liste.get(i).typeObjet.equals(this.typeObjet)){
				// determiner angle a partir de deltax et deltay. Calculer force en norme. Projeter en dx dy.
				teta=Math.atan2(yAstre-this.y, xAstre-this.x);
				vitesse = (masse*this.masse)/((yAstre-this.y)*(yAstre-this.y)+(xAstre-this.x)*(xAstre-this.x));
				this.dx = dx+vitesse*Math.cos(teta);
				this.dy = dy+vitesse*Math.sin(teta);
			}
		}
		this.centreG.x = (centreG.x+this.dx);						//translation des coordonnées du missile
		this.centreG.y = (centreG.y+this.dy);
		this.x = x+this.dx;
		this.y = y+this.dy;
		this.drawX=(int) (this.drawX+dx);
		this.drawY=(int) (this.drawY+dy);
		transfo.setToIdentity();									//Remise à zéro de la transformation affine
		transfo.translate(this.centreG.x,this.centreG.y);			//Positionne la hitbox
	
		angle = Math.atan2(dy, dx)-Math.PI*3/2; 					//Met a jour l'orientation du missile
		transfo.rotate(angle);										//Fait pivoter la hitbox
		
		traj.actualisation();										//Actualisation de la trajectoire apres le déplacement
	}
	
	public void draw(long t, Graphics g) { // Dessine le missile au temps t dans l'interface graphique g avec la bonne orientation
		
		AffineTransform at = new AffineTransform();
		at.rotate(angle);
		at.translate(-10, -25);
		AffineTransformOp op = new AffineTransformOp (at, 1);
		Graphics2D g2d =(Graphics2D)g; 													// cast le graphics en graphics2d pour pouvoir appliquer la transformation
		g2d.drawImage(images[(int) t % NbImages], op, (int)centreG.x, (int)centreG.y);	// dessine l'image
		traj.draw(t, g2d);																// dessine la trajectoire
		
		/*
		GeneralPath path1 = new GeneralPath();					------------------
		AffineTransform at2 = new AffineTransform();			DEBBUGING 
		at2.translate(centreG.x, centreG.y);					A UTILISER POUR VISUALISER LA HITBOX
		at2.rotate(angle);										------------------
        path1.append(limites.getPathIterator(at2), true);
        g2d.setColor(couleur);
        g2d.fill(path1);
        g2d.draw(path1.getBounds());
        */

		
	}
	/*
	public void placer(int posx, int posy, double aangle){
		centreG = new CentreGravite(posx, posy);
		angle=aangle;
		limites = new Polygon(posx, posy+25, posx+10, posy-25, posx-10, posy-25);
		centreG.x = x;
		centreG.y = y;
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
	}
	*/
}
