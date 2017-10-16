import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndEdge
{
	public static void main(String[] args)
	{
		Frame_Edge Frame_Edge1=new Frame_Edge();
		Frame_Edge1.setVisible(true);
	}
}
class Frame_Edge extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_Edge()
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
			double minGradientGrey=3;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.grey(1,1,1);
			ImageOp1.edge(minGradientGrey);
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
	public void edge(double minGradientGrey)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] pixels=new int[ImageWidth*ImageHeight];
		for(int i=1;i<ImageHeight-2;i++)
		{
			for(int j=1;j<ImageWidth-2;j++)
			{
				int alpha=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				double gradientGrey_0=this.getGradientGrey(i,j-1);
				double gradientGrey_1=this.getGradientGrey(i,j+1);
				double gradientGrey0_=this.getGradientGrey(i-1,j);
				double gradientGrey1_=this.getGradientGrey(i+1,j);
				double gradientGrey=this.getGradientGrey(i,j)-minGradientGrey;
				int red=255;
				int green=0;
				int blue=0;
				if((gradientGrey>gradientGrey_0&&gradientGrey>gradientGrey_1)||(gradientGrey>gradientGrey0_&&gradientGrey>gradientGrey1_))pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
				else pixels[i*ImageWidth+j]=Pixels[i*ImageWidth+j];
			}
		}
		this.Pixels=pixels;
	}
	private double gradient(int y0,int y1,int y2)
	{
		int dy1=y1-y0;
		int dy2=y2-y0;
		return Math.abs(dy1)+Math.abs(dy2);
	}
	public double getGradientGrey(int i,int j)
	{
		int grey=Pixels[i*ImageWidth+j]&0xff;
		int grey_1=Pixels[i*ImageWidth+(j+1)]&0xff;
		int grey1_=Pixels[(i+1)*ImageWidth+j]&0xff;
		double gradientGrey=gradient(grey,grey_1,grey1_);
		return gradientGrey;
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