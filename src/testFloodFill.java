import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class testFloodFill {
static BufferedImage img,out;
	public static void main(String[] args){
		
		
		try{
			File input = new File("BlacknWhiteSudoku.jpg");
			img = ImageIO.read(input);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		try{
			File output = new File("beforetest.jpg");
			ImageIO.write(img,"jpg",output);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		
		ExtractDigit_FloodFill ob =  new ExtractDigit_FloodFill(img,7,7) ;
		
	out =	ob.RegionLabeling();
		try{
			File output = new File("beforetestAfter.jpg");
			ImageIO.write(out,"jpg",output);
			}
			catch(IOException e){
				e.printStackTrace();
			}
	}
	
}
