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
			Image1=Toolkit.getDefaultToolkit().getImage("JavaAndPixel.gif");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] Pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,Pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			for(int i=0;i<imageHeight;i++)
			{
				for(int j=0;j<imageWidth;j++)
				{
					int alpha=this.getAlpha(Pixels[i*imageWidth+j]);
					int red=this.getRed(Pixels[i*imageWidth+j]);
					int green=this.getGreen(Pixels[i*imageWidth+j]);
					int blue=this.getBlue(Pixels[i*imageWidth+j]);
					Pixels[i*imageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
				}
			}
			ImageOp.writeOpenGLImage("JavaAndOpenGL",Pixels,imageWidth,imageHeight);
			Pixels=ImageOp.readOpenGLImage("JavaAndOpenGL",imageWidth,imageHeight);
			MemoryImageSource MemoryImageSource1=new MemoryImageSource(imageWidth,imageHeight,Pixels,0,imageWidth);
			BufferedImage BufferedImage2=ImageOp.getBufferedImage(Pixels,imageWidth,imageHeight);
			ImageIO.write(BufferedImage2,"JPG",new File("JavaAndPixel.jpg"));
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
	public static void writeOpenGLImage(String fileName,int[] pixels,int imageWidth,int imageHeight)
	{
		try
		{
			int k=0;
			byte[] bytes=new byte[imageHeight*imageWidth*4];
			for(int i=imageHeight-1;i>=0;i--)
			{
				for(int j=imageWidth-1;j>=0;j--)
				{
					int pixel=pixels[i*imageWidth+j];
					int alpha=(pixel>>24)&255;
					int red=(pixel>>16)&255;
					int green=(pixel>>8)&255;
					int blue=pixel&255;
					bytes[k++]=(byte)red;
					bytes[k++]=(byte)green;
					bytes[k++]=(byte)blue;
					bytes[k++]=(byte)alpha;
				}
			}
			File file=new File(fileName+"("+imageWidth+","+imageHeight+").OPGL");
     			FileOutputStream FileOutputStream1=new FileOutputStream(file);
			FileOutputStream1.write(bytes);
			FileOutputStream1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public static int[] readOpenGLImage(String fileName,int imageWidth,int imageHeight)
	{
		int[] pixels=null;
		try
		{
			File file=new File(fileName+"("+imageWidth+","+imageHeight+").OPGL");
     			FileInputStream FileInputStream1=new FileInputStream(file);
			byte[] bytes=new byte[imageHeight*imageWidth*4];
			FileInputStream1.read(bytes);
			FileInputStream1.close();
			pixels=new int[imageHeight*imageWidth];
			int k=0;
			for(int i=imageHeight-1;i>=0;i--)
			{
				for(int j=imageWidth-1;j>=0;j--)
				{
					int red=(int)bytes[k++];
					int green=(int)bytes[k++];
					int blue=(int)bytes[k++];
					int alpha=(int)bytes[k++];
					if(alpha<0)alpha+=256;
					if(red<0)red+=256;
					if(green<0)green+=256;
					if(blue<0)blue+=256;
					pixels[i*imageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
				}
			}
		}
		catch(Exception e){e.printStackTrace();}
		return pixels;
	}
	private static byte[] toBytes(int a)
	{
		int DH=(a>>24)&255;
		int DL=(a>>16)&255;
		int AH=(a>>8)&255;
		int AL=a&255;
		return new byte[]{(byte)DH,(byte)DL,(byte)AH,(byte)AL};
	}
	private static int toInt(byte[] b,int k)
	{
		int DH=(int)b[k++];
		int DL=(int)b[k++];
		int AH=(int)b[k++];
		int AL=(int)b[k++];
		if(DH<0)DH+=256;
		if(DL<0)DL+=256;
		if(AH<0)AH+=256;
		if(AL<0)AL+=256;
		return (DH<<24)|(DL<<16)|(AH<<8)|AL;
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
