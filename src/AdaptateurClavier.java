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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdaptateurClavier extends KeyAdapter {
	Jeu j;
	NiveauGraphique n;
	InterfaceGraphique f;

	AdaptateurClavier(Jeu jeu, NiveauGraphique niv, InterfaceGraphique fen) {
		j = jeu;
		n = niv;
		f = fen;
	}

	void deplace(int dl, int dc) {
		j.deplace(dl, dc);
		n.repaint();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				deplace(0, -1);
				break;
			case KeyEvent.VK_RIGHT:
				deplace(0, 1);
				break;
			case KeyEvent.VK_UP:
				deplace(-1, 0);
				break;
			case KeyEvent.VK_DOWN:
				deplace(1, 0);
				break;
			case KeyEvent.VK_Q:
			case KeyEvent.VK_A:
				System.exit(0);
				break;
			case KeyEvent.VK_ESCAPE:
				f.toggleFullscreen();
				break;
		}
	}
}
