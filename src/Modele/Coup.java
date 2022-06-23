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
public class Coup {
	Mouvement pousseur, caisse;

	private Mouvement creeDeplacement(String nom, Mouvement existant, int dL, int dC, int vL, int vC) {
		if (existant != null) {
			Configuration.alerte("Deplacement " + nom + " déjà présent : " + existant);
		}
		return new Mouvement(dL, dC, vL, vC);
	}

	public void deplacementPousseur(int dL, int dC, int vL, int vC) {
		pousseur = creeDeplacement("pousseur", pousseur, dL, dC, vL, vC);
	}

	public void ajouteMarque(int l, int c, int val) {
		Marque m = new Marque(l, c, val);
		niv.appliqueMarque(m);
		marques.insereQueue(m);
	}

	public Sequence<Mouvement> mouvements() {
		return mouvements;
	public void deplacementCaisse(int dL, int dC, int vL, int vC) {
		caisse = creeDeplacement("caisse", caisse, dL, dC, vL, vC);
	}

	public Mouvement pousseur() {
		return pousseur;
	}

	public Mouvement caisse() {
		return caisse;
	}

	public Sequence<Marque> marques() {
		return marques;
	}
}
