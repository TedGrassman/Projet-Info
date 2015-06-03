package framework;

import java.awt.Color;
import java.util.ArrayList;

public class Joueur {
	
	ArrayList <Station> Stations;
	static ArrayList <Joueur> Joueurs = new ArrayList<Joueur>();
	String nomJoueur;
	Color color;
	
	public Joueur(String nomJoueur, Color couleur) {
		this.nomJoueur = nomJoueur;
		Stations = new ArrayList<Station>();
		Joueurs.add(this);
		color=couleur;
	}
	
	public void rearme(){
		for(int i=0; i<Stations.size(); i++){
			Stations.get(i).tirFait=false;
		}
	}
}
