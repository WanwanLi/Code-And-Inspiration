import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndDodecahedron
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
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild((new Dodecahedron()).getDodecahedron(0.25f,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 
class Dodecahedron
{
	public Shape3D getDodecahedron(float r,Appearance appearance)
	{
		System.out.println(GeometryInfo.POLYGON_ARRAY);
		GeometryInfo GeometryInfo1=new GeometryInfo(5);
		float f=0.5f*(float)(Math.sqrt(5)+1);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(r,r,r),
			new Point3f(0f,r/f,r*f),
			new Point3f(r*f,0f,r/f),
			new Point3f(r/f,r*f,0f),

			new Point3f(-r,r,r),
			new Point3f(0f,-r/f,r*f),
			new Point3f(r,-r,r),
			new Point3f(r*f,0f,-r/f),

			new Point3f(r,r,-r),
			new Point3f(-r/f,r*f,0f),
			new Point3f(-r*f,0f,r/f),
			new Point3f(-r,-r,r),

			new Point3f(r/f,-r*f,0f),
			new Point3f(r,-r,-r),
			new Point3f(0f,r/f,-r*f),
			new Point3f(-r,r,-r),

			new Point3f(-r/f,-r*f,0f),
			new Point3f(-r*f,0f,-r/f),
			new Point3f(0f,-r/f,-r*f),
			new Point3f(-r,-r,-r)
		};
		int[] coordinateIndices=new int[]
		{
			0,1,5,6,2,
			0,2,7,8,3,
			0,3,9,4,1,
			1,4,10,11,5,

			2,6,12,13,7,
			3,8,14,15,9,
			5,11,16,12,6,
			7,13,18,14,8,

			9,15,17,10,4,
			19,16,11,10,17,
			19,17,15,14,18,
			19,18,13,12,16
		};
		int[] stripCounts=new int[12];
		for(int i=0;i<12;i++)stripCounts[i]=5;
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





