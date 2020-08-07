/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Ilya
 */
public class SimplexMethod extends Application {

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, WrongNumException, ClassNotFoundException {
             
        Controller.SetScene(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
