package framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

public class customButton extends JButton implements MouseListener, ActionListener {
	
	BufferedImage[] images = new BufferedImage[3];
	
	String[] NomImage = new String[3];
	String libellé;
	
	int h,l;
	int state=0;
	
	public customButton(String libellé){
		super();
		enableInputMethods(true);
		addMouseListener(this);
		this.setBorder(BorderFactory.createEmptyBorder(50, 0, 5, 0));
		this.libellé=libellé;
		
		//switch(libellé){
		//case "Lancer une partie":
			NomImage[0]="LP0.png";
			NomImage[1]="LP1.png";
			NomImage[2]="LP2.png";
			//break;
		//}
		
		try {
			for (int k = 0; k < 3; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (Exception err) {
			System.out.println(NomImage[0] + " introuvable !");
			System.exit(0);
		}
		
		h = images[0].getHeight(null);			//récupère une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth(null);
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
	
	public void paintComponent(Graphics g) {
		this.paintBorder(g);
		g.drawImage(images[state], 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Harrington", 1, 20));
		drawCenteredString(libellé, g);
	}
	
	public void drawCenteredString(String s, Graphics g) {
	    FontMetrics fm = g.getFontMetrics();
	    int x = (l - fm.stringWidth(s)) / 2;
	    int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(s, x, y);
	  }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		state=2;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		state=1;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		state=0;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
