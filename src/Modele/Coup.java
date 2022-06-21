package Modele;
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
import Structures.Sequence;

public abstract class Coup {
	Sequence<Mouvement> mouvements;
	Niveau niv;
	Sequence<Marque> marques;

	public Coup(Niveau n) {
		niv = n;
		mouvements = Configuration.nouvelleSequence();
		marques = Configuration.nouvelleSequence();
	}

	public void ajouteDeplacement(int dL, int dC, int vL, int vC) {
		Mouvement m = new Mouvement(dL, dC, vL, vC);
		if (niv.appliqueMouvement(m))
			mouvements.insereQueue(m);
		else
			Configuration.alerte("Déplacement impossible : " + m.toString());
	}

	public void ajouteMarque(int l, int c, int val) {
		Marque m = new Marque(l, c, val);
		niv.appliqueMarque(m);
		marques.insereQueue(m);
	}

	public Sequence<Mouvement> mouvements() {
		return mouvements;
	}

	public Sequence<Marque> marques() {
		return marques;
	}
}
