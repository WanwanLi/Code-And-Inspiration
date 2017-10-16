import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndSignal
{
	public static void main(String[] args)
	{
		Frame_Signal Frame_Signal1=new Frame_Signal();
		Frame_Signal1.setVisible(true);
	}
}
class Frame_Signal extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	Oscillogram Oscillogram1;
	public Frame_Signal()
	{
		int length=900;
		double[] f=Signal.Y(new double[][]{Signal.C(0,length/3),Signal.C(1,length/3),Signal.C(0,length/3)});
		double t0=0,t1=10;
		double[] t=Signal.X(t0,t1,length);
	//	double[] f=Signal.exp(-2,t);
		double[] x=t,y=f;

		double w0=-80*Math.PI,w1=80*Math.PI;
		double[] w=Signal.X(w0,w1,length);
		double[][] F=Signal.Fourier(w0,w1,length,f,t0,t1);
		double[][] freq=Signal.frequency(F);
		x=w;y=freq[0];

		f=Signal.FourierInverse(t0,t1,length,F,w0,w1);
		x=t;y=f;

		int left=30,top=50,width=1300,height=700,row=20,column=20;
		this.Oscillogram1=new Oscillogram(x,y,left,top,width,height,row,column);
	//	this.Oscillogram1.setMinMaxLines(0,2);
		this.Oscillogram1.setBaseLine(0);
	//	this.Oscillogram1.setFormat(Math.PI);
//		this.Oscillogram1.setColors(Color.white,Color.black,Color.blue);
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
		this.Oscillogram1.drawBackground(g);
		this.Oscillogram1.drawCurve(g);
	}
}
class f1 implements func
{
	public double y(double x)
	{
		return 2*Math.sin(2*Math.PI*2*x/32+Math.PI/2);
	}
}
interface func
{
	public double y(double x);
}
class Signal
{
	public static double[] X(double x0,double x1,int length)
	{
		double[] x=new double[length];
		double dx=(x1-x0)/(length-1);
		for(int i=0;i<length;i++)x[i]=x0+dx*i;
		return x;
	}
	public static double[] C(double c,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=c;
		return y;
	}
	public static double[] C(double c,int length)
	{
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=c;
		return y;
	}
	public static double[] Y(func f,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=f.y(x[i]);
		return y;
	}
	public static double[] Y(double[][] y)
	{
		int length=0,c=0;
		for(int i=0;i<y.length;i++)length+=y[i].length;
		double[] f=new double[length];
		for(int i=0;i<y.length;i++)for(int j=0;j<y[i].length;j++)f[c++]=y[i][j];
		return f;
	}
	public static double[] sin(double a,double b,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.sin(a*x[i]+b);
		return y;
	}
	public static double[] sin_cos(double a0,double b0,double a1,double b1,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.sin(a0*x[i]+b0)*Math.cos(a1*x[i]+b1);
		return y;
	}
	public static double[] cos_cos(double a0,double b0,double a1,double b1,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.cos(a0*x[i]+b0)*Math.cos(a1*x[i]+b1);
		return y;
	}
	public static double[] exp(double k,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.exp(k*x[i]);
		return y;
	}
	public static double[] pow(double a,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.pow(a,x[i]);
		return y;
	}
	public static double[] convolve(double[] y,double[] k)
	{
		int l=k.length;
		int length=y.length+k.length-1;
		double[] y1=new double[length];
		for(int i=0;i<length;i++)
		{
			y1[i]=0;
			System.out.print("y1["+i+"]=");
			for(int j=0;j<l&&i-j>=0;j++)
			{
				if(i-j<y.length)
				{
					y1[i]+=y[i-j]*k[j];
					System.out.print("y["+(i-j)+"]*h["+j+"]+");
				}
			}
			System.out.println("0.0="+y1[i]);
		}
		return y1;
	}
	public static double[] output(double[] a,double[] input,double[] b)
	{
		int length=input.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)
		{
			y[i]=0;
			for(int j=1;j<a.length&&i-j>=0;j++)y[i]-=a[j]*y[i-j];
			for(int j=0;j<b.length&&i-j>=0;j++)y[i]+=b[j]*input[i-j];
			y[i]/=a[0];
		}
		return y;
	}
	public static double[][] Fourier(double w0,double w1,int length,double[] f,double t0,double t1)
	{
		int l=length,n=f.length;
		double[][] F=new double[l][2];
		double dw=(w1-w0)/(l-1);
		double dt=(t1-t0)/(n-1);
		for(int i=0;i<l;i++)
		{
			double w=w0+i*dw;
			double[] Fi={0,0};
			for(int j=0;j<n;j++)
			{
				double t=t0+j*dt;
				double[] Fu=e(-w*t);
				mul(Fu,f[j]*dt);
				add(Fi,Fu);
			}
			F[i][0]=Fi[0];
			F[i][1]=Fi[1];
		}
		return F;
	}
	public static double[][] frequency(double[][] w)
	{
		int length=w.length;
		double[] A=new double[length];
		double[] p=new double[length];
		for(int i=0;i<length;i++)
		{
			A[i]=length(w[i]);
			p[i]=angle(w[i]);
		}
		return new double[][]{A,p};
	}
	public static double[] FourierInverse(double t0,double t1,int length,double[][] F,double w0,double w1)
	{
		int l=length,n=F.length;
		double[] f=new double[l];
		double dt=(t1-t0)/(l-1);
		double dw=(w1-w0)/(n-1);
		for(int i=0;i<l;i++)
		{
			double t=t0+i*dt;
			double[] Fi={0,0};
			for(int j=0;j<n;j++)
			{
				double w=w0+j*dw;
				double[] Fu=MUL(F[j],e(w*t));
				mul(Fu,dw);
				add(Fi,Fu);
			}
			f[i]=Fi[0]/(2*Math.PI);
		}
		return f;
	}
	public static double[] e(double w)
	{
		return new double[]{Math.cos(w),Math.sin(w)};
	}
	private static double[] ADD(double[] a,double[] b)
	{
		return new double[]{a[0]+b[0],a[1]+b[1]};
	}
	private static double[] MUL(double[] a,double[] b)
	{
		/*
			(a+bi)*(c+di)
			=ac+bci+adi-bd
			=ac-bd +(ad+bc)i
		*/
		double real=a[0]*b[0]-a[1]*b[1];
		double imaginary=a[0]*b[1]+a[1]*b[0];
		return new double[]{real,imaginary};
	}
	private static void add(double[] a,double[] b)
	{
		a[0]+=b[0];
		a[1]+=b[1];
	}
	private static void mul(double[] a,double k)
	{
		a[0]*=k;
		a[1]*=k;
	}
	private static double length(double[] a)
	{
		return Math.sqrt(a[0]*a[0]+a[1]*a[1]);
	}
	private static double angle(double[] a)
	{
		return Math.abs(a[0])>1E-10?Math.atan(a[1]/a[0]):a[1]>=0?2*Math.PI:-2*Math.PI;
	}
}
class Oscillogram
{
	public static final int X=1,Y=2;
	private double[] x;
	private double[] y;
	private double baseY;
	private int length,left,top,width,height,row,column;
	private double minX,maxX,minY,maxY,scaleX,scaleY;
	private int edge=70,dX=30,dY=20,lineWidth=2,pointSize=10;
	private int mode=0;
	private Color backgroundColor=Color.black;
	private Color gridColor=Color.white;
	private Color dataColor=Color.green;
	private int charWidth=7;
	private boolean formatPI=false;
	public Oscillogram(double[] x,double[] y,int left,int top,int width,int height,int row,int column)
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
		this.scaleX=(0.0+width-2*edge)/(maxX-minX);
		this.scaleY=(0.0+height-2*edge)/(maxY-minY);
		this.baseY=minY;
	}
	private int transformToCoordinateX(double xi)
	{
		return (int)(left+edge+(xi-minX)*scaleX);
	}
	private int transformToCoordinateY(double yi)
	{
		return (int)(top+height-edge-(yi-minY)*scaleY);
	}
	public void setFormat(double PI)
	{
		if(PI==Math.PI)this.formatPI=true;
	}
	private String format(double x)
	{
		if(formatPI)x/=Math.PI;
		String string=x+"";
		String formatString="";
		int i=0,j=0,len=string.length();
		while(i<len&&string.charAt(i)!='.')formatString+=string.charAt(i++);
		for(;j<3&&i<len;j++,i++)formatString+=string.charAt(i);
		boolean hasE=false;
		for(;i<len;i++)
		{
			if(string.charAt(i)=='E'){formatString+="E";hasE=true;continue;};
			if(hasE)formatString+=string.charAt(i);
		}
		if(formatPI)formatString+="PI";
		return formatString;
	}
	private void drawGrid(Graphics g)
	{
		double intervalY=(0.0+height-2*edge)/(row-1);
		double coordinateIntervalY=(maxY-minY)/(row-1);
		for(int i=0;i<row;i++)
		{
			int x0=left+edge;
			int x1=left+width-edge;
			int y=(int)(top+edge+i*intervalY);
			g.drawLine(x0,y,x1,y);
			double coordinateY=maxY-i*coordinateIntervalY;
			String text=format(coordinateY);
			g.drawString(text,x0-charWidth*(text.length()+1),y+2*charWidth/3);
		}
		double intervalX=(0.0+width-2*edge)/(column-1);
		double coordinateIntervalX=(maxX-minX)/(column-1);
		for(int j=0;j<column;j++)
		{
			int x=(int)(left+edge+j*intervalX);
			int y1=top+edge;
			int y2=top+height-edge;
			g.drawLine(x,y1,x,y2);
			double coordinateX=minX+j*coordinateIntervalX;
			String text=format(coordinateX);
			g.drawString(text,x-charWidth*text.length()/2,y2+3*charWidth);
		}
	}
	public void setColors(Color backgroundColor,Color gridColor,Color dataColor)
	{
		this.backgroundColor=backgroundColor;
		this.gridColor=gridColor;
		this.dataColor=dataColor;
	}
	public void setMinMaxLines(double minY,double maxY)
	{
		this.minY=minY;
		this.maxY=maxY;
		this.scaleY=(0.0+height-2*edge)/(maxY-minY);
	}
	public void setBaseLine(double y)
	{
		if(y>minY)this.baseY=y;
	}
	public void setLineWidth(int lineWidth)
	{
		this.lineWidth=lineWidth;
	}
	public void setPointSize(int pointSize)
	{
		this.pointSize=pointSize;
	}
	public void drawBackground(Graphics g)
	{
		g.setColor(backgroundColor);
		g.fillRect(left,top,width,height);
		g.setColor(gridColor);
		this.drawGrid(g);
	}
	public void drawStems(Graphics g)
	{
		g.setColor(dataColor);
		for(int i=0;i<length;i++)
		{
			int xi=transformToCoordinateX(x[i]);
			int y0=transformToCoordinateY(baseY);
			int y1=transformToCoordinateY(y[i]);
			for(int j=0;j<lineWidth;j++)
			{
				int x=xi+j-lineWidth/2;
				g.drawLine(x,y0,x,y1);
			}
		}
		int x0=transformToCoordinateX(minX);
		int x1=transformToCoordinateX(maxX);
		int yb=transformToCoordinateY(baseY);
		for(int j=0;j<lineWidth;j++)
		{
			int y=yb+j-lineWidth/2;
			g.drawLine(x0,y,x1,y);
		}

	}
	public void drawCurve(Graphics g)
	{
		g.setColor(dataColor);
		for(int i=0;i<length-1;i++)
		{
			int x0=transformToCoordinateX(x[i]);
			int y0=transformToCoordinateY(y[i]);
			int x1=transformToCoordinateX(x[i+1]);
			int y1=transformToCoordinateY(y[i+1]);
			for(int j=0;j<lineWidth;j++)
			{
				int dy=j-lineWidth/2;
				g.drawLine(x0,y0+dy,x1,y1+dy);
			}
		}
	}
	public void drawPoints(Graphics g)
	{
		g.setColor(dataColor);
		for(int i=0;i<length;i++)
		{
			int x=transformToCoordinateX(this.x[i]);
			int y=transformToCoordinateY(this.y[i]);
			g.fillOval(x-pointSize/2,y-pointSize/2,pointSize,pointSize);
		}
	}
}