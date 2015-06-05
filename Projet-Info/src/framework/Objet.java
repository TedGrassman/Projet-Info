package framework;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * @author Tanguy
 * @author Marc
 * @author Sami
 */

public abstract class Objet { // Classe abstraite, Objet dessinable dans le
								// JPanel

	double x, y; // Position de l'objet � l'�cran (centre de l'objet de
					// pr�f�rence) [A SUPPRIMER ??]
	int drawX, drawY; // Position de l'image � l'�cran (coin en haut � gauche)
	int h, l; // Hauteur et largeur de l'objet � l'�cran (image)
	double dx, dy; // Vecteur unitaire de d�placement

	BufferedImage[] images; // Image(s) de l'objet
	Rectangle limitesframe; // Rectangle englobant la fen�tre de jeu
	String nom_objet; // Nom de l'objet
	String typeObjet; // Type de l'objet [INUTILE??]
	Boolean actif; // Si l'objet est actif ou non
	int NbImages; // Nombre d'images ou sprites pour l'objet
	int masse; // Masse de l'objet (pour l'action de la gravit�)

	CentreGravite centreG; // centre de gravit� de l'objet
	Area limites; // Hitbox de l'objet
	int nbImEx = 27;

	public static ArrayList<Objet> liste = new ArrayList<Objet>(); // Liste de tous les Objets
																	// pour effectuer les op�rations
	AffineTransform transfo = new AffineTransform();

	int currentFrameNumber;

	public Objet(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type,
			int nbIm, int masse) {

		NbImages = nbIm;
		try {
			images = new BufferedImage[NbImages];
			for (int k = 0; k < NbImages; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (final Exception err) {
			System.out.println(NomImage + " introuvable !");
			System.exit(0);
		}

		h = images[0].getHeight(null); // r�cup�re une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth(null);

		x = ax; // initialise tous les autres attributs
		y = ay;
		centreG = new CentreGravite(x, y);
		dx = adx;
		dy = ady;

		drawX = (int) (x - l / 2); // Initialise les positions drawX et drawY,
		drawY = (int) (y - h / 2); // correspondant au coin sup�rieur gauche
									// (affichage de l'image)

		nom_objet = nom;
		typeObjet = type;
		limitesframe = aframe;
		actif = true;
		this.masse = masse;
		liste.add(this);
		currentFrameNumber = 0;
	}

	public Objet Collision() { // Renvoie l'Astre en collision avec l'Astre appelant la m�thode
		for (int i = 0; i < liste.size(); i++) { // Si aucune collision, renvoie l'Astre appelant la m�thode

			if (this.Collision(liste.get(i))) {
				return liste.get(i);
			}
		}
		return this;
	}

	public boolean Collision(Objet A1) { // Teste si l'astre est en collision avec un autre astre fourni en
											// parametre

		if ((int) centreG.distance(A1.centreG) < 150 && A1 != this) {
			System.out.println("approche");//
			final AffineTransform at1 = transfo;
			final GeneralPath path1 = new GeneralPath();
			path1.append(limites.getPathIterator(at1), true);
			final AffineTransform at2 = A1.transfo;
			final GeneralPath path2 = new GeneralPath();
			path2.append(A1.limites.getPathIterator(at2), true);
			final Area a1 = new Area(path1);
			final Area a2 = new Area(path2);
			a2.intersect(a1);
			if (!a2.isEmpty()) {
				return true;
			}
		}

		return false;
	}

	public void draw(long t, Graphics g, Font f) { // Dessine l'objet au temps t dans l'interface
													// graphique g
		g.drawImage(images[currentFrameNumber % NbImages], drawX, drawY, null);
		currentFrameNumber++;
	}

	public void d�truire(double ax, double ay, long t) {
		actif = false;
	}

	public abstract void move(long t); // M�thode abstraite : D�place l'objet suivant le vecteur, la vitesse et la
										// libert� de mouvement

}
