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
	private static int[] getInsertSortArray(int[] array)
	{
		int l=array.length;
		int[] newArray=new int[l];
		for(int i=0;i<l;i++)newArray[i]=Integer.MIN_VALUE;
		for(int i=0;i<l;i++)newArray=insertArray(newArray,array[i]);
		return newArray;
	}
	private static int[] insertArray(int[] array,int integer)
	{
		int length=array.length;
		int[] newArray=new int[length];
		int index=0;
		while(index<length&&integer<=array[index])index++;
		if(index==length)return array;
		for(int i=0;i<index;i++)newArray[i]=array[i];
		newArray[index]=integer;
		for(int i=index+1;i<length;i++)newArray[i]=array[i-1];
		return newArray;
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
			int colorCount=50;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.quantify(colorCount);
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
	public void quantify(int colorCount)
	{
		int colorInterval=10;
		int[][][] RGBcount=this.getRGBcount();
		int[][] popularColors=this.getPopularColors(RGBcount,colorCount,colorInterval);
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				int index=this.getQuantifiedColorIndex(red,green,blue,popularColors);
				red=popularColors[index][0];
				green=popularColors[index][1];
				blue=popularColors[index][2];
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private int distance(int x0,int y0,int z0,int x1,int y1,int z1)
	{
		return ((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)+(z1-z0)*(z1-z0));
	}
	private int getQuantifiedColorIndex(int red,int green,int blue,int[][] popularColors)
	{
		int index=0;
		int l=popularColors.length;
		int minDistance=Integer.MAX_VALUE;
		for(int i=0;i<l;i++)
		{
			int distance=distance(red,green,blue,popularColors[i][0],popularColors[i][1],popularColors[i][2]);
			if(distance<minDistance)
			{
				minDistance=distance;
				index=i;
			}
		}
		return index;
	}
	private int[][][] getRGBcount()
	{
		int[][][] RGBcount=new int[256][256][256];
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				RGBcount[red][green][blue]++;
			}
		}
		return RGBcount;
	}
	private static int[][] getPopularColors(int[][][] RGBcount,int colorCount,int colorInterval)
	{
		int[][] popularColors=new int[colorCount][2];
		for(int i=0;i<256;i+=colorInterval)
		{
			for(int j=0;j<256;j+=colorInterval)
			{
				for(int k=0;k<256;k+=colorInterval)
				{
					if(RGBcount[i][j][k]>0)
					{
						int RGBvalue=(i<<16)|(j<<8)|k;
						popularColors=insertPopularColor(popularColors,RGBvalue,RGBcount[i][j][k]);
					}
				}
			}
		}
		int[][] newPopularColors=new int[colorCount][3];
		for(int i=0;i<colorCount;i++)
		{
			int red=popularColors[i][0]>>16;
			int green=(popularColors[i][0]>>8)&255;
			int blue=popularColors[i][0]&255;
			newPopularColors[i]=new int[]{red,green,blue};
		}
		return newPopularColors;
	}
	private static int[][] insertPopularColor(int[][] popularColors,int RGBvalue,int RGBcount)
	{
		int length=popularColors.length;
		int[][] newPopularColors=new int[length][2];
		int index=0;
		while(index<length&&RGBcount<=popularColors[index][1])index++;
		if(index==length)return popularColors;
		for(int i=0;i<index;i++)newPopularColors[i]=popularColors[i];
		newPopularColors[index]=new int[]{RGBvalue,RGBcount};
		for(int i=index+1;i<length;i++)newPopularColors[i]=popularColors[i-1];
		return newPopularColors;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}