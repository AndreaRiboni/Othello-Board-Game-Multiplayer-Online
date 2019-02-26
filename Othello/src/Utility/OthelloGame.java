/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.util.Vector;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Gestisce la logica di Othello
 *
 * @author Riboni Andrea
 */
public class OthelloGame {

    private static int[][] othelliera;
    /**
     * Costanti. Si indica il colore di una cella.
     */
    public static final int BLANK = 0, BLACK = 1, WHITE = 2;
    private static final Paint COLBLACK = Paint.valueOf("#211c17"), COLWHITE = Paint.valueOf("#edecdc");

    /**
     * Costruttore. Crea l'othelliera
     */
    public OthelloGame() {
        othelliera = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                othelliera[i][o] = BLANK;
            }
        }
    }

    /**
     * Ritorna il colore della cella indicata.
     *
     * @param r Righe.
     * @param c Colonne:
     * @return colore della cella indicata.
     */
    public int getCell(int r, int c) {
        return othelliera[r][c];
    }

    /**
     * Ritorna l'oggetto colore della cella indicata.
     *
     * @param r Righe.
     * @param c Colonne:
     * @return colore della cella indicata.
     */
    public Paint getColor(int r, int c) {
        try {
            switch (othelliera[r][c]) {
                case WHITE:
                    return COLWHITE;
                case BLACK:
                    return COLBLACK;
                default:
                    return Color.TRANSPARENT;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return Color.TRANSPARENT;
        }
    }

    /**
     * Setta il colore della cella indicata.
     *
     * @param r Righe.
     * @param c Colonne.
     * @param col Colore.
     */
    public static void setColor(int r, int c, int col) {
        othelliera[r][c] = col;
    }

    /**
     * Metodo di controllo. Nel client, ha l'unico scopo di visualizzare le
     * celle nelle quali è possibile giocare. Impostare il parametro checking a
     * falso permette di avere solamente un feedback cognitivo della situazione,
     * senza effettivamente girare delle pedine.
     *
     * @param r Righe.
     * @param c Colonne.
     * @param checking Se falso, serve solo per testare e non aggiorna il valore
     * effettivo
     */
    private boolean checkTOT(int r, int c, boolean checking, int ActualCol) {
        boolean changed = false;
        int[] directions = new int[8]; //le otto direzioni
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
        }
        return changed;
    }

    /**
     * Controlla se una cella è vuota
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
     * Ritorna un vettore contenente le coordinate delle celle consigliate.
     *
     * @return Viene ritornato un Vector di interi di dimensione pari. Le celle
     * consigliate sono così formattate:
     * <ul>
     * <li> Coordinata X della prima cella </li>
     * <li> Coordinata Y della prima cella </li>
     * <li> Coordinata X della seconda cella </li>
     * <li> Coordinata Y della seconda cella </li>
     * </ul>
     * e così via.
     */
    public Vector<Integer> getAdvice() {
        Vector<Integer> recommended = new Vector<>(); //conterrà le coordinate delle celle ok
        //salvo il colore attuale e quello che sto cercando
        int ActualCol = Client.MyColor.toLowerCase().contains("ne") ? BLACK : WHITE;
        int LookingFor = ActualCol == BLACK ? WHITE : BLACK;
        //controllo ogni cella
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                //se la cella è del colore che sto cercando
                if (othelliera[i][o] == LookingFor) {
                    //controllo che sia vuota e che almeno una pedina si giri
                    if (isTransp(i + 1, o) && checkTOT(i + 1, o, true, ActualCol)) {
                        //se così, aggiungo le sue coordinate al mio vettore
                        recommended.add(i + 1);
                        recommended.add(o);
                    }
                    //e ripeto per le altre 7 posizioni adiacenti
                    if (isTransp(i - 1, o) && checkTOT(i - 1, o, true, ActualCol)) {
                        recommended.add(i - 1);
                        recommended.add(o);
                    }
                    if (isTransp(i, o - 1) && checkTOT(i, o - 1, true, ActualCol)) {
                        recommended.add(i);
                        recommended.add(o - 1);
                    }
                    if (isTransp(i, o + 1) && checkTOT(i, o + 1, true, ActualCol)) {
                        recommended.add(i);
                        recommended.add(o + 1);
                    }
                    if (isTransp(i - 1, o - 1) && checkTOT(i - 1, o - 1, true, ActualCol)) {
                        recommended.add(i - 1);
                        recommended.add(o - 1);
                    }
                    if (isTransp(i + 1, o + 1) && checkTOT(i + 1, o + 1, true, ActualCol)) {
                        recommended.add(i + 1);
                        recommended.add(o + 1);
                    }
                    if (isTransp(i + 1, o - 1) && checkTOT(i + 1, o - 1, true, ActualCol)) {
                        recommended.add(i + 1);
                        recommended.add(o - 1);
                    }
                    if (isTransp(i - 1, o + 1) && checkTOT(i - 1, o + 1, true, ActualCol)) {
                        recommended.add(i - 1);
                        recommended.add(o + 1);
                    }
                }
            }
        }
        return recommended;
    }

}
