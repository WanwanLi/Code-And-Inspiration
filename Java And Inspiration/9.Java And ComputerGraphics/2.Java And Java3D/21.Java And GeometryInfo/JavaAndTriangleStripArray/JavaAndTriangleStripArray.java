import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTriangleStripArray
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(1f,1f,-1f);
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
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0f,0f));
		Appearance1.setMaterial(Material1);
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(0.01,0.01,0.01));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild((new TriangleStripArray()).getTriangleStripArray(Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 
class TriangleStripArray
{
	public Shape3D getTriangleStripArray(Appearance appearance)
	{
		float[] coordinates=
		{
			0f,0f,0f,
			60f,-120f,30f,
			120f,60f,-30f,
			180f,-60f,-15f,
			240f,60f,-40f,
			240f,60f,20f,
			300f,-60f,-35f,
			360f,60f,-60f,
			420f,0f,0f
		};
		int[] coordinateIndices=
		{
			0,1,2,3,4,
			5,6,7,8
		};
		int[] stripCounts={5,4};
		GeometryInfo GeometryInfo1=new GeometryInfo(4);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(GeometryInfo1.getGeometryArray());
		shape3D.setAppearance(appearance);
		return shape3D;
	}
}