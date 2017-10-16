import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndQuantify
{
	public static void main(String[] args)
	{
		Frame_Quantify Frame_Quantify1=new Frame_Quantify();
		Frame_Quantify1.setVisible(true);
	}
}
class Frame_Quantify extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_Quantify()
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
			int n=4;int redGrade=n,greenGrade=n,blueGrade=n;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.quantify(redGrade,greenGrade,blueGrade);
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
	private int Int(double x)
	{
		int i=(int)x;
		double x0=x-i;
		double x1=(i+1)-x;
		return (x0<x1?i:(i+1));
	}
	private int[] getMinMaxRGBvalues()
	{
		int minRed=255,maxRed=0,minGreen=255,maxGreen=0,minBlue=255,maxBlue=0;
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				if(red<minRed)minRed=red;
				if(red>maxRed)maxRed=red;
				if(green<minGreen)minGreen=green;
				if(green>maxGreen)maxGreen=green;
				if(blue<minBlue)minBlue=blue;
				if(blue>maxBlue)maxBlue=blue;
			}
		}
		return new int[]{minRed,maxRed,minGreen,maxGreen,minBlue,maxBlue};
	}
	public void quantify(int redGrade,int greenGrade,int blueGrade)
	{
		int[] minMaxRGBvalues=this.getMinMaxRGBvalues();
		int minRed=minMaxRGBvalues[0];
		int maxRed=minMaxRGBvalues[1];
		int minGreen=minMaxRGBvalues[2];
		int maxGreen=minMaxRGBvalues[3];
		int minBlue=minMaxRGBvalues[4];
		int maxBlue=minMaxRGBvalues[5];
		double averageRed=(maxRed+0.0-minRed)/redGrade;
		double averageGreen=(maxGreen+0.0-minGreen)/greenGrade;
		double averageBlue=(maxBlue+0.0-minBlue)/blueGrade;
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				red=this.getQuantifiedColor(red,minRed,averageRed);
				green=this.getQuantifiedColor(green,minGreen,averageGreen);
				blue=this.getQuantifiedColor(blue,minBlue,averageBlue);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private int getQuantifiedColor(int color,int minColor,double averageColor)
	{
		int n=Int((color-minColor)/averageColor);
		return minColor+(int)(n*averageColor);
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}