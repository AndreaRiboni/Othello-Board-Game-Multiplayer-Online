/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverothello;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 *
 * @author Pellicanò Fabio
 * @author Riboni Andrea
 * @author Siracusa Andrea
 */
public class ServerOthello {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {

        System.out.println("                           \n"
                + " _____ _   _       _ _     \n"
                + "|     | |_| |_ ___| | |___ \n"
                + "|  |  |  _|   | -_| | | . |\n"
                + "|_____|_| |_|_|___|_|_|___|\n"
                + "                           \n");

        Server server = new Server(4444);
        server.start();
        System.out.println("   Localhost:   "+Inet4Address.getLocalHost()+"\n   Port Number: 4444\n");
        System.out.println("Server started. Looking for clients...");
    }

}
