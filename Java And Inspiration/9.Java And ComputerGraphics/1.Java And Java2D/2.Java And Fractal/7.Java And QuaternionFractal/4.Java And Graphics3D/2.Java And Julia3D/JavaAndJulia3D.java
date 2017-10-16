import java.awt.*;
import java.awt.event.*;
public class JavaAndJulia3D
{
	public static void main(String[] args)
	{
		Julia3D julia3D=new Julia3D(1.5,250,250,250,0,0,0);
	}
}
class Julia3D extends Frame implements MouseListener,MouseMotionListener
{

	double p=-1;
	double q=0.2;
	double s=0f;
	double v=0f;
	double t=0f;
	int dx=600,dy=400,dz=0;
	double r,dr,sX,sY,sZ,rotX,rotY,rotZ;
	final double INF=11235813;
	int xScreenSize=1200;
	int yScreenSize=800;
	Point3D[][] screenPixelCoordinates=new Point3D[yScreenSize][xScreenSize];
	DirectionalLight directionalLight;
	Color diffuseColor;
	Color specularColor;
	public Julia3D(double R,double sX,double sY,double sZ,double rotX,double rotY,double rotZ)
	{
		directionalLight=new DirectionalLight(new Color(255,255,255),new Vector(0,0,-1));
		diffuseColor=new Color(255,0,0);
		specularColor=new Color(255,255,255);
		this.setBackground(new Color(0,0,0));
		this.sX=sX;
		this.sY=sY;
		this.sZ=sZ;
		this.rotX=rotX;
		this.rotY=rotY;
		this.rotZ=rotZ;
		this.r=R;
		this.dr=R/400;
		for(int y=0;y<yScreenSize;y++)for(int x=0;x<xScreenSize;x++)screenPixelCoordinates[y][x]=new Point3D(x,y,-INF);
		this.getScreenPixelCoordinates();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
	public void getScreenPixelCoordinates()
	{
		for(double x=-r;x<r;x+=dr)
		{
			for(double y=-r;y<r;y+=dr)
			{
				for(double z=-r;z<r;z+=dr)
				{
					if(Julia(x,y,z,t)==100)
					{

						Point3D p=new Point3D(x,y,z);
						p.scale(sX,sY,sZ);
						p.rotate(rotX,rotY,rotZ);
						p.translate(dx,dy,dz);
						int X=(int)(p.x);
						int Y=(int)(p.y);
						if(X>0&&X<xScreenSize&&Y>0&&Y<yScreenSize&&p.z>screenPixelCoordinates[Y][X].z)screenPixelCoordinates[Y][X]=p;
					}
				}
			}
		}
	}
	public int Julia(double x0,double y0,double z0,double t0)
	{
		double x;
		double y;
		double z;
		double t;
		int i;
		for(i=0;i<100;i++)
		{
		    	x=x0*x0-y0*y0-z0*z0-t0*t0+p;
			y=2*x0*y0+q;
			z=2*x0*z0+s;
			t=2*x0*t0+v;
			if (x*x+y*y+z*z+t*t>4)return i;		         
			x0=x;
			y0=y;
			z0=z;
			t0=t;
		}
		return i;	
	}
	public void paint(Graphics g)
	{
		for(int y=0;y<yScreenSize-1;y++)for(int x=0;x<xScreenSize-1;x++)drawPixel(g,x,y);
	}
	public void drawPixel(Graphics g,int x,int y)
	{
		double x0=screenPixelCoordinates[y][x].x;
		double y0=screenPixelCoordinates[y][x].y;
		double z0=screenPixelCoordinates[y][x].z;
		if(z0==-INF)return;
		int i=2;
		double z1=screenPixelCoordinates[y][x+1].z;
		while(z1==-INF&&(x+i)<xScreenSize){z1=screenPixelCoordinates[y][x+i].z;i++;}
		if((x+i)==xScreenSize)return;
		double x1=screenPixelCoordinates[y][x+i-1].x;
		double y1=screenPixelCoordinates[y][x+i-1].y;
		i=2;
		double z2=screenPixelCoordinates[y+1][x].z;
		while(z2==-INF&&(y+i)<yScreenSize){z2=screenPixelCoordinates[y+i][x].z;i++;}
		if((y+i)==yScreenSize)return;
		double x2=screenPixelCoordinates[y+i-1][x].x;
		double y2=screenPixelCoordinates[y+i-1][x].y;
		Vector normal=new Vector();
		normal.cross(new Vector(x1-x0,y1-y0,z1-z0),new Vector(x2-x0,y2-y0,z2-z0));
		Color color=this.getColor(normal);
		g.setColor(color);
		g.drawLine(x,y,x,y);
	}
	private Color getColor(Vector PixelNormal)
	{
		double redDiffusionRatio=diffuseColor.getRed()/255;
		double greenDiffusionRatio=diffuseColor.getGreen()/255;
		double blueDiffusionRatio=diffuseColor.getBlue()/255;
		double cosLightDirection_PixelNormal=(new Vector()).cos(directionalLight.direction.oppositeVector(),PixelNormal);
		double red=redDiffusionRatio*directionalLight.color.getRed()*cosLightDirection_PixelNormal;
		double green=greenDiffusionRatio*directionalLight.color.getGreen()*cosLightDirection_PixelNormal;
		double blue=blueDiffusionRatio*directionalLight.color.getBlue()*cosLightDirection_PixelNormal;
		if(red<0)red=-red;
		if(green<0)green=-green;
		if(blue<0)blue=-blue;
		return new Color((int)red,(int)green,(int)blue);
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
	private boolean isCloseToZero(double d)
	{
		return (d>0&&d-0.0<1E-8)||(0>d&&0.0-d<1E-8);
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
class Point3D
{
	public double x;
	public double y;
	public double z;
	public Point3D(double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public void translate(double dX,double dY,double dZ)
	{
		x+=dX;
		y+=dY;
		z+=dZ;
	}
	public void rotate(double rotX,double rotY,double rotZ)
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
	public void scale(double sX,double sY,double sZ)
	{
		x*=sX;
		y*=sY;
		z*=sZ;
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