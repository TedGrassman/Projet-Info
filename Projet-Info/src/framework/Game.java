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
	
	Joueur Joueur1, Joueur2;
	Station Station1, Station2; // L'objet que l'utilisateur va déplacer
	AstreSpherique Planet1;
	Missile Missile1, Missile2, Missile3, Missile4, Missile5;
	ArrayList<Objet> Objets; // Liste de tous les objets du jeu
	ArrayList<Missile> Missiles; // Liste de tous les missiles
	ArrayList<Trajectoire> Trajectoires; // Liste de toutes les trajectoires
	ArrayList<Station> Stations; // Liste de toutes les stations
	int score; // Score du joueur
	Font font1, font2; // Objet de police d'écriture
	String[] NomImage = {"planete.png"};
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
		Trajectoires = new ArrayList<Trajectoire>(); // Créer la liste chainée en mémoire
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
		
//		Missile1 = new Missile(650, 600, 2.5f, 1f, Ecran, "Missile1", Color.RED);
//		Missile2 = new Missile(700, 700, 1.5f, -1.5f, Ecran, "Missile2", Color.GREEN);
//		Missile3 = new Missile(100, 300, 1f, 2f, Ecran, "Missile3", Color.BLUE);
//		Missile4 = new Missile(300, 650, -0.5f, -1f, Ecran, "Missile4", Color.YELLOW);
//		Missile5 = new Missile(1100, 550, -1.5f, 2f, Ecran, "Missile5", Color.WHITE);
//		
//		Objets.add(Missile1);
//		Objets.add(Missile2);
//		Objets.add(Missile3);
//		Objets.add(Missile4);
//		Objets.add(Missile5);
//		
//		Missiles.add(Missile1);
//		Missiles.add(Missile2);
//		Missiles.add(Missile3);
//		Missiles.add(Missile4);
//		Missiles.add(Missile5);
		
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
    	etat=ETAT.PREPARATION;
    	score = 0;
    	Astre.liste= new ArrayList<Astre>();
		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Trajectoires = new ArrayList<Trajectoire>(); // Créer la liste chainée en mémoire
		Stations = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		
		Planet1 = new AstreSpherique(650, 360, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		Objets.add(Planet1);
		
		Joueur1 = new Joueur("isHuman", "Joueur 1");
		Joueur2 = new Joueur("isHuman","Joueur 2");
		
		Station1 = new Station(100, 100, Ecran,"DeathStar 1", Color.RED, Joueur1);
		Station2 = new Station((int) (Ecran.getWidth() - 100), (int) (Ecran.getHeight() - 100), Ecran,"DeathStar 2", Color.BLUE, Joueur2);
		Objets.add(Station1);
		Objets.add(Station2);
		Stations.add(Station1);
		Stations.add(Station2);
		
		stationGagnante = null;

//		Missiles.add(Missile1);
//		Missiles.add(Missile2);
//		Missiles.add(Missile3);
//		Missiles.add(Missile4);
//		Missiles.add(Missile5);
		
		
		compt=0;
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
									int x, y;
									double angle=Math.atan2(my-stationCourante.y, mx-stationCourante.x);
									x = (int) (stationCourante.x+(stationCourante.l/2 + 25)*Math.cos(angle));
									y = (int) (stationCourante.y+(stationCourante.h/2 + 25)*Math.sin(angle));
									vecteurMissile = false;
									String nom = "Missile Station n°"+(j+1);
									Missile missile = new Missile(x, y, (float)((mx-S.centreG.x)/100), (float)((my-S.centreG.y)/100), Ecran, nom, S.color, S);
									Objets.add(missile);
									Missiles.add(missile);
									S.tirFait = true;
									System.out.println("Missile créé, tir fait");
									tirCree = true;
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
	    		} else {
					tirCree=false;
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
				for(int i=0; i<Objets.size(); i++){
					Objet O = Objets.get(i);
					Objet OC = Objets.get(i).Collision();
					if(OC != O){
						System.out.println("Collision de " +O.nom_objet+ " avec " +OC.nom_objet);
						O.actif = false;
						if(OC.typeObjet != "AstreSpherique")
							OC.actif = false;
						if(O.typeObjet == "Missile"){
							O.explosion.activer(O.x, O.y, gameTime);
						}
						if(OC.typeObjet == "Station"){
							OC.explosion.activer(OC.x, OC.y, gameTime);
						}
					}
				}
				
				//ACTUALISATION des EXPLOSIONS
				for(int i=0; i<Explosion.liste.size(); i++){
					Explosion.liste.get(i).actualisation(gameTime);
				}
				
				//ACTUALISATION des TRAJECTOIRES
				for(int i=0; i<Trajectoires.size(); i++){
					Trajectoires.get(i).actualisation();
				}
				
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
				
//				if (Missiles.isEmpty()){
//					etat = ETAT.PREPARATION;
//					if(Stations1.isEmpty() || Stations2.isEmpty()){
//						debutTour = true;
//						finJeu = true;
//						//etat = ETAT.FIN;
//						if(!Stations1.isEmpty() && Stations2.isEmpty())
//							winner = "Joueur 1 gagne !";
//							stationGagnante = Stations1.get(0);
//						if(Stations1.isEmpty() && !Stations2.isEmpty())
//							winner = "Joueur 2 gagne !";
//							stationGagnante = Stations2.get(0);
//						if(Stations1.isEmpty() && Stations2.isEmpty())
//							winner = "      Egalité !";
//					}
//				}
				
	    		break;
	    	
	    	case PAUSE:
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
