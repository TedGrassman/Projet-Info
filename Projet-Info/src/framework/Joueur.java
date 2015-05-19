package framework;

import java.util.ArrayList;

public class Joueur {
	
	public static enum typeJoueur {isHuman,isComputer};
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
}
