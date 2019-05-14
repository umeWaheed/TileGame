/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author ACER
 */
public class TileGame extends Application {
    
   public void start(Stage primaryStage) {
		try {
                        GridPane root = (GridPane)FXMLLoader.load(getClass().getClassLoader().getResource("view/StartMenu.fxml"));
			Scene scene = new Scene(root,180,180);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Game");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       launch(args);
    }
    
}
