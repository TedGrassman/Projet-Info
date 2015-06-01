package framework;

import java.util.ArrayList;

public class Joueur {
	
	String typeJoueur; // "isHuman" : joueur humain ; "isComputer" : IA
	ArrayList <Station> Stations;
	static ArrayList <Joueur> Joueurs = new ArrayList<Joueur>();
	String nomJoueur;
	
	public Joueur(String typeJoueur, String nomJoueur) {
		this.typeJoueur = typeJoueur;
		this.nomJoueur = nomJoueur;
		Stations = new ArrayList<Station>();
		Joueurs.add(this);
	}
	
	public void rearme(){
		for(int i=0; i<Stations.size(); i++){
			Stations.get(i).tirFait=false;
		}
	}
}
