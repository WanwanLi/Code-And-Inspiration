import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndIndexedTriangleArray
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),100);
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
		Transform3D transform3D = new Transform3D ();
		transform3D.setScale(0.5);
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		BranchGroup1.addChild(MouseTranslate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance1.setMaterial(Material1);
		float[] coordinates=
		{
			0f,0f,0f,
			1f,0f,0f,
			0f,1f,0f,
			0f,0f,1f,
			-1f,0f,0f,
			0f,-1f,0f,
			0f,0f,-1f
		};
		int[] coordinateIndices=
		{
			2,3,1, 2,1,6, 2,6,4, 2,4,3
		};
		float[] colors=
		{
			0f,0f,0f,
			1f,0f,0f,
			0f,1f,0f,
			0f,0f,1f,
			1f,1f,0f,
			1f,0f,1f,
			0f,1f,1f
		};
		Shape3D shape3D=new Shape3D();
		System.out.println(IndexedTriangleArray.COORDINATES);//|IndexedTriangleArray.COLOR_3);
		IndexedTriangleArray IndexedTriangleArray1=new IndexedTriangleArray(coordinates.length,1,coordinateIndices.length);
		IndexedTriangleArray1.setCoordinates(0,coordinates);
		IndexedTriangleArray1.setCoordinateIndices(0,coordinateIndices);
		//IndexedTriangleArray1.setColors(0,colors);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedTriangleArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		shape3D.setGeometry(GeometryInfo1.getGeometryArray());
		shape3D.setAppearance(Appearance1);
		TransformGroup2.addChild(shape3D);
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
