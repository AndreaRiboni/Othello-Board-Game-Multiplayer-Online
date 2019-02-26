/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello;

import Utility.OthelloGame;
import static Utility.OthelloGame.BLACK;
import static Utility.OthelloGame.WHITE;
import java.util.Vector;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Classe GUI di Othello
 *
 * @author Riboni Andrea
 */
public class iOthello {

    private static Rectangle[][] othelliera;
    private static Ellipse[][] pieces;
    private Group parent;
    /**
     * Logica di Othello e Othelliera
     */
    protected static OthelloGame othello;

    /**
     * Cosruttore.
     */
    public iOthello() {
        //Creo il gioco e la grafica di base
        othello = new OthelloGame();
        parent = new Group();
        othelliera = new Rectangle[8][8];
        pieces = new Ellipse[8][8];

        float dim = 685 / 8;
        for (int i = 0; i < 8; i++) {
            for (int o = 0; o < 8; o++) {
                //Creo la grafica a griglia
                othelliera[i][o] = new Rectangle(o * dim, i * dim, dim, dim);
                pieces[i][o] = new Ellipse(o * dim + dim / 2, i * dim + dim / 2, dim / 2 - 10, dim / 2 - 10);
                //Le pedine sono trasparenti, il campo verde
                othelliera[i][o].setFill(Paint.valueOf("#4e685e"));
                pieces[i][o].setFill(othello.getColor(i, o));
                //Il campo ha i bordi visibili
                othelliera[i][o].setStrokeWidth(1);
                pieces[i][o].setStrokeWidth(0);
                othelliera[i][o].setStroke(Color.BLACK);
                //Aggiungo ad ogni pedina il rispettivo listener
                pieces[i][o].addEventHandler(EventType.ROOT, new iOthelloListener(i, o));
                //Aggiugno al contenitore
                parent.getChildren().add(othelliera[i][o]);
                parent.getChildren().add(pieces[i][o]);
            }
        }
    }

    /**
     * Ritorna la grafica
     *
     * @return othelliera attuale
     */
    public Group getGraphics() {
        return parent;
    }

    /**
     * Aggiorna il colore delle pedine e dell'othelliera
     */
    public synchronized static void update() {
        Platform.runLater(() -> {
            for (int i = 0; i < 8; i++) {
                for (int o = 0; o < 8; o++) {
                    //Coloro la cella di verde
                    othelliera[i][o].setFill(Paint.valueOf("#4e685e"));
                    //Se devo colorare la pedina, lo faccio durante la rotazione
                    if (!othello.getColor(i, o).equals(pieces[i][o].getFill())) {
//                        RotateTransition start = new RotateTransition(Duration.millis(700), pieces[i][o]);
//                        start.setByAngle(0);
//                        start.setToAngle(90);
//                        start.setAxis(Rotate.X_AXIS);
//                        start.play();
                        pieces[i][o].setFill(othello.getColor(i, o));
//                        RotateTransition end = new RotateTransition(Duration.millis(700), pieces[i][o]);
//                        end.setAxis(Rotate.X_AXIS);
//                        end.setByAngle(90);
//                        end.setToAngle(180);
//                        end.play();
                    }
                }
            }
            //Segnalo visivamente le celle in cui è possibile effettuare una mossa
            Vector<Integer> recommended = othello.getAdvice();
            for (int i = 0; i < recommended.size(); i += 2) {
                othelliera[recommended.get((i))][recommended.get(i + 1)].setFill(Color.CORNFLOWERBLUE);
            }
        });

    }
}
