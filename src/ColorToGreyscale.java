
//greyscale digital image is an image in which the 
//value of each pixel vary from black at the 
//weakest intensity to white at the strongest.It is
//different from black n white image(which r binary imgs).
//4 well known methods to convert colorImg To Greyscale
//lightness method,average method,
//luminosity method -> gray = 0.21 R + 0.72 G + 0.07 B
//Luma (src wikipedia) -> Gray = (Red * 0.299 + Green * 0.587 + Blue * 0.114)

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.imageio.*;

import java.io.File;
import java.io.IOException;
//module1;
public class ColorToGreyscale {

	BufferedImage img,binaryImg,grid;
	int width,height;
	public void paint(Graphics g){
		g.drawImage(img,20,20,null);
	}
	
	public ColorToGreyscale(BufferedImage im){
		
		img = im;
		
		width = img.getWidth();
		height = img.getHeight();
		
		
		   for(int i=0;i<width;i++){
			 for(int j=0;j<height;j++){
				
				Color c = new Color(img.getRGB(i, j));
				
				
				int red = (int)(c.getRed()*0.299f);//getRed() returns 0-255
				int green = (int)(c.getGreen()*0.587f);
				int blue = (int)(c.getBlue()*0.144f);
				int grey = red+green+blue;//may be greater than 255
				if(grey>255)
					grey=255;
				Color newColor = new Color(grey,grey,grey);
				img.setRGB(i,j,newColor.getRGB());
				//getRGB() Returns the RGB value(int) representing the color
				//in the default sRGB ColorModel.
			 
			 }
		   }
		
		   try{
		File output = new File("ColorToGreyScale.jpg");
		ImageIO.write(img, "jpg", output);
		   }
	catch(IOException e){
		e.printStackTrace();
		}
		
		
	}
	
	public BufferedImage Threshold(){
		Thresholding th = new Thresholding(img);
		//th.ConvertToBinaryImg();
		binaryImg = th.AdaptiveThresholding();
		return binaryImg;
	}
	
	public BufferedImage BFSfloodFilling(){
		FloodFill_BFS ff = new FloodFill_BFS(binaryImg);
		 grid = ff.RegionLabeling();
		
		
	   try{
			File output = new File("Floodfill_BFS.jpg");
			ImageIO.write(grid,"jpg",output);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		
		 
		return grid;
	}
	
	
	
}
