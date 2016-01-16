import java.awt.*;        // Uses AWT's Layout Managers
import java.awt.event.*; 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;

import javax.imageio.ImageIO;
import javax.swing.*;    
 
public class Sudoku  {
   public static final int GRID_SIZE = 9;    // Size of the board
   public static final int SUBGRID_SIZE = 3; // Size of the sub-grid
 
   // Name-constants for UI control (sizes, colors and fonts)
   public static final int CELL_SIZE = 60;   // Cell width/height in pixels
   public static final int CANVAS_WIDTH  = CELL_SIZE * GRID_SIZE;
   public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
                                             // Board width/height in pixels
   public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
   JPanel controlPanel,buttonPanel,topPanel,rightPanel,boxPanel;
   JButton sol,check,url,file,start;
	public static ImagePanel imgpanel;
	 public static  JFrame frame;
 public static  JTextField tf1,tf2,tf3;
 String imgName="";
 int flag=0;
 //https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaGame_Sudoku.html

  
   private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];
 public static BufferedImage newim;
 public static  BufferedImage img;

   public static int[][] puzzle=null,reset=null;
    
	public static Test tobj=null;
	 public static Sudoku sobj;

   /**
    * Constructor to setup the game and the UI Components
    */
   public Sudoku() {
 
   }
   
   
   
   public void go(){
  frame = new JFrame();
          
	      controlPanel = new JPanel();
	      buttonPanel = new JPanel();
	      topPanel = new JPanel();
	      rightPanel = new JPanel();
boxPanel=new JPanel();
	   //Container cp = getContentPane();
      // cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));  // 9x9 GridLayout
       controlPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
/*controlPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
for (int i =0; i<(9); i++){
    final JLabel label = new JLabel("");
    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    controlPanel.add(label);
}
*/
       
		buttonPanel.setLayout(new FlowLayout());
       topPanel.setLayout(new FlowLayout());
      // boxPanel.setLayout(new BoxLayout(boxPanel,BoxLayout.Y_AXIS));
       boxPanel.setLayout(new BorderLayout());

     
      // Construct 9x9 JTextFields and add to the content-pane
  
       
       for (int row = 0; row < GRID_SIZE; ++row) {
           for (int col = 0; col < GRID_SIZE; ++col) {
              tfCells[row][col] = new JTextField();
             if(((row>=0&&row<=2)||(row>=6&&row<=9))&&((col>=0&&col<=2)||(col>=6&&col<=9)))
              tfCells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK)); 													// Allocate element of array
             else if(row>=3&&row<=5&&col>=3&&col<=5)
                 tfCells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK)); 													// Allocate element of array
            // else
             //    tfCells[row][col].setBorder(BorderFactory.createLineBorder(Color.GRAY)); 													// Allocate element of array

             
              controlPanel.add(tfCells[row][col]);            //  adds JTextField
                 tfCells[row][col].setText("");     // set to empty string
                 tfCells[row][col].setEditable(true);
   
                 tfCells[row][col].setBackground(Color.WHITE);
                
              tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
              tfCells[row][col].setFont(FONT_NUMBERS);
           }
        }
      // Set the size of the content-pane and pack all the components
      //  under this container.
      
      controlPanel.setPreferredSize(new Dimension(300, 300));
      frame.pack();
 
      //controlPanel.setLayout(new BoxLayout(cp,BoxLayout.Y_AXIS));
      start=new JButton("Start Vision");
      start.addActionListener(new startVision());
      file = new JButton("File Explorer");
      file.addActionListener(new getImgFile());
      sol = new JButton("SOLUTION");
      sol.addActionListener(new getSolution());
      check = new JButton("RESET");
      check.addActionListener(new resetSudoku());
      tf1 = new JTextField(30);
      tf2 = new JTextField(30);
      tf3 = new JTextField(10);

      url = new JButton("Enter URL of Sudoku Image");
      url.addActionListener(new getImgUrl());
      topPanel.add(url);
      topPanel.add(tf2);
     // controlPanel.add(sol);
     // frame.add(controlPanel);
      buttonPanel.add(check);
      buttonPanel.add(sol);
      buttonPanel.add(tf1);
      rightPanel.add(file);
      rightPanel.add(tf3);
      rightPanel.setPreferredSize(new Dimension(40,40));
     // ImageIcon icon = new ImageIcon("sudoku26.jpg");
      //JLabel label = new JLabel(icon);
     // boxPanel.add(new ImagePanel(newim),BorderLayout.CENTER);
      boxPanel.add(imgpanel,BorderLayout.CENTER);
      boxPanel.add(start,BorderLayout.NORTH);
      boxPanel.add(rightPanel,BorderLayout.SOUTH);
//boxPanel.add(label);

      frame.getContentPane().add(BorderLayout.NORTH,topPanel);
      frame.getContentPane().add(BorderLayout.WEST,controlPanel);
      frame.getContentPane().add(BorderLayout.SOUTH,buttonPanel);
      frame.getContentPane().add(BorderLayout.CENTER,boxPanel);
      
      //frame.getContentPane().add(BorderLayout.CENTER,new ImagePanel());       
      // this.add(controlPanel);
      
      //this.getContentPane().add(BorderLayout.CENTER,controlPanel);
     // this.add(cp);
     //this.add(sol);
      
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
     frame.setSize(600,500);
      frame.setTitle("Sudoku");
      frame.setVisible(true);
      
   }
   public void monitorState(){
	   Thread th = new Thread(new IncomingReader());
	 	 th.start();
   }
 
   /** The entry main() entry method */
   public static void main(String[] args) {
		
       imgpanel = new ImagePanel(img);

	   try{
			File input = new File("StartActivity.jpg");
			img = ImageIO.read(input);
			newim=img;
			imgpanel.image=newim;
	         imgpanel.paintnewImg();
			
	   }
	   catch(Exception e)
	   {
		   
	   }
	  // img=null;

		
	        tobj = new Test();
			tobj.selectFile();//calling gui part but initially set unvisible
			   
			   sobj = new Sudoku();  
			   sobj.monitorState();
			
			   SwingUtilities.invokeLater(new Runnable(){
		            @Override
		            public void run() {
		                /*JFrame frame = new JFrame("Image Timer");
		                frame.add(new ImagePanel());
		                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		                frame.pack();
		                frame.setLocationRelativeTo(null);
		                frame.setVisible(true);
		                */
		           	 sobj.go();//along with gui a thread will also start to monitor the file gui state
		             tf1.setText("Upload Sudoku Image, Start Vision and Get solution");

		            }
		        });
/*			 
			 
try{
	

	Thread.sleep(2000);
}catch(InterruptedException e){
	
}
	*/
			   

   
   
  /* SwingUtilities.updateComponentTreeUI(frame);

   frame.invalidate();
   frame.validate();
   frame.repaint();
  */
  // sobj.go();
  /* 
   SudokuSolverApp ssaobj = new SudokuSolverApp(img);
   reset = new int[9][9];
   puzzle = ssaobj.start();//get recognised digits
     if(puzzle!=null)
     {
    	 for(int i=0;i<9;i++)
    		 for(int j=0;j<9;j++)
    			 reset[i][j]=puzzle[i][j];
     }
   
    */ 
     
    
 	
   }
   
   public void displayRecognizeddigits(){
	   if(reset==null)
			return;
		for (int row = 0; row < GRID_SIZE; ++row) {
	         for (int col = 0; col < GRID_SIZE; ++col) {
	    
	        	 tfCells[row][col].setText(Integer.toString(reset[row][col]));
	        	 if(reset[row][col]!=0)
                 tfCells[row][col].setBackground(Color.GREEN);
	        	 else
	        	 {
	        		 if(reset[row][col]==0)
	    	        	 tfCells[row][col].setText("");//empty
	             tfCells[row][col].setBackground(Color.YELLOW);
	        	 }

	        	 
	         }
		 }
	   
	   
   }
   
   class getSolution implements ActionListener{
	   public void actionPerformed(ActionEvent event){
		   
		   if(reset==null)
			   return;
		   int flg=0;
		   for (int row = 0; row < GRID_SIZE; ++row) {
	 	         for (int col = 0; col < GRID_SIZE; ++col) {
	 	    
	 	        	 
	 	        	String str = tfCells[row][col].getText();
	 	        	 str.trim();
	 	        	 if(str.length()>1)
	 	        	 {
	 	                tf1.setText("No Solution Exist for this Sudoku");
	 	                return;
	 	        	 }
	 	        	 else if(str.length()==0)
		 	        	 puzzle[row][col]=0;
	 	        	 else
	 	        	 {		
	 	        		 puzzle[row][col]=Integer.parseInt(str);
	 	        		 if(puzzle[row][col]!=0)
	 	        			 flg++;
	 	        	 }
	 	         }
	 		 }
	 		
	 		   
		   
		   if(flg==81)
		   {
			   tf1.setText("Already Solved!!");
			   return;
		   }
		   
		   
		   SolveSudoku ssobj = new SolveSudoku(puzzle, newim);
		 	boolean ans = ssobj.solve();
		 	if(ans==true){
		 		System.out.println();
		 		puzzle=ssobj.display();// display and get soln
		 		 
		 		
		 		for (int row = 0; row < GRID_SIZE; ++row) {
		 	         for (int col = 0; col < GRID_SIZE; ++col) {
		 	    
		 	        	 
		 	        	 tfCells[row][col].setText(Integer.toString(puzzle[col][row]));//puzzle[col][row] bcoz solvesudoku contain array in this (col,row) manner  
		 	        	if(puzzle[col][row]==reset[row][col])
		 	                 tfCells[row][col].setBackground(Color.GREEN);
		 		        	 else
		 		             tfCells[row][col].setBackground(Color.YELLOW);
		 		 

		 	        	 
		 	         }
		 		 }
		 		
		 		
		 		
		 		tf1.setText("Solution Exist!!");

		 	}
		 	else
		 	{		
    		System.out.println("No solution");
            tf1.setText("No Solution Exist for this Sudoku");
		 	}
		   
	   }
	   
	   
	   
   }
   
   
public class resetSudoku implements ActionListener{
	public void actionPerformed(ActionEvent e){
		if(reset==null)
			return;
		for (int row = 0; row < GRID_SIZE; ++row) {
	         for (int col = 0; col < GRID_SIZE; ++col) {
	    
	        	 if(reset[row][col]==0)
		        	 tfCells[row][col].setText("");
	        	 else
	        	 tfCells[row][col].setText(Integer.toString(reset[row][col]));
	        	 
	        	 if(reset[row][col]!=0)
	                 tfCells[row][col].setBackground(Color.GREEN);
		        	 else
		             tfCells[row][col].setBackground(Color.GRAY);
		 
	        	 
	         }
		 }
		
		
	}
}

public class getImgFile implements ActionListener{
	public void actionPerformed(ActionEvent e){
        
		/*if(tobj==null)
		 tobj = new Test();
		
		if(tobj.getFile()==null)
		tobj.selectFile();
		*/
		
		//if(tobj.getState()==false)
			tobj.setvisible();
		
		
	}
}

/**
 * Converts a given Image into a BufferedImage
 *
 * @param img The Image to be converted
 * @return The converted BufferedImage
 */
public static BufferedImage toBufferedImage(Image img)
{
    if (img instanceof BufferedImage)
    {
        return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
}


public class getImgUrl implements ActionListener{
	public void actionPerformed(ActionEvent e){
		
		String str = tf2.getText();//from url textfield
        str=str.trim();
        if(str.length()==0)
        	{tf1.setText("No URL entered");
        	return ;
        	}
        
        try{
		URL url = new URL(str);
		Image image = ImageIO.read(url);

		
       img = toBufferedImage(image);//convert to buffered image 
       newim = toBufferedImage(image);//one more copy (bcoz u r changing size in imagepanel)

      // newim = ImageIO.read(url);
       int wd=img.getWidth();
       int ht=img.getHeight();
       
       System.out.print(wd+" "+ht);
     imgpanel.image=img;
     imgpanel.paintnewImg();
 	tf1.setText("Click on Start Vision");
	//tf2.setText("");


       
        }
        catch(MalformedURLException er)
        {
        	tf1.setText("Enter Valid URL");
        	er.printStackTrace();
        }catch(IOException ex)
        {        	tf1.setText("IO Exception , Check Internet Connection");

        	ex.printStackTrace();
        }
		
		
		
		
	}
}



public class startVision implements ActionListener{//start recognizing the sudoku imag
	public void actionPerformed(ActionEvent e){
		// tf1.setText("Wait ... ... ...");

		String str = tf2.getText();//from url textfield
		str=str.trim();
		if(str.length()!=0)//"")
		{
			System.out.print(str+"ERROR1");
		 	 SudokuSolverApp ssaobj = new SudokuSolverApp(newim);
			   reset = new int[9][9];
			   puzzle = ssaobj.start();//get recognised digits
			     if(puzzle!=null)
			     {
			    	 for(int i=0;i<9;i++)
			    		 for(int j=0;j<9;j++)
			    			 reset[i][j]=puzzle[i][j];
			     }
			     else{
			    	 System.out.print("no array came");
			     }
			    
	 			    displayRecognizeddigits();
			tf2.setText("");
		}
		else
		{
		 str=tf3.getText();
		 str=str.trim();

		 if(str.length()==0)
		 {
			 tf1.setText("No Image Selected");
			 return ;
		 }
			
			//System.out.print("\n"+str+"ERROR2");

		 
		 			//try{
		 			/*	newim = ImageIO.read(new File("testcases/"+str));
		 				img = ImageIO.read(new File("testcases/"+str));
		 			    imgpanel.image=img; 
		 			    imgpanel.paintnewImg();
		 				*/
		 				
		 			    //Thread thr = new Thread(new imathread());
		 			 	 //thr.start();
		 			 	 
		 			 	 SudokuSolverApp ssaobj = new SudokuSolverApp(newim);
		 			   reset = new int[9][9];
		 			   puzzle = ssaobj.start();//get recognised digits
		 			     if(puzzle!=null)
		 			     {
		 			    	 for(int i=0;i<9;i++)
		 			    		 for(int j=0;j<9;j++)
		 			    			 reset[i][j]=puzzle[i][j];
		 			     }
		 			     else{
		 			    	 System.out.print("no array came");
		 			     }
		 			    
			 			    displayRecognizeddigits();

		 			    
		 			//}
		 			//catch(IOException ee)
		 			//{
		 			//	ee.printStackTrace();
		 			//	 tf1.setText("Enter Valid File Name");
		 			//}
		 			//catch(Exception s){
		 			//	System.out.print("ERROR3");
		 			//}
		 
			tf3.setText("");
			tf1.setText("");
		}
		
		
			
	}
}
/*	
 * 
public void displayImage(){
	   
}
public class imathread implements Runnable{
	public void run(){
		tf1.setText("Wait... .. ... .. ... ");
		 imgpanel.image=img;
		    imgpanel.paintnewImg();
		
	}
}
  */ 
  public class IncomingReader implements Runnable{//reading state of 2nd gui file explorer (visible or  not)
	  public void run(){

		 while(true)
		  {	
			 //http://stackoverflow.com/questions/3976344/handling-interruptedexception-in-java
			 try{
				 
			 
             Thread.sleep(1500);//check every 1.5sec
			 
             // when 2nd gui is unvisible after opening file
			  if(tobj.getState()==0)
			  { imgName=tobj.getFile();
			    		if(imgName!=null&&imgName.length()!=0)
			    		{
			    			imgName=imgName.trim();
			    			tf3.setText(imgName);
			    			try{
			    			newim = ImageIO.read(new File("testcases/"+imgName));
			 				img = ImageIO.read(new File("testcases/"+imgName));
			 			    imgpanel.image=img; 
			 			    imgpanel.paintnewImg();
			    			tf1.setText("Click on Start Vision");
			    			 System.out.print(imgName+" m");
			 			     tobj.state=1;//file name read succesfully
			    			}
			    			catch(Exception r){
			    				tf1.setText("Enter Valid File Name");
			    				System.out.print("Error in thread");
			    				r.printStackTrace();
			    			}
			    		}
			   
			  }
			  
             
			 }
			 catch(InterruptedException e){
				 e.printStackTrace();
			 }
			 
			
			  
			  
			  
			  
		  }
		  
	  }
  }
   
   
}