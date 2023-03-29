package Modele;

import Global.Configuration;
import Structures.Sequence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

public class IAASTAR extends IA{

    class Case {
        int c;
        int l;
        Case pere;
        Case(int l, int c, Case pere){
            this.l = l;
            this.c = c;
            this.pere = pere;
        }
    }

    class Case2 {

        int c;
        int l;

        int pousseurc;
        int pousseurl;
        ArrayList<Case> chemin;
        Case2 pere;

        Case2(ArrayList<Case> chemin, Case2 pere, int l, int c, int pousseurl, int pousseurc) {
            this.chemin = chemin;
            this.pere = pere;
            this.c = c;
            this.l = l;
        }
    }
    @Override
    public Sequence<Coup> joue() {
        int destx = 8, desty = 7;
        int pousseurL = niveau.lignePousseur();
        int pousseurC = niveau.colonnePousseur();
        Sequence<Coup> resultat = Configuration.nouvelleSequence();
        ArrayList<Case> chemin = deplacement_caisse(desty, destx, pousseurL, pousseurC);
        for (int i = 0; i < chemin.size() - 1; i++) {
            resultat.insereQueue(niveau.deplace(chemin.get(i + 1).l - chemin.get(i).l, chemin.get(i + 1).c - chemin.get(i).c));
        }
        return resultat;
    }

    private ArrayList<Case> deplacement_caisse(int startl, int startc, int destl, int destc) {
        ArrayDeque<Case2> fifo = new ArrayDeque<>();
        boolean[][] cases_availables = niveau.case_checked();
        fifo.add(new Case2(new ArrayList<>(), null, startl, startc, niveau.pousseurL, niveau.pousseurC));
        while (!fifo.isEmpty()){
            Case2 pere = fifo.getFirst();
            fifo.removeFirst();
            if (pere.l == destl && pere.c == destc) {
                ArrayList<ArrayList<Case>> chemins = new ArrayList<>();
                while(pere != null) {
                    chemins.add(pere.chemin);
                    pere = pere.pere;
                }
                Collections.reverse(chemins);
                ArrayList<Case> chemin_global = new ArrayList<>();
                for (ArrayList<Case> chemin : chemins) {
                    chemin_global.addAll(chemin);
                }
                return chemin_global;
            }
            Case2 fils = new Case2(null, pere, pere.l + 1, pere.c, 0, 0);
            int objectivel = pere.l + (pere.l - fils.l);
            int objectivec = pere.c + (pere.c - fils.c);
            ArrayList<Case> chemin = deplacement_possible(objectivel, objectivec, pere.pousseurl, pere.pousseurc);
            if (chemin.size() > 0) {
                fils.pousseurc = objectivec;
                fils.pousseurl = objectivel;
                fils.chemin = chemin;
                fifo.push(fils);
            }
        }
        return new ArrayList<>();
    }



    private ArrayList<Case> deplacement_possible(int destl, int destc, int startl, int startc){
        ArrayList<Case> chemin = new ArrayList<>();
        ArrayDeque<Case> fifo = new ArrayDeque<>();
        boolean[][] cases_availables = niveau.case_checked();
        fifo.add(new Case(startl, startc, null));
        while (!fifo.isEmpty()){
            Case tmp = fifo.getFirst();
            fifo.removeFirst();
            if (parcours(tmp, destl, destc, cases_availables, fifo)) {
                while (tmp != null) {
                    chemin.add(tmp);
                    tmp = tmp.pere;
                }
                Collections.reverse(chemin);

                return chemin;
            }
        }
        return chemin;
    }

    private boolean parcours(Case current, int destl, int destc, boolean[][] cases_availables, Deque<Case> fifo){
        if (current.c == destc && current.l == destl){
            return true;
        }
        ArrayList<Case> listeFils = new ArrayList<>();
        listeFils.add(new Case(current.l + 1, current.c, current));
        listeFils.add(new Case(current.l - 1, current.c, current));
        listeFils.add(new Case(current.l, current.c + 1, current));
        listeFils.add(new Case(current.l, current.c - 1, current));
        for (Case fils :listeFils){
            if (cases_availables[fils.l][fils.c]) {
                cases_availables[fils.l][fils.c] = false;
                if (niveau.estOccupable(fils.l, fils.c)) {
                    fifo.add(fils);
                }
            }
        }
        return false;
    }
}
