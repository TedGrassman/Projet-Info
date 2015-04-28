package projetInfo;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

public class AstreSpherique extends Astre {
	
	Circle limites;

	public AstreSpherique(int ax, int ay, float adx, float ady,	String[] NomImage, Rectangle aframe, String nom, int nbIm, int masse, double rayon) {
		super(ax, ay, adx, ady, NomImage, aframe, nom, nbIm, masse);
		limites = new Circle(rayon);
		centreG = new CentreGravite((double)ax, (double)ay);
	}

	@Override
	public void move(long t) {
		// TODO Auto-generated method stub
		
	}
	

}
