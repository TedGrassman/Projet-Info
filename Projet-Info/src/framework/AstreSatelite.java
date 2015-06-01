package framework;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.Rectangle;



public class AstreSatelite  extends Astre  {
	double rayon;	//Rayon de l'astre
	AstreSpherique astre;  //astre sphérique autour duquel il tourne
	double omega;
	
	public AstreSatelite (String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse, double rayon, AstreSpherique astre, double omega) {
		super((int)(astre.x+100),(int)(astre.y), 0f, 0f, NomImage, aframe, nom, "AstreSpherique", nbIm, masse);
		this.rayon=rayon;
		limites = new Area(new Ellipse2D.Double(drawX, drawY, 2*(rayon),2*(rayon)));	//Création de la hitbox : disque de centre (ax,ay)
		this.astre=astre;
		centreG = new CentreGravite((int)(astre.x+rayon), (int)(astre.y));
		this.omega = 0.034 ;
	}

	public void move(long t) {
		
		double xAstre;
		double yAstre;
		xAstre = astre.centreG.x;
		yAstre = astre.centreG.y;
		
		this.x=xAstre + 100*Math.cos(omega*currentFrameNumber);
		this.y=yAstre + 100*Math.sin(omega*currentFrameNumber);
		drawX = (int)(this.x - rayon);
		drawY = (int)(this.y - rayon);
		this.centreG.x = x;
		this.centreG.y = y;
		currentFrameNumber++;
		limites = new Area(new Ellipse2D.Double(drawX, drawY, (2*rayon),(2*rayon)));
		
			
		
	}
	
	public void draw(long t, Graphics g, Font f){
	
		Graphics2D g2d =(Graphics2D)g; 													// cast le graphics en graphics2d pour pouvoir appliquer la transformation
																
		g2d.drawImage(images[(int) t % NbImages], null, (int)drawX, (int)drawY);	// dessine l'image
		

		
		
		

	}
}
