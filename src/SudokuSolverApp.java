import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SudokuSolverApp {
	public static BufferedImage img=null,grid=null,blacknWhiteSudo,tmp;
    int[][] arr;
	public SudokuSolverApp(BufferedImage img){
		this.img = img;
		
	}
	//public static void main(String[] args){
	public int[][] start(){

		/*	SudokuSolverApp ssobj = new SudokuSolverApp();

		try{
			File input = new File("sudoku25.jpg");
			img = ImageIO.read(input);
		}	
		catch(IOException e){
				e.printStackTrace();
			}
		*/
		
			int w = img.getWidth();
			int h = img.getHeight();
			System.out.println(w+" "+h);

			int newWd=w;
			int newHt=h;
			ReduceSize rsobj = new ReduceSize();
			/*if(newWd>4000||newHt>4000)
			{
				// half
				newWd=newWd/2;
				newHt=newHt/2;
				img = rsobj.scale(img, newWd, newHt);

			}
			else if(newWd>3000||newHt>3000)
			{
				//60%
				newWd=(newWd*6)/10;
				newHt=(newHt*6)/10;
				img = rsobj.scale(img, newWd, newHt);
				
			}
			else if(newWd>2048||newHt>2048)
			{
				//80%
				newWd=(newWd*8)/10;
				newHt=(newHt*8)/10;
				img = rsobj.scale(img, newWd, newHt);
				
			}*/
			//else
		//	{
				//80%
				System.out.println(w+" "+h);

				newWd=512;
				newHt=512;
				double scalex=512/(double)w;
				double scaley=512/(double)h;
				
				
				System.out.println(scalex+" "+scaley);
				img = rsobj.scaleImage(img, BufferedImage.TYPE_INT_RGB, newWd,newHt,scalex, scaley);
				//System.exit(0);
				
		//	}
			//Module 1:
			//converting colored image to grey (ColorToGreyScale.java)
			//then converting greyImg to blackandwhite image (Thresholding.java) 
			//then finding sudokugrid using BreadthFirstsearch FloodFill Algo (FloodFill_BFS.java) 
			
			ColorToGreyscale obj = new ColorToGreyscale(img);
			tmp = obj.Threshold();
			int wd = tmp.getWidth();
			int ht = tmp.getHeight();
			//bug detected ;)
			//tmp is needed bcoz we need to store blackNwhite state of sudoku for perspectivetransformation
			// just having ref will not work bcoz as we pass ref(of blacknwhite) to FloodFill_BFS() it gets changed.
			blacknWhiteSudo = new BufferedImage(wd,ht,BufferedImage.TYPE_INT_RGB);
			for(int i=0;i<wd;i++)
				for(int j=0;j<ht;j++)
					blacknWhiteSudo.setRGB(i, j, tmp.getRGB(i, j));
			
			/*
			File fl = new File("bug1.jpg");
    		try{
    		ImageIO.write(blacknWhiteSudo, "jpg", fl);
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}*/
			
			/*tmp = obj.BFSfloodFilling();
			wd = tmp.getWidth();
			ht = tmp.getHeight();
			
			grid = new BufferedImage(wd,ht,BufferedImage.TYPE_INT_ARGB);
			for(int i=0;i<wd;i++)
				for(int j=0;j<ht;j++)
					grid.setRGB(i, j, tmp.getRGB(i, j));
			*/
			grid = obj.BFSfloodFilling();
			
		/*	File fl = new File("bug2.jpg");
    		try{
    		ImageIO.write(grid, "jpg", fl);
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}
			*/
			
			// bug2 solved ;) when we write the image in jpeg 
			//format it gets compressed so some pixels values changes 
			//png is lossless
			
			
			// Module 2:
			//finding 4 straightlines in grid(top,left,right,bottom)
			//using Hought transform
			//then doing perspective transformation on blacknWhiteSudoku
			Hough obj2 = new Hough(grid,blacknWhiteSudo);
			//BufferedImage transformed = obj2.HoughTransform_PerspectiveMapp_andDigitRecog();
			arr = obj2.HoughTransform_PerspectiveMapp_andDigitRecog();

			return arr;
			
			//module 3:
			// recognising each digit using tess4j jni wrapper (tesseract-OCR)
			//then solving sodoku using backtracking sudoku algo
			// and showing solution on the image
			
			//RecognizeDigits rdobj = new RecognizeDigits(transformed);
			
			
			
			/*File fl = new File("bugugu.jpg");
    		try{
    		ImageIO.write(blacknWhiteSudo, "jpg", fl);
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}
		*/
		
		
			
	}
	//http://stackoverflow.com/questions/12879540/image-resizing-in-java-to-reduce-image-size
		//progressive bilinear scaling
		
	
}
