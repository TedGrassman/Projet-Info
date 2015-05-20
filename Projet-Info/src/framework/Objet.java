package framework;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

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

	double x, y; // Position de l'objet à l'écran (centre de l'objet de préférence) [A SUPPRIMER ??]
	int drawX, drawY; //Position de l'image à l'écran (coin en haut à gauche)
	int h, l; // Hauteur et largeur de l'objet à l'écran (image)
	double dx, dy; // Vecteur unitaire de déplacement

	BufferedImage[] images; // Image(s) de l'objet
	Rectangle limitesframe; // Rectangle englobant la fenêtre de jeu
	String nom_objet; // Nom de l'objet
	String typeObjet; // Type de l'objet											[INUTILE??]
	Boolean actif; // Si l'objet est actif ou non
	int NbImages; // Nombre d'images ou sprites pour l'objet
	int masse; // Masse de l'objet (pour l'action de la gravité)

	CentreGravite centreG; //centre de gravité de l'objet
	Area limites; //Hitbox de l'objet
	Explosion explosion;
	int nbImEx = 27;
	static String prefixeExplosion = "Explosion_Sequence_A ";

	public static ArrayList<Objet> liste = new ArrayList<Objet> ();	//Liste de tous les Objets pour effectuer les opérations
	AffineTransform transfo = new AffineTransform();
	
	int currentFrameNumber;
	
	public Objet(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type, int nbIm, int masse, String av) {

		NbImages = nbIm;
		try {
			images = new BufferedImage[NbImages];
			for (int k = 0; k < NbImages; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (Exception err) {
			System.out.println(NomImage + " introuvable !");
			System.exit(0);
		}
		
		h = images[0].getHeight(null);			//récupère une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth(null);
		
		x = ax;									//initialise tous les autres attributs
		y = ay;
		centreG = new CentreGravite(x, y);
		dx = adx;
		dy = ady;

		drawX = (int)(x-l/2);	// Initialise les positions drawX et drawY,
		drawY = (int)(y-h/2);	// correspondant au coin supérieur gauche (affichage de l'image)
		
		nom_objet = nom;
		typeObjet = type;
		limitesframe = aframe;
		actif = true;
		this.masse = masse;
		prefixeExplosion = av;
		explosion = new Explosion(x, y, nbImEx, prefixeExplosion);
		liste.add(this);
		currentFrameNumber = 0;
	}
	
	public Objet Collision(){						//Renvoie l'Astre en collision avec l'Astre appelant la méthode
		for (int i=0; i<liste.size(); i++){			//Si aucune collision, renvoie l'Astre appelant la méthode
			
			if (this.Collision(liste.get(i))){
				return liste.get(i);
			}
		}
		return this;
	}
	
	public boolean Collision (Objet A1){			//Teste si l'astre est en collision avec un autre astre fourni en parametre
		AffineTransform at1 = transfo;
		GeneralPath path1 = new GeneralPath();
        path1.append(this.limites.getPathIterator(at1), true);
        AffineTransform at2 = A1.transfo;
		GeneralPath path2 = new GeneralPath();
        path2.append(A1.limites.getPathIterator(at2), true);
        Area a1 = new Area(path1);
        Area a2 = new Area(path2);
        a2.intersect(a1);
        if (!a2.isEmpty()) {
        	return true;
        }
        return false;
	}

	public void draw(long t, Graphics g, Font f) { // Dessine l'objet au temps t dans l'interface graphique g
		//g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
		g.drawImage(images[currentFrameNumber % NbImages], drawX, drawY, null);
		currentFrameNumber++;
	}
	
	public void détruire(double ax, double ay, long t){
		this.explosion.activer(ax, ay, t);
		this.actif=false;
	}

	public abstract void move(long t); // Méthode abstraite : Déplace l'objet suivant le vecteur, la vitesse et la liberté de mouvement

}
