import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//backtracking Algo geeksforgeeks

public class SolveSudoku{
	
	BufferedImage image;
	int[][] sudoku;
	int[][] tmp;

	public SolveSudoku(int[][] sud,BufferedImage img){
		sudoku = new int[9][9];
		tmp = new int[9][9];

		try{
			image = ImageIO.read(new File("PerspTransform.png"));
		
		}
		catch(IOException ew){
			ew.printStackTrace();
		}
		
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				
					sudoku[i][j]=sud[j][i];
					System.out.print(sudoku[i][j]+" ");
					if(sudoku[i][j]!=0)
						tmp[i][j]=1;
					else
						tmp[i][j]=0;
			}
			System.out.println();
		}
		

		//image=img;
	}
	
	public boolean solve(){
		
		Point pt = FindUnassignedLocation();
		if(pt==null)//sudoku completely filled
			return true;
		int row = pt.x;
		int col = pt.y;
		
		for(int num=1;num<=9;num++)//considering all possible digits
		{
			
			if(isSafe(row,col,num))// checking if digit can be placed in that location
			{
			
				//make tentative assignment
				sudoku[row][col]=num;
				if(solve())// return true if success
					return true;
				//failure, remove assignment, try again with next digit
				sudoku[row][col]=0;
			}
			
		}
		
		return false;//triggers backtracking
		
	}
	public Point FindUnassignedLocation(){
		
		for(int row=0;row<9;row++){
			
			for(int col=0;col<9;col++){
				if(sudoku[row][col]==0)
					return new Point(row,col);
			}
		}
		return null;
	}
	public boolean isSafe(int row,int col,int num){
		
		//Check if 'num' is not already placed in current row,
	      // current column and current 3x3 box */
		
		for(int i=0;i<9;i++){
			if(sudoku[row][i]==num)
				return false;
		}
		
		for(int i=0;i<9;i++){
			if(sudoku[i][col]==num)
				return false;
		}
		
		int boxStartRow = row - row%3;
		int boxStartCol = col - col%3;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(sudoku[i+boxStartRow][j+boxStartCol]==num)
					return false;
			}	
		}
			
		return true;
		
		
	}
	public int[][] display(){
		int wd = image.getWidth();
		int ht = image.getHeight();
		Graphics g = image.getGraphics();
		g.setFont(g.getFont().deriveFont(30f));
		g.setColor(Color.GREEN);
		
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				
				if(tmp[i][j]==0)
				g.drawString(String.valueOf(sudoku[i][j]), i*(wd/9)+5, j*(ht/9)+ht/9);
					System.out.print(sudoku[i][j]+" ");
					
			}
			System.out.println();
		}
		
		try{
			File soln = new File("solution.png");
			ImageIO.write(image,"png",soln);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
	return sudoku;
	}

	
}
