package framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class roundButton extends JButton implements MouseListener { // boutons ronds "sélectionnables"

	static ArrayList<roundButton> liste = new ArrayList<roundButton>();

	BufferedImage[] images = new BufferedImage[4]; // images à afficher

	Son sonBoutonClic, sonBoutonEntered; // sons à jouer lors des clics et lorsque la souris est sur le bouton

	String[] NomImage = new String[4]; // nom des images à charger et afficher
	String libelle; // texte à afficher sur le bouton

	int h, l; // hauteur et largeux max de l'image
	int state = 0; // etat du bouton 0=neutre 1=éclairé 2=clic maintenu
					// 3=sélectionné
	int taillePolice, type; // type permet de créer des boutons de lots différents et d'apparence différente

	Shape shape;

	public roundButton(String libelle, int code) {
		super();
		enableInputMethods(true);
		addMouseListener(this);
		this.libelle = libelle;

		sonBoutonClic = new Son("res/sons/bouton-fx-185.wav");
		sonBoutonEntered = new Son("res/sons/boutonEntered.wav");
		type = code;
		switch (code) { // change le skin du bouton selon son type
		case 0:
			NomImage[0] = "small0.png";
			NomImage[1] = "small1.png";
			NomImage[2] = "small2.png";
			NomImage[3] = "small3.png";
			taillePolice = 80;
			break;
		case 1:
			NomImage[0] = "small0.png";
			NomImage[1] = "small1.png";
			NomImage[2] = "small2.png";
			NomImage[3] = "small3.png";
			taillePolice = 50;
			break;
		}

		try {
			for (int k = 0; k < images.length; k++)
				images[k] = ImageIO.read(new File("res/" + NomImage[k]));
		} catch (final Exception err) {
			System.out.println(NomImage[0] + " introuvable !");
			System.exit(0);
		}

		h = images[0].getHeight(null); // récupère une fois pour toutes la hauteur et largeur de l'image
		l = images[0].getWidth(null);

		setContentAreaFilled(false); // ne dessine ni le fond ni les bords du bouton
		setBorderPainted(false);

		liste.add(this);
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
		g.drawImage(images[state], 0, 0, null);
		g.setColor(new Color(0.2f, 0.2f, 0.4f, 0.99f));
		g.setFont(new Font("Harrington", 1, taillePolice));
		drawCenteredString(libelle, g);
	}

	public boolean contains(int x, int y) { // définition de la méthode contains
											// pour détecter les clics
		if (shape == null) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}

	public void drawCenteredString(String s, Graphics g) {
		final FontMetrics fm = g.getFontMetrics();
		final int x = (l - fm.stringWidth(s)) / 2;
		final int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		final Graphics2D g2d = (Graphics2D) g.create(); // anti aliasing sur le texte
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawString(s, x, y);
	}

	public void unselect() { // déselectionne tous les autres boutons du lot
		for (int i = 0; i < liste.size(); i++) {
			if (liste.get(i) != this && liste.get(i).type == type) {
				liste.get(i).state = 0;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (state != 3) {
			sonBoutonClic.jouer();
			unselect();
			state = 3;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (state != 3)
			state = 2;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if (state != 3) {
			state = 1;
			sonBoutonClic.stop();
			sonBoutonEntered.jouer();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if (state != 3)
			state = 0;
	}
}
