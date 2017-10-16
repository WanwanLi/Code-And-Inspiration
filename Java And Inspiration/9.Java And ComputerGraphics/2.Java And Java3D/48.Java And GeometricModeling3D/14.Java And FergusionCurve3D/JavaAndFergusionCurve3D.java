import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndFergusionCurve3D
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
		Point3d ctrlPoint0=new Point3d(-0.6,0.1,0.1);
		Point3d ctrlPoint1=new Point3d(0.6,0.0,-0.1);
		Point3d ctrlPoint2=new Point3d(0.0,0.5,0.0);
		double k0=3.0,k1=2.0;
		double dx0=ctrlPoint2.x-ctrlPoint0.x;
		double dy0=ctrlPoint2.y-ctrlPoint0.y;
		double dz0=ctrlPoint2.z-ctrlPoint0.z;
		Vector3d ctrlVector0=new Vector3d(k0*dx0,k0*dy0,k0*dz0);
		double dx1=ctrlPoint1.x-ctrlPoint2.x;
		double dy1=ctrlPoint1.y-ctrlPoint2.y;
		double dz1=ctrlPoint1.z-ctrlPoint2.z;
		Vector3d ctrlVector1=new Vector3d(k1*dx1,k1*dy1,k1*dz1);
		Point3d[] ctrlPoints={ctrlPoint0,ctrlPoint2,ctrlPoint1};
		int[] stripCounts=new int[]{ctrlPoints.length};
		LineStripArray LineStripArray1=new LineStripArray(ctrlPoints.length,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,ctrlPoints);
		Shape3D ctrlCurve3D=new Shape3D();
		ctrlCurve3D.setGeometry(LineStripArray1);
		TransformGroup1.addChild(ctrlCurve3D);
		FergusionCurve3D FergusionCurve3D=new FergusionCurve3D(ctrlPoint0,ctrlPoint1,ctrlVector0,ctrlVector1);
		TransformGroup1.addChild(FergusionCurve3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class FergusionCurve3D extends Shape3D
{
	public FergusionCurve3D(Point3d ctrlPoint0,Point3d ctrlPoint1,Vector3d ctrlVector0,Vector3d ctrlVector1)
	{
		int l=100;double dt=1.0/(l-1);
		Point3d[] coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoint0,ctrlPoint1,ctrlVector0,ctrlVector1,i*dt);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
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
	private Point3d getCoordinate(Point3d ctrlPoint0,Point3d ctrlPoint1,Vector3d ctrlVector0,Vector3d ctrlVector1,double t)
	{
		double x=0,y=0,z=0;
		x+=F0(ctrlPoint0,t).x;
		y+=F0(ctrlPoint0,t).y;
		z+=F0(ctrlPoint0,t).z;

		x+=F1(ctrlPoint1,t).x;
		y+=F1(ctrlPoint1,t).y;
		z+=F1(ctrlPoint1,t).z;

		x+=G0(ctrlVector0,t).x;
		y+=G0(ctrlVector0,t).y;
		z+=G0(ctrlVector0,t).z;

		x+=G1(ctrlVector1,t).x;
		y+=G1(ctrlVector1,t).y;
		z+=G1(ctrlVector1,t).z;
		return new Point3d(x,y,z);
	}
}
