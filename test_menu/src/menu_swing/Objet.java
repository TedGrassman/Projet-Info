package menu_swing;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import java.io.File;

import javax.imageio.ImageIO;

public abstract class Objet {
    int x, y, h, l;
    float dx, dy, vitesse;
    Image image;
    Rectangle limites, limitesframe;
    String nom_objet;
    boolean actif;
        
    public Objet(int ax, int ay, float adx, float ady, float avitesse,String NomImage, Rectangle aframe, String nom) {
        try {
            image= ImageIO.read(new File((String)"C:\\Users\\FALLOUH\\git\\Projet-Info\\test_menu\\res\\"+ NomImage));
        }
        catch(Exception err) {
            System.out.println(NomImage+" introuvable !");
            System.out.println("Mettre les images dans le repertoire :"+getClass().getClassLoader().getResource(NomImage));
            System.exit(0);
        }
        // récupère une fois pour toute la hauteur et largeur de l'image
        h= image.getHeight(null);
        l= image.getWidth(null);
        // définir les limites de l'objet pour les collisions et les sorties
        limites = new Rectangle(ax,ay,l,h);
        dx = adx;
        dy = ady;
        vitesse=avitesse;
        limitesframe=aframe;
        nom_objet=nom;
        x=ax;
        y=ay;
        actif=true;
        
        
    }
    public void draw (long t, Graphics g){
        g.drawImage(image,x,y,null);
    }
    public abstract void move (long t);
    
    public boolean Collision(Objet O){
        return limites.intersects(O.limites);
    }
}
