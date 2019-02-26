/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello;

import Utility.Client;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Listener di una cella
 * @author Riboni Andrea
 */
public class iOthelloListener implements EventHandler<Event> {

    private final int row, column;

    /**
     * Costruttore.
     * 
     * @param row riga della cella
     * @param col colonna della cella
     */
    public iOthelloListener(int row, int col) {
        this.row = row;
        column = col;
    }

    /**
     * Evento relativo al click del mouse
     * @param event 
     */
    @Override
    public void handle(Event event) {
        //Se è il mio turno e ho cliccato con il mouse
        if (event.getEventType().getName().equals("MOUSE_CLICKED") && Client.MyTurn) {
            //Invio il comando al server
            Client.place(row, column);
//            iOthello.update();
        }
    }

}
