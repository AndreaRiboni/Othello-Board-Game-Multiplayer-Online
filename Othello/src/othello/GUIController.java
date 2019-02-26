/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello;

import Alert.AlertStuff;
import Utility.Client;
import Utility.ClientThread;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Siracusa Andrea
 * @author Riboni Andrea
 * @author Longoni Lorenzo
 * @author Cesarano Andrea
 */
public class GUIController implements Initializable {

    @FXML
    private BorderPane BorderPane;
    @FXML
    private MenuBar Menu;
    @FXML
    private Menu MenuFile;
    @FXML
    private MenuItem ButtScreenshot;
    @FXML
    private MenuItem ButtEsci;
    @FXML
    private Menu MenuGioco;
    @FXML
    private MenuItem ButtProfilo;
    @FXML
    private Menu MenuConnect;
    @FXML
    private MenuItem ButtConnetti;
    @FXML
    private MenuItem ButtDisconnetti;
    @FXML
    private Menu MenuAiuto;
    @FXML
    private MenuItem ButtHelp;
    @FXML
    private SubScene ScenaUtility;

    /**
     * Subscene con la grafica di Othello
     */
    public static SubScene OthelloScene;

    /**
     * Rappresentazione grafica di Othello
     */
    protected static iOthello othelliera;
    public static AlertStuff alertbox;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        othelliera = new iOthello();
        alertbox = new AlertStuff();
        //Schermata di gioco
        OthelloScene = new SubScene(othelliera.getGraphics(), 685, 685);
        OthelloScene.setTranslateX(10);
        OthelloScene.setTranslateY(30);
//        othelliera.update();
        loadProfile();
    }

    /*
    Imposta la schermata di gioco
     */
    public static void showOthello() {
        OthelloScene.setRoot(othelliera.getGraphics());
    }

    @FXML
    private void takeScreenshot(Event event) {
        WritableImage img = new WritableImage((int)OthelloScene.getWidth(), (int)OthelloScene.getHeight());
        OthelloScene.getScene().snapshot(img);
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(Othello.stage);
        file = new File(file.getPath()+ ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
        } catch (IOException ex) {
            alertbox.setTitolo("Errore.");
            alertbox.setSottotitolo("Qualcosa è andato storto.");
            alertbox.setDescrizione("Lo screenshot non è stato salvato correttamente.");
            alertbox.show();
        }
    }

    @FXML
    private void chiudi(Event event) {
        alertbox.setInterfaccia(true);
        alertbox.setTitolo("Hey.");
        alertbox.setSottotitolo("Sei sicuro?");
        alertbox.setDescrizione("Pensaci bene.");
        alertbox.show();
        if (alertbox.getChoice()) {
            System.exit(0);
        }
    }

    /**
     * Carica il profilo.
     */
    private void loadProfile() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("iProfilo.fxml"));
            ScenaUtility.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void caricaProfilo(Event event) {
        //i due metodi sono uguali, ma spesso è necessario caricare il profilo
        //senza bisogno di un evento
        loadProfile();
    }


    @FXML
    private void connetti(Event event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("iProfilo.fxml"));
            ScenaUtility.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void disconnetti(Event event) {
        try {
            othelliera = new iOthello();
            GUIController.showOthello();
            othelliera.update();
            Client.disconnect();
            ClientThread.end = true;
        } catch (Exception e) {
            AlertStuff alertbox = new AlertStuff();
            alertbox.setTitolo("Eh ma");
            alertbox.setSottotitolo("Non puoi disconnetterti");
            alertbox.setDescrizione("Non sei connesso da nessuna parte.");
            alertbox.show();
        }
    }

    @FXML
    private void showAiuto(Event event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("iAiuto.fxml"));
            ScenaUtility.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mostraMosse(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MosseEffettuate.fxml"));
            ScenaUtility.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
