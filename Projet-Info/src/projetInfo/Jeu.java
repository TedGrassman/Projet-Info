
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
	long temps; // Mesure du temps qui s'écoule
	BufferedImage ArrierePlan; // Buffer pour accélérer la fluidité des animations
	Graphics buffer; // L'espace graphique associé à ArrièrePlan
	boolean ToucheHaut; // Si le joueur a pressé la touche "haut"
	boolean ToucheBas; // Si le joueur a pressé la touche "bas"
	boolean ToucheGauche; // Si le joueur a pressé la touche "gauche"
	boolean ToucheDroit; // Si le joueur a pressé la touche "droite"
	boolean ToucheEspace; // Si le joueur a pressé la touche "barre espace"
	Rectangle Ecran; // Les limites de la fenêtre
	// Navire Vaisseau; // L'objet que l'utilisateur va déplacer
	LinkedList<Objet> Objets; // Liste de tous les objets du jeu
	int score; // Score du joueur
	Boolean finjeu; // Jeu fini ou non
	Font font; // Objet de police d'écriture

	public Jeu() {
		this.setSize(1366, 720); // Définition de la fenêtre (HD)
		this.setResizable(false); // Fenêtre fixe (pour le moment)
		this.setLocationRelativeTo(null); // Localisation de la fenêtre
		this.setTitle("Projet de la Mort"); // Titre de la fenêtre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arrêt du thread à la fermeture de la fenêtre
		
		try { // Récupération de l'icône du programme
			this.setIconImage(ImageIO.read(new File("res/icon_32x32.png")));
		} catch (Exception err) {
			System.out.println("Icône introuvable !");
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
		timer = new Timer(10, new TimerAction()); // Timer à 10ms, normalement fluide
		
		Objets = new LinkedList<Objet>(); // Créer la liste chainée en mémoire
		
		try { // Récupération de la police d'écriture
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/Coalition.ttf"));
		} catch (Exception err) {
			System.out.println("Police d'écriture introuvable !");
		}
		if(font != null) buffer.setFont(font.deriveFont(40.0f));
		
		this.addKeyListener(new Jeu_this_keyAdapter()); // Ajout du KeyListener pour entrées clavier
		
		this.setVisible(true); // Rend la fenêtre visible
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
		repaint(); // appel implicite de la méthode paint pour raffraichir la zone d'affichage
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

		public void keyPressed(KeyEvent e) { // Action quand une touche est pressée
			int code = e.getKeyCode();
			switch (code) {
			case KeyEvent.VK_SPACE: // barre d'espacement
				ToucheEspace = true;
				break;
			case KeyEvent.VK_LEFT: // flèche gauche
				ToucheGauche = true;
				break;
			case KeyEvent.VK_RIGHT: // flèche droit
				ToucheDroit = true;
				break;
			case KeyEvent.VK_UP: // flèche haut
				ToucheHaut = true;
				break;
			case KeyEvent.VK_DOWN: // flèche bas
				ToucheBas = true;
				break;
			case KeyEvent.VK_ESCAPE: // Touche ESCAPE pour sortir du programme
				System.out.println("ARRÊT");
				System.exit(0);
				break;
			case KeyEvent.VK_PAUSE: // Touche PAUSE pour arrêter ou relancer le timer
				if (timer.isRunning()) {
					timer.stop();
					System.out.println("PAUSE");
				} else {
					timer.start();
					System.out.println("REPRISE");
				}
				break;
			}
			// pour tester les écouteurs
			// this.setTitle("Code clavier :"+Integer.toString(code));
		}

		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			switch (code) {
				case KeyEvent.VK_SPACE: // barre d'espacement
					ToucheEspace = true;
					break;
				case KeyEvent.VK_LEFT: // flèche gauche
					ToucheGauche = true;
					break;
				case KeyEvent.VK_RIGHT: // flèche droit
					ToucheDroit = true;
					break;
				case KeyEvent.VK_UP: // flèche haut
					ToucheHaut = true;
					break;
				case KeyEvent.VK_DOWN: // flèche bas
					ToucheBas = true;
					break;
			// pour tester les écouteurs
			// this.setTitle("touche relachée");
			}
		}
		
	}


	
} // Fin classe Jeu !!