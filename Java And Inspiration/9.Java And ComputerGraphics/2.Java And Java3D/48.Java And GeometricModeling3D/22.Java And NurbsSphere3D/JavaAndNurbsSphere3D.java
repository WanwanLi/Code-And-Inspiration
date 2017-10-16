import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndNurbsSphere3D
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
		Point3d[][] ctrlPoints=
		{
			{
				new Point3d(0,0.15,0)
			},
			{
				new Point3d(-0.2,0.15,-0.2),
				new Point3d(-0.2,0.15,0.2),
				new Point3d(0.2,0.15,0.2),
				new Point3d(0.2,0.15,-0.2)
			},
			{
				new Point3d(-0.4,0.0,-0.4),
				new Point3d(-0.4,0.0,0.4),
				new Point3d(0.4,0.0,0.4),
				new Point3d(0.4,0.0,-0.4)
			},
			{
				new Point3d(-0.2,-0.15,-0.2),
				new Point3d(-0.2,-0.15,0.2),
				new Point3d(0.2,-0.15,0.2),
				new Point3d(0.2,-0.15,-0.2)
			},
			{
				new Point3d(0,-0.15,0)
			}
		};
		double[][] weights=
		{
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1}
		};
		int[] orders={2,2,2,2,2};
		NurbsSphere3D NurbsSphere3D=new NurbsSphere3D(ctrlPoints,weights,orders);
		NurbsSphere3D.setAppearance(Appearance1);
		TransformGroup1.addChild(NurbsSphere3D);
		TransformGroup1.addChild(new PolyMesh3D(ctrlPoints));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class NurbsSphere3D extends Shape3D
{
	public NurbsSphere3D(Point3d[][] ctrlPoints,double[][] weights,int[] order)
	{
		int r=80,c=80,k=ctrlPoints.length,v=0;
		Point3d[][] curveCoordinates=new Point3d[c][k];
		Point3d[][] newCoordinates=new Point3d[c][r];
		Point3d[] coordinates=new Point3d[r*c];
		for(int i=0;i<k;i++)
		{
			int m=order[i],n=ctrlPoints[i].length;
			if(n==1)for(int j=0;j<c;j++)curveCoordinates[j][i]=ctrlPoints[i][0];
			else
			{
				double[] knots=getStandardUniformNurbsKnots(n,m);
				double[][] B=this.getParameterMatrix(knots,c,m,n);
				for(int j=0;j<c;j++)curveCoordinates[j][i]=this.getCoordinate(ctrlPoints[i],weights[i],B,j,m);
			}
		}
		for(int j=0;j<c;j++)newCoordinates[j]=getCoordinates(r,curveCoordinates[j]);
		for(int i=0;i<r;i++)for(int j=0;j<c;j++)coordinates[i*c+j]=newCoordinates[j][i];
		int[] coordinateIndices=new int[(r-1)*(c-1)*4];
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
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[] weights,double[][] B,int k,int order)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0,w=0;
		for(int i=0;i<n+order;i++)
		{
			x+=ctrlPoints[i%n].x*B[k][i]*weights[i%n];
			y+=ctrlPoints[i%n].y*B[k][i]*weights[i%n];
			z+=ctrlPoints[i%n].z*B[k][i]*weights[i%n];
			w+=B[k][i]*weights[i%n];
		}
		return new Point3d(x/w,y/w,z/w);
	}
	private double[][] getParameterMatrix(double[] knots,int CirclePointsLength,int order,int ctrlPointsLength)
	{
		double[] u=knots;
		int l=CirclePointsLength;
		int m=order,n=ctrlPointsLength+order;
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
	public static double[] getStandardUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+2*order+1;
		double[] knots=new double[l];
		double du=1.0/(l-1);
		for(int i=0;i<l;i++)knots[i]=i*du;
		return knots;
	}
	boolean isBetween(double x,double x0,double x1)
	{
		return x==0?x0==0:x0<x&&x<=x1;
	}


	public Point3d[] getCoordinates(int curveLength,Point3d[] ctrlPoints)
	{
		int l=curveLength,n=ctrlPoints.length;
		double[][] B=this.getParameterMatrix(l,n);
		Point3d[] coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,B,i);
		return coordinates;
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
	private double[][] getParameterMatrix(int curvePointsLength,int ctrlPointsLength)
	{
		int order=2;
		double[] u=getBezierUniformBSplineKnots(ctrlPointsLength,order);
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
}
class PolyMesh3D  extends Shape3D
{
	public PolyMesh3D(Point3d[][] meshPoints)
	{
		int m=meshPoints.length,v=0,c=0;
		for(int i=0;i<m;i++)if(meshPoints[i].length>1){v+=meshPoints[i].length+1;c++;}
		Point3d[] coordinates=new Point3d[v];v=0;
		int[] stripCounts=new int[c];c=0;
		for(int i=0;i<m;i++)
		{
			int n=meshPoints[i].length;
			if(n>1)
			{
				for(int j=0;j<n;j++)coordinates[v++]=meshPoints[i][j];
				coordinates[v++]=meshPoints[i][0];
				stripCounts[c++]=n+1;
			}
		}
		LineStripArray IndexedLineStripArray1=new LineStripArray(coordinates.length,IndexedLineStripArray.COORDINATES,stripCounts);
		IndexedLineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(IndexedLineStripArray1);
	}
}