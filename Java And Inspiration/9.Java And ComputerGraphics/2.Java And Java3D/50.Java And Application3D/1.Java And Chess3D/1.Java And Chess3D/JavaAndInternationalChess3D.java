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
		TextureLoader TextureLoader1=new TextureLoader("InternationalChess8.0.jpg",null);
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
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
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
							System.out.println("chess["+I+"]["+J+"]="+chess[I][J]);
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

class RotateSurface3D extends Shape3D
{
	public double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	public void rotateX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	public void rotateY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	public void rotateZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	public void rotate(Vector3d vector,Vector3d axis,double angle)
	{
		if(angle==0)return;
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rot_Z=angleToY(a.x,a.y);
		rotateZ(a,rot_Z);
		double rot_X=-angleToY(a.z,a.y);
		rotateZ(v,rot_Z);
		rotateX(v,rot_X);
		rotateY(v,angle);
		rotateX(v,-rot_X);
		rotateZ(v,-rot_Z);
		vector.x=v.x;
		vector.y=v.y;
		vector.z=v.z;
	}
	public RotateSurface3D(Point3d[] curvePoints,Vector3d axis,double angle0,double angle1,Appearance appearance)
	{
		int n=curvePoints.length,m=50,v=0;
		double da=(angle1-angle0)/(m-1);
		Vector3d[] P=new Vector3d[n];
		for(int j=0;j<n;j++)
		{
			P[j]=new Vector3d(curvePoints[j].x,curvePoints[j].y,curvePoints[j].z);
			rotate(P[j],axis,angle0);
		}
		Point3d[] coordinates=new Point3d[m*n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinates[i*n+j]=new Point3d(P[j].x,P[j].y,P[j].z);
				rotate(P[j],axis,da);
			}
		}
		int[] coordinateIndices=new int[(m-1)*n*2];v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());	
	}
}

class Pipeline3D extends Shape3D
{
	public Vector3d D(Point3d p1,Point3d p2)
	{
		return new Vector3d(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
	}
	public Vector3d MID(Vector3d v1,Vector3d v2)
	{
		return new Vector3d((v2.x+v1.x)/2,(v2.y+v1.y)/2,(v2.z+v1.z)/2);
	}
	public double MUL(Vector3d v1,Vector3d v2)
	{
		return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
	}
	public double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	double lengthOfVector(Vector3d v)
	{
		return Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
	}
	public Vector3d crossVector(Vector3d v0,Vector3d v1)
	{
		double x=v0.y*v1.z-v1.y*v0.z;
		double y=v0.z*v1.x-v1.z*v0.x;
		double z=v0.x*v1.y-v1.x*v0.y;
		return new Vector3d(x,y,z);
	}
	public double angleToVector(Vector3d v0,Vector3d v1)
	{
		double l0=lengthOfVector(v0);
		double l1=lengthOfVector(v1);
		return Math.acos((v0.x*v1.x+v0.y*v1.y+v0.z*v1.z)/(l0*l1));
	}
	public void rotateX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	public void rotateY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	public void rotateZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	public void rotate1(Vector3d vector,Vector3d axis,double angle)
	{
		if(angle==0)return;
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rot_Z=angleToY(a.x,a.y);
		rotateZ(a,rot_Z);
		double rot_X=-angleToY(a.z,a.y);
		rotateZ(v,rot_Z);
		rotateX(v,rot_X);
		rotateY(v,angle);
		rotateX(v,-rot_X);
		rotateZ(v,-rot_Z);
		vector.x=v.x;
		vector.y=v.y;
		vector.z=v.z;
	}
	public void rotate(Vector3d vector,Vector3d axis,double angle)
	{
		if(angle==0)return;
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		double l=lengthOfVector(axis);
		Vector3d u=new Vector3d(axis.x/l,axis.y/l,axis.z/l);
		double uv=MUL(u,v);
		Vector3d n=crossVector(u,v);
		double cosA=Math.cos(angle);
		double sinA=Math.sin(angle);
		vector.x=cosA*v.x+(1-cosA)*uv*u.x+sinA*n.x;
		vector.y=cosA*v.y+(1-cosA)*uv*u.y+sinA*n.y;
		vector.z=cosA*v.z+(1-cosA)*uv*u.z+sinA*n.z;
	}
	public Pipeline3D(Point3d[] points,double[] sizes,Appearance appearance)
	{
		int m=points.length,n=50,v=0;
		Point3d[] coordinates=new Point3d[(m+2)*n];
		int[] coordinateIndices=new int[(m+1)*n*2];
		double angle=2*Math.PI/(n-1);
		Vector3d dF0=new Vector3d(0,1,0),axis=dF0,dT=dF0;
		Vector3d[] d=new Vector3d[n];
		for(int j=0;j<n;j++)
		{
			double x=Math.cos(angle*j);
			double y=0;
			double z=Math.sin(angle*j);
			d[j]=new Vector3d(x,y,z);
		}
		for(int j=0;j<n;j++)coordinates[v++]=points[0];
		for(int i=0;i<m;i++)
		{
			Point3d P=points[i];
			double R=sizes[i];
			if(i==m-1)angle=0;
			else
			{
				Vector3d dF1=D(points[i],points[i+1]);
				Vector3d dF=i==0?dF1:MID(dF0,dF1);
				angle=angleToVector(dT,dF);
				axis=crossVector(dT,dF);
				dF0=dF1;
				dT=dF;	
			}
			for(int j=0;j<n;j++)
			{
				rotate(d[j],axis,angle);
				coordinates[v++]=new Point3d(P.x+R*d[j].x,P.y+R*d[j].y,P.z+R*d[j].z);
			}
		}
		for(int j=0;j<n;j++)coordinates[v++]=points[m-1];
		v=0;
		for(int i=1;i<m+2;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m+1];
		for(int i=0;i<m+1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());	
	}
}

class NurbsCurve3D extends Shape3D
{
	Point3d[] coordinates;
	public NurbsCurve3D(Point3d[] ctrlPoints,double[] weights,double[] knots,int order)
	{
		int l=100,m=order,n=ctrlPoints.length;
		double du=1.0/(l-1);
		double[][] B=this.getParameterMatrix(knots,l,m,n);
		this.coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,weights,B,i);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[] weights,double[][] B,int k)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0,w=0;
		for(int i=0;i<n;i++)
		{
			x+=ctrlPoints[i].x*B[k][i]*weights[i];
			y+=ctrlPoints[i].y*B[k][i]*weights[i];
			z+=ctrlPoints[i].z*B[k][i]*weights[i];
			w+=B[k][i]*weights[i];
		}
		return new Point3d(x/w,y/w,z/w);
	}
	public Point3d[] getCoordinates()
	{
		return this.coordinates;
	}
	public double[] getCoordinatesY(double k, double b)
	{
		int l=this.coordinates.length;
		double[] coordinatesY=new double[l];
		for(int i=0;i<l;i++)coordinatesY[i]=k*this.coordinates[i].y+b;
		return coordinatesY;
	}
	private double[][] getParameterMatrix(double[] knots,int curvePointsLength,int order,int ctrlPointsLength)
	{
		double[] u=knots;
		int l=curvePointsLength;
		int m=order,n=ctrlPointsLength;
		double d=(u[n]-u[m])/(l-1);
		double[][] b=new double[l][n];
		double[][] B=new double[m+1][n+1];
		for(int k=0;k<l;k++)
		{
			double t=u[m]+k*d;
			for(int j=0;j<=n;j++)B[0][j]=isBetween(t,u[j],u[j+1])?1:0;
			for(int i=1;i<=m;i++)
			{
				for(int j=0;j<n;j++)
				{
					double du0=u[j+0+i]-u[j+0];
					double du1=u[j+1+i]-u[j+1];
					double U0=du0==0?0:(t-u[j])/du0;
					double U1=du1==0?0:(u[j+1+i]-t)/du1;
					B[i][j]=B[i-1][j]*U0+B[i-1][j+1]*U1;
				}
			}
			for(int j=0;j<n;j++)b[k][j]=B[m][j];
		}
		return b;	
	}
	public static double[] getStandardUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		double du=1.0/(l-1);
		for(int i=0;i<l;i++)knots[i]=i*du;
		return knots;
	}
	public static double[] getBezierUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*order;
		double du=1.0/(k-1);
		for(int i=0;i<order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=i*du;
		for(int i=0;i<order;i++)knots[c++]=1;
		return knots;
	}
	public static double[] getSemiUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*(order+1);
		for(int i=0;i<=order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=0.5;
		for(int i=0;i<=order;i++)knots[c++]=1;
		return knots;
	}
	public  static double distance(Point3d point0,Point3d point1)
	{
		double dx=point1.x-point0.x;
		double dy=point1.y-point0.y;
		double dz=point1.z-point0.z;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}
	public  static double[] getRiesenfeldNurbsKnots(Point3d[] ctrlPoints,int order)
	{
		int l=ctrlPoints.length+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*order;
		double[] u=new double[k];u[0]=0;
		for(int i=1;i<k;i++)u[i]=u[i-1]+distance(ctrlPoints[i-1],ctrlPoints[i]);
		for(int i=0;i<order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=u[i]/u[k-1];
		for(int i=0;i<order;i++)knots[c++]=1;
		return knots;
	}
	boolean isBetween(double x,double x0,double x1)
	{
		return x==0?x0==0:x0<x&&x<=x1;
	}
}

class King extends TransformGroup
{
	public King(int color)
	{
		float unit=0.024f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
			new Point3d(0.0,2.275,0),
			new Point3d(0.17500000000000004,2.27,0),
			new Point3d(0.18000000000000016,2.2649999999999997,0),
			new Point3d(0.30500000000000016,2.235,0),
			new Point3d(0.31000000000000005,2.23,0),
			new Point3d(0.31500000000000017,2.195,0),
			new Point3d(0.2200000000000002,1.9499999999999997,0),
			new Point3d(0.21500000000000008,1.875,0),
			new Point3d(0.2200000000000002,1.8699999999999999,0),
			new Point3d(0.2300000000000002,1.805,0),
			new Point3d(0.19500000000000006,1.77,0),
			new Point3d(0.18000000000000016,1.7449999999999999,0),
			new Point3d(0.18500000000000005,1.7399999999999998,0),
			new Point3d(0.19500000000000006,1.7049999999999998,0),
			new Point3d(0.27,1.65,0),
			new Point3d(0.28,1.625,0),
			new Point3d(0.2450000000000001,1.5799999999999998,0),
			new Point3d(0.2500000000000002,1.5749999999999997,0),
			new Point3d(0.30500000000000016,1.555,0),
			new Point3d(0.32000000000000006,1.515,0),
			new Point3d(0.31500000000000017,1.5099999999999998,0),
			new Point3d(0.31000000000000005,1.4949999999999999,0),
			new Point3d(0.18000000000000016,1.47,0),
			new Point3d(0.18000000000000016,1.3399999999999999,0),
			new Point3d(0.18500000000000005,1.335,0),
			new Point3d(0.19000000000000017,1.1549999999999998,0),
			new Point3d(0.19500000000000006,1.15,0),
			new Point3d(0.2100000000000002,0.8499999999999999,0),
			new Point3d(0.21500000000000008,0.845,0),
			new Point3d(0.2300000000000002,0.7799999999999998,0),
			new Point3d(0.2350000000000001,0.7749999999999999,0),
			new Point3d(0.28500000000000014,0.6749999999999998,0),
			new Point3d(0.29000000000000004,0.6699999999999999,0),
			new Point3d(0.3750000000000002,0.52,0),
			new Point3d(0.3900000000000001,0.5049999999999999,0),
			new Point3d(0.41000000000000014,0.45500000000000007,0),
			new Point3d(0.41500000000000004,0.3899999999999997,0),
			new Point3d(0.3400000000000001,0.32499999999999973,0),
			new Point3d(0.33000000000000007,0.29000000000000004,0),
			new Point3d(0.38500000000000023,0.24499999999999966,0),
			new Point3d(0.5550000000000002,0.20500000000000007,0),
			new Point3d(0.5700000000000001,0.18999999999999995,0),
			new Point3d(0.5850000000000002,0.17999999999999972,0),
			new Point3d(0.6000000000000001,0.14500000000000002,0),
			new Point3d(0.6050000000000002,0.08999999999999986,0),
			new Point3d(0.6000000000000001,0.08499999999999996,0),
			new Point3d(0.6050000000000002,0.08000000000000007,0),
			new Point3d(0.6000000000000001,0.04499999999999993,0),
			new Point3d(0.54,0.004999999999999893,0),
			new Point3d(0.5450000000000002,0.0,0),
			new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k*unit,k*unit,k*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,10*unit,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Box(unit/6,unit,unit/6,Appearance1));
		this.addChild(TransformGroup2);
		transform3D.setTranslation(new Vector3f(0f,10.5f*unit,0f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Box(unit/2,unit/6,unit/6,Appearance1));
		this.addChild(TransformGroup3);
	}
}
class Queen extends TransformGroup
{
	public Queen(int color)
	{
		float unit=0.024f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,2.44,0),
                                        new Point3d(0.025000000000000133,2.435,0),
                                        new Point3d(0.030000000000000027,2.43,0),
                                        new Point3d(0.050000000000000044,2.42,0),
                                        new Point3d(0.07499999999999996,2.395,0),
                                        new Point3d(0.08000000000000007,2.345,0),
                                        new Point3d(0.07499999999999996,2.34,0),
                                        new Point3d(0.07499999999999996,2.295,0),
                                        new Point3d(0.11499999999999999,2.275,0),
                                        new Point3d(0.14500000000000002,2.255,0),
                                        new Point3d(0.19999999999999996,2.23,0),
                                        new Point3d(0.28500000000000014,2.22,0),
                                        new Point3d(0.29000000000000004,2.215,0),
                                        new Point3d(0.29499999999999993,2.2,0),
                                        new Point3d(0.29000000000000004,2.195,0),
                                        new Point3d(0.19500000000000006,1.9500000000000002,0),
                                        new Point3d(0.18999999999999995,1.875,0),
                                        new Point3d(0.19500000000000006,1.87,0),
                                        new Point3d(0.20500000000000007,1.8050000000000002,0),
                                        new Point3d(0.16999999999999993,1.77,0),
                                        new Point3d(0.15500000000000003,1.745,0),
                                        new Point3d(0.16000000000000014,1.74,0),
                                        new Point3d(0.16999999999999993,1.705,0),
                                        new Point3d(0.2450000000000001,1.65,0),
                                        new Point3d(0.2550000000000001,1.625,0),
                                        new Point3d(0.21999999999999997,1.58,0),
                                        new Point3d(0.2250000000000001,1.575,0),
                                        new Point3d(0.28,1.555,0),
                                        new Point3d(0.29499999999999993,1.515,0),
                                        new Point3d(0.29000000000000004,1.51,0),
                                        new Point3d(0.28500000000000014,1.495,0),
                                        new Point3d(0.15500000000000003,1.47,0),
                                        new Point3d(0.15500000000000003,1.34,0),
                                        new Point3d(0.16000000000000014,1.335,0),
                                        new Point3d(0.16500000000000004,1.155,0),
                                        new Point3d(0.16999999999999993,1.15,0),
                                        new Point3d(0.18500000000000005,0.8500000000000001,0),
                                        new Point3d(0.18999999999999995,0.845,0),
                                        new Point3d(0.20500000000000007,0.78,0),
                                        new Point3d(0.20999999999999996,0.7749999999999999,0),
                                        new Point3d(0.26,0.675,0),
                                        new Point3d(0.2650000000000001,0.6699999999999999,0),
                                        new Point3d(0.3500000000000001,0.52,0),
                                        new Point3d(0.365,0.5049999999999999,0),
                                        new Point3d(0.385,0.45500000000000007,0),
                                        new Point3d(0.3900000000000001,0.3900000000000001,0),
                                        new Point3d(0.31499999999999995,0.3250000000000002,0),
                                        new Point3d(0.30499999999999994,0.29000000000000004,0),
                                        new Point3d(0.3600000000000001,0.2450000000000001,0),
                                        new Point3d(0.53,0.20500000000000007,0),
                                        new Point3d(0.5450000000000002,0.18999999999999995,0),
                                        new Point3d(0.56,0.18000000000000016,0),
                                        new Point3d(0.575,0.14500000000000002,0),
                                        new Point3d(0.5800000000000001,0.08999999999999986,0),
                                        new Point3d(0.575,0.08499999999999996,0),
                                        new Point3d(0.5800000000000001,0.08000000000000007,0),
                                        new Point3d(0.575,0.04499999999999993,0),
                                        new Point3d(0.5150000000000001,0.004999999999999893,0),
                                        new Point3d(0.52,0.0,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k*unit,k*unit,k*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);
	}
}
class Bishop extends TransformGroup
{
	public Bishop(int color)
	{
		float unit=0.024f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,2.17,0),
                                        new Point3d(0.08000000000000007,2.17,0),
                                        new Point3d(0.08500000000000019,2.16,0),
                                        new Point3d(0.10000000000000009,2.15,0),
                                        new Point3d(0.11500000000000021,2.105,0),
                                        new Point3d(0.1100000000000001,2.075,0),
                                        new Point3d(0.1050000000000002,2.0700000000000003,0),
                                        new Point3d(0.0950000000000002,2.05,0),
                                        new Point3d(0.07500000000000018,2.025,0),
                                        new Point3d(0.10000000000000009,2.005,0),
                                        new Point3d(0.1100000000000001,2.0,0),
                                        new Point3d(0.11500000000000021,1.99,0),
                                        new Point3d(0.16000000000000014,1.955,0),
                                        new Point3d(0.18500000000000005,1.9300000000000002,0),
                                        new Point3d(0.2300000000000002,1.8650000000000002,0),
                                        new Point3d(0.26000000000000023,1.79,0),
                                        new Point3d(0.27,1.725,0),
                                        new Point3d(0.2650000000000001,1.7200000000000002,0),
                                        new Point3d(0.2550000000000001,1.63,0),
                                        new Point3d(0.2450000000000001,1.6150000000000002,0),
                                        new Point3d(0.2200000000000002,1.565,0),
                                        new Point3d(0.21500000000000008,1.56,0),
                                        new Point3d(0.20500000000000007,1.54,0),
                                        new Point3d(0.20000000000000018,1.5350000000000001,0),
                                        new Point3d(0.18500000000000005,1.51,0),
                                        new Point3d(0.19500000000000006,1.48,0),
                                        new Point3d(0.19000000000000017,1.475,0),
                                        new Point3d(0.16000000000000014,1.425,0),
                                        new Point3d(0.17000000000000015,1.4000000000000001,0),
                                        new Point3d(0.2100000000000002,1.395,0),
                                        new Point3d(0.2250000000000001,1.355,0),
                                        new Point3d(0.30000000000000004,1.335,0),
                                        new Point3d(0.31500000000000017,1.3,0),
                                        new Point3d(0.29500000000000015,1.26,0),
                                        new Point3d(0.28,1.25,0),
                                        new Point3d(0.2550000000000001,1.245,0),
                                        new Point3d(0.2500000000000002,1.24,0),
                                        new Point3d(0.19000000000000017,1.23,0),
                                        new Point3d(0.18500000000000005,1.225,0),
                                        new Point3d(0.15500000000000003,1.22,0),
                                        new Point3d(0.15500000000000003,1.19,0),
                                        new Point3d(0.16000000000000014,1.185,0),
                                        new Point3d(0.15500000000000003,1.18,0),
                                        new Point3d(0.16500000000000004,1.065,0),
                                        new Point3d(0.17000000000000015,1.06,0),
                                        new Point3d(0.17000000000000015,0.99,0),
                                        new Point3d(0.17500000000000004,0.9850000000000001,0),
                                        new Point3d(0.19500000000000006,0.8900000000000001,0),
                                        new Point3d(0.20000000000000018,0.885,0),
                                        new Point3d(0.2100000000000002,0.845,0),
                                        new Point3d(0.21500000000000008,0.8400000000000001,0),
                                        new Point3d(0.2200000000000002,0.815,0),
                                        new Point3d(0.2250000000000001,0.81,0),
                                        new Point3d(0.2450000000000001,0.7450000000000001,0),
                                        new Point3d(0.2500000000000002,0.74,0),
                                        new Point3d(0.26000000000000023,0.7250000000000001,0),
                                        new Point3d(0.29000000000000004,0.645,0),
                                        new Point3d(0.3350000000000002,0.5700000000000001,0),
                                        new Point3d(0.3800000000000001,0.51,0),
                                        new Point3d(0.3900000000000001,0.5050000000000001,0),
                                        new Point3d(0.42500000000000004,0.4500000000000002,0),
                                        new Point3d(0.42000000000000015,0.4249999999999998,0),
                                        new Point3d(0.41500000000000004,0.41999999999999993,0),
                                        new Point3d(0.40000000000000013,0.38500000000000023,0),
                                        new Point3d(0.405,0.3799999999999999,0),
                                        new Point3d(0.38500000000000023,0.375,0),
                                        new Point3d(0.3700000000000001,0.3599999999999999,0),
                                        new Point3d(0.33000000000000007,0.3250000000000002,0),
                                        new Point3d(0.32000000000000006,0.29000000000000004,0),
                                        new Point3d(0.3750000000000002,0.2450000000000001,0),
                                        new Point3d(0.5450000000000002,0.20500000000000007,0),
                                        new Point3d(0.56,0.18999999999999995,0),
                                        new Point3d(0.5750000000000002,0.18000000000000016,0),
                                        new Point3d(0.5900000000000001,0.14500000000000002,0),
                                        new Point3d(0.5950000000000002,0.08999999999999986,0),
                                        new Point3d(0.5900000000000001,0.08499999999999996,0),
                                        new Point3d(0.5950000000000002,0.08000000000000007,0),
                                        new Point3d(0.5900000000000001,0.04499999999999993,0),
                                        new Point3d(0.53,0.004999999999999893,0),
                                        new Point3d(0.5350000000000001,0.0,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3,k1=3.8;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit,k*unit,k1*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);
	}
}
class Knight extends TransformGroup
{
	public Knight(int color)
	{
		float unit=0.02f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,0.5050000000000001,0),
                                        new Point3d(0.3800000000000001,0.5050000000000001,0),
                                        new Point3d(0.40000000000000013,0.45500000000000007,0),
                                        new Point3d(0.40000000000000013,0.4249999999999998,0),
                                        new Point3d(0.3900000000000001,0.41999999999999993,0),
                                        new Point3d(0.38500000000000023,0.375,0),
                                        new Point3d(0.3700000000000001,0.3599999999999999,0),
                                        new Point3d(0.33000000000000007,0.3250000000000002,0),
                                        new Point3d(0.32000000000000006,0.29000000000000004,0),
                                        new Point3d(0.3750000000000002,0.2450000000000001,0),
                                        new Point3d(0.5450000000000002,0.20500000000000007,0),
                                        new Point3d(0.56,0.18999999999999995,0),
                                        new Point3d(0.5750000000000002,0.18000000000000016,0),
                                        new Point3d(0.5900000000000001,0.14500000000000002,0),
                                        new Point3d(0.5950000000000002,0.08999999999999986,0),
                                        new Point3d(0.5900000000000001,0.08499999999999996,0),
                                        new Point3d(0.5950000000000002,0.08000000000000007,0),
                                        new Point3d(0.5900000000000001,0.04499999999999993,0),
                                        new Point3d(0.53,0.004999999999999893,0),
                                        new Point3d(0.5350000000000001,0.0,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3,k1=3.8;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k*unit,k*unit,k*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);



		Point3d[] ctrlPoints=
		{
			new Point3d(0.0,0.0,0.0),
			new Point3d(0.0,0.25,0.0),
			new Point3d(0.15,0.6,0.0),
			new Point3d(0.3,0.8,0.0),
			new Point3d(0.6,1.2,0.0),
			new Point3d(0.2,1.5,0.0),
			new Point3d(-0.2,1.0,0.0)
		};
		double[] weights=
		{
			1,
			1,
			1,
			1,
			1,
			1.5,
			1
		};
		int order=3;double[] knots=NurbsCurve3D.getBezierUniformNurbsKnots(ctrlPoints.length,order);
		NurbsCurve3D nurbsCurve3D=new NurbsCurve3D(ctrlPoints,weights,knots,order);
		points=nurbsCurve3D.getCoordinates();
		ctrlPoints=new Point3d[]
		{
			new Point3d(0.0,0.5,0.0),
			new Point3d(0.5,0.4,0.0),
			new Point3d(1.2,0.25,0.0),
			new Point3d(1.4,0.15,0.0),
			new Point3d(1.6,0.2,0.0),
			new Point3d(2.0,0.1,0.0)
		};
		weights=new double[]
		{
			1,
			1,
			1,
			1,
			6,
			1
		};
		knots=NurbsCurve3D.getBezierUniformNurbsKnots(ctrlPoints.length,order);
		nurbsCurve3D=new NurbsCurve3D(ctrlPoints,weights,knots,order);
		double[] sizes=nurbsCurve3D.getCoordinatesY(1,0);

		SharedGroup SharedGroup1=new SharedGroup();
		SharedGroup1.addChild(new Pipeline3D(points,sizes,Appearance1));

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit*1.2,k1*unit,k1*unit/1.2));
		transform3D.setTranslation(new Vector3d(0,k1*unit/1.8,0));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Link(SharedGroup1));
		this.addChild(TransformGroup2);

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit*1.4,k1*unit,unit/1.5));
		transform3D.setTranslation(new Vector3d(unit/1.25,k1*unit/1.45,0));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Link(SharedGroup1));
		this.addChild(TransformGroup3);

		SharedGroup SharedGroup2=new SharedGroup();
		SharedGroup2.addChild(new Sphere(unit/4,Appearance1));
		
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0.5*unit,7.2*unit,0.5*unit));
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		TransformGroup4.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup4);

		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0.5*unit,7.2*unit,-0.5*unit));
		TransformGroup TransformGroup5=new TransformGroup(transform3D);
		TransformGroup5.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup5);

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(1.5,2.5,0.25));
		transform3D.setRotation(new AxisAngle4d(1,0,1,-Math.PI/4));
		transform3D.setTranslation(new Vector3d(1.8*unit,8*unit,-0.8*unit));
		TransformGroup TransformGroup6=new TransformGroup(transform3D);
		TransformGroup6.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup6);

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(1.5,2.5,0.25));
		transform3D.setRotation(new AxisAngle4d(-1,0,1,-Math.PI/4));
		transform3D.setTranslation(new Vector3d(1.8*unit,8*unit,0.8*unit));
		TransformGroup TransformGroup7=new TransformGroup(transform3D);
		TransformGroup7.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup7);
	}
}
class Rook extends TransformGroup
{
	public Rook(int color)
	{
		float unit=0.02f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,1.4149999999999998,0),
                                        new Point3d(0.395,1.4149999999999998,0),
                                        new Point3d(0.40000000000000013,1.4049999999999998,0),
                                        new Point3d(0.405,1.4,0),
                                        new Point3d(0.40000000000000013,1.295,0),
                                        new Point3d(0.3900000000000001,1.285,0),
                                        new Point3d(0.40000000000000013,1.2699999999999998,0),
                                        new Point3d(0.3350000000000002,1.18,0),
                                        new Point3d(0.3250000000000002,1.16,0),
                                        new Point3d(0.30000000000000004,1.1449999999999998,0),
                                        new Point3d(0.30500000000000016,1.0899999999999999,0),
                                        new Point3d(0.30000000000000004,1.0849999999999997,0),
                                        new Point3d(0.29000000000000004,0.8749999999999998,0),
                                        new Point3d(0.29500000000000015,0.8699999999999999,0),
                                        new Point3d(0.30000000000000004,0.7949999999999999,0),
                                        new Point3d(0.30500000000000016,0.7899999999999998,0),
                                        new Point3d(0.31000000000000005,0.7249999999999999,0),
                                        new Point3d(0.31500000000000017,0.7199999999999998,0),
                                        new Point3d(0.3250000000000002,0.6599999999999999,0),
                                        new Point3d(0.33000000000000007,0.6549999999999998,0),
                                        new Point3d(0.3350000000000002,0.6149999999999998,0),
                                        new Point3d(0.3450000000000002,0.5999999999999999,0),
                                        new Point3d(0.3600000000000001,0.5749999999999997,0),
                                        new Point3d(0.375,0.54,0),
                                        new Point3d(0.3800000000000001,0.5099999999999998,0),
                                        new Point3d(0.385,0.5049999999999999,0),
                                        new Point3d(0.405,0.4549999999999996,0),
                                        new Point3d(0.41000000000000014,0.4349999999999996,0),
                                        new Point3d(0.42500000000000004,0.41999999999999993,0),
                                        new Point3d(0.43500000000000005,0.4099999999999997,0),
                                        new Point3d(0.4700000000000002,0.3799999999999999,0),
                                        new Point3d(0.4750000000000001,0.33999999999999986,0),
                                        new Point3d(0.4800000000000002,0.33499999999999996,0),
                                        new Point3d(0.4800000000000002,0.3049999999999997,0),
                                        new Point3d(0.4700000000000002,0.2999999999999998,0),
                                        new Point3d(0.4800000000000002,0.2849999999999997,0),
                                        new Point3d(0.4850000000000001,0.21999999999999975,0),
                                        new Point3d(0.55,0.20499999999999963,0),
                                        new Point3d(0.5650000000000002,0.18999999999999995,0),
                                        new Point3d(0.5800000000000001,0.17999999999999972,0),
                                        new Point3d(0.5950000000000002,0.14500000000000002,0),
                                        new Point3d(0.6000000000000001,0.08999999999999986,0),
                                        new Point3d(0.5950000000000002,0.08499999999999996,0),
                                        new Point3d(0.6000000000000001,0.07999999999999963,0),
                                        new Point3d(0.5950000000000002,0.04499999999999993,0),
                                        new Point3d(0.5350000000000001,0.004999999999999893,0),
                                        new Point3d(0.54,0.0,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3,k1=3.8;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit,k*unit,k1*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,6*unit,0f));
		float PI=(float)Math.PI;
		for(int i=0;i<4;i++)
		{
			TransformGroup TransformGroup2=new TransformGroup(transform3D);
			TransformGroup2.addChild(new RingPlatform(1.55f*unit,unit,unit,i*PI/2,(i+1)*PI/2-PI/12,Appearance1));
			this.addChild(TransformGroup2);
		}
	}
}
class Pawn extends TransformGroup
{
	public Pawn(int color)
	{
		float unit=0.014f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		if(color==Chess3D.BLACK)Material1.setDiffuseColor(new Color3f(Color.black));
		else Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,2.055,0),
                                        new Point3d(0.1499999999999999,2.05,0),
                                        new Point3d(0.15500000000000003,2.045,0),
                                        new Point3d(0.24499999999999988,2.025,0),
                                        new Point3d(0.25,2.02,0),
                                        new Point3d(0.28,2.01,0),
                                        new Point3d(0.2849999999999999,2.005,0),
                                        new Point3d(0.33000000000000007,1.98,0),
                                        new Point3d(0.375,1.935,0),
                                        new Point3d(0.395,1.8900000000000001,0),
                                        new Point3d(0.405,1.855,0),
                                        new Point3d(0.41500000000000004,1.755,0),
                                        new Point3d(0.4099999999999999,1.75,0),
                                        new Point3d(0.405,1.675,0),
                                        new Point3d(0.3999999999999999,1.67,0),
                                        new Point3d(0.405,1.665,0),
                                        new Point3d(0.3999999999999999,1.6600000000000001,0),
                                        new Point3d(0.385,1.615,0),
                                        new Point3d(0.3799999999999999,1.6099999999999999,0),
                                        new Point3d(0.3600000000000001,1.57,0),
                                        new Point3d(0.345,1.555,0),
                                        new Point3d(0.345,1.55,0),
                                        new Point3d(0.21500000000000008,1.49,0),
                                        new Point3d(0.20999999999999996,1.485,0),
                                        new Point3d(0.2749999999999999,1.44,0),
                                        new Point3d(0.28,1.435,0),
                                        new Point3d(0.31499999999999995,1.35,0),
                                        new Point3d(0.31000000000000005,1.345,0),
                                        new Point3d(0.30000000000000004,1.3,0),
                                        new Point3d(0.15500000000000003,1.3,0),
                                        new Point3d(0.1499999999999999,1.295,0),
                                        new Point3d(0.1399999999999999,1.22,0),
                                        new Point3d(0.15500000000000003,1.05,0),
                                        new Point3d(0.15999999999999992,1.045,0),
                                        new Point3d(0.17500000000000004,0.9450000000000001,0),
                                        new Point3d(0.17999999999999994,0.94,0),
                                        new Point3d(0.19999999999999996,0.8600000000000001,0),
                                        new Point3d(0.20500000000000007,0.855,0),
                                        new Point3d(0.22999999999999998,0.7849999999999999,0),
                                        new Point3d(0.2350000000000001,0.78,0),
                                        new Point3d(0.27,0.7150000000000001,0),
                                        new Point3d(0.2749999999999999,0.71,0),
                                        new Point3d(0.385,0.5100000000000002,0),
                                        new Point3d(0.375,0.45500000000000007,0),
                                        new Point3d(0.31000000000000005,0.3500000000000001,0),
                                        new Point3d(0.31000000000000005,0.33000000000000007,0),
                                        new Point3d(0.345,0.31000000000000005,0),
                                        new Point3d(0.4650000000000001,0.28500000000000014,0),
                                        new Point3d(0.47,0.2799999999999998,0),
                                        new Point3d(0.55,0.2400000000000002,0),
                                        new Point3d(0.5549999999999999,0.23499999999999988,0),
                                        new Point3d(0.5900000000000001,0.20500000000000007,0),
                                        new Point3d(0.6000000000000001,0.16999999999999993,0),
                                        new Point3d(0.595,0.10499999999999998,0),
                                        new Point3d(0.5900000000000001,0.10000000000000009,0),
                                        new Point3d(0.55,0.015000000000000124,0),
                                        new Point3d(0.43500000000000005,0.004999999999999893,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3,k1=3.8;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit,k*unit,k1*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);
	}
}
