/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale.
 *
 * @author Bianchi Filippo
 * @author Cesarano Andrea
 * @author Longoni Lorenzo
 * @author Pellicanò Fabio
 * @author Riboni Andrea
 * @author Siracusa Andrea
 */
public class Othello extends Application {

    /**
     * Stage - Finestra dell'applicativo.
     */
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        root = new Group(root, GUIController.OthelloScene);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("An Othello game");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.exit(1);
        }
    }

}
