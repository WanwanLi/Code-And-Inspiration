import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndPointArray
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),100);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,0f,0f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance1.setMaterial(Material1);
		int number=8;
		float[] Coordinates=
		{
			0.5f,0.5f,0.0f,
			-0.5f,0.5f,0.0f,
			0.7f,0.0f,0.0f,
			-0.7f,0.0f,0.0f,
			-0.5f,-0.5f,0.0f,
			0.5f,-0.5f,0.0f,
			0.0f,0.8f,0.0f,
			0.0f,-0.8f,0.0f
		};
		float[] Colors=
		{
			0f,0f,0f,
			0f,0f,1f,
			0f,1f,0f,
			0f,1f,1f,
			1f,0f,0f,
			1f,0f,1f,
			1f,1f,0f,
			1f,1f,1f
		};
		Shape3D shape3D=new Shape3D();
		PointArray PointArray1=new PointArray(number,PointArray.COORDINATES|PointArray.COLOR_3);
		PointArray1.setCoordinates(0,Coordinates);
		PointArray1.setColors(0,Colors);
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(10f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance1.setPointAttributes(PointAttributes1);
		shape3D.setGeometry(PointArray1);
		shape3D.setAppearance(Appearance1);
		TransformGroup1.addChild(shape3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}