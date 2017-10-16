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
		int length=100;
		double[] x=Signal.X(-3,3,length);
		length=8;
		x=Signal.X(-1,6,length);
		double[] y=new double[]{0,1 ,2 ,3 ,3 ,3 ,3 ,0};
		double[] x1={1 ,2 ,1 ,1};
		double[] x2=Signal.C(1,5);
		y=Signal.convolve(x1,x2);
		double R=1,C=1;
		double[] a={1/(R*C),1};
		double[] b={1/(R*C)};
		a=new double[]{1,2};
		b=new double[]{1,2,1};
		length=1000;
		x=Signal.X(0,8,length);
		y=Signal.output(a,Signal.C(1,length),0,8,b);

		y=Signal.output(a,Signal.impulse(0,0.017,length),0,8,b);
		y=Signal.output(a,Signal.step(0,2,length),0,8,b);

		x=Signal.X(0,10,length);
		y=Signal.Y(new f7(),x);
		y=Signal.output(a,y,0,10,b);
/*
		length=20;
		a=new double[]{1,-0.25,0.5};
		b=new double[]{1,1};
		x=Signal.X(0,length,length);
		y=Signal.impulse(0,1,length);
		y=Signal.output(a,y,b);
*/	
		int left=30,top=50,width=1300,height=700,row=10,column=20;
		this.Oscillogram1=new Oscillogram(x,y,left,top,width,height,row,column);
//		this.Oscillogram1.setRange(-2,4);
//		this.Oscillogram1.setMinMaxLines(-1,1);
		this.Oscillogram1.setBaseLine(0);
	//	this.Oscillogram1.setPointSize(2);
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
//		this.Oscillogram1.drawStems(g);
//		this.Oscillogram1.drawPoints(g);
	}
}
class f0 implements func
{
	public double y(double x)
	{
		return x*x;
	}
}
class f1 implements func
{
	public double y(double x)
	{
		return (2-Math.exp(x))*Signal.heaviside(x);
	}
}
class f2 implements func
{
	public double y(double x)
	{
		return Math.cos(Math.PI*x)*(Signal.heaviside(x)-Signal.heaviside(x-2));
	}
}
class f3 implements func
{
	public double y(double x)
	{
		return Math.pow(-0.5,x)*Signal.heaviside(x);
	}
}
class f4 implements func
{
	public double y(double x)
	{
		return Signal.heaviside(x+2)-Signal.heaviside(x-5);
	}
}
class f5 implements func
{
	public double y(double x)
	{
		return (Signal.heaviside(x+2)-Signal.heaviside(x-2))*Math.cos(2*Math.PI*x);
	}
}
class f6 implements func
{
	public double y(double x)
	{
		return Math.pow(0.5,x)*Signal.heaviside(x);
	}
}
class f7 implements func
{
	public double y(double x)
	{
		return x==0?1.0:Math.exp(-2*x)*Signal.heaviside(x);
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
	public static double[] d(int n,func f,double[] x)
	{
		int length=x.length;
		double dx=(x[length-1]-x[0])/length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=d(n,f,x[i],dx);
		return y;
	}
	public static double d(int n,func f,double x,double dx)
	{
		double df=0;
		if(n==0)df=f.y(x);
		else df=(d(n-1,f,x+dx,dx)-d(n-1,f,x,dx))/dx;
		return df;
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
	public static double[] exp(double wi,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.exp(wi*x[i]);
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
			for(int j=0;j<l&&i-j>=0;j++)
			{
				if(i-j<y.length)
				{
					y1[i]+=y[i-j]*k[j];
				}
			}
		}
		return y1;
	}
	public static double[] impulse(int i,double y,int length)
	{
		double[] x=new double[length];
		x[i]=y;
		return x;
	}
	public static double[] step(int i,double y,int length)
	{
		double[] x=new double[length];
		for(int j=i;j<length;j++)x[j]=y;
		return x;
	}
	public static double d(int n,double[] y,int i)
	{
		if(n==0)return y[i];
		else return d(n-1,y,i)-d(n-1,y,i-1);
	}
	public static double[] output(double[] a,double[] input,double t0,double t1,double[] b)
	{
		int length=input.length;
		double[] x=input,y=new double[length];
		double dT=(t1-t0)/(length-1);
		double[] dt=new double[a.length>b.length?a.length:b.length];
		dt[0]=Math.pow(dT,a.length-1);
		for(int i=1;i<dt.length;i++)dt[i]=dt[i-1]/dT;
		double a0=a[0];
		for(int i=1;i<a.length;i++){a0*=dT;a0+=a[i];}
		for(int i=0;i<length;i++)
		{
			double yi=0;y[i]=0;
			for(int j=0;j<a.length&&i-j>=0;j++)yi-=a[j]*d(j,y,i)*dt[j];
			for(int j=0;j<b.length&&i-j>=0;j++)yi+=b[j]*d(j,x,i)*dt[j];
			y[i]=yi/a0;
		}
		return y;
	}
	public static double[] output(double[] a,double[] input,double[] b)
	{
		int length=input.length;
		double[] x=input,y=new double[length];
		for(int i=0;i<length;i++)
		{
			y[i]=0;
			for(int j=1;j<a.length&&i-j>=0;j++)y[i]-=a[j]*y[i-j];
			for(int j=0;j<b.length&&i-j>=0;j++)y[i]+=b[j]*x[i-j];
			y[i]/=a[0];
		}
		return y;
	}
	public static double heaviside(double x)
	{
		return x>0?1:x<0?0:0.5;
	}
	public static double[][] FourierTransform(double[] w,double[] f,double[] t)
	{
		int l=w.length,n=f.length;
		double[][] F=new double[2][l];
		double u0=w[0],du=(w[l-1]-w[0])/l;
		double x0=t[0],dx=(t[n-1]-t[0])/n;
		for(int i=0;i<l;i++)
		{
			double u=u0+i*du;
			double[] Fi={0,0};
			for(int j=0;j<n;j++)
			{
				double x=x0+j*dx;
				double _2PI=-2.0*Math.PI;
				double[] Fu=e(_2PI*u*x);
				mul(Fu,f[j]*dx);
				add(Fi,Fu);
			}
			F[0][i]=length(Fi);
			F[1][i]=angle(Fi);
		}
		return F;
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
	private static double[] PRO(double[] a,double k)
	{
		return new double[]{a[0]*k,a[1]*k};
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
	public void setRange(double minX,double maxX)
	{
		this.minX=minX;
		this.maxX=maxX;
		this.scaleX=(0.0+width-2*edge)/(maxX-minX);
	}
	public void setMinMaxLines(double minY,double maxY)
	{
		this.minY=minY;
		this.maxY=maxY;
		this.scaleY=(0.0+height-2*edge)/(maxY-minY);
	}
	public void setBaseLine(double y)
	{
		if(y>minY&&y<maxY)this.baseY=y;
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