import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

//import java.util.Timer;
import javax.swing.Timer;//important

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    public  BufferedImage image;

    public ImagePanel(BufferedImage im) {
      
    	
    	if(im!=null){
    		
    	
    	try {                
          image = im;//ImageIO.read(new File("sudoku0.jpg"));
          int wd=image.getWidth();
          int ht=image.getHeight();
         
          
          ReduceSize rs = new ReduceSize();
         image=rs.scaleImage(image, BufferedImage.TYPE_INT_RGB, 250, 250, 250/(double)wd, 250/(double)ht);
       } catch (Exception ex) {
            // handle exception...
       }
    	
    	}
    
      /* Timer timer = new Timer(500, new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e) {
              
        	   repaint();
           }
       });
       timer.start();
    */    
    }
    public void paintnewImg()
    {
    	if(image==null)
    		return;
    	 try {                
             //ImageIO.read(new File("sudoku0.jpg"));
             
    		// URL url = new URL("http://glareanverlag.files.wordpress.com/2011/07/sudoku_juli-2011_7_glarean-magazin.jpg");
             //image = ImageIO.read(url);
    		 int wd=image.getWidth();
             int ht=image.getHeight();
             
             System.out.print(wd+" "+ht);
           
             
             ReduceSize rs = new ReduceSize();
            image=rs.scaleImage(image, BufferedImage.TYPE_INT_RGB, 250, 250, 250/(double)wd, 250/(double)ht);
         repaint(); 
    	 } catch (Exception ex) {
               // handle exception...
          }
    	
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            

    }

}