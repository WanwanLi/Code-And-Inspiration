 import java.awt.*;
import java.awt.event.*;
public class JavaAndBranchGroup
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
		GeometryInfo GeometryInfo2=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo2.setCoordinates(coordinates);
		GeometryInfo2.setCoordinateIndices(coordinateIndices);
		GeometryInfo2.setStripCounts(stripCounts);
		GeometryInfo2.setTranslation(new Vector(-200,-100,200));
		GeometryInfo2.setDiffuseColor(new Color(0,255,0));
		BranchGroup BranchGroup1=new BranchGroup();
		BranchGroup1.addChild(GeometryInfo1);
		BranchGroup1.addChild(GeometryInfo2);
		BranchGroup1.setVisible(true);
	}
}
class TriangleInfo
{
	public double[] x=new double[3];
	public double[] y=new double[3];
	public double[] z=new double[3];
	public Vector normal;
	public Material material;
	public TriangleInfo next;
	public TriangleInfo(double[] x,double[] y,double[] z,Vector normal,Material material)
	{
		this.x[0]=x[0];
		this.x[1]=x[1];
		this.x[2]=x[2];
		this.y[0]=y[0];
		this.y[1]=y[1];
		this.y[2]=y[2];
		this.z[0]=z[0];
		this.z[1]=z[1];
		this.z[2]=z[2];
		this.normal=new Vector(normal.x,normal.y,normal.z);
		this.material=new Material(material.diffuseColor,material.specularColor);
		this.next=null;
	}
	private boolean isCloseToZero(double d)
	{
		return (d>0&&d-0.0<1E-8)||(0>d&&0.0-d<1E-8);
	}
	private int Abs(double x)
	{
		return (int)(x<0?-x:x);
	}
	public void drawLine(Graphics g,double x0,double y0,double z0,double x1,double y1,double z1,double[][] screenZBuffer)
	{
		int dX=600;
		int dY=400;
		int n=Abs(x1-x0);
		int m=Abs(y1-y0);
		int X=0,Y=0,I=0;
		double Z=0,dx=0,dy=0,dz=0;
		if(n>=m)I=n;
		else I=m;
		if(I==0)
		{
			X=(int)(x0);
			Y=(int)(y0);
			X=X+dX;
			Y=-Y+dY;
			if(X<0||Y<0)return;
			Z=(z1>z0?z1:z0);
			if(Z>=screenZBuffer[X][Y])
			{
				g.drawLine(X,Y,X,Y);
				screenZBuffer[X][Y]=Z;
			}
			return;
		}
		dx=(x1-x0)/I;
		dy=(y1-y0)/I;
		dz=(z1-z0)/I;
		for(int i=0;i<=I;i++)
		{
			X=(int)(x0+i*dx);
			Y=(int)(y0+i*dy);
			X=X+dX;
			Y=-Y+dY;
			if(X<0||Y<0)break;
			Z=z0+i*dz;
			if(Z>=screenZBuffer[X][Y])
			{
				g.drawLine(X,Y,X,Y);
				screenZBuffer[X][Y]=Z;
			}
		}
	}	
	private double Max(double[] x)
	{
		double max=20;
		for(int i=0;i<x.length;i++)if(Math.abs(x[i])>max)max=Math.abs(x[i]);
		return max;
	}
	public void paint(Graphics g,double[][] screenZBuffer,Vector viewDirection,DirectionalLight directionalLight)
	{
		Color color=this.getColor(viewDirection,directionalLight);
		if(color==null)return;
		g.setColor(color);
		double[] d={x[1]-x[0],y[1]-y[0],x[2]-x[0],y[2]-y[0],x[2]-x[1],y[2]-y[1]};	
		int n=(int)Max(d);	
		for(int i=0;i<n;i++)
		{
			double X01=x[0]+i*(x[1]-x[0])/n;
			double Y01=y[0]+i*(y[1]-y[0])/n;
			double Z01=z[0]+i*(z[1]-z[0])/n;
			double X02=x[0]+i*(x[2]-x[0])/n;
			double Y02=y[0]+i*(y[2]-y[0])/n;
			double Z02=z[0]+i*(z[2]-z[0])/n;
			drawLine(g,X01,Y01,Z01,X02,Y02,Z02,screenZBuffer);
			double X10=x[1]+i*(x[0]-x[1])/n;
			double Y10=y[1]+i*(y[0]-y[1])/n;
			double Z10=z[1]+i*(z[0]-z[1])/n;
			double X12=x[1]+i*(x[2]-x[1])/n;
			double Y12=y[1]+i*(y[2]-y[1])/n;
			double Z12=z[1]+i*(z[2]-z[1])/n;
			drawLine(g,X10,Y10,Z10,X12,Y12,Z12,screenZBuffer);
			double X20=x[2]+i*(x[0]-x[2])/n;
			double Y20=y[2]+i*(y[0]-y[2])/n;
			double Z20=z[2]+i*(z[0]-z[2])/n;
			double X21=x[2]+i*(x[1]-x[2])/n;
			double Y21=y[2]+i*(y[1]-y[2])/n;
			double Z21=z[2]+i*(z[1]-z[2])/n;
			drawLine(g,X20,Y20,Z20,X21,Y21,Z21,screenZBuffer);
		}
	}
	public Color getColor(Vector viewDirection,DirectionalLight directionalLight)
	{
		if((new Vector()).cos(viewDirection,normal)<=0)return null;
		double redDiffusionRatio=material.diffuseColor.getRed()/255;
		double greenDiffusionRatio=material.diffuseColor.getGreen()/255;
		double blueDiffusionRatio=material.diffuseColor.getBlue()/255;
		double cosLightDirection_normal=(new Vector()).cos(directionalLight.direction.oppositeVector(),normal);
		if(cosLightDirection_normal<0)return null;
		double redDiffusion=redDiffusionRatio*directionalLight.color.getRed()*cosLightDirection_normal;
		double greenDiffusion=greenDiffusionRatio*directionalLight.color.getGreen()*cosLightDirection_normal;
		double blueDiffusion=blueDiffusionRatio*directionalLight.color.getBlue()*cosLightDirection_normal;
		double redSpecularRatio=material.specularColor.getRed()/255;
		double greenSpecularRatio=material.specularColor.getGreen()/255;
		double blueSpecularRatio=material.specularColor.getBlue()/255;
		Vector reflectionLightDirection=directionalLight.direction.reflectedVector(normal);
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
}
class TriangleArrayInfo
{
	TriangleInfo FirstTriangle;
	TriangleInfo LastTriangle;
	public void insertTriangle(double[] x,double[] y,double[] z,Vector normal,Material material)
	{
		TriangleInfo newTriangleInfo=new TriangleInfo(x,y,z,normal,material);
		if(FirstTriangle==null)FirstTriangle=LastTriangle=newTriangleInfo;
		else
		{
			LastTriangle.next=newTriangleInfo;
			LastTriangle=newTriangleInfo;
		}
	}
	public void removeAllTriangles()
	{
		FirstTriangle=null;
	}
	public void showAllTriangles(Graphics g,double[][] screenZBuffer,Vector viewDirection,DirectionalLight directionalLight)
	{
		for(TriangleInfo triangle=FirstTriangle;triangle!=null;triangle=triangle.next)triangle.paint(g,screenZBuffer,viewDirection,directionalLight);
	}
}
class GeometryInfo
{
	double r,rotY,rotX;
	final int X=0,Y=1,Z=2;
	double[][] pX,pY,pZ;
	int Height=10;
	double[] coordinates;
	int[] coordinateIndices;
	int[] stripCounts;
	Material material;
	Color specularColor;
	Color diffuseColor;
	public final static int TRIANGLE_STRIP_ARRAY=4;
	public GeometryInfo(int mode)
	{
		this.material=new Material(new Color(255,0,0),new Color(255,255,255));
	}
	public void setMaterial(Material material)
	{
		if(material!=null)this.material=material;
	}
	public void setDiffuseColor(Color diffuseColor)
	{
		if(diffuseColor!=null)this.material.diffuseColor=diffuseColor;
	}
	public void setSpecularColor(Color specularColor)
	{
		if(specularColor!=null)this.material.specularColor=specularColor;
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
	public void setTranslation(Vector vector)
	{
		for(int i=0;i<coordinates.length;i+=3)
		{
			coordinates[i+X]+=vector.x;
			coordinates[i+Y]+=vector.y;
			coordinates[i+Z]+=vector.z;
		}
	}
	public void rotate(double rotX,double rotY,double rotZ)
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
			double z=coordinates[i+Z];
			double x=coordinates[i+X];
			coordinates[i+Z]=z*Math.cos(rotY)-x*Math.sin(rotY);
			coordinates[i+X]=z*Math.sin(rotY)+x*Math.cos(rotY);
		}
		for(int i=0;i<coordinates.length;i+=3)
		{
			double x=coordinates[i+X];
			double y=coordinates[i+Y];
			coordinates[i+X]=x*Math.cos(rotZ)-y*Math.sin(rotZ);
			coordinates[i+Y]=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		}
	}
	private void getTriangleStrip(TriangleArrayInfo triangleArrayInfo,int startIndex,int stripCount)
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
			normal.cross(new Vector(x[1]-x[0],y[1]-y[0],z[1]-z[0]),new Vector(x[2]-x[1],y[2]-y[1],z[2]-z[1]));
			if(normalIsOpposite)normal=normal.oppositeVector();
			triangleArrayInfo.insertTriangle(x,y,z,normal,material);
			normalIsOpposite=!normalIsOpposite;
		}
	}
	public void getTriangleStripArray(TriangleArrayInfo triangleArrayInfo)
	{
		if(stripCounts==null)return;
		int startIndex=0;
		for(int i=0;i<stripCounts.length;i++)
		{
			this.getTriangleStrip(triangleArrayInfo,startIndex,stripCounts[i]);
			startIndex+=stripCounts[i];
		}
	}
}
class BranchGroup extends Frame implements MouseListener,MouseMotionListener
{
	DirectionalLight directionalLight;
	Vector viewDirection;
	TriangleArrayInfo triangleArrayInfo;
	final double INF=11235813.0;
	int xScreenSize=1200;
	int yScreenSize=800;
	double[][] screenZBuffer=new double[xScreenSize][yScreenSize];
	private void initialize()
	{
		for(int x=0;x<xScreenSize;x++)for(int y=0;y<yScreenSize;y++)this.screenZBuffer[x][y]=-INF;
	}
	public BranchGroup()
	{
		this.initialize();
		this.setBackground(new Color(0,0,0));
		this.directionalLight=new DirectionalLight(new Color(255,255,255),new Vector(1,1,-1));
		this.viewDirection=new Vector(0,0,1);
		this.triangleArrayInfo=new TriangleArrayInfo();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void addChild(GeometryInfo geometryInfo)
	{
		if(geometryInfo!=null)
		{
			geometryInfo.getTriangleStripArray(this.triangleArrayInfo);
		}
	}
	public void paint(Graphics g)
	{
		this.triangleArrayInfo.showAllTriangles(g,screenZBuffer,viewDirection,directionalLight);
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
	public Vector reflectedVector(Vector Normal)
	{
		Vector incidence=new Vector(x,y,z);
		Vector normal=new Vector(Normal.x,Normal.y,Normal.z);
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
	public double product(Vector v0,Vector v1)
	{
		return (v0.x*v1.x+v0.y*v1.y+v0.z*v1.z);
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
class Point
{
	public double x;
	public double y;
	public double z;
	public Point(double x,double y,double z)
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
	public double distanceTo(Point point)
	{
		double dx=point.x-this.x;
		double dy=point.y-this.y;
		double dz=point.z-this.z;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
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
class Material
{
	public Color diffuseColor;
	public Color specularColor;
	public Material(Color diffuseColor,Color specularColor)
	{
		this.diffuseColor=diffuseColor;
		this.specularColor=specularColor;
	}
}