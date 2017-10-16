import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndCompare
{
	public static void main(String[] args)
	{
		Frame_Compare Frame_Compare1=new Frame_Compare();
		Frame_Compare1.setVisible(true);
	}
}
class Frame_Compare extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_Compare()
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
			Image2=Toolkit.getDefaultToolkit().getImage("JavaAndCompare.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image2,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image2.getWidth(this);
			imageHeight=Image2.getHeight(this);
			pixels=new int[imageWidth*imageHeight];
			PixelGrabber1=new PixelGrabber(Image2,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			int[] compareResults=ImageOp1.compare(pixels,imageWidth,imageHeight);
			i=compareResults[0];
			j=compareResults[1];
			System.out.println("MinMatchingDistance("+i+","+j+")="+compareResults[2]);
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.red);
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image1!=null)g.drawRect(j+100,i+100,imageWidth,imageHeight);
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
	public int[] compare(int[] pixels,int imageWidth,int imageHeight)
	{
		int[] compareResults=new int[3];
		int[] GreyValues=this.getGreyValues(Pixels,ImageWidth,ImageHeight);
		int[] greyValues=this.getGreyValues(pixels,imageWidth,imageHeight);
		int minMatchingDistance=11235813;
		if(ImageWidth>imageWidth&&ImageHeight>imageHeight)
		{
			for(int i=0;i<=ImageHeight-imageHeight;i++)
			{
				for(int j=0;j<=ImageWidth-imageWidth;j++)
				{
					int matchingDistance=this.getMatchingDistance(GreyValues,i,j,greyValues,imageWidth,imageHeight);
					if(matchingDistance<minMatchingDistance)
					{
						compareResults[0]=i;
						compareResults[1]=j;
						minMatchingDistance=matchingDistance;
					}
				}
			}
		}
		compareResults[2]=minMatchingDistance;
		return compareResults;
	}
	private int getMatchingDistance(int[] GreyValues,int i0,int j0,int[] greyValues,int imageWidth,int imageHeight)
	{
		int matchingDistance=0;
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				int greyDifference=GreyValues[(i+i0)*ImageWidth+(j+j0)]-greyValues[i*imageWidth+j];
				matchingDistance+=greyDifference*greyDifference;
			}
		}
		return matchingDistance;
	}
	private int[] getGreyValues(int[] pixels,int imageWidth,int imageHeight)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] greyValues=new int[imageWidth*imageHeight];
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				int red=ColorModel1.getRed(pixels[i*imageWidth+j]);
				int green=ColorModel1.getGreen(pixels[i*imageWidth+j]);
				int blue=ColorModel1.getBlue(pixels[i*imageWidth+j]);
				int grey=(red+green+blue)/3;
				greyValues[i*imageWidth+j]=grey;
			}
		}
		return greyValues;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}