import java.awt.*;
import java.applet.*;
public class JavaAndKoch extends Applet
{
	static LineQueue queue;
	static IFSCode[] IFS=new IFSCode[4];
	static int dx=400;
	static int dy=100;
	static int MaxDepth=5;
	static double cos60=Math.cos(Math.PI/3);
	static double sin60=Math.sin(Math.PI/3);
	static double cos_60=Math.cos(-Math.PI/3);
	static double sin_60=Math.sin(-Math.PI/3);
	static int size=500;
	public void init()
	{
		IFS[0]=new IFSCode(1.0/3.0,0,0,1.0/3.0,0,0,size);
		IFS[1]=new IFSCode(1.0/3.0*cos60,-1.0/3.0*sin60,1.0/3.0*sin60,1.0/3.0*cos60,1.0/3.0,0,size);
		IFS[2]=new IFSCode(1.0/3.0*cos_60,-1.0/3.0*sin_60,1.0/3.0*sin_60,1.0/3.0*cos_60,1.0/2.0,1.0/3.0*sin60,size);
		IFS[3]=new IFSCode(1.0/3.0,0,0,1.0/3.0,2.0/3.0,0,size);
		queue=new LineQueue();
		queue.enQueue(0,0,size,0,Color.BLACK);
		getKochLines();
	}
	private static void getKochLines()
	{
		if(queue==null||!queue.isNotEmpty())return;
		for(int i=0;i<MaxDepth;i++)
		{
			int length=queue.length;
			for(int j=0;j<length;j++)
			{
				Line line=queue.deQueue();
				Line Line0=line.getIFSLine(IFS[0]);
				Line Line1=line.getIFSLine(IFS[1]);
				Line Line2=line.getIFSLine(IFS[2]);
				Line Line3=line.getIFSLine(IFS[3]);
				queue.enQueue(Line0);
				queue.enQueue(Line1);
				queue.enQueue(Line2);
				queue.enQueue(Line3);
			}
		}
	}
	public void paint(Graphics g)
	{
		if(queue!=null)queue.drawAllLines(g,dx,dy);
	}
}
class IFSCode
{
	public double a,b,c,d,e,f;
	public IFSCode(double a,double b,double c,double d,double e,double f,int size)
	{
		this.a=a;
		this.b=b;
		this.c=c;
		this.d=d;
		this.e=e*size;
		this.f=f*size;
	}
}
class LineQueue
{
	Line front;
	Line rear;
	public int length;
	public LineQueue()
	{
		front=rear=null;
		length=0;
	}
	public void enQueue(int x0,int y0,int x1,int y1,Color color)
	{
		Line Line=new Line(x0,y0,x1,y1,color);
		if(front==null)front=rear=Line;
		else
		{
			rear.next=Line;
			rear=Line;
		}
		length++;
	}
	public void enQueue(Line Line)
	{
		if(Line==null)return;
		if(front==null)front=rear=Line;
		else
		{
			rear.next=Line;
			rear=Line;
		}
		length++;
	}
	public Line deQueue()
	{
		Line Line=front;
		if(front!=null){front=front.next;length--;}
		return Line;
	}
	public void drawAllLines(Graphics g,int dx,int dy)
	{
		for(Line q=front;q!=null;q=q.next){g.setColor(q.color);g.drawLine(q.x0+dx,q.y0+dy,q.x1+dx,q.y1+dy);}
	}
	public boolean isNotEmpty()
	{
		return (front!=null);
	}
}
class Line
{
	public int x0,y0,x1,y1;
	public Color color;
	public Line next;
	public Line(int x0,int y0,int x1,int y1,Color color)
	{
		this.x0=x0;
		this.x1=x1;
		this.y0=y0;
		this.y1=y1;
		this.color=new Color(color.getRed(),color.getGreen(),color.getBlue());
	}
	public Line getIFSLine(IFSCode IFS)
	{
		int X0=(int)(IFS.a*x0+IFS.b*y0+IFS.e);
		int Y0=(int)(IFS.c*x0+IFS.d*y0+IFS.f);
		int X1=(int)(IFS.a*x1+IFS.b*y1+IFS.e);
		int Y1=(int)(IFS.c*x1+IFS.d*y1+IFS.f);
		return new Line(X0,Y0,X1,Y1,color);
	}
}