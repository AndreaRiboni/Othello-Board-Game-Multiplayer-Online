/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverothello;

import java.util.Vector;

/**
 * Logica di Othello
 *
 * @author Riboni Andrea
 */
public class Othello {

    private int[][] othelliera;
    /**
     * Costanti. Viene specificato il colore
     */
    public static final int BLANK = 0, BLACK = 1, WHITE = 2;
    private ServerThread match;

    /**
     * Logica di una singola partita.
     *
     * @param partita identifica la partita corrente.
     */
    public Othello(ServerThread partita) {
        this.match = partita;
        othelliera = new int[8][8];
        othelliera[3][3] = WHITE;
        othelliera[4][4] = WHITE;
        othelliera[4][3] = BLACK;
        othelliera[3][4] = BLACK;
    }

    /**
     * Stampa la matrice.
     */
    public void print() {
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                System.out.print("|" + othelliera[i][o] + "|");
            }
            System.out.println("");
        }
    }

    /**
     * Ritorna il colore sottoforma di intero della cella
     *
     * @param r riga
     * @param c colonna
     * @return colore
     */
    public int getCell(int r, int c) {
        return othelliera[r][c];
    }

    /**
     * Ritorna il colore della cella sottoforma di stringa.
     *
     * @param r riga
     * @param c colonna
     * @return colore
     */
    public String getColor(int r, int c) {
        if (othelliera[r][c] == WHITE) {
            return "bianco";
        }
        if (othelliera[r][c] == BLACK) {
            return "nero";
        }
        return "vuoto";
    }

    /**
     * Metodo di control del campo.
     *
     * @param r riga nella quale posizionare la pedina
     * @param c colonna nella quale posizionare la pedina
     * @param checking se settato a vero, serve solo per testare e non aggiorna
     * il valore effettivo
     * @param ActualCol colore della pedina
     */
    private boolean checkTOT(int r, int c, boolean checking, int ActualCol) {
        if (getCell(r, c) != BLANK) {
            return false;
        }
        boolean changed = false;
        int[] directions = new int[8]; //le otto directions
        for (int i = 0; i < directions.length; i++) {
            boolean control = true;
            //offset
            int ro = 0, co = 0;
            /*
            Ogni direzione è contraddistinta da un offset sulle righe e sulle colonne.
            Per esempio, la direzione UP è una riga sopra e zero colonne di differenza
            e così via.
            ro indica l'offset delle righe, co quello delle colonne
             */
            switch (i) {
                case 0:
                    ro = 0;
                    co = 1;
                    break;
                case 1:
                    ro = 0;
                    co = -1;
                    break;
                case 2:
                    ro = -1;
                    co = 0;
                    break;
                case 3:
                    ro = 1;
                    co = 0;
                    break;
                case 4:
                    ro = 1;
                    co = 1;
                    break;
                case 5:
                    ro = -1;
                    co = -1;
                    break;
                case 6:
                    ro = 1;
                    co = -1;
                    break;
                case 7:
                    ro = -1;
                    co = 1;
                    break;
            }
            while (control) { //controllo tutte le celle nella direzione indicata che possono essere girate
                try {
                    //salvo il colore della cella adiacente
                    int col = getCell(r + directions[i] * ro + ro, c + directions[i] * co + co);
                    //se il colore è vuoto, non ci interessa
                    if (col == ActualCol || col == BLANK) {
                        control = false;
                        if (col == BLANK) {
                            directions[i] = 0;
                        }
                    } else {
                        directions[i]++;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    directions[i] = 0;
                    control = false;
                }
            }
            changed = changed || directions[i] > 0;
            //coloro tutte le celle trovate
            if (!checking) {
                for (int o = 0; o <= directions[i]; o++) {
                    setColor(r + o * ro, c + o * co, ActualCol);
                }
            } else if (changed) {
                return true;
            }
        }
        return changed;
    }

    /**
     * Imposta il colore di una cella
     *
     * @param r riga
     * @param c colonna
     * @param col colore
     */
    private void setColor(int r, int c, int col) {
        othelliera[r][c] = col;
    }

    /**
     * Controlla se la cella è vuota
     *
     * @param r riga
     * @param c colonna
     * @return true se è vuota
     */
    private boolean isTransp(int r, int c) {
        try {
            return othelliera[r][c] == BLANK;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Aggiorna le celle.
     *
     * @param r Riga.
     * @param c Colonna.
     * @param color Colore.
     * @return True se è cambiata una cella.
     */
    public boolean update(int r, int c, int color) {

        if (checkTOT(r, c, true, color)) {
//            System.out.println("I can do that move.");
            return checkTOT(r, c, false, color);
        } else {
//            System.out.println("update method could not do that move.");
//            print();
            return false;
        }
    }

    /**
     * Ritorna l'othellira
     *
     * @return Othelliera.
     */
    public int[][] getOthelliera() {
        return othelliera;
    }

    /**
     * Controlla la possibilità di effettuare una mossa
     *
     * @param myturn true se si cercano i neri, false per i bianchi
     * @return true se ci si può muovere
     */
    public boolean canMove(boolean myturn) {
        //salvo il colore attuale e quello che sto cercando
        int ActualCol = myturn ? BLACK : WHITE;
        int LookingFor = ActualCol == BLACK ? WHITE : BLACK;
        //controllo ogni cella
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                //se la cella è del colore che sto cercando
                if (othelliera[i][o] == LookingFor) {
                    //controllo che sia vuota e che almeno una pedina si giri
                    if (isTransp(i + 1, o) && checkTOT(i + 1, o, true, ActualCol)) {
                        //se così, aggiungo le sue coordinate al mio vettore
                        return true;
                    }
                    //e ripeto per le altre 7 posizioni adiacenti
                    if (isTransp(i - 1, o) && checkTOT(i - 1, o, true, ActualCol)) {
                        return true;
                    }
                    if (isTransp(i, o - 1) && checkTOT(i, o - 1, true, ActualCol)) {
                        return true;
                    }
                    if (isTransp(i, o + 1) && checkTOT(i, o + 1, true, ActualCol)) {
                        return true;
                    }
                    if (isTransp(i - 1, o - 1) && checkTOT(i - 1, o - 1, true, ActualCol)) {
                        return true;
                    }
                    if (isTransp(i + 1, o + 1) && checkTOT(i + 1, o + 1, true, ActualCol)) {
                        return true;
                    }
                    if (isTransp(i + 1, o - 1) && checkTOT(i + 1, o - 1, true, ActualCol)) {
                        return true;
                    }
                    if (isTransp(i - 1, o + 1) && checkTOT(i - 1, o + 1, true, ActualCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
