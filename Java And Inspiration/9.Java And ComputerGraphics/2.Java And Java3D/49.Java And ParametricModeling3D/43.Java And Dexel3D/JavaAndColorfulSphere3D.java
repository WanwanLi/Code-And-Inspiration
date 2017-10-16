import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndColorfulSphere3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),100);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		TransformGroup1.addChild(new ColorfulSphere3D(0.5f));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Dexel3D extends Shape3D
{
	private int row,column,level;
	private DexelTable DexelTable_XOZ;
	private int[][][] dexels;
	private int minRow,minColumn,maxRow,maxColumn,minLevel,maxLevel;
	private double x0,y0,z0,dx,dy,dz;
	public Dexel3D(int[][][] dexels,double length,double width,double height,Appearance appearance)
	{
		this.level=dexels.length;
		this.row=dexels[0].length;
		this.column=dexels[0][0].length;
		this.dexels=dexels;
		this.initDexelTable();
		this.initCoordinateSystem(length,width,height);
		Point3d[] coordinates=this.toCoordinates_XOZ();
		QuadArray QuadArray1=new QuadArray(coordinates.length,QuadArray.COORDINATES);
		QuadArray1.setCoordinates(0,coordinates);
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		new NormalGenerator().generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	private void initDexelTable()
	{
		this.DexelTable_XOZ=new DexelTable(row,column);
		for(int k=0;k<level;k++)
		{
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<column;j++)
				{
					if(dexels[k][i][j]!=0)
					{
						this.updateMinMax(i,j,k);
						this.DexelTable_XOZ.add(i,j,k);
					}
				}
			}
		}
	}
	Point3d[] toCoordinates_XOZ()
	{
		int[] coordinates=this.DexelTable_XOZ.toCoordinates();
		int l=coordinates.length/3;
		Point3d[] coordinates_XOZ=new Point3d[l];
		for(int i=0;i<l;i++)
		{
			double z=z0+coordinates[i*3+0]*dz;
			double x=x0+coordinates[i*3+1]*dx;
			double y=y0+coordinates[i*3+2]*dy;
			coordinates_XOZ[i]=new Point3d(x,y,z);
		}
		return coordinates_XOZ;
	}
	private void updateMinMax(int row,int column,int level)
	{
		if(row<minRow)this.minRow=row;
		else if(row>maxRow)this.maxRow=row;
		if(column<minColumn)this.minColumn=column;
		else if(column>maxColumn)this.maxColumn=column;
		if(level<minLevel)this.minLevel=level;
		else if(level>maxLevel)this.maxLevel=level;
	}
	private void initCoordinateSystem(double length,double width,double height)
	{
		this.dx=length/(maxColumn-minColumn);
		this.dy=height/(maxLevel-minLevel);
		this.dz=width/(maxRow-minRow);
		this.x0=-length/2;
		this.y0=-height/2;
		this.z0=-width/2;
	}
}
class DexelTable
{
	private LinkedList[][] levelTable;
	private int[][] levels;
	private int row,column;
	public DexelTable(int row,int column)
	{
		this.row=0;
		this.column=0;
		this.levels=new int[row][column];
		this.levelTable=new LinkedList[row][column];
	}
	public void add(int row,int column,int level)
	{
		if(levelTable[row][column]==null)
		{
			this.levelTable[row][column]=new LinkedList();
			this.levels[row][column]=level;
			this.levelTable[row][column].add(levels[row][column]);
			return;
		}
		if(level==levels[row][column]+1)this.levels[row][column]++;
		else
		{
			this.levelTable[row][column].add(levels[row][column]);
			this.levels[row][column]=level;
			this.levelTable[row][column].add(levels[row][column]);
		}
	}
	public int[] toCoordinates()
	{
		LinkedList level00,level01,level11,level10,coordinates=new LinkedList();
		for(int i=0;i<row-1;i++)
		{
			for(int j=0;j<column-1;j++)
			{
				boolean clockwise=true;
				int k00=-1,k01=-1,k11=-1,k10=-1;
				level00=levelTable[i+0][j+0];
				level01=levelTable[i+0][j+1];
				level11=levelTable[i+1][j+1];
				level10=levelTable[i+1][j+0];
				if(level00!=null)k00=level00.next();
				if(level01!=null)k01=level01.first();
				if(level11!=null)k11=level11.first();
				if(level10!=null)k10=level10.first();
				this.addClockwise(i,j,k00,k01,k11,k10,coordinates);
			}
		}
		return coordinates.toIntArray();
	}
	private void addClockwise(int i,int j,int k00,int k01,int k11,int k10,LinkedList coordinates)
	{
		int n=0;
		if(k00==-1)n++;if(k01==-1)n++;
		if(k11==-1)n++;if(k10==-1)n++;
		if(n>1)return;
		boolean b=false;
		int di=0,dj=0,dk=k00;
		if(k00!=-1){coordinates.add(i+0,j+0,k00);b=true;}
		if(k01!=-1)
		{
			coordinates.add(i+0,j+1,k01);
			if(!b){di=0;dj=1;dk=k01;}
		}
		if(k11!=-1)coordinates.add(i+1,j+1,k11);
		if(k10!=-1)coordinates.add(i+1,j+0,k10);
		if(n==1)coordinates.add(i+di,j+dj,dk);
	}
}
class ColorfulSphere3D extends Shape3D
{
	public ColorfulSphere3D(float r)
	{
		int m=40,n=80,k=0,c=0;
		Point3d[] coordinates=new Point3d[m*n];
		int[] coordinateIndices=new int[(m-1)*(n-1)*4];
		Color3f[] colors=new Color3f[m*n];
		for(int i=0;i<m;i++)
		{
			double a=Math.PI*i/(m-1);
			for(int j=0;j<n;j++)
			{
				double w=2.0*Math.PI*j/(n-1);
				double x=r*Math.cos(a);
				double y=r*Math.sin(a)*Math.sin(w);
				double z=r*Math.sin(a)*Math.cos(w);
				coordinates[k++]=new Point3d(x,y,z);
				float[] RGB=function(i+0.0,j+0.0);
				colors[c++]=new Color3f(RGB[0],RGB[1],RGB[2]);
			}
		}
		k=0;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				coordinateIndices[k++]=(i+0)*n+(j+0);
				coordinateIndices[k++]=(i+0)*n+(j+1);
				coordinateIndices[k++]=(i+1)*n+(j+1);
				coordinateIndices[k++]=(i+1)*n+(j+0);
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES|IndexedQuadArray.COLOR_3,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedQuadArray1.setColors(0,colors);
		IndexedQuadArray1.setColorIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		new NormalGenerator().generateNormals(GeometryInfo1);
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(new Material());
		this.setAppearance(Appearance1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	double minR=Double.MAX_VALUE;
	double minG=Double.MAX_VALUE;
	double minB=Double.MAX_VALUE;
	double maxR=Double.MIN_VALUE;
	double maxG=Double.MIN_VALUE;
	double maxB=Double.MIN_VALUE;
	private float[] function(double x,double y)
	{
		double r=x+y;
		double g=x*y;
		double b=x-y;
		if(r<minR)minR=r;
		if(g<minG)minG=g;
		if(b<minB)minB=b;
		if(r>maxR)maxR=r;
		if(g>maxG)maxG=g;
		if(b>maxB)maxB=b;
		float R=(float)((r-minR)/(maxR-minR));
		float G=(float)((g-minG)/(maxG-minG));
		float B=(float)((b-minB)/(maxB-minB));
		return new float[]{R,G,B};
	}
}
class Node
{
	public int data;
	public Node next,pre;
	public Node(int integer)
	{
		this.data=integer;
	}
}
class LinkedList
{
	private int length;
	private Node first,last;
	public LinkedList()
	{
		this.length=0;
		this.first=null;
		this.last=null;
	}
	public void add(int integer)
	{
		Node node=new Node(integer);
		if(first==null)this.first=this.last=node;
		else
		{
			this.last.next=node;
			node.pre=last;
			this.last=node;
		}
		this.length++;
	}
	public void add(int i,int j,int k)
	{
		this.add(i);
		this.add(j);
		this.add(k);
	}
	public int first()
	{
		if(first==null)return -1;
		return first.data;
	}
	public int last()
	{
		if(last==null)return -1;
		return last.data;
	}
	public int next()
	{
		if(first==null)return -1;
		int data=first.data;
		this.first=first.next;
		this.length--;
		return data;
	}
	public int pre()
	{
		if(last==null)return -1;
		int data=last.data;
		this.last=last.pre;
		this.length--;
		return data;
	}
	public int[] toIntArray()
	{
		int[] array=new int[length];
		for(int i=0;i<length;i++)
		{
			array[i]=first.data;
			this.first=first.next;
		}
		return array;
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
	public StringQueue(String queue)
	{
		this.stringQueue=queue;
		this.length=this.getLength();
		if(length==0)
		{
			this.stringQueue+=";";
			this.length=1;
		}
	}
	public void enQueue(char character)
	{
		this.stringQueue+=character;
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
	public void insert(String string,int index)
	{
		this.length++;
		if(index>=length)
		{
			this.stringQueue+=string+";";
			return;
		}
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(i,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public void set(int index,String string)
	{
		if(index>=length)return;
		int i=0,j=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			if(c==';')break;
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+";"+string+s1;
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
	private int getLength()
	{
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public String getStringQueue()
	{
		String queue="";
		queue+=stringQueue;
		return queue;
	}
	public void enStringQueue(StringQueue queue)
	{
		this.stringQueue+=queue.getStringQueue();
		this.length+=queue.length();
	}
	public StringQueue deStringQueue(int begin,int end)
	{
		int i=0,j=0,l=0,Length=stringQueue.length();
		String queue="";
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==begin)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			queue+=c;
			if(c==';')
			{
				l++;
				if(l>end)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+s1;
		this.length-=(end-begin+1);
		return new StringQueue(queue);
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
	public char[] toCharArray()
	{
		char[] array=new char[length];
		for(int i=0;i<length;i++)array[i]=stringQueue.charAt(i);
		return array;
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
