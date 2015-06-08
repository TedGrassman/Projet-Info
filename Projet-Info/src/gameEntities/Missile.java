package gameEntities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.AffineTransformOp;

public class Missile extends Objet {

	static final int MASSE_MISSILE = 10; // masse des missiles (par défaut)
	double angle; // orientation du missile par rapport à la verticale / force de poussée du moteur de fusée
	public static double poussee = 0.05;
	public static int nbPoints = 90;
	static int actionGravite = 1000;	// Distance à partir de laquelle la gravité est prise en compte
	Explosion explosion;				// ATTENTION : le fait que chaque missile ait sa propre animation d'explosion peut induire une Memory Leak, si on crée beaucoup de missiles !!
	static String pre = "missiles/";
	//nom des PNG du missile
	static String[] NomImage = { pre+"missile1_1.png", pre+"missile1_2.png", pre+"missile1_3.png", pre+"missile1_4.png",
		pre+"missile1_5.png", pre+"missile1_6.png", pre+"missile1_7.png", pre+"missile1_8.png", pre+"missile1_7.png", pre+"missile1_6.png",
		pre+"missile1_5.png", pre+"missile1_4.png", pre+"missile1_3.png", pre+"missile1_2.png", pre+"missile1_1.png" }; 
	
	Color couleur = Color.black; // couleur de la trajectoire
	Trajectoire traj; // trajectoire du missile
	Station station;
	static String prefixeExplosion = "explosion_missile_";
	boolean horsLimites = false;
	public static int nbr = 0; // Nombre de missiles créés, s'incrémentent dans constructeur
	int lifetime = 1500; //Durée de vie (en boucles) du missiles
	int cadre = 1500;	// Cadre au-dela duquel le missile est détruit (en pixels)

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
				if (centreG.distance(astr.centreG) < actionGravite) {

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


			if (x < limitesframe.getX() - cadre) 										// ------------------------------
				actif = false; 															// Désactivation du missile
			else if (x > limitesframe.getX() + limitesframe.getWidth() + cadre) 		// s'il sort d'une bande
				actif = false; 															// de dimension "cadre"
			if (y < limitesframe.getY() - cadre)										// autour du rectangle
				actif = false; 															// délimitant l'aire de jeu
			else if (y > limitesframe.getY() + limitesframe.getHeight() + cadre)		// ------------------------------
				actif = false;															
			if (x < limitesframe.getX() - 10)											//-------------------------------
				horsLimites = true;														// Vérifie si le missile est
			else if (x > limitesframe.getX() + limitesframe.getWidth() + 10)			// "horsLimites", càd si
				horsLimites = true;														// il est en dehors de l'écran
			if (y < limitesframe.getY() - 10)											//-------------------------------
				horsLimites = true;
			else if (y > limitesframe.getY() + limitesframe.getHeight() + 10)
				horsLimites = true;
			else if (x > limitesframe.getX() && x < limitesframe.getX() + limitesframe.getWidth()
					&& y > limitesframe.getY() && y < limitesframe.getY() + limitesframe.getHeight())
				horsLimites = false;
		} else {
			detruire(centreG.x, centreG.y, t);
		}

	}

	public void draw(long t, Graphics g, Font f) { // Dessine le missile au temps t dans l'interface graphique g avec la bonne
													// orientation

		
		final AffineTransform at = new AffineTransform();
		at.rotate(angle);
		at.translate(-15, -50);
		final AffineTransformOp op = new AffineTransformOp(at, 1);
		final Graphics2D g2d = (Graphics2D) g; // cast le graphics en graphics2d pour pouvoir appliquer la transformation
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON ); //Antialiasing
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
		
		
		// dessine le vecteur permettant d'indiquer la position d'un missile hors écran
		if (horsLimites) {
			if (x < 0 && y < 0) {
				g.drawLine(0, 0, (int) (taille * Math.sin(angle)), (int) (taille * Math.cos(angle)));
			}
			// 	X	 | 			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |

			if (x > xMax && y > yMax) {
				g.drawLine((int) xMax, (int) yMax, (int) xMax + (int) (taille * Math.sin(angle)), (int) yMax
						+ (int) (taille * Math.cos(angle)));
			}
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |	 X

			if (x < 0 && y > 0 && y < yMax) {
				g.drawLine(0, (int) y, 0 + (int) (taille * Math.sin(angle)), (int) y + (int) (taille * Math.cos(angle)));
			}
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//	 X	 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |
			if (y < 0 && x > 0 && x < xMax) {
				g.drawLine((int) x, 0, (int) x + (int) (taille * Math.sin(angle)), 0 + (int) (taille * Math.cos(angle)));
			}
			//		 |		X	 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |

			if (x < 0 && y > yMax) {
				g.drawLine(0, (int) yMax, 0 + (int) (taille * Math.sin(angle)),
						(int) yMax + (int) (taille * Math.cos(angle)));
			}
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//	 X	 |			 |

			if (y < 0 && x > xMax) {
				g.drawLine((int) xMax, 0, (int) xMax + (int) (taille * Math.sin(angle)),
						0 + (int) (taille * Math.cos(angle)));
			}
			//		 |			 |	 X
			// ______|___________|______
			//		 |			 |
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |

			if (x > xMax && y > 0 && y < yMax) {
				g.drawLine((int) xMax, (int) y, (int) xMax + (int) (taille * Math.sin(angle)), (int) y
						+ (int) (taille * Math.cos(angle)));
			}
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |	 X
			// ______|___________|______
			//		 |			 |
			//		 |			 |

			if (y > yMax && x > 0 && x < xMax) {
				g.drawLine((int) x, (int) yMax, (int) x + (int) (taille * Math.sin(angle)), (int) yMax
						+ (int) (taille * Math.cos(angle)));
			}
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |			 |
			// ______|___________|______
			//		 |			 |
			//		 |		X	 |

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

	public void detruire(double ax, double ay, long t) {
		super.detruire(ax, ay, t);
		explosion.activer(ax, ay, t);
	}
}
