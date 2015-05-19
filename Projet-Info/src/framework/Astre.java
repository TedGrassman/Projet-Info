package framework;


import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public abstract class Astre extends Objet{							//Classe abstraite dont héritent toutes les formes particulières d'astre ainsi que les missiles
	
	public static ArrayList<Astre> liste = new ArrayList<Astre> ();	//Liste de tous les Atres pour effectuer les opérations
	AffineTransform transfo = new AffineTransform();				//AffineTransform "vide" pour pouvoir créer une méthode Collision travaillant avec tout type d'astre
	
	public Astre(int ax, int ay, float adx, float ady, String[] NomImage, Rectangle aframe, String nom, String type, int nbIm, int masse) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, type, nbIm, masse, prefixeExplosion);
		liste.add(this);							//Ajoute l'astre nouvellement créé à la liste d'astres
	}

//	public Astre Collision(){						//Renvoie l'Astre en collision avec l'Astre appelant la méthode
//		for (int i=0; i<liste.size(); i++){			//Si aucune collision, renvoie l'Astre appelant la méthode
//			
//			if (this.Collision(liste.get(i))){
//				return liste.get(i);
//			}
//		}
//		return this;
//	}
//	public boolean Collision (Astre A1){			//Teste si l'astre est en collision avec un autre astre fourni en parametre
//		AffineTransform at1 = transfo;
//		GeneralPath path1 = new GeneralPath();
//        path1.append(this.limites.getPathIterator(at1), true);
//        AffineTransform at2 = A1.transfo;
//		GeneralPath path2 = new GeneralPath();
//        path2.append(A1.limites.getPathIterator(at2), true);
//        Area a1 = new Area(path1);
//        Area a2 = new Area(path2);
//        a2.intersect(a1);
//        if (!a2.isEmpty()) {
//        	return true;
//        }
//        return false;
//	}
	
	
	
}
