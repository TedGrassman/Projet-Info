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
	
	Son sonExplosion = new Son("res/sons/explosion-sourde.wav");
	
	final static Joueur Joueur1 = new Joueur("Joueur 1", Color.RED), Joueur2 = new Joueur("Joueur 2", Color.CYAN), Joueur3 = new Joueur("Joueur 3", Color.GREEN), Joueur4 = new Joueur("Joueur 4", Color.YELLOW);
	Station stationCourante;
	Station Station1, Station2,Station3,Station4; // L'objet que l'utilisateur va déplacer
	AstreSatelite Satelite1, Satelite2, Satelite3, Satelite4, Satelite5, Satelite6, Satelite7, Satelite8;
	AstreSpherique Planet1, Planet2, Planet3, Planet4, Planet5,Planet6,Planet7,Planet8, Planet9;
	AstreTrouNoir TrouNoir1, TrouNoir2;
	Missile Missile1, Missile2, Missile3, Missile4, Missile5;
	ArrayList<Objet> Objets; // Liste de tous les objets du jeu
	ArrayList<Missile> Missiles; // Liste de tous les missiles
	ArrayList<Station> Stations; // Liste de toutes les stations
	ArrayList <Joueur> Joueurs;  // Liste des joueurs en lice
	Font font1, font2; // Objet de police d'écriture
	String[] NomImage = {"planete.png"};
	String[] imageSat = {"moon.png"};
	String[] imageTrou = {"trouNoir.png"};
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
		Objets = new ArrayList<Objet>(); // Créer les listes en mémoire
		Missiles = new ArrayList<Missile>(); 
		Stations = new ArrayList<Station>(); 
		Joueurs = new ArrayList<Joueur>();  
		
		
		DisposeAstres(Framework.niveauChoisi);
		stationCourante = Stations.get(0);
		
		try { // Récupération de la police d'écriture
			font1 = Font.createFont(Font.TRUETYPE_FONT, new File("res/Coalition.ttf"));
			font2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/13_Misa.ttf"));
		} catch (Exception err) {
			System.err.println("Police(s) d'écriture introuvable(s) !");
		}
		
		etat=ETAT.PREPARATION;
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

    	Objet.liste = new ArrayList<Objet> ();
    	Astre.liste= new ArrayList<Astre>();
    	Explosion.liste= new ArrayList<Explosion>();
    	Missile.nbr=0;
    	Joueur1.reinitStations();
    	Joueur2.reinitStations();
    	Joueur3.reinitStations();
    	Joueur4.reinitStations();
    	
    	Ecran = new Rectangle(Framework.gauche, Framework.haut, Framework.frameWidth
				- Framework.droite - Framework.gauche, Framework.frameHeight - Framework.bas
				- Framework.haut);
    	
		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Stations = new ArrayList<Station>(); // Créer la liste chainée en mémoire
    	Joueurs = new ArrayList<Joueur>();
		
		DisposeAstres(Framework.niveauChoisi);
		stationCourante = Stations.get(0);
		
		etat=ETAT.PREPARATION;
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
    			if(mousePressed){
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
						String nom = "Missile Station n°"+(Stations.indexOf(stationCourante)+1);
						Missile missile = new Missile(x, y, (float)((mx-stationCourante.centreG.x)/100), (float)((my-stationCourante.centreG.y)/100), Ecran, nom, stationCourante.joueur.color, stationCourante);
						Objets.add(missile);
						Missiles.add(missile);
						stationCourante = Stations.get((Stations.indexOf(stationCourante)+1)%Stations.size());
					}
					mouseReleased=false;
    			}
    			
    			if (mouseClicked){
					mouseClicked = false;
				}
    			
	    		if(Missiles.size() >= Stations.size()){
	    			stationCourante = Stations.get(0);
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
						//System.out.println("Collision de " +O.nom_objet+ " avec " +OC.nom_objet);
							O.détruire(O.x, O.y, gameTime);
							sonExplosion.jouer();
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
					for(int i=0;i<Joueurs.size();i++){
						if(Joueurs.get(i).Stations.isEmpty()){
							Joueurs.remove(i);
							i--;
						}
					}
					if(Joueurs.size()==1){
						winner="Le Vainqueur est " +Joueurs.get(0).nomJoueur;
						etat=ETAT.FIN;
					}
							
					if(Joueurs.size()==0){
						winner = "Egalité !";
						etat=ETAT.FIN;
					}	
					if(Joueurs.size()>1){
						stationCourante = Stations.get(0);
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
				
				g2d.setColor(stationCourante.joueur.color);
				
				printCenteredString(g2d, stationCourante.joueur.nomJoueur,(int) (130*pH));
				printCenteredString(g2d, "Station n° "+ stationCourante.numero,(int) (150*pH));
				if(vecteurMissile){
					g2d.setColor(Color.white);
					g2d.drawLine((int)(stationCourante.centreG.x), (int)(stationCourante.centreG.y), (int)(mx), (int)(my));
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
				if(!Joueurs.isEmpty())	g2d.setColor(Joueurs.get(0).color);
				printCenteredString(g2d, winner, (int) (CentreEcranY + (70*pH)));
				break;
			case PAUSE:
				break;
			default:
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
    		case 0 :

    			Station1 = new Station((int)(CentreEcranX-(341*pW)),(int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 1", 1);
    			Station2 = new Station((int)(CentreEcranX+(341*pW)), (int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 2", 2);
    			Station3 = new Station((int)(CentreEcranX-(341*pW)),(int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 1", 1);
    			Station4 = new Station((int)(CentreEcranX+(341*pW)), (int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 2", 2);
				Objets.add(Station1);
				Objets.add(Station2);
				Objets.add(Station3);
				Objets.add(Station4);
				Stations.add(Station1);
				Stations.add(Station2);	
				Stations.add(Station3);
				Stations.add(Station4);		
				Joueurs.add(Joueur1);
				Joueurs.add(Joueur2);
				break;
    		case 1 :
    			
    			Station1 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY-(256*pH)), Ecran,"DeathStar 1", 1);
    			Station2 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY-(256*pH)), Ecran,"DeathStar 2",2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
    			Joueurs.add(Joueur1);
    			Joueurs.add(Joueur2);
    			if(Framework.nbJoueurs>2){
    				Station3 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 3", 3);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Joueurs.add(Joueur3);
    				if(Framework.nbJoueurs>3){
        				Station4 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 4", 4);
        				Objets.add(Station4);
        				Stations.add(Station4);
        				Joueurs.add(Joueur4);
        			}
    			}
    			else{
    				Station3 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 3", 1);
    				Station4 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 4", 2);
    				Objets.add(Station3);
    				Objets.add(Station4);
    				Stations.add(Station3);
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
    			Objets.add(Planet1);
    			Objets.add(Planet2);
    			Objets.add(Planet3);
    			Objets.add(Planet4);
    			Objets.add(Planet5);
    			Objets.add(Satelite1);
    			Objets.add(Satelite2);
    			Objets.add(Satelite3);
    			Objets.add(Satelite4);
    			break;
    		
    		case 25 :
    			Station1 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY-(256*pH)), Ecran,"DeathStar 1",  1);
    			Station2 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY-(256*pH)), Ecran,"DeathStar 2", 2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
    			Joueurs.add(Joueur1);
    			Joueurs.add(Joueur2);
    			if(Framework.nbJoueurs>2){
    				Station3 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 3", 3);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Joueurs.add(Joueur3);
    				if(Framework.nbJoueurs>3){
        				Station4 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 4", 4);
        				Objets.add(Station4);
        				Stations.add(Station4);
        				Joueurs.add(Joueur4);
        			}
    			}
    			else{
    				Station3 = new Station((int)(CentreEcranX-(455*pW)),(int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 3", 1);
    				Station4 = new Station((int)(CentreEcranX+(455*pW)), (int) (CentreEcranY+(256*pH)), Ecran,"DeathStar 4", 2);
    				Objets.add(Station3);
    				Objets.add(Station4);
    				Stations.add(Station3);
    				Stations.add(Station4);
    				
    			}
    			System.out.println(Station1.joueur.nomJoueur);
    			System.out.println(Station1.numero);
    			System.out.println(Station2.joueur.nomJoueur);
    			System.out.println(Station2.numero);
    			System.out.println(Station3.joueur.nomJoueur);
    			System.out.println(Station3.numero);
    			System.out.println(Station4.joueur.nomJoueur);
    			System.out.println(Station4.numero);
			
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
    		
    		case 2 :
    			Station1 = new Station((int)(CentreEcranX-(250*pW)),(int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 1",  1);
    			Station2 = new Station((int)(CentreEcranX+(250*pW)), (int) (CentreEcranY-(192*pH)), Ecran,"DeathStar 2", 2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
    			Joueurs.add(Joueur1);
    			Joueurs.add(Joueur2);
    			if(Framework.nbJoueurs>2){
    				Station3 = new Station((int)(CentreEcranX-(250*pW)),(int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 3", 3);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Joueurs.add(Joueur3);
    				if(Framework.nbJoueurs>3){
    					Station4 = new Station((int)(CentreEcranX+(250*pW)), (int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 4", 4);
        				Objets.add(Station4);
        				Stations.add(Station4);
        				Joueurs.add(Joueur4);
        			}
    			}
    			else{
    				Station3 = new Station((int)(CentreEcranX-(250*pW)),(int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 3", 1);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Station4 = new Station((int)(CentreEcranX+(250*pW)), (int) (CentreEcranY+(192*pH)), Ecran,"DeathStar 4", 2);
    				Objets.add(Station4);
    				Stations.add(Station4);
    			}
    			

			
    			Planet1 = new AstreSpherique((int)(CentreEcranX-(170*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet2 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY-(140*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet3 = new AstreSpherique((int)(CentreEcranX+(170*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet4 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY+(140*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			TrouNoir1 = new AstreTrouNoir((int)(CentreEcranX+(456*pW)),  (int) (CentreEcranY-(256*pH)), 0f, 0f, imageTrou, Ecran, "Planete1", 1, 8*MASSE_PLANETE, 50);
    			TrouNoir2 = new AstreTrouNoir((int)(CentreEcranX-(456*pW)),  (int) (CentreEcranY+(256*pH)), 0f, 0f, imageTrou, Ecran, "Planete1", 1, 8*MASSE_PLANETE, 50);
    			Planet6 = new AstreSpherique((int)(CentreEcranX-(456*pW)),  (int) (CentreEcranY-(256*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Planet7 = new AstreSpherique((int)(CentreEcranX+(456*pW)),  (int) (CentreEcranY+(256*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    		
    			Satelite1 = new AstreSatelite(imageSat, Ecran, "Satelite 1", 1, MASSE_PLANETE/10, 15, Planet1,0.01);
    			Satelite3= new AstreSatelite(imageSat, Ecran, "Satelite 3", 1, MASSE_PLANETE/10, 15, Planet3,0.01);
    			Satelite6 = new AstreSatelite(imageSat, Ecran, "Satelite 6", 1, MASSE_PLANETE/10, 15, Planet6,0.01);
    			Satelite7 = new AstreSatelite(imageSat, Ecran, "Satelite 7", 1, MASSE_PLANETE/10, 15, Planet7,0.01);
    			Objets.add(Planet1);
    			Objets.add(Planet2);
    			Objets.add(Planet3);
    			Objets.add(Planet4);
    			Objets.add(Planet6);
    			Objets.add(Planet7);
    			Objets.add(TrouNoir1);
    			Objets.add(TrouNoir2);
    			
    			Objets.add(Satelite1);
    			Objets.add(Satelite3);
    			Objets.add(Satelite6);
    			Objets.add(Satelite7);
    			
    			break;
    		case 3 :
    			Station1 = new Station((int)(CentreEcranX-(150*pW)),(int) (CentreEcranY-(150*pH)), Ecran,"DeathStar 1",  1);
    			Station2 = new Station((int)(CentreEcranX+(150*pW)), (int) (CentreEcranY-(150*pH)), Ecran,"DeathStar 2", 2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
    			Joueurs.add(Joueur1);
    			Joueurs.add(Joueur2);
    			if(Framework.nbJoueurs>2){
    				Station3 = new Station((int)(CentreEcranX-(150*pW)),(int) (CentreEcranY+(150*pH)), Ecran,"DeathStar 3", 3);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Joueurs.add(Joueur3);
    				if(Framework.nbJoueurs>3){
    					Station4 = new Station((int)(CentreEcranX+(150*pW)), (int) (CentreEcranY+(150*pH)), Ecran,"DeathStar 4", 4);
        				Objets.add(Station4);
        				Stations.add(Station4);
        				Joueurs.add(Joueur4);
        			}
    			}
    			else{
    				Station3 = new Station((int)(CentreEcranX-(150*pW)),(int) (CentreEcranY+(150*pH)), Ecran,"DeathStar 3", 1);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Station4 = new Station((int)(CentreEcranX+(150*pW)), (int) (CentreEcranY+(150*pH)), Ecran,"DeathStar 4", 2);
    				Objets.add(Station4);
    				Stations.add(Station4);
    			}
    			
			
    			
    			Planet2 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY-(250*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet3 = new AstreSpherique((int)(CentreEcranX+(300*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet4 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY+(250*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet9 = new AstreSpherique((int)(CentreEcranX-(300*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet5 = new AstreSpherique((int)(CentreEcranX+(400*pW)),  (int) (CentreEcranY-(300*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Planet6 = new AstreSpherique((int)(CentreEcranX-(400*pW)),  (int) (CentreEcranY-(300*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Planet7 = new AstreSpherique((int)(CentreEcranX+(400*pW)),  (int) (CentreEcranY+(300*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Planet8 = new AstreSpherique((int)(CentreEcranX-(400*pW)),  (int) (CentreEcranY+(300*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			TrouNoir1 = new AstreTrouNoir(CentreEcranX,  CentreEcranY, 0f, 0f, imageTrou, Ecran, "Planete1", 1, 8*MASSE_PLANETE, 50);
    			
    			Satelite5= new AstreSatelite(imageSat, Ecran, "Satelite 3", 1, MASSE_PLANETE/10, 15, Planet5,0.01);
    			Satelite6 = new AstreSatelite(imageSat, Ecran, "Satelite 6", 1, MASSE_PLANETE/10, 15, Planet6,0.01);
    			Satelite7 = new AstreSatelite(imageSat, Ecran, "Satelite 7", 1, MASSE_PLANETE/10, 15, Planet7,0.01);
    			Satelite8 = new AstreSatelite(imageSat, Ecran, "Satelite 7", 1, MASSE_PLANETE/10, 15, Planet8,0.01);
    			
    			Objets.add(Planet2);
    			Objets.add(Planet3);
    			Objets.add(Planet4);
    			Objets.add(Planet5);
    			Objets.add(Planet6);
    			Objets.add(Planet7);
    			Objets.add(Planet8);
    			Objets.add(Planet9);
    			Objets.add(TrouNoir1);
    			
    			Objets.add(Satelite5);
    			Objets.add(Satelite6);
    			Objets.add(Satelite7);
    			Objets.add(Satelite8);
    			break;
    		case 4 :
    			
    			Station1 = new Station((int)(CentreEcranX-(600*pW)),(int) (CentreEcranY), Ecran,"DeathStar 1",  1);
    			Station2 = new Station((int)(CentreEcranX),(int) (CentreEcranY-(300*pH)), Ecran,"DeathStar 2", 2);
    			Objets.add(Station1);
    			Objets.add(Station2);
    			Stations.add(Station1);
    			Stations.add(Station2);
    			Joueurs.add(Joueur1);
    			Joueurs.add(Joueur2);
    			if(Framework.nbJoueurs>2){
    				Station3 = new Station((int)(CentreEcranX+(600*pW)), (int) (CentreEcranY), Ecran,"DeathStar 3", 3);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Joueurs.add(Joueur3);
    				if(Framework.nbJoueurs>3){
    					Station4 = new Station((int)(CentreEcranX), (int) (CentreEcranY+(300*pH)), Ecran,"DeathStar 4", 4);
        				Objets.add(Station4);
        				Stations.add(Station4);
        				Joueurs.add(Joueur4);
        			}
    			}
    			else{
    				Station3 = new Station((int)(CentreEcranX+(600*pW)), (int) (CentreEcranY), Ecran,"DeathStar 3", 1);
    				Objets.add(Station3);
    				Stations.add(Station3);
    				Station4 = new Station((int)(CentreEcranX), (int) (CentreEcranY+(300*pH)), Ecran,"DeathStar 4", 2);
    				Objets.add(Station4);
    				Stations.add(Station4);
    			}


			
    			Planet1 = new AstreSpherique((int)(CentreEcranX-(170*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet2 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY-(140*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet3 = new AstreSpherique((int)(CentreEcranX+(170*pW)), CentreEcranY, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet4 = new AstreSpherique(CentreEcranX, (int) (CentreEcranY+(140*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
    			Planet5 = new AstreSpherique((int)(CentreEcranX+(456*pW)),  (int) (CentreEcranY-(256*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Planet6 = new AstreSpherique((int)(CentreEcranX-(456*pW)),  (int) (CentreEcranY-(256*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Planet7 = new AstreSpherique((int)(CentreEcranX+(456*pW)),  (int) (CentreEcranY+(256*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Planet8 = new AstreSpherique((int)(CentreEcranX-(456*pW)),  (int) (CentreEcranY+(256*pH)), 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE+200, 50);
    			Satelite1 = new AstreSatelite(imageSat, Ecran, "Satelite 1", 1, MASSE_PLANETE/10, 15, Planet1,0.01);
    			Satelite3= new AstreSatelite(imageSat, Ecran, "Satelite 3", 1, MASSE_PLANETE/10, 15, Planet3,0.01);
    			Satelite6 = new AstreSatelite(imageSat, Ecran, "Satelite 6", 1, MASSE_PLANETE/10, 15, Planet6,0.01);
    			Satelite7 = new AstreSatelite(imageSat, Ecran, "Satelite 7", 1, MASSE_PLANETE/10, 15, Planet7,0.01);
    			Objets.add(Planet1);
    			Objets.add(Planet2);
    			Objets.add(Planet3);
    			Objets.add(Planet4);
    			Objets.add(Planet5);
    			Objets.add(Planet6);
    			Objets.add(Planet7);
    			Objets.add(Planet8);
    			
    			
    			Objets.add(Satelite1);
    			Objets.add(Satelite3);
    			Objets.add(Satelite6);
    			Objets.add(Satelite7);
    			break;
    		
    	}
		
    }
}
