package projetInfo;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class Astre extends Objet{
	
	public static ArrayList<Astre> liste = new ArrayList<Astre> ();
	
	public Astre(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type, int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, type, nbIm, masse);
		liste.add(this);
	}
	
	public Astre Collision(){
		for (int i=0; i<liste.size(); i++){
			
			if (liste.get(i).Collision(this)){
				return liste.get(i);
			}
		}
		return this;
	}
}
