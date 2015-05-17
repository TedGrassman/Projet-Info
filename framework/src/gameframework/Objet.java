package gameframework;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;


import java.io.File;

import javax.imageio.ImageIO;

import java.awt.geom.Area;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * @author Tanguy
 * @author Marc
 * @author Sami
 */
@SuppressWarnings("unused")
public abstract class Objet { 		//Classe abstraite, Objet dessinable dans le JPanel

	double x, y; // Position de l'objet � l'�cran (centre de l'objet de pr�f�rence) [A SUPPRIMER ??]
	int drawX, drawY; //Position de l'image � l'�cran (coin en haut � gauche)
	int h, l; // Hauteur et largeur de l'objet � l'�cran (image)
	double dx, dy; // Vecteur unitaire de d�placement

	BufferedImage[] images; // Image(s) de l'objet
	Rectangle limitesframe; // Rectangle englobant la fen�tre de jeu
	String nom_objet; // Nom de l'objet
	String typeObjet; // Type de l'objet											[INUTILE??]
	Boolean actif; // Si l'objet est actif ou non
	int NbImages; // Nombre d'images ou sprites pour l'objet
	int masse; // Masse de l'objet (pour l'action de la gravit�)

	CentreGravite centreG; //centre de gravit� de l'objet
	Area limites; //Hitbox de l'objet



	public Objet(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type, int nbIm, int masse) {

		NbImages = nbIm;
		try {
			images = new BufferedImage[NbImages];
			for (int k = 0; k < NbImages; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (Exception err) {
			System.out.println(NomImage + " introuvable !");
			System.exit(0);
		}
		
		h = images[0].getHeight(null);			//r�cup�re une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth(null);
		
		x = ax;									//initialise tous les autres attributs
		y = ay;
		centreG = new CentreGravite(x, y);
		dx = adx;
		dy = ady;

		drawX = (int)(x-l/2);	// Initialise les positions drawX et drawY,
		drawY = (int)(y-h/2);	// correspondant au coin sup�rieur gauche (affichage de l'image)
		
		nom_objet = nom;
		typeObjet = type;
		limitesframe = aframe;
		actif = true;

		this.masse = masse;
	}

	public void draw(long t, Graphics g) { // Dessine l'objet au temps t dans l'interface graphique g
		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
	}

	public abstract void move(long t); // M�thode abstraite : D�place l'objet suivant le vecteur, la vitesse et la libert� de mouvement

}
