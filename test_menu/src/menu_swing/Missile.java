package menu_swing;
import java.awt.Rectangle;

public class Missile extends Objet {
    static String NomImage="Missile.png";
    public Missile(int ax, int ay,Rectangle R, String nom) {
        super(ax, ay, 0, -1, 10, NomImage, R, nom);
    }
    public void move(long t){
    x=x+(int)(vitesse*dx);
    y=y+(int)(vitesse*dy);
    // on test si le missile montant sort du haut de l'écran
    if (y+h<limitesframe.y) this.actif=false; // Le missile sera supprimé après
    limites.setLocation(x,y);
    }
}
