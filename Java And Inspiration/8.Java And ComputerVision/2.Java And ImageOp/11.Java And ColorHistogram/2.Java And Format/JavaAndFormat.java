import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndFormat
{
	public static void main(String[] args)
	{
		Frame_Format Frame_Format1=new Frame_Format();
		Frame_Format1.setVisible(true);
	}
}
class Frame_Format extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	Histogram Histogram1;
	public Frame_Format()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		try
		{
			Image1=Toolkit.getDefaultToolkit().getImage("..\\..\\JavaAndImageProcessing.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.grey(1,1,1);
			int len=4;
			double[] x=new double[len];
			double[] y=new double[len];
			for(int i=1;i<len;i++)
			{
				x[i]=255/len*i;
				y[i]=1.0/len;
			}
			ImageOp1.format(x,y);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
			this.getGreyImageHistogram(ImageOp1);
		}
		catch(Exception e){e.printStackTrace();}
	}
	private void getGreyImageHistogram(ImageOp ImageOp1)
	{
		double[] x=new double[256];
		double[] y=ImageOp1.getHistogram();
		for(int i=0;i<256;i++)x[i]=i;
		int left=100,top=100,width=800,height=600,row=10,column=20;
		this.Histogram1=new Histogram(x,y,left,top,width,height,row,column);		
	}
	public void paint(Graphics g)
	{
		if(Histogram1!=null)this.Histogram1.draw(g,Color.black,Color.white,Color.green);
		if(Image2!=null)g.drawImage(Image2,920,100,this);
	}
}
class ImageOp
{
	public int ImageWidth;
	public int ImageHeight;
	public int[] Pixels;
	public ImageOp(int[] pixels,int imageWidth,int imageHeight)
	{
		this.ImageWidth=imageWidth;
		this.ImageHeight=imageHeight;
		this.Pixels=new int[imageWidth*imageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				this.Pixels[i*ImageWidth+j]=pixels[i*ImageWidth+j];
			}
		}
	}
	public void grey(double r,double g,double b)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				double Grey=(r*red+g*green+b*blue)/(r+g+b);
				double Alpha=alpha;
				double Red=Grey;
				double Green=Grey;
				double Blue=Grey;
				Pixels[i*ImageWidth+j]=this.getARGBvalue(Alpha,Grey,Grey,Grey);
			}
		}
	}
	public void format(double[] x,double[] y)
	{
		int len=x.length<y.length?x.length:y.length;
		double[] Y=new double[len];
		Y[0]=y[0];
		for(int i=1;i<len;i++)Y[i]=Y[i-1]+y[i];
		double[] histogram=this.getHistogram();
		double[] Histogram=new double[256];
		Histogram[0]=histogram[0];
		for(int i=1;i<256;i++)Histogram[i]=Histogram[i-1]+histogram[i];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=this.getAlpha(Pixels[i*ImageWidth+j]);
				int grey=this.getRed(Pixels[i*ImageWidth+j]);
				int newGrey=grey;
				double H=Histogram[grey];
				if(H<Y[0])newGrey=(int)(x[0]*H/y[0]);
				else if(H>Y[len-1])newGrey=(int)(x[len-1]+(255-x[len-1])*(H-Y[len-1])/(1.0-Y[len-1]));
				else
				{
					int n=0;
					for(;n<len-1;n++)if(H<=Y[n+1])break;
					newGrey=(int)(x[n]+(x[n+1]-x[n])*(H-Y[n])/(Y[n+1]-Y[n]));
				}
				Pixels[i*ImageWidth+j]=(alpha<<24)|(newGrey<<16)|(newGrey<<8)|newGrey;
			}
		}
	}
	public double[] getHistogram()
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		double[] histogram=new double[256];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int red=this.getRed(Pixels[i*ImageWidth+j]);
				histogram[red]++;
			}
		}
		for(int i=0;i<256;i++)histogram[i]/=ImageWidth*ImageHeight;
		return histogram;
	}
	private int getARGBvalue(double alpha,double red,double green,double blue)
	{
		if(alpha<0)alpha=0;
		if(red<0)red=0;
		if(green<0)green=0;
		if(blue<0)blue=0;
		if(alpha>255)alpha=255;
		if(red>255)red=255;
		if(green>255)green=255;
		if(blue>255)blue=255;
		return ((int)alpha<<24)|((int)red<<16)|((int)green<<8)|(int)blue;
	}
	private int getAlpha(int pixel)
	{
		return pixel>>24;
	}
	private int getRed(int pixel)
	{
		return (pixel>>16)&0xff;
	}
	private int getGreen(int pixel)
	{
		return (pixel>>8)&0xff;
	}
	private int getBlue(int pixel)
	{
		return pixel&0xff;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}
class Histogram
{
	private double[] x;
	private double[] y;
	private int length,left,top,width,height,row,column;
	private double minX,maxX,minY,maxY,scaleX,scaleY;
	private int edgeX=60,edgeY=30,precisionX=2,precisionY=5;
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
			int y0=transformToCoordinateY(0);
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
	private String format(double x,int precision)
	{
		if(x>-1E-5&&x<1E-5)x=0;
		String string=x+"";
		String formatString="";
		int i=0,j=0,len=string.length();
		while(i<len&&string.charAt(i)!='.')formatString+=string.charAt(i++);
		for(;j<=precision&&i<len;j++,i++)formatString+=string.charAt(i);
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
			g.drawString(format(coordinateY,precisionY),x0-edgeX,y);
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
			g.drawString(format(coordinateX,precisionX),x,y2+edgeY);
		}
	}
	public void draw(Graphics g,Color backColor,Color gridColor,Color dataColor)
	{
		g.setColor(backColor);
		g.fillRect(left-edgeX,top-edgeY,width+2*edgeX,height+2*edgeY);
		g.setColor(gridColor);
		this.drawGrid(g);
		g.setColor(dataColor);
		this.drawColumns(g);
	}
	public void paint(Graphics g,Color backColor,Color gridColor,Color dataColor)
	{
		g.setColor(backColor);
		g.fillRect(left-edgeX,top-edgeY,width+2*edgeX,height+2*edgeY);
		g.setColor(gridColor);
		this.drawGrid(g);
		g.setColor(dataColor);
		this.drawDatas(g);
	}
}