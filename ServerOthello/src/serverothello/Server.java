/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverothello;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestisce la connessione al server
 * 
 * @author Riboni Andrea
 */
public class Server extends Thread {

    private ServerSocket server;

    /**
     * Costruttore
     * 
     * @param port port number
     */
    public Server(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("Initialization error.");
        }
    }

    /**
     * Gestisce la connessione con il server.
     */
    @Override
    public void run() {
        try {
            while (true) {
                try {
                    //Accetto un socket e leggo il suo primo messaggio.
                    Socket socket = server.accept();
                    System.out.println("Connected: " + socket);
                    UtilServer util = new UtilServer(socket);
                    util.start();
                } catch (IOException ex) {
                    System.out.println("An error occurred while reading the user-input");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
