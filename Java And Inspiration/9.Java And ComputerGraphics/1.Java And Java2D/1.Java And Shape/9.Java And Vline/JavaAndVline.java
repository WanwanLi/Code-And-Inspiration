 import java.awt.*;
import java.awt.event.*;
public class JavaAndVline
{
	public static void main(String[] args)
	{
		Frame_Vline Frame_Vline1=new Frame_Vline();
		Frame_Vline1.setVisible(true);
	}
}
class Frame_Vline extends Frame
{
	Histogram Histogram1;
	public Frame_Vline()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.getHistogram();
	}
	private void getHistogram()
	{
		int X=0,Y=1,n=8;
		double x0=-10,y0=-10,a0=2,b0=8,x1=10,y1=10,a1=-4,b1=8,t=0.5,v=4.0;
		Vline Vline1=new Vline(x0,y0,a0,b0,x1,y1,a1,b1);
		double[][] x=new double[3][2];
		double[][] y=new double[3][2];
		double[] xy=Vline1.getIntersectionPoint();
		double[][] XY=Vline1.getXY(n,v);
		x[0][0]=x0;
		x[0][1]=x0+t*a0;
		y[0][0]=y0;
		y[0][1]=y0+t*b0;
		x[1][0]=x1;
		x[1][1]=x1+t*a1;
		y[1][0]=y1;
		y[1][1]=y1+t*b1;
		x[2]=XY[X];
		y[2]=XY[Y];
		int left=100,top=100,width=800,height=600,row=10,column=20;
		int[] dataLengths=new int[]{2,2,XY[X].length};
		boolean[] isColumns=new boolean[]{false,false,false};
		this.Histogram1=new Histogram(x,y,dataLengths,isColumns,left,top,width,height,row,column);
	}
	public void paint(Graphics g)
	{
		Histogram1.drawHistogram(g,Color.black,new Color[]{Color.green,Color.blue,Color.orange},Color.white);
	}
}
class Vline
{
	private final int X=0,Y=1;
	private double x0,y0,a0,b0,x1,y1,a1,b1;
	public Vline(double x0,double y0,double a0,double b0,double x1,double y1,double a1,double b1)
	{
		this.x0=x0;
		this.y0=y0;
		this.a0=a0;
		this.b0=b0;
		this.x1=x1;
		this.y1=y1;
		this.a1=a1;
		this.b1=b1;
	}
	double[] getIntersectionPoint()
	{
		if(Math.abs(a1)>Math.abs(a0))
		{
			double k=b1/a1;
			double dx=x1-x0;
			double dy=y1-y0;
			double t0=(dx*k-dy)/(a0*k-b0);
			double[] xy=new double[2];
			xy[0]=x0+t0*a0;
			xy[1]=y0+t0*b0;
			return xy;
		}
		else
		{
			double k=b0/a0;
			double dx=x1-x0;
			double dy=y1-y0;
			double t1=-(dx*k-dy)/(a1*k-b1);
			double[] xy=new double[2];
			xy[0]=x1+t1*a1;
			xy[1]=y1+t1*b1;
			return xy;
		}
	}
	double[][] getXY(int n)
	{
		double[] x01y01=this.getIntersectionPoint();
		double x01=x01y01[X];
		double y01=x01y01[Y];
		StringQueue q0=new StringQueue();
		StringQueue q1=new StringQueue();
		q0.enQueue(x0,y0);
		q0.enQueue(x01,y01);
		q0.enQueue(x1,y1);
		for(int i=0;i<n;i++)
		{
			double[] x0y0=q0.getFirst();
			q1.enQueue(x0y0[X],x0y0[Y]);
			while(q0.hasMoreThanOne())
			{
				x0y0=q0.deQueue();
				double[] x1y1=q0.getFirst();
				x01=(x0y0[X]+x1y1[X])/2;
				y01=(x0y0[Y]+x1y1[Y])/2;
				q1.enQueue(x01,y01);
			}
			double[] x1y1=q0.deQueue();
			q1.enQueue(x1y1[X],x1y1[Y]);
			q0=q1;
			q1=new StringQueue();
		}
		int length=n+3;
		double[][] xy=new double[2][length];
		for(int i=0;i<length;i++)
		{
			double[] xiyi=q0.deQueue();
			xy[X][i]=xiyi[X];
			xy[Y][i]=xiyi[Y];
		}
		return xy;		
	}
	double[][] getXY(int n,int v)
	{
		if(v<=2)return this.getXY(n);
		double[] x01y01=this.getIntersectionPoint();
		double x01=x01y01[X];
		double y01=x01y01[Y];
		double dx0=(x01-x0)/v;
		double dy0=(y01-y0)/v;
		double x001=x0+(v-1)*dx0;
		double y001=y0+(v-1)*dy0;
		double dx1=(x1-x01)/v;
		double dy1=(y1-y01)/v;
		double x011=x01+dx1;
		double y011=y01+dy1;
		StringQueue q0=new StringQueue();
		StringQueue q1=new StringQueue();
		q0.enQueue(x0,y0);
		q0.enQueue(x001,y001);
		q0.enQueue(x011,y011);
		q0.enQueue(x1,y1);
		for(int i=0;i<n;i++)
		{
			double[] x0y0=q0.getFirst();
			q1.enQueue(x0y0[X],x0y0[Y]);
			while(q0.hasMoreThanOne())
			{
				x0y0=q0.deQueue();
				double[] x1y1=q0.getFirst();
				x01=(x0y0[X]+x1y1[X])/2;
				y01=(x0y0[Y]+x1y1[Y])/2;
				q1.enQueue(x01,y01);
			}
			double[] x1y1=q0.deQueue();
			q1.enQueue(x1y1[X],x1y1[Y]);
			q0=q1;
			q1=new StringQueue();
		}
		int length=q0.length();
		double[][] xy=new double[2][length];
		for(int i=0;i<length;i++)
		{
			double[] xiyi=q0.deQueue();
			xy[X][i]=xiyi[X];
			xy[Y][i]=xiyi[Y];
		}
		return xy;		
	}
	double[][] getXY(int n,double v)
	{
		if(v<=2.0)return this.getXY(n);
		if(n>10)n=10;
		double x12,y12;
		double[] x0y0,x1y1,x2y2;
		double[] x01y01=this.getIntersectionPoint();
		double x01=x01y01[X];
		double y01=x01y01[Y];
		double dx0=(x01-x0)/v;
		double dy0=(y01-y0)/v;
		double x001=x0+(v-1)*dx0;
		double y001=y0+(v-1)*dy0;
		double dx1=(x1-x01)/v;
		double dy1=(y1-y01)/v;
		double x011=x01+dx1;
		double y011=y01+dy1;
		StringQueue q0=new StringQueue();
		StringQueue q1=new StringQueue();
		q0.enQueue(x0,y0);
		q0.enQueue(x001,y001);
		q0.enQueue(x011,y011);
		q0.enQueue(x1,y1);
		for(int i=0;i<n;i++)
		{
			x0y0=q0.getFirst();
			q1.enQueue(x0y0[X],x0y0[Y]);
			while(q0.hasMoreThanTwo())
			{
				x0y0=q0.deQueue();
				x1y1=q0.getFirst();
				dx0=(x1y1[X]-x0y0[X])/v;
				dy0=(x1y1[Y]-x0y0[Y])/v;
				x01=x0y0[X]+(v-1)*dx0;
				y01=x0y0[Y]+(v-1)*dy0;
				q1.enQueue(x01,y01);
				x2y2=q0.getSecond();
				dx1=(x2y2[X]-x1y1[X])/v;
				dy1=(x2y2[Y]-x1y1[Y])/v;
				x12=x1y1[X]+dx1;
				y12=x1y1[Y]+dy1;
				q1.enQueue(x12,y12);
			}
			q0.deQueue();
			x1y1=q0.deQueue();
			q1.enQueue(x1y1[X],x1y1[Y]);
			q0=q1;
			q1=new StringQueue();
		}
		int length=q0.length();
		double[][] xy=new double[2][length];
		for(int i=0;i<length;i++)
		{
			double[] xiyi=q0.deQueue();
			xy[X][i]=xiyi[X];
			xy[Y][i]=xiyi[Y];
		}
		return xy;		
	}
}
class StringQueue
{
	private String stringQueue;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public void enQueue(double x,double y)
	{
		this.stringQueue+=x+","+y+";";
	}
	public double[] deQueue()
	{
		int n=0;
		String x="",y="";
		char c=stringQueue.charAt(n++);
		while(c!=',')
		{
			x+=c;
			c=stringQueue.charAt(n++);
		}
		c=stringQueue.charAt(n++);
		while(c!=';')
		{
			y+=c;
			c=stringQueue.charAt(n++);
		}
		double[] xy=new double[2];
		xy[0]=Double.parseDouble(x);
		xy[1]=Double.parseDouble(y);
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		return xy;
	}
	public double[] getFirst()
	{
		int n=0;
		String x="",y="";
		char c=stringQueue.charAt(n++);
		while(c!=',')
		{
			x+=c;
			c=stringQueue.charAt(n++);
		}
		c=stringQueue.charAt(n++);
		while(c!=';')
		{
			y+=c;
			c=stringQueue.charAt(n++);
		}
		double[] xy=new double[2];
		xy[0]=Double.parseDouble(x);
		xy[1]=Double.parseDouble(y);
		return xy;
	}
	public double[] getSecond()
	{
		int n=0;
		String x="",y="";
		char c=stringQueue.charAt(n++);
		while(c!=';')c=stringQueue.charAt(n++);
		c=stringQueue.charAt(n++);
		while(c!=',')
		{
			x+=c;
			c=stringQueue.charAt(n++);
		}
		c=stringQueue.charAt(n++);
		while(c!=';')
		{
			y+=c;
			c=stringQueue.charAt(n++);
		}
		double[] xy=new double[2];
		xy[0]=Double.parseDouble(x);
		xy[1]=Double.parseDouble(y);
		return xy;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public int length()
	{
		int l=0;
		for(int i=0;i<stringQueue.length();i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
	public boolean hasMoreThanOne()
	{
		int l=0;
		for(int i=0;i<stringQueue.length();i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
			if(l>1)return true;
		}
		return false;
	}
	public boolean hasMoreThanTwo()
	{
		int l=0;
		for(int i=0;i<stringQueue.length();i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
			if(l>2)return true;
		}
		return false;
	}
}
class Histogram
{
	private double[] x;
	private double[] y;
	private int length,left,top,width,height,row,column;
	private double minX,maxX,minY,maxY,scaleX,scaleY;
	private int edgeX=70,edgeY=30;
	private int mode=0;
	public Histogram(double[] x,double[] y,int left,int top,int width,int height,int row,int column)
	{
		this.left=left;
		this.top=top;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.setCoordinates(x,y);
	}
	public void setCoordinates(double[] x,double[] y)
	{
		this.length=x.length<y.length?x.length:y.length;
		this.x=new double[length];
		this.y=new double[length];
		this.maxX=x[0];
		this.minX=x[0];
		this.maxY=y[0];
		this.minY=y[0];
		for(int i=0;i<length;i++)
		{
			this.x[i]=x[i];
			if(x[i]>maxX)this.maxX=x[i];
			if(x[i]<minX)this.minX=x[i];
			this.y[i]=y[i];
			if(y[i]>maxY)this.maxY=y[i];
			if(y[i]<minY)this.minY=y[i];
		}
		this.scaleX=width/(maxX-minX);
		this.scaleY=height/(maxY-minY);
	}
	private int transformToCoordinateX(double xi)
	{
		return (int)(left+(xi-minX)*scaleX);
	}
	private int transformToCoordinateY(double yi)
	{
		return (int)(top+height-(yi-minY)*scaleY);
	}
	private void drawColumns(Graphics g)
	{
		for(int i=0;i<length;i++)
		{
			int xi=transformToCoordinateX(x[i]);
			int y0=transformToCoordinateY(minY);
			int y1=transformToCoordinateY(y[i]);
			g.drawLine(xi,y0,xi,y1);
		}
	}
	private void drawDatas(Graphics g)
	{
		for(int i=0;i<length-1;i++)
		{
			int x0=transformToCoordinateX(x[i]);
			int y0=transformToCoordinateY(y[i]);
			int x1=transformToCoordinateX(x[i+1]);
			int y1=transformToCoordinateY(y[i+1]);
			g.drawLine(x0,y0,x1,y1);
		}
	}
	private String format(double x)
	{
		String string=x+"";
		String formatString="";
		int i=0,j=0,len=string.length();
		while(i<len&&string.charAt(i)!='.')formatString+=string.charAt(i++);
		for(;j<3&&i<len;j++,i++)formatString+=string.charAt(i);
		return formatString;
	}
	private void drawGrid(Graphics g)
	{
		double intervalY=height/row;
		double coordinateIntervalY=(maxY-minY)/row;
		for(int i=0;i<=row;i++)
		{
			int x0=left;
			int x1=left+width;
			int y=(int)(top+i*intervalY);
			g.drawLine(x0,y,x1,y);
			double coordinateY=maxY-i*coordinateIntervalY;
			g.drawString(format(coordinateY),x0-edgeX,y);
		}
		double intervalX=width/column;
		double coordinateIntervalX=(maxX-minX)/column;
		for(int j=0;j<=column;j++)
		{
			int x=(int)(left+j*intervalX);
			int y1=top;
			int y2=top+height;
			g.drawLine(x,y1,x,y2);
			double coordinateX=minX+j*coordinateIntervalX;
			g.drawString(format(coordinateX),x,y2+edgeY);
		}
	}
	public void draw(Graphics g,Color backColor,Color dataColor,Color gridColor)
	{
		g.setColor(backColor);
		g.fillRect(left-edgeX,top-edgeY,width+2*edgeX,height+2*edgeY);
		g.setColor(dataColor);
		this.drawColumns(g);
		g.setColor(gridColor);
		this.drawGrid(g);
	}
	public void paint(Graphics g,Color backColor,Color dataColor,Color gridColor)
	{
		g.setColor(backColor);
		g.fillRect(left-edgeX,top-edgeY,width+2*edgeX,height+2*edgeY);
		g.setColor(dataColor);
		this.drawDatas(g);
		g.setColor(gridColor);
		this.drawGrid(g);
	}
	private double[][] X;
	private double[][] Y;
	private int[] dataLengths;
	private boolean[] isColumns;
	private int dataNumber;
	private int maxDataLength;
	private int max(int[] x)
	{
		int Max=x[0];
		for(int i=0;i<x.length;i++)if(x[i]>Max)Max=x[i];
		return Max;
	}
	public Histogram(double[][] X,double[][] Y,int[] dataLengths,boolean[] isColumns,int left,int top,int width,int height,int row,int column)
	{
		this.left=left;
		this.top=top;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.setCoordinatesAndAttributes(X,Y,dataLengths,isColumns);
	}
	public void setCoordinatesAndAttributes(double[][] X,double[][] Y,int[] dataLengths,boolean[] isColumns)
	{
		this.dataNumber=(dataLengths.length<=isColumns.length?dataLengths.length:isColumns.length);
		this.maxDataLength=max(dataLengths);
		this.X=new double[dataNumber][maxDataLength];
		this.Y=new double[dataNumber][maxDataLength];
		this.dataLengths=new int[dataNumber];
		this.isColumns=new boolean[dataNumber];
		this.maxX=X[0][0];
		this.minX=X[0][0];
		this.maxY=Y[0][0];
		this.minY=Y[0][0];
		for(int i=0;i<dataNumber;i++)
		{
			this.dataLengths[i]=dataLengths[i];
			this.isColumns[i]=isColumns[i];
			for(int j=0;j<dataLengths[i];j++)
			{
				this.X[i][j]=X[i][j];
				if(X[i][j]>maxX)this.maxX=X[i][j];
				if(X[i][j]<minX)this.minX=X[i][j];
				this.Y[i][j]=Y[i][j];
				if(Y[i][j]>maxY)this.maxY=Y[i][j];
				if(Y[i][j]<minY)this.minY=Y[i][j];
			}
		}
		this.scaleX=width/(maxX-minX);
		this.scaleY=height/(maxY-minY);
	}
	private void drawDatasAndColumns(Graphics g,int i)
	{
		for(int j=0;j<dataLengths[i];j++)
		{
			if(isColumns[i])
			{
				int Xij=transformToCoordinateX(X[i][j]);
				int Y0=transformToCoordinateY(minY);
				int Y1=transformToCoordinateY(Y[i][j]);
				g.drawLine(Xij,Y0,Xij,Y1);
			}
			else
			{
				if(j>=dataLengths[i]-1)continue;
				int X0=transformToCoordinateX(X[i][j]);
				int Y0=transformToCoordinateY(Y[i][j]);
				int X1=transformToCoordinateX(X[i][j+1]);
				int Y1=transformToCoordinateY(Y[i][j+1]);
				g.drawLine(X0,Y0,X1,Y1);
			}
		}
	}
	public void drawHistogram(Graphics g,Color backColor,Color[] dataColor,Color gridColor)
	{
		g.setColor(backColor);
		g.fillRect(left-edgeX,top-edgeY,width+2*edgeX,height+2*edgeY);
		g.setColor(gridColor);
		this.drawGrid(g);
		for(int i=0;i<dataNumber;i++)
		{
			g.setColor(dataColor[i%dataColor.length]);
			this.drawDatasAndColumns(g,i);
		}
	}
}