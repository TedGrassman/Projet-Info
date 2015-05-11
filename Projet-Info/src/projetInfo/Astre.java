package projetInfo;


import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public abstract class Astre extends Objet{
	
	public static ArrayList<Astre> liste = new ArrayList<Astre> ();
	AffineTransform transfo = new AffineTransform();
	
	public Astre(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type, int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, type, nbIm, masse);
		liste.add(this);
	}

	public boolean Collision(Missile missile) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
