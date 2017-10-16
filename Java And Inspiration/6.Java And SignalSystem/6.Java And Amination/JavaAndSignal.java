import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
public class JavaAndSignal
{
	public static void main(String[] args)
	{
		Frame_Signal Frame_Signal1=new Frame_Signal();
		Frame_Signal1.setVisible(true);
		Frame_Signal1.Timer1.start();
	}
}
class Frame_Signal extends JFrame implements ActionListener
{
	Oscillogram Oscillogram1;
	Timer Timer1;
	public Frame_Signal()
	{
		int length=32;
		double[] x=Signal.X(0,length-1,length);
		double[] y=Signal.C(0,x);y[5]=1;
		for(int j=5;j<length;j++)y[j]=1;
	//	y=Signal.Y(new f1(),x);
	//	y=Signal.sin(2*Math.PI*2/32,Math.PI/2,x);
	//	y=Signal.exp(Math.PI,x);
	//	y=Signal.pow(1.2,x);
	//	y=Signal.sin_cos(Math.PI/4,0,Math.PI/4,0,x);
	//	y=Signal.cos_cos(Math.PI/4,0,Math.PI/4,0,x);
	//	y=Signal.sin_cos(Math.PI/4,0,Math.PI/8,0,x);
		length=6;
		x=Signal.X(0,5,length);
		y=Signal.C(1,x);
		double[] k=x;
		y=Signal.convolve(y,k);
		x=Signal.X(0,y.length-1,y.length);
		double[] a={1.0,-0.8};
		double[] b={2};
		x=new double[]{1,2,3,4};
		y=Signal.output(a,x,b);
		for(int i=0;i<y.length;i++)System.out.println("x["+i+"]="+x[i]+"\ty["+i+"]="+y[i]);
		x=Signal.X(0,200,2000);
		b=Signal.sin(0.009*2*Math.PI,0.01*2*Math.PI,x);
		double[] y1=Signal.cos(0.4*Math.PI,0,x);
		double[] y2=Signal.square(0.4*Math.PI,0,x);
		double[] y3=Signal.sawtooth(0.4*Math.PI,0,0.5,x);
		y=Signal.modulate(b,y1);
		y=Signal.modulate(b,y2);
		y=Signal.modulate(b,y3);
		double t0=0,t1=1,x0=0,x1=10;
		length=100;
		x=Signal.X(x0,x1,length);
		double[] t=Signal.X(t0,t1,length);
		double[] Yt0=Signal.sin(0.4*Math.PI,0,x);
		double[] Yx0=Signal.sin(0.1*Math.PI,0,t);
		double[][] Ytx=Signal.Ytx(0.2,Yt0,Yx0,t0,t1,x0,x1);
		double[][] Y=new double[length][];
		for(int i=0;i<length;i++)Y[i]=Signal.sin(0.4*Math.PI+10*i,0,x);
		int left=30,top=50,width=1300,height=700,row=30,column=30;
		this.Timer1=new Timer(100,this);
		this.Oscillogram1=new Oscillogram(x,Y,left,top,width,height,row,column);
		this.Oscillogram1.setBaseLine(0);
//		this.Oscillogram1.setColors(Color.white,Color.black,Color.blue);
//		this.Oscillogram1.setFormat(Math.PI);
		this.Oscillogram1.setLineWidth(3);
	//	this.Oscillogram1.setMinMaxLines(-2,2);
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
//		this.Oscillogram1.drawStems(g);
//		this.Oscillogram1.drawPoints(g);
		this.Oscillogram1.drawCurve(g);
	}
	public void actionPerformed(ActionEvent e)
	{
		this.Oscillogram1.animate();
		this.repaint();
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
	public static double[] Y(func f,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=f.y(x[i]);
		return y;
	}
	public static double[] sin(double a,double b,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.sin(a*x[i]+b);
		return y;
	}
	public static double[] cos(double a,double b,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=Math.cos(a*x[i]+b);
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
		for(int i=0;i<length;i++)y[i]=Math.cos(wi*x[i]);
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
	public static double square(double x)
	{
		double d=x/(Math.PI*2);
		double f=d-(int)d;
		return f<=0.5?1:-1;
	}
	public static double[] square(double a,double b,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=square(a*x[i]+b);
		return y;
	}
	public static double unitSawtooth(double x,double k)
	{
		if(k==1)return x;
		if(x<=-1||x>=1)return 0.0;
		if(x>k)return 1-(x-k)/(1-k);
		if(x<-k)return (-x-k)/(1-k)-1;
		return x/k;
	}
	public static double sawtooth(double x,double k)
	{
		double d=x/(Math.PI*2);
		double f=d-(int)d;
		return -unitSawtooth(2*(f-0.5),k);
	}
	public static double[] sawtooth(double a,double b,double k,double[] x)
	{
		int length=x.length;
		double[] y=new double[length];
		for(int i=0;i<length;i++)y[i]=sawtooth(a*x[i]+b,k);
		return y;
	}
	public static void amplify(double[] y,double A)
	{
		for(int i=0;i<y.length;i++)y[i]*=A;
	}
	public static double[] modulate(double[] b,double[] y)
	{
		int length=y.length;
		double[] s=new double[length];
		for(int i=0;i<length;i++)s[i]=(1+b[i])*y[i];
		return s;
	}
	public static double[][] Ytx(double c,double[] Yt0,double[] Yx0,double t0,double t1,double x0,double x1)
	{
		int n=Yt0.length,m=Yx0.length,i,j;
		double dt=(t1-t0)/(m-1);
		double dx=(x1-x0)/(n-1);
		double[][] Y=new double[m][n];
		for(j=0;j<n;j++)Y[0][j]=Yt0[j];
		for(i=0;i<m;i++)Y[i][0]=Yx0[i];
		for(j=1;j<n;j++)Y[1][j]=Y[0][j-1]+(Y[0][j]-Y[0][j-1])+(Y[1][j-1]-Y[0][j-1]);
		for(i=1;i<m-1;i++)
		{
			for(j=1;j<n-1;j++)
			{
				Y[i+1][j]=Y[i-1][j]-2*Y[i][j]+c*dt/dx*dt/dx*(Y[i][j+1]-Y[i][j-1]+2*Y[i][j]);
			}
			Y[i+1][j]=Y[i][j-1]+(Y[i][j]-Y[i][j-1])+(Y[i+1][j-1]-Y[i][j-1]);
		}
		return Y;
	}
}
class Oscillogram
{
	private double[] x;
	private double[] y;
	private double[][] Y;
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
	private boolean isAnimation=false;
	private int timer=0;
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
		if(isAnimation)return;
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
	public Oscillogram(double[] x,double[][] Y,int left,int top,int width,int height,int row,int column)
	{
		this.left=left;
		this.top=top;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.isAnimation=true;
		this.timer=0;
		this.setCoordinates(x,Y[timer]);
	}
	public void animate()
	{
		if(Y==null)return;
		if(timer>=Y.length)timer=0;
		this.setCoordinates(x,Y[this.timer++]);
	}
}