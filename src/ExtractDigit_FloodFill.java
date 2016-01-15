import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;


public class ExtractDigit_FloodFill {

	int width,height;
	BufferedImage image;
	int label,totalPtsInARegion,label_max,maxRegion;
	int ii,jj;
	int xmin,xmax,ymin,ymax;//boundingbox;
	 ReduceSize rsobj ;
	//label_ max  to keep the label of the largest region
//maxRegion  to help to identify the largest region
	 int center;
	 int[][] visited,copy;
	public ExtractDigit_FloodFill(BufferedImage img,int i1,int j1){
		
	 width = img.getWidth();
	 height=img.getHeight();
	 image=img;
	 xmax=0;xmin=width-1;
	 ymax=0;ymin=height-1;
	 label=2;
	 ii=i1;//for giving unique names to images
	 jj=j1;
	 
	 //0=background,1=foreground
	 center =width/2;
	 visited=new int[width+7][height+7];
	 copy=new int[width+7][height+7];
	 for(int i=0;i<width;i++)
		 for(int j=0;j<height;j++)
		 {	 copy[i][j]=img.getRGB(i, j);
			 visited[i][j]=0;
		 }
	 int fg=0;
	
	  rsobj = new ReduceSize();

		/*for(int i=center-10;i<center+15;i++)
			 for(int j=center-10;j<center+15;j++)
				 image.setRGB(i, j, Color.green.getRGB());
			*/
	 /* 
	 try{
			File output = new File("digitss/"+ii+","+jj+"i.png");
			ImageIO.write(image,"png",output);
			}
			catch(IOException e){
				e.printStackTrace();
			}
	 */
	  
	 if(i1!=100&&j1!=101)
	 {
	 for(int i=center+10;i>center-10;i--)
		 {
		 for(int j=center-10;j<center+15;j++)
		 
			 if(image.getRGB(i, j)== Color.BLACK.getRGB())
			 {
				 fg=1;
				 isSafeForFloodFilling(i, j);
				 break;
			 }
		 
		 if(fg==1)
			 break;
		 }
	 }
		
	 
	
	 
	 //	Point src_pixel = new Point(width/2,height/2);
	}
	
	
	public BufferedImage fillBoundary(){
		
		//checking black boundary at top
		for(int i=0;i<width;i++){
			if(i>center-20&&i<center+20)
				continue;
			 for(int j=0;j<3;j++){
				 if(image.getRGB(i,j)==Color.BLACK.getRGB())
				 {
						//System.out.println("sghd");
					 //if(isSafeForFloodFilling(i, j))//20*20 region in middle may contain part of digit
					 floodFillBoundary(i, j);
					 
				 }
			 }
		}
		
		//checking black boundary at left
				for(int i=0;i<3;i++){
					 for(int j=0;j<height;j++){
						 if(image.getRGB(i,j)==Color.BLACK.getRGB())
						 {
							 //if(isSafeForFloodFilling(i, j))
							 floodFillBoundary(i, j);
							 
						 }
					 }
				}
				
				
				//checking black boundary at right
				for(int i=width-1;i>width-3;i--){
					 for(int j=0;j<height;j++){
						 if(image.getRGB(i,j)==Color.BLACK.getRGB())
						 {
							 //if(isSafeForFloodFilling(i, j))
							 floodFillBoundary(i, j);
							 
						 }
					 }
				}
				
				//checking black boundary at bottom
				for(int i=0;i<width;i++){
					if(i>center-20&&i<center+20)
						continue; 
					for(int j=height-1;j>height-3;j--){
						 if(image.getRGB(i,j)==Color.BLACK.getRGB())
						 {
							// if(isSafeForFloodFilling(i, j))
							 floodFillBoundary(i, j);
							 
						 }
					 }
				}
				
				
				 for(int i=0;i<width;i++)
					 for(int j=0;j<height;j++)
						 if(visited[i][j]==1)
				              image.setRGB(i, j, Color.BLACK.getRGB());			 
			
				/*
				if(ii==100&&jj==100){
				try{
					File output = new File("digitss/"+ii+","+jj+".png");
					ImageIO.write(image,"png",output);
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
				*/
				 
				
		return image;
	}
	public void floodFillBoundary(int x,int y){
		LinkedList<Point> queue = new LinkedList<Point>();
		queue.addFirst(new Point(x,y));
		while(!queue.isEmpty()){
			// System.out.println("r");

			Point pixel = queue.removeLast();
			int a = pixel.x;
			int b= pixel.y;
			
			
			
			if(a>=0&&a<width&&b>=0&&b<height
					&&image.getRGB(a,b)==Color.BLACK.getRGB())
			{
				//System.out.print(totalPtsInARegion+" ");
				totalPtsInARegion++;
				image.setRGB(a, b,Color.WHITE.getRGB());
				queue.addFirst(new Point(a+1,b));//east
				queue.addFirst(new Point(a-1,b));//west
				queue.addFirst(new Point(a,b+1));//nort
				queue.addFirst(new Point(a,b-1));//south
				queue.addFirst(new Point(a+1,b+1));//NE
				queue.addFirst(new Point(a+1,b-1));//SE
				queue.addFirst(new Point(a-1,b+1));//NW
				queue.addFirst(new Point(a-1,b-1));//SW

			}
			
		}
		
	}
	public boolean isSafeForFloodFilling(int x,int y){
		LinkedList<Point> queue = new LinkedList<Point>();
		queue.addFirst(new Point(x,y));
		while(!queue.isEmpty()){
			//System.out.println("r");

			Point pixel = queue.removeLast();
			int a = pixel.x;
			int b= pixel.y;
			
	//	if(a>center-10&&a<center+10&&b>center-10&&b<center+10)
		//	{
			//System.out.println("r");
			//return false;
			//}
				
			if(a>=0&&a<width&&b>=0&&b<height
					&&image.getRGB(a,b)==Color.BLACK.getRGB())
			{
				//System.out.print(totalPtsInARegion+" ");
				//totalPtsInARegion++;
				image.setRGB(a, b,Color.WHITE.getRGB());
				
				visited[a][b]=1;
				
				
				queue.addFirst(new Point(a+1,b));//east
				queue.addFirst(new Point(a-1,b));//west
				queue.addFirst(new Point(a,b+1));//nort
				queue.addFirst(new Point(a,b-1));//south
				queue.addFirst(new Point(a+1,b+1));//NE
				queue.addFirst(new Point(a+1,b-1));//SE
				queue.addFirst(new Point(a-1,b+1));//NW
				queue.addFirst(new Point(a-1,b-1));//SW

			}
			
		}
		return true;
		
	}
	
	public BufferedImage RegionLabeling(){
		 //System.out.println("r");
		maxRegion=0;
		
		for(int i=0;i<width;i++){
			 for(int j=0;j<height;j++){
				 //Color c = new Color(image.getRGB(i, j));
				// if(i==0&&j==0)
				 //System.out.println(image.getRGB(i,j)+" "+Color.BLACK.getRGB());

				 if(image.getRGB(i,j)==Color.BLACK.getRGB())
				 {
					 //System.out.println("b");
					 totalPtsInARegion=0;
				 floodfill(i, j, label);
				 	if(totalPtsInARegion>maxRegion)
				 	{
				 		label_max=label;
				 		maxRegion=totalPtsInARegion;
				 	}
				label++;//next label for next blob
				 
				 }
			 }
		 }
		int size = width*height;
		
		findLargestRegion();
		
	/*//bounding box of digits	
		for(int i=xmin;i<xmax;i++)
				 image.setRGB(i, ymin, Color.green.getRGB());
		for(int i=xmin;i<xmax;i++)
			 image.setRGB(i, ymax, Color.green.getRGB());
		for(int i=ymin;i<ymax;i++)
			 image.setRGB(xmin, i, Color.green.getRGB());
		for(int i=ymin;i<ymax;i++)
			 image.setRGB(xmax, i, Color.green.getRGB());
	/*	for(int i=xmin;i<xmax;i++)
			 for(int j=ymin;j<ymax;j++)
				 image.setRGB(i, j, Color.green.getRGB());
		*/
		
		int d1=xmax-xmin+1,d2=ymax-ymin+1;
		System.out.println(xmin+" "+xmax+" "+ymin+" "+ymax+" "+d1+" "+d2);
		System.out.println("Size of Img ="+size);
		System.out.println("Total Regions ="+label);
		System.out.println("label of maxRegion ="+label_max);
		System.out.println("No of pixels in maxRegion ="+maxRegion);
		
		
		/*
		try{
			File output = new File("digitss/"+ii+","+jj+"b.png");
			ImageIO.write(image,"png",output);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		*/
		
		
		int threshold=30;//pixels
		if(maxRegion<30||d2<13)
		return null;
		
		BufferedImage center = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int cx=width/2;
		int cy=height/2;
		int boxx=(xmax-xmin+1)/2;
		int boxy=(ymax-ymin+1)/2;
		
		
		
		//int px = cx-boxx;
		//int py = cy-boxy;
		/*
		BufferedImage boundingBx = new BufferedImage(d1+1, d2+1, BufferedImage.TYPE_INT_RGB);

		for(int i=xmin,px=0;i<=xmax;i++,px++)
			for(int j=ymin,py=0;j<=ymax;j++,py++)
				{
				boundingBx.setRGB(px, py, image.getRGB(i,j));
				}
		try{
			File output = new File("digitss/"+ii+","+jj+"bb.jpg");
			ImageIO.write(boundingBx,"jpg",output);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		*/
		for(int i=xmin;i<=xmax;i++)
			for(int j=ymin;j<=ymax;j++)
			{   //include (all digit parts)present in bondingbox
				image.setRGB(i, j, copy[i][j]);
			}
		
		
		if(ymin==0||ymax==height-1)
		{
			int fg=0,a=xmin,b=xmax;
			for(int i=xmin;i<=xmax;i++)
			{
				if(image.getRGB(i, ymin)==Color.WHITE.getRGB())
					a=i;
				else 
					break;
			}
			for(int i=xmax;i>=xmin;i--)
			{
				if(image.getRGB(i, ymin)==Color.WHITE.getRGB())
					b=i;
				else 
					break;
			}
			for(int i=a+1;i<b;i++)
				image.setRGB(i, ymin, Color.BLACK.getRGB());
		}
		
		
		
		
		/*if(ymax==height-1)
		{
			for(int i=xmin;i<=xmax;i++)
				image.setRGB(i, ymax, Color.BLACK.getRGB());
		}
		*/
		
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				center.setRGB(i, j, Color.WHITE.getRGB());
		//for(int k=0;k<5;k++)
		//{
			
		
		for(int i=xmin-2,px=cx-boxx;i<xmax+2;i++,px++)
		{
			for(int j=ymin-2,py=cy-boxy;j<ymax+2;j++,py++)
		
				 {
				// System.out.println(i+" "+j+" "+px+" "+py);
				 if(i<0||i>=width||j<0||j>=height||px<0||px>=width||py<0||py>=height)
					 continue;
				 center.setRGB(px, py, image.getRGB(i,j));
				 }
		
		}
		
		
		
		
		//cx=cx+width;
		//}
			//ReduceSize robj = new ReduceSize();
  		//center=robj.scaleImage(center, BufferedImage.TYPE_INT_RGB, 100*5, 100, (100*5)/(double)(54*5), 100/(double)54);

		
		
		try{
			File output = new File("digitss/"+ii+","+jj+"centre.png");
			ImageIO.write(center,"png",output);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		
		/*
		 * how to set pixel of an image to an integer?
		System.out.println(image.getRGB(5, 5));
		System.out.println(Color.BLACK.getRGB()+5);
		image.setRGB(5, 5, Color.BLACK.getRGB()+5);
		System.out.println(image.getRGB(5, 5));
		*/
		
		
		
		return center;
	}
	public void findLargestRegion(){
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				
				if(image.getRGB(i, j)==Color.BLACK.getRGB()+label_max)
				{	//System.out.println("s");
					if(xmin>i)
					    xmin=i;
					if(xmax<i)
						xmax=i;
					if(ymin>j)
						ymin=j;
					if(ymax<j)
						ymax=j;
					image.setRGB(i, j, Color.BLACK.getRGB());
				}
				else
				{
					image.setRGB(i, j, Color.WHITE.getRGB());
				}
				
				
			}
		}
	
		
		/*try{
		File output = new File("digitss/"+ii+","+jj+".png");
       // image=rsobj.scale(image, 40, 40);

		ImageIO.write(image,"png",output);
		}
		catch(IOException e){
			e.printStackTrace();
		}*/
		
	}
	
	
	
	public void floodfill(int x,int y,int labell){
		LinkedList<Point> queue = new LinkedList<Point>();
		queue.addFirst(new Point(x,y));
		while(!queue.isEmpty()){
			// System.out.println("r");

			Point pixel = queue.removeLast();
			int a = pixel.x;
			int b= pixel.y;
			
			
			if(a>=0&&a<width&&b>=0&&b<height
					&&image.getRGB(a,b)==Color.BLACK.getRGB())
			{
				//System.out.print(totalPtsInARegion+" ");
				totalPtsInARegion++;
				image.setRGB(a, b,Color.BLACK.getRGB()+labell);
				queue.addFirst(new Point(a+1,b));//east
				queue.addFirst(new Point(a-1,b));//west
				queue.addFirst(new Point(a,b+1));//nort
				queue.addFirst(new Point(a,b-1));//south
				queue.addFirst(new Point(a+1,b+1));//NE
				queue.addFirst(new Point(a+1,b-1));//SE
				queue.addFirst(new Point(a-1,b+1));//NW
				queue.addFirst(new Point(a-1,b-1));//SW

			}
			
		}
		
		
	}
	
}
