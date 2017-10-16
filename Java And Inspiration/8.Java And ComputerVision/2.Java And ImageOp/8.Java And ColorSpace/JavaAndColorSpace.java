import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndColorSpace
{
	public static void main(String[] args)
	{
		Frame_ColorSpace Frame_ColorSpace1=new Frame_ColorSpace();
		Frame_ColorSpace1.setVisible(true);
	}
}
class Frame_ColorSpace extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_ColorSpace()
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
			double contrast=1.5,brightness=20;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.invert();
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
	public void invert()
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
				double[] HSB=ColorSpace.getHSBfromRGB(red,green,blue);
				double hue=HSB[0],saturation=HSB[1],brightness=HSB[2];
				int[] RGB=ColorSpace.getRGBfromHSB(hue,saturation,brightness);
				red=RGB[0];green=RGB[1];blue=RGB[2];
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
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
	private static int max(int x,int y,int z)
	{
		int m=(z>y?z:y);
		return (x>m?x:m);
	}
	private static int min(int x,int y,int z)
	{
		int m=(z<y?z:y);
		return (x<m?x:m);
	}
}