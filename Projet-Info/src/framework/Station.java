package framework;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Station extends Objet {

	static String[] NomImage = { "base.png" };
	static String prefixeExplosion = "Explosion_Sequence_A ";
	Joueur joueur;
	int numero, lastVectorX, lastVectorY;
	Explosion explosion;
	Trajectoire lastTrajectoire;

	public Station(int ax, int ay, Rectangle aframe, String nom, int idJoueur) {
		super(ax, ay, 0, 0, NomImage, aframe, nom, "Station", 1, 0);
		limites = new Area(new Ellipse2D.Double(drawX, drawY, images[0].getWidth(null), images[0].getHeight(null))); // Hitbox
																														// elliptique
		switch (idJoueur) {
		case 1:
			Game.JOUEUR1.Stations.add(this);
			numero = Game.JOUEUR1.Stations.size();
			joueur = Game.JOUEUR1;
			break;
		case 2:
			Game.JOUEUR2.Stations.add(this);
			numero = Game.JOUEUR2.Stations.size();
			joueur = Game.JOUEUR2;
			break;
		case 3:
			Game.JOUEUR3.Stations.add(this);
			numero = Game.JOUEUR3.Stations.size();
			joueur = Game.JOUEUR3;
			break;
		case 4:
			Game.JOUEUR4.Stations.add(this);
			numero = Game.JOUEUR4.Stations.size();
			joueur = Game.JOUEUR4;
			break;
		}
		explosion = new Explosion(0.0, 0.0, 27, prefixeExplosion);
		lastVectorX = (int) centreG.x;
		lastVectorY = (int) centreG.y;
	}

	public void move(long t) {
		x += (2 * dx);
		y += (2 * dy);

		if (x - l / 2 < limitesframe.getX())
			x = limitesframe.getX() + l / 2;
		else if (x + l / 2 > limitesframe.getX() + limitesframe.getWidth())
			x = limitesframe.getX() + limitesframe.getWidth() - l / 2;
		if (y - h / 2 < limitesframe.getY())
			y = limitesframe.getY() + h / 2;
		else if (y + h / 2 > limitesframe.getY() + limitesframe.getHeight())
			y = limitesframe.getY() + limitesframe.getHeight() - h / 2;

		drawX = (int) (x - l / 2);
		drawY = (int) (y - h / 2);
		limites = new Area(new Ellipse2D.Double(drawX, drawY, images[0].getWidth(null), images[0].getHeight(null))); //Actualisation de la hitbox
	}

	public void draw(long t, Graphics g, Font f) {
		g.drawImage(images[(int) t % NbImages], drawX, drawY, null);
		g.setColor(joueur.color);
		g.setFont(new Font("Harrington", 1, 55));
		final Graphics2D g2d = (Graphics2D) g.create(); // anti aliasing sur le texte
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawString("" + numero, (int) x + 40, (int) y - 5);


		/*
		 * Graphics2D g2d =(Graphics2D)g;
		 * AffineTransform at2 = new AffineTransform(); 				DEBBUGING : dessin de la hitbox 
		 * GeneralPath path1 = new GeneralPath();
		 * path1.append(limites.getPathIterator(at2), true);
		 * g2d.setColor(Color.WHITE); g2d.fill(path1);
		 * g2d.draw(path1.getBounds());
		 */
	}

	public void detruire(double ax, double ay, long t) {
		super.detruire(ax, ay, t);
		explosion.activer(ax, ay, t);
		joueur.Stations.remove(this);
	}

	public void setLastVector(int x, int y) {
		lastVectorX = x;
		lastVectorY = y;
	}
}
