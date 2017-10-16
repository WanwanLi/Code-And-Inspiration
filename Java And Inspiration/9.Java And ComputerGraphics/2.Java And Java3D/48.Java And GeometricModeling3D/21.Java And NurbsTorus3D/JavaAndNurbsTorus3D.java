import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndNurbsTorus3D
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
				new Point3d(0,0,-0.2),
				new Point3d(0,0,-0.2),
				new Point3d(0,0,-0.2),
				new Point3d(0,0,-0.2)
			},
			{
				new Point3d(-0.2,-0.2,-0.2),
				new Point3d(-0.2,0.2,-0.2),
				new Point3d(0.2,0.2,-0.2),
				new Point3d(0.2,-0.2,-0.2)
			},
			{
				new Point3d(-0.2,-0.2,0.2),
				new Point3d(-0.2,0.2,0.2),
				new Point3d(0.2,0.2,0.2),
				new Point3d(0.2,-0.2,0.2)
			},
			{
				new Point3d(0,0,0.2),
				new Point3d(0,0,0.2),
				new Point3d(0,0,0.2),
				new Point3d(0,0,0.2)
			}
		};
		ctrlPoints=new Point3d[][]
		{
			{
				new Point3d(-0.2,-0.2,0),
				new Point3d(0.2,-0.2,0),
				new Point3d(0.2,0.2,0),
				new Point3d(-0.2,0.2,0)
			},
			{
				new Point3d(0,-0.2,-0.2),
				new Point3d(0,-0.2,0.2),
				new Point3d(0,0.2,0.2),
				new Point3d(0,0.2,-0.2)
			},
			{
				new Point3d(0.2,-0.2,0),
				new Point3d(-0.2,-0.2,0),
				new Point3d(-0.2,0.2,0),
				new Point3d(0.2,0.2,0)
			},
			{
				new Point3d(0,-0.2,0.2),
				new Point3d(0,-0.2,-0.2),
				new Point3d(0,0.2,-0.2),
				new Point3d(0,0.2,0.2)
			}
		};
		ctrlPoints=new Point3d[][]
		{
			{
				new Point3d(-0.4,-0.1,0),
				new Point3d(-0.2,-0.1,0),
				new Point3d(-0.2,0.1,0),
				new Point3d(-0.4,0.1,0)
			},
			{
				new Point3d(0,-0.1,-0.4),
				new Point3d(0,-0.1,-0.2),
				new Point3d(0,0.1,-0.2),
				new Point3d(0,0.1,-0.4)
			},
			{
				new Point3d(0.4,-0.1,0),
				new Point3d(0.2,-0.1,0),
				new Point3d(0.2,0.1,0),
				new Point3d(0.4,0.1,0)
			},
			{
				new Point3d(0,-0.1,0.4),
				new Point3d(0,-0.1,0.2),
				new Point3d(0,0.1,0.2),
				new Point3d(0,0.1,0.4)
			}
		};
		double[][] weights=
		{
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1}
		};
		int uOrder=4,vOrder=5;
		NurbsTorus3D NurbsTorus3D=new NurbsTorus3D(ctrlPoints,weights,uOrder,vOrder);
		NurbsTorus3D.setAppearance(Appearance1);
		TransformGroup1.addChild(NurbsTorus3D);
		TransformGroup1.addChild(new Mesh3D(ctrlPoints));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class NurbsTorus3D extends Shape3D
{
	public NurbsTorus3D(Point3d[][] ctrlPoints,double[][] weights,int uOrder,int vOrder)
	{
		double[] uKnots=getStandardUniformNurbsKnots(ctrlPoints.length,uOrder);
		double[] vKnots=getStandardUniformNurbsKnots(ctrlPoints[0].length,vOrder);
		int r=100,c=100,v=0,m=ctrlPoints.length,n=ctrlPoints[0].length;
		double[][] U=this.getParameterMatrix(uKnots,r,uOrder,m);
		double[][] V=this.getParameterMatrix(vKnots,c,vOrder,n);
		Point3d[] coordinates=new Point3d[r*c];
		int[] coordinateIndices=new int[(r-1)*(c-1)*4];
		for(int i=0;i<r;i++)for(int j=0;j<c;j++)coordinates[i*c+j]=this.getCoordinate(ctrlPoints,weights,U,V,i,j,uOrder,vOrder);
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
	private Point3d getCoordinate(Point3d[][] ctrlPoints,double[][] weights,double[][] U,double[][] V,int u,int v,int uOrder,int vOrder)
	{
		int m=ctrlPoints.length;
		int n=ctrlPoints[0].length;
		double x=0,y=0,z=0,w=0;
		for(int i=0;i<m+uOrder;i++)
		{
			for(int j=0;j<n+vOrder;j++)
			{
				x+=ctrlPoints[i%m][j%n].x*U[u][i]*V[v][j]*weights[i%m][j%n];
				y+=ctrlPoints[i%m][j%n].y*U[u][i]*V[v][j]*weights[i%m][j%n];
				z+=ctrlPoints[i%m][j%n].z*U[u][i]*V[v][j]*weights[i%m][j%n];
				w+=U[u][i]*V[v][j]*weights[i%m][j%n];
			}
		}
		return new Point3d(x/w,y/w,z/w);
	}
	private double[][] getParameterMatrix(double[] knots,int curvePointsLength,int order,int ctrlPointsLength)
	{
		double[] u=knots;
		int l=curvePointsLength;
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