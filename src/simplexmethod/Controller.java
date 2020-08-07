/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import javafx.stage.Stage;

/**
 *
 * @author Ilya
 */
public class Controller {

    static View view;

    public static void SetScene(Stage primaryStage) throws ClassNotFoundException, NullPointerException, WrongNumException {
        view = new View(primaryStage);
    }
}
