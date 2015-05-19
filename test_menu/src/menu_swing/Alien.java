package menu_swing;
import java.awt.Rectangle;

public class Alien extends Objet {
    static String Type1="Alien1a.png";
    static String Type2="Alien2a.png";
    static String Type3="Alien3a.png";
    int score;
    
    public Alien(int ax, int ay, int ascore,String NomImage, Rectangle aframe, String nom) {
        super(ax, ay, 1, 0, 10, NomImage, aframe, nom);
        score = ascore;
    }
    public void move(long t){
        x=x+(int)(vitesse*dx);
        y=y+(int)(vitesse*dy);
        if (t%10==0) dx=-dx;
        // descendre tous les 4 secondes
        if (t%40==0) dy=+1;
        else dy=0;
        // déplace le rectangle englobant des Aliens à la nouvelle position
        limites.setLocation(x,y);
    }
}
