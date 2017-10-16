import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndLagrangeCurve3D
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
		Point3d[] ctrlPoints=
		{
			new Point3d(-0.6,0.1,0.1),
			new Point3d(-0.4,0.25,0.1),
			new Point3d(-0.1,0.25,0.1),
			new Point3d(0.3,-0.25,0.1),
			new Point3d(0.5,-0.2,0.1),
			new Point3d(0.65,0.3,0.1)
		};
		double[] knots=LagrangeCurve3D.getUniformKnots(ctrlPoints.length);
//		knots=LagrangeCurve3D.getRiesenfeldKnots(ctrlPoints);

	/*	ctrlPoints=new Point3d[]
		{
			new Point3d(-0.1,-0.25,0.1),
			new Point3d(-0.25,-0.15,0.1),
			new Point3d(-0.2,0.15,0.1),
			new Point3d(0.0,0.25,0.1),
			new Point3d(0.4,0.11,0.1),
			new Point3d(0.3,-0.15,0.1),
			new Point3d(0.1,-0.18,0.1)
		};
	*/
		knots=LagrangeCurve3D.getUniformKnots(ctrlPoints.length);
		int[] stripCounts=new int[]{ctrlPoints.length};
		LineStripArray LineStripArray1=new LineStripArray(ctrlPoints.length,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,ctrlPoints);
		Shape3D ctrlCurve3D=new Shape3D();
		ctrlCurve3D.setGeometry(LineStripArray1);
		TransformGroup1.addChild(ctrlCurve3D);
		LagrangeCurve3D LagrangeCurve3D=new LagrangeCurve3D(ctrlPoints,knots);
		TransformGroup1.addChild(LagrangeCurve3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class LagrangeCurve3D extends Shape3D
{
	public LagrangeCurve3D(Point3d[] ctrlPoints,double[] knots)
	{
		int l=100,n=ctrlPoints.length;
		double[][] L=this.getParameterMatrix(knots,l,n);
		Point3d[] coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,L,i);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[][] L,int k)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0;
		for(int i=0;i<n;i++)
		{
			x+=ctrlPoints[i].x*L[k][i];
			y+=ctrlPoints[i].y*L[k][i];
			z+=ctrlPoints[i].z*L[k][i];
		}
		return new Point3d(x,y,z);
	}
	private double[][] getParameterMatrix(double[] knots,int curvePointsLength,int ctrlPointsLength)
	{
		double[] u=knots;
		int l=curvePointsLength;
		int n=ctrlPointsLength;
		double d=1.0/(l-1);
		double[][] L=new double[l][n];
		for(int k=0;k<l;k++)
		{
			double t=k*d;
			for(int i=0;i<n;i++)
			{
				L[k][i]=1.0;
				for(int j=0;j<n;j++)
				{
					L[k][i]*=i==j?1:(t-u[j])/(u[i]-u[j]);
				}
			}
		}
		return L;
	}
	public static double[] getUniformKnots(int ctrlPointsLength)
	{
		int l=ctrlPointsLength;
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
	public  static double[] getRiesenfeldKnots(Point3d[] ctrlPoints)
	{
		int l=ctrlPoints.length;
		double[] knots=new double[l];
		double[] u=new double[l];u[0]=0;
		for(int i=1;i<l;i++)u[i]=u[i-1]+distance(ctrlPoints[i-1],ctrlPoints[i]);
		for(int i=0;i<l;i++)knots[i]=u[i]/u[l-1];
		return knots;
	}
}
