import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndHoughLine
{
	public static void main(String[] args)
	{
		Frame_HoughLine Frame_HoughLine1=new Frame_HoughLine();
		Frame_HoughLine1.setVisible(true);
	}
}
class Frame_HoughLine extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_HoughLine()
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
			Image1=Toolkit.getDefaultToolkit().getImage("..\\JavaAndHough.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			int thresholdValue=20;
			ImageOp1.getHoughLine(Color.blue,thresholdValue);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.red);
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image2!=null)g.drawImage(Image2,600,100,this);
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
	private int[] getBinaryValues(int thresholdValue)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] binaryValues=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				int grey=(red+green+blue)/3;
				if(grey<thresholdValue)binaryValues[i*ImageWidth+j]=1;
				else binaryValues[i*ImageWidth+j]=0;
			}
		}
		return binaryValues;
	}
	public void binary(Color foregroundColor,Color backgroundColor,int thresholdValue)
	{
		int[] binaryValues=this.getBinaryValues(thresholdValue);
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=255;
				int red=backgroundColor.getRed();
				int green=backgroundColor.getGreen();
				int blue=backgroundColor.getBlue();
				if(binaryValues[i*ImageWidth+j]==1)
				{
					red=foregroundColor.getRed();
					green=foregroundColor.getGreen();
					blue=foregroundColor.getBlue();
				}
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void getHoughLine(Color lineColor,int thresholdValue)
	{
		int[] binaryValues=this.getBinaryValues(thresholdValue);
		LineCoordinateTransform LineCoordinateTransform1=new LineCoordinateTransform();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(binaryValues[i*ImageWidth+j]==1)LineCoordinateTransform1.addPolarCoordinate(j,i);
			}
		}
		double[] p0w0=LineCoordinateTransform1.getMaxNumberOfIntersectionPointsInPolarCoordinates();
		double[] y0y1=LineCoordinateTransform1.getYCoordinates(p0w0[0],p0w0[1],0,ImageWidth);
		for(int j=0;j<ImageWidth;j++)
		{
			int alpha=255;
			int red=lineColor.getRed();
			int green=lineColor.getGreen();
			int blue=lineColor.getBlue();
			int i=(int)(y0y1[0]+((y0y1[1]-y0y1[0])/ImageWidth)*j);
			Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
		}
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}
class LineCoordinateTransform
{
	int m=500;
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
	public void addPolarCoordinate(double x0,double y0)
	{
		double w=0.0;
		for(int i=0;i<360;i++)
		{
			w=(i+0.0)/360.0*2.0*Math.PI;
			int p=(int)(x0*Math.cos(w)+y0*Math.sin(w));
			if(0<=p&&p<m)this.numberOfIntersectionPointsInPolarCoordinates[p][i]++;
		}
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
