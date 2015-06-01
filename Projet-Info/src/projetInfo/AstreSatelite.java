package projetInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.Rectangle;



public class AstreSatelite  extends Astre  {
	double rayon;	//Rayon de l'astre
	AstreSpherique astre;  //astre sphérique autour duquel il tourne
	//double angle;
	double omega;
	
	public AstreSatelite (int ax, int ay, String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse, double rayon, AstreSpherique astre, double omega) {
		super(ax, ay,0, 0, NomImage, aframe, nom, "AstreSpherique", nbIm, masse);
		this.rayon=rayon;
		limites = new Area(new Ellipse2D.Double(drawX, drawY, (rayon),(rayon)));	//Création de la hitbox : disque de centre (ax,ay)
		this.astre=astre;
		centreG = new CentreGravite(ax, ay);
		omega = 0.0 ;
	}

	public void move(long t) {
		double xAstre;
		double yAstre;
		xAstre = astre.centreG.x;
		yAstre = astre.centreG.y;
		
		this.x=xAstre + 10*Math.cos(omega*t);
		this.y=yAstre + 10*Math.sin(omega*t);
		drawX = (int)(this.x - rayon);
		drawY = (int)(this.y - rayon);
		
		
		/*double xAstre=0.0;
		double yAstre=0.0;
		double teta=0.0;
		int masse = 0;
		double vitesse=5;
		
								
		xAstre = astre.centreG.x;
		yAstre = astre.centreG.y;
		masse = astre.masse;
		double r= 10;
				//Math.sqrt(Math.pow((xAstre-this.centreG.x),2)+Math.pow((yAstre-this.centreG.y),2));

		//if(this.centreG.x != xAstre && this.centreG.y != yAstre){
			// determiner angle a partir de deltax et deltay. Calculer force en norme. Projeter en dx dy.
			//teta=Math.atan2(yAstre-this.y, xAstre-this.x);
			//vitesse = (masse*this.masse)/((yAstre-this.y)*(yAstre-this.y)+(xAstre-this.x)*(xAstre-this.x));
			//this.dx = dx+vitesse;
			//this.dy = dy+vitesse;
			//}
		this.x=r*Math.cos(c);
		this.y=r*Math.sin(c);
		//this.centreG.x = (centreG.x+this.dx);						//translation des coordonnées du satelite
		//this.centreG.y = (centreG.y+this.dy);
		this.x = x+this.dx;
		this.y = y+this.dy;*/
		//this.drawX=(int) (this.drawX+dx);
		//this.drawY=(int) (this.drawY+dy);
		
		
		//this.drawX=(int) (this.drawX);
		//this.drawY=(int) (this.drawY);
		//transfo.setToIdentity();									//Remise à zéro de la transformation affine
		//transfo.translate(this.centreG.x,this.centreG.y);			//Positionne la hitbox
	
		//angle = Math.atan2(dy, dx)-Math.PI*3/2; 					//Met a jour l'orientation du missile
		//transfo.rotate(angle);		
		
	}
	
	public void draw(long t, Graphics g){
		//AffineTransform at = new AffineTransform();
		//at.rotate(angle);
		//at.translate(-10, -25);
		//AffineTransformOp op = new AffineTransformOp (at, 1);
		Graphics2D g2d =(Graphics2D)g; 													// cast le graphics en graphics2d pour pouvoir appliquer la transformation
																
		g2d.drawImage(images[(int) t % NbImages], op, (int)drawX, (int)drawY);	// dessine l'image
		
		g.setColor(Color.white);
		//g.setFont(f.deriveFont(15.0f));
		//g.drawString(nom_objet, (int)x, (int)(y+30));
		//g.drawString("x=" +(int)x, (int)x, (int)(y-30));
		//g.drawString("y=" +(int)y, (int)x, (int)(y-18));
		
		
		

	}
}
