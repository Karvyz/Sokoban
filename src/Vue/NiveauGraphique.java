package Vue;
/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Global.Configuration;
import Modele.Jeu;
import Modele.Niveau;
import Patterns.Observateur;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class NiveauGraphique extends JComponent implements Observateur {
	Image pousseur, mur, sol, caisse, but, caisseSurBut;
	Jeu j;
	int largeurCase;
	int hauteurCase;

	NiveauGraphique(Jeu jeu) {
		j = jeu;
		j.ajouteObservateur(this);
		pousseur = lisImage("Pousseur");
		mur = lisImage("Mur");
		sol = lisImage("Sol");
		caisse = lisImage("Caisse");
		but = lisImage("But");
		caisseSurBut = lisImage("Caisse_sur_but");
	}

	private Image lisImage(String nom) {
		InputStream in = Configuration.ouvre("Images/" + nom + ".png");
		Configuration.info("Chargement de l'image " + nom);
		try {
			// Chargement d'une image utilisable dans Swing
			return ImageIO.read(in);
		} catch (Exception e) {
			Configuration.erreur("Impossible de charger l'image " + nom);
		}
		return null;
	}

	private void tracer(Graphics2D g, Image i, int x, int y, int l, int h) {
		g.drawImage(i, x, y, l, h, null);
	}

	public void paintComponent(Graphics g) {
		Graphics2D drawable = (Graphics2D) g;
		Niveau n = j.niveau();

		int largeur = getSize().width;
		int hauteur = getSize().height;
		largeurCase = largeur / n.colonnes();
		hauteurCase = hauteur / n.lignes();
		// On prend des cases carrées
		largeurCase = Math.min(largeurCase, hauteurCase);
		hauteurCase = largeurCase;

		for (int ligne = 0; ligne < n.lignes(); ligne++)
			for (int colonne = 0; colonne < n.colonnes(); colonne++) {
				int x = colonne * largeurCase;
				int y = ligne * hauteurCase;
				// Tracé du sol
				if (n.aBut(ligne, colonne))
					tracer(drawable, but, x, y, largeurCase, hauteurCase);
				else
					tracer(drawable, sol, x, y, largeurCase, hauteurCase);
				// Tracé des objets
				if (n.aMur(ligne, colonne))
					tracer(drawable, mur, x, y, largeurCase, hauteurCase);
				else if (n.aPousseur(ligne, colonne))
					tracer(drawable, pousseur, x, y, largeurCase, hauteurCase);
				else if (n.aCaisse(ligne, colonne))
					if (n.aBut(ligne, colonne))
						tracer(drawable, caisseSurBut, x, y, largeurCase, hauteurCase);
					else
						tracer(drawable, caisse, x, y, largeurCase, hauteurCase);
			}
	}

	int hauteurCase() {
		return hauteurCase;
	}

	int largeurCase() {
		return largeurCase;
	}

	@Override
	public void miseAJour() {
		repaint();
	}
}