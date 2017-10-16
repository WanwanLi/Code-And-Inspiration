import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndBezierCurve3D
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
		Point3d[] ctrlPoints=new Point3d[]
		{
			new Point3d(-0.8,-0.6,0.1),
			new Point3d(-0.2,0.2,0.1),
			new Point3d(0.2,0.3,0.1),
			new Point3d(0.8,-0.5,0.1)
		};
		int[] stripCounts=new int[]{ctrlPoints.length};
		LineStripArray LineStripArray1=new LineStripArray(ctrlPoints.length,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,ctrlPoints);
		Shape3D ctrlCurve3D=new Shape3D();
		ctrlCurve3D.setGeometry(LineStripArray1);
		TransformGroup1.addChild(ctrlCurve3D);
		BezierCurve3D bezierCurve3D=new BezierCurve3D(ctrlPoints);
		TransformGroup1.addChild(bezierCurve3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class BezierCurve3D extends Shape3D
{
	private int[][] C;
	private int p=10;
	public BezierCurve3D(Point3d[] ctrlPoints)
	{
		int n=ctrlPoints.length;
		int l=n*p;
		double du=1.0/(l-1);
		this.C=(new Combination(n)).C;
		Point3d[] curvePoints=new Point3d[l];
		for(int i=0;i<l;i++)curvePoints[i]=this.getCurvePoint3d(ctrlPoints,n,i*du);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,curvePoints);
		this.setGeometry(LineStripArray1);
	}
	private double Bernstein(int i,int n,double u)
	{
		return this.C[n][i]*Math.pow(u,i)*Math.pow(1-u,n-i);
	}
	private Point3d getCurvePoint3d(Point3d[] ctrlPoints,int n,double u)
	{
		double x=0,y=0,z=0;
		for(int i=0;i<n;i++)
		{
			double b=this.Bernstein(i,n-1,u);
			x+=ctrlPoints[i].x*b;
			y+=ctrlPoints[i].y*b;
			z+=ctrlPoints[i].z*b;
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