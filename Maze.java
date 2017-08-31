// Name: Joshua Schnall
// USC loginid: jschnall
// CS 455 PA3
// Spring 2016

import java.util.LinkedList;
import java.util.ListIterator;


/**
   Maze class

   Stores information about a maze and can find a path through the maze
   (if there is one).
   
   Assumptions about structure of the maze, as given in mazeData, startLoc, and endLoc
   (parameters to constructor), and the path:
     -- no outer walls given in mazeData -- search assumes there is a virtual 
        border around the maze (i.e., the maze path can't go outside of the maze
        boundaries)
     -- start location for a path is maze coordinate startLoc
     -- exit location is maze coordinate exitLoc
     -- mazeData input is a 2D array of booleans, where true means there is a wall
        at that location, and false means there isn't (see public FREE / WALL 
        constants below) 
     -- in mazeData the first index indicates the row. e.g., mazeData[row][col]
     -- only travel in 4 compass directions (no diagonal paths)
     -- can't travel through walls
 */

public class Maze {
   
   public static final boolean FREE = false;   
   public static final boolean WALL = true;
   
   /*
    * Rep Invariants
    * -mazeData input which must be a boolean array 
    * -mazeRep - rep of mazeData made into integers of 1(wall) and 0(no wall) , and padded with values of 2
    * -mazeVisited - array of 1 and 0 values, 0 for not visited, one for visited, same size as passed array above
    * -start a MazeCoord which is referenced to the padded mazeRep matrix
    * -end  a MazeCoord which is referenced to the padded mazeRep matrix
    * -path the maze correct path to get to the exit, referenced to the padded matrix mazeRep
    * -path_unpadded - the true path associated with MazeData
    */
   
   private static int[][] mazeRep;  //rep of mazeData made into integers of 1(wall) and 0(no wall) , and padded with values of 2
   private static int[][] mazeVisited; // keeps an array of places already visited, same size as mazeRep, padded.
   private static MazeCoord start; //start loc padded
   private static MazeCoord end;  // end loc padded
   private static LinkedList<MazeCoord> path;  //stores path using padded coordinates
   private static LinkedList<MazeCoord> path_unpadded;//unpadded path, converted from padded path above
   private int called = 0; //check if search has already been called, 1 for called once, will equal 2 when already been called once;
   private boolean called_value; //check value of prior search

   /**
      Constructs a maze.
      @param mazeData the maze to search.  See general Maze comments for what
      goes in this array.
      @param startLoc the location in maze to start the search (not necessarily on an edge)
      @param endLoc the "exit" location of the maze (not necessarily on an edge)
      PRE: 0 <= startLoc.getRow() < mazeData.length and 0 <= startLoc.getCol() < mazeData[0].length
         and 0 <= endLoc.getRow() < mazeData.length and 0 <= endLoc.getCol() < mazeData[0].length

    */
   public Maze(boolean[][] mazeData, MazeCoord startLoc, MazeCoord endLoc){
       mazeRep = new int[mazeData.length+2][mazeData[0].length+2];	  //create mazeRep padded with values of 2 which increases each dimension by two spaces
	   for(int i = 0; i< mazeData.length+2 ; i++){
	   		for(int j = 0; j < mazeData[0].length+2; j++){
	   			if ((i > 0 && i< mazeData.length+1) && (j > 0 && j < mazeData[0].length +1)){
	   			    if(mazeData[i-1][j-1] == WALL){
	   				     mazeRep[i][j] = 1;  //if true and wall is present set equal to one
	   			    }else{
	   				     mazeRep[i][j] = 0; // if false and no wall set equal to zero
	   			    }
	   			}else{
	   				mazeRep[i][j] = 2;  // if out of bounds set equal to 2
	   			}
	   		}
        }
	   
		mazeVisited = new int[mazeRep.length][mazeRep[0].length];	  //construct visited matrix to prevent cycles
   		start = new MazeCoord(startLoc.getRow()+1, startLoc.getCol()+1);   //padded start loc
   		end = new MazeCoord(endLoc.getRow()+1, endLoc.getCol()+1);		//padded end loc
   		path = new LinkedList<MazeCoord>();    //initialize the path linkedlist
   		path_unpadded = new LinkedList<MazeCoord>();   //initialize unpadded path
 
   }


   /**
   Returns the number of rows in the maze
   @return number of rows
   */
   public int numRows() {
      return mazeRep.length -2;   //
   }

   
   /**
   Returns the number of columns in the maze
   @return number of columns
   */   
   public int numCols() {
      return mazeRep[0].length -2;   //
   } 
 
   
   /**
      Returns true iff there is a wall at this location
      @param loc the location in maze coordinates
      @return whether there is a wall here
      PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
   */
   public boolean hasWallAt(MazeCoord loc) {
      if(mazeRep[loc.getRow()+1][loc.getCol()+1] == 1){
    	  return true;
      }else{
    	  return false;
      }
   }
   

   /**
      Returns the entry location of this maze.
    */
   public MazeCoord getEntryLoc() {
      return new MazeCoord(start.getRow()-1, start.getCol()-1);   // return unpadded
   }
   
   
   /**
   Returns the exit location of this maze.
   */
   public MazeCoord getExitLoc() {
      return new MazeCoord(end.getRow()-1, end.getCol()-1);   // return unpadded
   }

   
   /**
      Returns the path through the maze. First element is starting location, and
      last element is exit location.  If there was not path, or if this is called
      before search, returns empty list.

      @return the maze path
    */
   public LinkedList<MazeCoord> getPath() {
	  if (called > 1){         // check if search already called once 
		   return path_unpadded;
	  }
	   
	  //add iterator to correctly traverse and make unpadded linked list copy
	  ListIterator<MazeCoord> iter = path.listIterator();	 
	  
	  for(int i = 0; i<path.size(); i++){			//create unpadded list copy
		  MazeCoord temp = iter.next();
		  path_unpadded.add(new MazeCoord(temp.getRow()-1, temp.getCol()-1));
	  }
      return path_unpadded;   // return the linked list path unpadded with sentinel values of 2
   }


   /**
      Find a path through the maze if there is one.  Client can access the
      path found via getPath method.
      @return whether path was found.
      
      
    */
   public boolean search()  {  
	   called += 1;        //check if search has already been called
	   if(called > 1){     //if so return previous result
		   if(called_value){
			   return true;
		   }else{
			   return false;
		   }
	   }
	  
	   MazeCoord searchLoc  = new MazeCoord(start.getRow(), start.getCol());
	   if(searchHelper(searchLoc)){  //call search helper
		   path.addFirst(start);   //added first start point outside of recursive loop
		   called_value = true;
		   return true;
	   }else{
		   called_value = false;
		   return false;
	   }
   }

   /**search helper function for recursive search in method search()
   @param searchLoc padded starting location
   @return true if path was found
   
   **/
   
   private boolean searchHelper(MazeCoord searchLoc) {
	   //3 base cases   
	   if (mazeRep[searchLoc.getRow()][searchLoc.getCol()] == 1){  //1st is ending at a wall
		   if(path.size() > 0){
			   path.remove(path.getFirst());  // remove as not part of final path
		   }
		   return false;
	   }
	   if (mazeVisited[searchLoc.getRow()][searchLoc.getCol()] == 1){  //second, point was already visited, creating a cycle
		   if(path.size() > 0){
			   path.remove(path.getFirst());  //remove as not part of final path
		   }
		   return false;  													   
	   }	   
	   if (searchLoc.getRow() == end.getRow() && searchLoc.getCol() == end.getCol()){        							 
		   return true;		//if made it to the finish, return true    
	   }
	     
	   mazeVisited[searchLoc.getRow()][searchLoc.getCol()] = 1;    //mark as visited
	   
	   //recursive cases
	   for(int i = -1; i<2;  i++){
		   for(int j = -1; j<2 ; j++){
			   if(i == 0 || j == 0){
				   if(mazeRep[searchLoc.getRow()+i][searchLoc.getCol()]   !=  2) {     // try for each position adjacent of current not outside of matrix
					   if(searchHelper(new MazeCoord(searchLoc.getRow()+i, searchLoc.getCol()+j))){     //try to find a path to the end 
						   MazeCoord next = new MazeCoord(searchLoc.getRow()+i, searchLoc.getCol()+j);   
						   path.addFirst(next);    //if that search was successful make part of the path, and return success
						   return true;      //each return true adds the new location to the head of the linked list	
					   }				 //then goes up level closer to start and adds that to head of linked list with addFirst method
				   }
			   }
		   }
	   }
	   return false;    //if we got through the loop without finding a path, return failure.
   }

}
