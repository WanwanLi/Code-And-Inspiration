import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndChessboardTree
{
	public static void main(String[] args)
	{
		Frame_ChessboardTree Frame_ChessboardTree1=new Frame_ChessboardTree();
		Frame_ChessboardTree1.setVisible(true);
	}
	private static void test()
	{
		int M=3,N=3;
		ChessboardTree ChessboardTree1=new ChessboardTree(M,N);
		String text="text1";
		int[][] pointArray=new int[][]
		{
			{0,0},
			{1,1},
			{2,2}
		};
		int minX=0,minY=0;
		ChessboardTree1.insert(text,pointArray);
		ChessboardTree1.println();
		System.out.println("Search Results:"+ChessboardTree1.search(pointArray));
	}
}
class Frame_ChessboardTree extends Frame
{
	ChessboardTree chessboardTree;
	public Frame_ChessboardTree()
	{
		int M=3,N=3;
		this.chessboardTree=new ChessboardTree(M,N);
		String text="text1";
		int[][] pointArray=new int[][]
		{
			{0,0},
			{1,1},
			{2,2}
		};
		this.chessboardTree.insert(text,pointArray);
		text="text2";
		pointArray=new int[][]
		{
			{10,20},
			{32,21},
			{52,72},
			{43,64},
			{77,81}
		};
		this.chessboardTree.insert(text,pointArray);
		text="text3";
		pointArray=new int[][]
		{
			{60,30},
			{82,21},
			{72,72},
			{63,34},
			{17,51}
		};
		this.chessboardTree.insert(text,pointArray);
		text="text4";
		pointArray=new int[][]
		{
			{60,30},
			{92,81},
			{80,72},
			{88,24},
			{69,61},
			{50,61},
			{90,61}
		};
		this.chessboardTree.insert(text,pointArray);
		this.setBackground(Color.black);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void paint(Graphics g)
	{
		this.chessboardTree.paint(g,800,100,10);
	}
}
class ChessboardNode
{
	private int M,N;
	public String text;
	public ChessboardNode[][] chessboard;
	public ChessboardNode(int M,int N)
	{
		this.M=M;
		this.N=N;
		this.chessboard=new ChessboardNode[M][N];
	}
	public void paint(Graphics g,int x,int y,int w,int height,int level)
	{
		if(this.text!=null)g.drawString(text,x+w*(N/2)-w/2,y+(M+1)*w+w/2);
		for(int i=0;i<M;i++)
		{
			for(int j=0;j<N;j++)
			{
				int x0=x+j*w;
				int y0=y+i*w;
				g.setColor(Color.white);
				g.drawRect(x0,y0,w,w);
				if(chessboard[i][j]!=null)
				{
					int r=w/2;
					this.drawPoint(g,x0,y0,r);
					int dj=j-N/2;
					int x1=x+dj*(M+1)*(height-level)*w;
					int y1=y+(M+5)*w;
					g.setColor(Color.white);
					g.drawLine(x0+r,y0+r,x1,y1);
					chessboard[i][j].paint(g,x1,y1,w,height,level+1);
				}
			}
		}
	}
	private void drawPoint(Graphics g,int x0,int y0,int r)
	{
		int x=x0+r;
		int y=y0+r;
		g.setColor(Color.blue);
		for(int i=-r;i<=r;i++)
		{
			for(int j=-r;j<=r;j++)
			{
				double t=(i*i+j*j+0.0)/(r*r);
				if(t<1.0)
				{
					double k=1.0-t;
					int c=(int)(255*k);
					int b=c+50;
					if(b>255)b=255;
					g.setColor(new Color(c,c,b));
					g.drawLine(x+i,y+j,x+i,y+j);
				}
			}
		}
	}
	public void println(int level)
	{
		if(this.text!=null)
		{
			for(int t=0;t<level;t++)System.out.print("\t");
			System.out.println(text);
		}
		for(int i=0;i<M;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(chessboard[i][j]==null)continue;
				for(int t=0;t<level;t++)System.out.print("\t");
				System.out.println("Node("+i+","+j+")");
				for(int t=0;t<level;t++)System.out.print("\t");
				System.out.println("{");
				chessboard[i][j].println(level+1);
				for(int t=0;t<level;t++)System.out.print("\t");
				System.out.println("}");
			}
		}
	}
}
class ChessboardTree
{
	private int M=1,N=1,X=0,Y=1;
	private int minX,minY,maxX,maxY;
	private ChessboardNode root;
	private int height=0;
	public ChessboardTree(int M,int N)
	{
		this.M=M;
		this.N=N;
		this.root=new ChessboardNode(M,N);
	}
	public void insert(String text,int[][] pointArray)
	{
		if(pointArray.length>height)this.height=pointArray.length;
		ChessboardNode n=root;
		this.getMinMax(pointArray);
		for(int p=0;p<pointArray.length;p++)
		{
			int x=pointArray[p][X];
			int y=pointArray[p][Y];
			int i=(M-1)*(y-minY)/(maxY-minY);
			int j=(N-1)*(x-minX)/(maxX-minX);
			if(n.chessboard[i][j]==null)
			{
				n.chessboard[i][j]=new ChessboardNode(M,N);
			}
			n=n.chessboard[i][j];
			if(p==pointArray.length-1)n.text=text;
		}
	}
	private void getMinMax(int[][] pointArray)
	{
		this.minX=Integer.MAX_VALUE;
		this.minY=Integer.MAX_VALUE;
		this.maxX=-Integer.MAX_VALUE;
		this.maxY=-Integer.MAX_VALUE;
		for(int p=0;p<pointArray.length;p++)
		{
			int x=pointArray[p][X];
			int y=pointArray[p][Y];
			if(x<minX)this.minX=x;
			if(y<minY)this.minY=y;
			if(x>maxX)this.maxX=x;
			if(y>maxY)this.maxY=y;
		}
	}
	public String search(int[][] pointArray)
	{
		String text=null;
		ChessboardNode n=root;
		this.getMinMax(pointArray);
		for(int p=0;p<pointArray.length;p++)
		{
			int x=pointArray[p][X];
			int y=pointArray[p][Y];
			int i=(M-1)*(y-minY)/(maxY-minY);
			int j=(N-1)*(x-minX)/(maxX-minX);
			if(n.chessboard[i][j]==null)return "null";
			else n=n.chessboard[i][j];
			if(p==pointArray.length-1)text=n.text;
		}
		return text==null?"null":text;
	}
	public void paint(Graphics g,int x,int y,int w)
	{
		this.root.paint(g,x,y,w,height,0);
	}
	public void println()
	{
		System.out.println("Chessboard Tree");
		this.root.println(0);
	}
}