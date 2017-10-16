import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
public class JavaAndPolygonCut extends Frame implements KeyListener,MouseListener,MouseMotionListener
{
	private int X,Y,X0,Y0,dX,dY,windowX0,windowY0,windowX,windowY;
	private Color color;
	private int[] coordinates;
	private int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private Polygon polygon;
	private Image screenImage;
	private List list;
	public static void main(String[] args)
	{
		Color color=Color.red;
		new JavaAndPolygonCut(color);
	}
	public JavaAndPolygonCut(Color color)
	{
		this.list=new List();
		this.color=color;
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
		this.setBackground(Color.black);
		this.screenImage=new BufferedImage(screenWidth,screenHeight,2);
		//this.test();
		this.setVisible(true);
	}
	void test()
	{
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
		int minX=90,maxX=450,minY=110,maxY=350;
		coordinates=Polygon.cut(coordinates,minX,maxX,minY,maxY);
for(int i=0;i<coordinates.length;i++)System.out.println("> "+coordinates[i]);
		this.polygon=new Polygon(coordinates);
		this.polygon.fill(screenImage,color);
	}
	public void paint(Graphics g)
	{
		g.drawImage(screenImage,0,0,null);
		g.setColor(new Color(0.0f,0.0f,0.5f,0.5f));
		g.fillRect(windowX0,windowY0,windowX,windowY);
		g.setColor(new Color(0,0,200));
		g.drawRect(windowX0,windowY0,windowX,windowY);
		Node n=list.first();
		while(n!=null)
		{
			if(n.next!=null)
			{
				g.setColor(Color.white);
				g.drawLine(n.x,n.y,n.next.x,n.next.y);
			}
			else return;
			n=n.next;
		}
	}
	public void mouseClicked(MouseEvent e)
	{
		this.windowX0=0;
		this.windowY0=0;
		this.windowX=0;
		this.windowY=0;
		this.list.add(e.getX(),e.getY());
		this.repaint();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e)
	{
		X=e.getX();
		Y=e.getY();
		this.windowX0=X;
		this.windowY0=Y;
	}
	public void mouseDragged(MouseEvent e)
	{
		this.windowY=e.getY()-Y;
		this.windowX=e.getX()-X;
		this.repaint();
	}
	public void mouseReleased(MouseEvent e)
	{
		this.X0+=dX;
		this.Y0+=dY;
		this.dY=0;
		this.dX=0;
		
	}
	public void keyReleased(KeyEvent e)
	{
		String t=KeyEvent.getKeyText(e.getKeyCode());
		if(t.equals("Enter"))
		{
			this.coordinates=list.toIntegerArray();
			this.polygon=new Polygon(coordinates);
			this.polygon.fill(screenImage,color);
			this.repaint();
			this.list=new List();
		}
		else if(t.equals("Delete"))
		{
			this.windowX0=0;
			this.windowY0=0;
			this.windowX=0;
			this.windowY=0;
			this.list=new List();
			this.screenImage=new BufferedImage(screenWidth,screenHeight,2);
			this.repaint();
		}
		if(t.equals("Backspace"))
		{
			this.coordinates=list.toIntegerArray();
			this.coordinates=Polygon.cut(coordinates,windowX0,windowX0+windowX,windowY0,windowY0+windowY);
			this.polygon=new Polygon(coordinates);
			this.polygon.fill(screenImage,color);
			this.repaint();
			this.list=new List();
		}
	}
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
	}
	public static int[] cut(int[] coordinates,int minX,int maxX,int minY,int maxY)
	{
		coordinates=cut(0,coordinates,minX,maxX,minY,maxY);
		coordinates=cut(1,coordinates,minX,maxX,minY,maxY);
		coordinates=cut(2,coordinates,minX,maxX,minY,maxY);
		coordinates=cut(3,coordinates,minX,maxX,minY,maxY);
		return coordinates;
	}
	private static int[] cut(int direction,int[] coordinates,int minX,int maxX,int minY,int maxY)
	{
		List list=new List();
		int length=coordinates.length/2;
		for(int i=0;i<length;i++)
		{
			int x=coordinates[i*2+0];
			int y=coordinates[i*2+1];
			if(!isInBounds(x,y,minX,maxX,minY,maxY))
			{
				int x0=coordinates[((length+i-1)%length)*2+0];
				int y0=coordinates[((length+i-1)%length)*2+1];
				int x1=coordinates[((length+i+1)%length)*2+0];
				int y1=coordinates[((length+i+1)%length)*2+1];
				if(direction==0)
				{
					if(x<minX)
					{
						int Y=getIntersectionXY(0,minX,minY,maxY,x,y,x0,y0);
						if(Y!=-1)list.add(minX,Y);
						Y=getIntersectionXY(0,minX,minY,maxY,x,y,x1,y1);
						if(Y!=-1)list.add(minX,Y);
					}
					else list.add(x,y);
				}
				else if(direction==1)
				{
					if(x>maxX)
					{
						int Y=getIntersectionXY(1,maxX,minY,maxY,x,y,x0,y0);
						if(Y!=-1)list.add(maxX,Y);
						Y=getIntersectionXY(1,maxX,minY,maxY,x,y,x1,y1);
						if(Y!=-1)list.add(maxX,Y);
					}
					else list.add(x,y);
				}
				else if(direction==2)
				{	
					if(y<minY)
					{
						int X=getIntersectionXY(2,minY,minX,maxX,x,y,x0,y0);
						if(X!=-1)list.add(X,minY);
						X=getIntersectionXY(2,minY,minX,maxX,x,y,x1,y1);
						if(X!=-1)list.add(X,minY);
					}
					else list.add(x,y);
				}
				else 
				{
					if(y>maxY)
					{
						int X=getIntersectionXY(3,maxY,minX,maxX,x,y,x0,y0);
						if(X!=-1)list.add(X,maxY);
						X=getIntersectionXY(3,maxY,minX,maxX,x,y,x1,y1);
						if(X!=-1)list.add(X,maxY);
					}
					else list.add(x,y);
				}
			}
			else list.add(x,y);
		}
		return list.toIntegerArray();
	}
	private static boolean isInBounds(int x,int y,int minX,int maxX,int minY,int maxY)
	{
		return (minX<=x&&x<=maxX&&minY<=y&&y<=maxY);
	}
	private static int getIntersectionXY(int direction,int minMax,int min,int max,int x0,int y0,int x1,int y1)
	{
		List list=new List();
		int minX=0,maxX=1,minY=2,maxY=3;
		if(direction==minX||direction==maxX)
		{
			minY=min;
			maxY=max;
			if(x0>x1){int t=x0;x0=x1;x1=t;t=y0;y0=y1;y1=t;}
			double y=y0,dy=(y1-y0+0.0)/(x1-x0);
			for(int x=x0;x<=x1;x++)
			{
				if(x==minMax)return (int)(y+0.5);
				y+=dy;

			}
		}
		if(direction==minY||direction==maxY)
		{
			minX=min;
			maxX=max;
			if(y0>y1){int t=x0;x0=x1;x1=t;t=y0;y0=y1;y1=t;}
			double x=x0,dx=(x1-x0+0.0)/(y1-y0);
			for(int y=y0;y<=y1;y++)
			{
				if(y==minMax)return (int)(x+0.5);
				x+=dx;
			}
		}
		return -1;
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
//System.out.print("activeEdgeList y="+(i+minY)+":");
//activeEdgeList.display();
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



class Node
{
	public int x;
	public int y;
	public Node next;
	public Node(int x,int y)
	{
		this.x=x;
		this.y=y;
		this.next=null;
	}
}
class List
{
	Node first,last,current;
	int length=0;
	public List()
	{
		this.first=this.last=null;
		this.current=this.first;
	}
	public void add(int x,int y)
	{
		this.length++;
		Node node=new Node(x,y);
		if(first==null)
		{
			this.first=this.last=node;
			this.current=this.first;
		}
		else 
		{
			this.last.next=node;
			this.last=node;
		}
	}
	int[] toIntegerArray()
	{
		int[] array=new int[length*2];
		Node n=first;
		for(int i=0;i<length;i++,n=n.next)
		{
			array[i*2+0]=n.x;
			array[i*2+1]=n.y;
		}
		return array;
	}
	public int length()
	{
		return length;
	}
	public Node first()
	{
		this.current=first;
		return this.next();
	}
	public Node next()
	{
		if(current==null)return null;
		Node n=current;
		this.current=current.next;
		return n;
	}
}