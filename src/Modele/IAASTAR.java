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

        int taille_chemin_total;
        int priorite;
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
            taille_chemin_total = 0;
            this.niveau = pere.niveau.clone();
        }
        Case2(ArrayList<Case> chemin, Case2 pere, int l, int c, Niveau niveau) {
            this.chemin = chemin;
            this.pere = pere;
            this.c = c;
            this.l = l;
            taille_chemin_total = 0;
            this.niveau = niveau;
        }
    }
    class Case2Comparator implements Comparator<Case2> {
        public int compare(Case2 c1, Case2 c2) {
            return c1.priorite - c2.priorite;
        }
    }

    ArrayList<int[]> enumeration(Niveau n) {
        Queue<int[]> caisses = new ArrayDeque<>(n.caisselc());
        Queue<int[]> destinations = new ArrayDeque<>(n.objectivelc());
        int k = caisses.size();
        for (int j = 0; j < k; j++) {
            int[] caisse = caisses.poll();
            for (int i = 0; i < destinations.size(); i++) {
                int[] destination = destinations.poll();

                Result result = deplacement_caisse(n, caisse[0], caisse[1], destination[0], destination[1], 500);

                if (result != null) {
                    if (caisses.size() == 0) {
                        return result.chemin;
                    }
                    ArrayList<int[]> suite_chemin = enumeration(result.niveau);
                    if (suite_chemin != null) {
                        result.chemin.addAll(suite_chemin);
                        return result.chemin;
                    }
                }
                destinations.add(destination);
            }
            caisses.add(caisse);
        }
        return null;
    }

    @Override
    public Sequence<Coup> joue() {
        Sequence<Coup> resultat = Configuration.nouvelleSequence();
        long start_timer = System.currentTimeMillis();
        ArrayList<int[]> chemin_global = enumeration(niveau);
        System.out.println("Duree de recherche : " + (System.currentTimeMillis() - start_timer) + "ms");

        if (chemin_global == null) {
            System.out.println("pas de chemin possible");
            return resultat;
        }
        for (int i = 0; i < chemin_global.size(); i++) {
            resultat.insereQueue(niveau.deplace(chemin_global.get(i)[0], chemin_global.get(i)[1]));
        }
        return resultat;
    }

    int distanceManhattan(int l1, int c1, int l2, int c2) {
        return Math.abs(l1 - l2) + Math.abs(c1 - c2);
    }

     class Result {
        ArrayList<int[]> chemin;
        Niveau niveau;

         public Result(ArrayList<int[]> chemin, Niveau niveau) {
             this.chemin = chemin;
             this.niveau = niveau;
         }
     }


    private Result deplacement_caisse(Niveau n, int startl, int startc, int destl, int destc, int max_depth) {
        PriorityQueue<Case2> fap = new PriorityQueue<>(new Case2Comparator());
        fap.add(new Case2(new ArrayList<>(), null, startl, startc, n.clone()));
        int depth = 0;
        while (!fap.isEmpty() && depth < max_depth){
            depth++;
            Case2 pere = fap.poll();
            if (pere.l == destl && pere.c == destc) {
                ArrayList<ArrayList<Case>> chemins = new ArrayList<>();
                ArrayList<Niveau> niveaus = new ArrayList<>();
                Niveau niveau_actuel = pere.niveau;
                while(pere != null) {
                    niveaus.add(pere.niveau);
                    chemins.add(pere.chemin);
                    pere = pere.pere;
                }
                Collections.reverse(niveaus);
//                niveaus.forEach(Niveau::print);
                Collections.reverse(chemins);
                ArrayList<Case> chemin_global = new ArrayList<>();
                chemin_global.add(new Case(n.pousseurL, n.pousseurC, null));
                for (ArrayList<Case> chemin : chemins) {
                    chemin_global.addAll(chemin);
                }
                ArrayList<int[]> resultat = new ArrayList<>();
                for (int i = 0; i < chemin_global.size() - 1; i++) {
                    int[] t = new int[2];
                    t[0] = chemin_global.get(i + 1).l - chemin_global.get(i).l;
                    t[1] = chemin_global.get(i + 1).c - chemin_global.get(i).c;
                    resultat.add(t);
                }
                return new Result(resultat, niveau_actuel);
            }
            ArrayList<Case2> liste_fils = new ArrayList<>();
            liste_fils.add(new Case2(null, pere, pere.l + 1, pere.c));
            liste_fils.add(new Case2(null, pere, pere.l - 1, pere.c));
            liste_fils.add(new Case2(null, pere, pere.l, pere.c + 1));
            liste_fils.add(new Case2(null, pere, pere.l, pere.c - 1));
            for (Case2 fils : liste_fils) {

                if (fils.niveau.aMur(fils.l, fils.c) || fils.niveau.aCaisse(fils.l, fils.c))
                    continue;

                int objectivel = pere.l + (pere.l - fils.l);
                int objectivec = pere.c + (pere.c - fils.c);
                if (objectivec == fils.niveau.pousseurC && objectivel == fils.niveau.pousseurL) {
                    accept_case2(fap, new ArrayList<>(), pere, fils, destl, destc);
                }
                else {
                    ArrayList<Case> chemin = deplacement_pousseur(pere.niveau, objectivel, objectivec, pere.l, pere.c, true);
                    if (chemin.size() > 0) {
                        accept_case2(fap, chemin, pere, fils, destl, destc);
                    }
                    else {
                        chemin = deplacement_pousseur(pere.niveau, objectivel, objectivec, pere.l, pere.c, false);
                        if (chemin.size() > 0) {
                            accept_case2(fap, chemin, pere, fils, destl, destc);
                        }
                    }
                }
            }
        }
        return null;
    }

    void accept_case2(PriorityQueue<Case2> fap, ArrayList<Case> chemin, Case2 pere, Case2 fils, int destl, int destc) {
        chemin.add(new Case(pere.l, pere.c, null));

        for (Case c : chemin) {
            if (fils.niveau.pousseurL != c.l || fils.niveau.pousseurC != c.c) {
                fils.niveau.deplace(c.l - fils.niveau.pousseurL,  c.c - fils.niveau.pousseurC);
            }
        }
        fils.chemin = chemin;
        fils.taille_chemin_total = pere.taille_chemin_total + chemin.size();
        fils.priorite = fils.taille_chemin_total + distanceManhattan(fils.l, fils.c, destl, destc);
        fap.add(fils);
    }



    private ArrayList<Case> deplacement_pousseur(Niveau n, int destl, int destc, int caissel, int caissec, boolean desactiver_deplacement_caisse){
        ArrayList<Case> chemin = new ArrayList<>();
        ArrayDeque<Case> fifo = new ArrayDeque<>();
        boolean[][] cases_availables = niveau.case_checked();
        fifo.add(new Case(n.pousseurL, n.pousseurC, null));
        while (!fifo.isEmpty()){
            Case current = fifo.getFirst();
            fifo.removeFirst();
            if (current.c == destc && current.l == destl) {
                while (current.l != n.pousseurL || current.c != n.pousseurC) {
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
                    if (!n.aMur(fils.l, fils.c) && !n.aPousseur(fils.l, fils.c) && (fils.l != caissel || fils.c != caissec)) {
                        if (n.aCaisse(fils.l, fils.c)) {
                            if (n.aBut(fils.l, fils.c) || desactiver_deplacement_caisse)
                                continue;
                            int dCaisL = fils.l - current.l + fils.l;
                            int dCaisC = fils.c - current.c + fils.c;

                            if (n.estOccupable(dCaisL, dCaisC)) {
                                fifo.add(fils);
                            }
                        }
                        else {
                            fifo.add(fils);
                        }
                    }
                }
            }
        }
        return chemin;
    }
}
