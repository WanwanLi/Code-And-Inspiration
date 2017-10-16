import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class JavaAndJPointArray 
{
	public static void main(String[] args)
	{

		double[] coordinates=
		{
			-50.5,-50.5,-50.0,
			-50.5,-50.5,50.0,
			-50.7,50.0,-50.0,
			-50.7,50.0,50.0,
			50.5,-50.5,-50.0,
			50.5,-50.5,50.0,
			50.0,50.8,-50.0,
			50.0,50.8,50.0
		};
		int[] colors=
		{
			0,0,0,
			0,0,255,
			0,255,0,
			0,255,255,
			255,0,0,
			255,0,255,
			255,255,0,
			255,255,255
		};
		int pointSize=20;
		JGraphics JGraphics1=new JGraphics();
		JPointArray JPointArray1=new JPointArray(coordinates,colors,pointSize);
		JPointArray1.add(JGraphics1);
		JGraphics1.setVisible(true);
	}
}
class JGraphics extends JFrame implements KeyListener,MouseListener,MouseMotionListener
{
	public static final int TYPE_POINT_ARRAY=1;
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
		if(t==TYPE_POINT_ARRAY)
		{
			((JPointArray)o).transform3D(dX,dY,dZ,rotX,rotY,rotZ,scale);
			((JPointArray)o).setPixels();
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

class JPointArray
{
	double[] Coordinates;
	double[] coordinates;
	int[] colors;
	int pointSize;
	int length;
	private int[] screenPixels;
	private double[] screenZBuffers;
	private int screenWidth,screenHeight;
	private final int X=0,Y=1,Z=2,R=0,G=1,B=2;
	private int centerX,centerY;
	private double distanceZ;
	private int eX=0,eY=0;
	public JPointArray(double[] coordinates,int[] colors,int pointSize)
	{
		this.Coordinates=coordinates;
		this.length=coordinates.length/3;
		this.coordinates=new double[length*3];
		this.colors=new int[length*3];
		this.pointSize=pointSize;
		for(int i=0;i<length*3;i++)
		{
			this.coordinates[i]=coordinates[i];
			this.colors[i]=colors[i];
		}
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
		g.getList().add(this,JGraphics.TYPE_POINT_ARRAY);
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
		for(int i=0;i<length;i++)
		{
			double z=coordinates[i*3+Z];
			double p=distanceZ/(distanceZ-z);
			int x=centerX+(int)(coordinates[i*3+X]*p);
			int y=centerY-(int)(coordinates[i*3+Y]*p);
			this.addPoint(i,x,y,z);
		}
	}
	private void addPoint(int p,int x,int y,double z)
	{
		for(int i=y-pointSize;i<=y+pointSize;i++)
		{
			for(int j=x-pointSize;j<=x+pointSize;j++)
			{
				int dy=i-y,dx=j-x;
				if(dx*dx+dy*dy>pointSize*pointSize)continue;
				if(isInScreenRange(i,j)&&z>screenZBuffers[i*screenWidth+j])
				{
					int red=colors[p*3+R];
					int green=colors[p*3+G];
					int blue=colors[p*3+B];
					this.screenZBuffers[i*screenWidth+j]=z;
					this.screenPixels[i*screenWidth+j]=(255<<24)|(red<<16)|(green<<8)|blue;
				}
			}
		}
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