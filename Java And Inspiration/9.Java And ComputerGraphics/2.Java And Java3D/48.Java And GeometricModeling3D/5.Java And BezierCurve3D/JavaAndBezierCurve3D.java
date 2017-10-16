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
	public BezierCurve3D(Point3d[] ctrlPoints)
	{
		int n=ctrlPoints.length;
		int l=100;
		double du=1.0/(l-1);
		int[][] C=this.getCombination(n);
		double[][] B=this.getParameterMatrix(l,n);
		Point3d[] coordinates=new Point3d[l];
//		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,C,B,i);
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,C,i*du);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,int[][] C,double[][] B,int k)
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
	private Point3d getCoordinate(Point3d[] ctrlPoints,int[][] C,double u)
	{
		int n=C.length;
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
