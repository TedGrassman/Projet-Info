package framework;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

@SuppressWarnings("serial")
public class Framework extends Canvas {
	public static int nbJoueurs;
	mp3 musiqueMenu;
	JPanel panel = new JPanel();
	JPanel menuPrincipal = new JPanel(), menuPause = new JPanel(), menuOptions = new JPanel(), menuLance = new JPanel();
	JPanel boutonsRonds = new JPanel();
	CardLayout layout = new CardLayout();
	Game.ETAT old = Game.ETAT.PREPARATION; // variable permettant de stocker l'etat du jeu lors d'une mise en pause
	Image bg;
	customButton play, reprendre, settings, exit, menu, menu2, lance;
	JSlider sliderPoussee;
	customText textMenu, textOptions, poussée, joueurs;
	roundButton n2, n3, n4;

	public static boolean resized = false; //indique si la fenetre vient d'être redimensionnée
	
	public static int gauche, haut, droite, bas; // taille des bords de la fenetre
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, LANCE, DESTROYED, PAUSE}
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    
    public Framework ()
    {
        super();
        try {
			bg = ImageIO.read(new File("res/stars.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        menuPrincipal.setLayout((new BoxLayout(menuPrincipal, BoxLayout.Y_AXIS)));
        menuPause.setLayout((new BoxLayout(menuPause, BoxLayout.Y_AXIS)));
        menuOptions.setLayout((new BoxLayout(menuOptions, BoxLayout.Y_AXIS)));
        menuLance.setLayout((new BoxLayout(menuLance, BoxLayout.Y_AXIS)));
        play = new customButton("Commencer le jeu");				//initialise les boutons
        play.setAlignmentX(Component.CENTER_ALIGNMENT);				//centre horizontalement les boutons
        lance = new customButton("Lancer une partie");
        lance.setAlignmentX(Component.CENTER_ALIGNMENT);	
        reprendre = new customButton ("Reprendre la partie");
        reprendre.setAlignmentX(Component.CENTER_ALIGNMENT);
    	settings = new customButton ("Options du jeu");
    	settings.setAlignmentX(Component.CENTER_ALIGNMENT);
    	exit = new customButton ("Quitter le jeu");
    	exit.setAlignmentX(Component.CENTER_ALIGNMENT);
    	menu = new customButton ("Retour au menu");
    	menu.setAlignmentX(Component.CENTER_ALIGNMENT);
    	menu2 = new customButton ("Retour au menu");
    	menu2.setAlignmentX(Component.CENTER_ALIGNMENT);
    	textMenu = new customText("MENU PRINCIPAL", 60);
    	textMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
    	textOptions = new customText("OPTIONS", 60);
    	textOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
    	poussée = new customText("Force de poussée des missiles", 30);
    	poussée.setAlignmentX(Component.CENTER_ALIGNMENT);
        sliderPoussee = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);	// slider horizontal, min 0, max 10, défaut à 5
        sliderPoussee.setOpaque(false);								//fond transparent
        sliderPoussee.setAlignmentX(Component.CENTER_ALIGNMENT);
        sliderPoussee.setMajorTickSpacing(1);						//espacement et dessin des crans
        sliderPoussee.setPaintTicks(true);
        sliderPoussee.setPaintLabels(true);							//dessin des chiffres sous les crans
        sliderPoussee.setForeground(Color.WHITE);
    	n2 = new roundButton("2", 0);
    	n3 = new roundButton("3", 0);
    	n4 = new roundButton("4", 0);
    	joueurs = new customText("Nombre de joueurs:", 30);
    	joueurs.setAlignmentX(Component.CENTER_ALIGNMENT);
        
    	play.addActionListener(this);								//crée les listeners
    	settings.addActionListener(this);
    	exit.addActionListener(this);
    	reprendre.addActionListener(this);
    	menu.addActionListener(this);
    	menu2.addActionListener(this);
    	lance.addActionListener(this);
        sliderPoussee.addChangeListener(this);
        n2.addActionListener(this);
        n3.addActionListener(this);
        n4.addActionListener(this);
    	
    	menuPrincipal.add(textMenu);
    	//menuPrincipal.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        menuPrincipal.add(lance);											//ajoute les boutons dans les cartes
        menuPrincipal.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        menuPrincipal.add(settings);										//une carte = un menu
        menuPrincipal.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        menuPrincipal.add(exit);
        menuPrincipal.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        
        menuPause.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        menuPause.add(reprendre);
        menuPause.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        menuPause.add(menu);
        menuPause.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        
        menuOptions.add(textOptions);
        menuOptions.add(poussée);
        menuOptions.add(sliderPoussee);
        menuOptions.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        menuOptions.add(menu2);

        menuLance.add(joueurs);
        boutonsRonds.add(n2);
        boutonsRonds.add(n3);
        boutonsRonds.add(n4);
        menuLance.add(boutonsRonds);
        menuLance.add(new Box.Filler(new Dimension(0,5), new Dimension(0,15), new Dimension(0,20)));
        menuLance.add(play);
        
        panel.setLayout(layout);									//définit le layout du panel principal en type "card"
        panel.setOpaque(false);										//arrière plan transparent
        
        menuPrincipal.setOpaque(false);
        menuPause.setOpaque(false);
        menuOptions.setOpaque(false);
        menuLance.setOpaque(false);
        boutonsRonds.setOpaque(false);
        
    	
    	panel.add(menuPrincipal, "mDepart");								//ajoute les cartes au panel principal
    	panel.add(menuPause, "mPause");
    	panel.add(menuOptions, "mOptions");
    	panel.add(menuLance, "mLance");
    	layout.show(panel, "mDepart");								//affiche la première carte
    	add(panel);													//ajoute le panel au framework
        

    	musiqueMenu = new mp3 ("res/sons/menu.mp3");
    	
    	
    	gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread("boucle jeu") {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize()
    {
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {
    
    }
    
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;
                case LANCE:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case PAUSE:
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();
                    musiqueMenu.stop();
                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.PLAYING;
                    newGame();
                break;
                case VISUALIZING:
                    // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec/2)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        gauche=this.getInsets().left;
                        haut=this.getInsets().top;
                        droite=this.getInsets().right;
                        bas=this.getInsets().bottom;
                        bg = bg.getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
                        
                        // When we get size of frame we change status.
                        if(resized && game !=null){
                        	frameWidth = this.getWidth();
                            frameHeight = this.getHeight();
                        	gameState = GameState.PLAYING;
                        	resized=false;
                        }
                        else{
                        	gameState = GameState.MAIN_MENU;
                        	musiqueMenu.jouer();
                        	musiqueMenu.boucle();
                        }
                        
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {	
    	g2d.drawImage(bg, 0, 0 , null);
        switch (gameState)
        {
            case PLAYING:
            	panel.setVisible(false);
                game.Draw(gameTime, g2d, mousePosition());
            break;
            case LANCE:
                layout.show(panel, "mLance");
            break;
            case MAIN_MENU:
            	layout.show(panel, "mDepart");
            	panel.setVisible(true);
            break;
            case PAUSE:
            	layout.show(panel, "mPause");
            	panel.setVisible(true);
            break;
            case OPTIONS:
            	layout.show(panel, "mOptions");
            break;
            case GAME_CONTENT_LOADING:
                g2d.drawString("CHARGEMENT", frameWidth-200, frameHeight-50);
            break;
        }
    }
    
    
    /**
     * Starts new game.
     */
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        if(game==null){
        	game = new Game();
        }
        else{
        game.RestartGame();
        }
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
        game.UpdateGame(gameTime, mousePosition());
    }
    
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {	
    	if(e.getKeyCode()==KeyEvent.VK_ESCAPE){	//si la touche échap est pressée...
    		//System.out.println(gameState);     DEBBUGING
    		if(game !=null){					//on vérifie que le jeu existe
	    		if(gameState==GameState.PAUSE){	//si le jeu est en pause...
	    				game.etat=old;					//on restaure son état d'avant la pause
	    				gameState = GameState.PLAYING;	//et on le relance
	    				return;							//on force le retour car sinon le "if" suivant se lance immédiatement
	    		}
	    		if(gameState == GameState.PLAYING){		//si on joue
	    			old = game.etat;					//on stocke l'etat dans lequel était le jeu
	    			game.etat=Game.ETAT.PAUSE;			//on le met en pause
	    			gameState=GameState.PAUSE;			//on affiche le menu pause
	    		}
    		}
    	}
    	
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    	if(game!=null){
    		if(game.etat == Game.ETAT.PREPARATION){
    			game.mouseClicked=true;
    		}
    	}
    }
    public void mousePressed(MouseEvent e) {
    	if(game!=null){
    		if(game.etat == Game.ETAT.PREPARATION){
    			game.mousePressed=true;
    		}
    	}
    	
    }
    public void mouseReleased(MouseEvent e) {
    	if(game!=null){
    		if(game.etat == Game.ETAT.PREPARATION){
    			game.mouseReleased=true;
    		}
    	}
    	
    }
    
    public void actionPerformed(ActionEvent event) { //clics sur les boutons

    	   Object source = event.getSource();
    	   
    	   if (source == exit) {
    		   musiqueMenu.stop();
    	       System.exit(0);
    	   } else if (source == lance){
    		   gameState=GameState.LANCE;
    		   this.validate();
    	   }
    	   else if (source == play && nbJoueurs>1) {
    	       gameState= GameState.STARTING;
    	       this.validate();
    	   } else if (source == settings){
    		   gameState= GameState.OPTIONS;
    		   this.validate();
    	   } else if (source == menu || source == menu2){
    		   
    		   if(source == menu){
    			   musiqueMenu.jouer();
    			   musiqueMenu.boucle();
    			   Game.etat = Game.ETAT.FIN;
    		   }
    		   gameState = GameState.MAIN_MENU;
    		   this.validate();
    		   
    	   } else if (source == reprendre){
    		   musiqueMenu.stop();
    		   gameState = GameState.PLAYING;
     		   game.etat=old;
     		   this.validate();
    	   } else if (source == n2){
    		   nbJoueurs=2;
    	   } else if (source == n3){
    		   nbJoueurs=3;
    	   } else if (source == n4){
    		   nbJoueurs=4;
    	   }
    }


	@Override
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if(source == sliderPoussee){
			if (!sliderPoussee.getValueIsAdjusting()) {
				double val = sliderPoussee.getValue();
				Missile.poussee=val/100;
			}

		}
	}
}
