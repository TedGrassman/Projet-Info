package projetInfo;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class Astre extends Objet{
	
	public static ArrayList<Astre> liste = new ArrayList<Astre> ();
	
	public Astre(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, nbIm, masse);
		liste.add(this);
	}

}
