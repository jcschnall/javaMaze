// Name: Joshua Schnall	
// USC loginid: jschnall@usc.edu
// CS 455 PA3
// Spring 2016


import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import java.util.Scanner;
import java.io.File;


/**
 * MazeViewer class
 * 
 * Program to read in and display a maze and a path through the maze. At user
 * command displays a path through the maze if there is one.
 * 
 * How to call it from the command line:
 * 
 *      java MazeViewer mazeFile
 * 
 * where mazeFile is a text file of the maze. The format is the number of rows
 * and number of columns, followed by one line per row, followed by the start location, 
 * and ending with the exit location. Each maze location is
 * either a wall (1) or free (0). Here is an example of contents of a file for
 * a 3x4 maze, with start location as the top left, and exit location as the bottom right
 * (we count locations from 0, similar to Java arrays):
 * 
 * 3 4 
 * 0111
 * 0000
 * 1110
 * 0 0
 * 2 3
 * 
 */

public class MazeViewer {
   
   private static final int WALL_CHAR = 1;  //changed to integer constant
   private static final int FREE_CHAR = 0;  //changed to integer constant

   public static void main(String[] args)  {

      String fileName = "";

      try {

         if (args.length < 1) {
            System.out.println("ERROR: missing file name command line argument");
         }	 
         else {
             fileName = args[0];
             
             JFrame frame = readMazeFile(fileName);

             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

             frame.setVisible(true);
          }

      }
      catch (FileNotFoundException exc) {
         System.out.println("File not found: " + fileName);  //print error if file not found
      }
      catch (IOException exc) {
    	 System.out.println("ERROR: file is not proper format for MazeViewer");   // print error if improper file format 
         exc.printStackTrace();
      }
   }

   /**
    readMazeFile reads in maze from the file whose name is given and 
    returns a MazeFrame created from it.
   
   @param fileName
             the name of a file to read from (file format shown in class comments, above)
   @returns a MazeFrame containing the data from the file.
        
   @throws FileNotFoundException
              if there's no such file (subclass of IOException)
   @throws IOException
              (hook given in case you want to do more error-checking.
               that would also involve changing main to catch other exceptions)
   */
   private static MazeFrame readMazeFile(String fileName) throws IOException {
	   File file = new File(fileName);  //get file
	   Scanner in = new Scanner(file);
	   			
	   //check for correct file format.... first number is integer
       if(!in.hasNextInt()){     
    	   throw new IOException();
       }
   	   
	   int x = in.nextInt();          //get x,y dimensions from file   
	   //check for correct file format.... second number is integer
       if(!in.hasNextInt()){     
    	   throw new IOException();
       }
       
	   int y = in.nextInt();
	   boolean[][] frameSize = new boolean[x][y];   //scans in maze size and creates boolean array of that size
	   
	   in.useDelimiter("");							//reset delimiter to single char
	   for(int i = 0; i< frameSize.length; i++){	
		   in.nextLine();
		   for(int j = 0; j< frameSize[0].length; j++)
			   //check for correct file format....all numbers being read in are integers and appropriate number of integers
		       if(!in.hasNextInt()){     
		    	   throw new IOException();
		       }
		       else if(in.nextInt() == WALL_CHAR){      
			       frameSize[i][j] = true;        //sets any 1 spots which are walls to true in boolean array
		
			   }
			   else{
			       frameSize[i][j] = false;  // sets zeros spots to false in boolean array
			    
			   }
	   }
	   in.useDelimiter("\\p{javaWhitespace}+");  // reset delimiter to default
	   
	   //check for correct file format.... second to last set of numbers are integers
       if(!in.hasNextInt()){     
    	   throw new IOException();
       }
	   
	   MazeCoord start = new MazeCoord(in.nextInt(), in.nextInt());
	   in.nextLine();
	   
	   //check for correct file format.... last set of numbers are integers
       if(!in.hasNextInt()){     
    	   throw new IOException();
       }
	   MazeCoord finish = new MazeCoord(in.nextInt(), in.nextInt());  // get the start and finish coordinates
	   
	   return new MazeFrame(frameSize, start, finish);

   }

}
