
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
	long temps; // Mesure du temps qui s'écoule
	BufferedImage ArrierePlan; // Buffer pour accélérer la fluidité des animations
	Graphics buffer; // L'espace graphique associé à ArrièrePlan
	boolean ToucheHaut; // Si le joueur a pressé la touche "haut"
	boolean ToucheBas; // Si le joueur a pressé la touche "bas"
	boolean ToucheGauche; // Si le joueur a pressé la touche "gauche"
	boolean ToucheDroit; // Si le joueur a pressé la touche "droite"
	boolean ToucheEspace; // Si le joueur a pressé la touche "barre espace"
	boolean ToucheZ, ToucheQ, ToucheS, ToucheD; // Contrôles du deuxième joueur
	Rectangle Ecran; // Les limites de la fenêtre
	Station Station1, Station2; // L'objet que l'utilisateur va déplacer
	AstreSpherique Planet1, Planet2, Planet3, Planet4;
	Missile Missile1, Missile2, Missile3, Missile4, Missile5;
	ArrayList<Objet> Objets; // Liste de tous les objets du jeu
	ArrayList<Missile> Missiles; // Liste de tous les missiles
	ArrayList<Trajectoire> Trajectoires; // Liste de toutes les trajectoires
	ArrayList<Station> Stations; // Liste de toutes les stations
	int score; // Score du joueur
	boolean finJeu; // Jeu fini ou non
	Font font1, font2; // Objet de police d'écriture
	String[] NomImage = {"planete.png"};
	String[] NomImageM = {"missile2.png"};
	Trajectoire Trajectoire1, Trajectoire2, Trajectoire3, Trajectoire4, Trajectoire5;
	boolean mouseClicked, mousePressed, mouseReleased; // Si le joueur definit la trajectoire de son missile
	boolean debutTour;
	double mx,my;
	int compt;
	Station stationCourante;
	boolean vecteurMissile = false;
	String winner = "";
	String load = "";

	public Jeu() {
		this.setSize(1366, 720);	// Définition de la fenêtre (HD)
		this.setResizable(false);	// Fenêtre fixe (pour le moment)
		this.setLocationRelativeTo(null);			// Localisation de la fenêtre
		this.setTitle("Projet de la Mort");			// Titre de la fenêtre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// Arrêt du thread à la fermeture de la fenêtre
		try {						// Récupération de l'icône du programme
			this.setIconImage(ImageIO.read(new File("res/icon_32x32.png")));
		} catch (Exception err) {
			System.err.println("Icône introuvable !");
		}
		ToucheHaut = ToucheBas = ToucheGauche = ToucheDroit = ToucheEspace = false;
		ToucheZ = ToucheQ = ToucheS = ToucheD = false; 
		temps = 0;
		score = 0;
		finJeu = false;
		debutTour=false;
		mouseClicked = mousePressed = mouseReleased = false;
		Ecran = new Rectangle(getInsets().left, getInsets().top, getSize().width
				- getInsets().right - getInsets().left, getSize().height - getInsets().bottom
				- getInsets().top);
		ArrierePlan = new BufferedImage(getSize().width, getSize().height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		timer = new Timer(10, new TimerAction()); // Timer à 10ms, normalement fluide

		Objets = new ArrayList<Objet>(); // Créer la liste chainée en mémoire
		Missiles = new ArrayList<Missile>(); // Créer la liste chainée en mémoire
		Trajectoires = new ArrayList<Trajectoire>(); // Créer la liste chainée en mémoire
		Stations = new ArrayList<Station>(); // Créer la liste chainée en mémoire
		
		//Planet1 = new AstreSpherique(300, 500, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		//Planet2 = new AstreSpherique(1000, 500, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		//Planet3 = new AstreSpherique(750, 100, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		Planet4 = new AstreSpherique(650, 360, 0f, 0f, NomImage, Ecran, "Planete1", 1, MASSE_PLANETE, 50);
		//Objets.add(Planet1);
		//Objets.add(Planet2);
		//Objets.add(Planet3);
		Objets.add(Planet4);
		
		Station1 = new Station(400, 300, Ecran,"DeathStar 1", Color.red);
		Station2 = new Station((int) (Ecran.getWidth() - 400), (int) (Ecran.getHeight() - 300), Ecran,
				"DeathStar 2", Color.blue);
		Objets.add(Station1);
		Objets.add(Station2);
		Stations.add(Station1);
		Stations.add(Station2);
		
		stationCourante = Stations.get(0);
		
//		Missile1 = new Missile(650, 600, 2.5f, 1f, Ecran, "Missile1", Color.RED);
//		Missile2 = new Missile(700, 700, 1.5f, -1.5f, Ecran, "Missile2", Color.GREEN);
//		Missile3 = new Missile(100, 300, 1f, 2f, Ecran, "Missile3", Color.BLUE);
//		Missile4 = new Missile(300, 650, -0.5f, -1f, Ecran, "Missile4", Color.YELLOW);
//		Missile5 = new Missile(1100, 550, -1.5f, 2f, Ecran, "Missile5", Color.WHITE);
//		
//		Objets.add(Missile1);
//		Objets.add(Missile2);
//		Objets.add(Missile3);
//		Objets.add(Missile4);
//		Objets.add(Missile5);
//		
//		Missiles.add(Missile1);
//		Missiles.add(Missile2);
//		Missiles.add(Missile3);
//		Missiles.add(Missile4);
//		Missiles.add(Missile5);

//		Trajectoire1 = new Trajectoire (Missile1, 50, 0, Color.RED);
//		Trajectoire2 = new Trajectoire (Missile2, 70, 0, Color.GREEN);
//		Trajectoire3 = new Trajectoire (Missile3, 50, 0, Color.BLUE);
//		Trajectoire4 = new Trajectoire (Missile4, 70, 0, Color.YELLOW);
//		Trajectoire5 = new Trajectoire (Missile5, 50, 0, Color.WHITE);
//		
//		Trajectoires.add(Trajectoire1);
//		Trajectoires.add(Trajectoire2);
//		Trajectoires.add(Trajectoire3);
//		Trajectoires.add(Trajectoire4);
//		Trajectoires.add(Trajectoire5);

		compt=0;
		
		try { // Récupération de la police d'écriture
			font1 = Font.createFont(Font.TRUETYPE_FONT, new File("res/Coalition.ttf"));
			font2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/13_Misa.ttf"));
		} catch (Exception err) {
			System.err.println("Police(s) d'écriture introuvable(s) !");
		}
		if (font1 != null)
			buffer.setFont(font1.deriveFont(40.0f));
		this.addKeyListener(new Jeu_this_keyAdapter()); // Ajout du KeyListener pour entrées clavier
		this.addMouseListener( new gestionSouris() );
	    this.addMouseMotionListener( new gestionSouris() );
		this.setVisible(true); // Rend la fenêtre visible
		timer.start(); // Lance le timer
	}

	public void paint(Graphics g) {
		// remplir le buffer de noir
		buffer.setColor(Color.BLACK);
		buffer.fillRect((int)Ecran.getX(), (int) Ecran.getY(), (int) (Ecran.getX() + Ecran.getWidth()), (int) (Ecran.getY() + Ecran.getHeight()));
		buffer.setColor(Color.white);
		buffer.setFont(font1.deriveFont(100.0f));
		
		// dessine TOUS les objets dans le buffer
		for (int k = 0; k < Objets.size(); k++) {
			Objet O = (Objet) Objets.get(k);
			O.draw(temps, buffer, font2);
		}
		
		

//		for(int i=0; i<Trajectoires.size(); i++){
//			Trajectoires.get(i).draw(temps, buffer);		// Trajectoires intégrées à classe Missile, dessinées via le draw de la classe
//		}
		
		for(int i=0; i<Explosion.liste.size(); i++){
			Explosion.liste.get(i).draw(temps, buffer);
		}
		
		if(!debutTour) {
			buffer.setColor(Color.white);
			buffer.setFont(font1.deriveFont(25.0f));
			buffer.drawString("Phase de préparation", 425, 100);
			buffer.setFont(font1.deriveFont(20.0f));
			int compt = 0;
			for (int k = 0; k < Stations.size(); k++) {
				Station O = Stations.get(k);
				buffer.setColor(O.color);
				if (O.tirFait == false && compt == 0) {
					buffer.drawString("Joueur "+(k+1), 575, 150);
					compt++;
				}
			}
			if(vecteurMissile){
				buffer.setColor(Color.white);
				buffer.drawLine((int)(stationCourante.x), (int)(stationCourante.y), (int)(mx), (int)(my));
			}
		} else if (debutTour && !finJeu){
			buffer.setColor(Color.white);
			buffer.setFont(font1.deriveFont(25.0f));
			switch ( (int)(temps) % 100){
				case 0 : load = "."; break;
				case 20 : load = ". ."; break;
				case 40 : load = ". . ."; break;
				case 60 : load = " "; break;
			}
			buffer.drawString("Phase de jeu "+load, 500, 100);
		} else if (finJeu){
			// Message de fin de jeu
			buffer.setColor(Color.white);
			buffer.setFont(font1.deriveFont(100.0f));
			buffer.drawString("GAME OVER", 250, (int) Ecran.getHeight() / 2 - 100);
			buffer.setFont(font1.deriveFont(50.0f));
			for (int k = 0; k < Stations.size(); k++) {
				Station O = Stations.get(k);
				if(O.actif)
					buffer.setColor(O.color);
			}
			buffer.drawString(winner, 350, (int) Ecran.getHeight() / 2 + 150);
		}

		// Ecris le score et le nombre de vies restantes, et le temps
		// buffer.setColor(Color.white);
		// buffer.setFont(font.deriveFont(40.0f));
		// buffer.drawString("SCORE : " + score, 15, Ecran.height - 15);
		// buffer.drawString("VIES : " + nombreViesRestantes, Ecran.width - 155,
		// Ecran.height - 15);
		// buffer.setFont(font.deriveFont(30.0f));
		// buffer.drawString("Temps : " + temps, Ecran.width - 175, 50);
		
		// dessine une seule fois le buffer dans le Panel
		g.drawImage(ArrierePlan, 0, 0, this);
		
	}

	public void boucle_principale_jeu() {
		
		if(debutTour==false && finJeu==false){
			
			System.out.println("Tour de préparation");
			boolean tirCree=false;
			
			for (int i=0; i<Stations.size(); i++){
				System.out.println("Station n°"+(i+1));
				
				if(tirCree==false){
					
					if (Stations.get(i).tirFait==false){
						System.out.println("Tir pas fait");
						
						if(mousePressed){
							stationCourante = Stations.get(i);
							vecteurMissile = true;
							mousePressed = false;
						}
						if(mouseReleased){
							int x, y;
							double angle=Math.atan2(my-stationCourante.y, mx-stationCourante.x);
							x = (int) (stationCourante.x+(stationCourante.l/2 + 25)*Math.cos(angle));
							y = (int) (stationCourante.y+(stationCourante.h/2 + 25)*Math.sin(angle));
							vecteurMissile = false;
							String nom = "Missile Station n°"+(i+1);
							Missile missile = new Missile(x, y, (float)((mx-Stations.get(i).centreG.x)/100), (float)((my-Stations.get(i).centreG.y)/100), Ecran, nom, Stations.get(i).color, Stations.get(i));
							Objets.add(missile);
							Missiles.add(missile);
							Stations.get(i).tirFait = true;
							System.out.println("Missile créé, tir fait");
							//timer.stop();
							compt++;
							tirCree = true;
							mouseReleased=false;
						}
						if (mouseClicked){
							mouseClicked = false;
						}
					} else {
						System.out.println("Tir déjà fait");
					}
				} else {
					tirCree=false;
				}
			}
			if (compt==Stations.size()){
				debutTour=true;
				System.out.println("Début du jeu");
				for (int i=0; i<Stations.size(); i++){
					Stations.get(i).tirFait = false;
				}
				compt=0;
			}
			
		}
					
		else if(debutTour==true && finJeu==false){
		
			// déplacement du Vaisseau 1 (prise en compte des actions utilisateurs sur touches)
			if (ToucheGauche) { Station1.dx = -1; Station1.dy = 0; }
			else if (ToucheDroit) { Station1.dx = +1; Station1.dy = 0; }
			else if (ToucheHaut) { Station1.dx = 0; Station1.dy = -1; }
			else if (ToucheBas) { Station1.dx = 0; Station1.dy = +1; }
			else { Station1.dx = 0; Station1.dy = 0; }
			// déplacement du Vaisseau 2 (prise en compte des actions utilisateurs sur touches)
			if (ToucheQ) { Station2.dx = -1; Station2.dy = 0; }
			else if (ToucheD) { Station2.dx = +1; Station2.dy = 0;}
			else if (ToucheZ) { Station2.dx = 0; Station2.dy = -1; }
			else if (ToucheS) { Station2.dx = 0; Station2.dy = +1; }
			else { Station2.dx = 0; Station2.dy = 0; }
			
			// MOUVEMENTS
			// déplace tous les objets par Polymorphisme
			for (int k = 0; k < Objets.size(); k++) {
				Objet O = (Objet) Objets.get(k);
				O.move(temps);
			}
			
			// COLLISIONS
			for(int i=0; i<Objets.size(); i++){
				Objet O = Objets.get(i);
				Objet OC = Objets.get(i).Collision();
				if(OC != O){
					System.out.println("Collision de " +O.nom_objet+ " avec " +OC.nom_objet);
					//timer.stop();				
					//timer.start();
					O.actif = false;
					if(OC.typeObjet != "AstreSpherique")
						OC.actif = false;
					if(O.typeObjet == "Missile"){
						O.explosion.activer(O.x, O.y, temps);
					}
					if(OC.typeObjet == "Station"){
						OC.explosion.activer(OC.x, OC.y, temps);
					}
				}
			}
			
			
			
		}
		
		//ACTUALISATION des EXPLOSIONS
		for(int i=0; i<Explosion.liste.size(); i++){
			Explosion.liste.get(i).actualisation(temps);
		}
		
		//ACTUALISATION des TRAJECTOIRES
		for(int i=0; i<Trajectoires.size(); i++){
			Trajectoires.get(i).actualisation();
		}
		
		// GARBAGE COLLECTOR
		
		for (int k = 0; k < Objets.size(); k++) {
			Objet O = (Objet) Objets.get(k);
			if (O.actif == false) {
				Objets.remove(k);
				k--; // parceque la liste s'est déplacée pour boucher le trou
			}
		}
		
		for (int k = 0; k < Missiles.size(); k++) {
			Missile O = Missiles.get(k);
			if (O.actif == false) {
				Missiles.remove(k);
				k--; // parceque la liste s'est déplacée pour boucher le trou
			}
		}
		for (int k = 0; k < Objet.liste.size(); k++) {
			Objet O = Objet.liste.get(k);
			if (O.actif == false) {
				Objet.liste.remove(k);
				k--; // parceque la liste s'est déplacée pour boucher le trou
			}
		}
		
		if (Missiles.isEmpty()){
			debutTour = false;
			if(!Stations.get(0).actif || !Stations.get(1).actif){
				debutTour = true;
				finJeu = true;
				if(!Stations.get(0).actif && Stations.get(1).actif)
					winner = "Joueur 2 gagne !";
				if(Stations.get(0).actif && !Stations.get(1).actif)
					winner = "Joueur 1 gagne !";
				if(!Stations.get(0).actif && !Stations.get(1).actif)
					winner = "      Egalité !";
			}
		}
		
//			for (int k = 0; k < Stations.size(); k++) {
//				Station O = Stations.get(k);
//				if (O.actif == false) {
//					Stations.remove(k);
//					k--; // parceque la liste s'est déplacée pour boucher le trou
//				}
//			}

	
	repaint(); // appel implicite de la méthode paint pour raffraichir la zone d'affichage

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
				case KeyEvent.VK_Q: // flèche gauche
					ToucheQ = true;
					break;
				case KeyEvent.VK_D: // flèche droit
					ToucheD = true;
					break;
				case KeyEvent.VK_Z: // flèche haut
					ToucheZ = true;
					break;
				case KeyEvent.VK_S: // flèche bas
					ToucheS = true;
					break;
			}
			// pour tester les écouteurs
			// this.setTitle("Code clavier :"+Integer.toString(code));
		}

		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			switch (code) {
				case KeyEvent.VK_SPACE: // barre d'espacement
					ToucheEspace = false;
					break;
				case KeyEvent.VK_LEFT: // flèche gauche
					ToucheGauche = false;
					break;
				case KeyEvent.VK_RIGHT: // flèche droit
					ToucheDroit = false;
					break;
				case KeyEvent.VK_UP: // flèche haut
					ToucheHaut = false;
					break;
				case KeyEvent.VK_DOWN: // flèche bas
					ToucheBas = false;
					break;
				case KeyEvent.VK_Q: // flèche gauche
					ToucheQ = false;
					break;
				case KeyEvent.VK_D: // flèche droit
					ToucheD = false;
					break;
				case KeyEvent.VK_Z: // flèche haut
					ToucheZ = false;
					break;
				case KeyEvent.VK_S: // flèche bas
					ToucheS = false;
					break;
			// pour tester les écouteurs
			// this.setTitle("touche relachée");
			}
		}
	}
	
	private class gestionSouris implements MouseListener, MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			mx=e.getX();   my=e.getY();
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			mx=e.getX();   my=e.getY();
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			mouseClicked=true;
			mx=e.getX();   my=e.getY();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			mousePressed=true;
			mx=e.getX();   my=e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			mouseReleased=true;
			mx=e.getX();   my=e.getY();			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			mx=e.getX();   my=e.getY();
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			mx=e.getX();   my=e.getY();
			
		}
		
	}
} // Fin classe Jeu !!
