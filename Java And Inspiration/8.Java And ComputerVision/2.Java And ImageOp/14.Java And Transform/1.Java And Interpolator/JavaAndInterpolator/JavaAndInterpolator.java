import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndInterpolator
{
	public static void main(String[] args)
	{
		Frame_Interpolator Frame_Interpolator1=new Frame_Interpolator();
		Frame_Interpolator1.setVisible(true);
	}
}
class Frame_Interpolator extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	Histogram Histogram1;
	public Frame_Interpolator()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		double[][] X=new double[2][400];
		double[][] Y=new double[2][400];
		double[] x=new double[20];
		double[] y=new double[20];
		int[] dataLengths={20,400};
		boolean[] isColumns={true,false};
		for(int i=0;i<20;i++)
		{
			X[0][i]=20*i+10*Math.random();
			Y[0][i]=800.0*Math.random();
			x[i]=X[0][i];
			y[i]=Y[0][i];
		}
		Interpolator Interpolator1=new Interpolator(x,y);
		x=new double[400];
		for(int i=0;i<400;i++){X[1][i]=i;x[i]=X[1][i];}
		y=Interpolator1.getRoundInterpolatedValues(x);
		for(int i=0;i<400;i++){Y[1][i]=y[i];}
		int left=100,top=100,width=1000,height=600,row=10,column=20;
		this.Histogram1=new Histogram(X,Y,dataLengths,isColumns,left,top,width,height,row,column);
	}
	public void paint(Graphics g)
	{
		if(Histogram1!=null)this.Histogram1.drawHistogram(g,Color.black,new Color[]{Color.green,Color.blue},Color.white);
	}
}
class Interpolator
{
	private double[] x;
	private double[] y;
	private int length;
	public Interpolator(double[] x,double[] y)
	{
		this.length=(x.length<=y.length?x.length:y.length);
		this.x=new double[length];
		this.y=new double[length];
		for(int i=0;i<length;i++)
		{
			this.x[i]=x[i];
			this.y[i]=y[i];
		}
	}
	private int round(double x,int j)
	{
		if(j==0)return ((this.x[j]-this.x[j+1])*0.5<=x?1:0);
		if(j==length-1)return (x<(this.x[j]-this.x[j-1])*0.5?1:0);
		double left=(this.x[j]-this.x[j+1])*0.5;
		double right=(this.x[j]-this.x[j-1])*0.5;
		if(left<=x&&x<right)return 1;
		else return 0;
	}
	private double linear(double x,int j)
	{
		if(j==0)return ((this.x[j]-this.x[j+1])*0.5<=x?1:0);
		if(j==length-1)return (x<(this.x[j]-this.x[j-1])*0.5?1:0);
		double left=(this.x[j]-this.x[j+1])*0.5;
		double right=(this.x[j]-this.x[j-1])*0.5;
		if(left<=x&&x<right)return 1;
		else return 0;
	}
	public double[] getRoundInterpolatedValues(double[] x)
	{
		double[] roundInterpolatedValues=new double[x.length];
		for(int i=0;i<x.length;i++)
		{
			for(int j=0;j<this.length;j++)
			{
				roundInterpolatedValues[i]+=this.y[j]*round(this.x[j]-x[i],j);
			}
		}
		return roundInterpolatedValues;
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