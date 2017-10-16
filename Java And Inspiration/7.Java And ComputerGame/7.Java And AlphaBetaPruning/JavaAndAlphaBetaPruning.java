import java.util.*;
import java.io.*;
public class JavaAndAlphaBetaPruning
{
	public static void main(String[] args)throws Exception
	{
		AlphaBetaPruning AlphaBetaPruning1=new AlphaBetaPruning();
		AlphaBetaPruning1.playGame();
	}
}
class AlphaBetaPruning
{
	char[][] chess;
	int result;
	public AlphaBetaPruning()
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
		for(int k=0;k<5;k++)
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
		int alpha=-2;
		int beta=2;
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
					nextScore=this.getMinScoreForO(alpha,beta);
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
	private int getMinScoreForO(int alpha,int beta)
	{
		if(this.isGameOver())return result;
		int minScore=beta;
		int nextScore=1;
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(chess[i][j]=='.')
				{
					chess[i][j]='O';
					nextScore=this.getMaxScoreForX(alpha,minScore);
					if(nextScore<minScore)minScore=nextScore;
					chess[i][j]='.';
					if(minScore<=alpha)return minScore;
				}
			}
		}
		return minScore;
	}

	private int getMaxScoreForX(int alpha,int beta)
	{
		if(this.isGameOver())return result;
		int maxScore=alpha;
		int nextScore=-1;
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(chess[i][j]=='.')
				{
					chess[i][j]='X';
					nextScore=this.getMinScoreForO(maxScore,beta);
					if(nextScore>maxScore)maxScore=nextScore;
					chess[i][j]='.';
					if(maxScore>=beta)return maxScore;
				}
			}
		}
		return maxScore;
	}
}






/*

 protected int findMin(int alpha,int beta,int step){
  int mn = beta;//极小最大值；
  if(step == 0){
   return evaluate();
  }
  int[][] rt = getBests(3-startbw);//返回部分最优解；
  for(int i = 0;i<rt.length;i++)
{
   int ii = rt[i][0];
   int jj =rt[i][1];
   if(getType(ii,jj,3-startbw)==1)
    return  -100*getMark(1);
   chess[ii][jj]= 3-startbw;
   int t = findMax(alpha,mn,step-1);
   chess[ii][jj]=0;
   if(t < mn)
    mn=t;
   if(mn <= alpha)//alpha 剪枝；
    return mn;
  }
  return mn;
 }
    极小值搜索过程：
  protected int findMax(int alpha,int beta,int step){
  int mx = alpha;//极大最小值；
  if(step == 0){ //达到特定深度返回；
   return evaluate();}
  //循环调用findmin，找出当前节点对应的最大值；
  for(int i = 3;i<11;i++){
   for(int j = 3;j<11;j++){//搜索范围为3×11；
    if(chess[i][j] ==0){
     if(getType(i,j,startbw)==1)//getTepy()得到当前棋局的棋型；
      return 100*getMark(1);//getMark()返回特定棋型的得分；
     chess[i][j] = startbw;
     int t = findMin(mx,beta,step-1);//调用极小值搜索；
     chess[i][j] = 0;
     if(t > mx)mx = t;//搜索极大值；
     if(mx >= beta)return mx;////beta剪枝；退出搜索；
    }
   }
  }
  return mx;
 }
}

 
*/