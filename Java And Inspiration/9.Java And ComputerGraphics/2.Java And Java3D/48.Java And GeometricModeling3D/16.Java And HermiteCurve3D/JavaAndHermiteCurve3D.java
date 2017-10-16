import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndHermiteCurve3D
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

		/*Transform3D Transform=new Transform3D();
		Transform.setScale(new Vector3d(0.001,0.001,1));
		TransformGroup1.setTransform(Transform);
		Point3d[] ctrlPoints=
		{
			 new Point3d(100,200,0),
			 new Point3d(400, 100, 0),
			 new Point3d(750, 420, 0),
			 new Point3d(950, 150, 0)
		};*/
		double[] knots=HermiteCurve3D.getUniformKnots(ctrlPoints.length);
//		knots=HermiteCurve3D.getRiesenfeldKnots(ctrlPoints);
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
*/	
		knots=HermiteCurve3D.getUniformKnots(ctrlPoints.length);
		int[] stripCounts=new int[]{ctrlPoints.length};
		LineStripArray LineStripArray1=new LineStripArray(ctrlPoints.length,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,ctrlPoints);
		Shape3D ctrlCurve3D=new Shape3D();
		ctrlCurve3D.setGeometry(LineStripArray1);
		TransformGroup1.addChild(ctrlCurve3D);
		HermiteCurve3D hermiteCurve3D=new HermiteCurve3D(ctrlPoints,knots);
		TransformGroup1.addChild(hermiteCurve3D);
	//	TransformGroup1.addChild(hermiteCurve3D.getHermiteVectors3D(ctrlPoints,knots));
		BranchGroup1.compile();
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class HermiteCurve3D extends Shape3D
{
	public HermiteCurve3D(Point3d[] ctrlPoints,double[] knots)
	{
		int l=100;
		Vector3d[] ctrlVectors=getCtrlVectors(ctrlPoints,knots);
		Point3d[] coordinates=getCoordinates(ctrlPoints,ctrlVectors,knots,l);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
	}
	public Shape3D getHermiteVectors3D(Point3d[] ctrlPoints,double[] knots)
	{
		int n=ctrlPoints.length,l=2*n;
		Point3d[] coordinates=new Point3d[l];
		Vector3d[] ctrlVectors=getCtrlVectors(ctrlPoints,knots);
		int[] stripCounts=new int[n];
		for(int i=0;i<n;i++)
		{
			coordinates[i*2+0]=ctrlPoints[i];
			double x=ctrlPoints[i].x+ctrlVectors[i].x;
			double y=ctrlPoints[i].y+ctrlVectors[i].y;
			double z=ctrlPoints[i].z+ctrlVectors[i].z;
			coordinates[i*2+1]=new Point3d(x,y,z);
			stripCounts[i]=2;
		}
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(LineStripArray1);
		return shape3D;
	}
	private Point3d F0(Point3d ctrlPoint0,double t)
	{
		double k=1-3*t*t+2*t*t*t;
		double x=ctrlPoint0.x*k;
		double y=ctrlPoint0.y*k;
		double z=ctrlPoint0.z*k;
		return new Point3d(x,y,z);
	}
	private Point3d F1(Point3d ctrlPoint1,double t)
	{
		double k=3*t*t-2*t*t*t;
		double x=ctrlPoint1.x*k;
		double y=ctrlPoint1.y*k;
		double z=ctrlPoint1.z*k;
		return new Point3d(x,y,z);
	}
	private Vector3d G0(Vector3d ctrlVector0,double t)
	{
		double k=t-2*t*t+t*t*t;
		double x=ctrlVector0.x*k;
		double y=ctrlVector0.y*k;
		double z=ctrlVector0.z*k;
		return new Vector3d(x,y,z);
	}
	private Vector3d G1(Vector3d ctrlVector1,double t)
	{
		double k=-t*t+t*t*t;
		double x=ctrlVector1.x*k;
		double y=ctrlVector1.y*k;
		double z=ctrlVector1.z*k;
		return new Vector3d(x,y,z);
	}
	private Point3d getCoordinate(Point3d ctrlPoint0,Point3d ctrlPoint1,Vector3d ctrlVector0,Vector3d ctrlVector1,double d,double t)
	{
		double x=0,y=0,z=0;
		x+=F0(ctrlPoint0,t).x;
		y+=F0(ctrlPoint0,t).y;
		z+=F0(ctrlPoint0,t).z;

		x+=F1(ctrlPoint1,t).x;
		y+=F1(ctrlPoint1,t).y;
		z+=F1(ctrlPoint1,t).z;

		x+=d*G0(ctrlVector0,t).x;
		y+=d*G0(ctrlVector0,t).y;
		z+=d*G0(ctrlVector0,t).z;

		x+=d*G1(ctrlVector1,t).x;
		y+=d*G1(ctrlVector1,t).y;
		z+=d*G1(ctrlVector1,t).z;
		return new Point3d(x,y,z);
	}
	private Point3d D(Point3d[] ctrlPoints,int i)
	{
		double dx=ctrlPoints[i+1].x-ctrlPoints[i].x;
		double dy=ctrlPoints[i+1].y-ctrlPoints[i].y;
		double dz=ctrlPoints[i+1].z-ctrlPoints[i].z;
		return new Point3d(dx,dy,dz);
	}
	private double d(double[] knots,int i)
	{
		return knots[i+1]-knots[i];
	}
	private int getIndex(double u,double[] knots)
	{
		int i=0,l=knots.length;
		for(i=0;i<l-1&&knots[i]<=u;i++);
		return i-1;
	}
	private Vector3d getBessellCtrlVector(Point3d[] ctrlPoints,double[] knots,int i)
	{
		int n=ctrlPoints.length;
		if(i==0)
		{
			Vector3d v=getBessellCtrlVector(ctrlPoints,knots,1);
			double d=d(knots,0);
			Point3d p=D(ctrlPoints,0);
			double x=2.0*p.x/d-v.x;
			double y=2.0*p.y/d-v.y;
			double z=2.0*p.z/d-v.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			if(r==0)return new Vector3d(0,1,0);
			return new Vector3d(x,y,z);
		}
		else if(i==n-1)
		{
			Vector3d v=getBessellCtrlVector(ctrlPoints,knots,n-2);
			double d=d(knots,n-2);
			Point3d p=D(ctrlPoints,n-2);
			double x=2.0*p.x/d-v.x;
			double y=2.0*p.y/d-v.y;
			double z=2.0*p.z/d-v.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			if(r==0)return new Vector3d(0,1,0);
			return new Vector3d(x,y,z);
		}
		else
		{
			double d0=d(knots,i-1);
			double d1=d(knots,i);
			Point3d p0=D(ctrlPoints,i-1);
			Point3d p1=D(ctrlPoints,i);
			double k0=d1/(d0+d1)/d0;
			double k1=d0/(d0+d1)/d1;
			double x=k0*p0.x+k1*p1.x;
			double y=k0*p0.y+k1*p1.y;
			double z=k0*p0.z+k1*p1.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			if(r==0)return new Vector3d(0,1,0);
			return new Vector3d(x,y,z);
		}
	}
	private Vector3d[] getCtrlVectors(Point3d[] ctrlPoints,double[] knots)
	{
		int l=ctrlPoints.length;
		Vector3d[] ctrlVectors=new Vector3d[l];
		for(int i=0;i<l;i++)ctrlVectors[i]=getBessellCtrlVector(ctrlPoints,knots,i);
		return ctrlVectors;
	}
	private Point3d[] getCoordinates1(Point3d[] ctrlPoints,Vector3d[] ctrlVectors,double[] knots,int curvePointsLength)
	{
		int l=ctrlPoints.length,c=0;
		Point3d[] coordinates=new Point3d[curvePointsLength];
		double[] u=knots;
		double d,t=0.0,dt=1.0/(curvePointsLength-1);
		for(int i=0;i<l-1;i++)
		{
			Point3d p0=ctrlPoints[i+0];
			Point3d p1=ctrlPoints[i+1];
			Vector3d v0=ctrlVectors[i+0];
			Vector3d v1=ctrlVectors[i+1];
			for(;t<u[i+1]&&c<curvePointsLength;t+=dt,c++)
			{
				d=u[i+1]-u[i];
				coordinates[c]=getCoordinate(p0,p1,v0,v1,d,(t-u[i])/d);
			}
		}
		return coordinates;
	}
	private Point3d[] getCoordinates(Point3d[] ctrlPoints,Vector3d[] ctrlVectors,double[] knots,int curvePointsLength)
	{
		int l=curvePointsLength;
		Point3d[] coordinates=new Point3d[l];
		double[] u=knots;
		double dt=1.0/(l-1);
		for(int i=0;i<l;i++)
		{
			double t=i*dt;
			int k=getIndex(t,u);
			Point3d p0=ctrlPoints[k+0];
			Point3d p1=ctrlPoints[k+1];
			Vector3d v0=ctrlVectors[k+0];
			Vector3d v1=ctrlVectors[k+1];
			coordinates[i]=getCoordinate(p0,p1,v0,v1,d(u,k),(t-u[k])/d(u,k));
		}
		return coordinates;
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
