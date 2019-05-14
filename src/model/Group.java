/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.paint.Color;

/**
 *
 * @author ACER
 */
public class Group {
    ArrayList<Tile> tiles;
    String color;
    
    public Group(){
        tiles = new ArrayList<Tile>();
    }
   
    public void addTile(Tile t){
        //t.setStyle("-fx-border-color:black; -fx-border-width: 5px; -fx-text-fill: #0000ff; -fx-font-size: 2em;  -fx-background-color: "+color);
        tiles.add(t);
    }
     public void getAdj(){
        if (tiles.size()>0){
            //System.out.print(" "+getValue()+" r:"+getRow()+" c:"+getCol());
            for (int i=0 ; i<tiles.size() ; i++)
                System.out.print(" "+tiles.get(i).getValue()+" r:"+tiles.get(i).getRow()+" c:"+tiles.get(i).getCol());
        }
    }
     
     public void remove(Tile t){
        t.setVisited(false);
        t.setGroup(false);
        t.setAdj(null);
        tiles.remove(t);
        //System.out.println("removed "+t.getValue()+"r:"+t.getRow()+" c:"+t.getCol());
     }
     
     public void removeAll(){
         for (int i=0 ; i<tiles.size() ; i++){
             tiles.get(i).setGroup(false);
             tiles.get(i).setVisited(false);
             tiles.get(i).setAdj(null);
             tiles.remove(i);           //remove from this group
             
         }
     }
     
     public int getSize(){
         return tiles.size();
     }
     
     public ArrayList getTiles(){
         return tiles;
     }
}
