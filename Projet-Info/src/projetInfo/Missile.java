
package projetInfo;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
//import javafx.scene.shape.Polygon;
//import javafx.scene.transform.Translate;


public class Missile extends Astre {


	//Polygon limites; //hitbox triangulaire
	double angle; //orientation du missile
	static final int MASSE_MISSILE=10;
	static String[] NomImage = {"missile2.png"}; //missile2.png

	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String[] tab) {
		super(ax, ay, adx,ady, tab, aframe, "Missile", "Missile", 1, MASSE_MISSILE);
		centreG = new CentreGravite(ax, ay);
		limites = new Polygon(10.0, 0, 20.0, 50.0, 0.0, 50.0);
		angle = 0.0 ;
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
	}
	
	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String nom) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "Missile", 1, MASSE_MISSILE);
		centreG = new CentreGravite(ax, ay);
		limites = new Polygon(10.0, 0, 20.0, 50.0, 0.0, 50.0);
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
		angle = 0.0 ;
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
		limites.setTranslateX(this.centreG.x);
		limites.setTranslateY(this.centreG.y);
		//limites.getTransforms().add(new Translate(this.dx, this.dy));

		angle = Math.atan2(dy, dx); // met a jour l'orientation du missile
		limites.setRotate(angle-Math.PI*3/2); // me demandez pas pourquoi il faut faire *3PI/2 ... ^-^
		System.out.println(limites.getTransforms());
		
	}
	
	public void draw(long t, Graphics g) { // Dessine le missile au temps t dans l'interface graphique g avec la bonne orientation
		AffineTransform at = new AffineTransform(); //crée une transformation a appliquer à l'image
		at.translate(limites.getTranslateX(), limites.getTranslateY()); //translate l'image jusqu'à son centre de gravité
		at.rotate(limites.getRotate()); //rotation de l'image, [correction de 0.1]
		at.translate(-images[(int) t % NbImages].getWidth()/2, -images[(int) t % NbImages].getHeight()/2); // decale l'image pour que la rotation se fasse autour de son centre
		Graphics2D g2d =(Graphics2D)g; // cast le graphics en graphics2d pour pouvoir appliquer la transformation
		g2d.drawImage(images[(int) t % NbImages], at, null); // dessine l'image
	}
	
	public void placer(int posx, int posy, double aangle){
		centreG = new CentreGravite(posx, posy);
		angle=aangle;
		limites = new Polygon(posx, posy+25, posx+10, posy-25, posx-10, posy-25);
		centreG.x = x;
		centreG.y = y;
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
	}
	
//	public void draw(long t, Graphics g){
//		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
//		g.setColor(Color.white);
//		g.drawOval(drawX, drawY, l, h);
//		g.setColor(Color.green);
//		g.drawOval((int)x-1, (int)y-1, 2, 2);
//	}
}
