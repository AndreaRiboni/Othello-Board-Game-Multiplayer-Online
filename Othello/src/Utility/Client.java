/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import Alert.AlertStuff;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Rectangle;
import othello.GUIController;

/**
 * Gestisce la connessione al server, il login e la registrazione
 *
 * @author Riboni Andrea
 */
public class Client {

    private static Socket client;
    private static String ip;
    private static int port;
    private static BufferedReader in;
    private static PrintWriter out;
    private static Rectangle r;
    public static AlertStuff AlertBox;
    private static String winner;
    /**
     * Numero di partite vinte
     */
    public static int WonMatches = 0;
    /**
     * Indica se è il mio turno
     */
    public static boolean MyTurn = false;
    /**
     * Indica il mio colore
     */
    public static String MyColor;

    /**
     * Pila contenente le mosse effettuate
     */
    public static Vector<String> MosseEffettuate;
//    public static Vector<int[][]> VecchieOthelliere;

    /**
     * "Costruttore" statico
     *
     * @param ip Indirizzo ip con cui connettersi.
     * @param port porta con cui connettersi.
     * @param r rettangolo di feedback
     * @return vero se è possibile inizializzare
     */
    public static boolean init(String ip, int port, Rectangle r) {
        Client.r = r;
        Client.ip = ip;
        Client.port = port;
        AlertBox = new AlertStuff();
        MosseEffettuate = new Vector<String>();
        try {
            client = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Si occupa di effettuare fare il login dell'utente.
     *
     * @param n Nome.
     * @param p Password.
     * @return Ritorna se il login è stato effettuato.
     */
    public static boolean login(String n, String p) {
        boolean okay = false;
        try {
            out.println("login;" + n + ";" + p);
            String str;
            str = in.readLine();
            if (!str.contains("error")) {
                okay = true;
                String[] data = str.split(";");
                System.out.println("Received: "+str);
                try {
                    WonMatches = Integer.parseInt(data[2]);
                    AlertBox.setTitolo("Bentornato,");
                    AlertBox.setSottotitolo(data[0]+".");
                    String goal = WonMatches == 1 ? "a" : "e";
                    AlertBox.setDescrizione("Secondo i registri, hai vinto almeno "+WonMatches+" partit"+goal+".");
                    AlertBox.show();
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                AlertBox.setTitolo("Attento!");
                AlertBox.setSottotitolo("Login Errato");
                AlertBox.setDescrizione("Probabilmente hai inserito nome o password errati.");
                AlertBox.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return okay;
    }

    /**
     * Effettua la registrazione dell'utente nel server.
     *
     * @param n Nome.
     * @param p Password.
     * @return Ritorna true se è stato possibile registrarsi.
     */
    public static boolean register(String n, String p) {
        boolean okay = false;
        try {
            out.println("register;" + n + ";" + p);
            if (!in.readLine().contains("error")) {
                okay = true;
            } else {
                AlertBox.setTitolo("Attento!");
                AlertBox.setSottotitolo("Non puoi registrarti così");
                AlertBox.setDescrizione("Il nome utente è già stato scelto.");
                AlertBox.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return okay;
    }

    /**
     * Invia una richiesta "connection;" al server
     */
    public static void sendRequest() {
        out.println("connection;");
        Thread LilThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String received = in.readLine(); //start partita colore
                    //estraggo il colore
                    System.out.println("Received: " + received);
                    if (received.toLowerCase().contains("nero")) {
                        MyColor = "nero";
                    } else {
                        MyColor = "bianco";
                    }
                    //avvio la partita su un thread specifico
                    ClientThread partita = new ClientThread(client, in, out);
                    winner = partita.go();
                } catch (IOException e) {
                }
            }
        });
        LilThread.start();
    }

    /**
     * Invia al server una richiesta di place
     *
     * @param x riga
     * @param y colonna
     */
    public static void place(int x, int y) {
        out.println("place: " + x + ", " + y + ";");
        MosseEffettuate.add("place> Riga: " + x + ", Colonna: " + y + ", Colore: " + MyColor + ";");
    }

    /**
     * termina la connessione
     */
    public static void disconnect() {
        try {
            client.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
