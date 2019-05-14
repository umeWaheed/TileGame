/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.BoardViewController;
import controller.xmlReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author ACER
 */
public class OptionViewController implements Initializable {
    @FXML
    private ComboBox<String> size;
    @FXML
    private ComboBox<String> level;

    @FXML
    private Button ok;
    
    @FXML
    private TextField rows, cols; 
    
    int row,col;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> lev = 
    FXCollections.observableArrayList(
        "Greedy",
        "Random",
        "Protective"
    );
        level.setItems(lev);        
    }
    
    @FXML
    public void startGame(ActionEvent e){
        if (checkCols() && checkRows() && checkLevel()){        // if all are correct
         try {
            //hide previous window
           ((Node)(e.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/BoardView.fxml"));
            GridPane root = (GridPane)loader.load();
            Scene scene = new Scene(root);
            BoardViewController ctrl = loader.getController();
                    
                     
            ctrl.setBoard(row,col,level.getValue(),xmlReader.getHighScore());        //Call with default settings
            Stage primaryStage = new Stage();
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
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Save game? ", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait();
                        if (ctrl.highestScore<ctrl.b.getScore())
                            ctrl.highestScore = ctrl.b.getScore();
                         
                      if (alert.getResult() == ButtonType.YES) {
                           xmlReader.writeFile(ctrl.rows, ctrl.cols,ctrl.level, ctrl.highestScore, ctrl.b.getScore(), ctrl.totalMoves, ctrl.getTiles());
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
    
   
    private boolean checkRows(){
         String r = rows.getText();
         if (r.matches("[4-9]")){
              row = r.charAt(0)-'0';
              rows.setStyle("");
              return true;
         }
         else{
            rows.setStyle("-fx-border-color:red; -fx-border-width: 5px;");
            return false;
         }     
    }
    
    
   private boolean checkCols(){
        String r = cols.getText();
         if (r.matches("[4-9]")){
             col = r.charAt(0)-'0';
              cols.setStyle("");
              return true;
         }
         else{
            cols.setStyle("-fx-border-color:red; -fx-border-width: 5px;");
            return false;
         }
    }
   
   private boolean checkLevel(){
       if (level.getValue()==null){
           level.setStyle("-fx-border-color:red; -fx-border-width: 5px;");
           return false;
       }
       else
           return true;
   }
    
}
