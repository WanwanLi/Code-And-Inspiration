import java.awt.*;
import java.awt.event.*;
public class JavaAndJulia3D
{
	public static void main(String[] args)
	{
		Julia3D Julia3D=new Julia3D(1.5,200,Math.PI/4);
	}
}
class Julia3D extends Frame implements MouseListener,MouseMotionListener
{
	double p=-1;
	double q=0.2;
	double s=0f;
	double v=0f;
	double t=0f;
	double dz=0.1;
	double r,k;
	final double INF=Double.MAX_VALUE;
	int screenWidth=1200;
	int screenHeight=800;
	double[][] screenZBuffer=new double[screenHeight][screenWidth];
	DirectionalLight directionalLight;
	Color diffuseColor;
	Color specularColor;
	public Julia3D(double r,double k,double rotY)
	{
		this.directionalLight=new DirectionalLight(new Color(255,255,255),new Vector(0,0,-1));
		this.diffuseColor=new Color(255,0,0);
		this.specularColor=new Color(255,255,255);
		this.setBackground(new Color(0,0,0));
		this.r=r;
		this.k=k;
		this.dz=1;
		this.getScreenZBuffer(rotY);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
	private void getScreenZBuffer(double rotY)
	{
		for(int i=0;i<screenHeight;i++)for(int j=0;j<screenWidth;j++)this.screenZBuffer[i][j]=-Double.MAX_VALUE;
		double R=r*k;
		int minI=(int)(screenHeight*0.5-R);
		int maxI=(int)(screenHeight*0.5+R);
		int minJ=(int)(screenWidth*0.5-R);
		int maxJ=(int)(screenWidth*0.5+R);
		for(int i=minI;i<maxI;i++)
		{
			for(int j=minJ;j<maxJ;j++)
			{
				double x=((j+0.0)-screenWidth*0.5)/k;
				double y=((i+0.0)-screenHeight*0.5)/k;
				double maxZ=r;
				double minZ=-r;
				double dZ=0.001;
				this.screenZBuffer[i][j]=this.getRoot(x,y,t,rotY,maxZ,minZ,dZ)*k;
			}
		}
	}
	private double getRoot(double x,double y,double t,double rotY,double maxZ,double minZ,double dZ)
	{
		double z=maxZ;
		double dz=z/100;
		while(dz>dZ)
		{
			while(!isRoot(x,y,z,t,rotY))
			{
				z-=dz;
				if(z<minZ)break;
			}
			if(z<minZ)break;
			while(isRoot(x,y,z,t,rotY))
			{
				dz/=2;
				z+=dz;
			}
		}
		if(dz<=dZ)return z;
		else return -Double.MAX_VALUE;
	}
	private boolean isRoot(double x,double y,double z,double t,double rotY)
	{
		double Z=z*Math.cos(rotY)-x*Math.sin(rotY);
		double X=z*Math.sin(rotY)+x*Math.cos(rotY);
		return (Julia(X,y,Z,t)==100);
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
		for(int i=0;i<screenHeight-1;i++)
		{
			for(int j=0;j<screenWidth-1;j++)
			{
				drawPixel(g,i,j);
			}
		}
	}
	private Vector getPixelNormal(int i,int j)
	{
		double z00=screenZBuffer[i+0][j+0];
		double z01=screenZBuffer[i+0][j+1];
		double z10=screenZBuffer[i+1][j+0];
		Vector normal=new Vector();
		Vector v0=new Vector(1,0,z01-z00);
		Vector v1=new Vector(0,1,z10-z00);
		normal.cross(v0,v1);
		return normal;
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
	public void drawPixel(Graphics g,int i,int j)
	{
		if(screenZBuffer[i][j]==-Double.MAX_VALUE)return;
		Vector normal=this.getPixelNormal(i,j);
		if(normal==null)return;
		Color color=this.getColor(normal);
		g.setColor(color);
		g.drawLine(j,i,j,i);
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