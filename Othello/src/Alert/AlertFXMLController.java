/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Alert;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Riboni Andrea
 */
public class AlertFXMLController implements Initializable {

    @FXML
    private TextArea Descrizione;
    @FXML
    private Label Titolo;
    @FXML
    private Label Sottotitolo;
    @FXML
    private Button Cancella;
    @FXML
    private Button Conferma;

    /**
     * Se settato a vero, l'utente ha scelto l'opzione "affermativa" proposta
     */
    public static boolean confirmed = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Titolo.setText(AlertStuff.getTitolo());
        Sottotitolo.setText(AlertStuff.getSottotitolo());
        Descrizione.setText(AlertStuff.getDescrizione());
        Descrizione.setEditable(false);
        if(!AlertStuff.isInterfaccia()){
            Cancella.setVisible(false);
            Conferma.setVisible(false);
        }
    }    

    @FXML
    private void cancella(ActionEvent event) {
        confirmed = false;
        AlertStuff.chiudi();
    }

    @FXML
    private void conferma(ActionEvent event) {
        confirmed = true;
        AlertStuff.chiudi();
    }
}
