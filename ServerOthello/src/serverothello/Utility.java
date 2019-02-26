/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverothello;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestisce login e password
 * 
 * @author Pellicanò Fabio
 * @author Siracusa Andrea
 */
public class Utility {

    /*
    NOME ; PASSWORD ; VITTORIE
     */
    public static final int NAME = 0, PASSWORD = 1, MATCHES = 2;
    
    /**
     * registra un utente
     *
     * @param name nome utente
     * @param password password
     * @return se il nome utente non esiste già, ritorna vero
     * @author Siracusa
     */
    public static boolean registra(String name, String password) {
        File f = new File("users\\"+name + ".dat");
        if (f.exists()) {
            return false;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(name + ";" + password + ";0;");
            bw.close();
        } catch (IOException e) {
        }
        return true;
    }

    /**
     * Login
     *
     * @return vero se il login è andato a buon fine
     */
    public static boolean login(String name, String psw) {
        File f = new File("users\\"+name + ".dat");
        if (!f.exists()) {
            return false;
        }
        try {
            BufferedReader in = null;
            boolean ok = false;
            in = new BufferedReader(new FileReader(f));
            String line, file = "";
            while ((line = in.readLine()) != null) {
                file += line;
            }
            in.close();
            String[] credentials = file.split(";");
            if (credentials[0].equals(name) && credentials[1].equals(psw)) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }

    /**
     * ritorna quale messaggio si sta mandando
     * @param str
     * @return 
     */
    public int protocol(String str) {
        if (str.startsWith("start")) {
            return 0;
        }
        if (str.startsWith("connection")) {
            return 1;
        }
        if (str.startsWith("round")) {
            return 2;
        }
        if (str.startsWith("place")) {
            return 3;
        }
        if (str.startsWith("update")) {
            return 4;
        }
        if (str.startsWith("end")) {
            return 5;
        }
        return -1;
    }

}
