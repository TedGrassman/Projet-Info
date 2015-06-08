package gameEntities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Explosion {

	double x, y;
	int drawX, drawY;
	int h, l;
	String av;
	static String ap = ".png";
	static String[] NomImage; // nom des PNG de l'explosion
	BufferedImage[] images; // Image(s) de l'objet
	Boolean actif; // Si l'objet est actif ou non
	int NbImages; // Nombre d'images ou sprites pour l'objet
	String nom_objet; // Nom de l'objet
	String typeObjet; // Type de l'objet
	long currentFrameNumber;
	public static ArrayList<Explosion> liste = new ArrayList<Explosion>(); //Liste de tous les objets pour effectuer les operations

	public Explosion(double ax, double ay, int nbImEx, String av) {
		x = ax;
		y = ay;
		NbImages = nbImEx;
		this.av = av;
		String uni = "";
		NomImage = new String[NbImages];
		for (int k = 0; k < NbImages; k++) {
			if (k < 9)
				uni = "0";
			else
				uni = "";
			NomImage[k] = "" + av + uni + (k + 1) + ap;
		}

		int error = 0; // Si une image n'et pas trouvée, permet de savoir laquelle
		try {
			images = new BufferedImage[NbImages];
			for (int k = 0; k < NbImages; k++) {
				images[k] = ImageIO.read(new File("res/explosions/" + NomImage[k]));
				error = k;
			}
		} catch (final Exception err) {
			System.err.println(NomImage[error + 1] + " introuvable !");
			System.exit(0);
		}
		h = images[0].getHeight(null); // récupère une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth(null);
		drawX = (int) (x - l / 2); // Initialise les positions drawX et drawY,
		drawY = (int) (y - h / 2); // correspondant au coin supérieur gauche
									// (affichage de l'image)
		actif = false;
		liste.add(this);
		currentFrameNumber = 1;

	}

	public void actualisation(long t) {
		if (currentFrameNumber > NbImages) {
			actif = false;
			liste.remove(this);
		}

	}

	public void draw(long t, Graphics g) { // Dessine l'objet au temps t dans l'interface graphique g
		if (actif == true) {
			g.drawImage(images[(int) ((currentFrameNumber - 1) % NbImages)], drawX, drawY, null);
			//System.out.println("EXPLOSION");
			currentFrameNumber++;
			actualisation(t);
		}
	}

	public void activer(double ax, double ay, long t) {
		x = ax;
		y = ay;
		actif = true;
		drawX = (int) (x - l / 2);
		drawY = (int) (y - h / 2);
	}
}
