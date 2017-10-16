import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndInterpolator2D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
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
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		double length=1,width=0.8,height=0.6;
		int row=5,column=4,level=3;
		Color gridColor=new Color(255,255,255);
		Color columnColor=new Color(0,200,0);
		Color frameColor=new Color(0,100,200);
		Histogram3D histogram3D=new Histogram3D(length,width,height,row,column,level);
		row=4;column=6;
		double minX=-3,minZ=-2;
		double[] x=new double[row*column];
		double[] y=new double[row*column];
		double[] z=new double[row*column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				x[i*column+j]=minX+j;
				z[i*column+j]=minZ+i;
				y[i*column+j]=Math.random();
			}
		}
		histogram3D.addColumns(x,y,z,columnColor);
		Interpolator2D interpolator2D=new Interpolator2D(y,minX,minZ,row,column);
		double maxX=minX+column-1;
		double maxZ=minZ+row-1;
		row=20;column=30;
		x=new double[row*column];
		z=new double[row*column];
		double dX=(maxX-minX)/(column-1);
		double dZ=(maxZ-minZ)/(row-1);
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				x[i*column+j]=minX+j*dX;
				z[i*column+j]=minZ+i*dZ;
			}
		}
		double BSpline_a=0,BSpline_b=1,Catmull_Rom_a=0.5,Catmull_Rom_b=0,a=1.0/3.0,b=1.0/3.0,n=2.5;
		y=interpolator2D.getSplineInterpolatedValues(x,z,a,b,row,column);
		y=interpolator2D.getLanczosInterpolatedValues(x,z,n,row,column);
		y=interpolator2D.getLinearInterpolatedValues(x,z,row,column);
		histogram3D.addFrame(x,y,z,row,column,frameColor);
		histogram3D.addSurface(x,y,z,row,column,frameColor);
		histogram3D.addGrid(gridColor);
		TransformGroup1.addChild(histogram3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
}
class Interpolator2D
{
	private double[] x;
	private double[] y;
	private double[] z;
	private int row,column,length;
	public Interpolator2D(double[] y,double minX,double minZ,int row,int column)
	{
		this.row=row;
		this.column=column;
		int length=row*column;
		this.x=new double[length];
		this.y=new double[length];
		this.z=new double[length];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				this.x[i*column+j]=minX+j;
				this.y[i*column+j]=y[i*column+j];
				this.z[i*column+j]=minZ+i;
			}
		}
	}
	private int round(double x)
	{
		return (-0.5<=x&&x<0.5?1:0);
	}
	private int round2D(double x,double z)
	{
		return round(x)*round(z);
	}
	public double getRoundInterpolatedValue(double x,double z)
	{
		double roundInterpolatedValue=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				roundInterpolatedValue+=this.y[i*column+j]*round2D(this.x[i*column+j]-x,this.z[i*column+j]-z);
			}
		}
		return roundInterpolatedValue;
	}
	public double[] getRoundInterpolatedValues(double[] x,double[] z,int row,int column)
	{
		double[] roundInterpolatedValues=new double[row*column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				roundInterpolatedValues[i*column+j]=this.getRoundInterpolatedValue(x[i*column+j],z[i*column+j]);
			}
		}
		return roundInterpolatedValues;
	}
	private double linear(double x)
	{
		if(-1<x&&x<=1)return 1-Math.abs(x);
		else return 0;
	}
	private double linear2D(double x,double z)
	{
		return linear(x)*linear(z);
	}
	public double getLinearInterpolatedValue(double x,double z)
	{
		double linearInterpolatedValue=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				linearInterpolatedValue+=this.y[i*column+j]*linear2D(this.x[i*column+j]-x,this.z[i*column+j]-z);
			}
		}
		return linearInterpolatedValue;
	}
	public double[] getLinearInterpolatedValues(double[] x,double[] z,int row,int column)
	{
		double[] linearInterpolatedValues=new double[row*column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				linearInterpolatedValues[i*column+j]=this.getLinearInterpolatedValue(x[i*column+j],z[i*column+j]);
			}
		}
		return linearInterpolatedValues;
	}
	private double sinc(double x)
	{
		if(x==0)return 1;
		else return Math.sin(Math.PI*x)/(Math.PI*x);
	}
	private double sinc2D(double x,double z)
	{
		return sinc(x)*sinc(z);
	}
	public double getSincInterpolatedValue(double x,double z)
	{
		double sincInterpolatedValue=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				sincInterpolatedValue+=this.y[i*column+j]*sinc2D(this.x[i*column+j]-x,this.z[i*column+j]-z);
			}
		}
		return sincInterpolatedValue;
	}
	public double[] getSincInterpolatedValues(double[] x,double[] z,int row,int column)
	{
		double[] sincInterpolatedValues=new double[row*column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				sincInterpolatedValues[i*column+j]=this.getSincInterpolatedValue(x[i*column+j],z[i*column+j]);
			}
		}
		return sincInterpolatedValues;
	}
	private double cubic(double x,double a)
	{
		double absX=Math.abs(x);
		if(absX>=2)return 0;
		double absX3=absX*absX*absX;
		double absX2=absX*absX;
		if(0<=absX&&absX<1)return (2-a)*absX3+(a-3)*absX2+1;
		else if(1<=absX&&absX<2)return -a*absX3+5*a*absX2-8*a*absX+4*a;
		else return 0;
	}
	private double cubic2D(double x,double z,double a)
	{
		return cubic(x,a)*cubic(z,a);
	}
	public double getCubicInterpolatedValue(double x,double z,double a)
	{
		double cubicInterpolatedValue=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				cubicInterpolatedValue+=this.y[i*column+j]*cubic2D(this.x[i*column+j]-x,this.z[i*column+j]-z,a);
			}
		}
		return cubicInterpolatedValue;
	}
	public double[] getCubicInterpolatedValues(double[] x,double[] z,double a,int row,int column)
	{
		double[] cubicInterpolatedValues=new double[row*column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				cubicInterpolatedValues[i*column+j]=this.getCubicInterpolatedValue(x[i*column+j],z[i*column+j],a);
			}
		}
		return cubicInterpolatedValues;
	}
	private double spline(double x,double a,double b)
	{
		double absX=Math.abs(x);
		if(absX>=2)return 0;
		double absX3=absX*absX*absX;
		double absX2=absX*absX;
		if(0<=absX&&absX<1)return (-6*a-9*b+12)*absX3+(6*a+12*b-18)*absX2-2*b+6;
		else if(1<=absX&&absX<2)return (-6*a-b)*absX3+(30*a+6*b)*absX2+(-48*a-12*b)*absX+24*a+8*b;
		else return 0;
	}
	private double spline2D(double x,double z,double a,double b)
	{
		return spline(x,a,b)*spline(z,a,b);
	}
	public double getSplineInterpolatedValue(double x,double z,double a,double b)
	{
		double splineInterpolatedValue=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				splineInterpolatedValue+=this.y[i*column+j]*spline2D(this.x[i*column+j]-x,this.z[i*column+j]-z,a,b);
			}
		}
		return splineInterpolatedValue;
	}
	public double[] getSplineInterpolatedValues(double[] x,double[] z,double a,double b,int row,int column)
	{
		double[] splineInterpolatedValues=new double[row*column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				splineInterpolatedValues[i*column+j]=this.getSplineInterpolatedValue(x[i*column+j],z[i*column+j],a,b);
			}
		}
		return splineInterpolatedValues;
	}
	private double winc(double x,double n)
	{
		double absX=Math.abs(x);
		if(absX>=n)return 0;
		else if(absX==0)return 1;
		else return Math.sin(Math.PI*x/n)/(Math.PI*x/n);
	}
	private double Lanczos(double x,double n)
	{
		return winc(x,n)*sinc(x);
	}
	private double Lanczos2D(double x,double z,double n)
	{
		return Lanczos(x,n)*Lanczos(z,n);
	}
	public double getLanczosInterpolatedValue(double x,double z,double n)
	{
		double LanczosInterpolatedValue=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				LanczosInterpolatedValue+=this.y[i*column+j]*Lanczos2D(this.x[i*column+j]-x,this.z[i*column+j]-z,n);
			}
		}
		return LanczosInterpolatedValue;
	}
	public double[] getLanczosInterpolatedValues(double[] x,double[] z,double n,int row,int column)
	{
		double[] LanczosInterpolatedValues=new double[row*column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				LanczosInterpolatedValues[i*column+j]=this.getLanczosInterpolatedValue(x[i*column+j],z[i*column+j],n);
			}
		}
		return LanczosInterpolatedValues;
	}
}
class Histogram3D extends TransformGroup
{
	private int number;
	private double edgeX,edgeY,edgeZ;
	private double length,width,height;
	private int row,column,level;
	private double minX,maxX,minY,maxY,minZ,maxZ,scaleX,scaleY,scaleZ;
	public Histogram3D(double length,double width,double height,int row,int column,int level)
	{
		this.length=length;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.level=level;
		this.minX=-1;
		this.maxX=1;
		this.minY=-1;
		this.maxY=1;
		this.minZ=-1;
		this.maxZ=1;
	}
	public void addGrid(Color gridColor)
	{
		TransformGroup grid=new TransformGroup();
		TransformGroup gridCoordinates=new TransformGroup();
		Appearance Appearance1=new Appearance();
		ColoringAttributes ColoringAttributes1=new ColoringAttributes();
		ColoringAttributes1.setColor(new Color3f(gridColor));
		Appearance1.setColoringAttributes(ColoringAttributes1);
		Appearance1.setMaterial(null);
		Font3D font3D=new Font3D(new Font("Microsoft Tai Le",Font.BOLD,1),null);
		double fontSize=0.2,fontEdge=0.3;
		Transform3D Transform3D_gridCoordinate=new Transform3D();
		double intervalX=length/column;
		double intervalY=height/level;
		double intervalZ=width/row;
		double coordinateIntervalX=(maxX-minX)/column;
		double coordinateIntervalY=(maxY-minY)/level;
		double coordinateIntervalZ=(maxZ-minZ)/row;
		int v=0,n=2*(row+1+column+1+level+1)*2;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		Transform3D_gridCoordinate.rotX(-Math.PI/2);
		for(int i=0;i<=row;i++)
		{
			double x0=-0.5*length;
			double x1=0.5*length;
			double y=-0.5*height;
			double z=-0.5*width+i*intervalZ;
			coordinates[v++]=new Point3d(x0,y,z);
			coordinates[v++]=new Point3d(x1,y,z);
			double coordinateZ=minZ+i*coordinateIntervalZ;
			String coordinate=format(coordinateZ);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalZ,fontSize*intervalZ,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x1+fontEdge*intervalZ,y,z+fontSize*intervalZ));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int j=0;j<=column;j++)
		{
			double x=-0.5*length+j*intervalX;
			double y=-0.5*height;
			double z0=-0.5*width;
			double z1=0.5*width;
			coordinates[v++]=new Point3d(x,y,z0);
			coordinates[v++]=new Point3d(x,y,z1);
			double coordinateX=minX+j*coordinateIntervalX;
			String coordinate=format(coordinateX);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalX,fontSize*intervalX,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x-fontSize*intervalX,y,z1+fontEdge*intervalX));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		Transform3D_gridCoordinate.rotY(0);
		for(int j=0;j<=column;j++)
		{
			double x=-0.5*length+j*intervalX;
			double y0=-0.5*height;
			double y1=0.5*height;
			double z=-0.5*width;
			coordinates[v++]=new Point3d(x,y0,z);
			coordinates[v++]=new Point3d(x,y1,z);
			double coordinateX=minX+j*coordinateIntervalX;
			String coordinate=format(coordinateX);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalX,fontSize*intervalX,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x-fontSize*intervalX,y1+fontEdge*intervalX,z));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int k=0;k<=level;k++)
		{
			double x0=-0.5*length;
			double x1=0.5*length;
			double y=-0.5*height+k*intervalY;
			double z=-0.5*width;
			coordinates[v++]=new Point3d(x0,y,z);
			coordinates[v++]=new Point3d(x1,y,z);
			double coordinateY=minY+k*coordinateIntervalY;
			String coordinate=format(coordinateY);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalY,fontSize*intervalY,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x1+fontEdge*intervalY,y,z));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		Transform3D_gridCoordinate.rotY(Math.PI/2);
		for(int k=0;k<=level;k++)
		{
			double x=-0.5*length;
			double y=-0.5*height+k*intervalY;
			double z0=-0.5*width;
			double z1=0.5*width;
			coordinates[v++]=new Point3d(x,y,z0);
			coordinates[v++]=new Point3d(x,y,z1);
			double coordinateY=minY+k*coordinateIntervalY;
			String coordinate=format(coordinateY);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalY,fontSize*intervalY,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x,y,z1+2.5*fontEdge*intervalY));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int i=0;i<=row;i++)
		{
			double x=-0.5*length;
			double y0=-0.5*height;
			double y1=0.5*height;
			double z=-0.5*width+i*intervalZ;
			coordinates[v++]=new Point3d(x,y0,z);
			coordinates[v++]=new Point3d(x,y1,z);
			double coordinateZ=minZ+i*coordinateIntervalZ;
			String coordinate=format(coordinateZ);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalZ,fontSize*intervalZ,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x,y1+fontEdge*intervalZ,z+fontSize*intervalZ));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(gridColor);
		Shape3D gridLines=new Shape3D();
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance2=new Appearance();
		Appearance2.setLineAttributes(LineAttributes1);
		gridLines.setGeometry(LineArray1);
		gridLines.setAppearance(Appearance2);
		grid.addChild(gridLines);
		grid.addChild(gridCoordinates);
		this.addChild(grid);
	}
	public void setCoordinates(double[] x,double[] y,double[] z)
	{
		int len=x.length<y.length?x.length:y.length;
		this.number=len<z.length?len:z.length;
		for(int i=0;i<number;i++)
		{
			if(x[i]>maxX)this.maxX=x[i];
			if(x[i]<minX)this.minX=x[i];
			if(y[i]>maxY)this.maxY=y[i];
			if(y[i]<minY)this.minY=y[i];
			if(z[i]>maxZ)this.maxZ=z[i];
			if(z[i]<minZ)this.minZ=z[i];
		}
		this.scaleX=length/(maxX-minX);
		this.scaleY=height/(maxY-minY);
		this.scaleZ=width/(maxZ-minZ);
	}
	private double transformToCoordinateX(double xi)
	{
		return -0.5*length+(xi-minX)*scaleX;
	}
	private double transformToCoordinateY(double yi)
	{
		return -0.5*height+(yi-minY)*scaleY;
	}
	private double transformToCoordinateZ(double zi)
	{
		return -0.5*width+(zi-minZ)*scaleZ;
	}
	public void addColumns(double[] x,double[] y,double[] z,Color columnColor)
	{
		this.setCoordinates(x,y,z);
		int n=2*number,v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<number;i++)
		{
			double X=this.transformToCoordinateX(x[i]);
			double Y0=this.transformToCoordinateY(minY);
			double Y1=this.transformToCoordinateY(y[i]);
			double Z=this.transformToCoordinateZ(z[i]);
			coordinates[v++]=new Point3d(X,Y0,Z);
			coordinates[v++]=new Point3d(X,Y1,Z);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(columnColor);
		Shape3D columns=new Shape3D();
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		columns.setGeometry(LineArray1);
		columns.setAppearance(Appearance1);
		this.addChild(columns);
	}
	public void addPoints(double[] x,double[] y,double[] z,Color pointColor)
	{
		this.setCoordinates(x,y,z);
		int n=number,v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<n;i++)
		{
			double X=this.transformToCoordinateX(x[i]);
			double Y=this.transformToCoordinateY(y[i]);
			double Z=this.transformToCoordinateZ(z[i]);
			coordinates[i]=new Point3d(X,Y,Z);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(pointColor);
		Shape3D points=new Shape3D();
		PointArray PointArray1=new PointArray(n,PointArray.COORDINATES|PointArray.COLOR_3);
		PointArray1.setCoordinates(0,coordinates);
		PointArray1.setColors(0,colors);
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(10.0f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setPointAttributes(PointAttributes1);
		points.setGeometry(PointArray1);
		points.setAppearance(Appearance1);
		this.addChild(points);
	}
	public void addCurve(double[] x,double[] y,double[] z,Color curveColor)
	{
		this.setCoordinates(x,y,z);
		int n=2*(number-1),v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<number-1;i++)
		{
			double X0=this.transformToCoordinateX(x[i]);
			double Y0=this.transformToCoordinateY(y[i]);
			double Z0=this.transformToCoordinateZ(z[i]);
			double X1=this.transformToCoordinateX(x[i+1]);
			double Y1=this.transformToCoordinateY(y[i+1]);
			double Z1=this.transformToCoordinateZ(z[i+1]);
			coordinates[v++]=new Point3d(X0,Y0,Z0);
			coordinates[v++]=new Point3d(X1,Y1,Z1);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(curveColor);
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		this.addChild(new Shape3D(LineArray1,Appearance1));
	}
	public void addSurface(double[] x,double[] y,double[] z,int row,int column,Color surfaceColor)
	{
		this.setCoordinates(x,y,z);
		Point3d[] coordinates=new Point3d[row*column];
		int[] coordinateIndices=new int[(row-1)*column*2];
		int[] stripCounts=new int[row-1];
		int v=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				double X=this.transformToCoordinateX(x[i*column+j]);
				double Y=this.transformToCoordinateY(y[i*column+j]);
				double Z=this.transformToCoordinateZ(z[i*column+j]);
				coordinates[v++]=new Point3d(X,Y,Z);
			}
		}
		v=0;
		for(int i=1;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				coordinateIndices[v++]=i*column+j;
				coordinateIndices[v++]=(i-1)*column+j;
			}
		}
		for(int i=0;i<row-1;i++)stripCounts[i]=2*column;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(surfaceColor));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		this.addChild(new Shape3D(GeometryInfo1.getGeometryArray(),Appearance1));
		v=0;
		for(int i=1;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				coordinateIndices[v++]=(i-1)*column+j;
				coordinateIndices[v++]=i*column+j;
			}
		}
		GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.addChild(new Shape3D(GeometryInfo1.getGeometryArray(),Appearance1));
	}
	public void addFrame(double[] x,double[] y,double[] z,int row,int column,Color frameColor)
	{
		this.setCoordinates(x,y,z);
		int n=(row-1)*(column-1)*4+(row-1)*2+(column-1)*2,v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<row-1;i++)
		{
			for(int j=0;j<column-1;j++)
			{
				double X00=this.transformToCoordinateX(x[(i+0)*column+(j+0)]);
				double Y00=this.transformToCoordinateY(y[(i+0)*column+(j+0)]);
				double Z00=this.transformToCoordinateZ(z[(i+0)*column+(j+0)]);
				double X01=this.transformToCoordinateX(x[(i+0)*column+(j+1)]);
				double Y01=this.transformToCoordinateY(y[(i+0)*column+(j+1)]);
				double Z01=this.transformToCoordinateZ(z[(i+0)*column+(j+1)]);
				double X10=this.transformToCoordinateX(x[(i+1)*column+(j+0)]);
				double Y10=this.transformToCoordinateY(y[(i+1)*column+(j+0)]);
				double Z10=this.transformToCoordinateZ(z[(i+1)*column+(j+0)]);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X01,Y01,Z01);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X10,Y10,Z10);
			}
		}
		for(int i=0;i<row-1;i++)
		{
				int j=column-1;
				double X00=this.transformToCoordinateX(x[(i+0)*column+(j+0)]);
				double Y00=this.transformToCoordinateY(y[(i+0)*column+(j+0)]);
				double Z00=this.transformToCoordinateZ(z[(i+0)*column+(j+0)]);
				double X10=this.transformToCoordinateX(x[(i+1)*column+(j+0)]);
				double Y10=this.transformToCoordinateY(y[(i+1)*column+(j+0)]);
				double Z10=this.transformToCoordinateZ(z[(i+1)*column+(j+0)]);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X10,Y10,Z10);
		}
		for(int j=0;j<column-1;j++)
		{
				int i=row-1;
				double X00=this.transformToCoordinateX(x[(i+0)*column+(j+0)]);
				double Y00=this.transformToCoordinateY(y[(i+0)*column+(j+0)]);
				double Z00=this.transformToCoordinateZ(z[(i+0)*column+(j+0)]);
				double X01=this.transformToCoordinateX(x[(i+0)*column+(j+1)]);
				double Y01=this.transformToCoordinateY(y[(i+0)*column+(j+1)]);
				double Z01=this.transformToCoordinateZ(z[(i+0)*column+(j+1)]);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X01,Y01,Z01);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(frameColor);
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		this.addChild(new Shape3D(LineArray1,Appearance1));
	}
	private static String format(double x)
	{
		if(Math.abs(x)<1.0E-3)return "0.0";
		String string=x+"";
		String formatString="";
		int i=0,j=0,len=string.length();
		while(i<len&&string.charAt(i)!='.')formatString+=string.charAt(i++);
		for(;j<4&&i<len;j++,i++)formatString+=string.charAt(i);
		return formatString;
	}
}