/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
/**
 *
 * @author ACER
 */
public class Tile extends Button{
    int value;  //denotes x
    boolean isVisited;
    boolean inGroup;
    int row;
    int col;
    Group adj;  //list of neighbours
    static HashMap<Integer,String> groupColors = new HashMap<Integer,String>();
    String color;    //color of the tile
    
    public Tile(int v, int r, int c){
        setText(""+v);
        setMinSize(60,60);
        this.setMaxSize(80, 60);
        value=v;
        setColor();        //set color according to value
        isVisited=false;
        inGroup=false;
        row=r;
        col=c;     
        //adj = new ArrayList<Tile>();
    }
    
    //add colors for the numbers
    public static void populateColors(){
        Color c[] = {Color.rgb(238,192,37),Color.rgb(175,208,77),Color.rgb(211,127,74),Color.rgb(209,74,74),Color.rgb(211,73,149)
                ,Color.rgb(155,65,206)
                ,Color.rgb(103,57,172),Color.rgb(51,65,189),Color.rgb(1,125,188),Color.rgb(7,162,191),Color.rgb(82,227,221)};
        String name; 
        for (int i=0 ; i<c.length ; i++){
            name = String.format( "#%02X%02X%02X",(int)( c[i].getRed() * 255 ),(int)( c[i].getGreen() * 255 ),(int)( c[i].getBlue() * 255 ) );
            groupColors.put(i+1,name);
        }
    }
    
    //set color according to the number x
    public void setColor(){
        Color c;
        if (groupColors.containsKey(value)){       //if key exists already
            color = groupColors.get(value);         //get color from the value
        }
        else{
            c = Color.color(Math.random(), Math.random(), Math.random());   //pick a random color for group
            color = String.format( "#%02X%02X%02X",(int)( c.getRed() * 255 ),(int)( c.getGreen() * 255 ),(int)( c.getBlue() * 255 ) );
            groupColors.put(value,color);           //put a new pair
        }
          setStyle("-fx-border-color:black; -fx-border-width: 3px; -fx-border-radius: 5px; -fx-text-fill: black; -fx-font-size: 2em;  -fx-background-radius: 3px; -fx-background-color: "+color);
    }
    
    public void setAdj(Group g){
        adj= g;
    }
    
    public Group getAdj(){
        return adj;
    }
    
    
    
//    public ArrayList getAdj(){
//        if (adj.size()>0){
//            System.out.print(" "+getValue()+" r:"+getRow()+" c:"+getCol());
//            for (int i=0 ; i<adj.size() ; i++)
//                System.out.print(" "+adj.get(i).getValue()+" r:"+adj.get(i).getRow()+" c:"+adj.get(i).getCol());
//        }
//            return adj;
//    }
    
//     public void addTile(Tile t){
//        adj.addTile(t);
//    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public boolean inGroup() {
        return inGroup;
    }

    public void setGroup(boolean inGroup) {
        this.inGroup = inGroup;
    }
    
    public int getValue(){
        return value;
    }
    
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public void addValue(){
        value++;
        setText(""+value);
        setColor();     //change color
    }
    
    public void changeValue(int u, int l){
        Random rnd = new Random();
        value = rnd.nextInt(u-l)+l;       //upper bound-1 + lower bound
        setText(""+value);
        setColor();     //update color
    }
    
    public void setValue(int v){
        value = v;                  //set the value to shifted tile
        setText(""+value);
        setColor();      //update color
    }
    
    public void fall_down(){
        row++;      //fall down vertically
    }
    
    public void removeTile(){
        isVisited=false;
        inGroup=false;
    }
    
    public void resetTile(){
        setVisited(false);
        setGroup(false);
        setAdj(null);
    }
}
