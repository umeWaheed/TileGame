/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author ACER
 */
public class startController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button newButton,resumeGame;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }   
    
    @FXML
    public void startNewGame(ActionEvent e){
        try {
            //hide previous window
           ((Node)(e.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/OptionView.fxml"));
            GridPane root = (GridPane)loader.load();
            Scene scene = new Scene(root,180,180);
            
            Stage primaryStage = new Stage();
           
            
            
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception ex) {
                ex.printStackTrace();
        }
    }
    
    @FXML
    public void resume(ActionEvent e){
        try {
            //hide previous window
           ((Node)(e.getSource())).getScene().getWindow().hide();
            int board[][]=xmlReader.readFile();     //read tile values
            int highscore = xmlReader.highScore;            //get high score
            int score = xmlReader.score;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/BoardView.fxml"));
            GridPane root = (GridPane)loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            
            BoardViewController ctrl = loader.getController();
            ctrl.setBoard(xmlReader.rows,xmlReader.cols,xmlReader.level,xmlReader.moves,score, highscore, board);
            
            primaryStage.setMinWidth(ctrl.cols*60+80);
            primaryStage.setMinHeight(ctrl.rows*60+60);
            primaryStage.setMaxWidth(ctrl.cols*60+80);
            primaryStage.setMaxHeight(ctrl.rows*60+60);
            
           
               primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

         @Override
         public void handle(WindowEvent event) {
             Platform.runLater(new Runnable() {

                 @Override
                 public void run() {
                     if (!ctrl.b.checkStatus()){        //if game is not over yet
                        Alert alert = new Alert(AlertType.CONFIRMATION, "Save game? ", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait();
                        if (ctrl.highestScore<ctrl.b.getScore())
                            ctrl.highestScore = ctrl.b.getScore();
                         
                      if (alert.getResult() == ButtonType.YES) {
                           xmlReader.writeFile(ctrl.rows, ctrl.cols, ctrl.level, ctrl.highestScore, ctrl.b.getScore(), ctrl.totalMoves, ctrl.getTiles());
                      }
                   }
                   else    //if game is over store high score
                     xmlReader.writeHighScore(ctrl.highestScore);
                 }
             });
         }
     });
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception ex) {
                ex.printStackTrace();
        }
    }
    
}
