package framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.AffineTransformOp;

@SuppressWarnings("unused")
public class Missile extends Objet {

	static final int MASSE_MISSILE = 10; // masse des missiles (par défaut)
	double angle; // orientation du missile par rapport à la verticale / force de poussée du moteur de fusée
	static double poussee = 0.05;
	Explosion explosion;
	//nom des PNG du missile
	static String[] NomImage = { "missile1_1.png", "missile1_2.png", "missile1_3.png", "missile1_4.png",
			"missile1_5.png", "missile1_6.png", "missile1_7.png", "missile1_8.png", "missile1_7.png", "missile1_6.png",
			"missile1_5.png", "missile1_4.png", "missile1_3.png", "missile1_2.png", "missile1_1.png" }; 
	
	Color couleur = Color.black; // couleur de la trajectoire
	Trajectoire traj; // trajectoire du missile
	Station station;
	static String prefixeExplosion = "explosion_missile_";
	boolean horsLimites = false;
	static int nbr = 0; // Nombre de missiles créés, s'incrémentent dans constructeur
	int lifetime = 1000;

	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String[] tab) {
		super(ax, ay, adx, ady, tab, aframe, "Missile", "Missile", 10, MASSE_MISSILE);
		nbr++;
		centreG = new CentreGravite(ax, ay);
		angle = 0.0;
		final int[] xpoints = { -10, 0, 10 }; // Creation des tableaux de coordonnées du triangle de hitbox
		final int[] ypoints = { 25, -25, 25 };
		limites = new Area(new Polygon(xpoints, ypoints, 3)); // Creation de la hitbox (triangle)
		station = null;
		explosion = new Explosion(0.0, 0.0, 27, prefixeExplosion);
	}

	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String nom, Color acouleur, Station station) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "Missile", 10, MASSE_MISSILE);
		nbr++;
		centreG = new CentreGravite(ax, ay);
		centreG = new CentreGravite(ax, ay); // Creation du centre de gravité au centre du missile
		final int[] xpoints = { -10, 0, 10 }; // Creation des tableaux de coordonnées du triangle de hitbox
		final int[] ypoints = { 25, -25, 25 };
		limites = new Area(new Polygon(xpoints, ypoints, 3)); // Creation de la hitbox (triangle)
		angle = Math.atan2(dy, dx) - Math.PI * 3 / 2; // Orientation initiale du missile : verticale
		couleur = acouleur;
		traj = new Trajectoire(this, 90, 5, couleur); // Creation de la trajectoire
		this.station = station;
		explosion = new Explosion(0.0, 0.0, 27, prefixeExplosion);
	}

	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String nom, Color acouleur) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "Missile", 10, MASSE_MISSILE);
		centreG = new CentreGravite(ax, ay);
		angle = 0.0;
		centreG = new CentreGravite(ax, ay); // Creation du centre de gravité au centre du missile
		final int[] xpoints = { -10, 0, 10 }; // Creation des tableaux de coordonnées du triangle de hitbox
		final int[] ypoints = { 25, -25, 25 };
		limites = new Area(new Polygon(xpoints, ypoints, 3)); // Creation de la hitbox (triangle)
		angle = Math.atan2(dy, dx) - Math.PI * 3 / 2; // Orientation initiale du missile : verticale
		couleur = acouleur;
		traj = new Trajectoire(this, 90, 5, couleur); // Creation de la trajectoire
		station = null;
		explosion = new Explosion(0.0, 0.0, 27, prefixeExplosion);
	}

	public void move(long t) { // déplacement du missile à chaque cycle
		lifetime--;
		if (lifetime > 0) {
			double xAstre = 0.0;
			double yAstre = 0.0;
			double teta = 0.0;
			int masse = 0;
			Objet astr;
			double vitesse = 0;
			for (int i = 0; i < liste.size(); i++) { // calcul du déplacement lié à la gravité
				astr = liste.get(i);
				if (centreG.distance(astr.centreG) < 500) {

					xAstre = astr.centreG.x;
					yAstre = astr.centreG.y;
					masse = astr.masse;

					if (centreG.x != xAstre && centreG.y != yAstre && !astr.typeObjet.equals(typeObjet)) {
						// determiner angle a partir de deltax et deltay.
						// Calculer force en norme. Projeter en dx dy.
						teta = Math.atan2(yAstre - y, xAstre - x);
						vitesse = (masse * this.masse) / ((yAstre - y) * (yAstre - y) + (xAstre - x) * (xAstre - x));
						dx += vitesse * Math.cos(teta);
						dy += vitesse * Math.sin(teta);
					}
				}
			}
			angle = Math.atan2(dy, dx) - Math.PI * 3 / 2; // Met a jour l'orientation du missile
			dx += poussee * Math.cos(angle + Math.PI * 3 / 2);
			dy += poussee * Math.sin(angle + Math.PI * 3 / 2);

			centreG.x = (centreG.x + dx); // translation des coordonnées du missile
			centreG.y = (centreG.y + dy);
			x = x + dx;
			y = y + dy;
			drawX = (int) (drawX + dx);
			drawY = (int) (drawY + dy);
			transfo.setToIdentity(); // Remise à zéro de la transformation affine
			transfo.translate(centreG.x, centreG.y); // Positionne la hitbox

			transfo.rotate(angle); // Fait pivoter la hitbox

			traj.actualisation(); // Actualisation de la trajectoire apres le déplacement

			if (x < limitesframe.getX() - 500) // ------------------------------
				actif = false; // Désactivation du missile
			else if (x > limitesframe.getX() + limitesframe.getWidth() + 1000) // s'il sort d'une bande
				actif = false; // de 500 autour du rectangle
			if (y < limitesframe.getY() - 500) // délimitant l'aire de jeu
				actif = false; // ------------------------------
			else if (y > limitesframe.getY() + limitesframe.getHeight() + 1000)
				actif = false;

			if (x < limitesframe.getX() - 10)
				horsLimites = true;
			else if (x > limitesframe.getX() + limitesframe.getWidth() + 10)
				horsLimites = true;
			if (y < limitesframe.getY() - 10)
				horsLimites = true;
			else if (y > limitesframe.getY() + limitesframe.getHeight() + 10)
				horsLimites = true;
			else if (x > limitesframe.getX() && x < limitesframe.getX() + limitesframe.getWidth()
					&& y > limitesframe.getY() && y < limitesframe.getY() + limitesframe.getHeight())
				horsLimites = false;
		} else {
			détruire(centreG.x, centreG.y, t);
		}

	}

	public void draw(long t, Graphics g, Font f) { // Dessine le missile au temps t dans l'interface graphique g avec la bonne
													// orientation

		final AffineTransform at = new AffineTransform();
		at.rotate(angle);
		at.translate(-15, -50);
		final AffineTransformOp op = new AffineTransformOp(at, 1);
		final Graphics2D g2d = (Graphics2D) g; // cast le graphics en graphics2d pour pouvoir appliquer la transformation
		traj.draw(t, g2d); // dessine la trajectoire
		g2d.drawImage(images[currentFrameNumber % NbImages], op, (int) centreG.x, (int) centreG.y); // dessine l'image
		currentFrameNumber++;

		// g.setColor(Color.white);
		// g.setFont(f.deriveFont(15.0f));
		// g.drawString(nom_objet, (int)x, (int)(y+30));
		// g.drawString("x=" +(int)x, (int)x, (int)(y-30));
		// g.drawString("y=" +(int)y, (int)x, (int)(y-18));

		final double xMax = limitesframe.getWidth();
		final double yMax = limitesframe.getHeight();
		final double taille = Math.sqrt(Math.pow((limitesframe.getWidth() / 2 - x) * 720 / 1366, 2)
				+ Math.pow((limitesframe.getHeight() / 2 - y) * 1366 / 720, 2)) / 10;
		final double angle = Math.atan2(limitesframe.getWidth() / 2 - x, limitesframe.getHeight() / 2 - y);

		g.setColor(station.joueur.color);

		if (horsLimites) {
			if (x < 0 && y < 0) {
				g.drawLine(0, 0, (int) (taille * Math.sin(angle)), (int) (taille * Math.cos(angle)));
			}
			// X | |
			// ______|___________|______
			// | |
			// | |
			// ______|___________|______
			// | |
			// | |

			if (x > xMax && y > yMax) {
				g.drawLine((int) xMax, (int) yMax, (int) xMax + (int) (taille * Math.sin(angle)), (int) yMax
						+ (int) (taille * Math.cos(angle)));
			}
			// | |
			// ______|___________|______
			// | |
			// | |
			// ______|___________|______
			// | |
			// | | X

			if (x < 0 && y > 0 && y < yMax) {
				g.drawLine(0, (int) y, 0 + (int) (taille * Math.sin(angle)), (int) y + (int) (taille * Math.cos(angle)));
			}
			// | |
			// ______|___________|______
			// | |
			// X | |
			// ______|___________|______
			// | |
			// | |
			if (y < 0 && x > 0 && x < xMax) {
				g.drawLine((int) x, 0, (int) x + (int) (taille * Math.sin(angle)), 0 + (int) (taille * Math.cos(angle)));
			}
			// | X |
			// ______|___________|______
			// | |
			// | |
			// ______|___________|______
			// | |
			// | |

			if (x < 0 && y > yMax) {
				g.drawLine(0, (int) yMax, 0 + (int) (taille * Math.sin(angle)),
						(int) yMax + (int) (taille * Math.cos(angle)));
			}
			// | |
			// ______|___________|______
			// | |
			// | |
			// ______|___________|______
			// | |
			// X | |

			if (y < 0 && x > xMax) {
				g.drawLine((int) xMax, 0, (int) xMax + (int) (taille * Math.sin(angle)),
						0 + (int) (taille * Math.cos(angle)));
			}
			// | | X
			// ______|___________|______
			// | |
			// | |
			// ______|___________|______
			// | |
			// | |

			if (x > xMax && y > 0 && y < yMax) {
				g.drawLine((int) xMax, (int) y, (int) xMax + (int) (taille * Math.sin(angle)), (int) y
						+ (int) (taille * Math.cos(angle)));
			}
			// | |
			// ______|___________|______
			// | |
			// | | X
			// ______|___________|______
			// | |
			// | |

			if (y > yMax && x > 0 && x < xMax) {
				g.drawLine((int) x, (int) yMax, (int) x + (int) (taille * Math.sin(angle)), (int) yMax
						+ (int) (taille * Math.cos(angle)));
			}
			// | |
			// ______|___________|______
			// | |
			// | |
			// ______|___________|______
			// | |
			// | X |

		}

		/*
		 * GeneralPath path1 = new GeneralPath();
		 * AffineTransform at2 = new AffineTransform(); 						DEBBUGING
		 * at2.translate(centreG.x, centreG.y); 								A UTILISER POUR VISUALISER LA HITBOX 
		 * at2.rotate(angle);
		 * path1.append(limites.getPathIterator(at2), true);
		 * g2d.setColor(couleur); g2d.fill(path1); g2d.draw(path1.getBounds());
		 */

	}

	public void détruire(double ax, double ay, long t) {
		super.détruire(ax, ay, t);
		explosion.activer(ax, ay, t);
	}
}
