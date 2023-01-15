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

class SequenceListe {
	Maillon tete, queue;

	public void insereTete(int element) {
		Maillon m = new Maillon(element, tete);
		if (queue == null)
			queue = m;
		tete = m;
	}

	public void insereQueue(int element) {
		Maillon m = new Maillon(element, null);
		if (queue == null) {
			tete = queue = m;
		} else {
			queue.suivant = m;
			queue = m;
		}
	}

	public int extraitTete() {
		int resultat;
		// Exception si tete == null (sequence vide)
		resultat = tete.element;
		tete = tete.suivant;
		if (tete == null) {
			queue = null;
		}
		return resultat;
	}

	public boolean estVide() {
		return tete == null;
	}

	public String toString() {
		String resultat = "SequenceListe [ ";
		boolean premier = true;
		Maillon m = tete;
		while (m != null) {
			if (!premier)
				resultat += ", ";
			resultat += m.element;
			m = m.suivant;
			premier = false;
		}
		resultat += " ]";
		return resultat;
	}
}
