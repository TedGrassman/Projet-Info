package framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {
	public static boolean Click = false;
	public static enum ETAT{J1, J2, SIMULATION}
	public static ETAT etat;
	Rectangle Ecran; // Les limites de la fenêtre
	final int MASSE_PLANETE = 500;
	Station Vaisseau1, Vaisseau2; // L'objet que l'utilisateur va déplacer
	AstreSpherique Planet1, Planet2, Planet3, Planet4;
	Missile Missile1, Missile2, Missile3, Missile4, Missile5;
	ArrayList<Objet> Objets; // Liste de tous les objets du jeu
	ArrayList<Missile> Missiles; // Liste de tous les missiles
	ArrayList<Trajectoire> Trajectoires; // Liste de toutes les trajectoires
	ArrayList<Station> Stations1, Stations2; // Liste de toutes les stations de chaque joueur
	int score; // Score du joueur
	Font font; // Objet de police d'écriture
	String[] NomImage = {"planete.png"};
	String[] NomImageM = {"missile3_1.png","missile3_2.png","missile3_3.png","missile3_4.png","missile3_5.png","missile3_6.png","missile3_7.png","missile3_8.png","missile3_9.png","missile3_10.png"};
	Trajectoire Trajectoire1, Trajectoire2, Trajectoire3, Trajectoire4, Trajectoire5;
	int compt;

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
		Stations1 = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		Stations2 = new ArrayList<Station>();
		Planet4 = new AstreSpherique(650, 360, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		Objets.add(Planet4);
		
		Vaisseau1 = new Station(100, 100, Ecran,"DeathStar 1");
		Vaisseau2 = new Station((int) (Ecran.getWidth() - 100), (int) (Ecran.getHeight() - 100), Ecran,
				"DeathStar 2");
		Objets.add(Vaisseau1);
		Objets.add(Vaisseau2);
		Stations1.add(Vaisseau1);
		Stations2.add(Vaisseau2);
		

		Missile1 = new Missile(650, 600, 2.5f, 1f, Ecran, "Missile1", Color.RED);
		Missile2 = new Missile(700, 700, 1.5f, -1.5f, Ecran, "Missile2", Color.GREEN);
		Missile3 = new Missile(100, 300, 1f, 2f, Ecran, "Missile3", Color.BLUE);
		Missile4 = new Missile(300, 650, -0.5f, -1f, Ecran, "Missile4", Color.YELLOW);
		Missile5 = new Missile(1100, 550, -1.5f, 2f, Ecran, "Missile5", Color.WHITE);
		
		Objets.add(Missile1);
		Objets.add(Missile2);
		Objets.add(Missile3);
		Objets.add(Missile4);
		Objets.add(Missile5);
		
		Missiles.add(Missile1);
		Missiles.add(Missile2);
		Missiles.add(Missile3);
		Missiles.add(Missile4);
		Missiles.add(Missile5);
		
		etat=ETAT.J1;
		compt=0;
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
    	etat=ETAT.J1;
    	score = 0;
		
		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Trajectoires = new ArrayList<Trajectoire>(); // Créer la liste chainée en mémoire
		Stations1 = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		Stations2 = new ArrayList<Station>();
		Planet4 = new AstreSpherique(650, 360, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		Objets.add(Planet4);
		
		Vaisseau1 = new Station(100, 100, Ecran,"DeathStar 1");
		Vaisseau2 = new Station((int) (Ecran.getWidth() - 100), (int) (Ecran.getHeight() - 100), Ecran,
				"DeathStar 2");
		Objets.add(Vaisseau1);
		Objets.add(Vaisseau2);
		Stations1.add(Vaisseau1);
		Stations2.add(Vaisseau2);
		

		Missile1 = new Missile(650, 600, 2.5f, 1f, Ecran, "Missile1", Color.RED);
		Missile2 = new Missile(700, 700, 1.5f, -1.5f, Ecran, "Missile2", Color.GREEN);
		Missile3 = new Missile(100, 300, 1f, 2f, Ecran, "Missile3", Color.BLUE);
		Missile4 = new Missile(300, 650, -0.5f, -1f, Ecran, "Missile4", Color.YELLOW);
		Missile5 = new Missile(1100, 550, -1.5f, 2f, Ecran, "Missile5", Color.WHITE);
		
		Objets.add(Missile1);
		Objets.add(Missile2);
		Objets.add(Missile3);
		Objets.add(Missile4);
		Objets.add(Missile5);
		
		Missiles.add(Missile1);
		Missiles.add(Missile2);
		Missiles.add(Missile3);
		Missiles.add(Missile4);
		Missiles.add(Missile5);

		
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
    	
    	switch(etat){
    	case J1 :
    		System.out.println("J1");
    		if (Click){
	    		System.out.println(compt);
	    		Missile missile = new Missile((int)(Stations1.get(0).centreG.x), (int)(Stations1.get(compt).centreG.y), (float)((mousePosition.x-Stations1.get(0).centreG.x)/100), (float)((mousePosition.y-Stations1.get(0).centreG.y)/100), Ecran, "Missile J1 n°"+(compt+1), Color.RED);
	    		Objets.add(missile);
				Missiles.add(missile);
	    		System.out.println("Missile"+(compt+1) +" tiré");
	    		compt++;
	    		Click=false;
    			
    		}
    		if(compt>=Stations1.size()){
    			compt=0;
    			etat=ETAT.J2;
    		}
    		break;
    	case J2:
    		System.out.println("J2");
    		if (Click){
    			Missile missile = new Missile((int)(Stations2.get(0).centreG.x), (int)(Stations2.get(compt).centreG.y), (float)((mousePosition.x-Stations2.get(0).centreG.x)/100), (float)((mousePosition.y-Stations2.get(0).centreG.y)/100), Ecran, "Missile J2 n°"+(compt+1), Color.BLUE);
    			Objets.add(missile);
				Missiles.add(missile);
    			System.out.println("Missile"+(compt+1) +" tiré");
    			compt++;
    			Click=false;
    		}
    		if(compt>=Stations2.size()){
    			compt=0;
    			etat=ETAT.SIMULATION;
    		}
    		break;
    	case SIMULATION:
    		System.out.println("Simulation");
    		for (int k = 0; k < Objets.size(); k++) {
				Objet O = Objets.get(k);
				O.move(gameTime);
    		}
    		break;

		}
    	for(int i=0; i<Missiles.size(); i++){
			if(Missiles.get(i).Collision() != Missiles.get(i)){
				System.out.println("Collision de Missile "+(i+1)+" avec " +Missiles.get(i).Collision().nom_objet);
			}
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
    	//g2d.setColor(Color.BLACK);

		//g2d.fillRect((int)Ecran.getX(), (int) Ecran.getY(), (int) (Ecran.getX() + Ecran.getWidth()), (int) (Ecran.getY() + Ecran.getHeight()));

		//g2d.setColor(Color.white);
		
		// dessine TOUS les objets dans le buffer
		for (int k = 0; k < Objets.size(); k++) {
			Objet O = Objets.get(k);
			O.draw(gameTime, g2d);
		}
    }
}
