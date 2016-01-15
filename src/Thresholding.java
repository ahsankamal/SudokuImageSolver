import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Thresholding {
BufferedImage img;
int width,height;
int threshold;

public Thresholding(BufferedImage imag){
	    threshold = 127;
		width = imag.getWidth();
		height = imag.getHeight();
		img=imag;
	}


public BufferedImage ConvertToBinaryImg(){
	for(int i=0;i<width;i++){
		for(int j=0;j<height;j++){
			Color c = new Color(img.getRGB(i, j));
			int avg = (c.getRed() + c.getGreen() +c.getBlue())/3;
			
			if(avg>=threshold)
			{
			img.setRGB(i, j, Color.WHITE.getRGB());
			}
			else
			{
			img.setRGB(i, j, Color.BLACK.getRGB());
			}
		}
	}
	try{
	File output = new File("BlacknWhiteSudoku.jpg");
	ImageIO.write(img, "jpg", output);
	}
	catch(IOException e){
		e.printStackTrace();
	}
	return img;

}

public BufferedImage AdaptiveThresholding(){
	
	int wd = img.getWidth();
	int ht = img.getHeight();
//integral image 
	long[][] intImg = new long[ht+7][wd+7];
	BufferedImage out = new BufferedImage(wd,ht,BufferedImage.TYPE_INT_RGB);
	long sum=0;
	
	/*Graphics2D g = out.createGraphics();
	g.drawImage(img,0,0,null);
	g.dispose();
	*/
	
	for(int i=0;i<wd;i++){
		sum=0;
		for(int j=0;j<ht;j++){
			
			Color c = new Color(img.getRGB(i, j));
		//	int avg = c.getRed();// + c.getGreen() +c.getBlue())/3;
	/*if(c.getBlue()==c.getGreen())
		{
		System.out.println(c.getRed()+" "+c.getGreen()+" "+c.getBlue()+" "+c.getRGB()+" "+img.getRGB(i, j));
		return img;
		}
		*/
			/*int pix=(img.getRGB(i, j));
			int r = (pix >> 16) & 0xFF;
			int g = (pix >> 8) & 0xFF;
			int b = pix & 0xFF;
			//System.out.println(pix & 0xFF);
			 * in greyscale r=g=b take any
			*/
			
			sum=sum+c.getGreen();
			//sum=sum+(pix & 0xFF);
			if(i==0)
			{
				intImg[j][i]=sum;
			}
			else
			{
				intImg[j][i]=intImg[j][i-1]+sum;
			}
			
		}
		
	}
	
	double s=wd/8;
	double t=15;
	double  tt =(100-t)/100;
	int s2=(int)(s/2);//to find s*s region
	int bl=0,wt=0;
for(int i=0;i<wd;i++){
	
	for(int j=0;j<ht;j++){
		
		
		// finding s*s region
		int x1 = i-s2;
		if(x1<0)
			x1=0;
		
		int x2 = i+s2;
		if(x2>=wd)
			x2=wd-1;
		
		int y1 = j-s2;
		if(y1<0)
			y1=0;
		
		int y2 = j+s2;
		if(y2>=ht)
			y2=ht-1;
		
		
		int count = (x2-x1)*(y2-y1);//no of pixels in that region
		
		//sum = intImg[x2][y2]-intImg[x2][y1-1]-intImg[x1-1][y2]+intImg[x1-1][y1-1];
		if(x1==0||y1==0)
		sum = intImg[y2][x2]-intImg[y2][x1]-intImg[y1][x2]+intImg[y1][x1];
		else
		sum = intImg[y2][x2]-intImg[y2][x1-1]-intImg[y1-1][x2]+intImg[y1-1][x1-1];
		
		
		
		Color c1 = new Color(img.getRGB(i, j));
		//int valueofpixel = c1.getRed();// + c1.getGreen() +c1.getBlue())/3;
		//int valueofpixel = Math.abs(img.getRGB(i, j));//(c.getRed() + c.getGreen() +c.getBlue())/3;
		/*int pix=(img.getRGB(i, j));
		int r = (pix >> 16) & 0xFF;
		int g = (pix >> 8) & 0xFF;
		int b = pix & 0xFF;
	    	*/
		
		int valueofpixel=c1.getGreen();//(pix & 0xFF);
		
		//System.out.println(valueofpixel*count+" "+(sum)*((100-t)/100));
		
		if(valueofpixel*count <= sum*tt)
			{
			//System.out.println("black");
			//bl++;
		out.setRGB(i, j,Color.BLACK.getRGB());
			}
	
	 	else
		{
	 		//wt++;
			//System.out.println("w");
		out.setRGB(i, j, Color.WHITE.getRGB());
		}
		
		
		
	}
	
}
//System.out.println(bl+" "+wt);
try{
File output = new File("Adap_BlacknWhiteSudoku.jpg");
ImageIO.write(out, "jpg", output);
}
catch(IOException e){
	e.printStackTrace();
}
return out;



}


}
