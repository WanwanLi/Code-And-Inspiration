import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTetrahedron
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
		Material1.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild((new Tetrahedron()).getTetrahedron(0.5f,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 
class Tetrahedron
{
	public Shape3D getTetrahedron(float r,Appearance appearance)
	{
		System.out.println(IndexedTriangleArray.COORDINATES|IndexedTriangleArray.NORMALS);
		IndexedTriangleArray IndexedTriangleArray1=new IndexedTriangleArray(4,3,12);
		IndexedTriangleArray1.setCoordinate(0,new Point3f(r,r,r));
		IndexedTriangleArray1.setCoordinate(1,new Point3f(r,-r,-r));
		IndexedTriangleArray1.setCoordinate(2,new Point3f(-r,r,-r));
		IndexedTriangleArray1.setCoordinate(3,new Point3f(-r,-r,r));
		float f=(float)(1/Math.sqrt(3));
		IndexedTriangleArray1.setNormal(0,new Vector3f(f,f,-f));
		IndexedTriangleArray1.setNormal(1,new Vector3f(f,-f,f));
		IndexedTriangleArray1.setNormal(2,new Vector3f(-f,-f,-f));
		IndexedTriangleArray1.setNormal(3,new Vector3f(-f,f,f));
		int[] coordinateIndices=new int[]
		{
			0,1,2,
			0,3,1,
			1,3,2,
			2,3,0
		};
		int[] normalIndices=new int[]
		{
			0,0,0,
			1,1,1,
			2,2,2,
			3,3,3
		};
		IndexedTriangleArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedTriangleArray1.setNormalIndices(0,normalIndices);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(IndexedTriangleArray1);
		shape3D.setAppearance(appearance);
		return shape3D;
	}
}