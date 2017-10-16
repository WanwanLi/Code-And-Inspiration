import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndMap
{
	public static void main(String[] args)
	{
		Frame_Map Frame_Map1=new Frame_Map();
		Frame_Map1.setVisible(true);
	}
}
class Frame_Map extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_Map()
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
			int edgeValue=11;
			String[] levelColorImages=new String[12];
			for(int i=0;i<12;i++)levelColorImages[i]="LevelColors\\"+i+".JPG";
			Color[] levelColors=this.getLevelColors(levelColorImages);
			Image1=Toolkit.getDefaultToolkit().getImage("Maps\\NewYork.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
		//	ImageOp1.segmentationMap(levelColors,edgeValue);
		//	ImageOp1.region(levelColors,edgeValue);
			ImageOp1.segmentationAndRegion(levelColors,edgeValue);
			Image1=this.createImage(ImageOp1.getMemoryImageSource());
		//	ImageOp1.displayRegions();
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	private Color[] getLevelColors(String[] levelColorImages) throws Exception
	{
		int length=levelColorImages.length;
		Color[] levelColors=new Color[length];
		for(int i=0;i<length;i++)
		{
			Image image=Toolkit.getDefaultToolkit().getImage(levelColorImages[i]);
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(image,0);
			MediaTracker1.waitForID(0);
			int imageWidth=image.getWidth(this);
			int imageHeight=image.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber pixelGrabber=new PixelGrabber(image,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			pixelGrabber.grabPixels();
			int red=ImageOp.getRed(pixels[imageHeight*imageWidth/2]);
			int green=ImageOp.getGreen(pixels[imageHeight*imageWidth/2]);
			int blue=ImageOp.getBlue(pixels[imageHeight*imageWidth/2]);
			levelColors[i]=new Color(red,green,blue);
		}
		return levelColors;
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,50,100,this);
		if(Image2!=null)g.drawImage(Image2,750,100,this);
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
	private int[] getMapVelues(Color[] levelColors)
	{
		int[] mapValues=new int[ImageHeight*ImageWidth];
		int level=levelColors.length;
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int value=0;
				int minDistance=Integer.MAX_VALUE;
				for(int k=0;k<level;k++)
				{
					int distance=this.getColorDistance(levelColors[k],Pixels[i*ImageWidth+j]);
					if(distance<minDistance){minDistance=distance;value=k;}
				}
				mapValues[i*ImageWidth+j]=value;
			}
		}
		return mapValues;
	}		
	public void segmentationMap(Color[] levelColors,int edgeValue)
	{
		int[] mapValues0=this.getMapVelues(levelColors);
		int[][] filter=new int[][]
		{	
			{0,1,0},
			{1,1,1},
			{0,1,0}
		};
		this.expandFilter(filter);
		int[] mapValues1=this.getMapVelues(levelColors);
		int[] densityValues=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=255,red,green,blue;
				int value0=mapValues0[i*ImageWidth+j];
				int value=mapValues1[i*ImageWidth+j];
				if(value0!=value&&value0<edgeValue)value=value0;
				red=levelColors[value].getRed();
				green=levelColors[value].getGreen();
				blue=levelColors[value].getBlue();
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private boolean isInImageRange(int i,int j)
	{
		return (0<=i&&i<ImageHeight&&0<=j&&j<ImageWidth);
	}
	private void floodFill(int i,int j,int label,int[] regionValues)
	{
		StringQueue q=new StringQueue();
		q.enQueue(i,j);
		while(q.isNotEmpty())
		{
			int[] ij=q.deQueue();
			int  I=ij[0],J=ij[1],I0=I-1,I1=I+1,J0=J-1,J1=J+1;
			if(isInImageRange(I,J0)&&regionValues[I*ImageWidth+J0]==1){q.enQueue(I,J0);regionValues[I*ImageWidth+J0]=label;}
			if(isInImageRange(I,J1)&&regionValues[I*ImageWidth+J1]==1){q.enQueue(I,J1);regionValues[I*ImageWidth+J1]=label;}
			if(isInImageRange(I0,J)&&regionValues[I0*ImageWidth+J]==1){q.enQueue(I0,J);regionValues[I0*ImageWidth+J]=label;}
			if(isInImageRange(I1,J)&&regionValues[I1*ImageWidth+J]==1){q.enQueue(I1,J);regionValues[I1*ImageWidth+J]=label;}
			if(isInImageRange(I0,J0)&&regionValues[I0*ImageWidth+J0]==1){q.enQueue(I0,J0);regionValues[I0*ImageWidth+J0]=label;}
			if(isInImageRange(I0,J1)&&regionValues[I0*ImageWidth+J1]==1){q.enQueue(I0,J1);regionValues[I0*ImageWidth+J1]=label;}
			if(isInImageRange(I1,J0)&&regionValues[I1*ImageWidth+J0]==1){q.enQueue(I1,J0);regionValues[I1*ImageWidth+J0]=label;}
			if(isInImageRange(I1,J1)&&regionValues[I1*ImageWidth+J1]==1){q.enQueue(I1,J1);regionValues[I1*ImageWidth+J1]=label;}
		}
	}
	private int labelTheRegions(int[] regionValues)
	{
		int label=2;
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(regionValues[i*ImageWidth+j]==1)
				{
					regionValues[i*ImageWidth+j]=label;
					this.floodFill(i,j,label,regionValues);
					label++;
				}
			}
		}
		return label;
	}
	public void region(Color[] levelColors,int edgeValue)
	{
		int[][] filter=new int[][]
		{	
			{5,5,5},
			{5,5,5},
			{5,5,5}
		};
		this.erodeFilter(filter);
		int[] mapValues=this.getMapVelues(levelColors);
		int[] regionValues=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(mapValues[i*ImageWidth+j]<edgeValue)regionValues[i*ImageWidth+j]=1;
			}
		}
		int label=this.labelTheRegions(regionValues);
		int[] pixels=new int[label];
		for(int i=0;i<label;i++)
		{
			int alpha=255;
			int red=(int)(255*Math.random());
			int green=(int)(255*Math.random());
			int blue=(int)(255*Math.random());
			pixels[i]=(alpha<<24)|(red<<16)|(green<<8)|blue;
		}
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Pixels[i*ImageWidth+j]=pixels[regionValues[i*ImageWidth+j]];
			}
		}
	}
	public void segmentationAndRegion(Color[] levelColors,int edgeValue)
	{
		int[] mapValues0=this.getMapVelues(levelColors);
		int[][] filter=new int[][]
		{	
			{0,1,0},
			{1,1,1},
			{0,1,0}
		};
		int[] density=new int[]
		{
			5,12,38,75,175,375,750,1750,4000,6000,1
		};
		int[] duplicates=duplicate();
		this.expandFilter(filter);
		int[] mapValues1=this.getMapVelues(levelColors);
		int[] densityValues=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=255,red,green,blue;
				int value0=mapValues0[i*ImageWidth+j];
				int value=mapValues1[i*ImageWidth+j];
				if(value0!=value&&value0<edgeValue)value=value0;
				red=levelColors[value].getRed();
				green=levelColors[value].getGreen();
				blue=levelColors[value].getBlue();
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
				if(value<edgeValue)densityValues[i*ImageWidth+j]=density[value];
			}
		}
		this.write(densityValues,"densityValues.txt");
		Pixels=duplicates;
		filter=new int[][]
		{	
			{5,5,5},
			{5,12,5},
			{5,5,5}
		};
		this.erodeFilter(filter);
		int[] mapValues=this.getMapVelues(levelColors);
		int[] regionValues=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(mapValues[i*ImageWidth+j]<edgeValue)regionValues[i*ImageWidth+j]=1;
			}
		}
		int label=this.labelTheRegions(regionValues);
		int regionValue=0;
		for(int i=0;i<ImageHeight;i++)
		{
			regionValue=0;
			for(int j=0;j<ImageWidth;j++)
			{
				if(regionValue!=regionValues[i*ImageWidth+j]&&regionValues[i*ImageWidth+j]!=0)regionValue=regionValues[i*ImageWidth+j];
				if(regionValues[i*ImageWidth+j]==0&&densityValues[i*ImageWidth+j]!=0)regionValues[i*ImageWidth+j]=regionValue;
			}
		}
		this.write(regionValues,"regionValues.txt");
		int[] pixels=new int[label];
		for(int i=0;i<label;i++)
		{
			int alpha=255;
			int red=(int)(255*Math.random());
			int green=(int)(255*Math.random());
			int blue=(int)(255*Math.random());
			if(i==0){red=0;green=0;blue=0;}
			pixels[i]=(alpha<<24)|(red<<16)|(green<<8)|blue;
		}
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Pixels[i*ImageWidth+j]=pixels[regionValues[i*ImageWidth+j]];
			}
		}
	}
	public void displayRegions()
	{
		int label=11;
		int[] pixels=new int[label];
		for(int i=0;i<label;i++)
		{
			int alpha=255;
			int red=(int)(255*Math.random());
			int green=(int)(255*Math.random());
			int blue=(int)(255*Math.random());
			pixels[i]=(alpha<<24)|(red<<16)|(green<<8)|blue;
		}
		int[] regionValues=this.read("regionValues.txt");
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Pixels[i*ImageWidth+j]=pixels[regionValues[i*ImageWidth+j]];
			}
		}
	}
	private int[] read(String fileName)
	{
		int[] pixels=new int[ImageHeight*ImageWidth];
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName));
			for(int i=0;i<ImageHeight;i++)
			{
				int n=0;
				String s=BufferedReader1.readLine();
				for(int j=0;j<ImageWidth;j++)
				{
					String pixel="";
					char c=s.charAt(n++);
					while(c!='\t')
					{
						pixel+=c;
						c=s.charAt(n++);
					}
					pixels[i*ImageWidth+j]=Integer.parseInt(pixel);
				}
				s="";
			}
			BufferedReader1.close();
		}
		catch(Exception e){}
		return pixels;
	}
	private void write(int[] pixels,String fileName)
	{
		try
		{
			String s="";
			PrintWriter PrintWriter1=new PrintWriter(new File(fileName));
			for(int i=0;i<ImageHeight;i++)
			{
				for(int j=0;j<ImageWidth;j++)
				{
					s+=pixels[i*ImageWidth+j]+"\t";
				}
				PrintWriter1.println(s);
				s="";
			}
			PrintWriter1.close();
		}
		catch(Exception e){}
	}
	public static Color getAverageColor(int[] pixels,int imageWidth,int imageHeight)
	{
		int red=0;
		int green=0;
		int blue=0;
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				red+=getRed(pixels[i*imageWidth+j]);
				green+=getGreen(pixels[i*imageWidth+j]);
				blue+=getBlue(pixels[i*imageWidth+j]);
			}
		}
		red/=(imageWidth*imageHeight);
		green/=(imageWidth*imageHeight);
		blue/=(imageWidth*imageHeight);
		return new Color(red,green,blue);
	}
	private int getColorDistance(Color color,int pixel)
	{
		int dRed=color.getRed()-this.getRed(pixel);
		int dGreen=color.getGreen()-this.getGreen(pixel);
		int dBlue=color.getBlue()-this.getBlue(pixel);
		return (dRed*dRed+dGreen*dGreen+dBlue*dBlue);
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
				if(red<erodeFilterARGBvalue[1])erodeFilterARGBvalue[1]=red;
				if(green<erodeFilterARGBvalue[2])erodeFilterARGBvalue[2]=green;
				if(blue<erodeFilterARGBvalue[3])erodeFilterARGBvalue[3]=blue;
			}
		}
		return erodeFilterARGBvalue;
	}
	public static int getAlpha(int pixel)
	{
		return (pixel>>24)&0xff;
	}
	public static int getRed(int pixel)
	{
		return (pixel>>16)&0xff;
	}
	public static int getGreen(int pixel)
	{
		return (pixel>>8)&0xff;
	}
	public static int getBlue(int pixel)
	{
		return pixel&0xff;
	}
	private int bound(int x)
	{
		if(x<0)x=0;
		if(x>255)x=255;
		return x;
	}
	private int[] duplicate()
	{
		int[] pixels=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight*ImageWidth;i++)pixels[i]=Pixels[i];
		return pixels;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		int[] Pixels=this.duplicate();
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}
class StringQueue
{
	private String stringQueue;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public StringQueue(String string)
	{
		this.stringQueue=string;
	}
	public void enQueue(int i,int j)
	{
		this.stringQueue+=i+","+j+";";
	}
	public int[] toIntArray(int length)
	{
		String s="";
		int n=0;
		int[] array=new int[length];
		for(int i=0;i<length;i++)
		{
			char c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			array[i]=Integer.parseInt(s);
			s="";
		}
		return array;
	}
	public int[] deQueue()
	{
		int n=0;
		String i="",j="";
		char c=stringQueue.charAt(n++);
		while(c!=',')
		{
			i+=c;
			c=stringQueue.charAt(n++);
		}
		c=stringQueue.charAt(n++);
		while(c!=';')
		{
			j+=c;
			c=stringQueue.charAt(n++);
		}
		int[] ij=new int[2];
		ij[0]=Integer.parseInt(i);
		ij[1]=Integer.parseInt(j);
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		return ij;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}