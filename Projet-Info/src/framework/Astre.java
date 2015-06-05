package framework;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public abstract class Astre extends Objet { // Classe abstraite dont h�ritent
											// toutes les formes particuli�res
											// d'astre ainsi que les missiles

	public static ArrayList<Astre> liste = new ArrayList<Astre>(); // Liste de
																	// tous les
																	// Atres
																	// pour
																	// effectuer
																	// les
																	// op�rations
	AffineTransform transfo = new AffineTransform(); // AffineTransform "vide"
														// pour pouvoir cr�er
														// une m�thode Collision
														// travaillant avec tout
														// type d'astre

	public Astre(int ax, int ay, float adx, float ady, String[] NomImage,
			Rectangle aframe, String nom, String type, int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, type, nbIm, masse);
		liste.add(this); // Ajoute l'astre nouvellement cr�� � la liste d'astres
	}

}
