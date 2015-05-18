
package projetInfo;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
//import javafx.scene.shape.Polygon;
//import javafx.scene.transform.Translate;

import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.File;

import javax.imageio.ImageIO;


@SuppressWarnings("unused")
public class Missile extends Objet {

//	Polygon limites;								//hitbox triangulaire
	static final int MASSE_MISSILE=10;				//masse des missiles (par défaut)
	double angle; 									//orientation du missile par rapport à la verticale
	static String[] NomImage = {"missile1_1.png","missile1_2.png","missile1_3.png","missile1_4.png","missile1_5.png","missile1_6.png",
		"missile1_7.png", "missile1_8.png","missile1_7.png","missile1_6.png","missile1_5.png","missile1_4.png","missile1_3.png",
		"missile1_2.png","missile1_1.png"};			//nom des PNG du missile
	Color couleur = Color.black;									//couleur de la trajectoire
	Trajectoire traj;								//trajectoire du missile
	static int nbr=0;					// Nombre de missiles créés, s'incrémentent dans constructeur
	static String prefixeExplosion = "explosion_missile_";
	boolean horsLimites = false;
	Station station;


	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String[] tab) {
		super(ax, ay, adx,ady, tab, aframe, "Missile", "Missile", 1, MASSE_MISSILE, prefixeExplosion);
		nbr++;
		centreG = new CentreGravite(ax, ay);
		angle = 0.0 ;
		//limites = new Polygon(10.0, 0, 20.0, 50.0, 0.0, 50.0);
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
		int[]xpoints = {-10,0,10};									//Creation des tableaux de coordonnées du triangle de hitbox
		int[]ypoints = {25,-25,25};
		limites = new Area (new Polygon(xpoints, ypoints, 3));		//Creation de la hitbox (triangle)
		traj = new Trajectoire (this, 90, 5, couleur);				//Creation de la trajectoire
	}
	
	public Missile(int ax, int ay, float adx, float ady, Rectangle aframe, String nom, Color acouleur, Station station) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, "Missile", NomImage.length, MASSE_MISSILE, prefixeExplosion);
		nbr++;
		centreG = new CentreGravite(ax, ay);
		angle = 0.0 ;
		//limites = new Polygon(10.0, 0, 20.0, 50.0, 0.0, 50.0);
		//limites = new Circle(ax, ay, images[0].getWidth(null)/2);
		centreG = new CentreGravite(ax, ay);						//Creation du centre de gravité au centre du missile
		int[]xpoints = {-10,0,10};									//Creation des tableaux de coordonnées du triangle de hitbox
		int[]ypoints = {25,-25,25};
		limites = new Area (new Polygon(xpoints, ypoints, 3));		//Creation de la hitbox (triangle)
		angle = Math.atan2(dy, dx)-Math.PI*3/2;						//Orientation initiale du missile : dépend de sa vitesse initiale
		couleur = acouleur;
		traj = new Trajectoire (this, 90, 5, couleur);				//Creation de la trajectoire
		this.station = station;

	}

	
	public void move(long t) {										//déplacement du missile à chaque cycle
		double xAstre=0.0;
		double yAstre=0.0;
		double teta=0.0;
		int masse = 0;
		double vitesse=0;
		for (int i=0; i<liste.size(); i++){							//calcul du déplacement lié à la gravité
			xAstre = liste.get(i).centreG.x;
			yAstre = liste.get(i).centreG.y;
			masse = liste.get(i).masse;

			if(this.centreG.x != xAstre && this.centreG.y != yAstre && !liste.get(i).typeObjet.equals(this.typeObjet) && !liste.get(i).typeObjet.equals("Station")){
				// determiner angle a partir de deltax et deltay. Calculer force en norme. Projeter en dx dy.
				teta=Math.atan2(yAstre-this.y, xAstre-this.x);
				vitesse = (masse*this.masse)/((yAstre-this.y)*(yAstre-this.y)+(xAstre-this.x)*(xAstre-this.x));
				this.dx = dx+vitesse*Math.cos(teta);
				this.dy = dy+vitesse*Math.sin(teta);
			}
		}
		this.centreG.x = (centreG.x+this.dx);						//translation des coordonnées du missile
		this.centreG.y = (centreG.y+this.dy);
		this.x = x+this.dx;
		this.y = y+this.dy;
		this.drawX=(int) (this.drawX+dx);
		this.drawY=(int) (this.drawY+dy);
		transfo.setToIdentity();									//Remise à zéro de la transformation affine
		transfo.translate(this.centreG.x,this.centreG.y);			//Positionne la hitbox
	
		angle = Math.atan2(dy, dx)-Math.PI*3/2; 					//Met a jour l'orientation du missile
		transfo.rotate(angle);										//Fait pivoter la hitbox
		
		traj.actualisation();										//Actualisation de la trajectoire apres le déplacement
		
		
		if (x < limitesframe.getX() - 1000)											//------------------------------
			this.actif = false;														// Désactivation du missile
		else if (x > limitesframe.getX() + limitesframe.getWidth() + 1000)			// s'il sort d'une bande
			this.actif = false;														// de 1000 autour du rectangle
		if (y < limitesframe.getY() - 1000)											// délimitant l'aire de jeu
			this.actif = false;														//------------------------------
		else if (y > limitesframe.getY() + limitesframe.getHeight() + 1000)
			this.actif = false;
		
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
	}
	
	public void draw(long t, Graphics g, Font f) { // Dessine le missile au temps t dans l'interface graphique g avec la bonne orientation
		
		AffineTransform at = new AffineTransform();
		at.rotate(angle);
		at.translate(-10, -25);
		AffineTransformOp op = new AffineTransformOp (at, 1);
		Graphics2D g2d =(Graphics2D)g; 													// cast le graphics en graphics2d pour pouvoir appliquer la transformation
		traj.draw(t, g2d);																// dessine la trajectoire
		g2d.drawImage(images[(int) t % NbImages], op, (int)centreG.x, (int)centreG.y);	// dessine l'image
		
		g.setColor(Color.white);
		g.setFont(f.deriveFont(15.0f));
		g.drawString(nom_objet, (int)x, (int)(y+30));
		g.drawString("x=" +(int)x, (int)x, (int)(y-30));
		g.drawString("y=" +(int)y, (int)x, (int)(y-18));
		
		double xMax = limitesframe.getWidth();
		double yMax = limitesframe.getHeight(); 
		double taille = Math.sqrt(Math.pow((limitesframe.getWidth()/2-x)*720/1366, 2)+Math.pow((limitesframe.getHeight()/2-y)*1366/720, 2))/10;
		double angle = Math.atan2(limitesframe.getWidth()/2-x, limitesframe.getHeight()/2-y);
		
		g.setColor(station.color);	
		
		if(horsLimites){
			if(x<0 && y<0){
				//taille = Math.sqrt(Math.pow((0-x), 2)+Math.pow((0-y), 2))/5;
				g.drawLine(0, 0, (int)(taille*Math.sin(angle)), (int)(taille*Math.cos(angle)));
			}
			//	X	|			|		
			//______|___________|______
			//		|			|		
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|		

			if(x>xMax && y>yMax){
				//taille = Math.sqrt(Math.pow((xMax-x), 2)+Math.pow((yMax-y), 2))/5;		
				g.drawLine((int)xMax, (int)yMax, (int)xMax+(int)(taille*Math.sin(angle)),
						(int)yMax+(int)(taille*Math.cos(angle)));		
			}
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|	X

			if(x<0 && y>0 && y<yMax){
				//taille = Math.sqrt(Math.pow((0-x), 2))/2.5;
				g.drawLine(0, (int)y, 0+(int)(taille*Math.sin(angle)),
						(int)y+(int)(taille*Math.cos(angle)));
			}
			//		|			|		
			//______|___________|______
			//		|			|		
			//	X	|			|		
			//______|___________|______
			//		|			|		
			//		|			|	
			if(y<0 && x>0 && x<xMax){
				//taille = Math.sqrt(Math.pow((0-y), 2))/2.5;
				g.drawLine((int)x, 0, (int)x+(int)(taille*Math.sin(angle)),
						0+(int)(taille*Math.cos(angle)));
			}
			//		|	  X		|		
			//______|___________|______
			//		|			|		
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|	

			if(x<0 && y>yMax){
				//taille = Math.sqrt(Math.pow((0-x), 2)+Math.pow((yMax-y), 2))/5;
				g.drawLine(0, (int)yMax, 0+(int)(taille*Math.sin(angle)),
						(int)yMax+(int)(taille*Math.cos(angle)));
			}
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|		
			//______|___________|______
			//		|			|		
			//	X	|			|	

			if(y<0 && x>xMax){
				//taille = Math.sqrt(Math.pow((xMax-x), 2)+Math.pow((0-y), 2))/5;
				g.drawLine((int)xMax, 0, (int)xMax+(int)(taille*Math.sin(angle)),
						0+(int)(taille*Math.cos(angle)));
			}			
			//		|			|	X	
			//______|___________|______
			//		|			|		
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|	

			if(x>xMax && y>0 && y<yMax){
				//taille = Math.sqrt(Math.pow((xMax-x), 2))/2.5;
				g.drawLine((int)xMax, (int)y, (int)xMax+(int)(taille*Math.sin(angle)),
						(int)y+(int)(taille*Math.cos(angle)));
			}
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|	X	
			//______|___________|______
			//		|			|		
			//		|			|	

			if(y>yMax && x>0 && x<xMax){
				//taille = Math.sqrt(Math.pow((yMax-y), 2))/2.5;
				g.drawLine((int)x, (int)yMax, (int)x+(int)(taille*Math.sin(angle)),
						(int)yMax+(int)(taille*Math.cos(angle)));
			}
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|			|		
			//______|___________|______
			//		|			|		
			//		|	  X		|	

			
		}
		
		
//		GeneralPath path1 = new GeneralPath();					//------------------
//		AffineTransform at2 = new AffineTransform();			//DEBBUGING 
//		at2.translate(centreG.x, centreG.y);					//A UTILISER POUR VISUALISER LA HITBOX
//		at2.rotate(angle);										//------------------
//      path1.append(limites.getPathIterator(at2), true);
//      g2d.setColor(couleur);
//      g2d.fill(path1);
//      g2d.draw(path1.getBounds());
        

		
	}
	/*
	public void placer(int posx, int posy, double aangle){
		centreG = new CentreGravite(posx, posy);
		angle=aangle;
		limites = new Polygon(posx, posy+25, posx+10, posy-25, posx-10, posy-25);
		centreG.x = x;
		centreG.y = y;
		drawX = (int)(x-l/2);
		drawY = (int)(y-h/2);
	}
	*/
}
