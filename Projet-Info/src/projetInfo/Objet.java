package projetInfo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * @author Tanguy
 * @author Marc
 * @author Sami
 */
public abstract class Objet {

	int x, y; // Position de l'objet à l'écran (coin en haut à gauche)
	int h, l; // Hauteur et largeur de l'objet à l'écran (image)
	float dx, dy; // Vecteur unitaire de déplacement
	float vitesse; // Vitesse de déplacement
	Image[] images; // Images de l'objet
	Rectangle limites; // Rectangle englobant l'objet à l'écran
	Rectangle limitesframe; // Rectangle englobant la fenêtre de jeu
	String nom_objet; // Nom de l'objet
	Boolean actif; // Si l'objet est actif ou non
	int NbImages; // Nombre d'images ou sprites pour l'objet
	int masse; // Masse de l'objet (pour l'action de la gravit
	CentreGravite centreG; //centre de gravité de l'objet

	public Objet(int ax, int ay, float adx, float ady, float avitesse, String[] NomImage,
			Rectangle aframe, String nom, int nbIm, int masse) {
		NbImages = nbIm;
		try {
			images = new Image[NbImages];
			for (int k = 0; k < NbImages; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (Exception err) {
			System.out.println(NomImage + " introuvable !");
			System.out.println("Mettre les images dans le repertoire :"
					+ getClass().getClassLoader().getResource(NomImage[1]));
			System.exit(0);
		}
		/* récupère une fois pour toute la hauteur et largeur de l'image */
		h = images[0].getHeight(null);
		l = images[0].getWidth(null);
		/* définir les limites de l'objet pour les collisions et les sorties */
		limites = new Rectangle(ax, ay, l, h);
		/* initialise tous les autres attributs */
		x = ax;
		y = ay;
		dx = adx;
		dy = ady;
		vitesse = avitesse;
		nom_objet = nom;
		limitesframe = aframe;
		actif = true;
		this.masse = masse;
	}

	public void draw(long t, Graphics g) { // Dessine l'objet au temps t dans l'interface graphique g
		g.drawImage(images[(int) t % NbImages], x, y, null);
	}

	public abstract void move(long t); // Méthode abstraite : Déplace l'objet suivant le vecteur, la vitesse et la liberté de mouvement

	public boolean Collision(Objet O) { // Teste la collision avec un autre objet O de même type
		return this.limites.intersects(O.limites);
	}
}
