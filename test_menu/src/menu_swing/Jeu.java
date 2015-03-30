package menu_swing;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Jeu extends JPanel implements KeyListener {
    Timer timer;
    long temps;
    BufferedImage ArrierePlan;
    Graphics buffer;
    boolean ToucheHaut, ToucheBas, ToucheDroit, ToucheGauche, ToucheEspace;
    Rectangle Ecran;
    Navire Vaisseau;
    // Liste de tous les objets du jeu (missiles, bombes, aliens)
    LinkedList <Objet> Objets;
    int score;
    boolean finJeu;
    int nombreAliensVivants;
    int nombreViesRestantes;
    Font font;
    AudioClip sonShoot;
    
    
    
    public Jeu(int tx, int ty) throws MalformedURLException {
        super();
        URL urlTir = Paths.get("C:/Users/FALLOUH/git/Projet-Info/test_menu/res/shoot.wav").toUri().toURL();
        sonShoot =  Applet.newAudioClip(urlTir);
        sonShoot.play();
        setSize(tx,ty);
        temps=0;
        ToucheHaut=false;
        ToucheBas=false;
        ToucheDroit =false;
        ToucheGauche = false;
        ToucheEspace = false;
        
        Ecran=new Rectangle(getInsets().left,getInsets().top,getSize().width-getInsets().right-getInsets().left,getSize().height-getInsets().bottom-getInsets().top);
        
        ArrierePlan =new BufferedImage(getSize().width,getSize().height,
        BufferedImage.TYPE_INT_RGB);
        buffer = ArrierePlan.getGraphics();
        
        timer = new Timer(17*3, new TimerAction());
        
        // Créer la liste chainée en mémoire
        Objets = new LinkedList <Objet> ();
        // Créer le navire
        Vaisseau = new Navire(Ecran,"NAVIRE");
        // Ajouter le navire dans la liste d'objets
        Objets.add(Vaisseau);
        
        // Création des Aliens avec des scores différents suivant le type d'alien
        int sc=0;
        String NomsImages="";
        for (int y=0; y<5; y++) {
        switch (y) {
        case 0 :NomsImages = Alien.Type3;
        sc=40;
        break;
        case 1 :NomsImages = Alien.Type2;
        case 2 :NomsImages = Alien.Type2;
        sc=20;
        break;
        case 3 :NomsImages = Alien.Type1;
        case 4 :NomsImages = Alien.Type1;
        sc=10;
        break;
        }
        for (int x=0; x<12; x++) {
        Objet alien=new Alien(100+x*50,50+y*30,sc,NomsImages,Ecran, "ALIEN");
        Objets.add(alien);
        }
        }
        
        nombreAliensVivants=5*12; // 5 lignes de 12 Aliens
        nombreViesRestantes=3;
        // 3 vies au début du jeu
        score=0; finJeu=false;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\FALLOUH\\git\\Projet-Info\\test_menu\\res\\"+"COMPUTER.TTF"));
        } catch (Exception err) { System.out.println("Police introuvable");
                                  System.exit(0);                                      
        }
        buffer.setFont(font.deriveFont(40.0f));
        
        buffer.setColor(Color.white);
        buffer.drawString("SCORE : "+score,50,Ecran.height-20);
        buffer.drawString("Nbr de vies : "+nombreViesRestantes,
        Ecran.width/2,Ecran.height-20);
            
        this.addKeyListener(this);
        
        timer.start();
        setVisible(true);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

    }
    
    public void paint(Graphics g){
        buffer.setColor(Color.black);
        buffer.fillRect(Ecran.x,Ecran.y,Ecran.x+Ecran.width,Ecran.y+Ecran.height);
        buffer.setColor(Color.white);
        buffer.drawString("SCORE : "+score,50,Ecran.height-20);
        buffer.drawString("Nbr de vies : "+nombreViesRestantes, Ecran.width/2,Ecran.height-30);
        buffer.drawString("Nbr aliens : "+nombreAliensVivants, Ecran.width/2,Ecran.height-5);
        for (int k=0; k<Objets.size(); k++) {
        Objet O = (Objet) Objets.get(k);
        O.draw(temps, buffer);
        }
        if(finJeu){
        if(nombreAliensVivants==0){
            buffer.setColor(Color.green);
        buffer.drawString(" Easy win ",Ecran.width/2-60,Ecran.height/2);// effectuer le message que vous voulez
        }// si (nombreAliensVivants==0) alors le joueur a gagné sinon il a perdu
        else{
            buffer.setColor(Color.pink);
            buffer.drawString(" T'es mauvais ",Ecran.width/2-60,Ecran.height/2);
        }
        }
        g.drawImage(ArrierePlan,0,0,this);
        
    }
    
    private class TimerAction implements ActionListener {
    // ActionListener appelee toutes les 100 millisecondes
        public void actionPerformed(ActionEvent e) {
            boucle_principale_jeu();
            temps++;
        }
    }
    
    public void boucle_principale_jeu(){
        for (int k=0; k<Objets.size(); k++) {
        Objet O = (Objet) Objets.get(k);
        O.move(temps);
        }
        if (ToucheGauche) { Vaisseau.dx=-1; Vaisseau.dy=0; }
        else
        if (ToucheDroit) { Vaisseau.dx=+1; Vaisseau.dy=0; }
        else
        if (ToucheHaut) { Vaisseau.dx=0; Vaisseau.dy=-1; }
        else
        if (ToucheBas) { Vaisseau.dx=0; Vaisseau.dy=+1; }
        else
        { Vaisseau.dx=0; Vaisseau.dy=0; }
        // déplace l'objet sans le dessiner
        Vaisseau.move(temps);
        // force le rafraîchissement de l'image et le dessin de l'objet
        
        if (ToucheEspace) {
        Missile M=new Missile(Vaisseau.x+Vaisseau.l/2,Vaisseau.y, Ecran,"MISSILE");
        Objets.add(M);
        sonShoot.play();
        }
        int t = (int)(80*Math.random());
        if (t<=2) t=2;
        // on va simplifier le jeu en lançant un missile au hasard du haut du frame
        if (temps% ( t)==0) {
        Bombe B=new Bombe((int)(Vaisseau.x +300*Math.random()-150) ,0, Ecran, "BOMBE");
        Objets.add(B);
        }
        
        for (int k1=0; k1<Objets.size(); k1++) {
            Objet O1 = (Objet) Objets.get(k1);
            if ((O1.actif)&&(O1.nom_objet=="MISSILE"))
                for (int k2=0; k2<Objets.size(); k2++) {
                    Objet O2 = (Objet) Objets.get(k2);
                if ((O2.actif)&&(O2.nom_objet=="ALIEN"))
            
                    if (O1.Collision(O2)){
                        if (nombreAliensVivants >1) nombreAliensVivants --;
                        else {
                            nombreAliensVivants --;
                            finJeu=true;
                        }
                        // mis a jour du score
                        score+= ((Alien)O2).score;
                        // on supprimera plus tard ces objets
                        O1.actif=false;
                        O2.actif=false;
                    }
                }
        }
        // accélère le jeu toutes les 10 secondes
        if (temps%100==0) timer.setDelay((int)(timer.getDelay()*0.9));
        
        // vérifions le collision entre une bombe alien active et le navire
        for (int k=0; k<Objets.size(); k++) {
        Objet O = (Objet) Objets.get(k);
        if (O.nom_objet=="BOMBE")
        if ((O.actif)&&(Vaisseau.Collision(O)))
        {
        O.actif=false; // rendre la bombe inactive
        if (nombreViesRestantes>1) nombreViesRestantes--;
        else {
            nombreViesRestantes--;
            finJeu=true;
        }
        }
            
        // vérifie qu'un alien actif ne touche pas le vaisseau
        for (int k1=0; k1<Objets.size(); k1++) {
        Objet O1 = (Objet) Objets.get(k1);
        if ((O1.actif)&&(O1.nom_objet=="ALIEN"))
        if (O1.Collision(Vaisseau))
        finJeu=true;
        }
            
        if (finJeu) {
        timer.stop();
        repaint();
        // forcer le rafraîchissement de l'écran
        }
        
        // balaye la liste et supprime tous les objets inactifs
        // c'est le "gabrbage collector" du jeu
        for (int j=0; j<Objets.size(); j++) {
        Objet O4 = (Objet) Objets.get(j);
        if (O4.actif==false) {
        Objets.remove(j);
        j--; // parceque la liste s'est déplacée pour boucher le trou
        }
        }
            
        repaint();
    }
    } 
        public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                // barre d'espacement
                case KeyEvent.VK_SPACE  : ToucheEspace=true;
                                        break;
                // fleche gauche
                case KeyEvent.VK_LEFT   : ToucheGauche=true;
                                        break;
                // fleche droit                                          
                case KeyEvent.VK_RIGHT  : ToucheDroit=true;
                                        break;    
                // fleche haut
                case KeyEvent.VK_UP   : ToucheHaut=true;
                                        break;
                // fleche bas                                          
                case KeyEvent.VK_DOWN  : ToucheBas=true;
                                        break;    
                // Touche ESCAPE pour sortir du programme                                        
                case KeyEvent.VK_ESCAPE : System.exit(0);
                                         break;
                // Touche Pause pour arrêter ou relancer le timer
                case KeyEvent.VK_ENTER   :  if(!finJeu){
                    if (timer.isRunning()) timer.stop();
                                                         else     timer.start();
                }
                                        break;        
                }
                // pour tester les écouteurs
                //this.setTitle("Code clavier :"+Integer.toString(code));
            }
    
            public void keyReleased(KeyEvent e) {
    
                int code = e.getKeyCode();
                switch (code) {
                // barre d'espacement
                case KeyEvent.VK_SPACE  : ToucheEspace=false;
                                        break;
                // fleche gauche
                case KeyEvent.VK_LEFT   : ToucheGauche=false;
                                        break;
                // fleche droit                                          
                case KeyEvent.VK_RIGHT  : ToucheDroit=false;
                                        break;    
                // fleche haut
                case KeyEvent.VK_UP   : ToucheHaut=false;
                                        break;
                // fleche bas                                          
                case KeyEvent.VK_DOWN  : ToucheBas=false;
                                        break;
                }
                // pour tester les écouteurs
                //this.setTitle("touche relachée");
                }
            public void keyTyped(KeyEvent e) { }
            }
