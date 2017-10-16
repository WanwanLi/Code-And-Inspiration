import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndMatch
{
	public static void main(String[] args)
	{
		Frame_Match Frame_Match1=new Frame_Match();
		Frame_Match1.setVisible(true);
	}
}
class Frame_Match extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	Histogram Histogram1;
	public Frame_Match()
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
			Image1=Toolkit.getDefaultToolkit().getImage("..\\..\\..\\JavaAndImageProcessing.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			double contrast=1.5,brightness=20;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			int len=256;
			double u=128;
			double o=40;
			double[] y=new double[len];
			for(int i=0;i<len;i++)y[i]=NormalDistribution(i,u,o);
			ImageOp1.match(y);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
			this.getBrightImageHistogram(ImageOp1);
		}
		catch(Exception e){e.printStackTrace();}
	}
	private void getBrightImageHistogram(ImageOp ImageOp1)
	{
		double[] x=new double[256];
		double[] y=ImageOp1.getHistogram(ImageOp1.getBrightnessValues());
		for(int i=0;i<256;i++)x[i]=i;
		int left=100,top=100,width=800,height=600,row=10,column=20;
		this.Histogram1=new Histogram(x,y,left,top,width,height,row,column);		
	}
	private double NormalDistribution(double x,double u,double o)
	{
		return Math.exp(-(x-u)*(x-u)/(2*o*o))/(o*Math.sqrt(2*Math.PI));
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
	private int[] getNewBrightness(double[] H,double[] Y)
	{
		int[] NewBrightness=new int[256];
		for(int brightness=0;brightness<256;brightness++)
		{
			int newBrightness=0;
			for(;newBrightness<255;newBrightness++)if(Y[newBrightness]>=H[brightness])break;
			NewBrightness[brightness]=newBrightness;
		}
		return NewBrightness;
	}
	public void match(double[] y)
	{
		double[] Y=new double[256];
		Y[0]=y[0];
		for(int i=1;i<256;i++)Y[i]=Y[i-1]+y[i];
		int[] brightnessValues=this.getBrightnessValues();
		double[] histogram=this.getHistogram(brightnessValues);
		double[] H=new double[256];
		H[0]=histogram[0];
		for(int i=1;i<256;i++)H[i]=H[i-1]+histogram[i];
		int[] NewBrightness=this.getNewBrightness(H,Y);
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=this.getAlpha(Pixels[i*ImageWidth+j]);
				int red=this.getRed(Pixels[i*ImageWidth+j]);
				int green=this.getGreen(Pixels[i*ImageWidth+j]);
				int blue=this.getBlue(Pixels[i*ImageWidth+j]);
				double[] HSB=ColorSpace.getHSBfromRGB(red,green,blue);
				double hue=HSB[0],saturation=HSB[1],brightness=HSB[2];
				brightness=(NewBrightness[(int)(brightness*255)]+0.0)/255;
				int[] RGB=ColorSpace.getRGBfromHSB(hue,saturation,brightness);
				red=RGB[0];green=RGB[1];blue=RGB[2];
				this.Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public double[] getHistogram(int[] brightnessValues)
	{
		double[] histogram=new double[256];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				histogram[brightnessValues[i*ImageWidth+j]]++;
			}
		}
		for(int i=0;i<256;i++)histogram[i]/=ImageHeight*ImageWidth;
		return histogram;
	}
	public int[] getBrightnessValues()
	{
		int[] brightness=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int red=this.getRed(Pixels[i*ImageWidth+j]);
				int green=this.getGreen(Pixels[i*ImageWidth+j]);
				int blue=this.getBlue(Pixels[i*ImageWidth+j]);
				brightness[i*ImageWidth+j]=max(red,green,blue);
			}
		}
		return brightness;
	}
	private int max(int r,int g,int b)
	{
		int m=(r>g?r:g);
		return (m>b?m:b);
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
class ColorSpace
{
	public static double[] getHSBfromRGB(int red,int green,int blue)
	{
		double[] HSB=new double[3];
		if(red==green&&green==blue)
		{
			HSB[0]=-1;
			HSB[1]=0;
			HSB[2]=(red+0.0)/255;
			return HSB;
		}
		double hue=0,saturation=0,brightness=0;
		int high=max(red,green,blue);
		int low=min(red,green,blue);
		int range=high-low;
		if(red==high)hue=(green-blue+0.0)/range+0;
		else if(green==high)hue=(blue-red+0.0)/range+2;
		else if(blue==high)hue=(red-green+0.0)/range+4;
		if(hue<0)hue+=6;
		hue/=6;
		saturation=(range+0.0)/high;
		brightness=(high+0.0)/255;
		HSB[0]=hue;
		HSB[1]=saturation;
		HSB[2]=brightness;
		return HSB;
	}
	public static int[] getRGBfromHSB(double hue,double saturation,double brightness)
	{
		int[] RGB=new int[3];
		if(hue==-1)
		{
			RGB[0]=(int)(255*brightness);
			RGB[1]=(int)(255*brightness);
			RGB[2]=(int)(255*brightness);
			return RGB;
		}
		double red=0,green=0,blue=0;
		double range=saturation*brightness;
		double high=brightness;
		double low=brightness-range;
		double Hue=(6.0*hue)%6.0;
		int index=(int)Hue;
		double mid_low=(index%2==0?(Hue-index)*range:(index+1-Hue)*range);
		double mid=low+mid_low;
		switch(index)
		{
			case 0:red=high;green=mid;blue=low;break;
			case 1:red=mid;green=high;blue=low;break;
			case 2:red=low;green=high;blue=mid;break;
			case 3:red=low;green=mid;blue=high;break;
			case 4:red=mid;green=low;blue=high;break;
			case 5:red=high;green=low;blue=mid;break;
		}	
		RGB[0]=(int)(255*red);
		RGB[1]=(int)(255*green);
		RGB[2]=(int)(255*blue);
		return RGB;
	}
	public static int max(int x,int y,int z)
	{
		int m=(z>y?z:y);
		return (x>m?x:m);
	}
	public static int min(int x,int y,int z)
	{
		int m=(z<y?z:y);
		return (x<m?x:m);
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