import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndLaplaceFilter
{
	public static void main(String[] args)
	{
		Frame_LaplaceFilter Frame_LaplaceFilter1=new Frame_LaplaceFilter();
		Frame_LaplaceFilter1.setVisible(true);
	}
}
class Frame_LaplaceFilter extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_LaplaceFilter()
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
			double[][] filter=ImageOp1.getLaplaceFilterKernel(4);
			double scale=1.5;
			ImageOp1.filter(filter,scale);
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
	public double[][] getLaplaceFilterKernel(int neighbor)
	{
		if(neighbor==4)return new double[][]
		{
			{ 0,-1, 0},
			{-1, 4,-1},
			{ 0,-1, 0}
		};
		else return new double[][]
		{
			{-1,-1,-1},
			{-1, 8,-1},
			{-1,-1,-1}
		};
	}
	public void filter(double[][] filter,double scale)
	{
		int row=filter.length;
		int column=filter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		int[] pixels=new int[ImageWidth*ImageHeight];
		for(int i=r;i<ImageHeight-r;i++)
		{
			for(int j=c;j<ImageWidth-c;j++)
			{
				double[] ARGBvalues=this.getFilterARGBvalues(Pixels,filter,r,c,i,j);
				double alpha=this.getAlpha(Pixels[i*ImageWidth+j])+scale*ARGBvalues[0];
				double red=this.getRed(Pixels[i*ImageWidth+j])+scale*ARGBvalues[1];
				double green=this.getGreen(Pixels[i*ImageWidth+j])+scale*ARGBvalues[2];
				double blue=this.getBlue(Pixels[i*ImageWidth+j])+scale*ARGBvalues[3];
				pixels[i*ImageWidth+j]=this.getARGBvalue(alpha,red,green,blue);
			}
		}
		this.Pixels=pixels;
	}
	private double[] getFilterARGBvalues(int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double[] ARGBvalues=new double[4];
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				ARGBvalues[0]+=this.getAlpha(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
				ARGBvalues[1]+=this.getRed(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
				ARGBvalues[2]+=this.getGreen(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
				ARGBvalues[3]+=this.getBlue(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return ARGBvalues;
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