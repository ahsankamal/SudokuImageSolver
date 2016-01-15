/*

			........................
			........................
			........................//90 degree lines
			
			.  .
			.  .
			.  .
			.  .
			.  .
			.  .//0 or 180 degree lines
			
			
			
			0                         180
		0	............................
			.
			.
		
			.
			.
			.
			.
			.
			.	
		2*houghHeight
            
            
            
centre shifted to width/2,height/2 from 0,0 in image(topleft corner)
.(0,0)       +90
            .
            .
            .
       +0   . +180,+179
......................................
	+180	. . +0     +angle
			.   .
			.     . r     .
			.+90    .   .
			.         .
			.       .
			.


*
*
*
*
*
*/
import java.io.*;

import javax.imageio.*;

import java.awt.image.*;


// module2:

public class Hough {

	static BufferedImage img,blacknWhiteSudoku,tmp,transformed;
	static int width,height;
	int[][] arr;

	public Hough(BufferedImage im,BufferedImage im2){
		try{
		img = ImageIO.read(new File("Floodfill_BFS.jpg"));	
		Thresholding ob = new Thresholding(img);
		img = ob.AdaptiveThresholding();
		
		tmp = im;
		//ColorToGreyscale o =new ColorToGreyscale(tmp);
		//tmp=o.Threshold();
		
		/*
		
		File	fl = new File("bug3a.png");
		try{
		ImageIO.write(img, "png", fl);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		fl = new File("bug3b.jpg");
		try{
		ImageIO.write(tmp, "jpg", fl);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		*/
		
		
		blacknWhiteSudoku = im2;
		width = img.getWidth();
		height = img.getHeight();
        System.out.println(width+" "+height);
        System.out.println(tmp.getWidth()+" "+tmp.getHeight());
        int c=0,d=0;
		for(int i=0;i<width;i++)
		{
				
			for(int j=0;j<height;j++)
			{
				if(tmp.getRGB(i, j)!=img.getRGB(i, j))
		        // System.out.println("Bug 3");
				c++;
				//else
				//d++;
					
			}
		
		}
         System.out.println(c);
         
		
		
		}
		catch(Exception e){
			
			e.printStackTrace();
		}
		
	}
	//public static void main(String[] args){
	
	public int[][] HoughTransform_PerspectiveMapp_andDigitRecog(){	
	    
		//Hough ob  = new Hough();
        //System.out.println(width+" "+height);
		HoughTransform obj = new HoughTransform(width,height,img);
		obj.addPoints();
		BufferedImage output = obj.getHoughArrayImage();
		
		File fl = new File("houghSudokuGridWave.jpg");
		
		try{
		ImageIO.write(output, "jpg", fl);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		double max = obj.getHighestValue();
		//obj.display_Hough_Array(max/2);

		System.out.println("MAx in Hough=:"+max);
		
		obj.getLines((int)(max*5)/10);//50% threshold
		//obj.getLines((int)(max*2)/5);//40% threshold
		//obj.getLines((int)(max*3)/10);//30% threshold
		//obj.getLines((int)(max*35)/100);//35% threshold
		//obj.getLines((int)(max)/10);//10% threshold

		obj.saveImage();	
		arr = obj.perspectiveMapping(blacknWhiteSudoku);
		return arr;
		
		}
	
}
