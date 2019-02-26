/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverothello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce la partita
 *
 * @author Pellicanò Fabio
 * @author Riboni Andrea
 */
public class ServerThread extends Thread {

    private Socket black, white;
    private BufferedReader in1, in2;
    private PrintWriter out1, out2;
    private boolean RequestAccept;
    private final String match, ColBlack, ColWhite;
    /**
     * Si specificano le informazioni della partita
     */
    public boolean end = false, turn = true;
    private Othello game;
    private int errors;

    /**
     * Costruttore.
     */
    public ServerThread() {
        game = new Othello(this);
        RequestAccept = false;
        ColBlack = "nero";
        ColWhite = "bianco";
        //Il nome "univoco" della partita è un numero casuale intero a 64 bit
        match = Long.toString((long) (Math.random() * Long.MAX_VALUE));
        System.out.println("Match created: '" + match + "'");
        errors = 0;
    }

    /**
     * Imposta i giocatori di questa partita
     *
     * @param clients socket dei due client
     */
    public void setClients(Vector<Socket> clients) {
        try {
            black = clients.get(clients.size() - 2);
            white = clients.get(clients.size() - 1);
            //clients.clear();
            System.out.println("Match between " + black + " and " + white);
            in1 = new BufferedReader(new InputStreamReader(black.getInputStream()));
            out1 = new PrintWriter(black.getOutputStream(), true);
            in2 = new BufferedReader(new InputStreamReader(white.getInputStream()));
            out2 = new PrintWriter(white.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        RequestAccept = true;
    }

    /**
     * Gestisce la partita e il protocollo.
     */
    public void run() {
        //Imposto i colori
        out1.println("start: " + match + ", " + ColBlack + ";");
        out2.println("start: " + match + ", " + ColWhite + ";");
        updateCell();
        out1.println("round: nero");
        out2.println("round: nero");
        System.out.println("The match has started.");
        //Finchè la partita è in corso
        while (!end) {
            end = isEnded();
            if (end) {
                break;
            }
            try {
                //Stabilisco i canali di comunicazione, via riferimenti,
                //al giocatore attuale
                BufferedReader player = turn ? in1 : in2;
                PrintWriter playerOut = turn ? out1 : out2;
                //Leggo l'input
                String input = player.readLine();
                //Processo l'input
                process(input, playerOut);
            } catch (IOException e) {
            }

        }
        sendWinner();
    }

    /**
     * Processa l'input dell'utente
     *
     * @param in stringa ricevuta
     * @param out stream di output
     */
    private void process(String in, PrintWriter out) {
        String name = out.equals(out1) ? "black" : "white";
        System.out.println(name + " said -> " + in);
        try {
            if (in.startsWith("pla")) { //place
                //Salvo riga, colonna e colore.
                int row = Integer.parseInt(in.charAt(in.length() - 5) + "");
                int col = Integer.parseInt(in.charAt(in.length() - 2) + "");
                int color = turn ? Othello.BLACK : Othello.WHITE;

                //Se posso fare questa mossa
                if (game.update(row, col, color)) {
                    //Invio le celle aggiornate ai client
                    updateCell();
                    updateTurn();
                } else {
                    //Altrimenti, comunico che la mossa non è valida
                    out.println("movenotvalid;");
                }
            }
        } catch (NullPointerException e) {
            if (++errors > 32) {
                end = true;
                try {
                    black.close();
                    white.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * Aggiorna le celle e le invia ai client
     */
    private void updateCell() {
        int[][] oth = game.getOthelliera();
        //Guardo ogni cella ed invio tutte le celle non vuote.
        //I client aggiorneranno per loro conto solo quelle da aggiornare
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                if (!game.getColor(i, o).equals("vuoto")) {
                    out1.println("update: " + game.getColor(i, o) + ", " + i + ", " + o + ";");
                    out2.println("update: " + game.getColor(i, o) + ", " + i + ", " + o + ";");
                }
            }
        }
    }

    private boolean isEnded() {
        if (!black.isConnected() || !white.isConnected()) {
            return true;
        }
        //Se non posso muovermi, il gioco è finito
        if (!game.canMove(true) && !game.canMove(false)) {
            System.out.println("No one can move.");
            return true;
        }
        if(!game.canMove(turn)){
            System.out.println("You could not move.");
            updateTurn();
        }
        int[][] othelliera = game.getOthelliera();
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                if (othelliera[i][o] == Othello.BLANK) {
                    return false; //se c'è almeno una cella bianca e posso muovermi, posso continuare
                }
            }
        }
        System.out.println("No more cells.");
        return true;
    }

    private void sendWinner() {
        int[][] othelliera = game.getOthelliera();
        int white = 0, black = 0;
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                if (othelliera[i][o] == Othello.WHITE) {
                    white++;
                } else if (othelliera[i][o] == Othello.BLACK) {
                    black++;
                }
            }
        }
        System.out.println("Winner setted: " + "end: black, " + black + ", white, " + white + ";");
        out1.println("end: black, " + black + ", white, " + white + ";");
        out2.println("end: black, " + black + ", white, " + white + ";");

    }

    /**
     * Aggiorna il turn e lo comunica ai client
     */
    private void updateTurn() {
        turn = !turn;
        String color = turn ? ColBlack : ColWhite;
        out1.println("round: " + color);
        out2.println("round: " + color);
    }

    /**
     * Gestisce la connession.
     *
     * @return True se puoi connetterti.
     */
    public boolean canConnect() {
        return RequestAccept;
    }
}
