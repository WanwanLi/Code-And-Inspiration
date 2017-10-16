import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndTrack
{
	public static void main(String[] args)
	{
		Frame_Track Frame_Track1=new Frame_Track();
		Frame_Track1.setVisible(true);
	}
}
class Frame_Track extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_Track()
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
			Image1=Toolkit.getDefaultToolkit().getImage("JavaAndTrack.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.track(Color.black,Color.white,Color.red,Color.green,200);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.red);
		if(Image1!=null)g.drawImage(Image1,250,50,this);
		if(Image2!=null)g.drawImage(Image2,250,250,this);
	}
}
class ImageOp
{
	public int ImageWidth;
	public int ImageHeight;
	public int[] Pixels;
	public int[][] DirectionTable=new int[][]
	{
		new int[]{0,1},
		new int[]{-1,1},
		new int[]{-1,0},
		new int[]{-1,-1},
		new int[]{0,-1},
		new int[]{1,-1},
		new int[]{1,0},
		new int[]{1,1}
	};
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
	public void track(Color backgroundColor,Color foregroundColor,Color edgeColor,Color regionColor,int thresholdValue)
	{
		int edgeValue=2;
		int regionValue=3;
		int[] trackValues=this.getTrackValues(thresholdValue,edgeValue,regionValue);
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=255;
				int red=backgroundColor.getRed();
				int green=backgroundColor.getGreen();
				int blue=backgroundColor.getBlue();
				if(trackValues[i*ImageWidth+j]==1)
				{
					red=foregroundColor.getRed();
					green=foregroundColor.getGreen();
					blue=foregroundColor.getBlue();
				}
				else if(trackValues[i*ImageWidth+j]==edgeValue)
				{
					red=edgeColor.getRed();
					green=edgeColor.getGreen();
					blue=edgeColor.getBlue();
				}
				else if(trackValues[i*ImageWidth+j]==regionValue)
				{
					red=regionColor.getRed();
					green=regionColor.getGreen();
					blue=regionColor.getBlue();
				}
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private int[] getTrackValues(int thresholdValue,int edgeValue,int regionValue)
	{
		int[] binaryValues=this.getBinaryValues(thresholdValue);
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(isEdge(binaryValues,i,j))
				{
					int[] edgeTable=this.getEdgeTable(binaryValues,i,j);
					int[] regionTable=this.getRegionTable(binaryValues,edgeTable);
					this.drawRegion(binaryValues,regionTable,regionValue);
					this.drawEdge(binaryValues,edgeTable,edgeValue);
				}
			}
		}
		return binaryValues;
	}
	private void drawEdge(int[] binaryValues,int[] edgeTable,int edgeValue)
	{
		int l=edgeTable.length;
		if(l==0)return;
		int i=edgeTable[0];
		int j=edgeTable[1];
		binaryValues[i*ImageWidth+j]=edgeValue;
		for(int k=2;k<l;k++)
		{
			i+=DirectionTable[edgeTable[k]][0];
			j+=DirectionTable[edgeTable[k]][1];
			binaryValues[i*ImageWidth+j]=edgeValue;
		}
	}
	private void drawRegion(int[] binaryValues,int[] regionTable,int regionValue)
	{
		int l=regionTable.length/4;
		int d=0;
		for(int k=0;k<l;k++)
		{
			int i0=regionTable[k*4+0+d];
			int j0=regionTable[k*4+1+d];
			int i1=regionTable[k*4+2+d];
			int j1=regionTable[k*4+3+d];
			if(i0!=i1){d-=2;if(k>0)k--;continue;}
			for(int j=j0;j<=j1;j++)binaryValues[i0*ImageWidth+j]=regionValue;
			for(int j=j0;j<=j1;j++)binaryValues[i1*ImageWidth+j]=regionValue;
		}
	}
	private int[] getEdgeTable(int[] binaryValues,int i,int j)
	{
		int i0=i;
		int j0=j;
		StringQueue edgeTableQueue=new StringQueue();
		edgeTableQueue.enQueue(i0);
		edgeTableQueue.enQueue(j0);
		int d=getNewDirection(binaryValues,0,i0,j0);
		if(d==-1)return new int[0];
		edgeTableQueue.enQueue(d);
		i+=DirectionTable[d][0];
		j+=DirectionTable[d][1];
		while(!((i==i0)&&(j==j0)))
		{
			d=getNewDirection(binaryValues,d,i,j);
			edgeTableQueue.enQueue(d);
			i+=DirectionTable[d][0];
			j+=DirectionTable[d][1];
		}
		return edgeTableQueue.toIntArray();
	}
	private int[] getRegionTable(int[] binaryValues,int[] edgeTable)
	{
		StringQueue queue_I=new StringQueue();
		StringQueue queue_J=new StringQueue();
		int l=edgeTable.length;
		if(l==0)return new int[0];
		int i=edgeTable[0];
		int j=edgeTable[1];
		this.addRegionCoordinate(queue_I,queue_J,binaryValues,i,j);
		for(int k=2;k<l-1;k++)
		{
			i+=DirectionTable[edgeTable[k]][0];
			j+=DirectionTable[edgeTable[k]][1];
			this.addRegionCoordinate(queue_I,queue_J,binaryValues,i,j);
		}
		int[] regionCoordinate_I=queue_I.toIntArray();
		int[] regionCoordinate_J=queue_J.toIntArray();
		l=regionCoordinate_I.length;
		int[] coordinate_I=new int[l];
		int[] coordinate_J=new int[l];
		HashSort HashSort1=new HashSort(regionCoordinate_J);
		regionCoordinate_J=HashSort1.getArray();
		int[] index=HashSort1.getIndex();
		for(i=0;i<l;i++)coordinate_I[i]=regionCoordinate_I[index[i]];
		for(i=0;i<l;i++)regionCoordinate_I[i]=coordinate_I[i];
		HashSort1=new HashSort(regionCoordinate_I);
		regionCoordinate_I=HashSort1.getArray();
		index=HashSort1.getIndex();
		for(i=0;i<l;i++)coordinate_J[i]=regionCoordinate_J[index[i]];
		for(i=0;i<l;i++)regionCoordinate_J[i]=coordinate_J[i];
		int[] regionTable=new int[l*2];
		for(i=0;i<l;i++)
		{
			regionTable[i*2+0]=regionCoordinate_I[i];
			regionTable[i*2+1]=regionCoordinate_J[i];
		}
		return regionTable;
	}
	private int getNewDirection(int[] binaryValues,int currentDirection,int i,int j)
	{
		int oppositeDirection=currentDirection+4;
		for(int d=1;d<=8;d++)
		{
			int D=(oppositeDirection-d+8)%8;
			int I=i+DirectionTable[D][0];
			int J=j+DirectionTable[D][1];
			if(isEdge(binaryValues,I,J))return D;
		}
		return -1;
	}
	private boolean isEdge(int[] binaryValues,int i,int j)
	{
		if(binaryValues[i*ImageWidth+j]!=1)return false;
		for(int d=0;d<8;d++)
		{
			int I=i+DirectionTable[d][0];
			int J=j+DirectionTable[d][1];
			if(isInImageRange(I,J)&&binaryValues[I*ImageWidth+J]>1)return false;
		}
		for(int d=0;d<8;d++)
		{
			int I=i+DirectionTable[d][0];
			int J=j+DirectionTable[d][1];
			if(isInImageRange(I,J)&&binaryValues[I*ImageWidth+J]==0)return true;
		}
		return false;
	}
	private void addRegionCoordinate(StringQueue queue_I,StringQueue queue_J,int[] binaryValues,int i,int j)
	{
		if((j==0)||(j==ImageWidth-1))return;
		boolean isLeftEdge=(binaryValues[i*ImageWidth+(j-1)]==0&&binaryValues[i*ImageWidth+(j+1)]==1);
		boolean isRightEdge=(binaryValues[i*ImageWidth+(j-1)]==1&&binaryValues[i*ImageWidth+(j+1)]==0);
		boolean isDoubleEdge=(binaryValues[i*ImageWidth+(j-1)]==0&&binaryValues[i*ImageWidth+(j+1)]==0);
		if(isLeftEdge||isRightEdge)
		{
			queue_I.enQueue(i);
			queue_J.enQueue(j);
		}
		else if(isDoubleEdge)
		{
			queue_I.enQueue(i);
			queue_J.enQueue(j);
			queue_I.enQueue(i);
			queue_J.enQueue(j);
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
	private boolean isInImageRange(int i,int j)
	{
		return (0<=i&&i<ImageHeight&&0<=j&&j<ImageWidth);
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}
class StringQueue
{
	private String stringQueue;
	private int length;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public void enQueue(int a)
	{
		this.stringQueue+=a+";";
		this.length++;
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
		this.length++;
	}
	public void enQueue(int[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i]+";";
		this.length+=l;
	}
	public String deQueue()
	{
		String string="";
		if(stringQueue.length()==0)return string;
		int n=0;
		char c=stringQueue.charAt(n++);
		while(c!=';')
		{
			string+=c;
			c=stringQueue.charAt(n++);
		}
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		this.length--;
		return string;
	}
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public String[] getStrings()
	{
		int l=this.length();
		String[] strings=new String[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			strings[n++]=s;
			s="";
		}
		return strings;
	}
	public int[] toIntArray()
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
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}
class HashSort
{
	private int[] array;
	private int[] index;
	public HashSort(int[] array)
	{
		int min=Integer.MAX_VALUE;
		int max=-Integer.MAX_VALUE;
		int l=array.length;
		this.array=new int[l];
		for(int i=0;i<l;i++)
		{
			if(array[i]>max)max=array[i];
			if(array[i]<min)min=array[i];
		}
		int length=max-min+1;
		int[] HashTable=new int[length];
		StringQueue[] IndexTable=new StringQueue[length];
		for(int i=0;i<l;i++)
		{
			HashTable[array[i]-min]=array[i];
			if(IndexTable[array[i]-min]==null)IndexTable[array[i]-min]=new StringQueue();
			IndexTable[array[i]-min].enQueue(i);
		}
		int n=0;
		this.array=new int[l];
		this.index=new int[l];
		for(int i=0;i<length;i++)
		{
			if(IndexTable[i]!=null)
			{
				int[] indices=IndexTable[i].toIntArray();
				for(int j=0;j<indices.length;j++)
				{
					this.array[n]=HashTable[i];
					this.index[n++]=indices[j];
				}
			}
		}
	}
	public int[] getArray()
	{
		return this.array;
	}
	public int[] getIndex()
	{
		return this.index;
	}
	public void show(int[] array)
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+i+"]="+array[i]);
	}
	public void display()
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+index[i]+"]="+array[i]);
	}
}