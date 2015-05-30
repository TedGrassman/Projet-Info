package framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class customText extends JLabel{
	Image fond;										//image derri�re le texte
	String texte;									//texte � �crire
	int l, h;										//dimensions finales de l'image
	Font police;									//police d'�criture
	
	public customText(String s, int taille){		//constructeur n�1 : � partir du texte � afficher et de la taille souhait�e
		super();
		this.setOpaque(false);						//fond transparent
		police = new Font("Hobo Std", Font.PLAIN, taille);		//police de tous les customText construits par le constructeur 1
		texte=s;
		try {													//tente de r�cup�rer le png
			for (int k = 0; k < 3; k++)
				fond = ImageIO.read(new File("res/textBG.png"));
		} catch (Exception err) {
			System.out.println("Fond texte introuvable !");
			System.exit(0);
		}
		
	    l = (int) (police.getStringBounds(texte, new FontRenderContext(null, false, false)).getWidth()+ 200);	//dimensionne l'image pour que le texte rentre dedans
	    h = (int) (police.getStringBounds(texte, new FontRenderContext(null, false, false)).getHeight()+50);
	    fond = fond.getScaledInstance(l, h, BufferedImage.SCALE_SMOOTH);
	}
	
	public customText(String s, Font f){
		super();
		this.setOpaque(false);
		police = f;
		texte=s;
		try {
			for (int k = 0; k < 3; k++)
				fond = ImageIO.read(new File("res/textBG.png"));
		} catch (Exception err) {
			System.out.println("Fond texte introuvable !");
			System.exit(0);
		}
		
	    l = (int) (police.getStringBounds(texte, new FontRenderContext(null, false, false)).getWidth())+200;
	    h = (int) (police.getStringBounds(texte, new FontRenderContext(null, false, false)).getHeight())+50 ;
	    fond = fond.getScaledInstance(l, h, BufferedImage.SCALE_SMOOTH);
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(fond, 0, 0, null);
		g.setFont(police);
		drawCenteredString(texte, g);
	}
	
	public void drawCenteredString(String s, Graphics g) {
	    FontMetrics fm = g.getFontMetrics();
	    int x = (l - fm.stringWidth(s)) / 2;
	    int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    Graphics2D g2d = (Graphics2D)g.create();					//anti aliasing sur le texte
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2d.setColor(new Color((float)(59/255), (float)(60/255), (float)(60/255), 0.85f));
	    g2d.drawString(s, x, y);
	  }
	
	public Dimension getPreferredSize(){
		return new Dimension(l, h);
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(l, h);
	}
	
	public Dimension getMaximumSize() {
		return new Dimension(l, h);
	}
}
