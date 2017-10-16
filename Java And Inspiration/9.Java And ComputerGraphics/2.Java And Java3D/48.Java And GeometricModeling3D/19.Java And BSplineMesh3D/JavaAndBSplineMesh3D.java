import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndBSplineMesh3D
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
		Point3d[][] surfacePoints=
		{
			{
				new Point3d(-0.6,-0.1,-0.2),
				new Point3d(-0.4,-0.25,-0.3),
				new Point3d(-0.1,-0.25,-0.3),
				new Point3d(0.3,-0.55,-0.2),
				new Point3d(0.5,-0.42,-0.3),
				new Point3d(0.65,-0.3,-0.2)
			},
			{
				new Point3d(-0.6,0.1,0.1),
				new Point3d(-0.4,0.25,0.1),
				new Point3d(-0.1,0.25,0.1),
				new Point3d(0.3,-0.25,0.1),
				new Point3d(0.5,-0.2,0.1),
				new Point3d(0.65,0.3,0.1)
			},
			{
				new Point3d(-0.6,-0.1,0.2),
				new Point3d(-0.4,-0.25,0.3),
				new Point3d(-0.1,-0.25,0.3),
				new Point3d(0.3,-0.55,0.2),
				new Point3d(0.5,-0.42,0.3),
				new Point3d(0.65,-0.3,0.2)
			},
		};
	//	int uOrder=2;double[] uKnots=BSplineSurface3D.getStandardUniformBSplineKnots(surfacePoints.length,uOrder);
	//	int vOrder=2;double[] vKnots=BSplineSurface3D.getStandardUniformBSplineKnots(surfacePoints[0].length,vOrder);
		int uOrder=2;double[] uKnots=BSplineSurface3D.getBezierUniformBSplineKnots(surfacePoints.length,uOrder);
		int vOrder=2;double[] vKnots=BSplineSurface3D.getBezierUniformBSplineKnots(surfacePoints[0].length,vOrder);
		BSplineMesh3D bSplineMesh3D=new BSplineMesh3D(surfacePoints,uOrder,vOrder);
		TransformGroup1.addChild(bSplineMesh3D);
		BSplineSurface3D bSplineSurface3D=new BSplineSurface3D(bSplineMesh3D.getCtrlPoints(),uKnots,vKnots,uOrder,vOrder);
		bSplineSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(bSplineSurface3D);
		TransformGroup1.addChild(new Mesh3D(surfacePoints));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class BSplineMesh3D extends Shape3D
{
	Point3d[][] ctrlPoints;
	public BSplineMesh3D(Point3d[][] surfacePoints,int uOrder,int vOrder)
	{
		int m=surfacePoints.length,n=surfacePoints[0].length;
		Point3d[][] newSurfacePoints=transpose(surfacePoints);
		double[][] U=getParameterMatrix(m,uOrder);
		double[][] V=getParameterMatrix(n,vOrder);
		double[][] B=new double[m*n][m*n];
		for(int u=0;u<m;u++)
		{
			for(int v=0;v<n;v++)
			{
				for(int i=0;i<m;i++)
				{
					for(int j=0;j<n;j++)
					{
						B[u*n+v][i*n+j]=U[u][i]*V[v][j];
					}
				}
			}
		}
		inverse(B);
		this.ctrlPoints=new Point3d[m][n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)ctrlPoints[i][j]=getCoordinate(surfacePoints,B,i*n+j);
		this.setGeometry(new Mesh3D(ctrlPoints).getGeometry());
	}
	public Point3d[][] getCtrlPoints()
	{
		return this.ctrlPoints;
	}
	private Point3d getCoordinate(Point3d[][] ctrlPoints,double[][] B,int k)
	{
		int m=ctrlPoints.length;
		int n=ctrlPoints[0].length;
		double x=0,y=0,z=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				x+=ctrlPoints[i][j].x*B[k][i*n+j];
				y+=ctrlPoints[i][j].y*B[k][i*n+j];
				z+=ctrlPoints[i][j].z*B[k][i*n+j];
			}
		}
		return new Point3d(x,y,z);
	}
	private double[][] getParameterMatrix(int curvePointsLength,int order)
	{
		int l=curvePointsLength;
		int m=order,n=curvePointsLength;
		double[] u=getBezierUniformBSplineKnots(n,m);
	//	double[] u=getStandardUniformBSplineKnots(n,m);
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
	public  static double distance(Point3d point0,Point3d point1)
	{
		double dx=point1.x-point0.x;
		double dy=point1.y-point0.y;
		double dz=point1.z-point0.z;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
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
	public  static double[] getRiesenfeldNurbsKnots(Point3d[] ctrlPoints,int order)
	{
		int l=ctrlPoints.length+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*order;
		double[] u=new double[k];u[0]=0;
		for(int i=1;i<k;i++)u[i]=u[i-1]+distance(ctrlPoints[i-1],ctrlPoints[i]);
		for(int i=0;i<order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=u[i]/u[k-1];
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
	private Point3d[][] transpose(Point3d[][] pointsMatrix)
	{
		int m=pointsMatrix.length,n=pointsMatrix[0].length;
		Point3d[][] newMatrix=new Point3d[n][m];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)newMatrix[i][j]=pointsMatrix[j][i];
		}
		return newMatrix;
	}
	public static void print(double[][] matrix)
	{
		int m=matrix.length;
		int n=matrix[0].length;
		for(int i=0;i<n*18;i++)System.out.print("_");System.out.println("\n");
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				System.out.print("\t"+format(matrix[i][j])+"\t");
			}
			System.out.println();
		}
		for(int i=0;i<n*18;i++)System.out.print("_");System.out.println("\n");
	}
	private static String format(double d)
	{
		String s=d+"";
		if(s.length()>=6&&!s.contains("E"))return s.substring(0,6);
		if(s.contains("E"))s="0.0";
		return s;
	}
}
class BSplineSurface3D extends Shape3D
{
	public BSplineSurface3D(Point3d[][] ctrlPoints,double[] uKnots,double[] vKnots,int uOrder,int vOrder)
	{
		int r=100,c=100,v=0,m=ctrlPoints.length,n=ctrlPoints[0].length;
		Point3d[][] newCtrlPoints=new Point3d[n][m];
		for(int i=0;i<n;i++)for(int j=0;j<m;j++)newCtrlPoints[i][j]=ctrlPoints[j][i];
		double[][] U=this.getParameterMatrix(uKnots,r,uOrder,m);
		double[][] V=this.getParameterMatrix(vKnots,c,vOrder,n);
		Point3d[] coordinates=new Point3d[r*c];
		Point3d[] ctrlCoordinates=new Point3d[n];
		int[] coordinateIndices=new int[(r-1)*(c-1)*4];
		for(int i=0;i<r;i++)
		{
			for(int k=0;k<n;k++)ctrlCoordinates[k]=getCoordinate(newCtrlPoints[k],U,i);
			for(int j=0;j<c;j++)coordinates[i*c+j]=this.getCoordinate(ctrlCoordinates,V,j);
		}
/*
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)coordinates[i*c+j]=this.getCoordinate(ctrlPoints,U,V,i,j);
		}
*/
		for(int i=0;i<r-1;i++)
		{
			for(int j=0;j<c-1;j++)
			{
				coordinateIndices[v++]=(i+0)*c+(j+0);
				coordinateIndices[v++]=(i+1)*c+(j+0);
				coordinateIndices[v++]=(i+1)*c+(j+1);
				coordinateIndices[v++]=(i+0)*c+(j+1);
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES|IndexedQuadArray.NORMALS,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[][] B,int k)
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
	private Point3d getCoordinate(Point3d[][] ctrlPoints,double[][] U,double[][] V,int u,int v)
	{
		int m=ctrlPoints.length;
		int n=ctrlPoints[0].length;
		double x=0,y=0,z=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				x+=ctrlPoints[i][j].x*U[u][i]*V[v][j];
				y+=ctrlPoints[i][j].y*U[u][i]*V[v][j];
				z+=ctrlPoints[i][j].z*U[u][i]*V[v][j];
			}
		}
		return new Point3d(x,y,z);
	}
	private double[][] getParameterMatrix(double[] knots,int curvePointsLength,int order,int ctrlPointsLength)
	{
		double[] u=knots;
		int l=curvePointsLength;
		int m=order,n=ctrlPointsLength;
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
	public static double[] getSemiUniformBSplineKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*(order+1);
		for(int i=0;i<=order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=0.5;
		for(int i=0;i<=order;i++)knots[c++]=1;
		return knots;
	}
	boolean isBetween(double x,double x0,double x1)
	{
		return x==0?x0==0:x0<x&&x<=x1;
	}
}
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