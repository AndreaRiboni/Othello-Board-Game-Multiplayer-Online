/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Bianchi Filippo
 */
public class iAiutoController implements Initializable {

    @FXML
    private Label LabelRegole;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LabelRegole.setText("L'Othello è un gioco su scacchiera a due giocatori."
                + "\n\nIl primo giocatore userà le\npedine nere e il secondo le pedine bianche."
                + "\n\nDurante il proprio turno,\nil giocatore può posizionare la pedina su una casella vuota."
                + "\n\nL'obiettivo è 'imprigionare' le\npedine avversarie (orizzontalmente, verticalmente o in obliquo)."
                + "\n\nUna volta imprigionata, la pedina\navversaria assumerà il colore del giocante."
                + "\n\nIl giocatore è obbligato a capovolgere\nalmeno una pedina per mossa."
                + "\n\nSe non dovesse essere possibile,\nil turno va all'avversario."
                + "\n\nIl gioco termina all'impossibilità\ndi giocare da parte di entrambi i giocatori."
                + "\n\nSarà detto vincitore colui che,\nal termine, ha più pedine dell'avversario.");
    }    
    
}
