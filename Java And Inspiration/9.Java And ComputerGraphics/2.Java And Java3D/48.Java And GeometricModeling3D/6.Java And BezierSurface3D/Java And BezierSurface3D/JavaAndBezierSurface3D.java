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
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
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
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class BezierSurface3D extends Shape3D
{
	private int[][] C;
	private int p=10;
	public BezierSurface3D(Point3d[][] ctrlPoints)
	{
		int m=ctrlPoints.length;
		int n=ctrlPoints[0].length;
		int r=m*p,c=n*p,v=0;
		double du=1.0/(r-1),dv=1.0/(c-1);
		this.C=(new Combination(max(m,n))).C;
		Point3d[] surfacePoints=new Point3d[r*c];
		int[] surfacePointIndices=new int[(r-1)*(c-1)*4];
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				surfacePoints[i*c+j]=this.getSurfacePoint3d(ctrlPoints,m,n,i*du,j*dv);

			}
		}
		for(int i=0;i<r-1;i++)
		{
			for(int j=0;j<c-1;j++)
			{
				surfacePointIndices[v++]=(i+0)*c+(j+0);
				surfacePointIndices[v++]=(i+1)*c+(j+0);
				surfacePointIndices[v++]=(i+1)*c+(j+1);
				surfacePointIndices[v++]=(i+0)*c+(j+1);
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(surfacePoints.length,IndexedQuadArray.COORDINATES|IndexedQuadArray.NORMALS,surfacePointIndices.length);
		IndexedQuadArray1.setCoordinates(0,surfacePoints);
		IndexedQuadArray1.setCoordinateIndices(0,surfacePointIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	private int max(int a,int b)
	{
		return a>=b?a:b;
	}
	private double Bernstein(int i,int n,double u)
	{
		return this.C[n][i]*Math.pow(u,i)*Math.pow(1-u,n-i);
	}
	private Point3d getSurfacePoint3d(Point3d[][] ctrlPoints,int m,int n,double u,double v)
	{
		double x=0,y=0,z=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double b=this.Bernstein(i,m-1,u)*this.Bernstein(j,n-1,v);
				x+=ctrlPoints[i][j].x*b;
				y+=ctrlPoints[i][j].y*b;
				z+=ctrlPoints[i][j].z*b;
			}
		}
		return new Point3d(x,y,z);
	}
}
class Matrix
{
	public float[] getBezierCoordinates(int m,float[] U,float[][] B,float[][] P)
	{
		float[] coordinates=new float[m+1];
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)coordinates[j]+=U[i]*B[i][j];
		for(int j=0;j<m+1;j++){U[j]=coordinates[j];coordinates[j]=0;}
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)coordinates[j]+=U[i]*P[i][j];
		return coordinates;
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