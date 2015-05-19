package framework;

import java.awt.CardLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

@SuppressWarnings("serial")
public class Framework extends Canvas {
	JPanel panel = new JPanel();
	JPanel menu = new JPanel(); 
	JButton play = new customButton("Lancer une partie");
	JButton reprendre = new JButton ("Reprendre la partie");
	JButton settings = new JButton("Options");
	JButton exit = new JButton("Ragequit");
	JButton mainMenu = new JButton("Retour menu");
	CardLayout layout = new CardLayout();
	Game.ETAT old = Game.ETAT.PREPARATION; // variable permettant de stocker l'etat du jeu lors d'une mise en pause
	
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
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
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
        panel.setLayout(layout);
    	play.addActionListener(this);
    	settings.addActionListener(this);
    	exit.addActionListener(this);
    	mainMenu.addActionListener(this);
    	reprendre.addActionListener(this);
    	menu.add(play);
    	menu.add(settings);
    	menu.add(exit);
    	menu.setBackground(null);
    	panel.setBackground(null);
    	panel.add(menu, "Menu");
    	add(panel);
    	layout.show(panel, "Menu");
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
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
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
                        
                        // When we get size of frame we change status.
                        if(resized && game !=null){
                        	frameWidth = this.getWidth();
                            frameHeight = this.getHeight();
                        	gameState = GameState.PLAYING;
                        	resized=false;
                        }
                        else{
                        	gameState = GameState.MAIN_MENU;
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
        switch (gameState)
        {
            case PLAYING:
            	panel.setVisible(false);
                game.Draw(gameTime, g2d, mousePosition());
            break;
            case GAMEOVER:
                //...
            break;
            case MAIN_MENU:
            	
            	panel.setVisible(true);
            break;
            case OPTIONS:
                //...
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
    	if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
    		if(game !=null){
	    		if(menu.getComponentCount()==3 && old != Game.ETAT.PAUSE){
	    			menu.add(reprendre);
	    		}
	    		if(gameState==GameState.MAIN_MENU){
	    			//panel.setVisible(false);
	     		   gameState = GameState.PLAYING;
	     		   game.etat=old;
	    		}
	    		else{
	    			old = game.etat;
	    			game.etat=Game.ETAT.PAUSE;
	    			gameState=GameState.MAIN_MENU;
	    		}
    		}
        System.out.println(e);
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
    		if(game.etat != Game.ETAT.PAUSE && game.etat != Game.ETAT.SIMULATION){
    			game.mouseClicked=true;
    		}
    	}
    }
    public void mousePressed(MouseEvent e) {
    	if(game!=null){
    		if(game.etat != Game.ETAT.PAUSE && game.etat != Game.ETAT.SIMULATION){
    			game.mousePressed=true;
    		}
    	}
    	
    }
    public void mouseReleased(MouseEvent e) {
    	if(game!=null){
    		if(game.etat != Game.ETAT.PAUSE && game.etat != Game.ETAT.SIMULATION){
    			game.mouseReleased=true;
    		}
    	}
    	
    }
    
    public void actionPerformed(ActionEvent event) {

    	   Object source = event.getSource();

    	   if (source == exit) {
    	       System.exit(0);
    	   } else if (source == play) {
    	       gameState= GameState.STARTING;
    	       
    		   
    	       this.validate();
    	   } else if (source == settings){

    	   } else if (source == mainMenu){
    	       gameState = GameState.MAIN_MENU;
    	       this.validate();
    	   } else if (source == reprendre){
    		   gameState = GameState.PLAYING;
     		   game.etat=old;
     		   this.validate();
    	   }
    }
}
