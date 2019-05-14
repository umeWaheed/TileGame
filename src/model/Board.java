/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
/**
 *
 * @author ACER
 */
public class Board {
    public Tile[][] gameBoard;
    ArrayList<Group> groups;
    String level;
    int rows;int cols;
    int score;
    int minVal;
    int upper, lower;
   
    public Board(int row, int col, String lev){
        rows=row;
        cols=col;
        groups = new ArrayList<Group>();
        gameBoard = new Tile[row][col];
        score=6;        //current max is 6
        minVal=1;       //current min is 1
        level = lev;
    }
    
    public void randInit(){
         Random rnd = new Random();
        int number;
         for (int i=0 ; i<rows ; i++)
            for (int j=0 ; j<cols ; j++){
                number=rnd.nextInt(6)+1;
                gameBoard[i][j] = new Tile(number,i,j);
            }
         colorMax();
    }
    
    //for 5x5 board testing
    public void initialize(){
        int arr[][] = {{4,1,1,1,3},{1,3,2,2,3},{1,1,2,1,1},{4,2,2,3,1},{2,1,4,4,6}};
        for (int i=0 ; i<rows ; i++)
            for (int j=0 ; j<cols ; j++){
                gameBoard[i][j] = new Tile(arr[i][j],i,j);
            }
        colorMax();
    }
    
    public void display(){
        for (int i=0 ; i<rows ; i++){
            for (int j=0 ; j<cols ; j++){
                System.out.print(gameBoard[i][j].getValue()+" ");
            }
        System.out.println();
        }
    }
    
    public Tile[][] getTiles(){
        return gameBoard;
    }
    
    public void setTiles(Tile[][] tiles){
        gameBoard=tiles;
    }
    
    public void setTiles(int[][] tiles){
        for (int i=0 ; i<rows ; i++)
            for (int j=0 ; j<cols ; j++){
                gameBoard[i][j] = new Tile(tiles[i][j],i,j);
            }
    }
    
    public void setBounds(int u, int l){
        upper = u;
        lower = l;
    }
    
    public void display1(){     //displays groups
        for (int i=0 ; i<groups.size() ; i++){
            groups.get(i).getAdj();
        System.out.println();
        }
    }
    
    //check all the neighbors of tile and add to list BFS
    public void checkNeighbours(Tile t, Queue<Tile> list){  //add directly connected unvisited neighbors of t to list
        if (t.getRow()<rows-1 && gameBoard[t.getRow()+1][t.getCol()].getValue()==t.getValue() && !gameBoard[t.getRow()+1][t.getCol()].isVisited()){       //check bottom neighbour
            list.add(gameBoard[t.getRow()+1][t.getCol()]);
            gameBoard[t.getRow()+1][t.getCol()].setVisited(true);
        }
        if (t.getCol()<cols-1 && gameBoard[t.getRow()][t.getCol()+1].getValue()==t.getValue() && !gameBoard[t.getRow()][t.getCol()+1].isVisited()){       //check right neighbour
            list.add(gameBoard[t.getRow()][t.getCol()+1]);
             gameBoard[t.getRow()][t.getCol()+1].setVisited(true);
        }
        if (t.getRow()>0 && gameBoard[t.getRow()-1][t.getCol()].getValue()==t.getValue() && !gameBoard[t.getRow()-1][t.getCol()].isVisited()){       //check up neighbour
            list.add(gameBoard[t.getRow()-1][t.getCol()]);
             gameBoard[t.getRow()-1][t.getCol()].setVisited(true);
        }
        if (t.getCol()>0 && gameBoard[t.getRow()][t.getCol()-1].getValue()==t.getValue() && !gameBoard[t.getRow()][t.getCol()-1].isVisited()){       //check left neighbour
            list.add(gameBoard[t.getRow()][t.getCol()-1]);
             gameBoard[t.getRow()][t.getCol()-1].setVisited(true);
        }
    }
    
    public void reset(){
        groups.clear();
        for (int i=0 ; i<rows ; i++){
            for (int j=0 ; j<cols ; j++){
                gameBoard[i][j].setGroup(false);
                gameBoard[i][j].setVisited(false);
                gameBoard[i][j].setAdj(null);
               }
        }
    }
    
    public void checkGroups(){
        reset();        //reset after each move for new groups
        Queue<Tile> traversal = new LinkedList<Tile>();
        for (int i=0 ; i<rows ; i++){
            for (int j=0 ; j<cols ; j++){
                //pick a tile add its adjacnet tiles to list
                if (!gameBoard[i][j].isVisited() && !gameBoard[i][j].inGroup()){    //neither visited nor in any group
                    //find directly connected neighbors
                    gameBoard[i][j].setVisited(true);
                    checkNeighbours(gameBoard[i][j],traversal);
                    
                    //if traversal is empty then no neighbours exist
                    //else check neighbors for others
                    if (!traversal.isEmpty()){
                        gameBoard[i][j].setGroup(true);                
                        Group g = new Group();      //assign a random color to group
                        groups.add(g);
                        g.addTile(gameBoard[i][j]);
                        gameBoard[i][j].setAdj(g);
                    
                        Tile temp;
                        while(!traversal.isEmpty()){
                            temp = traversal.remove();
                            temp.setGroup(true);
                            temp.setAdj(g);
                            g.addTile(temp);
                            checkNeighbours(temp,traversal);
                        }
                    }
                }
            }
        }
        colorMax();
    }
    
    public void removeTile(Tile t){
        t.addValue();
        Group g = t.getAdj();
        if (g!=null){       // if tile is in group
            g.remove(t);
            shiftDown(g);       //pass the tiles that will be removed
        }
        checkGroups();
    }
    
    public void removeTile(int val){        //remove tile by value
        Tile t;
        for (int i=0 ; i<rows ; i++)
            for(int j=0 ; j<cols ; j++){
                t = gameBoard[i][j]; 
                if (t.getValue()==val){
                    Group g = t.getAdj();
                    if (g!=null){       // if tile is in group
                        shiftDown(g);       //pass the tiles that will be removed
                    }
                    else{       //if no group
                        shiftDown(t);
                    }
                }
            }
        checkGroups();
    }
    
    public void shiftDown(Tile t){        //shift down tile with no group 
        int col = t.getCol();
        int row = t.getRow();
        for (int j=row ; j>0 ; j--){
            gameBoard[j][col].setValue(gameBoard[j-1][col].getValue());
        }
    }
    
    public void shiftDown(Group g){
        Random rnd = new Random();
        ArrayList<Tile> t;// = g.getTiles();
        do{
            t=g.getTiles();             // as each time a tile is removed so get the remaining tiles
            Tile temp = t.get(0);       //get a tile and find its column
            int col = temp.getCol();
            int row = temp.getRow();
            for (int j=row ; j>0 ; j--){
                gameBoard[j][col].setValue(gameBoard[j-1][col].getValue());
            }
            g.remove(temp);                     //remove temp from group
            
            //allocate value to 0th cell
            if (level.equals("Greedy"))
                greedy(gameBoard[0][col]);      //use greedy approach to change value
            else if (level.equals("Random"))
                random(gameBoard[0][col]);      //use random approach to change value
            else
                protective(gameBoard[0][col]);      //use protective approach to change value
           
        }while(g.getSize()>0);
    }
    
    public boolean checkStatus(){
        if (groups.isEmpty()){      //if there are no groups game ends           
           return true;
        }
        return false;
    }
    
    public void findMinMax(){
         // find the maximum score
        score=gameBoard[0][0].getValue();
        //find the minimum score
        minVal=gameBoard[0][0].getValue();
        
        for (int i=0 ; i<rows ; i++){
            for(int j=0 ; j<cols ; j++){
                if (gameBoard[i][j].getValue()>score){
                    score = gameBoard[i][j].getValue();
                }
                if (gameBoard[i][j].getValue()<minVal)
                    minVal = gameBoard[i][j].getValue();
            }
        }
        colorMax();
    }
    
    //find the max tile and color it white
    public void colorMax(){
        for (int i=0 ; i<rows ; i++){
            for(int j=0 ; j<cols ; j++){
                if (gameBoard[i][j].getValue()==score)
                    gameBoard[i][j].setStyle("-fx-border-color:black; -fx-border-width: 3px; -fx-border-radius: 5px; -fx-text-fill: black; -fx-font-size: 2em;  -fx-background-radius: 3px; -fx-background-color: white");
            
            if (gameBoard[i][j].getValue()==score-1)    //previous max
                gameBoard[i][j].setColor();
       }
        }
    }
    
    public int getScore(){
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getMinVal(){
        return minVal;
    }
    
    private void random(Tile t){
         Random rnd = new Random();
        int v = rnd.nextInt(upper-lower)+lower;       //upper bound-1 + lower bound
        t.setValue(v);
    }
    
    private void greedy(Tile t){
        int max=0;
        int r = t.getRow();             //row of this tile
        int c = t.getCol();              //col of the tile
        int left = t.getCol()-1;        //col of left neighbot
        int right = t.getCol()+1;       //col of right neighbor
        int down = t.getRow()+1;        //down will have same col but row 1
        
        //if all 3 neighbours exist
        if (left>=0 && right<cols && down<rows){
            if (gameBoard[r][left].getValue()> gameBoard[r][right].getValue() && gameBoard[r][left].getValue()>gameBoard[down][c].getValue())
                max = gameBoard[r][left].getValue();
            else if (gameBoard[r][right].getValue()> gameBoard[r][left].getValue() && gameBoard[r][right].getValue()>gameBoard[down][c].getValue())
                max = gameBoard[r][right].getValue();
            else
                max = gameBoard[down][c].getValue();
        }
        // 2 neighbors right and down
        else if(left<0 && right<cols && down<rows) {
            if (gameBoard[r][right].getValue()>gameBoard[down][c].getValue())
                max = gameBoard[r][right].getValue();
            else
                max = gameBoard[down][c].getValue();
        }
        //2 neighbors left and down
        else{
            if (gameBoard[r][left].getValue()>gameBoard[down][c].getValue())
                max = gameBoard[r][left].getValue();
            else
                max = gameBoard[down][c].getValue();
        }
        if (max%2==1){
            random(t);      //to randomize value to some extent
        }
        else
            t.setValue(max);
    }
    
    private void protective(Tile t){
        Random rnd = new Random(); 
         
        int r = t.getRow();             //row of this tile
        int c = t.getCol();              //col of the tile
        int left = t.getCol()-1;        //col of left neighbot
        int right = t.getCol()+1;       //col of right neighbor
        int down = t.getRow()+1;        //down will have same col but row 1
        int leftVal=0,rightVal=0,downVal=0;
        
        //if all 3 neighbours exist
        if (left>=0 && right<cols && down<rows){
          leftVal = gameBoard[r][left].getValue();
          rightVal = gameBoard[r][right].getValue(); 
          downVal = gameBoard[down][c].getValue();
        }
        // 2 neighbors right and down
        else if(left<0 && right<cols && down<rows) {
            rightVal = gameBoard[r][right].getValue(); 
            downVal = gameBoard[down][c].getValue();
        }
        //2 neighbors left and down
        else{
            leftVal = gameBoard[r][left].getValue(); 
            downVal = gameBoard[down][c].getValue();
        }
        //1 to max
        //keep on generating random number else we get a unique one
        int random;
        do{
            random = lower + rnd.nextInt(upper - lower);
        }while(random==leftVal || random==rightVal || random==downVal);
        t.setValue(random);
    }
}
