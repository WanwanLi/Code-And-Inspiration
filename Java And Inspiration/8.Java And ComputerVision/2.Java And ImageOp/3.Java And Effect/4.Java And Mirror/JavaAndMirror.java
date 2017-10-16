import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndMirror
{
	public static void main(String[] args)
	{
		Frame_Mirror Frame_Mirror1=new Frame_Mirror();
		Frame_Mirror1.setVisible(true);
	}
}
class Frame_Mirror extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_Mirror()
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
			ImageOp1.mirror(ImageOp.HORIZONTAL);
			ImageOp1.mirror(ImageOp.VERTICAL);
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
	public static final int HORIZONTAL=0;
	public static final int VERTICAL=1;
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
	public void mirror(int direction)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] pixels=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(direction==HORIZONTAL)pixels[i*ImageWidth+j]=Pixels[i*ImageWidth+(ImageWidth-1-j)];
				else if(direction==VERTICAL)pixels[i*ImageWidth+j]=Pixels[(ImageHeight-1-i)*ImageWidth+j];
				else return;
			}
		}
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)Pixels[i*ImageWidth+j]=pixels[i*ImageWidth+j];
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
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}