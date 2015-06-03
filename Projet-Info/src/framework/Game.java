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
	final Color[] coulJoueurs = {Color.RED, Color.CYAN, Color.GREEN, Color.YELLOW};
	
	Son sonExplosion = new Son("res/sons/explosion-sourde.wav");
	
	Joueur Joueur1, Joueur2, Joueur3, Joueur4;
	Joueur[] creJoueurs = {Joueur1, Joueur2, Joueur3, Joueur4};
	Station Station1, Station2,Station3,Station4; // L'objet que l'utilisateur va déplacer
	AstreSatelite Satelite1, Satelite2, Satelite3, Satelite4;
	AstreSpherique Planet1, Planet2, Planet3, Planet4, Planet5;
	AstreTrouNoir TrouNoir1;
	Missile Missile1, Missile2, Missile3, Missile4, Missile5;
	ArrayList<Objet> Objets; // Liste de tous les objets du jeu
	ArrayList<Missile> Missiles; // Liste de tous les missiles
	ArrayList<Station> Stations; // Liste de toutes les stations
	Font font1, font2; // Objet de police d'écriture
	String[] NomImage = {"planete.png"};
	String[] imageSat = {"moon.png"};
	String[] imageTrou = {"trouNoir.png"};
	int compt;
	boolean debutTour;
	Station stationCourante;
	boolean vecteurMissile = false;
	String winner = "";
	String load = "";
	int CentreEcranX;
	int CentreEcranY;
	double pH;
	double pW;
	double height;
	double width;
	
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
        
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
    	Ecran = new Rectangle(Framework.gauche, Framework.haut, Framework.frameWidth
				- Framework.droite - Framework.gauche, Framework.frameHeight - Framework.bas
				- Framework.haut);
		CentreEcranX = (int)(Ecran.getWidth()/2);
		CentreEcranY = (int)(Ecran.getHeight()/2);
		width = Ecran.getWidth();
		height = Ecran.getHeight();		
		pH=height/768;		
		pW=(width/1366);
		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Stations = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		
		
		
		DisposeAstres(2);
		
		
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
    	//Reinitialisation des variables static
    	Astre.liste= new ArrayList<Astre>();
    	Joueur.Joueurs = new ArrayList<Joueur>();
    	Objet.liste = new ArrayList<Objet> ();
    	Missile.nbr=0;
    	creJoueurs[0]=Joueur1;
    	creJoueurs[1]=Joueur2;
    	creJoueurs[2]=Joueur3;
    	creJoueurs[3]=Joueur4;
    	
    	Ecran = new Rectangle(Framework.gauche, Framework.haut, Framework.frameWidth
				- Framework.droite - Framework.gauche, Framework.frameHeight - Framework.bas
				- Framework.haut);
    	
		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Stations = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		
		
		DisposeAstres(2);
		
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
										Missile missile = new Missile(x, y, (float)((mx-S.centreG.x)/100), (float)((my-S.centreG.y)/100), Ecran, nom, S.joueur.color, S);
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
						
						
						if(O.typeObjet == "Missile"){
							O.explosion.activer(O.x, O.y, gameTime);
							sonExplosion.jouer();
						}
						if(OC.typeObjet == "Station"){
							OC.détruire(OC.centreG.x, OC.centreG.y, gameTime);
						}
						O.actif = false;
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
					for(int i=0;i<Joueur.Joueurs.size();i++){
						if(Joueur.Joueurs.get(i).Stations.isEmpty()){
							Joueur.Joueurs.remove(i);
							i--;
						}
					}
					if(Joueur.Joueurs.size()==1){
						winner="Le Vainqueur est " +Joueur.Joueurs.get(0).nomJoueur;
						etat=ETAT.FIN;
					}
							
					if(Joueur.Joueurs.size()==0){
						winner = "Egalité !";
						etat=ETAT.FIN;
					}	
					if(Joueur.Joueurs.size()>1){
						
						debutTour = true;
						for(int i=0;i<Joueur.Joueurs.size();i++){
								Joueur.Joueurs.get(i).rearme();
						}
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
				g2d.setFont(font1.deriveFont((float) (25*pW)));
				printCenteredString(g2d,"Phase de préparation", (int)(100*pH));
				g2d.setFont(font1.deriveFont(20.0f));
				int compt = 0;
				for (int k = 0; k < Stations.size(); k++) {
					Station O = Stations.get(k);
					g2d.setColor(O.joueur.color);
					if (O.tirFait == false && compt == 0) {
						printCenteredString(g2d, O.joueur.nomJoueur,(int) (130*pH));
						printCenteredString(g2d, "Station n° "+ O.numero,(int) (150*pH));
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
				g2d.setFont(font1.deriveFont((float) (25*pW)));
				switch ( (int)(gameTime/Framework.secInNanosec) % 4){
					case 0 : load = "."; break;
					case 1 : load = ". ."; break;
					case 2 : load = ". . ."; break;
					case 3 : load = " "; break;
				}
				printCenteredString(g2d, "Phase de jeu "+load,  (int)(100*pH));
				break;
			case FIN :
				// Message de fin de jeu
				g2d.setColor(Color.white);
				g2d.setFont(font1.deriveFont((float) (100*pW)));
				printCenteredString(g2d, "GAME OVER",  (int) (CentreEcranY - (100*pH)));
				g2d.setFont(font1.deriveFont(50.0f));
				if(!Joueur.Joueurs.isEmpty())	g2d.setColor(Joueur.Joueurs.get(0).color);
				//g2d.drawString(winner, (int) (CentreEcranX-(200*pW)), ((int) (CentreEcranY + (70*pH))));
				printCenteredString(g2d, winner, (int) (CentreEcranY + (70*pH)));
				break;
		}
    }
    
    private void printCenteredString(Graphics2D g2d, String s,  int YPos){
        int stringLen = (int)
            g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        int start = Framework.frameWidth/2 - stringLen/2;
        g2d.drawString(s, start , YPos);
 }
    private void DisposeAstres (int map){
    	switch(map){
    		case 1 :
    			Joueur1 = new Joueur("Joueur 1", Color.red);
    			Joueur2 = new Joueur("Joueur 2", Color.blue);
    			Station1 = new Station((int)(CentreEcranX-(341*pW)),(int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 1", Joueur1);
    			Station2 = new Station((int)(CentreEcranX+(341*pW)), (int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 2", Joueur2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
			
    			Planet1 = new AstreSpherique(CentreEcranX, CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+100, 50);
    			Planet2 = new AstreSpherique(900, 800, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		
    			Satelite1 = new AstreSatelite(imageSat, Ecran, "Satelite 1", 1, MASSE_PLANETE/10, 15, Planet1,0.01);
		
    			Objets.add(Planet1);
    			Objets.add(Planet2);
    			Objets.add(Satelite1);
    			break;
    		case 2 :
    			for(int i=0; i<Framework.nbJoueurs; i++){
    				creJoueurs[i]= new Joueur ("Joueur"+(i+1), coulJoueurs[i]);
    			}
    			Station1 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY-(256*pH)), Ecran,"DeathStar 1",  creJoueurs[0]);
    			Station2 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY-(256*pH)), Ecran,"DeathStar 2", creJoueurs[1]);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
    			if(creJoueurs[2] != null){
    				Station3 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 3", creJoueurs[2]);
    				Objets.add(Station3);
    				Stations.add(Station3);
    			}
    			else{
    				Station3 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 3", creJoueurs[1]);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Station4 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 4", creJoueurs[0]);
    				Objets.add(Station4);
    				Stations.add(Station4);
    				
    			}
    			if(creJoueurs[3]!=null){
    				Station4 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 4", creJoueurs[3]);
    				Objets.add(Station4);
    				Stations.add(Station4);
    			}
    			

			
    			Planet1 = new AstreSpherique((int)(CentreEcranX-(600*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet2 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY-(171*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet3 = new AstreSpherique((int)(CentreEcranX+(600*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet4 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY+(171*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet5 = new AstreSpherique(CentreEcranX, CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Satelite1 = new AstreSatelite(imageSat, Ecran, "Satelite 1", 1, MASSE_PLANETE/10, 15, Planet1,0.01);
    			Satelite2 = new AstreSatelite(imageSat, Ecran, "Satelite 2", 1, MASSE_PLANETE/10, 15, Planet2,0.01);
    			Satelite3= new AstreSatelite(imageSat, Ecran, "Satelite 3", 1, MASSE_PLANETE/10, 15, Planet3,0.01);
    			Satelite4 = new AstreSatelite(imageSat, Ecran, "Satelite 4", 1, MASSE_PLANETE/10, 15, Planet4,0.01);
    			TrouNoir1 = new AstreTrouNoir((int)(CentreEcranX - 300), CentreEcranY, 0f, 0f, imageTrou, Ecran, "Planete1", 1, 8*MASSE_PLANETE, 50);
    			Objets.add(Planet1);
    			Objets.add(Planet2);
    			Objets.add(Planet3);
    			Objets.add(Planet4);
    			Objets.add(Planet5);
    			Objets.add(TrouNoir1);
    			Objets.add(Satelite1);
    			Objets.add(Satelite2);
    			Objets.add(Satelite3);
    			Objets.add(Satelite4);
    			break;
    		case 3 :
    			Joueur1 = new Joueur( "Joueur 1", Color.red);
    			Joueur2 = new Joueur("Joueur 2", Color.blue);
    			Station1 = new Station((int)(CentreEcranX-(341*pW)),(int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 1", Joueur1);
    			Station2 = new Station((int)(CentreEcranX+(341*pW)), (int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 2", Joueur2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
			
    			Planet1 = new AstreSpherique(CentreEcranX, CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+100, 50);
    			Planet2 = new AstreSpherique(900, 800, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		
    			Satelite1 = new AstreSatelite(imageSat, Ecran, "Satelite 1", 1, MASSE_PLANETE/10, 15, Planet1,0.01);
		
    			Objets.add(Planet1);
    			Objets.add(Planet2);
    			Objets.add(Satelite1);
    			break;
    		case 4 :
    			Joueur1 = new Joueur("Joueur 1", Color.red);
    			Joueur2 = new Joueur("Joueur 2", Color.blue);
    			Station1 = new Station((int)(CentreEcranX-(341*pW)),(int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 1", Joueur1);
    			Station2 = new Station((int)(CentreEcranX+(341*pW)), (int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 2", Joueur2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
    			Planet1 = new AstreSpherique(CentreEcranX, CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+100, 50);
    			Planet2 = new AstreSpherique(900, 800, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		
    			Satelite1 = new AstreSatelite(imageSat, Ecran, "Satelite 1", 1, MASSE_PLANETE/10, 15, Planet1,0.01);
		
    			Objets.add(Planet1);
    			Objets.add(Planet2);
    			Objets.add(Satelite1);
    			break;
    		case 0 :
    			Joueur1 = new Joueur("Joueur 1", Color.red);
    			Joueur2 = new Joueur("Joueur 2", Color.blue);
    			Station1 = new Station((int)(CentreEcranX-(341*pW)),(int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 1", Joueur1);
    			Station2 = new Station((int)(CentreEcranX+(341*pW)), (int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 2", Joueur2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);		
    			break;
    	}
		
    }
}
