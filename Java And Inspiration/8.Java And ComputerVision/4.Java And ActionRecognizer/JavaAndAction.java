import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndAction
{
	public static void main(String[] args)
	{
		Frame_Action Frame_Action1=new Frame_Action();
		Frame_Action1.setVisible(true);
	}
}
class Frame_Action extends Frame
{
	private Action Action1;
	public Frame_Action()
	{
		this.setSize(450,600);
		this.Action1=new Action("Action1");
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
		this.Action1.paint(g);
	}
}
class Point
{
	public int x,y;
	public Point(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
}
class Action
{
	private Color pointColor,lineColor;
	private Point[] interestPoints;
	private int[] interestPointsIndices;
	public Action(String fileName)
	{
		try
		{
			BufferedReader reader=new BufferedReader(new FileReader(new File("Action\\"+fileName+".action")));
			int n=0;
			String s=reader.readLine();
			for(;s!=null;n++,s=reader.readLine());
			reader=new BufferedReader(new FileReader(new File("Action\\"+fileName+".action")));
			this.interestPoints=new Point[n/2];
			for(int i=0;i<n/2;i++)
			{
				s=reader.readLine();
				int x=Integer.parseInt(s);
				s=reader.readLine();
				int y=Integer.parseInt(s);
				this.interestPoints[i]=new Point(x,y);
			}
			reader=new BufferedReader(new FileReader(new File("Action\\drawLine.action")));
			n=0;
			s=reader.readLine();
			for(;s!=null;n++,s=reader.readLine());
			this.interestPointsIndices=new int[n];
			reader=new BufferedReader(new FileReader(new File("Action\\drawLine.action")));
			for(int i=0;i<n;i++)
			{
				int index=Integer.parseInt(reader.readLine());
				this.interestPointsIndices[i]=index;
			}
		}catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		int r=10;
		for(int i=0;i<interestPoints.length;i++)
		{
			int x=interestPoints[i].x;
			int y=interestPoints[i].y;
			g.setColor(Color.white);
			g.drawString(""+i,x,y);
			this.drawPoint(g,x,y,r);
		}
		for(int i=0;i<interestPointsIndices.length/2;i++)
		{
			int i0=interestPointsIndices[2*i+0];
			int i1=interestPointsIndices[2*i+1];
			int x0=interestPoints[i0].x;
			int y0=interestPoints[i0].y;
			int x1=interestPoints[i1].x;
			int y1=interestPoints[i1].y;
			g.setColor(Color.white);
			g.drawLine(x0+r,y0+r,x1+r,y1+r);
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
	ChessboardNode root;
	public ChessboardTree(int M,int N)
	{
		this.M=M;
		this.N=N;
		this.root=new ChessboardNode(M,N);
	}
	public void insert(String text,int[][] pointArray)
	{
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
	public void drawTree(Graphics g,int x,int y)
	{

	}
	public void println()
	{
		System.out.println("Chessboard Tree");
		this.root.println(0);
	}
}