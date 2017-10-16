import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndBehaviorRecogniation
{
	public static void main(String[] args)
	{
		Frame_BehaviorRecogniation Frame_BehaviorRecogniation1=new Frame_BehaviorRecogniation();
		Frame_BehaviorRecogniation1.setVisible(true);
	}
}
class Frame_BehaviorRecogniation extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_BehaviorRecogniation()
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
			Image1=Toolkit.getDefaultToolkit().getImage("Images\\action2.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			Image2=Toolkit.getDefaultToolkit().getImage("Images\\background.jpg");
			MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image2,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image2.getWidth(this);
			imageHeight=Image2.getHeight(this);
			pixels=new int[imageWidth*imageHeight];
			PixelGrabber1=new PixelGrabber(Image2,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			int threshold=10,width=1;
			ImageOp1.drawEdge(pixels,Color.white,threshold,width);
		//	ImageOp1.drawShrinkedEdges(pixels,new Color[]{Color.gray,Color.blue,Color.green,Color.cyan,Color.yellow,Color.pink,Color.white,Color.red},threshold);
			ImageOp1.drawSkeleton(pixels,Color.white,width,threshold);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.red);
		if(Image2!=null)g.drawImage(Image2,250,100,this);
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
	private int getDirection(int di,int dj)
	{
		for(int i=0;i<8;i++)
		{
			if(DirectionTable[i][0]==di&&DirectionTable[i][1]==dj)return i;
		}
		return -1;
	}
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
	private boolean[][] isEdgeOrBackground;
	private boolean isSkeleton;
	public void drawEdge(int[] backgroundPixels,Color edgeColor,int threshold,int edgeWidth)
	{
		this.isEdgeOrBackground=new boolean[ImageHeight][ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(isEdge(backgroundPixels,i,j,threshold))
				{
					int[] edgeTable=this.getEdgeTable(backgroundPixels,i,j,threshold);
					this.drawEdge(edgeColor,edgeTable,edgeWidth);
					return;
				}
			}
		}
	}
	public void drawShrinkedEdges(int[] backgroundPixels,Color[] edgeColors,int threshold)
	{
		int edgeWidth=1;
		this.isEdgeOrBackground=new boolean[ImageHeight][ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(isEdge(backgroundPixels,i,j,threshold))
				{
					int[] edgeTable=this.getEdgeTable(backgroundPixels,i,j,threshold);
					this.drawEdge(edgeColors[0],edgeTable,edgeWidth);
					for(int c=1;c<edgeColors.length;c++)
					{
						edgeTable=this.getShrinkedEdgeTable(edgeTable);
						this.drawEdge(edgeColors[c],edgeTable,edgeWidth);
						this.setEdge(edgeTable);
					}
					return;
				}
			}
		}
	}
	public void drawSkeleton(int[] backgroundPixels,Color skeletonColor,int edgeWidth,int threshold)
	{
		this.isEdgeOrBackground=new boolean[ImageHeight][ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(isEdge(backgroundPixels,i,j,threshold))
				{
					int[] edgeTable=this.getEdgeTable(backgroundPixels,i,j,threshold);
					do
					{
						this.isSkeleton=true;
						edgeTable=this.getShrinkedEdgeTable(edgeTable);
						this.setEdge(edgeTable);
					}
					while(!isSkeleton);
					this.drawEdge(skeletonColor,edgeTable,edgeWidth);
					return;
				}
			}
		}
	}
	private int[] getEdgeTable(int[] backgroundPixels,int i,int j,int threshold)
	{
		int i0=i,j0=j;
		StringQueue edgeTableQueue=new StringQueue();
		edgeTableQueue.enQueue(i);
		edgeTableQueue.enQueue(j);
		int d=this.getNewDirection(backgroundPixels,0,i,j,threshold);
		edgeTableQueue.enQueue(d);
		i+=DirectionTable[d][0];
		j+=DirectionTable[d][1];
		while(!((i==i0)&&(j==j0)))
		{
			d=this.getNewDirection(backgroundPixels,d,i,j,threshold);
			edgeTableQueue.enQueue(d);
			i+=DirectionTable[d][0];
			j+=DirectionTable[d][1];
		}
		return edgeTableQueue.toIntArray();
	}
	private int[] getShrinkedEdgeTable(int[] edgeTable)
	{
		int l=edgeTable.length;
		if(l==0)return new int[0];
		StringQueue edgeTableQueue=new StringQueue();
		int i=edgeTable[0],I=10;
		int j=edgeTable[1],J=10;
		int d=edgeTable[2];
		int[] newIJ=this.enQueueShrinkedEdgeTable(edgeTableQueue,i,j,d,I,J);
		I=newIJ[0];J=newIJ[1];
		edgeTableQueue.enQueue(I);
		edgeTableQueue.enQueue(J);
		for(int k=3;k<l;k++)
		{
			i+=DirectionTable[d][0];
			j+=DirectionTable[d][1];
			d=edgeTable[k];
			newIJ=this.enQueueShrinkedEdgeTable(edgeTableQueue,i,j,d,I,J);
			I=newIJ[0];J=newIJ[1];

		}
		return edgeTableQueue.toIntArray();
	}
	private int[] enQueueShrinkedEdgeTable(StringQueue edgeTableQueue,int i,int j,int d,int I,int J)
	{
		int newI=0,newJ=0;
		if(d%2==0)
		{
			int r6=(d+6)%8;
			int I6=i+DirectionTable[r6][0];
			int J6=j+DirectionTable[r6][1];
			if(isEdgeOrBackground[I6][J6]){newI=i;newJ=j;}
			else {newI=I6;newJ=J6;this.isSkeleton=false;}
		}
		else
		{
			int r5=(d+5)%8;
			int I5=i+DirectionTable[r5][0];
			int J5=j+DirectionTable[r5][1];
			int r7=(d+7)%8;
			int I7=i+DirectionTable[r7][0];
			int J7=j+DirectionTable[r7][1];
			if(!isEdgeOrBackground[I7][J7])
			{
				if(!isEdgeOrBackground[I5][J5])
				{
					newI=I5;newJ=J5;
					int D=this.getDirection(newI-I,newJ-J);
					if(D!=-1){edgeTableQueue.enQueue(D);I=newI;J=newJ;}
				}
				newI=I7;newJ=J7;this.isSkeleton=false;
			}
			else{newI=i;newJ=j;}
		}
		int D=this.getDirection(newI-I,newJ-J);
		if(D!=-1)edgeTableQueue.enQueue(D);
		else if(abs(newI-I)==2||abs(newJ-J)==2)
		{
			int d1=this.getDirection(i-I,j-J);
			int d2=this.getDirection(newI-i,newJ-j);
			edgeTableQueue.enQueue(d1);
			edgeTableQueue.enQueue(d2);
		}
		return new int[]{newI,newJ};
	}
	private void setEdge(int[] edgeTable)
	{
	
		int l=edgeTable.length;
		if(l==0)return;
		int i=edgeTable[0];
		int j=edgeTable[1];
		this.isEdgeOrBackground[i][j]=true;
		for(int k=2;k<l;k++)
		{
			i+=DirectionTable[edgeTable[k]][0];
			j+=DirectionTable[edgeTable[k]][1];
			this.isEdgeOrBackground[i][j]=true;
		}
	}
	private void drawEdge(Color edgeColor,int[] edgeTable,int edgeWidth)
	{
	
		int l=edgeTable.length;
		if(l==0)return;
		int i=edgeTable[0];
		int j=edgeTable[1];
		this.drawBlock(edgeColor,i,j,edgeWidth);
		for(int k=2;k<l;k++)
		{
			i+=DirectionTable[edgeTable[k]][0];
			j+=DirectionTable[edgeTable[k]][1];
			this.drawBlock(edgeColor,i,j,edgeWidth);
		}
	}
	private void drawBlock(Color color,int i,int j,int w)
	{
		int r=color.getRed();
		int g=color.getGreen();
		int b=color.getBlue();
		for(int di=0;di<w;di++)
		{
			for(int dj=0;dj<w;dj++)
			{
				int I=i+di-w/2;
				int J=j+dj-w/2;
				if(isInImageRange(I,J))this.Pixels[I*ImageWidth+J]=(255<<24|r<<16|g<<8|b);
			}
		}
	}
	private int getNewDirection(int[] backgroundPixels,int currentDirection,int i,int j,int threshold)
	{
		int oppositeDirection=currentDirection+4;
		for(int d=1;d<=8;d++)
		{
			int D=(oppositeDirection-d+8)%8;
			int I=i+DirectionTable[D][0];
			int J=j+DirectionTable[D][1];
			if(isInImageRange(I,J))
			{
				this.isEdgeOrBackground[I][J]=true;
				if(isEdge(backgroundPixels,I,J,threshold))return D;
			}
		}
		return -1;
	}
	private boolean isEdge(int[] backgroundPixels,int i,int j,int threshold)
	{
		if(isBackground(backgroundPixels,i,j,threshold))return false;
		for(int d=0;d<8;d++)
		{
			int I=i+DirectionTable[d][0];
			int J=j+DirectionTable[d][1];
			if(isInImageRange(I,J)&&isBackground(backgroundPixels,I,J,threshold))return true;
		}
		return false;
	}
	private boolean isBackground(int[] backgroundPixels,int i,int j,int threshold)
	{
		int r0=this.getRed(backgroundPixels[i*ImageWidth+j]);
		int g0=this.getGreen(backgroundPixels[i*ImageWidth+j]);
		int b0=this.getBlue(backgroundPixels[i*ImageWidth+j]);
		int r=this.getRed(Pixels[i*ImageWidth+j]);
		int g=this.getGreen(Pixels[i*ImageWidth+j]);
		int b=this.getBlue(Pixels[i*ImageWidth+j]);
		if(abs(r-r0)+abs(g-g0)+abs(b-b0)<threshold)return true;
		else return false;
	}
	private int abs(int n)
	{
		return n<0?-n:n;
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
	public int integerAt(int index)
	{
		if(index>=length)return 0;
		int l=stringQueue.length();
		int n=0,i=0;
		char c;
		for(i=0;i<l&&n<index;i++)
		{
			c=stringQueue.charAt(i);
			if(c==';')n++;
		}
		String s="";
		c=stringQueue.charAt(i++);
		while(c!=';')
		{
			s+=c;
			c=stringQueue.charAt(i++);
		}
		return Integer.parseInt(s);
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