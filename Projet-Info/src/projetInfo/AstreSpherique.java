package projetInfo;

import java.awt.Color;
import java.awt.Graphics;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

public class AstreSpherique extends Astre {
	
	Circle limites;
	double rayon;

	public AstreSpherique(int ax, int ay, float adx, float ady,	String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse, double rayon) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, nbIm, masse);
		this.rayon = rayon;
		limites = new Circle(ax, ay, rayon);
		//centreG = new CentreGravite(ax, ay);
	}

	@Override
	public void move(long t) {
		// TODO Auto-generated method stub
		
	}
	
//	public void draw(long t, Graphics g){
//		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
//		g.setColor(Color.white);
//		g.drawOval(drawX, drawY, l, h);
//		g.setColor(Color.red);
//		g.drawOval((int)x, (int)y, 10, 10);
//	}
	
	
	

}
