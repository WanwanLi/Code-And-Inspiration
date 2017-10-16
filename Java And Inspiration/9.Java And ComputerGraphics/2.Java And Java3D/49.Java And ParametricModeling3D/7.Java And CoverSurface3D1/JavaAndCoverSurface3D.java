import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCoverSurface3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance1.setMaterial(Material1);
		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance2.setMaterial(Material2);
		int m=20,n1=20;double x0=-1.8,x1=1.8,z0=-1.5,z1=1.5,dx=(x1-x0)/(n1-1),dz=(z1-z0)/(m-1),errorDistance=0.01;
		Point3d[][] surfacePoints=new Point3d[m][n1];
		for(int i=0;i<m;i++)
		{
			double z=z0+i*dz;
			for(int j=0;j<n1;j++)
			{

				double x=x0+j*dx;
				double y=F(-x,-z);
				surfacePoints[i][j]=new Point3d(x,y,z);
			}
		}
		CoverSurface3D CoverSurface3D=new CoverSurface3D(surfacePoints,errorDistance);
		CoverSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(CoverSurface3D);
		Surface3D Surface3D=new Surface3D(surfacePoints);
		Surface3D.setAppearance(Appearance1);
	//	TransformGroup1.addChild(Surface3D);
		TransformGroup1.addChild(new Mesh3D(surfacePoints));


//for(int i=0;i<m;i++)
/*
{
Point3d[] points1=CoverSurface3D.getVectorizedPoints(surfacePoints[0],errorDistance);
Points3D points3D=new Points3D(points1);
TransformGroup1.addChild(points3D);

			int n=80;Point3d[] points=new Point3d[n];
			Point3d[] curvePoints=CoverSurface3D.getVectorizedPoints(surfacePoints[0],errorDistance);
			int l=curvePoints.length;Point3d[] ctrlPoints=new Point3d[l];
			double[][] B=CoverSurface3D.getParameterMatrix(l,l);CoverSurface3D.inverse(B);
			for(int j=0;j<l;j++)ctrlPoints[j]=CoverSurface3D.getCoordinate(curvePoints,B,j);
			B=CoverSurface3D.getParameterMatrix(n,l);
			for(int j=0;j<n;j++)points[j]=CoverSurface3D.getCoordinate(ctrlPoints,B,j);
			Curve3D curve=new Curve3D(points);
			TransformGroup1.addChild(curve);
}
*/


		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	static Point3d sub(double k,Point3d p0,Point3d p1)
	{
		return new Point3d(k*(p1.x-p0.x),k*(p1.y-p0.y),k*(p1.z-p0.z));
	}
	public static double F(double x,double z)
	{
		double w=3,A=0.2;x*=w;z*=w;
		return A*((int)(Math.sin(x)+Math.sin(z)+x+z));// x= -3.2..3.2,y=-2.2..2.2;
	//	return A*(Math.sin(x)*Math.sin(z)+x+z);// x= -3.2..3.2,y=-2.2..2.2;
	}
}
class CoverSurface3D extends Shape3D
{
	public CoverSurface3D(Point3d[][] surfacePoints,double errorDistance)
	{
		int m=80,n=80,v=0;
		Point3d[] coordinates=new Point3d[m*n];
		Point3d[][] newSurfacePoints=new Point3d[n][surfacePoints.length];
		for(int i=0;i<surfacePoints.length;i++)
		{
			Point3d[] curvePoints=getVectorizedPoints(surfacePoints[i],errorDistance);
			int l=curvePoints.length;Point3d[] ctrlPoints=new Point3d[l];
			double[][] B=getParameterMatrix(l,l);inverse(B);
			for(int j=0;j<l;j++)ctrlPoints[j]=getCoordinate(curvePoints,B,j);
			B=getParameterMatrix(n,l);
		//	for(int j=0;j<n;j++)newSurfacePoints[j][i]=this.getCoordinate(ctrlPoints,B,j);
			for(int j=0;j<n;j++)newSurfacePoints[j][i]=this.getCoordinate(curvePoints,B,j);
		}
		for(int i=0;i<newSurfacePoints.length;i++)
		{
			Point3d[] curvePoints=getVectorizedPoints(newSurfacePoints[i],errorDistance);
			int l=curvePoints.length;Point3d[] ctrlPoints=new Point3d[l];
			double[][] B=getParameterMatrix(l,l);inverse(B);
			for(int j=0;j<l;j++)ctrlPoints[j]=getCoordinate(curvePoints,B,j);
			B=getParameterMatrix(m,l);
		//	for(int j=0;j<m;j++)coordinates[j*n+i]=this.getCoordinate(ctrlPoints,B,j);
			for(int j=0;j<m;j++)coordinates[j*n+i]=this.getCoordinate(curvePoints,B,j);
		}
		int[] coordinateIndices=new int[(m-1)*n*2];
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	public Point3d[] getVectorizedPoints(Point3d[] curvePoints,double errorDistance)
	{
		int length=curvePoints.length,i=0,v=2;
		boolean[] isVertex=new boolean[length];
		isVertex[0]=true;for(int j=1;j<length-1;j++)isVertex[j]=false;isVertex[length-1]=true;
		for(int j=1;j<length;j++)
		{
			double distance=getAverageDistanceToLine(curvePoints,i,j);
			if(distance>errorDistance)
			{
				i=j-1;
				v++;
				isVertex[i]=true;
			}
		}
		Point3d[] vectorizedPoints=new Point3d[v];v=0;
		for(int j=0;j<length;j++)if(isVertex[j])vectorizedPoints[v++]=curvePoints[j];
		return vectorizedPoints;
	}
	private double distance(Point3d p0,Point3d p1)
	{
		double x=p1.x-p0.x;
		double y=p1.y-p0.y;
		double z=p1.z-p0.z;
		return Math.sqrt(x*x+y*y+z*z);
	}
	private Point3d midPoint(Point3d p0,Point3d p1,double u)
	{
		double x=(1-u)*p0.x+u*p1.x;
		double y=(1-u)*p0.y+u*p1.y;
		double z=(1-u)*p0.z+u*p1.z;
		return new Point3d(x,y,z);
	}
	private double getAverageDistanceToLine(Point3d[] curvePoints,int i,int j)
	{
		if(i==j||i+1==j)return 0;
		double d=0;
		Point3d p0=curvePoints[i],p1=curvePoints[j];
		for(int k=i+1;k<j;k++)
		{
			double u=(0.0+k-i)/(j-i);
			Point3d m=midPoint(p0,p1,u);
			Point3d p=curvePoints[k];
			d+=distance(m,p);
		}
		return d/(j-i-1);
	}
	public Point3d getCoordinate(Point3d[] ctrlPoints,double[][] B,int k)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0;
		for(int i=0;i<n;i++)
		{
			x+=ctrlPoints[i].x*B[k][i];
			y+=ctrlPoints[i].y*B[k][i];
			z+=ctrlPoints[i].z*B[k][i];
		}
		return new Point3d(x,y,z);
	}
	public double[][] getParameterMatrix(int curvePointsLength,int ctrlPointsLength)
	{
		int l=curvePointsLength;
		int m=1,n=ctrlPointsLength;
		double[] u=getStandardUniformBSplineKnots(n,m);
	//	double[] u=getBezierUniformBSplineKnots(n,m);
		double d=(u[n]-u[m])/(l-1);
		double[][] b=new double[l][n];
		double[][] B=new double[m+1][n+1];
		for(int k=0;k<l;k++)
		{
			double t=u[m]+k*d;
			for(int j=0;j<=n;j++)B[0][j]=isBetween(t,u[j],u[j+1])?1:0;
			for(int i=1;i<=m;i++)
			{
				for(int j=0;j<n;j++)
				{
					double du0=u[j+0+i]-u[j+0];
					double du1=u[j+1+i]-u[j+1];
					double U0=du0==0?0:(t-u[j])/du0;
					double U1=du1==0?0:(u[j+1+i]-t)/du1;
					B[i][j]=B[i-1][j]*U0+B[i-1][j+1]*U1;
				}
			}
			for(int j=0;j<n;j++)b[k][j]=B[m][j];
		}
		return b;	
	}
	public static double[] getStandardUniformBSplineKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		double du=1.0/(l-1);
		for(int i=0;i<l;i++)knots[i]=i*du;
		return knots;
	}
	public static double[] getBezierUniformBSplineKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*order;
		double du=1.0/(k-1);
		for(int i=0;i<order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=i*du;
		for(int i=0;i<order;i++)knots[c++]=1;
		return knots;
	}
	boolean isBetween(double x,double x0,double x1)
	{
		return x==0?x0==0:x0<x&&x<=x1;
	}
	public void inverse(double[][] a)
	{
		int n=a.length;
		double[][] AE=new double[n][n+n];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)AE[i][j]=a[i][j];
			AE[i][n+i]=1;
		}
		for(int k=0;k<n;k++)
		{
			for(int i=0;i<n;i++)
			{
				if(i==k)continue;
				double M=AE[i][k]/AE[k][k];
				for(int j=k;j<n+n;j++)AE[i][j]-=M*AE[k][j];
			}
		}
		for(int i=0;i<n;i++)
		{
			double M=AE[i][i];
			for(int j=0;j<n+n;j++)AE[i][j]/=M;
		}
		for(int i=0;i<n;i++)for(int j=0;j<n;j++)a[i][j]=AE[i][n+j];
	}
}
class Surface3D extends Shape3D
{
	public Surface3D(Point3d[][] surfacePoints)
	{
		int m=surfacePoints.length,n=surfacePoints[0].length,v=0;
		Point3d[] coordinates=new Point3d[m*n];
		for(int i=0;i<m;i++)for(int j=0;j<m;j++)coordinates[i*n+j]=surfacePoints[i][j];
		int[] coordinateIndices=new int[(m-1)*n*2];
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
}
/*
class LinearSurface3D extends Shape3D
{
	public LinearSurface3D(Point3d[] startEdgePoints,Point3d[] endEdgePoints)
	{
		int n=startEdgePoints.length,v=0;
		Point3d[] coordinates=new Point3d[2*n];
		for(int j=0;j<n;j++)coordinates[v++]=startEdgePoints[j];
		for(int j=0;j<n;j++)coordinates[v++]=endEdgePoints[j];
		int[] coordinateIndices=new int[n*2];v=0;
		for(int j=0;j<n;j++)
		{
			coordinateIndices[v++]=n+j;
			coordinateIndices[v++]=j;
		}
		int[] stripCounts={2*n};
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
}
*/
class Mesh3D  extends Shape3D
{
	public Mesh3D(Point3d[][] meshPoints)
	{
		int m=meshPoints.length,n=meshPoints[0].length,v=0;
		Point3d[] coordinates=new Point3d[m*n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)coordinates[i*n+j]=meshPoints[i][j];
		int[] coordinateIndices=new int[(m-1)*(n-1)*4+(m-1)*2+(n-1)*2];
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+1)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+1);
			}
		}
		for(int i=0;i<m-1;i++)
		{
			int j=n-1;
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+1)*n+(j+0);
			}
		}
		{
			int i=m-1;
			for(int j=0;j<n-1;j++)
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+1);
			}
		}
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates.length,IndexedLineArray.COORDINATES,coordinateIndices.length);
		IndexedLineArray1.setCoordinates(0,coordinates);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		this.setGeometry(IndexedLineArray1);
	}
}
class Curve3D extends Shape3D
{
	public Curve3D(Point3d[] coordinates)
	{
		int n=coordinates.length;
		int[] coordinateIndices=new int[2*(n-1)];
		int c=0;
		for(int i=0;i<n-1;i++)
		{
			coordinateIndices[c++]=i;
			coordinateIndices[c++]=i+1;
		}
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates.length,IndexedLineArray.COORDINATES,coordinateIndices.length);
		IndexedLineArray1.setCoordinates(0,coordinates);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		this.setGeometry(IndexedLineArray1);
	}
}
class Points3D extends Shape3D
{
	public Points3D(Point3d[] coordinates)
	{
		PointArray PointArray1=new PointArray(coordinates.length,PointArray.COORDINATES);
		PointArray1.setCoordinates(0,coordinates);
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(10f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setPointAttributes(PointAttributes1);
		this.setAppearance(Appearance1);
		this.setGeometry(PointArray1);
	}
}