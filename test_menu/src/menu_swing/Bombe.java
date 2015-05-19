package menu_swing;
import java.awt.Rectangle;

public class Bombe extends Objet {
    static String NomImage="Bombe.png";
    public Bombe(int ax,int ay,Rectangle aframe,String nom) {
        super(ax, ay, 0, 1, 10, NomImage, aframe, nom);
    }
    public void move(long t){
    x=x+(int)(vitesse*dx); y=y+(int)(vitesse*dy);
    // on test si la bombe descendante sort en bas de l'écran
    if (y>limitesframe.y+limitesframe.height) this.actif=false;
    // déplace le rectangle englobant à la nouvelle position
    limites.setLocation(x,y);
    }
}
