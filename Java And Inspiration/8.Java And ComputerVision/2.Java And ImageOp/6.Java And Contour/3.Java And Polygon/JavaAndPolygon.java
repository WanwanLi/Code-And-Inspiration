import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndPolygon
{
	public static void main(String[] args)
	{
		Frame_Polygon Frame_Polygon1=new Frame_Polygon();
		Frame_Polygon1.setVisible(true);
	}
}
class Frame_Polygon extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_Polygon()
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
			Image1=Toolkit.getDefaultToolkit().getImage("Images\\shape3.jpg");
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
			int threshold=100,width=1;
			int[] contour=ImageOp1.getContour(pixels,threshold);
			int[] polygon=ImageOp1.toPolygon(contour);
			ImageOp1.setColor(Color.white);
			ImageOp1.drawPolygon(polygon);
			ImageOp1.drawPoints(polygon,5);
			//ImageOp1.printPolygon(polygon,0.005,-0.88,-1.25);
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
	public int[] getContour(int[] backgroundPixels,int threshold)
	{
		this.isEdgeOrBackground=new boolean[ImageHeight][ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(isEdge(backgroundPixels,i,j,threshold))
				{
					int d=this.getNewDirection(backgroundPixels,0,i,j,threshold);
					if(d!=-1)return this.getContour(backgroundPixels,i,j,threshold);
				}
			}
		}
		return new int[0];
	}
	private int[] getContour(int[] backgroundPixels,int i,int j,int threshold)
	{
		int i0=i,j0=j;
		StringQueue contourQueue=new StringQueue();
		contourQueue.enQueue(i);
		contourQueue.enQueue(j);
		int d=this.getNewDirection(backgroundPixels,0,i,j,threshold);
		i+=DirectionTable[d][0];
		j+=DirectionTable[d][1];
		contourQueue.enQueue(i);
		contourQueue.enQueue(j);
		while(!((i==i0)&&(j==j0)))
		{
			d=this.getNewDirection(backgroundPixels,d,i,j,threshold);
			i+=DirectionTable[d][0];
			j+=DirectionTable[d][1];
			contourQueue.enQueue(i);
			contourQueue.enQueue(j);
		}
		return contourQueue.toIntArray();
	}
	public int[] toPolygon(int[] contour)
	{
		int q=0,length=contour.length/2;
		int i=contour[2*q+0],j=contour[2*q+1];
		StringQueue polygonQueue=new StringQueue();
		polygonQueue.enQueue(i);
		polygonQueue.enQueue(j);
		for(int p=1;p<length;p++)
		{
			int distance=this.averageDistanceToLine(contour,q,p);
			if(distance>0)
			{
				q=p-1;
				i=contour[2*q+0];
				j=contour[2*q+1];
				polygonQueue.enQueue(i);
				polygonQueue.enQueue(j);
			}
		}
		return polygonQueue.toIntArray();
	}
	private int averageDistanceToLine(int[] contour,int q,int p)
	{
		int distance=0,length=0,k=q;
		int x0=contour[2*q+1],y0=contour[2*q+0];
		int x1=contour[2*p+1],y1=contour[2*p+0];
		int dx=x1-x0,dy=y1-y0;
		boolean dxIsPositive=true;
		boolean dyIsPositive=true;
		if(dx<0){dxIsPositive=false;dx=-dx;}
		if(dy<0){dyIsPositive=false;dy=-dy;}
		if(dx>dy)
		{
			int y=y0,e=2*dy-dx;
			if(dxIsPositive)
			{
				for(int x=x0;x<=x1;x++)
				{
					int j=contour[2*(k++)+1];
					while(j!=x)j=contour[2*(k++)+1];
					int i=contour[2*(k-1)+0];
					distance+=Math.abs(i-y);
					if(e>0)
					{
						if(dyIsPositive)y++;else y--;
						e+=2*dy-2*dx;
					}
					else e+=2*dy;
				}
			}
			else
			{
				for(int x=x0;x>=x1;x--)
				{
					int j=contour[2*(k++)+1];
					while(j!=x)j=contour[2*(k++)+1];
					int i=contour[2*(k-1)+0];
					distance+=Math.abs(i-y);
					if(e>0)
					{
						if(dyIsPositive)y++;else y--;
						e+=2*dy-2*dx;
					}
					else e+=2*dy;
				}
			}
		}
		else
		{
			int x=x0,e=2*dx-dy;
			if(dyIsPositive)
			{
				for(int y=y0;y<=y1;y++)
				{
					int i=contour[2*(k++)+0];
					while(i!=y)i=contour[2*(k++)+0];
					int j=contour[2*(k-1)+1];
					distance+=Math.abs(j-x);
					if(e>0)
					{
						if(dxIsPositive)x++;else x--;
						e+=2*dx-2*dy;
					}
					else e+=2*dx;
				}
			}
			else
			{
				for(int y=y0;y>=y1;y--)
				{
					int i=contour[2*(k++)+0];
					while(i!=y)i=contour[2*(k++)+0];
					int j=contour[2*(k-1)+1];
					distance+=Math.abs(j-x);
					if(e>0)
					{
						if(dxIsPositive)x++;else x--;
						e+=2*dx-2*dy;
					}
					else e+=2*dx;
				}
			}
		}
		length=dx>dy?dx:dy;
		if(length==0)return 0;
		return (distance+length/2)/length;
	}
	private int Red=0,Green=0,Blue=0;
	public void setColor(Color color)
	{
		this.Red=color.getRed();
		this.Green=color.getGreen();
		this.Blue=color.getBlue();
	}
	public void drawLine(int x0,int y0,int x1,int y1)
	{
		int dx=x1-x0,dy=y1-y0;
		boolean dxIsPositive=true;
		boolean dyIsPositive=true;
		if(dx<0){dxIsPositive=false;dx=-dx;}
		if(dy<0){dyIsPositive=false;dy=-dy;}
		if(dx>dy)
		{
			int y=y0,e=2*dy-dx;
			if(dxIsPositive)
			{
				for(int x=x0;x<=x1;x++)
				{
					this.setPixel(x,y);
					if(e>0)
					{
						if(dyIsPositive)y++;else y--;
						e+=2*dy-2*dx;
					}
					else e+=2*dy;
				}
			}
			else
			{
				for(int x=x0;x>=x1;x--)
				{
					this.setPixel(x,y);
					if(e>0)
					{
						if(dyIsPositive)y++;else y--;
						e+=2*dy-2*dx;
					}
					else e+=2*dy;
				}
			}
		}
		else
		{
			int x=x0,e=2*dx-dy;
			if(dyIsPositive)
			{
				for(int y=y0;y<=y1;y++)
				{
					this.setPixel(x,y);
					if(e>0)
					{
						if(dxIsPositive)x++;else x--;
						e+=2*dx-2*dy;
					}
					else e+=2*dx;
				}
			}
			else
			{
				for(int y=y0;y>=y1;y--)
				{
					this.setPixel(x,y);
					if(e>0)
					{
						if(dxIsPositive)x++;else x--;
						e+=2*dx-2*dy;
					}
					else e+=2*dx;
				}
			}
		}
	}
	public void drawPolygon(int[] points)
	{
		int length=points.length/2;
		for(int i=0;i<length-1;i++)
		{
			int y0=points[i*2+0];
			int x0=points[i*2+1];
			int y1=points[(i+1)*2+0];
			int x1=points[(i+1)*2+1];
			this.drawLine(x0,y0,x1,y1);
		}
		int y0=points[(length-1)*2+0];
		int x0=points[(length-1)*2+1];
		int y1=points[0];
		int x1=points[1];
		this.drawLine(x0,y0,x1,y1);
	}
	public void printPolygon(int[] points,double scale,double dx,double dy)
	{
		int length=points.length/2;
		String s="\t\t\t\t\tnew Point3d";
		for(int i=0;i<length;i++)
		{
			int y=points[i*2+0];
			int x=points[i*2+1];
			System.out.println(s+"("+(scale*x+dx)+","+(scale*y+dy)+","+0+"),");
		}
	}
	public void drawPoints(int[] points,int width)
	{
		int length=points.length/2;
		for(int i=0;i<length;i++)
		{
			int y=points[i*2+0];
			int x=points[i*2+1];
			this.drawBlock(x,y,width);
		}
	}
	public void setPixel(int x,int y)
	{
		if(x<0||x>ImageWidth)return;
		if(y<0||y>ImageHeight)return;
		this.Pixels[y*ImageWidth+x]=(255<<24|Red<<16|Green<<8|Blue);
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
	private void drawBlock(int j,int i,int w)
	{
		for(int di=0;di<w;di++)
		{
			for(int dj=0;dj<w;dj++)
			{
				int y=i+di-w/2;
				int x=j+dj-w/2;
				this.setPixel(x,y);
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