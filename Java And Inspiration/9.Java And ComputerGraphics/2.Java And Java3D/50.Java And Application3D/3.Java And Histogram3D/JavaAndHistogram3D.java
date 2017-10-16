import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndHistogram3D
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
		int number=25;
		double[] x=new double[number*number];
		double[] y=new double[number*number];
		double[] z=new double[number*number];
		double minX=-2;
		double maxX=2;
		double minZ=-2;
		double maxZ=2;
		double dX=(maxX-minX)/(number-1);
		double dZ=(maxZ-minZ)/(number-1);
		for(int i=0;i<number;i++)
		{
			for(int j=0;j<number;j++)
			{
				x[i*number+j]=minX+i*dX;
				z[i*number+j]=minZ+j*dZ;
				y[i*number+j]=Y(x[i*number+j],z[i*number+j]);
			}
		}
		double length=1,width=0.8,height=0.6;
		int row=5,column=4,level=3;
		Color gridColor=new Color(255,255,255);
		Color columnColor=new Color(0,200,0);
		Color pointColor=new Color(0,200,100);
		Color curveColor=new Color(0,0,200);
		Color surfaceColor=new Color(200,100,0);
		Color frameColor=new Color(0,100,200);
		Histogram3D histogram3D=new Histogram3D(length,width,height,row,column,level);
		histogram3D.addColumns(x,y,z,columnColor);
		histogram3D.addPoints(x,y,z,pointColor);
		histogram3D.addCurve(x,y,z,curveColor);
		histogram3D.addSurface(x,y,z,number,number,surfaceColor);
		histogram3D.printMinMax();
		histogram3D.addFrame(x,y,z,number,number,frameColor);
		histogram3D.addGrid(gridColor);
		TransformGroup1.addChild(histogram3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	static double Y(double x,double y)
	{
		return x*y;
	}
}
class Histogram3D extends TransformGroup
{
	private final double INF=11235813.0;
	private int number;
	private double edgeX,edgeY,edgeZ;
	private double length,width,height;
	private int row,column,level,minI,maxI;
	private double minX,maxX,minY,maxY,minZ,maxZ,scaleX,scaleY,scaleZ,minY_X,minY_Z,maxY_X,maxY_Z;
	public Histogram3D(double length,double width,double height,int row,int column,int level)
	{
		this.length=length;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.level=level;
		this.minX=INF;
		this.maxX=-INF;
		this.minY=INF;
		this.maxY=-INF;
		this.minZ=INF;
		this.maxZ=-INF;
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
		int minI=0,maxI=0;
		for(int i=0;i<number;i++)
		{
			if(x[i]>=maxX)this.maxX=x[i];
			if(x[i]<=minX)this.minX=x[i];
			if(y[i]>=maxY){this.maxY=y[i];maxI=i;}
			if(y[i]<=minY){this.minY=y[i];minI=i;}
			if(z[i]>=maxZ)this.maxZ=z[i];
			if(z[i]<=minZ)this.minZ=z[i];
		}
		this.scaleX=length/(maxX-minX);
		this.scaleY=height/(maxY-minY);
		this.scaleZ=width/(maxZ-minZ);
		this.minY_X=x[minI];
		this.minY_Z=z[minI];
		this.maxY_X=x[maxI];
		this.maxY_Z=z[maxI];
	}
	public void printMinMax()
	{
		System.out.println("minY("+minY_X+","+minY_Z+")="+minY);
		System.out.println("maxY("+maxY_X+","+maxY_Z+")="+maxY);
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

class Function3D extends Shape3D
{
	double minX,maxX;
	int m=100;
	int n=50;
	public Point3d Function(double x)
	{
		double w=2;
		return new Point3d(x,Math.sin(x*w),Math.cos(x*w));
	}
	public Vector3d Differential(double x)
	{
		double dx=0.000001;
		double dy=0.000001;
		double dz=0.000001;
		Point3d p1=Function(x);
		Point3d p2=Function(x+dx);
		return new Vector3d(1,(p2.y-p1.y)/dy,(p2.z-p1.z)/dz);
	}
	public Function3D(double minX,double maxX,double r,Appearance appearance)
	{
		double dx=(maxX-minX)/m;
		Point3d[] coordinates=new Point3d[m*n];
		int[] coordinateIndices=new int[(m-1)*n*2];
		int[] stripCounts=new int[m-1];
		double u=2*Math.PI/(n-1);
		double x,y,z,X,Y,Z,rotY,rotZ,dX,dY,dZ,R=0;
		int v=0;
		for(int i=0;i<m;i++)
		{
			dX=minX+dx*i;
			if(i==1)dX=minX;
			if(i==m-2)dX=maxX-dx;
			dY=Function(dX).y;
			dZ=Function(dX).z;
			Vector3d dF=Differential(dX);
			rotY=-Math.atan(dF.z);
			rotZ=Math.atan(dF.y);
			if(i==0||i==m-1)R=0;else R=r;
			for(int j=0;j<n;j++)
			{
				x=0;
				y=R*Math.cos(u*j);
				z=R*Math.sin(u*j);
				Z=z*Math.cos(rotY)-x*Math.sin(rotY);
				X=z*Math.sin(rotY)+x*Math.cos(rotY);
				z=Z;
				x=X;
				X=x*Math.cos(rotZ)-y*Math.sin(rotZ);
				Y=x*Math.sin(rotZ)+y*Math.cos(rotZ);
				x=X;
				y=Y;
				coordinates[v++]=new Point3d(x+dX,y+dY,z+dZ);
			}
		}
		v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=i*n+j;
				coordinateIndices[v++]=(i-1)*n+j;
			}
		}
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());		
	}
}
class Vector3D extends TransformGroup
{
	public Vector3D(Point3d p,Vector3d v)
	{
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,0.8f,0f));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		double length=Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
		double rotZ=Math.atan(v.y)-Math.PI/2;
		double rotY=-Math.atan(v.z);
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0f,length/2,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Cylinder((float)(length/100),(float)(length),Appearance1));
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0f,length,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Cone((float)(length/30),(float)(length/5),Appearance1));
		TransformGroup TransformGroup3=new TransformGroup();
		TransformGroup3.addChild(TransformGroup1);
		TransformGroup3.addChild(TransformGroup2);
		transform3D=new Transform3D();
		transform3D.rotZ(rotZ);
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		TransformGroup4.addChild(TransformGroup3);
		transform3D=new Transform3D();
		transform3D.rotY(rotY);
		TransformGroup TransformGroup5=new TransformGroup(transform3D);
		TransformGroup5.addChild(TransformGroup4);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(p.x,p.y,p.z));
		TransformGroup TransformGroup6=new TransformGroup(transform3D);
		TransformGroup6.addChild(TransformGroup5);
		this.addChild(TransformGroup6);
	}
}