package framework;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class AstreTrouNoir extends Astre { // Astre de forme sphérique défini
											// par son rayon

	// Circle limites;
	double rayon; // Rayon de l'astre

	public AstreTrouNoir(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom,
			int nbIm, int masse, double rayon) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "AstreSpherique", nbIm, masse);
		this.rayon = rayon;
		limites = new Area(new Ellipse2D.Double(drawX, drawY, (2 * rayon), (2 * rayon))); // Création
																							// de
																							// la
																							// hitbox
																							// :
																							// disque
																							// de
																							// centre
																							// (ax,ay)
	}

	@Override
	public void move(long t) { // Implémentation de la méthode move obligatoire,
								// mais on ne la
								// définit pas pour un astre sphérique pour
								// l'instant

	}

	public void draw(long t, Graphics g) {
		final Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(images[currentFrameNumber % NbImages], drawX, drawY, null);
		currentFrameNumber++;

		/*
		 * AffineTransform at = new AffineTransform(); GeneralPath path1 = new
		 * GeneralPath(); ---------- path1.append(limites.getPathIterator(at),
		 * true); DEBBUGING : dessin de la hitbox g2d.fill(path1); ----------
		 */
	}

}
