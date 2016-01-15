import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;


public class FloodFill_BFS {

	int width,height;
	BufferedImage image;
	int label,totalPtsInARegion,label_max,maxRegion;
	int[][] labels;
	boolean[][] visited;
	//label_ max  to keep the label of the largest region
//maxRegion  to help to identify the largest region

	public FloodFill_BFS(BufferedImage img){
		
	 width = img.getWidth();
	 height=img.getHeight();
	 image=img;
	 label=2;
	 visited = new boolean[width+7][height+7];
	 labels = new int[width+7][height+7];
	 for(int i=0;i<width;i++)
		 for(int j=0;j<height;j++)
			 {
			 visited[i][j]=false;
			 if(image.getRGB(i, j)==Color.BLACK.getRGB())
				 labels[i][j]=1;
			 else
				 labels[i][j]=0;
			 }
	 
	 
	 
	 //0=background,1=foreground
	
	 
	 
	 
	 //	Point src_pixel = new Point(width/2,height/2);
	}
	public BufferedImage RegionLabeling(){
		 //System.out.println("r");
		maxRegion=0;
		
		for(int i=0;i<width;i++){
			 for(int j=0;j<height;j++){
				 //Color c = new Color(image.getRGB(i, j));
				 if(i==0&&j==0)
				 System.out.println(image.getRGB(i,j)+" "+Color.BLACK.getRGB());

			//	 if(image.getRGB(i,j)==Color.BLACK.getRGB())
				if(labels[i][j]==1)
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
		System.out.println("Size of Img ="+size);
		System.out.println("Total Regions ="+label);
		System.out.println("label of maxRegion ="+label_max);
		System.out.println("No of pixels in maxRegion ="+maxRegion);
		/*
		 * how to set pixel of an image to an integer?
		System.out.println(image.getRGB(5, 5));
		System.out.println(Color.BLACK.getRGB()+5);
		image.setRGB(5, 5, Color.BLACK.getRGB()+5);
		System.out.println(image.getRGB(5, 5));
		*/
		
			
		return image;
	}
	public void findLargestRegion(){
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				
				//if(image.getRGB(i, j)==Color.BLACK.getRGB()+label_max)
				if(labels[i][j]==label_max)
				{	//System.out.println("s");
					image.setRGB(i, j, Color.BLACK.getRGB());
				}
				else
				{
					image.setRGB(i, j, Color.WHITE.getRGB());
				}
				
				
			}
		}
	
		
	}
	
	public void floodfill(int x,int y,int labell){
		LinkedList<Point> queue = new LinkedList<Point>();
		queue.addFirst(new Point(x,y));
		visited[x][y]=true;
		while(!queue.isEmpty()){
			//System.out.println("r");

			Point pixel = queue.removeLast();
			int a = pixel.x;
			int b= pixel.y;
			if(a>=0&&a<width&&b>=0&&b<height
					&&image.getRGB(a,b)==Color.BLACK.getRGB())
			{
				
				//System.out.print(totalPtsInARegion+" ");
				totalPtsInARegion++;
				//image.setRGB(a, b,Color.BLACK.getRGB()+labell);
				labels[a][b]=labell;
				
				if(a+1<width&&visited[a+1][b]==false)
				{
				queue.addFirst(new Point(a+1,b));//east
				visited[a+1][b]=true;
				}
				if(a-1>=0&&visited[a-1][b]==false)
				{
					queue.addFirst(new Point(a-1,b));//west
					visited[a-1][b]=true;

				}
				if(b+1<height&&visited[a][b+1]==false)
				{
					queue.addFirst(new Point(a,b+1));//nort
					visited[a][b+1]=true;
				}
				if(b-1>=0&&visited[a][b-1]==false)
				{
					queue.addFirst(new Point(a,b-1));//south
					visited[a][b-1]=true;
				}
				if(a+1<width&&b+1<height&&visited[a+1][b+1]==false)
				{
					queue.addFirst(new Point(a+1,b+1));//NE
					visited[a+1][b+1]=true;
				}
				if(a+1<width&&b-1>=0&&visited[a+1][b-1]==false)
				{
					queue.addFirst(new Point(a+1,b-1));//SE
					visited[a+1][b-1]=true;
				}
				if(a-1>=0&&b+1<height&&visited[a-1][b+1]==false)
				{
					queue.addFirst(new Point(a-1,b+1));//NW
					visited[a-1][b+1]=true;
				}
				if(a-1>=0&&b-1>=0&&visited[a-1][b-1]==false)
				{
					queue.addFirst(new Point(a-1,b-1));//SW
					visited[a-1][b-1]=true;
				}
				

			}
			
		}
		
		
	}
	
}
