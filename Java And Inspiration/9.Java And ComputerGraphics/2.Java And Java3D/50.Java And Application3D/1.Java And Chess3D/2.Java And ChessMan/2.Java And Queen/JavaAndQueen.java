import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndQueen
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
		TransformGroup1.addChild(new Queen());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
}
class Queen extends TransformGroup
{
	public Queen()
	{
		float unit=0.05f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,2.44,0),
                                        new Point3d(0.025000000000000133,2.435,0),
                                        new Point3d(0.030000000000000027,2.43,0),
                                        new Point3d(0.050000000000000044,2.42,0),
                                        new Point3d(0.07499999999999996,2.395,0),
                                        new Point3d(0.08000000000000007,2.345,0),
                                        new Point3d(0.07499999999999996,2.34,0),
                                        new Point3d(0.07499999999999996,2.295,0),
                                        new Point3d(0.11499999999999999,2.275,0),
                                        new Point3d(0.14500000000000002,2.255,0),
                                        new Point3d(0.19999999999999996,2.23,0),
                                        new Point3d(0.28500000000000014,2.22,0),
                                        new Point3d(0.29000000000000004,2.215,0),
                                        new Point3d(0.29499999999999993,2.2,0),
                                        new Point3d(0.29000000000000004,2.195,0),
                                        new Point3d(0.19500000000000006,1.9500000000000002,0),
                                        new Point3d(0.18999999999999995,1.875,0),
                                        new Point3d(0.19500000000000006,1.87,0),
                                        new Point3d(0.20500000000000007,1.8050000000000002,0),
                                        new Point3d(0.16999999999999993,1.77,0),
                                        new Point3d(0.15500000000000003,1.745,0),
                                        new Point3d(0.16000000000000014,1.74,0),
                                        new Point3d(0.16999999999999993,1.705,0),
                                        new Point3d(0.2450000000000001,1.65,0),
                                        new Point3d(0.2550000000000001,1.625,0),
                                        new Point3d(0.21999999999999997,1.58,0),
                                        new Point3d(0.2250000000000001,1.575,0),
                                        new Point3d(0.28,1.555,0),
                                        new Point3d(0.29499999999999993,1.515,0),
                                        new Point3d(0.29000000000000004,1.51,0),
                                        new Point3d(0.28500000000000014,1.495,0),
                                        new Point3d(0.15500000000000003,1.47,0),
                                        new Point3d(0.15500000000000003,1.34,0),
                                        new Point3d(0.16000000000000014,1.335,0),
                                        new Point3d(0.16500000000000004,1.155,0),
                                        new Point3d(0.16999999999999993,1.15,0),
                                        new Point3d(0.18500000000000005,0.8500000000000001,0),
                                        new Point3d(0.18999999999999995,0.845,0),
                                        new Point3d(0.20500000000000007,0.78,0),
                                        new Point3d(0.20999999999999996,0.7749999999999999,0),
                                        new Point3d(0.26,0.675,0),
                                        new Point3d(0.2650000000000001,0.6699999999999999,0),
                                        new Point3d(0.3500000000000001,0.52,0),
                                        new Point3d(0.365,0.5049999999999999,0),
                                        new Point3d(0.385,0.45500000000000007,0),
                                        new Point3d(0.3900000000000001,0.3900000000000001,0),
                                        new Point3d(0.31499999999999995,0.3250000000000002,0),
                                        new Point3d(0.30499999999999994,0.29000000000000004,0),
                                        new Point3d(0.3600000000000001,0.2450000000000001,0),
                                        new Point3d(0.53,0.20500000000000007,0),
                                        new Point3d(0.5450000000000002,0.18999999999999995,0),
                                        new Point3d(0.56,0.18000000000000016,0),
                                        new Point3d(0.575,0.14500000000000002,0),
                                        new Point3d(0.5800000000000001,0.08999999999999986,0),
                                        new Point3d(0.575,0.08499999999999996,0),
                                        new Point3d(0.5800000000000001,0.08000000000000007,0),
                                        new Point3d(0.575,0.04499999999999993,0),
                                        new Point3d(0.5150000000000001,0.004999999999999893,0),
                                        new Point3d(0.52,0.0,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k*unit,k*unit,k*unit));
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
