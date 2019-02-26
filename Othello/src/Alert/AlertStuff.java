/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Alert;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Si occupa della creazione di alert box (finestre, popup) complementari al
 * funzionamento del gioco e indicanti informazioni di vario tipo.
 *
 * @author Riboni Andrea
 */
public class AlertStuff {

    private static String titolo, sottotitolo, descrizione;
    private static boolean interfaccia;
    private static Stage finestra;

    /**
     * Costruttore
     *
     * @param titolo Nome all'interno del messaggio indicante il tipo di
     * informazione.
     * @param sottotitolo Specifica il tipo di informazione.
     * @param descrizione Utilizzata per specificare nel dettaglio
     * l'informazione.
     * @param interfaccia E' un valore booleano, che nel caso true, permette di
     * interagire con l'utente.
     */
    public AlertStuff(String titolo, String sottotitolo, String descrizione, boolean interfaccia) {
        this.titolo = titolo;
        this.sottotitolo = sottotitolo;
        this.descrizione = descrizione;
        this.interfaccia = interfaccia;
    }

    /**
     * Costruttore vuoto
     */
    public AlertStuff() {
        this.titolo = "";
        this.sottotitolo = "";
        this.descrizione = "";
        this.interfaccia = false;
    }

    private void init() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("AlertFXML.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(AlertStuff.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene s = new Scene(root, 412, 257);
        finestra = new Stage();
        finestra.setScene(s);
        finestra.setTitle("Attenzione");
    }

    /**
     * Mostra la finestra
     */
    public void show() {
        init();
        finestra.setAlwaysOnTop(true);
        finestra.setResizable(false);
        finestra.showAndWait();
    }

    /**
     * Setta il titolo.
     *
     * @param titolo Nome all'interno del messaggio indicante il tipo di
     * informazione.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;

    }

    /**
     * Setta il sottotitolo.
     *
     * @param sottotitolo Specifica il tipo di informazione.
     */
    public void setSottotitolo(String sottotitolo) {
        this.sottotitolo = sottotitolo;
    }

    /**
     * Setta la descrizione.
     *
     * @param descrizione Utilizzata per specificare nel dettaglio
     * l'informazione.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Setta il valore booleano.
     *
     * @param interfaccia E' un valore booleano, che nel caso true, permette di
     * interagire con l'utente.
     */
    public void setInterfaccia(boolean interfaccia) {
        this.interfaccia = interfaccia;
    }

    /**
     * Ritorna true se la finestra è interagibile.
     *
     * @return true se interagibile.
     */
    public static boolean isInterfaccia() {
        return interfaccia;
    }

    /**
     * Successivamente all'interazione con l'utente ritorna un valore booleano.
     *
     * @return ritorna la scelta effettuata.
     */
    public static boolean choice() {
        return AlertFXMLController.confirmed;
    }

    /**
     * Chiude la finestra.
     */
    public static void chiudi() {
        finestra.close();
    }

    /**
     * Ritorna il titolo assegnato
     * @return titolo
     */
    public static String getTitolo() {
        return titolo;
    }

    /**
     * Ritorna il sottititolo assegnato
     * @return sottotitolo
     */
    public static String getSottotitolo() {
        return sottotitolo;
    }

    /**
     * Ritorna la descrizione assegnata
     * @return descrizione
     */
    public static String getDescrizione() {
        return descrizione;
    }

    /**
     * Ritorna la scelta effettuata
     * @return se true, l'utente ha cliccato OK
     */
    public static boolean getChoice() {
        return AlertFXMLController.confirmed;
    }
}
