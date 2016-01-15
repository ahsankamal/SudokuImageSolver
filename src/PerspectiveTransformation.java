import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class PerspectiveTransformation{
	int width,height;
	int sudoku_dim;
	int[][] image1;
	int[][] image2;
	BufferedImage srcimage,targetimage;
	double x1p, x2p, x3p, x4p, y1p, y2p, y3p, y4p; // to set

	public PerspectiveTransformation(BufferedImage img,
			int dim,
			double x1p,double y1p,
			double x2p,double y2p,
			double x3p,double y3p,
			double x4p,double y4p){
		this.x1p=x1p;
		this.y1p=y1p;
		this.x2p=x2p;
		this.y2p=y2p;
		this.x3p=x3p;
		this.y3p=y3p;
		this.x4p=x4p;
		this.y4p=y4p;
		
		srcimage = img;

	/*	try{
		
			//File fl = new File("Beforetransformed.jpg");
			//ImageIO.write(srcimage,"jpg",fl);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		*/
		
		width = img.getWidth();
		height = img.getHeight();
		//sudoku_dim = Math.max(width, height);
        sudoku_dim=dim;
		//sudoku_dim = Math.min(width, height);
		targetimage = new BufferedImage(sudoku_dim,sudoku_dim,BufferedImage.TYPE_INT_RGB);
		/*try{
			//File fl = new File("Beforetransformed222.jpg");
			//ImageIO.write(targetimage,"jpg",fl);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			*/
		
		image2 = new int[sudoku_dim][sudoku_dim];
		
	}
	public BufferedImage transform(){
		  int x, y; // x, y on page 380
	       int xp, yp; // x prime, y prime on page 380
	        
	       double a11, a12, a13, a21, a22, a23, a31, a32, a33; // on page 383
	     
	       // formula 16.28 on page 383
	       double deno=((x2p-x3p)*(y4p-y3p)-(x4p-x3p)*(y2p-y3p));
	      // if(deno==0)
	    	//   deno=0.01;
	       a31 = ((x1p-x2p+x3p-x4p)*(y4p-y3p)-(y1p-y2p+y3p-y4p)*(x4p-x3p))/deno;
	       // formula 16.29 on page 383
	       a32 = ((y1p-y2p+y3p-y4p)*(x2p-x3p)-(x1p-x2p+x3p-x4p)*(y2p-y3p))/deno;
	    
	       // formula 16.17 on page 380
	       a33 = 1;
  
	       // formula 16.30 on page 383
	       a11 = x2p-x1p+a31*x2p;
	       a12 = x4p-x1p+a32*x4p;
	       a13 = x1p;
		    System.out.println("\nperspectiveTransf"+ a31+"    "+a32+"    "+a11);

	       // formula 16.31 on page 383
	       a21 = y2p-y1p+a31*y2p;
	       a22 = y4p-y1p+a32*y4p;
	       a23 = y1p;

	      
	       // Projective mapping via the unit square on page 382 : unit square to image2 dimension
	       a31 = a31 / sudoku_dim;
	       a32 = a32 / sudoku_dim;

	       a11 = a11 / sudoku_dim;
	       a12 = a12 / sudoku_dim;
	       
	       a21 = a21 / sudoku_dim;
	       a22 = a22 / sudoku_dim;
	       // for each point (x, y) in image2,
	       //      we calculate (xp, yp) in image1 using formula 16.18 and 16.19 on page 380
	       // the formulas are the ones presented in the book
	           
	       for (x = 0; x < sudoku_dim; x++)
	       {
	           for (y = 0; y < sudoku_dim; y++) 
	           {
	                
	                xp = (int) ((a11*x +a12*y + a13) / (a31*x + a32*y + 1)); // formula 16.18 on page 380
	                yp = (int) ((a21*x +a22*y + a23) / (a31*x + a32*y + 1)); // formula 16.19 on page 380
	                
	                if (yp >= 0 && yp < height && xp >= 0 && xp < width)
		               //image2[x][y] = srcimage.getRGB(xp, yp);
	                	
	                	//targetimage.setRGB(x, y, Color.WHITE.getRGB());  
	                	//image2[y][x] = image1[yp][xp];
	                     // image2[x][y] = Color.WHITE.getRGB();
					{
						Color c = new Color(srcimage.getRGB(xp, yp));
						image2[x][y] = c.getRGB();
				
					}
	                else
	                       image2[x][y] = Color.BLACK.getRGB();
	               // targetimage.setRGB(x, y, Color.WHITE.getRGB());

	           targetimage.setRGB(x, y, image2[x][y]);
	           }
	
	
	       }//closing outer for
	 if(sudoku_dim>60){
	try{
		//File fl = new File("PersAftertransformed"+a11+".jpg");
		File fl = new File("PerspTransform.png");

		ImageIO.write(targetimage,"png",fl);
	    }
		catch(Exception e){
			e.printStackTrace();
		}
	       }
	
    return targetimage;
	
	}
	
	

}