/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverothello;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce le operazioni non inerenti al gioco
 * 
 * @author Bianchi Filippo
 * @author Cesarano Andrea
 * @author Longoni Lorenzo
 * @author Pellicanò Fabio
 * @author Riboni Andrea
 * @author Siracusa Andrea
 */
public class UtilServer extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static final Vector<Socket> clients = new Vector<>();

    /**
     * Costruttore
     * @param sock socket del client che intende effettuare l'azione
     */
    public UtilServer(Socket sock) {
        try {
            socket = sock;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(UtilServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gestisce login e registrazione
     */
    @Override
    public void run() {
        try {
            String input = in.readLine();
            //Se vuole registrarsi, lo faccio. (Idem per il login)
            if (input.contains("register")) {
                String[] user = input.split(";");
                if (Utility.registra(user[1], user[2])) { //register;nome;pass
                    System.out.println(user[1] + " has just registered.");
                    out.println("registered");
                } else {
                    System.out.println(user[1] + " can not be registered.");
                    out.println("regerror");
                }
                socket.close();
            } else if (input.contains("login")) { //login;nome;pass
                String[] user = input.split(";");
                if (Utility.login(user[1], user[2])) {
                    System.out.println(user[1] + " has just logged in.");
                    BufferedReader br = new BufferedReader(new FileReader("users\\"+user[1] + ".dat"));
                    String temp = "", line;
                    while ((line = br.readLine()) != null) {
                        temp += line;
                    }
                    out.println(temp);
//                    out.println(br.readLine());
                } else {
                    out.println("logerror");
                    System.out.println(user[1] + " has just tried to log in.");
                }
                socket.close();
            } else {
                if (input.equals("connection;")) {
                    //Se è suo intento giocare una partita, attendo un secondo socket
                    //e li inoltro ad un thread che si occupi di loro due.
                    clients.add(socket);
                    System.out.println(socket + " to the queue.");
                    if (clients.size() % 2 == 0) {
                        ServerThread match = new ServerThread();
                        match.setClients(clients);
                        System.out.println("Two Clients Reached.");
                        if (match.canConnect()) {
                            System.out.println("Starting a match.");
                            match.start();
                        }
                    }
                }
            }
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
        }
    }
}
