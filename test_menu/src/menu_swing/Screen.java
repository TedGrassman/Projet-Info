package menu_swing;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Screen extends JFrame implements ActionListener, KeyListener {
private static final long serialVersionUID = 1L;

int width, height;

JButton play = new JButton("Lancer une partie");
JButton settings = new JButton("Options");
JButton exit = new JButton("Ragequit");
JButton mainMenu = new JButton("Retour menu");

CardLayout layout = new CardLayout();

JPanel panel = new JPanel();
JPanel game = new JPanel();
JPanel menu = new JPanel(); 

JMenuBar jmb = new JMenuBar();
JMenu jmJeu = new JMenu("Jeu");
JMenuItem jmiRagequit = new JMenuItem("Ragequit");

boolean joue = true;

Thread gameThread = new Thread() { // inutile en l'état, il faudrait que la méthode Jeu s'exécute à chaque cycle
    public void run(){
    	//while(joue){
       try {
		game = new Jeu(700, 480);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // ici il faudrait appeler la boucle de jeu
       panel.add(game, "Game");
       layout.show(panel, "Game");
       game.requestFocus();
    //	}
    }
    public void kill(){
    	joue=false;
    }
};

public Screen(int width, int height) {
   this.width = width;
   this.height = height;

   panel.setLayout(layout);        
   addButtons();

   setSize(width, height);
   setResizable(false);
   setLocationRelativeTo(null);
   setVisible(true);
   setTitle("NomDuJeu");
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   requestFocus();

}

private void addButtons() {

   play.addActionListener(this);
   settings.addActionListener(this);
   exit.addActionListener(this);
   mainMenu.addActionListener(this);

   //menu buttons
   menu.add(play);
   menu.add(settings);
   menu.add(exit);
   
   //Menu persistant

   jmb.add(jmJeu);
   jmJeu.add(jmiRagequit);
   jmiRagequit.addActionListener(this);
   this.setJMenuBar(jmb);

   //game buttons
   

   //background colors
   //game.setBackground(Color.MAGENTA);
   menu.setBackground(Color.GREEN);

   //adding children to parent Panel
   panel.add(menu,"Menu");
   //panel.add(game,"Game");

   add(panel);
   layout.show(panel,"Menu");

}

public void actionPerformed(ActionEvent event) {

   Object source = event.getSource();

   if (source == exit) {
       System.exit(0);
   } else if (source == play) {
	   
       gameThread.start();
	   
       
       //this.validate();
   } else if (source == settings){

   } else if (source == mainMenu){
       layout.show(panel, "Menu");
   } else if (source == jmiRagequit){
	   // modification d'une variable entraînant la fin du thread
   }

   }

@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyPressed(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}
}