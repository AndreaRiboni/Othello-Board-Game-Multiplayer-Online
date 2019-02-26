/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import Alert.AlertStuff;
import static Utility.Client.AlertBox;
import static Utility.Client.MyColor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.paint.Paint;
import othello.GUIController;
import othello.iOthello;
import othello.iProfiloController;

/**
 * Gestice la partita e il protocollo
 *
 * @author Riboni Andrea
 * @author Siracusa Andrea
 * @author Pellicanò Fabio
 */
public class ClientThread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String winner;
    /**
     * Indica se la partita è in corso: se è vero, non è in corso.
     */
    public static boolean end = true;
    private int WinnerColor;

    /**
     * Costruttore
     *
     * @param socket socket utilizzato
     * @param in canale di comunicazione in input
     * @param out canale di comunicazione in output
     */
    public ClientThread(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        end = false;
        WinnerColor = -1;
        winner = "";
    }

    /**
     * Processa una richiesta di UPDATE
     *
     * @param in stinga di update
     */
    private void update(String in) {
        int row, col, color;
        row = Integer.parseInt(in.charAt(in.length() - 5) + "");
        col = Integer.parseInt(in.charAt(in.length() - 2) + "");
        color = in.toLowerCase().contains("nero") ? 1 : 2;
        OthelloGame.setColor(row, col, color);
        iOthello.update();
    }

    /**
     * Processa una richiesta di START
     *
     * @param in
     */
    private void begin(String in) {
        if (in.toLowerCase().contains("bia")) {
            Client.MyColor = "bianco";
        } else {
            Client.MyColor = "nero";
        }
    }

    /**
     * Processa una richiesta di ROUND
     *
     * @param in
     */
    private void round(String in) {
        Client.MyTurn = in.toLowerCase().contains(Client.MyColor); //è il mio turno
        if (Client.MyTurn) {
            iProfiloController.STATUS.setFill(Paint.valueOf("#44aa44"));
        } else {
            iProfiloController.STATUS.setFill(Paint.valueOf("#aa4444"));
        }
    }

    /**
     * Processa il comando ricevuto
     *
     * @param in stringa con il comando
     */
    private synchronized void process(String in) {
        System.out.println("Received: " + in);
        switch (in.toLowerCase().substring(0, 3)) {
            case "sta":
                begin(in);
                break;
            case "rou":
                round(in);
                break;
            case "upd":
                update(in);
                break;
            case "end":
                end = true;
                winner = in;
                String[] decode = in.split(",");
                int nero = Integer.parseInt(decode[1].replace(" ", "").replace(";", ""));
                int bianco = Integer.parseInt(decode[3].replace(" ", "").replace(";", ""));
                GUIController.alertbox.setTitolo("FINE");
                String colore = MyColor.toLowerCase();
                boolean vinto = false,
                 pareggio = false;
                if (nero > bianco) {
                    vinto = (colore.equals("black") || colore.equals("nero"));
                } else if (bianco > nero) {
                    vinto = (colore.equals("white") || colore.equals("bianco"));
                } else {
                    pareggio = true;
                }
                if (vinto) {
                    iProfiloController.STATUSPARTITA.setFill(Paint.valueOf("#44aa44"));
                } else {
                    iProfiloController.STATUSPARTITA.setFill(Paint.valueOf("#aa4444"));
                }
                if (pareggio) {
                    iProfiloController.STATUSPARTITA.setFill(Paint.valueOf("#aaaa44"));
                }
                break;
        }
    }

    /**
     * Processa le stringhe del protocollo fino al completamento della partita
     */
    public String go() {
        while (!end) {
            try {
                String input = in.readLine();
                process(input);
            } catch (IOException e) {
            }
        }
        System.out.println("Ended");
        return winner;
    }
}
