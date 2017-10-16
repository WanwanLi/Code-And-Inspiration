import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndNurbsCircle3D
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
			new Point3d(-0.2,-0.2,0.1),
			new Point3d(-0.2,0.2,0.1),
			new Point3d(0.0,0.3,0.1),
			new Point3d(0.2,0.2,0.1),
			new Point3d(0.2,-0.2,0.1),
			new Point3d(0.0,-0.3,0.1)
		};
		double[] weights=
		{
			1,
			1,
			1,
			1,
			1,
			1
		};
		ctrlPoints=new Point3d[]
		{
			new Point3d(-0.2,-0.2,0.1),
			new Point3d(-0.2,0.2,0.1),
			new Point3d(0.2,0.2,0.1),
			new Point3d(0.2,-0.2,0.1)
		};
		weights=new double[]
		{
			1,
			1,
			1,
			1,
			1,
			1
		};
		int order=3;int[] stripCounts=new int[]{ctrlPoints.length+1};
		LineStripArray LineStripArray1=new LineStripArray(ctrlPoints.length+1,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,ctrlPoints);
		LineStripArray1.setCoordinate(ctrlPoints.length,ctrlPoints[0]);
		Shape3D ctrlCircle3D=new Shape3D();
		ctrlCircle3D.setGeometry(LineStripArray1);
		TransformGroup1.addChild(ctrlCircle3D);
		NurbsCircle3D nurbsCircle3D=new NurbsCircle3D(ctrlPoints,weights,order);
		TransformGroup1.addChild(nurbsCircle3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class NurbsCircle3D extends Shape3D
{
	public NurbsCircle3D(Point3d[] ctrlPoints,double[] weights,int order)
	{
		int l=100,m=order,n=ctrlPoints.length;
		double du=1.0/(l-1);
		double[] knots=getStandardUniformNurbsKnots(n,m);
		double[][] B=this.getParameterMatrix(knots,l,m,n);
		Point3d[] coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,weights,B,i,m);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
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
}
