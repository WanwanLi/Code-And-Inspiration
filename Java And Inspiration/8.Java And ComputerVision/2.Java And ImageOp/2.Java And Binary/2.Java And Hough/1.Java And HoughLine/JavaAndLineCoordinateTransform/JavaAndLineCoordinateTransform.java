import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndLineCoordinateTransform
{
	public static void main(String[] args)
	{
		Frame_LineCoordinateTransform Frame_LineCoordinateTransform1=new Frame_LineCoordinateTransform();
		Frame_LineCoordinateTransform1.setVisible(true);
	}
}
class Frame_LineCoordinateTransform extends Frame
{
	Histogram Histogram1;
	public Frame_LineCoordinateTransform()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.getLineCoordinateTransformHistogram();
	}
	private void getLineCoordinateTransformHistogram()
	{
		LineCoordinateTransform LineCoordinateTransform1=new LineCoordinateTransform();
		double[] x0=new double[]{30,130,230};
		double[] y0=new double[]{70,170,270};
		double[][] X=new double[4][360];
		double[][] Y=new double[4][360];
		for(int i=0;i<360;i++)X[0][i]=X[1][i]=X[2][i]=i;
		Y[0]=LineCoordinateTransform1.getPolarCoordinate(x0[0],y0[0]);
		Y[1]=LineCoordinateTransform1.getPolarCoordinate(x0[1],y0[1]);
		Y[2]=LineCoordinateTransform1.getPolarCoordinate(x0[2],y0[2]);
		double[] p0w0=LineCoordinateTransform1.getMaxNumberOfIntersectionPointsInPolarCoordinates();
		double[] y0y1=LineCoordinateTransform1.getYCoordinates(p0w0[0],p0w0[1],30,230);
		Y[3][0]=y0y1[0];
		Y[3][1]=y0y1[1];
		X[3][0]=30;
		X[3][1]=230;
		int[] dataLengths={360,360,360,2};
		boolean[] isColumns={false,false,false,false};
		int left=100,top=100,width=800,height=600,row=10,column=20;
		this.Histogram1=new Histogram(X,Y,dataLengths,isColumns,left,top,width,height,row,column);		
	}
	public void paint(Graphics g)
	{
		if(Histogram1!=null)this.Histogram1.drawHistogram(g,Color.black,new Color[]{Color.blue,Color.green,Color.yellow,Color.red},Color.white);
	}
}
class LineCoordinateTransform
{
	int m=1000;
	int[][] numberOfIntersectionPointsInPolarCoordinates;
	public LineCoordinateTransform()
	{
		this.numberOfIntersectionPointsInPolarCoordinates=new int[m][360];
	}
	public double[] getYCoordinates(double p0,double w0,double x0,double x1)
	{
		double[] yCoordinates=new double[2];
		double xC=p0*Math.cos(w0);
		double yC=p0*Math.sin(w0);
		yCoordinates[0]=yC+(xC-x0)/Math.tan(w0);
		yCoordinates[1]=yC+(xC-x1)/Math.tan(w0);
		return yCoordinates;
	}
	public double[] getPolarCoordinate(double x0,double y0)
	{
		double w=0.0;
		double[] polarCoordinate=new double[360];
		for(int i=0;i<360;i++)
		{
			w=(i+0.0)/360.0*2.0*Math.PI;
			polarCoordinate[i]=x0*Math.cos(w)+y0*Math.sin(w);
			int p=(int)polarCoordinate[i];
			if(0<=p&&p<m)this.numberOfIntersectionPointsInPolarCoordinates[p][i]++;
		}
		return polarCoordinate;
	}
	public double[] getMaxNumberOfIntersectionPointsInPolarCoordinates()
	{
		double[] pw=new double[2];
		int maxNumber=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<360;j++)
			{
				if(numberOfIntersectionPointsInPolarCoordinates[i][j]>maxNumber)
				{
					maxNumber=numberOfIntersectionPointsInPolarCoordinates[i][j];
					pw[0]=i;
					pw[1]=(j+0.0)/(360.0)*(2*Math.PI);
				}
			}
		}
		System.out.println("maxNumber="+maxNumber);
		return pw;
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
