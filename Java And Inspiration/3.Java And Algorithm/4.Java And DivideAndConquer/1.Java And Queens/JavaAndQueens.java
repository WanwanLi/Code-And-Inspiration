import java.io.*;
public class JavaAndQueens
{
	public static void main(String[] args)
	{
		Queens Queens8=new Queens();
		Queens8.showResult();
	}
}
class Queens
{
	char[][] chess;
	int[] r=new int[8];
	public Queens()
	{
		chess=new char[][]
		{
			{'.',' ','.',' ','.',' ','.',' '},
			{' ','.',' ','.',' ','.',' ','.'},
			{'.',' ','.',' ','.',' ','.',' '},
			{' ','.',' ','.',' ','.',' ','.'},
			{'.',' ','.',' ','.',' ','.',' '},
			{' ','.',' ','.',' ','.',' ','.'},
			{'.',' ','.',' ','.',' ','.',' '},
			{' ','.',' ','.',' ','.',' ','.'}
		};	
	}
	private int getAbsoluteValueOf(int k)
	{
		return (k<0?-k:k);		
	}
	private boolean canPlaceQueenAt(int i,int j)
	{
		for(int c=0;c<j;c++)if(i==r[c]||(j-c)==this.getAbsoluteValueOf(i-r[c]))return false;
		return true;
	}
	private void placeTheQueenAt(int j)
	{
		if(j>=8)this.showAllQueens();
		else
		{
			for(int i=0;i<8;i++)
			{
				if(canPlaceQueenAt(i,j))
				{
					r[j]=i;
					placeTheQueenAt(j+1);
				}
			}
		}
	}
	private void showAllQueens()
	{
		String s="Solve 8 Queens Problem:\n\n ";
		for(int i=0;i<8;i++)s+=" "+i;
		s+="\n";
		for(int i=0;i<8;i++)
		{
			s+=i+"";
			for(int j=0;j<8;j++)
			{
				if(i==r[j])s+=" Q";
				else s+=" "+chess[i][j];
				
			}
			s+="\n";
		}
		System.out.println(s);
	}
	public void showResult()
	{
		this.placeTheQueenAt(0);
	}
}






