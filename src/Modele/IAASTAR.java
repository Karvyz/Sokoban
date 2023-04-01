package Modele;

import Global.Configuration;
import Structures.Sequence;

import java.util.*;

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
        Niveau niveau;
        ArrayList<Case> chemin;
        Case2 pere;

        Case2(ArrayList<Case> chemin, Case2 pere, int l, int c) {
            this.chemin = chemin;
            this.pere = pere;
            this.c = c;
            this.l = l;
            this.niveau = pere.niveau.clone();
        }
        Case2(ArrayList<Case> chemin, Case2 pere, int l, int c, Niveau niveau) {
            this.chemin = chemin;
            this.pere = pere;
            this.c = c;
            this.l = l;
            this.niveau = niveau;
        }
    }
    @Override
    public Sequence<Coup> joue() {
        Sequence<Coup> resultat = Configuration.nouvelleSequence();
        int[] caisse = niveau.caisselc();
        System.out.println("caisse : " + Arrays.toString(caisse));
        int[] destination = niveau.objectivelc();
        System.out.println("objective : " + Arrays.toString(destination));

        ArrayList<Case> chemin = deplacement_caisse(caisse[0], caisse[1], destination[0], destination[1]);
        System.out.println("Resultat");
        chemin.forEach(c -> System.out.println(c.l + " " + c.c));
        for (int i = 0; i < chemin.size() - 1; i++) {
            resultat.insereQueue(niveau.deplace(chemin.get(i + 1).l - chemin.get(i).l, chemin.get(i + 1).c - chemin.get(i).c));
        }
        return resultat;
    }

    private ArrayList<Case> deplacement_caisse(int startl, int startc, int destl, int destc) {
        ArrayDeque<Case2> fifo = new ArrayDeque<>();
        boolean[][] cases_availables = niveau.case_checked();
        fifo.add(new Case2(new ArrayList<>(), null, startl, startc, niveau.clone()));
        while (!fifo.isEmpty()){
            Case2 pere = fifo.getFirst();
            fifo.removeFirst();
            if (pere.l == destl && pere.c == destc) {
                System.out.println("fini");
                ArrayList<ArrayList<Case>> chemins = new ArrayList<>();
                while(pere != null) {
                    chemins.add(pere.chemin);
                    pere = pere.pere;
                }
                Collections.reverse(chemins);
                ArrayList<Case> chemin_global = new ArrayList<>();
                for (ArrayList<Case> chemin : chemins) {
                    System.out.println("part chemin");
                    chemin.forEach(c -> System.out.println(c.l + " " + c.c));
                    chemin_global.addAll(chemin);
                }
                return chemin_global;
            }
            ArrayList<Case2> liste_fils = new ArrayList<>();
            liste_fils.add(new Case2(null, pere, pere.l + 1, pere.c));
            liste_fils.add(new Case2(null, pere, pere.l - 1, pere.c));
            liste_fils.add(new Case2(null, pere, pere.l, pere.c + 1));
            liste_fils.add(new Case2(null, pere, pere.l, pere.c - 1));
            for (Case2 fils : liste_fils) {
                System.out.println("nouveau fils " + fils.l + " " + fils.c + " qui a comme pere : " + pere.l + " " + pere.c);
                if (!cases_availables[fils.l][fils.c]) {
                    continue;
                }
                cases_availables[fils.l][fils.c] = false;
                if (fils.niveau.aMur(fils.l, fils.c))
                    continue;

                int objectivel = pere.l + (pere.l - fils.l);
                int objectivec = pere.c + (pere.c - fils.c);
                System.out.println("pousseur vas de " + fils.niveau.pousseurL + " " + fils.niveau.pousseurC + " vers " + objectivel + " " + objectivec);
                if (objectivec == fils.niveau.pousseurC && objectivel == fils.niveau.pousseurL) {
                    accept_case2(fifo, new ArrayList<>(), pere, fils);
                }
                else {
                    ArrayList<Case> chemin = deplacement_pousseur(pere.niveau, objectivel, objectivec);
                    System.out.println("taille chemin : " + chemin.size());
                    if (chemin.size() > 0) {
                        accept_case2(fifo, chemin, pere, fils);
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    void accept_case2(ArrayDeque<Case2> fifo, ArrayList<Case> chemin, Case2 pere, Case2 fils) {
        chemin.add(new Case(pere.l, pere.c, null));
        System.out.println("taille chemin accepté : " + chemin.size());
        System.out.println("chemin accepté :");
        chemin.forEach(c -> System.out.println(c.l + " " + c.c));

        for (Case c : chemin) {
            if (fils.niveau.pousseurL != c.l || fils.niveau.pousseurC != c.c) {
                fils.niveau.deplace(c.l - fils.niveau.pousseurL,  c.c - fils.niveau.pousseurC);
            }
        }
        fils.chemin = chemin;

        fifo.push(fils);
    }



    private ArrayList<Case> deplacement_pousseur(Niveau n, int destl, int destc){
        ArrayList<Case> chemin = new ArrayList<>();
        ArrayDeque<Case> fifo = new ArrayDeque<>();
        boolean[][] cases_availables = niveau.case_checked();
        fifo.add(new Case(n.pousseurL, n.pousseurC, null));
        while (!fifo.isEmpty()){
            Case current = fifo.getFirst();
            fifo.removeFirst();
            if (current.c == destc && current.l == destl) {
                while (current != null) {
                    chemin.add(current);
                    current = current.pere;
                }
                Collections.reverse(chemin);

                return chemin;
            }
            ArrayList<Case> listeFils = new ArrayList<>();
            listeFils.add(new Case(current.l + 1, current.c, current));
            listeFils.add(new Case(current.l - 1, current.c, current));
            listeFils.add(new Case(current.l, current.c + 1, current));
            listeFils.add(new Case(current.l, current.c - 1, current));
            for (Case fils :listeFils){
                if (cases_availables[fils.l][fils.c]) {
                    cases_availables[fils.l][fils.c] = false;
                    if (n.estOccupable(fils.l, fils.c)) {
                        fifo.add(fils);
                    }
                }
            }
        }
        return chemin;
    }
}
