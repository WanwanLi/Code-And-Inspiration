import java.awt.*;
import java.awt.event.*;
public class JavaAndDiscreteMap
{
	public static void main(String[] args)
	{
		DiscreteMap DiscreteMap1=new DiscreteMap(50,100,1300,600,Color.red);
		DiscreteMap1.getLogisticMapRoots(3.0,4.0);
	//	DiscreteMap1.getHenonMapRoots(0,1.4,0.3);
	//	DiscreteMap1.getHenonMapRoots1(0,1.4,0.3);
	//	DiscreteMap1.getTentMapRoots(0,1);
		DiscreteMap1.setVisible(true);
	}
}
class DiscreteMap extends Frame
{
	final double INF=Double.MAX_VALUE;
	double minX=INF,maxX=-INF,minY=INF,maxY=-INF;
	int m=500,n=500,left,top,width,height;Color color;
	double[][] roots=new double[m][n];
	double[] rootsX=null;
	double[] rootsY=null;
	public DiscreteMap(int left,int top,int width,int height,Color color)
	{
		this.left=left;
		this.top=top;
		this.width=width;
		this.height=height;
		this.color=color;
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void getLogisticMapRoots(double a0,double a1)
	{
		this.minX=INF;
		this.maxX=-INF;
		int N=20;
		double da=(a1-a0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double a=a0+i*da;
			double x=0.5;
			for(int j=0;j<N;j++)x=a*x*(1-x);
			for(int j=0;j<n;j++)
			{
				x=a*x*(1-x);
				roots[i][j]=x;
				if(x<minX)minX=x;
				if(x>maxX)maxX=x;
			}
		}
	}
	public void getHenonMapRoots(double a0,double a1,double b)
	{
		this.minX=INF;
		this.maxX=-INF;
		int N=400;
		double da=(a1-a0)/(m-1);
		double x0=0,y0=0,x1,y1;
		for(int i=0;i<m;i++)
		{
			double a=a0+i*da;
			for(int j=0;j<N;j++)
			{
				x1=y0+1-a*x0*x0;
				y1=b*x0;
				x0=x1;
				y0=y1;
			}
			for(int j=0;j<n;j++)
			{
				x1=y0+1-a*x0*x0;
				y1=b*x0;
				x0=x1;
				y0=y1;
				roots[i][j]=x1;
				if(x1<minX)minX=x1;
				if(x1>maxX)maxX=x1;
			}
		}
	}
	public void getHenonMapRoots1(double a0,double a1,double b)
	{
		this.minX=INF;
		this.maxX=-INF;
		int N=400;
		double da=(a1-a0)/(m-1);
		double x0=0,y0=0,x1,y1;
		for(int i=0;i<m;i++)
		{
			double a=a0+i*da;
			for(int j=0;j<N;j++)
			{
				x1=a-x0*x0+b*y0;
				y1=x0;
				x0=x1;
				y0=y1;
			}
			for(int j=0;j<n;j++)
			{
				x1=a-x0*x0+b*y0;
				y1=x0;
				x0=x1;
				y0=y1;
				roots[i][j]=x1;
				if(x1<minX)minX=x1;
				if(x1>maxX)maxX=x1;
			}
		}
	}
	public void getTentMapRoots(double a0,double a1)
	{
		this.minX=INF;
		this.maxX=-INF;
		int N=200;
		double da=(a1-a0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double a=a0+i*da;
			double x=0.34;
			for(int j=0;j<N;j++)x=a-(1+a)*Math.abs(x);
			for(int j=0;j<n;j++)
			{
				x=a-(1+a)*Math.abs(x);
				roots[i][j]=x;
				if(x<minX)minX=x;
				if(x>maxX)maxX=x;
			}
		}
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(left,top,width,height);
		g.setColor(color);
		if(rootsY!=null)
		{
			for(int j=0;j<n;j++)
			{
				int x=left+(int)(width*(rootsX[j]-minX)/(maxX-minX));
				int y=top+height-(int)(height*(rootsY[j]-minY)/(maxY-minY));
				g.fillRect(x,y,1,1);
			}
		}
		else
		{
			for(int i=0;i<m;i++)
			{
				for(int j=0;j<n;j++)
				{
					int x=left+(int)(width*(i+0.0)/(m-1));
					int y=top+height-(int)(height*(roots[i][j]-minX)/(maxX-minX));
					g.fillRect(x,y,1,1);
				}
			}
		}
	}
}
