import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndRook
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
		TransformGroup1.addChild(new Rook());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
}
class Rook extends TransformGroup
{
	public Rook()
	{
		float unit=0.05f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,1.4149999999999998,0),
                                        new Point3d(0.395,1.4149999999999998,0),
                                        new Point3d(0.40000000000000013,1.4049999999999998,0),
                                        new Point3d(0.405,1.4,0),
                                        new Point3d(0.40000000000000013,1.295,0),
                                        new Point3d(0.3900000000000001,1.285,0),
                                        new Point3d(0.40000000000000013,1.2699999999999998,0),
                                        new Point3d(0.3350000000000002,1.18,0),
                                        new Point3d(0.3250000000000002,1.16,0),
                                        new Point3d(0.30000000000000004,1.1449999999999998,0),
                                        new Point3d(0.30500000000000016,1.0899999999999999,0),
                                        new Point3d(0.30000000000000004,1.0849999999999997,0),
                                        new Point3d(0.29000000000000004,0.8749999999999998,0),
                                        new Point3d(0.29500000000000015,0.8699999999999999,0),
                                        new Point3d(0.30000000000000004,0.7949999999999999,0),
                                        new Point3d(0.30500000000000016,0.7899999999999998,0),
                                        new Point3d(0.31000000000000005,0.7249999999999999,0),
                                        new Point3d(0.31500000000000017,0.7199999999999998,0),
                                        new Point3d(0.3250000000000002,0.6599999999999999,0),
                                        new Point3d(0.33000000000000007,0.6549999999999998,0),
                                        new Point3d(0.3350000000000002,0.6149999999999998,0),
                                        new Point3d(0.3450000000000002,0.5999999999999999,0),
                                        new Point3d(0.3600000000000001,0.5749999999999997,0),
                                        new Point3d(0.375,0.54,0),
                                        new Point3d(0.3800000000000001,0.5099999999999998,0),
                                        new Point3d(0.385,0.5049999999999999,0),
                                        new Point3d(0.405,0.4549999999999996,0),
                                        new Point3d(0.41000000000000014,0.4349999999999996,0),
                                        new Point3d(0.42500000000000004,0.41999999999999993,0),
                                        new Point3d(0.43500000000000005,0.4099999999999997,0),
                                        new Point3d(0.4700000000000002,0.3799999999999999,0),
                                        new Point3d(0.4750000000000001,0.33999999999999986,0),
                                        new Point3d(0.4800000000000002,0.33499999999999996,0),
                                        new Point3d(0.4800000000000002,0.3049999999999997,0),
                                        new Point3d(0.4700000000000002,0.2999999999999998,0),
                                        new Point3d(0.4800000000000002,0.2849999999999997,0),
                                        new Point3d(0.4850000000000001,0.21999999999999975,0),
                                        new Point3d(0.55,0.20499999999999963,0),
                                        new Point3d(0.5650000000000002,0.18999999999999995,0),
                                        new Point3d(0.5800000000000001,0.17999999999999972,0),
                                        new Point3d(0.5950000000000002,0.14500000000000002,0),
                                        new Point3d(0.6000000000000001,0.08999999999999986,0),
                                        new Point3d(0.5950000000000002,0.08499999999999996,0),
                                        new Point3d(0.6000000000000001,0.07999999999999963,0),
                                        new Point3d(0.5950000000000002,0.04499999999999993,0),
                                        new Point3d(0.5350000000000001,0.004999999999999893,0),
                                        new Point3d(0.54,0.0,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3,k1=3.8;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit,k*unit,k1*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,6*unit,0f));
		float PI=(float)Math.PI;
		for(int i=0;i<4;i++)
		{
			TransformGroup TransformGroup2=new TransformGroup(transform3D);
			TransformGroup2.addChild(new RingPlatform(1.55f*unit,unit,unit,i*PI/2,(i+1)*PI/2-PI/12,Appearance1));
			this.addChild(TransformGroup2);
		}
	}
}
class RingPlatform extends Shape3D
{
	public RingPlatform(float R,float r,float h,float w0,float w1,Appearance appearance)
	{
		int m=100;
		int n=4;
		int index=0;
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m*n,1,4*(m-1)*n+4+4);
		Transform3D transform3D=new Transform3D();
		transform3D.rotY(w0);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(r,0f,0f),
			new Point3f(R,0f,0f),
			new Point3f(R,h,0f),
			new Point3f(r,h,0f)
		};
		for(int j=0;j<n;j++)transform3D.transform(coordinates[j]);
		transform3D.rotY((w1-w0)/m);
		for(int i=0;i<m;i++)
		{
			IndexedQuadArray1.setCoordinates(i*n,coordinates);
			for(int j=0;j<n;j++)transform3D.transform(coordinates[j]);
		}
		int[] coordinateIndices=new int[]{0,1,2,3};
		IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
		index+=4;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices=new int[]
				{
					((i+0)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+1)%n),
					((i+0)%m)*n+((j+1)%n)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		coordinateIndices=new int[]
		{
			(m-1)*n+3,
			(m-1)*n+2,
			(m-1)*n+1,
			(m-1)*n+0
		};
		IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());
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
