package projetInfo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;






import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.File;

import javax.imageio.ImageIO;

import com.sun.javafx.geom.Area;

/**
 * @author Tanguy
 * @author Marc
 * @author Sami
 */
public abstract class Objet {

	double x, y; // Position de l'objet à l'écran (centre de l'objet de préférence)
	int drawX, drawY; //Position de l'image à l'écran (coin en haut à gauche)
	int h, l; // Hauteur et largeur de l'objet à l'écran (image)
	double dx, dy; // Vecteur unitaire de déplacement
	//float vitesse; // Vitesse de déplacement

	BufferedImage[] images; // Images de l'objet
	Rectangle limitesframe; // Rectangle englobant la fenêtre de jeu
	String nom_objet; // Nom de l'objet
	String typeObjet; // Type de l'objet
	Boolean actif; // Si l'objet est actif ou non
	int NbImages; // Nombre d'images ou sprites pour l'objet
	int masse; // Masse de l'objet (pour l'action de la gravit
	CentreGravite centreG; //centre de gravité de l'objet
	Shape limites; //Forme englobant l'objet à l'écran

	public Objet(int ax, int ay, float adx, float ady, String[] NomImage,
			Rectangle aframe, String nom, String type, int nbIm, int masse) {
		NbImages = nbIm;
		try {
			images = new BufferedImage[NbImages];
			for (int k = 0; k < NbImages; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (Exception err) {
			System.out.println(NomImage + " introuvable !");
			//System.out.println("Mettre les images dans le repertoire :"+ getClass().getClassLoader().getResource(NomImage[1]));
			System.exit(0);
		}
		/* récupère une fois pour toute la hauteur et largeur de l'image */
		h = images[0].getHeight(null);
		l = images[0].getWidth(null);
		/* définir les limites de l'objet pour les collisions et les sorties */
		//limites = new Rectangle(ax, ay, l, h);
		/* initialise tous les autres attributs */
		x = ax;
		y = ay;
		centreG = new CentreGravite(x, y);
		dx = adx;
		dy = ady;
		drawX = (int)(x-l/2);	// Initialise les positions drawX et drawY,
		drawY = (int)(y-h/2);	// correspondant au coin supérieur gauche (affichage de l'image)
		//vitesse = avitesse;
		nom_objet = nom;
		typeObjet = type;
		limitesframe = aframe;
		actif = true;
		this.masse = masse;
	}

	public void draw(long t, Graphics g) { // Dessine l'objet au temps t dans l'interface graphique g
		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
	}

	public abstract void move(long t); // Méthode abstraite : Déplace l'objet suivant le vecteur, la vitesse et la liberté de mouvement

//	public static Shape Collision(Objet O1, Objet O2) { // Teste la collision entre deux objets O1 et O2
//		return Shape.intersect(O1.limites, O2.limites);
//	}
	
	public Boolean Collision(Objet O1){
		/*if(O1.limites.getBoundsInParent().intersects(this.limites.getBoundsInParent()) && this!=O1)
			return true;
		else
			return false;
			*/
	Shape inter = Shape.intersect (this.limites, O1.limites);
	if(inter.getBoundsInLocal().getWidth() != -1) {
        return true;
    }
    else {
        return false;
    }
	
		
	}
	
}
