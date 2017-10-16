import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;
public class JavaAndPrim
{
	public static void main(String[] args)
	{
		Frame_Prim frame_Prim=new Frame_Prim();
		frame_Prim.setVisible(true);
	}
}
class Frame_Prim extends Frame
{
	private Graph2D graph2D;
	public Frame_Prim()
	{
		this.setBounds(100,100,300,100);
		String[] vertices=new String[]{"A","B","C","D","E"};
		String[] from=new String[]{"A","A","A","C","E", "B","B","B"};
		String[] to=new String[]{"E","D","C","D","D","D","E","C"};
		double[] weights=new double[]{50,80,300,60,90,75,200,70};
		Point[] locations=new Point[]{new Point(100,100),new Point(600,100),new Point(1000,300),new Point(600,700),new Point(100,500)};
		int length=10;
		double maxedges=100;
		double density=0.5;
		this.graph2D=this.getGraph2D(vertices,from,to,weights);
		this.graph2D.setLocations(locations);
		this.graph2D.getMinimumSpanningTreeByPrim();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}	
	private Graph2D getGraph2D(String[] vertices,String[] from,String[] to,double[] weights)
	{
		int length=vertices.length;
		double[][] edges=new double[length][length];
		for(int i=0;i<length;i++)for(int j=0;j<length;j++)if(i!=j)edges[i][j]=Graph2D.MAX;
		int l=weights.length;
		for(int i=0;i<l;i++)
		{
			int f=this.getIndex(from[i],vertices);
			int t=this.getIndex(to[i],vertices);
			edges[f][t]=weights[i];
			edges[t][f]=weights[i];
		}
		return new Graph2D(vertices,edges);
	}
	private int getIndex(String vertex,String[] vertices)
	{
		int l=vertices.length;
		for(int i=0;i<l;i++)if(vertex.equals(vertices[i]))return i;
		return -1;
	}
	public void paint(Graphics g)
	{
		this.graph2D.paint(g);
		g.setColor(Color.blue);
		this.graph2D.drawMinimumSpanningTree(g);
	}
}
class Graph2D
{
	private String[] vertices;
	private double[][] edges;
	private Point[] locations;
	private int[] minimumSpanningTree;
	private int length;
	private int startX;
	private int startY;
	private int width;
	private int height;
	private int diameter;
	public static final double MAX=Double.MAX_VALUE;
	public Graph2D(String[] vertices,double[][] edges)
	{
		this.length=vertices.length;
		this.vertices=new String[length];
		this.edges=new double[length][length];
		this.diameter=30;
		for(int i=0;i<length;i++)
		{
			this.vertices[i]=vertices[i];
			for(int j=0;j<length;j++)
			{
				this.edges[i][j]=edges[i][j];
			}
		}
	}
	private Point[] getRandomLocations()
	{
		Point[] verticesLocations=new Point[length];
		Point[][] edgesMidPoints=new Point[length][length];
		double interval=diameter*6.0;
		for(int i=0;i<length;i++)
		{
			int x=(int)((width-2*diameter)*Math.random())+startX+2*diameter;
			int y=(int)((height-2*diameter)*Math.random())+startY+2*diameter;
			for(int j=0;isClose(verticesLocations,edgesMidPoints,i,x,y,interval)&&j<10;j++)
			{
				x=(int)((width-2*diameter)*Math.random())+startX+2*diameter;
				y=(int)((height-2*diameter)*Math.random())+startX+2*diameter;
			}
			verticesLocations[i]=new Point(x,y);
			for(int j=0;j<i;j++)
			{
				if(edges[i][j]==MAX)continue;
				int xij=(x+verticesLocations[j].x)/2;
				int yij=(y+verticesLocations[j].y)/2;
				edgesMidPoints[i][j]=new Point(xij,yij);
			}
		}
		return verticesLocations;
	}
	public void getMinimumSpanningTreeByPrim()
	{
		double[] distance=new double[length];
		boolean[] select=new boolean[length];
		int[] pre=new int[length];
		int[] MST=new int[length*2];
		int n=0,v=0;
		for(int i=0;i<length;i++)
		{
			distance[i]=edges[0][i];
			select[i]=false;
			pre[i]=0;
		}
		select[0]=true;
		for(int i=0;i<length;i++)
		{
			double min=MAX;
			for(int j=0;j<length;j++)
			{
				if(!select[j]&&distance[j]<min)
				{
					v=j;
					min=distance[j];
				}
			}
			MST[n++]=pre[v];
			MST[n++]=v;
			select[v]=true;
			for(int j=0;j<length;j++)
			{
				if(!select[j]&&edges[v][j]<distance[j])
				{
					pre[j]=v;
					distance[j]=edges[v][j];
				}
			}
		}
		this.minimumSpanningTree=MST;
	}
	private boolean isClose(Point[] verticesLocations,Point[][] edgesMidPoints,int index,int x,int y,double interval)
	{
		for(int i=0;i<index;i++)
		{
			int dx=verticesLocations[i].x-x;
			int dy=verticesLocations[i].y-y;
			double dl=Math.sqrt(dx*dx+dy*dy);
			if(dl<interval)return true;
			int midX=(verticesLocations[i].x+x)/2;
			int midY=(verticesLocations[i].y+y)/2;
			for(int j=0;j<i;j++)
			{
				if(edgesMidPoints[i][j]==null)continue;
				dx=edgesMidPoints[i][j].x-midX;
				dy=edgesMidPoints[i][j].y-midY;
				dl=Math.sqrt(dx*dx+dy*dy);
				if(dl<interval)return true;
			}
		}
		return false;
	}
	public void setBounds(int startX,int startY,int width,int height)
	{
		this.startX=startX;
		this.startY=startY;
		this.width=width;
		this.height=height;
		this.diameter=width/length/10+20;
		this.locations=this.getRandomLocations();
	}
	public void setLocations(Point[] locations)
	{
		this.locations=locations;
	}
	public void drawMinimumSpanningTree(Graphics g)
	{
		if(minimumSpanningTree==null)return;
		int l=minimumSpanningTree.length;
		for(int i=0;i<l;i+=2)
		{
			int i0=minimumSpanningTree[i+0];
			int i1=minimumSpanningTree[i+1];
			Point p0=locations[i0];
			Point p1=locations[i1];
			g.drawLine(p0.x,p0.y,p1.x,p1.y);
		}
	}
	public void paint(Graphics g)
	{
		if(locations==null)return;
		g.drawRect(startX,startY,width,height);
		g.setFont(new Font("",Font.BOLD,15));
		for(int i=0;i<length;i++)
		{
			int xi=locations[i].x;
			int yi=locations[i].y;
			int li=vertices[i].length();
			for(int j=i+1;j<length;j++)
			{
				if(edges[i][j]!=MAX)
				{
					int xj=locations[j].x;
					int yj=locations[j].y;
					g.drawLine(xi,yi,xj,yj);
					String weightij=(int)edges[i][j]+"";
					int lij=weightij.length();
					int xij=(xi+xj)/2;
					int yij=(yi+yj)/2;
					g.drawString(weightij,xij-3*lij,yij+5);
				}
			}
			g.setColor(Color.white);
			g.fillOval(xi-diameter/2,yi-diameter/2,diameter,diameter);
			g.setColor(Color.black);
			g.drawOval(xi-diameter/2,yi-diameter/2,diameter,diameter);
			g.drawString(vertices[i],xi-3*li,yi+5);
		}
	}
}