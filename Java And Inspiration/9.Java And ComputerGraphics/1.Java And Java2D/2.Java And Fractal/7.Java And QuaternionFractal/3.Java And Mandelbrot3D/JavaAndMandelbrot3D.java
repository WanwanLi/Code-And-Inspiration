import java.awt.*;
import java.awt.event.*;
public class JavaAndMandelbrot3D
{
	public static void main(String[] args)
	{
		Mandelbrot3D Mandelbrot3D=new Mandelbrot3D(200,1,Math.PI/6);
	}
}
class Mandelbrot3D extends Frame implements MouseListener,MouseMotionListener
{
	double x,y,z,t,p,q,s,v;
	double pMin=-2.2;
	double pMax=0.7;
	double qMin=-1.2;
	double qMax=1.2;
	double sMin=-1.2;
	double sMax=1.2;
	double r,k;
	final double INF=Double.MAX_VALUE;
	int screenWidth=1200;
	int screenHeight=800;
	double[][] screenZBuffer=new double[screenHeight][screenWidth];
	DirectionalLight directionalLight;
	Color diffuseColor;
	Color specularColor;
	int[] pixels;
	public Mandelbrot3D(double r,double k,double rotY)
	{
		this.directionalLight=new DirectionalLight(new Color(255,255,255),new Vector(0,0,-1));
		this.diffuseColor=new Color(255,0,0);
		this.specularColor=new Color(255,255,255);
		this.setBackground(new Color(0,0,0));
		this.r=r;
		this.k=k;
		this.v=0;
		this.getScreenZBuffer(rotY);
		this.getPixels();
		this.smooth();
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
		double maxZ=R;
		double minZ=-R;
		double dZ=0.1;
		for(int i=minI;i<maxI;i++)
		{
			double q=qMin+(qMax-qMin)*(i-minI)/(maxI-minI);
			for(int j=minJ;j<maxJ;j++)
			{
				double p=pMin+(pMax-pMin)*(j-minJ)/(maxJ-minJ);
				this.screenZBuffer[i][j]=this.getRoot(p,q,v,rotY,maxZ,minZ,dZ);
			}
		}
	}
	private double getRoot(double p,double q,double v,double rotY,double maxZ,double minZ,double dZ)
	{
		double z=maxZ;
		double s=sMin+(sMax-sMin)*(z-minZ)/(maxZ-minZ);
		double dz=z/200;
		while(dz>dZ)
		{
			while(!isRoot(p,q,s,v,rotY))
			{
				z-=dz;
				s=sMin+(sMax-sMin)*(z-minZ)/(maxZ-minZ);
				if(z<minZ)break;
			}
			if(z<minZ)break;
			while(isRoot(p,q,s,v,rotY))
			{
				dz/=2;
				z+=dz;
				s=sMin+(sMax-sMin)*(z-minZ)/(maxZ-minZ);
			}
		}
		if(dz<=dZ)return z;
		else return -Double.MAX_VALUE;
	}
	private boolean isRoot(double p,double q,double s,double v,double rotY)
	{
		double P=p*Math.cos(rotY)-s*Math.sin(rotY);
		double S=p*Math.sin(rotY)+s*Math.cos(rotY);
		return (Mandelbrot(P,q,S,v)==100);
	}
	public int Mandelbrot(double p,double q,double s,double v)
	{
		double x0=0,y0=0,z0=0,t0=0;
		double x,y,z,t;
		int i;
		for(i=0;i<100;i++)
		{
		    	x=x0*x0-y0*y0-z0*z0-t0*t0+p;
			y=2*x0*y0+q;
			z=2*x0*z0+s;
			t=2*x0*t0+v;
			if (x*x+y*y+z*z+t*t>100)return i;		         
			x0=x;
			y0=y;
			z0=z;
			t0=t;
		}
		return i;	
	}
	public void paint(Graphics g)
	{
		if(pixels==null)return;
		for(int i=0;i<screenHeight;i++)
		{
			for(int j=0;j<screenWidth;j++)
			{
				int pixel=pixels[i*screenWidth+j];
				if(pixel==0)continue;
				int red=pixel>>16;
				int green=pixel>>8&255;
				int blue=pixel&255;
				g.setColor(new Color(red,green,blue));
				g.drawLine(j,i,j,i);
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
	private void getPixels()
	{
		this.pixels=new int[screenHeight*screenWidth];
		for(int i=0;i<screenHeight;i++)
		{
			for(int j=0;j<screenWidth;j++)
			{
				if(screenZBuffer[i][j]==-Double.MAX_VALUE)continue;
				Vector normal=this.getPixelNormal(i,j);
				if(normal==null)continue;
				Color color=this.getColor(normal);
				int red=color.getRed();
				int green=color.getGreen();
				int blue=color.getBlue();
				this.pixels[i*screenWidth+j]=(red<<16|green<<8|blue);
			}
		}
	}
	private void smooth()
	{
		int[] pixels33=new int[3*3];
		int[] p=new int[screenWidth*screenHeight];
		for(int i=1;i<screenHeight-1;i++)
		{
			for(int j=1;j<screenWidth-1;j++)
			{
				int c=0;
				for(int m=i-1;m<=i+1;m++)
				{
					for(int n=j-1;n<=j+1;n++)
					{
						if(pixels[m*screenWidth+n]!=0)pixels33[c++]=pixels[m*screenWidth+n];
					}
				}
				p[i*screenWidth+j]=this.getAveragePixel(pixels33,c);
			}
		}
		for(int i=1;i<screenHeight-1;i++)
		{
			for(int j=1;j<screenWidth-1;j++)this.pixels[i*screenWidth+j]=p[i*screenWidth+j];
		}
	}
	private int getAveragePixel(int[] pixelArray,int count)
	{
		if(count==0)return 0;
		int red=0;
		int green=0;
		int blue=0;
		for(int i=0;i<count;i++)
		{
			red+=pixelArray[i]>>16;
			green+=pixelArray[i]>>8&255;
			blue+=pixelArray[i]&255;
		}
		red/=count;
		green/=count;
		blue/=count;
		return (red<<16|green<<8|blue);
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