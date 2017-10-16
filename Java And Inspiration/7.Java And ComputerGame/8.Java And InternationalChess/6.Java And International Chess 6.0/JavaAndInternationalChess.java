import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Scanner;
import javax.swing.Timer;

public class JavaAndInternationalChess
{
	public static void main(String[] args)
	{
		InternationalChess InternationalChess1=new InternationalChess();
		InternationalChess1.playGame();
	}
}
class InternationalChess extends Frame implements ActionListener,MouseListener,KeyListener
{
	class Position
	{
		public int i0;
		public int j0;
		public int i1;
		public int j1;
		public int Score;
		public Position Next;
		public Position(int i0,int j0,int i1,int j1,int score)
		{
			this.i0=i0;
			this.j0=j0;
			this.i1=i1;
			this.j1=j1;
			this.Score=score;
		}
	}
	class PositionList
	{
		public Position BestPosition;
		public void add(int i0,int j0,int i1,int j1,int score)
		{
			Position p=new Position(i0, j0,i1,j1,score);
			if(BestPosition==null)BestPosition=p;
			else if(score>=BestPosition.Score)
			{
				p.Next=BestPosition;
				BestPosition=p;
			}
			else
			{
				Position b=BestPosition,t=BestPosition;
				for(b=BestPosition;b!=null&&p.Score<b.Score;t=b,b=b.Next);
				p.Next=b;
				t.Next=p;
			}
		}
		public Position getBestPosition()
		{
			return BestPosition;
		}
		public void showAllPositions()
		{
			Position b;
			for(b=BestPosition;b!=null;b=b.Next)System.out.println(b.i0+""+b.j0+""+b.i1+""+b.j1+"\t"+b.Score);
		}
		public void clear()
		{
			BestPosition=null;
		}
	}
	class ChessBoard
	{
		public char[][] Chess=new char[8][8];
		public ChessBoard Next;
		public ChessBoard(char[][] chess)
		{
			for(int i=0;i<8;i++)for(int j=0;j<8;j++)this.Chess[i][j]=chess[i][j];
			Next=null;
		}
		public void loadTo(char[][] chess)
		{
			for(int i=0;i<8;i++)for(int j=0;j<8;j++)chess[i][j]=this.Chess[i][j];
		}
	}
	class ChessBoardRecord
	{
		ChessBoard Recent;
		public void add(char[][] chess)
		{
			ChessBoard Temporay=new ChessBoard(chess);
			if(Recent==null)Recent=Temporay;
			else
			{
				Temporay.Next=Recent;
				Recent=Temporay;
			}
		}
		public char[][] getRecentChess()
		{
			char[][] recentChess=new char[8][8];
			for(int i=0;i<8;i++)
			{
				for(int j=0;j<8;j++)
				{
					recentChess[i][j]=Recent.Chess[i][j];
				}
			}
			if(Recent.Next!=null)Recent=Recent.Next;
			return recentChess;
		}
	}
	char[][] chess;
	Scanner Scanner1;
	Frame Frame1;
	String command;
	boolean canMoveYellow;
	int recursionCounter=0;
	public InternationalChess()
	{
		this.setTitle("International  Chess 6.0: Made By LiWanwan");
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setVisible(true);
		this.addTimer(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addWindowListener(new WindowAdapter()
		{		
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		chess=new char[][]
		{
			{'R','N','B','K','Q','B','N','R'},
			{'P','P','P','P','P','P','P','P'},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{'p','p','p','p','p','p','p','p'},
			{'r','n','b','k','q','b','n','r'}
		};
		Scanner1=new Scanner(System.in);
	}
	private void addTimer(ActionListener actionListener)
	{
		new Timer(100, actionListener).start();
	}
	private void display()
	{
		System.out.print(" ");
		for(int i=0;i<8;i++)System.out.print(" "+i);	
		for(int i=0;i<8;i++)
		{
			System.out.print("\n"+i);	
			for(int j=0;j<8;j++)
			{
				if(chess[i][j]==' '&&(i+j)%2!=0)System.out.print(" "+'.');
				else if(chess[i][j]=='p')System.out.print(" "+'a');
				else System.out.print(" "+chess[i][j]);
			}
		}
		System.out.println();
	}
	public void actionPerformed(ActionEvent e)
	{
		this.repaint();
	}
	public void paint(Graphics g)
	{
		BufferedImage bufferedImage=new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		this.drawImage(bufferedImage.getGraphics()); g.drawImage(bufferedImage, 0, 0, null);
	}
	private void drawImage(Graphics g)
	{
		g.drawImage(Toolkit.getDefaultToolkit().getImage("chess\\InternationalChess6.0.jpg"),0,0,this);
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(mouseIsClicked&&canMoveFrom(Y0,X0,i,j))g.drawImage(Toolkit.getDefaultToolkit().getImage("chess\\win.gif"),80*j+J,80*i+I,this);
				if(chess[i][j]>'Z'&&chess[i][j]<'z')g.drawImage(Toolkit.getDefaultToolkit().getImage("chess\\chessman\\Black\\"+chess[i][j]+".gif"),80*j+J,80*i+I,this);
				else if(chess[i][j]>'A'&&chess[i][j]<'Z')g.drawImage(Toolkit.getDefaultToolkit().getImage("chess\\chessman\\Yellow\\"+chess[i][j]+".gif"),80*j+J,80*i+I,this);
			}
		}
	}
	private int Abs(int n)
	{
		return (n>0?n:-n);
	}
	private boolean isSameColor(int i0,int j0,int i1,int j1)
	{
		if(chess[i0][j0]==' '||chess[i1][j1]==' ')return false;
		else return ((chess[i0][j0]<'Z')==(chess[i1][j1]<'Z'));
	}
	private boolean canMoveFrom(int i0,int j0,int i1,int j1)
	{
		if(i0<0||j0<0||i1<0||j1<0||i0>7||j0>7||i1>7||j1>7||chess[i0][j0]==' '||isSameColor(i0,j0,i1,j1))return false;
		int di=Abs(i1-i0);
		int dj=Abs(j1-j0);
		int i,j;
		if(di==0&&dj==0)return false;
		boolean isVHLine=(di==0||dj==0);
		boolean isSkewLine=(di==dj);
		switch(chess[i0][j0])
		{
			case 'K':
				if(di>1)return false;
				else if(i0==0&&j0==3)
				{
					if(i1==0&&j1==1&&chess[0][0]=='R'&&chess[0][1]==' '&&chess[0][2]==' ')return true;
					else if(i1==0&&j1==6&&chess[0][7]=='R'&&chess[0][6]==' '&&chess[0][5]==' '&&chess[0][4]==' ')return true;
				}
				if(dj>1)return false;
				else return true;
			case 'k':
				if(di>1)return false;
				else if(i0==7&&j0==3)
				{
					if(i1==7&&j1==1&&chess[7][0]=='r'&&chess[7][1]==' '&&chess[7][2]==' ')return true;
					else if(i1==7&&j1==6&&chess[7][7]=='r'&&chess[7][6]==' '&&chess[7][5]==' '&&chess[7][4]==' ')return true;
				}
				if(dj>1)return false;
				else return true;
			case 'Q':
			case 'q':
				if(isSkewLine)
				{
					if(i0<i1)
					{
						if(j0<j1)
						{
							for(i=i0+1,j=j0+1;i<i1;i++,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0+1,j=j0-1;i<i1;i++,j--)if(chess[i][j]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(i=i0-1,j=j0+1;i>i1;i--,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0-1,j=j0-1;i>i1;i--,j--)if(chess[i][j]!=' ')return false;
					}
					return true;
				}
				else if(isVHLine)
				{
					if(dj==0)
					{
						if(i0<i1)
						{
							for(i=i0+1;i<i1;i++)if(chess[i][j0]!=' ')return false;
						}
						else for(i=i0-1;i>i1;i--)if(chess[i][j0]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(j=j0+1;j<j1;j++)if(chess[i0][j]!=' ')return false;
						}
						else for(j=j0-1;j>j1;j--)if(chess[i0][j]!=' ')return false;
					}
					return true;
				}
				else return false;
			case 'B':
			case 'b':
				if(isSkewLine)
				{
					if(i0<i1)
					{
						if(j0<j1)
						{
							for(i=i0+1,j=j0+1;i<i1;i++,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0+1,j=j0-1;i<i1;i++,j--)if(chess[i][j]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(i=i0-1,j=j0+1;i>i1;i--,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0-1,j=j0-1;i>i1;i--,j--)if(chess[i][j]!=' ')return false;
					}
					return true;
				}
				else return false;
			case 'N':
			case 'n':
				if((di==2&&dj==1)||(di==1&&dj==2))return true;
				else return false;
			case 'R':
			case 'r':
				if(isVHLine)
				{
					if(dj==0)
					{
						if(i0<i1)
						{
							for(i=i0+1;i<i1;i++)if(chess[i][j0]!=' ')return false;
						}
						else for(i=i0-1;i>i1;i--)if(chess[i][j0]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(j=j0+1;j<j1;j++)if(chess[i0][j]!=' ')return false;
						}
						else for(j=j0-1;j>j1;j--)if(chess[i0][j]!=' ')return false;
					}
					return true;
				}
				else return false;
			case 'P':
				if(di==0)return false;
				if(i1<i0||dj>1||di>2)return false;
				if(di==2&&(i0!=1||dj!=0||chess[i0+1][j0]!=' '))return false;
				if(dj==1&&chess[i1][j1]==' ')return false;
				if(dj==0&&chess[i1][j1]!=' ')return false;
				return true;
			case 'p':
				if(di==0)return false;
				if(i1>i0||dj>1||di>2)return false;
				if(di==2&&(i0!=6||dj!=0||chess[i0-1][j0]!=' '))return false;
				if(dj==1&&chess[i1][j1]==' ')return false;
				if(dj==0&&chess[i1][j1]!=' ')return false;
				return true;
			default:
				return false;
		}
	}
	private void moveFrom(int i0,int j0,int i1,int j1)
	{
		if(chess[i1][j1]=='K')
		{
			System.out.println("YELLOW KING LOSE!");
			System.exit(0);
		}
		if(chess[i1][j1]=='k')
		{
			System.out.println("black king lose!");
			System.exit(0);
		}
		if(chess[i0][j0]=='K')
		{
			if(Abs(j1-j0)>1)
			{
				if(j1==1)
				{
					chess[0][1]='K';
					chess[0][3]=' ';
					chess[0][2]='R';
					chess[0][0]=' ';
				}
				else if(j1==6)
				{
					chess[0][6]='K';
					chess[0][3]=' ';
					chess[0][5]='R';
					chess[0][7]=' ';
				}
				return;
			}
		}
		if(chess[i0][j0]=='k')
		{
			if(Abs(j1-j0)>1)
			{
				if(j1==1)
				{
					chess[7][1]='k';
					chess[7][3]=' ';
					chess[7][2]='r';
					chess[7][0]=' ';
				}
				else if(j1==6)
				{
					chess[7][6]='k';
					chess[7][3]=' ';
					chess[7][5]='r';
					chess[7][7]=' ';
				}
				return;
			}
		}		
		if(chess[i0][j0]=='P')
		{
			if(i1==7)
			{
				chess[i1][j1]='Q';	
				chess[i0][j0]=' ';
				return ;
			}
		}
		if(chess[i0][j0]=='p')
		{
			if(i1==0)
			{
				chess[i1][j1]='q';	
				chess[i0][j0]=' ';
				return ;
			}
		}
		chess[i1][j1]=chess[i0][j0];
		chess[i0][j0]=' ';
	}
	private int x0,y0,x1,y1;
	private void displayAllPosibleMove(int i0,int j0)
	{
		System.out.print(" ");
		for(int i=0;i<8;i++)System.out.print(" "+i);	
		for(int i=0;i<8;i++)
		{
			System.out.print("\n"+i);	
			for(int j=0;j<8;j++)
			{
				if(chess[i][j]==' '&&this.canMoveFrom(i0,j0,i,j))System.out.print(" "+'*');
				else
				{
					if(chess[i][j]==' '&&(i+j)%2!=0)System.out.print(" "+'.');
					else if(chess[i][j]=='p')System.out.print(" "+'a');
					else System.out.print(" "+chess[i][j]);
				}
			}
		}
		System.out.println();
	}
	private void showRules()
	{
		String s="";
		String rules="";
		BufferedReader BufferedReader1;
		try
		{
			BufferedReader1=new BufferedReader(new FileReader("Rules.txt"));
			s=BufferedReader1.readLine();
			while(s!=null)
			{
				rules+=s+"\n";
				s=BufferedReader1.readLine();
			}
		}
		catch(Exception e){}
		System.out.println(rules);
	}
	public int Depth=3;
	public int LongestTime=10000; 
	private void getAndSetPositionForYellow()
	{
		System.out.print("YELLOW KING>");
		int maxScore=-100;
		int nextScore=-100;
		recursionCounter=0;
		PositionList PositionList1=new PositionList();
		Position bestPosition=PositionList1.BestPosition;
		this.getAllPosiblePositionsForYellow(PositionList1);
		for(Position p=PositionList1.BestPosition;p!=null;p=p.Next)
		{
			int depth=0;
			int i0=p.i0;
			int j0=p.j0;
			int i1=p.i1;
			int j1=p.j1;
			int score=p.Score;
			if(score==100){bestPosition=p;break;}
			ChessBoard ChessBoard1=new ChessBoard(this.chess);
			this.moveFrom(i0,j0,i1,j1);
			nextScore=score-this.getMaxScoreForBlack(depth);
			if(nextScore>maxScore){maxScore=nextScore;bestPosition=p;}
			ChessBoard1.loadTo(this.chess);

		}
		if(bestPosition==null){System.out.println("Yellow King Lose!");System.exit(0);}
		int i0=bestPosition.i0;
		int j0=bestPosition.j0;
		int i1=bestPosition.i1;
		int j1=bestPosition.j1;
		System.out.println(i0+""+j0+""+i1+""+j1);
		this.moveFrom(i0,j0,i1,j1);
	}
	private int getMaxScoreForBlack(int depth)
	{
		recursionCounter++;
		int maxScore=-100;
		int nextScore=-100;
		PositionList PositionList1=new PositionList();
		this.getAllPosiblePositionsForBlack(PositionList1);
		for(Position p=PositionList1.BestPosition;p!=null;p=p.Next)
		{
			int i0=p.i0;
			int j0=p.j0;
			int i1=p.i1;
			int j1=p.j1;
			int score=p.Score;
			if(score>10)return score;
			ChessBoard ChessBoard1=new ChessBoard(this.chess);
			this.moveFrom(i0,j0,i1,j1);
			nextScore=score-this.getMaxScoreForYellow(depth+1);
			if(nextScore>maxScore)maxScore=nextScore;
			ChessBoard1.loadTo(this.chess);
		}
		return maxScore;
	}
	private int getMaxScoreForYellow(int depth)
	{
		recursionCounter++;
		int maxScore=-100;
		int nextScore=-100;
		PositionList PositionList1=new PositionList();
		this.getAllPosiblePositionsForYellow(PositionList1);
		if(depth>Depth||recursionCounter>LongestTime)return PositionList1.BestPosition.Score;
		for(Position p=PositionList1.BestPosition;p!=null;p=p.Next)
		{
			int i0=p.i0;
			int j0=p.j0;
			int i1=p.i1;
			int j1=p.j1;
			int score=p.Score;
			if(score>10)return score;
			ChessBoard ChessBoard1=new ChessBoard(this.chess);
			this.moveFrom(i0,j0,i1,j1);
			nextScore=score-this.getMaxScoreForBlack(depth+1);
			if(nextScore>maxScore)maxScore=nextScore;
			ChessBoard1.loadTo(this.chess);
		}
		return maxScore;	
	}

	private int getScore(int i,int j)
	{
		switch(chess[i][j])
		{
			case 'k':return 100;
			case 'q':return 50;
			case 'r':return 20;
			case 'b':return 10;
			case 'n':return 5;
			case 'p':return 1;
			case 'K':return 100;
			case 'Q':return 50;
			case 'R':return 20;
			case 'B':return 10;
			case 'N':return 5;
			case 'P':return 1;
			default:return 0;
		}
	}
	private void getAllPosiblePositionsForYellow(PositionList PositionList1)
	{
		int x=0,y=0;
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				switch(chess[i][j])
				{
					case 'R':
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						break;
					case 'N':
						if(canMoveFrom(i,j,i+2,j+1))PositionList1.add(i,j,i+2,j+1,this.getScore(i+2,j+1));
						if(canMoveFrom(i,j,i+2,j-1))PositionList1.add(i,j,i+2,j-1,this.getScore(i+2,j-1));
						if(canMoveFrom(i,j,i+1,j+2))PositionList1.add(i,j,i+1,j+2,this.getScore(i+1,j+2));
						if(canMoveFrom(i,j,i+1,j-2))PositionList1.add(i,j,i+1,j-2,this.getScore(i+1,j-2));
						if(canMoveFrom(i,j,i-2,j+1))PositionList1.add(i,j,i-2,j+1,this.getScore(i-2,j+1));
						if(canMoveFrom(i,j,i-2,j-1))PositionList1.add(i,j,i-2,j-1,this.getScore(i-2,j-1));
						if(canMoveFrom(i,j,i-1,j+2))PositionList1.add(i,j,i-1,j+2,this.getScore(i-1,j+2));
						if(canMoveFrom(i,j,i-1,j-2))PositionList1.add(i,j,i-1,j-2,this.getScore(i-1,j-2));
						break;
					case 'B':
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						break;
					case 'K':
						if(canMoveFrom(i,j,i+1,j))PositionList1.add(i,j,i+1,j,this.getScore(i+1,j));
						if(canMoveFrom(i,j,i+1,j+1))PositionList1.add(i,j,i+1,j+1,this.getScore(i+1,j+1));
						if(canMoveFrom(i,j,i+1,j-1))PositionList1.add(i,j,i+1,j-1,this.getScore(i+1,j-1));
						if(canMoveFrom(i,j,i,j+1))PositionList1.add(i,j,i,j+1,this.getScore(i,j+1));
						if(canMoveFrom(i,j,i,j-1))PositionList1.add(i,j,i,j-1,this.getScore(i,j-1));
						if(canMoveFrom(i,j,i-1,j))PositionList1.add(i,j,i-1,j,this.getScore(i-1,j));
						if(canMoveFrom(i,j,i-1,j+1))PositionList1.add(i,j,i-1,j+1,this.getScore(i-1,j+1));
						if(canMoveFrom(i,j,i-1,j-1))PositionList1.add(i,j,i-1,j-1,this.getScore(i-1,j-1));
						break;
					case 'Q':
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						break;
					case 'P':
						if(canMoveFrom(i,j,i+2,j))PositionList1.add(i,j,i+2,j,this.getScore(i+2,j));
						if(canMoveFrom(i,j,i+1,j))PositionList1.add(i,j,i+1,j,this.getScore(i+1,j));
						if(canMoveFrom(i,j,i+1,j+1))PositionList1.add(i,j,i+1,j+1,this.getScore(i+1,j+1));
						if(canMoveFrom(i,j,i+1,j-1))PositionList1.add(i,j,i+1,j-1,this.getScore(i+1,j-1));
						break;
				}
			}
		}
	}
	private void getAllPosiblePositionsForBlack(PositionList PositionList1)
	{
		int x=0,y=0;
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				switch(chess[i][j])
				{
					case 'r':
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						break;
					case 'n':
						if(canMoveFrom(i,j,i+2,j+1))PositionList1.add(i,j,i+2,j+1,this.getScore(i+2,j+1));
						if(canMoveFrom(i,j,i+2,j-1))PositionList1.add(i,j,i+2,j-1,this.getScore(i+2,j-1));
						if(canMoveFrom(i,j,i+1,j+2))PositionList1.add(i,j,i+1,j+2,this.getScore(i+1,j+2));
						if(canMoveFrom(i,j,i+1,j-2))PositionList1.add(i,j,i+1,j-2,this.getScore(i+1,j-2));
						if(canMoveFrom(i,j,i-2,j+1))PositionList1.add(i,j,i-2,j+1,this.getScore(i-2,j+1));
						if(canMoveFrom(i,j,i-2,j-1))PositionList1.add(i,j,i-2,j-1,this.getScore(i-2,j-1));
						if(canMoveFrom(i,j,i-1,j+2))PositionList1.add(i,j,i-1,j+2,this.getScore(i-1,j+2));
						if(canMoveFrom(i,j,i-1,j-2))PositionList1.add(i,j,i-1,j-2,this.getScore(i-1,j-2));
						break;
					case 'b':
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						break;
					case 'k':
						if(canMoveFrom(i,j,i+1,j))PositionList1.add(i,j,i+1,j,this.getScore(i+1,j));
						if(canMoveFrom(i,j,i+1,j+1))PositionList1.add(i,j,i+1,j+1,this.getScore(i+1,j+1));
						if(canMoveFrom(i,j,i+1,j-1))PositionList1.add(i,j,i+1,j-1,this.getScore(i+1,j-1));
						if(canMoveFrom(i,j,i,j+1))PositionList1.add(i,j,i,j+1,this.getScore(i,j+1));
						if(canMoveFrom(i,j,i,j-1))PositionList1.add(i,j,i,j-1,this.getScore(i,j-1));
						if(canMoveFrom(i,j,i-1,j))PositionList1.add(i,j,i-1,j,this.getScore(i-1,j));
						if(canMoveFrom(i,j,i-1,j+1))PositionList1.add(i,j,i-1,j+1,this.getScore(i-1,j+1));
						if(canMoveFrom(i,j,i-1,j-1))PositionList1.add(i,j,i-1,j-1,this.getScore(i-1,j-1));
						break;
					case 'q':
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,y));else break;}
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(x,y));else break;}
						break;
					case 'p':
						if(canMoveFrom(i,j,i-2,j))PositionList1.add(i,j,i-2,j,this.getScore(i-2,j));
						if(canMoveFrom(i,j,i-1,j))PositionList1.add(i,j,i-1,j,this.getScore(i-1,j));
						if(canMoveFrom(i,j,i-1,j+1))PositionList1.add(i,j,i-1,j+1,this.getScore(i-1,j+1));
						if(canMoveFrom(i,j,i-1,j-1))PositionList1.add(i,j,i-1,j-1,this.getScore(i-1,j-1));
						break;
				}
			}
		}
	}
	int X0, Y0, X1, Y1, J=690, I=55;
	boolean mouseIsClicked=false;
	boolean hasFinishedInput=false;
	public void mouseClicked(MouseEvent e)
	{
		if(!mouseIsClicked)
		{
			X0=(e.getX()-J)/80;
			Y0=(e.getY()-I)/80;
			if(chess[Y0][X0]<'a')return;
			mouseIsClicked=true;
		}
		else 
		{
			X1=(e.getX()-J)/80;
			Y1=(e.getY()-I)/80;
			hasFinishedInput=true;
			mouseIsClicked=false;
		}
	}			
	private void getAndSetPositionForBlack()
	{
		while(true)
		{
			System.out.print("");
			if(hasFinishedInput&&canMoveFrom(Y0,X0,Y1,X1))
			{
				this.moveFrom(Y0,X0,Y1,X1);	
				System.out.println("black king>"+Y0+""+X0+""+Y1+""+X1);
				canMoveYellow=true;
				hasFinishedInput=false;
				X0=Y0=X1=Y1=0;
				return;
			}
		}
	}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar()=='z')
		{
			chess=ChessBoardRecord1.getRecentChess();
		}
	}
	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	ChessBoardRecord ChessBoardRecord1;
	public void playGame()
	{
		this.display();
		canMoveYellow=true;
		ChessBoardRecord1=new ChessBoardRecord();
		ChessBoardRecord1.add(chess);
		while(true)
		{
			this.getAndSetPositionForYellow();
			ChessBoardRecord1.add(chess);
			this.display();
			this.getAndSetPositionForBlack();
			ChessBoardRecord1.add(chess);
			this.display();
		}
	}
}