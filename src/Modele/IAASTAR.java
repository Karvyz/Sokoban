package Modele;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IAASTAR extends IA{
    ArrayDeque<Case> fifo;
    boolean[][] cases_available;

    class Case {
        int x;
        int y;
        Case pere;
        Case(int x, int y, Case pere){
            this.x = x;
            this.y = y;
            this.pere = pere;
        }
    }
    @Override
    public Sequence<Coup> joue() {
        int destx = 8, desty = 7;
        int pousseurL = niveau.lignePousseur();
        int pousseurC = niveau.colonnePousseur();
        fifo = new ArrayDeque();
        cases_available = niveau.case_checked();
        Sequence<Coup> resultat = Configuration.nouvelleSequence();
        fifo.add(new Case(pousseurC, pousseurL, null));
        while (!fifo.isEmpty()){
            Case tmp = fifo.getFirst();
            fifo.removeFirst();
            if (parcours(tmp, destx, desty)) {
                ArrayList<Case> chemin = new ArrayList<>();
                while (tmp != null) {
                    chemin.add(tmp);
                    tmp = tmp.pere;
                }
                Collections.reverse(chemin);
                for (int i = 0; i < chemin.size() - 1; i++) {
                    resultat.insereQueue(niveau.deplace(chemin.get(i + 1).y - chemin.get(i).y, chemin.get(i + 1).x - chemin.get(i).x));
                }
                return resultat;
            }
        }
        return resultat;
    }

    private boolean parcours(Case current, int destx, int desty){

        if (current.x == destx && current.y == desty){
            return true;
        }
        ArrayList<Case> listeFils = new ArrayList<>();
        listeFils.add(new Case(current.x + 1, current.y, current));
        listeFils.add(new Case(current.x - 1, current.y, current));
        listeFils.add(new Case(current.x, current.y + 1, current));
        listeFils.add(new Case(current.x, current.y - 1, current));
        for (Case fils :listeFils){
            if (cases_available[fils.y][fils.x]) {
                cases_available[fils.y][fils.x] = false;
                if (niveau.estOccupable(fils.y, fils.x)) {
                    fifo.add(fils);
                }
            }
        }
        return false;
    }
}
