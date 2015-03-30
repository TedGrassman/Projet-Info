package menu_swing;
import java.awt.Rectangle;

public class Navire extends Objet {
    static String NomImage="Navire.png";
    public Navire(Rectangle aframe,String nom) {
        super((aframe.width/2)-13, aframe.height-80, 0, 0, 10, NomImage, aframe, nom);    
    }
    public void move(long t){
        x=x+(int)(vitesse*dx);
        y=y+(int)(vitesse*dy);
        
        if (x<limitesframe.x) 
            x= limitesframe.x;
        else
        if (x+l>limitesframe.x+limitesframe.width)
            x=limitesframe.x+limitesframe.width-l;
        if (y<limitesframe.y) y= limitesframe.y;
        else
        if (y+h>limitesframe.y+limitesframe.height)
            y=limitesframe.y+limitesframe.height-h;
        
        limites.setLocation(x,y);
    }
}
