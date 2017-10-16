import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndPixel
{
	public static void main(String[] args)
	{
		Frame_Pixel Frame_Pixel1=new Frame_Pixel();
		Frame_Pixel1.setVisible(true);
	}
}
class Frame_Pixel extends Frame
{
	public Image Image1,Image2;
	private int imageWidth,imageHeight;
	public Frame_Pixel()
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
			int[] Pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,Pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(Pixels,imageWidth,imageHeight);
			ImageOp1.setColor(Color.red);
			ImageOp1.drawLine(30,50,300,500);
			MemoryImageSource MemoryImageSource1=ImageOp1.getMemoryImageSource();
			Image2=this.createImage(MemoryImageSource1);
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
	private int Red=0,Green=0,Blue=0;
	public void setColor(Color color)
	{
		this.Red=color.getRed();
		this.Green=color.getGreen();
		this.Blue=color.getBlue();
	}
	public void drawLine(int x0,int y0,int x1,int y1)
	{
		if(x0>x1){x0+=x1;x1=x0-x1;x0-=x1;}
		if(y0>y1){y0+=y1;y1=y0-y1;y0-=y1;}
		int dx=x1-x0,dy=y1-y0;
		if(dx>dy)
		{
			int y=y0,e=2*dy-dx;
			for(int x=x0;x<=x1;x++)
			{
				this.setPixel(x,y);
				if(e>0){y++;e+=2*dy-2*dx;}
				else e+=2*dy;
			}
		}
		else
		{
			int x=x0,e=2*dx-dy;
			for(int y=y0;y<=y1;y++)
			{
				this.setPixel(x,y);
				if(e>0){x++;e+=2*dx-2*dy;}
				else e+=2*dx;
			}
		}
	}
	public void setPixel(int x,int y)
	{
		if(x<0||x>ImageWidth)return;
		if(y<0||y>ImageHeight)return;
		this.Pixels[y*ImageWidth+x]=(255<<24|Red<<16|Green<<8|Blue);
	}
	private static int getAlpha(int pixel)
	{
		return (pixel>>24)&0xff;
	}
	private static int getRed(int pixel)
	{
		return (pixel>>16)&0xff;
	}
	private static int getGreen(int pixel)
	{
		return (pixel>>8)&0xff;
	}
	private static int getBlue(int pixel)
	{
		return pixel&0xff;
	}
	public BufferedImage getBufferedImage()
	{
		BufferedImage BufferedImage1=new BufferedImage(ImageWidth,ImageHeight,1);
		Graphics g=BufferedImage1.getGraphics();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int pixel=this.Pixels[i*ImageWidth+j];
				float alpha=getAlpha(pixel)/255f;
				float red=getRed(pixel)/255f;
				float green=getGreen(pixel)/255f;
				float blue=getBlue(pixel)/255f;
				g.setColor(new Color(red,green,blue,alpha));
				g.drawLine(j,i,j,i);
			}
		}
		return BufferedImage1;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}