// Name: Joshua Schnall
// USC loginid: jschnall@usc.edu
// CS 455 PA3
// Spring 2016

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.awt.Graphics2D;
import java.awt.*;
import javax.swing.JComponent;
import java.util.ListIterator;
import java.util.LinkedList;

/**
   MazeComponent class
   
   A component that displays the maze and path through it if one has been found.
*/
public class MazeComponent extends JComponent
{
   private Maze maze;  // maze object  
   private static final int START_X = 10; // where to start drawing maze in frame
   private static final int START_Y = 10;
   private static final int BOX_WIDTH = 20;  // width and height of one maze unit
   private static final int BOX_HEIGHT = 20;
   private static final int INSET = 2;  // how much smaller on each side to make entry/exit inner box
   
   
   /**
      Constructs the component.
      @param maze   the maze to display
   */
   public MazeComponent(Maze maze) 
   {   
      this.maze = maze;
   }

   
   /**
     Draws the current state of maze including the path through it if one has
     been found.
     @param g the graphics context
     
     Draws a blue maze line connecting points along the path and multiple black and white squares
     marking the maze walls.  The entry square is yellow and exit green.
   */
   public void paintComponent(Graphics g)
   {  
	   Graphics2D g2 = (Graphics2D) g;	   
	  
	   //for each entry in maze object draw rectangle black or white in grid format	   
	   for(int i = 0; i< maze.numRows(); i++){
		   for(int j = 0; j< maze.numCols(); j++){ 
			   if(maze.hasWallAt(new MazeCoord(i,j))){  // if wall draw a black rectangle
				   Rectangle square = new Rectangle( START_Y + j*BOX_HEIGHT, START_X + i*BOX_WIDTH,  BOX_WIDTH, BOX_HEIGHT);
				   g2.setColor(Color.BLACK);
				   g2.fill(square);
			   }
			   else{                                    //else draw a white rectangle
				   Rectangle square = new Rectangle(START_Y + j*BOX_HEIGHT, START_X + i*BOX_WIDTH,  BOX_WIDTH, BOX_HEIGHT);
				   g2.setColor(Color.WHITE);
				   g2.fill(square);  
			   }
		   }
	   }   
	   // draw start and finish in yellow and green
	   Rectangle square = new Rectangle( START_Y + INSET/2+ maze.getEntryLoc().getCol()*BOX_HEIGHT, 
			   START_X+ INSET/2 + maze.getEntryLoc().getRow()*BOX_WIDTH, BOX_WIDTH-INSET, BOX_HEIGHT-INSET);  //inset by the desired amount to center and size reduced
	   g2.setColor(Color.YELLOW);    
	   g2.fill(square);
	   
	   square = new Rectangle( START_Y + INSET/2 + maze.getExitLoc().getCol()*BOX_HEIGHT,     //inset by the desired amount to center and size reduced
			   START_X +  INSET/2 + maze.getExitLoc().getRow()*BOX_WIDTH,  BOX_WIDTH-INSET, BOX_HEIGHT-INSET);
	   g2.setColor(Color.GREEN);
	   g2.fill(square);
	   
	   
	   //for each entry in linked list draw line segment
		   LinkedList<MazeCoord> path = maze.getPath();
		   //create iterator   
		   ListIterator<MazeCoord> iter = path.listIterator();
		   if(iter.hasNext()){
			   MazeCoord temp = iter.next();
	  
			   for(int i = 0; i< path.size()-1; i++){  //iterator goes through path by two coordinates to connect each set of two coordinates
				   MazeCoord temp1 = iter.next();
				   Point2D.Double from = new Point2D.Double(temp.getCol()*BOX_WIDTH +START_Y +BOX_HEIGHT/2, //line segments are also inset by one half box height/width
						   temp.getRow()*BOX_HEIGHT+START_X + BOX_HEIGHT/2);								//done to center the line segments
				   Point2D.Double to = new Point2D.Double(temp1.getCol()*BOX_WIDTH +START_Y +BOX_HEIGHT/2,
						   temp1.getRow()*BOX_HEIGHT+START_X + BOX_HEIGHT/2);
				   Line2D.Double segment = new Line2D.Double(from, to);
				   g2.setColor(Color.BLUE);
				   g2.draw(segment);
				   temp = temp1;
			   } 
		   }
   }
   
}



