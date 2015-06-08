
package framework;

import gameEntities.Missile;

import java.awt.BorderLayout;
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
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

/**
 * Framework that controls the game (Game.java): create it, update it and
 * draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

@SuppressWarnings("serial")
public class Framework extends Canvas {
	public static int nbJoueurs, niveauChoisi;
	mp3 musiqueMenu, musiqueLance, musiqueNiveau;
	int aMenu, aLance; // Aléatoire pour le choix de la musique de menu et de lancement
	JPanel panel = new JPanel();
	JPanel menuPrincipal = new JPanel(), menuPause = new JPanel(), menuOptions = new JPanel(),
			menuLance = new JPanel(), menuEnd = new JPanel();
	JPanel boutonsRonds = new JPanel(), boutonsNiveau = new JPanel();
	JPanel bottom1 = new JPanel(), bottom2 = new JPanel(), bottom3 = new JPanel(), bottom4 = new JPanel(), bottom5 = new JPanel();
	CardLayout layout = new CardLayout();
	Game.ETAT old = Game.ETAT.PREPARATION; // variable permettant de stocker l'etat du jeu lors d'une mise en pause
	Image bg;
	customButton play, play2, reprendre, reprendre2, settings, exit, menu, menu2, menu3, menu4, lance, lance2;
	JSlider sliderPoussee, sliderTrajectoire;
	customText textMenu, textOptions, poussee, trajectoire, joueurs, niveau;
	RoundButton j2, j3, j4, n1, n2, n3, n4;
	Random rand = new Random();

	public static boolean resized = false; // indique si la fenetre vient d'être redimensionnée

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
	 * Time of one second in nanoseconds. 1 second = 1 000 000 000 nanoseconds
	 */
	public static final long secInNanosec = 1000000000L;

	/**
	 * Time of one millisecond in nanoseconds. 1 millisecond = 1 000 000
	 * nanoseconds
	 */
	public static final long milisecInNanosec = 1000000L;

	/**
	 * FPS - Frames per second How many times per second the game should update?
	 */
	private final int GAME_FPS = 60;
	/**
	 * Pause between updates. It is in nanoseconds.
	 */
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

	/**
	 * Possible states of the game
	 */
	public static enum GameState {
		STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, LANCE, GAME_OVER, PAUSE
	}

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

	public Framework() {
		super();
		try {
			bg = ImageIO.read(new File("res/stars.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		menuPrincipal.setLayout((new BoxLayout(menuPrincipal, BoxLayout.Y_AXIS)));
		menuPause.setLayout((new BoxLayout(menuPause, BoxLayout.Y_AXIS)));
		menuOptions.setLayout((new BoxLayout(menuOptions, BoxLayout.Y_AXIS)));
		menuLance.setLayout((new BoxLayout(menuLance, BoxLayout.Y_AXIS)));
		menuEnd.setLayout((new BoxLayout(menuEnd, BoxLayout.Y_AXIS)));
		bottom1.setLayout(new BoxLayout(bottom1, BoxLayout.X_AXIS));
		bottom2.setLayout(new BoxLayout(bottom2, BoxLayout.X_AXIS));
		bottom3.setLayout(new BoxLayout(bottom3, BoxLayout.X_AXIS));
		bottom4.setLayout(new BoxLayout(bottom4, BoxLayout.X_AXIS));
		bottom5.setLayout(new BoxLayout(bottom5, BoxLayout.X_AXIS));
		boutonsNiveau.setLayout(new BoxLayout(boutonsNiveau, BoxLayout.X_AXIS));
		boutonsRonds.setLayout(new BoxLayout(boutonsRonds, BoxLayout.X_AXIS));

		play = new customButton("Commencer le jeu", 0); // initialise les boutons
		play.setAlignmentX(Component.CENTER_ALIGNMENT); // centre horizontalement les boutons
		lance = new customButton("Lancer une partie", 0);
		lance.setAlignmentX(Component.CENTER_ALIGNMENT);
		play2 = new customButton("Relancer la partie", 0);
		play2.setAlignmentX(Component.CENTER_ALIGNMENT);
		reprendre = new customButton("Reprendre la partie", 0);
		reprendre.setAlignmentX(Component.CENTER_ALIGNMENT);
		settings = new customButton("Options du jeu", 0);
		settings.setAlignmentX(Component.CENTER_ALIGNMENT);
		exit = new customButton("Quitter le jeu", 1);
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		menu = new customButton("Retour au menu", 1);
		menu.setAlignmentX(Component.CENTER_ALIGNMENT);
		menu2 = new customButton("Retour au menu", 1);
		menu2.setAlignmentX(Component.CENTER_ALIGNMENT);
		menu3 = new customButton("Retour au menu", 1);
		menu3.setAlignmentX(Component.CENTER_ALIGNMENT);
		menu4 = new customButton("Retour au menu", 1);
		menu4.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Initialise zones de texte
		textMenu = new customText("MENU PRINCIPAL", 60);
		textMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		textOptions = new customText("OPTIONS", 60);
		textOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
		poussee = new customText("Force de poussée des missiles", 30);
		poussee.setAlignmentX(Component.CENTER_ALIGNMENT);
		trajectoire = new customText("Longueur de la trajectoire des missiles", 30);
		trajectoire.setAlignmentX(Component.CENTER_ALIGNMENT);
		joueurs = new customText("Nombre de joueurs", 30);
		joueurs.setAlignmentX(Component.CENTER_ALIGNMENT);
		niveau = new customText("Choix du niveau", 30);
		niveau.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// slider horizontal, min 0, max 10, défaut à 5
		sliderPoussee = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 5);
		sliderPoussee.setOpaque(false); // fond transparent
		sliderPoussee.setAlignmentX(Component.CENTER_ALIGNMENT);
		sliderPoussee.setMajorTickSpacing(1); // espacement et dessin des crans
		sliderPoussee.setPaintTicks(true);
		sliderPoussee.setPaintLabels(true); // dessin des chiffres sous les crans
		sliderPoussee.setForeground(Color.WHITE);
		sliderPoussee.setMaximumSize(new Dimension(1000, 70));
		
		// slider horizontal, min 0, max 10, défaut à 5
		sliderTrajectoire = new JSlider(SwingConstants.HORIZONTAL, 50, 500, 100); 
		sliderTrajectoire.setOpaque(false); // fond transparent
		sliderTrajectoire.setAlignmentX(Component.CENTER_ALIGNMENT);
		sliderTrajectoire.setMajorTickSpacing(50); // espacement et dessin des crans
		sliderTrajectoire.setMinorTickSpacing(10);
		sliderTrajectoire.setPaintTicks(true);
		sliderTrajectoire.setPaintLabels(true); // dessin des chiffres sous les crans
		sliderTrajectoire.setForeground(Color.WHITE);
		sliderTrajectoire.setMaximumSize(new Dimension(1000, 70));
		
		// roundButtons
		j2 = new RoundButton("2", 0);
		j3 = new RoundButton("3", 0);
		j4 = new RoundButton("4", 0);
		n1 = new RoundButton("Niv1", 1);
		n2 = new RoundButton("Niv2", 1);
		n3 = new RoundButton("Niv3", 1);
		n4 = new RoundButton("Niv4", 1);
		
		// crée les listeners
		play.addActionListener(this);
		settings.addActionListener(this);
		exit.addActionListener(this);
		reprendre.addActionListener(this);
		menu.addActionListener(this);
		menu2.addActionListener(this);
		menu3.addActionListener(this);
		menu4.addActionListener(this);
		lance.addActionListener(this);
		play2.addActionListener(this);
		sliderPoussee.addChangeListener(this);
		sliderTrajectoire.addChangeListener(this);
		j2.addActionListener(this);
		j3.addActionListener(this);
		j4.addActionListener(this);
		n1.addActionListener(this);
		n2.addActionListener(this);
		n3.addActionListener(this);
		n4.addActionListener(this);
		
		menuPrincipal.add(textMenu);
		menuPrincipal.add(lance); // ajoute les boutons dans les cartes
		menuPrincipal.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuPrincipal.add(settings); // une carte = un menu
		menuPrincipal.add(Box.createVerticalGlue());
		bottom1.add(Box.createHorizontalGlue());
		bottom1.add(exit);
		bottom1.add(new Box.Filler(new Dimension(5, 5), new Dimension(5, 5), new Dimension(5, 5)));
		menuPrincipal.add(bottom1);
		menuPrincipal.add(new Box.Filler(new Dimension(5, 10), new Dimension(5, 10), new Dimension(5, 10)));
		
		menuPause.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuPause.add(reprendre);
		menuPause.add(Box.createVerticalGlue());
		bottom4.add(Box.createHorizontalGlue());
		bottom4.add(menu);
		bottom4.add(new Box.Filler(new Dimension(5, 5), new Dimension(5, 5), new Dimension(5, 5)));
		menuPause.add(bottom4);
		menuPause.add(new Box.Filler(new Dimension(5, 10), new Dimension(5, 10), new Dimension(5, 10)));
		
		menuEnd.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuEnd.add(play2);
		menuEnd.add(Box.createVerticalGlue());
		bottom5.add(Box.createHorizontalGlue());
		bottom5.add(menu4);
		bottom5.add(new Box.Filler(new Dimension(5, 5), new Dimension(5, 5), new Dimension(5, 5)));
		menuEnd.add(bottom5);
		menuEnd.add(new Box.Filler(new Dimension(5, 10), new Dimension(5, 10), new Dimension(5, 10)));
		
		menuOptions.add(textOptions);
		menuOptions.add(poussee);
		menuOptions.add(sliderPoussee);
		menuOptions.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuOptions.add(trajectoire);
		menuOptions.add(sliderTrajectoire);
		menuOptions.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuOptions.add(Box.createVerticalGlue());
		bottom2.add(Box.createHorizontalGlue());
		bottom2.add(menu2);
		bottom2.add(new Box.Filler(new Dimension(5, 5), new Dimension(5, 5), new Dimension(5, 5)));
		menuOptions.add(bottom2);
		menuOptions.add(new Box.Filler(new Dimension(5, 10), new Dimension(5, 10), new Dimension(5, 10)));
		
		menuLance.add(joueurs);
		boutonsRonds.add(j2);
		boutonsRonds.add(j3);
		boutonsRonds.add(j4);
		boutonsNiveau.add(n1);
		boutonsNiveau.add(n2);
		boutonsNiveau.add(n3);
		boutonsNiveau.add(n4);
		menuLance.add(boutonsRonds);
		menuLance.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuLance.add(niveau);
		menuLance.add(boutonsNiveau);
		menuLance.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuLance.add(play);
		menuLance.add(Box.createVerticalGlue());
		bottom3.add(Box.createHorizontalGlue());
		bottom3.add(menu3);
		bottom3.add(new Box.Filler(new Dimension(5, 5), new Dimension(5, 5), new Dimension(5, 5)));
		menuLance.add(bottom3);
		menuLance.add(new Box.Filler(new Dimension(5, 10), new Dimension(5, 10), new Dimension(5, 10)));
		
		panel.setLayout(layout); // définit le layout du panel principal en type "card"
		panel.setOpaque(false); // arrière plan transparent
		menuPrincipal.setOpaque(false);
		menuPause.setOpaque(false);
		menuOptions.setOpaque(false);
		menuLance.setOpaque(false);
		menuEnd.setOpaque(false);
		boutonsRonds.setOpaque(false);
		boutonsNiveau.setOpaque(false);
		bottom1.setOpaque(false);
		bottom2.setOpaque(false);
		bottom3.setOpaque(false);
		bottom4.setOpaque(false);
		bottom5.setOpaque(false);
		

		panel.add(menuPrincipal, "mDepart"); // ajoute les cartes au panel principal
		panel.add(menuPause, "mPause");
		panel.add(menuOptions, "mOptions");
		panel.add(menuLance, "mLance");
		panel.add(menuEnd, "mEnd");
		layout.show(panel, "mDepart"); // affiche la première carte
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER); // ajoute le panel au framework
		
		aMenu = rand.nextInt(2 - 0 + 1) + 0;
		aLance = rand.nextInt(1 - 0 + 1) + 0;
		musiqueMenu = new mp3("res/sons/menu"+aMenu+".mp3");
		musiqueLance = new mp3("res/sons/lance"+aLance+".mp3");
		
		
		gameState = GameState.VISUALIZING;

		// We start the game in a new thread.
		final Thread gameThread = new Thread("boucle jeu") {
			@Override
			public void run() {
				GameLoop();
			}
		};
		gameThread.start();
	}

	/**
	 * Set variables and objects. This method is intended to set the variables
	 * and objects for this class, variables and objects for the actual game can
	 * be set in Game.java.
	 */
	private void Initialize() {
	}

	/**
	 * Load files - images, sounds, ... This method is intended to load files
	 * for this class, files for the actual game can be loaded in Game.java.
	 */
	private void LoadContent() {

	}

	/**
	 * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is
	 * updated and then the game is drawn on the screen.
	 */
	private void GameLoop() {
		// This two variables are used in VISUALIZING state of the game. We used
		// them to wait some time so that we get correct frame/window
		// resolution.
		long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

		// This variables are used for calculating the time that defines for how
		// long we should put threat to sleep to meet the GAME_FPS.
		long beginTime, timeTaken, timeLeft;

		while (true) {
			beginTime = System.nanoTime();

			switch (gameState) {
			case PLAYING:
				gameTime += System.nanoTime() - lastTime;

				game.UpdateGame(gameTime, mousePosition());

				lastTime = System.nanoTime();
				break;
			case LANCE:
				// ...
				break;
			case MAIN_MENU:
				// ...
				break;
			case PAUSE:
				// ...
				break;
			case OPTIONS:
				// ...
				break;
			case GAME_CONTENT_LOADING:
				// ...
				break;
			case STARTING:
				// Sets variables and objects.
				Initialize();
				// Load files - images, sounds, ...
				LoadContent();
				// When all things that are called above finished, we change
				// game status to main menu.
				
				if (game == null) {
					newGame();
				} else {
					restartGame();
				}
				
				
				break;
			case VISUALIZING:
				// On attend 1/2 seconde et que la fenêtre ait une taille non nulle
				if (getWidth() > 1 && visualizingTime > secInNanosec/2) {
					frameWidth = getWidth();
					frameHeight = getHeight();
					gauche = this.getInsets().left;
					haut = this.getInsets().top;
					droite = this.getInsets().right;
					bas = this.getInsets().bottom;
					bg = bg.getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
					

					// When we get size of frame we change status.
					if (resized && game != null) {
						frameWidth = getWidth();
						frameHeight = getHeight();
						gameState = GameState.PLAYING;
						resized = false;
					} else {	// Si fenêtre non redimensionnée, on change l'état à MAIN_MENU
						gameState = GameState.MAIN_MENU;
						musiqueMenu.fadeIn();
						musiqueMenu.boucle();
					}

				} else {
					visualizingTime += System.nanoTime() - lastVisualizingTime;
					lastVisualizingTime = System.nanoTime();
				}
				break;
			case GAME_OVER:
				// ...
				break;
			default:
				// ...
				break;
			}

			// Repaint the screen.
			repaint();

			// Here we calculate the time that defines for how long we should
			// put threat to sleep to meet the GAME_FPS.
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
			// If the time is less than 10 milliseconds, then we will put thread
			// to sleep for 10 millisecond so that some other thread can do some
			// work.
			if (timeLeft < 10)
				timeLeft = 10; // set a minimum
			try {
				// Provides the necessary delay and also yields control so that
				// other thread can do work.
				Thread.sleep(timeLeft);
			} catch (final InterruptedException ex) {
			}
		}
	}

	/**
	 * Draw the game to the screen. It is called through repaint() method in
	 * GameLoop() method.
	 */
	@Override
	public void Draw(Graphics2D g2d) {
		g2d.drawImage(bg, 0, 0, null);
		switch (gameState) {
		case PLAYING:
			panel.setVisible(false);
			game.Draw(gameTime, g2d, mousePosition());
			break;
		case LANCE:
			layout.show(panel, "mLance");
			panel.setVisible(true);
			break;
		case MAIN_MENU:
			layout.show(panel, "mDepart");
			panel.setVisible(true);
			break;
		case PAUSE:
			game.Draw(gameTime, g2d, mousePosition());
			layout.show(panel, "mPause");
			panel.setVisible(true);
			break;
		case OPTIONS:
			layout.show(panel, "mOptions");
			panel.setVisible(true);
			break;
		case GAME_CONTENT_LOADING:
			g2d.drawString("CHARGEMENT", frameWidth - 200, frameHeight - 100);
			panel.setVisible(true);
			break;
		case GAME_OVER:
			game.Draw(gameTime, g2d, mousePosition());
			layout.show(panel, "mEnd");
			panel.setVisible(true);
			break;
		case STARTING:
			break;
		case VISUALIZING:
			break;
		default:
			break;
		}
	}

	/**
	 * Starts new game.
	 */
	private void newGame() {
		// We set gameTime to zero and lastTime to current time for later
		// calculations.
		gameTime = 0;
		lastTime = System.nanoTime();
		
		//if (game == null) {					// ----------------------
		game = new Game();						// C'est ICI qu'on lance
		//} else {								// le jeu (classe Game)
		//	game.RestartGame();					// pour la première fois
		//}										// ----------------------
	}

	/**
	 * Restart game - reset game time and call RestartGame() method of game
	 * object so that reset some variables.
	 */
	private void restartGame() {
		// We set gameTime to zero and lastTime to current time for later
		// calculations.
		gameTime = 0;
		lastTime = System.nanoTime();

		game.RestartGame();

		// We change game status so that the game can start.
		gameState = GameState.PLAYING;
		//game.UpdateGame(gameTime, mousePosition());
	}

	/**
	 * Returns the position of the mouse pointer in game frame/window. If mouse
	 * position is null than this method return 0,0 coordinate.
	 * 
	 * @return Point of mouse coordinates.
	 */
	private Point mousePosition() {
		try {
			final Point mp = this.getMousePosition();

			if (mp != null)
				return this.getMousePosition();
			else
				return new Point(0, 0);
		} catch (final Exception e) {

			return new Point(0, 0);
		}
	}

	/**
	 * This method is called when keyboard key is released.
	 * 
	 * @param e KeyEvent
	 */
	@Override
	public void keyReleasedFramework(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // si la touche échap est pressée...
			// System.out.println(gameState); 															DEBBUGING
			if (game != null) { // on vérifie que le jeu existe
				if (gameState == GameState.PAUSE) { // si le jeu est en pause...
					Game.etat = old; // on restaure son état d'avant la pause
					gameState = GameState.PLAYING; // et on le relance
					return; // on force le retour car sinon le "if" suivant senlance immédiatement
				}
				if (gameState == GameState.PLAYING) { // si on joue
					old = Game.etat; // on stocke l'etat dans lequel était le jeu
					Game.etat = Game.ETAT.PAUSE; // on le met en pause
					gameState = GameState.PAUSE; // on affiche le menu pause
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
		if (game != null) {
			if (Game.etat == Game.ETAT.PREPARATION) {
				Game.mouseClicked = true;
			}
		}
	}
	/**
	 * This method is called when mouse button is pressed.
	 * 
	 * @param e MouseEvent
	 */
	public void mousePressed(MouseEvent e) {
		if (game != null) {
			if (Game.etat == Game.ETAT.PREPARATION) {
				Game.mousePressed = true;
			}
		}

	}
	/**
	 * This method is called when mouse button is released.
	 * 
	 * @param e MouseEvent
	 */
	public void mouseReleased(MouseEvent e) {
		if (game != null) {
			if (Game.etat == Game.ETAT.PREPARATION) {
				Game.mouseReleased = true;
			}
		}

	}
	
	/**
	 * Cette méthode est appelée quand une action est détectée par un actionListener des boutons
	 * (clic sur un bouton)
	 * 
	 * @param e MouseEvent
	 */
	public void actionPerformed(ActionEvent event) { // clics sur les boutons

		final Object source = event.getSource();
		
		if (source == exit) {
			musiqueMenu.fadeOut();
			System.exit(0);
		} else if (source == lance) {
			if(musiqueMenu.fadeFinished)
				musiqueMenu.fadeOut();
			else musiqueMenu.stop();
			aLance = rand.nextInt(1 - 0 + 1) + 0;
			musiqueLance = new mp3("res/sons/lance"+aLance+".mp3");
			musiqueLance.fadeIn();
			musiqueLance.boucle();
			gameState = GameState.LANCE;
			validate();
		} else if (source == play && nbJoueurs > 1 && niveauChoisi > 0) {
			if(musiqueLance.fadeFinished)
				musiqueLance.fadeOut();
			else musiqueLance.stop();
			musiqueNiveau = new mp3("res/sons/niveau" + niveauChoisi + ".mp3");
			musiqueNiveau.fadeIn();
			musiqueNiveau.boucle();
			gameState = GameState.STARTING;
			validate();
		} else if (source == play2) {
			if(musiqueNiveau.fadeFinished)
				musiqueNiveau.fadeOut();
			else musiqueNiveau.stop();
			musiqueNiveau = new mp3("res/sons/niveau" + niveauChoisi + ".mp3");
			musiqueNiveau.fadeIn();
			musiqueNiveau.boucle();
			gameState = GameState.STARTING;
			validate();
		} else if (source == settings) {
			gameState = GameState.OPTIONS;
			validate();
		} else if (source == menu || source == menu2 || source == menu3 || source == menu4) {
						
			if (source == menu || source == menu4) {
				if(musiqueNiveau.fadeFinished)
					musiqueNiveau.fadeOut();
				else musiqueNiveau.stop();
				aMenu = rand.nextInt(2 - 0 + 1) + 0;
				musiqueMenu = new mp3("res/sons/menu"+aMenu+".mp3");
				musiqueMenu.fadeIn();
				musiqueMenu.boucle();
				Game.etat = Game.ETAT.FIN;
			} else if (source == menu3){
				if(musiqueLance.fadeFinished)
					musiqueLance.fadeOut();
				else musiqueLance.stop();
				aMenu = rand.nextInt(2 - 0 + 1) + 0;
				musiqueMenu = new mp3("res/sons/menu"+aMenu+".mp3");
				musiqueMenu.fadeIn();
				musiqueMenu.boucle();
			}
			gameState = GameState.MAIN_MENU;
			validate();

		} else if (source == reprendre) {
			gameState = GameState.PLAYING;
			Game.etat = old;
			validate();
		} else if (source == j2) {
			nbJoueurs = 2;
		} else if (source == j3) {
			nbJoueurs = 3;
		} else if (source == j4) {
			nbJoueurs = 4;
		} else if (source == n1) {
			niveauChoisi = 1;
		} else if (source == n2) {
			niveauChoisi = 2;
		} else if (source == n3) {
			niveauChoisi = 3;
		} else if (source == n4) {
			niveauChoisi = 4;
		}
	}
	
	
	/**
	 * Cette méthode est appelée quand une action est détectée par un changeListener des sliders
	 * 
	 * @param e MouseEvent
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		final Object source = e.getSource();
		if (source == sliderPoussee) {
			if (!sliderPoussee.getValueIsAdjusting()) {
				final double val = sliderPoussee.getValue();
				Missile.poussee = val / 100;
			}

		}
		if (source == sliderTrajectoire) {
			if (!sliderTrajectoire.getValueIsAdjusting()) {
				final int val = sliderTrajectoire.getValue();
				Missile.nbPoints = val;
			}

		}
	}
}
