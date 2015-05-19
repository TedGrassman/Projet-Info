package gameframework;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Trajectoire {
	ArrayList<Point> listePoints; //liste des points de la courbe
	int nbPoints; // nombre de points a afficher simultanément
	Astre cible; // astre dont on veut tracer la trajectoire
	int delai; // nombre de cycles d'attente avant de dessiner la trajectoire 
	Color couleur; // couleur de la courbe
	
	public Trajectoire(Astre acible, int anbPoints, int adelai, Color acouleur){
		listePoints = new ArrayList<Point>();
		nbPoints=anbPoints;
		cible=acible;
		delai=adelai;
		couleur=acouleur;
	}
	
	public void actualisation(){
		listePoints.add(new Point (cible.centreG.x, cible.centreG.y)); // ajoute le centre de gravité actuel de l'astre en dernière position de la liste
		if ( listePoints.size() >= nbPoints+delai ) listePoints.remove( 0 ); // si la liste est trop longue, supprime le plus vieux point
	}
	
	public void draw(long t, Graphics g) { // RMQ : utiliser un buffer pour l'appel sinon c'est pas fluide
			if (listePoints.size()>delai){ // vérifie que la liste est plus longue que le délai souhaité
				Color preced = g.getColor(); // stocke la couleur actuelle du Graphics
				g.setColor(couleur); // change la couleur à la couleur souhaitée pour la courbe
				for (int i =0; i<listePoints.size()-delai; i++){
					g.fillOval((int)listePoints.get(i).x, (int)listePoints.get(i).y, 2, 2); // dessine des cercles de diametre 1 à l'emplacement des points de la liste
				}
				g.setColor(preced); // restaure la couleur du Graphics
			}
		}
	
	
}
