package framework;

import java.awt.Color;
import java.util.ArrayList;

public class Joueur {

	ArrayList<Station> Stations;
	String nomJoueur;
	Color color;

	public Joueur(String nomJoueur, Color couleur) {
		this.nomJoueur = nomJoueur;
		Stations = new ArrayList<Station>();
		color = couleur;
	}

	public void reinitStations() {
		Stations = new ArrayList<Station>();
	}
}
