package framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;


/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {
	
	public static boolean mouseClicked, mousePressed, mouseReleased; // Booléens pour évènement souris
	public static enum ETAT{PREPARATION, SIMULATION, PAUSE, FIN}
	public static ETAT etat;
	Rectangle Ecran; // Les limites de la fenêtre
	final int MASSE_PLANETE = 500;
	
	static Son sonExplosion = new Son("res/sons/explosion-sourde.wav");
	
	Joueur Joueur1, Joueur2;
	Station Station1, Station2; // L'objet que l'utilisateur va déplacer
	AstreSatelite Satelite;
	AstreSpherique Planet1, Planet2;
	Missile Missile1, Missile2, Missile3, Missile4, Missile5;
	ArrayList<Objet> Objets; // Liste de tous les objets du jeu
	ArrayList<Missile> Missiles; // Liste de tous les missiles
	ArrayList<Station> Stations; // Liste de toutes les stations
	int score; // Score du joueur
	Font font1, font2; // Objet de police d'écriture
	String[] NomImage = {"planete.png"};
	String[] imageSat = {"moon.png"};
	//String[] NomImageM = {"missile3_1.png","missile3_2.png","missile3_3.png","missile3_4.png","missile3_5.png","missile3_6.png","missile3_7.png","missile3_8.png","missile3_9.png","missile3_10.png"};
	Trajectoire Trajectoire1, Trajectoire2, Trajectoire3, Trajectoire4, Trajectoire5;
	int compt;
	boolean debutTour, finJeu;
	Station stationCourante, stationGagnante;
	boolean vecteurMissile = false;
	String winner = "";
	String load = "";
	
    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        System.out.println("jeu créé");
        Thread threadForInitGame = new Thread("gameinit") {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
        
        //Initialize();
        //Framework.gameState = Framework.GameState.PLAYING;
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
    	Ecran = new Rectangle(Framework.gauche, Framework.haut, Framework.frameWidth
				- Framework.droite - Framework.gauche, Framework.frameHeight - Framework.bas
				- Framework.haut);
		score = 0;
		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Stations = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		
		Joueur1 = new Joueur("isHuman", "Joueur 1");
		Joueur2 = new Joueur("isHuman","Joueur 2");
		
		Station1 = new Station(200, 100, Ecran,"DeathStar 1", Color.red, Joueur1);
		Station2 = new Station((int) (Ecran.getWidth() - 200), (int) (Ecran.getHeight() - 100), Ecran,"DeathStar 2", Color.blue, Joueur2);
		Objets.add(Station1);
		Objets.add(Station2);
		Stations.add(Station1);
		Stations.add(Station2);
		
		Planet1 = new AstreSpherique(650, 360, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+100, 50);
		Planet2 = new AstreSpherique(900, 800, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		
		Satelite = new AstreSatelite(imageSat, Ecran, "Satelite 1", 1, MASSE_PLANETE/10, 15, Planet1,0.01);
		
		Objets.add(Planet1);
		Objets.add(Planet2);
		Objets.add(Satelite);
		stationGagnante = null;
		
		try { // Récupération de la police d'écriture
			font1 = Font.createFont(Font.TRUETYPE_FONT, new File("res/Coalition.ttf"));
			font2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/13_Misa.ttf"));
		} catch (Exception err) {
			System.err.println("Police(s) d'écriture introuvable(s) !");
		}
		
		etat=ETAT.PREPARATION;
		compt=0;
		mouseClicked = mousePressed = mouseReleased;
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
    
    }    
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
    	
    	score = 0;										//Reinitialisation des variables static
    	Astre.liste= new ArrayList<Astre>();
    	Joueur.Joueurs = new ArrayList<Joueur>();
    	Objet.liste = new ArrayList<Objet> ();
    	Missile.nbr=0;
    	
    	Ecran = new Rectangle(Framework.gauche, Framework.haut, Framework.frameWidth
				- Framework.droite - Framework.gauche, Framework.frameHeight - Framework.bas
				- Framework.haut);
    	
		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Stations = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		
		Joueur1 = new Joueur("isHuman", "Joueur 1");
		Joueur2 = new Joueur("isHuman","Joueur 2");
		
		Station1 = new Station(400, 300, Ecran,"DeathStar 1", Color.red, Joueur1);
		Station2 = new Station((int) (Ecran.getWidth() - 400), (int) (Ecran.getHeight() - 300), Ecran,"DeathStar 2", Color.blue, Joueur2);
		Objets.add(Station1);
		Objets.add(Station2);
		Stations.add(Station1);
		Stations.add(Station2);
		
		Planet1 = new AstreSpherique(650, 360, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		Objets.add(Planet1);
		
		stationGagnante = null;
		
		etat=ETAT.PREPARATION;
		compt=0;
		mouseClicked = mousePressed = mouseReleased;
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
    	int mx = mousePosition.x;
		int my = mousePosition.y;
    	
    	switch(etat){
	    	
    		case PREPARATION :
    			boolean tirCree=false;
	    		System.out.println("Tour de préparation");
	    		
	    		if(tirCree==false){
	    			
	    			for (int i=0; i<Joueur.Joueurs.size(); i++){
	    				Joueur J = Joueur.Joueurs.get(i);
	    				System.out.println(" Joueur : " + J.nomJoueur);
					
	    				for (int j=0; j<J.Stations.size(); j++) {
	    					Station S = J.Stations.get(j);
	    					System.out.println("  Station : " + S.nom_objet);
						
	    					if (S.tirFait==false){
	    						System.out.println("Tir pas fait");
							
								if(mousePressed){
									stationCourante = S;
									vecteurMissile = true;
									mousePressed = false;
								}
								if(mouseReleased){ 
									if(vecteurMissile){		// deuxième condition au cas où le clic était maintenu depuis l'etat SIMULATION
										int x, y;
										double angle=Math.atan2(my-stationCourante.y, mx-stationCourante.x);
										x = (int) (stationCourante.x+(stationCourante.l/2 + 27)*Math.cos(angle));
										y = (int) (stationCourante.y+(stationCourante.h/2 + 27)*Math.sin(angle));
										vecteurMissile = false;
										String nom = "Missile Station n°"+(j+1);
										Missile missile = new Missile(x, y, (float)((mx-S.centreG.x)/100), (float)((my-S.centreG.y)/100), Ecran, nom, S.color, S);
										Objets.add(missile);
										Missiles.add(missile);
										S.tirFait = true;
										System.out.println("Missile créé, tir fait");
										tirCree = true;
									}
									mouseReleased=false;
									
								}
								if (mouseClicked){
									mouseClicked = false;
								}
	    					} else {
	    						System.out.println("Tir déjà fait");
	    					}
	    				}
	    			}
	    		}
	    		
	    		if(Missiles.size() >= Stations.size()){
	    			etat=ETAT.SIMULATION;
	    		}
	    		break;
	    	
	    	case SIMULATION:
	    		
	    		System.out.println("Simulation");
	    		
	    		// MOUVEMENTS
	    		// déplace tous les objets par Polymorphisme
	    		for (int k = 0; k < Objets.size(); k++) {
					Objet O = Objets.get(k);
					O.move(gameTime);
	    		}
	    		
	    		// COLLISIONS
				for(int i=0; i<Missiles.size(); i++){
					Missile O = Missiles.get(i);
					Objet OC = Missiles.get(i).Collision();
					if(OC != O){
						System.out.println("Collision de " +O.nom_objet+ " avec " +OC.nom_objet);
						O.actif = false;
						
						if(O.typeObjet == "Missile"){
							O.explosion.activer(O.x, O.y, gameTime);
							sonExplosion.jouer();
						}
						if(OC.typeObjet == "Station"){
							OC.détruire(OC.centreG.x, OC.centreG.y, gameTime);
						}
					}
				}
				
				/*//ACTUALISATION des EXPLOSIONS
				for(int i=0; i<Explosion.liste.size(); i++){
					Explosion.liste.get(i).actualisation(gameTime);
				}
				*/
				//ACTUALISATION des TRAJECTOIRES
				/*for(int i=0; i<Trajectoires.size(); i++){
					Trajectoires.get(i).actualisation();
				}
				*/
				// GARBAGE COLLECTOR
				
				for (int k = 0; k < Objets.size(); k++) {
					Objet O = (Objet) Objets.get(k);
					if (O.actif == false) {
						Objets.remove(k);
						k--; // parceque la liste s'est déplacée pour boucher le trou
					}
				}
				for (int k = 0; k < Missiles.size(); k++) {
					Missile O = Missiles.get(k);
					if (O.actif == false) {
						Missiles.remove(k);
						k--; // parceque la liste s'est déplacée pour boucher le trou
					}
				}
				for (int k = 0; k < Objet.liste.size(); k++) {
					Objet O = Objet.liste.get(k);
					if (O.actif == false) {
						Objet.liste.remove(k);
						k--; // parceque la liste s'est déplacée pour boucher le trou
					}
				}
				for (int k = 0; k < Stations.size(); k++) {
				Station O = Stations.get(k);
				if (O.actif == false) {
					Stations.remove(k);
					k--; // parceque la liste s'est déplacée pour boucher le trou
					}
				}
				
				if (Missiles.isEmpty()){
					
					if(Joueur1.Stations.isEmpty() || Joueur2.Stations.isEmpty()){
						etat=ETAT.FIN;
						finJeu = true;
						//etat = ETAT.FIN;
						if(!Joueur1.Stations.isEmpty() && Joueur2.Stations.isEmpty()){
							winner = "Joueur 1 gagne !";
							stationGagnante = Joueur1.Stations.get(0);
						}
						if(Joueur1.Stations.isEmpty() && !Joueur2.Stations.isEmpty()){
							winner = "Joueur 2 gagne !";
							stationGagnante = Joueur2.Stations.get(0);
						}
						if(Joueur1.Stations.isEmpty() && Joueur2.Stations.isEmpty())
							winner = "      Egalité !";
					}
					else{
						
						debutTour = true;
						Joueur1.rearme();
						Joueur2.rearme();
						etat = ETAT.PREPARATION;
					}
					
				}
				
	    		break;
	    	
	    	case PAUSE: //ne fait rien (jeu freezé)
	    		break;
	    	
			case FIN:
				
				break;
			
			default:
				break;
	
		}
    	
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(long gameTime, Graphics2D g2d, Point mousePosition)
    {
    	int mx = mousePosition.x;
		int my = mousePosition.y;
    	
    	//g2d.setColor(Color.BLACK);
		//g2d.fillRect((int)Ecran.getX(), (int) Ecran.getY(), (int) (Ecran.getX() + Ecran.getWidth()), (int) (Ecran.getY() + Ecran.getHeight()));
		//g2d.setColor(Color.white);
		
		// dessine TOUS les objets dans le buffer
		for (int k = 0; k < Objets.size(); k++) {
			Objet O = Objets.get(k);
			O.draw(gameTime, g2d, font2);
		}
		
		// dessine les explosions dans le buffer
		for(int i=0; i<Explosion.liste.size(); i++){
			Explosion.liste.get(i).draw(gameTime, g2d);
		}
		
		switch(etat){
			case PREPARATION :
				g2d.setColor(Color.white);
				g2d.setFont(font1.deriveFont(25.0f));
				g2d.drawString("Phase de préparation", 425, 50);
				g2d.setFont(font1.deriveFont(20.0f));
				g2d.drawString("Joueur 1", 575, 100);
				int compt = 0;
				for (int k = 0; k < Stations.size(); k++) {
					Station O = Stations.get(k);
					g2d.setColor(O.color);
					if (O.tirFait == false && compt == 0) {
						g2d.drawString("Station n° "+(k+1), 550, 125);
						compt++;
					}
				}
				if(vecteurMissile){
					g2d.setColor(Color.white);
					g2d.drawLine((int)(stationCourante.x), (int)(stationCourante.y), (int)(mx), (int)(my));
				}
				break;
			
			case SIMULATION :
				g2d.setColor(Color.white);
				g2d.setFont(font1.deriveFont(25.0f));
				switch ( (int)(gameTime) % 100){
					case 0 : load = "."; break;
					case 20 : load = ". ."; break;
					case 40 : load = ". . ."; break;
					case 60 : load = " "; break;
				}
				g2d.drawString("Phase de jeu "+load, 500, 100);
				break;
			case FIN :
				// Message de fin de jeu
				g2d.setColor(Color.white);
				g2d.setFont(font1.deriveFont(100.0f));
				g2d.drawString("GAME OVER", 250, (int) Ecran.getHeight() / 2 - 100);
				g2d.setFont(font1.deriveFont(50.0f));
				if(stationGagnante != null)
						g2d.setColor(stationGagnante.color);
				g2d.drawString(winner, 350, (int) Ecran.getHeight() / 2 + 150);
				break;
		}
    }
}
