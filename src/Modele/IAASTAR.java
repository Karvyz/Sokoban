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
    @Override
    public Sequence<Coup> joue() {
        int destx = 8, desty = 7;
        int pousseurL = niveau.lignePousseur();
        int pousseurC = niveau.colonnePousseur();
        Sequence<Coup> resultat = Configuration.nouvelleSequence();
        ArrayList<Case> chemin = deplacement_possible(desty, destx, pousseurL, pousseurC);
        for (int i = 0; i < chemin.size() - 1; i++) {
            resultat.insereQueue(niveau.deplace(chemin.get(i + 1).l - chemin.get(i).l, chemin.get(i + 1).c - chemin.get(i).c));
        }
        return resultat;
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
