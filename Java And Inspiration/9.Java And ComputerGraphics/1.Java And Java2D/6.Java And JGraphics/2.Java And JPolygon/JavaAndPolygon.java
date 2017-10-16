import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
public class JavaAndPolygon extends Frame implements KeyListener,MouseListener,MouseMotionListener
{
	private int X,Y,X0,Y0,dX,dY;
	private Color color;
	private int[] coordinates;
	private int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private Polygon polygon;
	private Image screenImage;
	public static void main(String[] args)
	{
		Color color=Color.red;
		int[] coordinates=new int[]
		{
			230,100,
			80,100,
			100,200,
			150,300,
			400,400,
			500,400,
			400,100
		};
		new JavaAndPolygon(color,coordinates);
	}
	public JavaAndPolygon(Color color,int[] coordinates)
	{
		this.color=color;
		this.coordinates=coordinates;
		this.setBounds(0,0,screenWidth,screenHeight);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.polygon=new Polygon(coordinates);
		this.screenImage=new BufferedImage(screenWidth,screenHeight,2);
		this.polygon.fill(screenImage,color);
		this.setVisible(true);
	}
	public void paint(Graphics g)
	{
		g.drawImage(screenImage,0,0,null);
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e)
	{
		X=e.getX();
		Y=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		this.dY=e.getY()-Y;
		this.dX=e.getX()-X;
		this.repaint();
	}
	public void mouseReleased(MouseEvent e)
	{
		this.X0+=dX;
		this.Y0+=dY;
		this.dY=0;
		this.dX=0;
	}
	public void keyReleased(KeyEvent e){}
	public void keyPressed(KeyEvent e){}
	public void  keyTyped(KeyEvent e){}
}
class Polygon
{
	private EdgeList[] edgeListTable;
	private EdgeList activeEdgeList;
	private int minY=Integer.MAX_VALUE;
	private int maxY=Integer.MIN_VALUE;
	public Polygon(int[] coordinates)
	{
		int length=coordinates.length;
		for(int i=1;i<length;i+=2)
		{
			if(coordinates[i]>maxY)this.maxY=coordinates[i];
			if(coordinates[i]<minY)this.minY=coordinates[i];
		}
		this.activeEdgeList=new EdgeList();
		this.initializeEdgeListTable(coordinates);
		this.display();
	}
	private void initializeEdgeListTable(int[] coordinates)
	{
		this.edgeListTable=new EdgeList[maxY-minY+1];
		int length=coordinates.length,l=length/2,L=l;
		for(int i=0;i<L;i++)
		{
			int i0=(i-1+l)%l;
			int i1=(i+1)%l;
			double x0=coordinates[i0*2+0]+0.0;
			int y0=coordinates[i0*2+1];
			double x=coordinates[i*2+0]+0.0;
			int y =coordinates[i*2+1];
			double x1=coordinates[i1*2+0]+0.0;
			int y1=coordinates[i1*2+1];
			while(y0==y)
			{
System.out.println("y0==y");
				i=(i-1+l)%l;
				L--;
				i0=(i-1+l)%l;
				i1=(i+1)%l;
				x0=coordinates[i0*2+0]+0.0;
				y0=coordinates[i0*2+1];
				x=coordinates[i*2+0]+0.0;
				y =coordinates[i*2+1];
				x1=coordinates[i1*2+0]+0.0;
				y1=coordinates[i1*2+1];
			}
			if(y1==y)
			{
System.out.println("y1==y");

				this.addEdgeNode(x0,y0,x,y,-1,-1);
				while(y1==y)
				{
					i=(i+1)%l;
					i1=(i+1)%l;
					x=coordinates[i*2+0]+0.0;
					y =coordinates[i*2+1];
					x1=coordinates[i1*2+0]+0.0;
					y1=coordinates[i1*2+1];
				}
				this.addEdgeNode(-1,-1,x,y,x1,y1);
			}
			else this.addEdgeNode(x0,y0,x,y,x1,y1);
		}
	}
	private void addEdgeNode(double x0,int y0,double x,int y,double x1,int y1)
	{
		double dx0=(x0-x)/(y0-y);
		double dx1=(x1-x)/(y1-y);
		if(y0>y&&y1>y)
		{
			if(edgeListTable[y-minY]==null)this.edgeListTable[y-minY]=new EdgeList();
			this.edgeListTable[y-minY].insert(new EdgeNode(x,dx0,y0));
			this.edgeListTable[y-minY].insert(new EdgeNode(x,dx1,y1));
		}
		else if(y0<y&&y1<y);
		else
		{
			if(edgeListTable[y-minY]==null)this.edgeListTable[y-minY]=new EdgeList();
			if(y1<y0)this.edgeListTable[y-minY].insert(new EdgeNode(x,dx0,y0));
			else this.edgeListTable[y-minY].insert(new EdgeNode(x,dx1,y1));
		}
	}
	public void display()
	{
		for(int i=0;i<edgeListTable.length;i++)
		{
			if(edgeListTable[i]!=null)
			{
				System.out.print("y="+(i+minY)+" : ");
				edgeListTable[i].display();
			}
		}
	}
	public void fill(Image image,Color color)
	{
		Graphics g=image.getGraphics();
		g.setColor(color);
		this.activeEdgeList=new EdgeList();
		for(int i=0;i<edgeListTable.length;i++)
		{

			if(edgeListTable[i]!=null)
			{
				while(edgeListTable[i].isNotEmpty())activeEdgeList.insert(edgeListTable[i].first());
			}
			if(activeEdgeList.isNotEmpty())
			{
System.out.print("activeEdgeList y="+(i+minY)+":");
activeEdgeList.display();
				EdgeList newEdgeList=new EdgeList();
				while(activeEdgeList.isNotEmpty())
				{
					EdgeNode n0=activeEdgeList.first();
					EdgeNode n1=activeEdgeList.first();
					int x0=(int)n0.x,x1=(int)n1.x,y=minY+i;
					g.drawLine(x0,y,x1,y);
					if(y<n0.maxY-1)
					{
						n0.x+=n0.dx;
						newEdgeList.insert(n0);
					}
					if(y<n1.maxY-1)
					{
						n1.x+=n1.dx;
						newEdgeList.insert(n1);
					}
				}
				this.activeEdgeList=newEdgeList;
			}
		}
	}
	public static void fillTrangle(Graphics g,int x0,int y0,int x1,int y1,int x2,int y2)
	{
		if(y0==y1&&y1==y2)
		{
			int minX=min(min(x0,x1),x2);
			int maxX=max(max(x0,x1),x2);
			g.drawLine(minX,y0,maxX,y0);
			return;
		}
		int[] X={x0,x1,x2},Y={y0,y1,y2};
		int[] Z=new int[3];
		int[] x=new int[3],y=new int[3],z=new int[3],b=new int[3];
		for(int i=0;i<3;i++)for(int j=i+1;j<3;j++)if(Y[j]<Y[i])b[i]++;else b[j]++;
		for(int i=0;i<3;i++)
		{
			x[b[i]]=X[i];
			y[b[i]]=Y[i];
			z[b[i]]=Z[i];
		}
		double xL=x[0],xR=x[0];
		double dx01=(x[1]-x[0]+0.0)/(y[1]-y[0]);
		double dx02=(x[2]-x[0]+0.0)/(y[2]-y[0]);
		double dx12=(x[2]-x[1]+0.0)/(y[2]-y[1]);
		if(y[1]>y[0])
		{
			for(int i=y[0];i<=y[1];i++)
			{
				g.drawLine((int)xL,i,(int)xR,i);
				xL+=dx01;
				xR+=dx02;
			}
		}
		else 
		{
			xL=x[1];
			xR=x[0];
			g.drawLine((int)xL,y[1],(int)xR,y[0]);
		}
		if(y[2]>y[1])
		{
			for(int i=y[1];i<=y[2];i++)
			{
				g.drawLine((int)xL,i,(int)xR,i);
				xL+=dx12;
				xR+=dx02;
			}
		}
		else g.drawLine(x[1],y[1],x[2],y[2]);
	}
	private boolean equals(double d1,double d2)
	{
		if(Math.abs(d1-d2)<5)return true;
		else return false;
	}
}
class EdgeNode
{
	public double x;
	public double dx;
	public int maxY;
	public EdgeNode next;
	public EdgeNode(double x,double dx,int maxY)
	{
		this.x=x;
		this.dx=dx;
		this.maxY=maxY;
		this.next=null;
	}
}
class EdgeList
{
	private EdgeNode first,last;
	public EdgeList()
	{
		this.first=null;
		this.last=null;
	}
	public void insert(EdgeNode node)
	{
		if(first==null)
		{
			this.first=node;
			this.last=node;
		}
		else if(node.x>=last.x)
		{
			this.last.next=node;
			this.last=node;
		}
		else if(node.x<=first.x)
		{
			node.next=first;
			this.first=node;
		}
		else
		{
			EdgeNode m,n;
			for(n=first,m=n;node.x>n.x;m=n,n=n.next);
			node.next=n;
			m.next=node;
		}
	}
	public EdgeNode first()
	{
		if(first==null)return null;
		EdgeNode node=first;
		this.first=first.next;
		node.next=null;
		return node;
	}
	public boolean isNotEmpty()
	{
		return (first!=null);
	}
	public void display()
	{
		for(EdgeNode n=first;n!=null;n=n.next)
		{
			System.out.print("x="+n.x+","+"dx="+n.dx+","+"maxY="+n.maxY+"; ");
		}
		System.out.println();
	}
}