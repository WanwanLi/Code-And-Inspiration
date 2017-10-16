import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndBezierSurface3D
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
		Point3d[][] ctrlPoints=new Point3d[][]
		{
			{
				new Point3d(-0.8,-0.6,-0.5),
				new Point3d(-0.2,0.2,-0.5),
				new Point3d(0.2,0.3,-0.5),
				new Point3d(0.8,-0.5,-0.5)
			},
			{
				new Point3d(-0.8,-0.1,-0.2),
				new Point3d(-0.2,0.4,-0.2),
				new Point3d(0.2,0.4,-0.2),
				new Point3d(0.8,-0.1,-0.2)
			},
			{
				new Point3d(-0.8,-0.1,0.2),
				new Point3d(-0.2,0.4,0.2),
				new Point3d(0.2,0.4,0.2),
				new Point3d(0.8,-0.1,0.2)
			},
			{
				new Point3d(-0.8,-0.6,0.5),
				new Point3d(-0.2,0.2,0.5),
				new Point3d(0.2,0.3,0.5),
				new Point3d(0.8,-0.5,0.5)
			}
		};
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance1.setMaterial(Material1);
		BezierSurface3D bezierSurface3D=new BezierSurface3D(ctrlPoints);
		bezierSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(bezierSurface3D);
		TransformGroup1.addChild(new Mesh3D(ctrlPoints));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class BezierSurface3D extends Shape3D
{
	public BezierSurface3D(Point3d[][] ctrlPoints)
	{
		int r=100,c=100,v=0;
		double du=1.0/(r-1),dv=1.0/(c-1);
		int m=ctrlPoints.length;
		int n=ctrlPoints[0].length;
		Point3d[][] newCtrlPoints=new Point3d[n][m];
		for(int i=0;i<n;i++)for(int j=0;j<m;j++)newCtrlPoints[i][j]=ctrlPoints[j][i];
		int[][] C=this.getCombination(max(m,n));
		Point3d[] coordinates=new Point3d[r*c];
		Point3d[] ctrlCoordinates=new Point3d[n];
		int[] coordinateIndices=new int[(r-1)*(c-1)*4];
		for(int i=0;i<r;i++)
		{
			for(int k=0;k<n;k++)ctrlCoordinates[k]=getCoordinate(newCtrlPoints[k],C,m,i*du);
			for(int j=0;j<c;j++)coordinates[i*c+j]=this.getCoordinate(ctrlCoordinates,C,n,j*dv);
		}
/*
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				coordinates[i*c+j]=this.getCoordinate(ctrlPoints,C,m,n,i*du,j*dv);
			}
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
	private Point3d getBezierCoordinate(Point3d[] ctrlPoints,int[][] C,double[][] B,int k)
	{
		int n=ctrlPoints.length;
		int l=B.length;
		double x=0,y=0,z=0;
		for(int i=0;i<n;i++)
		{
			double b=C[n-1][i]*B[k][i]*B[l-1-k][n-1-i];
			x+=ctrlPoints[i].x*b;
			y+=ctrlPoints[i].y*b;
			z+=ctrlPoints[i].z*b;
		}
		return new Point3d(x,y,z);
	}
	private double Bernstein(int[][] C,int n,int i,double u)
	{
		return C[n-1][i]*Math.pow(u,i)*Math.pow(1-u,n-1-i);
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,int[][] C,int n,double u)
	{
		double x=0,y=0,z=0;
		for(int i=0;i<n;i++)
		{
			double b=this.Bernstein(C,n,i,u);
			x+=ctrlPoints[i].x*b;
			y+=ctrlPoints[i].y*b;
			z+=ctrlPoints[i].z*b;
		}
		return new Point3d(x,y,z);
	}
	private Point3d getCoordinate(Point3d[][] ctrlPoints,int[][] C,int m,int n,double u,double v)
	{
		double x=0,y=0,z=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double b=this.Bernstein(C,m,i,u)*this.Bernstein(C,n,j,v);
				x+=ctrlPoints[i][j].x*b;
				y+=ctrlPoints[i][j].y*b;
				z+=ctrlPoints[i][j].z*b;
			}
		}
		return new Point3d(x,y,z);
	}
	private int[][] getCombination(int n)
	{
		int[][] c=new int[n][n];
		for(int i=0;i<n;i++)
		{
			c[i][0]=1;
			c[i][i]=1;
		}
		for(int i=2;i<n;i++)
		{
			for(int j=1;j<i;j++)
			{
				c[i][j]=c[i-1][j-1]+c[i-1][j];
			}
		}
		return c;
	}
	private double[][] getParameterMatrix(int l,int n)
	{
		double d=1.0/(l-1);
		double[][] u=new double[l][n];
		for(int i=0;i<l;i++)
		{
			for(int j=0;j<n;j++)
			{
				u[i][j]=Math.pow(i*d,j);
			}
		}
		return u;
	}
	private int max(int a,int b)
	{
		return a>=b?a:b;
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
class Combination
{
	public int[][] C;
	private int n;
	public Combination(int n)
	{
		this.n=n;
		this.C=new int[n+1][n+1];
		for(int i=0;i<=n;i++)
		{
			this.C[i][0]=1;
			this.C[i][i]=1;
		}
		for(int i=2;i<=n;i++)
		{
			for(int j=1;j<i;j++)
			{
				this.C[i][j]=C[i-1][j-1]+C[i-1][j];
			}
		}
	}
	public void println()
	{
		for(int i=0;i<=n;i++)
		{
			for(int j=0;j<=i;j++)
			{
				System.out.print(C[i][j]+"\t");
			}
			System.out.println();
		}
	}
}