import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndNurbsCurve3D
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
		double[] weights=
		{
			1,
			2,
			1,
			1,
			3,
			1,
		};
	//	int order=1;double[] knots={0.0,0.0,0.2,0.4,0.6,0.8,1.0,1.0};
	//	int order=2;double[] knots={0.0,0.0,0.0,0.25,0.5,0.75,1.0,1.0,1.0};
	//	int order=3;double[] knots={0.0,0.0,0.0,0.0,0.33,0.66,1.0,1.0,1.0,1.0};
	//	int order=4;double[] knots={0.0,0.0,0.0,0.0,0.0,0.5,1.0,1.0,1.0,1.0,1.0};
	//	int order=5;double[] knots={0.0,0.0,0.0,0.0,0.0,0.0,1.0,1.0,1.0,1.0,1.0,1.0};
		int order=2;double[] knots=NurbsCurve3D.getBezierUniformNurbsKnots(ctrlPoints.length,order);
	//	int order=2;double[] knots=NurbsCurve3D.getRiesenfeldNurbsKnots(ctrlPoints,order);
	//	int order=3;double[] knots=NurbsCurve3D.getStandardUniformNurbsKnots(ctrlPoints.length,order);
	//	int order=3;double[] knots=NurbsCurve3D.getSemiUniformNurbsKnots(ctrlPoints.length,order);
/*
		ctrlPoints=new Point3d[]
		{
			new Point3d(-0.1,-0.25,0.1),
			new Point3d(-0.25,-0.15,0.1),
			new Point3d(-0.2,0.15,0.1),
			new Point3d(0.0,0.25,0.1),
			new Point3d(0.4,0.11,0.1),
			new Point3d(0.3,-0.15,0.1),
			new Point3d(0.1,-0.18,0.1)
		};
		int order=3;double[] knots=NurbsCurve3D.getSemiUniformNurbsKnots(ctrlPoints.length,order);
*/

		int[] stripCounts=new int[]{ctrlPoints.length};
		LineStripArray LineStripArray1=new LineStripArray(ctrlPoints.length,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,ctrlPoints);
		Shape3D ctrlCurve3D=new Shape3D();
		ctrlCurve3D.setGeometry(LineStripArray1);
		TransformGroup1.addChild(ctrlCurve3D);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(1.5f);
		ColoringAttributes ColoringAttributes1=new ColoringAttributes();
		ColoringAttributes1.setColor(new Color3f(0,1,1));
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		Appearance1.setColoringAttributes(ColoringAttributes1);
		NurbsCurve3D nurbsCurve3D=new NurbsCurve3D(ctrlPoints,weights,knots,order,Appearance1);
		TransformGroup1.addChild(nurbsCurve3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class NurbsCurve3D extends Shape3D
{
	public NurbsCurve3D(Point3d[] ctrlPoints,double[] weights,double[] knots,int order,Appearance appearance)
	{
		int l=100,m=order,n=ctrlPoints.length;
		double du=1.0/(l-1);
		double[][] B=this.getParameterMatrix(knots,l,m,n);
		Point3d[] coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,weights,B,i);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
		this.setAppearance(appearance);
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[] weights,double[][] B,int k)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0,w=0;
		for(int i=0;i<n;i++)
		{
			x+=ctrlPoints[i].x*B[k][i]*weights[i];
			y+=ctrlPoints[i].y*B[k][i]*weights[i];
			z+=ctrlPoints[i].z*B[k][i]*weights[i];
			w+=B[k][i]*weights[i];
		}
		return new Point3d(x/w,y/w,z/w);
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
	public static double[] getStandardUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		double du=1.0/(l-1);
		for(int i=0;i<l;i++)knots[i]=i*du;
		return knots;
	}
	public static double[] getBezierUniformNurbsKnots(int ctrlPointsLength,int order)
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
	public static double[] getSemiUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*(order+1);
		for(int i=0;i<=order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=0.5;
		for(int i=0;i<=order;i++)knots[c++]=1;
		return knots;
	}
	public  static double distance(Point3d point0,Point3d point1)
	{
		double dx=point1.x-point0.x;
		double dy=point1.y-point0.y;
		double dz=point1.z-point0.z;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
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
}
