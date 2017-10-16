import java.util.*;
import java.io.*;
public class JavaAndInternationalChess
{
	public static void main(String[] args)
	{
		InternationalChess InternationalChess1=new InternationalChess();
		InternationalChess1.playGame();
	}
}
class InternationalChess
{
	public void displayNumber()
	{
		System.out.println("A:"+(int)('A'));
		System.out.println("a:"+(int)('a'));
		System.out.println("Z:"+(int)('Z'));
		System.out.println("z:"+(int)('z'));
	}
	char[][] chess;
	Scanner Scanner1;
	PositionList PositionList1;
	String command;
	boolean canMoveYellow;
	public InternationalChess()
	{
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
		PositionList1=new PositionList();
	}	
	private void display()
	{
		System.out.print(" ");
		for(int i=0;i<8;i++)System.out.print(" "+i);	
		for(int i=0;i<8;i++)
		{
			System.out.print("\n"+i);	
			for(int j=0;j<8;j++)
				if(chess[i][j]==' '&&(i+j)%2!=0)System.out.print(" "+'.');
				else if(chess[i][j]=='p')System.out.print(" "+'a');
				else System.out.print(" "+chess[i][j]);
		}
		System.out.println();
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
					if(i1==0&&j1==6&&chess[0][7]=='R'&&chess[0][6]==' '&&chess[0][5]==' '&&chess[0][4]==' ')return true;
				}
				else if(dj>1)return false;
				else return true;
			case 'k':
				if(di>1)return false;
				else if(i0==7&&j0==3)
				{
					if(i1==7&&j1==1&&chess[7][0]=='r'&&chess[7][1]==' '&&chess[7][2]==' ')return true;
					if(i1==7&&j1==6&&chess[7][7]=='r'&&chess[7][6]==' '&&chess[7][5]==' '&&chess[7][4]==' ')return true;
				}
				else if(dj>1)return false;
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
				if(di==2&&(i0!=1||dj!=0))return false;
				if(dj==1&&chess[i1][j1]==' ')return false;
				if(dj==0&&chess[i1][j1]!=' ')return false;
				return true;
			case 'p':
				if(di==0)return false;
				if(i1>i0||dj>1||di>2)return false;
				if(di==2&&(i0!=6||dj!=0))return false;
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
				else
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
				else
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
	private void getAndSetPositionForYellow()
	{
		System.out.print("YELLOW KING>");
		PositionList1.clear();
		this.getAllPosiblePositions();
		//PositionList1.showAllPositions();
			
		Position newPosition=PositionList1.getBestPosition();
		int i0=newPosition.i0;
		int j0=newPosition.j0;
		int i1=newPosition.i1;
		int j1=newPosition.j1;
		System.out.println(i0+""+j0+""+i1+""+j1);
		this.moveFrom(i0,j0,i1,j1);
	}
	private void getAndSetPositionForBlack()
	{
		while(true)
		{
			System.out.print("black king>");
			command=Scanner1.next();
			if(command.length()<2)continue;
			if(command.equals("exit"))System.exit(0);
			if(command.equals("display")){this.display();continue;}
			if(command.equals("show")){PositionList1.showAllPositions();continue;}
			if(command.equals("rules")){this.showRules();continue;}
			x0=Integer.parseInt(command.charAt(0)+"");
			y0=Integer.parseInt(command.charAt(1)+"");
			if(chess[x0][y0]<'a'){System.out.println("Is Not Your Turn!");continue;}
			if(command.length()==2)
			{
				this.displayAllPosibleMove(x0,y0);
				continue;
			}
			x1=Integer.parseInt(command.charAt(2)+"");
			y1=Integer.parseInt(command.charAt(3)+"");
			if(canMoveFrom(x0,y0,x1,y1))
			{
				this.moveFrom(x0,y0,x1,y1);	
				canMoveYellow=true;
				break;
			}
			else System.out.println("Valid Command!");
		}	
	}
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
		Position BestPosition;
		int Number=0;
		public void add(int i0,int j0,int i1,int j1,int score)
		{
			Position p=new Position(i0, j0,i1,j1,score);
			if(BestPosition==null)BestPosition=p;
			else if(score>BestPosition.Score)
			{
				p.Next=BestPosition;
				BestPosition=p;
			}
			else
			{
				Position b=BestPosition,t=BestPosition;
				for(b=BestPosition;b!=null&&p.Score<=b.Score;t=b,b=b.Next);
				p.Next=b;
				t.Next=p;
			}
			Number++;
		}
		public Position getBestPosition()
		{
			if(BestPosition.Score==0)
			{
				double d=Math.random()*Number;
				Position p=BestPosition;
				double n=0;
				for(;p!=null&&n<d;p=p.Next,n++);
				if(p!=null&&p.Score==0)return p;
				else return BestPosition;
			}
			return BestPosition;
		}
		public void showAllPositions()
		{
			Position b;
			for(b=BestPosition;b!=null;b=b.Next)System.out.println(b.i0+""+b.j0+""+b.i1+""+b.j1+"\t"+b.Score);
		}
		public void clear()
		{
			Number=0;
			BestPosition=null;
		}
	}
	private int getScore(int i0,int j0,int i1,int j1)
	{
		int score=this.getLostScore(chess[i0][j0],i0,j0);
		switch(chess[i1][j1])
		{
			case 'k':score+=200;
			case 'q':score+=50;
			case 'r':score+=20;
			case 'b':score+=10;
			case 'n':score+=5;
			case 'p':score+=1;
			default:score+=0;
		}
		return score;
	}
	private int getLostScore(char c,int i,int j)
	{
		boolean canBeEaten=false;
		char t=chess[i][j];
		chess[i][j]=c;
		char ch='.';
		for(int i0=7;i0>=0;i0--)
		{
			for(int j0=0;j0<8;j0++)
			{
				if(chess[i0][j0]>='a'&&chess[i0][j0]<='z'&&canMoveFrom(i0,j0,i,j))
				{
					canBeEaten=true;
					ch=chess[i0][j0];
					//System.out.print(i0+""+j0+":"+ch+" can eat ");
					break;
				}
			}
		}
		chess[i][j]=t;
		if(canBeEaten)
		{	//System.out.println(i+""+j+":"+c);
			switch(c)
			{
				case 'K':return 100;
				case 'Q':return 50;
				case 'R':return 20;
				case 'B':return 10;
				case 'N':return 5;
				case 'P':return 1;
				default:return 0;
			}
		}
		else return 0;
	}
	
	private void getAllPosiblePositions()
	{
		int x=0,y=0;
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				switch(chess[i][j])
				{
					case 'R':
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(i,j,x,j)-this.getLostScore('R',x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(i,j,x,j)-this.getLostScore('R',x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,j,i,y)-this.getLostScore('R',i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,j,i,y)-this.getLostScore('R',i,y));else break;}
						break;
					case 'N':
						if(canMoveFrom(i,j,i+2,j+1))PositionList1.add(i,j,i+2,j+1,this.getScore(i,j,i+2,j+1)-this.getLostScore('N',i+2,j+1));
						if(canMoveFrom(i,j,i+2,j-1))PositionList1.add(i,j,i+2,j-1,this.getScore(i,j,i+2,j-1)-this.getLostScore('N',i+2,j-1));
						if(canMoveFrom(i,j,i+1,j+2))PositionList1.add(i,j,i+1,j+2,this.getScore(i,j,i+1,j+2)-this.getLostScore('N',i+1,j+2));
						if(canMoveFrom(i,j,i+1,j-2))PositionList1.add(i,j,i+1,j-2,this.getScore(i,j,i+1,j-2)-this.getLostScore('N',i+1,j-2));
						if(canMoveFrom(i,j,i-2,j+1))PositionList1.add(i,j,i-2,j+1,this.getScore(i,j,i-2,j+1)-this.getLostScore('N',i-2,j+1));
						if(canMoveFrom(i,j,i-2,j-1))PositionList1.add(i,j,i-2,j-1,this.getScore(i,j,i-2,j-1)-this.getLostScore('N',i-2,j-1));
						if(canMoveFrom(i,j,i-1,j+2))PositionList1.add(i,j,i-1,j+2,this.getScore(i,j,i-1,j+2)-this.getLostScore('N',i-1,j+2));
						if(canMoveFrom(i,j,i-1,j-2))PositionList1.add(i,j,i-1,j-2,this.getScore(i,j,i-1,j-2)-this.getLostScore('N',i-1,j-2));
						break;
					case 'B':
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('B',x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('B',x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('B',x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('B',x,y));else break;}
						break;
					case 'K':
						if(canMoveFrom(i,j,i+1,j))PositionList1.add(i,j,i+1,j,this.getScore(i,j,i+1,j)-this.getLostScore('K',i+1,j));
						if(canMoveFrom(i,j,i+1,j+1))PositionList1.add(i,j,i+1,j+1,this.getScore(i,j,i+1,j+1)-this.getLostScore('K',i+1,j+1));
						if(canMoveFrom(i,j,i+1,j-1))PositionList1.add(i,j,i+1,j-1,this.getScore(i,j,i+1,j-1)-this.getLostScore('K',i+1,j-1));
						if(canMoveFrom(i,j,i,j+1))PositionList1.add(i,j,i,j+1,this.getScore(i,j,i,j+1)-this.getLostScore('K',i,j+1));
						if(canMoveFrom(i,j,i,j-1))PositionList1.add(i,j,i,j-1,this.getScore(i,j,i,j-1)-this.getLostScore('K',i,j-1));
						if(canMoveFrom(i,j,i-1,j))PositionList1.add(i,j,i-1,j,this.getScore(i,j,i-1,j)-this.getLostScore('K',i-1,j));
						if(canMoveFrom(i,j,i-1,j+1))PositionList1.add(i,j,i-1,j+1,this.getScore(i,j,i-1,j+1)-this.getLostScore('K',i-1,j+1));
						if(canMoveFrom(i,j,i-1,j-1))PositionList1.add(i,j,i-1,j-1,this.getScore(i,j,i-1,j-1)-this.getLostScore('K',i-1,j-1));
						break;
					case 'Q':
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(i,j,x,j)-this.getLostScore('Q',x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.add(i,j,x,j,this.getScore(i,j,x,j)-this.getLostScore('Q',x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,j,i,y)-this.getLostScore('Q',i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.add(i,j,i,y,this.getScore(i,j,i,y)-this.getLostScore('Q',i,y));else break;}
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('Q',x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('Q',x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('Q',x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.add(i,j,x,y,this.getScore(i,j,x,y)-this.getLostScore('Q',x,y));else break;}
						break;
					case 'P':
						if(canMoveFrom(i,j,i+2,j))PositionList1.add(i,j,i+2,j,this.getScore(i,j,i+2,j)-this.getLostScore('P',i+2,j));
						if(canMoveFrom(i,j,i+1,j))PositionList1.add(i,j,i+1,j,this.getScore(i,j,i+1,j)-this.getLostScore('P',i+1,j));
						if(canMoveFrom(i,j,i+1,j+1))PositionList1.add(i,j,i+1,j+1,this.getScore(i,j,i+1,j+1)-this.getLostScore('P',i+1,j+1));
						if(canMoveFrom(i,j,i+1,j-1))PositionList1.add(i,j,i+1,j-1,this.getScore(i,j,i+1,j-1)-this.getLostScore('P',i+1,j-1));
						break;
				}
			}
		}
	}
	public void playGame()
	{
		this.display();
		canMoveYellow=true;
		while(true)
		{
			this.getAndSetPositionForYellow();
			this.display();
			this.getAndSetPositionForBlack();
			this.display();
		}
	}
}



