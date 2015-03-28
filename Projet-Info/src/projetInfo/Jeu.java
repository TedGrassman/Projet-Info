
package projetInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Jeu extends JFrame {

	Timer timer; // Horloge de temps avec un intervalle de temps de 40ms
	long temps; // Mesure du temps qui s'�coule
	BufferedImage ArrierePlan; // Buffer pour acc�l�rer la fluidit� des animations
	Graphics buffer; // L'espace graphique associ� � Arri�rePlan
	boolean ToucheHaut; // Si le joueur a press� la touche "haut"
	boolean ToucheBas; // Si le joueur a press� la touche "bas"
	boolean ToucheGauche; // Si le joueur a press� la touche "gauche"
	boolean ToucheDroit; // Si le joueur a press� la touche "droite"
	boolean ToucheEspace; // Si le joueur a press� la touche "barre espace"
	Rectangle Ecran; // Les limites de la fen�tre
	// Navire Vaisseau; // L'objet que l'utilisateur va d�placer
	LinkedList<Objet> Objets; // Liste de tous les objets du jeu
	int score; // Score du joueur
	Boolean finjeu; // Jeu fini ou non
	Font font; // Objet de police d'�criture

	public Jeu() {
		this.setSize(1366, 720); // D�finition de la fen�tre (HD)
		this.setResizable(false); // Fen�tre fixe (pour le moment)
		this.setLocationRelativeTo(null); // Localisation de la fen�tre
		this.setTitle("Projet de la Mort"); // Titre de la fen�tre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arr�t du thread � la fermeture de la fen�tre
		
		try { // R�cup�ration de l'ic�ne du programme
			this.setIconImage(ImageIO.read(new File("res/icon_32x32.png")));
		} catch (Exception err) {
			System.out.println("Ic�ne introuvable !");
		}
		
		ToucheHaut = ToucheBas = ToucheGauche = ToucheDroit = ToucheEspace = false;
		temps = 0;
		score = 0;
		finjeu = false;
		Ecran = new Rectangle(getInsets().left, getInsets().top, getSize().width
				- getInsets().right - getInsets().left, getSize().height - getInsets().bottom
				- getInsets().top);
		ArrierePlan = new BufferedImage(getSize().width, getSize().height,
				BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		timer = new Timer(10, new TimerAction()); // Timer � 10ms, normalement fluide
		
		Objets = new LinkedList<Objet>(); // Cr�er la liste chain�e en m�moire
		
		try { // R�cup�ration de la police d'�criture
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/Coalition.ttf"));
		} catch (Exception err) {
			System.out.println("Police d'�criture introuvable !");
		}
		if(font != null) buffer.setFont(font.deriveFont(40.0f));
		
		this.addKeyListener(new Jeu_this_keyAdapter()); // Ajout du KeyListener pour entr�es clavier
		
		this.setVisible(true); // Rend la fen�tre visible
		timer.start(); // Lance le timer
	
	}
	
	public void paint(Graphics g) {

		// remplire le buffer de noir
		buffer.setColor(Color.black);
		buffer.fillRect(Ecran.x, Ecran.y, Ecran.x + Ecran.width, Ecran.y
				+ Ecran.height);
		
		buffer.setColor(Color.white);
		buffer.setFont(font.deriveFont(100.0f));
		buffer.drawString("WelcomE", 275, Ecran.height/2+20);

//		if (finjeu) {
//			// Message de fin de jeu
//			buffer.setColor(Color.white);
//			buffer.setFont(font.deriveFont(100.0f));
//			buffer.drawString("GAME OVER", 100, Ecran.height/2+20);
//		} else {
//			// dessine TOUS les objets dans le buffer
//			for (int k = 0; k < Objets.size(); k++) {
//				Objet O = (Objet) Objets.get(k);
//				O.draw(temps, buffer);
//			}
//		}
		// Ecris le score et le nombre de vies restantes, et le temps
//		buffer.setColor(Color.white);
//		buffer.setFont(font.deriveFont(40.0f));
//		buffer.drawString("SCORE : " + score, 15, Ecran.height - 15);
//		buffer.drawString("VIES : " + nombreViesRestantes, Ecran.width - 155,
//				Ecran.height - 15);
//		buffer.setFont(font.deriveFont(30.0f));
//		buffer.drawString("Temps : " + temps, Ecran.width - 175, 50);

		// dessine une seule fois le buffer dans le Panel
		g.drawImage(ArrierePlan, 0, 0, this);
	}
	
	public void boucle_principale_jeu() {
		repaint(); // appel implicite de la m�thode paint pour raffraichir la zone d'affichage
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Jeu monJeu = new Jeu();
	}
	
	
	private class TimerAction implements ActionListener {
		// ActionListener appelee toutes les 100 millisecondes
		public void actionPerformed(ActionEvent e) {
			boucle_principale_jeu();
			temps++;
		}
	}
	
	private class Jeu_this_keyAdapter extends KeyAdapter {

		public void keyPressed(KeyEvent e) { // Action quand une touche est press�e
			int code = e.getKeyCode();
			switch (code) {
			case KeyEvent.VK_SPACE: // barre d'espacement
				ToucheEspace = true;
				break;
			case KeyEvent.VK_LEFT: // fl�che gauche
				ToucheGauche = true;
				break;
			case KeyEvent.VK_RIGHT: // fl�che droit
				ToucheDroit = true;
				break;
			case KeyEvent.VK_UP: // fl�che haut
				ToucheHaut = true;
				break;
			case KeyEvent.VK_DOWN: // fl�che bas
				ToucheBas = true;
				break;
			case KeyEvent.VK_ESCAPE: // Touche ESCAPE pour sortir du programme
				System.out.println("ARR�T");
				System.exit(0);
				break;
			case KeyEvent.VK_PAUSE: // Touche PAUSE pour arr�ter ou relancer le timer
				if (timer.isRunning()) {
					timer.stop();
					System.out.println("PAUSE");
				} else {
					timer.start();
					System.out.println("REPRISE");
				}
				break;
			}
			// pour tester les �couteurs
			// this.setTitle("Code clavier :"+Integer.toString(code));
		}

		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			switch (code) {
				case KeyEvent.VK_SPACE: // barre d'espacement
					ToucheEspace = true;
					break;
				case KeyEvent.VK_LEFT: // fl�che gauche
					ToucheGauche = true;
					break;
				case KeyEvent.VK_RIGHT: // fl�che droit
					ToucheDroit = true;
					break;
				case KeyEvent.VK_UP: // fl�che haut
					ToucheHaut = true;
					break;
				case KeyEvent.VK_DOWN: // fl�che bas
					ToucheBas = true;
					break;
			// pour tester les �couteurs
			// this.setTitle("touche relach�e");
			}
		}
		
	}


	
} // Fin classe Jeu !!