package gameEntities;

import java.awt.Color;
import java.util.ArrayList;

public class Joueur {

	public ArrayList<Station> Stations;
	public String nomJoueur;
	public Color color;

	public Joueur(String nomJoueur, Color couleur) {
		this.nomJoueur = nomJoueur;
		Stations = new ArrayList<Station>();
		color = couleur;
	}

	public void reinitStations() {
		Stations = new ArrayList<Station>();
	}
}
