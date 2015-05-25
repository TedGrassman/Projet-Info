package framework;


import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public abstract class Astre extends Objet{							//Classe abstraite dont héritent toutes les formes particulières d'astre ainsi que les missiles
	
	public static ArrayList<Astre> liste = new ArrayList<Astre> ();	//Liste de tous les Atres pour effectuer les opérations
	AffineTransform transfo = new AffineTransform();				//AffineTransform "vide" pour pouvoir créer une méthode Collision travaillant avec tout type d'astre
	
	public Astre(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type, int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, type, nbIm, masse, prefixeExplosion);
		liste.add(this);							//Ajoute l'astre nouvellement créé à la liste d'astres
	}

}
