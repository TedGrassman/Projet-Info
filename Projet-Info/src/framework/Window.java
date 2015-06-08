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
		// D�finit le titre de la fen�tre
		setTitle("Space War");
		try { // R�cup�ration de l'ic�ne du programme
			setIconImage(ImageIO.read(new File("res/icon_32x32.png")));
		} catch (final Exception err) {
			System.err.println("Ic�ne introuvable !");
		}

		// D�finit la taille de la fen�tre
		if (true) // Mode plein �cran
		{
			// Enl�ve la "d�coration" (barres sur les c�t�s)
			setUndecorated(true);
			// Met la fen�tre en plein �cran (taille maximale du moniteur)
			setExtendedState(MAXIMIZED_BOTH);
			setResizable(false);
		} else // Mode fen�tr�
		{
			// Taille de la fen�tre
			this.setSize((int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth(),
					(int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
			// Met la fen�tre au centre de l'�cran
			setLocationRelativeTo(null);
			// La fen�tre peut �tre redimensionn�e (gestion dans Framework et dans Window)
			setResizable(true);
		}

		// Ferme l'application quand le joueur ferme la fen�tre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arr�t du thread � la fermeture de la fen�tre

		// Cr�e l'instance du Framework.java qui �tend
		// Canvas.java et le met dans la fen�tre Window
		this.setLayout(new BorderLayout());
		add(new Framework(), BorderLayout.CENTER);
		setVisible(true);
		
		// Gestion du redimensionnement de la fen�tre (pas utile en plein �cran)
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

		// M�thode Main. C'est elle qui est lance le jeu gr�ce � une interface Runnable
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Window();
			}
		});
	}

}
