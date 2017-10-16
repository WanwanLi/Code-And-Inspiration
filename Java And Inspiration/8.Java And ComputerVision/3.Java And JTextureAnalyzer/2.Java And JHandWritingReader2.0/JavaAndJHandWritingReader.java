import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndJHandWritingReader
{
	public static void main(String[] args)
	{
		JHandWritingReader JHandWritingReader1=new JHandWritingReader();
		JHandWritingReader1.setVisible(true);
	}
}
class JHandWritingReader extends JFrame implements MouseListener,MouseMotionListener,ActionListener
{
	private Timer timer;
	private Image image,image1;
	private int imageWidth,imageHeight,imageX,imageY;
	private Graphics g;
	private int counter;
	private boolean[][] characterTemplateSet;
	private int characterWidth=10,characterHeight=20,minASCII=33,maxASCII=127;
	private boolean isTiming;
	private TextArea textArea;
	private int minI,maxI,minJ,maxJ;
	private int ImageWidth,ImageHeight;
	public static int[][] DirectionTable=new int[][]
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
	public JHandWritingReader()
	{
		this.timer=new Timer(1000,this);
		this.timer.start();
		this.counter=0;
		this.imageWidth=500;
		this.imageHeight=500;
		this.imageX=200;
		this.imageY=100;
		this.textArea=new TextArea();
		this.textArea.setBounds(imageX+imageWidth+30,imageY,imageWidth,imageHeight);
		this.setLayout(null);
		this.add(textArea);
		this.textArea.setFont(new Font(null,Font.BOLD,100));
		this.image=new BufferedImage(imageWidth,imageHeight,2);
		this.g=image.getGraphics();
		this.g.setColor(Color.white);
		this.g.fillRect(0,0,imageWidth,imageHeight);
		this.characterTemplateSet=this.getASCIICharacterTemplateSet();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	private boolean[][] getASCIICharacterTemplateSet()
	{
		boolean[][] ASCIICharacterTemplateSet=new boolean[maxASCII-minASCII][];
		for(int i=minASCII;i<maxASCII;i++)
		{
			Image image=new BufferedImage(300,300,2);
			Graphics g=image.getGraphics();
			g.setColor(Color.black);
			g.setFont(new Font(null,Font.BOLD,300));
			char c=(char)i;
			if(c=='-'||c=='.'||c=='\'')g.drawString("",0,239);
			else
			{
				g.drawString(""+c,0,239);
				g.drawString(""+c,1,239);
			}
			ASCIICharacterTemplateSet[i-minASCII]=this.getTemplate(image,characterWidth,characterHeight);
		}
		return ASCIICharacterTemplateSet;
	}
	private boolean[] getTemplate(Image image,int templateWidth,int templateHeight)
	{
		int width=image.getWidth(this),height=image.getHeight(this);
		int[] pixels=new int[width*height];
		PixelGrabber pixelGrabber=new PixelGrabber(image,0,0,width,height,pixels,0,width);
		try{pixelGrabber.grabPixels();}catch(Exception e){e.printStackTrace();}
		int minI=Integer.MAX_VALUE;
		int maxI=Integer.MIN_VALUE;
		int minJ=Integer.MAX_VALUE;
		int maxJ=Integer.MIN_VALUE;
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(pixels[i*width+j]==(255<<24))
				{
					if(i<minI)minI=i;
					if(j<minJ)minJ=j;
					if(i>maxI)maxI=i;
					if(j>maxJ)maxJ=j;
				}
			}
		}
		int h=templateHeight,w=templateWidth;
		double m=(maxI-minI+0.0)/h;
		double n=(maxJ-minJ+0.0)/w;
		boolean[] template=new boolean[h*w];
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				int i1=(int)(minI+i*m+0.5);
				int j1=(int)(minJ+j*n+0.5);
				if(i1>=height||j1>=width)continue;
				if(pixels[i1*width+j1]==(255<<24))template[i*w+j]=true;
				else template[i*w+j]=false;
			}
		}
		return template;
	}
	private String getText(Image image,int templateWidth,int templateHeight)
	{
		String text="";
		int edgeValue=2;
		int regionValue=3;
		int width=image.getWidth(this),height=image.getHeight(this);
		this.ImageWidth=width;
		this.ImageHeight=height;
		int[] binaryValues=new int[width*height];
		int[] pixels=new int[width*height];
		PixelGrabber pixelGrabber=new PixelGrabber(image,0,0,width,height,pixels,0,width);
		try{pixelGrabber.grabPixels();}catch(Exception e){e.printStackTrace();}
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(pixels[i*width+j]==(255<<24))binaryValues[i*width+j]=1;
			}
		}
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(isEdge(binaryValues,i,j))
				{
					this.minI=maxI=i;
					this.minJ=maxJ=j;
					int[] edgeTable=this.getEdgeTable(binaryValues,i,j);
					int[] regionTable=this.getRegionTable(binaryValues,edgeTable);
					this.drawRegion(binaryValues,regionTable,regionValue);
					this.drawEdge(binaryValues,edgeTable,edgeValue);
					text+=this.getChar(pixels,templateWidth,templateHeight);
				}
			}
		}
		return text;
	}
	private char getChar(int[] pixels,int templateWidth,int templateHeight)
	{
		int h=templateHeight,w=templateWidth;
		double m=(maxI-minI+0.0)/h;
		double n=(maxJ-minJ+0.0)/w;
		boolean[] template=new boolean[h*w];
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				int i1=(int)(minI+i*m+0.5);
				int j1=(int)(minJ+j*n+0.5);
				if(i1>=imageHeight||j1>=imageWidth)continue;
				if(pixels[i1*imageWidth+j1]==(255<<24))template[i*w+j]=true;
				else template[i*w+j]=false;
			}
		}
		int index=this.getIndex(template,characterTemplateSet);
		return (char)(index+minASCII);
	}
	private int getIndex(boolean[] templateSample,boolean[][] templateSet)
	{
		int index=0;
		int minDistance=Integer.MAX_VALUE;
		int l=templateSet.length;
		for(int i=0;i<l;i++)
		{
			int distance=this.getDistance(templateSample,templateSet[i]);
			if(distance<minDistance)
			{
				minDistance=distance;
				index=i;
			}
		}
		return index;
	}
	private int getDistance(boolean[] template1,boolean[] template2)
	{
		int distance=0;
		int l=template1.length;
		for(int i=0;i<l;i++)
		{
			if(template1[i]!=template2[i])distance++;
			else distance--;
		}
		return distance;
	}
	public void paint(Graphics g)
	{
		g.drawRect(imageX,imageY,imageWidth,imageHeight);
		g.drawImage(image,imageX,imageY,this);
	}
	public void actionPerformed(ActionEvent e)	
	{
		if(!isTiming)return;
		this.counter++;
		if(counter>1)
		{
			this.textArea.setText(textArea.getText()+this.getText(image,characterWidth,characterHeight)+"\n");
			g.setColor(Color.white);
			g.fillRect(0,0,imageWidth,imageHeight);
			this.repaint();
			this.isTiming=false;
			this.counter=0;
		}
	}
	public void mouseClicked(MouseEvent e)
	{
		this.image.flush();
		this.repaint();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e)
	{
		this.counter=0;
		isTiming=false;
	}
	public void mouseDragged(MouseEvent e)
	{
		int Y=e.getY();
		int X=e.getX();
		g.setColor(Color.black);
		g.fillRect(X-imageX,Y-imageY,20,20);
		this.repaint();
	}
	public void mouseReleased(MouseEvent e)
	{
		isTiming=true;
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
		this.updateMinMax(i,j);
		while(!((i==i0)&&(j==j0)))
		{
			d=getNewDirection(binaryValues,d,i,j);
			edgeTableQueue.enQueue(d);
			i+=DirectionTable[d][0];
			j+=DirectionTable[d][1];
			this.updateMinMax(i,j);
		}
		return edgeTableQueue.toIntArray();
	}
	private void updateMinMax(int i,int j)
	{
		if(i<minI)this.minI=i;
		if(j<minJ)this.minJ=j;
		if(i>maxI)this.maxI=i;
		if(j>maxJ)this.maxJ=j;
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
			if(isInImageRange(I,J)&&binaryValues[I*ImageWidth+J]==0)return true;
		}
		return false;
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
	private boolean isInImageRange(int i,int j)
	{
		return (0<=i&&i<ImageHeight&&0<=j&&j<ImageWidth);
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