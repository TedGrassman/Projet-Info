package projetInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.Rectangle;



public class AstreSpherique extends Astre {
	
	//Circle limites;
	double rayon;

	public AstreSpherique(int ax, int ay, float adx, float ady,	String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse, double rayon) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "AstreSpherique", nbIm, masse);
		this.rayon=rayon;
		limites = new Area(new Ellipse2D.Double(drawX, drawY, (2*rayon),(2*rayon)));
		//centreG = new CentreGravite(ax, ay);
	}

	@Override
	public void move(long t) {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(long t, Graphics g){
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(images[(int) t % NbImages], drawX, drawY, null);
		/*AffineTransform at = new AffineTransform();
		GeneralPath path1 = new GeneralPath();
        path1.append(limites.getPathIterator(at), true);
        g2d.fill(path1);
*/
		/*
		g.setColor(Color.white);
		g.drawOval(drawX, drawY, l, h);
		g.setColor(Color.red);
		g.drawOval((int)(x-rayon), (int)(y-rayon), (int)(2*rayon), (int)(2*rayon));
		*/
	}
	
	
	

}
