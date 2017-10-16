import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;
public class JavaAndGraph2D
{
	public static void main(String[] args)
	{
		Frame_Graph2D frame_Graph2D=new Frame_Graph2D();
		frame_Graph2D.setVisible(true);
	}
}
class Frame_Graph2D extends Frame
{
	private Graph2D graph2D;
	public Frame_Graph2D()
	{
		this.setBounds(100,100,300,100);
		int length=10;
		double maxedges=100;
		double density=0.5;
		this.graph2D=this.getRandomGraph2D(length,maxedges,density);
		this.graph2D.setBounds(50,60,1300,700);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	private Graph2D getRandomGraph2D(int length,double maxedges,double density)
	{
		String[] vertices=new String[length];
		double[][] edges=new double[length][length];
		for(int i=0;i<length;i++)
		{
			vertices[i]="v"+i;
			for(int j=i+1;j<length;j++)
			{
				double e=maxedges*Math.random();
				if(e>density*maxedges)e=Graph2D.MAX;
				edges[i][j]=e;
				edges[j][i]=e;

			}

		}
		return new Graph2D(vertices,edges);
	}
	public void paint(Graphics g)
	{
		this.graph2D.paint(g);
	}
}
class Graph2D
{
	private String[] vertices;
	private double[][] edges;
	private Point[] locations;
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
		this.diameter=width/length/10+10;
		this.locations=this.getRandomLocations();
	}
	public void setLocations(Point[] locations)
	{
		this.locations=locations;
	}
	public void paint(Graphics g)
	{
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