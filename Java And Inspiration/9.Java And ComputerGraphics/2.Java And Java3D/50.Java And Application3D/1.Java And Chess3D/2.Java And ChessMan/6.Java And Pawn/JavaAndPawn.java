import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndPawn
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		TransformGroup1.addChild(new Pawn());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
}
class Pawn extends TransformGroup
{
	public Pawn()
	{
		float unit=0.05f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,2.055,0),
                                        new Point3d(0.1499999999999999,2.05,0),
                                        new Point3d(0.15500000000000003,2.045,0),
                                        new Point3d(0.24499999999999988,2.025,0),
                                        new Point3d(0.25,2.02,0),
                                        new Point3d(0.28,2.01,0),
                                        new Point3d(0.2849999999999999,2.005,0),
                                        new Point3d(0.33000000000000007,1.98,0),
                                        new Point3d(0.375,1.935,0),
                                        new Point3d(0.395,1.8900000000000001,0),
                                        new Point3d(0.405,1.855,0),
                                        new Point3d(0.41500000000000004,1.755,0),
                                        new Point3d(0.4099999999999999,1.75,0),
                                        new Point3d(0.405,1.675,0),
                                        new Point3d(0.3999999999999999,1.67,0),
                                        new Point3d(0.405,1.665,0),
                                        new Point3d(0.3999999999999999,1.6600000000000001,0),
                                        new Point3d(0.385,1.615,0),
                                        new Point3d(0.3799999999999999,1.6099999999999999,0),
                                        new Point3d(0.3600000000000001,1.57,0),
                                        new Point3d(0.345,1.555,0),
                                        new Point3d(0.345,1.55,0),
                                        new Point3d(0.21500000000000008,1.49,0),
                                        new Point3d(0.20999999999999996,1.485,0),
                                        new Point3d(0.2749999999999999,1.44,0),
                                        new Point3d(0.28,1.435,0),
                                        new Point3d(0.31499999999999995,1.35,0),
                                        new Point3d(0.31000000000000005,1.345,0),
                                        new Point3d(0.30000000000000004,1.3,0),
                                        new Point3d(0.15500000000000003,1.3,0),
                                        new Point3d(0.1499999999999999,1.295,0),
                                        new Point3d(0.1399999999999999,1.22,0),
                                        new Point3d(0.15500000000000003,1.05,0),
                                        new Point3d(0.15999999999999992,1.045,0),
                                        new Point3d(0.17500000000000004,0.9450000000000001,0),
                                        new Point3d(0.17999999999999994,0.94,0),
                                        new Point3d(0.19999999999999996,0.8600000000000001,0),
                                        new Point3d(0.20500000000000007,0.855,0),
                                        new Point3d(0.22999999999999998,0.7849999999999999,0),
                                        new Point3d(0.2350000000000001,0.78,0),
                                        new Point3d(0.27,0.7150000000000001,0),
                                        new Point3d(0.2749999999999999,0.71,0),
                                        new Point3d(0.385,0.5100000000000002,0),
                                        new Point3d(0.375,0.45500000000000007,0),
                                        new Point3d(0.31000000000000005,0.3500000000000001,0),
                                        new Point3d(0.31000000000000005,0.33000000000000007,0),
                                        new Point3d(0.345,0.31000000000000005,0),
                                        new Point3d(0.4650000000000001,0.28500000000000014,0),
                                        new Point3d(0.47,0.2799999999999998,0),
                                        new Point3d(0.55,0.2400000000000002,0),
                                        new Point3d(0.5549999999999999,0.23499999999999988,0),
                                        new Point3d(0.5900000000000001,0.20500000000000007,0),
                                        new Point3d(0.6000000000000001,0.16999999999999993,0),
                                        new Point3d(0.595,0.10499999999999998,0),
                                        new Point3d(0.5900000000000001,0.10000000000000009,0),
                                        new Point3d(0.55,0.015000000000000124,0),
                                        new Point3d(0.43500000000000005,0.004999999999999893,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3,k1=3.8;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit,k*unit,k1*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);
	}
}
class RotateSurface3D extends Shape3D
{
	public double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	public void rotateX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	public void rotateY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	public void rotateZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	public void rotate(Vector3d vector,Vector3d axis,double angle)
	{
		if(angle==0)return;
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rot_Z=angleToY(a.x,a.y);
		rotateZ(a,rot_Z);
		double rot_X=-angleToY(a.z,a.y);
		rotateZ(v,rot_Z);
		rotateX(v,rot_X);
		rotateY(v,angle);
		rotateX(v,-rot_X);
		rotateZ(v,-rot_Z);
		vector.x=v.x;
		vector.y=v.y;
		vector.z=v.z;
	}
	public RotateSurface3D(Point3d[] curvePoints,Vector3d axis,double angle0,double angle1,Appearance appearance)
	{
		int n=curvePoints.length,m=50,v=0;
		double da=(angle1-angle0)/(m-1);
		Vector3d[] P=new Vector3d[n];
		for(int j=0;j<n;j++)
		{
			P[j]=new Vector3d(curvePoints[j].x,curvePoints[j].y,curvePoints[j].z);
			rotate(P[j],axis,angle0);
		}
		Point3d[] coordinates=new Point3d[m*n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinates[i*n+j]=new Point3d(P[j].x,P[j].y,P[j].z);
				rotate(P[j],axis,da);
			}
		}
		int[] coordinateIndices=new int[(m-1)*n*2];v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());	
	}
}
