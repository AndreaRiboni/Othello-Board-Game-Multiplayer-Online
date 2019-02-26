/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello;

import Alert.AlertStuff;
import Utility.Client;
import Utility.ClientThread;
import Utility.OthelloGame;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import static othello.iOthello.othello;

/**
 * FXML Controller class
 *
 * @author Pellicanò Fabio
 * @author Siracusa Andrea
 */
public class iProfiloController implements Initializable {

    @FXML
    private TextField nome;
    @FXML
    private TextField psw;
    @FXML
    private TextField ip;
    @FXML
    private TextField porta;
    @FXML
    private Rectangle feedback;
    @FXML
    private Button logbutton;
    @FXML
    private Button regbutton;
    @FXML
    private Button startbutton;
    @FXML
    private Button noLogbutton;
    @FXML
    private Label NBianchi;
    @FXML
    private Label NNeri;
    @FXML
    private Rectangle feedbackpartita;
    
    private boolean profiled = false;
    public static Rectangle STATUS, STATUSPARTITA;
    public static Label NUMWHITE, NUMBLACK;
    public static String winner;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        STATUS = feedback;
        STATUSPARTITA = feedbackpartita;
        NUMWHITE = NBianchi;
        NUMBLACK = NNeri;
        feedback.setArcHeight(250);
        feedback.setArcWidth(250);
        nome.setDisable(true);
        psw.setDisable(true);
        logbutton.setDisable(true);
        regbutton.setDisable(true);
        noLogbutton.setDisable(true);
        ip.setDisable(!ClientThread.end);
        porta.setDisable(!ClientThread.end);
        startbutton.setDisable(!ClientThread.end);
    }

    @FXML
    private void login(ActionEvent event) {
        if (Client.login(nome.getText(), psw.getText())) {
            hide(true);
//            feedback.setFill(Color.GREEN);
            startbutton.setDisable(false);
            noLogbutton.setDisable(true);
            profiled = true;
        } else {
//            feedback.setFill(Color.RED);
            hide(true);
            ip.setDisable(false);
            porta.setDisable(false);
            startbutton.setDisable(false);
        }
    }

    @FXML
    private void registrati(ActionEvent event) {
        if (Client.register(nome.getText(), psw.getText())) {
            hide(true);
//            feedback.setFill(Color.GREEN);
            startbutton.setDisable(false);
            noLogbutton.setDisable(true);
            profiled = true;
        } else {
//            feedback.setFill(Color.RED);
            hide(true);
            ip.setDisable(false);
            porta.setDisable(false);
            startbutton.setDisable(false);
        }
    }

    @FXML
    private void start(ActionEvent event) {
        try {
            if (Client.init(ip.getText(), Integer.parseInt(porta.getText()), feedback)) {
                ip.setDisable(true);
                porta.setDisable(true);
                startbutton.setDisable(true);
                noLogbutton.setDisable(false);
                nome.setDisable(profiled);
                psw.setDisable(profiled);
                logbutton.setDisable(profiled);
                regbutton.setDisable(profiled);
            }
        } catch (Exception e) {
            feedback.setFill(Paint.valueOf("#aa4444"));
        }
    }

    private void hide(boolean b) {
        nome.setDisable(b);
        psw.setDisable(b);
        ip.setDisable(b);
        porta.setDisable(b);
        startbutton.setDisable(b);
        logbutton.setDisable(b);
        regbutton.setDisable(b);
    }

    @FXML
    private void pass(ActionEvent event) {
        hide(true);
        feedback.setFill(Color.GREEN);
        noLogbutton.setDisable(true);
        AlertStuff alert = new AlertStuff();
        alert.setTitolo("Ottimo");
        alert.setSottotitolo("Attendi");
        alert.setDescrizione("La partita comincerà a breve, sei stato messo in coda.");
        alert.show();
        Client.sendRequest();
        GUIController.showOthello();
    }

    @FXML
    private void conta(ActionEvent event) {
        int b = 0, w = 0;
        try {
            for (int i = 0; i < 8; i++) {
                for (int o = 0; o < 8; o++) {
                    if (othello.getCell(i, o) == OthelloGame.BLACK) {
                        b++;
                    } else if (othello.getCell(i, o) == OthelloGame.WHITE) {
                        w++;
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        NBianchi.setText(w + "");
        NNeri.setText(b + "");
    }

    @FXML
    private void resetta(ActionEvent event) {
        NBianchi.setText("");
        NNeri.setText("");
    }

}
