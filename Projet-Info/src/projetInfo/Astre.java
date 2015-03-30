package projetInfo;

import javafx.scene.shape.Rectangle;
<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.util.LinkedList;
>>>>>>> branch 'master' of https://github.com/TedGrassman/Projet-Info.git

public abstract class Astre extends Objet{
	
	static ArrayList<Astre> liste = new ArrayList<Astre> ();
	
public Astre(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, nbIm, masse);
		liste.add(this);
	}


}
