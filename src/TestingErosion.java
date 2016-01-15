import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class TestingErosion extends AbstractOperation
{
	public static void main(String[] args){
		
		Erosion eobj = new Erosion(STRUCTURING_ELEMENT_SHAPE.SQUARE,1);

		Dilation dobj = new Dilation(STRUCTURING_ELEMENT_SHAPE.SQUARE,1);
		for(int i=0;i<1;i++){
			for(int j=0;j<1;j++){
				
				File img = new File("0,4centre.png");
				//File img = new File("digitss/"+i+","+j+"i.jpg");
				try{	
					System.out.print("a");
					BufferedImage im = ImageIO.read(img);
				int	width = im.getWidth();
					int height = im.getHeight();
					BufferedImage imge = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

					
					   for(int ii=0;ii<width;ii++){
						 for(int jj=0;jj<height;jj++){
							
							Color c = new Color(im.getRGB(ii, jj));
							
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
					
					
					
					imge = eobj.execute(imge);
					//imge = dobj.execute(imge);
					//imge = dobj.execute(imge);

					//imge = eobj.execute(imge);

					//ImageIO.write(imge, "jpg",new File("digitss/"+i+","+j+"e.jpg") );
					ImageIO.write(imge, "jpg",new File("0,4.png") );

				}
				catch(Exception e){
					e.printStackTrace();
				}
	}
}
}

	@Override
	public BufferedImage execute(BufferedImage img) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}