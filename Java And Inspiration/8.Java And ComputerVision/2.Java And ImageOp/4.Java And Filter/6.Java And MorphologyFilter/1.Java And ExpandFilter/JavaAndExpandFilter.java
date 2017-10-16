import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndExpandFilter
{
	public static void main(String[] args)
	{
		Frame_ExpandFilter Frame_ExpandFilter1=new Frame_ExpandFilter();
		Frame_ExpandFilter1.setVisible(true);
	}
}
class Frame_ExpandFilter extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_ExpandFilter()
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
			ImageOp1.expandFilter(Filter);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
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
	public void expandFilter(int[][] Filter)
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
				int[] expandFilterARGBvalue=this.getExpandFilterARGBvalue(Pixels,Filter,r,c,i,j);
				int alpha=this.bound(expandFilterARGBvalue[0]);
				int red=this.bound(expandFilterARGBvalue[1]);
				int green=this.bound(expandFilterARGBvalue[2]);
				int blue=this.bound(expandFilterARGBvalue[3]);
				pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
		this.Pixels=pixels;
	}
	private int[] getExpandFilterARGBvalue(int[] pixels,int[][] expandFilter,int r,int c,int i,int j)
	{
		int[] expandFilterARGBvalue=new int[4];
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				int alpha=this.getAlpha(pixels[u*ImageWidth+v])+expandFilter[u-(i-r)][v-(j-c)];
				int red=this.getRed(pixels[u*ImageWidth+v])+expandFilter[u-(i-r)][v-(j-c)];
				int green=this.getGreen(pixels[u*ImageWidth+v])+expandFilter[u-(i-r)][v-(j-c)];
				int blue=this.getBlue(pixels[u*ImageWidth+v])+expandFilter[u-(i-r)][v-(j-c)];
				if(alpha>expandFilterARGBvalue[0])expandFilterARGBvalue[0]=alpha;
				if(red>expandFilterARGBvalue[1])expandFilterARGBvalue[1]=red;
				if(green>expandFilterARGBvalue[2])expandFilterARGBvalue[2]=green;
				if(blue>expandFilterARGBvalue[3])expandFilterARGBvalue[3]=blue;
			}
		}
		return expandFilterARGBvalue;
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