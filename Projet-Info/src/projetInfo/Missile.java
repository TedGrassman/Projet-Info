
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


public class Missile extends Astre {


	//Polygon limites; //hitbox triangulaire
	double angle; //orientation du missile
	static final int MASSE_MISSILE=10;
	static String[] NomImage = {"missile2.png"};
	Color couleur;

	public Missile(int ax, int ay, Rectangle aframe, String[] tab, Color acouleur) {
		super(ax, ay, 0, 0, tab, aframe, "Missile", "Missile", 1, MASSE_MISSILE);
		centreG = new CentreGravite(ax, ay); // A MODIFIER !!!!
		int[]xpoints = {-10,0,10};
		int[]ypoints = {25,-25,25};
		limites = new Area(new Polygon(xpoints, ypoints, 3));
		angle = 0.0 ;
		couleur=acouleur;
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
	}
	
	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String nom, Color acouleur) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "Missile", 1, MASSE_MISSILE);
		centreG = new CentreGravite(ax, ay); // A MODIFIER !!!!
		int[]xpoints = {-10,0,10};
		int[]ypoints = {25,-25,25};
		limites = new Area (new Polygon(xpoints, ypoints, 3));
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
		angle = 0.0 ;
		couleur=acouleur;
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

			if(this.centreG.x != xAstre && this.centreG.y != yAstre && !liste.get(i).typeObjet.equals(this.typeObjet)){
				// determiner angle a partir de deltax et deltay. Calculer force en norme. Projeter en dx dy.
				teta=Math.atan2(yAstre-this.y, xAstre-this.x);
				vitesse = (masse*this.masse)/((yAstre-this.y)*(yAstre-this.y)+(xAstre-this.x)*(xAstre-this.x));
				this.dx = dx+vitesse*Math.cos(teta);
				this.dy = dy+vitesse*Math.sin(teta);
			}
		}
		this.centreG.x = (centreG.x+this.dx);
		this.centreG.y = (centreG.y+this.dy);
		this.x = x+this.dx;
		this.y = y+this.dy;
		this.drawX=(int) (this.drawX+dx);
		this.drawY=(int) (this.drawY+dy);
		transfo.setToIdentity();
		transfo.translate(this.centreG.x,this.centreG.y);
		
		//limites.setTranslateX(this.centreG.x);
		//limites.setTranslateY(this.centreG.y);
		//limites.getTransforms().add(new Translate(this.dx, this.dy));

		angle = Math.atan2(dy, dx)-Math.PI*3/2; // met a jour l'orientation du missile
		//limites.setRotate(angle-Math.PI*3/2); // me demandez pas pourquoi il faut faire *3PI/2 ... ^-^
		transfo.rotate(angle);
		//transfo.translate(-images[(int) t % NbImages].getWidth()/2, -images[(int) t % NbImages].getHeight()/2);
		//limites.transform(transfo);
		
	}
	
	public void draw(long t, Graphics g) { // Dessine le missile au temps t dans l'interface graphique g avec la bonne orientation
		
		AffineTransform at = new AffineTransform();
		at.rotate(angle);
		at.translate(-10, -25);
		AffineTransformOp op = new AffineTransformOp (at, 1);
		Graphics2D g2d =(Graphics2D)g; // cast le graphics en graphics2d pour pouvoir appliquer la transformation
		g2d.drawImage(images[(int) t % NbImages], op, (int)centreG.x, (int)centreG.y); // dessine l'image
		/*
		GeneralPath path1 = new GeneralPath();
		AffineTransform at2 = new AffineTransform();
		at2.translate(centreG.x, centreG.y);
		at2.rotate(angle);
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
	
//	public void draw(long t, Graphics g){
//		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
//		g.setColor(Color.white);
//		g.drawOval(drawX, drawY, l, h);
//		g.setColor(Color.green);
//		g.drawOval((int)x-1, (int)y-1, 2, 2);
//	}
	public Astre Collision(){
		for (int i=0; i<liste.size(); i++){
			
			if (this.Collision(liste.get(i))){
				return liste.get(i);
			}
		}
		return this;
	}
	/*public boolean Collision (Astre A1){
		AffineTransform at1 = transfo;
		GeneralPath path1 = new GeneralPath();
        path1.append(this.limites.getPathIterator(at1), true);
        AffineTransform at2 = new AffineTransform();
        at2.translate(A1.centreG.x, A1.centreG.y);
		GeneralPath path2 = new GeneralPath();
        path2.append(A1.limites.getPathIterator(at2), true);
        Area a1 = new Area(path1);
        Area a2 = new Area(path2);
        a2.intersect(a1);
        if (!a2.isEmpty()) {
        	return true;
        }
        return false;
	}
	*/
	public boolean Collision (Astre A1){
		AffineTransform at1 = transfo;
		GeneralPath path1 = new GeneralPath();
        path1.append(this.limites.getPathIterator(at1), true);
        AffineTransform at2 = A1.transfo;
		GeneralPath path2 = new GeneralPath();
        path2.append(A1.limites.getPathIterator(at2), true);
        Area a1 = new Area(path1);
        Area a2 = new Area(path2);
        a2.intersect(a1);
        if (!a2.isEmpty()) {
        	return true;
        }
        return false;
	}
}
