package gameEntities;

import java.awt.Rectangle;

public abstract class Astre extends Objet { // Classe abstraite dont h�ritent toutes les formes particuli�res
											// d'astres

	public Astre(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type,
			int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, type, nbIm, masse);
	}

}
