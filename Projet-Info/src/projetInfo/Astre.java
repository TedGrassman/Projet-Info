package projetInfo;

import javafx.scene.shape.Rectangle;
import java.util.LinkedList;

public abstract class Astre extends Objet{
	
	static LinkedList<Astre> liste = new LinkedList<Astre> ();
	
public Astre(int ax, int ay, float adx, float ady, float avitesse,
			String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse) {
		super(ax, ay, adx, ady, avitesse, NomImage, aframe, nom, nbIm, masse);
		liste.add(this);
	}


}
