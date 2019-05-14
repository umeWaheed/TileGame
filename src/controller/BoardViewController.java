/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.Board;
import model.Group;
import model.Tile;

/**
 * FXML Controller class
 *
 * @author ACER
 */
public class BoardViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    GridPane gridPane;
   
    Tile[][] tiles;
    Tile[][] prevState;
    
    Board b;
    int prevMax;
    @FXML
    Label moves,info;
    
    @FXML
    Button bomb, undo;        
    
    int totalMoves;
    int highestScore;
    boolean bombSelected;
    int bombPrice, undoPrice;
    int ub,lb,w;
    int rows, cols;
    String level;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        Tile.populateColors();      //fill hashmap of colors
    
        ub=6;       //set upper bound
        lb=1;       //set lower bound
        w=5;        //width
        
        bombSelected=false;
        bombPrice=50;
        undoPrice=20;
        
        moves = new Label("i am a label");
        info = new Label();
        bomb = new Button();
        undo = new Button();
      
        Image image = new Image(getClass().getResourceAsStream("bomb.png"),40,40,true,true);
        Image image1 = new Image(getClass().getResourceAsStream("undo.png"),40,40,true,true);
        bomb.setGraphic(new ImageView(image));
        undo.setGraphic(new ImageView(image1));
        bomb.setDisable(true);
        undo.setDisable(true); 
        
        //addControls();
    }
    
    public void addControls(){
        bomb.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    useBomb();
                }
        });
        undo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    undoMove();
                }
        });
        gridPane.add(moves,0,rows+1);
        gridPane.add(info,3,rows+1,3,1);
        gridPane.add(bomb,cols+1,1);
        gridPane.add(undo,cols+1,2);
    }
    
    private void storePrevState(){
        prevState = new Tile[rows][cols];
        for (int i=0 ; i<rows ; i++)
            for (int j=0 ; j<cols ; j++){
                prevState[i][j] = new Tile(tiles[i][j].getValue(),i,j);     //create a new tile with same attributes
            }
    }
    
    public void setBoard(int r, int c, String level, int h){
        rows=r;
        cols=c;
        this.level = level;
        b = new Board(rows,cols,level);
        b.setBounds(ub,lb);
        b.randInit();    
        tiles = b.getTiles();
        b.checkGroups();
        //b.display1();
        for (int i=0 ; i<rows ; i++)
            for (int j=0 ; j<cols ; j++){
              gridPane.add(tiles[i][j],j,i);
            }
        highestScore = h;
        totalMoves=0;
        moves.setText("Moves:"+totalMoves);   
        addControls();
    addGridEvent(); 
    }
    
    public void setBoard(int r, int c, String level, int mov, int score , int hscore, int board[][]){
        rows=r;
        cols=c;
        this.level = level;
        b = new Board(rows,cols,level);
        b.setBounds(ub,lb);
        b.setTiles(board);      //setTiles based on the values read
        tiles = b.getTiles();
        b.checkGroups();
        //b.display1();
        for (int i=0 ; i<rows ; i++)
            for (int j=0 ; j<cols ; j++){
              gridPane.add(tiles[i][j],j,i);
            }
        totalMoves=mov;
        moves.setText("Moves:"+totalMoves);
        highestScore=hscore;
        b.setScore(score);
        addControls();
        addGridEvent(); 
    }
    
    private void addGridEvent() {
        gridPane.getChildren().forEach(item -> {
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    info.setText("");
                    if (event.getSource() instanceof Tile){
                    Tile t = (Tile)event.getSource();
                    if (bombSelected){
                        t.setStyle("-fx-text-fill: red;");
                        b.shiftDown(t);     //works for both group and without one
                        b.checkGroups();
                        bombSelected = false;
                    }
                    else{
                        if (t.inGroup()){
                            totalMoves++;
                            moves.setText("Moves:"+totalMoves);
                            if (totalMoves>=50 && bomb.isDisabled()){
                                info.setText("Bomb enabled!!");
                                bomb.setDisable(false);
                            }
                            if (totalMoves>=20 && undo.isDisabled()){
                                info.setText("Undo enabled!!");
                                undo.setDisable(false);
                            }
                            if (totalMoves>=19)      //store moves after 19th
                                storePrevState();   //store before changing
                            b.removeTile(t);
                        }
                        else
                            info.setText("Tile not in any group");
                    }
                    compareMaxScore();          //check if a new max score has been reached then delete all min tiles
                    if (b.checkStatus()){
                        if (b.getScore()<highestScore)
                            new Alert(Alert.AlertType.INFORMATION, "Game Over!!\n Your score is: "+b.getScore()+"\n Highest score is: "+highestScore).showAndWait();
                        else{
                            highestScore = b.getScore();        //update high score
                            new Alert(Alert.AlertType.INFORMATION, "Game Over!!\n New high score  "+b.getScore()+" acheived!!").showAndWait();
                        }
                        bomb.setDisable(true);
                        undo.setDisable(true);
                    }
                    }
                }
            });
        }
        );
    }
    
    private void compareMaxScore(){
        prevMax = b.getScore();         //previous score
        b.findMinMax();
        int current = b.getScore();     //find score again
        ub = current;                   //update upper bound
        int min = b.getMinVal();        //store the minimum value
        if (prevMax!=current){          // if a new max has been found
           // System.out.println("new max found removing min "+min);
            info.setText("New high score is "+current);
            b.removeTile(min);
            if (current>=9){            // if a new score has been reached and is greater than 9 then increase width
                w++;        //increment width 
                if (ub-w>1){
                    lb = ub-w;
            }
            else
                    lb=1;
            }
        }
        b.setBounds(ub,lb);
    }
    
    @FXML
    public void undoMove(){
        info.setText("Undo last move using "+undoPrice+ "moves");
        gridPane.getChildren().clear();
        b.setTiles(prevState);      //reset gameboard
        tiles=b.getTiles();         //reset tiles in this class
        
        for (int i=0 ; i<rows ; i++){
            for (int j=0 ; j<cols ; j++){
              gridPane.add(tiles[i][j],j,i);
            }
        }
        b.checkGroups();
        addGridEvent();
        
        totalMoves-=undoPrice;
        undoPrice*=2;       //next time 20*2 will be required
        moves.setText("Moves: "+totalMoves);
        if (totalMoves<20)
            undo.setDisable(true);
        
        gridPane.add(moves,0,rows+1);
        gridPane.add(info,3,rows+1);
        gridPane.add(bomb,cols+1,1);
        gridPane.add(undo,cols+1,2);
    }
    
    @FXML
    public void useBomb(){
        info.setText("Click on a tile to destroy!! using "+bombPrice+" moves");
        bombSelected=true;
        totalMoves -= bombPrice;
        bombPrice*=2;       //doubles next time
        moves.setText("moves: "+totalMoves);
         if (totalMoves<50)      // if moves are less than 50 disable button
            bomb.setDisable(true);
         if (totalMoves<20 && !undo.isDisable())        //disable undo if it is enabled
             undo.setDisable(true);
    }
    
    public int[][] getTiles(){
        int [][] t = new int[rows][cols];
        for (int i=0 ; i<rows ; i++)
            for (int j=0 ; j<cols ; j++)
                t[i][j] = tiles[i][j].getValue();       //store value of tile
        return t;
    }
}
