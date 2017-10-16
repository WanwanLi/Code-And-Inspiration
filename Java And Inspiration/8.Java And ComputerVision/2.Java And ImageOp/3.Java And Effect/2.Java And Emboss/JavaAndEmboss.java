import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndEmboss
{
	public static void main(String[] args)
	{
		Frame_Emboss Frame_Emboss1=new Frame_Emboss();
		Frame_Emboss1.setVisible(true);
	}
}
class Frame_Emboss extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_Emboss()
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
			ImageOp1.emboss(5,255,0,0);
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
	public void emboss(int k,int r,int g,int b)
	{
		for(int i=0;i<ImageHeight-1;i++)
		{
			for(int j=0;j<ImageWidth-1;j++)
			{
				int alpha=255;
				int red=bound((getRed(Pixels[i*ImageWidth+j])-getRed(Pixels[(i+1)*ImageWidth+(j+1)]))*k+r);
				int green=bound((getGreen(Pixels[i*ImageWidth+j])-getGreen(Pixels[(i+1)*ImageWidth+(j+1)]))*k+g);
				int blue=bound((getBlue(Pixels[i*ImageWidth+j])-getBlue(Pixels[(i+1)*ImageWidth+(j+1)]))*k+b);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private int bound(int x)
	{
		if(x<0)x=0;
		if(x>255)x=255;
		return x;
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