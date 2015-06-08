package framework;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
 * 
 * @author www.gametutorial.net
 */

@SuppressWarnings("serial")
public class Window extends JFrame {

	@SuppressWarnings("unused")
	public Window() {
		// Définit le titre de la fenêtre
		setTitle("Space War");
		try { // Récupération de l'icône du programme
			setIconImage(ImageIO.read(new File("res/icon_32x32.png")));
		} catch (final Exception err) {
			System.err.println("Icône introuvable !");
		}

		// Définit la taille de la fenêtre
		if (true) // Mode plein écran
		{
			// Enlève la "décoration" (barres sur les côtés)
			setUndecorated(true);
			// Met la fenêtre en plein écran (taille maximale du moniteur)
			setExtendedState(MAXIMIZED_BOTH);
			setResizable(false);
		} else // Mode fenêtré
		{
			// Taille de la fenêtre
			this.setSize((int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth(),
					(int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
			// Met la fenêtre au centre de l'écran
			setLocationRelativeTo(null);
			// La fenêtre peut être redimensionnée (gestion dans Framework et dans Window)
			setResizable(true);
		}

		// Ferme l'application quand le joueur ferme la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arrêt du thread à la fermeture de la fenêtre

		// Crée l'instance du Framework.java qui étend
		// Canvas.java et le met dans la fenêtre Window
		this.setLayout(new BorderLayout());
		add(new Framework(), BorderLayout.CENTER);
		setVisible(true);
		
		// Gestion du redimensionnement de la fenêtre (pas utile en plein écran)
		addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				Framework.resized = true;

				// if(Framework.gameState==Framework.GameState.PLAYING){
				Framework.gameState = Framework.GameState.VISUALIZING;
				// }
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void main(String[] args) {

		// Méthode Main. C'est elle qui est lance le jeu grâce à une interface Runnable
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Window();
			}
		});
	}

}
