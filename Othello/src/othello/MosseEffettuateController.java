/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello;

import Utility.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Longoni Lorenzo
 * @author Cesarano Andrea
 */
public class MosseEffettuateController implements Initializable {

    @FXML
    private TextArea area;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void update(ActionEvent event) {
        area.setText("");
        try {
            for (int i = 0; i < Client.MosseEffettuate.size(); i++) {
                area.appendText(Client.MosseEffettuate.get(i) + "\n");
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        area.setText("");
    }

}
