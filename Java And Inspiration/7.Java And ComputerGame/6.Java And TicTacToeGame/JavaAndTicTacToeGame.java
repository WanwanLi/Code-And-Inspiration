import java.util.*;
import java.io.*;
public class JavaAndTicTacToeGame
{
	public static void main(String[] args)throws Exception
	{
		TicTacToeGame TicTacToeGame1=new TicTacToeGame();
		TicTacToeGame1.playGame();
	}
}
class TicTacToeGame
{
	char[][] chess;
	int result;
	public TicTacToeGame()
	{
		chess=new char[][]
		{
			{'.','.','.'},
			{'.','.','.'},
			{'.','.','.'}
		};
		result=0;
	}
	private void display()
	{
		String s="Tic Tac Toe:\n";
		for(int i=0;i<3;i++)s+="\t"+i;
		s+="\n\n";
		for(int i=0;i<3;i++)
		{
			s+=i;
			for(int j=0;j<3;j++)s+="\t"+chess[i][j];
			s+="\n\n";
		}
		System.out.println(s);
	}
	private void showResult()
	{
		if(result==-1)System.out.println("Game Over!You Win!");
		else if(result==1)System.out.println("Game Over!You Lose!");
		else System.out.println("Game Over!");
		System.exit(0);
	}
	public void playGame()
	{
		while(true)
		{
			this.setPositionForX();
			this.display();
			if(this.isGameOver())this.showResult();
			this.getAndSetPositionForO();
			this.display();
			if(this.isGameOver())this.showResult();			
		}
	}
	private void getAndSetPositionForO()
	{
		Scanner scanner=new Scanner(System.in);
		while(true)
		{
			System.out.print("Please Input Row:");
			int i=scanner.nextInt();
			System.out.print("Please Input Column:");
			int j=scanner.nextInt();
			if(i>=0&&i<=2&&j>=0&&j<=2&&chess[i][j]=='.'){chess[i][j]='O';return;}
			else System.out.println("Please Input Again!");
		}
	}
	private int getStatus(char a,char b,char c)
	{
		if(a=='X'&&b=='X'&&c=='X')return 1;	
		else if(a=='O'&&b=='O'&&c=='O')return -1;
		else return 0;
	}
	private int getScore()
	{
		int score=0;
		for(int i=0;i<3;i++){score=this.getStatus(chess[i][0],chess[i][1],chess[i][2]);if(score!=0)return score;}
		for(int i=0;i<3;i++){score=this.getStatus(chess[0][i],chess[1][i],chess[2][i]);if(score!=0)return score;}
		score=this.getStatus(chess[0][0],chess[1][1],chess[2][2]);
		if(score!=0)return score;
		return this.getStatus(chess[0][2],chess[1][1],chess[2][0]);
	}
	private boolean isGameOver()
	{
		result=this.getScore();
		if(result!=0)return true;
		for(int i=0;i<3;i++)for(int j=0;j<3;j++)if(chess[i][j]=='.')return false;
		return true;
	}
	private void setPositionForX()
	{
		int maxScore=-2;
		int nextScore=-1;
		int bestRow=0;
		int bestColumn=0;
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(chess[i][j]=='.')
				{
					chess[i][j]='X';
					if(this.isGameOver())return;
					nextScore=this.getMinScoreForO();
					if(nextScore>maxScore)
					{
						maxScore=nextScore;
						bestRow=i;
						bestColumn=j;
					}
					chess[i][j]='.';
				}
			}
		}
		chess[bestRow][bestColumn]='X';
	}
	private int getMinScoreForO()
	{
		if(this.isGameOver())return result;
		int minScore=2;
		int nextScore=1;
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(chess[i][j]=='.')
				{
					chess[i][j]='O';
					nextScore=this.getMaxScoreForX();
					if(nextScore<minScore)minScore=nextScore;
					chess[i][j]='.';
				}
			}
		}
		return minScore;
	}
	private int getMaxScoreForX()
	{
		if(this.isGameOver())return result;
		int maxScore=-2;
		int nextScore=-1;
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(chess[i][j]=='.')
				{
					chess[i][j]='X';
					nextScore=this.getMinScoreForO();
					if(nextScore>maxScore)maxScore=nextScore;
					chess[i][j]='.';
				}
			}
		}
		return maxScore;
	}
}