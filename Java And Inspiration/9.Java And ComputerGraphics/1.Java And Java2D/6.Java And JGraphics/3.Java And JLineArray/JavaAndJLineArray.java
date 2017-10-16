import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class JavaAndJLineArray 
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
/*
		double[] coordinates=
		{
			-50,0,20,
			50,0,-20
		};
		int[] colors=
		{
			255,0,0,
			255,255,255
		};
*/
		int lineWidth=80;
		JGraphics JGraphics1=new JGraphics();
		JLineArray JLineArray1=new JLineArray(coordinates,colors,lineWidth);
		JLineArray1.add(JGraphics1);
		JGraphics1.setVisible(true);
	}
}
class JGraphics extends JFrame implements KeyListener,MouseListener,MouseMotionListener
{
	public static final int TYPE_LINE_ARRAY=2;
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
		if(t==TYPE_LINE_ARRAY)
		{
			((JLineArray)o).transform3D(dX,dY,dZ,rotX,rotY,rotZ,scale);
			((JLineArray)o).setPixels();
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

class JLineArray
{
	double[] Coordinates;
	double[] coordinates;
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
	public JLineArray(double[] coordinates,int[] colors,int lineWidth)
	{
		this.Coordinates=coordinates;
		this.length=coordinates.length/3;
		this.coordinates=new double[length*3];
		this.colors=new int[length*3];
		this.lineWidth=lineWidth;
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
		g.getList().add(this,JGraphics.TYPE_LINE_ARRAY);
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
		for(int i=0;i<length-1;i++)
		{
			double z0=coordinates[(i+0)*3+Z];
			double p=distanceZ/(distanceZ-z0);
			int x0=centerX+(int)(coordinates[(i+0)*3+X]*p);
			int y0=centerY-(int)(coordinates[(i+0)*3+Y]*p);
			double z1=coordinates[(i+1)*3+Z];
			p=distanceZ/(distanceZ-z1);
			int x1=centerX+(int)(coordinates[(i+1)*3+X]*p);
			int y1=centerY-(int)(coordinates[(i+1)*3+Y]*p);
			this.addLine(i,i+1,x0,y0,z0,x1,y1,z1);
		}
	}
	private void addLine(int p0,int p1,int x0,int y0,double z0,int x1,int y1,double z1)
	{
		int dx=x1-x0;
		int dy=y1-y0;
		if(abs(dx)>=abs(dy))
		{
			if(x0>x1)
			{
				int t=p0;p0=p1;p1=t;
				t=x0;x0=x1;x1=t;
				t=y0;y0=y1;y1=t;
				double t1=z0;z0=z1;z1=t1;
				dx=-dx;dy=-dy;
			}
			double y=y0;
			double z=z0;
			double k=(dy+0.0)/dx;
			double dz=(z1-z0)/dx;
			for(int j=x0;j<=x1;j++)
			{
				int i=(int)(y+0.5);
				if(isInScreenRange(i,j)&&z>screenZBuffers[i*screenWidth+j])
				{
					int red0=colors[p0*3+R],green0=colors[p0*3+G],blue0=colors[p0*3+B];
					int red1=colors[p1*3+R],green1=colors[p1*3+G],blue1=colors[p1*3+B];
					int[] color=getInterpolatedColor(red0,green0,blue0,red1,green1,blue1,j-x0,dx);
					int red=color[R],green=color[G],blue=color[B];
					this.screenZBuffers[i*screenWidth+j]=z;
					this.screenPixels[i*screenWidth+j]=(255<<24)|(red<<16)|(green<<8)|blue;
				}
				y+=k;
				z+=dz;
			}
		}
		else
		{
			if(y0>y1)
			{
				int t=p0;p0=p1;p1=t;
				t=x0;x0=x1;x1=t;
				t=y0;y0=y1;y1=t;
				double t1=z0;z0=z1;z1=t1;
				dx=-dx;dy=-dy;
			}
			double x=x0;
			double z=z0;
			double k=(dx+0.0)/dy;
			double dz=(z1-z0)/dy;
			for(int i=y0;i<=y1;i++)
			{
				int j=(int)(x+0.5);
				if(isInScreenRange(i,j)&&z>screenZBuffers[i*screenWidth+j])
				{
					int red0=colors[p0*3+R],green0=colors[p0*3+G],blue0=colors[p0*3+B];
					int red1=colors[p1*3+R],green1=colors[p1*3+G],blue1=colors[p1*3+B];
					int[] color=getInterpolatedColor(red0,green0,blue0,red1,green1,blue1,i-y0,dy);
					int red=color[R],green=color[G],blue=color[B];
					this.screenZBuffers[i*screenWidth+j]=z;
					this.screenPixels[i*screenWidth+j]=(255<<24)|(red<<16)|(green<<8)|blue;
				}
				x+=k;
				z+=dz;
			}
		}
	}
	private int abs(int x)
	{
		return (x>=0?x:-x);
	}
	private int[] getInterpolatedColor(int red0,int green0,int blue0,int red1,int green1,int blue1,int j,int dx)
	{
		int[] color=new int[3];
		double dr=(red1-red0+0.0)/dx;
		double dg=(green1-green0+0.0)/dx;
		double db=(blue1-blue0+0.0)/dx;
		color[R]=(int)(red0+j*dr);
		color[G]=(int)(green0+j*dg);
		color[B]=(int)(blue0+j*db);
		return color;
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