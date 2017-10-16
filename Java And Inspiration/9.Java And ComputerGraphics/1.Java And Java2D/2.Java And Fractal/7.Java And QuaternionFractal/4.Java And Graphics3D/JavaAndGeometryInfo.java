import java.awt.*;
import java.awt.event.*;
public class JavaAndGeometryInfo
{
	public static void main(String[] args)
	{
		int m=200;
		int n=400;
		double R=200;
		double[] coordinates=new double[3*m*n];
		int[] coordinateIndices=new int[(m-1)*2*n];
		int[] stripCounts=new int[m-1];
		final int X=0,Y=1,Z=2;
		int v=0;
		for(int i=0;i<m;i++)
		{
			double u=Math.PI/(m-1)*i;
			for(int j=0;j<n;j++)
			{
				double w=2*Math.PI/n*j;
				coordinates[v+X]=R*Math.sin(u)*Math.cos(w);
				coordinates[v+Y]=R*Math.cos(u);
				coordinates[v+Z]=R*Math.sin(u)*Math.sin(w);
				v+=3;
			}
		}
		v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
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
		int x01=(x0+x1)/2;
		int y01=(y0+y1)/2;
		int x12=(x1+x2)/2;
		int y12=(y1+y2)/2;
		int x20=(x2+x0)/2;
		int y20=(y2+y0)/2;
		g.drawLine(x0,y0,x1,y1);
		g.drawLine(x1,y1,x2,y2);
		g.drawLine(x2,y2,x0,y0);
		drawMiddleLine(g,x0,y0,x1,y1,x2,y2,x0,y0,0);
		drawMiddleLine(g,x1,y1,x2,y2,x0,y0,x1,y1,0);
		drawMiddleLine(g,x2,y2,x0,y0,x1,y1,x2,y2,0);
	}
	private void drawMiddleLine(Graphics g,int x0,int y0,int x1,int y1,int x2,int y2,int x3,int y3,int h)
	{
		if(h>Height)return;
		int x01=(x0+x1)/2;
		int y01=(y0+y1)/2;
		int x23=(x2+x3)/2;
		int y23=(y2+y3)/2;
		g.drawLine(x01,y01,x23,y23);
		drawMiddleLine(g,x0,y0,x01,y01,x23,y23,x3,y3,h+1);
		drawMiddleLine(g,x01,y01,x1,y1,x2,y2,x23,y23,h+1);
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
			normal.cross(new Vector(x[1]-x[0],y[1]-y[0],z[1]-z[0]),new Vector(x[2]-x[1],y[2]-y[1],z[2]-z[1]));
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