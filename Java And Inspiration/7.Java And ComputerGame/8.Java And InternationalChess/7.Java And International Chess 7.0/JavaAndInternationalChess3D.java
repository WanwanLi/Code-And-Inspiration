import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.Enumeration;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndInternationalChess3D extends Applet implements MouseListener
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
		public void insert(int i0,int j0,int i1,int j1,int score)
		{
			Position p=new Position(i0, j0,i1,j1,score);
			if(BestPosition==null)BestPosition=p;
			else if(score<BestPosition.Score)
			{
				p.Next=BestPosition;
				BestPosition=p;
			}
			else
			{
				Position b=BestPosition,t=BestPosition;
				for(b=BestPosition;b!=null&&p.Score>=b.Score;t=b,b=b.Next);
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
	char[][] chess;
	boolean canMoveYellow;
	int recursionCounter=0;
	PointArray PointArray1;
	PickCanvas PickCanvas1;
	Block[][] Blocks;
	Chess3D chess3D;
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndInternationalChess3D(),400,400);
	}
	public void init()
	{
		Canvas3D canvas3D=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		canvas3D.addMouseListener(this);
		this.setLayout(new BorderLayout());
		this.add(canvas3D);
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		TextureLoader TextureLoader1=new TextureLoader("InternationalChess7.0.jpg",null);
		ImageComponent2D imageComponent2D=TextureLoader1.getImage();
		Background Background1=new Background(imageComponent2D);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		this.chess=new char[][]
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
		this.chess3D=new Chess3D(this.chess);
		TransformGroup1.addChild(this.chess3D);
		ChessBoard ChessBoard1=new ChessBoard();
		this.Blocks=ChessBoard1.Blocks;
		TransformGroup1.addChild(ChessBoard1);
		Transform3D transform3D=new Transform3D();
		transform3D.rotX(Math.PI/2.8);
		transform3D.setTranslation(new Vector3f(0f,-0.05f,0f));
		TransformGroup1.setTransform(transform3D);
		this.PickCanvas1=new PickCanvas(canvas3D,BranchGroup1);
		PickCanvas1.setMode(PickTool.GEOMETRY);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		this.playGame();
	}
	int X0;
	int Y0;
	int X1;
	int Y1;
	boolean mouseIsClicked=false;
	boolean hasFinishedInput=false;
	public void mouseClicked(MouseEvent e)
	{
		boolean hasFound=false;
		int I=0,J=0;
		this.PickCanvas1.setShapeLocation(e);
		PickResult[] PickResults=this.PickCanvas1.pickAll();
		for(int k=0;k<PickResults.length;k++)
		{
			Node node=PickResults[k].getObject();
			if(node instanceof Shape3D)
			{
				I=J=0;
				for(int i=0;i<8;i++)
				{
					for(int j=0;j<8;j++)
					{
						if(this.Blocks[i][j].equals(node))
						{
							hasFound=true;
							I=i;
							J=j;
							break;
						}
					}
					if(hasFound)break;
				}
			}
			if(hasFound)break;
		}
		if(!hasFound){System.out.println("Not Found!");return;}
		if(!mouseIsClicked)
		{
			X0=J;
			Y0=I;
			if(chess[Y0][X0]<'a')return;
			mouseIsClicked=true;
			for(int i=0;i<8;i++)
			{
				for(int j=0;j<8;j++)
				{
					if(this.canMoveFrom(Y0,X0,i,j))this.chess3D.placeHintBall(i,j);
				}
			}
		}
		else 
		{
			X1=J;
			Y1=I;
			this.chess3D.removeAllHintBalls();
			hasFinishedInput=true;
			mouseIsClicked=false;
		}
	}			
	private void getAndSetPositionForBlack()
	{
		while(true)
		{
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
	public void playGame()
	{
		canMoveYellow=true;
		while(true)
		{
			this.getAndSetPositionForYellow();
			this.getAndSetPositionForBlack();
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
		if(i0==0&&j0==3&&i1==0&&chess[i0][j0]=='K')
		{
			if(j1==1||j1==6)
			{
				if(j1==1)
				{
					chess[0][1]='K';
					chess[0][3]=' ';
					chess[0][2]='R';
					chess[0][0]=' ';
					this.chess3D.moveFrom(0,3,0,1);
					this.chess3D.moveFrom(0,0,0,2);
				}
				else if(j1==6)
				{
					chess[0][6]='K';
					chess[0][3]=' ';
					chess[0][5]='R';
					chess[0][7]=' ';
					this.chess3D.moveFrom(0,3,0,6);
					this.chess3D.moveFrom(0,7,0,5);
				}
				return;
			}
		}
		if(i0==7&&j0==3&&i1==7&&chess[i0][j0]=='k')
		{
			if(j1==1||j1==6)
			{
				if(j1==1)
				{
					chess[7][1]='k';
					chess[7][3]=' ';
					chess[7][2]='r';
					chess[7][0]=' ';
					this.chess3D.moveFrom(7,3,7,1);
					this.chess3D.moveFrom(7,0,7,2);
				}
				else if(j1==6)
				{
					chess[7][6]='k';
					chess[7][3]=' ';
					chess[7][5]='r';
					chess[7][7]=' ';
					this.chess3D.moveFrom(7,3,7,6);
					this.chess3D.moveFrom(7,7,7,5);
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
				this.chess3D.moveFrom(i0,j0,i1,j1);
				this.chess3D.placeQueenAt(Chess3D.YELLOW,i1,j1);
				return ;
			}
		}
		if(chess[i0][j0]=='p')
		{
			if(i1==0)
			{
				chess[i1][j1]='q';	
				chess[i0][j0]=' ';
				this.chess3D.moveFrom(i0,j0,i1,j1);
				this.chess3D.placeQueenAt(Chess3D.BLACK,i1,j1);
				return ;
			}
		}
		chess[i1][j1]=chess[i0][j0];
		chess[i0][j0]=' ';
		this.chess3D.moveFrom(i0,j0,i1,j1);
	}
	private int x0,y0,x1,y1;
	public int Depth=10;
	public int Width=5;
	public int LongestTime=10000;
	private int[] MinScore=new int[]{-50,-20,-10,-5,-1,0};
	private int[] MaxScore=new int[]{50,20,10,5,1,0};
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
			char c=chess[i1][j1];
			chess[i1][j1]=chess[i0][j0];
			chess[i0][j0]=' ';
			nextScore=this.getMinScoreForBlack(depth);
			if(nextScore>maxScore){maxScore=nextScore;bestPosition=p;}
			chess[i0][j0]=chess[i1][j1];
			chess[i1][j1]=c;
		}
		if(bestPosition==null){System.out.println("Yellow King Lose!");System.exit(0);}
		int i0=bestPosition.i0;
		int j0=bestPosition.j0;
		int i1=bestPosition.i1;
		int j1=bestPosition.j1;
		System.out.println(i0+""+j0+""+i1+""+j1);
		this.moveFrom(i0,j0,i1,j1);
	}
	private int getMinScoreForBlack(int depth)
	{
		recursionCounter++;
		int minScore=100;
		int nextScore=100;
		PositionList PositionList1=new PositionList();
		this.getAllPosiblePositionsForBlack(PositionList1);
		for(Position p=PositionList1.BestPosition;p!=null;p=p.Next)
		{
			int i0=p.i0;
			int j0=p.j0;
			int i1=p.i1;
			int j1=p.j1;
			int score=p.Score;
			if(score<MinScore[Width])return score;
			char c=chess[i1][j1];
			chess[i1][j1]=chess[i0][j0];
			chess[i0][j0]=' ';
			nextScore=this.getMaxScoreForYellow(depth+1);
			if(nextScore<minScore)minScore=nextScore;
			chess[i0][j0]=chess[i1][j1];
			chess[i1][j1]=c;
		}
		return minScore;
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
			if(score>MaxScore[Width])return score;
			char c=chess[i1][j1];
			chess[i1][j1]=chess[i0][j0];
			chess[i0][j0]=' ';
			nextScore=this.getMinScoreForBlack(depth+1);
			if(nextScore>maxScore)maxScore=nextScore;
			chess[i0][j0]=chess[i1][j1];
			chess[i1][j1]=c;
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
			case 'K':return -100;
			case 'Q':return -50;
			case 'R':return -20;
			case 'B':return -10;
			case 'N':return -5;
			case 'P':return -1;
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
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.insert(i,j,x,j,this.getScore(x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.insert(i,j,x,j,this.getScore(x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.insert(i,j,i,y,this.getScore(i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.insert(i,j,i,y,this.getScore(i,y));else break;}
						break;
					case 'n':
						if(canMoveFrom(i,j,i+2,j+1))PositionList1.insert(i,j,i+2,j+1,this.getScore(i+2,j+1));
						if(canMoveFrom(i,j,i+2,j-1))PositionList1.insert(i,j,i+2,j-1,this.getScore(i+2,j-1));
						if(canMoveFrom(i,j,i+1,j+2))PositionList1.insert(i,j,i+1,j+2,this.getScore(i+1,j+2));
						if(canMoveFrom(i,j,i+1,j-2))PositionList1.insert(i,j,i+1,j-2,this.getScore(i+1,j-2));
						if(canMoveFrom(i,j,i-2,j+1))PositionList1.insert(i,j,i-2,j+1,this.getScore(i-2,j+1));
						if(canMoveFrom(i,j,i-2,j-1))PositionList1.insert(i,j,i-2,j-1,this.getScore(i-2,j-1));
						if(canMoveFrom(i,j,i-1,j+2))PositionList1.insert(i,j,i-1,j+2,this.getScore(i-1,j+2));
						if(canMoveFrom(i,j,i-1,j-2))PositionList1.insert(i,j,i-1,j-2,this.getScore(i-1,j-2));
						break;
					case 'b':
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						break;
					case 'k':
						if(canMoveFrom(i,j,i+1,j))PositionList1.insert(i,j,i+1,j,this.getScore(i+1,j));
						if(canMoveFrom(i,j,i+1,j+1))PositionList1.insert(i,j,i+1,j+1,this.getScore(i+1,j+1));
						if(canMoveFrom(i,j,i+1,j-1))PositionList1.insert(i,j,i+1,j-1,this.getScore(i+1,j-1));
						if(canMoveFrom(i,j,i,j+1))PositionList1.insert(i,j,i,j+1,this.getScore(i,j+1));
						if(canMoveFrom(i,j,i,j-1))PositionList1.insert(i,j,i,j-1,this.getScore(i,j-1));
						if(canMoveFrom(i,j,i-1,j))PositionList1.insert(i,j,i-1,j,this.getScore(i-1,j));
						if(canMoveFrom(i,j,i-1,j+1))PositionList1.insert(i,j,i-1,j+1,this.getScore(i-1,j+1));
						if(canMoveFrom(i,j,i-1,j-1))PositionList1.insert(i,j,i-1,j-1,this.getScore(i-1,j-1));
						break;
					case 'q':
						for(x=i+1;x<8;x++){if(canMoveFrom(i,j,x,j))PositionList1.insert(i,j,x,j,this.getScore(x,j));else break;}
						for(x=i-1;x>=0;x--){if(canMoveFrom(i,j,x,j))PositionList1.insert(i,j,x,j,this.getScore(x,j));else break;}
						for(y=j+1;y<8;y++){if(canMoveFrom(i,j,i,y))PositionList1.insert(i,j,i,y,this.getScore(i,y));else break;}
						for(y=j-1;y>=0;y--){if(canMoveFrom(i,j,i,y))PositionList1.insert(i,j,i,y,this.getScore(i,y));else break;}
						for(x=i+1,y=j+1;x<8&&y<8;x++,y++){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i+1,y=j-1;x<8&&y>=0;x++,y--){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j-1;x>=0&&y>=0;x--,y--){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						for(x=i-1,y=j+1;x>=0&&y<8;x--,y++){if(canMoveFrom(i,j,x,y))PositionList1.insert(i,j,x,y,this.getScore(x,y));else break;}
						break;
					case 'p':
						if(canMoveFrom(i,j,i-2,j))PositionList1.insert(i,j,i-2,j,this.getScore(i-2,j));
						if(canMoveFrom(i,j,i-1,j))PositionList1.insert(i,j,i-1,j,this.getScore(i-1,j));
						if(canMoveFrom(i,j,i-1,j+1))PositionList1.insert(i,j,i-1,j+1,this.getScore(i-1,j+1));
						if(canMoveFrom(i,j,i-1,j-1))PositionList1.insert(i,j,i-1,j-1,this.getScore(i-1,j-1));
						break;
				}
			}
		}
	}
}
class Chess3D extends TransformGroup
{
	public static final int BLACK=0;
	public static final int YELLOW=1;
	float unit=0.12f;
	TransformGroup[][] TransformGroup_ChessMan=new TransformGroup[8][8];
	TransformGroup[] TransformGroup_YellowQueen=new TransformGroup[8];
	TransformGroup[] TransformGroup_BlackQueen=new TransformGroup[8];
	TransformGroup[][] TransformGroup_HintBall=new TransformGroup[8][8];
	int YellowQueenNumber=8;
	int BlackQueenNumber=8;
	public Chess3D(char[][] chess)
	{
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				Transform3D transform3D=new Transform3D();
				transform3D.setTranslation(new Vector3f(j*unit,0f,(i-4)*unit));
				TransformGroup_ChessMan[i][j]=new TransformGroup(transform3D);
				TransformGroup_ChessMan[i][j].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				TransformGroup TransformGroup2=new TransformGroup();
				switch(chess[i][j])
				{
					case 'K':TransformGroup2=new King(Chess3D.YELLOW);break;
					case 'k':TransformGroup2=new King(Chess3D.BLACK);break;
					case 'Q':TransformGroup2=new Queen(Chess3D.YELLOW);break;
					case 'q':TransformGroup2=new Queen(Chess3D.BLACK);break;
					case 'B':TransformGroup2=new Bishop(Chess3D.YELLOW);break;
					case 'b':TransformGroup2=new Bishop(Chess3D.BLACK);break;
					case 'N':TransformGroup2=new Knight(Chess3D.YELLOW);break;
					case 'n':TransformGroup2=new Knight(Chess3D.BLACK);break;
					case 'R':TransformGroup2=new Rook(Chess3D.YELLOW);break;
					case 'r':TransformGroup2=new Rook(Chess3D.BLACK);break;
					case 'P':TransformGroup2=new Pawn(Chess3D.YELLOW);break;
					case 'p':TransformGroup2=new Pawn(Chess3D.BLACK);break;
				}
				TransformGroup_ChessMan[i][j].addChild(TransformGroup2);
				this.addChild(TransformGroup_ChessMan[i][j]);
			}
		}
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(1000f,1000f,1000f));
		for(int i=0;i<8;i++)
		{
			TransformGroup_YellowQueen[i]=new TransformGroup(transform3D);
			TransformGroup_YellowQueen[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			TransformGroup_YellowQueen[i].addChild(new Queen(Chess3D.YELLOW));
			this.addChild(TransformGroup_YellowQueen[i]);
			TransformGroup_BlackQueen[i]=new TransformGroup(transform3D);
			TransformGroup_BlackQueen[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			TransformGroup_BlackQueen[i].addChild(new Queen(Chess3D.BLACK));
			this.addChild(TransformGroup_BlackQueen[i]);
		}
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				TransformGroup_HintBall[i][j]=new TransformGroup(transform3D);
				TransformGroup_HintBall[i][j].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				TransformGroup_HintBall[i][j].addChild(new HintBall());
				this.addChild(TransformGroup_HintBall[i][j]);
			}
		}
	}
	public void moveFrom(int i0,int j0,int i1,int j1)
	{
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(j1*unit,0f,(i1-4)*unit));
		TransformGroup_ChessMan[i0][j0].setTransform(transform3D);
		transform3D.setTranslation(new Vector3f(1000f,1000f,1000f));
		TransformGroup_ChessMan[i1][j1].setTransform(transform3D);
		TransformGroup_ChessMan[i1][j1]=TransformGroup_ChessMan[i0][j0];
		TransformGroup_ChessMan[i0][j0]=new TransformGroup();
	}
	public void placeQueenAt(int color,int i,int j)
	{
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(1000f,1000f,1000f));
		TransformGroup_ChessMan[i][j].setTransform(transform3D);
		transform3D.setTranslation(new Vector3f(j*unit,0f,(i-4)*unit));
		if(color==Chess3D.BLACK)
		{
			BlackQueenNumber--;
			TransformGroup_BlackQueen[BlackQueenNumber].setTransform(transform3D);
			TransformGroup_ChessMan[i][j]=TransformGroup_BlackQueen[BlackQueenNumber];
		}
		else
		{
			YellowQueenNumber--;
			TransformGroup_YellowQueen[YellowQueenNumber].setTransform(transform3D);
			TransformGroup_ChessMan[i][j]=TransformGroup_YellowQueen[YellowQueenNumber];
		}
	}
	public void placeHintBall(int i,int j)
	{
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(j*unit,0f,(i-4)*unit));
		TransformGroup_HintBall[i][j].setTransform(transform3D);
	}
	public void removeAllHintBalls()
	{
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(1000f,1000f,1000f));
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				TransformGroup_HintBall[i][j].setTransform(transform3D);
			}
		}
	}
}
class ChessBoard extends TransformGroup
{
	public Block Blocks[][]=new Block[8][8];
	public ChessBoard()
	{
		float unit=0.12f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.white));
		Appearance1.setMaterial(Material1);
		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(Color.orange));
		Appearance2.setMaterial(Material2);
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				Transform3D transform3D=new Transform3D();
				transform3D.setTranslation(new Vector3f(j*unit,0f,(i-4)*unit));
				TransformGroup TransformGroup1=new TransformGroup(transform3D);
				Blocks[i][j]=new Block(unit/2,unit/4,unit/2,((i+j)%2==0?Appearance1:Appearance2));
				TransformGroup1.addChild(Blocks[i][j]);
				this.addChild(TransformGroup1);
			}
		}
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,-unit/4,0f));
		this.setTransform(transform3D);
	}
}
class King extends TransformGroup
{
	public King(int color)
	{
		float unit=0.05f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		this.addChild(new RoundPlatform(unit,unit/2,4*unit,Appearance1));
		this.addChild(new Cylinder(1.2f*unit,unit/6,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,4*unit,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RoundPlatform(unit/2,3*unit/4,unit,Appearance1));
		TransformGroup1.addChild(new Cylinder(2*unit/3,unit/8,Appearance1));
		this.addChild(TransformGroup1);
		transform3D.setTranslation(new Vector3f(0f,5*unit,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Box(unit/6,unit,unit/6,Appearance1));
		this.addChild(TransformGroup2);
		transform3D.setTranslation(new Vector3f(0f,5.5f*unit,0f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Box(unit/2,unit/6,unit/6,Appearance1));
		this.addChild(TransformGroup3);
	}
}
class Queen extends TransformGroup
{
	public Queen(int color)
	{
		float unit=0.05f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		this.addChild(new RoundPlatform(unit,unit/2,4*unit,Appearance1));
		this.addChild(new Cylinder(1.2f*unit,unit/6,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,4*unit,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RoundPlatform(unit/2,3*unit/4,unit,Appearance1));
		TransformGroup1.addChild(new Cylinder(2*unit/3,unit/8,Appearance1));
		this.addChild(TransformGroup1);
		transform3D.setTranslation(new Vector3f(0f,5*unit,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Sphere(unit/2,Appearance1));
		this.addChild(TransformGroup2);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(2*unit/3,5.15f*unit,0f));
		for(int i=0;i<12;i++)
		{
			Transform3D Transform3D_Rotation=new Transform3D();
			Transform3D_Rotation.set(new AxisAngle4d(0f,1f,0f,i*Math.PI/6));
			TransformGroup TransformGroup4=new TransformGroup(Transform3D_Rotation);
			TransformGroup TransformGroup3=new TransformGroup(transform3D);
			TransformGroup3.addChild(new Cone(unit/10,unit/3,Appearance1));
			TransformGroup4.addChild(TransformGroup3);
			this.addChild(TransformGroup4);
		}
		transform3D.setTranslation(new Vector3f(0f,5.5f*unit,0f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Sphere(unit/6,Appearance1));
		this.addChild(TransformGroup3);
	}
}
class Bishop extends TransformGroup
{
	public Bishop(int color)
	{
		float unit=0.04f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		this.addChild(new RoundPlatform(unit,unit/2,4*unit,Appearance1));
		this.addChild(new Cylinder(1.2f*unit,unit/6,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,4*unit,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Cylinder(2*unit/3,unit/8,Appearance1));
		this.addChild(TransformGroup1);
		transform3D.setTranslation(new Vector3f(0f,4.5f*unit,0f));
		transform3D.setScale(new Vector3d(0.5,1.0,0.5));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Sphere(unit,Appearance1));
		this.addChild(TransformGroup2);
		transform3D.setTranslation(new Vector3f(0f,5.5f*unit,0f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Sphere(unit/3,Appearance1));
		this.addChild(TransformGroup3);
	}
}
class Knight extends TransformGroup
{
	public Knight(int color)
	{
		float unit=0.04f;
		double size=0.04;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		this.addChild(new RoundPlatform(unit,3*unit/4,unit,Appearance1));
		this.addChild(new Cylinder(1.2f*unit,unit/6,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(-unit,unit,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new KnightBezier(Appearance1,size));
		this.addChild(TransformGroup1);
		transform3D.setTranslation(new Vector3f(-unit/3,3.8f*unit,-unit/4));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Sphere(unit/8,Appearance1));
		this.addChild(TransformGroup2);
		transform3D.setTranslation(new Vector3f(-unit/3,3.8f*unit,unit/4));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Sphere(unit/8,Appearance1));
		this.addChild(TransformGroup3);
		transform3D.rotZ(Math.PI/10);
		transform3D.setScale(new Vector3d(1,1,0.8));
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		transform3D.setTranslation(new Vector3f(0f,2*unit,0f));
		TransformGroup TransformGroup5=new TransformGroup(transform3D);
		TransformGroup5.addChild(new Cone(unit,3*unit,Appearance1));
		TransformGroup4.addChild(TransformGroup5);
		this.addChild(TransformGroup4);
		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(1,1,0.65));
		transform3D.setTranslation(new Vector3f(0f,unit,0f));
		TransformGroup TransformGroup6=new TransformGroup(transform3D);
		TransformGroup6.addChild(new Cylinder(1.35f*unit,unit/8,Appearance1));
		this.addChild(TransformGroup6);
	}
}
class Rook extends TransformGroup
{
	public Rook(int color)
	{
		float unit=0.04f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		this.addChild(new RoundPlatform(unit,unit/2,3*unit,Appearance1));
		this.addChild(new Cylinder(1.2f*unit,unit/6,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,3*unit,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Cylinder(2*unit/3,unit/4,Appearance1));
		this.addChild(TransformGroup1);
		transform3D.setTranslation(new Vector3f(0f,3.1f*unit,0f));
		float PI=(float)Math.PI;
		for(int i=0;i<4;i++)
		{
			TransformGroup TransformGroup2=new TransformGroup(transform3D);
			TransformGroup2.addChild(new RingPlatform(3*unit/4,unit/2,3*unit/4,i*PI/2,(i+1)*PI/2-PI/12,Appearance1));
			this.addChild(TransformGroup2);
		}
	}
}
class Pawn extends TransformGroup
{
	public Pawn(int color)
	{
		float unit=0.03f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		this.addChild(new RoundPlatform(unit,unit/3,2.5f*unit,Appearance1));
		this.addChild(new Cylinder(1.2f*unit,unit/6,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,2.5f*unit,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Cylinder(2*unit/3,unit/8,Appearance1));
		this.addChild(TransformGroup1);
		transform3D.setTranslation(new Vector3f(0f,3*unit,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Sphere(2*unit/3,Appearance1));
		this.addChild(TransformGroup2);
	}
}
class KnightBezier extends TransformGroup
{
	public KnightBezier(Appearance appearance,double size)
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader("Knight.bezier"));
			String stringLine=BufferedReader1.readLine();
			int n=Integer.parseInt(stringLine);
			int[][] index=new int[n][16];
			String stringData="";
			for(int i=0;i<n;i++)
			{
				stringLine=BufferedReader1.readLine();
				int p=0;
				for(int j=0;j<16;j++)
				{
					char c=stringLine.charAt(p++);
					while(c!=','&&p<stringLine.length()){stringData+=c;c=stringLine.charAt(p++);}
					if(p==stringLine.length())stringData+=c;
					index[i][j]=Integer.parseInt(stringData);
					stringData="";
				}
			}
			stringLine=BufferedReader1.readLine();
			int m=Integer.parseInt(stringLine);
			Point3d[] point3ds=new Point3d[m];
			stringData="";
			stringLine=BufferedReader1.readLine();
			int p=0;
			for(int i=0;i<m;i++)
			{
				int k=0;
				double[] d=new double[3];
				while(k<3)
				{
					char c=stringLine.charAt(p++);
					while(c!=','&&p<stringLine.length()){stringData+=c;c=stringLine.charAt(p++);}
					if(p==stringLine.length()){stringData+=c;stringLine=BufferedReader1.readLine();p=0;}
					d[k++]=Double.parseDouble(stringData);
					stringData="";
				}
				point3ds[i]=new Point3d(d[0],d[1],d[2]);
			}
			Point3d[][] controlPoints=new Point3d[4][4];
			for(int k=0;k<n;k++)
			{
				for(int i=0;i<4;i++)
				{
					for(int j=0;j<4;j++)
					{
						controlPoints[i][j]=point3ds[index[k][i*4+j]-1];
					}
				}
				Bezier3D bezier3D=new Bezier3D(controlPoints);
				bezier3D.setAppearance(appearance);
				this.addChild(bezier3D);
			}
		}catch(Exception e){e.printStackTrace();}
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(3*size,3*size,3*size));
		this.setTransform(transform3D);
	}
}
class Bezier2D extends Shape3D
{
	static int H=6;
	public static final int NUMBER=(1<<H);
	int coordinateIndex=0;
	public Point3d[] Coordinates=new Point3d[NUMBER];
	int[] stripCounts=new int[]{NUMBER};
	private void devideControlPoints(int h,Point3d P0,Point3d P1,Point3d P2,Point3d P3)
	{
		if(h>=H)Coordinates[coordinateIndex++]=new Point3d(P3);
		else
		{
			Point3d P01=new Point3d((P0.x+P1.x)/2,(P0.y+P1.y)/2,(P0.z+P1.z)/2);
			Point3d P12=new Point3d((P1.x+P2.x)/2,(P1.y+P2.y)/2,(P1.z+P2.z)/2);
			Point3d P23=new Point3d((P2.x+P3.x)/2,(P2.y+P3.y)/2,(P2.z+P3.z)/2);
			Point3d P0112=new Point3d((P01.x+P12.x)/2,(P01.y+P12.y)/2,(P01.z+P12.z)/2);
			Point3d P1223=new Point3d((P12.x+P23.x)/2,(P12.y+P23.y)/2,(P12.z+P23.z)/2);
			Point3d P01121223=new Point3d((P0112.x+P1223.x)/2,(P0112.y+P1223.y)/2,(P0112.z+P1223.z)/2);
			this.devideControlPoints(h+1,P0,P01,P0112,P01121223);
			this.devideControlPoints(h+1,P01121223,P1223,P23,P3);
		}
	}
	public Bezier2D(Point3d P0,Point3d P1,Point3d P2,Point3d P3)
	{
		coordinateIndex=0;
		this.devideControlPoints(0,P0,P1,P2,P3);	
		LineStripArray LineStripArray1=new LineStripArray(NUMBER,GeometryArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,Coordinates);
		this.setGeometry(LineStripArray1);
	}
}
class Bezier3D extends Shape3D
{
	public Bezier3D(Point3d[][] controlPoints)
	{
		int m=Bezier2D.NUMBER;
		int n=Bezier2D.NUMBER;
		Point3d[] coordinates=new Point3d[m*n];
		int coordinateIndex=0;
		Point3d[] Point3d4=new Point3d[4];
		Bezier2D[] Bezier2D4=new Bezier2D[4];
		for(int k=0;k<4;k++)Bezier2D4[k]=new Bezier2D(controlPoints[k][0],controlPoints[k][1],controlPoints[k][2],controlPoints[k][3]);
		for(int i=0;i<m;i++)
		{
			for(int k=0;k<4;k++)Point3d4[k]=Bezier2D4[k].Coordinates[i];
			Bezier2D bezier2D=new Bezier2D(Point3d4[0],Point3d4[1],Point3d4[2],Point3d4[3]);
			for(int j=0;j<n;j++)coordinates[coordinateIndex++]=bezier2D.Coordinates[j];
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m*n,1,4*m*n);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		coordinateIndex=0;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{			
				int[] coordinateIndices=new int[]
				{
					((i+0)%m)*n+(j+0)%n,
					((i+1)%m)*n+(j+0)%n,
					((i+1)%m)*n+(j+1)%n,
					((i+0)%m)*n+(j+1)%n
				};
				IndexedQuadArray1.setCoordinateIndices(coordinateIndex,coordinateIndices);
				coordinateIndex+=4;
			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
}
class RoundPlatform extends Shape3D
{
	public RoundPlatform(float R,float r,float h,Appearance appearance)
	{
		int m=100;
		int n=4;
		int index=0;
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m*n,1,4*m*n);
		Transform3D transform3D=new Transform3D();
		transform3D.rotY(2*Math.PI/m);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(0f,0f,0f),
			new Point3f(R,0f,0f),
			new Point3f(r,h,0f),
			new Point3f(0f,h,0f)
		};
		for(int i=0;i<m;i++)
		{
			IndexedQuadArray1.setCoordinates(i*n,coordinates);
			for(int j=0;j<n;j++)transform3D.transform(coordinates[j]);
		}
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				int[] coordinateIndices=new int[]
				{
					((i+0)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+1)%n),
					((i+0)%m)*n+((j+1)%n)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
}
class RingPlatform extends Shape3D
{
	public RingPlatform(float R,float r,float h,float w0,float w1,Appearance appearance)
	{
		int m=100;
		int n=4;
		int index=0;
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m*n,1,4*(m-1)*n+4+4);
		Transform3D transform3D=new Transform3D();
		transform3D.rotY(w0);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(r,0f,0f),
			new Point3f(R,0f,0f),
			new Point3f(R,h,0f),
			new Point3f(r,h,0f)
		};
		for(int j=0;j<n;j++)transform3D.transform(coordinates[j]);
		transform3D.rotY((w1-w0)/m);
		for(int i=0;i<m;i++)
		{
			IndexedQuadArray1.setCoordinates(i*n,coordinates);
			for(int j=0;j<n;j++)transform3D.transform(coordinates[j]);
		}
		int[] coordinateIndices=new int[]{0,1,2,3};
		IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
		index+=4;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices=new int[]
				{
					((i+0)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+1)%n),
					((i+0)%m)*n+((j+1)%n)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		coordinateIndices=new int[]
		{
			(m-1)*n+3,
			(m-1)*n+2,
			(m-1)*n+1,
			(m-1)*n+0
		};
		IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
}
class Block extends Shape3D
{
	public Block(float xdim,float ydim,float zdim,Appearance appearance)
	{
		int m=2;
		int n=4;
		int index=0;
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m*n,1,4*m*n+8);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(-xdim,ydim,zdim),
			new Point3f(-xdim,-ydim,zdim),
			new Point3f(-xdim,-ydim,-zdim),
			new Point3f(-xdim,ydim,-zdim),
			new Point3f(xdim,ydim,zdim),
			new Point3f(xdim,-ydim,zdim),
			new Point3f(xdim,-ydim,-zdim),
			new Point3f(xdim,ydim,-zdim)
		};
		IndexedQuadArray1.setCoordinates(0,coordinates);
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				int[] coordinateIndices=new int[]
				{
					((i+0)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+1)%n),
					((i+0)%m)*n+((j+1)%n)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		int[] coordinateIndices=new int[]
		{
			3,2,1,0,
			4,5,6,7
		};
		IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}
class HintBall extends TransformGroup
{
	public HintBall()
	{
		float unit=0.12f;
		Appearance Appearance1=new Appearance();
		Sphere Sphere1=new Sphere(unit/4,Primitive.GENERATE_TEXTURE_COORDS|Sphere.GENERATE_NORMALS|Sphere.GENERATE_TEXTURE_COORDS,1000,Appearance1);
		Appearance1=Sphere1.getAppearance();
		Appearance1.setMaterial(new Material());
		TextureLoader TextureLoader1=new TextureLoader("HintBall.jpg",null);
		Texture Texture1=TextureLoader1.getTexture();
		Appearance1.setTexture(Texture1);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		Appearance1.setTextureAttributes(TextureAttributes1);
		Transform3D transform3D=new Transform3D();
		transform3D.set(new AxisAngle4d(-1f,0f,0f,Math.PI/2));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(Sphere1);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,unit/4,0f));
		this.setTransform(transform3D);
		this.addChild(TransformGroup1);
	}
}