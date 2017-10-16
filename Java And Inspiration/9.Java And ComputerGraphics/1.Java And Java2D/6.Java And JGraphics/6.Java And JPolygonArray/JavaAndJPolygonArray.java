import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class JavaAndJPolygonArray 
{
	public static void main(String[] args)
	{
		double[] coordinates=
		{
			0,0,0,
			60,-120,30,
			120,-60,30,
			180,-60,15,
			240,60,40,
			240,60,20,
			300,-60,-35,
			360,60,-60,
			420,0,0
		};
		int[] coordinateIndices=
		{
			0,1,2,3,4,
			5,6,7,8
		};
		int[] stripCounts=new int[]{5,4};
		int[] colors=new int[]
		{
			255,0,0,
			0,255,0
		};
		JGraphics JGraphics1=new JGraphics();
		JPolygonArray JPolygonArray1=new JPolygonArray(coordinates,coordinateIndices,stripCounts,colors);
		JPolygonArray1.add(JGraphics1);
		JGraphics1.setVisible(true);
	}
}
class JGraphics extends JFrame implements KeyListener,MouseListener,MouseMotionListener
{
	public static final int TYPE_POLYGON_ARRAY=6;
	private List list;
	private int length;
	private int[] screenPixels;
	private double[] screenZBuffers;
	private int centerX,centerY;
	private double distanceZ;
	private Toolkit toolkit;
	private final int X=0,Y=1,Z=2,R=0,G=1,B=2;
	private double Y0,X0,dY,dX,dZ,rotX,rotY,rotZ,scale=1.0;
	private int screenWidth,screenHeight,screenMinX,screenMaxX,screenMinY,screenMaxY;
	private int eX=0,eY=0,eX0,eY0;
	public JGraphics()
	{
		this.list=new List();
		this.toolkit=Toolkit.getDefaultToolkit();
		this.screenWidth=(int)toolkit.getScreenSize().getWidth();
		this.screenHeight=(int)toolkit.getScreenSize().getHeight();
		this.screenPixels=new int[screenHeight*screenWidth];
		this.screenZBuffers=new double[screenHeight*screenWidth];
		this.centerX=screenWidth/2;
		this.centerY=screenHeight/2;
		this.screenMinX=screenWidth;
		this.screenMaxX=0;
		this.screenMinY=screenHeight;
		this.screenMaxY=0;
		this.distanceZ=screenWidth+screenHeight;
		this.setBounds(0,0,screenWidth,screenHeight);
		this.setBackground(Color.black);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	public void paint(Graphics g)
	{
		this.initScreenZBuffers();
		Node n=this.list.first();
		while(n!=null)
		{
			Object o=n.object;
			int t=n.type;
			this.setPixels(o,t);
			n=this.list.next();
		}
		this.paintPixels(g);
	}
	private void setPixels(Object o,int t)
	{
		if(t==TYPE_POLYGON_ARRAY)
		{
			((JPolygonArray)o).transform3D(dX,dY,dZ,rotX,rotY,rotZ,scale);
			((JPolygonArray)o).setPixels();
		}
	}
	private void initScreenZBuffers()
	{
		for(int i=screenMinY;i<screenMaxY;i++)
		{
			for(int j=screenMinX;j<screenMaxX;j++)
			{
				this.screenZBuffers[i*screenWidth+j]=-Double.MAX_VALUE;
			}
		}
	}
	private void paintPixels(Graphics g)
	{
		for(int i=0;i<screenHeight;i++)
		{
			for(int j=0;j<screenWidth;j++)
			{
				if(screenZBuffers[i*screenWidth+j]==-Double.MAX_VALUE)
				{

					if(screenPixels[i*screenWidth+j]!=0)
					{
						this.screenPixels[i*screenWidth+j]=0;
					}
					else continue;
				}
				if(i<screenMinY)this.screenMinY=i;
				if(i>screenMaxY)this.screenMaxY=i;
				if(j<screenMinX)this.screenMinX=j;
				if(j>screenMaxX)this.screenMaxX=j;
				int red=(screenPixels[i*screenWidth+j]>>16)&255;
				int green=(screenPixels[i*screenWidth+j]>>8)&255;
				int blue=(screenPixels[i*screenWidth+j]>>0)&255;
				g.setColor(new Color(red,green,blue));
				g.drawLine(j,i,j,i);
			}
		}
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e)
	{
		this.eX0=e.getX();
		this.eY0=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		this.eX=e.getX();
		this.eY=e.getY();
		int deX=eX-eX0;
		int deY=eY-eY0;
		this.eX0=eX;
		this.eY0=eY;
		this.dX=0;
		this.dY=0;
		this.dZ=0;
		double w=Math.PI/100;
		this.rotX=(deY==0)?0:deY*w;
		this.rotY=(deX==0)?0:deX*w;
		this.rotZ=0;
		this.scale=1;
		this.repaint();
	}
	public void mouseReleased(MouseEvent e)
	{
		this.X0=0;
		this.Y0=0;
	}
	public void keyReleased(KeyEvent e){}
	public void keyPressed(KeyEvent e)
	{
		this.dX=0;
		this.dY=0;
		this.dZ=0;
		this.rotX=0;
		this.rotY=0;
		this.rotZ=0;
		this.scale=1;
		int d=10;
		String t=KeyEvent.getKeyText(e.getKeyCode());
		if(t.equals("Left"))this.dX=-d;
		else if(t.equals("Right"))this.dX=d;
		else if(t.equals("Up"))this.dY=-d;
		else if(t.equals("Down"))this.dY=d;
		else if(t.equals("Page Up"))this.dZ=2*d;
		else if(t.equals("Page Down"))this.dZ=-2*d;
		else if(t.equals("Backspace"))this.rotZ=Math.PI/20;
		else if(t.equals("Enter"))this.rotZ=-Math.PI/20;
		else if(t.equals("Insert"))this.scale+=0.1;
		else if(t.equals("Delete"))this.scale-=0.1;
		else ;
		this.repaint();
	}
	public void  keyTyped(KeyEvent e){}
	public List getList()
	{
		return this.list;
	}
	public int getCenterX()
	{
		return this.centerX;
	}
	public int getCenterY()
	{
		return this.centerY;
	}
	public double getDistanceZ()
	{
		return this.distanceZ;
	}
	public int getScreenWidth()
	{
		return this.screenWidth;
	}
	public int getScreenHeight()
	{
		return this.screenHeight;
	}
	public int[] getScreenPixels()
	{
		return this.screenPixels;
	}
	public double[] getScreenZBuffers()
	{
		return this.screenZBuffers;
	}
}

class JPolygonArray
{
	double[] Coordinates;
	double[] coordinates;
	int[] coordinateIndices;
	int[] stripCounts;
	int[] colors;
	int lineWidth;
	int length;
	private int[] screenPixels;
	private double[] screenZBuffers;
	private int screenWidth,screenHeight;
	private final int X=0,Y=1,Z=2,R=0,G=1,B=2;
	private int centerX,centerY;
	private double distanceZ;
	private int eX=0,eY=0;
	public JPolygonArray(double[] coordinates,int[] coordinateIndices,int[] stripCounts,int[] colors)
	{
		this.Coordinates=coordinates;
		this.length=coordinates.length/3;
		this.coordinates=new double[length*3];
		this.coordinateIndices=coordinateIndices;
		this.stripCounts=stripCounts;
		this.colors=colors;
		for(int i=0;i<length*3;i++)this.coordinates[i]=coordinates[i];
	}
	public void add(JGraphics g)
	{
		this.centerX=g.getCenterX();
		this.centerY=g.getCenterY();
		this.distanceZ=g.getDistanceZ();
		this.screenWidth=g.getScreenWidth();
		this.screenHeight=g.getScreenHeight();
		this.screenPixels=g.getScreenPixels();
		this.screenZBuffers=g.getScreenZBuffers();
		g.getList().add(this,JGraphics.TYPE_POLYGON_ARRAY);
	}
	public void reset()
	{
		for(int i=0;i<length*3;i++)
		{
			this.coordinates[i]=Coordinates[i];
		}
	}
	public void transform3D(double dX,double dY,double dZ,double rotX,double rotY,double rotZ,double scale)
	{
		double x,y,z;
		for(int i=0;i<length;i++)
		{
			this.coordinates[i*3+X]*=scale;
			this.coordinates[i*3+Y]*=scale;
			this.coordinates[i*3+Z]*=scale;
			y=coordinates[i*3+Y];
			z=coordinates[i*3+Z];
			this.coordinates[i*3+Y]=y*Math.cos(rotX)-z*Math.sin(rotX);
			this.coordinates[i*3+Z]=y*Math.sin(rotX)+z*Math.cos(rotX);
			z=coordinates[i*3+Z];
			x=coordinates[i*3+X];
			this.coordinates[i*3+Z]=z*Math.cos(rotY)-x*Math.sin(rotY);
			this.coordinates[i*3+X]=z*Math.sin(rotY)+x*Math.cos(rotY);
			x=coordinates[i*3+X];
			y=coordinates[i*3+Y];
			this.coordinates[i*3+X]=x*Math.cos(rotZ)-y*Math.sin(rotZ);
			this.coordinates[i*3+Y]=x*Math.sin(rotZ)+y*Math.cos(rotZ);
			this.coordinates[i*3+X]+=dX;
			this.coordinates[i*3+Y]-=dY;
			this.coordinates[i*3+Z]+=dZ;
		}
	}
	public void setPixels()
	{
		if(stripCounts==null)return;
		int startIndex=0,count=0;
		for(int i=0;i<stripCounts.length;i++)
		{
			this.addPolygon(count++,startIndex,stripCounts[i]);
			startIndex+=stripCounts[i];
		}
	}
	private void addPolygon(int count,int startIndex,int stripCount)
	{
		double[] polygonCoordinates=new double[stripCount*3];
		for(int i=0;i<stripCount;i++)
		{
			int index=coordinateIndices[startIndex+i];
			double x=centerX+coordinates[index*3+X];
			double y=centerY-coordinates[index*3+Y];
			double z=coordinates[index*3+Z];
			polygonCoordinates[i*3+X]=x;
			polygonCoordinates[i*3+Y]=y;
			polygonCoordinates[i*3+Z]=z;
		}
		int red=colors[count*3+R],green=colors[count*3+G],blue=colors[count*3+B];
		(new Polygon3D(polygonCoordinates)).fill(new Color(red,green,blue),screenPixels,screenZBuffers,screenWidth);
	}
	private boolean isInScreenRange(int i,int j)
	{
		boolean b1=(0<=i&&i<screenHeight);
		boolean b2=(0<=j&&j<screenWidth);
		return b1&&b2;
	}
}

class Node
{
	public Object object;
	public int type;
	public Node next;
	public Node(Object object,int type)
	{
		this.object=object;
		this.type=type;
		this.next=null;
	}
}
class List
{
	Node first,last,current;
	public List()
	{
		this.first=this.last=null;
		this.current=this.first;
	}
	public void add(Object object,int type)
	{
		Node node=new Node(object,type);
		if(first==null)
		{
			this.first=this.last=node;
			this.current=this.first;
		}
		else 
		{
			this.last.next=node;
			this.last=node;
		}
	}
	public Node first()
	{
		this.current=first;
		return this.next();
	}
	public Node next()
	{
		if(current==null)return null;
		Node n=current;
		this.current=current.next;
		return n;
	}
}


class Polygon3D
{
	private EdgeList[] edgeListTable;
	private EdgeList activeEdgeList;
	private int minY=Integer.MAX_VALUE;
	private int maxY=Integer.MIN_VALUE;
	public Polygon3D(double[] coordinates)
	{
		int length=coordinates.length;
		for(int i=1;i<length;i+=3)
		{
			if(coordinates[i]>maxY)this.maxY=(int)coordinates[i];
			if(coordinates[i]<minY)this.minY=(int)coordinates[i];
		}
		this.activeEdgeList=new EdgeList();
		this.initializeEdgeListTable(coordinates);
	}
	private void initializeEdgeListTable(double[] coordinates)
	{
		this.edgeListTable=new EdgeList[maxY-minY+1];
		int length=coordinates.length,l=length/3,L=l;
		for(int i=0;i<L;i++)
		{
			int i0=(i-1+l)%l;
			int i1=(i+1)%l;
			double x0=coordinates[i0*3+0];
			int y0=(int)coordinates[i0*3+1];
			double z0=coordinates[i0*3+2];
			double x=coordinates[i*3+0];
			int y=(int)coordinates[i*3+1];
			double z=coordinates[i*3+2];
			double x1=coordinates[i1*3+0];
			int y1=(int)coordinates[i1*3+1];
			double z1=coordinates[i1*3+2];
			while(y0==y)
			{
				i=(i-1+l)%l;
				L--;
				i0=(i-1+l)%l;
				i1=(i+1)%l;
				x0=coordinates[i0*3+0];
				y0=(int)coordinates[i0*3+1];
				z0=coordinates[i0*3+2];
				x=coordinates[i*3+0];
				y=(int)coordinates[i*3+1];
				z=coordinates[i*3+2];
				x1=coordinates[i1*3+0];
				y1=(int)coordinates[i1*3+1];
				z1=coordinates[i1*3+2];
			}
			if(y1==y)
			{
				this.addEdgeNode(x0,y0,z0,x,y,z,-1,-1,-1);
				while(y1==y)
				{
					i=(i+1)%l;
					i1=(i+1)%l;
					x=coordinates[i*3+0];
					y=(int)coordinates[i*3+1];
					z=coordinates[i*3+2];
					x1=coordinates[i1*3+0];
					y1=(int)coordinates[i1*3+1];
					z1=coordinates[i1*3+2];
				}
				this.addEdgeNode(-1,-1,-1,x,y,z,x1,y1,z1);
			}
			else this.addEdgeNode(x0,y0,z0,x,y,z,x1,y1,z1);
		}
	}
	private void addEdgeNode(double x0,int y0,double z0,double x,int y,double z,double x1,int y1,double z1)
	{
		double dx0=(x0-x)/(y0-y);
		double dx1=(x1-x)/(y1-y);
		double dz0=(z0-z)/(y0-y);
		double dz1=(z1-z)/(y1-y);
		if(y0>y&&y1>y)
		{
			if(edgeListTable[y-minY]==null)this.edgeListTable[y-minY]=new EdgeList();
			this.edgeListTable[y-minY].insert(new EdgeNode(x,dx0,z,dz0,y0));
			this.edgeListTable[y-minY].insert(new EdgeNode(x,dx1,z,dz1,y1));
		}
		else if(y0<y&&y1<y);
		else
		{
			if(edgeListTable[y-minY]==null)this.edgeListTable[y-minY]=new EdgeList();
			if(y1<y0)this.edgeListTable[y-minY].insert(new EdgeNode(x,dx0,z,dz0,y0));
			else this.edgeListTable[y-minY].insert(new EdgeNode(x,dx1,z,dz1,y1));
		}
	}
	public void display()
	{
		for(int i=0;i<edgeListTable.length;i++)
		{
			if(edgeListTable[i]!=null)
			{
				System.out.print("y="+(i+minY)+" : ");
				edgeListTable[i].display();
			}
		}
	}
	public void fill(Color color,int[] screenPixels,double[] screenZBuffers,int screenWidth)
	{
		this.activeEdgeList=new EdgeList();
		for(int i=0;i<edgeListTable.length;i++)
		{
			if(edgeListTable[i]!=null)
			{
				while(edgeListTable[i].isNotEmpty())activeEdgeList.insert(edgeListTable[i].first());
			}
			if(activeEdgeList.isNotEmpty())
			{
				EdgeList newEdgeList=new EdgeList();
				while(activeEdgeList.isNotEmpty())
				{
					EdgeNode n0=activeEdgeList.first();
					EdgeNode n1=activeEdgeList.first();
					int y=minY+i;
					double x0=n0.x,x1=n1.x;
					double z0=n0.z,z=z0,z1=n1.z,dz=(z1-z0)/(x1-x0);
					for(int x=(int)x0;x<=(int)x1;x++)
					{
						if(screenZBuffers[y*screenWidth+x]<z)
						{
							int red=color.getRed();
							int green=color.getGreen();
							int blue=color.getBlue();
							screenPixels[y*screenWidth+x]=(255<<24)|(red<<16)|(green<<8)|blue;
							screenZBuffers[y*screenWidth+x]=z;
						}
						z+=dz;
					}
					if(y<n0.maxY-1)
					{
						n0.x+=n0.dx;
						n0.z+=n0.dz;
						newEdgeList.insert(n0);
					}
					if(y<n1.maxY-1)
					{
						n1.x+=n1.dx;
						n1.z+=n1.dz;
						newEdgeList.insert(n1);
					}
				}
				this.activeEdgeList=newEdgeList;
			}
		}
	}
	private boolean equals(double d1,double d2)
	{
		if(Math.abs(d1-d2)<5)return true;
		else return false;
	}
}
class EdgeNode
{
	public double x;
	public double dx;
	public double z;
	public double dz;
	public int maxY;
	public EdgeNode next;
	public EdgeNode(double x,double dx,double z,double dz,int maxY)
	{
		this.x=x;
		this.dx=dx;
		this.z=z;
		this.dz=dz;
		this.maxY=maxY;
		this.next=null;
	}
}
class EdgeList
{
	private EdgeNode first,last;
	public EdgeList()
	{
		this.first=null;
		this.last=null;
	}
	public void insert(EdgeNode node)
	{
		if(first==null)
		{
			this.first=node;
			this.last=node;
		}
		else if(node.x>=last.x)
		{
			this.last.next=node;
			this.last=node;
		}
		else if(node.x<=first.x)
		{
			node.next=first;
			this.first=node;
		}
		else
		{
			EdgeNode m,n;
			for(n=first,m=n;node.x>n.x;m=n,n=n.next);
			node.next=n;
			m.next=node;
		}
	}
	public EdgeNode first()
	{
		if(first==null)return null;
		EdgeNode node=first;
		this.first=first.next;
		node.next=null;
		return node;
	}
	public boolean isNotEmpty()
	{
		return (first!=null);
	}
	public void display()
	{
		for(EdgeNode n=first;n!=null;n=n.next)
		{
			System.out.print("x="+n.x+","+"dx="+n.dx+","+"z="+n.z+","+"dz="+n.dz+","+"maxY="+n.maxY+"; ");
		}
		System.out.println();
	}
}


/*

import java.awt.*;
import java.awt.event.*;
public class JavaAndGeometryInfo
{
	public static void main(String[] args)
	{
		double[] coordinates=
		{
			0,0,0,
			60,-120,30,
			120,60,-30,
			180,-60,-15,
			240,60,-40,
			240,60,20,
			300,-60,-35,
			360,60,-60,
			420,0,0
		};
		int[] coordinateIndices=
		{
			0,1,2,3,4,
			5,6,7,8
		};
		int[] stripCounts={5,4};
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		GeometryInfo1.setDirectionalLight(new DirectionalLight(Color.white,new Vector(1,1,-1)));
		GeometryInfo1.setTransform3D(0,0,0);
	}
}
class GeometryInfo extends Frame implements MouseListener,MouseMotionListener
{
	int dx=600,dy=400,dz=0;
	double r,rotY,rotX;
	final int X=0,Y=1,Z=2;
	double[][] pX,pY,pZ;
	int Height=3;
	double[] coordinates;
	int[] coordinateIndices;
	int[] stripCounts;
	DirectionalLight directionalLight;
	Color diffuseColor;
	Color specularColor;
	public final static int TRIANGLE_STRIP_ARRAY=4;
	public GeometryInfo(int mode)
	{
		directionalLight=new DirectionalLight(new Color(255,255,255),new Vector(0,0,-1));
		diffuseColor=new Color(255,0,0);
		specularColor=new Color(255,255,255);
		this.setBackground(new Color(0,0,0));
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
	public void fillTriangle(Graphics g,int x0,int y0,int x1,int y1,int x2,int y2)
	{
		int n=500;		
		for(int i=0;i<n;i++)
		{
			int X01=(int)(x0+i*(x1-x0)/n);
			int Y01=(int)(y0+i*(y1-y0)/n);
			int X02=(int)(x0+i*(x2-x0)/n);
			int Y02=(int)(y0+i*(y2-y0)/n);
			g.drawPolygon(X01,Y01,X02,Y02);
			int X10=(int)(x1+i*(x0-x1)/n);
			int Y10=(int)(y1+i*(y0-y1)/n);
			int X12=(int)(x1+i*(x2-x1)/n);
			int Y12=(int)(y1+i*(y2-y1)/n);
			g.drawPolygon(X10,Y10,X12,Y12);
			int X20=(int)(x2+i*(x0-x2)/n);
			int Y20=(int)(y2+i*(y0-y2)/n);
			int X21=(int)(x2+i*(x1-x2)/n);
			int Y21=(int)(y2+i*(y1-y2)/n);
			g.drawPolygon(X20,Y20,X21,Y21);
		}
	}
	public void setCoordinates(double[] coordinates)
	{
		this.coordinates=new double[coordinates.length];
		for(int i=0;i<coordinates.length;i++)this.coordinates[i]=coordinates[i];
	}
	public void setCoordinateIndices(int[] coordinateIndices)
	{
		this.coordinateIndices=new int[coordinateIndices.length];
		for(int i=0;i<coordinateIndices.length;i++)this.coordinateIndices[i]=coordinateIndices[i];
	}
	public void setStripCounts(int[] stripCounts)
	{
		this.stripCounts=new int[stripCounts.length];
		for(int i=0;i<stripCounts.length;i++)this.stripCounts[i]=stripCounts[i];
	}
	public void setDirectionalLight(DirectionalLight directionalLight)
	{
		if(directionalLight!=null)this.directionalLight=directionalLight;
	}
	public void setDiffuseColor(Color diffuseColor)
	{
		if(diffuseColor!=null)this.diffuseColor=diffuseColor;
	}
	public void setSpecularColor(Color specularColor)
	{
		if(specularColor!=null)this.specularColor=specularColor;
	}
	public void setTransform3D(double rotX,double rotY,double rotZ)
	{
		for(int i=0;i<coordinates.length;i+=3)
		{
			double y=coordinates[i+Y];
			double z=coordinates[i+Z];
			coordinates[i+Y]=y*Math.cos(rotX)-z*Math.sin(rotX);
			coordinates[i+Z]=y*Math.sin(rotX)+z*Math.cos(rotX);
		}
		for(int i=0;i<coordinates.length;i+=3)
		{
			double x=coordinates[i+X];
			double z=coordinates[i+Z];
			coordinates[i+X]=x*Math.cos(rotY)+z*Math.sin(rotY);
			coordinates[i+Z]=-x*Math.sin(rotY)+z*Math.cos(rotY);
		}
		for(int i=0;i<coordinates.length;i+=3)
		{
			double x=coordinates[i+X];
			double y=coordinates[i+Y];
			coordinates[i+X]=x*Math.cos(rotZ)-y*Math.sin(rotZ);
			coordinates[i+Y]=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		}
	}
	private boolean isCloseToZero(double d)
	{
		return (d>0&&d-0.0<1E-8)||(0>d&&0.0-d<1E-8);
	}
	private Color getColor(Vector triangleNormal)
	{
		if(triangleNormal.z<0)return null;
		double redDiffusionRatio=diffuseColor.getRed()/255;
		double greenDiffusionRatio=diffuseColor.getGreen()/255;
		double blueDiffusionRatio=diffuseColor.getBlue()/255;
		double cosLightDirection_triangleNormal=(new Vector()).cos(directionalLight.direction.oppositeVector(),triangleNormal);
		double redDiffusion=redDiffusionRatio*directionalLight.color.getRed()*cosLightDirection_triangleNormal;
		double greenDiffusion=greenDiffusionRatio*directionalLight.color.getGreen()*cosLightDirection_triangleNormal;
		double blueDiffusion=blueDiffusionRatio*directionalLight.color.getBlue()*cosLightDirection_triangleNormal;
		double redSpecularRatio=specularColor.getRed()/255;
		double greenSpecularRatio=specularColor.getGreen()/255;
		double blueSpecularRatio=specularColor.getBlue()/255;
		Vector reflectionLightDirection=directionalLight.direction.reflectedVector(triangleNormal);
		Vector viewDirection=new Vector(0,0,1);
		int reflectionExponent=37;
		double pow_cosReflectionLightDirection_ViewDirection_ReflectionExponent=Math.pow((new Vector()).cos(reflectionLightDirection,viewDirection),reflectionExponent);
		double redSpecular=redSpecularRatio*directionalLight.color.getRed()*pow_cosReflectionLightDirection_ViewDirection_ReflectionExponent;
		double greenSpecular=greenSpecularRatio*directionalLight.color.getGreen()*pow_cosReflectionLightDirection_ViewDirection_ReflectionExponent;
		double blueSpecular=blueSpecularRatio*directionalLight.color.getBlue()*pow_cosReflectionLightDirection_ViewDirection_ReflectionExponent;
		double red=isCloseToZero(redSpecular)?redDiffusion:((redDiffusion+redSpecular)>255?255:(redDiffusion+redSpecular));
		double green=isCloseToZero(greenSpecular)?greenDiffusion:((greenDiffusion+greenSpecular)>255?255:(greenDiffusion+greenSpecular));
		double blue=isCloseToZero(blueSpecular)?blueDiffusion:((blueDiffusion+blueSpecular)>255?255:(blueDiffusion+blueSpecular));
		if(red<0||green<0||blue<0)return null;
		return new Color((int)red,(int)green,(int)blue);
	}
	private void fillTriangleStrip(Graphics g,int startIndex,int stripCount)
	{
		double[] x=new double[3];
		double[] y=new double[3];
		double[] z=new double[3];
		boolean normalIsOpposite=false;
		for(int i=0;i<stripCount-2;i++)
		{
			int index=coordinateIndices[startIndex+(i+0)];
			x[0]=coordinates[index*3+X];
			y[0]=coordinates[index*3+Y];
			z[0]=coordinates[index*3+Z];
			index=coordinateIndices[startIndex+(i+1)];
			x[1]=coordinates[index*3+X];
			y[1]=coordinates[index*3+Y];
			z[1]=coordinates[index*3+Z];
			index=coordinateIndices[startIndex+(i+2)];
			x[2]=coordinates[index*3+X];
			y[2]=coordinates[index*3+Y];
			z[2]=coordinates[index*3+Z];
			Vector normal=new Vector();
			normal.cross(new Vector(x[1]-x[0],y[1]-y[0],z[1]-z[0]),new Vector(x[2]-x[0],y[2]-y[0],z[2]-z[0]));
			if(normalIsOpposite)normal=normal.oppositeVector();
			Color color=this.getColor(normal);
			if(color!=null)
			{
				g.setColor(color);
				this.fillTriangle(g,(int)x[0]+dx,-(int)y[0]+dy,(int)x[1]+dx,-(int)y[1]+dy,(int)x[2]+dx,-(int)y[2]+dy);
			}
			normalIsOpposite=!normalIsOpposite;
		}
	}
	public void paint(Graphics g)
	{
		if(stripCounts==null)return;
		int startIndex=0;
		for(int i=0;i<stripCounts.length;i++)
		{
			fillTriangleStrip(g,startIndex,stripCounts[i]);
			startIndex+=stripCounts[i];
		}
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
}
class Vector
{
	public double x;
	public double y;
	public double z;
	public Vector()
	{
		this.x=0;
		this.y=0;
		this.z=0;
	}
	public Vector(double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	private double Abs(double x)
	{
		return (x<0?-x:x);
	}
	public void cross(Vector v0,Vector v1)
	{
		this.x=v0.y*v1.z-v1.y*v0.z;
		this.y=v0.z*v1.x-v1.z*v0.x;
		this.z=v0.x*v1.y-v1.x*v0.y;
	}
	public Vector oppositeVector()
	{
		return new Vector(-x,-y,-z);
	}
	public Vector reflectedVector(Vector normalVector)
	{
		Vector incidence=new Vector(x,y,z);
		Vector normal=new Vector(normalVector.x,normalVector.y,normalVector.z);
		double rotY=Math.atan(normal.z/normal.x);
		incidence.transform(0,rotY,0);
		normal.transform(0,rotY,0);
		double rotZ=Math.atan(normal.x/normal.y);
		incidence.transform(0,0,rotZ);
		normal.transform(0,0,rotZ);
		incidence.x=-incidence.x;
		incidence.z=-incidence.z;
		incidence.transform(0,0,-rotZ);
		incidence.transform(0,-rotY,0);
		incidence=incidence.oppositeVector();
		return incidence;
	}
	public double cos(Vector v0,Vector v1)
	{
		return (v0.x*v1.x+v0.y*v1.y+v0.z*v1.z)/(Math.sqrt(v0.x*v0.x+v0.y*v0.y+v0.z*v0.z)*Math.sqrt(v1.x*v1.x+v1.y*v1.y+v1.z*v1.z));
	}
	public void transform(double rotX,double rotY,double rotZ)
	{
		double X,Y,Z;
		Y=y*Math.cos(rotX)-z*Math.sin(rotX);
		Z=y*Math.sin(rotX)+z*Math.cos(rotX);
		y=Y;z=Z;
		Z=z*Math.cos(rotY)-x*Math.sin(rotY);
		X=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=Z;x=X;
		X=x*Math.cos(rotZ)-y*Math.sin(rotZ);
		Y=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		x=X;y=Y;
	}
}
class DirectionalLight
{
	public Vector direction;
	public Color color;
	public DirectionalLight(Color color,Vector direction)
	{
		this.color=color;
		this.direction=direction;
	}
}

*/