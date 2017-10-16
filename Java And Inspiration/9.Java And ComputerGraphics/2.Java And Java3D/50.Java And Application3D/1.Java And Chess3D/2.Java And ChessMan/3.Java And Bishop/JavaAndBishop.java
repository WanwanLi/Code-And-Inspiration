import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndBishop
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
		TransformGroup1.addChild(new Bishop());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
}
class Bishop extends TransformGroup
{
	public Bishop()
	{
		float unit=0.05f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,2.17,0),
                                        new Point3d(0.08000000000000007,2.17,0),
                                        new Point3d(0.08500000000000019,2.16,0),
                                        new Point3d(0.10000000000000009,2.15,0),
                                        new Point3d(0.11500000000000021,2.105,0),
                                        new Point3d(0.1100000000000001,2.075,0),
                                        new Point3d(0.1050000000000002,2.0700000000000003,0),
                                        new Point3d(0.0950000000000002,2.05,0),
                                        new Point3d(0.07500000000000018,2.025,0),
                                        new Point3d(0.10000000000000009,2.005,0),
                                        new Point3d(0.1100000000000001,2.0,0),
                                        new Point3d(0.11500000000000021,1.99,0),
                                        new Point3d(0.16000000000000014,1.955,0),
                                        new Point3d(0.18500000000000005,1.9300000000000002,0),
                                        new Point3d(0.2300000000000002,1.8650000000000002,0),
                                        new Point3d(0.26000000000000023,1.79,0),
                                        new Point3d(0.27,1.725,0),
                                        new Point3d(0.2650000000000001,1.7200000000000002,0),
                                        new Point3d(0.2550000000000001,1.63,0),
                                        new Point3d(0.2450000000000001,1.6150000000000002,0),
                                        new Point3d(0.2200000000000002,1.565,0),
                                        new Point3d(0.21500000000000008,1.56,0),
                                        new Point3d(0.20500000000000007,1.54,0),
                                        new Point3d(0.20000000000000018,1.5350000000000001,0),
                                        new Point3d(0.18500000000000005,1.51,0),
                                        new Point3d(0.19500000000000006,1.48,0),
                                        new Point3d(0.19000000000000017,1.475,0),
                                        new Point3d(0.16000000000000014,1.425,0),
                                        new Point3d(0.17000000000000015,1.4000000000000001,0),
                                        new Point3d(0.2100000000000002,1.395,0),
                                        new Point3d(0.2250000000000001,1.355,0),
                                        new Point3d(0.30000000000000004,1.335,0),
                                        new Point3d(0.31500000000000017,1.3,0),
                                        new Point3d(0.29500000000000015,1.26,0),
                                        new Point3d(0.28,1.25,0),
                                        new Point3d(0.2550000000000001,1.245,0),
                                        new Point3d(0.2500000000000002,1.24,0),
                                        new Point3d(0.19000000000000017,1.23,0),
                                        new Point3d(0.18500000000000005,1.225,0),
                                        new Point3d(0.15500000000000003,1.22,0),
                                        new Point3d(0.15500000000000003,1.19,0),
                                        new Point3d(0.16000000000000014,1.185,0),
                                        new Point3d(0.15500000000000003,1.18,0),
                                        new Point3d(0.16500000000000004,1.065,0),
                                        new Point3d(0.17000000000000015,1.06,0),
                                        new Point3d(0.17000000000000015,0.99,0),
                                        new Point3d(0.17500000000000004,0.9850000000000001,0),
                                        new Point3d(0.19500000000000006,0.8900000000000001,0),
                                        new Point3d(0.20000000000000018,0.885,0),
                                        new Point3d(0.2100000000000002,0.845,0),
                                        new Point3d(0.21500000000000008,0.8400000000000001,0),
                                        new Point3d(0.2200000000000002,0.815,0),
                                        new Point3d(0.2250000000000001,0.81,0),
                                        new Point3d(0.2450000000000001,0.7450000000000001,0),
                                        new Point3d(0.2500000000000002,0.74,0),
                                        new Point3d(0.26000000000000023,0.7250000000000001,0),
                                        new Point3d(0.29000000000000004,0.645,0),
                                        new Point3d(0.3350000000000002,0.5700000000000001,0),
                                        new Point3d(0.3800000000000001,0.51,0),
                                        new Point3d(0.3900000000000001,0.5050000000000001,0),
                                        new Point3d(0.42500000000000004,0.4500000000000002,0),
                                        new Point3d(0.42000000000000015,0.4249999999999998,0),
                                        new Point3d(0.41500000000000004,0.41999999999999993,0),
                                        new Point3d(0.40000000000000013,0.38500000000000023,0),
                                        new Point3d(0.405,0.3799999999999999,0),
                                        new Point3d(0.38500000000000023,0.375,0),
                                        new Point3d(0.3700000000000001,0.3599999999999999,0),
                                        new Point3d(0.33000000000000007,0.3250000000000002,0),
                                        new Point3d(0.32000000000000006,0.29000000000000004,0),
                                        new Point3d(0.3750000000000002,0.2450000000000001,0),
                                        new Point3d(0.5450000000000002,0.20500000000000007,0),
                                        new Point3d(0.56,0.18999999999999995,0),
                                        new Point3d(0.5750000000000002,0.18000000000000016,0),
                                        new Point3d(0.5900000000000001,0.14500000000000002,0),
                                        new Point3d(0.5950000000000002,0.08999999999999986,0),
                                        new Point3d(0.5900000000000001,0.08499999999999996,0),
                                        new Point3d(0.5950000000000002,0.08000000000000007,0),
                                        new Point3d(0.5900000000000001,0.04499999999999993,0),
                                        new Point3d(0.53,0.004999999999999893,0),
                                        new Point3d(0.5350000000000001,0.0,0),
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
