
package projetInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Jeu extends JFrame {

	final int MASSE_PLANETE = 500;
	Timer timer; // Horloge de temps avec un intervalle de temps de 40ms
	long temps; // Mesure du temps qui s'�coule
	BufferedImage ArrierePlan; // Buffer pour acc�l�rer la fluidit� des animations
	Graphics buffer; // L'espace graphique associ� � Arri�rePlan
	boolean ToucheHaut; // Si le joueur a press� la touche "haut"
	boolean ToucheBas; // Si le joueur a press� la touche "bas"
	boolean ToucheGauche; // Si le joueur a press� la touche "gauche"
	boolean ToucheDroit; // Si le joueur a press� la touche "droite"
	boolean ToucheEspace; // Si le joueur a press� la touche "barre espace"
	boolean ToucheZ, ToucheQ, ToucheS, ToucheD; // Contr�les du deuxi�me joueur
	Rectangle Ecran; // Les limites de la fen�tre
	Station Vaisseau1, Vaisseau2; // L'objet que l'utilisateur va d�placer
	AstreSpherique Planet1, Planet2, Planet3, Planet4;
	Missile Missile1, Missile2, Missile3, Missile4, Missile5;
	ArrayList<Objet> Objets; // Liste de tous les objets du jeu
	ArrayList<Missile> Missiles; // Liste de tous les missiles
	ArrayList<Trajectoire> Trajectoires; // Liste de toutes les trajectoires
	ArrayList<Station> Stations; // Liste de toutes les stations
	int score; // Score du joueur
	Boolean finjeu; // Jeu fini ou non
	Font font; // Objet de police d'�criture
	String[] NomImage = {"planete.png"};
	String[] NomImageM = {"missile3_1.png","missile3_2.png","missile3_3.png","missile3_4.png","missile3_5.png","missile3_6.png","missile3_7.png","missile3_8.png","missile3_9.png","missile3_10.png"};
	Trajectoire Trajectoire1, Trajectoire2, Trajectoire3, Trajectoire4, Trajectoire5;
	boolean Click; // Si le joueur definit la trajectoire de son missile
	boolean DebutTour;
	double mx,my;
	int compt;

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
		ToucheZ = ToucheQ = ToucheS = ToucheD = false; 
		temps = 0;
		score = 0;
		finjeu = false;
		DebutTour=false;
		Click=false;
		Ecran = new Rectangle(getInsets().left, getInsets().top, getSize().width
				- getInsets().right - getInsets().left, getSize().height - getInsets().bottom
				- getInsets().top);
		ArrierePlan = new BufferedImage(getSize().width, getSize().height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		timer = new Timer(10, new TimerAction()); // Timer � 10ms, normalement fluide

		Objets = new ArrayList<Objet>(); // Cr�er la liste chain�e en m�moire
		Missiles = new ArrayList<Missile>(); // Cr�er la liste chain�e en m�moire
		Trajectoires = new ArrayList<Trajectoire>(); // Cr�er la liste chain�e en m�moire
		Stations = new ArrayList<Station>(); // Cr�er la liste chain�e en m�moire
		
		//Planet1 = new AstreSpherique(300, 500, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		//Planet2 = new AstreSpherique(1000, 500, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		//Planet3 = new AstreSpherique(750, 100, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		Planet4 = new AstreSpherique(650, 360, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		//Objets.add(Planet1);
		//Objets.add(Planet2);
		//Objets.add(Planet3);
		Objets.add(Planet4);
		
		Vaisseau1 = new Station(100, 100, Ecran,"DeathStar 1");
		Vaisseau2 = new Station((int) (Ecran.getWidth() - 100), (int) (Ecran.getHeight() - 100), Ecran,
				"DeathStar 2");
		Objets.add(Vaisseau1);
		Objets.add(Vaisseau2);
		Stations.add(Vaisseau1);
		Stations.add(Vaisseau2);
		

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
		
		try { // R�cup�ration de la police d'�criture
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/Coalition.ttf"));
		} catch (Exception err) {
			System.out.println("Police d'�criture introuvable !");
		}
		if (font != null)
			buffer.setFont(font.deriveFont(40.0f));
		this.addKeyListener(new Jeu_this_keyAdapter()); // Ajout du KeyListener pour entr�es clavier
		this.addMouseListener( new gestionSouris() );
	    this.addMouseMotionListener( new gestionSouris() );
		this.setVisible(true); // Rend la fen�tre visible
		timer.start(); // Lance le timer
	}

	public void paint(Graphics g) {
		// remplir le buffer de noir
		buffer.setColor(Color.BLACK);

		buffer.fillRect((int)Ecran.getX(), (int) Ecran.getY(), (int) (Ecran.getX() + Ecran.getWidth()), (int) (Ecran.getY() + Ecran.getHeight()));

		buffer.setColor(Color.white);
		buffer.setFont(font.deriveFont(100.0f));
		if (finjeu) {
			// Message de fin de jeu
			buffer.setColor(Color.white);
			buffer.setFont(font.deriveFont(100.0f));
			buffer.drawString("GAME OVER", 100, (int) Ecran.getHeight() / 2 + 20);
		} else {
			// dessine TOUS les objets dans le buffer
			for (int k = 0; k < Objets.size(); k++) {
				Objet O = (Objet) Objets.get(k);
				O.draw(temps, buffer);
			}
			

		}
		g.drawImage(ArrierePlan, 0, 0, this);
		
	}

	public void boucle_principale_jeu() {
		
		if(DebutTour==false){
			
			System.out.println("Tour de pr�paration");
			boolean tirCree=false;
			for (int i=0; i<Stations.size(); i++){
				System.out.println("Station n�"+(i+1));
				if(tirCree==false){
					if (Stations.get(i).tirFait==false){
						System.out.println("Tir pas fait");
						if (Click){
							String nom = "Missile Station n�"+(i+1);
							Missile missile = new Missile((int)(Stations.get(i).centreG.x), (int)(Stations.get(i).centreG.y), (float)((mx-Stations.get(i).centreG.x)/100), (float)((my-Stations.get(i).centreG.y)/100), Ecran, nom, Color.RED);
							Objets.add(missile);
							Missiles.add(missile);
							Stations.get(i).tirFait = true;
							System.out.println("Missile cr��, tir fait");
							//timer.stop();
							compt++;
							tirCree = true;
							Click=false;
						}
					} else {
						System.out.println("Tir d�j� fait");
					}
				} else {
					tirCree=false;
				}
			}
			if (compt==Stations.size()){
				DebutTour=true;
				System.out.println("D�but du jeu");
			}
			
		}
					
		else {
		
			// d�placement du Vaisseau 1 (prise en compte des actions utilisateurs sur touches)
			if (ToucheGauche) {
				Vaisseau1.dx = -1;
				Vaisseau1.dy = 0;
			} else if (ToucheDroit) {
				Vaisseau1.dx = +1;
				Vaisseau1.dy = 0;
			} else if (ToucheHaut) {
				Vaisseau1.dx = 0;
				Vaisseau1.dy = -1;
			} else if (ToucheBas) {
				Vaisseau1.dx = 0;
				Vaisseau1.dy = +1;
			} else {
				Vaisseau1.dx = 0;
				Vaisseau1.dy = 0;
			}
			// d�placement du Vaisseau 2 (prise en compte des actions utilisateurs sur touches)
			if (ToucheQ) {
				Vaisseau2.dx = -1;
				Vaisseau2.dy = 0;
			} else if (ToucheD) {
				Vaisseau2.dx = +1;
				Vaisseau2.dy = 0;
			} else if (ToucheZ) {
				Vaisseau2.dx = 0;
				Vaisseau2.dy = -1;
			} else if (ToucheS) {
				Vaisseau2.dx = 0;
				Vaisseau2.dy = +1;
			} else {
				Vaisseau2.dx = 0;
				Vaisseau2.dy = 0;
			}
			// d�place tous les objets par Polymorphisme
			for (int k = 0; k < Objets.size(); k++) {
				Objet O = (Objet) Objets.get(k);
				O.move(temps);
			}
			/*
			 * TRAJECTOIRES 
			 * 
			 * 
			 */
			
			for(int i=0; i<Trajectoires.size(); i++){
				Trajectoires.get(i).actualisation();
			}
			
			for(int i=0; i<Missiles.size(); i++){
				if(Missiles.get(i).Collision() != Missiles.get(i)){
					System.out.println("Collision de Missile "+(i+1)+" avec " +Missiles.get(i).Collision().nom_objet);
					//timer.stop();				
					//timer.start();
				}
			}
			
			
			

		}
		
		repaint(); // appel implicite de la m�thode paint pour raffraichir la zone d'affichage
		
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Jeu monJeu = new Jeu();
	}

	private class TimerAction implements ActionListener {

		// ActionListener appelee toutes les 10 millisecondes
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
				case KeyEvent.VK_Q: // fl�che gauche
					ToucheQ = true;
					break;
				case KeyEvent.VK_D: // fl�che droit
					ToucheD = true;
					break;
				case KeyEvent.VK_Z: // fl�che haut
					ToucheZ = true;
					break;
				case KeyEvent.VK_S: // fl�che bas
					ToucheS = true;
					break;
			}
			// pour tester les �couteurs
			// this.setTitle("Code clavier :"+Integer.toString(code));
		}

		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			switch (code) {
				case KeyEvent.VK_SPACE: // barre d'espacement
					ToucheEspace = false;
					break;
				case KeyEvent.VK_LEFT: // fl�che gauche
					ToucheGauche = false;
					break;
				case KeyEvent.VK_RIGHT: // fl�che droit
					ToucheDroit = false;
					break;
				case KeyEvent.VK_UP: // fl�che haut
					ToucheHaut = false;
					break;
				case KeyEvent.VK_DOWN: // fl�che bas
					ToucheBas = false;
					break;
				case KeyEvent.VK_Q: // fl�che gauche
					ToucheQ = false;
					break;
				case KeyEvent.VK_D: // fl�che droit
					ToucheD = false;
					break;
				case KeyEvent.VK_Z: // fl�che haut
					ToucheZ = false;
					break;
				case KeyEvent.VK_S: // fl�che bas
					ToucheS = false;
					break;
			// pour tester les �couteurs
			// this.setTitle("touche relach�e");
			}
		}
	}
	
	private class gestionSouris implements MouseListener, MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			Click=true;
			mx=e.getX();   my=e.getY(); 
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
} // Fin classe Jeu !!
