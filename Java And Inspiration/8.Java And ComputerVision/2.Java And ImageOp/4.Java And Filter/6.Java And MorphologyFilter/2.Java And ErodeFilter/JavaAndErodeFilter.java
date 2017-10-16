import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndErodeFilter
{
	public static void main(String[] args)
	{
		Frame_ErodeFilter Frame_ErodeFilter1=new Frame_ErodeFilter();
		Frame_ErodeFilter1.setVisible(true);
	}
}
class Frame_ErodeFilter extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_ErodeFilter()
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
			Image1=Toolkit.getDefaultToolkit().getImage("..\\..\\..\\..\\JavaAndImageProcessing.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			Image1=this.createImage(ImageOp1.getMemoryImageSource());
			int[][] Filter=new int[][]
			{
				{5,10,5},
				{10,20,10},
				{5,10,5}
			};
			ImageOp1.erodeFilter(Filter);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
			//BufferedImage BufferedImage2=ImageOp1.getBufferedImage();
			//ImageIO.write(BufferedImage2,"JPG",new File("JavaAndIamgeIO.jpg"));
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
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
	public void erodeFilter(int[][] Filter)
	{
		int row=Filter.length;
		int column=Filter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		int[] pixels=new int[ImageWidth*ImageHeight];
		for(int i=r;i<ImageHeight-r;i++)
		{
			for(int j=c;j<ImageWidth-c;j++)
			{
				int[] erodeFilterARGBvalue=this.getErodeFilterARGBvalue(Pixels,Filter,r,c,i,j);
				int alpha=this.bound(erodeFilterARGBvalue[0]);
				int red=this.bound(erodeFilterARGBvalue[1]);
				int green=this.bound(erodeFilterARGBvalue[2]);
				int blue=this.bound(erodeFilterARGBvalue[3]);
				pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
		this.Pixels=pixels;
	}
	private int[] getErodeFilterARGBvalue(int[] pixels,int[][] erodeFilter,int r,int c,int i,int j)
	{
		int[] erodeFilterARGBvalue=new int[]{255,255,255,255};
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				int alpha=this.getAlpha(pixels[u*ImageWidth+v])-erodeFilter[u-(i-r)][v-(j-c)];
				int red=this.getRed(pixels[u*ImageWidth+v])-erodeFilter[u-(i-r)][v-(j-c)];
				int green=this.getGreen(pixels[u*ImageWidth+v])-erodeFilter[u-(i-r)][v-(j-c)];
				int blue=this.getBlue(pixels[u*ImageWidth+v])-erodeFilter[u-(i-r)][v-(j-c)];
				//if(alpha<erodeFilterARGBvalue[0])erodeFilterARGBvalue[0]=alpha;
				if(red<erodeFilterARGBvalue[1])erodeFilterARGBvalue[1]=red;
				if(green<erodeFilterARGBvalue[2])erodeFilterARGBvalue[2]=green;
				if(blue<erodeFilterARGBvalue[3])erodeFilterARGBvalue[3]=blue;
			}
		}
		return erodeFilterARGBvalue;
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
	private int bound(int x)
	{
		if(x<0)x=0;
		if(x>255)x=255;
		return x;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}