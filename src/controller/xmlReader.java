/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Tile;
import org.w3c.dom.*;

public class xmlReader {
    static int highScore=0;
    static int score;
    static int moves;
    static int rows;
    static int cols;
    static String level;
    
   public static int getHighScore(){
       try {
         File inputFile = new File("src/model/gameState.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList nList = doc.getElementsByTagName("highscore");
         System.out.println("----------------------------");
         Node n = nList.item(0);        //score is the only item
         Element ele = (Element) n;
         System.out.println("Score : " + ele.getTextContent());
         highScore = Integer.parseInt(ele.getTextContent());        //store high score
       }
       catch(Exception e){
           e.printStackTrace();
       }
       return highScore;
   } 
   
   public static int[][] readFile(){
       int board[][]=new int[5][5];
        try {
         File inputFile = new File("src/model/gameState.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList nList = doc.getElementsByTagName("highscore");
         System.out.println("----------------------------");
         Node n = nList.item(0);        //score is the only item
         Element ele = (Element) n;
         System.out.println("Score : " + ele.getTextContent());
         highScore = Integer.parseInt(ele.getTextContent());        //store high score
        
         NodeList list = doc.getElementsByTagName("score");         // user score
         Node sc = list.item(0);
         Element ele1 = (Element)sc;
         score = Integer.parseInt(ele1.getTextContent());  
         
         list = doc.getElementsByTagName("moves");         //moves
         sc = list.item(0);
         ele1 = (Element)sc;
         moves = Integer.parseInt(ele1.getTextContent());  
         
         list = doc.getElementsByTagName("rows");         // board size
         sc = list.item(0);
         ele1 = (Element)sc;
         rows= Integer.parseInt(ele1.getTextContent()); 
         
         list = doc.getElementsByTagName("cols");         // board size
         sc = list.item(0);
         ele1 = (Element)sc;
         cols= Integer.parseInt(ele1.getTextContent()); 
         
         list = doc.getElementsByTagName("level");         // user score
         sc = list.item(0);
         ele1 = (Element)sc;
         level= ele1.getTextContent();  
         
         board = new int[rows][cols];
         
         NodeList nList1 = doc.getElementsByTagName("row");
         System.out.println("----------------------------");
         
         for (int i = 0; i < nList1.getLength(); i++) {
            Node nNode = nList1.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               NodeList nList2 = eElement.getElementsByTagName("col");
               for (int j=0 ; j< nList2.getLength() ; j++){
                    Node nNode1 = nList2.item(j);
                    Element elem  = (Element) nNode1;
                    System.out.print(" " + elem.getTextContent());
                    board[i][j]=Integer.parseInt(elem.getTextContent());        //store score
               }
               System.out.println();
           }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
        return board;
   }
    
   public static void writeFile(int r, int c, String l, int h, int score, int moves, int[][] gameState){
       try{
         File inputFile = new File("src/model/gameState.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.newDocument();
         
          Element root = doc.createElement("root");
          doc.appendChild(root);
            
         Element high = doc.createElement("highscore");
         high.appendChild(doc.createTextNode(h+""));
         root.appendChild(high);
          //store high score
        
         Element sc = doc.createElement("score");
         sc.appendChild(doc.createTextNode(score+""));
         root.appendChild(sc);
         
         Element mv = doc.createElement("moves");
         mv.appendChild(doc.createTextNode(moves+""));
         root.appendChild(mv);
         
         Element rw = doc.createElement("rows");
         rw.appendChild(doc.createTextNode(r+""));
         root.appendChild(rw);
         
         Element cl = doc.createElement("cols");
         cl.appendChild(doc.createTextNode(c+""));
         root.appendChild(cl);
         
          Element lev = doc.createElement("level");
         lev.appendChild(doc.createTextNode(l));
         root.appendChild(lev);
         
        
         
         for (int i = 0; i < r; i++) {
                     
               Element r1 = doc.createElement("row");
              
               for (int j=0 ; j< c ; j++){
                    Element col = doc.createElement("col");
                    
                    col.appendChild(doc.createTextNode(""+gameState[i][j]));
                    r1.appendChild(col);
               }
               root.appendChild(r1);        //add row to root
           }
         
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/model/gameState.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public static void writeHighScore(int h){
       try{
         File inputFile = new File("src/model/gameState.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList nList = doc.getElementsByTagName("highscore");
         System.out.println("----------------------------");
         Node n = nList.item(0);        //score is the only item
         Element ele = (Element) n;
         ele.setTextContent(""+h);      //set high score
         
          doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/model/gameState.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
            
       }catch (Exception e) {
         e.printStackTrace();
      }
   }
}
