package projetInfo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;


@SuppressWarnings("unused")
public class Explosion {
	
	double x,y;
	int drawX, drawY;
	int h, l;
	long t0;
	String av;
	static String ap = ".png";
	static String[] NomImage;		//nom des PNG de l'explosion
	BufferedImage[] images; // Image(s) de l'objet
	Boolean actif; // Si l'objet est actif ou non
	int NbImages; // Nombre d'images ou sprites pour l'objet
	String nom_objet; // Nom de l'objet
	String typeObjet; // Type de l'objet
	public static ArrayList<Explosion> liste = new ArrayList<Explosion> ();	//Liste de tous les Objets pour effectuer les opérations
	
	
	public Explosion(double ax, double ay, int nbImEx, String av) {
		t0 = 0;
		x = ax;
		y = ay;
		this.NbImages = nbImEx;
		this.av = av;
		String uni = "";
		NomImage = new String[NbImages];
		for (int k = 0; k < NbImages; k++){
			if(k<9)
				uni = "0";
			else
				uni = "";
			NomImage[k] = "" + av + uni + (k+1) + ap;
		}
		
		int error = 0; //Si une image n'et pas trouvée, permet de savoir laquelle
		try {
			images = new BufferedImage[NbImages];
			for (int k = 0; k < NbImages; k++){
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
				error = k;
			}
		} catch (Exception err) {
			System.err.println(NomImage[error+1] + " introuvable !");
			System.exit(0);
		}
		h = images[0].getHeight(null);			//récupère une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth(null);
		drawX = (int)(x-l/2);	// Initialise les positions drawX et drawY,
		drawY = (int)(y-h/2);	// correspondant au coin supérieur gauche (affichage de l'image)
		actif = false;
		liste.add(this);
		
	}

	public void actualisation(long t) {
//		drawX = (int)(x-l/2);
//		drawY = (int)(y-h/2);		
		if(t == t0 + NbImages)
			this.actif = false;
	}
	
	public void draw(long t, Graphics g) { // Dessine l'objet au temps t dans l'interface graphique g
		if(actif){
			g.drawImage(images[(int)((t-t0-1)) % NbImages], drawX, drawY, null);
		}
	}
	
	public void activer(double ax, double ay, long t){
		x = ax;
		y = ay;
		t0 = t;
		actif = true;
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
	}
}
