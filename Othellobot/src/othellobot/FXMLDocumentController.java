/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othellobot;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * Controller
 * 
 * @author Riboni Andrea
 */
public class FXMLDocumentController implements Initializable {


    @FXML
    private TextField IPField;
    @FXML
    private TextField PortField;
    @FXML
    private Ellipse StatusConnection;
    @FXML
    private Ellipse StatusMatch;
    @FXML
    private Label StartButton;
    @FXML
    private Ellipse ButtConnect;
    @FXML
    private Rectangle cont1;
    @FXML
    private Rectangle cont2;
    @FXML
    private Slider LevelDiff;
    @FXML
    private Label POWERED;
    @FXML
    private Label dif;
    @FXML
    private Label algorithm;

    private Utility utility;
    private boolean connected = false, requested = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PathTransition path = new PathTransition();
        path.setNode(POWERED);
        path.setDuration(Duration.seconds(10));
        path.setCycleCount(PathTransition.INDEFINITE);
        path.setPath(new Line(400,10,-200,10));
        path.play();
    }

    @FXML
    private void start(MouseEvent event) {
        if (!requested && connected) {
            utility.setDifficult(LevelDiff.getValue());
            utility.start();
            cont1.setFill(Paint.valueOf("#aa4444"));
            cont2.setFill(Paint.valueOf("#aa4444"));
            StatusMatch.setFill(Paint.valueOf("#b6ed9e"));
            StartButton.setDisable(true);
            LevelDiff.setDisable(true);
            requested = false;
        } else {
            StatusMatch.setFill(Paint.valueOf("#aa4444"));
        }
    }

    @FXML
    private void connetti(MouseEvent event) {
        if (!connected) {
            System.out.println("Connecting");
            try {
                utility = new Utility(IPField.getText(), Integer.parseInt(PortField.getText()));
            } catch (NumberFormatException e) {
            }
            connected = utility.connect();
            if (connected) {
                ButtConnect.setFill(Paint.valueOf("#222222"));
                ButtConnect.setStroke(Paint.valueOf("#aa4444"));
                StatusConnection.setFill(Paint.valueOf("#b6ed9e"));
                IPField.setDisable(true);
                PortField.setDisable(true);
            } else {
                StatusConnection.setFill(Paint.valueOf("#aa4444"));
            }
        }
    }
    
    @FXML
    private void update(MouseEvent event) {
        dif.setText((int)LevelDiff.getValue()+"");
        String alg = "";
        switch((int)LevelDiff.getValue()){
            case 1:
                alg = "Random Algorithm";
                break;
            case 2:
                alg = "Best Temporal Move";
                break;
            case 3:
                alg = "Minimax [5]";
                break;
            case 4:
                alg = "Minimax [7]";
                break;
            case 5:
                alg = "Minimax [9]";
                break;
        }
        algorithm.setText(alg);
    }

}
