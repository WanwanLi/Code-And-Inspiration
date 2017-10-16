import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndMotion
{
	public static void main(String[] args)
	{
		Frame_Motion Frame_Motion1=new Frame_Motion();
		Frame_Motion1.setVisible(true);
	}
}
class Frame_Motion extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_Motion()
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
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.motion(10.5,20.5,8);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image2!=null)g.drawImage(Image2,100+imageWidth+100,100,this);
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
	public void motion(double x,double y,int T)
	{
		int[] pixels=new int[ImageWidth*ImageHeight];
		double u=x/T;
		double v=y/T;
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int n=0;
				double alpha=0;
				double red=0;
				double green=0;
				double blue=0;
				for(int t=0;t<T;t++)
				{
					double[] ARGBvalue=this.getARGBvalue(j-u*t,i-v*t);
					if(ARGBvalue==null)continue;
					alpha+=ARGBvalue[0];
					red+=ARGBvalue[1];
					green+=ARGBvalue[2];
					blue+=ARGBvalue[3];
					n++;
				}
				pixels[i*ImageWidth+j]=this.getARGBvalue(alpha/n,red/n,green/n,blue/n);
			}
		}
		this.Pixels=pixels;
	}
	private double[] getARGBvalue(double x,double y)
	{
		double[] ARGBvalue=new double[4];
		int j0=(int)x;
		int j1=j0+1;
		if(j0<0||j1>=ImageWidth)return null;
		int i0=(int)y;
		int i1=i0+1;
		if(i0<0||i1>=ImageHeight)return null;
		double alpha0=interpolate(getAlpha(Pixels[i0*ImageWidth+j0]),getAlpha(Pixels[i0*ImageWidth+j1]),x-j0);
		double alpha1=interpolate(getAlpha(Pixels[i1*ImageWidth+j0]),getAlpha(Pixels[i1*ImageWidth+j1]),x-j0);
		double red0=interpolate(getRed(Pixels[i0*ImageWidth+j0]),getRed(Pixels[i0*ImageWidth+j1]),x-j0);
		double red1=interpolate(getRed(Pixels[i1*ImageWidth+j0]),getRed(Pixels[i1*ImageWidth+j1]),x-j0);
		double green0=interpolate(getGreen(Pixels[i0*ImageWidth+j0]),getGreen(Pixels[i0*ImageWidth+j1]),x-j0);
		double green1=interpolate(getGreen(Pixels[i1*ImageWidth+j0]),getGreen(Pixels[i1*ImageWidth+j1]),x-j0);
		double blue0=interpolate(getBlue(Pixels[i0*ImageWidth+j0]),getBlue(Pixels[i0*ImageWidth+j1]),x-j0);
		double blue1=interpolate(getBlue(Pixels[i1*ImageWidth+j0]),getBlue(Pixels[i1*ImageWidth+j1]),x-j0);
		ARGBvalue[0]=interpolate(alpha0,alpha1,y-i0);
		ARGBvalue[1]=interpolate(red0,red1,y-i0);
		ARGBvalue[2]=interpolate(green0,green1,y-i0);
		ARGBvalue[3]=interpolate(blue0,blue1,y-i0);
		return ARGBvalue;
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
	private double interpolate(double a,double b,double k)
	{
		return (a*(1-k)+b*k);
	}
	private int getAlpha(int pixel)
	{
		return (pixel>>24)&0xff;
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