/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othellobot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce la logica del bot
 *
 * @author Pellicanò Fabio
 * @author Riboni Andrea
 * @author Siracusa Andrea
 */
public class Utility extends Thread {

    private Socket socket;
    private String ip;
    private int port;
    private BufferedReader in;
    private PrintWriter out;
    private static int[][] othelliera;
    public static final int BLANK = 0, BLACK = 1, WHITE = 2;
    public static boolean MyTurn = false;
    private static final String COLBLACK = "nero", COLWHITE = "bianco";
    public static String MyColor;
    private boolean end;
    private int difficulty;

    /**
     * Costruttore.
     *
     * @param ip IP del server
     * @param port Porta del server
     */
    public Utility(String ip, int port) {
        end = false;
        this.ip = ip;
        this.port = port;
        difficulty = 0;
        othelliera = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                othelliera[i][o] = BLANK;
            }
        }
    }

    /**
     * Ritorna informazioni relative a una cella
     *
     * @param r riga
     * @param c colonna
     * @return colore della cella
     */
    public int getCell(int r, int c) {
        return othelliera[r][c];
    }

    /**
     * Imposta il colore di una cella
     *
     * @param r riga
     * @param c colonna
     * @param col colore
     */
    public static void setColor(int r, int c, int col) {
        othelliera[r][c] = col;
    }

    private boolean isTransp(int r, int c) {
        try {
            return othelliera[r][c] == BLANK;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Imposta la difficoltà del gioco.
     *
     * @param diff livello di difficoltà
     */
    public void setDifficult(double diff) {
        difficulty = (int) Math.ceil(diff);
    }

    /**
     * Metodo di controllo. Nel client, ha l'unico scopo di visualizzare le
     * celle nelle quali è possibile giocare.
     *
     * @param r
     * @param c
     * @param checking se settato a falso, serve solo per testare e non aggiorna
     * il valore effettivo
     * @return Ritorna un array di interi così formattato:
     * <ul>
     * <li>Cella 0: Riga Cella</li>
     * <li>1: Colonna Cella</li>
     * <li>2: Quante pedine sono state girate se fosse stata piazzata una pedina
     * in Riga-Colonna</li>
     * <li>3: <ol>
     * <li>0 se non ha cambiato nulla</li>
     * <li>1 se ha girato almeno una pedina</li>
     * </ol>
     * </li>
     * </ul>
     */
    private int[] checkTOT(int[][] othelliera, int r, int c, boolean checking, int ActualCol) {
        int[] ritorno = new int[4];
        boolean changed = false;
        int[] directions = new int[8]; //le otto direzioni
        int maggiore = -1;
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
                    int col = othelliera[r + directions[i] * ro + ro][c + directions[i] * co + co];
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
            if (maggiore < directions[i]) {
                ritorno[0] = r;
                ritorno[1] = c;
                ritorno[2] = directions[i];
                maggiore = directions[i];
            }

        }
        ritorno[3] = changed ? 1 : 0;
        return ritorno;
    }

    /**
     * Ritorna un vettore contenente le coordinate delle celle consigliate.
     *
     * @param othelliera campo
     * @param MePlaying da settare a true per ricevere le pedine relative al
     * proprio turno; a false per il turno avversario.
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
    public Vector<Integer> getAdvice(int[][] othelliera, boolean MePlaying) {
        Vector<Integer> recommended = new Vector<>(); //conterrà le coordinate delle celle ok
        //salvo il colore attuale e quello che sto cercando
        int ActualCol, LookingFor;
        if (MePlaying) {
            ActualCol = MyColor.toLowerCase().contains("ne") ? BLACK : WHITE;
        } else {
            ActualCol = MyColor.toLowerCase().contains("ne") ? WHITE : BLACK;
        }
        LookingFor = ActualCol == BLACK ? WHITE : BLACK;
        //controllo ogni cella
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                //se la cella è del colore che sto cercando
                if (othelliera[i][o] == LookingFor) {
                    //controllo che sia vuota e che almeno una pedina si giri
                    if (isTransp(i + 1, o) && checkTOT(othelliera, i + 1, o, true, ActualCol)[3] == 1) {
                        //se così, aggiungo le sue coordinate al mio vettore
                        recommended.add(i + 1);
                        recommended.add(o);
                    }
                    //e ripeto per le altre 7 posizioni adiacenti
                    if (isTransp(i - 1, o) && checkTOT(othelliera, i - 1, o, true, ActualCol)[3] == 1) {
                        recommended.add(i - 1);
                        recommended.add(o);
                    }
                    if (isTransp(i, o - 1) && checkTOT(othelliera, i, o - 1, true, ActualCol)[3] == 1) {
                        recommended.add(i);
                        recommended.add(o - 1);
                    }
                    if (isTransp(i, o + 1) && checkTOT(othelliera, i, o + 1, true, ActualCol)[3] == 1) {
                        recommended.add(i);
                        recommended.add(o + 1);
                    }
                    if (isTransp(i - 1, o - 1) && checkTOT(othelliera, i - 1, o - 1, true, ActualCol)[3] == 1) {
                        recommended.add(i - 1);
                        recommended.add(o - 1);
                    }
                    if (isTransp(i + 1, o + 1) && checkTOT(othelliera, i + 1, o + 1, true, ActualCol)[3] == 1) {
                        recommended.add(i + 1);
                        recommended.add(o + 1);
                    }
                    if (isTransp(i + 1, o - 1) && checkTOT(othelliera, i + 1, o - 1, true, ActualCol)[3] == 1) {
                        recommended.add(i + 1);
                        recommended.add(o - 1);
                    }
                    if (isTransp(i - 1, o + 1) && checkTOT(othelliera, i - 1, o + 1, true, ActualCol)[3] == 1) {
                        recommended.add(i - 1);
                        recommended.add(o + 1);
                    }
                }
            }
        }
        return recommended;
    }

    /**
     * Cerca la posizione migliore in cui fare la propria mossa. Non si fa
     * riferimento alle mosse successive, quanto più alla mossa migliore che si
     * può fare nel singolo turno.
     *
     * @return coordinate della mossa
     */
    public int[] getBestPos() {
        int major = -1;
        int[] best = {-1, -1, -1}, temp = {-1, -1, -1};
        best = null;
        Vector<Integer> cells = getAdvice(othelliera, true);
        int color = MyColor.contains("ne") || MyColor.contains("bla") ? BLACK : WHITE;
        for (int i = 0; i < cells.size(); i += 2) {
            temp = checkTOT(othelliera, cells.get(i), cells.get(i + 1), true, color);
            if (major < temp[2]) {
                major = temp[2];
                best = temp;
            }
        }
        int[] array = new int[2];
        try {
            System.out.println("IN " + best[0] + ":" + best[1] + " GIRI " + major);
            array[0] = best[0];
            array[1] = best[1];
        } catch (NullPointerException e) {
        }
        return array;
    }

    private void printMatrix(int[][] othelliera) {
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                System.out.print(othelliera[i][o]);
            }
            System.out.println("");
        }
    }

    /**
     * Tenta di connettersi al server
     *
     * @return true se è stato possibile
     */
    public boolean connect() {
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void process(String input) {
        if (input.startsWith("start")) {
            if (input.contains("nero") || input.contains("black")) {
                MyColor = "nero";
            } else {
                MyColor = "bianco";
            }
            System.out.println("Color setted: " + MyColor);
            MyTurn = false;
        } else if (input.startsWith("round")) {
            MyTurn = ((input.contains("nero") || input.contains("black")) && MyColor.equals(COLBLACK))
                    || ((input.contains("bianco") || input.contains("white")) && MyColor.equals(COLWHITE));
            if (MyTurn) {
                System.out.println("It's my turn");
                play();
                MyTurn = false;
            } else {
                System.out.println("Not my turn.");
            }
        } else if (input.startsWith("update")) {
            System.out.println("Updating the game");
            update(input);
            MyTurn = false;
        } else if (input.startsWith("end")) {
            System.out.println("Ended");
            end = true;
        }
    }

    /**
     * La mossa scelta è data da minimax
     *
     * @return Stringa da passare al canale di comunicazione con il server
     */
    private String highLevel(int diff) {
        int[] best = {0, 0, Integer.MIN_VALUE}, temp = {0, 0, 0};
        Vector<Integer> moves = getAdvice(othelliera, true);
        int mycolor = MyColor.contains("ne") ? BLACK : WHITE;
        for (int i = 0; i < moves.size(); i += 2) {
            int[][] figlio = othelliera.clone();
            getSon(figlio, moves.get(i), moves.get(i + 1), mycolor);
            temp[0] = moves.get(i);
            temp[1] = moves.get(i + 1);
            switch (diff) {
                case 3:
                    temp[2] = minimax(figlio, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, MyTurn);
                    break;
                case 4:
                    temp[2] = minimax(figlio, 7, Integer.MIN_VALUE, Integer.MAX_VALUE, MyTurn);
//                temp[2] = negamax(figlio, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                    break;
                case 5:
                    temp[2] = minimax(figlio, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, MyTurn);
                    break;
            }
            if (temp[2] > best[2]) {
                best = temp;
            }
//            System.out.println("MOSSA NUMERO " + i + ": " + moves.get(i) + "," + moves.get(i + 1));
        }
//            System.out.println("Ending the evaluation");
        String output = "place: " + best[0] + ", " + best[1] + ";";
//            System.out.println("Best position found >>>> " + output);
        return output;
    }

    /**
     * La mossa scelta è quella che gira più pedine
     *
     * @return Stringa da passare al canale di comunicazione con il server
     */
    private String Level2() {
        int[] best = getBestPos();
        String output = "place: " + best[0] + ", " + best[1] + ";";
        System.out.println("Best position found >>>> " + output);
        return output;
    }

    /**
     * Mosse casuali
     *
     * @return Stringa da passare al canale di comunicazione con il server
     */
    private String Level1() {
        Vector<Integer> available = getAdvice(othelliera, true);
        int chosen = (int) (Math.random() * available.size());
        if (chosen % 2 == 1) {
            chosen--;
        }
        String output = "place: " + available.get(chosen) + ", " + available.get(chosen + 1) + ";";
        System.out.println("Best position found >>>> " + output);
        return output;
    }

    private void play() { //place: x, y;
        String output = null;
        float time_start = System.currentTimeMillis();
        switch (difficulty) {
            case 1:
                output = Level1();
                break;
            case 2:
                output = Level2();
                break;
            default:
                output = highLevel(difficulty);
                break;
        }
        float time = System.currentTimeMillis() - time_start;
        if (time < 1500) {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
            }
        }
        out.println(output);
    }

    /**
     * Si connette e risponde alle mosse fino alla fine della partita
     */
    public void run() {
        out.println("connection;");
        System.out.println("Connection requested");
        while (!end) {
            try {
                if (in.ready()) {
                    String input = null;
                    input = in.readLine().toLowerCase();
                    if (!input.contains("pdate")) {
                        System.out.println("Received >>> " + input);
                    }
                    process(input.toLowerCase());
                }
            } catch (IOException e) {
            }
        }
        System.exit(0);
    }

    private void update(String input) { //update: COLORE, X, Y;
        int row = Integer.parseInt(input.charAt(input.length() - 5) + "");
        int col = Integer.parseInt(input.charAt(input.length() - 2) + "");
        int color = (input.contains("nero") || input.contains("black")) ? BLACK : WHITE;
        othelliera[row][col] = color;
    }

    /**
     * Controlla la posizione del nodo in questione
     *
     * @param othelliera nodo
     * @return true se è un nodo terminale
     */
    private boolean isTerminal(int[][] othelliera) {
        return (getAdvice(othelliera, true).isEmpty() && getAdvice(othelliera, false).isEmpty());
    }

    /**
     * Calcola il valore euristico di un nodo
     *
     * @param othelliera nodo
     * @return valore euristico
     */
    private int heuristic(int[][] othelliera, boolean MyTurn) {
        int col = -1;
        if (MyTurn) {
            if (MyColor.equals(COLBLACK)) {
                col = BLACK;
            } else {
                col = WHITE;
            }
        } else {
            if (MyColor.equals(COLBLACK)) {
                col = WHITE;
            } else {
                col = BLACK;
            }
        }
        int opposite = col == BLACK ? WHITE : BLACK;
        int mine = 0, others = 0;
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                if (getCell(i, o) == col) {
                    mine++;
                } else if (getCell(i, o) == opposite) {
                    others++;
                }
            }
        }
        return mine - others;
    }

    private void getSon(int[][] othelliera, int r, int c, int col) {
        checkTOT(othelliera, r, c, false, col);
//        printMatrix(othelliera);
    }

    /**
     * Algoritmo di ricerca MiniMax.
     *
     * @param othelliera campo
     * @param prof profondità
     * @param row riga
     * @param col colonna
     * @return Viene ritornato il valore minimax della determinata
     */
    private int minimax(int[][] othelliera, int prof, int alfa, int beta, boolean MyTurn) {
        //Nodo terminale?
        if (prof == 0 || isTerminal(othelliera)) {
            return heuristic(othelliera, MyTurn);
        }
        Vector<Integer> sons = getAdvice(othelliera, MyTurn);
        int value, best;
        int mycolor = MyColor.contains("nero") ? BLACK : WHITE;
        int opposite = mycolor == BLACK ? WHITE : BLACK;

        if (!MyTurn) { //Giocatore minimizzante
            best = Integer.MAX_VALUE;
            for (int i = 0; i < sons.size(); i += 2) {
                int[][] figlio = othelliera.clone();
                getSon(figlio, i, i + 1, mycolor);
                value = minimax(figlio, prof - 1, alfa, beta, true);
                best = Math.max(best, value);
                alfa = Math.max(alfa, best);
                beta = Math.min(alfa, value);
                if (beta <= alfa) {
                    break;
                }
            }
        } else { //Giocatore massimizzante
            best = Integer.MIN_VALUE;
            for (int i = 0; i < sons.size(); i += 2) {
                int[][] figlio = othelliera.clone();
                getSon(figlio, i, i + 1, opposite);
                value = minimax(figlio, prof - 1, alfa, beta, false);
                best = Math.max(best, value);
                alfa = Math.max(alfa, best);
                if (beta <= alfa) {
                    break;
                }
            }
        }
        return best;
    }

    //Da non usare
    @Deprecated
    private int negamax(int[][] othelliera, int prof, int alfa, int beta, boolean MyTurn) {
        if (prof == 0 || isTerminal(othelliera)) {
            return heuristic(othelliera, MyTurn);
        }
        int best = Integer.MIN_VALUE;
        int mycolor = MyColor.contains("nero") ? BLACK : WHITE;
        Vector<Integer> sons = getAdvice(othelliera, MyTurn);
        for (int i = 0; i < sons.size(); i += 2) {
            int[][] figlio = othelliera.clone();
            getSon(figlio, i, i + 1, mycolor);
            int val = -negamax(figlio, prof - 1, -beta, -alfa, !MyTurn);
            best = Math.max(best, val);
            alfa = Math.max(alfa, val);
            if (alfa >= beta) {
                break;
            }
        }
        return best;
    }
}
