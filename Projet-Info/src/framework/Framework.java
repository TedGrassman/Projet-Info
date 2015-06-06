
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
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

/**
 * Framework that controls the game (Game.java) that created it, update it and
 * draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

@SuppressWarnings("serial")
public class Framework extends Canvas {
	public static int nbJoueurs, niveauChoisi;
	mp3 musiqueMenu, musiqueNiveau, musiqueLance;
	JPanel panel = new JPanel();
	JPanel menuPrincipal = new JPanel(), menuPause = new JPanel(), menuOptions = new JPanel(),
			menuLance = new JPanel(), menuLoading = new JPanel();
	JPanel boutonsRonds = new JPanel(), boutonsNiveau = new JPanel();
	CardLayout layout = new CardLayout();
	Game.ETAT old = Game.ETAT.PREPARATION; // variable permettant de stocker l'etat du jeu lors d'une mise en pause
	Image bg;
	customButton play, reprendre, settings, exit, menu, menu2, menu3, lance;
	JSlider sliderPoussee, sliderTrajectoire;
	customText textMenu, textOptions, textLoading, poussee, trajectoire, joueurs, niveau;
	roundButton j2, j3, j4, n1, n2, n3, n4;

	public static boolean resized = false; // indique si la fenetre vient d'�tre
											// redimensionn�e

	public static int gauche, haut, droite, bas; // taille des bords de la
													// fenetre
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
		STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, LANCE, DESTROYED, PAUSE
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
		menuLoading.setLayout((new BoxLayout(menuLoading, BoxLayout.Y_AXIS)));

		play = new customButton("Commencer le jeu", 0); // initialise les boutons
		play.setAlignmentX(Component.CENTER_ALIGNMENT); // centre horizontalement les boutons
		lance = new customButton("Lancer une partie", 0);
		lance.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		
		//Initialise zones de texte
		textMenu = new customText("MENU PRINCIPAL", 60);
		textMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		textOptions = new customText("OPTIONS", 60);
		textOptions.setAlignmentX(Component.CENTER_ALIGNMENT);

		textLoading = new customText("CHARGEMENT", 60);
		textLoading.setAlignmentX(Component.CENTER_ALIGNMENT);
		//textLoading.setAlignmentY(Component.CENTER_ALIGNMENT);
		poussee = new customText("Force de pouss�e des missiles", 30);
		poussee.setAlignmentX(Component.CENTER_ALIGNMENT);
		trajectoire = new customText("Longueur de la trajectoire des missiles", 30);
		trajectoire.setAlignmentX(Component.CENTER_ALIGNMENT);
		joueurs = new customText("Nombre de joueurs", 30);
		joueurs.setAlignmentX(Component.CENTER_ALIGNMENT);
		niveau = new customText("Choix du niveau", 30);
		niveau.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// slider horizontal, min 0, max 10, d�faut � 5
		sliderPoussee = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 5);
		sliderPoussee.setOpaque(false); // fond transparent
		sliderPoussee.setAlignmentX(Component.CENTER_ALIGNMENT);
		sliderPoussee.setMajorTickSpacing(1); // espacement et dessin des crans
		sliderPoussee.setPaintTicks(true);
		sliderPoussee.setPaintLabels(true); // dessin des chiffres sous les crans
		sliderPoussee.setForeground(Color.WHITE);
		
		// slider horizontal, min 0, max 10, d�faut � 5
		sliderTrajectoire = new JSlider(SwingConstants.HORIZONTAL, 50, 500, 100); 
		sliderTrajectoire.setOpaque(false); // fond transparent
		sliderTrajectoire.setAlignmentX(Component.CENTER_ALIGNMENT);
		sliderTrajectoire.setMajorTickSpacing(50); // espacement et dessin des crans
		sliderTrajectoire.setMinorTickSpacing(10);
		sliderTrajectoire.setPaintTicks(true);
		sliderTrajectoire.setPaintLabels(true); // dessin des chiffres sous les crans
		sliderTrajectoire.setForeground(Color.WHITE);
		
		// roundButtons
		j2 = new roundButton("2", 0);
		j3 = new roundButton("3", 0);
		j4 = new roundButton("4", 0);
		n1 = new roundButton("Niv1", 1);
		n2 = new roundButton("Niv2", 1);
		n3 = new roundButton("Niv3", 1);
		n4 = new roundButton("Niv4", 1);
		// n5 = new roundButton("Niv5", 1);



		play.addActionListener(this); // cr�e les listeners
		settings.addActionListener(this);
		exit.addActionListener(this);
		reprendre.addActionListener(this);
		menu.addActionListener(this);
		menu2.addActionListener(this);
		menu3.addActionListener(this);
		lance.addActionListener(this);
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
		menuPrincipal.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuPrincipal.add(exit);
		menuPrincipal.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuPause.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuPause.add(reprendre);
		menuPause.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuPause.add(menu);
		menuPause.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuOptions.add(textOptions);
		menuOptions.add(poussee);
		menuOptions.add(sliderPoussee);
		menuOptions.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuOptions.add(trajectoire);
		menuOptions.add(sliderTrajectoire);
		menuOptions.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuOptions.add(menu2);
		
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
		menuLance.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 15), new Dimension(0, 20)));
		menuLance.add(menu3);
		
		//menuLoading.setAlignmentX(frameHeight/2);
		//menuLoading.add(new Box.Filler(new Dimension(500, frameHeight/2), new Dimension(500, frameHeight/2), new Dimension(500, frameHeight)));
		menuLoading.add(textLoading);
			

		panel.setLayout(layout); // d�finit le layout du panel principal en type "card"
		panel.setOpaque(false); // arri�re plan transparent

		menuPrincipal.setOpaque(false);
		menuPause.setOpaque(false);
		menuOptions.setOpaque(false);
		menuLance.setOpaque(false);
		menuLoading.setOpaque(false);
		boutonsRonds.setOpaque(false);
		boutonsNiveau.setOpaque(false);

		panel.add(menuPrincipal, "mDepart"); // ajoute les cartes au panel principal
		panel.add(menuPause, "mPause");
		panel.add(menuOptions, "mOptions");
		panel.add(menuLance, "mLance");
		panel.add(menuLoading, "mLoading");
		layout.show(panel, "mDepart"); // affiche la premi�re carte
		add(panel); // ajoute le panel au framework

		musiqueMenu = new mp3("res/sons/menu.mp3");
		musiqueLance = new mp3("res/sons/lance.mp3");

		gameState = GameState.VISUALIZING;

		// We start game in new thread.
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
				musiqueMenu.fadeOut();
				// When all things that are called above finished, we change
				// game status to main menu.
				gameState = GameState.PLAYING;
				newGame();
				break;
			case VISUALIZING:
				// On Ubuntu OS (when I tested on my old computer)
				// this.getWidth() method doesn't return the correct value
				// immediately (eg. for frame that should be 800px width,
				// returns 0 than 790 and at last 798px).
				// So we wait one second for the window/frame to be set to its
				// correct size. Just in case we
				// also insert 'this.getWidth() > 1' condition in case when the
				// window/frame size wasn't set in time,
				// so that we although get approximately size.
				if (getWidth() > 1 && visualizingTime > secInNanosec / 2) {
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
					} else {
						gameState = GameState.MAIN_MENU;
						musiqueMenu.jouer();
						musiqueMenu.boucle();
					}

				} else {
					visualizingTime += System.nanoTime() - lastVisualizingTime;
					lastVisualizingTime = System.nanoTime();
				}
				break;
			}

			// Repaint the screen.
			repaint();

			// Here we calculate the time that defines for how long we should
			// put threat to sleep to meet the GAME_FPS.
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In
																			// milliseconds
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
			layout.show(panel, "mLoading");
			panel.setVisible(true);
			//g2d.drawString("CHARGEMENT", frameWidth - 200, frameHeight - 50);
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
		if (game == null) {
			game = new Game();
		} else {
			game.RestartGame();
		}
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
		game.UpdateGame(gameTime, mousePosition());
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
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // si la touche �chap est press�e...
			// System.out.println(gameState); 									DEBBUGING
			if (game != null) { // on v�rifie que le jeu existe
				if (gameState == GameState.PAUSE) { // si le jeu est en pause...
					Game.etat = old; // on restaure son �tat d'avant la pause
					gameState = GameState.PLAYING; // et on le relance
					return; // on force le retour car sinon le "if" suivant senlance imm�diatement
				}
				if (gameState == GameState.PLAYING) { // si on joue
					old = Game.etat; // on stocke l'etat dans lequel �tait le jeu
					Game.etat = Game.ETAT.PAUSE; // on le met en pause
					gameState = GameState.PAUSE; // on affiche le menu pause
				}
			}
		}

	}

	/**
	 * This method is called when mouse button is clicked.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (game != null) {
			if (Game.etat == Game.ETAT.PREPARATION) {
				Game.mouseClicked = true;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		if (game != null) {
			if (Game.etat == Game.ETAT.PREPARATION) {
				Game.mousePressed = true;
			}
		}

	}

	public void mouseReleased(MouseEvent e) {
		if (game != null) {
			if (Game.etat == Game.ETAT.PREPARATION) {
				Game.mouseReleased = true;
			}
		}

	}

	public void actionPerformed(ActionEvent event) { // clics sur les boutons

		final Object source = event.getSource();

		if (source == exit) {
			musiqueMenu.fadeOut();
			//musiqueMenu.stop();
			System.exit(0);
		} else if (source == lance) {
			musiqueMenu.fadeOut();
			//musiqueMenu.stop();
			musiqueLance.fadeIn();
			//musiqueLance.jouer();
			musiqueLance.boucle();
			gameState = GameState.LANCE;
			validate();
		} else if (source == play && nbJoueurs > 1 && niveauChoisi > 0) {
			musiqueLance.fadeOut();
			//musiqueLance.stop();
			musiqueNiveau = new mp3("res/sons/niveau" + niveauChoisi + ".mp3");
			musiqueNiveau.fadeIn();
			//musiqueNiveau.jouer();
			musiqueNiveau.boucle();
			gameState = GameState.STARTING;
			validate();
		} else if (source == settings) {
			gameState = GameState.OPTIONS;
			validate();
		} else if (source == menu || source == menu2 || source == menu3) {

			if (source == menu) {
				musiqueNiveau.fadeOut();
				//musiqueNiveau.stop();
				musiqueMenu.fadeIn();
				//musiqueMenu.jouer();
				musiqueMenu.boucle();
				Game.etat = Game.ETAT.FIN;
			} else if (source == menu3){
				musiqueLance.fadeOut();
				//musiqueLance.stop();
				musiqueMenu.fadeIn();
				//musiqueMenu.jouer();
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
