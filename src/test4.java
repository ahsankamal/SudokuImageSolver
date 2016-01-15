// include 4 jar files (jai_imageio.jar, jna.jar,
//                  commons-io-2.4.jar, and tess4j.jar)
// copy tessdata folder in root directory of project
//http://tess4j.sourceforge.net/tutorial/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
//tess4j( OCR)
public class test4 extends AbstractOperation{//do erosion n dilation if OCR failed to recognise digit in 1st chance

	static int[][] sudoku;
	//public static void main(String[] args){
	public int testocr(BufferedImage sudo){
		
	//File imgFile = new File("digitss/0,7.jpg");
		//if(sudo==null)
			//return;
		BufferedImage sud = sudo;
		
		/*try{
		 sud = ImageIO.read(new File("sudoku10.jpg"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		*/
		
		int wd = sud.getWidth();
		int ht = sud.getHeight();
		//Graphics g = sud.getGraphics();
		//g.setFont(g.getFont().deriveFont(wd/9));
		//g.setColor(Color.YELLOW);
		
		
		String result="";
		
		ITesseract instance = new Tesseract();
		instance.setTessVariable("tessedit_char_whitelist", "123456789");
		//recognise digits
		
		try{
		 result = instance.doOCR(sudo);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	/*	
		result=result.trim();
		if(result.length()==0)
			return 0;
		
		if(result.length()>1)
		{
			result = result.substring(0, 1);
		}
		System.out.print(result.length());
		System.out.println("["+result+"]"+"["+Integer.parseInt(result)+"]");
	//	sudoku[i][j]=Integer.parseInt(result);
		//g.drawString(result, i*(wd/9)+15, j*(ht/9)+ht/9);
	*/
		//int digit=1;
		result=result.trim();
		if(result.length()!=0)
		{
		System.out.print(result.length());
		System.out.println(" "+result);
		//System.out.println("["+result+"]"+"["+Integer.parseInt(result)+"]");
		return Integer.parseInt(result.substring(0, 1));
		}
		else
		{
			int	width = sud.getWidth();
			int height = sud.getHeight();
			BufferedImage imge = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

			
			   for(int ii=0;ii<width;ii++){
				 for(int jj=0;jj<height;jj++){
					
					Color c = new Color(sud.getRGB(ii, jj));
					
					//imge.setRGB(ii,jj,im.getRGB(ii,jj));
					int red = (int)(c.getRed()*0.299f);//getRed() returns 0-255
					int green = (int)(c.getGreen()*0.587f);
					int blue = (int)(c.getBlue()*0.144f);
					int grey = red+green+blue;//may be greater than 255
					if(grey>255)
						grey=255;
					Color newColor = new Color(grey,grey,grey);
					
					imge.setRGB(ii,jj,newColor.getRGB());
					
					//getRGB() Returns the RGB value(int) representing the color
					//in the default sRGB ColorModel.
				 
				 		}
			   		}
			
			
				Dilation dobj = new Dilation(STRUCTURING_ELEMENT_SHAPE.SQUARE,1);
				//Erosion eobj = new Erosion(STRUCTURING_ELEMENT_SHAPE.SQUARE,1);

			//imge = eobj.execute(imge);
			imge = dobj.execute(imge);
			try{
				 result = instance.doOCR(imge);

				}
				catch(Exception e){
					e.printStackTrace();
				}
			result=result.trim();

			if(result.length()!=0)
			{
			System.out.print(result.length());
			System.out.println(" "+result);
			//System.out.println("["+result+"]"+"["+Integer.parseInt(result)+"]");
			return Integer.parseInt(result.substring(0, 1));
			}
			
			
		}
		return 1;//if not classified by ocr// by default classify it as 1
		
		
	/*	
		sudoku = new int[9][9];
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
			sudoku[i][j]=0;
				
				File img = new File("digitss/"+i+","+j+".jpg");
				try{
					String result = instance.doOCR(img);
					//if(result.length()!=0)
					//Integer.parseInt(result);
					
					result=result.trim();
					if(result.length()==0)
						continue;
					System.out.print(result.length());
					System.out.println("["+result+"]"+"["+Integer.parseInt(result)+"]");
					sudoku[i][j]=Integer.parseInt(result);
					g.drawString(result, i*(wd/9), j*(ht/9)+ht/9);
				
				}catch(TesseractException e){
					System.err.println(e.getMessage());
				}
				
			}
			System.out.println();

		}
		
		
		
		g.dispose();
		
		try{
		File fl = new File("digitss/sud.jpg");
		ImageIO.write(sud, "jpg", fl);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		SolveSudoku ssobj = new SolveSudoku(sudoku, sud);
		boolean ans = ssobj.solve();
		if(ans==true){
			System.out.println();
			ssobj.display();
		}
		else
		{		
			System.out.println("No solution");

		}
		//instance.setLanguage("eng");

		//instance.setOcrEngineMode(2); // see http://www.emgu.com/wiki/files/2.3.0/document/html/a4eee77d-90ad-4f30-6783-bc3ef71f8d49.htm
instance.setTessVariable("tessedit_char_whitelist", "0123456789");
       // Tesseract ins = Tesseract.getInstance();  // JNA Interface Mapping
*/
	
		
		
/*		
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				Rectangle recti = new Rectangle(j*(wd/9),i*(ht/9)  ,(wd/9),(ht/9));

				try{
					String result2 = instance.doOCR(img,recti);
					System.out.println(i+" "+j+" -> "+result2);
					
				}catch(TesseractException e){
					System.err.println(e.getMessage());
				}
				
			}
			//System.out.println();

		
		}
*/
		
		
	}
	@Override
	public BufferedImage execute(BufferedImage img) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
