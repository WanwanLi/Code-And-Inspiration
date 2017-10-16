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
			Image1=Toolkit.getDefaultToolkit().getImage("JavaAndPixel.JPG");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int k=4;
			int imageWidth1=imageWidth/k;
			int imageHeight1=imageHeight/k;
			int[] Pixels=new int[imageWidth*imageHeight];
			int[] Pixels1=new int[imageWidth1*imageHeight1];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,Pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			for(int i=0;i<imageHeight1;i++)
			{
				for(int j=0;j<imageWidth1;j++)
				{
					int id=k*(i*imageWidth+j);
					int alpha=this.getAlpha(Pixels[id]);
					int red=this.getRed(Pixels[id]);
					int green=this.getGreen(Pixels[id]);
					int blue=this.getBlue(Pixels[id]);
					Pixels1[i*imageWidth1+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
				}
			}
			MemoryImageSource MemoryImageSource1=new MemoryImageSource(imageWidth1,imageHeight1,Pixels1,0,imageWidth1);
			BufferedImage BufferedImage1=ImageOp.getBufferedImage(Pixels1,imageWidth1,imageHeight1);
			ImageIO.write(BufferedImage1,"JPG",new File("JavaAndPixel1.JPG"));
			Image2=this.createImage(MemoryImageSource1);
		}
		catch(Exception e){e.printStackTrace();}
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
	public static BufferedImage getBufferedImage(int[] pixels,int imageWidth,int imageHeight)
	{
		BufferedImage BufferedImage1=new BufferedImage(imageWidth,imageHeight,1);
		Graphics g=BufferedImage1.getGraphics();
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				int pixel=pixels[i*imageWidth+j];
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
}