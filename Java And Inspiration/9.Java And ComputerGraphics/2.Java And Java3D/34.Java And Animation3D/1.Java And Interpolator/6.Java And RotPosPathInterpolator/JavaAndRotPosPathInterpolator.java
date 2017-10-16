import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.interpolators.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndRotPosPathInterpolator
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
		MouseRotate MouseRotate1=new MouseRotate();
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup TransformGroup2=new TransformGroup();
		TransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);	
		Alpha Alpha1=new Alpha();
		Alpha1.setLoopCount(-1);
		Alpha1.setIncreasingAlphaDuration(4000);
		Alpha1.setDecreasingAlphaDuration(3000);
		Alpha1.setMode(Alpha.INCREASING_ENABLE|Alpha.DECREASING_ENABLE);
		Point3f[] positions=new Point3f[]
		{
			new Point3f(-0.25f,0f,0f),
			new Point3f(0f,0.25f,0f),
			new Point3f(0.25f,0f,0f)
		};
		Quat4f[] quaternions=new Quat4f[]
		{
			newQuat4f(0,0,1,1),
			newQuat4f(Math.PI,0,1,1),
			newQuat4f(Math.PI*2,0,1,1)
		};
		float[] knots=new float[]{0f,0.3f,1f};
		Transform3D transform3D=new Transform3D();
		RotPosPathInterpolator RotPosPathInterpolator1=new RotPosPathInterpolator(Alpha1,TransformGroup2,transform3D,knots,quaternions,positions);
		RotPosPathInterpolator1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(RotPosPathInterpolator1);
		TransformGroup2.addChild(new Prism(6,0.6f,0.4f,Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	private static Quat4f newQuat4f(double w,double x,double y,double z)
	{
		double a=w/2;
		double r=Math.sqrt(x*x+y*y+z*z);
		float Quat4f_w=(float)Math.cos(a);
		float Quat4f_z=-(float)(x*Math.sin(a)/r);
		float Quat4f_y=-(float)(y*Math.sin(a)/r);
		float Quat4f_x=(float)(z*Math.sin(a)/r);
		return  new Quat4f(Quat4f_w,Quat4f_x,Quat4f_y,Quat4f_z);
	}
}
class Prism extends TransformGroup
{
	public Prism(int m,float h,float r,Appearance appearance)
	{
		double w=2*Math.PI/m;
		double cosw=Math.cos(w);
		double sinw=Math.sin(w);
		double[] transformMatrix=new double[]
		{
			cosw,-sinw,0,0,
			sinw, cosw,0, 0,
			     0,       0,1, 0,
			     0,       0,0, 1
		};
		Transform3D polygonTransform3D=new Transform3D(transformMatrix);
		Point3d[] polygonPoint3d=new Point3d[m];
		polygonPoint3d[0]=new Point3d(r,0,0);
		for(int i=1;i<m;i++)
		{
			polygonPoint3d[i]=new Point3d();
			polygonTransform3D.transform(polygonPoint3d[i-1],polygonPoint3d[i]);
		}

		int[] polygonIndices=new int[3*(m-1)];
		int polygonIndex=0;
		for(int i=0;i<m-1;i++)
		{
			polygonIndices[polygonIndex++]=i+1;
			polygonIndices[polygonIndex++]=i;
			polygonIndices[polygonIndex++]=0;
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=3;
		GeometryInfo GeometryInfo2=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo2.setCoordinates(polygonPoint3d);
		GeometryInfo2.setCoordinateIndices(polygonIndices);
		GeometryInfo2.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator2=new NormalGenerator();
		NormalGenerator2.generateNormals(GeometryInfo2);
		Shape3D Shape3D2=new Shape3D();
		Shape3D2.setAppearance(appearance);
		Shape3D2.setGeometry(GeometryInfo2.getGeometryArray());
		this.addChild(Shape3D2);

		Transform3D translationTransform3D=new Transform3D();
		translationTransform3D.setTranslation(new Vector3f(0f,0f,h));
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(2*m,1,4*m*2);
		IndexedQuadArray1.setCoordinates(0,polygonPoint3d);
		for(int j=0;j<m;j++)translationTransform3D.transform(polygonPoint3d[j]);


		polygonIndices=new int[3*(m-1)];
		polygonIndex=0;
		for(int i=0;i<m-1;i++)
		{
			polygonIndices[polygonIndex++]=0;
			polygonIndices[polygonIndex++]=i;
			polygonIndices[polygonIndex++]=i+1;
		}
		stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=3;
		GeometryInfo GeometryInfo3=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo3.setCoordinates(polygonPoint3d);
		GeometryInfo3.setCoordinateIndices(polygonIndices);
		GeometryInfo3.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator3=new NormalGenerator();
		NormalGenerator3.generateNormals(GeometryInfo3);
		Shape3D Shape3D3=new Shape3D();
		Shape3D3.setAppearance(appearance);
		Shape3D3.setGeometry(GeometryInfo3.getGeometryArray());
		this.addChild(Shape3D3);

		IndexedQuadArray1.setCoordinates(m,polygonPoint3d);
		int index=0;
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<m;j++)
			{
				int[] coordinateIndices=new int[]
				{
					((i+0)%2)*m+((j+0)%m),
					((i+1)%2)*m+((j+0)%m),
					((i+1)%2)*m+((j+1)%m),
					((i+0)%2)*m+((j+1)%m)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		Shape3D Shape3D1=new Shape3D();
		Shape3D1.setAppearance(appearance);
		Shape3D1.setGeometry(GeometryInfo1.getGeometryArray());
		this.addChild(Shape3D1);
	}
}