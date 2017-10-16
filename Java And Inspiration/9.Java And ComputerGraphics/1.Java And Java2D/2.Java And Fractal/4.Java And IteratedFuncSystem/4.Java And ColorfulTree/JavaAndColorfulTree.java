import java.awt.*;
import java.awt.event.*;
public class JavaAndColorfulTree 
{
	public static void main(String[] args)
	{
		Frame_ColorfulTree Frame_ColorfulTree1=new Frame_ColorfulTree();
		Frame_ColorfulTree1.setVisible(true);
	}
}
class Frame_ColorfulTree extends Frame
{
	static QuadQueue queue;
	static IFSCode[] IFS=new IFSCode[4];
	static int dx=400;
	static int dy=400;
	static int MaxDepth=9;
	int sizeX=100;
	int sizeY=100;
	public Frame_ColorfulTree()
	{
		IFS[0]=new IFSCode(0.04,0,-0.23,-0.65,-0.08,0.26,sizeX,sizeY);
		IFS[1]=new IFSCode(0.61,0,0,0.31,0.07,2.5,sizeX,sizeY);
		IFS[2]=new IFSCode(-0.65,0.29,0.3,0.48,1.14,0.09,sizeX,sizeY);
		IFS[3]=new IFSCode(0.64,-0.3,0.16,0.56,-0.56,0.4,sizeX,sizeY);
		queue=new QuadQueue();
		queue.enQueue(0,0,sizeX,0,sizeX,sizeY,0,sizeY,new Color(0,255,0));
		getColorfulTreeQuads();
		this.setBackground(Color.BLACK);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	private static void getColorfulTreeQuads()
	{
		if(queue==null||!queue.isNotEmpty())return;
		for(int i=0;i<MaxDepth;i++)
		{
			int length=queue.length;
			for(int j=0;j<length;j++)
			{
				Quad quad=queue.deQueue();
				Quad quad0=quad.getIFSQuad(IFS[0]);
				Quad quad1=quad.getIFSQuad(IFS[1]);
				Quad quad2=quad.getIFSQuad(IFS[2]);
				Quad quad3=quad.getIFSQuad(IFS[3]);
				quad0.color=new Color(0,255,0);
				quad1.color=new Color(0,255,255);
				quad2.color=new Color(0,0,255);
				quad3.color=new Color(255,255,0);
				queue.enQueue(quad0);
				queue.enQueue(quad1);
				queue.enQueue(quad2);
				queue.enQueue(quad3);
			}
		}
	}
	public void paint(Graphics g)
	{
		if(queue!=null)queue.drawAllQuads(g,dx,dy);
	}
}
class IFSCode
{
	public double a,b,c,d,e,f;
	public IFSCode(double a,double b,double c,double d,double e,double f,int sizeX,int sizeY)
	{
		this.a=a;
		this.b=b;
		this.c=c;
		this.d=d;
		this.e=e*sizeX;
		this.f=f*sizeY;
	}
}
class QuadQueue
{
	Quad front;
	Quad rear;
	public int length;
	int Height=3;
	public QuadQueue()
	{
		front=rear=null;
		length=0;
	}
	public void enQueue(int x0,int y0,int x1,int y1,int x2,int y2,int x3,int y3,Color color)
	{
		Quad quad=new Quad(x0,y0,x1,y1,x2,y2,x3,y3,color);
		if(front==null)front=rear=quad;
		else
		{
			rear.next=quad;
			rear=quad;
		}
		length++;
	}
	public void enQueue(Quad quad)
	{
		if(quad==null)return;
		if(front==null)front=rear=quad;
		else
		{
			rear.next=quad;
			rear=quad;
		}
		length++;
	}
	public Quad deQueue()
	{
		Quad quad=front;
		if(front!=null){front=front.next;length--;}
		return quad;
	}
	public boolean isNotEmpty()
	{
		return (front!=null);
	}
	public void drawAllQuads(Graphics g,int dx,int dy)
	{
		for(Quad q=front;q!=null;q=q.next)this.fillQuad(g,q.x0+dx,-q.y0+dy,q.x1+dx,-q.y1+dy,q.x2+dx,-q.y2+dy,q.x3+dx,-q.y3+dy,q.color);
	}
	public void fillQuad(Graphics g,int x0,int y0,int x1,int y1,int x2,int y2,int x3,int y3,Color color)
	{
		int x01=(x0+x1)/2;
		int y01=(y0+y1)/2;
		int x12=(x1+x2)/2;
		int y12=(y1+y2)/2;
		int x20=(x2+x0)/2;
		int y20=(y2+y0)/2;
		g.setColor(color);
		g.drawLine(x0,y0,x1,y1);
		g.drawLine(x1,y1,x2,y2);
		g.drawLine(x2,y2,x3,y3);
		g.drawLine(x3,y3,x0,y0);
		drawMiddleLine(g,x0,y0,x1,y1,x2,y2,x3,y3,0);
		drawMiddleLine(g,x1,y1,x2,y2,x3,y3,x0,y0,0);
	}
	private void drawMiddleLine(Graphics g,int x0,int y0,int x1,int y1,int x2,int y2,int x3,int y3,int h)
	{
		if(h>Height)return;
		int x01=(x0+x1)/2;
		int y01=(y0+y1)/2;
		int x23=(x2+x3)/2;
		int y23=(y2+y3)/2;
		g.drawLine(x01,y01,x23,y23);
		drawMiddleLine(g,x0,y0,x01,y01,x23,y23,x3,y3,h+1);
		drawMiddleLine(g,x01,y01,x1,y1,x2,y2,x23,y23,h+1);
	}
}
class Quad
{
	public int x0,y0,x1,y1,x2,y2,x3,y3;
	public Color color;
	public Quad next;
	public Quad(int x0,int y0,int x1,int y1,int x2,int y2,int x3,int y3,Color color)
	{
		this.x0=x0;
		this.x1=x1;
		this.x2=x2;
		this.x3=x3;
		this.y0=y0;
		this.y1=y1;
		this.y2=y2;
		this.y3=y3;
		this.color=new Color(color.getRed(),color.getGreen(),color.getBlue());
	}
	public Quad getIFSQuad(IFSCode IFS)
	{
		int X0=(int)(IFS.a*x0+IFS.b*y0+IFS.e);
		int Y0=(int)(IFS.c*x0+IFS.d*y0+IFS.f);
		int X1=(int)(IFS.a*x1+IFS.b*y1+IFS.e);
		int Y1=(int)(IFS.c*x1+IFS.d*y1+IFS.f);
		int X2=(int)(IFS.a*x2+IFS.b*y2+IFS.e);
		int Y2=(int)(IFS.c*x2+IFS.d*y2+IFS.f);
		int X3=(int)(IFS.a*x3+IFS.b*y3+IFS.e);
		int Y3=(int)(IFS.c*x3+IFS.d*y3+IFS.f);
		return new Quad(X0,Y0,X1,Y1,X2,Y2,X3,Y3,color);
	}
}