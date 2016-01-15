
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

/**"http://homepages.inf.ed.ac.uk/rbf/HIPR2/hough.htm">http://homepages.
 * inf.ed.ac.uk/rbf/HIPR2/hough.htm
  Olly Oechsle, University of Essex

 * 
 * If you represent a line as
 * x cos(theta) + y sin (theta) = r
 * and you know values of x and y, you can calculate all the values of r by
 * going through all the possible values of theta. If you plot the values of r
 * on a graph for every value of theta you get a sinusoidal curve. This is the
 * Hough transformation.

 * The hough tranform works by looking at a number of such x,y coordinates,
 * which are usually found by some kind of edge detection. Each of these
 * coordinates is transformed into an r, theta curve. This curve is discretised
 * so we actually only look at a certain discrete number of theta values.
 * "Accumulator" cells in a hough array along this curve are incremented for X
 * and Y coordinate.
 *
 * The accumulator space is plotted rectangularly with theta on one axis and r
 * on the other. Each point in the array represents an (r, theta) value which
 * can be used to represent a line using the formula above.
 * 
 * Once all the points have been added should be full of curves. The algorithm
 * then searches for local peaks in the array. The higher the peak the more
 * values of x and y crossed along that curve, so high peaks give good
 * indications of a line.
 * 
 * 
 */

public class HoughTransform extends Thread {

        // The size of the neighbourhood in which to search for other local maxima
        final int neighbourhoodSize = 2;

        // How many discrete values of theta shall we check?
        final int maxTheta = 180;
        double topleftX,topleftY;
        double toprightX,toprightY;
        double bottomleftX,bottomleftY;
        double bottomrightX,bottomrightY;
        

        // Using maxTheta, work out the step
        final double thetaStep = Math.PI / maxTheta;

        // the width and height of the image
        protected int width, height;

        // the hough array
        protected int[][] houghArray;

        // the coordinates of the centre of the image
        protected float centerX, centerY;

        // the height of the hough array
        protected int houghHeight;

        // double the hough height (allows for negative numbers)
        protected int doubleHeight;

        // the number of points that have been added
        protected int numPoints;

        // cache of values of sin and cos for different theta values. Has a
        // significant performance improvement.
        private double[] sinCache;
        private double[] cosCache;
        int totalLines;
        
        BufferedImage image;
        
        /**
         *
         */
        public HoughTransform(int width, int height, BufferedImage img) {

                this.width = width;
                this.height = height;
                image = img; 

    			File	fl = new File("bug3.jpg");
        		try{
        		ImageIO.write(image, "jpg", fl);
        		}
        		catch(IOException e){
        			e.printStackTrace();
        		}
    			
                initialise();

        }

        /**
         * Initialises the hough array.
         */
        public void initialise() {

                // Calculate the maximum height the hough array needs to have
                houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2;

                // Double the height of the hough array to cope with negative r values
                doubleHeight = 2 * houghHeight;
                System.out.println(width+" "+height+"\n2*HoughHeight = "+doubleHeight);
                // Create the hough array
                houghArray = new int[maxTheta][doubleHeight];

                // Find edge points and vote in array
                centerX = width / 2;
                centerY = height / 2;

                // Count how many points there are
                numPoints = 0;

                // cache the values of sin and cos for faster processing
                sinCache = new double[maxTheta];
                cosCache = sinCache.clone();
                for (int t = 0; t < maxTheta; t++) {
                        double realTheta = t * thetaStep;
                        sinCache[t] = Math.sin(realTheta);
                        cosCache[t] = Math.cos(realTheta);
                }
                
                
                
        }

        /**
         * Adds points from an image. The image is assumed to be greyscale black and
         * white, so all pixels that are not black are counted as edges. The image
         * should have the same dimensions as the one passed to the constructor.
         */
        public void addPoints() {
        
                // Now find edge points and update the hough array
                for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                                // Find non-black pixels
                               //if ((image.getRGB(x, y) & 0x000000ff) != 0)
                        		if(image.getRGB(x,y)==Color.BLACK.getRGB())//if black
                        		{
                                        addPoint(x, y);
                        		}
                        }
                }
        }

        /**
         * Adds a single point to the hough transform. You can use this method
         * directly if your data isn't represented as a buffered image.
         */
        public void addPoint(int x, int y) {

                // Go through each value of theta
                for (int t = 0; t < maxTheta; t++) {

                        // Work out the r values for each theta step
                        int r = (int) (((x - centerX) * cosCache[t]) + ((y - centerY) * sinCache[t]));

                        // this copes with negative values of r
                        r += houghHeight;

                        if (r < 0 || r >= doubleHeight)
                                continue;

                        // Increment the hough array
                        houghArray[t][r]++;

                }

                numPoints++;
        }

        public void  display_Hough_Array(int threshold){
        	System.out.println();
        	System.out.println(maxTheta+" "+doubleHeight);
        	for(int i=0;i<maxTheta;i++)
        	{
        		for(int j=0;j<doubleHeight;j++)
        		{	if(houghArray[i][j]>threshold)
        			System.out.print("["+houghArray[i][j]+" "+i+" "+j+"]");
        		}
        		//System.out.println();
        	}
        }
        
        
        /**
         * Once points have been added in some way this method extracts the lines
         * and returns them as a Vector of HoughLine objects, which can be used to
         * draw on the
         * 
         *  percentageThreshold
         *            The percentage threshold above which lines are determined from
         *            the hough array
         */
        public Vector<HoughLine> getLines(int threshold) {

                // Initialise the vector of lines that we'll return
                Vector<HoughLine> lines = new Vector<HoughLine>(20);
                totalLines=0;
                int[][] arr = new int[2][100000];
                // Only proceed if the hough array is not empty
                if (numPoints == 0)
                        return lines;
int oldT=1000;
                // Search for local peaks above threshold to draw
                for (int t = 0; t < maxTheta; t++) {

                    //int minR=100000,maxR=0;
                	loop: for (int r = neighbourhoodSize; r < doubleHeight
                                        - neighbourhoodSize; r++) {

                                // Only consider points above threshold
                                if (houghArray[t][r] > threshold) {

                                        int peak = houghArray[t][r];
                                        
                                        // Check that this peak is indeed the local maxima
                                        for (int dx = -neighbourhoodSize; dx <= neighbourhoodSize; dx++) {
                                                for (int dy = -neighbourhoodSize; dy <= neighbourhoodSize; dy++) {
                                                        int dt = t + dx;
                                                        int dr = r + dy;
                                                        if (dt < 0)
                                                                dt = dt + maxTheta;
                                                        else if (dt >= maxTheta)
                                                                dt = dt - maxTheta;
                                                        if (houghArray[dt][dr] > peak) {
                                                                // found a bigger point nearby, skip
                                                        //    System.out.println(t+" ab "+r);    
                                                        	continue loop;
                                                       
                                                        }
                                                }
                                        }

                                        //if(minR>r)
                                        	//minR=r;
                                        //if(maxR<r)
                                        	//maxR=r;
                                        // calculate the true value of theta
                                        double theta = t * thetaStep;

                                        if((t>=0&&t<=20)||(t>=80&&t<=100)||(t>=160&&t<=180))
                                        {//consider only these angles
                                        arr[0][totalLines]=t;
                                        arr[1][totalLines]=r;
                                        totalLines++;
                                        }
                                        // add the line to the vector
                                 //draaw all lines
                                 
                                      
                                  HoughLine hobj = new HoughLine(theta,r);
                            	//		System.out.print("["+houghArray[t][r]+" "+t+" "+r+" "+theta+"]");
                                        lines.add(hobj);
                  
                                        hobj.draw(image, Color.RED.getRGB());
                              
                                        //if((t==88&&r==935)||(t==5&&r==714)||(t==174&&r==762)||(t==88&&r==2953))//BFSfloodfill
                                    // if((t==0&&r==234)||(t==90&&r==298)||(t==179&&r==307)||(t==90&&r==666))//floodfillsudoku1
                                     // if((t==4&&r==239)||(t==91&&r==257)||(t==90&&r==498)||(t==176&&r==215))//floodfillsudoku2
                                        //if((t==90&&r==498)||(t==176&&r==215))
                                      //  if((t==0&&r==1040)||(t==94&&r==1018))

                                	   // if(t==179||t==178||t==177||t==176)
                                       // hobj.draw(image, Color.GREEN.getRGB());
                                      //if(t==0||t==1||t==2||t==3||t==4)
  									
                                }// closing if
                                
                                
                        }//closing inner for
                    /*
                    if(minR!=100000&&maxR!=0)
                    {		
                    	if(oldT==1000)
                    	{
                    		System.out.println(oldT+" "+t);
                    		oldT=t;

                    		  // calculate the true value of theta
                            double theta = t * thetaStep;
                            // add the line to the vector
                            HoughLine hobj = new HoughLine(theta,minR);
                			System.out.print("["+houghArray[t][minR]+" "+t+" "+minR+"]");
                            lines.add(hobj);
                            hobj.draw(image, Color.GREEN.getRGB());
          
                            HoughLine hobj2 = new HoughLine(theta,maxR);
                			System.out.print("["+houghArray[t][maxR]+" "+t+" "+maxR+"]");
                            lines.add(hobj2);
                            hobj2.draw(image, Color.GREEN.getRGB());
                    		
                    	}
                    	else if(Math.abs(oldT-t)>80&&Math.abs(oldT-t)<100)
                    	{
                    		System.out.println(oldT+" "+t);
                    		oldT=t;

                    		 double theta = t * thetaStep;
                             // add the line to the vector
                             HoughLine hobj = new HoughLine(theta,minR);
                 			System.out.print("["+houghArray[t][minR]+" "+t+" "+minR+"]");
                             lines.add(hobj);
                             hobj.draw(image, Color.GREEN.getRGB());
           
                             HoughLine hobj2 = new HoughLine(theta,maxR);
                 			System.out.print("["+houghArray[t][maxR]+" "+t+" "+maxR+"]");
                             lines.add(hobj2);
                             hobj2.draw(image, Color.GREEN.getRGB());
                    	}
                    }
                    */
                    
                }//closing outer for

                
    		/*	System.out.println("\n\ntotallines"+totalLines+"\n");
                for(int i=0;i<totalLines;i++)
                {
                	int tt = arr[0][i];
                	int rr = arr[1][i];
        			System.out.print("["+houghArray[tt][rr]+" "+tt+" "+rr+"]");
                }
              */
                
                
                
                //double[][] linesCoordinates = new double[7][1000000];
                int lineNo=0;	
                double max=0.0,min=1000000.0;
                //leftmost,rightmost,topmost,bottommost lines(not in order yet)
                
                int line1_t=0,line1_r=0;
                int line2_t=0,line2_r=0;
                int line3_t=0,line3_r=0;
                int line4_t=0,line4_r=0;

             	
                
            	//linescoordinates 0th and 1st index contain x and y
            	//2nd and 3rd contain angle and r of lineA
            	//4th and 5th also contain angle and r of lineB
            	//x and y are intersection point of lineA and lineB
            	//6th contain x+y
            	//min x+y = topleft coner
            	//max x+y = bottomright corner
                
                
                
                
                for(int i=0;i<totalLines;i++){
                	for(int j=i+1;j<totalLines;j++){
                		
                		double a1,a2,b1,b2,c1,c2,tt1,tt2;
                		if(Math.abs(arr[0][i]-arr[0][j])<75||Math.abs(arr[0][i]-arr[0][j])>105||lineNo>2000000)
                			continue;
                		tt1=arr[0][i]*thetaStep;
                		tt2=arr[0][j]*thetaStep;//angle2
                		if(tt1==tt2)
                			continue;
                	
                		a1=Math.cos(tt1);
                		b1=Math.sin(tt1);
                		a2=Math.cos(tt2);
                		b2=Math.sin(tt2);
                		c1=arr[1][i]-houghHeight;
                		c2=arr[1][j]-houghHeight;
                		
                		
                		
                		double x,y;
                		y = (a1*c2-c1*a2)/(a1*b2-a2*b1);
                		x = (c1*b2-c2*b1)/(a1*b2-a2*b1);
                		
                		x=x+centerX;
                		y=y+centerY;
                		
                	//	if(arr[0][i]==90&&arr[1][i]==498&&arr[0][j]==176&&arr[1][j]==215)
                		//{
                //  System.out.println("\n\n\n"+a2*c1+" "+a1*c2+" "+a2*b1+" "+a1*b2+" "+(a2*c1 - a1*c2)/(a2*b1-a1*b2)+" "+(a2*b1-a1*b2)+" "+(a2*c1 - a1*c2)+" "+b1*y);
                		//System.out.println("\n\n\n"+y+" "+x+" "+c1+" "+c2+" "+a1+" "+a2+" "+b1+" "+b2);
                		//}
                		
                		
                		if(x<0||x>width)
                			continue;
                		if(y<0||y>height)
                			continue;
    // System.out.println(lineNo);
//canbe >=5000 
                		
                		 /*linesCoordinates[0][lineNo]=x;
                		 linesCoordinates[1][lineNo]=y;

                		 linesCoordinates[2][lineNo]=arr[0][i];
                		 linesCoordinates[3][lineNo]=arr[1][i];

                		 linesCoordinates[4][lineNo]=arr[0][j];
                		 linesCoordinates[5][lineNo]=arr[1][j];
                		 linesCoordinates[6][lineNo]=x+y;
                		*/
                      //  System.out.println(x+" "+y);
                		 if(min>x+y)
                			{
                			min=x+y;
                			line1_t=arr[0][i];//topmost Line if t=90
                			line1_r=arr[1][i];
                			line2_t=arr[0][j];//leftmost Line if t=0 or 180
                			line2_r=arr[1][j];
                			
                			topleftX=x;
                			topleftY=y;
                			}
                		if(max<x+y)
                			{
                			max=x+y;
                			line3_t=arr[0][i];//bottomost Line if t=90
                			line3_r=arr[1][i];
                			line4_t=arr[0][j];//rightmost Line if t=0 or 180
                			line4_r=arr[1][j];
                			bottomrightX=x;
                			bottomrightY=y;
                			}
                		 
               		 lineNo++;
                		
                	}
                }
                

                int tt=0,rr=0;
                if(line1_t>=70&&line1_t<=110)//90 degree
                {
                	System.out.println("top\nleft\n");
                	
                }
                else
                {
                	tt = line1_t;
                	line1_t=line2_t;
                	line2_t=tt;
                	rr = line1_r;
                	line1_r=line2_r;
                	line2_r=rr; 	
                	System.out.println("left\ntop\n");

                }
                if(line3_t>=70&&line3_t<=110)
                {
                	System.out.println("bottom\nright\n");
                	
                }
                else
                {
                	tt = line3_t;
                	line3_t=line4_t;
                	line4_t=tt;
                	rr = line3_r;
                	line3_r=line4_r;
                	line4_r=rr; 	
                	System.out.println("right\nbottom\n");

                }
                
                //line1_t = topline
                //line2_t = leftline
                //line3_t = bottomline
                //line4_t = rightline
                
                double a1,a2,b1,b2,c1,c2,tt1,tt2;
        		
        		tt1=line1_t*thetaStep;
        		tt2=line4_t*thetaStep;//angle2
   
        		a1=Math.cos(tt1);
        		b1=Math.sin(tt1);
        		a2=Math.cos(tt2);
        		b2=Math.sin(tt2);
        		c1=line1_r-houghHeight;
        		c2=line4_r-houghHeight;
        		
        		double x,y;
        		y = (a1*c2-c1*a2)/(a1*b2-a2*b1);
        		x = (c1*b2-c2*b1)/(a1*b2-a2*b1);
        		
        		x=x+centerX;
        		y=y+centerY;
        				
        		if(x<0||x>width)
        			x=bottomrightX;
        		if(y<0||y>height)
        			y=topleftY;
                
                toprightX=x;
                toprightY=y;
                
            	
        		tt1=line2_t*thetaStep;
        		tt2=line3_t*thetaStep;//angle2
   
        		a1=Math.cos(tt1);
        		b1=Math.sin(tt1);
        		a2=Math.cos(tt2);
        		b2=Math.sin(tt2);
        		c1=line2_r-houghHeight;
        		c2=line3_r-houghHeight;
        		
        		
        		y = (a1*c2-c1*a2)/(a1*b2-a2*b1);
        		x = (c1*b2-c2*b1)/(a1*b2-a2*b1);
        		
        		x=x+centerX;
        		y=y+centerY;
        				
        		if(x<0||x>width)
        			x=topleftX;
        		if(y<0||y>height)
        			y=bottomrightY;
                
                bottomleftX=x;
                bottomleftY=y;
                
                
                /*
                int[][] horizontalLine = new int[2][2];
                int[][] verticalLine = new int[2][2];
                
                int hl=0,fg1=0;;
                int vl=0,fg2=0;
                //finding 2 parallel horizontalLines
                if(line1_t>=80&&line1_t<=100)//angle approx 90
                {
                	if(hl<2)
                	{
                	horizontalLine[hl][0]=line1_t;
                	horizontalLine[hl][1]=line1_r;
                	hl++;
                	}
                	
                }
                if(line2_t>=80&&line2_t<=100)
                {	if(hl<2)
                	{
                	horizontalLine[hl][0]=line2_t;
                	horizontalLine[hl][1]=line2_r;
                	hl++;
                	}
                	
                }
                if(line3_t>=80&&line3_t<=100)
                {	if(hl<2)
                	{
                	horizontalLine[hl][0]=line3_t;
                	horizontalLine[hl][1]=line3_r;
                	hl++;
                	}
                	else
                	{
                		if(hl==2)
                			fg1=2;
                	}
                } 
                if(line4_t>=80&&line4_t<=100)
                {	if(hl<2)
                	{
                	horizontalLine[hl][0]=line4_t;
                	horizontalLine[hl][1]=line4_r;
                	hl++;
                	}
                	else
                	{
                		if(hl==2)
                			fg1=2;
                	}
                }
                
                //finding 2 vertical lines, angle canbe 0 or 180 approx
             if((line1_t>=0&&line1_t<=20)||(line1_t<=180&&line1_t>=160))
             {
            	 if(vl<2)
            	 {
            		 verticalLine[vl][0]=line1_t;
                 	 verticalLine[vl][1]=line1_r;
                 	vl++;
            	 }
            	 
             }
                
             if((line2_t>=0&&line2_t<=20)||(line2_t<=180&&line2_t>=160))
             {
            	 if(vl<2)
            	 {
            		 verticalLine[vl][0]=line2_t;
                 	 verticalLine[vl][1]=line2_r;
                 	vl++;
            	 }
            	 
             }
                
             if((line3_t>=0&&line3_t<=20)||(line3_t<=180&&line3_t>=160))
             {
            	 if(vl<2)
            	 {
            		 verticalLine[vl][0]=line3_t;
                 	 verticalLine[vl][1]=line3_r;
                 	vl++;
            	 }
            	 else
            	 {
            		 if(vl==2)
            		 {
            			 fg2=2;
            		 }
            	 }
             }
                
             if((line4_t>=0&&line4_t<=20)||(line4_t<=180&&line4_t>=160))
             {
            	 if(vl<2)
            	 {
            		 verticalLine[vl][0]=line4_t;
                 	 verticalLine[vl][1]=line4_r;
                 	vl++;
            	 }
            	 else
            	 {
            		 if(vl==2)
            			 fg2=2;
            	 }
             }
               //fg1,fg2 to detect bug
                */
                
    			System.out.println("\nTotalLineNos"+lineNo+"\n");
            	System.out.println("\n"+line1_t+" "+line1_r+"\n");
            	System.out.println("\n"+line2_t+" "+line2_r+"\n");
            	System.out.println("\n"+line3_t+" "+line3_r+"\n");
            	System.out.println("\n"+line4_t+" "+line4_r+"\n");
            	/*
            	System.out.println("\nAc_TotalLineNos"+lineNo+"\n");
            	System.out.println("\n"+verticalLine[0][0]+" "+verticalLine[0][1]+"\n");
            	System.out.println("\n"+verticalLine[1][0]+" "+verticalLine[1][1]+"\n");
            	System.out.println("\n"+horizontalLine[0][0]+" "+horizontalLine[0][1]+"\n");
            	System.out.println("\n"+horizontalLine[1][0]+" "+horizontalLine[1][1]+"\n");
            	*/
            	
            	// drawing 4 corner lines
                HoughLine hobj1 = new HoughLine(line1_t*thetaStep,line1_r);
                  lines.add(hobj1);
                  hobj1.draw(image, Color.GREEN.getRGB());
               
                HoughLine hobj2 = new HoughLine(line2_t*thetaStep,line2_r);
                  lines.add(hobj2);
                  hobj2.draw(image, Color.GREEN.getRGB());
                
                HoughLine hobj3 = new HoughLine(line3_t*thetaStep,line3_r);
                  lines.add(hobj3);
                  hobj3.draw(image, Color.GREEN.getRGB());
                HoughLine hobj4 = new HoughLine(line4_t*thetaStep,line4_r);
                  lines.add(hobj4);
                  hobj4.draw(image, Color.GREEN.getRGB());
                double tmpLx=0,tmpLy=0;
                double tmpRx=0,tmpRy=0;
                
                
                
                /*if(hl==2&&vl==2)//everything is fine//4 lines 
                  {
            
                	  
                	  // intersection of h1 line ,v1 line
                	double a1,a2,b1,b2,c1,c2,tt1,tt2;
              		tt1=horizontalLine[0][0]*thetaStep;
              		tt2=verticalLine[0][0]*thetaStep;//angle2
            
              		a1=Math.cos(tt1);
              		b1=Math.sin(tt1);
              		a2=Math.cos(tt2);
              		b2=Math.sin(tt2);
              		c1=horizontalLine[0][1]-houghHeight;
              		c2=verticalLine[0][1]-houghHeight;
              		double x,y;
              		y = (a1*c2-c1*a2)/(a1*b2-a2*b1);
              		x = (c1*b2-c2*b1)/(a1*b2-a2*b1);
              		
              		x=x+centerX;
              		y=y+centerY;
            
              			if((x==bottomrightX&&y==bottomrightY)||(x==topleftX&&y==topleftY))
              				{
                        	System.out.println("Nothing");//outof 4 nothing 2 mustbe printed
            	
              				}
              			else
              			{
              				tmpLx=x;
              				tmpLy=y;
              			}
                  	  // intersection of h1 line ,v2 line  
                		tt1=horizontalLine[0][0]*thetaStep;
                		tt2=verticalLine[1][0]*thetaStep;//angle2
              
                		a1=Math.cos(tt1);
                		b1=Math.sin(tt1);
                		a2=Math.cos(tt2);
                		b2=Math.sin(tt2);
                		c1=horizontalLine[0][1]-houghHeight;
                		c2=verticalLine[1][1]-houghHeight;
                		y = (a1*c2-c1*a2)/(a1*b2-a2*b1);
                		x = (c1*b2-c2*b1)/(a1*b2-a2*b1);
                		
                		x=x+centerX;
                		y=y+centerY;
               
                			if((x==bottomrightX&&y==bottomrightY)||(x==topleftX&&y==topleftY))
                				{
                            	System.out.println("Nothing");
                				}
                			else
                			{
                				tmpLx=x;
                				tmpLy=y;
                			}
                      	  // intersection of h2 line ,v1 line
                			tt1=horizontalLine[1][0]*thetaStep;
                    		tt2=verticalLine[0][0]*thetaStep;//angle2
                  
                    		a1=Math.cos(tt1);
                    		b1=Math.sin(tt1);
                    		a2=Math.cos(tt2);
                    		b2=Math.sin(tt2);
                    		c1=horizontalLine[1][1]-houghHeight;
                    		c2=verticalLine[0][1]-houghHeight;
                    		y = (a1*c2-c1*a2)/(a1*b2-a2*b1);
                    		x = (c1*b2-c2*b1)/(a1*b2-a2*b1);
                    		
                    		x=x+centerX;
                    		y=y+centerY;
                   
                    			if((x==bottomrightX&&y==bottomrightY)||(x==topleftX&&y==topleftY))
                    				{
                                	System.out.println("Nothing");

                    				}
                    			else
                    			{
                    				tmpRx=x;
                    				tmpRy=y;
                    			}
                    			
                          	  // intersection of h2 line ,v2 line	
                    			tt1=horizontalLine[1][0]*thetaStep;
                        		tt2=verticalLine[1][0]*thetaStep;//angle2
                      
                        		a1=Math.cos(tt1);
                        		b1=Math.sin(tt1);
                        		a2=Math.cos(tt2);
                        		b2=Math.sin(tt2);
                        		c1=horizontalLine[1][1]-houghHeight;
                        		c2=verticalLine[1][1]-houghHeight;
                        		y = (a1*c2-c1*a2)/(a1*b2-a2*b1);
                        		x = (c1*b2-c2*b1)/(a1*b2-a2*b1);
                        		
                        		x=x+centerX;
                        		y=y+centerY;
                       
                        			if((x==bottomrightX&&y==bottomrightY)||(x==topleftX&&y==topleftY))
                        				{
                                    	System.out.println("Nothing");
                      	
                        				}
                        			else
                        			{
                        				tmpRx=x;
                        				tmpRy=y;
                        			}
                    
            
                  }
                  
                  
                 if(tmpLx<tmpRx)
                 {
                	 bottomleftX=tmpLx;
                	 bottomleftY=tmpLy;
                	 toprightX=tmpRx;
                	 toprightY=tmpRy;
                 }
                 else
                 {
                	 bottomleftX=tmpRx;
                	 bottomleftY=tmpRy;
                	 toprightX=tmpLx;
                	 toprightY=tmpLy;
                 }
                */
                
                
                
                 // 3rd method to find corner is to see distance from origin i.e r
                 //eg t,r //origin at centre
                 //179,334 leftmost line
                 //179,154 rightmost
                 
                 //90,150 topmost
                 //90,332 bottom most line
                 
                 
            	System.out.println("\n"+width+" "+height+"\n");
              
           
            	/*
            	//topleft and bottomright  corners are alredy computed
            	//line
            	//l1 -> l3,l4
            	//l2 -> l3,l4
            	
            	for(int i=0;i<lineNo;i++){
                
                	if(line1_t==linesCoordinates[2][i]&&line1_r==linesCoordinates[3][i])
                	{
                		
                		// only one out of line3 and line4 will satisfy the condition
                		//other will never intersect in image
                      if(line3_t==linesCoordinates[4][i]&&line3_r==linesCoordinates[5][i])
                     	{
                     		//assume bottomleftX
                     			bottomleftX=(int)linesCoordinates[0][i];
                     			bottomleftY=(int)linesCoordinates[1][i];
                     	
                     	}
                     if(line4_t==linesCoordinates[4][i]&&line4_r==linesCoordinates[5][i])
                  		{
                  			//line3_t is near 90 degree
                  			bottomleftX=(int)linesCoordinates[0][i];
                  			bottomleftY=(int)linesCoordinates[1][i];
                  			
                  			
                  			
                  	   }
                     
                     
                	}//closing outer if
                	
                	
                	
                	if(line1_t==linesCoordinates[4][i]&&line1_r==linesCoordinates[5][i])
                	{
                     if(line3_t==linesCoordinates[2][i]&&line3_r==linesCoordinates[3][i])
                     	{
                     		
                     			bottomleftX=(int)linesCoordinates[0][i];
                     			bottomleftY=(int)linesCoordinates[1][i];
                     	  		
                     			
                     	}
                     if(line4_t==linesCoordinates[2][i]&&line4_r==linesCoordinates[3][i])
                  		{
                  		
                  			bottomleftX=(int)linesCoordinates[0][i];
                  			bottomleftY=(int)linesCoordinates[1][i];
                  		}
                  		
                  			
                  	
                	}
                	
                	
                	
                	

                	if(line2_t==linesCoordinates[2][i]&&line2_r==linesCoordinates[3][i])
                	{
                     if(line3_t==linesCoordinates[4][i]&&line3_r==linesCoordinates[5][i])
                     	{
                     		
                     			toprightX=(int)linesCoordinates[0][i];
                     			toprightY=(int)linesCoordinates[1][i];
                     	  		
                     			
                     	}
                     if(line4_t==linesCoordinates[4][i]&&line4_r==linesCoordinates[5][i])
                  		{
                  		
                    	 toprightX=(int)linesCoordinates[0][i];
                    	 toprightY=(int)linesCoordinates[1][i];
                  		}
                  		
                  			
                  	
                	}
                	
                	
                	if(line2_t==linesCoordinates[4][i]&&line2_r==linesCoordinates[5][i])
                	{
                     if(line3_t==linesCoordinates[2][i]&&line3_r==linesCoordinates[3][i])
                     	{
                     		
                     			toprightX=(int)linesCoordinates[0][i];
                     			toprightY=(int)linesCoordinates[1][i];
                     	  		
                     			
                     	}
                     if(line4_t==linesCoordinates[2][i]&&line4_r==linesCoordinates[3][i])
                  		{
                  		
                    	 toprightX=(int)linesCoordinates[0][i];
                    	 toprightY=(int)linesCoordinates[1][i];
                  		}
                  		
                  			
                  	
                	}
                	
                	
                	
                	
                	
            	}
            	*/	
                /*	System.out.print((int)linesCoordinates[0][i]+" "+(int)linesCoordinates[1][i]+" ");
                	System.out.print((int)linesCoordinates[2][i]+" "+(int)linesCoordinates[3][i]+" ");
                	System.out.println((int)linesCoordinates[4][i]+" "+(int)linesCoordinates[5][i]+" "+linesCoordinates[6][i]);
				*/
                	
                
            	
            	
                /*
                int tmpx=0,tmpy=0;
                if(toprightX<bottomleftX)
                {
                	tmpx=toprightX;
                	tmpy=toprightY;
                	toprightX=bottomleftX;
                	toprightY=bottomrightY;
                	bottomleftX=tmpx;
                	bottomleftY=tmpy;             	
                	
                }*/
            
            	
            	  /*if(bottomleftX<0||bottomleftX>width||bottomleftY<0||bottomleftY>height)
                  {
                  	System.out.print("Algofail");
                  	bottomleftX=topleftX;
                  	bottomleftY=bottomrightY;
                  }
                  if(toprightX<0||toprightX>width||toprightY<0||toprightY>height)
                  {   System.out.print("Algofail");
                  	toprightX=bottomrightX;
                  	toprightY=topleftY;
                  }*/
            	
            /*	
             *  this method failed on sudoku0
            	toprightX=bottomrightX;
            	toprightY=topleftY;
            	bottomleftX=topleftX;
            	bottomleftY=bottomrightY;
            */
                  
            	
            	System.out.print("\n Topleft corner"+topleftX+" "+topleftY+" ");
            	System.out.print("\n Topright corner"+toprightX+" "+toprightY+" ");
            	System.out.print("\n BottomLeft corner"+bottomleftX+" "+bottomleftY+" ");
            	System.out.print("\n BottomRight corner"+bottomrightX+" "+bottomrightY+" ");

            	//do one more improvement 
            	// check whether the 4 corners are actually forming the square
            	//cal distances
            	
            	
                
                return lines;
        }

        public void saveImage(){
        
    		File fl = new File("houghSudokuStraightLine.png");
    		try{
    		ImageIO.write(image, "png", fl);
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}
        }
        
        public int[][] perspectiveMapping(BufferedImage im){
//      PerspectiveTransformation ptobj = new PerspectiveTransformation(image, topleftX, topleftY, toprightX, toprightY, bottomrightX, bottomrightY, bottomleftX, bottomleftY);
        	
        	/*File fl = new File("bugkaha_hn.jpg");
    		try{
    		ImageIO.write(im, "jpg", fl);
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}
    		*/
    		
        	int newGridDim=486;//567*567
        	int oneCellDim=newGridDim/9;//63
        	
        	BufferedImage gridimg=null;
        	
        	try{
        	BufferedImage imgg = im;//ImageIO.read(new File("BlacknWhiteSudoku10.jpg")); //567=81*7
//PerspectiveTransformation ptobj = new PerspectiveTransformation(image, bottomrightX, bottomrightY, toprightX, toprightY,topleftX, topleftY, bottomleftX, bottomleftY);
        	
        	/*if(newGridDim >imgg.getHeight()&&newGridDim>imgg.getWidth())
        	{
        		newGridDim = 486;//Math.max(imgg.getHeight(),imgg.getWidth());
        		oneCellDim=newGridDim/9;
        				System.out.println(newGridDim+" Dim , cell "+oneCellDim);
        	}	
        	if(newGridDim >imgg.getHeight()&&newGridDim>imgg.getWidth())
        	{
        		newGridDim = 405;//Math.max(imgg.getHeight(),imgg.getWidth());
        		oneCellDim=newGridDim/9;
        				System.out.println(newGridDim+" Dim , cell "+oneCellDim);
        	}
        	*/
        	
        	PerspectiveTransformation ptobj = new PerspectiveTransformation(imgg,newGridDim,topleftX, topleftY, toprightX, toprightY,bottomrightX, bottomrightY,bottomleftX, bottomleftY);
        	gridimg =  ptobj.transform();
        	    
        	//        		  		 removing boundary of grid using floodfill 
        	   // Thresholding th = new Thresholding(gridimg);
	  			//BufferedImage binaryImg = th.ConvertToBinaryImg();
	  			ExtractDigit_FloodFill fobj = new ExtractDigit_FloodFill(gridimg,100,101);

	  			gridimg=fobj.fillBoundary();
	  			//fobj.RegionLabeling();
        	 
        	    
        	}
        	catch(Exception e){
        		e.printStackTrace();
        	}
        	int wd = gridimg.getWidth();
        	int ht = gridimg.getHeight();
        	
       /* 	
       	   PerspectiveTransformation digit = new PerspectiveTransformation(gridimg, 60, 3*(wd/9), 0,   4*(wd/9),0,  
						   4*(wd/9),ht/9    ,	3*(wd/9),(ht/9));
      	
        	BufferedImage four = digit.transform();

	  		Thresholding th = new Thresholding(four);
	  			BufferedImage binaryImg = th.ConvertToBinaryImg();
	  			ExtractDigit_FloodFill fobj = new ExtractDigit_FloodFill(binaryImg);

	  			fobj.fillBoundary();
	  			fobj.RegionLabeling();
       */

        	
        	test4 tobj = new test4();
        	ReduceSize robj = new ReduceSize();	  	

        	int arr[][] = new int[9][9];
        	//int xx=0,yy=0;
        	for(int i=0;i<9;i++){
        	for(int j=0;j<9;j++){
        		
        		 double topleftx=i*(wd/9),toplefty=j*(ht/9);
                 double toprightx=i*(wd/9)+wd/9,toprighty=j*(ht/9);
                 double bottomleftx=i*(wd/9),bottomlefty=(j*(ht/9)+ht/9);
                 double bottomrightx=(i*(wd/9)+wd/9),bottomrighty=(j*(ht/9)+ht/9);
                 
                 
                 if(i==0||j==0)
                 {
                	 if(topleftx!=0)
                		 topleftx--;
                	 if(toplefty!=0)
                     	toplefty--;
                	 if(toprighty!=0)
                		 toprighty--;
                	 if(toprightx!=0)
                		 toprightx--;
                	 if(bottomrightx!=0)
                		 bottomrightx--;
                	 if(bottomrighty!=0)
                		 bottomrighty--;
                	 if(bottomlefty!=0)
                		 bottomlefty--;
                	 if(bottomleftx!=0)
                		 bottomleftx--; 
                	 
                 }
                 else
                 {
                		 topleftx--;
                		 toplefty--;
                		 toprighty--;
                		 toprightx--;
                		 bottomrightx--;
                		 bottomrighty--;
                		 bottomlefty--;
                		 bottomleftx--; 
                	  
                 }
                 
        		
        		
        		
        		
        		
        		
        		
        		 // PerspectiveTransformation digit = new PerspectiveTransformation(gridimg,63, i*(wd/9), j*(ht/9),   i*(wd/9)+wd/9,j*(ht/9),  
        			//	     	i*(wd/9)+wd/9,j*(ht/9)+ht/9   , i*(wd/9),j*(ht/9)+ht/9);
              	PerspectiveTransformation digit = new PerspectiveTransformation(gridimg,oneCellDim,topleftx, toplefty, toprightx, toprighty,bottomrightx, bottomrighty,bottomleftx, bottomlefty);
                 
                 BufferedImage four = digit.transform();

        		  		    //no need to do it
        		  			ExtractDigit_FloodFill fobj = new ExtractDigit_FloodFill(four,i,j);
                 		   
        					fobj.fillBoundary();
        		  	four=fobj.RegionLabeling();
        		  	 //Thresholding th = new Thresholding(four);
 		  			//four = th.AdaptiveThresholding();

        			
        			
        		  	if(four!=null)
        		  			{   
        //four=robj.scaleImage(four, BufferedImage.TYPE_INT_RGB, 30,30,30/(double)54, 30/(double)54);
        		  			/*try{
                				File output = new File("digitss/"+i+","+j+"c.png");
                				ImageIO.write(four,"png",output);
                				}
                				catch(IOException e){
                					e.printStackTrace();
                				}
        		  			*/
        		  		
    		  		    arr[j][i]=tobj.testocr(four);
        		  			}
                 
        	}
        	
        	
        }
        	
        	
        	for(int i=0;i<9;i++){
        		
        	
        		for(int j=0;j<9;j++)
        			System.out.print(arr[i][j]+" ");
    			System.out.println();
        	}
        	
        	return arr;
        	
        	//return gridimg;//after perspective mapping
  	
        	
        	/*
       for(int i=0;i<9;i++){
       	for(int j=0;j<9;j++){
       		
       	 double topleftx=i*(wd/9),toplefty=j*(ht/9);
         double toprightx=i*(wd/9)+wd/9,toprighty=j*(ht/9);
         double bottomleftx=i*(wd/9),bottomlefty=(j*(ht/9)+ht/9);
         double bottomrightx=(i*(wd/9)+wd/9),bottomrighty=(j*(ht/9)+ht/9);
         
         
         if(i==0||j==0)
         {
        	 if(topleftx!=0)
        		 topleftx--;
        	 if(toplefty!=0)
             	toplefty--;
        	 if(toprighty!=0)
        		 toprighty--;
        	 if(toprightx!=0)
        		 toprightx--;
        	 if(bottomrightx!=0)
        		 bottomrightx--;
        	 if(bottomrighty!=0)
        		 bottomrighty--;
        	 if(bottomlefty!=0)
        		 bottomlefty--;
        	 if(bottomleftx!=0)
        		 bottomleftx--; 
        	 
         }
         else
         {
        		 topleftx--;
        		 toplefty--;
        		 toprighty--;
        		 toprightx--;
        		 bottomrightx--;
        		 bottomrighty--;
        		 bottomlefty--;
        		 bottomleftx--; 
        	  
         }
         
         
     		  System.out.println(i+","+j);
       		  System.out.println(topleftx+"," +toplefty);
       		  System.out.println(toprightx+","+toprighty);
       		  System.out.println(bottomleftx+","+bottomlefty);
       		  System.out.println(  bottomrightx+","+bottomrighty);
       		  
       	}
 		  System.out.println(); 		  System.out.println();


       }
        	 	*/
       
        	
        }
        
        
        
        /**
         * Gets the highest value in the hough array
         */
        public int getHighestValue() {
                int max = 0;
                for (int t = 0; t < maxTheta; t++) {
                        for (int r = 0; r < doubleHeight; r++) {
                                if (houghArray[t][r] > max) {
                                        max = houghArray[t][r];
                                }
                        }
                }
                return max;
        }

        /**
         * Gets the hough array as an image, in case you want to have a look at it.
         */
        public BufferedImage getHoughArrayImage() {
                int max = getHighestValue();
                BufferedImage imag = new BufferedImage(maxTheta, doubleHeight,
                                BufferedImage.TYPE_INT_ARGB);
                for (int t = 0; t < maxTheta; t++) {
                        for (int r = 0; r < doubleHeight; r++) {
                                double value = 255 * ((double) houghArray[t][r]) / max;
                                int v = 255 - (int) value;
                                int c = new Color(v, v, v).getRGB();
                                imag.setRGB(t, r, c);
                        }
                }
                
                     
                
                
                
                return imag;
        }

}