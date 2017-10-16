import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndEdge
{
	public static void main(String[] args)
	{
		Frame_Edge Frame_Edge1=new Frame_Edge();
		Frame_Edge1.setVisible(true);
	}
}
class Frame_Edge extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_Edge()
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
			Image1=Toolkit.getDefaultToolkit().getImage("..\\JavaAndMorphology.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			int thresholdValue=150;
			ImageOp1.binary(Color.black,Color.white,thresholdValue);
			int[][] Filter=new int[][]
			{
				{1,1,1},
				{1,1,1},
				{1,1,1}
			};
			ImageOp1.edgeFilter(Filter,Color.black,Color.white,150);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,200,200,this);
		if(Image2!=null)g.drawImage(Image2,200,400,this);
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
	private int[] getBinaryValues(int thresholdValue)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] binaryValues=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				int grey=(red+green+blue)/3;
				if(grey<thresholdValue)binaryValues[i*ImageWidth+j]=1;
				else binaryValues[i*ImageWidth+j]=0;
			}
		}
		return binaryValues;
	}
	private void binary(int[] binaryValues,Color foregroundColor,Color backgroundColor,int thresholdValue)
	{
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=255;
				int red=backgroundColor.getRed();
				int green=backgroundColor.getGreen();
				int blue=backgroundColor.getBlue();
				if(binaryValues[i*ImageWidth+j]==1)
				{
					red=foregroundColor.getRed();
					green=foregroundColor.getGreen();
					blue=foregroundColor.getBlue();
				}
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void binary(Color foregroundColor,Color backgroundColor,int thresholdValue)
	{
		int[] binaryValues=this.getBinaryValues(thresholdValue);
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=255;
				int red=backgroundColor.getRed();
				int green=backgroundColor.getGreen();
				int blue=backgroundColor.getBlue();
				if(binaryValues[i*ImageWidth+j]==1)
				{
					red=foregroundColor.getRed();
					green=foregroundColor.getGreen();
					blue=foregroundColor.getBlue();
				}
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void edgeFilter(int[][] Filter,Color foregroundColor,Color backgroundColor,int thresholdValue)
	{
		int[] binaryValues=this.getBinaryValues(thresholdValue);
		int row=Filter.length;
		int column=Filter[0].length;
		int[] binaryFilter=this.getBinaryFilter(Filter);
		int[] newBinaryValues=new int[ImageWidth*ImageHeight];
		int count=binaryFilter.length/2;
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(binaryValues[i*ImageWidth+j]==1)
				{
					newBinaryValues[i*ImageWidth+j]=1;
					for(int v=0;v<count;v++)
					{
						int di=binaryFilter[2*v+0];
						int dj=binaryFilter[2*v+1];
						if(isInImageRange(i+di,j+dj)&&binaryValues[(i+di)*ImageWidth+(j+dj)]==0)
						{
							newBinaryValues[i*ImageWidth+j]=0;
							break;
						}
					}
				}
			}
		}
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)binaryValues[i*ImageWidth+j]-=newBinaryValues[i*ImageWidth+j];
		this.binary(binaryValues,foregroundColor,backgroundColor,thresholdValue);
	}
	public int[] getBinaryFilter(int[][] Filter)
	{
		int row=Filter.length;
		int column=Filter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		int count=0,v=0;
		for(int i=0;i<row;i++)for(int j=0;j<column;j++)if(Filter[i][j]==1)count++;
		int[] binaryFilter=new int[count*2];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				if(Filter[i][j]==1)
				{
					binaryFilter[v++]=i-r;
					binaryFilter[v++]=j-c;
				}
			}
		}
		return binaryFilter;
	}
	private boolean isInImageRange(int i,int j)
	{
		return (i>=0&&i<ImageHeight&&j>=0&&j<ImageWidth);
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}