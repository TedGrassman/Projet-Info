package framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class customButton extends JButton implements MouseListener {

	BufferedImage[] images = new BufferedImage[3];

	Son sonBoutonClic, sonBoutonEntered;

	String[] NomImage = new String[3];
	String libelle;

	int h, l;
	int state = 0, code;

	public customButton(String libelle, int code) {
		super();
		this.code = code;
		enableInputMethods(true);
		addMouseListener(this);
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.libelle = libelle;

		sonBoutonClic = new Son("res/sons/bouton-fx-185.wav");
		sonBoutonEntered = new Son("res/sons/boutonEntered.wav");
		switch (code) {
		case 0:
			NomImage[0] = "LP0.png";
			NomImage[1] = "LP1.png";
			NomImage[2] = "LP2.png";
			break;
		case 1:
			NomImage[0] = "exit0.png";
			NomImage[1] = "exit1.png";
			NomImage[2] = "exit2.png";
			break;
		}

		try {
			for (int k = 0; k < 3; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (final Exception err) {
			System.out.println(NomImage[0] + " introuvable !");
			System.exit(0);
		}

		h = images[0].getHeight(); // récupère une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth();
		setSize(getPreferredSize());
	}

	public Dimension getPreferredSize() {
		return new Dimension(l, h);
	}

	public Dimension getMinimumSize() {
		return new Dimension(l, h);
	}

	public Dimension getMaximumSize() {
		return new Dimension(l, h);
	}

	public void paintComponent(Graphics g) {
		paintBorder(g);
		g.drawImage(images[state], 0, 0, null);
		switch (code){
		case 0 :
			g.setColor(new Color(0.2f, 0.2f, 0.3f, 0.9f));
			break;
		case 1 :
			g.setColor(new Color(1f, 1f, 0.6f, 0.9f));
			break;
		}
		
		g.setFont(new Font("Harrington", 1, 20));
		drawCenteredString(libelle, g);
	}

	public void drawCenteredString(String s, Graphics g) {
		final FontMetrics fm = g.getFontMetrics();
		final int x = (l - fm.stringWidth(s)) / 2;
		final int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		final Graphics2D g2d = (Graphics2D) g.create(); // anti aliasing sur le texte
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawString(s, x, y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		sonBoutonClic.jouer();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		state = 2;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		state = 1;
		sonBoutonClic.stop();
		sonBoutonEntered.jouer();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		state = 0;
	}

}
